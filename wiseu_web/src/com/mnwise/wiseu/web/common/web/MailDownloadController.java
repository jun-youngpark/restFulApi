package com.mnwise.wiseu.web.common.web;

import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import com.mnwise.wiseu.web.common.service.MailPreviewService;
import com.mnwise.wiseu.web.common.service.MimeViewService;

@Controller
public class MailDownloadController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MailDownloadController.class);

    @Autowired private MailPreviewService mailPreviewService;
    @Autowired private MimeViewService mimeViewService;

    @RequestMapping(value = "/common/mime_attachDown.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView downloadMailAttach(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String cmd = StringUtil.escapeXss(request.getParameter("cmd"));
            String sServiceNo = StringUtil.escapeXss(request.getParameter("serviceNo"));
            String sCustomerKey = StringUtil.escapeXss(request.getParameter("customerKey"));
            String sCustomerNm = StringUtil.escapeXss(request.getParameter("customerNm"));
            String sCustomerEmail = StringUtil.escapeXss(request.getParameter("customerEmail"));
            String sResultSeq = StringUtil.escapeXss(request.getParameter("resultSeq"));
            // TODO : 파일
            String sFileNm = StringUtil.escapeXss(request.getParameter("fileNm")).replaceAll("\\.\\.", "").replaceAll("\\r\\n", "");
            sFileNm = new String(Base64.decodeBase64(sFileNm.getBytes()));

            log.debug("File name : " + sFileNm);
            log.debug("cmd : " + cmd);

            MimeViewVo mimeVo = new MimeViewVo();
            Object content = null;
            if(cmd.equals("resultAttachView")) {
                try {
                    mimeVo.setServiceNo(Integer.parseInt(sServiceNo));
                    mimeVo.setCustomerKey(sCustomerKey);
                    mimeVo.setCustomerNm(sCustomerNm);
                    mimeVo.setCustomerEmail(sCustomerEmail);
                    mimeVo.setResultSeq(Long.parseLong(sResultSeq));

                    mimeVo = mimeViewService.getECMimeInfo(mimeVo);
                    content = mimeViewService.getAttachContent(mimeVo, sFileNm);
                } catch(Exception e) {
                    log.debug("Result attach view error : " + e.getMessage());
                }
            } else if(cmd.equals("preview")) {
                try {
                    int serviceNo = ServletRequestUtils.getIntParameter(request, "serviceNo", 0);
                    String customerkey = StringUtil.escapeXss(request.getParameter("customerKey"));
                    String customerNm = StringUtil.escapeXss(request.getParameter("customerNm"));
                    String customerEmail = StringUtil.escapeXss(request.getParameter("customerEmail"));
                    String serviceType = StringUtil.escapeXss(request.getParameter("serviceType"));
                    String subType = StringUtil.escapeXss(request.getParameter("subType"));
                    String seg = StringUtil.escapeXss(request.getParameter("seg"));
                    mimeVo.setServiceNo(serviceNo);
                    mimeVo.setType(serviceType);
                    mimeVo.setCustomerKey(customerkey);
                    mimeVo.setCustomerNm(customerNm);
                    mimeVo.setCustomerEmail(customerEmail);
                    mimeVo.setSeg(seg);

                    // Hashtable previewTable =
                    // mailPreviewService.makePreview(serviceNo, customerkey,
                    // serviceType, subType,"20081217", seg, seq);
                    Hashtable<String, Object> previewTable = mailPreviewService.makePreview(serviceNo, customerkey, null, serviceType, subType, "20081217", seg);

                    mimeVo.setMime((String) previewTable.get("content"));
                    mimeVo.setTo((String) previewTable.get("to"));
                    mimeVo.setFrom((String) previewTable.get("from"));
                    mimeVo.setSubject((String) previewTable.get("subject"));
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) previewTable.get("attachFile");
                    mimeVo.setAttachFiles(map);
                    content = map.get(sFileNm);
                } catch(Exception e) {
                    log.debug("Preview error : " + e.getMessage());
                }
            } else if(cmd.equals("suspendAttachView")) {
                try {
                    String sType = StringUtil.escapeXss(request.getParameter("channelType"));
                    String sHanIdx = StringUtil.escapeXss(request.getParameter("hanIdx"));
                    String sFileIdx = StringUtil.escapeXss(request.getParameter("fileIdx"));
                    String sStartPos = StringUtil.escapeXss(request.getParameter("startPos"));
                    String sEndPos = StringUtil.escapeXss(request.getParameter("endPos"));
                    String sTid = StringUtil.escapeXss(request.getParameter("tid"));

                    mimeVo.setServiceNo(Integer.parseInt(sServiceNo));
                    mimeVo.setCustomerKey(sCustomerKey);
                    mimeVo.setCustomerNm(sCustomerNm);
                    mimeVo.setCustomerEmail(sCustomerEmail);
                    mimeVo.setType(sType);
                    mimeVo.setHanIdx(sHanIdx);
                    mimeVo.setFileIdx(sFileIdx);
                    mimeVo.setStartIndex(sStartPos);
                    mimeVo.setEndIndex(sEndPos);
                    mimeVo.setTid(sTid);
                    mimeVo.setAttachFileNm(sFileNm);

                    mimeVo = mimeViewService.getMimeInfo(mimeVo);
                    content = (String) mimeVo.getAttachFiles().get(sFileNm);
                } catch(Exception e) {
                    log.debug("Suspend attach view error : " + e.getMessage());
                }
            }

            if(content != null) {
                response.setContentType("application/download;charset=euc-kr");
                response.setHeader("Pragma", "public");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Cache-Control", "max-age=0");
                // response.setHeader("Content-Disposition", "attachment;
                // filename=\"" + sFileNm + "\"");
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
                    log.debug("IO write error : " + e.getMessage());
                } finally {
                    IOUtil.closeQuietly(out);
                }
            }

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

}
