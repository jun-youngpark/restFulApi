package com.mnwise.wiseu.web.ecare.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.env.service.EnvServerInfoService;

/**
 * 이케어 재발송 처리 Controller
 */
@Controller("ecareResendController")
public class ResendController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ResendController.class);

    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private EnvServerInfoService envServerInfoService;

    @RequestMapping(value = "/ecare/resend.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView resend(HttpServletRequest request) throws Exception {
        try {
            int scenarioNo = ServletRequestUtils.getIntParameter(request, "scenarioNo", 0);
            int ecareNo = ServletRequestUtils.getIntParameter(request, "ecareNo", 0);
            int depthNo = ServletRequestUtils.getIntParameter(request, "depthNo", 1);
            String resultSeq = ServletRequestUtils.getStringParameter(request, "resultSeq", "0");
            String channelType = ServletRequestUtils.getStringParameter(request, "channelType");
            String resendMode = ServletRequestUtils.getStringParameter(request, "mode");

            EcareScenarioVo scenarioVo = new EcareScenarioVo();
            scenarioVo.setScenarioNo(scenarioNo);
            scenarioVo.getEcareVo().setEcareNo(ecareNo);
            scenarioVo.getEcareVo().setResultSeq(resultSeq);

            scenarioVo.setUserVo(getLoginUserVo());

            ServerInfoVo serverInfo = envServerInfoService.selectEnvServerInfo();

            int newEcareNo = 0;

            final String resendParam = ServletRequestUtils.getStringParameter(request, "resendMode", " ");

            if(isOmniChannelMessage(resendParam.substring(0, 1))) {
                final UserVo userVo = scenarioVo.getUserVo();
                newEcareNo = ecareScenarioService.makeOmniChannelSubEcare(scenarioNo, ecareNo, resendParam, userVo);
            } else {
                newEcareNo = ecareScenarioService.setRegistResendEcare(scenarioVo, resendMode, serverInfo);
            }

            depthNo++;

            return new ModelAndView(new org.springframework.web.servlet.view.RedirectView(
                "/ecare/ecare2Step.do?scenarioNo=" + scenarioNo + "&depthNo=" + depthNo + "&ecareVo.channelType=" + channelType + "&ecareVo.ecareNo=" + newEcareNo));  // /ecare/ecare_2step_form.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 생성할 캠페인이 옴니채널 메시지인지 relationType 으로 확인한다.
     *
     * @param relationType 'S': 성공, 'F': 실패, 'O': 오픈
     * @return
     */
    private boolean isOmniChannelMessage(String relationType) {
        return Const.RELATION_SUCCESS.equals(relationType) || Const.RELATION_FAIL.equals(relationType) || Const.RELATION_OPEN.equals(relationType);
    }
}
