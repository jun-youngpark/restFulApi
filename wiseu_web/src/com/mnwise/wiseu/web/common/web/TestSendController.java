package com.mnwise.wiseu.web.common.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.TestSendVo;
import com.mnwise.wiseu.web.common.service.TesterService;

/**
 * 테스트 발송 결과보기 Controller
 */
@Controller
public class TestSendController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TestSendController.class);

    @Autowired private TesterService testerService;

    /**
     * [캠페인>캠페인 등록] 캠페인 테스트발송(팝업) - 테스트 발송 결과보기<br/>
     * [이케어>이케어 등록] 이케어 테스트발송(팝업) - 테스트 발송 결과보기 <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/common/testSendResultList.do", method = {RequestMethod.GET, RequestMethod.POST})  // /common/tester_popup_send_view.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            int serviceNo = ServletRequestUtils.getIntParameter(request, "no", 1);
            String serviceType = ServletRequestUtils.getStringParameter(request, "serviceType");
            String channelType = ServletRequestUtils.getStringParameter(request, "channelType");

            TestSendVo testSendVo = new TestSendVo();

            PagingUtil.setPagingInfo(request, testSendVo);

            int testSendListCount = testerService.getTestSendListCount(serviceNo, serviceType);  // paging 위한 total row count
            // log.debug("카운트 숫자 : " +testSendListCount);

            ModelAndView mav = new ModelAndView("common/testSendResultList");  // common/tester_popup_send_view
            List<TestSendVo> testSendList = new ArrayList<>();
            if(testSendListCount > 0) {
                testSendList = testerService.getTestSendList(testSendVo, serviceNo, serviceType);
                mav.addObject("testSendList", testSendList);
            }

            mav.addObject("serviceNo", String.valueOf(serviceNo));
            mav.addObject("serviceType", serviceType);
            mav.addObject("channelType", channelType);
            mav.addObject("testSendVo", testSendVo);
            mav.addObject("totalCount", testSendListCount);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
