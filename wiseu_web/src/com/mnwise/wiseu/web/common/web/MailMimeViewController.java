package com.mnwise.wiseu.web.common.web;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.mts.Config;
import com.mnwise.mts.mail.util.SimpleMailSender;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.MailPreviewVo;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import com.mnwise.wiseu.web.common.service.MailPreviewService;
import com.mnwise.wiseu.web.common.service.MimeViewService;
import com.mnwise.wiseu.web.common.util.ReadingMime;

@Controller
public class MailMimeViewController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MailMimeViewController.class);

    @Autowired private MailPreviewService mailPreviewService;
    @Autowired private MimeViewService mimeViewService;

    @Value("${fax.resend.url}")
    private String faxResendUrl;
    @Value("${local.ip}")
    private String localIp;

    /**
     * [캠페인>캠페인 등록>2단계>캠페인 미리보기(팝업)] 상단 대상자 목록 출력
     * [이케어>이케어 등록>2단계>이케어 미리보기(팝업)] 상단 대상자 목록 출력
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/previewMainPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /common/mime_view.do
    public ModelAndView previewList(@RequestParam(defaultValue="0") int serviceNo, String serviceType, String searchKey, String searchValue,
                                    @RequestParam(defaultValue=" ") String subType, @RequestParam(defaultValue="N")String abTestType, HttpServletRequest request) throws Exception {
        try {
            //isJeonmun : 대상자 테이블이 NVSCHEDULEACCEPT 인지 확인하는 용도
            boolean isJeonmun = false;
            if(subType.equals("S") || subType.equals("R")) {
                isJeonmun = mailPreviewService.isJeonmun(serviceNo);
            }

            // 공백이면 실시간임..
            if(serviceType.equalsIgnoreCase("EM")) {
                subType = " ";
            }

            @SuppressWarnings("rawtypes")
            List mailPreviewList = null;
            if(!serviceType.equalsIgnoreCase("EC") || !subType.equals(""))
                mailPreviewList = mailPreviewService.previewList(serviceNo, serviceType, searchKey, searchValue, subType);

            ModelAndView mav = new ModelAndView("common/previewMainPopup");  // common/mail_mime_list
            mav.addObject("serviceNo", String.valueOf(serviceNo));
            mav.addObject("serviceType", serviceType);
            mav.addObject("subType", subType);
            mav.addObject("isJeonmun", isJeonmun);
            mav.addObject("list", mailPreviewList);
            mav.addObject("abTestType", abTestType);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계>캠페인 미리보기(팝업)] 하단 미리보기 내용 출력
     * [이케어>이케어 등록>2단계>이케어 미리보기(팝업)] 하단 미리보기 내용 출력
     *
     * @param mimeVo
     * @param serviceNo
     * @param serviceType
     * @param abTestType
     * @param abType
     * @param previewType
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/previewConts.do", method={RequestMethod.GET, RequestMethod.POST})  // /common/previewMime.do
    public ModelAndView preview(MimeViewVo mimeVo, @RequestParam(defaultValue="0") int serviceNo, String serviceType, @RequestParam(defaultValue="N") String abTestType,
        String abType, @RequestParam(defaultValue="H") String previewType,@RequestParam(defaultValue="") String subType, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            Hashtable<String, Object> previewTable = null;
            mimeVo.setType(serviceType);
            if("H".equals(previewType)) {
                String fileNm = StringUtil.escapeXss(request.getParameter("fileNm")).replaceAll("\\.\\.", "").replaceAll("\\r\\n", "");
                String realtimeArg = StringUtil.escapeXss(request.getParameter("realtimeArg"));
                //A/B테스트 발송 사용 일 경우
                if(!"N".equals(abTestType)) {
                    previewTable = mailPreviewService.makePreviewAbTest(serviceNo, mimeVo.getCustomerKey(), realtimeArg, serviceType, mimeVo.getSubType()==null?subType:mimeVo.getSubType() , "20081217", mimeVo.getSeg(), abTestType, abType);
                } else {  //A/B 미사용 경우 기존 로직
                    previewTable = mailPreviewService.makePreview(serviceNo, mimeVo.getCustomerKey(), realtimeArg, serviceType, mimeVo.getSubType()==null?subType:mimeVo.getSubType() , "20081217", mimeVo.getSeg());
                }
                mimeVo.setMime((String) previewTable.get("content"));
                // 채널 타입이 메일인 경우
                String channelType = (String) previewTable.get("channelType");
                if("M".equalsIgnoreCase(channelType)) {
                    String to = ((String) previewTable.get("to")).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                    mimeVo.setTo(to);
                    String from = ((String) previewTable.get("from")).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                    mimeVo.setFrom(from);
                    mimeVo.setSubject(StringUtil.escapeXss((String) previewTable.get("subject")));
                    @SuppressWarnings("unchecked")
                    Map<String, Object> attachMap = (Map<String, Object>) previewTable.get("attachFile");
                    mimeVo.setAttachFiles(attachMap);
                    if(attachMap != null)
                        log.debug("map size : " + attachMap.size());
                    if(StringUtil.isNotBlank(fileNm)) {
                        log.debug(" File name : " + fileNm);
                        try {
                            returnData.put("contents", (String) attachMap.get(new String(Base64.decodeBase64(fileNm.getBytes())).trim()));
                        } catch(Exception e) {
                            returnData.put("contents", "");
                            log.debug("contents blank error. (3)");
                        }
                        return new ModelAndView("common/previewConts_attach", returnData);  // common/mail_mime_attach_view
                    }
                }
                returnData.put("mimeVo", mimeVo);
                log.debug("#channelType : " + channelType);
                if(channelType.equalsIgnoreCase("M")) {
                    return new ModelAndView("common/previewConts_mail", returnData);  // common/mail_mime_view
                } else if(channelType.equalsIgnoreCase("F")) {
                    // 외부연동 발송 전 미리보기 요청인 경우에는 발송 기능을 jsp에 들어가도록 한다.
                    //if(request.getRequestURI().indexOf("channel_preview.do") > -1) {
                        //returnData.put("outside", "Y");
                    //}
                    return new ModelAndView("common/previewConts_fax", returnData);  // common/fax_html_view
                }
                return null;
            } else {
                String ecareNo = ServletRequestUtils.getStringParameter(request, "no");
                String data = ServletRequestUtils.getStringParameter(request, "data", "");
                data = data.replaceAll("&quot;", "\"");

                mimeVo.setMime(mailPreviewService.makeTemplatePreview(ecareNo, data));
                returnData.put("mimeVo", mimeVo);

                return new ModelAndView("common/previewConts_mail", returnData);  // common/mail_mime_view
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 발송 내역 보기 (메일은 마임을 재생성해서 출력, FAX의 경우에는 FAX 내역보기 링크로 처리)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ModelAndView view(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            Hashtable<String, Object> previewTable = null;
            MimeViewVo mimeVo = new MimeViewVo();
            String serviceType = ServletRequestUtils.getStringParameter(request, "serviceType", "EC");
            int serviceNo = ServletRequestUtils.getIntParameter(request, "serviceNo", 0);
            String customerKey = ServletRequestUtils.getStringParameter(request, "customerKey");
            String subType = ServletRequestUtils.getStringParameter(request, "subType");
            String seg = ServletRequestUtils.getStringParameter(request, "seg", "");
            String fileNm = ServletRequestUtils.getStringParameter(request, "fileNm");
            String srfidd = ServletRequestUtils.getStringParameter(request, "srfidd", "");
            String resndBtn = ServletRequestUtils.getStringParameter(request, "resnd_btn", "off");
            String seq = ServletRequestUtils.getStringParameter(request, "seq");

            mimeVo.setServiceNo(serviceNo);
            mimeVo.setType(serviceType);
            mimeVo.setCustomerKey(customerKey);
            mimeVo.setSeg(seg);

            // FAX 인 경우에는 html을 만들 필요는 없음.
            // MAIL인 경우에는 마임을 보는 거라면 마임 보기로 넘기고 그렇지 않다면 미리보기 로직을 태운다.
            // 채널 정보만 가져오면 되는데 그냥 기존 쿼리를 그대로 사용함
            MailPreviewVo previewVo = mailPreviewService.selectServicePreview(serviceType, serviceNo);
            String channelType = previewVo.getChannelType();
            // 채널 타입이 메일이면 메일 데이터를 가져온다.
            if(channelType.equalsIgnoreCase("M")) {
                previewTable = mailPreviewService.makePreview(serviceNo, customerKey, serviceType, subType, "20081217", seg, seq);

                mimeVo.setMime((String) previewTable.get("content"));
                String tmp = (String) previewTable.get("to");
                mimeVo.setTo(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                tmp = (String) previewTable.get("from");
                mimeVo.setFrom(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                mimeVo.setSubject(StringUtil.escapeXss((String) previewTable.get("subject")));
                @SuppressWarnings("rawtypes")
                Map map = (Map) previewTable.get("attachFile");
                mimeVo.setAttachFiles(map);

                if(fileNm != null && !fileNm.trim().equals("")) {
                    try {
                        returnData.put("contents", (String) map.get(new String(Base64.decodeBase64(fileNm.getBytes())).trim()));
                    } catch(Exception e) {
                        returnData.put("contents", "");
                        log.error(null, e);
                    }
                    return new ModelAndView("common/previewConts_attach", returnData);  // common/mail_mime_attach_view
                }

                returnData.put("mimeVo", mimeVo);
            }

            returnData.put("customerKey", customerKey);
            returnData.put("resndBtn", resndBtn);

            if(channelType.equalsIgnoreCase("M")) {
                return new ModelAndView("common/mail_resend_view", returnData);
            } else if(channelType.equalsIgnoreCase("F")) {
                // 채널이 FAX인데 srfidd를 넘기지 않는 경우가 있을 수 있어서 처리함. 2010.8.24 한규현
                if(srfidd.equals("")) {
                    srfidd = mailPreviewService.getFaxSrfidd(serviceType, serviceNo, customerKey);

                }
                // 외부연동 발송 전 미리보기 요청인 경우에는 발송 기능을 jsp에 들어가도록 한다.
                //if(request.getRequestURI().indexOf("channel_view.do") > -1) {
                    //returnData.put("outside", "Y");
                //}

                returnData.put("resendUrl", faxResendUrl);  // 팩스 재발송 URL
                // 팩스 발송 내역 보기 srfidd
                returnData.put("srfidd", srfidd);
                return new ModelAndView("common/fax_resend_view", returnData);
            }
            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/common/resendConts.do", method={RequestMethod.GET, RequestMethod.POST})  // /common/resendMime.do
    public ModelAndView reSend(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> retMap = new HashMap<>();
            MimeViewVo mimeVo = new MimeViewVo();
            MimeViewVo tmpMimeVo = new MimeViewVo();

            String sServiceNo = ServletRequestUtils.getStringParameter(request, "serviceNo");
            String sCustomerKey = ServletRequestUtils.getStringParameter(request, "customerKey");
            String sCustomerNm = ServletRequestUtils.getStringParameter(request, "customerNm");
            String sCustomerEmail = ServletRequestUtils.getStringParameter(request, "customerEmail");
            String sResultSeq = ServletRequestUtils.getStringParameter(request, "resultSeq");
            String sType = ServletRequestUtils.getStringParameter(request, "type");
            String sReplaceEmail = ServletRequestUtils.getStringParameter(request, "targetMail");

            mimeVo.setResultView("true");
            mimeVo.setServiceNo(Integer.parseInt(sServiceNo));
            mimeVo.setCustomerKey(sCustomerKey);
            mimeVo.setCustomerNm(sCustomerNm);
            mimeVo.setCustomerEmail(sCustomerEmail);
            mimeVo.setResultSeq(Long.parseLong(sResultSeq));
            mimeVo.setType(sType);

            if(sType.equalsIgnoreCase("EC")) {
                try {
                    tmpMimeVo = mimeViewService.getECMimeInfo(mimeVo);
                } catch(NullPointerException e) {
                    tmpMimeVo = null;
                }
            } else if(sType.equalsIgnoreCase("EM")) {
                try {
                    tmpMimeVo = mimeViewService.getEMMimeInfo(mimeVo);
                } catch(NullPointerException e) {
                    tmpMimeVo = null;
                }
            }

            if(!MailPreviewService.isLoad()) {
                mailPreviewService.loadConf();
            }

            if(tmpMimeVo != null) {
                ReadingMime parse = mimeViewService.getMimeParse(tmpMimeVo);
                parse.mimeReading();

                SimpleMailSender sender = new SimpleMailSender();
                sender.setDnsHost("164.124.101.2");
                sender.setHelo(Config.getSmtpHelo());
                sender.setLocalIp(this.localIp);
                parse.mimeParse();
                sender.setMailFrom(parse.getFrom());

                if(sReplaceEmail == null || sReplaceEmail.trim().length() <= 0) {
                    sender.setRcptTo(parse.getTo());
                } else {
                    sender.setRcptTo(sReplaceEmail);
                }

                sender.sendMessage(parse.getMime());
                if(!sender.getRtnCode().equals("250")) {
                    retMap.put("msg", sender.getRtnMsg());
                    retMap.put("result", "F");
                } else {
                    retMap.put("msg", sender.getRtnMsg());
                    retMap.put("result", "S");
                }
                sender.close();
            }

            retMap.put("mimeVo", mimeVo);

            return new ModelAndView("/common/resendConts", retMap);  // /common/mail_mime_resend
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 고객이력] 다시보기(팝업)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/replayMime.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView resultView(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> retMap = new HashMap<>();
            MimeViewVo mimeVo = new MimeViewVo();
            MimeViewVo tmpMimeVo = new MimeViewVo();

            String sServiceNo = ServletRequestUtils.getStringParameter(request, "serviceNo");
            String sCustomerKey = ServletRequestUtils.getStringParameter(request, "customerKey");
            String sCustomerNm = ServletRequestUtils.getStringParameter(request, "customerNm");
            String sCustomerEmail = ServletRequestUtils.getStringParameter(request, "customerEmail");
            String sResultSeq = ServletRequestUtils.getStringParameter(request, "resultSeq");
            String sType = ServletRequestUtils.getStringParameter(request, "type");

            mimeVo.setResultView("true");
            mimeVo.setServiceNo(Integer.parseInt(sServiceNo));
            mimeVo.setCustomerKey(sCustomerKey);
            mimeVo.setCustomerNm(sCustomerNm);
            mimeVo.setCustomerEmail(sCustomerEmail);
            mimeVo.setResultSeq(Long.parseLong(sResultSeq));
            mimeVo.setType(sType);

            if(sType.equalsIgnoreCase("EC")) {
                try {
                    tmpMimeVo = mimeViewService.getECMimeInfo(mimeVo);
                } catch(NullPointerException e) {
                    tmpMimeVo = null;
                }
            } else if(sType.equalsIgnoreCase("EM")) {
                try {
                    tmpMimeVo = mimeViewService.getEMMimeInfo(mimeVo);
                } catch(NullPointerException e) {
                    tmpMimeVo = null;
                }
            }

            if(tmpMimeVo == null) {
                mimeVo.setMime("<br><center><p>발송된 마임이 존재하지 않습니다.</p></center>");
            } else {
                try {
                    ReadingMime parse = mimeViewService.getMimeParse(tmpMimeVo);
                    parse.mimeReading();
                    parse.mimeParse();

                    mimeVo.setMime(parse.getContent());
                    mimeVo.setAttachFiles(parse.getAttachFiles());
                    mimeVo.setFrom(parse.getFrom());
                    mimeVo.setTo(parse.getTo());
                    mimeVo.setSubject(StringUtil.escapeXss(parse.getSubject()));

                } catch(FileNotFoundException e) {
                    mimeVo.setMime("<br><center><p>발송된 마임이 삭제되었거나, 파일이 존재하지 않습니다.</p></center>");
                }
            }

            retMap.put("mimeVo", mimeVo);

            return new ModelAndView("/common/previewConts_mail", retMap);  // common/mail_mime_view
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    public ModelAndView resultAttachView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String> retMap = new HashMap<>();
            MimeViewVo mimeVo = new MimeViewVo();
            String sServiceNo = request.getParameter("serviceNo");
            String sCustomerKey = request.getParameter("customerKey");
            String sCustomerNm = request.getParameter("customerNm");
            String sCustomerEmail = request.getParameter("customerEmail");
            String sResultSeq = request.getParameter("resultSeq");
            String sFileNm = request.getParameter("fileNm");

            mimeVo.setServiceNo(Integer.parseInt(sServiceNo));
            mimeVo.setCustomerKey(sCustomerKey);
            mimeVo.setCustomerNm(sCustomerNm);
            mimeVo.setCustomerEmail(sCustomerEmail);
            mimeVo.setResultSeq(Long.parseLong(sResultSeq));

            mimeVo = mimeViewService.getECMimeInfo(mimeVo);

            response.setContentType("text/html; charset=EUC-KR");

            retMap.put("contents", (mimeViewService.getAttachContent(mimeVo, sFileNm)));

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + sFileNm + "\"");

            OutputStream out = response.getOutputStream();
            out.write((mimeViewService.getAttachContent(mimeVo, sFileNm)).getBytes());
            IOUtil.closeQuietly(out);

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/common/suspendMimeList.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView suspendMimeList(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> retMap = new HashMap<>();
            String schannelType = ServletRequestUtils.getStringParameter(request, "channelType");
            int serviceNo = ServletRequestUtils.getIntParameter(request, "serviceNo");

            Map<String, Object> tmpMap = mimeViewService.getSuspendList(schannelType, serviceNo);

            retMap.put("list", tmpMap.get("list"));
            retMap.put("channel", schannelType);
            retMap.put("serviceNo", serviceNo);
            retMap.put("tid", tmpMap.get("tid"));

            return new ModelAndView("/common/mail_suspend_list", retMap);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/common/suspendMime.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView suspendView(HttpServletRequest request) throws Exception {
        try {
            MimeViewVo mimeVo = new MimeViewVo();
            mimeVo.setServiceNo(Integer.parseInt(ServletRequestUtils.getStringParameter(request, "serviceNo")));
            mimeVo.setCustomerKey(ServletRequestUtils.getStringParameter(request, "customerKey"));
            mimeVo.setCustomerNm(ServletRequestUtils.getStringParameter(request, "customerNm"));
            mimeVo.setCustomerEmail(ServletRequestUtils.getStringParameter(request, "customerEmail"));
            mimeVo.setType(ServletRequestUtils.getStringParameter(request, "channelType"));
            mimeVo.setHanIdx(ServletRequestUtils.getStringParameter(request, "hanIdx"));
            mimeVo.setFileIdx(ServletRequestUtils.getStringParameter(request, "fileIdx"));
            mimeVo.setStartIndex(ServletRequestUtils.getStringParameter(request, "startPos"));
            mimeVo.setEndIndex(ServletRequestUtils.getStringParameter(request, "endPos"));
            mimeVo.setTid(ServletRequestUtils.getStringParameter(request, "tid"));

            Map<String, Object> retMap = new HashMap<>();
            retMap.put("mimeVo", mimeViewService.getMimeInfo(mimeVo));

            return new ModelAndView("/common/mail_suspend_view", retMap);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/common/suspendAttachMime.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView suspendAttachView(HttpServletRequest request) throws Exception {
        try {
            String sFileNm = ServletRequestUtils.getStringParameter(request, "fileNm");

            final MimeViewVo mimeVo = new MimeViewVo();
            mimeVo.setServiceNo(Integer.parseInt(ServletRequestUtils.getStringParameter(request, "serviceNo")));
            mimeVo.setCustomerKey(ServletRequestUtils.getStringParameter(request, "customerKey"));
            mimeVo.setCustomerNm(ServletRequestUtils.getStringParameter(request, "customerNm"));
            mimeVo.setCustomerEmail(ServletRequestUtils.getStringParameter(request, "customerEmail"));
            mimeVo.setType(ServletRequestUtils.getStringParameter(request, "channelType"));
            mimeVo.setHanIdx(ServletRequestUtils.getStringParameter(request, "hanIdx"));
            mimeVo.setFileIdx(ServletRequestUtils.getStringParameter(request, "fileIdx"));
            mimeVo.setStartIndex(ServletRequestUtils.getStringParameter(request, "startPos"));
            mimeVo.setEndIndex(ServletRequestUtils.getStringParameter(request, "endPos"));
            mimeVo.setTid(ServletRequestUtils.getStringParameter(request, "tid"));
            mimeVo.setAttachFileNm(sFileNm);

            final MimeViewVo newMimeVo = mimeViewService.getMimeInfo(mimeVo);

            Map<String, Object> retMap = new HashMap<>();
            retMap.put("contents", newMimeVo.getAttachFiles().get(sFileNm));

            return new ModelAndView("/common/previewConts_attach", retMap);  // common/mail_mime_attach_view
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 이케어 고객이력조회 결과보기
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    @RequestMapping(value="/common/reseltMime.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView view_result(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            Hashtable<String, Object> previewTable = null;
            MimeViewVo mimeVo = new MimeViewVo();
            int serviceNo = ServletRequestUtils.getIntParameter(request, "serviceNo", 0);
            String serviceType = ServletRequestUtils.getStringParameter(request, "serviceType");
            String condition = ServletRequestUtils.getStringParameter(request, "condition", " ");
            String customerKey = ServletRequestUtils.getStringParameter(request, "customerKey", " ");
            String slot1 = ServletRequestUtils.getStringParameter(request, "slot1", " ");
            String slot2 = ServletRequestUtils.getStringParameter(request, "slot2", " ");

            String useResendButton = null;
            boolean resendBtn = false;

            // session 값을 넣어주어 발송 URL(resendHistory) 에서 체크하여 직접 접근하지 못하도록한다.
            HttpSession session = request.getSession();
            session.putValue("resend", "alive");

            mimeVo.setServiceNo(serviceNo);
            mimeVo.setType(serviceType);
            mimeVo.setCustomerKey(condition); // 결과보기시에 seq값이 넘어가지 않아서 추가함

            boolean isResendHistory = false;

            previewTable = mailPreviewService.makePreview(serviceNo, serviceType, condition, customerKey, slot1, slot2, isResendHistory);

            useResendButton = (String) previewTable.get("useResendButton");
            if(useResendButton.equals("on")) {
                resendBtn = true;
            }

            mimeVo.setCondition((String) previewTable.get("condition"));
            mimeVo.setCustomerEmail((String) previewTable.get("customerEmail"));
            mimeVo.setResendButton(resendBtn);
            mimeVo.setMime((String) previewTable.get("content"));

            // 채널 타입이 메일인 경우
            String channelType = (String) previewTable.get("channelType");
            if("M".equalsIgnoreCase(channelType)) {
                String tmp = (String) previewTable.get("to");
                mimeVo.setTo(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                tmp = (String) previewTable.get("from");
                mimeVo.setFrom(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                mimeVo.setSubject(StringUtil.escapeXss((String) previewTable.get("subject")));
                Map<String, Object> map = (Map<String, Object>) previewTable.get("attachFile");
                mimeVo.setAttachFiles(map);
            }
            returnData.put("mimeVo", mimeVo);

            return new ModelAndView("common/previewConts_mail", returnData);  // common/mail_mime_view
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 이케어 고객이력조회 결과보기 재발송
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value="/common/resendHistoryMime.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView reSendHistory(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            Hashtable<String, Object> previewTable = null;
            MimeViewVo mimeVo = new MimeViewVo();
            int serviceNo = ServletRequestUtils.getIntParameter(request, "serviceNo", 0);
            String condition = ServletRequestUtils.getStringParameter(request, "condition");
            String targetMail = ServletRequestUtils.getStringParameter(request, "targetMail");
            HttpSession session = request.getSession();
            String checkSession = (String) session.getValue("resend");

            // session 체크를 해서 발송 URL 에 직접 접근하지 못하도록한다.
            if(checkSession == null) {
                return new ModelAndView("common/error/error", returnData);
            } else {
                session.removeValue("resend");
            }
            mimeVo.setServiceNo(serviceNo);
            mimeVo.setCondition(condition);
            String serviceType = "EC";
            String subType = null;
            boolean isResendHistory = true;

            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String seq = formatter.format(currentTime);
            String reqDt = seq.substring(0, 8);
            String reqTm = seq.substring(8, 14);

            // 1000~9999까지의 정수를 랜덤하게 생성하여 seq 에 붙여서 새 seq 를 생성
            SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
            r.setSeed(new Date().getTime());
            int i = (int) ((r.nextDouble() * 9000) + 1000);
            seq = seq + i;

            previewTable = mailPreviewService.makePreview(serviceNo, serviceType, condition, "", "", "", isResendHistory);
            String isJeonmun = (String) previewTable.get("isJeonmun");
            String jeonmun = (String) previewTable.get("header") + "||" + (String) previewTable.get("target");
            mimeVo.setCustomerEmail(targetMail);
            mimeVo.setResendEcareNo((Integer) previewTable.get("resendEcareNo"));
            mimeVo.setChannel((String) previewTable.get("channelType"));
            mimeVo.setSeq(seq);
            mimeVo.setReqDt(reqDt);
            mimeVo.setReqTm(reqTm);
            mimeVo.setCustomerNm((String) previewTable.get("customerNm"));
            mimeVo.setCustomerKey((String) previewTable.get("customerKey"));
            mimeVo.setJeonmun(jeonmun);
            mimeVo.setSubject(StringUtil.escapeXss((String) previewTable.get("subject")));

            serviceType = (String) previewTable.get("serviceType");
            subType = (String) previewTable.get("subType");

            // NVREALTIMEACCEPT 테이블에 재발송 대상자를 insert 하기위해 mimeVo 에 데이터를 수집한다.
            // 스케쥴에 대한 재발송이면...
            // nvrealtimeaccept 테이블에 필드값으로 어떤 경우에 대한 재발송인지 알 수 있다.
            // REQ_USER_ID = schedule:스케쥴 발송이력에 대한 재발송 or defferedtime:준실시간 발송이력에 대한
            // 재발송
            // SENDER_NM = normal:스케쥴 중 기존테이블을 이용한 서비스의 재발송
            // or jeonmun:스케쥴 중 전문테이블을 이용한 서비스의 재발송
            // or seq:준실시간에 대한 재발송
            // SENDER = 원천정보 S:기존테이블을 이용한 서비스의 재발송 , J:전문테이블을 이용한 서비스의 재발송
            if(serviceType.equals("S") && subType.equals("S")) {
                if(isJeonmun.equals("true")) {
                    // 스케쥴 전문일 경우
                    // REQ_USER_ID = schedule
                    // SENDER_NM = jeonmun
                    // SENDER = J
                    mailPreviewService.insertResendDataForJeonmun(mimeVo);
                } else {
                    // 스케쥴 일반 테이블일 경우
                    // REQ_USER_ID = schedule
                    // SENDER_NM = normal
                    // SENDER = S
                    mailPreviewService.insertResendDataForSchedule(mimeVo);
                }
            } else {
                // 준실시간일 경우는 seq, email 등 mimveVo 에 들어온 값을 사용하여 값만 바꿔서 그대로 insert.
                // REQ_USER_ID = defferedtime
                // SENDER_NM = 재발송 이력에 남아있는 seq
                // SENDER = 원래값 그대로 S or J
                mailPreviewService.insertResendDataForDefferedTime(mimeVo);
            }

            returnData.put("mimeVo", mimeVo);

            return new ModelAndView("common/mail_resend_result", returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 외부 URL 호출용 미리보기 결과보기와 로직은 같다.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public ModelAndView view_pre(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            Hashtable<String, Object> previewTable = null;
            MimeViewVo mimeVo = new MimeViewVo();
            int serviceNo = ServletRequestUtils.getIntParameter(request, "serviceNo", 0);
            String serviceType = ServletRequestUtils.getStringParameter(request, "serviceType");
            String condition = ServletRequestUtils.getStringParameter(request, "condition", " ");
            String customerKey = ServletRequestUtils.getStringParameter(request, "customerKey", " ");
            String slot1 = ServletRequestUtils.getStringParameter(request, "slot1", " ");
            String slot2 = ServletRequestUtils.getStringParameter(request, "slot2", " ");

            // session 값을 넣어주어 발송 URL(resendHistory) 에서 체크하여 직접 접근하지 못하도록한다.
            HttpSession session = request.getSession();
            session.putValue("resend", "alive");

            boolean isResendHistory = false;

            mimeVo.setServiceNo(serviceNo);
            mimeVo.setType(serviceType);
            mimeVo.setCondition(condition);
            mimeVo.setResendButton(false);

            previewTable = mailPreviewService.makePreview(serviceNo, serviceType, condition, customerKey, slot1, slot2, isResendHistory);

            mimeVo.setCustomerEmail((String) previewTable.get("customerEmail"));
            mimeVo.setMime((String) previewTable.get("content"));

            // 채널 타입이 메일인 경우
            String channelType = (String) previewTable.get("channelType");
            if("M".equalsIgnoreCase(channelType)) {
                String tmp = (String) previewTable.get("to");
                mimeVo.setTo(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                tmp = (String) previewTable.get("from");
                mimeVo.setFrom(tmp.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                mimeVo.setSubject(StringUtil.escapeXss((String) previewTable.get("subject")));
                Map<String, Object> map = (Map<String, Object>) previewTable.get("attachFile");
                mimeVo.setAttachFiles(map);
            }
            returnData.put("mimeVo", mimeVo);

            return new ModelAndView("common/previewConts_mail", returnData);  // common/mail_mime_view
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/common/defferedTargetSend.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto defferedTargetSend(String seq) {
        try {
            int rowCount = mailPreviewService.defferedTargetSend(seq);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(rowCount);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value="/common/defferedTargetReSend.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto defferedTargetReSend(String seq, String srfidd, String receiver) {
        try {
            int rowCount = mailPreviewService.defferedTargetReSend(seq, srfidd, receiver);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(rowCount);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
