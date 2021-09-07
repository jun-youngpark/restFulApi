package com.mnwise.wiseu.web.editor.service;

import static com.mnwise.common.util.StringUtil.CRLF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.base.util.HtmlUtil;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.common.service.AbstractEcareHistoryService;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerDao;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareKmMapDao;
import com.mnwise.wiseu.web.ecare.dao.EcareLinkTraceDao;
import com.mnwise.wiseu.web.ecare.dao.EcareMultipartFileDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateHistoryDao;
import com.mnwise.wiseu.web.editor.model.EcareEditorVo;
import com.mnwise.wiseu.web.editor.model.EcareItemVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.LinkTraceVo;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.wiseu.web.template.dao.MobileContentsDao;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.util.KakaoButtonUtils;

@Service
public class EditorEcareService extends AbstractEcareHistoryService implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EditorEcareService.class);

    @Autowired private EcareDao ecareDao;
    @Autowired private EcareKmMapDao ecareKmMapDao;
    @Autowired private EcareLinkTraceDao ecareLinkTraceDao;
    @Autowired private EcareMultipartFileDao ecareMultipartFileDao;
    @Autowired private EcareTemplateDao ecareTemplateDao;
    @Autowired private EcareTemplateHistoryDao ecareTemplateHistoryDao;
    @Autowired private EcMsgHandlerDao ecMsgHandlerDao;
    @Autowired private EcMsgHandlerHistoryDao ecMsgHandlerHistoryDao;
    @Autowired private MobileContentsDao mobileContentsDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private ServerInfoDao serverInfoDao;

    private ApplicationContext ctx;

    @Value("${update.HT.mode}")
    private String updateHTmode;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public EcareEditorVo selectEditorEcare(int no) throws Exception {
        return ecareDao.selectEditorEcare(no);
    }

    public HandlerVo selectEditorEcareHandler(int no) {
        return ecMsgHandlerDao.selectEcMsgHandlerByPk(no);
    }

    /**
     * 개인화인자에 대한 정보를 조회한다.
     * - 20100507 : 채널에 따라 개인인자 조회하기 위해 채널 인자 추가
     *
     * @throws Exception
     */
    public List<?> selectEditorEcareSemantic(int no, int segmentNo, String serviceType, String iUserId, String channelType) throws Exception {
        if(serviceType.equalsIgnoreCase("S")) { // 스케쥴, 준실시간
            return semanticDao.selectSemanticListBySegmentNo(segmentNo);
        } else { // 실시간
            return checkDefaultEcareKmMap(no, iUserId, channelType);
        }
    }

    /**
     * 개인화인자 값을 확인한다.
     * - 20100507 : 채널에 따라 개인인자 조회하기 위해 채널 인자 추가 및 처리
     *
     * @throws Exception
     */
    private List<EcareItemVo> checkDefaultEcareKmMap(int no, String iUserId, String channelType) throws Exception {
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        String keyUserId = msAccessor.getMessage("editor.segment.userid", localeChangeInterceptor.getLocale());
        String keyName = msAccessor.getMessage("editor.segment.name", localeChangeInterceptor.getLocale());

        // Default Mail
        String key = "IEMAIL";
        String name = msAccessor.getMessage("editor.segment.email", localeChangeInterceptor.getLocale());
        String type = "E";

        if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType) || Channel.PUSH.equals(channelType)) {
            key = "IPHONE";
            name = msAccessor.getMessage("editor.segment.phone", localeChangeInterceptor.getLocale());
            type = Channel.SMS;
        } else if(Channel.FAX.equals(channelType)) {
            key = "IFAX";
            name = msAccessor.getMessage("editor.segment.fax", localeChangeInterceptor.getLocale());
            type = Channel.FAX;
        }

        String[] keys = {
            "IUSERID", "INAME", key
        }; // 키 값
        String[] names = {
            keyUserId, keyName, name
        }; // 키 값 이름
        String[] types = {
            "K", "N", type
        }; // 키 타입
        boolean[] exsits = {
            false, false, false
        }; // 키 유무

        EcareItemVo ecareItemVo = null;
        List<EcareItemVo> list = ecareKmMapDao.selectEditorEcareItem(no);

        // 개인화인자 값이 없으면 K, N, E 값을 추가
        if(list == null || list.size() == 0) {
            if(list == null)
                list = new ArrayList<>();

            for(int i = 0; i < types.length; i++) {
                ecareItemVo = new EcareItemVo();
                ecareItemVo.setEcareNo(no);
                ecareItemVo.setItemCd("CO");
                ecareItemVo.setItemNm(names[i]);
                ecareItemVo.setItemfieldNm(keys[i]);
                ecareItemVo.setItemType(types[i]);
                ecareItemVo.setKnowledgemapId(ecareKmMapDao.selectEditorEcareItemMax());
                ecareItemVo.setUserId(iUserId);

                ecareKmMapDao.insertEcareKmMap(ecareItemVo);

                list.add(ecareItemVo);
            }

            return list;
        }

        // 개인화인자에 키 값이 있는지 체크
        for(int i = 0; i < list.size(); i++) {
            ecareItemVo = list.get(i);

            for(int j = 0; j < types.length; j++) {
                if(ecareItemVo.getItemType() != null && ecareItemVo.getItemType().equals(types[j])) {
                    exsits[j] = true;
                }
            }
        }

        // 없는 키 값 추가
        for(int i = 0; i < exsits.length; i++) {
            if(!exsits[i]) {
                ecareItemVo = new EcareItemVo();
                ecareItemVo.setEcareNo(no);
                ecareItemVo.setItemCd("CO");
                ecareItemVo.setItemNm(names[i]);
                ecareItemVo.setItemfieldNm(keys[i]);
                ecareItemVo.setItemType(types[i]);
                ecareItemVo.setKnowledgemapId(ecareKmMapDao.selectEditorEcareItemMax());

                ecareKmMapDao.insertEcareKmMap(ecareItemVo);

                list.add(ecareItemVo);
            }
        }

        return list;
    }

    /**
     * 템플릿 목록을 조회한다.
     *
     * @param no 서비스 번호 (ecare_no)
     * @param segmentNo 세그먼트 번호
     * @param serviceType 서비스 타입
     * @throws Exception
     */
    public List<TemplateVo> selectEditorEcareTemplate(int no, int segmentNo, String serviceType) throws Exception {
        List<TemplateVo> templateList = ecareTemplateDao.selectEditorEcareTemplate(no);

        for(TemplateVo templateVo : templateList) {
            if(templateVo.getTemplate() == null || templateVo.getTemplate().length() == 0) {
                templateVo.setTemplate("");
            }
        }

        return templateList;
    }

    /**
     * SMS/MMS 템플릿 목록을 조회한다. (저작기 용)
     * - 채널에 따라 개인인자 조회하기 위해 채널 인자 추가(toMarkFactor(.., "S")) SMS/MMS는 같은 개인화인자를 사용하기 때문에 S타입으로 넘겨준다.
     *
     * @param no 서비스 번호 (ecare_no)
     * @param segmentNo 세그먼트 번호
     * @param serviceType 서비스 타입
     * @throws Exception
     */
    public List<TemplateVo> selectEditorPhoneEcareTemplate(int no, int segmentNo, String serviceType, String iUserId) throws Exception {
        List<TemplateVo> templateList = ecareTemplateDao.selectEditorEcareTemplate(no);

        for(TemplateVo templateVo : templateList) {
            String template = templateVo.getTemplate();
            if(template == null || template.length() == 0) {
                templateVo.setTemplate("");
                continue;
            }

            // 에디터에 개인화 정보를 보여줄 때는 변환이 안되는 태그로 바꾼다.
            template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
            template = template.replace("<%record=context.get(\"record\")%>", "");
            templateVo.setTemplate(toMarkFactor(no, segmentNo, serviceType, iUserId, template, "S"));
        }

        return templateList;
    }

    /**
     * MTS 가 해석가능한 개인화인자를 캠페인 저작기에 표시될 개인화인자로 변환한다.
     * - 20100506 : SqlMapEditorCampaignDao 클래스에서 복사해 온뒤 서비스타입,userId 를 추가
     * - 20100506 : 개인 인자변환시 실시간일때의 인자를 처리하는 부분을 추가
     * - 20100507 : 채널에 따라 개인인자 조회하기 위해 채널 인자 추가
     *
     * @param no 캠페인번호
     * @param segmentNo 세그먼트번호
     * @param serviceType 서비스타입
     * @param iUserId userId
     * @param template 템플릿
     * @return 템플릿
     * @throws Exception
     */
    public String toMarkFactor(int no, int segmentNo, String serviceType, String iUserId, String template, String channelType) throws Exception {
        SemanticVo semanticVo = null;
        EcareItemVo ecareItemVo = null;
        StringBuilder sb = new StringBuilder(template);
        Pattern pattern = null;
        Matcher matcher = null;

        @SuppressWarnings("rawtypes")
        List itemList = selectEditorEcareSemantic(no, new Integer(segmentNo), serviceType, iUserId, channelType);
        if(log.isDebugEnabled())
            log.debug("개인인자 수: " + itemList.size());

        // 개인, 전문 인자 변환
        for(int i = 0; i < itemList.size(); i++) {
            if(serviceType.equalsIgnoreCase("S")) { // 스케쥴, 준실시간
                try {
                    semanticVo = (SemanticVo) itemList.get(i);

                    // 전문인자 - NVSEMATIC 테이블에 FIELD_KEY 값이 D 가 2개 이상일 수 없다.
                    if(StringUtil.notEquals(semanticVo.getFieldKey(), "D")) {
                        pattern = Pattern.compile("<%=\\(record.getString\\(" + semanticVo.getFieldSeq() + "\\)\\)%>");
                        matcher = pattern.matcher(sb.toString());

                        while(matcher.find()) {
                            sb.replace(matcher.start(), matcher.end(), "{$" + semanticVo.getFieldDesc() + "$}");
                            matcher = pattern.matcher(sb.toString());
                        }
                    }
                } catch(Exception e) {
                    log.error(null, e);
                }
            } else { // 실시간
                ecareItemVo = (EcareItemVo) itemList.get(i);
                pattern = Pattern.compile("<%=\\(context.get\\(\"" + ecareItemVo.getItemfieldNm() + "\"\\)\\)%>");
                matcher = pattern.matcher(sb.toString());

                while(matcher.find()) {
                    sb.replace(matcher.start(), matcher.end(), "{$" + ecareItemVo.getItemNm() + "$}");
                    matcher = pattern.matcher(sb.toString());
                }
            }

        }

        // 나머지 스크립트릿 기호들을 변환
        String tmpTemplate = sb.toString();
        tmpTemplate = tmpTemplate.replaceAll("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
        tmpTemplate = tmpTemplate.replaceAll("<%", "\\{\\$");
        tmpTemplate = tmpTemplate.replaceAll("%>", "\\$\\}");

        return tmpTemplate;
    }

    public List<TemplateVo> selectEcareTemplateAndKakaoButtons(int no, int segmentNo, String serviceType, String iUserid) throws Exception {
        List<TemplateVo> templateList = ecareTemplateDao.selectEcareTemplateAndKakaoButtons(no);
        for(TemplateVo templateVo : templateList) {
            try {
                String template = templateVo.getTemplate();
                if(template == null || template.length() == 0) {
                    templateVo.setTemplate("");
                }

                // 에디터에 개인화 정보를 보여줄 때는 변환이 안되는 태그로 바꾼다.
                template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
                template = template.replace("<%record=context.get(\"record\")%>", "");
                templateVo.setTemplate(template);
                templateVo.setKakaoButtons(KakaoButtonUtils.convertJsonToHtmlTrElement(templateVo.getKakaoButtons()));
                templateVo.setKakaoQuickReplies(KakaoButtonUtils.convertJsonToHtmlTrElement(templateVo.getKakaoQuickReplies()));
            } catch(IOException e) {
                log.error(e.getMessage(), e);
                templateVo.setKakaoButtons("");
                templateVo.setKakaoQuickReplies("");
            }
        }

        return templateList;
    }

    /**
     * 이케어 템플릿 저장
     * - 20110514 : 그루비 템플릿은 def record=context.get("record") 로 자동생성. 제목 부분 자동추가
     *
     * @param no 이케어번호
     * @param templateVos 템플릿 리스트
     * @return
     * @throws Exception
     */
    public int updateEditorEcareTemplate(int no, List<TemplateVo> templateVos) {
        int cnt = 0;
        for (TemplateVo templateVo : templateVos) {
            if (templateVo != null) {
                templateVo.setNo(no);
                cnt += ecareTemplateDao.updateEditorEcareTemplate(templateVo);
            }
        }
        return cnt;
    }

    public int updateEditorEcare(EcareEditorVo editorVo) throws Exception {
        int no = editorVo.getNo();
        String handler = editorVo.getHandler();
        String handlerType = editorVo.getHandlerType();
        int segmentNo = editorVo.getSegmentNo();
        List<TemplateVo> templateVos = editorVo.getTemplateVos();
        String serviceType = editorVo.getServiceType();
        String subType = editorVo.getSubType();
        String iUserId = editorVo.getUserId();
        String channelType = editorVo.getChannelType();
        int resendEcareNo = ecareDao.selectResendEcareNo(editorVo.getNo());

        if(serviceType.equals("S") && subType.equals("S") && channelType.equals(Channel.MAIL) && resendEcareNo != 0 && !updateHTmode.equals("4")) {
            if(updateHTmode.equals("1") || updateHTmode.equals("3")) {
                updateEditorEcareHandler(resendEcareNo, handler, handlerType);
            }
            if(updateHTmode.equals("2") || updateHTmode.equals("3")) {
                updateEditorEcareTemplate(resendEcareNo, templateVos);
            }
        }

        //카카오 챗버블 , 템플릿코드, 발신프로필 키 설정
        if(ChannelUtil.isKakao(channelType)) {
            MobileVo mobileVo = new MobileVo();
            mobileVo.setContsNo(templateVos.get(0).getContsNo());
            MobileVo kakaoVo = mobileContentsDao.selectKakaoTemplate(mobileVo);
            templateVos.get(0).setTemplate(kakaoVo.getContsTxt());
            templateVos.get(0).setKakaoButtons(kakaoVo.getKakaoButtons());
        }

        //2017.06.19 HISTORY 저장 시 개인화 태그 변경 필요. KSM
        String template = "";
        for(TemplateVo templateVo : templateVos) {
            template = templateVo.getTemplate();
            template = prePersonalizeFactor(no, segmentNo, serviceType, handlerType, iUserId, template, channelType);
            // 20180921. 재발송시 개인화 처리 문제 때문에 원본템플릿 저장 로직과 동일하게 editorEcareDao 의 변환 로직을 그대로 사용하도록 적용..
            templateVo.setTemplate(toPersonalizeFactor(no, segmentNo, serviceType, iUserId, template, channelType));
        }

        // START 20170321 홍휘정 버전저장기능 추가 -----------
        List<TemplateVo> changedTemplateList = new LinkedList<>();
        List<TemplateVo> oldTemplateList = selectEditorEcareTemplate(no, segmentNo, serviceType);
        if(oldTemplateList.size() > 0) {
            for(TemplateVo oldTemplate : oldTemplateList) {
                for(TemplateVo newTemplate : templateVos) {
                    if(StringUtil.equals(oldTemplate.getSeg(), newTemplate.getSeg())) {
                        if(isNotEqualTemplates(oldTemplate, newTemplate)                            // 템플릿 유형이 같고 조회 시 \n이 \r\n으로 조회되는 경우가 있음. 비교를 위해 replace
                            || isNotEqualContsNo(channelType, oldTemplate, newTemplate)) {
                            newTemplate.setTmplVer(ecareTemplateHistoryDao.selectNextTemplateVersion(no, oldTemplate.getSeg()));
                            changedTemplateList.add(newTemplate);
                        }
                    }
                }
            }
        } else { /// 최초 이케어 등록시
            for(TemplateVo newTemplate : templateVos) {
                newTemplate.setTmplVer(1);
                changedTemplateList.add(newTemplate);
            }
        }

        // 핸들러 버전 처리
        String changedHandler = null; // 비교후 핸들러를 담을 변수
        HandlerVo oldHanler = selectEditorEcareHandler(no);
        int nextHandlerVer = 0;
        // 최초 이케어 등록시(oldHanler == null) 또는 핸들러 내용 변경이 없는 경우
        // 조회 시 \n이 \r\n으로 조회되는 경우가 있음. 비교를 위해 replace
        if((oldHanler == null || oldHanler.getHandler() == null) || StringUtil.equals(handler.replaceAll("\r\n", "\n"), oldHanler.getHandler().replaceAll("\r\n", "\n")) == false) {
            changedHandler = handler;
            nextHandlerVer = ecMsgHandlerHistoryDao.selectNextHandlerVersion(no);
        }
        // handler->changedHandler로 변경, 버전추가
        return updateEditorEcare(editorVo, changedHandler, nextHandlerVer, changedTemplateList);
    }

    /**
     * 캠페인 저작기 개인화인자를 MTS 가 해석가능한 context 나 record 로 변환한다.
     * - 20100506 : SqlMapEditorCampaignDao 클래스에서 복사해 온뒤 서비스타입,userId 를 추가
     * - 20100507 : 실시간일때의 처리도 추가
     * - 20100507 : 채널에 따라 개인인자 조회하기 위해 채널 인자 추가
     *
     * @param no 캠페인번호
     * @param segmentNo 세그먼트번호
     * @param template 템플릿
     * @param channelType 채널유형
     * @return 템플릿
     * @throws Exception
     */
    public String toPersonalizeFactor(int no, int segmentNo, String serviceType, String iUserId, String template, String channelType) throws Exception {
        SemanticVo semanticVo = null;
        EcareItemVo ecareItemVo = null;
        StringBuilder sb = new StringBuilder(template);

        Pattern pattern = null;
        Matcher matcher = null;

        // 링크추적, 수신거부, 수신확인 링크 표시를 디비에 넣을 값으로 변경
        String[] markLinks = {
            "\\{\\$=\\(LinkTrace\\)\\$\\}", "\\{\\$=\\(Reject\\)\\$\\}", "\\{\\$=\\(ReceiveConfirm\\)\\$\\}"
        };
        String[] links = {
            "<%=(LinkTrace)%>", "<%=(Reject)%>", "<br/><img width='0' height='0' src='<%=(ReceiveConfirm)%>'/>"
        };

        for(int i = 0; i < links.length; i++) {
            pattern = Pattern.compile(markLinks[i]);
            matcher = pattern.matcher(sb.toString());

            while(matcher.find()) {
                sb.replace(matcher.start(), matcher.end(), links[i]);
            }
        }

        // 개인인자 조회
        @SuppressWarnings("rawtypes")
        List itemList = selectEditorEcareSemantic(no, new Integer(segmentNo), serviceType, iUserId, channelType);
        if(log.isDebugEnabled())
            log.debug("개인인자 수: " + itemList.size());

        // 개인, 전문 인자 변환
        for(int i = 0; i < itemList.size(); i++) {
            try {
                if(serviceType.equalsIgnoreCase("R")) {
                    ecareItemVo = (EcareItemVo) itemList.get(i);
                    pattern = Pattern.compile("\\{\\$" + ecareItemVo.getItemNm() + "\\$\\}");
                    matcher = pattern.matcher(sb.toString());

                    while(matcher.find()) {
                        sb.replace(matcher.start(), matcher.end(), "<%=(context.get(\"" + ecareItemVo.getItemfieldNm() + "\"))%>");
                        matcher = pattern.matcher(sb.toString());
                    }
                } else {
                    semanticVo = (SemanticVo) itemList.get(i);

                    // 전문인자 - NVSEMATIC 테이블에 FIELD_KEY 값이 D 가 2개 이상일 수 없다.
                    if(StringUtil.notEquals(semanticVo.getFieldKey(), "D")) {
                        pattern = Pattern.compile("\\{\\$" + semanticVo.getFieldDesc() + "\\$\\}");
                        matcher = pattern.matcher(sb.toString());

                        while(matcher.find()) {
                            sb.replace(matcher.start(), matcher.end(), "<%=(record.getString(" + semanticVo.getFieldSeq() + "))%>");
                            matcher = pattern.matcher(sb.toString());
                        }
                    }
                }
            } catch(Exception e) {
                log.error(null, e);
            }
        }

        // 나머지 스크립트릿 기호들을 변환
        String tmpTemplate = sb.toString();
        tmpTemplate = tmpTemplate.replaceAll("\\{\\$", "<%");
        tmpTemplate = tmpTemplate.replaceAll("\\$\\}", "%>");

        return tmpTemplate;
    }

    /**
     * 이케어 핸들러 저장
     *
     * @param no 이케어번호
     * @param handler 핸들러
     * @param handlerType 핸들러타입(S:스크립트, G:그루비)
     * @return
     * @throws Exception
     */
    public int updateEditorEcareHandler(int no, String handler, String handlerType) throws Exception {
        HandlerVo handlerVo = new HandlerVo();
        handlerVo.setNo(no);
        handlerVo.setHandler(handler);
        handlerVo.setType(handlerType);

        int count = ecMsgHandlerDao.selectEditorEcareHandlerCount(no);
        if(count > 0) {
            count = ecMsgHandlerDao.updateEditorEcareHandler(handlerVo);
        } else {
            count = ecMsgHandlerDao.insertEcMsgHandler(handlerVo);
        }

        return count;
    }

    /**
     * 이케어정보를 저장한다.
     *
     * @param preface 이케어제목
     * @param templateType 템플릿타입(1:메일핸들러기본, 11:수정된메일핸들러, 2:SMS핸들러, 3:MMS핸들러)=> 개선 후 11:수정핸들러, 그 외 기본핸들러)
     * @param handler 핸들러
     * @param handlerType 핸들러타입(S:스크립트, G:그루비)
     * @param segmentNo 세그먼트번호
     * @param template 템플릿
     * @param seg 세그(멀티템플릿정보)
     * @param openclickPath 수신확인URL
     * @param serviceType 서비스타입(S:스케쥴 및 준실시간, R:실시간)
     * @param channelType 채널타입(M:메일, S:SMS, T:MMS, F:팩스) modHistory : 이케어 수정 이력 추가
     * @param handlerSeq
     * @throws Exception
     */
    public int updateEditorEcare(EcareEditorVo ecareEditorVo, String handler, int nextHandlerVer,List<TemplateVo> templateVos) throws Exception {
        String handlerType = ecareEditorVo.getHandlerType();
        int segmentNo = ecareEditorVo.getSegmentNo();
        String serviceType = ecareEditorVo.getServiceType();
        int no = ecareEditorVo.getNo();
        String iUserId = ecareEditorVo.getUserId();
        String historyMsg = ecareEditorVo.getHistoryMsg();

        // 버전 추가
        ecareEditorVo.setCoverVer(getTmplVer(templateVos, "cover"));
        //ecareEditorVo.setPrefaceVer(getTmplVer(templateVos, "preface"));
        ecareEditorVo.setTmplVer(getTmplVer(templateVos, " "));
        ecareEditorVo.setHandlerVer(nextHandlerVer);
        if(Channel.ALIMTALK.equals(ecareEditorVo.getChannelType())
            || Channel.BRANDTALK.equals(ecareEditorVo.getChannelType())) {
            if(!templateVos.isEmpty()) {
                ecareEditorVo.setKakaoSenderkey(templateVos.get(0).getKakaoSenderKey());
                ecareEditorVo.setKakaoTemplateCd(templateVos.get(0).getKakaoTmplCd());
            }
        }
        // 이케어 테이블 데이터 적재(버전정보,카카오정보)
        ecareDao.updateEditorEcare(ecareEditorVo);

        if(handler != null) {           // 핸들러 테이블 데이터 적재
            updateEditorEcareHandler(ecareEditorVo.getNo(), handler, ecareEditorVo.getHandlerType());
        }
        if(templateVos != null) {           // 템플릿 테이블 데이터 적재
            updateEditorEcareTemplate(no, templateVos);
        }
        // 컨텐츠 변경 이력 테이블 데이터 적재
        insertEcareHistory(no, iUserId, handler, handlerType, templateVos, historyMsg, nextHandlerVer);

        return 1;
    }

    private boolean isNotEqualContsNo(String channelType, TemplateVo oldTemplate, TemplateVo newTemplate) {
        return channelType.equals(Channel.ALIMTALK) &&
            oldTemplate.getContsNo() != newTemplate.getContsNo();
    }


    private boolean isNotEqualTemplates(TemplateVo oldTemplate, TemplateVo newTemplate) {
        return !StringUtil.equals(oldTemplate.getTemplate().replaceAll("\r\n", "\n"), newTemplate.getTemplate().replaceAll("\r\n", "\n"));
    }

    public String prePersonalizeFactor(int no, int segmentNo, String serviceType, String handlerType, String iUserId, String template, String channelType) {
        StringBuilder sb = new StringBuilder();

        if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)
            || Channel.ALIMTALK.equals(channelType) || Channel.PUSH.equals(channelType)) {
            template = HtmlUtil.htmlCodeConverter(template);
            template = template.replaceAll("<br\\s*/*>", "\r\n");
            template = template.replaceAll("<div>", "").replaceAll("</div>", "");
            template = template.replaceAll("<p>", "").replaceAll("</p>", "");
        }
        sb.append(template);

        return sb.toString();
    }

    public int selectEditorEcareLinkseqMax(int no) throws Exception {
        return ecareLinkTraceDao.selectEditorEcareLinkseqMax(no);
    }

    /**
     * 이케어 링크트레이스 정보를 저장한다.
     * @throws Exception
     */
    public int updateEditorEcareLinktrace(List<LinkTraceVo> linktraceVos) throws Exception {
        int count = 0;

        for(LinkTraceVo linktraceVo : linktraceVos) {
            if(linktraceVo.getLinkYn().equalsIgnoreCase("n"))
                continue;

            count = ecareLinkTraceDao.selectEditorEcareLinktraceExist(linktraceVo);

            if(count == 0) {
                ecareLinkTraceDao.insertEcareLinkTrace(linktraceVo);
            }
        }

        return 1;
    }

    public int deleteEditorEcareItem(int no, String itemfieldNm) throws Exception {
        return ecareKmMapDao.deleteEcareKmMapByPk(no, itemfieldNm);
    }

    /**
     * 이케어 실시간 개인화인자 값을 추가한다.
     * @throws Exception
     */
    public String updateEditorEcareItem(int ecareNo, String knowledgemapId, String grpCd, String userId, String itemCd, String itemNm, int itemindent, String itemfieldNm, int itemLength,
        String itemType, String itemFormat, String itemVal) throws Exception {
        EcareItemVo ecareItemVo = new EcareItemVo();
        ecareItemVo.setEcareNo(ecareNo);
        ecareItemVo.setKnowledgemapId(knowledgemapId);
        ecareItemVo.setGrpCd(grpCd);
        ecareItemVo.setUserId(userId);
        ecareItemVo.setItemCd(itemCd);
        ecareItemVo.setItemNm(itemNm);
        ecareItemVo.setItemindent(itemindent);
        ecareItemVo.setItemfieldNm(itemfieldNm);
        ecareItemVo.setItemLength(itemLength);
        ecareItemVo.setItemType(itemType);
        ecareItemVo.setItemFormat(itemFormat);
        ecareItemVo.setQuerySeq(0);
        ecareItemVo.setItemVal(itemVal);
        log.debug(ecareItemVo.toString());

        // KnowledgemapId이 존재하면 Update 없으면 Insert
        int count = ecareKmMapDao.selectEditorEcareItemCount(ecareItemVo);

        security.securityObject(ecareItemVo, "ENCRYPT");
        if(count == 0) {
            ecareItemVo.setKnowledgemapId(ecareKmMapDao.selectEditorEcareItemMax());
            //SECUDB NONE
            ecareKmMapDao.insertEditorEcareItem(ecareItemVo);
            return ecareItemVo.getKnowledgemapId();
        } else if(count == 1) {
            //SECUDB NONE
            ecareKmMapDao.updateEditorEcareItem(ecareItemVo);
            return "2";
        } else {
            return "0";
        }
    }

    public int deleteEditorEcareTemplate(int no, String seg) throws Exception {
        return ecareTemplateDao.deleteEcareTemplateByPk(no, seg);
    }

    public List<MultipartFileVo> selectEditorEcareMultipartfile(int no) {
        return ecareMultipartFileDao.selectEditorEcareMultipartFile(no);
    }

    public int selectEditorEcareMultipartfileMax(int ecareNo) throws Exception {
        return ecareMultipartFileDao.selectEditorEcareMultipartFileMax(ecareNo);
    }

    public void insertEditorEcareMultipartfile(MultipartFileVo multipartfileVo) {
        ecareMultipartFileDao.insertEcareMultipartFile(multipartfileVo);
    }

    public int deleteEditorEcareMultipartfile(int no, String fileAlias) throws Exception {
        String filePath = null;
        File file = null;

        MultipartFileVo multipartfileVo = new MultipartFileVo();
        multipartfileVo.setNo(no);
        multipartfileVo.setFileAlias(fileAlias);

        MultipartFileVo multipartFile = ecareMultipartFileDao.selectEditorEcareMultipartFileOne(multipartfileVo);

        if(multipartFile != null) {
            filePath = ((MultipartFileVo) multipartFile).getFilePath();
            file = new File(filePath);

            if(file.exists()) {
                if(!file.delete()) {
                    return 0;
                }
            }
        }

        return ecareMultipartFileDao.deleteEditorEcareMultipartfile(multipartfileVo);
    }

    public List<EcareItemVo> insertEcareKmMap(int no, String handler, String template) throws Exception {
        List<String> addItemList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(handler);
        sb.append(CRLF);
        sb.append(template);

        Pattern pattern = Pattern.compile("context.get.*?\\)");
        Matcher matcher = pattern.matcher(sb.toString());

        // 핸들러, 템플릿에서 개인화인자를 뽑아온다.
        String factor = null;
        int start = -1;
        int end = -1;
        while(matcher.find()) {
            factor = matcher.group();
            start = factor.indexOf("\"") + 1;
            end = factor.lastIndexOf("\"");

            addItemList.add(factor.substring(start, end));
        }

        // 현 디비에 있는 개인화인자
        List<EcareItemVo> list = ecareKmMapDao.selectEditorEcareItem(no);
        EcareItemVo ecareItemVo = null;

        // 디비에 있는 개인화인자와 핸들러, 템플릿에 있는 중복되는 인자는 제외
        List<String> removeItemList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            ecareItemVo = list.get(i);

            if(addItemList.contains(ecareItemVo.getItemfieldNm())) {
                removeItemList.add(ecareItemVo.getItemfieldNm());
            }
        }
        addItemList.removeAll(removeItemList);

        // 추가되어야 할 개인화인자들을 디비에 넣음
        for(int i = 0; i < addItemList.size(); i++) {
            ecareItemVo = new EcareItemVo();
            ecareItemVo.setEcareNo(no);
            ecareItemVo.setItemCd("CO");
            ecareItemVo.setItemNm(null);
            ecareItemVo.setItemfieldNm(addItemList.get(i));
            ecareItemVo.setItemType(null);

            ecareKmMapDao.insertEcareKmMap(ecareItemVo);

            list.add(ecareItemVo);
        }

        return list;
    }

    public void makeAlimtalkFormFile(HttpServletResponse response) {
        final ReportExcelDownload reportExcel = new ReportExcelDownload();
        try {
            String filePath = StringUtil.escapeFilePath(PropertyUtil.getProperty("web.root") + "/WEB-INF/report/template/ecare/alimtalk_template_form.xlsx");
            reportExcel.downLoadFile(response, filePath);
        } catch(Exception e) {
            log.error("while making excel file, io exception occurred. " + e.getMessage());
        }
    }


    public void mergeMultipartFile(EcareEditorVo editorVo) {
        //파일이 존재할경우 삭제
        if(ecareMultipartFileDao.selectEditorEcareMultipartFileCount(editorVo.getNo())> 0){
             if(ecareMultipartFileDao.deleteEcareMultipartFile(editorVo.getNo()) == 0) {
                 return;
             }
        }
        // 첨부 파일 등록
        for(MultipartFileVo multipartFileVo : editorVo.getMultipartFileVos()) {
            multipartFileVo.setNo(editorVo.getNo());
            multipartFileVo.setSeq(ecareMultipartFileDao.selectEditorEcareMultipartFileMax(editorVo.getNo()));
            ecareMultipartFileDao.insertEcareMultipartFile(multipartFileVo);
        }

    }
}
