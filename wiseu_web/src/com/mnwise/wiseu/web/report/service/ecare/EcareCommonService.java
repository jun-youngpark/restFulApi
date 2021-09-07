package com.mnwise.wiseu.web.report.service.ecare;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;
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
import com.mnwise.wiseu.web.channel.dao.MzFtSendLogDao;
import com.mnwise.wiseu.web.channel.dao.MzSendLogDao;
import com.mnwise.wiseu.web.common.util.StringCharsetConverter;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendResultDao;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.dao.EcareRptOpenDao;
import com.mnwise.wiseu.web.report.dao.KakaoRptSendResultDao;
import com.mnwise.wiseu.web.report.dao.SmsRptSendResultDao;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

@Service
public class EcareCommonService extends BaseService implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EcareCommonService.class);

    @Autowired private EcareDao ecareDao;
    @Autowired private EcareRptOpenDao ecareRptOpenDao;
    @Autowired private EcareSendResultDao ecareSendResultDao;
    @Autowired private KakaoRptSendResultDao kakaoRptSendResultDao;
    @Autowired private MzFtSendLogDao mzFtSendLogDao;
    @Autowired private MzSendLogDao mzSendLogDao;
    @Autowired private SmsRptSendResultDao smsRptSendResultDao;

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public List<EcareVo> getScenarioChannelList(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareDao.getScenarioChannelList(scenarioInfoVo);
    }

    public String getChannel(int ecareNo) {
        return ecareDao.getChannel(ecareNo);
    }

    public EcareScenarioInfoVo selectScenarioInfo(EcareScenarioInfoVo scenarioInfoVo) {
        EcareScenarioInfoVo ecareScenarioInfoVo = null;

        // 스케쥴(스케쥴)
        if(scenarioInfoVo.getEcareInfoVo().getServiceType().equals("S") && scenarioInfoVo.getEcareInfoVo().getSubType().equals("S")) {
            ecareScenarioInfoVo = ecareDao.selectScenarioScheduleInfo(scenarioInfoVo.getEcareNo());
        } else {  // 실시간(실시간, 스케쥴(분))
            ecareScenarioInfoVo = ecareDao.selectScenarioRealtimeInfo(scenarioInfoVo.getEcareNo());
        }

        return ecareScenarioInfoVo;
    }

    public int selectOpenCnt(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareRptOpenDao.selectOpenCnt(scenarioInfoVo);
    }

    public RptSendResultVo selectScheduleSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareSendResultDao.selectScheduleSendResult(scenarioInfoVo);
    }

    public RptSendResultVo selectRealtimeSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareSendResultDao.selectRealtimeSendResult(scenarioInfoVo);
    }

    public RptSendResultVo selectFaxSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareSendResultDao.selectFaxSendResult(scenarioInfoVo);
    }

    public RptSendResultVo selectEcareFaxScheduleSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareSendResultDao.selectEcareFaxScheduleSendResult(scenarioInfoVo);
    }

    public RptSendResultVo selectEcareSMSScheduleSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return smsRptSendResultDao.selectEcareSMSScheduleSendResult(scenarioInfoVo);
    }

    public RptSendResultVo selectSMSSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return smsRptSendResultDao.selectSMSSendResult(scenarioInfoVo);
    }

    /**
     * 발송 성공 로그 다운로드
     *
     * @param ecareNo 이케어 번호
     * @return
     */
    public void makeCsvEcareReportLog(OutputStream out, String mode, int ecareNo, String reportDt, String resultSeq, String serviceType, String subType, String channelType) {
        if(mode.equals("send_success")) {
            // 발송 성공
            if(channelType.equals(Channel.MAIL) || channelType.equals(Channel.FAX)) {
                makeCsvSendSuccessLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType);
            } else if(channelType.equals(Channel.SMS) || channelType.equals(Channel.LMSMMS)) {
                makeCsvSMSSendLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType, "Y");
            } else if(channelType.equals(Channel.ALIMTALK) || channelType.equals("AS") || channelType.equals("AL")) {
                makeCsvAlimtalkSendLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType, "Y");
            } else if(channelType.equals(Channel.FRIENDTALK) || channelType.equals("CS") || channelType.equals("CL")) {
                makeCsvFriendtalkSendLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType, "Y");
            }
        } else if(mode.equals("send_fail")) {
            // 발송 실패
            if(channelType.equals(Channel.MAIL) || channelType.equals(Channel.FAX)) {
                makeCsvSendFailLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType);
            } else if(channelType.equals(Channel.SMS) || channelType.equals(Channel.LMSMMS)) {
                makeCsvSMSSendLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType, "N");
            } else if(channelType.equals(Channel.ALIMTALK) || channelType.equals("AS") || channelType.equals("AL")) {
                makeCsvAlimtalkSendLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType, "N");
            } else if(channelType.equals(Channel.FRIENDTALK) || channelType.equals("CS") || channelType.equals("CL")) {
                makeCsvFriendtalkSendLog(out, ecareNo, reportDt, resultSeq, serviceType, subType, channelType, "N");
            }
        } else if(mode.equals("normal_receipt")) {
            // 수신 확인
            makeCsvNormalReceiptLog(out, ecareNo, reportDt, resultSeq, serviceType, subType);
        }
    }

    /**
     * 발송 성공 로그 다운로드
     *
     * @param ecareNo
     * @return
     */
    public void makeCsvSendSuccessLog(OutputStream out, int ecareNo, String reportDt, String resultSeq, String serviceType, String subType, String channelType) {
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
        Locale locale = localeChangeInterceptor.getLocale();

        String customerId = msAccessor.getMessage("report.ecare.customer.id", locale);
        String customerNm = msAccessor.getMessage("report.ecare.customer.name", locale);
        String customerEmail = msAccessor.getMessage("report.ecare.customer.email", locale);
        String customerFax = msAccessor.getMessage("report.ecare.customer.faxno", locale);
        String sendDate = msAccessor.getMessage("report.ecare.title.send.date", locale);
        String slot1 = msAccessor.getMessage("report.ecare.title.error.slot1", locale);
        String slot2 = msAccessor.getMessage("report.ecare.title.error.slot2", locale);
        String write5 = msAccessor.getMessage("report.ecare.title.error.write5", locale);

        serviceType = serviceType.toUpperCase();
        subType = subType.toUpperCase();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer sql = new StringBuffer();
            //SECUDB UNIT
            //NVECARESENDLOG : CUSTOMER_EMAIL, CUSTOMER_NM, MESSAGE_KEY
            sql.append("\n SELECT A.CUSTOMER_KEY                                    ");
            sql.append("\n       ,A.CUSTOMER_NM                                     ");
            sql.append("\n       ,A.CUSTOMER_EMAIL                                  ");
            sql.append("\n       ,A.SLOT1 ,A.SLOT2                                  ");
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1)
                sql.append("\n       ,(A.SEND_DT || ' ' || A.SEND_TM) AS SEND_DATE      ");
            else
                sql.append("\n       ,(A.SEND_DT + ' ' + A.SEND_TM) AS SEND_DATE      ");

            sql.append("\n FROM NVECARESENDLOG A, NVECARESENDRESULT B               ");
            sql.append("\n WHERE A.ECARE_NO = ?                                        ");
            sql.append("\n       AND A.ECARE_NO = B.ECARE_NO                       ");
            sql.append("\n       AND A.RESULT_SEQ = B.RESULT_SEQ                       ");
            if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                sql.append("\n       AND B.RESULT_SEQ = ?                                  ");
            } else {
                sql.append("\n       AND B.START_DT = ?                                  ");
            }
            if("M".equals(channelType)) {
                sql.append("\n       AND A.ERROR_CD = '250'                                  ");
            } else {
                sql.append("\n       AND A.ERROR_CD = '0'                              ");
            }

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, ecareNo);
            if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                pstmt.setString(2, resultSeq);
            } else {
                pstmt.setString(2, reportDt);
            }
            rs = pstmt.executeQuery();
            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            if("M".equals(channelType)) {
                out.write(printObjectToBytes(customerId + "\t" + customerNm + "\t" + customerEmail + "\t" + sendDate + "\n"));
            } else {
                out.write(printObjectToBytes(customerNm + "\t" + customerFax + "\t" + sendDate + "\t" + slot1 + "\t" + slot2 + "\n"));
            }

            int rowCnt = 0;

            while(rs.next()) {
                if("M".equals(channelType)) {
                    out.write(printObjectToBytes(rs.getString("customer_key")));
                    out.write(Const.TAB);
                    String customer_nm = StringCharsetConverter.convert(rs.getString("customer_nm"));
                    customer_nm = security.securityWithColumn(customer_nm, "CUSTOMER_NM", "DECRYPT");
                    out.write(printObjectToBytes(customer_nm));
                    out.write(Const.TAB);
                    String customer_email = rs.getString("customer_email");
                    customer_email = security.securityWithColumn(customer_email, "CUSTOMER_EMAIL", "DECRYPT");
                    out.write(printObjectToBytes(customer_email));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rs.getString("send_date")));
                    out.write(Const.NEWLINE);
                } else {
                    // 20100623 김일남 전화번호 사이에 - 추가
                    String phoneNo = rs.getString("customer_email");
                    phoneNo = security.securityWithColumn(phoneNo, "CUSTOMER_EMAIL", "DECRYPT");
                    if(phoneNo.substring(0, 2).equals("02")) {
                        if(phoneNo.length() == 9) {
                            phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 5) + "-" + phoneNo.substring(5);
                        } else {
                            phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 6) + "-" + phoneNo.substring(6);
                        }
                    } else {
                        phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 6) + "-" + phoneNo.substring(6);
                    }
                    out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("customer_nm"))));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(phoneNo));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rs.getString("send_date")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("slot1"))));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("slot2"))));
                    out.write(Const.NEWLINE);
                }
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
     * 발송 로그 다운로드 - SMS
     *
     * @param successGbn -성공여부
     * @return
     */
    public void makeCsvSMSSendLog(OutputStream out, int ecareNo, String reportDt, String resultSeq, String serviceType, String subType, String channelType, String successGbn) {
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
        Locale locale = localeChangeInterceptor.getLocale();

        String smsno = msAccessor.getMessage("report.ecare.customer.smsno", locale);
        String sendDate = msAccessor.getMessage("report.ecare.title.send.date", locale);
        String errorCd = msAccessor.getMessage("report.ecare.title.error.cd", locale);
        String errorVal = msAccessor.getMessage("report.ecare.title.error.msg", locale);
        String slot1 = msAccessor.getMessage("report.ecare.title.error.slot1", locale);
        String slot2 = msAccessor.getMessage("report.ecare.title.error.slot2", locale);
        String write5 = msAccessor.getMessage("report.ecare.title.error.write5", locale);

        serviceType = serviceType.toUpperCase();
        subType = subType.toUpperCase();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();

            StringBuffer sql = new StringBuffer();

            if(successGbn.equals("Y")) { // 성공 OR 실패
                sql.append("\n  SELECT TRAN_PHONE ");
                if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                    sql.append("\n  , TRAN_DATE");
                } else {
                    sql.append("\n  , TRAN_DATE");
                }

                sql.append("\n  , SLOT1, SLOT2    ");
                sql.append("\n  FROM V_SMS_SUCCESS_FILE_LOG                                                  ");
                sql.append("\n  WHERE SERVICE_TYPE = 'ec'                                                   ");
                sql.append("\n    AND SERVICE_NO = ?                                                    ");

                if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                    sql.append("\n       AND RESULT_SEQ = ?                                                  ");
                } else {
                    if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                        sql.append("\n       AND TO_CHAR(TRAN_DATE,'yyyyMMdd') = ?                        ");
                    } else {
                        sql.append("\n       AND REPLACE(CONVERT(VARCHAR(8),TRAN_DATE,112),'-','') = ?    ");
                    }
                }

            } else {
                sql.append("\n  SELECT TRAN_PHONE , TRAN_DATE, TRAN_RSLT    , VAL, SLOT1, SLOT2            ");
                sql.append("\n FROM V_SMS_FAIL_FILE_LOG a,                                                   ");
                // 20100623 김일남 sms,mms채널 분기처리 - 오류코드 때문에 -- KSM LGU 일때만 MMS 에러 코드가 구분되어있음..
                //if(channelType.equals("S"))
                sql.append("\n (SELECT CD,  VAL, CD_CAT FROM NV_CD_MST WHERE CD_CAT IN ('AS0001', 'AS0002')) e    ");
                //else
                //    sql.append("\n (SELECT CD,  VAL, CD_CAT FROM NV_CD_MST WHERE CD_CAT = 'AS0002')e    ");
                sql.append("\n  WHERE SERVICE_TYPE = 'ec'                                                  ");
                sql.append("\n    AND A.TRAN_RSLT = E.CD                                                 ");
                sql.append("\n    AND SERVICE_NO = ?                                                    ");

                if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                    sql.append("\n       AND RESULT_SEQ = ?                                                  ");
                } else {
                    if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                        sql.append("\n       AND TO_CHAR(TRAN_DATE,'yyyyMMdd') = ?                        ");
                    } else {
                        sql.append("\n       AND REPLACE(CONVERT(VARCHAR(8),TRAN_DATE,112),'-','') = ?    ");
                    }
                }
            }
            // log.debug(sql);

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, ecareNo);

            if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                pstmt.setString(2, resultSeq);
            } else {
                pstmt.setString(2, reportDt);
            }

            rs = pstmt.executeQuery();
            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            if(successGbn.equals("Y")) {
                out.write(printObjectToBytes(smsno + "\t" + sendDate + "\t" + slot1 + "\t" + slot2 + "\n"));
            } else {
                out.write(printObjectToBytes(smsno + "\t" + sendDate + "\t" + errorCd + "\t" + errorVal + "\t" + slot1 + "\t" + slot2 + "\n"));
            }

            int rowCnt = 0;
            while(rs.next()) {
                // 전화번호 사이에 - 추가
                String phoneNo = rs.getString("TRAN_PHONE");
                phoneNo = security.securityWithColumn(phoneNo, "TRAN_PHONE", "DECRYPT");

                String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
                if(Pattern.matches(regEx, phoneNo)) {
                    phoneNo = phoneNo.replaceAll(regEx, "$1-$2-$3");
                }

                out.write(printObjectToBytes(phoneNo));
                out.write(Const.TAB);
                out.write(printObjectToBytes(rs.getString("TRAN_DATE")));
                out.write(Const.TAB);
                if(successGbn.equals("N")) {
                    out.write(printObjectToBytes(rs.getString("TRAN_RSLT")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rs.getString("VAL")));
                    out.write(Const.TAB);
                }
                out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("SLOT1"))));
                out.write(Const.TAB);
                out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("SLOT2"))));
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
     * 발송 실패 로그 다운로드
     *
     * @param ecareNo
     * @return
     */
    public void makeCsvSendFailLog(OutputStream out, int ecareNo, String reportDt, String resultSeq, String serviceType, String subType, String channelType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
        Locale locale = localeChangeInterceptor.getLocale();

        String customerId = msAccessor.getMessage("report.ecare.customer.id", locale);
        String customerNm = msAccessor.getMessage("report.ecare.customer.name", locale);
        String customerEmail = msAccessor.getMessage("report.ecare.customer.email", locale);
        String customerFax = msAccessor.getMessage("report.ecare.customer.faxno", locale);
        String sendDate = msAccessor.getMessage("report.ecare.title.send.date", locale);
        String errorCd = msAccessor.getMessage("report.ecare.title.error.cd", locale);
        String errorMsg = msAccessor.getMessage("report.ecare.title.error.msg", locale);
        String slot1 = msAccessor.getMessage("report.ecare.title.error.slot1", locale);
        String write5 = msAccessor.getMessage("report.ecare.title.error.write5", locale);

        serviceType = serviceType.toUpperCase();
        subType = subType.toUpperCase();

        try {
            conn = getConnection();

            StringBuffer sql = new StringBuffer();

            if("M".equals(channelType)) {
                //SECUDB UNIT
                //NVECARESENDLOG : CUSTOMER_EMAIL, CUSTOMER_NM, MESSAGE_KEY
                sql.append("\n SELECT A.CUSTOMER_KEY                                    ");
                sql.append("\n       ,A.CUSTOMER_NM                                     ");
                sql.append("\n       ,A.CUSTOMER_EMAIL                                  ");
                sql.append("\n       ,A.SLOT1, A.SLOT2                                  ");
                String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
                if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1)
                    sql.append("\n       ,(A.SEND_DT || ' ' || A.SEND_TM) AS SEND_DATE      ");
                else
                    sql.append("\n       ,(A.SEND_DT + ' ' + A.SEND_TM) AS SEND_DATE      ");

                sql.append("\n       ,A.ERROR_CD                                 ");
                sql.append("\n       ,A.ERR_MSG                                  ");

                sql.append("\n FROM NVECARESENDLOG A, NVECARESENDRESULT B               ");
                sql.append("\n WHERE A.ECARE_NO = ?                            ");
                sql.append("\n       AND A.ECARE_NO = B.ECARE_NO                                  ");
                sql.append("\n       AND A.RESULT_SEQ = B.RESULT_SEQ                       ");

                if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                    sql.append("\n       AND B.RESULT_SEQ = ?                                  ");
                } else {
                    sql.append("\n       AND B.START_DT = ?                                  ");
                }
                sql.append("\n       AND A.ERROR_CD <> '250'                              ");
            } else {
                //SECUDB UNIT
                //NVECARESENDLOG : CUSTOMER_EMAIL, CUSTOMER_NM, MESSAGE_KEY
                sql.append("\n SELECT A.CUSTOMER_KEY                                    ");
                sql.append("\n       ,A.CUSTOMER_NM                                     ");
                sql.append("\n       ,A.CUSTOMER_EMAIL                                  ");
                sql.append("\n       ,A.SLOT1, A.SLOT2                                  ");
                String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
                if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1)
                    sql.append("\n       ,(a.SEND_DT || ' ' || a.SEND_TM) AS SEND_DATE      ");
                else
                    sql.append("\n       ,(a.SEND_DT + ' ' + a.SEND_TM) AS SEND_DATE      ");

                sql.append("\n       ,A.ERROR_CD                                 ");
                sql.append("\n       ,VAL AS ERR_MSG                                  ");
                sql.append("\n FROM NVECARESENDLOG A, NVECARESENDRESULT B               ");
                sql.append("\n    ,(SELECT CD, VAL FROM NV_CD_MST WHERE CD_CAT = 'AF0001')  C             ");
                sql.append("\n WHERE A.ECARE_NO = ?                            ");
                sql.append("\n       AND A.ECARE_NO = B.ECARE_NO                                  ");
                sql.append("\n       AND A.RESULT_SEQ = B.RESULT_SEQ                       ");
                sql.append("\n       AND A.ERROR_CD = C.CD                                  ");

                if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                    sql.append("\n       AND B.RESULT_SEQ = ?                                  ");
                } else {
                    sql.append("\n       AND B.START_DT = ?                                  ");
                }
                sql.append("\n       AND A.ERROR_CD <> '0'                              ");
            }

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, ecareNo);
            if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                pstmt.setString(2, resultSeq);
            } else {
                pstmt.setString(2, reportDt);
            }
            rs = pstmt.executeQuery();
            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            if("M".equals(channelType)) {
                out.write(printObjectToBytes(customerId + "\t" + customerNm + "\t" + customerEmail + "\t" + sendDate + "\t" + errorCd + "\t" + errorMsg + "\n"));
            } else {
                out.write(printObjectToBytes(customerNm + "\t" + customerFax + "\t" + sendDate + "\t" + errorCd + "\t" + errorMsg + "\t" + slot1 + "\t" + slot1 + "\n"));
            }

            int rowCnt = 0;

            while(rs.next()) {
                if("M".equals(channelType)) {
                    out.write(printObjectToBytes(rs.getString("CUSTOMER_KEY")));
                    out.write(Const.TAB);
                    String customer_nm = StringCharsetConverter.convert(rs.getString("CUSTOMER_NM"));
                    customer_nm = security.securityWithColumn(customer_nm, "CUSTOMER_NM", "DECRYPT");
                    out.write(printObjectToBytes(customer_nm));
                    out.write(Const.TAB);
                    String customer_email = rs.getString("CUSTOMER_EMAIL");
                    customer_email = security.securityWithColumn(customer_email, "CUSTOMER_EMAIL", "DECRYPT");
                    out.write(printObjectToBytes(customer_email));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rs.getString("SEND_DATE")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rs.getString("ERROR_CD")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("ERR_MSG"))));
                    out.write(Const.NEWLINE);
                } else {
                    // 20100623 김일남 전화번호 사이에 - 추가
                    String phoneNo = rs.getString("customer_email");
                    phoneNo = security.securityWithColumn(phoneNo, "CUSTOMER_EMAIL", "DECRYPT");
                    if(phoneNo.substring(0, 2).equals("02")) {
                        if(phoneNo.length() == 9) {
                            phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 5) + "-" + phoneNo.substring(5);
                        } else {
                            phoneNo = phoneNo.substring(0, 2) + "-" + phoneNo.substring(2, 6) + "-" + phoneNo.substring(6);
                        }
                    } else {
                        phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 6) + "-" + phoneNo.substring(6);
                    }
                    String customer_nm = StringCharsetConverter.convert(rs.getString("CUSTOMER_NM"));
                    customer_nm = security.securityWithColumn(customer_nm, "CUSTOMER_NM", "DECRYPT");
                    out.write(printObjectToBytes(customer_nm));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(phoneNo));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rs.getString("SEND_DATE")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rs.getString("ERROR_CD")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("ERR_MSG"))));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("SLOT1"))));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(StringCharsetConverter.convert(rs.getString("SLOT2"))));
                    out.write(Const.NEWLINE);
                }
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
     * 수신 확인 로그 다운로드
     *
     * @param ecareNo
     * @return
     */
    public void makeCsvNormalReceiptLog(OutputStream out, int ecareNo, String reportDt, String resultSeq, String serviceType, String subType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
        Locale locale = localeChangeInterceptor.getLocale();

        String customerId = msAccessor.getMessage("report.ecare.customer.id", locale);
        String customerNm = msAccessor.getMessage("report.ecare.customer.name", locale);
        String customerEmail = msAccessor.getMessage("report.ecare.customer.email", locale);
        String openDate = msAccessor.getMessage("report.ecare.customer.open.date", locale);
        String openCount = msAccessor.getMessage("report.ecare.customer.open.count", locale);
        String write5 = msAccessor.getMessage("report.ecare.title.error.write5", locale);
        serviceType = serviceType.toUpperCase();
        subType = subType.toUpperCase();

        try {
            conn = getConnection();

            StringBuffer sql = new StringBuffer();
            //SECUDB UNIT
            //NVECARESENDLOG : CUSTOMER_EMAIL, CUSTOMER_NM, MESSAGE_KEY
            sql.append("\n SELECT OPEN_DATE,                                                         ");
            sql.append("\n        SL.CUSTOMER_KEY,                                                   ");
            sql.append("\n        SL.CUSTOMER_NM,                                                    ");
            sql.append("\n        SL.CUSTOMER_EMAIL,                                                 ");
            sql.append("\n        OPEN_CNT                                                           ");
            sql.append("\n   FROM NVECARESENDLOG SL, (SELECT ECARE_NO,                                 ");
            sql.append("\n                              RESULT_SEQ,                                  ");
            sql.append("\n                              LIST_SEQ,                                    ");
            sql.append("\n                              RECORD_SEQ,                                  ");
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1)
                sql.append("\n                              MIN(OPEN_DT || ' ' || OPEN_TM) AS OPEN_DATE, ");
            else
                sql.append("\n                              MIN(OPEN_DT + ' ' + OPEN_TM) AS OPEN_DATE, ");
            //SECUDB NONE
            //NVECARERECEIPT : 해당 필드 없음
            sql.append("\n                              COUNT(ECARE_NO) AS OPEN_CNT               ");
            sql.append("\n                         FROM NVECARERECEIPT                                    ");
            sql.append("\n                        WHERE ECARE_NO = ?                              ");
            sql.append("\n                           GROUP BY ECARE_NO,                              ");
            sql.append("\n                              RESULT_SEQ,                                  ");
            sql.append("\n                              LIST_SEQ,                                    ");
            sql.append("\n                              RECORD_SEQ ) RE, NVECARESENDRESULT SR        ");
            sql.append("\n  WHERE SL.ECARE_NO = ?                                                 ");
            sql.append("\n    AND SL.ECARE_NO = RE.ECARE_NO                                    ");
            sql.append("\n    AND SL.ECARE_NO = SR.ECARE_NO                                    ");
            sql.append("\n    AND SL.RESULT_SEQ = RE.RESULT_SEQ                                      ");
            sql.append("\n    AND SL.RESULT_SEQ = SR.RESULT_SEQ                                      ");
            if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                sql.append("\n    AND SR.RESULT_SEQ = ?                                                      ");
            } else {
                sql.append("\n    AND SR.START_DT = ?                                                      ");
            }
            sql.append("\n    AND SL.LIST_SEQ = RE.LIST_SEQ                                          ");
            sql.append("\n    AND SL.RECORD_SEQ = RE.RECORD_SEQ                                      ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, ecareNo);
            pstmt.setInt(2, ecareNo);
            if(serviceType.indexOf("S") > -1 && subType.indexOf("S") > -1) {
                pstmt.setString(3, resultSeq);
            } else {
                pstmt.setString(3, reportDt);
            }
            rs = pstmt.executeQuery();
            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            out.write(printObjectToBytes(openDate + "\t" + customerId + "\t" + customerNm + "\t" + customerEmail + "\t" + openCount + "\n"));

            int rowCnt = 0;

            while(rs.next()) {
                out.write(printObjectToBytes(rs.getString("OPEN_DATE")));
                out.write(Const.TAB);
                out.write(printObjectToBytes(rs.getString("CUSTOMER_KEY")));
                out.write(Const.TAB);
                String customer_nm = StringCharsetConverter.convert(rs.getString("CUSTOMER_NM"));
                customer_nm = security.securityWithColumn(customer_nm, "CUSTOMER_NM", "DECRYPT");
                out.write(printObjectToBytes(customer_nm));
                out.write(Const.TAB);
                String customer_email = rs.getString("CUSTOMER_EMAIL");
                customer_email = security.securityWithColumn(customer_email, "CUSTOMER_EMAIL", "DECRYPT");
                out.write(printObjectToBytes(customer_email));
                out.write(Const.TAB);
                out.write(printObjectToBytes(rs.getString("OPEN_CNT")));
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

    private byte[] printObjectToBytes(Object value) throws UnsupportedEncodingException {
        return (value == null) ? "".getBytes(Const.UFT16LE) : value.toString().getBytes(Const.UFT16LE);
    }

    /**
     * 알림톡 & 대체채널(sms,lms) 성공/실패 대상자 csv 다운로드
     *
     * @param out
     * @param ecareNo
     * @param reportDt
     * @param resultSeq
     * @param serviceType
     * @param subType
     * @param channelType
     * @param successGbn
     */
    public void makeCsvAlimtalkSendLog(OutputStream out, int ecareNo, String reportDt, String resultSeq, String serviceType, String subType, String channelType, String successGbn) {
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        String smsno = msAccessor.getMessage("report.ecare.customer.smsno", localeChangeInterceptor.getLocale());
        String sendDate = msAccessor.getMessage("report.ecare.title.send.date", localeChangeInterceptor.getLocale());
        String errorCd = msAccessor.getMessage("report.ecare.title.error.cd", localeChangeInterceptor.getLocale());
        String errorVal = msAccessor.getMessage("report.ecare.title.error.msg", localeChangeInterceptor.getLocale());
        String write5 = msAccessor.getMessage("report.ecare.title.error.write5", localeChangeInterceptor.getLocale());

        try {
            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            if(successGbn.equals("Y")) {
                out.write(printObjectToBytes(smsno + "\t" + sendDate + "\n"));
            } else {
                out.write(printObjectToBytes(smsno + "\t" + sendDate + "\t" + errorCd + "\t" + errorVal + "\n"));
            }

            List<CaseInsensitiveMap> alimtalkDataList = mzSendLogDao.selectAlimtalkCsvDataList(ecareNo, reportDt, channelType, successGbn);

            int rowCnt = 0;
            for(int cnt = 0; cnt < alimtalkDataList.size(); cnt++) {
                CaseInsensitiveMap rsMap = (CaseInsensitiveMap) alimtalkDataList.get(cnt);
                String phoneNo = StringUtil.defaultIfEmpty((String) rsMap.get("PHONE_NUM"), "00000000000");
                if (!phoneNo.equals("00000000000")) {
                    phoneNo = security.securityWithColumn(phoneNo, "PHONE_NUM", "DECRYPT");
                }
                if(phoneNo.length() == 10) {
                    phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 6) + "-" + phoneNo.substring(6);
                } else {
                    phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 7) + "-" + phoneNo.substring(7);
                }

                out.write(printObjectToBytes(phoneNo));
                out.write(Const.TAB);
                out.write(printObjectToBytes(rsMap.get("SND_DTM")));
                if(successGbn.equals("N")) {
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rsMap.get("RSLT_CD")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rsMap.get("ERROR_DESC")));
                }
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
        }
    }

    /**
     * 친구톡 & 대체채널(sms,lms) 성공/실패 대상자 csv 다운로드
     *
     * @param out
     * @param ecareNo
     * @param reportDt
     * @param resultSeq
     * @param serviceType
     * @param subType
     * @param channelType
     * @param successGbn
     */
    public void makeCsvFriendtalkSendLog(OutputStream out, int ecareNo, String reportDt, String resultSeq, String serviceType, String subType, String channelType, String successGbn) {
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        String smsno = msAccessor.getMessage("report.ecare.customer.smsno", localeChangeInterceptor.getLocale());
        String sendDate = msAccessor.getMessage("report.ecare.title.send.date", localeChangeInterceptor.getLocale());
        String errorCd = msAccessor.getMessage("report.ecare.title.error.cd", localeChangeInterceptor.getLocale());
        String errorVal = msAccessor.getMessage("report.ecare.title.error.msg", localeChangeInterceptor.getLocale());
        String write5 = msAccessor.getMessage("report.ecare.title.error.write5", localeChangeInterceptor.getLocale());

        try {
            out.write(0xFF); // UTF-16LE BOM
            out.write(0xFE);
            if(successGbn.equals("Y")) {
                out.write(printObjectToBytes(smsno + "\t" + sendDate + "\n"));
            } else {
                out.write(printObjectToBytes(smsno + "\t" + sendDate + "\t" + errorCd + "\t" + errorVal + "\n"));
            }

            List<CaseInsensitiveMap> friendtalkDataList = mzFtSendLogDao.selectFriendtalkCsvDataList(ecareNo, reportDt, channelType, successGbn);

            int rowCnt = 0;
            for(int cnt = 0; cnt < friendtalkDataList.size(); cnt++) {
                CaseInsensitiveMap rsMap = (CaseInsensitiveMap) friendtalkDataList.get(cnt);
                String phoneNo = StringUtil.defaultIfEmpty((String) rsMap.get("PHONE_NUM"), "00000000000");
                if (!phoneNo.equals("00000000000")) {
                    phoneNo = security.securityWithColumn(phoneNo, "PHONE_NUM", "DECRYPT");
                }
                if(phoneNo.length() == 10) {
                    phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 6) + "-" + phoneNo.substring(6);
                } else {
                    phoneNo = phoneNo.substring(0, 3) + "-" + phoneNo.substring(3, 7) + "-" + phoneNo.substring(7);
                }

                out.write(printObjectToBytes(phoneNo));
                out.write(Const.TAB);
                out.write(printObjectToBytes(rsMap.get("SND_DTM")));
                if(successGbn.equals("N")) {
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rsMap.get("RSLT_CD")));
                    out.write(Const.TAB);
                    out.write(printObjectToBytes(rsMap.get("ERROR_DESC")));
                }
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
        }
    }

    /**
     * 알림톡 & 대체채널(sms,lms) 발송결과
     *
     * @param scenarioInfoVo
     * @return
     */
    public RptSendResultVo selectAlimtalkSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return mzSendLogDao.selectAlimtalkSendResult(scenarioInfoVo);
    }

    /**
     * 2018.03 wiseMoka 병합 친구톡 & 대체채널(sms,lms) 발송결과
     *
     * @param scenarioInfoVo
     * @return
     */
    public RptSendResultVo selectFriendtalkSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return mzFtSendLogDao.selectFriendtalkSendResult(scenarioInfoVo);
    }

    /**
     * 2020.06 브랜드톡 TODO: 알림,친구톡도사용
     *
     * @param scenarioInfoVo
     * @return
     */
    public RptSendResultVo selectKakaoSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return kakaoRptSendResultDao.selectKakaoSendResult(scenarioInfoVo);
    }

}
