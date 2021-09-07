package com.mnwise.wiseu.web.resend.web;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import com.mnwise.wiseu.web.common.service.MailPreviewService;
import com.mnwise.wiseu.web.resend.model.LstResendVo;
import com.mnwise.wiseu.web.resend.service.LstResendService;

@Controller
public class LstResendController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(LstResendController.class);

    @Autowired private LstResendService lstResendService;
    @Autowired private MailPreviewService mailPreviewService;
    @Autowired private PushService pushService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;

    @Value("${push.db.seq:1}")
    private String pushDbSeq;

    /**
     * [캠페인>캠페인 고객이력] 다시보기(팝업)
     * - 재발송 사유, 재발송 수신처 출력
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/resend/replayMainPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /resend/previous_template.do
    public ModelAndView previousTemplate(LstResendVo hisVo, @RequestParam(defaultValue="N") String abTestType, @RequestParam(defaultValue="") String slot1,
                                         HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("resend/replayMainPopup");  // resend/previous_template

        try {
            hisVo.setUserVo(getLoginUserVo());

            LstResendVo resendVo = lstResendService.selectPreviousDetail(hisVo);

            if(resendVo == null) {
                mav.addObject("exception", messageSourceAccessor.getMessage("common.error.no.matching.data"));
            } else {
                mav.addObject("serviceNo", resendVo.getServiceNo());
                mav.addObject("serviceNm", resendVo.getServiceNm());
                mav.addObject("hisVo", resendVo);
            }

            mav.addObject("lstResendUse", super.lstResendUse);
            mav.addObject("channel", hisVo.getChannel());
            mav.addObject("client", hisVo.getClient());
            mav.addObject("subType", hisVo.getSubType());
            mav.addObject("abTestType", abTestType);
            mav.addObject("slot1", slot1);
        } catch(Exception e) {
            mav.addObject("exception", e.toString());
            log.error(e.getMessage());
        }
        return mav;
    }

    /**
     * [캠페인>캠페인 고객이력>다시보기(팝업)] 발송 메시지 출력
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/resend/replayConts.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView previousTemplateView(LstResendVo lstResendVo, @RequestParam(defaultValue="" ,value="fileNm") String fileNm, @RequestParam(defaultValue="") String mode,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            lstResendVo.setUserVo(getLoginUserVo());
            String client = lstResendVo.getClient();
            String channel = lstResendVo.getChannel();

            LstResendVo hisVo = lstResendService.selectPreviousDetail(lstResendVo);  // 상세정보 때문에 한번 조회
            Hashtable<String, Object> previewTable = null;

            if(Channel.ALIMTALK.equalsIgnoreCase(channel)) {  // 알림톡은 핸들러 다시 안돌리고, 생성되어 있던 메시지를 그냥 들고온다.
                returnData.put("content", lstResendService.getAltalkMsg(lstResendVo));
            } else if(Channel.FRIENDTALK.equalsIgnoreCase(channel)) {
                returnData.put("content" , lstResendService.getFrtalkMsg(lstResendVo));
                returnData.put("imgPath", hisVo.getFilePreviewPath());
            } else if(Channel.PUSH.equalsIgnoreCase(channel)) {
                // PUSH도 핸들러 돌리지 않고 생성되어있던 메시지를 가져온다. DB가 다를수 있으니 DB 연결정보를 환경설정에서 가져온다.
                returnData.put("content", pushService.getPushMsg(this.pushDbSeq, lstResendVo));
            } else {
                // A/B 테스트 타입이 N 미사용일경우
                String abTestType = hisVo.getAbTestType();
                int serviceNo = Integer.parseInt(hisVo.getServiceNo());
                String customerKey = hisVo.getSubType().equalsIgnoreCase("N") ? hisVo.getSeq() : hisVo.getCustomerKey();
                if(StringUtil.isBlank(abTestType) || "N".equals(abTestType)) {
                    previewTable = mailPreviewService.makePrevious(serviceNo, customerKey, client, hisVo.getSubType(), hisVo.getResultSeq(), hisVo.getListSeq());
                } else {
                    previewTable = mailPreviewService.makePreviousAbTest(serviceNo, customerKey, client, hisVo.getSubType(), hisVo.getResultSeq(), hisVo.getListSeq(), abTestType, hisVo.getSlot1());
                }

                // 메일채널
                if(Channel.MAIL.equalsIgnoreCase(channel)) {
                    MimeViewVo mimeVo = mailMimeCreate(previewTable);
                    mimeVo.setServiceNo(Integer.parseInt(hisVo.getServiceNo()));
                    mimeVo.setCustomerKey(hisVo.getCustomerKey());
                    mimeVo.setType(client);
                    mimeVo.setCustomerNm(hisVo.getCustomerNm());
                    mimeVo.setCustomerEmail(hisVo.getCustomerEmail());
                    returnData.put("mimeVo", mimeVo);
                    returnData.put("hisVo", hisVo);

                    String sFileNm = StringUtil.escapeXss(fileNm).replaceAll("\\.\\.", "").replaceAll("\\r\\n", "");
                    if(StringUtil.isNotBlank(sFileNm)) {
                        Map<String, Object> map = mimeVo.getAttachFiles();
                        if("down".equalsIgnoreCase(mode)) {

                            sFileNm = new String(Base64.decodeBase64(sFileNm.getBytes()));
                            Object content = map.get(sFileNm);
                            if(content != null) {
                                response.setContentType("application/download;charset=euc-kr");
                                response.setHeader("Pragma", "public");
                                response.setCharacterEncoding("utf-8");
                                response.setHeader("Cache-Control", "max-age=0");
                                setDisposition(sFileNm, request, response);

                                byte[] contentArray = null;
                                if(content instanceof String) {
                                    contentArray = ((String) content).getBytes();
                                } else {
                                    contentArray = (byte[]) content;
                                }

                                response.setContentLength(contentArray.length);

                                OutputStream out = null;

                                try {
                                    out = response.getOutputStream();
                                    out.write(contentArray);
                                } catch(Exception e) {
                                    log.error("IO write error : " + e.getMessage());
                                } finally {
                                    IOUtil.closeQuietly(out);
                                }
                            }
                        }

                        try {
                            returnData.put("contents", map.get(new String(Base64.decodeBase64(sFileNm.getBytes())).trim()));
                        } catch(Exception e) {
                            returnData.put("contents", "");
                            log.error(null, e);
                        }
                        return new ModelAndView("common/previewConts_attach", returnData);  // common/mail_mime_attach_view
                    }
                }
            }

            if(Channel.SMS.equalsIgnoreCase(channel) || Channel.LMSMMS.equalsIgnoreCase(channel)) {
                returnData.put("subject", previewTable.get("subject"));
                returnData.put("content", previewTable.get("content"));
                if(Channel.LMSMMS.equalsIgnoreCase(channel)) {
                    if(previewTable.get("prevImagePath") != null){
                        returnData.put("imgPaths", (String[]) previewTable.get("prevImagePath"));
                    }
                }
            } else if(Channel.FAX.equalsIgnoreCase(channel)) {
                returnData.put("content", previewTable.get("content"));
            }else if(Channel.BRANDTALK.equalsIgnoreCase(channel)) {
                returnData.put("content" , previewTable.get("content"));
                returnData.put("imgPath", hisVo.getFilePreviewPath());
            }

            returnData.put("channel", channel);

            return new ModelAndView("resend/replayConts", returnData);  // resend/previous_template_view
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 고객이력>다시보기(팝업)] - 발송 버튼 클릭
     *
     * @param paramMap
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/resend/resendRequest.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto resendRequest(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) {
        try {
            paramMap.put("reqUserId", getLoginId());

            int row = lstResendService.insertLstResendRequest(paramMap);

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(row);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private MimeViewVo mailMimeCreate(Hashtable previewTable) {
        MimeViewVo mimeVo = new MimeViewVo();
        mimeVo.setMime((String) previewTable.get("content"));
        // 채널 타입이 메일인 경우
        String tmp = (String) previewTable.get("to");
        mimeVo.setTo(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

        tmp = (String) previewTable.get("from");
        mimeVo.setFrom(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
        mimeVo.setSubject(StringUtil.escapeXss((String) previewTable.get("subject")));

        mimeVo.setAttachFiles((Map) previewTable.get("attachFile"));

        return mimeVo;
    }

    /**
     * [리포트>캠페인>캠페인 리스트>캠페인 리포트] 전체 요약 - 발송일련번호 클릭
     * [리포트>이케어>이케어 리스트>이케어 리포트] 일정별 발송현황 - 발송일련번호 클릭
     *
     * @param resultSeq
     * @param serviceNo
     * @param channel
     * @param client
     * @param request
     * @return
     */
    @RequestMapping(value="/resend/getResendTargetCnt.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public Map<String, Object> getResendTargetCnt(@RequestParam(defaultValue="") String resultSeq, int serviceNo, String channel, String client, HttpServletRequest request) {
        Map<String, Object> returnData = new HashMap<>();

        try {
            int resendTargetCnt = 0;

            if(resultSeq.length() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("userVo", getLoginUserVo());
                map.put("resultSeq", resultSeq);
                map.put("client", client);
                map.put("ecareNo", String.valueOf(serviceNo));
                map.put("channel", channel);

                if(Channel.MAIL.equalsIgnoreCase(channel)) {
                    map.put("resendErrorCdArray", StringUtil.split(lstResendService.getResendMailErrorCd(), ","));
                } else if(Channel.FAX.equalsIgnoreCase(channel)) {
                    map.put("resendErrorCdArray", StringUtil.split(lstResendService.getResendFaxErrorCd(), ","));
                } else if(Channel.ALIMTALK.equalsIgnoreCase(channel)) {
                    map.put("resendErrorCdArray", StringUtil.split(lstResendService.getResendAltalkErrorCd(), ","));
                } else if(Channel.FRIENDTALK.equalsIgnoreCase(channel)) {
                    map.put("resendErrorCdArray", StringUtil.split(lstResendService.getResendFRTErrorCd(), ","));
                } else if(Channel.PUSH.equalsIgnoreCase(channel)) {
                    map.put("resendErrorCdArray", StringUtil.split(pushService.getResendErrorCd(), ","));
                } else {
                    map.put("resendErrorCdArray", StringUtil.split(lstResendService.getResendSmsErrorCd(), ","));
                }
                resendTargetCnt = lstResendService.getResendTargetCount(map);
            }

            returnData.put("resendTargetCnt", resendTargetCnt);
            returnData.put("resultSeq", resultSeq);
            returnData.put("subType", ServletRequestUtils.getStringParameter(request, "form_subType", ""));
        } catch(Exception e) {
            log.error(null, e);
        }

        return returnData;
    }
}
