package com.mnwise.wiseu.web.editor.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.Const.ServiceType;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.editor.model.EditorVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.LinkTraceVo;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.editor.service.EditorCampaignService;
import com.mnwise.wiseu.web.editor.service.EditorEcareService;
import com.mnwise.wiseu.web.segment.model.SemanticVo;


@Controller
public class EditorForwardController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorForwardController.class);

    @Autowired private EditorCampaignService editorCampaignService;
    @Autowired private EditorEcareService editorEcareService;
    @Autowired private PushService pushService;

    /**
     * 각 채널 별 editor.jsp로 건네주는 역할을 한다.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/editor/editor.do", method=RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            // 캠페인 인자 type, campaignNo, segmentNo, handlerType, channelType
            String type = ServletRequestUtils.getStringParameter(request, "type");

            int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignNo", 0);
            int segmentNo = ServletRequestUtils.getIntParameter(request, "segmentNo", 0);
            String handlerType = ServletRequestUtils.getStringParameter(request, "handlerType");
            String channelType = ServletRequestUtils.getStringParameter(request, "channelType");
            //A/B 테스트 구분 값
            String abTestType = ServletRequestUtils.getStringParameter(request, "abTestType");

            // 이케어 인자 type, ecareNo, segmentNo, serviceType, handlerType,
            // channelType
            int ecareNo = ServletRequestUtils.getIntParameter(request, "ecareNo", 0);
            String serviceType = ServletRequestUtils.getStringParameter(request, "serviceType");
            String sendOne = ServletRequestUtils.getStringParameter(request, "sendOne");
            // 저작기 다국어설정 사용 여부 인자
            String useMultiLang = ServletRequestUtils.getStringParameter(request, "useMultiLang");

            // webExecMode 1: wiseu / 2: wiseMoka
            String webExecMode = ServletRequestUtils.getStringParameter(request, "webExecMode");

            String forwardJsp = null;
            String channelFileName= Const.enumChannel.findName(channelType);
            if(type.equals("campaign")) {
                if(channelType.equals(Channel.PUSH)) {
                     if(0 < campaignNo && Channel.PUSH.equals(channelType)) {
                         returnData.put("pushInfoMap", pushService.getPushServiceInfo(ServiceType.CAMPAIGN, campaignNo));
                         returnData.put("pushMsgInfoList", pushService.selectPushMsgTypeList(true));
                     }
                 }
                 returnData.put("campaignNo", String.valueOf(campaignNo));
             } else if(type.equals("ecare")) {
                 if(channelType.equals(Channel.PUSH)) {
                     if(0 < ecareNo && Channel.PUSH.equals(channelType)) {
                         returnData.put("pushInfoMap", pushService.getPushServiceInfo(ServiceType.ECARE, ecareNo));
                         returnData.put("pushMsgInfoList", pushService.selectPushMsgTypeList(true));
                     }
                 }
                 returnData.put("ecareNo", String.valueOf(ecareNo));
                 returnData.put("serviceType", serviceType);
                 returnData.put("sendOne", sendOne);
             }
             forwardJsp = "/editor/editor_" + type + "_" + channelFileName;  // "/editor/"+type+"_"+channelFileName+"_editor"
             // 자바스크립트명 추가
             returnData.put("editorJsFileName", type + "Editor_" + channelFileName + ".js");  // type+channelFileName.substring(0,1).toUpperCase() + channelFileName.substring(1) + "Editor.js"

             // 캠페인, 이케어 공통
            returnData.put("segmentNo", String.valueOf(segmentNo));
            returnData.put("handlerType", handlerType);
            returnData.put("useMultiLang", useMultiLang);
            returnData.put("abTestType", abTestType);
            returnData.put("webExecMode", webExecMode);

            return new ModelAndView(forwardJsp, returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/selectEditor.json", method=RequestMethod.POST)  // /editor/{serviceNo}/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public Object selectEditor(String type, int serviceNo) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    return editorEcareService.selectEditorEcare(serviceNo);
                case ServiceType.CAMPAIGN :
                    return editorCampaignService.selectEditorCampaign(serviceNo);
            }

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/selectEditorHandler.json", method=RequestMethod.POST)  // /editor/{serviceNo}/handler/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public HandlerVo selectEditorHandler(String type, int serviceNo) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    return editorEcareService.selectEditorEcareHandler(serviceNo);
                case ServiceType.CAMPAIGN :
                    return editorCampaignService.selectEditorCampaignHandler(serviceNo);
            }

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/selectEditorMultipartfile.json", method=RequestMethod.POST)  // /editor/{serviceNo}/file/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public List<MultipartFileVo> selectEditorMultipartfile(String type, int serviceNo) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    return editorEcareService.selectEditorEcareMultipartfile(serviceNo);
                case ServiceType.CAMPAIGN :
                    return editorCampaignService.selectEditorCampaignMultipartfile(serviceNo);
            }

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/selectEditorCampaignSemantic.json", method=RequestMethod.POST)  // /editor/{segmentNo}/segment/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public List<SemanticVo> selectEditorCampaignSemantic(int segmentNo) {
        try {
            return editorCampaignService.selectEditorCampaignSemantic(segmentNo);
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }

    @RequestMapping(value="/editor/selectEditorEcareSemantic.json", method=RequestMethod.POST)  // /editor/{segmentNo}/segment/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public List<?> selectEditorEcareSemantic(@RequestBody EditorVo editorVo) {
        try {
            return editorEcareService.selectEditorEcareSemantic(editorVo.getNo(), editorVo.getSegmentNo(), editorVo.getServiceType(), editorVo.getUserId(), editorVo.getChannel());
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }

    @RequestMapping(value="/editor/selectEditorTemplate.json", method=RequestMethod.POST)  // /editor/{serviceNo}/{segmentNo}/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public List<TemplateVo> selectEditorTemplate(String type, int serviceNo, int segmentNo, @RequestParam(value="serviceType", required = false) String serviceType) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    return editorEcareService.selectEditorEcareTemplate(serviceNo , segmentNo , serviceType);
                case ServiceType.CAMPAIGN :
                    return editorCampaignService.selectEditorCampaignTemplate(serviceNo, segmentNo);
            }

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/selectEditorMaxLinkSeq.json", method=RequestMethod.GET)  // /editor/{serviceNo}/link/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public int selectEditorLinkseqMax(String type, int serviceNo) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    return editorEcareService.selectEditorEcareLinkseqMax(serviceNo);
                case ServiceType.CAMPAIGN :
                    return editorCampaignService.selectEditorCampaignLinkseqMax(serviceNo);
            }

            return 0;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/updateEditorLinktrace.json", method=RequestMethod.POST)  // /editor/linkTrace/{type}_editor.do, method=RequestMethod.PUT
    @ResponseBody public int updateEditorLinktrace(String type, @RequestBody List<LinkTraceVo> linktraceVos) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    return editorEcareService.updateEditorEcareLinktrace(linktraceVos);
                case ServiceType.CAMPAIGN :
                    return editorCampaignService.updateEditorCampaignLinktrace(linktraceVos);
            }

            return 0;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/selectEditorPhoneTemplate.json", method=RequestMethod.POST)  // /editor/{serviceNo}/{segmentNo}/{channelType}/{type}_editor.do, method=RequestMethod.GET
    @ResponseBody public List<TemplateVo> selectEditorPhoneTemplate(String type, String channelType, @RequestBody EditorVo editorVo) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    switch(channelType.toUpperCase()) {
                        case Channel.ALIMTALK :
                        case Channel.BRANDTALK :
                            return editorEcareService.selectEcareTemplateAndKakaoButtons(editorVo.getNo(), editorVo.getSegmentNo(), editorVo.getServiceType(), editorVo.getUserId());
                        case Channel.FRIENDTALK :
                        case Channel.SMS :
                        case Channel.LMSMMS :
                        case Channel.PUSH :
                            return editorEcareService.selectEditorPhoneEcareTemplate(editorVo.getNo(), editorVo.getSegmentNo(), editorVo.getServiceType(), editorVo.getUserId());
                    }

                case ServiceType.CAMPAIGN :
                    switch(channelType.toUpperCase()) {
                        case Channel.FRIENDTALK :
                        case Channel.BRANDTALK :
                            return editorCampaignService.selectEditorPhoneCampaignFrtalkTemplate(editorVo.getNo(), editorVo.getSegmentNo());
                        case Channel.SMS :
                        case Channel.LMSMMS :
                        case Channel.PUSH :
                            return editorCampaignService.selectEditorPhoneCampaignTemplate(editorVo.getNo(), editorVo.getSegmentNo());
                    }
            }

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/updateEditor.json", method=RequestMethod.POST)  // /editor/{type}_editor.do, method=RequestMethod.PUT
    @ResponseBody public int updateEditor(String type, @RequestBody EditorVo editorVo) throws Exception {
        try {
            switch(type.toUpperCase()) {
                case ServiceType.CAMPAIGN :
                    return editorCampaignService.updateEditorCampaign(editorVo);
            }

            return 0;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/updateEditorHandler.json", method=RequestMethod.POST)  // /editor/{channelType}/{type}_editor.do, method=RequestMethod.PUT
    @ResponseBody public int updateEditorHandler(String type, String channelType, @RequestBody EditorVo editorVo) throws Exception {
        try {
            int result = -1;

            switch(channelType.toUpperCase()) {
                case Channel.SMS :
                    switch(type.toUpperCase()) {
                        case ServiceType.CAMPAIGN :
                            result = editorCampaignService.updateEditorCampaignSmsHandler(editorVo.getNo(), editorVo.getHandler(), editorVo.getHandlerType());
                            break;
                    }

                    break;
                case Channel.FRIENDTALK :
                case Channel.ALIMTALK :
                case Channel.PUSH :
                case Channel.LMSMMS :
                case Channel.BRANDTALK:
                    switch(type.toUpperCase()) {
                         case ServiceType.CAMPAIGN :
                            result = editorCampaignService.updateEditorCampaignMmsHandler(editorVo.getNo(),
                                editorVo.getHandler(), editorVo.getHandlerType());
                            break;
                    }

                    break;
            }

            return result;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/updateEditorTemplate.json", method=RequestMethod.POST)  // /editor/{channelType}/template/{type}_editor.do, method=RequestMethod.PUT
    @ResponseBody public int updateEditorTemplate(String type, String channelType, @RequestBody EditorVo editorVo) throws Exception {
        try {
            int result = 0;

            switch(type.toUpperCase()) {
                case ServiceType.CAMPAIGN :
                    switch(channelType.toUpperCase()) {
                        case Channel.SMS :
                            result = editorCampaignService.updateEditorCampaignSmsTemplate(editorVo.getNo(), editorVo.getSegmentNo()
                                , editorVo.getTemplateContent(), editorVo.getSeg(), editorVo.getHandlerType());
                            break;
                        case Channel.FRIENDTALK :
                        case Channel.BRANDTALK:
                            result = editorCampaignService.updateEditorCampaignKakaoTemplate(editorVo.getNo(),
                                editorVo.getSegmentNo(), editorVo.getTemplateContent(), editorVo.getSeg(),
                                editorVo.getHandlerType(), editorVo.getKakaoButtons());
                            break;
                        case Channel.LMSMMS :
                            result = editorCampaignService.updateEditorCampaignMmsTemplate(editorVo.getNo(), editorVo.getSegmentNo()
                                , editorVo.getTemplateContent(), editorVo.getSeg(), editorVo.getHandlerType());
                            break;
                        case Channel.PUSH :
                            result = editorCampaignService.updateEditorCampaignPushTemplate(editorVo.getNo(),
                                editorVo.getSegmentNo(), editorVo.getTemplateContent(), editorVo.getSeg(), editorVo.getHandlerType());
                    }
                    break;
            }

            return result;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/editor/deleteEditorCampaignMultipartfile.json", method=RequestMethod.POST)  // /editor/{serviceNo}/{surveyNo}/file/{type}_editor.do, method=RequestMethod.DELETE
    @ResponseBody public ResultDto deleteEditorCampaignMultipartfile(String type, int serviceNo, String fileAlias) {
        try {
            int row = 0;
            switch(type.toUpperCase()) {
                case ServiceType.ECARE :
                    row = editorEcareService.deleteEditorEcareMultipartfile(serviceNo, fileAlias);
                case ServiceType.CAMPAIGN :
                    row = editorCampaignService.deleteEditorCampaignMultipartfile(serviceNo, fileAlias);
            }

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(row);
            return resultDto;
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 개인화 저장 버튼 클릭 - 핸들러 / 템플러에 있는 개인화 인자 저장
     *
     * @param editorVo
     * @return
     * @throws Exception
     */
    /*@RequestMapping(value="/editor/insertEcareKmMap.json", method=RequestMethod.POST)  // /editor/ecareKm/editor.do, method=RequestMethod.PUT
    @ResponseBody public List<EcareItemVo> insertEcareKmMap(@RequestBody EditorVo editorVo) throws Exception {
        try {
            return editorEcareService.insertEcareKmMap(editorVo.getNo(), editorVo.getHandler(), editorVo.getTemplateContent());
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }*/

    /**
     * 실시간 개인인자 추가/수정
     *
     * @param ecareNo
     * @param knowledgemapId
     * @param grpCd
     * @param userId
     * @param itemCd
     * @param itemNm
     * @param itemindent
     * @param itemfieldNm
     * @param itemLength
     * @param itemType
     * @param itemFormat
     * @param itemVal
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/editor/updateEditorEcareItem.json", method=RequestMethod.POST)  // /editor/item/editor.do, method=RequestMethod.PUT
    @ResponseBody public ResultDto updateEditorEcareItem(int ecareNo, String knowledgemapId, String grpCd, String userId, String itemCd, String itemNm,
        int itemindent, String itemfieldNm, int itemLength, String itemType, String itemFormat, String itemVal) throws Exception {
        try {
            String value = editorEcareService.updateEditorEcareItem(
                ecareNo, knowledgemapId, grpCd, userId, itemCd, itemNm, itemindent, itemfieldNm, itemLength, itemType, itemFormat, itemVal
            );
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(value);
            return resultDto;
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 실시간 인자 지우기
     *
     * @param ecareNo
     * @param itemfieldNm
     * @return
     */
    @RequestMapping(value="/editor/deleteEditorEcareItem.json", method=RequestMethod.POST)  // /editor/item/editor.do, method=RequestMethod.DELETE
    @ResponseBody public ResultDto deleteEditorEcareItem(int ecareNo, String itemfieldNm) {
        try {
            int row = editorEcareService.deleteEditorEcareItem(ecareNo, itemfieldNm);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(row);
            return resultDto;
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

}
