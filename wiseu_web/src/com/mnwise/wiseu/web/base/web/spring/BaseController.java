package com.mnwise.wiseu.web.base.web.spring;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.admin.model.AdminSessionVo;

@Controller
public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Value("${env.product.use}")
    protected String envProductUse;
    @Value("${channel.use.list}")
    protected String channelUseList;
    @Value("${campaign.channel.use.list}")
    protected String campaignChannelUseList;
    @Value("${ecare.channel.use.list}")
    protected String ecareChannelUseList;
    @Value("${web.exec.mode:1}")
    protected String webExecMode;
    @Value("${import.upload.dir}")
    protected String importUploadDir;
    @Value("${template.upload.path}")
    protected String templateUploadDir;
    @Value("${template.upload.url}")
    protected String templateDefaultUrl;
    @Value("${lst.resend.use}")
    protected String lstResendUse;
    @Value("${security.mail.use:off}")
    protected String useSecurityMail;
    @Value("${pdf.mail.use:off}")
    protected String usePdfMail;
    @Value("${sms.individual.charge}")
    protected String smsIndividualCharge;

    protected AdminSessionVo getLoginSessionVo() {
        return (AdminSessionVo) RequestContextHolder.currentRequestAttributes().getAttribute("adminSessionVo", RequestAttributes.SCOPE_SESSION);
    }

    protected UserVo getLoginUserVo() {
        return ((AdminSessionVo) RequestContextHolder.currentRequestAttributes().getAttribute("adminSessionVo", RequestAttributes.SCOPE_SESSION)).getUserVo();
    }

    protected String getLoginId() {
        return ((AdminSessionVo) RequestContextHolder.currentRequestAttributes().getAttribute("adminSessionVo", RequestAttributes.SCOPE_SESSION)).getUserVo().getUserId();
    }

    protected Map<String, Map<String, String>> getLoginRoleMap() {
        return ((AdminSessionVo) RequestContextHolder.currentRequestAttributes().getAttribute("adminSessionVo", RequestAttributes.SCOPE_SESSION)).getRoleMap();
    }

    // 시큐어 코딩 추가- 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
    protected boolean isInvalidAccess(String grpCd, String userId) {
        return isInvalidAccess(grpCd, userId, getLoginUserVo().getUserTypeCd());
    }
    // 시큐어 코딩 추가- 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
    protected boolean isInvalidAccess(String grpCd, String userId, String userTypeCd) {
        UserVo userVo = getLoginUserVo();
        if(StringUtil.equals(userVo.getUserTypeCd(), "A")){
            return false;
        }

        if(StringUtil.equals(userTypeCd, "U")) {  // 사용자 권한
            if(StringUtil.equals(userVo.getUserId(), userId) == false) {
                log.error("No permission... Session User : " + userVo.getUserId() + " / Access Data User : " + userId);
                return true;
            }
        } else if(StringUtil.equals(userTypeCd, "M") || StringUtil.equals(userTypeCd, "G")) {  // 매니저 권한
            if(grpCd.startsWith(userVo.getGrpCd()) == false) {
                log.error("No permission... Session User : " + userVo.getUserId() + " / Access Data User : " + userId);
                return true;
            }
        }

        return false;
    }

    /**
     * 브라우저 구분을 얻는다.
     *
     * @param userAgent User-Agent HTTP Request Header
     * @return browser 브라우저명
     */
    protected String getBrowser(String userAgent) {
        if(userAgent != null) {
            if(userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1) {
                return "MSIE";
            } else if(userAgent.indexOf("Chrome") > -1) {
                return "Chrome";
            } else if(userAgent.indexOf("Opera") > -1) {
                return "Opera";
            }
        }

        return "Firefox";
    }

    /**
     * Content-Disposition 헤더를 설정한다.
     *
     * @param filename 파일명
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return 인코딩된 파일명
     * @throws Exception 오류가 발생하는 경우
     */
    public String setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String browser = getBrowser(request.getHeader("User-Agent"));

        String encodedFilename = null;

        if(browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if(browser.equals("Firefox") || browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if(browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if(c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new IOException("Not supported browser");
        }

        response.setHeader("Content-Disposition", "attachment; filename=" + encodedFilename);

        if("Opera".equals(browser)) {
            response.setContentType("application/octet-stream;charset=UTF-8");
        }

        return encodedFilename;
    }
}
