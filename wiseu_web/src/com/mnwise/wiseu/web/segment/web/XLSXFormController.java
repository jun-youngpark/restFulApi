package com.mnwise.wiseu.web.segment.web;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.EmailValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.csvreader.CsvWriter;
import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.ui.upload.CSVInsertedCntInfo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.UploadSegVo;
import com.mnwise.wiseu.web.segment.service.UploadSegService;

/**
 * XLSX 대상자 파일 올리기 2단계 Controller
 */
@Controller
public class XLSXFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(XLSXFormController.class);

    @Autowired private UploadSegService uploadSegService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;

    @ModelAttribute("uploadSegVo")
    public UploadSegVo formBackingObject(String importedFilePath, @RequestParam(defaultValue="") String inMetaData, @RequestParam(defaultValue="") String delimiter,
                                         @RequestParam(defaultValue="") String vmail, @RequestParam(defaultValue="") String vphone, HttpServletRequest request) throws Exception {
        try {
            importedFilePath = URLDecoder.decode(importedFilePath, "utf-8");
        } catch(Exception e) {
            log.warn(null, e);
        }

        FileInputStream in = null;
        XSSFWorkbook workbook = null;

        UserVo userVo = getLoginUserVo();
        UploadSegVo uploadSegVo = new UploadSegVo(userVo.getLanguage());

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

            in = new FileInputStream(StringUtil.escapeFilePath(importedFilePath));
            workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int totalLine = sheet.getLastRowNum() + 1;  // getLastRowNum() 메소드는 실제 rowcount -1 이 리턴
            if(inMetaData.equals("on")) {
                totalLine--;
            }
            uploadSegVo.setTotalLine(totalLine);

            if(sheet != null) {
                // 헤더 정보가 있다면 2개의 row를 가져오고 없다면 1개의 row를 가져온다.
                int whileCnt = inMetaData.equals("on") ? 2 : 1;
                String[] headers = null;
                Iterator<Row> rows = sheet.rowIterator();
                for(int i = 0; rows.hasNext(); i++) {
                    if(i == whileCnt)
                        break;
                    XSSFRow row = (XSSFRow) rows.next();
                    int cc = row.getLastCellNum();
                    String[] firstData = new String[cc];
                    if(i < 1 && "on".equals(inMetaData)) {
                        headers = new String[cc];
                    }
                    Iterator<Cell> cells = row.cellIterator();
                    int j = 0;
                    // 헤더 정보 (헤더 정보가 없으면 가져오지 않는다.)
                    if(whileCnt == 2 && i == 0) {
                        while(cells.hasNext()) {
                            XSSFCell cell = (XSSFCell) cells.next();
                            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                // 대상자 파일이라 더블형 데이터는 없다고 생각하고 처리한다 int로 변환안하면 1 이 1.0 으로 나온다..
                                headers[j] = Integer.toString((int) cell.getNumericCellValue());
                            } else {
                                headers[j] = cell.getRichStringCellValue().getString();
                            }
                            j++;
                        }
                    }
                    // 첫번째 데이터
                    if(whileCnt == 1 || i == 1) {
                        while(cells.hasNext()) {
                            XSSFCell cell = (XSSFCell) cells.next();
                            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                // 대상자 파일이라 더블형 데이터는 없다고 생각하고 처리한다 int로 변환안하면 1 이 1.0 으로 나온다..
                                firstData[j] = Integer.toString((int) cell.getNumericCellValue());
                            } else {
                                firstData[j] = cell.getRichStringCellValue().getString();
                            }
                            j++;
                        }
                    }

                    if(inMetaData.equals("on")) {
                        uploadSegVo.setHeaders(headers);
                        uploadSegVo.setFirstData(firstData);
                    } else {
                        uploadSegVo.setFirstData(firstData);
                    }
                }
            }

            uploadSegVo.setTotalLine(totalLine);
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(workbook);
            IOUtil.closeQuietly(in);
        }

        return uploadSegVo;
    }

    /**
     * [대상자 관리>대상자파일 올리기>2단계] 대상자 기본정보 및 컬럼지정 화면 출력 (XLSX)
     * [공통팝업>대상자관리>2단계] 대상자 기본정보 및 컬럼지정 (팝업) 화면 출력 (XLSX)
     *
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/target/xlsximport.do, /segment/upload/xlsximport.do
    @RequestMapping(value={"/target/fileSegment2Step_xlsx.do", "/segment/fileSegment2Step_xlsx.do"}, method=RequestMethod.GET)
    public ModelAndView referenceData(HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("segment/fileSegment2Step");  // segment/upload/csvimport
            if(request.getRequestURI().indexOf("target") > -1) {
                mav.addObject("action", "/target/fileSegment2Step_xlsx.do");  // /segment/target/xlsximport.do
            } else {
                mav.addObject("action", "/segment/fileSegment2Step_xlsx.do");  // /segment/upload/xlsximport.do
            }
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자 관리>대상자파일 올리기>2단계] 대상자 기본정보 및 컬럼지정 - 다음단계 버튼 클릭 (XLSX)
     * [공통팝업>대상자관리>2단계] 대상자 기본정보 및 컬럼지정 (팝업) - 다음단계 버튼 클릭 (XLSX)
     *
     * @param uploadSegVo
     * @param reject
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/target/xlsximport.do, /segment/upload/xlsximport.do
    @RequestMapping(value={"/target/fileSegment2Step_xlsx.do", "/segment/fileSegment2Step_xlsx.do"}, method=RequestMethod.POST)
    protected ModelAndView onSubmit(UploadSegVo uploadSegVo, @RequestParam(defaultValue="") String reject, HttpServletRequest request) throws Exception {
        int totalLine = uploadSegVo.getTotalLine();
        UserVo userVo = getLoginUserVo();

        SegmentVo segmentVo = uploadSegVo.getSegmentVo();
        segmentVo.setSegmentSize(totalLine);  // 일단 총 라인 수를 세그먼트 사이즈로 업데이트
        segmentVo.setUserId(userVo.getUserId());
        segmentVo.setGrpCd(userVo.getGrpCd());
        segmentVo.setSegmentType("N");
        segmentVo.setSegType("F");
        segmentVo.setReject(reject);

        String importedFilePath = uploadSegVo.getImportedFilePath();
        String splita = importedFilePath.substring(0, importedFilePath.lastIndexOf("."));
        String splitb = importedFilePath.substring(importedFilePath.lastIndexOf("."));
        String writeErrFilePath = splita + "_err" + splitb;  // 에러 정보를 넣어 놓을 파일
        FileUtil.forceDelete(writeErrFilePath);

        char RealDelimeter = uploadSegVo.getDelimiter().equals("comma") ? ',' : uploadSegVo.getDelimiter().equals("tab") ? '\t' : uploadSegVo.getDelimiter().charAt(0);
        CsvWriter csvErrWriter = new CsvWriter(writeErrFilePath, RealDelimeter, Charset.forName("euc-kr"));

        int noErrCnt = 0;
        int whileCnt = 0;
        FileInputStream in = null;
        XSSFWorkbook workbook = null;

        try {
            in = new FileInputStream(StringUtil.escapeFilePath(importedFilePath));
            workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.rowIterator();

            // 헤더가 있는지 여부를 체크해서 있으면 헤더 정보 건너 뛰도록
            if(uploadSegVo.getInMetaData().equals("on"))
                rows.next();
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
            segmentVo.setSegmentNo(maxSegmentNo);

            List<Map<String, Object>> paramMapList = new ArrayList<>();

            CSVInsertedCntInfo insertedCntInfo = new CSVInsertedCntInfo();
            insertedCntInfo.setTotalCnt(totalLine);

            EmailValidator emailValidator = EmailValidator.getInstance();

            // NVFILEUPLOAD 테이블에서 MAX값을 가져온다.
            int maxTargetNo = uploadSegService.selectNextTargetNo();

            while(rows.hasNext()) {
                XSSFRow row = (XSSFRow) rows.next();

                // 헤더가 있다면 헤더의 length와 현재 데이터의 length를 비교, 처리하자.
                // 특히 고객 ID가 널이거나 이메일에 @가 없다거나 전화번호에 문자가 들어가 있다거나 이런것들을 체크한다.
                boolean isErr = false;
                int cc = row.getLastCellNum();
                // 헤더의 필드 수와 현재 레코드의 필드수가 다르다면 에러로 처리한다.
                if(cvsHeaders.length == cc) {
                    String[] csvData = new String[cc];

                    Map<String, Object> fileUploadMap = new HashMap<>();
                    fileUploadMap.put("TARGET_NO", maxTargetNo);

                    String csvColumnData = null;
                    for(int i = 0; i < cc; i++) {
                        XSSFCell cell = row.getCell(i);
                        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            // 대상자 파일이라 더블형 데이터는 없다고 생각하고 처리한다 int로 변환안하면 1 이 1.0 으로 나온다..
                            csvColumnData = Integer.toString((int) cell.getNumericCellValue());
                        } else {
                            csvColumnData = cell.getRichStringCellValue().getString();
                        }

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

                        if(!isErr)
                            fileUploadMap.put(cvsHeaders[i], csvData[i]);
                    }

                    if(isErr) {
                        for(int i = 0; i < cc; i++) {
                            if(row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                csvData[i] = Integer.toString((int) row.getCell(i).getNumericCellValue());
                            } else {
                                csvData[i] = row.getCell(i).getRichStringCellValue().getString();
                            }
                        }

                        csvErrWriter.writeRecord(csvData);
                    } else {
                        paramMapList.add(fileUploadMap);
                    }
                } else {
                    String[] csvData = new String[cc];
                    for(int i = 0; i < cc; i++) {
                        if(row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            csvData[i] = Integer.toString((int) row.getCell(i).getNumericCellValue());
                        } else {
                            csvData[i] = row.getCell(i).getRichStringCellValue().getString();
                        }
                    }

                    csvErrWriter.writeRecord(csvData);
                }

                if(!isErr) {
                    noErrCnt++;
                }
                whileCnt++;

                // max값이 엉킬수 있어 첫번째 데이터를 우선 넣는다. 이후 200건씩을 벌크로 DB에 넣는다.
                if(noErrCnt == 1 || noErrCnt % 200 == 0 || whileCnt == totalLine) {
                    uploadSegService.bulkDataImport(paramMapList);
                    paramMapList = new ArrayList<>();

                    insertedCntInfo.setInProgress(true);
                    insertedCntInfo.setInsertedCnt(noErrCnt);
                    request.getSession().setAttribute("insertedCntInfo", insertedCntInfo);
                }
            }

            insertedCntInfo.setInProgress(false);
            insertedCntInfo.setInsertedCnt(noErrCnt);
            request.getSession().setAttribute("insertedCntInfo", insertedCntInfo);

            // 발송 결과를 업데이트 한다.
            int importedDataCnt = uploadSegService.importedDataCnt(maxTargetNo);
            segmentVo.setSegmentSize(importedDataCnt);
            segmentVo.setSqlBody("  WHERE TARGET_NO = " + maxTargetNo);

            uploadSegService.updateSegmentInfoAfterImportFile(segmentVo);

            csvErrWriter.close();
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(workbook);
            IOUtil.closeQuietly(in);
        }

        Map<String, String> returnData = new HashMap<>();
        if(totalLine == noErrCnt) {  // DB에 등록 된 수와 CSV 파일의 총 라인수 비교
            FileUtil.forceDelete(writeErrFilePath);
            returnData.put("insertedErrFilePath", "");
        } else {
            returnData.put("insertedErrFilePath", writeErrFilePath);
        }

        returnData.put("segmentNo", String.valueOf(segmentVo.getSegmentNo()));
        returnData.put("segmentNm", segmentVo.getSegmentNm());
        returnData.put("segmentSize", String.valueOf(segmentVo.getSegmentSize()));
        returnData.put("totalLine", String.valueOf(totalLine));
        returnData.put("reject", segmentVo.getReject());

        if(request.getRequestURI().indexOf("target") > -1) {
            returnData.put("submitPage", "/target/fileSegment3Step.do");  // /segment/target/upload_result.do
        } else {
            returnData.put("submitPage", "/segment/fileSegment3Step.do");  // /segment/upload/upload_result.do
        }

        return new ModelAndView(new RedirectView(returnData.get("submitPage")), returnData);
    }
}
