package com.mnwise.wiseu.web.report.service.campaign;

import static com.mnwise.wiseu.web.common.util.CodeUtil.getErrCdByChannel;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.DomainLogDao;
import com.mnwise.wiseu.web.campaign.dao.LinkTraceDao;
import com.mnwise.wiseu.web.campaign.dao.ReceiptDao;
import com.mnwise.wiseu.web.campaign.dao.RejectDao;
import com.mnwise.wiseu.web.campaign.dao.ReturnMailDao;
import com.mnwise.wiseu.web.campaign.dao.ScenarioDao;
import com.mnwise.wiseu.web.campaign.dao.SendLogDao;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.channel.dao.EmLogDao;
import com.mnwise.wiseu.web.channel.dao.MzFtSendLogDao;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.common.util.StringCharsetConverter;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignAbTestVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportBasicVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportErrorVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportLinkClickVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportReceiveVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportReturnMailVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignSendResultVo;
import com.mnwise.wiseu.web.report.rowhandler.ErrorListCsvDownloadCallback;
import com.mnwise.wiseu.web.segment.dao.LinkResultDao;
import static com.mnwise.wiseu.web.common.util.CodeUtil.getErrCdByChannel;
/**
 * 캠페인 리포트 Service
 */
@Service
public class CampaignReportService extends BaseService implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(CampaignReportService.class);

    @Autowired private CampaignDao campaignDao;
    @Autowired private DomainLogDao domainLogDao;
    @Autowired private EmLogDao emLogDao;
    @Autowired private ReturnMailDao returnMailDao;
    @Autowired private LinkResultDao linkResultDao;
    @Autowired private LinkTraceDao linkTraceDao;
    @Autowired private MzFtSendLogDao mzFtSendLogDao;
    @Autowired private ReceiptDao receiptDao;
    @Autowired private RejectDao rejectDao;
    @Autowired private ScenarioDao scenarioDao;
    @Autowired private SendLogDao sendLogDao;
    @Autowired private SendResultDao sendResultDao;
    @Autowired private TemplateDao templateDao;

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    /**
     * 캠페인 리포트 리스트 카운트
     *
     * @param campaignReportVo
     * @return
     */
    public int getCampaignReportListCount(CampaignReportVo campaignReportVo) {
        return sendResultDao.getCampaignReportListCount(campaignReportVo);
    }

    /**
     * 캠페인 리포트 리스트
     *
     * @param campaignReportVo
     * @return
     */
    public List<CampaignReportVo> getCampaignReportList(CampaignReportVo campaignReportVo) {
        return sendResultDao.getCampaignReportList(campaignReportVo);
    }

    /**
     * 캠페인 리포트 리스트 엑셀 다운로드
     *
     * @param scenarioInfoVo
     * @return
     */
    public List<CampaignReportVo> selectCampaignReportListForExcel(CampaignReportVo campaignReportVo) {
        return sendResultDao.selectCampaignReportListForExcel(campaignReportVo);
    }

    /**
     * 캠페인 리스트 업데이트
     *
     * @param campaignNo
     * @return
     */
    public int updateCampaignList(int campaignNo) {
        return sendResultDao.updateCampaignList(campaignNo);
    }

    /**
     * 캠페인 리스트 업데이트
     *
     * @param campaignNo
     * @return
     */
    public int updateSmsCampaignList(int campaignNo, String sendDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet rs = null;
        ResultSet rsSchedule = null;

        int intRst = 0;
        int serviceNo = 0;
        int targetCnt = 0;
        int sendCnt = 0;
        int successCnt = 0;
        int failCnt = 0;

        String serviceType = null;
        String reportDt = null;
        String sendTm = null;
        String gubun = null;
        String resultSeq = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();

            // 3일전 데이터에 삭제
            if(database.indexOf("DB2") > -1) {
                query.append("\n    DELETE FROM NVSMSRPTSENDRESULT WHERE SERVICE_NO = ? AND SERVICE_TYPE = 'em'   ");
            } else if(database.indexOf("ORACLE") > -1) {
                query.append("\n    DELETE FROM NVSMSRPTSENDRESULT WHERE SERVICE_NO = ? AND SERVICE_TYPE = 'em'   ");
            } else {
                query.append("\n    DELETE FROM NVSMSRPTSENDRESULT WHERE SERVICE_NO = ? AND SERVICE_TYPE = 'em'   ");
            }

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);
            pstmt.executeUpdate();

            query.setLength(0);
            query = getSMSSelectQuery(database);
            pstmt2 = conn.prepareStatement(query.toString());
            pstmt2.setInt(1, campaignNo);
            pstmt2.setInt(2, campaignNo);
            rs = pstmt2.executeQuery();

            // 스케쥴 3일간 발송로그가 있으면 작업 수행
            while(rs.next()) {
                serviceType = rs.getString("service_type");
                serviceNo = rs.getInt("service_no");
                resultSeq = rs.getString("result_seq");
                successCnt = rs.getInt("success_cnt");
                failCnt = rs.getInt("fail_cnt");
                sendCnt = successCnt + failCnt;
                gubun = rs.getString("TRAN_TYPE");

                query.setLength(0);
                // 총 발송대상자 수,발송날짜와 시간을 가져온다.
                query.append("SELECT TARGET_CNT,START_DT,START_TM FROM NVSENDRESULT WHERE CAMPAIGN_NO = ? AND RESULT_SEQ = ?");
                pstmt3 = conn.prepareStatement(query.toString());
                pstmt3.setInt(1, serviceNo);
                pstmt3.setString(2, resultSeq);
                rsSchedule = pstmt3.executeQuery();
                while(rsSchedule.next()) {
                    targetCnt = rsSchedule.getInt("target_cnt");
                    reportDt = rsSchedule.getString("start_dt");
                    sendTm = rsSchedule.getString("start_tm");
                    // INSERT 작업수행
                    insertNvsmsrptsendresult(conn, serviceType, serviceNo, resultSeq, reportDt, targetCnt, sendCnt, successCnt, failCnt, gubun, sendTm);
                    // NvSendResult 테이블에 Log_STS 값을 LE 로 바꾼다.
                    updateStsNVSENDRESULT(conn, serviceNo, serviceType);
                }
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(pstmt2);
            IOUtil.closeQuietly(pstmt3);
            IOUtil.closeQuietly(conn);
        }

        return intRst;
    }

    /**
     * 캠페인 리포트 : 로그 VIEW 테이블에서 리포트용 데이터를 가져옴
     *
     * @param campaignNo
     * @return
     */
    private StringBuffer getSMSSelectQuery(String database) {
        StringBuffer sbQuery = new StringBuffer();

        sbQuery.append(" SELECT SERVICE_TYPE,SERVICE_NO,RESULT_SEQ,TRAN_TYPE            ");
        sbQuery.append("        , SUM(SUCCESS_CNT) AS SUCCESS_CNT                        ");
        sbQuery.append("        , SUM(FAIL_CNT) AS FAIL_CNT                                ");
        sbQuery.append(" FROM (SELECT SERVICE_TYPE,SERVICE_NO,RESULT_SEQ,TRAN_TYPE        ");
        sbQuery.append("         , 1 AS SUCCESS_CNT, 0 AS FAIL_CNT                        ");
        sbQuery.append("        FROM V_SMS_SUCCESS_FILE_LOG                                    ");
        sbQuery.append("        WHERE SERVICE_NO = ?                                 ");
        sbQuery.append("                 AND SERVICE_TYPE = 'em'                            ");
        sbQuery.append("             UNION ALL                                            ");
        sbQuery.append("         SELECT SERVICE_TYPE,SERVICE_NO,RESULT_SEQ,TRAN_TYPE        ");
        sbQuery.append("         , 0 AS SUCCESS_CNT, 1 AS FAIL_CNT                        ");
        sbQuery.append("        FROM V_SMS_FAIL_FILE_LOG                                    ");
        sbQuery.append("        WHERE SERVICE_NO = ?                                 ");
        sbQuery.append("                 AND SERVICE_TYPE = 'em'                            ");
        sbQuery.append("             ) A                                                    ");
        sbQuery.append("    GROUP BY SERVICE_TYPE,SERVICE_NO,RESULT_SEQ,TRAN_TYPE       ");

        return sbQuery;
    }

    /**
     * 캠페인 report 없데이트
     *
     * @param campaignNo
     * @return
     */
    private void insertNvsmsrptsendresult(Connection conn, String serviceType, int serviceNo, String resultSeq, String reportDt, int targetCnt, int sendCnt, int successCnt, int failCnt, String gubun, String sendTm) throws Exception {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn
                .prepareStatement("INSERT INTO NVSMSRPTSENDRESULT(SERVICE_TYPE,SERVICE_NO,RESULT_SEQ,REPORT_DT,TARGET_CNT,SEND_CNT,SUCCESS_CNT,FAIL_CNT,SEND_GBN,SEND_TM) VALUES(?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, serviceType);
            pstmt.setInt(2, serviceNo);
            pstmt.setLong(3, Long.parseLong(resultSeq));
            pstmt.setString(4, reportDt);
            pstmt.setInt(5, targetCnt);
            pstmt.setInt(6, sendCnt);
            pstmt.setInt(7, successCnt);
            pstmt.setInt(8, failCnt);
            pstmt.setString(9, gubun);
            pstmt.setString(10, sendTm);

            pstmt.executeUpdate();
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(pstmt);
        }
    }

    /**
     * 캠페인 sendresult에 log_sts 업데이트
     *
     * @param campaignNo
     * @return
     */
    public void updateStsNVSENDRESULT(Connection _con, int serviceNo, String serviceType) throws Exception {
        PreparedStatement pstmt = null;
        try {
            pstmt = _con.prepareStatement("UPDATE NVSENDRESULT SET LOG_STS= 'LE' WHERE CAMPAIGN_NO=?");
            pstmt.setInt(1, serviceNo);
            pstmt.executeUpdate();
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(pstmt);
        }
    }

    /**
     * 캠페인 리포트 기본정보 가져오기
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportBasicVo getCampaignReportBasicInfo(int campaignNo) {
        return sendResultDao.getCampaignReportBasicInfo(campaignNo);
    }

    /**
     * 캠페인 SMS 리포트 기본정보 가져오기
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportBasicVo getCampaignSMSReportBasicInfo(int campaignNo) {
        return sendResultDao.getCampaignSMSReportBasicInfo(campaignNo);
    }

    /**
     * 캠페인 친구톡 리포트 기본정보 가져오기
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportBasicVo getCampaignKakaoReportBasicInfo(int campaignNo) {
        return sendResultDao.getCampaignKakaoReportBasicInfo(campaignNo);
    }

    /**
     * 캠페인 리포트 요약분석 발송결과
     *
     * @param map
     * @return
     */
    public CampaignReportVo getCampaignSummaryDetail(Map<String, Object> map) {
        return sendResultDao.getCampaignSummaryDetail(map);
    }

    /**
     * 캠페인 SMS 리포트 요약분석 발송결과
     *
     * @param map
     * @return
     */
    public List<CampaignReportVo> getCampaignSMSSummaryDetail(Map<String, Object> map) {
        return sendResultDao.getCampaignSMSSummaryDetail(map);
    }

    /**
     * 캠페인 카카오톡 리포트 요약분석 발송결과
     *
     * @param map
     * @return
     */
    public List<CampaignReportVo> getCampaignKakaoSummaryDetail(Map<String, Object> map) {
        return sendResultDao.getCampaignKakaoSummaryDetail(map);
    }

    /**
     * 캠페인 리포트 요약분석 발송결과
     *
     * @param map
     * @return
     */
    public List<CampaignReportVo> getCampaignFaxSummaryDetail(Map<String, Object> map) {
        return sendResultDao.getCampaignFaxSummaryDetail(map);
    }

    /**
     * 캠페인 요약 분석 수신확인 정보
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportVo getCampaignSummaryReceive(int campaignNo) {
        return receiptDao.getCampaignSummaryReceive(campaignNo);
    }

    /**
     * 캠페인 요약 분석 리턴메일
     *
     * @param campaignNo
     * @return
     */
    public int getCampaignSummaryReturn(int campaignNo) {
        return sendResultDao.getCampaignSummaryReturn(campaignNo);
    }

    /**
     * 캠페인 요약 분석 수신거부
     *
     * @param campaignNo
     * @return
     */
    public int getCampaignSummaryReject(int campaignNo) {
        return rejectDao.getCampaignSummaryReject(campaignNo);
    }

    /**
     * 캠페인 요약 분석 스팸차단
     *
     * @param campaignNo
     * @param lang
     * @return
     */
    public int getCampaignSummarySpam(int campaignNo, String lang) {
        return domainLogDao.getCampaignSummarySpam(campaignNo, lang);
    }

    /**
     * 캠페인 리포트 전체요약 발송결과 리스트
     *
     * @param map
     * @return
     */
    public List<CampaignReportVo> getCampaignSummaryAllList(Map<String, Object> map) {
        return sendResultDao.getCampaignSummaryAllList(map);
    }

    /**
     * 캠페인 리포트 전체요약 수신확인 결과 리스트
     *
     * @param scenarioNo
     * @param lang
     * @return
     */
    public List<CampaignReportReceiveVo> getCampaignSummaryReceiveList(int scenarioNo, String lang) {
        return sendResultDao.getCampaignSummaryReceiveList(scenarioNo, lang);
    }

    /**
     * 캠페인 리포트 수신확인결과 엑셀 다운로드
     *
     * @param out
     * @param scenarioNo
     * @param campaignNo
     */
    public void makeCsvCampaignReceiveDetailList(OutputStream out, int scenarioNo, int campaignNo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            // 20140320 김민성 수신확인 엑셀다운로드 재수정 쿼리
            // CUSTOMER_KEY 와 CUSTOMER_ID 를 연결할시 CUSTOMER_ID가 인코딩 문제로 깨질수 있으므로
            // 주석처리
            //SECUDB UNIT
            // NVSENDLOG : CUSTOMER_NM, CUSTOMER_EMAIL, MESSAGE_KEY
            // NVRECEIPT : 해당 컬럼 없음
            query.append("\n        SELECT X.CUSTOMER_KEY                                                              ");
            query.append("\n              ,X.CUSTOMER_NM                                                              ");
            query.append("\n              ,X.CUSTOMER_EMAIL                                                          ");
            query.append("\n              ,CASE WHEN Y.DURATION_CNT = 0 THEN NULL ELSE 'O' END DURATION_YN          ");
            query.append("\n              ,CASE WHEN Y.DUPLICATION_CNT = 1 THEN NULL ELSE 'O' END DUPLICATION_YN      ");
            query.append("\n              ,CASE WHEN Y.MOBILE_CNT = 0 THEN NULL ELSE 'O' END MOBILE_YN              ");
            query.append("\n              ,Y.DURATION_CNT                                                              ");
            query.append("\n              ,Y.DUPLICATION_CNT                                                          ");
            query.append("\n              ,Y.MOBILE_CNT                                                              ");
            query.append("\n              ,Y.OPEN_DT                                                                  ");
            query.append("\n              ,Y.OPEN_TM                                                                  ");
            query.append("\n         FROM NVSENDLOG X                                                                  ");
            query.append("\n             ,(SELECT CAMPAIGN_NO                                                          ");
            query.append("\n                     ,CUSTOMER_ID                                                          ");
            query.append("\n                     ,RESULT_SEQ                                                          ");
            query.append("\n                     ,LIST_SEQ                                                          ");
            query.append("\n                     ,MIN(OPEN_DT) OPEN_DT                                              ");
            query.append("\n                     ,MIN(OPEN_TM) OPEN_TM                                              ");
            query.append("\n                     ,COUNT(CUSTOMER_ID) DUPLICATION_CNT                                  ");
            query.append("\n                     ,COUNT(CASE WHEN MOBILE_YN = 'Y' THEN 1                              ");
            query.append("\n                             ELSE NULL END) MOBILE_CNT                                  ");
            query.append("\n                     ,COUNT(CASE WHEN READING_DURATION >= 10 THEN 1                      ");
            query.append("\n                             ELSE NULL END) DURATION_CNT                                  ");
            query.append("\n                 FROM NVRECEIPT                                                          ");
            query.append("\n                WHERE CAMPAIGN_NO = ?                                                      ");
            query.append("\n             GROUP BY CAMPAIGN_NO, CUSTOMER_ID, RESULT_SEQ, LIST_SEQ ) Y                  ");
            query.append("\n         WHERE X.CAMPAIGN_NO = Y.CAMPAIGN_NO                                              ");
            // query.append("\n AND X.CUSTOMER_KEY = Y.CUSTOMER_ID ");
            query.append("\n           AND X.RESULT_SEQ = Y.RESULT_SEQ                                              ");
            query.append("\n           AND X.LIST_SEQ = Y.LIST_SEQ                                                  ");
            query.append("\n           AND X.CAMPAIGN_NO = ?                                                        ");
            query.append("\n           AND X.RESEND_YN = 'N'                                                        ");

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);
            // 20140320 캠페인 리포트 쿼리수정으로 인한 추가
            pstmt.setInt(2, campaignNo);

            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String id = msAccessor.getMessage("report.campaign.reportdao.id", locale);
            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            String email = msAccessor.getMessage("report.campaign.reportdao.email", locale);
            String write1 = msAccessor.getMessage("report.campaign.reportdao.out.write1", locale);
            String write2 = msAccessor.getMessage("report.campaign.reportdao.out.write2", locale);
            String write3 = msAccessor.getMessage("report.campaign.reportdao.out.write3", locale);
            String write4 = msAccessor.getMessage("report.campaign.reportdao.out.write4", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);
            // 2013-04-08 남기욱 - 데이터 정렬 및 모바일 수신 추가
            String write7 = msAccessor.getMessage("report.campaign.reportdao.out.write7", locale);
            String write8 = msAccessor.getMessage("report.campaign.reportdao.out.write8", locale);
            String write9 = msAccessor.getMessage("report.campaign.reportdao.out.write9", locale);
            String write10 = msAccessor.getMessage("report.campaign.reportdao.out.write10", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(id.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(email.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write9.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write10.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write1.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write3.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write2.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write4.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write7.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(write8.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            /* 엑셀 다운로드 log 확인용 System.out.println("*****Excel Log *****"); */
            String cusKey;
            String cusNm;
            String cusEmail;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                cusKey = rs.getString("customer_key");
                if(cusKey.trim().startsWith("=")) {
                    cusKey = "'" + cusKey;
                }
                cusNm = rs.getString("customer_nm");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                cusEmail = rs.getString("customer_email");
                cusEmail = security.securityWithColumn(cusEmail, "CUSTOMER_EMAIL", "DECRYPT");
                if(cusEmail.trim().startsWith("=")) {
                    cusEmail = "'" + cusEmail;
                }

                out.write(StringUtil.defaultString(cusKey).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(cusNm)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusEmail).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("open_dt")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("open_tm")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                // 2013-04-08 남기욱 - 데이터 정렬 및 모바일 수신 추가
                out.write(StringUtil.defaultString(rs.getString("duration_yn")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("duration_cnt")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("duplication_yn")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("duplication_cnt")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("mobile_yn")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("mobile_cnt")).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;

                /* 엑셀 다운로드 log 확인용 System.out.println("*****Excel Log :  " +" "+ rs.getString("customer_key") +" "+ rs.getString("customer_nm") +" "+ rs.getString("customer_email") +" "+
                 * rs.getString("open_dt") +" "+ rs.getString("open_tm")); */
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 리포트 성공대상자 엑셀 다운로드
     *
     * @param out
     * @param scenarioNo
     * @param campaignNo
     * @param channelType
     */
    public void makeCsvCampaignSuccessDetailList(OutputStream out, int scenarioNo, int campaignNo, String channelType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            query.append("SELECT X.CUSTOMER_KEY                          \n");
            query.append("      ,X.CUSTOMER_NM                           \n");
            query.append("      ,X.CUSTOMER_EMAIL                        \n");
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                query.append("      , X.SEND_DT || ' ' || X.SEND_TM SEND_DTM \n");
            } else if(database.indexOf("MYSQL") > -1) {
                query.append("      , CONCAT(X.SEND_DT, ' ', X.SEND_TM) SEND_DTM \n");
            } else {
                query.append("      , X.SEND_DT + X.SEND_TM SEND_DTM        \n");
            }
            //SECUDB UNIT
            // NVSENDLOG : CUSTOMER_NM, CUSTOMER_EMAIL, MESSAGE_KEY
            query.append("  FROM NVSENDLOG X                             \n");
            query.append(" WHERE X.ERROR_CD IN ( ");
            int temp = 0;
            for(String code: getErrCdByChannel(channelType).split(",")) {
                if(temp > 0 ){ query.append(","); }
                query.append("'").append(code).append("'");
                temp= temp+1;
            }
            query.append(" ) \n");
            query.append("   AND CAMPAIGN_NO = ?                         \n");
            query.append("   AND RESEND_YN = 'N'                         \n");

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);

            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String id = msAccessor.getMessage("report.campaign.reportdao.id", locale);
            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            String email = msAccessor.getMessage("common.menu.number", locale);
            if(Channel.MAIL.equals(channelType)) {
                email = msAccessor.getMessage("report.campaign.reportdao.email", locale);
            }
            String sendday = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(id.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(email.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(sendday.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            /* 엑셀 다운로드 log 확인용 System.out.println("*****Excel Log *****"); */
            String cusKey;
            String cusNm;
            String cusEmail;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                cusKey = rs.getString("customer_key");
                if(cusKey.trim().startsWith("=")) {
                    cusKey = "'" + cusKey;
                }
                cusNm = rs.getString("customer_nm");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                cusEmail = rs.getString("customer_email");
                cusEmail = security.securityWithColumn(cusEmail, "CUSTOMER_EMAIL", "DECRYPT");
                if(cusEmail.trim().startsWith("=")) {
                    cusEmail = "'" + cusEmail;
                }

                out.write(StringUtil.defaultString(cusKey).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(cusNm)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusEmail).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("send_dtm")).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;

                /* 엑셀 다운로드 log 확인용 System.out.println("*****Excel Log :  " +" "+ rs.getString("customer_key") +" "+ rs.getString("customer_nm") +" "+ rs.getString("customer_email") +" "+
                 * rs.getString("send_dtm")); */
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 리포트 성공대상자 엑셀 다운로드 (Fax)
     * - FAX 대상자파일을 다운로드할 때 엑셀파일의 컬럼명을 고객명/FAX번호/발송일시/부가정보1/부가정보2 로 수정하고, 전화번호사이에 '-' 를 삽입
     *
     * @param out
     * @param scenarioNo
     * @param campaignNo
     */
    public void makeCsvCampaignSuccessDetailFaxList(OutputStream out, int scenarioNo, int campaignNo, long resultSeq) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            StringBuffer query = new StringBuffer();
            query.append("SELECT X.SLOT1, X.SLOT2                           \n");
            query.append("      ,X.CUSTOMER_NM                           \n");
            query.append("      ,X.CUSTOMER_EMAIL                        \n");
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                query.append("      , X.SEND_DT || ' ' || X.SEND_TM SEND_DTM \n");
            } else if(database.indexOf("MYSQL") > -1) {
                query.append("      , CONCAT(X.SEND_DT, ' ', X.SEND_TM) SEND_DTM \n");
            } else {
                query.append("      , X.SEND_DT + X.SEND_TM SEND_DTM        \n");
            }
            //SECUDB UNIT
            // NVSENDLOG : CUSTOMER_NM, CUSTOMER_EMAIL, MESSAGE_KEY
            query.append("  FROM NVSENDLOG X                             \n");
            query.append(" WHERE X.ERROR_CD = '0'                      \n");
            query.append("   AND CAMPAIGN_NO = ?                         \n");
            if(resultSeq != 0) {
                query.append("  AND RESULT_SEQ = ?                                        \n");
            }
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);
            if(resultSeq != 0) {
                pstmt.setLong(2, resultSeq);
            }

            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            String faxno = msAccessor.getMessage("report.campaign.reportdao.faxno", locale);
            String sendday = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
            String slot1 = msAccessor.getMessage("report.campaign.reportdao.slot1", locale);
            String slot2 = msAccessor.getMessage("report.campaign.reportdao.slot2", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(faxno.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(sendday.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot1.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot2.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String phoneNo;
            String cusNm;
            String slotVal1;
            String slotVal2;
            while(rs.next()) {
                // 전화번호 사이에 '-' 추가
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를 붙여준다.
                phoneNo = rs.getString("customer_email");
                phoneNo = security.securityWithColumn(phoneNo, "CUSTOMER_EMAIL", "DECRYPT");
                if(phoneNo != null && phoneNo.trim().startsWith("=")) {
                    phoneNo = "'" + phoneNo;
                }
                if(phoneNo != null && phoneNo.substring(0, 2).equals("02")) {
                    if(phoneNo.length() == 9) {
                        phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 5) + "-" + phoneNo.substring(5);
                    } else {
                        phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 6) + "-" + phoneNo.substring(6);
                    }
                } else {
                    phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 6) + "-" + phoneNo.substring(6);
                }
                cusNm = rs.getString("customer_nm");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm != null && cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                slotVal1 = rs.getString("slot1");
                if(slotVal1 != null && slotVal1.trim().startsWith("=")) {
                    slotVal1 = "'" + slotVal1;
                }
                slotVal2 = rs.getString("slot2");
                if(slotVal2 != null && slotVal2.trim().startsWith("=")) {
                    slotVal2 = "'" + slotVal2;
                }

                out.write(StringUtil.defaultString(StringCharsetConverter.convert(cusNm)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(phoneNo).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("send_dtm")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal1)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal2)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 리포트 성공대상자 엑셀 다운로드 (SMS)
     *
     * @param out
     * @param scenarioNo
     * @param campaignNo
     */
    public void makeCsvCampaignSuccessDetailSmsList(OutputStream out, int scenarioNo, int campaignNo, String channelType, long resultSeq) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                query.append("SELECT SUBSTR(TRAN_PHONE,1,3) || '-' || SUBSTR(TRAN_PHONE,4,4)|| '-'|| SUBSTR(TRAN_PHONE,8,4) AS TRAN_PHONE , TRAN_DATE,                \n");
            } else {
                query.append("SELECT SUBSTRING(TRAN_PHONE,0,4) + '-' + SUBSTRING(TRAN_PHONE,4,4)+ '-'+ SUBSTRING(TRAN_PHONE,8,4) AS TRAN_PHONE , TRAN_DATE,                \n");
            }

            query.append("      SLOT1, SLOT2                                            \n");
            query.append("  FROM V_SMS_SUCCESS_FILE_LOG A                              \n");
            query.append("  WHERE SERVICE_TYPE = 'em'                                 \n");
            query.append("  AND SERVICE_NO = ?                                        \n");
            if(resultSeq != 0) {
                query.append("  AND RESULT_SEQ = ?                                        \n");
            }

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);
            // System.out.println(query.toString());
            if(resultSeq != 0) {
                pstmt.setLong(2, resultSeq);
            }
            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String smsno = msAccessor.getMessage("report.campaign.reportdao.smsno", locale);
            String sendday = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
            String slot1 = msAccessor.getMessage("report.campaign.reportdao.slot1", locale);
            String slot2 = msAccessor.getMessage("report.campaign.reportdao.slot2", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(smsno.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(sendday.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot1.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot2.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String phoneVal;
            String slotVal1;
            String slotVal2;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                phoneVal = rs.getString("tran_phone");
                if(phoneVal != null && phoneVal.trim().startsWith("=")) {
                    phoneVal = "'" + phoneVal;
                }
                slotVal1 = rs.getString("slot1");
                if(slotVal1 != null && slotVal1.trim().startsWith("=")) {
                    slotVal1 = "'" + slotVal1;
                }
                slotVal2 = rs.getString("slot2");
                if(slotVal2 != null && slotVal2.trim().startsWith("=")) {
                    slotVal2 = "'" + slotVal2;
                }

                out.write(StringUtil.defaultString(phoneVal).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("tran_date")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal1)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal2)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    public void makeCsvCampaignDetailFRTList(OutputStream out, int scenarioNo, int campaignNo, String channelType, long resultSeq , String rsltCd) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            StringBuffer query = new StringBuffer();
            //String database = conn.getMetaData().getDatabaseProductName().toUpperCase();

            query.append("SELECT  PHONE_NUM , SND_DTM,                      \n");
            query.append("      SLOT1, SLOT2                                \n");
            query.append("  FROM MZFTSENDLOG A                              \n");
            query.append("  WHERE SERVICE_NO = ?                            \n");
            query.append("  AND RESULT_SEQ = ?                              \n");
            query.append("  AND SERVICE_TYPE = 'em'                         \n");
            query.append("  AND RSLT_CD = ?                                 \n"); // 성공코드 추가

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);
            pstmt.setLong(2, resultSeq);
            pstmt.setString(3, rsltCd);

            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String smsno = msAccessor.getMessage("report.campaign.reportdao.smsno", locale);
            String sendday = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
            String slot1 = msAccessor.getMessage("report.campaign.reportdao.slot1", locale);
            String slot2 = msAccessor.getMessage("report.campaign.reportdao.slot2", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(smsno.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(sendday.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot1.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot2.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String phoneVal;
            String slotVal1;
            String slotVal2;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를 붙여준다.
                // 복호화 추가
                phoneVal = security.securityWithColumn(rs.getString("PHONE_NUM") , "PHONE_NUM", "DECRYPT");

                // 전화번호 형태로 변경 (010-2222-2222)
                String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";

                if(Pattern.matches(regEx, phoneVal)) {
                    phoneVal = phoneVal.replaceAll(regEx, "$1-$2-$3");
                }

                if(phoneVal != null && phoneVal.trim().startsWith("=")) {
                    phoneVal = "'" + phoneVal;
                }
                slotVal1 = rs.getString("SLOT1");
                if(slotVal1 != null && slotVal1.trim().startsWith("=")) {
                    slotVal1 = "'" + slotVal1;
                }
                slotVal2 = rs.getString("SLOT2");
                if(slotVal2 != null && slotVal2.trim().startsWith("=")) {
                    slotVal2 = "'" + slotVal2;
                }

                out.write(StringUtil.defaultString(phoneVal).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("SND_DTM")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal1)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal2)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 리포트 성공대상자 엑셀 다운로드 (친구톡)
     *
     * @param out
     * @param scenarioNo
     * @param campaignNo
     */
    public void makeCsvCampaignSuccessDetailFRTList(OutputStream out, int scenarioNo, int campaignNo, String channelType, long resultSeq) {
        makeCsvCampaignDetailFRTList(out, scenarioNo, campaignNo, channelType, resultSeq , "0000");
    }

    /**
     * 캠페인 리포트 리턴메일 엑셀 다운로드
     *
     * @param out
     * @param scenarioNo
     * @param campaignNo
     */
    public void makeCsvCampaignReturnDetailList(OutputStream out, int scenarioNo, int campaignNo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            //SECUDB UNIT
            //NVSENDLOG : CUSTOMER_NM, CUSTOMER_EMAIL, MESSAGE_KEY
            //NVRETURNMAIL : 해당 필드 없음
            if(database.indexOf("ORACLE") > -1) {
                query.append("SELECT A.CUSTOMER_ID                  \n");
                query.append("     ,B.CUSTOMER_NM                   \n");
                query.append("     ,B.CUSTOMER_EMAIL                \n");
                query.append("     ,A.SMTPCODE                      \n");
                query.append("     ,A.RETURN_MSG                    \n");
                query.append("     ,C.VAL ERROR_MSG                 \n");
                query.append("FROM   NVRETURNMAIL A                 \n");
                query.append("     ,NVSENDLOG B                     \n");
                query.append("     ,(SELECT CD                      \n");
                query.append("             ,VAL                     \n");
                query.append("             ,CD_CAT                  \n");
                query.append("        FROM   NV_CD_MST              \n");
                query.append("        WHERE  CD_CAT IN('C00011'     \n");
                query.append("                     , 'C00021')) C   \n");
                query.append("WHERE  A.CAMPAIGN_NO = B.CAMPAIGN_NO  \n");
                query.append("AND    A.RESULT_SEQ = B.RESULT_SEQ    \n");
                query.append("AND    A.LIST_SEQ = B.LIST_SEQ        \n");
                query.append("AND    A.CUSTOMER_ID = B.CUSTOMER_KEY \n");
                query.append("AND    A.SMTPCODE = C.CD(+)           \n");
                query.append("AND    A.CAMPAIGN_NO = ?              \n");
            } else {
                // mssql
                query.append("SELECT A.CUSTOMER_ID                                         \n");
                query.append("     ,B.CUSTOMER_NM                                          \n");
                query.append("     ,B.CUSTOMER_EMAIL                                       \n");
                query.append("     ,A.SMTPCODE                                             \n");
                query.append("     ,A.RETURN_MSG                                           \n");
                query.append("     ,C.VAL ERROR_MSG                                        \n");
                query.append("FROM   NVRETURNMAIL A                                        \n");
                query.append("        LEFT OUTER JOIN                                      \n");
                query.append("     ,(SELECT CD                                             \n");
                query.append("             ,VAL                                            \n");
                query.append("             ,CD_CAT                                         \n");
                query.append("        FROM   NV_CD_MST                                     \n");
                query.append("        WHERE  CD_CAT IN('C00011'                            \n");
                query.append("                     , 'C00021')) C ON A.SMTPCODE = C.CD     \n");
                query.append("     ,NVSENDLOG B                                            \n");
                query.append("WHERE  A.CAMPAIGN_NO = B.CAMPAIGN_NO                         \n");
                query.append("AND    A.RESULT_SEQ = B.RESULT_SEQ                           \n");
                query.append("AND    A.LIST_SEQ = B.LIST_SEQ                               \n");
                query.append("AND    A.CUSTOMER_ID = B.CUSTOMER_KEY                        \n");
                query.append("AND    A.CAMPAIGN_NO = ?                                     \n");
            }
            query.append("AND    B.RESEND_YN = 'N'                                     \n");
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);

            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String id = msAccessor.getMessage("report.campaign.reportdao.id", locale);
            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            String email = msAccessor.getMessage("report.campaign.reportdao.email", locale);
            String errorcode = msAccessor.getMessage("report.campaign.domain.title.errorcode", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);
            String returnMessage = msAccessor.getMessage("report.campaign.reportdao.out.return.message", locale);
            String errorMessage = msAccessor.getMessage("report.campaign.reportdao.out.error.message", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(id.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(email.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorcode.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(returnMessage.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorMessage.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String cusId;
            String cusNm;
            String cusEmail;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                cusId = rs.getString("customer_id");
                if(cusId.trim().startsWith("=")) {
                    cusId = "'" + cusId;
                }
                cusNm = rs.getString("customer_nm");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                cusEmail = rs.getString("customer_email");
                cusEmail = security.securityWithColumn(cusEmail, "CUSTOMER_EMAIL", "DECRYPT");
                if(cusEmail.trim().startsWith("=")) {
                    cusEmail = "'" + cusEmail;
                }
                out.write(StringUtil.defaultString(cusId).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(cusNm)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusEmail).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("smtpcode")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(rs.getString("return_msg"))).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(rs.getString("error_msg"))).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 리포트 수신거부 엑셀 다운로드
     *
     * @param out
     * @param scenarioNo
     * @param campaignNo
     */
    public void makeCsvCampaignRejectDetailList(OutputStream out, int scenarioNo, int campaignNo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                //SECUDB UNIT
                //NVREJECT : CUSTOMER_EMAIL, CUSTOMER_NM
                query.append("SELECT A.CAMPAIGN_NO                                                                                   \n");
                query.append("      ,A.CUSTOMER_ID                                                                                        \n");
                query.append("      ,B.CUSTOMER_NM                                                                                      \n");
                query.append("      ,B.CUSTOMER_EMAIL                                                                                   \n");
                query.append("      ,TO_CHAR(TO_DATE(A.REJECT_DT || A.REJECT_TM, 'yyyymmddhh24miss'), 'yyyy-mm-dd hh24:mi:ss') REJECT_DTM \n");
                query.append("  FROM NVREJECT   A                                                                                     \n");
                query.append("     ,NVSENDLOG B                                                                                         \n");
                query.append("WHERE  A.CAMPAIGN_NO = B.CAMPAIGN_NO                                                     \n");
                query.append("AND    A.RESULT_SEQ = B.RESULT_SEQ                                                                \n");
                query.append("AND    A.LIST_SEQ = B.LIST_SEQ                                                                         \n");
                query.append("AND    A.CUSTOMER_ID = B.CUSTOMER_KEY                                                        \n");
                query.append("AND    A.CAMPAIGN_NO = ?                                                                             \n");
            } else {
                //SECUDB UNIT
                //NVREJECT : CUSTOMER_EMAIL, CUSTOMER_NM
                query.append("SELECT A.CAMPAIGN_NO                                        \n");
                query.append("      ,A.CUSTOMER_ID                                        \n");
                query.append("      ,B.CUSTOMER_NM                                        \n");
                query.append("      ,B.CUSTOMER_EMAIL                                     \n");
                query.append("      ,SUBSTRING(A.REJECT_DT + A.REJECT_TM,1,4)+'-'+          \n");
                query.append("       SUBSTRING(A.REJECT_DT + A.REJECT_TM,5,2)+'-'+          \n");
                query.append("       SUBSTRING(A.REJECT_DT + A.REJECT_TM,7,2)+' '+          \n");
                query.append("       SUBSTRING(A.REJECT_DT + A.REJECT_TM,9,2)+':'+          \n");
                query.append("       SUBSTRING(A.REJECT_DT + A.REJECT_TM,11,2)+':'+          \n");
                query.append("       SUBSTRING(A.REJECT_DT + A.REJECT_TM,13,2)  REJECT_DTM   \n");
                query.append("  FROM NVREJECT A                                                  \n");
                query.append("     ,NVSENDLOG B                                                                                         \n");
                query.append("WHERE  A.CAMPAIGN_NO = B.CAMPAIGN_NO                                                     \n");
                query.append("AND    A.RESULT_SEQ = B.RESULT_SEQ                                                                \n");
                query.append("AND    A.LIST_SEQ = B.LIST_SEQ                                                                         \n");
                query.append("AND    A.CUSTOMER_ID = B.CUSTOMER_KEY                                                        \n");
                query.append("AND    A.CAMPAIGN_NO = ?                                                                             \n");
            }

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);

            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String id = msAccessor.getMessage("report.campaign.reportdao.id", locale);
            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            String email = msAccessor.getMessage("report.campaign.reportdao.email", locale);
            String campaign_no = msAccessor.getMessage("report.campaign.reportdao.campaign.no", locale);
            String rejectDay = msAccessor.getMessage("report.campaign.reportdao.rejectday", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(campaign_no.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(id.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(email.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(rejectDay.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String cusId;
            String cusNm;
            String cusEmail;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                cusId = rs.getString("customer_id");
                if(cusId.trim().startsWith("=")) {
                    cusId = "'" + cusId;
                }
                cusNm = rs.getString("customer_nm");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                cusEmail = rs.getString("customer_email");
                cusEmail = security.securityWithColumn(cusEmail, "CUSTOMER_EMAIL", "DECRYPT");
                if(cusEmail.trim().startsWith("=")) {
                    cusEmail = "'" + cusEmail;
                }

                out.write(StringUtil.defaultString(rs.getString("campaign_no")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusId).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(cusNm)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusEmail).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("reject_dtm")).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 오류 분석 리포트 리스트
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param lang
     * @return
     */
    public List<CampaignReportErrorVo> getCampaignReportErrorList(int scenarioNo, int campaignNo, String lang, String channelType, long resultSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("scenarioNo", scenarioNo);
        map.put("campaignNo", campaignNo);
        map.put("channelType", channelType);
        map.put("resultSeq", resultSeq);
        map.put("lang", lang);

        if(Channel.MAIL.equals(channelType)) {
            return domainLogDao.getCampaignReportErrorList(map);
        } else if(Channel.FAX.equals(channelType)) {
            return sendLogDao.getCampaignReportFaxErrorList(map);
        } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {
            return emLogDao.getCampaignReportSMSErrorList(map);
        } else if(Channel.FRIENDTALK.equals(channelType)) {
            return mzFtSendLogDao.getCampaignReportFRTErrorList(map);
        }else if(Channel.BRANDTALK.equals(channelType)) {
            map.put("successCode", PropertyUtil.getProperty("brandtalk.code.success", "A0000"));
            return sendLogDao.getCampaignReportBRTErrorList(map);
        }  else if(Channel.PUSH.equals(channelType)) {
            map.put("successCodeArray", PropertyUtil.getProperty("push.code.success", "000").split(","));
            return sendLogDao.getCampaignReportPushErrorList(map);
        }

        return null;

    }

    /**
     * 캠페인 오류 분석 리포트 - 에러코드별 사용자 목록 리스트 (엑셀 다운로드용)
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param errorCd 에러코드
     * @param lang 언어
     * @return
     */
    public void makeCsvCampaignErrorCodeDetailList(OutputStream out, int scenarioNo, int campaignNo, String errorCd, String lang) {
        makeCsvCampaignErrorCodeDetailList(out, scenarioNo, campaignNo, errorCd, lang, null);
    }

    /**
     * 캠페인 오류 분석 리포트 - 에러코드별 사용자 목록 리스트 (엑셀 다운로드용)
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param errorCd 에러코드
     * @param lang 언어
     * @return
     */
    public void makeCsvCampaignFaxErrorCodeDetailList(OutputStream out, int scenarioNo, int campaignNo, String errorCd, String lang, long resultSeq) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            query.append("  SELECT CUSTOMER_NM, CUSTOMER_EMAIL, ERROR_CD, VAL ,SLOT1, SLOT2       \n");
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                query.append("       ,  SEND_DT ||' '|| SEND_TM AS SEND_DATE                                       \n");
            } else {
                query.append("       ,  SEND_DT +' '+ SEND_TM AS SEND_DATE                                       \n");
            }
            //SECUDB UNIT
            // NVSENDLOG : CUSTOMER_NM, CUSTOMER_EMAIL, MESSAGE_KEY
            query.append("  FROM (SELECT CUSTOMER_NM, CUSTOMER_EMAIL, ERROR_CD , SEND_DT, SEND_TM, SLOT1,SLOT2, RESULT_SEQ FROM NVSENDLOG  WHERE CAMPAIGN_NO = ? AND ERROR_CD = ?) A,   \n");
            query.append("       (SELECT CD,  VAL, CD_CAT FROM NV_CD_MST  WHERE CD_CAT = 'AF0001' AND CD NOT IN ('0')) B   \n");
            query.append("  WHERE A.ERROR_CD = B.CD                      \n");

            if(resultSeq != 0) {
                query.append("  AND A.RESULT_SEQ = ?                     \n");
            }

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);
            pstmt.setString(2, errorCd);

            if(resultSeq != 0) {
                pstmt.setLong(3, resultSeq);
            }
            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            String faxno = msAccessor.getMessage("report.campaign.reportdao.faxno", locale);
            String sendDay = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
            String errorCode = msAccessor.getMessage("report.campaign.domain.title.errorcode", locale);
            String errorMessage = msAccessor.getMessage("report.campaign.reportdao.out.error.message", locale);
            String slot1 = msAccessor.getMessage("report.campaign.reportdao.slot1", locale);
            String slot2 = msAccessor.getMessage("report.campaign.reportdao.slot2", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(faxno.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(sendDay.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorCode.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorMessage.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot1.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot2.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String phoneNo;
            String cusNm;
            String slotVal1;
            String slotVal2;
            while(rs.next()) {
                // 전화번호 사이에 '-' 추가
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를 붙여준다.
                phoneNo = rs.getString("customer_email");
                phoneNo = security.securityWithColumn(phoneNo, "CUSTOMER_EMAIL", "DECRYPT");
                if(phoneNo != null && phoneNo.trim().startsWith("=")) {
                    phoneNo = "'" + phoneNo;
                }
                if(phoneNo != null && phoneNo.substring(0, 2).equals("02")) {
                    if(phoneNo.length() == 9) {
                        phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 5) + "-" + phoneNo.substring(5);
                    } else {
                        phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 6) + "-" + phoneNo.substring(6);
                    }
                } else {
                    phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 6) + "-" + phoneNo.substring(6);
                }

                cusNm = rs.getString("CUSTOMER_NM");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm != null && cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                slotVal1 = rs.getString("SLOT1");
                if(slotVal1 != null && slotVal1.trim().startsWith("=")) {
                    slotVal1 = "'" + slotVal1;
                }
                slotVal2 = rs.getString("SLOT2");
                if(slotVal2 != null && slotVal2.trim().startsWith("=")) {
                    slotVal2 = "'" + slotVal2;
                }

                out.write(StringUtil.defaultString(StringCharsetConverter.convert(cusNm)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(phoneNo).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("SEND_DATE")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("ERROR_CD")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("VAL")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal1)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal2)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 오류 분석 리포트 - 에러코드별 사용자 목록 리스트 (엑셀 다운로드용)
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param errorCd 에러코드
     * @param lang 언어
     * @return
     */
    public void makeCsvCampaignSMSErrorCodeDetailList(OutputStream out, int scenarioNo, int campaignNo, String errorCd, String lang, long resultSeq, String channelType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                query.append("SELECT SUBSTR(TRAN_PHONE,1,3) || '-' || SUBSTR(TRAN_PHONE,4,4)|| '-'|| SUBSTR(TRAN_PHONE,8,4) AS TRAN_PHONE , TRAN_DATE,                \n");
            } else {
                query.append("SELECT SUBSTRING(TRAN_PHONE,0,4) + '-' + SUBSTRING(TRAN_PHONE,4,4)+ '-'+ SUBSTRING(TRAN_PHONE,8,4) AS TRAN_PHONE , TRAN_DATE,                \n");
            }
            query.append("   TRAN_RSLT, VAL, SLOT1, SLOT2    \n");
            query.append("  FROM V_SMS_FAIL_FILE_LOG a,                             \n");
            query.append("       (SELECT CD,  VAL, CD_CAT FROM NV_CD_MST  WHERE CD_CAT IN ('AS0001','AS0002')) e              \n");
            query.append("  WHERE SERVICE_NO = ?                             \n");
            query.append("   AND SERVICE_TYPE = 'em'                         \n");
            query.append("  AND A.TRAN_RSLT = E.CD                           \n");
            query.append("  AND A.TRAN_RSLT = ?                          \n");

            if(resultSeq != 0) {
                query.append("  AND A.RESULT_SEQ = ?                            \n");
            }
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);
            pstmt.setString(2, errorCd);

            if(resultSeq != 0) {
                pstmt.setLong(3, resultSeq);
            }
            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String smsno = msAccessor.getMessage("report.campaign.reportdao.smsno", locale);
            String sendDay = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
            String errorCode = msAccessor.getMessage("report.campaign.domain.title.errorcode", locale);
            String errorMessage = msAccessor.getMessage("report.campaign.reportdao.out.error.message", locale);
            String slot1 = msAccessor.getMessage("report.campaign.reportdao.slot1", locale);
            String slot2 = msAccessor.getMessage("report.campaign.reportdao.slot2", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(smsno.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(sendDay.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorCode.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorMessage.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot1.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(slot2.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String phoneVal;
            String slotVal1;
            String slotVal2;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                phoneVal = rs.getString("tran_phone");
                if(phoneVal != null && phoneVal.trim().startsWith("=")) {
                    phoneVal = "'" + phoneVal;
                }
                slotVal1 = rs.getString("slot1");
                if(slotVal1 != null && slotVal1.trim().startsWith("=")) {
                    slotVal1 = "'" + slotVal1;
                }
                slotVal2 = rs.getString("slot2");
                if(slotVal2 != null && slotVal2.trim().startsWith("=")) {
                    slotVal2 = "'" + slotVal2;
                }

                out.write(StringUtil.defaultString(phoneVal).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("tran_date")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("tran_rslt")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("val")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal1)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(slotVal2)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 오류 분석 리포트 - 에러코드별 사용자 목록 리스트 (엑셀 다운로드용 - 친구)
     *
     * @param out
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param errorCd 에러코드
     * @param lang 언어
     * @param resultSeq
     * @param channelType 채널 타입
     *
     * @return
     */
    public void makeCsvCampaignFtErrorCodeDetailList(OutputStream out, int scenarioNo, int campaignNo, String errorCd, String lang, long resultSeq, String channelType) {
        makeCsvCampaignDetailFRTList(out, scenarioNo, campaignNo,  channelType, resultSeq, errorCd);
    }

    /**
     * 캠페인 오류 분석 리포트 - 에러코드별 사용자 목록 리스트 (엑셀 다운로드용 - 브랜드톡)
     *
     * @param out
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param errorCd 에러코드
     * @param lang 언어
     * @param resultSeq
     * @param channelType 채널 타입
     *
     * @return
     */
    public void makeCsvEmErrorCodeDetailList(int campaignNo, long resultSeq, String errorCd, OutputStream out) throws Exception{
        ErrorListCsvDownloadCallback callback = new ErrorListCsvDownloadCallback(out);
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
        Locale locale = localeChangeInterceptor.getLocale();
        String id = msAccessor.getMessage("report.ecare.customer.id", locale);
        String name = msAccessor.getMessage("report.ecare.customer.name", locale);
        String contact = msAccessor.getMessage("report.ecare.customer.smsno", locale);
        String sendday = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
        String title[]= {id, name, contact, sendday};
        callback.createTitle(title);

        sendLogDao.makeCsvEmErrorCodeDetailList(campaignNo, resultSeq, errorCd, callback);
    }

    /**
     * 캠페인 리턴메일 분석 리포트 - Bounce별 반송메일 갯수
     *
     * @param campaignNo 캠페인번호
     * @return
     */
    public List<CampaignReportReturnMailVo> getCampaignReturnMailDetailList(int campaignNo, String lang) {
        return returnMailDao.getCampaignReturnMailDetailList(campaignNo, lang);
    }

    /**
     * 캠페인 오류 분석 리포트 - 에러코드별 & 도메인별 사용자 목록 리스트 (엑셀 다운로드용)
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param errorCd 에러코드
     * @param domainNm 도메인명
     * @return
     */
    public void makeCsvCampaignErrorCodeDetailList(OutputStream out, int scenarioNo, int campaignNo, String errorCd, String lang, String domainNm) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            //SECUDB UNIT
            // NVSENDLOG : CUSTOMER_NM, CUSTOMER_EMAIL, MESSAGE_KEY
            if(database.indexOf("ORACLE") > -1) {
                query.append("\n        SELECT X.CUSTOMER_NM                                              ");
                query.append("\n              ,X.CUSTOMER_EMAIL                                           ");
                query.append("\n              , X.SEND_DT || ' ' || X.SEND_TM SEND_DTM                    ");
                query.append("\n              ,X.ERROR_CD                                                 ");
                query.append("\n              ,DECODE(Y.CD_CAT, 'C00001', VAL, X.ERR_MSG) ERROR_MSG       ");
                query.append("\n              ,DECODE(Y.CD_CAT, 'C00011', 'O') SOFT_BOUNCE_YN             ");
                query.append("\n              ,DECODE(Y.CD_CAT, 'C00021', 'O') HARD_BOUNCE_YN             ");
                query.append("\n          FROM NVSENDLOG X                                                ");
                query.append("\n              ,(SELECT CD                                                 ");
                query.append("\n                      ,VAL                                                ");
                query.append("\n                      ,CD_CAT                                             ");
                query.append("\n                  FROM NV_CD_MST                                          ");
                query.append("\n                 WHERE CD_CAT IN('C00001', 'C00011', 'C00021')            ");
                query.append("\n                   AND LANG = ? ) Y                                       ");
                query.append("\n         WHERE X.ERROR_CD = Y.CD(+)                                       ");
                query.append("\n           AND CAMPAIGN_NO = ?                                            ");
                query.append("\n           AND X.ERROR_CD = ?                                              ");
            } else if(database.indexOf("DB2") > -1) {
                query.append("\n        SELECT X.CUSTOMER_NM                                              ");
                query.append("\n              ,X.CUSTOMER_EMAIL                                           ");
                query.append("\n              , X.SEND_DT || ' ' || X.SEND_TM SEND_DTM                    ");
                query.append("\n              ,X.ERROR_CD                                                 ");
                query.append("\n              ,DECODE(Y.CD_CAT, 'C00001', VAL, X.ERR_MSG) ERROR_MSG       ");
                query.append("\n              ,DECODE(Y.CD_CAT, 'C00011', 'O') SOFT_BOUNCE_YN             ");
                query.append("\n              ,DECODE(Y.CD_CAT, 'C00021', 'O') HARD_BOUNCE_YN             ");
                query.append("\n          FROM NVSENDLOG X LEFT OUTER JOIN                                ");
                query.append("\n               (SELECT CD                                                 ");
                query.append("\n                      ,VAL                                                ");
                query.append("\n                      ,CD_CAT                                             ");
                query.append("\n                  FROM NV_CD_MST                                          ");
                query.append("\n                 WHERE CD_CAT IN('C00001', 'C00011', 'C00021')            ");
                query.append("\n                   AND LANG = ? ) Y                                       ");
                query.append("\n          ON x.ERROR_CD = Y.CD                                            ");
                query.append("\n           WHERE CAMPAIGN_NO = ?                                          ");
                query.append("\n           AND X.ERROR_CD = ?                                              ");
            } else if(database.indexOf("MYSQL") > -1) {
                query.append("SELECT X.CUSTOMER_NM                                                      \n");
                query.append("      ,X.CUSTOMER_EMAIL                                                   \n");
                query.append("      , CONCAT(X.SEND_DT, X.SEND_TM) SEND_DTM                             \n");
                query.append("      ,X.ERROR_CD                                                         \n");
                query.append("      ,CASE WHEN Y.CD_CAT =  'C00001' THEN VAL ELSE X.ERR_MSG END ERROR_MSG \n");
                query.append("      ,CASE WHEN Y.CD_CAT =  'C00011' THEN 'O' ELSE '' END SOFT_BOUNCE_YN \n");
                query.append("      ,CASE WHEN Y.CD_CAT =  'C00021' THEN 'O' ELSE '' END HARD_BOUNCE_YN \n");
                query.append("  FROM NVSENDLOG X LEFT OUTER JOIN                                        \n");
                query.append("       (SELECT CD                                                         \n");
                query.append("              ,VAL                                                        \n");
                query.append("              ,CD_CAT                                                     \n");
                query.append("          FROM NV_CD_MST                                                  \n");
                query.append("         WHERE CD_CAT IN('C00001', 'C00011', 'C00021') AND LANG = ? ) Y ON X.ERROR_CD = Y.CD      \n");
                query.append(" WHERE CAMPAIGN_NO = ?                                                    \n");
                query.append("   AND X.ERROR_CD = ?                                                        \n");
            } else {
                query.append("SELECT X.CUSTOMER_NM                                                      \n");
                query.append("      ,X.CUSTOMER_EMAIL                                                   \n");
                query.append("      , X.SEND_DT + X.SEND_TM SEND_DTM                                    \n");
                query.append("      ,X.ERROR_CD                                                         \n");
                query.append("      ,CASE WHEN Y.CD_CAT =  'C00001' THEN VAL ELSE X.ERR_MSG END ERROR_MSG \n");
                query.append("      ,CASE WHEN Y.CD_CAT =  'C00011' THEN 'O' ELSE '' END SOFT_BOUNCE_YN \n");
                query.append("      ,CASE WHEN Y.CD_CAT =  'C00021' THEN 'O' ELSE '' END HARD_BOUNCE_YN \n");
                query.append("  FROM NVSENDLOG X LEFT OUTER JOIN                                        \n");
                query.append("       (SELECT CD                                                         \n");
                query.append("              ,VAL                                                        \n");
                query.append("              ,CD_CAT                                                     \n");
                query.append("          FROM NV_CD_MST                                                  \n");
                query.append("         WHERE CD_CAT IN('C00001', 'C00011', 'C00021') AND LANG = ? ) Y ON X.ERROR_CD = Y.CD      \n");
                query.append(" WHERE CAMPAIGN_NO = ?                                                    \n");
                query.append("   AND X.ERROR_CD = ?                                                        \n");
            }

            if(StringUtil.isNotEmpty(domainNm)) {
                if(domainNm.equalsIgnoreCase("ZZZ.DOMAIN")) {
                    query.append("\n   AND X.SEND_DOMAIN NOT IN  (SELECT DOMAIN_NM FROM NVTOPDOMAIN WHERE DOMAIN_NM <> ? )  ");
                } else {
                    query.append("\n   AND X.SEND_DOMAIN = ?     ");
                }
            }

            // 재발송 제외
            query.append("\n   AND X.RESEND_YN = 'N'     ");
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, lang);
            pstmt.setInt(2, campaignNo);
            pstmt.setString(3, errorCd);

            if(domainNm != null && !domainNm.equals("")) {
                pstmt.setString(4, domainNm);
            }

            rs = pstmt.executeQuery();

            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            String email = msAccessor.getMessage("report.campaign.reportdao.email", locale);
            String sendDay = msAccessor.getMessage("report.campaign.reportdao.sendday", locale);
            String errorCode = msAccessor.getMessage("report.campaign.domain.title.errorcode", locale);
            String errorMessage = msAccessor.getMessage("report.campaign.reportdao.out.error.message", locale);
            String write5 = msAccessor.getMessage("report.campaign.reportdao.out.write5", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(email.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(sendDay.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorCode.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(errorMessage.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String cusNm;
            String cusEmail;
            String errMsg;
            while(rs.next()) {
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                cusNm = rs.getString("customer_nm");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                cusEmail = rs.getString("customer_email");
                cusEmail = security.securityWithColumn(cusEmail, "CUSTOMER_EMAIL", "DECRYPT");
                if(cusEmail.trim().startsWith("=")) {
                    cusEmail = "'" + cusEmail;
                }
                errMsg = rs.getString("error_msg");
                if(errMsg.trim().startsWith("=")) {
                    errMsg = "'" + errMsg;
                }

                out.write(StringUtil.defaultString(StringCharsetConverter.convert(cusNm)).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusEmail).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("send_dtm")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rs.getString("error_cd")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(StringCharsetConverter.convert(errMsg)).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write5.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }


    /**
     * 캠페인 링크클릭 리포트
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    public List<CampaignReportLinkClickVo> getCampaignReportLinkClickList(int scenarioNo, int campaignNo) {
        return linkTraceDao.getCampaignReportLinkClickList(scenarioNo, campaignNo);
    }

    /**
     * 캠페인 링크클릭 리포트 총합계
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    public CampaignReportLinkClickVo getCampaignReportLinkClickTotal(int scenarioNo, int campaignNo) {
        return linkTraceDao.getCampaignReportLinkClickTotal(scenarioNo, campaignNo);
    }

    /**
     * 캠페인 수신거부 리포트
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    public List<CampaignReportBasicVo> getCampaignReportRejectList(int scenarioNo, int campaignNo) {
        return rejectDao.getCampaignReportRejectList(scenarioNo, campaignNo);
    }

    /**
     * 캠페인 수신거부 대상자 다운로드
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     */
    public void makeCsvCampaignReportRejectDetailList(OutputStream out, int scenarioNo, int campaignNo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                //SECUDB NONE
                //NVREJECT : 해당 필드 없음
                query.append("SELECT     A.CAMPAIGN_NO                                                          ");
                query.append("\n          ,A.CUSTOMER_ID                                                    ");
                query.append("\n          ,B.CUSTOMER_NM                                                    ");
                query.append("\n          ,B.CUSTOMER_EMAIL                                                    ");
                query.append("\n          ,TO_CHAR(TO_DATE(A.REJECT_DT || A.REJECT_TM, 'yyyymmddhh24miss')    ");
                query.append("\n           , 'yyyy-mm-dd hh24:mi:ss') REJECT_DTM                          ");
                query.append("\n      FROM NVREJECT A                                                       ");
                query.append("     ,NVSENDLOG B                                                                                         \n");
                query.append("WHERE  A.CAMPAIGN_NO = B.CAMPAIGN_NO                                                     \n");
                query.append("AND    A.RESULT_SEQ = B.RESULT_SEQ                                                                \n");
                query.append("AND    A.LIST_SEQ = B.LIST_SEQ                                                                         \n");
                query.append("AND    A.CUSTOMER_ID = B.CUSTOMER_KEY                                                        \n");
                query.append("AND    A.CAMPAIGN_NO = ?                                                                             \n");
            }else if(database.indexOf("MYSQL") > -1 ) {
                query.append("\n    SELECT A.CAMPAIGN_NO                                           ");
                query.append("\n          ,A.CUSTOMER_ID                                                    ");
                query.append("\n          ,B.CUSTOMER_NM                                                    ");
                query.append("\n          ,B.CUSTOMER_EMAIL                                                    ");
                query.append("\n          ,date_format(CONCAT(REJECT_DT,REJECT_TM),'%Y-%m-%d %H:%i:%S') REJECT_DTM ");
                query.append("\n      FROM NVREJECT  A                                                      ");
                query.append("     ,NVSENDLOG B                                                                                         \n");
                query.append("WHERE  A.CAMPAIGN_NO = B.CAMPAIGN_NO                                                     \n");
                query.append("AND    A.RESULT_SEQ = B.RESULT_SEQ                                                                \n");
                query.append("AND    A.LIST_SEQ = B.LIST_SEQ                                                                         \n");
                query.append("AND    A.CUSTOMER_ID = B.CUSTOMER_KEY                                                        \n");
                query.append("AND    A.CAMPAIGN_NO = ?                                                                             \n");
            }else {
                //SECUDB NONE
                //NVREJECT : 해당 필드 없음
                query.append("\n    SELECT A.CAMPAIGN_NO                                           ");
                query.append("\n          ,A.CUSTOMER_ID                                                    ");
                query.append("\n          ,B.CUSTOMER_NM                                                    ");
                query.append("\n          ,B.CUSTOMER_EMAIL                                                    ");
                query.append("\n          ,SUBSTRING(A.REJECT_DT + A.REJECT_TM,1,4)+'-'+                      ");
                query.append("\n           SUBSTRING(A.REJECT_DT + A.REJECT_TM,5,2)+'-'+                      ");
                query.append("\n           SUBSTRING(A.REJECT_DT + A.REJECT_TM,7,2)+' '+                      ");
                query.append("\n           SUBSTRING(A.REJECT_DT + A.REJECT_TM,9,2)+':'+                      ");
                query.append("\n           SUBSTRING(A.REJECT_DT + A.REJECT_TM,11,2)+':'+                      ");
                query.append("\n           SUBSTRING(A.REJECT_DT + A.REJECT_TM,13,2)  REJECT_DTM               ");
                query.append("\n      FROM NVREJECT  A                                                      ");
                query.append("     ,NVSENDLOG B                                                                                         \n");
                query.append("WHERE  A.CAMPAIGN_NO = B.CAMPAIGN_NO                                                     \n");
                query.append("AND    A.RESULT_SEQ = B.RESULT_SEQ                                                                \n");
                query.append("AND    A.LIST_SEQ = B.LIST_SEQ                                                                         \n");
                query.append("AND    A.CUSTOMER_ID = B.CUSTOMER_KEY                                                        \n");
                query.append("AND    A.CAMPAIGN_NO = ?                                                                             \n");
            }

            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, campaignNo);

            rs = pstmt.executeQuery();
            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
            Locale locale = localeChangeInterceptor.getLocale();

            String campaign_no = msAccessor.getMessage("report.campaign.reportdao.campaign.no", locale);
            String id = msAccessor.getMessage("report.campaign.reportdao.id", locale);
            String name = msAccessor.getMessage("report.campaign.reportdao.name", locale);
            //20191211 이름,이메일추가
            String email = msAccessor.getMessage("report.campaign.reportdao.email", locale);
            String rejectDay = msAccessor.getMessage("report.campaign.reportdao.rejectday", locale);
            String write6 = msAccessor.getMessage("report.campaign.reportdao.out.write6", locale);

            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(campaign_no.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(id.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            //20191211 이름,이메일추가
            out.write(name.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(email.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(rejectDay.getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);

            int rowCnt = 0;

            String cusId;
            //20191211 이름,이메일추가
            String cusNm;
            String cusEmail;
            String rejectDtm;
            while(rs.next()) {
                out.write(StringUtil.defaultString(rs.getString("campaign_no")).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                cusId = rs.getString("customer_id");
                if(cusId.trim().startsWith("=")) {
                    cusId = "'" + cusId;
                }

                cusNm = rs.getString("customer_nm");
                cusNm = security.securityWithColumn(cusNm, "CUSTOMER_NM", "DECRYPT");
                if(cusNm.trim().startsWith("=")) {
                    cusNm = "'" + cusNm;
                }
                cusEmail = rs.getString("customer_email");
                cusEmail = security.securityWithColumn(cusEmail, "CUSTOMER_EMAIL", "DECRYPT");
                if(cusEmail.trim().startsWith("=")) {
                    cusEmail = "'" + cusEmail;
                }
                rejectDtm= rs.getString("reject_dtm");

                out.write(StringUtil.defaultString(cusId).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusNm).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(cusEmail).getBytes(Const.UFT16LE));
                out.write(Const.TAB);
                out.write(StringUtil.defaultString(rejectDtm).getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
                rowCnt++;
            }

            if(rowCnt == 0) {
                out.write(write6.getBytes(Const.UFT16LE));
                out.write(Const.NEWLINE);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 캠페인 리포트 - 도메인 분석 리스트
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    public List<CampaignReportVo> getCampaignReportDomainList(int scenarioNo, int campaignNo, String lang) {
        return sendResultDao.getCampaignReportDomainList(scenarioNo, campaignNo, lang);
    }

    /**
     * 해당 캠페인이 속한 시나리오의 최대 Depth No 를 가져온다. 재발송은 일반발송과 타겟발송의 가장 마지막 Depth 만 가능하다. 따라서, 일반발송과 타겟발송의 가장 마지막 Depth를 가져온다.
     *
     * @param scenarioNo
     * @param campaignNo
     * @param relationType
     * @return
     */
    public int getCampaignReportMaxDepth(int scenarioNo, int campaignNo, String relationType) {
        if(relationType.equals("R")) {
            relationType = "N";
        }
        return campaignDao.getCampaignReportMaxDepth(scenarioNo, campaignNo, relationType);
    }

    /**
     * 캠페인 템플릿 리스트를 가져온다.
     *
     * @param campaignNo
     * @return
     */
    public List<TemplateVo> getTemplateList(int campaignNo) {
        return templateDao.getTemplateList(campaignNo);
    }

    /**
     * 캠페인 전체요약 총 발송 정보 가져오기 - 그래프에서 데이터를 이용하기 위해 사용
     *
     * @param map
     * @return
     */
    public CampaignSendResultVo getCampaignReportAllCntInfo(Map<String, Object> map) {
        return sendResultDao.getCampaignReportAllCntInfo(map);
    }

    /**
     * 캠페인 작성자 정보 조회
     *
     * @param scenarioNo
     * @return
     */
    public CaseInsensitiveMap selectCreateUserInfo(int scenarioNo) {
        return scenarioDao.selectCreateUserInfo(scenarioNo);
    }

    public List<CampaignAbTestVo> getCampaignReportAbTestList(int campaignNo, String abTestCond) {
        if("O".equals(abTestCond)) {
            return sendLogDao.getCampaignReportAbTestOpenList(campaignNo);
        } else {
            return sendLogDao.getCampaignReportAbTestLinkClickList(campaignNo);
        }
    }

    public int getCampaignReportAbRealCnt(int campaignNo, String abTestCond) {
        if("O".equals(abTestCond)) {
            return receiptDao.getCampaignReportAbRealOpenCnt(campaignNo);
        }

        return linkResultDao.getCampaignReportAbRealLinkCnt(campaignNo);
    }
}