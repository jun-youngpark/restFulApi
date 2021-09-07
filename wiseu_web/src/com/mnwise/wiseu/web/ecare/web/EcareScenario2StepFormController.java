package com.mnwise.wiseu.web.ecare.web;

import static com.mnwise.wiseu.web.base.Const.Channel.MAIL;
import static com.mnwise.wiseu.web.base.Const.Channel.PUSH;
import static com.mnwise.wiseu.web.base.Const.EcareSubType.NREALTIME;
import static com.mnwise.wiseu.web.base.Const.EcareSubType.SCHEDULE;
import static com.mnwise.wiseu.web.base.Const.EcareSubType.SCHEDULE_MINUTE;
import static com.mnwise.wiseu.web.base.Const.enumChannel.findName;
import static com.mnwise.wiseu.web.template.util.KakaoButtonUtils.convertJsonToKakaoButtonList;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.ServiceType;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.model.PushVo;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.ecare.model.AddQueryVo;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.model.EcareTargetDto;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.ecare.service.AddQueryService;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.editor.model.EcareEditorVo;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.editor.service.EditorEcareService;
import com.mnwise.wiseu.web.env.service.EnvServerInfoService;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.wiseu.web.segment.service.SegmentService;
import com.mnwise.wiseu.web.segment.service.SqlSegmentService;

