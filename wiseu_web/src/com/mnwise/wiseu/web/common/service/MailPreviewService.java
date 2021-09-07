package com.mnwise.wiseu.web.common.service;

import static com.mnwise.common.util.StringUtil.CRLF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.ASE.pscript.ASEConsole;
import com.mnwise.ASE.pscript.CompileFrontEnd;
import com.mnwise.ASE.pscript.Message;
import com.mnwise.ASE.pscript.PCode;
import com.mnwise.ASE.pscript.PscriptVM;
import com.mnwise.ASE.pscript.ScrProperties;
import com.mnwise.common.io.IOUtil;
import com.mnwise.common.jeonmun.JsonJeonmun;
import com.mnwise.common.security.aria.Aria;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.common.util.Util;
import com.mnwise.mts.Config;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.Const.Client;
import com.mnwise.wiseu.web.base.util.TcpipClient;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.SendLogDao;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.mockup.ScrPropertiesMockup;
import com.mnwise.wiseu.web.common.model.MailPreviewVo;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import com.mnwise.wiseu.web.common.model.TargetQueryInfo;
import com.mnwise.wiseu.web.common.util.DBConnectionUtil;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.common.util.ReadingMime;
import com.mnwise.wiseu.web.common.util.SecurityUtil;
import com.mnwise.wiseu.web.ecare.dao.AddQueryDao;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareMultipartFileDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendLogDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.RealtimeAcceptDao;
import com.mnwise.wiseu.web.ecare.dao.RealtimeAcceptDataDao;
import com.mnwise.wiseu.web.ecare.dao.ScheduleAcceptDao;
import com.mnwise.wiseu.web.ecare.model.AddQueryVo;
import com.mnwise.wiseu.web.ecare.model.RealtimeAcceptDataVo;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

/**
 * 메일 미리보기 서비스
 *
 * - 20100605 : _LOG context에 값넣도록 추가하고 로그폴더 생성도 하도록 추가
 * - 20100625 : /spool/preview/fax 가 없으면 생성하도록 설정
 * - 20100701 : 보안메일의 경우에 TEMPLATE테이블에 cover, preface 등이 들어갈수 있으므로 ECARE의 경우에 TEMPLATE 테이블을 한번더 가져옴.
 * - 20100824 : 미리보기 시 대상자 쿼리를 불러올 때 DB별로 코덱정보가 다르므로 기존 APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져오도록 수정
 */
