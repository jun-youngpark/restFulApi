package com.mnwise.wiseu.web.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.Mapper;
import com.mnwise.common.util.NumberUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.ImageModelVo;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.common.service.SendMessageService;
import com.mnwise.wiseu.web.common.ui.upload.SendInfo;
import com.mnwise.wiseu.web.common.util.WiseuSerialNumber;
import com.mnwise.wiseu.web.env.model.EnvSenderInfoVo;
import com.mnwise.wiseu.web.env.service.EnvSenderInfoService;
import com.mnwise.wiseu.web.template.model.KakaoProfile;

import net.sf.json.JSONObject;

@Controller
public class SendMappingController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SendMappingController.class);

    @Autowired private EnvSenderInfoService envSenderInfoService;
    @Autowired private SendMessageService sendMessageService;

    @Value("${send.bulk.per.count:20000}")
    private int sendBulkCount;
    private static int resultSeqIndex = 0;

    private static synchronized int getResultSeqIndex() {
        resultSeqIndex = (resultSeqIndex + 1) % 1000;
        return resultSeqIndex;
    }

    /**
     * [캠페인>캠페인 등록>3단계] 발송 버튼 클릭 - MOKA에서 사용
     * [이케어>이케어 등록>3단계] 발송 버튼 클릭 - MOKA에서 사용
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/sendMessage.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView sendMessage(HttpServletRequest request) throws Exception {
        try {
            log.info("=========START===========");
            String serviceType = StringUtil.defaultString(request.getParameter("serviceType"), "ec");
            String channel = StringUtil.defaultString(request.getParameter("channel"), "M");
            String reqDtm = request.getParameter("sendStartDt").replaceAll("-", "") + request.getParameter("sendStartTm").replaceAll(":", "");
            int serviceNo = NumberUtil.toInt(request.getParameter("serviceNo"), 0);
            String scenarioNo = StringUtil.defaultString(request.getParameter("scenarioNo"), "0");
            String depthNo = StringUtil.defaultString(request.getParameter("depthNo"), "0");
            String targetQuery = sendMessageService.getTargetQuery(serviceType, serviceNo);
            ImageModelVo imVo = sendMessageService.getTemplate(serviceType, channel, serviceNo);

            String template = imVo.getTemplate();
            String[] imagePath = null;
            imagePath = imVo.getFilePath();

            String startDt = reqDtm.substring(0, 8);
            String startTm = reqDtm.substring(8, 14);

            long resultSeq = (System.currentTimeMillis() * 1000L) + getResultSeqIndex();

            MessageVo mobileVo = null;
            if(serviceType.equals("ec")) {
                mobileVo = sendMessageService.getEcareDetail(serviceNo);
            } else {
                mobileVo = sendMessageService.getCampaignDetail(serviceNo);
            }
            mobileVo.setServiceNo(serviceNo + "");
            mobileVo.setResultSeq(resultSeq);
            mobileVo.setStartDt(startDt);
            mobileVo.setStartTm(startTm);
            String friendtalkImagePath = "";
            JSONObject json = new JSONObject();
            JSONObject attachmentData = new JSONObject();

            String imageUrl = "";
            if("C".equals(channel)) {
                imageUrl = sendMessageService.getImageURl(serviceType, serviceNo);
                if(StringUtil.isNotBlank(imageUrl)) {
                    json.put("img_url", imageUrl);
                    attachmentData.put("image", json);
                    friendtalkImagePath = sendMessageService.getFriendtalkImagePath(mobileVo.getFileName1());
                }
            }

            sendMessageService.insertNVECARESENDRESULT(mobileVo);

            String tmplCd = mobileVo.getTmplCd();
            String senderKey = mobileVo.getSenderKey();

            List<Hashtable<String, Object>> sendMessageList = sendMessageService.sendMessage(serviceType, targetQuery, channel, serviceNo);
            List<MessageVo> sendList = new ArrayList<>();

            String sendMessage = "";

            SendInfo sendInfo = new SendInfo();
            sendInfo.setTotalCnt(sendMessageList.size());

            // 미리 세팅 가능 값은 여기서 세팅
            int resultCnt = 0;
            try {
                UserVo userVo = getLoginUserVo();

                for(int i = 0; i < sendMessageList.size(); i++) {
                    if("A".equals(channel) || "C".equals(channel)) {
                        sendMessage = Mapper.mapping(template, sendMessageList.get(i), "#{", "}");
                    } else {
                        sendMessage = Mapper.mapping(template, sendMessageList.get(i), "{$", "$}");
                    }

                    MessageVo mv = new MessageVo();
                    mv.setServiceType(serviceType);
                    mv.setSenderKey(senderKey);
                    mv.setResultSeq(resultSeq);
                    mv.setChannel(channel);
                    mv.setReqDeptCd(userVo.getGrpCd());
                    mv.setReqUsrId(userVo.getUserId());
                    mv.setSmsSndYn(mobileVo.getSmsSndYn());
                    mv.setCallBack(mobileVo.getCallBack().replaceAll("-", ""));
                    mv.setReqDtm(startDt + startTm);
                    if(channel.equals("C") || channel.equals("A")) {
                        mv.setSubject(mobileVo.getFailbackSubject());
                        mv.setFileName1(friendtalkImagePath);
                        if(StringUtil.isNotBlank(imageUrl)) {
                            mv.setAttachment(attachmentData.toString());
                        }
                        if("A".equals(mobileVo.getAdFlag())) {
                            mv.setAdFlag("Y");
                        } else {
                            mv.setAdFlag("N");
                        }
                    }

                    mv.setTmplCd(tmplCd);
                    mv.setSn(WiseuSerialNumber.getSn(serviceType, 1));
                    mv.setSendMessage(sendMessage);
                    mv.setPhoneNum((String) sendMessageList.get(i).get("S"));
                    mv.setServiceNo(serviceNo + "");
                    if(channel.equals("T")) {
                        mv.setResultSeqPage((i / this.sendBulkCount));
                        mv.setListSeq(StringUtil.leftPad(String.valueOf(i + 1), 10, '0'));
                        mv.setSubject(mobileVo.getSubject());
                        if(imagePath == null) {
                            mv.setFileCnt(0);
                        } else {
                            if(imagePath.length == 1) {
                                mv.setFileType("IMG");
                                mv.setFileName1(imagePath[0]);
                                if(StringUtil.isNotBlank(sendMessage)) {
                                    mv.setFileCnt(2);
                                } else {
                                    mv.setFileCnt(1);
                                }
                                mv.setServiceDep("ALL");
                            } else if(imagePath.length == 2) {
                                mv.setFileType("IMG");
                                mv.setFileName1(imagePath[0]);
                                mv.setFileName2(imagePath[1]);
                                mv.setServiceDep("ALL");
                                if(StringUtil.isNotBlank(sendMessage)) {
                                    mv.setFileCnt(3);
                                } else {
                                    mv.setFileCnt(2);
                                }
                            } else if(imagePath.length == 3) {
                                mv.setFileType("IMG");
                                mv.setFileName1(imagePath[0]);
                                mv.setFileName2(imagePath[1]);
                                mv.setFileName3(imagePath[2]);
                                mv.setServiceDep("ALL");
                                if(StringUtil.isNotBlank(sendMessage)) {
                                    mv.setFileCnt(4);
                                } else {
                                    mv.setFileCnt(3);
                                }
                            }
                        }
                    }

                    sendList.add(mv);

                    if(i == 0 || i % this.sendBulkCount == 0 || i == (sendMessageList.size() - 1)) {
                        if("A".equals(channel)) {
                            resultCnt = sendMessageService.bulkAlimtalkDataImport(sendList);
                        } else if("C".equals(channel)) {
                            resultCnt = sendMessageService.bulkFriendtalkDataImport(sendList);
                        } else if("S".equals(channel)) {
                            resultCnt = sendMessageService.bulkSMSDataImport(sendList);
                        } else if("T".equals(channel)) {
                            if(imagePath == null) {
                                resultCnt = sendMessageService.bulkLMSkDataImport(sendList);
                            } else {
                                resultCnt = sendMessageService.bulkMMSDataImport(sendList);
                            }
                        }
                        sendInfo.setInProgress(true);
                        sendInfo.setInsertedCnt((i + 1));
                        request.getSession().setAttribute("sendInfo", sendInfo);
                        sendList = new ArrayList<>();
                    }

                }

                sendInfo.setInProgress(false);
                sendInfo.setInsertedCnt(sendMessageList.size());
                request.getSession().setAttribute("sendInfo", sendInfo);
            } catch(Exception e) {
                log.error("### ERROR : FAIL SEND MESSAGE...");
                log.error(e.getMessage());
                resultCnt = 0;
            }

            Map<String, Object> returnData = new HashMap<>();

            if(serviceType.equals("ec")) {
                returnData.put("scenarioNo", scenarioNo);
                returnData.put("ecareVo.ecareNo", serviceNo);
                returnData.put("ecareVo.channelType", channel);
                returnData.put("channelType", channel);
                returnData.put("depthNo", depthNo);
                returnData.put("resultCnt", resultCnt);

                return new ModelAndView(new RedirectView("/ecare/ecare2Step.do"), returnData);  // /ecare/ecare_3step_form.do
            } else {
                returnData.put("scenarioNo", scenarioNo);
                returnData.put("campaignVo.campaignNo", serviceNo);
                returnData.put("campaignVo.channelType", channel);
                returnData.put("channelType", channel);
                returnData.put("depthNo", depthNo);
                returnData.put("resultCnt", resultCnt);

                return new ModelAndView(new RedirectView("/campaign/campaign3Step.do"), returnData);  // /campaign/campaign_3step_form.do
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>단건발송] 단건발송 메세지 작성 화면 출력 - MOKA에서 사용
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/ecare/onceSend.do", method={RequestMethod.GET, RequestMethod.POST})  // /ecare/send_once.do
    public ModelAndView list(@RequestParam(defaultValue="S") String channel, String sendDt, String sendTm, HttpServletRequest request) throws Exception {
        try {
            String userId = getLoginId();

            List<KakaoProfile> profileList = sendMessageService.selectKakaoProfileList(userId);

            EnvSenderInfoVo senderInfo = new EnvSenderInfoVo();
            senderInfo.setUserId(userId);
            EnvSenderInfoVo sender = envSenderInfoService.selectEnvSenderInfo(senderInfo);

            String forwardJsp = "";
            if("A".equals(channel)) {
                forwardJsp = "/common/onceSend_alt";  // /common/send_once_altalk
            } else if("C".equals(channel)) {
                forwardJsp = "/common/onceSend_frt";  // /common/send_once_frtalk
            } else if("S".equals(channel)) {
                forwardJsp = "/common/onceSend_sms";  // /common/send_once_sms
            } else if("T".equals(channel)) {
                forwardJsp = "/common/onceSend_mms";  // /common/send_once_lms
            }

            Map<String, Object> retMap = new HashMap<>();
            retMap.put("kakaoProfileList", profileList);
            retMap.put("channel", channel);
            retMap.put("sendDt", StringUtil.defaultString(sendDt, DateUtil.getNowDateTime("yyyymmdd")));
            retMap.put("sendTm", StringUtil.defaultString(sendTm, DateUtil.getNowDateTime("HHmmss")));
            retMap.put("seedTime", System.currentTimeMillis());
            retMap.put("senderNm", sender.getSenderNm());
            retMap.put("senderTel", StringUtil.defaultString(sender.getSenderTel()).replaceAll("-", ""));
            return new ModelAndView(forwardJsp, retMap);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/common/sendMessageOnce.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto sendMessageOnce(String channel, int serviceNo, String send_dtm, String callBack, String phoneNum, String sendDt, String sendTm,
                    String subject, String template, String senderKey, String tmplCd, String smsSndYn, String contsNo, String adFlag, HttpServletRequest request) {
        try {
            sendMessageService.sendMessageOnce(channel, serviceNo, send_dtm, callBack, phoneNum, sendDt, sendTm, subject, template, senderKey, tmplCd, smsSndYn, contsNo, adFlag, getLoginUserVo());
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}