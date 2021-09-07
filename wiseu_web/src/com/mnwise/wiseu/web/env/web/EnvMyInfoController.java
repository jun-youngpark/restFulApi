package com.mnwise.wiseu.web.env.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.account.service.AccountService;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.model.EnvMyInfoVo;
import com.mnwise.wiseu.web.env.service.EnvMyInfoService;

/**
 * 개인정보관리 Controller
 */
@Controller
public class EnvMyInfoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvMyInfoController.class);

    @Autowired private AccountService accountService;
    @Autowired private EnvMyInfoService envMyInfoService;

    /**
     * - [환경설정>개인정보 관리] 개인정보 관리 <br/>
     * - URL : /env/myInfo.do <br/>
     * - JSP : /env/myInfo.jsp <br/>
     * 환경설정 - 개인정보 관리에서 개인정보를 가져온다.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/myInfo.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/env_myinfo.do
    public ModelAndView list(String message, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = new UserVo();
            userVo.setUserId(getLoginId());
            userVo = envMyInfoService.selectEnvMyInfo(userVo.getUserId());

            EnvMyInfoVo envMyInfoVo = new EnvMyInfoVo();
            BeanUtil.copyProperties(envMyInfoVo, userVo);
            envMyInfoVo.setUsertypeCd(userVo.getUserTypeCd());
            envMyInfoVo.setLastupdateDt(userVo.getLastUpdateDt());

            HashMap<String, Object> returnData = new HashMap<>();
            returnData.put("envMyInfoVo", userVo);
            returnData.put("webExecMode", super.webExecMode);
            returnData.put("message", message);

            return new ModelAndView("/env/myInfo", returnData);  // /env/env_myinfo
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 3개월안에 사용한 비밀번호인지 체크
     *
     * @param userId
     * @param pass
     * @return 사용한 적이 없으면 0, 있으면 1이상
     * @throws Exception
     */
    @RequestMapping(value="/env/checkShaPw.json", method={RequestMethod.GET, RequestMethod.POST})  // /env/{userId}/env_myinfo.do
    @ResponseBody public int checkShaPw(String userId, @RequestParam(value="passWd", required = true) String passWd) throws Exception {
        try {
            return accountService.checkShaPw(userId, passWd);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>개인정보 관리] 개인정보 관리 - 저장
     *
     * @param envMyInfoVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/updateMyInfo.do", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto update(EnvMyInfoVo envMyInfoVo) throws Exception {
        try {
            envMyInfoVo.setLastupdateDt(new SimpleDateFormat("yyyyMMdd").format(new Date()));

            String validMsg = validate(envMyInfoVo);
            String message = "";

            if(validMsg.equals("DPFAIL")) {
                message = validMsg;
            } else if(validMsg.equals("UPFAIL")) {
                message = validMsg;
            } else if(validMsg.equals("NPFAIL")) {
                message = validMsg;
            } else if(validMsg.equals("SPFAIL")) {
                message = validMsg;
            } else if(validMsg.equals("PLFAIL")) {
                message = validMsg;
            } else if(validMsg.equals("SUCCESS")) {
                // data Modify
                envMyInfoService.updateEnvMyInfo(envMyInfoVo);
                message = "save";
            }
            return new ResultDto(ResultDto.OK, message);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    public String validate(EnvMyInfoVo envMyInfoVo) {
        String passWd = envMyInfoVo.getPassWd();
        String validFlag = "SUCCESS";

        // 영문 소문자
        Pattern downPattern = Pattern.compile("[a-z]");
        Matcher downMach = downPattern.matcher(passWd);
        // 패턴 매치 실패
        if(!downMach.find()) {
            validFlag = "DPFAIL";
            return validFlag;
        }

        // 영문 대문자
        Pattern upPattern = Pattern.compile("[A-Z]");
        Matcher upMach = upPattern.matcher(passWd);
        // 패턴 매치 실패
        if(!upMach.find()) {
            validFlag = "UPFAIL";
            return validFlag;
        }

        // 숫자
        Pattern numPattern = Pattern.compile("\\d");
        Matcher numMach = numPattern.matcher(passWd);
        // 패턴 매치 실패
        if(!numMach.find()) {
            validFlag = "NPFAIL";
            return validFlag;
        }

        // 특수문자
        Pattern spePattern = Pattern.compile("[~!@#$%^&*()]");
        Matcher speMach = spePattern.matcher(passWd);
        // 패턴 매치 실패
        if(!speMach.find()) {
            validFlag = "SPFAIL";
            return validFlag;
        }

        // 9자리 ~ 14자리 사이
        if(passWd.length() < 8 || passWd.length() > 14) {
            // 패턴 매치 실패
            validFlag = "PLFAIL";
            return validFlag;
        }

        return validFlag;
    }
}