@Service
public class MailPreviewService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(MailPreviewService.class);

    @Autowired private AddQueryDao addQueryDao;
    @Autowired private CampaignDao campaignDao;
    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private EcareDao ecareDao;
    @Autowired private EcareMultipartFileDao ecareMultipartFileDao;
    @Autowired private EcareSendLogDao ecareSendLogDao;
    @Autowired private EcareTemplateDao ecareTemplateDao;
    @Autowired private EcareTemplateHistoryDao ecareTemplateHistoryDao;
    @Autowired private EcMsgHandlerHistoryDao ecMsgHandlerHistoryDao;
    @Autowired private RealtimeAcceptDao realtimeAcceptDao;
    @Autowired private RealtimeAcceptDataDao realtimeAcceptDataDao;
    @Autowired private ScheduleAcceptDao scheduleAcceptDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private SendLogDao sendLogDao;
    @Autowired private TemplateDao templateDao;

    @Value("${conf.preview}")
    private String previewConf;
    @Value("${lts.ip}")
    private String ltsIp;
    @Value("${lts.resend.port}")
    private String ltsResendPort;
    @Value("${lts.port}")
    private String ltsPort;
    @Value("${use.resend.button}")
    private String useResendButton;

    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private static boolean isLoad = false;

    public void setJdbcPassword(String jdbcPassword) {
        if("on".equals(PropertyUtil.getProperty("cipher","off").toLowerCase())) {
            try {
                this.jdbcPassword = Aria.wiseuDecrypt(jdbcPassword);
            } catch(Exception e) {
                log.error(null, e);
            }
        } else {
            this.jdbcPassword = jdbcPassword;
        }
    }

    public static boolean isLoad() {
        return isLoad;
    }

    private void contextPrint(ScrProperties contexts) {
        contexts.dump();
        @SuppressWarnings("unchecked")
        Enumeration<String> enu = contexts.keys();
        while(enu.hasMoreElements()) {
            String name = enu.nextElement();
            if(!name.equalsIgnoreCase("_TEMPLATE") && !name.equalsIgnoreCase("_HANDLER"))
                log.info("name:" + name + " value:" + contexts.get(name));
        }
    }

    /**
     * 미리보기용 데이터를 가져온다. (메일은 mime, FAX는 html)
     *
     * - 20100712 : 미리보기시에 오류날경우 스크립트 라인 나오도록 변경
     * - 20100909 : eCare 실시간 미리보기 기능 추가
     *
     * @param aid
     * @param customerkey
     * @param serviceType
     * @param subType
     * @param sendDate
     * @param seg
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Hashtable makePreview(int aid, String customerkey, String serviceType, String subType, String sendDate, String seg) throws Exception {
        return makePreview(aid, customerkey, "", serviceType, subType, sendDate);
    }

    /**
     * - 20110513 : 이케어 고객이력조회 결과보기 추가
     * - 20110513 : 이케어 고객이력조회 결과보기 재발송 기능 추가(결과보기와 재발송버튼을 눌렀을 때 같은 로직임) 결과보기 및 재발송(스케쥴,준실시간 자동생성) - 메일 중 스케쥴,준실시간만 지원
     *
     * @param aid
     * @param serviceType
     * @param condition
     * @return
     * @throws Exception
     */
    public Hashtable<String, Object> makePreview(int aid, String serviceType, String condition, String customerKey, String slot1, String slot2, boolean isResendHistory) throws Exception {
        init(previewConf);
        dbinit();
        File dir = new File(Config.getMTSSpool());
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/fax");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getLog());
        if(!dir.exists())
            dir.mkdirs();

        Hashtable<String, Object> table = new Hashtable<>();
        ScrProperties contexts = new ScrProperties();
        String fileName = null;

        BufferedReader in = null;
        try {
            log.info("===============================================================");
            log.info(" Preview Mail Make Start..");
            log.info("aid : " + aid + ", serviceType : " + serviceType + ", condition : " + condition);
            contexts = TargetInfo(aid, serviceType, condition, customerKey, slot1, slot2, isResendHistory);
            log.info("===============================================================");
            // 채널 타입을 넘겨준다.
            table.put("channelType", contexts.get("_CHANNEL_TYPE"));

            boolean isNormal = executeHandler(contexts);

            if(isNormal) {
                if("F".equals(contexts.get("_CHANNEL_TYPE"))) {
                    fileName = Config.getServerRoot() + "/spool/preview/fax/" + contexts.get("_CREATE_FILENAME").toString() + ".html";

                    in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                    String temp = null;
                    StringBuilder str = new StringBuilder();

                    while((temp = in.readLine()) != null) {
                        str.append(temp.toString() + CRLF);
                    }
                    table.put("content", str.toString());
                } else {
                    fileName = Config.getServerRoot() + "/spool/preview/" + contexts.get("_CREATE_FILENAME").toString() + ".mime";

                    ReadingMime parse = new ReadingMime(fileName, null);

                    parse.mimeReading();

                    parse.mimeParse();

                    table.put("content", parse.getContent());
                    table.put("attachFile", parse.getAttachFiles());
                    table.put("to", parse.getTo());
                    table.put("from", parse.getFrom());
                    table.put("subject", parse.getSubject());
                    table.put("customerKey", contexts.get("_CUSTOMER_NIC"));
                    table.put("customerNm", contexts.get("_CUSTOMER_NM"));
                    table.put("customerEmail", contexts.get("_CUSTOMER_EMAIL"));
                    table.put("resendEcareNo", contexts.get("_RESEND_ECARE_NO"));
                    table.put("header", contexts.get("_HEADER"));
                    table.put("target", contexts.get("_TARGET"));
                    table.put("serviceType", contexts.get("_SERVICE_TYPE"));
                    table.put("subType", contexts.get("_SUB_TYPE"));
                    table.put("isJeonmun", contexts.get("_JEONMUN"));
                    table.put("condition", contexts.get("_CONDITION"));
                    table.put("useResendButton", useResendButton);
                }

                File f = new File(fileName);
                if(f.delete()) {
                    log.info(fileName + "삭제 완료");
                } else {
                    log.warn(fileName + "삭제 실패");
                }
            } else {
                table.put("to", "");
                table.put("from", "");
                table.put("content", contexts.get("_EXCEPTION_MESSAGE"));
            }

            log.info(" Preview Mail Make Completed!!");
            log.info("===============================================================");
        } catch(Exception e) {
            log.error("Exception occurred. " + e.getMessage());
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }

        return table;
    }

    public Hashtable<String, Object> makePreview(int aid, String customerkey, String realtimeArg, String serviceType, String subType, String sendDate, String seg) throws Exception {
        init(previewConf);
        dbinit();
        File dir = new File(Config.getMTSSpool());
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/fax");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getLog());
        if(!dir.exists())
            dir.mkdirs();

        Hashtable<String, Object> table = new Hashtable<>();
        ScrProperties contexts = new ScrProperties();
        String fileName = null;

        BufferedReader in = null;
        try {
            log.info("===============================================================");
            log.info(" Preview Mail Make Start..");
            log.info("aid : " + aid + ", customerkey : " + customerkey + ", serviceType : " + serviceType + ", sendDate : " + sendDate + ", seg : " + seg + ", realtimeArg : " + realtimeArg);
            contexts = TargetInfo(aid, customerkey, realtimeArg, serviceType, subType, sendDate, seg);
            log.info("===============================================================");
            // 채널 타입을 넘겨준다.
            table.put("channelType", contexts.get("_CHANNEL_TYPE"));

            boolean isNormal = executeHandler(contexts);

            if(isNormal) {
                if("F".equals(contexts.get("_CHANNEL_TYPE"))) {
                    fileName = Config.getServerRoot() + "/spool/preview/fax/" + contexts.get("_CREATE_FILENAME").toString() + ".html";

                    in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                    String temp = null;
                    StringBuilder str = new StringBuilder();

                    while((temp = in.readLine()) != null) {
                        str.append(temp.toString() + CRLF);
                    }
                    String content = str.toString();

                    table.put("content", content);

                } else {
                    fileName = Config.getServerRoot() + "/spool/preview/" + contexts.get("_CREATE_FILENAME").toString() + ".mime";

                    ReadingMime parse = new ReadingMime(fileName, null);

                    parse.mimeReading();

                    parse.mimeParse();

                    table.put("content", parse.getContent());
                    table.put("attachFile", parse.getAttachFiles());
                    table.put("to", parse.getTo());
                    table.put("from", parse.getFrom());
                    table.put("subject", parse.getSubject());
                }

                File f = new File(fileName);
                if(f.delete()) {
                    log.info(fileName + "삭제 완료");
                } else {
                    log.warn(fileName + "삭제 실패");
                }
            } else {
                table.put("to", "");
                table.put("from", "");
                table.put("content", contexts.get("_EXCEPTION_MESSAGE"));
            }

            log.info(" Preview Mail Make Completed!!");
            log.info("===============================================================");
        } catch(Exception e) {
            log.error("Exception occurred. " + e.getMessage());
            throw e;
        } finally {
            deleteSpoolFiles(contexts);
            IOUtil.closeQuietly(in);
        }

        return table;
    }

    /* 미리보기 시 생성한 Spool 파일 삭제 */
    private void deleteSpoolFiles(ScrProperties contexts) {
        final String tid = (String) contexts.get("_TID");
        if(StringUtil.isNotEmpty(tid)) {
            File spoolDir = new File(Config.getMTSSpool());
            spoolDir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    if(file.isFile() && file.getName().matches("." + tid + ".*")) {
                        file.delete();
                    }
                    return false;
                }
            });
        }
    }

    public ScrProperties TargetInfo(int aid, String customerkey, String realtimeArg, String serviceType, String subType, String sendDate, String seg) {
        String handler = null;
        String handlerType = null;

        ScrProperties contexts = new ScrProperties();
        contexts.put("_HANDLER_ID", "1");
        contexts.put("_SERVER_ROOT", Config.getServerRoot());
        contexts.put("_LOG", Config.getLog());
        contexts.put("_BUFFERING_SIZE", "1000");
        contexts.put("_MAX_USERS_PER_DOMAIN", "1");
        contexts.put("_jdbc.driver", jdbcDriver);
        contexts.put("_jdbc.url", jdbcUrl);
        contexts.put("_jdbc.user", jdbcUser);
        contexts.put("_jdbc.password", jdbcPassword);
        contexts.put("_jdbc.encoding", "");

        log.info("serviceType:" + serviceType);
        log.info("seg:" + seg);
        MailPreviewVo previewVo = selectServicePreview(serviceType, aid);
        handler = StringUtil.defaultString(previewVo.getAppsource()).trim();
        handlerType = previewVo.getHandlerType();
        contexts.put("_HANDLER", handler); // 핸들러
        contexts.put("_HANDLER_TYPE", handlerType); // 핸들러
        contexts.put("_CAMPAIGN_NAME", previewVo.getServiceNm());
        contexts.put("_SUB_TYPE", subType);

        // 20100701 hongjun 보안메일의 경우에 TEMPLATE 테이블에 seg컬럼에 cover 명칭으로 들어가는
        // 템플릿등이 있는데 이를 핸들러에서 사용하기 위해서는 context에 담아줘야한다.
        List<MailPreviewVo> coverTemplate = selectTemplate(serviceType, aid, seg);
        log.info("template length:" + coverTemplate.size());
        for(int i = 0; i < coverTemplate.size(); i++) {
            MailPreviewVo mpv = coverTemplate.get(i);
            log.info("seg" + i + ":" + mpv.getSeg());
            if(mpv.getSeg().trim().equals("")) {
                contexts.put("_TEMPLATE", mpv.getTemplate());
            } else {
                contexts.put("_TEMPLATE_" + mpv.getSeg().trim(), mpv.getTemplate());
            }
        }

        // spool 멀티 템플릿용
        contexts.put("_SPOOL", Config.getSpool());

        contexts.put("_SEND_MODE", "R"); // Real or Test
        String serviceTypeString = "SCHEDULE";
        if("R".equals(previewVo.getServiceType())) {
            serviceTypeString = "REALTIME";
        }

        DecimalFormat df = new DecimalFormat("000000000000000");
        contexts.put("_CAMPAIGN_NO", df.format(new Integer(aid)));

        contexts.put("_TYPE", serviceTypeString);
        contexts.put("_CLIENT", serviceType.toLowerCase()); // em or ec
        contexts.put("_JOB_CLASS", previewVo.getClass());
        contexts.put("_MESSAGE_THREAD_NUM", "1");
        contexts.put("_JOB_CLASS", "I"); // 정보성(I),상업성(A)
        contexts.put("_STATUS", "W");
        contexts.put("_TOTAL_COUNT", "1");
        contexts.put("_MERGE_MAKE_DONE", "N");
        contexts.put("_PREVIEW_YN", "Y");
        contexts.put("_LIST_SEQ", "1");

        contexts.put("_CHANNEL_TYPE", previewVo.getChannelType());
        contexts.put("_RETMAIL_RECEIVER", previewVo.getRetmailReceiver() == null ? "" : previewVo.getRetmailReceiver());
        contexts.put("_SENDER_NM", previewVo.getSenderNm() == null ? "" : previewVo.getSenderNm());
        contexts.put("_SENDER_EMAIL", previewVo.getSenderEmail() == null ? "" : previewVo.getSenderEmail());

        contexts.put("_SENDER_TEL", previewVo.getSenderTel() == null ? "" : previewVo.getSenderTel());
        contexts.put("_RECEIVER_NM", previewVo.getReceiverNm() == null ? "" : previewVo.getReceiverNm());
        contexts.put("_RETRY_CNT", "1");
        contexts.put("_RECE_CON", "N");
        String resultSeq = String.valueOf(Util.getResultSeq());
        contexts.put("_RESULT_SEQ", resultSeq);

        String tid = new StringBuilder(31).append(StringUtil.leftPad(String.valueOf(aid), 15, '0')).append(StringUtil.leftPad(resultSeq, 16, '0')).toString();
        contexts.put("_TID", tid); // 임의 TID 생성
        contexts.put("_CUSTOMER_NIC", customerkey.trim()); // 장홍준 파일생성시 고객키값도

        // 같이 넣는다
        contexts.put("_CREATE_FILENAME", tid + "_" + customerkey.trim());
        contexts.put("_PREVIEW_YN", "Y"); // MAS에서 이 값이 Y일 경우에는 발송을 안하고
        // execPreviewTemplate() 한다.
        contexts.put("_RECOVER_SERVER_SID", "MAS1");
        contexts.put("_REQUEST_TIME", sendDate + "0000");

        // 실시간인 경우 추가
        if("R".equals(previewVo.getServiceType())) {
            String args[] = realtimeArg.split(",");
            for(int cnt = 0; cnt < args.length; cnt++) {
                int index = args[cnt].indexOf("=");
                contexts.put(args[cnt].substring(0, index), args[cnt].substring(index + 1));
            }
        } else if("N".equals(previewVo.getSubType())) {
            // 준실시간인 경우 처리(NVREALTIMEACCEPT 테이블 조회)
            getDefferedTargetDbInfo(contexts, aid, customerkey, previewVo.getSubType(), "");
        } else {
            Integer segmentNo;
            if(isOmniChannelSubCampaign(serviceType, previewVo.getRelationType(), previewVo.getDepthNo())) {
                CampaignVo campaignVo = campaignDao.selectParentCampaignInfo(previewVo.getScenarioNo());
                segmentNo = campaignVo.getSegmentNo();
            } else {
                segmentNo = previewVo.getSegmentNo();
            }

            getTargetDbInfo(contexts, aid, segmentNo, customerkey, previewVo.getSubType());
        }

        // 시작일시,종료일시를 얻는다.
        contexts.put("_START_DATE", sendDate.replaceAll("-", ""));
        contexts.put("_END_DATE", "00000000");

        if("M".equals(previewVo.getChannelType())) {
            contexts.put("_SERVICE_PREFACE", previewVo.getServicePreface() == null ? "" : previewVo.getServicePreface());
        }

        if("EC".equalsIgnoreCase(serviceType)) {
            setAdditionalEcareInfo(contexts, previewVo, aid);
        }

        if(handlerType.equals("G")) {
            groovyHandlerWrite(contexts);
        }

        return contexts;
    }

    /**
     * 템플릿을 가져온다
     *
     * @param serviceType
     * @param serviceNo
     * @param seg
     * @return
     */
    public List<MailPreviewVo> selectTemplate(String serviceType, int serviceNo, String seg) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceNo", serviceNo);
        map.put("seg", seg.equals("기본") || seg.equals("") ? " " : seg);

        if(serviceType.equalsIgnoreCase("EM")) {
            return templateDao.selectCampaignTemplate(map);
        } else {
            return ecareTemplateDao.selectEcareTemplate(map);
        }
    }

    private void groovyHandlerWrite(ScrProperties contexts) {
        String filePath = Config.getMTSSpool() + "/H" + contexts.get("_TID") + ".groovy";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            out.write(((String) contexts.get("_HANDLER")).getBytes());
            out.flush();
        } catch(Exception e) {
            log.error(e.toString(), e);
            return;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }

    /**
     * 대상자 쿼리 및 컬럼 정보를 가져온다
     *
     * - 20100318 : 쿼리를 200건정도만 가져왔더니 아이디를 못찾는경우 에러가 나서 모든 쿼리결과를 가져오도록 변경
     * - 20100824 : 미리보기 시 대상자 쿼리를 불러올 때 DB별로 코덱정보가 다르므로 기존 APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져오도록 수정함
     * @param contexts
     * @param serviceNo
     * @param segmentNo
     * @param customerId
     * @return
     */
    private boolean getTargetDbInfo(ScrProperties contexts, int serviceNo, int segmentNo, String customerId, String subType) {
        List<SemanticVo> semanticList = semanticDao.selectSemanticListBySegmentNo(segmentNo);
        final StringBuilder nmSb = new StringBuilder();
        final StringBuilder descSb = new StringBuilder();

        for(SemanticVo semanticVo : semanticList) {
            // 고객키인 경우
            final String fieldKey = semanticVo.getFieldKey();
            final String fieldSeq = String.valueOf(semanticVo.getFieldSeq());
            if(fieldKey.equals("K")) {
                contexts.put("_KEY_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("E")) {
                // 고객 이메일인 경우
                contexts.put("_EMAIL_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("N")) {
                // 고객명인경우
                contexts.put("_NAME_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("S")) {
                // 고객전화인 경우
                contexts.put("_SMS_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("F")) {
                // FAX 경우
                contexts.put("_FAX_FIELD_SEQ", fieldSeq);
            }

            nmSb.append(semanticVo.getFieldNm()).append("\t");
            descSb.append(semanticVo.getFieldDesc()).append("\t");
        }

        contexts.put("_FIELD_NAMES", nmSb.toString());
        contexts.put("_FIELD_DESCRIPTIONS", descSb.toString());

        // 대상자쿼리 정보 가져오기
        TargetQueryInfo targetQueryInfo = segmentDao.selectTargetQueryInfo(segmentNo);
        StringBuffer sbTargetQuery = new StringBuffer();
        String sTargetQuery = null;
        if(targetQueryInfo.getSqlHead().toUpperCase().indexOf("NVSCHEDULEACCEPT") > -1) {
            return getDefferedTargetDbInfo(contexts, serviceNo, customerId, subType, targetQueryInfo.getSqlHead().toUpperCase());
        } else {

            sTargetQuery = sbTargetQuery.append(" SELECT ")
                .append(" * FROM ( ").append(targetQueryInfo.getSqlHead()) // NVSEGMENT.SQLHEAD
                .append(" ").append(targetQueryInfo.getSqlBody()) // NVSEGMENT.SQLBODY
                .append(" ").append(targetQueryInfo.getSqlTail()) // NVSEGMENT.SQLTAIL
                .append(" ) A ")
                .toString();
        }

        if(sTargetQuery.equals(""))
            return false;

        if(targetQueryInfo.getDbKind().equalsIgnoreCase("SYBASE")) {
            sTargetQuery = sTargetQuery + " at isolation 0";
        }
        sTargetQuery = sTargetQuery.replace('\r', ' ').replace('\n', ' ').trim();

        log.info(" 대상자 조건\n" + sTargetQuery);
        log.info(">> 대상자 쿼리 만들기 완료");

        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(targetQueryInfo.getDriverNm(), targetQueryInfo.getDriverDsn(), targetQueryInfo.getDbUserId(), targetQueryInfo.getDbPassword());

        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sTargetQuery);
            rsmd = rs.getMetaData();

            int colCnt = rsmd.getColumnCount();

            String sTmp = null;
            String sFields = null;

            StringBuilder sbTmp = new StringBuilder();
            for(int i = 1; i <= colCnt; i++) {
                sTmp = rsmd.getColumnLabel(i).toUpperCase(); // toUpperCase를
                // 꼭 해줘라..
                sbTmp.append(sTmp);
                if(i < colCnt)
                    sbTmp.append("\t");
            }
            sFields = sbTmp.toString();
            contexts.put("_HEADER", sFields); // 헤더 정보를 넣는다.]

            sbTmp.setLength(0);

            boolean isDataFind = false;
            while(rs.next()) {
                sTmp = rs.getString(contexts.getInt("_KEY_FIELD_SEQ")).trim();
                if(sTmp == null)
                    continue;
                log.info("" + sTmp + "\t" + customerId);
                if((isDataFind = sTmp.equals(customerId.trim()))) {
                    break;
                }
            }

            if(!isDataFind)
                throw new Exception(customerId + " is not found!!");

            // 각 필드들에 대하여 미리보기 시 대상자 쿼리를 불러올 때 DB별로 코덱정보가 다르므로 기존
            // APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져오도록 수정함
            for(int i = 1; i <= colCnt; i++) {
                if(StringUtil.isEmpty(targetQueryInfo.getEncoding())) {
                    sTmp = rs.getString(i);
                } else {
                    sTmp = new String(rs.getString(i).getBytes(targetQueryInfo.getEncoding()), targetQueryInfo.getDecoding());

                }
                // 복호화 추가
                sTmp = sTmp != null ? security.securityWithColumn(sTmp.trim(), rsmd.getColumnName(i), "DECRYPT") : sTmp;
                sTmp = Util.revisionValue(sTmp);

                // 필드값을 기록할 때 마지막 필드면 행 분리자를, 마지막 필드가 아니면 필드 구분자('\t')를
                // 추가한다.
                if(i == colCnt) {
                    sbTmp.append(sTmp);
                } else {
                    sbTmp.append(sTmp);
                    sbTmp.append("\t");
                }
            }
            // end of for

            contexts.put("_TARGET", sbTmp.toString());
        } catch(Exception e) {
            log.error(e.getMessage());
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return false;
    }

    /**
     * 이케어 고객이력조회 결과보기 마임파일명을 이쪽에서 생성한다. 대상자 쿼리 및 컬럼 정보를 가져온다
     *
     * @param contexts
     * @param serviceNo
     * @param segmentNo
     * @param customerId
     * @return
     */
    private boolean getDefferedTargetDbInfo(ScrProperties contexts, int ecareNo, String customerSeq, String subType, String sqlHead) {
        contexts.put("_SEND_MODE", "T"); // Real or Test
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(1);
        String targetQuery = null;

        if(subType.equals("N")) {
            targetQuery = new String("SELECT * FROM NVREALTIMEACCEPT WHERE ECARE_NO = " + ecareNo + " AND SEQ = '" + customerSeq + "'");
        } else {
            targetQuery = new String(sqlHead + " WHERE ECARE_NO = " + ecareNo + " AND SEQ = '" + customerSeq + "'");
        }

        if(targetQuery.equals(""))
            return false;

        if(dbInfoVo.getDbKind().equalsIgnoreCase("SYBASE")) {
            targetQuery = targetQuery + " at isolation 0";
        }

        targetQuery = targetQuery.replace('\r', ' ').replace('\n', ' ').trim();

        log.info(" 대상자 조건\n" + targetQuery);
        log.info(">> 대상자 쿼리 만들기 완료");

        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());

        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(targetQuery);
            rsmd = rs.getMetaData();

            int colCnt = rsmd.getColumnCount();

            String sTmp = null;
            String sFields = null;

            StringBuilder sbTmp = new StringBuilder();
            for(int i = 1; i <= colCnt; i++) {
                sTmp = rsmd.getColumnLabel(i).toUpperCase();
                sbTmp.append(sTmp);
                if(i < colCnt)
                    sbTmp.append("\t");
            }
            sFields = sbTmp.toString();
            contexts.put("_HEADER", sFields); // 헤더 정보를 넣는다.]

            sbTmp.setLength(0);

            boolean isDataFind = false;
            while(rs.next()) {
                sTmp = rs.getString("SEQ");
                if(sTmp == null)
                    continue;
                log.info("" + sTmp + "\t" + customerSeq);
                if((isDataFind = sTmp.equals(customerSeq))) {
                    break;
                }
            }

            // 결과보기용 파일명을 id 로 만들기 위해 여기에서 처리.
            String resultSeq = String.valueOf(Util.getResultSeq());
            contexts.put("_RESULT_SEQ", resultSeq);

            String tid = new StringBuilder(31).append(StringUtil.leftPad(String.valueOf(ecareNo), 15, '0')).append(StringUtil.leftPad(resultSeq, 16, '0')).toString();
            contexts.put("_TID", tid); // 임의 TID 생성
            contexts.put("_CUSTOMER_NIC", rs.getString("RECEIVER_ID"));
            contexts.put("_CREATE_FILENAME", tid + "_" + rs.getString("RECEIVER_ID"));
            // 결과보기용 끝

            if(!isDataFind)
                throw new Exception(customerSeq + " is not found!!");

            // 각 필드들에 대하여..
            for(int i = 1; i <= colCnt; i++) {
                sTmp = rs.getString(i);
                sTmp = sTmp != null ? security.securityWithColumn(sTmp.trim(), rsmd.getColumnName(i), "DECRYPT") : sTmp;
                sTmp = Util.revisionValue(sTmp);

                // 필드값을 기록할 때 마지막 필드면 행 분리자를, 마지막 필드가 아니면 필드 구분자('\t')를
                // 추가한다.
                if(i == colCnt) {
                    sbTmp.append(sTmp);
                } else {
                    sbTmp.append(sTmp);
                    sbTmp.append("\t");
                }
            }
            // end of for

            contexts.put("_TARGET", sbTmp.toString());
            contexts.put("_CUSTOMER_NM", rs.getString("RECEIVER_NM").trim());
            contexts.put("_CUSTOMER_EMAIL", rs.getString("RECEIVER").trim());
        } catch(Exception e) {
            log.error(e.getMessage());
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return false;
    }

    /**
     * 준실시간 서비스 미리보기 시 상위 20개만 가져오는 대상자 쿼리를 생성한다.
     *
     * @param serviceNo
     * @param searchKey
     * @param searchValue
     * @return mailPreviewList
     */
    @SuppressWarnings("rawtypes")
    public List previewDeferredTimeList(int serviceNo, String searchKey, String searchValue, String subType) {
        Map<String, String> map = new HashMap<>();
        map.put("customerKeyFieldName", "RECEIVER_ID");
        map.put("customerNameFieldName", "RECEIVER_NM");
        map.put("customerEmailFieldName", "RECEIVER");
        map.put("customerSeqFieldName", "SEQ");

        // wiseU db 정보를 가져온다.
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(1);
        TargetQueryInfo targetQueryInfo = new TargetQueryInfo();
        targetQueryInfo.setDriverDsn(dbInfoVo.getDriverDsn());
        targetQueryInfo.setDriverNm(dbInfoVo.getDriverNm());
        targetQueryInfo.setDbUserId(dbInfoVo.getDbUserId());
        targetQueryInfo.setDbPassword(dbInfoVo.getDbPassword());

        /* 고객 DB가 INFORMIX이고 버젼이 10.x 미만인 경우 인라인뷰 쿼리가 동작하지 않으므로 DB 종류가 INFORMIX일 경우 일반 쿼리로 분기하여 처리함 20150527 kpjeong - 이케어 미리보기화면의 검색 버튼으로 검색이 안되는 현상 수정 by 송치호 */
        StringBuilder sbTargetQuery = new StringBuilder();

        sbTargetQuery.append(" SELECT ");

        final String sqlKind = dbInfoVo.getDbKind();
        if(sqlKind.toUpperCase().startsWith("INFORMIX")) {
            sbTargetQuery.append("FIRST 20 ");
        } else if(sqlKind.toUpperCase().startsWith("MSSQL") || sqlKind.equalsIgnoreCase("SYBASE")) {
            sbTargetQuery.append("TOP 20 ");
        }

        sbTargetQuery.append(" RECEIVER_ID, RECEIVER_NM, RECEIVER, SEQ FROM NVREALTIMEACCEPT WHERE SEND_FG='R' AND ECARE_NO = " + serviceNo);

        // SECUDB CHK
        if(searchValue != null && !searchValue.trim().equals("")) {
            if(searchKey.equals("customerKey")) {
                sbTargetQuery.append(" AND RECEIVER_ID='" + searchValue.trim() + "' ");
            } else if(searchKey.equals("customerName")) {
                sbTargetQuery.append(" AND RECEIVER_NM='" + searchValue.trim() + "' ");
            } else if(searchKey.equals("customerEmail")) {
                sbTargetQuery.append(" AND RECEIVER='" + searchValue.trim() + "' ");
            }
        }

        if(sqlKind.equalsIgnoreCase("ORACLE")) {
            sbTargetQuery.append(" AND ROWNUM <= 20");
            sbTargetQuery.append(" ORDER BY SEQ DESC");
        } else if(sqlKind.equalsIgnoreCase("DB2")) {
            sbTargetQuery.append(" ORDER BY SEQ DESC");
            sbTargetQuery.append(" FETCH FIRST 20 ROWS ONLY");
        } else if(sqlKind.equalsIgnoreCase("MYSQL")) {
            sbTargetQuery.append(" ORDER BY SEQ DESC limit 20");
        } else if(sqlKind.toUpperCase().startsWith("MSSQL") || sqlKind.equalsIgnoreCase("SYBASE")) {
            sbTargetQuery.append(" ORDER BY SEQ DESC");
        }

        return selectMailPreviewList(sbTargetQuery.toString(), map, targetQueryInfo);
    }

    /**
     * - 20100311 : 컬럼인덱스 정보가 있을경우 컬럼인덱스로 겟하도록 한다
     * - 20100824 : 미리보기 시 대상자 쿼리를 불러올 때 DB별로 코덱정보가 다르므로 기존 APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져오도록 수정
     *
     * @param query
     * @param keyMap
     * @param targetQueryInfo
     * @return
     */
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public List selectMailPreviewList(String query, Map keyMap, TargetQueryInfo targetQueryInfo) {
        String dbPassWord = null;
        try {
            dbPassWord = targetQueryInfo.getDbPassword();
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(targetQueryInfo.getDriverNm(), targetQueryInfo.getDriverDsn(), targetQueryInfo.getDbUserId(), dbPassWord);

        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        final List previewList = new ArrayList();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            int numcols = rs.getMetaData().getColumnCount();
            while(rs.next()) {
                Map map = new HashMap();
                for(int i = 1; i <= numcols; i++) {
                    if(rs.getMetaData().getColumnName(i).toUpperCase().equals("SEG")) {
                        map.put("seg", rs.getString(i));
                    }
                }

                Iterator it = keyMap.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    Object key = pairs.getKey();
                    String value = (String) pairs.getValue();

                    Object data = null;
                    String s = null;

                    if("0123456789".indexOf(value.charAt(0)) == -1) {
                        data = rs.getString((String) value);
                    } else {
                        data = rs.getString(Integer.parseInt(value));
                    }

                    // 20100824 : 미리보기 시 대상자 쿼리를 불러올 때 DB별로 코덱정보가 다르므로 기존 APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져오도록 수정함
                    if(StringUtil.isEmpty(targetQueryInfo.getEncoding())) {

                    } else {
                        s = (String) data;
                        data = new String(s.getBytes(targetQueryInfo.getEncoding()), targetQueryInfo.getDecoding());
                    }
                    map.put(key, data);
                }
                // SECUDB UNIT
                security.securityMap(map, "DECRYPT");

                previewList.add(map);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return previewList;
    }

    /**
     * 미리보기 대상 리스트를 조회한다.
     *
     * - 20100308 : SQLFILTER 로직 추가(대상자 DB가 다를경우 nvsegment의 SQLFILTER 컬럼의 쿼리만 날린다)
     * - 20100311 : 대상자 쿼리에 테이블 alias있을경우 문제가 생겨 컬럼명정보 대신 컬럼인덱스 정보를 사용하도록 변경한다(검색부분제외)
     *
     * @param serviceNo
     * @param serviceType
     * @param searchKey
     * @param searchValue
     * @param searchType
     * @param subType
     * @return
     */
    // 준실시간인 경우에는 세그먼트가 없으므로 쿼리를 생성하도록 한다.
    @SuppressWarnings("rawtypes")
    public List previewList(int serviceNo, String serviceType, String searchKey, String searchValue, String subType) {
        if("N".equals(subType)) {
            return previewDeferredTimeList(serviceNo, searchKey, searchValue, subType);
        }

        String customerKeyFieldName = null;
        String customerNameFieldName = null;
        String customerEmailFieldName = null;
        String customerKeyFieldSeq = null;
        String customerNameFieldSeq = null;
        String customerEmailFieldSeq = null;

        SegmentVo segmentVo = null;
        CampaignVo campaignVo;
        if(Client.EM.equals(serviceType)) {
            campaignVo = campaignDao.selectRelationAndDepth(serviceNo);
        } else {
            campaignVo = new CampaignVo();
        }

        if(isOmniChannelSubCampaign(serviceType, campaignVo.getRelationType(), campaignVo.getDepthNo())) {
            CampaignVo campInfoVo = campaignDao.selectParentCampaignInfo(campaignVo.getScenarioNo());
            segmentVo = segmentDao.selectSegmentInfo(campInfoVo.getCampaignNo(), serviceType);
        } else {
            segmentVo = segmentDao.selectSegmentInfo(serviceNo, serviceType);
        }

        // 대상자쿼리 정보 가져오기
        TargetQueryInfo targetQueryInfo = segmentDao.selectTargetQueryInfo(segmentVo.getSegmentNo());

        // SEMANTIC 정보 가져오기
        List<SemanticVo> semanticList = semanticDao.selectSemanticListBySegmentNo(segmentVo.getSegmentNo());
        for(SemanticVo semanticVo : semanticList) {
            final String fieldKey = semanticVo.getFieldKey();
            if("K".equals(fieldKey)) {
                customerKeyFieldName = semanticVo.getFieldNm();
                customerKeyFieldSeq = String.valueOf(semanticVo.getFieldSeq());
            }
            if("E".equals(fieldKey)) {
                customerEmailFieldName = semanticVo.getFieldNm();
                customerEmailFieldSeq = String.valueOf(semanticVo.getFieldSeq());
            }
            if("F".equals(fieldKey)) {
                customerEmailFieldName = semanticVo.getFieldNm();
                customerEmailFieldSeq = String.valueOf(semanticVo.getFieldSeq());
            }
            if("N".equals(fieldKey)) {
                customerNameFieldName = semanticVo.getFieldNm();
                customerNameFieldSeq = String.valueOf(semanticVo.getFieldSeq());
            }
        }

        /* 대상자 쿼리 SQLHEAD 정의(select ... from 테이블명) DBMS별로 상위 20개만 추출 하도록 쿼리 변경 */
        final String dbKind = targetQueryInfo.getDbKind();
        String sqlHead = new StringBuffer().append("SELECT ")
            .append((dbKind.toUpperCase().startsWith("MSSQL") || dbKind.equalsIgnoreCase("SYBASE")) ? "TOP 20"
                : ((dbKind.toUpperCase().startsWith("INFORMIX")) ? "FIRST 20" : ""))
            .append(targetQueryInfo.getSqlHead().substring(targetQueryInfo.getSqlHead().toUpperCase().indexOf("SELECT") + 6)).toString();

        // 대상자쿼리 SQLBODY 정의 (Where ...)
        String sqlBody = targetQueryInfo.getSqlBody();
        if(searchValue != null && !searchValue.trim().equals("")) {
            if(StringUtil.isEmpty(sqlBody)) {
                sqlBody += " WHERE ";
            } else {
                sqlBody += " AND ";
            }

            if(searchKey.equals("customerKey")) {
                sqlBody += customerKeyFieldName + "='" + searchValue + "'";
            } else if(searchKey.equals("customerName")) {
                sqlBody += customerNameFieldName + "='" + searchValue + "'";
            } else if(searchKey.equals("customerEmail")) {
                sqlBody += customerEmailFieldName + "='" + searchValue + "'";
            } else {
                sqlBody += searchKey + "='" + searchValue + "'";
            }
        }

        if(sqlHead.toUpperCase().indexOf("NVREALTIMEACCEPT") > 0) {
            sqlBody += " AND send_fg = 'R'";
        }

        // 대상자쿼리 SQLTAIL 정의
        String sqlTail = targetQueryInfo.getSqlTail();
        sqlTail = (sqlTail == null) ? "" : sqlTail;

        StringBuilder sbTargetQuery = new StringBuilder();

        /* 고객 DB가 INFORMIX이고 버젼이 10.x 미만인 경우 인라인뷰 쿼리가 동작하지 않으므로 DB 종류가 INFORMIX일 경우 일반 쿼리로 분기하여 처리함 */
        if(dbKind.toUpperCase().startsWith("INFORMIX")) {
            sbTargetQuery.append("\n " + sqlHead + " ");
            sbTargetQuery.append("\n " + sqlBody + " ");
            sbTargetQuery.append("\n " + sqlTail + " ");
        } else {
            sbTargetQuery.append("\n SELECT *                                            ");
            sbTargetQuery.append("\n   FROM (                                            ");
            sbTargetQuery.append("\n         " + sqlHead + "                             ");
            sbTargetQuery.append("\n         " + sqlBody + "                             ");
            sbTargetQuery.append("\n         " + sqlTail + "                             ");
            sbTargetQuery.append("\n         ) target                                    ");
            // Oracle or DB2 일 경우
            if(dbKind.equalsIgnoreCase("ORACLE")) {
                sbTargetQuery.append("\n  WHERE ROWNUM <= 20                             ");
            } else if(dbKind.equalsIgnoreCase("DB2")) {
                sbTargetQuery.append("\n  FETCH FIRST 20 ROWS ONLY                       ");
            } else if(dbKind.equalsIgnoreCase("MYSQL")) {
                sbTargetQuery.append("\n  limit 20                                       ");
            }
        }

        // 대상자가 있는 DBMS에 연결
        Map<String, String> map = new HashMap<>();
        map.put("customerKeyFieldName", customerKeyFieldSeq);
        map.put("customerNameFieldName", customerNameFieldSeq);
        map.put("customerEmailFieldName", customerEmailFieldSeq);

        // 스케쥴 서비스의경우만 SEQ값 포함
        if(targetQueryInfo.getSqlHead().toUpperCase().indexOf("NVSCHEDULEACCEPT") > -1) {
            map.put("customerSeqFieldName", "SEQ");
        }

        // SQLFILTER 로직 추가
        // 가능하면 이 부분 타겟팅 자체를 SqlMapSegmentDao.java 클래스의 selectTargetList 하고 합치면 좋을것도 같다
        if(segmentVo.getSqlfilter() != null) {
            if(segmentVo.getDbinfoSeq() == 1) {
                sbTargetQuery.insert(0, "SELECT * FROM (");
                sbTargetQuery.append(") A WHERE ").append(customerKeyFieldName).append(" IN (").append(segmentVo.getSqlfilter()).append(") ");
            } else {
                // 로컬 DB가 아닌경우 재발송은 로컬DB쪽거를 셀렉트하도록 한다
                sbTargetQuery.setLength(0);
                sbTargetQuery.append(segmentVo.getSqlfilter());
                map.clear();
                map.put("customerKeyFieldName", "1");

                DbInfoVo vo = dbInfoDao.selectDbInfoByPk(1);

                targetQueryInfo.setDriverDsn(vo.getDriverDsn());
                targetQueryInfo.setDriverNm(vo.getDriverNm());
                targetQueryInfo.setDbUserId(vo.getDbUserId());
                targetQueryInfo.setDbPassword(vo.getDbPassword());
            }
        }

        return selectMailPreviewList(sbTargetQuery.toString(), map, targetQueryInfo);
    }

    public void dbinit() throws Exception{
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(1);
        if(dbInfoVo != null) {
            this.jdbcDriver = dbInfoVo.getDriverNm();
            this.jdbcUser = dbInfoVo.getDbUserId();
            this.jdbcUrl = dbInfoVo.getDriverDsn();
            this.jdbcPassword = dbInfoVo.getDbPassword();
        }else {
            log.error("NVDBINFO seq[1] is empty. Register wiseu database");
            throw new Exception("NVDBINFO seq[1] is empty. Register wiseu database");
        }
    }

    public static final boolean init(String confPath) {
        // 이미 설정이 되어 있으면 다시 읽지 않는다.
        if(isLoad)
            return true;

        try {
            String config = FileUtil.readFileToString(confPath);

            // 미리보기 공통 경로를 replace 함. KOH
            config = config.replaceAll("[$]\\{WU_HOME\\}", System.getProperty("wiseu.home"));

            // 미리보기쪽에서는 사용하는데 없어서 db.server=oracle , cipher=off 로 고정
            // db 정보를 global.conf를 이용함으로 불필요함으로 하드 코딩 된 소스 제거
            Config.init(config, true);

            isLoad = true;
        } catch(Exception e) {
            log.error("Exception occurred. " + e.getMessage());
            isLoad = false;
            return false;
        }
        return true;
    }

    public void loadConf() throws Exception {
        Config.init(previewConf, true);
    }

    public String sendMailRequestAll(int aid) {
        TcpipClient tc = new TcpipClient();

        try {
            log.debug("======================= SEND-MAIL START =========================");
            tc.open(ltsIp, Integer.parseInt(ltsPort));
            tc.setAID(String.valueOf(aid));
            tc.setArg("MANUAL_MODE", "Y");
            tc.commit();
            tc.quit();
            log.debug("=======================  SEND-MAIL END ===========================");
            return "메일 발송을 요청하였습니다.";
        } catch(NumberFormatException e) {
            log.error(e.getMessage());
            return "발송요청 도중 에러가 발생하였습니다.";
        } catch(IOException ioe) {
            log.error(ioe.getMessage());
            return "발송요청 도중 에러가 발생하였습니다.";
        }
    }

    /**
     * 서비스 미리보기 용 기본정보를 가져온다. 발송 결과 미리보기 시 기본정보를 가져온다.
     *
     * @param serviceType
     * @param aid
     * @param seg
     * @return
     * @throws Exception
     */
    public MailPreviewVo selectServicePreview(String serviceType, int serviceNo) {
        if(serviceType.equalsIgnoreCase("EM")) {
            return campaignDao.selectCampaignPreview(serviceNo);
        }

        return ecareDao.selectEcarePreview(serviceNo);
    }

    /**
     * 준실시간 데이터를 발송이 되도록 업데이트 한다.
     *
     * @param seq
     * @return
     */
    public int defferedTargetSend(String seq) {
        return realtimeAcceptDao.defferedTargetSend(seq);
    }

    /**
     * 준실시간 데이터를 재발송이 되도록 업데이트 한다.
     *
     * @param seq
     * @param srfidd
     * @param receiver
     * @return
     */
    public int defferedTargetReSend(String seq, String srfidd, String receiver) {
        String newSeq = seq + System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        map.put("seq", seq);
        map.put("newSeq", newSeq);
        map.put("srfidd", srfidd);
        //SECUDB CHK
        map.put("receiver", receiver);

        int rvalue = realtimeAcceptDao.defferedTargetReSend(map);

        List<RealtimeAcceptDataVo> realtimeAcceptDataList = realtimeAcceptDataDao.selectRealtimeAcceptDataList(map);

        if(realtimeAcceptDataList != null) {
            for(int i = 0, n = realtimeAcceptDataList.size(); i < n; i++) {
                RealtimeAcceptDataVo realtimeAcceptData = realtimeAcceptDataList.get(i);
                // 새로 생성한 SEQ로 변경
                realtimeAcceptData.setSeq(newSeq);
                realtimeAcceptDataDao.insertRealtimeAcceptData(realtimeAcceptData);
            }
        }

        return rvalue;
    }

    /**
     * 팩스 발송 이력 보기 시에 srfidd 값이 없을 경우 우리쪽 로그에서 조회한다.
     *
     * @param serviceType
     * @param serviceNo
     * @param seq
     * @return
     */
    public String getFaxSrfidd(String serviceType, int serviceNo, String seq) {
        Map<String, Object> map = new HashMap<>();
        map.put("seq", seq);
        map.put("serviceNo", serviceNo);

        if(serviceType.equalsIgnoreCase("EC")) {
            return ecareSendLogDao.selectFaxSrfidd(map);
        }

        return sendLogDao.selectFaxSrfidd(map);
    }

    /**
     * 이케어 고객이력조회 결과보기 추가
     * 이케어 고객이력조회 결과보기 재발송 기능 추가(결과보기와 재발송버튼을 눌렀을 때 같은 로직임) 결과보기 및 재발송(스케쥴,준실시간 자동생성) - 메일 중 스케쥴,준실시간만 지원
     *
     * @param aid
     * @param serviceType
     * @param condition
     * @return
     * @throws Exception
     */
    public ScrProperties TargetInfo(int aid, String serviceType, String condition, String customerKey, String slot1, String slot2, boolean isResendHistory) {
        String sendDate = "20110503";

        ScrProperties contexts = new ScrProperties();
        contexts.put("_HANDLER_ID", "1");
        contexts.put("_SERVER_ROOT", Config.getServerRoot());
        contexts.put("_LOG", Config.getLog());
        contexts.put("_BUFFERING_SIZE", "1000");
        contexts.put("_MAX_USERS_PER_DOMAIN", "1");
        contexts.put("_jdbc.driver", jdbcDriver);
        contexts.put("_jdbc.url", jdbcUrl);
        contexts.put("_jdbc.user", jdbcUser);
        contexts.put("_jdbc.password", jdbcPassword);
        contexts.put("_jdbc.encoding", "");

        log.info("serviceType:" + serviceType);
        log.info("condition:" + condition);

        MailPreviewVo previewVo = ecareDao.selectEcarePreview(aid);
        String handlerType = previewVo.getHandlerType();
        contexts.put("_HANDLER", previewVo.getAppsource().trim()); // 핸들러
        contexts.put("_HANDLER_TYPE", handlerType); // 핸들러
        contexts.put("_CAMPAIGN_NAME", previewVo.getServiceNm());

        contexts.put("_RESEND_ECARE_NO", previewVo.getResendEcareNo());

        // 20100701 hongjun 보안메일의 경우에 TEMPLATE 테이블에 seg컬럼에 cover 명칭으로 들어가는
        // 템플릿등이 있는데 이를 핸들러에서 사용하기 위해서는 context에 담아줘야한다.
        List<MailPreviewVo> coverTemplate = ecareTemplateDao.selectEcareTemplateHistory(aid);
        log.info("template length:" + coverTemplate.size());
        for(int i = 0; i < coverTemplate.size(); i++) {
            MailPreviewVo mpv = coverTemplate.get(i);
            log.info("seg" + i + ":" + mpv.getSeg());
            if(mpv.getSeg().trim().equals("")) {
                contexts.put("_TEMPLATE", mpv.getTemplate());
            } else {
                contexts.put("_TEMPLATE_" + mpv.getSeg().trim(), mpv.getTemplate());
            }
        }

        // spool 멀티 템플릿용
        contexts.put("_SPOOL", Config.getSpool());

        contexts.put("_SEND_MODE", "R"); // Real or Test
        String serviceTypeString = "SCHEDULE";
        if("R".equals(previewVo.getServiceType())) {
            serviceTypeString = "REALTIME";
        }

        DecimalFormat df = new DecimalFormat("000000000000000");
        contexts.put("_CAMPAIGN_NO", df.format(new Integer(aid)));

        contexts.put("_TYPE", serviceTypeString);
        contexts.put("_SERVICE_TYPE", previewVo.getServiceType());
        contexts.put("_SUB_TYPE", previewVo.getSubType());
        contexts.put("_CLIENT", serviceType.toLowerCase()); // em or ec
        contexts.put("_JOB_CLASS", previewVo.getClass());
        contexts.put("_MESSAGE_THREAD_NUM", "1");
        contexts.put("_JOB_CLASS", "I"); // 정보성(I),상업성(A)
        contexts.put("_STATUS", "W");
        contexts.put("_TOTAL_COUNT", "1");
        contexts.put("_MERGE_MAKE_DONE", "N");
        contexts.put("_PREVIEW_YN", "Y");
        contexts.put("_LIST_SEQ", "1");

        contexts.put("_CHANNEL_TYPE", previewVo.getChannelType());
        contexts.put("_RETMAIL_RECEIVER", previewVo.getRetmailReceiver() == null ? "" : previewVo.getRetmailReceiver());
        contexts.put("_SENDER_NM", previewVo.getSenderNm());
        contexts.put("_SENDER_EMAIL", previewVo.getSenderEmail());

        contexts.put("_SENDER_TEL", previewVo.getSenderTel());
        contexts.put("_RECEIVER_NM", previewVo.getReceiverNm());
        contexts.put("_RETRY_CNT", "1");
        contexts.put("_RECE_CON", "N");
        contexts.put("_PREVIEW_YN", "Y"); // MAS에서 이 값이 Y일 경우에는 발송을 안하고
        contexts.put("_RECOVER_SERVER_SID", "MAS1");
        contexts.put("_REQUEST_TIME", sendDate + "0000");

        // 준실시간인 경우 처리(NVREALTIMEACCEPT 테이블 조회)
        if(previewVo.getSubType() != null && previewVo.getSubType().equals("N")) {
            getDefferedTargetDbInfo(contexts, aid, condition, previewVo.getSubType(), "");
            contexts.put("_JEONMUN", "true");
            contexts.put("_SEGMENT_NO", 0);
            contexts.put("_CONDITION", condition);
        } else {
            boolean isJeonmun = getTargetDbInfoForHistory(contexts, aid, previewVo.getSegmentNo(), condition, customerKey, slot1, slot2, isResendHistory);
            if(isJeonmun) {
                contexts.put("_JEONMUN", "true");
            } else {
                contexts.put("_JEONMUN", "false");
            }
        }

        // 시작일시,종료일시를 얻는다.
        contexts.put("_START_DATE", sendDate.replaceAll("-", ""));
        contexts.put("_END_DATE", "00000000");

        if("EC".equalsIgnoreCase(serviceType)) {
            setAdditionalEcareInfo(contexts, previewVo, aid);
        }

        if(handlerType.equals("G")) {
            groovyHandlerWrite(contexts);
        }

        return contexts;
    }

    /**
     * 이케어 고객이력조회 결과보기 추가 스케쥴 발송결과일 경우
     *
     * @param contexts
     * @param serviceNo
     * @param segmentNo
     * @param condition
     * @param customerKey
     * @param slot1
     * @param slot2
     * @param isResendHistory
     * @return
     */
    private boolean getTargetDbInfoForHistory(ScrProperties contexts, int serviceNo, int segmentNo, String condition, String customerKey, String slot1, String slot2, boolean isResendHistory) {
        List<SemanticVo> semanticList = semanticDao.selectSemanticListBySegmentNo(segmentNo);
        String keyFieldName = null;
        String slot1FieldName = null;
        String slot2FieldName = null;
        StringBuilder temp = new StringBuilder();

        for(SemanticVo semanticVo : semanticList) {
            // 고객키인 경우
            final String fieldKey = semanticVo.getFieldKey();
            final String fieldSeq = String.valueOf(semanticVo.getFieldSeq());

            if(fieldKey.equals("K")) {
                contexts.put("_KEY_FIELD_SEQ", fieldSeq);
                keyFieldName = semanticVo.getFieldNm();
            } else if(fieldKey.equals("E")) {
                // 고객 이메일인 경우
                contexts.put("_EMAIL_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("N")) {
                // 고객명인경우
                contexts.put("_NAME_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("S")) {
                // 고객전화인 경우
                contexts.put("_SMS_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("F")) {
                // FAX 경우
                contexts.put("_FAX_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("A")) {
                // slot1 인 경우
                slot1FieldName = semanticVo.getFieldNm();
            } else if(fieldKey.equals("B")) {
                // slot2 인 경우
                slot2FieldName = semanticVo.getFieldNm();
            }
        }

        // 대상자쿼리 정보 가져오기
        TargetQueryInfo targetQueryInfo = segmentDao.selectTargetQueryInfo(segmentNo);
        String sqlHead = targetQueryInfo.getSqlHead().toUpperCase();

        contexts.put("_SEND_MODE", "T"); // Real or Test
        StringBuilder targetQuery = new StringBuilder();
        targetQuery.append(" SELECT ");
        targetQuery.append(" * FROM ( ");
        targetQuery.append(targetQueryInfo.getSqlHead());
        targetQuery.append(" WHERE ");

        // 전문테이블이 아니면...
        if(sqlHead.indexOf("NVSCHEDULEACCEPT") > -1) {
            targetQuery.append(" ECARE_NO = ").append(serviceNo).append(" AND SEQ = '").append(condition).append("'");
        } else {
            // 전문테이블이면...
            // 결과보기는 고객이력조회에서 접근했을 경우 slot1,slot2,customerKey 로 대상자를 가져오지만
            // 재발송 로직은 외부URL 호출이던 고객이력조회를 통해서 오던
            // condition 결과보기에서 만들어준 condition 값으로 대상자를 가져온다.
            if(isResendHistory || condition != "") {
                targetQuery.append(condition);
            } else {
                temp.append(keyFieldName).append(" = '").append(customerKey).append("'");
                if(slot1FieldName != null && slot1 != " ") {
                    temp.append(" AND ").append(slot1FieldName).append(" = '").append(slot1).append("'");
                }
                if(slot2FieldName != null && slot2 != " ") {
                    temp.append(" AND ").append(slot2FieldName).append(" = '").append(slot2).append("'");
                }
                condition = temp.toString();
                targetQuery.append(condition);
            }
        }
        contexts.put("_CONDITION", condition);
        targetQuery.append(" ");
        targetQuery.append(targetQueryInfo.getSqlTail());
        targetQuery.append(" ) A ");
        String sTargetQuery = targetQuery.toString();
        if(sTargetQuery.equals(""))
            return false;

        if(targetQueryInfo.getDbKind().equalsIgnoreCase("SYBASE")) {
            sTargetQuery = sTargetQuery + " at isolation 0";
        }
        sTargetQuery = sTargetQuery.replace('\r', ' ').replace('\n', ' ').trim();

        log.info(" 대상자 조건\n" + sTargetQuery);
        log.info(">> 대상자 쿼리 만들기 완료");

        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(targetQueryInfo.getDriverNm(), targetQueryInfo.getDriverDsn(), targetQueryInfo.getDbUserId(), targetQueryInfo.getDbPassword());

        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sTargetQuery);
            rsmd = rs.getMetaData();

            int colCnt = rsmd.getColumnCount();

            String sTmp = null;
            String sFields = null;

            StringBuilder sbTmp = new StringBuilder();
            for(int i = 1; i <= colCnt; i++) {
                sTmp = rsmd.getColumnLabel(i).toUpperCase(); // toUpperCase를
                // 꼭 해줘라..
                sbTmp.append(sTmp);
                if(i < colCnt)
                    sbTmp.append("\t");
            }
            sFields = sbTmp.toString();
            contexts.put("_HEADER", sFields); // 헤더 정보를 넣는다.]

            sbTmp.setLength(0);

            while(rs.next()) {
                sTmp = rs.getString(contexts.getInt("_KEY_FIELD_SEQ")).trim();
                if(sTmp == null)
                    continue;
                log.info("" + sTmp + "\t" + condition);
                break;
            }

            // 결과보기용 파일명을 id 로 만들기 위해 여기에서 처리.
            String resultSeq = String.valueOf(Util.getResultSeq());
            contexts.put("_RESULT_SEQ", resultSeq);

            String tid = new StringBuffer(31).append(StringUtil.leftPad(String.valueOf(serviceNo), 15, '0')).append(StringUtil.leftPad(resultSeq, 16, '0')).toString();
            contexts.put("_TID", tid); // 임의 TID 생성
            contexts.put("_CUSTOMER_NIC", sTmp.trim()); // 장홍준 파일생성시 고객키값도
            contexts.put("_CREATE_FILENAME", tid + "_" + sTmp.trim());
            // 결과보기용 끝

            // 각 필드들에 대하여 미리보기 시 대상자 쿼리를 불러올 때 DB별로 코덱정보가 다르므로 기존
            // APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져오도록 수정함
            for(int i = 1; i <= colCnt; i++) {
                if(StringUtil.isEmpty(targetQueryInfo.getEncoding())) {
                    sTmp = rs.getString(i);
                } else {
                    sTmp = new String(rs.getString(i).getBytes(targetQueryInfo.getEncoding()), targetQueryInfo.getDecoding());

                }
                sTmp = sTmp != null ? sTmp.trim() : sTmp;
                sTmp = Util.revisionValue(sTmp);

                // 필드값을 기록할 때 마지막 필드면 행 분리자를, 마지막 필드가 아니면 필드 구분자('\t')를
                // 추가한다.
                if(i == colCnt) {
                    sbTmp.append(sTmp);
                } else {
                    sbTmp.append(sTmp);
                    sbTmp.append("\t");
                }
            }
            // end of for

            contexts.put("_TARGET", sbTmp.toString());
            contexts.put("_CUSTOMER_NM", rs.getString(contexts.getInt("_NAME_FIELD_SEQ")).trim());
            contexts.put("_CUSTOMER_EMAIL", rs.getString(contexts.getInt("_EMAIL_FIELD_SEQ")).trim());
        } catch(Exception e) {
            log.error(e.getMessage());
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return (sqlHead.indexOf("NVSCHEDULEACCEPT") > -1) ? true : false;
    }

    public void insertResendDataForSchedule(MimeViewVo mimeVo) throws Exception {
        realtimeAcceptDao.insertResendDataForSchedule(mimeVo);
    }

    public void insertResendDataForDefferedTime(MimeViewVo mimeVo) throws Exception {
        realtimeAcceptDao.insertResendDataForDefferedTime(mimeVo);

        String jonmun = realtimeAcceptDao.selectDefferedTimeJeonmun(mimeVo);
        mimeVo.setJeonmun(jonmun);

        realtimeAcceptDao.updateJeonmun(mimeVo);
    }

    public void insertResendDataForJeonmun(MimeViewVo mimeVo) throws Exception {
        realtimeAcceptDao.insertResendDataForJeonmun(mimeVo);
        String jonmun = scheduleAcceptDao.selectJeonmun(mimeVo);
        mimeVo.setJeonmun(jonmun);
        realtimeAcceptDao.updateJeonmun(mimeVo);
    }

    /**
     * 스케쥴 미리보기일 경우 전문테이블 사용여부를 확인한다.
     *
     * @param serviceNo
     * @return
     */
    public boolean isJeonmun(int serviceNo) {
        MailPreviewVo previewVo = ecareDao.selectEcarePreview(serviceNo);
        TargetQueryInfo targetQueryInfo = segmentDao.selectTargetQueryInfo(previewVo.getSegmentNo());
        if(targetQueryInfo.getSqlHead().toUpperCase().indexOf("NVSCHEDULEACCEPT") > -1) {
            return true;
        } else {
            return false;
        }

    }

    //2017.07.10 KSM 재발송/다시보기
    public Hashtable<String, Object> makePrevious(int aid, String customerkey, String client, String subType, String resultSeq, String listSeq) throws Exception {
        init(previewConf);
        dbinit();

        File dir = new File(Config.getMTSSpool());
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/fax");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getLog());
        if(!dir.exists())
            dir.mkdirs();

        Hashtable<String, Object> table = new Hashtable<>();
        ScrProperties contexts = new ScrProperties();

        String fileName = null;

        BufferedReader in = null;
        try {
            log.info("===============================================================");
            log.info(" Preview Mail Make Start..");
            contexts = TargetInfo(resultSeq, listSeq, aid, customerkey, client, subType);
            log.info("===============================================================");
            // 채널 타입을 넘겨준다.
            table.put("channelType", contexts.get("_CHANNEL_TYPE"));

            boolean isNormal = executeHandler(contexts);

            if(isNormal) {
                if(Channel.MAIL.equals(contexts.get("_CHANNEL_TYPE"))) {
                    fileName = Config.getServerRoot() + "/spool/preview/" + contexts.get("_CREATE_FILENAME").toString() + ".mime";

                    ReadingMime parse = new ReadingMime(fileName, null);

                    parse.mimeReading();

                    parse.mimeParse();

                    table.put("content", parse.getContent());
                    table.put("attachFile", parse.getAttachFiles());
                    table.put("to", parse.getTo());
                    table.put("from", parse.getFrom());
                    table.put("subject", parse.getSubject());

                    File f = new File(fileName);
                    if(f.delete()) {
                        log.info(fileName + "삭제 완료");
                    } else {
                        log.warn(fileName + "삭제 실패");
                    }

                } else if(Channel.FAX.equals(contexts.get("_CHANNEL_TYPE"))) {
                    fileName = Config.getServerRoot() + "/spool/preview/fax/" + contexts.get("_CREATE_FILENAME").toString() + ".html";

                    in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                    String temp = null;
                    StringBuffer str = new StringBuffer();

                    while((temp = in.readLine()) != null) {
                        str.append(temp.toString() + CRLF);
                    }
                    String content = str.toString();
                    IOUtil.closeQuietly(in);

                    table.put("content", content);
                    table.put("subject", contexts.get("_SUBJECT") == null ? "" : (String) contexts.get("_SUBJECT"));

                    File f = new File(fileName);
                    if(f.delete()) {
                        log.info(fileName + "삭제 완료");
                    } else {
                        log.warn(fileName + "삭제 실패");
                    }
                } else if(Channel.ALIMTALK.equals(contexts.get("_CHANNEL_TYPE"))) {
                    table.put("content", (String) contexts.get("contents"));
                } else if(Channel.LMSMMS.equals(contexts.get("_CHANNEL_TYPE"))) {
                    table.put("content", (String) contexts.get("contents"));
                    table.put("subject", contexts.get("subject") == null ? "" : (String) contexts.get("subject"));
                    if("Y".equals(contexts.get("attachImgYn"))){    //이미지 첨부 여부
                        table.put("prevImagePath", contexts.get("prevImagePath") !=null ? (String[]) contexts.get("prevImagePath") : "");
                    }
                }else {
                    table.put("content", (String) contexts.get("contents"));
                    table.put("subject", contexts.get("subject") == null ? "" : (String) contexts.get("subject"));
                }
            } else {
                table.put("to", "");
                table.put("from", "");
                table.put("content", contexts.get("_EXCEPTION_MESSAGE"));
            }

            log.info(" Preview Mail Make Completed!!");
            log.info("===============================================================");
        } catch(Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            deleteSpoolFiles(contexts);
            IOUtil.closeQuietly(in);
        }

        return table;
    }

    //  다시보기
    public ScrProperties TargetInfo(String resultSeq, String listSeq, int aid, String customerkey, String serviceType, String subType) throws Exception {
        ScrProperties contexts = new ScrProperties();
        contexts.put("_HANDLER_ID", "1");
        contexts.put("_SERVER_ROOT", Config.getServerRoot());
        contexts.put("_LOG", Config.getLog());
        contexts.put("_BUFFERING_SIZE", "1000");
        contexts.put("_MAX_USERS_PER_DOMAIN", "1");
        contexts.put("_jdbc.driver", jdbcDriver);
        contexts.put("_jdbc.url", jdbcUrl);
        contexts.put("_jdbc.user", jdbcUser);
        contexts.put("_jdbc.password", jdbcPassword);
        contexts.put("_jdbc.encoding", "");

        MailPreviewVo previewVo = selectServicePrevious(aid, serviceType, resultSeq);
        String handlerType = previewVo.getHandlerType();
        contexts.put("_HANDLER", previewVo.getAppsource().trim()); // 핸들러
        contexts.put("_HANDLER_TYPE", handlerType); // 핸들러
        contexts.put("_CAMPAIGN_NAME", previewVo.getServiceNm());

        List<MailPreviewVo> coverTemplate = selectTemplatePrevious(aid, serviceType, resultSeq);
        log.info("template length:" + coverTemplate.size());
        for(int i = 0; i < coverTemplate.size(); i++) {
            MailPreviewVo mpv = coverTemplate.get(i);
            log.info("seg" + i + ":" + mpv.getSeg());
            if(mpv.getSeg().trim().equals("")) {
                contexts.put("_TEMPLATE", mpv.getTemplate());
            } else {
                contexts.put("_TEMPLATE_" + mpv.getSeg().trim(), mpv.getTemplate());
            }
        }

        contexts.put("_SPOOL", Config.getSpool());
        contexts.put("_SEND_MODE", "R"); // Real or Test
        String serviceTypeString = "SCHEDULE";
        if("R".equals(previewVo.getServiceType())) {
            serviceTypeString = "REALTIME";
        }

        DecimalFormat df = new DecimalFormat("000000000000000");
        contexts.put("_CAMPAIGN_NO", df.format(new Integer(aid)));
        contexts.put("_TYPE", serviceTypeString);
        contexts.put("_CLIENT", serviceType.toLowerCase()); // em or ec
        contexts.put("_JOB_CLASS", previewVo.getClass());
        contexts.put("_MESSAGE_THREAD_NUM", "1");
        contexts.put("_JOB_CLASS", "I"); // 정보성(I),상업성(A)
        contexts.put("_STATUS", "W");
        contexts.put("_TOTAL_COUNT", "1");
        contexts.put("_MERGE_MAKE_DONE", "N");
        contexts.put("_PREVIEW_YN", "Y");
        contexts.put("_LIST_SEQ", listSeq);
        contexts.put("_CHANNEL_TYPE", previewVo.getChannelType());
        contexts.put("_RETMAIL_RECEIVER", previewVo.getRetmailReceiver() == null ? "" : previewVo.getRetmailReceiver());
        contexts.put("_SENDER_NM", previewVo.getSenderNm() == null? " " :previewVo.getSenderNm());
        contexts.put("_SENDER_EMAIL", previewVo.getSenderEmail());
        contexts.put("_SENDER_TEL", previewVo.getSenderTel());
        contexts.put("_RECEIVER_NM", previewVo.getReceiverNm());
        contexts.put("_RETRY_CNT", "1");
        contexts.put("_RECE_CON", "N");
        contexts.put("_RESULT_SEQ", resultSeq);
        contexts.put("_SERVICE_TYPE", previewVo.getServiceType()==null?" ":previewVo.getServiceType());
        contexts.put("_SUB_TYPE", previewVo.getSubType()==null?" ":previewVo.getSubType());
        String tid = new StringBuffer(31).append(StringUtil.leftPad(String.valueOf(aid), 15, '0')).append(StringUtil.leftPad(resultSeq, 16, '0')).toString();
        contexts.put("_TID", tid); // 임의 TID 생성
        contexts.put("_CUSTOMER_NIC", customerkey.trim()); // 장홍준 파일생성시 고객키값도
        contexts.put("_CREATE_FILENAME", tid + "_" + customerkey.trim());
        contexts.put("_PREVIEW_YN", "Y"); // MAS에서 이 값이 Y일 경우에는 발송을 안하고
        contexts.put("_RECOVER_SERVER_SID", "MAS1");
        contexts.put("_REQUEST_TIME", "0000");
        contexts.put("_START_DATE", "00000000");
        contexts.put("_END_DATE", "00000000");
        contexts.put("_SERVICE_PREFACE", previewVo.getServicePreface() == null ? "" : previewVo.getServicePreface());

        if(Channel.SMS.equalsIgnoreCase(previewVo.getChannelType())) {
            @SuppressWarnings("unchecked")
            Enumeration<String> enu = contexts.keys();
            String sKey = "";
            Hashtable<String, String> contextsMap = new Hashtable<>();
            while(enu.hasMoreElements()) {
                sKey = enu.nextElement();
                contextsMap.put(sKey, (String) contexts.get(sKey));
            }

            //20170717 KSM 다시보기에서는 Dg파일 임의로 지정해도 상관없음.
            contexts.put("_REQ_DEPT_ID", "preview_dept_id");
            contexts.put("_REQ_USER_ID", "preview_user_id");
        } else if(Channel.LMSMMS.equalsIgnoreCase(previewVo.getChannelType())) {
            contexts.put("_PREVIOUS_TEM_URL", PropertyUtil.getProperty("previous.tem.url"));
            contexts.put("_PREVIOUS_TEM_DIR", PropertyUtil.getProperty("previous.tem.dir"));

            @SuppressWarnings("unchecked")
            Enumeration<String> enu = contexts.keys();
            String sKey = "";
            Hashtable<String, String> contextsMap = new Hashtable<>();
            while(enu.hasMoreElements()) {
                sKey = enu.nextElement();
                contextsMap.put(sKey, (String) contexts.get(sKey));
            }

            contexts.put("_REQ_DEPT_ID", "preview_dept_id");
            contexts.put("_REQ_USER_ID", "preview_user_id");
            contexts.put("_RE_PREVIEW", "Y");
        }

        // 준실시간인 경우 처리(NVREALTIMEACCEPT 테이블 조회)
        if("N".equals(previewVo.getSubType())) {
            getDefferedTargetDbInfo(contexts, aid, customerkey, previewVo.getSubType(), "");
        } else {
            getLstTergetDbInfo(contexts, aid, previewVo.getSegmentNo(), customerkey, previewVo.getSubType(), resultSeq, listSeq);
        }

        if ("EC".equalsIgnoreCase(serviceType)) {
            setAdditionalEcareInfo(contexts, previewVo, aid);
        }

        if(handlerType.equals("G")) {
            groovyHandlerWrite(contexts);
        }

        return contexts;
    }

    public List<MailPreviewVo> selectTemplatePrevious(int serviceNo, String serviceType, String resultSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceNo", serviceNo);
        map.put("resultSeq", resultSeq);

        if(serviceType.equalsIgnoreCase("EM")) {
            map.put("seg", " ");
            return templateDao.selectCampaignTemplate(map);
        } else {
            MailPreviewVo mailPreviewVo = ecareDao.selectEcarePreviousView(map);
            List<MailPreviewVo> tempVo = new ArrayList<>();
            MailPreviewVo previewTemplate;
            // 템플릿
            previewTemplate = ecareTemplateHistoryDao.selectEcarePreviousTemplate(mailPreviewVo);
            if(previewTemplate != null)
                tempVo.add(previewTemplate);
            // 커버
            previewTemplate = ecareTemplateHistoryDao.selectEcarePreviousCoverTemplate(mailPreviewVo);
            if(previewTemplate != null)
                tempVo.add(previewTemplate);
            // 머리말
            previewTemplate = ecareTemplateHistoryDao.selectEcarePreviousPrefaceTemplate(mailPreviewVo);
            if(previewTemplate != null)
                tempVo.add(previewTemplate);

            return tempVo;
        }
    }

    // 재발송/다시보기
    public MailPreviewVo selectServicePrevious(int serviceNo, String serviceType, String resultSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceNo", serviceNo);
        map.put("resultSeq", resultSeq);

        MailPreviewVo tmp = null;
        if(serviceType.equalsIgnoreCase("EM")) {
            tmp = campaignDao.selectCampaignPreview(serviceNo);
        } else {
            tmp = ecareDao.selectEcarePreviousView(map);
            if(0 != tmp.getHandlerVer()) {
                MailPreviewVo handlerVo = ecMsgHandlerHistoryDao.selectEcarePreviousHandler(tmp);
                tmp.setAppsource(handlerVo.getAppsource());
                tmp.setHandlerType(handlerVo.getHandlerType());
            }
        }
        return tmp;
    }

    private boolean getLstTergetDbInfo(ScrProperties contexts, int serviceNo, int segmentNo, String customerId, String subType, String resultSeq, String listSeq) {
        //헤더는 모든서비스 공통!
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2 ");
        sb.append(" FROM NVRESENDTARGET ");
        sb.append(" WHERE SERVICE_NO = " + serviceNo);
        sb.append(" AND RESULT_SEQ = " + resultSeq);
        sb.append(" AND LIST_SEQ IN ('0000000000', '" + listSeq + "')");
        sb.append(" AND CLIENT = '" + ((String) contexts.get("_CLIENT")).toUpperCase() + "'");
        sb.append(" ORDER BY LIST_SEQ ASC ");
        //행 분리자가 포함되어 있으면 공백으로 치환
        String sTargetQuery = sb.toString();
        sTargetQuery = sTargetQuery.replace('\r', ' ').replace('\n', ' ').trim();

        List<SemanticVo> semanticList = semanticDao.selectSemanticListBySegmentNo(segmentNo);
        final StringBuilder nmSb = new StringBuilder();
        final StringBuilder descSb = new StringBuilder();

        for(SemanticVo semanticVo : semanticList) {

            // 고객키인 경우
            final String fieldKey = semanticVo.getFieldKey();
            final String fieldSeq = String.valueOf(semanticVo.getFieldSeq());
            if(fieldKey.equals("K")) {
                contexts.put("_KEY_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("E")) {
                // 고객 이메일인 경우
                contexts.put("_EMAIL_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("N")) {
                // 고객명인경우
                contexts.put("_NAME_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("S")) {
                // 고객전화인 경우
                contexts.put("_SMS_FIELD_SEQ", fieldSeq);
                //2017.06.19 MMS에서도 사용하므로 추가
                contexts.put("_MMS_FIELD_SEQ", fieldSeq);
            } else if(fieldKey.equals("F")) {
                // FAX 경우
                contexts.put("_FAX_FIELD_SEQ", fieldSeq);
            }

            nmSb.append(semanticVo.getFieldNm()).append("\t");
            descSb.append(semanticVo.getFieldDesc()).append("\t");
        }

        contexts.put("_FIELD_NAMES", nmSb.toString());
        contexts.put("_FIELD_DESCRIPTIONS", descSb.toString());
        // 대상자쿼리 정보 가져오기
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(1);
        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());

        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sTargetQuery);
            int idx = 1;
            while(rs.next()) {
                if(idx++ == 1) {
                    contexts.put("_HEADER", (rs.getString("TARGET_LST1") + StringUtil.defaultIfEmpty(rs.getString("TARGET_LST2"), "")).replaceAll("SEQ\tREJCD\t", ""));
                } else {
                    // 암/복호화 처리
                    contexts.put("_TARGET", SecurityUtil.ariaDecrypt((rs.getString("TARGET_LST1") + StringUtil.defaultIfEmpty(rs.getString("TARGET_LST2"), "")).replaceAll("^\\d+\t\\d{3}\t", "")) );
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return true;
    }

    public Hashtable<String, Object> makePreviewAbTest(int aid, String customerkey, String realtimeArg, String serviceType, String subType, String sendDate, String seg, String abTestType, String abType) throws Exception {
        init(previewConf);
        dbinit();

        File dir = new File(Config.getMTSSpool());
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/fax");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getLog());
        if(!dir.exists())
            dir.mkdirs();

        Hashtable<String, Object> table = new Hashtable<>();
        ScrProperties contexts = new ScrProperties();
        String fileName = null;

        BufferedReader in = null;
        try {
            log.info("===============================================================");
            log.info(" Preview Mail Make A/B test Start..");
            log.info("aid : " + aid + ", customerkey : " + customerkey + ", serviceType : " + serviceType + ", sendDate : " + sendDate + ", seg : " + seg + ", realtimeArg : " + realtimeArg
                + ", abTestType : " + abTestType + ", abType : " + abType);
            contexts = TargetInfoTestAb(aid, customerkey, realtimeArg, serviceType, subType, sendDate, seg, abTestType, abType);
            log.info("===============================================================");
            // 채널 타입을 넘겨준다.
            table.put("channelType", contexts.get("_CHANNEL_TYPE"));

            boolean isNormal = executeHandler(contexts);

            if(isNormal) {
                if("F".equals(contexts.get("_CHANNEL_TYPE"))) {
                    fileName = Config.getServerRoot() + "/spool/preview/fax/" + contexts.get("_CREATE_FILENAME").toString() + ".html";

                    in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                    String temp = null;
                    StringBuilder str = new StringBuilder();

                    while((temp = in.readLine()) != null) {
                        str.append(temp.toString() + CRLF);
                    }
                    table.put("content", str.toString());

                    IOUtil.closeQuietly(in);
                } else {
                    fileName = Config.getServerRoot() + "/spool/preview/" + contexts.get("_CREATE_FILENAME").toString() + ".mime";

                    ReadingMime parse = new ReadingMime(fileName, null);

                    parse.mimeReading();

                    parse.mimeParse();

                    table.put("content", parse.getContent());
                    table.put("attachFile", parse.getAttachFiles());
                    table.put("to", parse.getTo());
                    table.put("from", parse.getFrom());
                    table.put("subject", parse.getSubject());
                }

                File f = new File(fileName);
                if(f.delete()) {
                    log.info(fileName + "삭제 완료");
                } else {
                    log.warn(fileName + "삭제 실패");
                }
            } else {
                table.put("to", "");
                table.put("from", "");
                table.put("content", contexts.get("_EXCEPTION_MESSAGE"));
            }

            log.info(" Preview Mail Make Completed!!");
            log.info("===============================================================");
        } catch(Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            deleteSpoolFiles(contexts);
            IOUtil.closeQuietly(in);
        }

        return table;
    }

    private ScrProperties TargetInfoTestAb(int aid, String customerkey, String realtimeArg, String serviceType, String subType, String sendDate, String seg, String abTestType, String abType) {
        String handler = null;
        String handlerType = null;

        ScrProperties contexts = new ScrProperties();
        contexts.put("_HANDLER_ID", "1");
        contexts.put("_SERVER_ROOT", Config.getServerRoot());
        contexts.put("_LOG", Config.getLog());
        contexts.put("_BUFFERING_SIZE", "1000");
        contexts.put("_MAX_USERS_PER_DOMAIN", "1");
        contexts.put("_jdbc.driver", jdbcDriver);
        contexts.put("_jdbc.url", jdbcUrl);
        contexts.put("_jdbc.user", jdbcUser);
        contexts.put("_jdbc.password", jdbcPassword);
        contexts.put("_jdbc.encoding", "");

        //N:미사용, S:제목, T:템플릿
        contexts.put("_AB_TEST_TYPE", abTestType);
        //A:첫번째 제목 or 템플릿 B: 두번째 제목 or 템플릿
        contexts.put("_AB_TYPE", abType);

        log.info("serviceType:" + serviceType);
        log.info("seg:" + seg);
        log.info("abTestType:" + abTestType);
        log.info("abType:" + abType);
        MailPreviewVo previewVo = selectServicePreview(serviceType, aid);
        handler = previewVo.getAppsource().trim();
        handlerType = previewVo.getHandlerType();
        contexts.put("_HANDLER", handler); // 핸들러
        contexts.put("_HANDLER_TYPE", handlerType); // 핸들러
        contexts.put("_CAMPAIGN_NAME", previewVo.getServiceNm());

        // 20100701 hongjun 보안메일의 경우에 TEMPLATE 테이블에 seg컬럼에 cover 명칭으로 들어가는
        // 템플릿등이 있는데 이를 핸들러에서 사용하기 위해서는 context에 담아줘야한다.
        List<MailPreviewVo> coverTemplate = selectTemplateAb(serviceType, aid, seg, abTestType, abType);

        log.info("template length:" + coverTemplate.size());

        for(int i = 0; i < coverTemplate.size(); i++) {
            MailPreviewVo mpv = coverTemplate.get(i);

            log.info("seg" + i + ":" + mpv.getSeg());

            //A/B 템플릿 사용
            if("T".equals(abTestType)) {
                contexts.put("_TEMPLATE", coverTemplate.get(0).getTemplate());
                contexts.put("_TEMPLATE_AB", coverTemplate.get(1).getTemplate());
            } else {
                //A/B 제목 사용(제목은 템플릿 1개 seg = " ")
                if(mpv.getSeg().trim().equals("")) {
                    contexts.put("_TEMPLATE", mpv.getTemplate());
                }
            }
        }

        // spool 멀티 템플릿용
        contexts.put("_SPOOL", Config.getSpool());

        contexts.put("_SEND_MODE", "R"); // Real or Test
        String serviceTypeString = "SCHEDULE";
        if("R".equals(previewVo.getServiceType())) {
            serviceTypeString = "REALTIME";
        }

        DecimalFormat df = new DecimalFormat("000000000000000");
        contexts.put("_CAMPAIGN_NO", df.format(new Integer(aid)));

        contexts.put("_TYPE", serviceTypeString);
        contexts.put("_CLIENT", serviceType.toLowerCase()); // em or ec
        contexts.put("_JOB_CLASS", previewVo.getClass());
        contexts.put("_MESSAGE_THREAD_NUM", "1");
        contexts.put("_JOB_CLASS", "I"); // 정보성(I),상업성(A)
        contexts.put("_STATUS", "W");
        contexts.put("_TOTAL_COUNT", "1");
        contexts.put("_MERGE_MAKE_DONE", "N");
        contexts.put("_PREVIEW_YN", "Y");

        if("A".equals(abType)) {
            contexts.put("_LIST_SEQ", "1");
        } else {
            contexts.put("_LIST_SEQ", "2");
        }

        contexts.put("_CHANNEL_TYPE", previewVo.getChannelType());
        contexts.put("_RETMAIL_RECEIVER", previewVo.getRetmailReceiver() == null ? "" : previewVo.getRetmailReceiver());
        contexts.put("_SENDER_NM", previewVo.getSenderNm());
        contexts.put("_SENDER_EMAIL", previewVo.getSenderEmail());

        contexts.put("_SENDER_TEL", previewVo.getSenderTel());
        contexts.put("_RECEIVER_NM", previewVo.getReceiverNm());
        contexts.put("_RETRY_CNT", "1");
        contexts.put("_RECE_CON", "N");
        String resultSeq = String.valueOf(Util.getResultSeq());
        contexts.put("_RESULT_SEQ", resultSeq);

        String tid = new StringBuffer(31).append(StringUtil.leftPad(String.valueOf(aid), 15, '0')).append(StringUtil.leftPad(resultSeq, 16, '0')).toString();
        contexts.put("_TID", tid); // 임의 TID 생성
        contexts.put("_CUSTOMER_NIC", customerkey.trim()); // 장홍준 파일생성시 고객키값도

        // 같이 넣는다
        contexts.put("_CREATE_FILENAME", tid + "_" + customerkey.trim());
        contexts.put("_PREVIEW_YN", "Y"); // MAS에서 이 값이 Y일 경우에는 발송을 안하고
        // execPreviewTemplate() 한다.
        contexts.put("_RECOVER_SERVER_SID", "MAS1");
        contexts.put("_REQUEST_TIME", sendDate + "0000");

        // 실시간인 경우 추가
        if("R".equals(previewVo.getServiceType())) {
            String args[] = realtimeArg.split(",");
            for(int cnt = 0; cnt < args.length; cnt++) {
                int index = args[cnt].indexOf("=");
                contexts.put(args[cnt].substring(0, index), args[cnt].substring(index + 1));
            }
        }
        // 준실시간인 경우 처리(NVREALTIMEACCEPT 테이블 조회)
        else if("N".equals(previewVo.getSubType())) {
            getDefferedTargetDbInfo(contexts, aid, customerkey, previewVo.getSubType(), "");
        } else {
            Integer segmentNo;
            if(isOmniChannelSubCampaign(serviceType, previewVo.getRelationType(), previewVo.getDepthNo())) {
                CampaignVo campaignVo = campaignDao.selectParentCampaignInfo(previewVo.getScenarioNo());
                segmentNo = campaignVo.getSegmentNo();
            } else {
                segmentNo = previewVo.getSegmentNo();
            }

            getTargetDbInfo(contexts, aid, segmentNo, customerkey, previewVo.getSubType());
        }

        // 시작일시,종료일시를 얻는다.
        contexts.put("_START_DATE", sendDate.replaceAll("-", ""));
        contexts.put("_END_DATE", "00000000");

        if("M".equals(previewVo.getChannelType())) {
            //A/B 제목 사용
            final String servicePreface = previewVo.getServicePreface();
            if("S".equals(abTestType)) {
                contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);
                final String servicePrefaceAb = previewVo.getServicePrefaceAb();
                contexts.put("_SERVICE_PREFACE_AB", servicePrefaceAb == null ? "" : servicePrefaceAb);
            } else if("T".equals(abTestType)) {
                //A/B 템플릿 사용
                contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);
            } else {
                //A/B 미사용
                contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);
            }
        }

        if("EC".equalsIgnoreCase(serviceType)) {
            setAdditionalEcareInfo(contexts, previewVo, aid);
        }

        if(handlerType.equals("G")) {
            groovyHandlerWrite(contexts);
        }

        return contexts;
    }

    /**
     * <pre>
     * 이케어를 미리보기할 때 필요한 메일 타입, 첨부파일 목록, 쿼리 목록 데이터를 추가한다.
     * </pre>
     *
     * @param contexts
     * @param previewVo
     * @param ecareNo
     */
    private void setAdditionalEcareInfo(ScrProperties contexts, MailPreviewVo previewVo, int ecareNo) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        if ("M".equals(previewVo.getChannelType())) {
            contexts.put("_MAIL_TYPE", previewVo.getMailType());

            List<MultipartFileVo> multipartFileVos = ecareMultipartFileDao.selectEditorEcareMultipartFile(ecareNo);
            List<Map> fileList = new ArrayList<>();

            for (MultipartFileVo multipartFileVo : multipartFileVos) {
                fileList.add(objectMapper.convertValue(multipartFileVo, Map.class));
            }

            contexts.put("_ATTACH_LIST", fileList);
        }

        List<AddQueryVo> addQueryVos = addQueryDao.selectAddQuery(ecareNo);
        List<Map> queryList = new ArrayList<>();

        for (AddQueryVo addQueryVo : addQueryVos) {
            queryList.add(objectMapper.convertValue(addQueryVo, Map.class));
        }

        contexts.put("_QUERY_LIST", queryList);
    }

    public List<MailPreviewVo> selectTemplateAb(String serviceType, int serviceNo, String seg, String abTestType, String abType) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceNo", serviceNo);
        map.put("seg", seg.equals("기본") || seg.equals("") ? " " : seg);

        if(serviceType.equalsIgnoreCase("EM")) {
            //A/B 기능 사용
            if(!"N".equals(abTestType)) {

                //A/B 템플릿 사용
                if("T".equals(abType)) {
                    return templateDao.selectCampaignTemplateAb(map);
                }
                //A/B 제목 사용
                else {
                    return templateDao.selectCampaignTemplate(map);
                }
            } else {
                return templateDao.selectCampaignTemplate(map);
            }
        } else {
            return ecareTemplateDao.selectEcareTemplate(map);
        }
    }

    // A/B 테스트 재발송/다시보기
    public Hashtable<String, Object> makePreviousAbTest(int aid, String customerkey, String client, String subType, String resultSeq, String listSeq, String abTestType, String slot1) throws Exception {
        init(previewConf);
        dbinit();
        File dir = new File(Config.getMTSSpool());
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getSpool() + "/preview/fax");
        if(!dir.exists())
            dir.mkdirs();
        dir = new File(Config.getLog());
        if(!dir.exists())
            dir.mkdirs();

        Hashtable<String, Object> table = new Hashtable<>();
        ScrProperties contexts = new ScrProperties();
        String fileName = null;

        BufferedReader in = null;
        try {
            log.info("===============================================================");
            log.info(" Preview Mail Make AbTest Start..");
            contexts = TargetInfoReAbTest(resultSeq, listSeq, aid, customerkey, client, subType, abTestType, slot1);
            log.info("===============================================================");
            // 채널 타입을 넘겨준다.
            table.put("channelType", contexts.get("_CHANNEL_TYPE"));

            boolean isNormal = executeHandler(contexts);

            if(isNormal) {
                if("M".equals(contexts.get("_CHANNEL_TYPE"))) {
                    fileName = Config.getServerRoot() + "/spool/preview/" + contexts.get("_CREATE_FILENAME").toString() + ".mime";

                    ReadingMime parse = new ReadingMime(fileName, null);

                    parse.mimeReading();

                    parse.mimeParse();

                    table.put("content", parse.getContent());
                    table.put("attachFile", parse.getAttachFiles());
                    table.put("to", parse.getTo());
                    table.put("from", parse.getFrom());
                    table.put("subject", parse.getSubject());

                    File f = new File(fileName);
                    if(f.delete()) {
                        log.info(fileName + "삭제 완료");
                    } else {
                        log.warn(fileName + "삭제 실패");
                    }

                } else if("F".equals(contexts.get("_CHANNEL_TYPE"))) {
                    fileName = Config.getServerRoot() + "/spool/preview/fax/" + contexts.get("_CREATE_FILENAME").toString() + ".html";

                    in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                    String temp = null;
                    StringBuilder str = new StringBuilder();

                    while((temp = in.readLine()) != null) {
                        str.append(temp + CRLF);
                    }

                    table.put("content", str.toString());
                    table.put("subject", contexts.get("_SUBJECT") == null ? "" : (String) contexts.get("_SUBJECT"));

                    File f = new File(fileName);
                    if(f.delete()) {
                        log.info(fileName + "삭제 완료");
                    } else {
                        log.warn(fileName + "삭제 실패");
                    }
                } else if("A".equals(contexts.get("_CHANNEL_TYPE"))) {
                    table.put("content", (String) contexts.get("contents"));
                } else {
                    table.put("content", (String) contexts.get("contents"));
                    table.put("subject", contexts.get("subject") == null ? "" : (String) contexts.get("subject"));
                }
            } else {
                table.put("to", "");
                table.put("from", "");
                table.put("content", contexts.get("_EXCEPTION_MESSAGE"));
            }

            log.info(" Preview Mail Make Completed!!");
            log.info("===============================================================");
        } catch(Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            deleteSpoolFiles(contexts);
            IOUtil.closeQuietly(in);
        }

        return table;
    }

    // A/B 테스트  다시보기
    public ScrProperties TargetInfoReAbTest(String resultSeq, String listSeq, int aid, String customerkey, String serviceType, String subType, String abTestType, String slot1) throws Exception {
        String handler = null;
        String handlerType = null;

        String abType = slot1.substring(1);

        ScrProperties contexts = new ScrProperties();
        contexts.put("_HANDLER_ID", "1");
        contexts.put("_SERVER_ROOT", Config.getServerRoot());
        contexts.put("_LOG", Config.getLog());
        contexts.put("_BUFFERING_SIZE", "1000");
        contexts.put("_MAX_USERS_PER_DOMAIN", "1");
        contexts.put("_jdbc.driver", jdbcDriver);
        contexts.put("_jdbc.url", jdbcUrl);
        contexts.put("_jdbc.user", jdbcUser);
        contexts.put("_jdbc.password", jdbcPassword);
        contexts.put("_jdbc.encoding", "");

        //N:미사용, S:제목, T:템플릿
        contexts.put("_AB_TEST_TYPE", abTestType);
        //A:첫번째 (제목 or 템플릿) B: 두번째 (제목 or 템플릿)
        contexts.put("_AB_TYPE", abType);

        MailPreviewVo previewVo = selectServicePrevious(aid, serviceType, resultSeq);
        handler = previewVo.getAppsource().trim();
        handlerType = previewVo.getHandlerType();
        contexts.put("_HANDLER", handler); // 핸들러
        contexts.put("_HANDLER_TYPE", handlerType); // 핸들러
        contexts.put("_CAMPAIGN_NAME", previewVo.getServiceNm());

        List<MailPreviewVo> coverTemplate = selectTemplatePreviousAbTest(aid, serviceType, resultSeq, abTestType, abType);

        for(int i = 0; i < coverTemplate.size(); i++) {
            MailPreviewVo mpv = coverTemplate.get(i);

            log.info("seg" + i + ":" + mpv.getSeg());

            //A/B 템플릿 사용
            if("T".equals(abTestType)) {
                contexts.put("_TEMPLATE", coverTemplate.get(0).getTemplate());
                contexts.put("_TEMPLATE_AB", coverTemplate.get(1).getTemplate());
            } else {
                //A/B 제목 사용(제목은 템플릿 1개 seg = " ")
                if(mpv.getSeg().trim().equals("")) {
                    contexts.put("_TEMPLATE", mpv.getTemplate());
                }
            }
        }

        contexts.put("_SPOOL", Config.getSpool());
        contexts.put("_SEND_MODE", "R"); // Real or Test
        String serviceTypeString = "SCHEDULE";
        if("R".equals(previewVo.getServiceType())) {
            serviceTypeString = "REALTIME";
        }

        DecimalFormat df = new DecimalFormat("000000000000000");
        contexts.put("_CAMPAIGN_NO", df.format(new Integer(aid)));
        contexts.put("_TYPE", serviceTypeString);
        contexts.put("_CLIENT", serviceType.toLowerCase()); // em or ec
        contexts.put("_JOB_CLASS", previewVo.getClass());
        contexts.put("_MESSAGE_THREAD_NUM", "1");
        contexts.put("_JOB_CLASS", "I"); // 정보성(I),상업성(A)
        contexts.put("_STATUS", "W");
        contexts.put("_TOTAL_COUNT", "1");
        contexts.put("_MERGE_MAKE_DONE", "N");
        contexts.put("_PREVIEW_YN", "Y");
        final String channelType = previewVo.getChannelType();
        contexts.put("_CHANNEL_TYPE", channelType);
        contexts.put("_RETMAIL_RECEIVER", previewVo.getRetmailReceiver() == null ? "" : previewVo.getRetmailReceiver());
        contexts.put("_SENDER_NM", previewVo.getSenderNm());
        contexts.put("_SENDER_EMAIL", previewVo.getSenderEmail());
        contexts.put("_SENDER_TEL", previewVo.getSenderTel());
        contexts.put("_RECEIVER_NM", previewVo.getReceiverNm());
        contexts.put("_RETRY_CNT", "1");
        contexts.put("_RECE_CON", "N");
        contexts.put("_RESULT_SEQ", resultSeq);

        if("A".equals(abType)) {
            contexts.put("_LIST_SEQ", "1");
        } else {
            contexts.put("_LIST_SEQ", "2");
        }

        String tid = new StringBuffer(31).append(StringUtil.leftPad(String.valueOf(aid), 15, '0')).append(StringUtil.leftPad(resultSeq, 16, '0')).toString();
        contexts.put("_TID", tid); // 임의 TID 생성
        contexts.put("_CUSTOMER_NIC", customerkey.trim()); // 장홍준 파일생성시 고객키값도
        contexts.put("_CREATE_FILENAME", tid + "_" + customerkey.trim());
        contexts.put("_PREVIEW_YN", "Y"); // MAS에서 이 값이 Y일 경우에는 발송을 안하고
        contexts.put("_RECOVER_SERVER_SID", "MAS1");
        contexts.put("_REQUEST_TIME", "0000");
        contexts.put("_START_DATE", "00000000");
        contexts.put("_END_DATE", "00000000");

        final String servicePreface = previewVo.getServicePreface();
        if(Channel.MAIL.equals(channelType)) {
            //A/B 제목 사용
            if("S".equals(abTestType)) {
                contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);
                final String servicePrefaceAb = previewVo.getServicePrefaceAb();
                contexts.put("_SERVICE_PREFACE_AB", servicePrefaceAb == null ? "" : servicePrefaceAb);
            } else if("T".equals(abTestType)) {
                //A/B 템플릿 사용
                contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);
            } else {
                //A/B 미사용
                contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);
            }
        } else if(Channel.SMS.equalsIgnoreCase(channelType)) {
            @SuppressWarnings("unchecked")
            Enumeration<String> enu = contexts.keys();
            String sKey = "";
            Hashtable<String, String> contextsMap = new Hashtable<>();
            while(enu.hasMoreElements()) {
                sKey = enu.nextElement();
                contextsMap.put(sKey, (String) contexts.get(sKey));
            }

            //20170717 KSM 다시보기에서는 Dg파일 임의로 지정해도 상관없음.
            contexts.put("_REQ_DEPT_ID", "preview_dept_id");
            contexts.put("_REQ_USER_ID", "preview_user_id");
            contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);
        } else if(Channel.LMSMMS.equalsIgnoreCase(channelType)) {
            contexts.put("_PREVIOUS_TEM_URL", PropertyUtil.getProperty("previous.tem.url"));
            contexts.put("_PREVIOUS_TEM_DIR", PropertyUtil.getProperty("previous.tem.dir"));
            contexts.put("_SERVICE_PREFACE", servicePreface == null ? "" : servicePreface);

            @SuppressWarnings("unchecked")
            Enumeration<String> enu = contexts.keys();
            String sKey = "";
            Hashtable<String, String> contextsMap = new Hashtable<>();
            while(enu.hasMoreElements()) {
                sKey = enu.nextElement();
                contextsMap.put(sKey, (String) contexts.get(sKey));
            }

            contexts.put("_REQ_DEPT_ID", "preview_dept_id");
            contexts.put("_REQ_USER_ID", "preview_user_id");
            contexts.put("_RE_PREVIEW", "Y");
        }

        // 준실시간인 경우 처리(NVREALTIMEACCEPT 테이블 조회)
        if("N".equals(previewVo.getSubType())) {
            getDefferedTargetDbInfo(contexts, aid, customerkey, previewVo.getSubType(), "");
        } else {
            getLstTergetDbInfo(contexts, aid, previewVo.getSegmentNo(), customerkey, previewVo.getSubType(), resultSeq, listSeq);
        }

        if("EC".equalsIgnoreCase(serviceType)) {
            setAdditionalEcareInfo(contexts, previewVo, aid);
        }

        if(handlerType.equals("G")) {
            groovyHandlerWrite(contexts);
        }

        return contexts;
    }

    public List<MailPreviewVo> selectTemplatePreviousAbTest(int serviceNo, String serviceType, String resultSeq, String abTestType, String abType) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceNo", serviceNo);
        map.put("resultSeq", resultSeq);

        if(serviceType.equalsIgnoreCase("EM")) {
            map.put("seg", " ");

            //A/B 템플릿 사용
            if("T".equals(abTestType)) {
                return templateDao.selectCampaignTemplateAb(map);
            } else {  //A/B 제목 사용
                return templateDao.selectCampaignTemplate(map);
            }

        } else {
            MailPreviewVo mailPreviewVo = ecareDao.selectEcarePreviousView(map);
            List<MailPreviewVo> tempVo = new ArrayList<>();
            MailPreviewVo previewTemplate;
            // 템플릿
            previewTemplate = ecareTemplateHistoryDao.selectEcarePreviousTemplate(mailPreviewVo);
            if(previewTemplate != null)
                tempVo.add(previewTemplate);
            // 커버
            previewTemplate = ecareTemplateHistoryDao.selectEcarePreviousCoverTemplate(mailPreviewVo);
            if(previewTemplate != null)
                tempVo.add(previewTemplate);
            // 머리말
            previewTemplate = ecareTemplateHistoryDao.selectEcarePreviousPrefaceTemplate(mailPreviewVo);
            if(previewTemplate != null)
                tempVo.add(previewTemplate);

            return tempVo;
        }
    }

    /**
     * 옴니채널 메시지용 하위 캠페인인지 확인한다.<br>
     * 하위 캠페인이면 본 캠페인의 대상자 정보를 이용하여 미리보기를 실행한다.
     *
     * @param serviceType - "EM": 캠페인
     * @param relationType - 성공("S"), 실패("F"), 오픈("O")
     * @param depthNo - 본 캠페인: 1, 하위 캠페인: 2 이상
     * @return
     */
    private boolean isOmniChannelSubCampaign(String serviceType, final String relationType, int depthNo) {
        return Client.EM.equals(serviceType) && (Const.RELATION_SUCCESS.equals(relationType) || Const.RELATION_FAIL.equals(relationType) || Const.RELATION_OPEN.equals(relationType)) && depthNo > 1;
    }

    /**
     * 핸들러 타입('S', 'G')에 맞에 핸들러 코드를 실행
     *
     * @param contexts
     * @return
     * @throws IOException
     * @throws ResourceException
     * @throws ScriptException
     */
    private boolean executeHandler(ScrProperties contexts) {
        if(log.isDebugEnabled()) {
            contextPrint(contexts);
        }

        boolean isDisplayStackTrace = PropertyUtil.getProperty("display.stacktrace", "off").equals("on");
        contexts.put("_DISPLAY_STACKTRACE", isDisplayStackTrace);

        try {
            if("S".equals(contexts.get("_HANDLER_TYPE"))) {
                CompileFrontEnd parser = new CompileFrontEnd();
                PCode pcode = parser.processFrontEnd((String) contexts.get("_HANDLER"));
                PscriptVM pvm = new PscriptVM(pcode);
                pvm.setContext(contexts);

                StringWriter swOut = new StringWriter();
                pvm.exec(swOut);
            } else {
                String[] roots = new String[] { Config.getMTSSpool() };
                GroovyScriptEngine gse = new GroovyScriptEngine(roots);

                Binding binding = new Binding();
                binding.setProperty("context", contexts);
                binding.setProperty("console", new ASEConsole());
                binding.setProperty("LinkTrace", "#");
                binding.setProperty("Reject", "#");
                binding.setProperty("ReceiveConfirm", "#");

                Object obj = contexts.get("_TID");
                gse.run("H" + contexts.get("_TID") + ".groovy", binding);

                Exception exception = (Exception) contexts.get("_EXCEPTION");
                if(exception != null) {
                    throw exception;
                }
            }
        } catch(Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"alert_bar\" >Handler Error: <span class=\"font_orange\">");
            sb.append(e.toString());

            Throwable cause = e.getCause();
            if(cause != null) {
                sb.append("<br>");
                sb.append(cause);
            }

            sb.append("</span></div>");

            StringBuilder stackSb = new StringBuilder();

            StackTraceElement[] stackTrace = e.getStackTrace();
            for(StackTraceElement element : stackTrace) {
                String fileName = element.getFileName();
                if(fileName != null && fileName.matches("H\\d{31}[.].+")) {
                    int lineNumber = element.getLineNumber();
                    sb.append("Handler Source(");
                    sb.append(lineNumber);
                    sb.append(" line): ");
                    if(lineNumber > 0) {
                        sb.append(((String) contexts.get("_HANDLER")).split("\r\n|\r|\n")[lineNumber - 1]);
                    }
                    sb.append("<br>");
                }

                stackSb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                stackSb.append(element.toString());
                stackSb.append("<br>");
            }

            if(PropertyUtil.getProperty("display.stacktrace", "off").equals("on") && stackSb.length() > 0) {
                sb.append("StackTrace:");
                sb.append("<br>");
                sb.append(stackSb.toString());
            }

            contexts.put("_EXCEPTION_MESSAGE", sb.toString());

            return false;
        }

        return true;
    }

    /**
     * 전문 샘플을 이용해서 템플릿를 미리보기 한다.
     * @param ecareNo
     * @param data
     * @return
     */
    public String makeTemplatePreview(String ecareNo, String data) {
        ScrProperties scrProperties = new ScrProperties();
        scrProperties.put("_PREVIEW_YN", "Y");

        Message message = new Message(scrProperties);

        boolean isDisplayStackTrace = PropertyUtil.getProperty("display.stacktrace", "off").equals("on");
        String templateName = "TEMPLATE";
        try {
            message.setBindingProperty("context", new ScrPropertiesMockup());
            message.setBindingProperty("jRecord", new JsonJeonmun(data));
            message.setBindingProperty("LinkTrace", "#");
            message.setBindingProperty("Reject", "#");
            message.setBindingProperty("ReceiveConfirm", "#");

            List<MailPreviewVo> list = selectTemplate("EC", Integer.parseInt(ecareNo), "");
            MailPreviewVo mailPreviewVo = list.get(0);

            message.prepareGroovyTemplateWithCustomVariable("{#", "#}", "<%=(jRecord.getString(\"", "\"))%>", templateName, mailPreviewVo.getTemplate());
        } catch(Exception e) {
            return message.getExceptionContent(templateName, e, isDisplayStackTrace);
        }

        try {
            return message.execGroovyTemplate(templateName);
        } catch(Exception e) {
            return message.getExceptionContent(templateName, e, isDisplayStackTrace);
        }
    }
}