@Controller
public class EcareScenario2StepFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareScenario2StepFormController.class);

    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private EditorEcareService editorEcareService;
    @Autowired private AddQueryService addQueryService;
    @Autowired private EnvServerInfoService envServerInfoService;
    @Autowired private SqlSegmentService sqlSegmentService;
    @Autowired private SegmentService segmentService;
    @Autowired private ServletContext servletContext;
    @Autowired private PushService pushService;
    @Value("${editor.multilang.use:off}")
    private String useMultiLang;
    @Value("${omnichannel.message.use:off}")
    private String useOmniChannelMessage;
    @Value("${verify.department.use:off}")
    private String useVerifyDepart;


    /**
     * [이케어>이케어 등록>2단계] 이케어 메세지 작성 화면 출력 (전체 채널)
     * [이케어 리스트> 상세 이케어] 이케어 메세지 작성 화면 출력 (전체 채널)
     * - JSP : /ecare/ecare2Step_mail.jsp (Email)
     * - JSP : /ecare/ecare2Step_sms.jsp (SMS)
     * - JSP : /ecare/ecare2Step_mms.jsp (LMS/MMS)
     * - JSP : /ecare/ecare2Step_fax.jsp (FAX)
     * - JSP : /ecare/ecare2Step_alt.jsp (알림톡)
     * - JSP : /ecare/ecare2Step_frt.jsp (친구톡) - MOKA에서 사용
     * - JSP : /ecare/ecare2Step_brt.jsp (브랜드톡)
     * - JSP : /ecare/ecare2Step_push.jsp (PUSH)
     */
    @RequestMapping(value="/ecare/ecare2Step.do", method=RequestMethod.GET)  // /ecare/ecare_2step_form.do
    public ModelAndView ecare2Step(@RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="1") int depthNo, String msg, HttpServletRequest request) throws Exception {
     try {
            int ecareNo = ServletRequestUtils.getIntParameter(request, "ecareVo.ecareNo", 0);
            String channelType = ServletRequestUtils.getStringParameter(request, "ecareVo.channelType", "M");
            ModelAndView mav =new ModelAndView("/ecare/ecare2Step");  // /ecare/ecare_2step_채널_form);
            int maxDepthNo = 1;
            // 해당 채널의 최대 DEPTH_NO을 알기 위함
            for(EcareVo ecareVo : ecareScenarioService.getEcareScenariochannelInfo(scenarioNo)) {
                if(ecareVo.getChannelType().equals(channelType)) {
                    maxDepthNo = ecareVo.getDepthNo();
                }
            }
            EcareScenarioVo ecareScenarioVo= ecareScenarioService.getEcareScenario2StepInfo(scenarioNo, ecareNo, getLoginId(), getLoginUserVo().getLanguage());
            ecareScenarioVo.setMaxDepthNo(maxDepthNo);
            ecareScenarioVo.getUserVo().setLanguage((getLoginUserVo().getLanguage()));
            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(ecareScenarioVo.getGrpCd(), ecareScenarioVo.getUserId())) {
                return null;
            }

            //[STEP1] 컨텐츠제작(첨부파일)
            if(MAIL.equals(channelType)) {
                mav.addObject("multipartFileList",editorEcareService.selectEditorEcareMultipartfile(ecareNo));
            }

            //[STEP2] 대상자 정보 & 부가데이터
            if(SCHEDULE.equals(ecareScenarioVo.getServiceType())
                    && (SCHEDULE.equals(ecareScenarioVo.getSubType()) ||  SCHEDULE_MINUTE.equals(ecareScenarioVo.getSubType()))
                ) {
                //대상자 정보(스케줄)
                mav.addObject("targetQueryInfo", segmentService.selectTargetQueryInfo(ecareScenarioVo.getSegmentNo()));
                //필수 키 설정 정보(스케줄)
                mav.addObject("semanticFieldList", sqlSegmentService.selectSemanticList(ecareScenarioVo.getSegmentNo()));
            }
            //부가데이터
            mav.addObject("addQueryList", addQueryService.selectAddQuery(ecareNo));

            //[STEP3]이케어 발송 정보
            mav.addObject("ecareScenarioVo", ecareScenarioVo);

            //[고급설정]옴니채널
            if(this.useOmniChannelMessage.equals("on")) {
                mav.addObject("scenarioList", ecareScenarioService.findOmniChannelEcares(ecareScenarioVo));//옴니채널
            }

            referenceData(mav, ecareScenarioVo);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }


    /**
     * [이케어>이케어 등록>2단계] 사용 가능한 옴니채널 목록을 불러온다.
     *
     * @param svcId 서비스ID
     * @return
     */
    @RequestMapping(value="/include/ecareOmniChannel.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView omniChannelList(int scenarioNo, int ecareNo , int depthNo, String channelType, String subType) throws Exception {
        try {
            ModelAndView mav =new ModelAndView("/include/ecareOmniChannel");
            //[고급설정]옴니채널
            if(this.useOmniChannelMessage.equals("on")) {
                EcareScenarioVo ecareScenarioVo = new EcareScenarioVo();
                ecareScenarioVo.setScenarioNo(scenarioNo);
                ecareScenarioVo.getEcareVo().setEcareNo(ecareNo);
                ecareScenarioVo.getEcareVo().setDepthNo(depthNo + 1);
                ecareScenarioVo.setUserVo(getLoginUserVo());
                ecareScenarioService.findSubEcareAndStoreMap(ecareScenarioVo, mav);
                
              //[ljh][20210712][준실시간 옴니채널 모든채널 사용]
//                if(Const.EcareSubType.NREALTIME.equals(subType) || depthNo > 1 ) {  //준실시간 이거나 옴니채널은 최초 채널만 생성 가능
                if( depthNo > 1 ) {  //옴니채널은 최초 채널만 생성 가능
                    List<String> channelList = Arrays.asList(channelType);
                    mav.addObject("channelList", channelList);
                }else if(Const.EcareSubType.NREALTIME.equals(subType) || Const.EcareSubType.SCHEDULE.equals(subType)){
                    String[] channelArr = PropertyUtil.getProperty("ecare.channel.use.list").split(",");    //이케어 사용 채널목록
                    mav.addObject("channelList", channelArr);
                }else {
                    mav.addObject("channelList", ecareScenarioService.findAvailableChannels(scenarioNo, channelType));
                }
            }
            return mav;
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }
    /**
     * [이케어] 채널별 추가 데이터
     * -
     */
    public ModelAndView referenceData(ModelAndView mav, EcareScenarioVo ecareScenarioVo) throws Exception {
            ecareScenarioVo.setWebExecMode(super.webExecMode);
            ecareScenarioVo.setSmsIndividualCharge(super.smsIndividualCharge);

            ServerInfoVo envServerInfoVo = getServerInfoVo();
            mav.addObject("useTestSend", "Y".equalsIgnoreCase(envServerInfoVo.getB4RealSendTestSendYn()) ? "on" : "off");
            mav.addObject("useVerifyBeforesend", "Y".equalsIgnoreCase(envServerInfoVo.getB4SendVerifyYn()) ? "on" : "off");
            mav.addObject("useMultiLang", this.useMultiLang);
            mav.addObject("resultIdNm", Const.RESULT_ID_PREFIX);
            mav.addObject("dbInfoList", sqlSegmentService.selectDbInfoList());
            mav.addObject("seedTime", System.currentTimeMillis());
            mav.addObject("channelAbbr", findName(ecareScenarioVo.getEcareVo().getChannelType()));
            mav.addObject("notOmniEcare","N".equals(ecareScenarioVo.getEcareVo().getRelationType()));

            if(ChannelUtil.isKakao(ecareScenarioVo.getEcareVo().getChannelType())) {
                mav.addObject("kakaoProfileList", ecareScenarioService.getKakaoProfileList(getLoginId()));
                mav.addObject("kakaoButtonList", convertJsonToKakaoButtonList(ecareScenarioService.selectKakaoButtons(ecareScenarioVo.getEcareVo().getEcareNo(), Const.ServiceType.ECARE)));
            }

            if(PUSH.equals(ecareScenarioVo.getEcareVo().getChannelType())) {
                mav.addObject("pushInfoMap", pushService.getPushServiceInfo(ServiceType.ECARE, ecareScenarioVo.getEcareVo().getEcareNo()));
                mav.addObject("pushMsgInfoList", pushService.selectPushMsgTypeList(true));
            }

            return mav;
    }

    private ServerInfoVo getServerInfoVo() {
        Object obj = servletContext.getAttribute("envServerInfoVo");
        return (obj == null) ? envServerInfoService.selectEnvServerInfo() : (ServerInfoVo) obj;
    }


    /**
     * [이케어>이케어 등록>2단계] 이케어 메세지 작성 내용 저장 (전체 채널)
     * [이케어 리스트> 상세 이케어] 이케어 메세지 작성 내용 저장 (전체 채널)
     *
     * @param request
     * @param response
     * @param command
     * @param exception
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/ecare2StepTarget.json", method=RequestMethod.POST)  // /ecare/ecare_2step_form.do
    @ResponseBody public ResultDto ecare2StepTarget(EcareVo ecareVo, EcareTargetDto ecareTargetDto
            , String addQueryData, String semanticData) throws Exception {
        try {
                ObjectMapper objectMapper = new ObjectMapper();
                //부가 데이터 정보 등록/수정
                addQueryService.mergeAddQuery(objectMapper.readValue(StringUtil.unescapeXss(addQueryData), AddQueryVo[].class), ecareVo.getEcareNo());
                if(!NREALTIME.equals(ecareVo.getSubType())) {
                    SemanticVo[] semanticVo = objectMapper.readValue(StringUtil.unescapeXss(semanticData), SemanticVo[].class);
                    if(ArrayUtils.isNotEmpty(semanticVo)){
                        segmentService.mergeTargetKey(ecareTargetDto, semanticVo); //대상자 테이블 등록/수정
                    }
                    segmentService.mergeTarget(ecareTargetDto); //대상자 키 정보 등록/수정
                }
                ecareScenarioService.updateEcare2StepInfo(ecareVo);

                return new ResultDto(ResultDto.OK);
            }catch(Exception e) {
                log.error(null, e);
                return new ResultDto(ResultDto.FAIL, e.getMessage());
            }
    }


    /**
     * [이케어>이케어 등록>2단계] 이케어 메세지 작성 내용 저장 (전체 채널)
     * [이케어 리스트> 상세 이케어] 이케어 메세지 작성 내용 저장 (전체 채널)
     *
     * @param request
     * @param response
     * @param command
     * @param exception
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/ecare2StepInfo.json", method=RequestMethod.POST)  // /ecare/ecare_2step_form.do
    @ResponseBody public ResultDto ecare2StepInfo(EcareScenarioVo ecareScenarioVo, String[] cycleItem) throws Exception {
        try {
                final EcareVo ecareVo = ecareScenarioVo.getEcareVo();
                ecareScenarioService.updateEcareScenario2StepInfo(ecareScenarioVo);
                if(!"5".equals(ecareVo.getEcareScheduleVo().getCycleCd())) { //분할발송인경우 
                    ecareVo.setDivideYn("N");
                }
                ecareScenarioService.updateEcare2StepInfo(ecareVo);
                if(MAIL.equals(ecareVo.getChannelType())) {
                    ecareScenarioService.updateEcareTraceInfo2StepInfo(ecareVo);
                }

                if(SCHEDULE.equals(ecareVo.getSubType())
                    || SCHEDULE_MINUTE.equals(ecareVo.getSubType())) {
                    ecareScenarioService.updateEcareSchedule2StepInfo(ecareVo.getEcareScheduleVo());
                    ecareScenarioService.updateEcareCycle2StepInfo(ecareVo.getEcareScheduleVo(),cycleItem);
                }

                return new ResultDto(ResultDto.OK);
            }catch(Exception e) {
                log.error(null, e);
                return new ResultDto(ResultDto.FAIL, e.getMessage());
            }

    }

    /**
     * [이케어>이케어 등록>2단계] 이케어 메세지 작성 내용 저장 (전체 채널)
     * [이케어 리스트> 상세 이케어] 이케어 메세지 작성 내용 저장 (전체 채널)
     *
     * @param request
     * @param response
     * @param command
     * @param exception
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/ecareEditor.json", method=RequestMethod.POST)  // /ecare/ecare_2step_form.do
    @ResponseBody public ResultDto ecareEditor(@RequestBody EcareEditorVo editorVo) throws Exception {
        try {
            //첨부파일
            if(MAIL.equals(editorVo.getChannelType())) {
                editorEcareService.mergeMultipartFile(editorVo);
            }

            //PUSH SERVICE 정보 변경.
            if(PUSH.equals(editorVo.getChannelType())) {
                ObjectMapper objectMapper = new ObjectMapper();
                PushVo pushVo = objectMapper.readValue(StringUtil.unescapeXss(editorVo.getPushData()), PushVo.class);
                pushService.updatePushServiceInfo(pushVo.getSvcType(), editorVo.getNo(), pushVo.getPushAppId(), pushVo.getPushMsgInfo()
                    , pushVo.getPushMenuId(), pushVo.getPushPopImgUse(),pushVo.getPushImgUrl()
                    , pushVo.getPushBigImgUse(),pushVo.getPushBigImgUrl(), pushVo.getPushWebUrl());
            }

            //이케어 템플릿/핸들러 저장
            editorEcareService.updateEditorEcare(editorVo);

            return new ResultDto(ResultDto.OK);
        }catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

}
