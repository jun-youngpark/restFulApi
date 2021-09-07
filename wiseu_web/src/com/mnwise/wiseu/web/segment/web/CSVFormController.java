package com.mnwise.wiseu.web.segment.web;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.ui.upload.CSVInsertedCntInfo;
import com.mnwise.wiseu.web.editor.util.SmartEncodingInputStream;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.UploadSegVo;
import com.mnwise.wiseu.web.segment.service.UploadSegService;

/**
 * CSV 대상자 파일 올리기 2단계 Controller
 */
@Controller
public class CSVFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CSVFormController.class);

    @Autowired private UploadSegService uploadSegService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;

    /**
     * 대상자 파일과 시멘틱 매핑 화면을 출력한다.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute("uploadSegVo")
    public UploadSegVo formBackingObject(String importedFilePath, @RequestParam(defaultValue="") String inMetaData, @RequestParam(defaultValue="") String delimiter,
                                         @RequestParam(defaultValue="") String vmail, @RequestParam(defaultValue="") String vphone, @RequestParam(defaultValue="") String reject, HttpServletRequest request) throws Exception {
        try {
            importedFilePath = URLDecoder.decode(importedFilePath, "UTF-8");
        } catch(Exception e) {
            log.warn(null, e);
        }

        UserVo userVo = getLoginUserVo();
        UploadSegVo uploadSegVo = new UploadSegVo(userVo.getLanguage());  // UploadSegVo 생성 시 언어 설정이 필요

        SmartEncodingInputStream in = null;
        CsvReader csvReader = null;

        try {
            //경로조작 취약점 조치
            if(!importedFilePath.startsWith(super.importUploadDir)) {
                uploadSegVo.setTotalLine(-1);
                uploadSegVo.setErrorMsg(messageSourceAccessor.getMessage("common.path.traversal.err", new Locale("ko")));
                return uploadSegVo;
            }
            uploadSegVo.setImportedFilePath(importedFilePath);
            uploadSegVo.setInMetaData(inMetaData);
            uploadSegVo.setDelimiter(delimiter);
            uploadSegVo.setVmail(vmail);
            uploadSegVo.setVphone(vphone);

            in = new SmartEncodingInputStream(new FileInputStream(StringUtil.escapeFilePath(importedFilePath)), SmartEncodingInputStream.BUFFER_LENGTH_4KB, Charset.forName("EUC-KR"), true);
            String charset = in.getEncoding().displayName();

            char RealDelimeter = delimiter.equals("comma") ? ',' : delimiter.equals("tab") ? '\t' : delimiter.charAt(0);
            csvReader = new CsvReader(in, RealDelimeter, Charset.forName(charset));

            // 첫라인 헤더 정보가 없으면 헤더 정보를 읽지 않는다.
            if(inMetaData.equals("on")) {
                csvReader.readHeaders();
                String[] headers = csvReader.getHeaders();
                if("UTF-8".equals(charset)) {
                    if(headers[0].codePointAt(0) == 65279) {
                        headers[0] = headers[0].substring(1);
                    }
                }
                uploadSegVo.setHeaders(headers);
            }
            int totalLine = 0;

            // TODO 헤더 칼럼 수와 (첫번째)데이터 칼럼 수가 일치 하지 않을 경우 에러를 알려준다.
            while(csvReader.readRecord()) {
                // 빈줄을 제거한 총 라인수를 알기 위함.
                if(totalLine == 0) {
                    int cc = csvReader.getColumnCount();

                    if(reject.equals(Const.UPLOAD_REJECT_NO)) {
                        if(cc < 3) {
                            break;
                        }
                    }
                    String[] firstData = new String[cc];
                    for(int i = 0; i < cc; i++) {
                        firstData[i] = csvReader.get(i);
                    }
                    uploadSegVo.setFirstData(firstData);
                }
                totalLine++;
            }
            csvReader.close();

            uploadSegVo.setTotalLine(totalLine);
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            if(csvReader != null)
                csvReader.close();

            IOUtil.closeQuietly(in);
        }
        return uploadSegVo;
    }

    /**
     * [대상자 관리>대상자파일 올리기>2단계] 대상자 기본정보 및 컬럼지정 화면 출력 (CSV)
     * [공통팝업>대상자관리>2단계] 대상자 기본정보 및 컬럼지정 (팝업) 화면 출력 (CSV)
     *
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/target/csvimport.do, /segment/upload/csvimport.do
    @RequestMapping(value={"/target/fileSegment2Step_csv.do", "/segment/fileSegment2Step_csv.do"}, method=RequestMethod.GET)
    public ModelAndView referenceData(@RequestParam(defaultValue="") String reject, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("segment/fileSegment2Step");  // segment/upload/csvimport
            if(request.getRequestURI().indexOf("target") > -1) {
                mav.addObject("action", "/target/fileSegment2Step_csv.do");  // /segment/target/csvimport.do
            } else {
                mav.addObject("action", "/segment/fileSegment2Step_csv.do");  // /segment/upload/csvimport.do
            }
            mav.addObject("reject", reject);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자 관리>대상자파일 올리기>2단계] 대상자 기본정보 및 컬럼지정 - 다음단계 버튼 클릭 (CSV)
     * [공통팝업>대상자관리>2단계] 대상자 기본정보 및 컬럼지정 (팝업) - 다음단계 버튼 클릭 (CSV)
     *
     * @param uploadSegVo
     * @param reject
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/target/csvimport.do, /segment/upload/csvimport.do
    @RequestMapping(value={"/target/fileSegment2Step_csv.do", "/segment/fileSegment2Step_csv.do"}, method=RequestMethod.POST)
    public ModelAndView onSubmit(UploadSegVo uploadSegVo, @RequestParam(defaultValue="") String reject, HttpServletRequest request) throws Exception {
        try {
            Map<String, String> returnData = new HashMap<>();
            String importedFilePath = uploadSegVo.getImportedFilePath();

            // 대량 업로드 선택시 비동기로 수행한다.
            if(uploadSegVo.isLargeUpload()) {
                asyncUpload(request, reject, returnData, uploadSegVo, importedFilePath);
            } else {
                syncUpload(request, reject, returnData, uploadSegVo, importedFilePath);
            }

            FileUtil.forceDelete(importedFilePath);  // 파일 삭제

            return new ModelAndView(new RedirectView(returnData.get("submitPage")), returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 비동기 업로드 수행 대량의 데이타 인서트 수행시에는 백그라운드에서 스레드로 처리
     * 파일명을 세그먼트 번호로 바꾸어서 백그라운드에서 수행시 파일을 찾아서 처리
     * TODO 업로드에 대한 전체적인 로직을 바꿀 필요 있음. 현재는 시간여건상 기존 로직에 추가해서 작업
     *
     * @param request
     * @param reject
     * @param returnData
     * @param uploadSegVo
     * @param importedFilePath
     * @throws Exception
     */
    private void asyncUpload(HttpServletRequest request, String reject, Map<String, String> returnData, UploadSegVo uploadSegVo, String importedFilePath) throws Exception {
        UserVo userVo = getLoginUserVo();

        SegmentVo segmentVo = uploadSegVo.getSegmentVo();
        segmentVo.setSegmentSize(0);  // 세그먼트 사이즈는 업로드 완료후 실제 카운트를 업데이트
        segmentVo.setUserId(userVo.getUserId());
        segmentVo.setGrpCd(userVo.getGrpCd());
        segmentVo.setSegmentType("N");
        segmentVo.setSegType("F");
        segmentVo.setActiveYn("N");
        segmentVo.setReject(reject);
        segmentVo.setSegmentSts(Const.SEGMENT_STS_UPLOAD_WAIT);
        segmentVo.setDelimiter(uploadSegVo.getDelimiter());
        segmentVo.setInMetaData(uploadSegVo.getInMetaData());
        segmentVo.setLargeUpload(uploadSegVo.isLargeUpload());
        segmentVo.setSqlBody(" WHERE TARGET_NO = " + uploadSegService.selectNextTargetNo());

        // NVFILEUPLOAD에 데이터를 넣기 전에 SEGMENT 테이블에 먼저 데이터를 넣어야 한다.
        int slotNum = 1;
        String[] cvsHeaders = uploadSegVo.getKeys();
        for(int i = 0; i < cvsHeaders.length; i++) {
            // 기타 정보 일 경우 SLOT1, SLOT2 필드와 매핑하도록 처리
            if("SLOT".equals(cvsHeaders[i])) {
                cvsHeaders[i] = cvsHeaders[i] + String.valueOf(slotNum);
                slotNum++;
            }
        }

        // NVSEGMENT INSERT
        int maxSegmentNo = uploadSegService.setRegistSegment(segmentVo, cvsHeaders, uploadSegVo.getKeysName());

        // 업로드된 파일
        File uploadFile = new File(StringUtil.escapeFilePath(importedFilePath));
        new File(uploadFile.getParent() + "/" + maxSegmentNo).mkdir();

        // 해당 세그먼트 디렉토리로 move
        File saveFile = new File(uploadFile.getParent() + "/" + maxSegmentNo + "/" + maxSegmentNo + importedFilePath.substring(importedFilePath.lastIndexOf(".")));
        uploadFile.renameTo(saveFile);

        returnData.put("segmentNo", String.valueOf(segmentVo.getSegmentNo()));
        returnData.put("segmentNm", segmentVo.getSegmentNm());
        returnData.put("segmentSize", "0");
        returnData.put("totalLine", "0");
        returnData.put("segmentSts", Const.SEGMENT_STS_UPLOAD_WAIT);

        if(request.getRequestURI().indexOf("target") > -1) {
            returnData.put("submitPage", "/target/fileSegment3Step.do");  // /segment/target/upload_result.do
        } else {
            returnData.put("submitPage", "/segment/fileSegment3Step.do");  // /segment/upload/upload_result.do
        }
    }

    /**
     * 소량의 데이타 업로드 수행시에는 바로 처리
     * TODO 현재 컨트롤러에서 처리하는 부분에 대해서는 트랜잭션 처리를 위해서 서비스로 로직을 옮길 필요 있음
     *
     * @param request
     * @param reject
     * @param returnData
     * @param uploadSegVo
     * @param importedFilePath
     * @throws Exception
     */
    private void syncUpload(HttpServletRequest request, String reject, Map<String, String> returnData, UploadSegVo uploadSegVo, String importedFilePath) throws Exception {
        int totalLine = uploadSegVo.getTotalLine();
        UserVo userVo = getLoginUserVo();

        SegmentVo segmentVo = uploadSegVo.getSegmentVo();
        segmentVo.setSegmentSize(totalLine);  // 일단 총 라인 수를 세그먼트 사이즈로 업데이트
        segmentVo.setUserId(userVo.getUserId());
        segmentVo.setGrpCd(userVo.getGrpCd());
        segmentVo.setSegmentType("N");
        segmentVo.setSegType("F");
        segmentVo.setActiveYn("N");
        segmentVo.setReject(reject);
        segmentVo.setSegmentSts(Const.SEGMENT_STS_UPLOAD_RUN);

        String splita = importedFilePath.substring(0, importedFilePath.lastIndexOf("."));
        String splitb = importedFilePath.substring(importedFilePath.lastIndexOf("."));
        String writeErrFilePath = splita + "_err" + splitb;
        FileUtil.forceDelete(writeErrFilePath);  // 에러 정보를 넣어 놓을 파일

        int noErrCnt = 0;
        int whileCnt = 0;

        CsvWriter csvErrWriter = null;
        CsvReader csvReader = null;
        SmartEncodingInputStream in = null;
        try {
            in = new SmartEncodingInputStream(new FileInputStream(StringUtil.escapeFilePath(importedFilePath)), SmartEncodingInputStream.BUFFER_LENGTH_4KB, Charset.forName("EUC-KR"), true);
            String charset = in.getEncoding().displayName();

            char RealDelimeter = uploadSegVo.getDelimiter().equals("comma") ? ',' : uploadSegVo.getDelimiter().equals("tab") ? '\t' : uploadSegVo.getDelimiter().charAt(0);
            csvReader = new CsvReader(in, RealDelimeter, Charset.forName(charset));

            // 헤더가 있는지 여부를 체크해서 있으면 헤더 정보 건너 뛰도록
            if(uploadSegVo.getInMetaData().equals("on"))
                csvReader.readHeaders();
            String[] cvsHeaders = uploadSegVo.getKeys();

            // NVFILEUPLOAD에 데이터를 넣기 전에 SEGMENT 테이블에 먼저 데이터를 넣어야 한다.
            int slotNum = 1;
            for(int i = 0; i < cvsHeaders.length; i++) {
                // 기타 정보 일 경우 SLOT1, SLOT2 필드와 매핑하도록 처리
                if("SLOT".equals(cvsHeaders[i])) {
                    cvsHeaders[i] = cvsHeaders[i] + String.valueOf(slotNum);
                    slotNum++;
                }
            }
            int maxSegmentNo = uploadSegService.setRegistSegment(segmentVo, cvsHeaders, uploadSegVo.getKeysName());

            log.debug("Csv Submit -- > 5 : " + new Date());

            // 에러 내용 저장 할 파일 만들기
            File directory = new File(StringUtil.escapeFilePath(importedFilePath));
            String parent = directory.getParent();

            int dirNum = maxSegmentNo / 100;
            File directory2 = new File(parent + "/" + dirNum);
            if(!directory2.isDirectory()) {
                FileUtil.forceMkdir(directory2);
            }
            writeErrFilePath = directory2.getAbsolutePath() + "/" + maxSegmentNo + "_err.csv";

            csvErrWriter = new CsvWriter(writeErrFilePath, RealDelimeter, Charset.forName(charset));

            segmentVo.setSegmentNo(maxSegmentNo);

            List<Map<String, Object>> paramMapList = new ArrayList<>();

            CSVInsertedCntInfo insertedCntInfo = new CSVInsertedCntInfo();
            insertedCntInfo.setTotalCnt(totalLine);

            EmailValidator emailValidator = EmailValidator.getInstance();

            // NVFILEUPLOAD 테이블에서 MAX값을 가져온다.
            int maxTargetNo = uploadSegService.selectNextTargetNo();

            // TODO 전문 때문에 trim하는 걸 뺌. 하지만 전문 데이터 때문에 trim 기능을 일괄적으로 빼야 하는 것에
            // 대해서는 확인 필요
            csvReader.setTrimWhitespace(false);

            while(csvReader.readRecord()) {
                // 헤더가 있다면 헤더의 length와 현재 데이터의 length를 비교, 처리하자.
                // 특히 고객 ID가 널이거나 이메일에 @가 없다거나 전화번호에 문자가 들어가 있다거나 이런것들을 체크한다.
                boolean isErr = false;
                int cc = csvReader.getColumnCount();

                // 헤더의 필드 수와 현재 레코드의 필드수가 다르다면 에러로 처리한다.
                if(cvsHeaders.length == cc) {
                    String[] csvData = new String[cc];
                    // int slotNum = 1;
                    Map<String, Object> fileUploadMap = new HashMap<>();
                    fileUploadMap.put("TARGET_NO", maxTargetNo);

                    String csvColumnData = null;
                    for(int i = 0; i < cc; i++) {
                        csvColumnData = csvReader.get(i);
                        // 20141113 김민성 - csv 파일내 데이터 공백 제거(암호화 처리 위해 추가)
                        csvColumnData = csvColumnData.trim();

                        if("CUSTOMER_ID".equals(cvsHeaders[i])) {
                            if("".equals(csvColumnData) || csvColumnData.getBytes().length > 50)
                                isErr = true;
                        }
                        if("CUSTOMER_EMAIL".equals(cvsHeaders[i])) {
                            if(("Y".equals(uploadSegVo.getVmail()) && !emailValidator.isValid(csvColumnData)) || csvColumnData.getBytes().length > 100)
                                isErr = true;
                        }
                        if("CUSTOMER_NM".equals(cvsHeaders[i]) && csvColumnData.getBytes().length > 100) {
                            isErr = true;
                        }

                        if("Y".equals(uploadSegVo.getVphone()) && "CUSTOMER_TEL".equals(cvsHeaders[i])) {
                            csvColumnData = StringUtil.deleteWhitespace(csvColumnData.replaceAll("[-)(]", ""));
                            if(!StringUtil.isNumeric(csvColumnData)) {
                                isErr = true;
                            }
                        }

                        if("CUSTOMER_FAX".equals(cvsHeaders[i])) {
                            csvColumnData = StringUtil.deleteWhitespace(csvColumnData.replaceAll("[-)(]", ""));
                            if(!StringUtil.isNumeric(csvColumnData)) {
                                isErr = true;
                            }
                        }

                        csvData[i] = csvColumnData;
                        // log.debug(cvsHeaders[i]+"="+firstData[i] + ":");
                        // Map에 담아 iBATIS로 넘긴다.
                        if(!isErr)
                            fileUploadMap.put(cvsHeaders[i], csvData[i]);
                    }

                    // int rValue = csvService.bulkDataImport(fileUploadMap);
                    // 에러 발생시 파일에 쓰도록 처리
                    if(isErr) {
                        csvErrWriter.writeRecord(csvReader.getValues());
                    } else {
                        paramMapList.add(fileUploadMap);
                    }
                } else {
                    csvErrWriter.writeRecord(csvReader.getValues());
                }

                if(!isErr) {
                    noErrCnt++;
                }
                whileCnt++;

                /* 첫번째 데이터를 우선 넣어 놓는다(max값이 엉킬 수 있으므로). 그리고 이후 200건씩을 벌크로 DB에 넣는다. */
                int insertRowCnt = 1000;
                if(noErrCnt == 1 || noErrCnt % insertRowCnt == 0 || whileCnt == totalLine) {
                    try {
                        uploadSegService.bulkDataImport(paramMapList);
                        paramMapList = new ArrayList<>();
                        insertedCntInfo.setInProgress(true);
                        request.getSession().setAttribute("insertedCntInfo", insertedCntInfo);
                        insertedCntInfo.setInsertedCnt(noErrCnt);
                    }catch(Exception e) {
                        csvErrWriter.writeRecord(csvReader.getValues());
                        noErrCnt--;
                    }
                }
            }

            insertedCntInfo.setInProgress(false);
            insertedCntInfo.setInsertedCnt(noErrCnt);
            request.getSession().setAttribute("insertedCntInfo", insertedCntInfo);

            // 발송 결과를 업데이트
            segmentVo.setSegmentSize(noErrCnt);
            segmentVo.setSqlBody("  WHERE TARGET_NO = " + maxTargetNo);
            segmentVo.setActiveYn("Y");
            segmentVo.setSegmentSts(Const.SEGMENT_STS_UPLOAD_END);

            uploadSegService.updateSegmentInfoAfterImportFile(segmentVo);

            csvReader.close();
            csvErrWriter.close();
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            if(csvReader != null)
                csvReader.close();

            if(csvErrWriter != null)
                csvErrWriter.close();

            IOUtil.closeQuietly(in);
        }

        if(totalLine == noErrCnt) {  // DB에 등록 된 수와 CSV 파일의 총 라인수 비교
            FileUtil.forceDelete(writeErrFilePath);
            returnData.put("insertedErrFilePath", "");
        } else {
            returnData.put("insertedErrFilePath", writeErrFilePath.replaceAll("\\\\", "/"));
        }

        returnData.put("segmentNo", String.valueOf(segmentVo.getSegmentNo()));
        returnData.put("segmentNm", segmentVo.getSegmentNm());
        returnData.put("segmentSize", String.valueOf(segmentVo.getSegmentSize()));
        returnData.put("totalLine", String.valueOf(totalLine));
        returnData.put("reject", segmentVo.getReject());
        returnData.put("tabUse", uploadSegVo.getTabUse());

        if(request.getRequestURI().indexOf("target") > -1) {
            returnData.put("submitPage", "/target/fileSegment3Step.do");  // /segment/target/upload_result.do
        } else {
            returnData.put("submitPage", "/segment/fileSegment3Step.do");  // /segment/upload/upload_result.do
        }
    }
}
