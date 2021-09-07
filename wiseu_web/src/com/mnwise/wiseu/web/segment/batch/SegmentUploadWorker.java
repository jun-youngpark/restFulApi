package com.mnwise.wiseu.web.segment.batch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.service.SegmentService;
import com.mnwise.wiseu.web.segment.service.UploadSegService;

/**
 * 세그먼트 업로드용 배치 스레드
 */
public class SegmentUploadWorker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SegmentUploadWorker.class);

    private SegmentService segmentService;
    private UploadSegService uploadSegService;
    private SegmentVo segmentVo;
    private String uploadDir;

    public void setSegmentService(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    public void setSegmentVo(SegmentVo segmentVo) {
        this.segmentVo = segmentVo;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void run() {
        log.debug("SegmentUploadWorker(" + segmentVo.getSegmentNo() + ") start..");

        try {
            int segmentNo = segmentVo.getSegmentNo();
            char RealDelimeter = segmentVo.getDelimiter().equals("comma") ? ',' : segmentVo.getDelimiter().equals("tab") ? '\t' : segmentVo.getDelimiter().charAt(0);

            String fileName = uploadDir + "/" + segmentNo + "/" + segmentNo + ".csv";
            File file = new File(fileName);

            if(log.isDebugEnabled()) {
                log.debug("--------------------------------------");
                log.debug("file path : " + file.getAbsolutePath() + ", " + file.exists());
                log.debug("--------------------------------------");
            }

            // 에러 정보를 넣어 놓을 파일을 생성한다.
            String writeErrFilePath = uploadDir + "/" + segmentNo + "/" + segmentNo + "_err.csv";
            (new File(writeErrFilePath)).delete();

            String writeSuccFilePath = uploadDir + "/" + segmentNo + "/" + segmentNo + "_succ.csv";
            (new File(writeSuccFilePath)).delete();

            String[] csvHeaders = null;
            CsvWriter csvErrWriter = null;
            CsvWriter csvSuccWriter = null;
            CsvReader csvReader = null;
            InputStream is = null;

            is = new BufferedInputStream(new FileInputStream(fileName));
            csvReader = new CsvReader(is, RealDelimeter, Charset.forName("euc-kr"));
            csvHeaders = segmentService.selectSegmentField(segmentNo);

            // 헤더가 있는지 여부를 체크해서 있으면 헤더 정보 건너 뛰도록
            if(segmentVo.getInMetaData().equals("on"))
                csvReader.readHeaders();

            csvErrWriter = new CsvWriter(writeErrFilePath, RealDelimeter, Charset.forName("euc-kr"));
            csvSuccWriter = new CsvWriter(writeSuccFilePath, RealDelimeter, Charset.forName("euc-kr"));

            EmailValidator emailValidator = EmailValidator.getInstance();

            int maxTargetNo = segmentVo.getTargetNo();

            csvReader.setTrimWhitespace(false);

            while(csvReader.readRecord()) {
                // 헤더가 있다면 헤더의 length와 현재 데이터의 length를 비교, 처리하자.
                // 특히 고객 ID가 널이거나 이메일에 @가 없다거나 전화번호에 문자가 들어가 있다거나 이런것들을 체크한다.
                boolean isErr = false;
                int cc = csvReader.getColumnCount();

                // 헤더의 필드 수와 현재 레코드의 필드수가 다르다면 에러로 처리한다.
                if(csvHeaders.length == cc) {
                    String[] csvData = new String[cc];
                    // int slotNum = 1;
                    Map<String, String> fileUploadMap = new HashMap<String, String>();
                    fileUploadMap.put("TARGET_NO", String.valueOf(maxTargetNo));

                    String csvColumnData = null;
                    for(int i = 0; i < cc; i++) {
                        csvColumnData = csvReader.get(i);
                        // 아이디가 없는 경우 처리를 어떻게 할지 확인
                        if("CUSTOMER_ID".equals(csvHeaders[i]) && "".equals(csvColumnData)) {
                            isErr = true;
                        }
                        if("CUSTOMER_EMAIL".equals(csvHeaders[i]) && !emailValidator.isValid(csvColumnData)) {
                            isErr = true;
                        }

                        if("CUSTOMER_TEL".equals(csvHeaders[i])) {
                            csvColumnData = StringUtil.deleteWhitespace(csvColumnData.replaceAll("[-)(]", ""));
                            if(!StringUtil.isNumeric(csvColumnData)) {
                                isErr = true;
                            }
                        }

                        if("CUSTOMER_FAX".equals(csvHeaders[i])) {
                            csvColumnData = StringUtil.deleteWhitespace(csvColumnData.replaceAll("[-)(]", ""));
                            if(!StringUtil.isNumeric(csvColumnData)) {
                                isErr = true;
                            }
                        }

                        csvData[i] = csvColumnData;
                        // log.debug(cvsHeaders[i]+"="+firstData[i] + ":");
                        // Map에 담아 iBATIS로 넘긴다.
                        if(!isErr)
                            fileUploadMap.put(csvHeaders[i], csvData[i]);
                    }

                    // int rValue = csvService.bulkDataImport(fileUploadMap);
                    // 에러 발생시 파일에 쓰도록 처리
                    if(isErr) {
                        csvErrWriter.writeRecord(csvReader.getValues());
                    } else {
                        csvSuccWriter.writeRecord(csvReader.getValues());
                    }
                } else {
                    csvErrWriter.writeRecord(csvReader.getValues());
                }
                if(!isErr) {
                }
            }

            int importedDataCnt = uploadSegService.importedDataCnt(maxTargetNo);
            segmentVo.setSegmentSize(importedDataCnt);
            segmentVo.setActiveYn("Y");

            csvReader.close();
            csvErrWriter.close();
            csvSuccWriter.close();

            Thread.sleep(100000);
            log.debug("SegmentUploadWorker(" + segmentVo.getSegmentNo() + ") end..");

            segmentVo.setSegmentSts(Const.SEGMENT_STS_UPLOAD_END);
            segmentService.updateSegmentForSegmentSts(segmentVo);
        } catch(Exception e) {
            log.error(e.getMessage());
        } finally {
            // 스레드가 모든 작업을 마치고 끝내기전에 상태코드가 완료가 아니라면 업로드 실패로 간주한다.
            if(!segmentVo.getSegmentSts().equals(Const.SEGMENT_STS_UPLOAD_END)) {
                segmentVo.setSegmentSts(Const.SEGMENT_STS_UPLOAD_FAIL);
                try {
                    segmentService.updateSegmentForSegmentSts(segmentVo);
                } catch(Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
