package com.mnwise.wiseu.web.channel.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.campaign.dao.ReceiptDao;
import com.mnwise.wiseu.web.campaign.dao.SendLogDao;
import com.mnwise.wiseu.web.channel.dao.PushAppDao;
import com.mnwise.wiseu.web.channel.dao.PushServiceInfoDao;
import com.mnwise.wiseu.web.common.dao.CdMstDao;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.common.model.CdMstVo;
import com.mnwise.wiseu.web.common.util.DBConnectionUtil;
import com.mnwise.wiseu.web.common.util.StringCharsetConverter;
import com.mnwise.wiseu.web.ecare.dao.EcareReceiptDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendLogDao;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.resend.model.LstResendVo;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;

import net.sf.json.JSONObject;

/**
 * PUSH 서비스 인터페이스 구현
 */
@Service
public class PushService extends BaseService implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(PushService.class);

    @Autowired private CdMstDao cdMstDao;
    @Autowired private EcareReceiptDao ecareReceiptDao;
    @Autowired private EcareSendLogDao ecareSendLogDao;
    @Autowired private PushAppDao pushAppDao;
    @Autowired private PushServiceInfoDao pushServiceInfoDao;
    @Autowired private ReceiptDao receiptDao;
    @Autowired private SendLogDao sendLogDao;
    @Autowired private ServerInfoDao serverInfoDao;
    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private JdbcTemplate jdbcTemplate;

    private ApplicationContext ctx;

    @Value("${push.db.getMessageQuery:''}")
    private String getMessageQuery;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    /**
     * workType 요청 값(insert, update, delete) 에 따라서 기능을 수행 후 반영된 데이터의 수를 반환.
     * @param workType insert, update, delete
     * @param pushAppId 앱 아이디
     * @param pushAppNm 앱 이름
     * @param userId 작성자 정보
     * @return 변경된 데이터 수
     * @throws Exception
     */
    public int updatePushApp(Map<String, Object> param) throws Exception {
        int result = -1;
        if("insert".equalsIgnoreCase((String)param.get("updateType"))) {
            result = pushAppDao.insertPushApp(param);
        } else if("update".equalsIgnoreCase((String)param.get("updateType"))) {
            result = pushAppDao.updatePushApp(param);
        }
        return result;
    }

    /**
     * PUSH APP list
     * @param useOnly Y:미사용 데이터 표시 안함. N:미사용 데이터 표시
     * @return 앱 리스트
     */
    public List<CaseInsensitiveMap> selectPushAppList(String useOnly) {
        return pushAppDao.selectPushAppList("Y".equalsIgnoreCase(useOnly));
    }

    /**
     * PUSH SERVICE 정보.
     *
     * @param svcType
     * @param svcNo
     * @return PUSH
     * @throws Exception
     * @see PushDao.selectPushServiceInfo
     */
    public CaseInsensitiveMap getPushServiceInfo(String svcType, int svcNo) throws Exception {
        return pushServiceInfoDao.selectPushServiceInfo(svcType, svcNo);
    }

    /**
     * PUSH SERVICE 정보 변경.
     *
     * @param svcType
     * @param svcNo
     * @param pushAppId
     * @param msgType
     * @param menuId
     * @param popUse
     * @param imgUrl
     * @param popBigImgUse
     * @param bigImgUrl
     * @param webUrl
     * @return
     * @throws Exception
     */
    public int updatePushServiceInfo(String svcType, int svcNo, String pushAppId, String msgType, String menuId, String popImgUse, String imgUrl, String popBigImgUse, String bigImgUrl, String webUrl) throws Exception {
        CaseInsensitiveMap pushServiceInfoMap = pushServiceInfoDao.selectPushServiceInfo(svcType, svcNo);
        if(null==pushServiceInfoMap || 0==pushServiceInfoMap.size()) {
            return pushServiceInfoDao.insertPushServiceInfo(svcType, svcNo, pushAppId, msgType, menuId, popImgUse, imgUrl, popBigImgUse, bigImgUrl, webUrl);
        }
        else {
            return pushServiceInfoDao.updatePushServiceInfo( svcType, svcNo, pushAppId, msgType, menuId, popImgUse, imgUrl, popBigImgUse, bigImgUrl, webUrl);
        }
    }

    /**
     * 캠페인 리포트 요약보기 수신확인 건수 데이터
     * @param campaignNo
     * @return
     * @throws Exception
     */
    public CaseInsensitiveMap selectPushReceiptCount(int campaignNo) throws Exception {
        return receiptDao.selectPushReceiptCount(campaignNo);
    }

    /**
     * 캠페인 리포트 PUSH OS별 발송 정보
     * @param campaignNo
     * @return
     */
    public CaseInsensitiveMap selectPushSendlogCount(int campaignNo) {
        return sendLogDao.selectPushSendlogCount(campaignNo);
    }

    /**
     * PUSH 사용 코드 정보
     * @param treeElement
     * @return
     */
    public List<CaseInsensitiveMap> selectCodeGroup(String parCdCat) {
        return cdMstDao.selectCodeGroup(parCdCat);
    }

    /**
     * CODE LIST
     * @param param
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectCodeList(String parCdCat) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        // 1. get request
        // 2. set default result data
        resultMap.put("flag", -1);

        // 3. vaildate
        // 4. get data
        List<CdMstVo> codeList = cdMstDao.selectCodeList(parCdCat);
        if(null==codeList) {}
        else if(codeList.size() > 0) {
            resultMap.put("flag", 1);
            resultMap.put("codeList", codeList);
        } else {
            resultMap.put("flag", 0);
        }
        return resultMap;
    }

    /**
     * PUSH 사용 코드를 JSON 형태로 반환
     * @param groupList
     * @param text
     * @return
     * @throws Exception
     */
    public JSONObject[] getCodeJSON(List<CaseInsensitiveMap> groupList, String text) throws Exception {
        // source
        JSONObject[] jo = new JSONObject[groupList.size()];
        if(null==text) {
            List<JSONObject> childrenElement = new ArrayList<>();
            for(int i = 0; i < groupList.size(); i++) {
                CaseInsensitiveMap element = groupList.get(i);
                jo[i] = createJson(element);

                List<CaseInsensitiveMap> children = this.selectCodeGroup((String)element.get("CD_CAT"));
                for(int j = 0; j < children.size(); j++) {
                    childrenElement.add(createJson(children.get(j)));
                }
                jo[i].put("children", childrenElement);
            }
        } else {
            for(int i = 0; i < jo.length; i++) {
                jo[i] = createJson(groupList.get(i));
            }
        }
        return jo;
    }


    private JSONObject createJson(CaseInsensitiveMap element) {
        JSONObject jo = new JSONObject();
        String cd = (String) element.get("CD");
        String cd_cat = (String) element.get("CD_CAT");
        jo.put("text", createText(element));
        jo.put("id", cd);
        if("AP00".equals(cd_cat)) {
            jo.put("expanded", new Boolean(true));
        }
        return jo;
    }

    private String createText(Map<String, Object> element) {
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"javascript:void(0);\" onClick=\"choice('");
        sb.append(StringUtil.trimToEmpty((String) element.get("CD"))).append("', '");
        sb.append(StringUtil.trimToEmpty((String) element.get("CD_CAT"))).append("', '");
        sb.append(StringUtil.trimToEmpty((String) element.get("PAR_CD_CAT"))).append("', '");
        sb.append(element.get("VAL") == null ? "" : StringUtil.trimToEmpty((String) element.get("VAL"))).append("', '");
        sb.append(element.get("CD_DESC") == null ? "" : StringUtil.trimToEmpty((String) element.get("CD_DESC"))).append("', '");
        sb.append(element.get("USE_YN") == null ? "" : StringUtil.trimToEmpty((String) element.get("USE_YN"))).append("'");
        sb.append(")\">");
        sb.append(StringUtil.trimToEmpty((String) element.get("VAL"))).append("</a>");

        return sb.toString();
    }

    /**
     * PUSH 메시지 정보를 반환.
     * @param dbSeq
     * @param lstResendVo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getPushMsg(String dbSeq, LstResendVo lstResendVo) throws Exception {
        // 환경설정에 있는 쿼리를 활용하여 데이터를 가져온다.
        if(null==jdbcTemplate) {
            DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(Integer.parseInt(dbSeq));
            DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo, ctx);
            this.jdbcTemplate = dbConnectionUtil.getJdbcTemplate();
        }

        // 결과가 없을때 에러가 발생하기 때문에 catch문을 사용해야 함.
        try {
            return this.jdbcTemplate.queryForMap(this.getMessageQuery, new Object[] { lstResendVo.getSeq() }, new int[] { java.sql.Types.VARCHAR });
        } catch(Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * PUSH ERROR 코드 CSV 다운로드
     *
     * @param outputStream
     * @param scenarioNo
     * @param campaignNo
     * @param errorCd
     * @param language
     * @param resultSeq
     */
    public void makeCsvCampaignPushErrorCodeDetailList(ServletOutputStream out, int scenarioNo, int campaignNo, String errorCd, String language, long resultSeq) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuffer query = new StringBuffer();
            String database = conn.getMetaData().getDatabaseProductName().toUpperCase();
            query.append("  SELECT CUSTOMER_NM, CUSTOMER_EMAIL, ERROR_CD, VAL ,SLOT1, SLOT2       \n");
            if(database.indexOf("ORACLE") > -1 || database.indexOf("DB2") > -1) {
                query.append("       ,  SEND_DT ||' '|| SEND_TM AS SEND_DATE \n");
            } else {
                query.append("       ,  SEND_DT +' '+ SEND_TM AS SEND_DATE \n");
            }
            //SECUDB UNIT
            // NVSENDLOG : CUSTOMER_NM, CUSTOMER_EMAIL, MESSAGE_KEY
            query.append("  FROM (SELECT CUSTOMER_NM, CUSTOMER_EMAIL, ERROR_CD , SEND_DT, SEND_TM, SLOT1,SLOT2, RESULT_SEQ FROM NVSENDLOG  WHERE CAMPAIGN_NO = ? AND ERROR_CD = ?) A,   \n");
            query.append("       (SELECT CD,  VAL, CD_CAT FROM NV_CD_MST  WHERE CD_CAT LIKE 'AP%' AND CD NOT IN ('0')) B   \n");
            query.append("  WHERE A.ERROR_CD = B.CD \n");

            if(resultSeq != 0) {
                query.append("  AND A.RESULT_SEQ = ? \n");
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
            String faxno = msAccessor.getMessage("report.ecare.customer.smsno", locale);
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
                out.write(("'" + StringUtil.defaultString(rs.getString("ERROR_CD"))).getBytes(Const.UFT16LE));
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
     * PUSH ERROR 코드 리스트 - ecare
     * @param ecareNo
     * @param resultSeq
     * @param sendDt
     * @param language
     * @return
     * @throws Exception
     */
    public List<DomainLogVo> selectEcarePushErrorReportList(int ecareNo, String resultSeq, String sendDt, String language) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecare_no", ecareNo);
        paramMap.put("result_seq", resultSeq);
        paramMap.put("lang", language);
        paramMap.put("send_dt", sendDt);
        return ecareSendLogDao.selectEcarePushErrorReportList(paramMap);
    }

    /**
     * 이케어 리포트 결과건수
     * @param ecareNo
     * @param resultSeq
     * @param startDt
     * @param endDt
     * @throws Exception
     */
    public Map<String, Object> getEcareResultCount(int ecareNo, String resultSeq, String startDt, String endDt) throws Exception {
        int openCnt=0, succCnt=0, sendCnt=0;
        Map<String, Object> resultMap = new HashMap<>();
        CaseInsensitiveMap receiptCountMap = ecareReceiptDao.selectPushEcareReceiptCount(ecareNo, resultSeq, startDt, endDt);
        CaseInsensitiveMap sendlogCountMap = ecareSendLogDao.selectPushEcareSendlogCount(ecareNo, resultSeq, startDt, endDt);
        if(null!=receiptCountMap) {
            openCnt += Integer.parseInt(String.valueOf(receiptCountMap.get("IOS_RECEIPT"))) + Integer.parseInt(String.valueOf(receiptCountMap.get("AND_RECEIPT")));
        }
        if(null!=sendlogCountMap) {
            sendCnt += Integer.parseInt(String.valueOf(sendlogCountMap.get("SEND_CNT")));
            succCnt += Integer.parseInt(String.valueOf(sendlogCountMap.get("SUCC_CNT")));
        }

        // 결과 반환
        resultMap.put("openCnt", openCnt);
        resultMap.put("succCnt", succCnt);
        resultMap.put("sendCnt", sendCnt);
        resultMap.put("receiptCountMap", receiptCountMap);
        resultMap.put("sendlogCountMap", sendlogCountMap);
        return resultMap;
    }

    /**
     * 재발송 에러코드 반환(재발송에서 제외)
     * @return
     */
    public String getResendErrorCd() {
        return serverInfoDao.getResendErrorCd();
    }

    /**
     * PUSH MSG_TYPE (메시지유형) 목록
     * @param useOnly
     * @return
     * @throws Exception
     */
    public List<CaseInsensitiveMap> selectPushMsgTypeList(boolean useOnly) throws Exception {
        return cdMstDao.selectPushMsgTypeList(useOnly);
    }

    /**
     * MSG_TYPE 설정
     * @param workType
     * @param cd
     * @param cdDesc
     * @param lang
     * @return
     * @throws Exception
     */
    public int updatePushMsgType(Map<String,Object> param) throws Exception {
        int result = -1;
        if("insert".equalsIgnoreCase((String)param.get("updateType"))) {
            result = cdMstDao.insertPushMsgType(param);
        } else if("update".equalsIgnoreCase((String)param.get("updateType"))) {
            result = cdMstDao.updatePushMsgType(param);
        }
        return result;
    }
}
