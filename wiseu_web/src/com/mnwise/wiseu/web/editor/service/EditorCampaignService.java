package com.mnwise.wiseu.web.editor.service;
import static com.mnwise.common.util.StringUtil.CRLF;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.util.HtmlUtil;
import com.mnwise.wiseu.web.campaign.dao.ApplicationDao;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.LinkTraceDao;
import com.mnwise.wiseu.web.campaign.dao.MultipartFileDao;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.editor.model.CampaignEditorVo;
import com.mnwise.wiseu.web.editor.model.EditorVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.LinkTraceVo;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import static com.mnwise.wiseu.web.editor.util.MailTemplate.rearrange;

@Service
public class EditorCampaignService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(EditorCampaignService.class);

    @Autowired private ApplicationDao applicationDao;
    @Autowired private CampaignDao campaignDao;
    @Autowired private LinkTraceDao linkTraceDao;
    @Autowired private MultipartFileDao multipartFileDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private ServerInfoDao serverInfoDao;
    @Autowired private TemplateDao templateDao;

    public int selectEditorCampaignLinkseqMax(int no) throws Exception {
        return linkTraceDao.selectEditorCampaignLinkseqMax(no);
    }

    public List<SemanticVo> selectEditorCampaignSemantic(int segmentNo) {
        return semanticDao.selectSemanticListBySegmentNo(segmentNo);
    }

    public CampaignEditorVo selectEditorCampaign(int no) throws Exception {
        return campaignDao.selectEditorCampaign(no);
    }

    public HandlerVo selectEditorCampaignHandler(int no) {
        return applicationDao.selectEditorCampaignHandler(no);
    }

    public List<MultipartFileVo> selectEditorCampaignMultipartfile(int no) {
        return multipartFileDao.selectEditorCampaignMultipartFile(no);
    }

    /**
     * 캠페인 템플릿 가져오기 (저작기)
     *
     * @param no
     * @param segmentNo
     * @return
     * @throws Exception
     */
    public List<TemplateVo> selectEditorCampaignTemplate(int no, int segmentNo) throws Exception {
        List<TemplateVo> templateList = templateDao.selectEditorCampaignTemplate(no);

        // 템플릿 수대로 변환 수행
        for(TemplateVo templateVo : templateList) {
            // 템플릿 없다면 초기화
            if(templateVo.getTemplate() == null || templateVo.getTemplate().length() == 0) {
                templateVo.setTemplate("");
                templateVo.setDbTemplate("");
                continue;
            } else {
                templateVo.setDbTemplate(templateVo.getTemplate());
            }

            templateVo.setTemplate(toMarkFactor(no, segmentNo, templateVo.getTemplate()));
        }

        return templateList;
    }

    /**
     * MTS 가 해석가능한 개인화인자를 캠페인 저작기에 표시될 개인화인자로 변환한다.
     *
     * @param no 캠페인번호
     * @param segmentNo 세그먼트번호
     * @param template 템플릿
     * @return 템플릿
     * @throws Exception
     */
    public String toMarkFactor(int no, int segmentNo, String template) throws Exception {
        SemanticVo semanticVo = null;
        StringBuilder sb = new StringBuilder(template);
        Pattern pattern = null;
        Matcher matcher = null;

        // 링크추적, 수신거부, 수신확인 링크 값을 저작기에 표시형태로 변경
        String[] links = {
            "<%=\\(LinkTrace\\)%>",
            "<%=\\(Reject\\)%>",
            "<br/><img width='0' height='0' src='<%=\\(ReceiveConfirm\\)%>'/>"
        };
        String[] markLinks = {
            "{$=(LinkTrace)$}",
            "{$=(Reject)$}",
            "{$=(ReceiveConfirm)$}"
        };

        List<SemanticVo> itemList = semanticDao.selectSemanticListBySegmentNo(segmentNo);
        if(log.isDebugEnabled())
            log.debug("개인인자 수: " + itemList.size());

        for(int i = 0; i < links.length; i++) {
            pattern = Pattern.compile(links[i]);
            matcher = pattern.matcher(sb.toString());

            while(matcher.find()) {
                sb.replace(matcher.start(), matcher.end(), markLinks[i]);
            }
        }

        // 개인, 전문 인자 변환
        for(int i = 0; i < itemList.size(); i++) {
            try {
                semanticVo = itemList.get(i);

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
        }

        // 나머지 스크립트릿 기호들을 변환
        String tmpTemplate = sb.toString();
        tmpTemplate = tmpTemplate.replaceAll("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
        tmpTemplate = tmpTemplate.replaceAll("<%", "\\{\\$");
        tmpTemplate = tmpTemplate.replaceAll("%>", "\\$\\}");

        return tmpTemplate;
    }

    /**
     * SMS,MMS 템플릿을 가져오기 (저작기)
     *
     * @param no
     * @param segmentNo
     * @return
     * @throws Exception
     */
    public List<TemplateVo> selectEditorPhoneCampaignTemplate(int no, int segmentNo) throws Exception {
        List<TemplateVo> templateList = templateDao.selectEditorCampaignTemplate(no);

        // 템플릿 수대로 변환 수행
        for(TemplateVo templateVo : templateList) {
            String template = templateVo.getTemplate();
            if(template == null || template.length() == 0) {
                templateVo.setTemplate("");
                templateVo.setDbTemplate("");
                continue;
            } else {
                templateVo.setDbTemplate(template);
            }

            // 에디터에 개인화 정보를 보여줄 때는 변환이 안되는 태그로 바꾼다.
            template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
            template = template.replace("<%record=context.get(\"record\")%>", "");
            templateVo.setTemplate(toMarkFactor(no, segmentNo, template));
        }

        return templateList;
    }

    /**
     * 캠페인 템플릿 가져오기 (리포트)
     *
     * @param no
     * @param segmentNo
     * @return
     * @throws Exception
     */
    public List<TemplateVo> selectEditorCampaignTemplateForReport(int no, int segmentNo) throws Exception {
        List<TemplateVo> templateList = templateDao.selectEditorCampaignTemplate(no);

        // 템플릿 수대로 변환 수행
        for(TemplateVo templateVo : templateList) {
            // 템플릿 없다면 초기화
            if(templateVo.getTemplate() == null || templateVo.getTemplate().length() == 0) {
                templateVo.setTemplate("");
                templateVo.setDbTemplate("");
                continue;
            } else {
                templateVo.setDbTemplate(templateVo.getTemplate());
            }

            templateVo.setTemplate(toMakrReport(no, segmentNo, templateVo.getTemplate()));
        }

        return templateList;
    }

    /**
     * MTS 가 해석가능한 개인화인자를 캠페인 리포트에 표시될 개인화인자로 변환한다.
     *
     * @param no 캠페인번호
     * @param segmentNo 세그먼트번호
     * @param template 템플릿
     * @return 템플릿
     * @throws Exception
     */
    public String toMakrReport(int no, int segmentNo, String template) throws Exception {
        SemanticVo semanticVo = null;
        Pattern pattern = null;
        Matcher matcher = null;

        StringBuilder sb = new StringBuilder(template);

        List<SemanticVo> itemList = semanticDao.selectSemanticListBySegmentNo(segmentNo);
        if(log.isDebugEnabled())
            log.debug("개인인자 수: " + itemList.size());

        // 개인, 전문 인자 변환
        for(int i = 0; i < itemList.size(); i++) {
            try {
                semanticVo = itemList.get(i);

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
        }

        return sb.toString();
    }

    public int updateEditorCampaign(EditorVo editorVo) throws Exception {
        int no = editorVo.getNo();
        String preface = editorVo.getCampaignPreface();
        String prefaceTest = editorVo.getCampaignPrefaceAb();
        int templateType = editorVo.getTemplateType();
        String handler = editorVo.getHandler();
        String handlerType = editorVo.getHandlerType();
        int segmentNo = editorVo.getSegmentNo();
        String template = editorVo.getTemplateContent();
        String templateAb = editorVo.getTemplateAbContent();
        String seg = editorVo.getSeg();
        String channelType = editorVo.getChannel();
        String abTestType = editorVo.getAbTestType();
        int handlerSeq = editorVo.getHandlerSeq();

        if(!"T".equals(editorVo.getAbTestType())) {
            return updateEditorCampaign(no, preface, prefaceTest, templateType, handler, handlerType, segmentNo, template, seg, channelType, abTestType, handlerSeq);
        } else {
            //A템플릿
            updateEditorCampaign(no, preface, prefaceTest, templateType, handler, handlerType, segmentNo, template, " ", channelType, abTestType, handlerSeq);
            //B템플릿
            return updateEditorCampaign(no, preface, prefaceTest, templateType, handler, handlerType, segmentNo, templateAb, seg, channelType, abTestType, handlerSeq);
        }
    }

    /**
     * NVCAMPAIGN 테이블에 TEMPLATE_TYPE,메일 제목 UPDATE
     *
     * @param : template_type
     * @param : campaignPreface 메일제목
     * @param : no 캠페인번호
     * @throws Exception
     */
    public int updateEditorCampaign(int no, String preface, String prefaceTest, int templateType, String handler, String handlerType, int segmentNo, String template, String seg, String channelType,
        String abTestType, int handlerSeq) throws Exception {
        CampaignEditorVo campaignEditorVo = new CampaignEditorVo();
        campaignEditorVo.setNo(no);
        campaignEditorVo.setCampaignPreface(preface);
        campaignEditorVo.setCampaignPrefaceAb(prefaceTest);
        campaignEditorVo.setTemplateType(templateType);
        campaignEditorVo.setHandlerSeq(handlerSeq);

        if(StringUtil.isNotEmpty(preface)) {
            if(preface.getBytes().length > 250) {
                return 100;
            }
        }
        campaignDao.updateEditorCampaign(campaignEditorVo);

        // SMS,MMSX인 경우에는 js에서 dwr을 이용해서 핸들러,템플릿을 따로 저장한다.
        if(Channel.MAIL.equals(channelType)) {
            updateEditorCampaignHandler(no, handler, handlerType);
            updateEditorCampaignTemplate(no, segmentNo, template, seg, handlerType, abTestType);
        } else if(Channel.FAX.equals(channelType)) {
            updateEditorCampaignHandler(no, handler, handlerType);
            updateEditorCampaignFaxTemplate(no, segmentNo, template, seg, handlerType);
        }

        return 1;
    }

    /**
     * 핸들러 저장
     *
     * @param : no 캠페인번호
     * @param : handler 핸들러내용
     * @param : type 핸들러 타입 (groovy / script)
     */
    public int updateEditorCampaignHandler(int no, String handler, String handlerType) {
        HandlerVo handlerVo = new HandlerVo();
        handlerVo.setNo(no);
        handlerVo.setHandler(handler);
        handlerVo.setType(handlerType);

        int count = applicationDao.selectEditorCampaignHandlerCount(no);
        if(count > 0) {
            count = applicationDao.updateEditorCampaignHandler(handlerVo);
        } else {
            count = applicationDao.insertEditorCampaignHandler(handlerVo);
        }

        return count;
    }

    /**
     * 템플릿 저장
     * @throws Exception
     */
    public int updateEditorCampaignTemplate(int no, int segmentNo, String template, String seg, String handlerType, String abTestType) throws Exception {
        TemplateVo templateVo = new TemplateVo();

        if(template == null) {
            templateVo.setNo(no);
            templateVo.setSeg(seg);
        } else {
            templateVo.setNo(no); // 캠페인번호
            StringBuilder sb = new StringBuilder();

            StringBuilder record = new StringBuilder();
            ServerInfoVo serverinfoVo = serverInfoDao.selectServerInfo();

            if(template.indexOf("context.get(\"record\")") == -1) {
                // 템플릿 선언부
                record.append("<%").append(CRLF);
                if(handlerType.equals("S")) { // 스크립트
                    record.append("record=context.get(\"record\")").append(CRLF);

                    record.append("sb = java.lang.StringBuffer.new()").append(CRLF);
                    record.append("sb.append(\"").append(serverinfoVo.getLinkPath()).append("\")").append(CRLF);
                    record.append(rearrange(serverinfoVo.getAseLinkMergeParam())).append(CRLF);
                    record.append("LinkTrace = ").append("sb.toString()").append(CRLF);

                    // 수신거부 링크
                    record.append("sb.setLength(0)").append(CRLF);
                    record.append("sb.append(\"").append(serverinfoVo.getRejectPath()).append("\")").append(CRLF);
                    record.append(rearrange(serverinfoVo.getAseRejectMergeParam())).append(CRLF);
                    record.append("Reject = ").append("sb.toString()").append(CRLF);

                    // 수신확인 링크
                    record.append("sb.setLength(0)").append(CRLF);
                    record.append("ReceiveConfirm = context.get(\"_RECE_CON\")").append(CRLF);
                    record.append("if(ReceiveConfirm.equals(\"Y\"))").append(CRLF);
                    record.append("sb.append(\"").append(serverinfoVo.getOpenclickPath()).append("\")").append(CRLF);
                    record.append(rearrange(serverinfoVo.getAseOpenScriptlet())).append(CRLF);
                    record.append("ReceiveConfirm = ").append("sb.toString()").append(CRLF);
                    record.append("else").append(CRLF);
                    record.append("ReceiveConfirm = \"\"").append(CRLF);
                    record.append("end if").append(CRLF);
                } else { // 그루비
                    record.append("com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\");").append(CRLF);

                    // 링크추적 링크
                    record.append("StringBuffer sb = new StringBuffer()").append(CRLF);
                    record.append("sb.append(\"").append(serverinfoVo.getLinkPath()).append("\")").append(CRLF);
                    record.append(rearrange(serverinfoVo.getGroovyLinkMergeParam())).append(CRLF);

                    //A/B 미사용시
                    if("N".equals(abTestType)) {
                    } else {
                        // 수신확인시 A/B 구분
                        record.append("sb.append(\"&AB_TYPE=\")").append(CRLF);
                        record.append("sb.append(context.get(\"AB_TYPE\"))").append(CRLF);
                    }

                    record.append("String LinkTrace = ").append("sb.toString()").append(CRLF);

                    // 수신거부 링크
                    record.append("sb.setLength(0)").append(CRLF);
                    record.append("String Reject").append(CRLF);
                    record.append("if(\"Y\".equals(context.get(\"_PREVIEW_YN\"))) {").append(CRLF);
                    record.append("Reject = \"#\"").append(CRLF);
                    record.append("} else {").append(CRLF);
                    record.append("sb.append(\"").append(serverinfoVo.getRejectPath()).append("\")").append(CRLF);
                    record.append(rearrange(serverinfoVo.getGroovyRejectMergeParam())).append(CRLF);
                    record.append("Reject = ").append("sb.toString()").append(CRLF);
                    record.append("}").append(CRLF);

                    // 수신확인 링크
                    record.append("sb.setLength(0)").append(CRLF);
                    record.append("String ReceiveConfirm = context.get(\"_RECE_CON\")").append(CRLF);
                    record.append("if(ReceiveConfirm.equals(\"Y\")) {").append(CRLF);
                    record.append("sb.append(\"").append(serverinfoVo.getOpenclickPath()).append("\")").append(CRLF);
                    record.append(rearrange(serverinfoVo.getGroovyOpenScriptlet())).append(CRLF);

                    //A/B 미사용시
                    if("N".equals(abTestType)) {
                    } else {
                        // 수신확인시 A/B 구분
                        record.append("sb.append(\"&AB_TYPE=\")").append(CRLF);
                        record.append("sb.append(context.get(\"AB_TYPE\"))").append(CRLF);
                    }

                    record.append("ReceiveConfirm = ").append("sb.toString()").append(CRLF);
                    record.append("} else { ReceiveConfirm = \"\" }").append(CRLF);
                }
                record.append("%>").append(CRLF);
                sb.append(record.toString());
            }

            // 링크추적, 수신확인, 수신거부, 개인화, 전문 변환
            sb.append(toPersonalizeFactor(no, segmentNo, template));

            // 수신확인 이미지 태그 추가
            if(sb.indexOf("<%=(ReceiveConfirm)%>") == -1) {
                if(sb.indexOf("</body>") > -1) {
                    sb.insert(sb.indexOf("</body>"), "<br/><img width='0' height='0' src='<%=(ReceiveConfirm)%>'/>");
                } else {
                    sb.insert(sb.length(), "<br/><img width='0' height='0' src='<%=(ReceiveConfirm)%>'/>");
                }
            }

            templateVo.setTemplate(sb.toString());
            templateVo.setSeg(seg);
        }

        // 템플릿이 존재하는지 확인
        int count = templateDao.selectEditorCampaignTemplateCount(templateVo);

        // 템플릿이 존재한다면 업데이트, 없으면 인서트
        if(count > 0) {
            count = templateDao.updateEditorCampaignTemplate(templateVo);
        } else {
            count = templateDao.insertEditorCampaignTemplate(templateVo);
        }

        return count;
    }

    /**
     * 캠페인 저작기 개인화인자를 MTS 가 해석가능한 context 나 record 로 변환한다.
     *
     * @param no 캠페인번호
     * @param segmentNo 세그먼트번호
     * @param template 템플릿
     * @return 템플릿
     * @throws Exception
     */
    public String toPersonalizeFactor(int no, int segmentNo, String template) throws Exception {
        SemanticVo semanticVo = null;
        StringBuilder sb = new StringBuilder(template);

        Pattern pattern = null;
        Matcher matcher = null;

        // 링크추적, 수신거부, 수신확인 링크 표시를 디비에 넣을 값으로 변경
        String[] markLinks = {
            "\\{\\$=\\(LinkTrace\\)\\$\\}",
            "\\{\\$=\\(Reject\\)\\$\\}",
            "\\{\\$=\\(ReceiveConfirm\\)\\$\\}"
        };
        String[] links = {
            "<%=(LinkTrace)%>",
            "<%=(Reject)%>",
            "<br/><img width='0' height='0' src='<%=(ReceiveConfirm)%>'/>"
        };

        for(int i = 0; i < links.length; i++) {
            pattern = Pattern.compile(markLinks[i]);
            matcher = pattern.matcher(sb.toString());

            while(matcher.find()) {
                sb.replace(matcher.start(), matcher.end(), links[i]);
            }
        }

        // 개인인자 조회
        List<SemanticVo> itemList = semanticDao.selectSemanticListBySegmentNo(segmentNo);
        if(log.isDebugEnabled())
            log.debug("개인인자 수: " + itemList.size());

        // 개인, 전문 인자 변환
        for(int i = 0; i < itemList.size(); i++) {
            try {
                semanticVo = itemList.get(i);

                // 전문인자 - NVSEMATIC 테이블에 FIELD_KEY 값이 D 가 2개 이상일 수 없다.
                if(StringUtil.notEquals(semanticVo.getFieldKey(), "D")) {
                    pattern = Pattern.compile("\\{\\$" + semanticVo.getFieldDesc() + "\\$\\}");
                    matcher = pattern.matcher(sb.toString());

                    while(matcher.find()) {
                        sb.replace(matcher.start(), matcher.end(), "<%=(record.getString(" + semanticVo.getFieldSeq() + "))%>");
                        matcher = pattern.matcher(sb.toString());
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
     * FAX 템플릿을 저장한다.
     * @throws Exception
     */
    public int updateEditorCampaignFaxTemplate(int no, int segmentNo, String template, String seg, String handlerType) throws Exception {
        TemplateVo templateVo = new TemplateVo();

        if(template == null) {
            templateVo.setNo(no);
            templateVo.setSeg(seg);
        } else {
            templateVo.setNo(no); // 캠페인번호
            StringBuilder sb = new StringBuilder();

            StringBuilder record = new StringBuilder();

            if(template.indexOf("context.get(\"record\")") == -1) {
                // 템플릿 선언부
                record.append("<%").append(CRLF);
                if(handlerType.equals("S")) { // 스크립트
                    record.append("record=context.get(\"record\")").append(CRLF);
                } else { // 그루비
                    record.append("com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\");").append(CRLF);
                }
                record.append("%>").append(CRLF);
                sb.append(record.toString());
            }

            // 링크추적, 수신확인, 수신거부, 개인화, 전문 변환
            sb.append(toPersonalizeFactor(no, segmentNo, template));

            templateVo.setTemplate(sb.toString());
            templateVo.setSeg(seg);
        }

        // 템플릿이 존재하는지 확인
        int count = templateDao.selectEditorCampaignTemplateCount(templateVo);

        // 템플릿이 존재한다면 업데이트, 없으면 인서트
        if(count > 0) {
            count = templateDao.updateEditorCampaignTemplate(templateVo);
        } else {
            count = templateDao.insertEditorCampaignTemplate(templateVo);
        }

        return count;
    }

    /**
     * 링크트레이스 저장
     */
    public int updateEditorCampaignLinktrace(List<LinkTraceVo> linktraceVos) throws Exception {
        int count = 0;

        //for(int i = 0; i < linktraceVos.length; i++) {
        for(LinkTraceVo linktraceVo : linktraceVos) {
            if(("n").equalsIgnoreCase(linktraceVo.getLinkYn()))
                continue;

            count = linkTraceDao.selectEditorCampaignLinktraceExist(linktraceVo);

            if(count == 0) {
                linktraceVo.setNo(linktraceVo.getNo());
                linkTraceDao.insertLinkTrace(linktraceVo);
            }
        }

        return 1;
    }

    public int selectEditorCampaignMultipartfileMax() throws Exception {
        return multipartFileDao.selectEditorCampaignMultipartfileMax();
    }

    public void insertEditorCampaignMultipartfile(MultipartFileVo multipartfileVo) throws Exception {
        multipartFileDao.insertMultipartFile(multipartfileVo);
    }

    public int deleteEditorCampaignMultipartfile(int no, String fileAlias) throws Exception {
        MultipartFileVo multipartfileVo = new MultipartFileVo();
        multipartfileVo.setNo(no);
        multipartfileVo.setFileAlias(fileAlias);

        MultipartFileVo obj = multipartFileDao.selectEditorCampaignMultipartFileOne(multipartfileVo);

        if(obj != null) {
            String filePath = obj.getFilePath();
            File file = new File(filePath);

            if(file.exists()) {
                if(!file.delete()) {
                    return 0;
                }
            }
        }

        return multipartFileDao.deleteEditorCampaignMultipartFile(multipartfileVo);
    }

    public int deleteEditorCampaignTemplate(int no, String seg) throws Exception {
        return templateDao.deleteTemplateByPk(no, seg);
    }

    /**
     * SMS 템플릿을 저장한다.
     * @throws Exception
     */
    public int updateEditorCampaignSmsHandler(int no, String handler, String type) throws Exception {
        HandlerVo handlerVo = new HandlerVo();
        handlerVo.setNo(no);
        handlerVo.setHandler(handler);
        handlerVo.setType(type);

        int count = applicationDao.selectEditorCampaignHandlerCount(no);
        if(count > 0) {
            count = applicationDao.updateEditorCampaignHandler(handlerVo);
        } else {
            count = applicationDao.insertEditorCampaignHandler(handlerVo);
        }

        return count;
    }

    /**
     * 캠페인 템플릿을 업데이트한다.
     * @throws Exception
     */
    public int updateEditorCampaignSmsTemplate(int no, int segmentNo, String template, String seg, String handlerType) throws Exception {
        TemplateVo templateVo = new TemplateVo();

        if(template == null) {
            templateVo.setNo(no);
            templateVo.setSeg(seg);
        } else {
            template = HtmlUtil.htmlCodeConverter(template);

            templateVo.setNo(no);
            StringBuilder sbTemplate = null;
            // 템플릿저장 후 로드시 저작기에서 // 공백을 &nbsp로 변환함하여 // 저장시 처리
            if(template.indexOf("context.get(\"record\")") == -1) {
                sbTemplate = new StringBuilder();
                if(handlerType.equals("S")) {
                    sbTemplate.append("<%record=context.get(\"record\")%>");
                } else {
                    sbTemplate.append("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>");
                }

                if(log.isDebugEnabled())
                    log.debug("SMS segmentNo -> " + segmentNo);

                // 개인화 태그 {% %} -> <% %>, {^ ^} -> (" ") 로 변경

                sbTemplate.append(toPersonalizeFactor(no, segmentNo, template));
                templateVo.setTemplate(sbTemplate.toString());
                templateVo.setSeg(seg);
            } else {
                templateVo.setTemplate(toPersonalizeFactor(no, segmentNo, template));
                templateVo.setSeg(seg);
            }
        }

        // 템플릿이 존재하는지 확인
        int count = templateDao.selectEditorCampaignTemplateCount(templateVo);

        // 템플릿이 존재한다면 업데이트, 없으면 인서트
        if(count > 0) {
            count = templateDao.updateEditorCampaignTemplate(templateVo);
        } else {
            count = templateDao.insertEditorCampaignTemplate(templateVo);
        }

        return count;
    }

    /**
     * MMS 핸들러를 저장한다.
     */
    public int updateEditorCampaignMmsHandler(int no, String handler, String type) throws Exception {
        HandlerVo handlerVo = new HandlerVo();
        handlerVo.setNo(no);
        handlerVo.setHandler(handler);
        handlerVo.setType(type);

        int count = applicationDao.selectEditorCampaignHandlerCount(no);
        if(count > 0) {
            count = applicationDao.updateEditorCampaignHandler(handlerVo);
        } else {
            count = applicationDao.insertEditorCampaignHandler(handlerVo);
        }

        return count;
    }

    /**
     * MMS 템플릿을 저장한다.
     * @throws Exception
     */
    public int updateEditorCampaignMmsTemplate(int no, int segmentNo, String template, String seg, String handlerType) throws Exception {
        TemplateVo templateVo = new TemplateVo();

        if(template == null) {
            templateVo.setNo(no);
            templateVo.setSeg(seg);
        } else {
            template = template.replaceAll("&lt;", "<");
            template = template.replaceAll("&gt;", ">");
            template = template.replaceAll("&amp;", "&");
            template = template.replaceAll("&quot;", "\"");
            template = template.replaceAll("&nbsp;", " ");
            template = template.replaceAll("&middot;", "·");
            template = template.replaceAll("&hellip;", "…");

            templateVo.setNo(no);
            StringBuffer sbTemplate = new StringBuffer();
            if(handlerType.equals("S")) {
                sbTemplate.append("<%record=context.get(\"record\")%>");
            } else {
                sbTemplate.append("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>");
            }

            if(log.isDebugEnabled())
                log.debug("SMS segmentNo -> " + segmentNo);

            // 개인화 태그 {% %} -> <% %>, {^ ^} -> (" ") 로 변경
            sbTemplate.append(toPersonalizeFactor(no, segmentNo, template));
            //sbTemplate.append(template);

            templateVo.setTemplate(sbTemplate.toString());
            templateVo.setSeg(seg);
        }

        // 템플릿이 존재하는지 확인
        int count = templateDao.selectEditorCampaignTemplateCount(templateVo);

        // 템플릿이 존재한다면 업데이트, 없으면 인서트
        if(count > 0) {
            count = templateDao.updateEditorCampaignTemplate(templateVo);
        } else {
            count = templateDao.insertEditorCampaignTemplate(templateVo);
        }

        return count;
    }

    public int updateEditorCampaignKakaoTemplate(int no, int segmentNo, String template, String seg, String handlerType, String kakaoButtons) throws Exception {
        int cnt = updateEditorCampaignMmsTemplate(no, segmentNo, template, seg, handlerType);

        TemplateVo templateVo = new TemplateVo();
        templateVo.setNo(no);
        templateVo.setSeg(seg);
        templateVo.setKakaoButtons(kakaoButtons);

        templateDao.updateCampaignKakaoButtons(templateVo);
        return cnt;
    }

    /**
     * PUSH 템플릿 저장.
     *
     * @param no
     * @param segmentNo
     * @param template
     * @param seg
     * @param handlerType
     * @return
     * @throws Exception
     */
    public int updateEditorCampaignPushTemplate(int no, int segmentNo, String template, String seg, String handlerType) throws Exception {
        TemplateVo templateVo = new TemplateVo();

        if(template == null) {
            templateVo.setNo(no);
            templateVo.setSeg(seg);
        } else {
            templateVo.setNo(no);
            StringBuffer sbTemplate = new StringBuffer();

            if(handlerType.equals("S")) {
                sbTemplate.append("<%record=context.get(\"record\")%>");
            } else {
                sbTemplate.append("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>");
            }

            template = HtmlUtil.htmlCodeConverter(template);

            // 개인화 태그 {% %} -> <% %>, {^ ^} -> (" ") 로 변경
            sbTemplate.append(template);

            templateVo.setTemplate(toPersonalizeFactor(no, segmentNo, sbTemplate.toString()));
            templateVo.setSeg(seg);
        }

        int count = templateDao.selectEditorCampaignTemplateCount(templateVo);
        if(count > 0) {
            count = templateDao.updateEditorCampaignTemplate(templateVo);
        } else {
            count = templateDao.insertEditorCampaignTemplate(templateVo);
        }

        return count;
    }

    /**
     * 친구톡 템플릿을 가져오기 (저작기)
     *
     * @param no
     * @param segmentNo
     * @return
     * @throws Exception
     */
    public List<TemplateVo> selectEditorPhoneCampaignFrtalkTemplate(int no, int segmentNo) throws Exception {
        List<TemplateVo> templateList = templateDao.selectEditorCampaignFrtalkTemplate(no);

        // 템플릿 수대로 변환 수행
        for(TemplateVo templateVo : templateList) {
            String template = templateVo.getTemplate();
            if(template == null || template.length() == 0) {
                templateVo.setTemplate("");
                templateVo.setDbTemplate("");
                continue;
            } else {
                templateVo.setDbTemplate(template);
            }

            // 에디터에 개인화 정보를 보여줄 때는 변환이 안되는 태그로 바꾼다.
            template = template.replace("<%com.mnwise.ASE.agent.util.TextReader record=context.get(\"record\")%>", "");
            template = template.replace("<%record=context.get(\"record\")%>", "");
            templateVo.setTemplate(toMarkFactor(no, segmentNo, template));
        }

        return templateList;
    }

}
