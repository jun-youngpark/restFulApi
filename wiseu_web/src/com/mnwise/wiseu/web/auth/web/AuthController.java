package com.mnwise.wiseu.web.auth.web;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.admin.service.AdminService;
import com.mnwise.wiseu.web.auth.service.AuthService;
import com.mnwise.wiseu.web.common.util.AuthUtil;
import com.mnwise.wiseu.web.common.util.PropertyUtil;

@Controller
public class AuthController {
    private static final Log log = LogFactory.getLog(AuthController.class);

    @Autowired
    AuthService authService;
    @Autowired
    AdminService adminService;

    @Value("${vaildation.domain}")
    private String vaildDomainOnOff;

    private static final String[] validDomainList = PropertyUtil.getProperty("vaildation.domain.list").split(",");

    @RequestMapping(value = "/auth/check.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, String> authCheck(@RequestBody Map<String, String> authMap) throws Exception {
        Map<String, String> returnData = new HashMap<>();
        AuthUtil auth = new AuthUtil();
        try {
            authMap.put("validTime", auth.getValidTime());
            if(authService.checkUsedToken(authMap) > 0) {
                returnData.put("result", "false");
                returnData.put("errMsg", "이미 사용한 토큰입니다.");
                return returnData;
            }

            if(authService.checkTimeout(authMap) > 0) {
                returnData.put("result", "false");
                returnData.put("errMsg", "해당 토큰의 사용 시간이 만료되었습니다.");
                return returnData;
            }

            int cnt = authService.checkToken(authMap);
            if(cnt == 1) {
                returnData.put("result", "true");
                returnData.put("errMsg", "인증성공!");
            } else {
                returnData.put("result", "false");
                returnData.put("errMsg", "인증실패! 유효하지 않은 토큰입니다.");
            }
        }catch(SQLException e){
            log.error("while checking token, Exception occurred. " + e.getMessage());
            returnData.put("result", "false");
            returnData.put("errMsg", "인증실패! 관리자에게 문의하세요.");
        }
        return returnData;
    }

    @RequestMapping(value = "/auth/mail/send.do", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> mailSend(@RequestBody Map<String, String> authMap)  throws Exception {
        AuthUtil auth = new AuthUtil();
        String adminId= authMap.get("adminId");
        String adminPwd= authMap.get("adminPwd");
        String mail= authMap.get("mail");
        Map<String, String> returnData = new HashMap<>();
        if(StringUtil.checkNull(adminId) && StringUtil.checkNull(adminPwd)) {
            UserVo userVo = null;
            try {
                userVo = adminService.getAdminByAdminId(adminId, adminPwd);

                if(userVo == null) {
                    returnData.put("result", "false");
                    returnData.put("errMsg", "로그인실패");
                    log.info("접속 권한이 없거나 존재하지 않는 계정입니다. : ID " + adminId);
                    return returnData;
                } else {
                    // 로그인 시도 최종 횟수 가져옴 default = 0
                    int failCount = userVo.getLoginCnt();

                    GregorianCalendar date = new GregorianCalendar();
                    date.setGregorianChange(new Date());
                    date.add(Calendar.DATE, -90); // 3개월 전 날짜 추출

                    // 일반계정이고 login cnt 가 5회 넘을 경우 로그인 불가능
                    if(!adminId.equals("admin") && failCount >= 5) {
                        returnData.put("result", "false");
                        returnData.put("errMsg", "계정이잠겼습니다.");
                        log.info("계정이 잠겼습니다. : ID " + adminId);
                        return returnData;
                    }

                    if(!adminPwd.trim().equals(userVo.getPassWd())) {// 암호 불일치
                        String userId = userVo.getUserId();
                        if(!userId.equals("admin")) {
                            // admin이 아닐 경우 login cnt +1 업데이트
                            adminService.updateLoginFailCount(userId);
                            returnData.put("result", "false");
                            returnData.put("errMsg", "암호불일치");
                            log.info("5회 이상 로그인 실패 시 계정 잠깁니다. 현재 로그인 시도 횟수 : " + (failCount + 1) + " ID : " + adminId);
                            return returnData;
                        } else {
                            returnData.put("result", "false");
                            returnData.put("errMsg", "잘못된 비밀번호 입니다 ID :" + adminId);
                            log.info("잘못된 비밀번호 입니다 ID : " + adminId);
                            return returnData;
                        }
                    }
                }

                //이메일 유효성
                final EmailValidator emailValidator = EmailValidator.getInstance();

                if(emailValidator.isValid(mail) == false) {
                    returnData.put("result", "false");
                    returnData.put("errMsg", "계정에 등록된 메일형식이 올바르지 않습니다.");
                    return returnData;
                }

                if("on".equalsIgnoreCase(vaildDomainOnOff)) {
                    String domain = mail.substring(mail.indexOf("@")+1);
                    if(containsCaseInsensitive(Arrays.asList(validDomainList),domain)){
                        returnData.put("result", "false");
                        returnData.put("errMsg", "등록된 계정의 메일만 가능합니다. conf 파일 확인을 확인해주세요.");
                        return returnData;
                    }
                }

                authMap.put("validTime", auth.getValidTime());
                authMap.put("userId", userVo.getUserId());
                authMap.put("reqDtm", DateUtil.dateToString("yyyyMMddHHmmss", new Date()));
                authMap.put("token", auth.getRendonToken());

                //새로운 토큰 발급전에 기존에 유효한 토큰 무효화
                authService.updateVaildToken(authMap);
                //메일발송
                authService.insertAuthToken(authMap);

                Map<String, String> result = auth.sendAuthMail(mail);
                if("F".equals(result.get("result"))){
                    returnData.put("result", "false");
                    returnData.put("errMsg", "인증 메일 발송에 실패하였습니다. \n상세내역 : " + result.get("msg"));
                } else {
                    returnData.put("result", "true");
                    returnData.put("errMsg", "인증코드가 "+mail+" 메일로 발송되었습니다.");
                }

            } catch(Exception e) {
                log.error("while Sending Mail, Exception occurred. " + e.getMessage());
                returnData.put("result", "false");
                returnData.put("errMsg", "메일 보내기 실패! 관리자에게 문의하세요.");
                return returnData;
            }
        }
        return returnData;
    }

    public boolean containsCaseInsensitive(List<String> l , String s){
        for (String string : l){
           if (string.equalsIgnoreCase(s)){
               return false;
            }
        }
       return true;
   }

}
