package com.mnwise.wiseu.web.env.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.Const.Client;
import com.mnwise.wiseu.web.base.Const.MsgType;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.campaign.dao.ApplicationDao;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.common.dao.CommonDao;
import com.mnwise.wiseu.web.common.service.AbstractEcareHistoryService;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerDao;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.editor.model.CampaignEditorVo;
import com.mnwise.wiseu.web.editor.model.DefaultHandlerVo;
import com.mnwise.wiseu.web.editor.model.EcareEditorVo;
import com.mnwise.wiseu.web.env.dao.DefaultHandlerDao;

@Service
public class EnvDefaultHandleService extends AbstractEcareHistoryService implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EnvDefaultHandleService.class);

    @Autowired private ApplicationDao applicationDao;
    @Autowired private CampaignDao campaignDao;
    @Autowired private CommonDao commonDao;
    @Autowired private DefaultHandlerDao defaultHandleDao;
    @Autowired private EcareDao ecareDao;
    @Autowired private EcMsgHandlerDao ecMsgHandlerDao;
    @Autowired private EcMsgHandlerHistoryDao ecMsgHandlerHistoryDao;
    @Autowired private DefaultHandlerDao defaultHandlerDao;

    private ApplicationContext ctx;

    @Value("${web.root}")
    private String webRoot;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러 목록을 가져온다.
     *
     * @param defaultHandler
     * @return
     * @throws Exception
     */
    public List<DefaultHandlerVo> getHandlerList(DefaultHandlerVo defaultHandler) throws Exception {
        return defaultHandleDao.getHandlerList(defaultHandler);
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러를 등록한다.
     *
     * @param defaultHandler
     */
    public void insertHandler(DefaultHandlerVo defaultHandler) {
        defaultHandleDao.insertHandler(defaultHandler);
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러를 변경한다.
     *
     * @param defaultHandler
     */
    public void deleteHandler(DefaultHandlerVo defaultHandler) {
        defaultHandleDao.deleteHandler(defaultHandler);
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러를 가져온다.
     *
     * @param defaultHandler
     * @return
     */
    public DefaultHandlerVo selectDefaultHandler(int seq) {
        return defaultHandleDao.selectDefaultHandler(seq);
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러를 변경한다.
     *
     * @param defaultHandler
     * @throws Exception
     */
    public Map<String, Object> updateHandler(DefaultHandlerVo defaultHandler) throws Exception {
        Map<String, Object> resultMap = new HashMap<String,Object>();
        String msgType = defaultHandler.getMsgType();
        // 과거버전 지원..
        if("".equals(StringUtil.defaultIfBlank(msgType, ""))) {
            // 캠페인
            if("".equals(StringUtil.defaultIfBlank(defaultHandler.getServiceType(),""))) {
                defaultHandler.setMsgType(MsgType.CAMPAIGN);
            }else {
                defaultHandler.setMsgType(MsgType.ECARE);
                msgType = MsgType.ECARE;
            }
        }

        defaultHandleDao.updateDefaultHandlerByPk(defaultHandler);

        // 사용중인 이케어 리스트의 핸들러 정보 업데이트
        int success = 0, tmp=0, pass=0, error=0, total=0;

        // 변경된 핸들러를 사용하는 서비스 확인. 발송에러(O),발송완료(E) 상태 제외.
        List<CaseInsensitiveMap> serviceList = commonDao.selectHandlerUseServiceList(defaultHandler.getSeq());
        total=serviceList.size();

        for(int i=0; i < total; i++) {
            try {
                CaseInsensitiveMap serviceMap = serviceList.get(i);
                // 혹시 MSSQL 에서 오류생기면 Integer.parseInt(String.valueOf()) 이거로 해야함.
                int templateType = ((java.math.BigDecimal)serviceMap.get("TEMPLATE_TYPE")).intValue();
                int serviceNo = ((java.math.BigDecimal)serviceMap.get("SERVICE_NO")).intValue();
                // 수정상태인 핸들러는 거른다.
                if(11==templateType) {
                    pass++;
                    continue;
                }

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("serviceNo", serviceMap.get("SERVICE_NO"));
                paramMap.put("handlerSeq", defaultHandler.getSeq());
                paramMap.put("handler", defaultHandler.getHandler());

                // 핸들러 업데이트
                if(MsgType.ECARE.equalsIgnoreCase(msgType)) {
                    tmp = ecMsgHandlerDao.updateEcareHandlerFromDefault(paramMap);
                } else if(MsgType.CAMPAIGN.equalsIgnoreCase(msgType)) {
                    tmp = applicationDao.updateCampaignHandlerFromDefault(paramMap);
                } else {
                    tmp = 0;
                }

                // 이케어정보에 최신 핸들러히스토리 번호를 지정
                if(MsgType.ECARE.equalsIgnoreCase(msgType) && tmp > 0 ) {
                    int nextHandlerVer = ecMsgHandlerHistoryDao.selectNextHandlerVersion(serviceNo);
                    EcareEditorVo ecareEditorVo = new EcareEditorVo();
                    ecareEditorVo.setTemplateType(templateType);
                    ecareEditorVo.setEcarePreface((String)serviceMap.get("SERVICE_PREFACE"));
                    ecareEditorVo.setHandlerVer(nextHandlerVer);
                    ecareEditorVo.setHandlerSeq(defaultHandler.getSeq());
                    ecareEditorVo.setNo(serviceNo);
                    ecareDao.updateEditorEcare(ecareEditorVo);

                    // 히스토리도 추가.
                    insertEcareHistory(
                        serviceNo,
                        defaultHandler.getUserId(), defaultHandler.getHandler(),
                        defaultHandler.getHandleType(), null, "set default", nextHandlerVer);
                }
                success +=tmp;
            } catch(Exception e) {
                log.error(e.getMessage(), e);
                error++;
            }
        }
        resultMap.put("total", total);
        resultMap.put("pass", pass);
        resultMap.put("success", success);
        resultMap.put("error", error);
        return resultMap;
    }

    /**
     * 사용가능한 핸들러 목록을 가져온다.
     *
     * @param serviceGubun
     * @param campaignNo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getHandlerUseList(String serviceGubun, int serviceNo) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if(Client.EM.equals(serviceGubun)) {
            CampaignEditorVo editorVo = campaignDao.selectEditorCampaign(serviceNo);
            resultMap.put("handlerSeq", editorVo.getHandlerSeq());
            resultMap.put("templateType", editorVo.getTemplateType());
        }
        else {
            EcareEditorVo editorVo = ecareDao.selectEditorEcare(serviceNo);
            resultMap.put("handlerSeq", editorVo.getHandlerSeq());
            resultMap.put("templateType", editorVo.getTemplateType());
        }

        resultMap.put("list", defaultHandleDao.selectHandlerUseList(serviceGubun, serviceNo));
        return resultMap;
    }

    /**
     * 기본핸들러 수 를 가져온다
     * @return
     * @throws Exception
     */
    public int selectDefaultHandlerCount(DefaultHandlerVo defaultHandler) throws Exception {
        return defaultHandleDao.selectDefaultHandlerCount(defaultHandler);
    }

    /**
     * 현재 핸들러를 모두 삭제 후, 기본핸들러 생성
     */
    public int insertEditorDefaultHandler(String userId) {
        // 기본핸들러 모두 삭제
        int count = defaultHandlerDao.deleteEditorDefaultHandler();
        if(log.isDebugEnabled())
            log.debug("deleteEditorDefaultHandler count: " + count);

        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");
        String charset = msAccessor.getMessage("common.charset", localeChangeInterceptor.getLocale());
        String surveyKey = "";
        String language = localeChangeInterceptor.getLanguage();

        int seq = 0;

        // 현재시간
        Date date = new Date();
        String createDt = new SimpleDateFormat("yyyyMMdd").format(date);
        String createTm = new SimpleDateFormat("HHmmss").format(date);

        try {
            // 그루비
            File groovyDir = new File(webRoot + "/WEB-INF/resources/handler/groovy");
            File[] groovyClientDir = groovyDir.listFiles();
            File[] groovyHandlerFile = null;

            if(groovyClientDir == null)
                return 0;

            for(int i = 0; i < groovyClientDir.length; i++) {
                groovyHandlerFile = groovyClientDir[i].listFiles();

                for(int j = 0; j < groovyHandlerFile.length; j++) {
                    // 핸들러 파일인지 체크
                    if(groovyHandlerFile[j].getName().indexOf(".han") == -1) {
                        continue;
                    }
                    seq = insertHandler(groovyHandlerFile[j], groovyClientDir[i], userId, "G", charset, surveyKey, language, createDt, createTm, seq);
                }
            }

            // 스크립트
            File scriptDir = new File(webRoot + "/WEB-INF/resources/handler/script");
            File[] scriptClientDir = scriptDir.listFiles();
            File[] scriptHandlerFile = null;

            if(scriptClientDir == null)
                return 0;

            for(int i = 0; i < scriptClientDir.length; i++) {
                scriptHandlerFile = scriptClientDir[i].listFiles();

                for(int j = 0; j < scriptHandlerFile.length; j++) {
                    // 핸들러 파일인지 체크
                    if(scriptHandlerFile[j].getName().indexOf(".han") == -1) {
                        continue;
                    }
                    seq = insertHandler(scriptHandlerFile[j], scriptClientDir[i], userId, "S", charset, surveyKey, language, createDt, createTm, seq);
                }
            }

        } catch(Exception e) {
            log.error(null, e);
        }

        return seq;
    }

    public int insertHandler(File handlerFile, File clientDir, String userId, String handleType, String charset, String surveyKey, String language, String createDt, String createTm, int seq) throws Exception {
        FileInputStream in = null;
        StringBuilder handlerNm = new StringBuilder();

        byte[] b = null;

        int offset = -1;
        String fileName = "";
        // 핸들러에 추가 이름 지정.
        // 핸들러 파일명 구성시 대시(-)를 포함하면 "대시 이후 ~ 확장자 이전까지" 문자열을 등록시 핸들러명 맨 앞에 삽입함.
        // 언더바(_) 는 공백으로 변환함.
        // ex) mail-pdf.han > pdf 메일
        String handlerSubName = "";
        DefaultHandlerVo defaultHandler = new DefaultHandlerVo();

        try {
            offset = handlerFile.getName().indexOf(".");
            if(offset > -1) {
                fileName = handlerFile.getName().substring(0, offset);
            } else {
                fileName = handlerFile.getName();
            }

            in = new FileInputStream(handlerFile);
            b = new byte[in.available()];
            in.read(b);
            IOUtil.closeQuietly(in);

            // 핸들러 파일 이름 '-' 이후 이름 subName
            if(fileName.indexOf("-") > -1) {
                handlerSubName = fileName.substring(fileName.lastIndexOf("-")+1, fileName.length());
                fileName = fileName.substring(0, fileName.lastIndexOf("-"));
                handlerSubName = handlerSubName.replace("_", " ");
                handlerSubName = handlerSubName.toUpperCase();
            }

            defaultHandler.setSeq(++seq);
            //defaultHandler.setHandleNm(groovyClientDir[i].getName());
            //defaultHandler.setHandleDesc(groovyClientDir[i].getName());
            defaultHandler.setServiceType(getServiceType(fileName));
            defaultHandler.setChannel(getChannel(fileName));
            defaultHandler.setHandleType(handleType);
            defaultHandler.setHandleAttr(getHandleAttr(handlerSubName));
            // 다국어 처리 및 설문인 경우 설문키{%질문지%} -> {%Questionnaire%} 처리를 함.
            defaultHandler.setHandler(new String(b).replaceAll("#CHARSET#", charset).replaceAll("#SURVEYKEY#", surveyKey).replaceAll("#LANGUAGE#", language));
            defaultHandler.setUserId(userId);
            defaultHandler.setCreateDt(createDt);
            defaultHandler.setCreateTm(createTm);
            if(fileName.endsWith("_ab")) {
                defaultHandler.setAbTestYn("Y");
                defaultHandler.setHandleNm(defaultHandler.getHandleNm() + "_ab");
            } else {
                defaultHandler.setAbTestYn("N");
            }
            defaultHandler.setMsgType(getMsgType(clientDir.getName()));

            handlerNm.setLength(0);
            if("".equals(handlerSubName)) {}
            else {
                handlerNm.append(handlerSubName).append(" ");
            }
            handlerNm.append(Channel.getChannelName(defaultHandler.getChannel()));
            if("".equals(getServiceTypeName(defaultHandler.getServiceType()))) {}
            else {
                handlerNm.append(" ").append(getServiceTypeName(defaultHandler.getServiceType()));
            }
            if("S".equals(defaultHandler.getHandleType())) {
                handlerNm.append(" script");
            }
            if("S".equals(defaultHandler.getHandleAttr())) {
                handlerNm.append(" security");
            }
            if("Y".equals(defaultHandler.getAbTestYn())) {
                handlerNm.append(" AB");
            }

            defaultHandler.setHandleNm(handlerNm.toString());
            defaultHandler.setHandleDesc(handlerNm.append(" desc").toString());

            log.debug(defaultHandler.toString());
            defaultHandlerDao.insertDefaultHandler(defaultHandler);
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }
        return seq;
    }

    /**
     * 채널 정보
     *
     * @param fileName
     * @return
     */
    public String getChannel(String fileName) {
        if(fileName == null)
            return null;
        else if(fileName.startsWith("mail"))
            return "M";
        else if(fileName.startsWith("sms"))
            return "S";
        else if(fileName.startsWith("mms"))
            return "T";
        else if(fileName.startsWith("fax"))
            return "F";
        else if(fileName.startsWith("push"))
            return "P";
        else if(fileName.startsWith("brandtalk"))
            return "B";
        else if(fileName.startsWith("frtalk"))
            return "C";
        else if(fileName.startsWith("altalk"))
            return "A";
        else
            return null;
    }

    /**
     * 서비스 타입
     *
     * @param fileName
     * @return
     */
    public String getServiceType(String fileName) {
        if(fileName == null)
            return null;
        else if(fileName.endsWith("schedule"))
            return "S";
        else if(fileName.endsWith("nrealtime"))
            return "N";
        else if(fileName.endsWith("realtime"))
            return "R";
        else
            return null;
    }

    public String getServiceTypeName(String serviceType) {
        if("S".equals(serviceType)) {
            return "schedule";
        }
        else if("N".equals(serviceType)) {
            return "nrealtime";
        }
        else if("R".equals(serviceType)) {
            return "realtime";
        } else {
            return "";
        }
    }

    public static String getHandleAttr(String handlerSubName) {
        return StringUtil.isNotBlank(handlerSubName) ? handlerSubName : "D";
    }


    /**
     * 메시지 타입
     * @return
     */
    public static String getMsgType(String fileName) {
        return (MsgType.full.ECARE.toLowerCase().equals(fileName)) ? MsgType.ECARE : MsgType.CAMPAIGN;
    }
}
