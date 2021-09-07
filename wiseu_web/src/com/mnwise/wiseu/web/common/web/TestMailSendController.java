package com.mnwise.wiseu.web.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.TestGrpVo;
import com.mnwise.wiseu.web.common.model.TesterPoolVo;
import com.mnwise.wiseu.web.common.model.TreeParam;
import com.mnwise.wiseu.web.common.service.TesterService;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.env.model.TreeNode;
import com.mnwise.wiseu.web.env.model.TreeNodeState;

/**
 * 테스트 발송(팝업) Controller
 */
@Controller
public class TestMailSendController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TestMailSendController.class);

    @Autowired private TesterService testerService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    /**
     * - [캠페인>캠페인 등록>2단계] 캠페인 테스트발송(팝업)
     * - [이케어>이케어 등록>2단계] 이케어 테스트발송(팝업)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/testSendPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /common/testSend_popup.do
    public ModelAndView list(@RequestParam(defaultValue="0") int no, @RequestParam(defaultValue="0") int testGrpCd, @RequestParam(defaultValue="") String serviceType,
                             @RequestParam(defaultValue="") String channel, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            // 그룹 리스트를 가져온다.
            List<TestGrpVo> testGrpList = testerService.getTestGrpList(userVo.getUserId());

            TestGrpVo testGrpVo = null;
            List<TesterPoolVo> testerPoolList = null;

            // 생성되어 있는 그룹이 있을 경우 대상자 리스트를 가져온다.
            if(testGrpList != null) {
                // 현재 선택되어 있는 그룹이 없을 경우(grpCd 값 0) 최상위 그룹에 소속되어 있는 대상자들을 보여준다.
                TesterPoolVo testerPoolVo = new TesterPoolVo();
                testerPoolVo.setUserId(userVo.getUserId());
                testerPoolVo.setTestGrpCd(testGrpCd);
                testerPoolVo.setServiceNo(no);
                testerPoolVo.setServiceType(serviceType);
                testerPoolList = testerService.getTesterList(testerPoolVo);

                // 선택한 그룹의 정보를 가져온다. 현재 선택되어 있는 그룹이 없을 경우(grpCd 값 0) 최상위 그룹의 정보를 보여준다.
                testGrpVo = testerService.getTestGrpInfo(testGrpCd, userVo.getUserId());
                if(testGrpVo==null) {
                    testGrpVo = new TestGrpVo();
                }
            }
            ModelAndView mav = new ModelAndView("common/testSendPopup");  // common/testSend_popup
            mav.addObject("userId", userVo.getUserId());
            mav.addObject("no", no);
            mav.addObject("serviceType", serviceType);
            mav.addObject("testGrpVo", testGrpVo);
            mav.addObject("testerPoolList", testerPoolList);
            mav.addObject("testGrpList", testGrpList);
            mav.addObject("channel", channel);
            mav.addObject("webExecMode", super.webExecMode);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }


    /**
     * - [캠페인>캠페인 등록>2단계] 그룹 tree를 가져온다
     * - [이케어>이케어 등록>2단계] 그룹 tree를 가져온다
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/test_group.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ArrayList<TreeNode> codeTree(@RequestParam(defaultValue="0") int no, @RequestParam(defaultValue="0") int testGrpCd, @RequestParam(defaultValue="") String serviceType,
                             @RequestParam(defaultValue="") String channel, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            ArrayList<TreeNode> treeList = new ArrayList<TreeNode>();

            if(testGrpCd == 0) {
                String defaultGrpNm = "/";
                TestGrpVo testGrpVo = new TestGrpVo();
                testGrpVo.setTestGrpNm(defaultGrpNm);
                testGrpVo.setUserId(userVo.getUserId());
                testGrpCd = testerService.insertTestGrp(testGrpVo);

                if(testGrpCd > 0) {
                    treeList.add(new TreeNode(testGrpCd + "", defaultGrpNm, "#", new TreeNodeState()));
                }

                return treeList;
            }


            // 그룹 리스트를 가져온다.
            List<TestGrpVo> testGrpList = testerService.getTestGrpList(userVo.getUserId());

            // 생성되어 있는 그룹이 있을 경우 대상자 리스트를 가져온다.
            if(testGrpList != null) {
                for(TestGrpVo tree : testGrpList) {
                    String grpCd= Integer.toString(tree.getTestGrpCd());
                    String grpNm= tree.getTestGrpNm();
                    String superGrpcd= Integer.toString(tree.getTestSupragrpCd());
                    superGrpcd = superGrpcd.equals("-1")?"#":superGrpcd;
                    treeList.add(new TreeNode(grpCd, grpNm, superGrpcd, new TreeNodeState()));
                }
            }

            return treeList;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [캠페인>캠페인 등록>2단계] 캠페인 테스트발송(팝업)
     * - [이케어>이케어 등록>2단계] 이케어 테스트발송(팝업)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/get_test_group.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ModelAndView getTestGrouop(@RequestParam(defaultValue="0") int no, @RequestParam(defaultValue="0") int testGrpCd, @RequestParam(defaultValue="") String serviceType,
                             @RequestParam(defaultValue="") String channel, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            // 그룹 리스트를 가져온다.
            List<TestGrpVo> testGrpList = testerService.getTestGrpList(userVo.getUserId());

            TestGrpVo testGrpVo = null;
            List<TesterPoolVo> testerPoolList = null;

            // 생성되어 있는 그룹이 있을 경우 대상자 리스트를 가져온다.
            if(testGrpList != null) {
                // 현재 선택되어 있는 그룹이 없을 경우(grpCd 값 0) 최상위 그룹에 소속되어 있는 대상자들을 보여준다.
                TesterPoolVo testerPoolVo = new TesterPoolVo();
                testerPoolVo.setUserId(userVo.getUserId());
                testerPoolVo.setTestGrpCd(testGrpCd);
                testerPoolVo.setServiceNo(no);
                testerPoolVo.setServiceType(serviceType);
                testerPoolList = testerService.getTesterList(testerPoolVo);

                // 선택한 그룹의 정보를 가져온다. 현재 선택되어 있는 그룹이 없을 경우(grpCd 값 0) 최상위 그룹의 정보를 보여준다.
                testGrpVo = testerService.getTestGrpInfo(testGrpCd, userVo.getUserId());
                if(testGrpVo==null) {
                    testGrpVo = new TestGrpVo();
                }
            }
            ModelAndView mav = new ModelAndView();
            mav.addObject("testGrpVo", testGrpVo);
            mav.addObject("testerPoolList", testerPoolList);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [테스트발송(팝업)] 그룹 생성
     *
     * @param param 트리 요청파라미터
     * @return 처리결과 DTO
     */
    @RequestMapping(value = "/common/insertTestGrp.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertTestGrp(TreeParam param) {
        try {
            TestGrpVo testGrpVo = new TestGrpVo();
            testGrpVo.setTestGrpNm(param.getNodeName());
            testGrpVo.setTestSupragrpCd(Integer.parseInt(param.getParentId()));
            testGrpVo.setUserId(getLoginId());
            int testGrpCd = testerService.insertTestGrp(testGrpVo);
            if(testGrpCd > 0) {
                ResultDto resultDto = new ResultDto(ResultDto.OK);
                resultDto.setValue(testGrpCd + "");
                return resultDto;
            }
        } catch (Exception e) {
            log.error(null, e);
        }
        return new ResultDto(ResultDto.FAIL);
    }

    /**
     * [테스트발송(팝업)] 이름 변경
     *
     * @param param 트리 요청파라미터
     * @return 처리결과 DTO
     */
    @RequestMapping(value = "/common/updateTestGrp.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateTestGrp(TreeParam param) {
        try {
            TestGrpVo testGrpVo = new TestGrpVo();
            testGrpVo.setTestGrpNm(param.getNodeName());
            testGrpVo.setTestGrpCd(Integer.parseInt(param.getNodeId()));
            int row = testerService.updateTestGrp(testGrpVo);
            if(row > 0) {
                return new ResultDto(ResultDto.OK);
            }
        } catch (Exception e) {
            log.error(null, e);
        }
        return new ResultDto(ResultDto.FAIL);
    }

    /**
     * [테스트발송(팝업)] 그룹 삭제
     *
     * @param param 트리 요청파라미터
     * @return 처리결과 DTO
     */
    @RequestMapping(value = "/common/deleteTestGrp.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteTestGrp(TreeParam param) {
        try {
            int row = testerService.updateTestGrpActiveYn(Integer.parseInt(param.getNodeId()));
            if(row == 1) {
                return new ResultDto(ResultDto.OK);
            }
        } catch (Exception e) {
            log.error(null, e);
        }
        return new ResultDto(ResultDto.FAIL);
    }

    /**
     * [테스트발송(팝업)] 주소록 테스터 추가
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/insertTester.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView testerInsert(TesterPoolVo testerPoolVo, @RequestParam(defaultValue="0") int no, @RequestParam(defaultValue="") String serviceType, HttpServletRequest request) throws Exception {
        try {
            if(super.webExecMode.equals(Const.WISEMOKA)) {
                if(StringUtil.isBlank(testerPoolVo.getTestReceiverEmail())) {
                    // wiseMOKA에서는 테스터 주소록에 이메일이 필수가 아니지만
                    // NVTESTUSERPOOL.TESTRECEIVER_EMAIL 컬럼에 null을 설정할 수 없어 수정
                    testerPoolVo.setTestReceiverEmail(" ");
                }
            }

            String loginId = getLoginId();
            testerPoolVo.setUserId(loginId);

            testerService.insertTester(testerPoolVo);

            ModelAndView mav = new ModelAndView(new RedirectView("/common/testSendPopup.do"));  // /common/testSend_popup.do
            mav.addObject("no", no);
            mav.addObject("message", "save");
            mav.addObject("userId", loginId);
            mav.addObject("serviceType", serviceType);
            mav.addObject("testGrpCd", testerPoolVo.getTestGrpCd());
            mav.addObject("webExecMode", super.webExecMode);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [테스트발송(팝업)] 주소록 테스터 수정
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/updateTester.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView testerUpdate(TesterPoolVo testerPoolVo, @RequestParam(defaultValue="0") int no, @RequestParam(defaultValue="") String serviceType, HttpServletRequest request) throws Exception {
        try {
            String loginId = getLoginId();
            testerPoolVo.setUserId(loginId);

            testerService.updateTester(testerPoolVo);

            ModelAndView mav = new ModelAndView(new RedirectView("/common/testSendPopup.do"));  // /common/testSend_popup.do
            mav.addObject("no", no);
            mav.addObject("message", "save");
            mav.addObject("userId", loginId);
            mav.addObject("serviceType", serviceType);
            mav.addObject("testGrpCd", testerPoolVo.getTestGrpCd());
            mav.addObject("webExecMode", super.webExecMode);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [테스트발송(팝업)] 주소록 테스터 삭제
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/deleteTester.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView testerDelete(@RequestParam(defaultValue="0") int no, @RequestParam(defaultValue="") String serviceType,
                                     @RequestParam(defaultValue="0") int testGrpCd, int[] checkSeqNo, HttpServletRequest request) throws Exception {
        try {
            String loginId = getLoginId();
            testerService.deleteTester(loginId, checkSeqNo);

            ModelAndView mav = new ModelAndView(new RedirectView("/common/testSendPopup.do"));  // /common/testSend_popup.do
            mav.addObject("no", no);
            mav.addObject("message", "del");
            mav.addObject("userId", loginId);
            mav.addObject("serviceType", serviceType);
            mav.addObject("testGrpCd", testGrpCd);
            mav.addObject("webExecMode", super.webExecMode);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [테스트발송(팝업)] 주소록 파일 올리기
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/common/uploadTester.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView testerFileUpload(HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView(new RedirectView("/common/testSendPopup.do"));  // /common/testSend_popup.do
            MultipartHttpServletRequest multipartRequest = null;

            try {
                multipartRequest = customMultipartResolver.resolveMultipart(request);
            } catch(MaxUploadSizeExceededException e) {
                log.warn("업로드 파일 사이즈 초과!");
                mav.addObject("message", "uploadSizeFail");
                return mav;
            }

            int no = ServletRequestUtils.getIntParameter(multipartRequest, "no", 0);
            int testGrpCd = ServletRequestUtils.getIntParameter(multipartRequest, "testGrpCd", 0);
            String serviceType = StringUtil.defaultString(multipartRequest.getParameter("serviceType"));
            String loginId = getLoginId();

            mav.addObject("no", no);
            mav.addObject("userId", loginId);
            mav.addObject("serviceType", serviceType);
            mav.addObject("testGrpCd", testGrpCd);
            mav.addObject("webExecMode", super.webExecMode);

            MultipartFile multipartFile = multipartRequest.getFile("fileUpload");

            if(multipartFile.getSize() == 0) {
                log.warn("파일 사이즈가 0입니다.");
                mav.addObject("message", "uploadSizeZero");
                return mav;
            }

            int result = testerService.insertFileUploadTester(multipartFile.getInputStream(), testGrpCd, loginId);

            if(result != 0) {
                mav.addObject("message", "save");
            } else {
                mav.addObject("message", "uploadFail");
            }

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/common/insertTestUser.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertTestUser(String checkSeqNo, int no, String serviceType, String userId) {
        try {
            testerService.insertTestUser(checkSeqNo, no, serviceType, userId);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value="/common/insertSendTestUser.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertSendTestUser(String checkSeqNo, int no, String serviceType, String grpCd, String userId) {
        try {
            testerService.insertSendTestUser(checkSeqNo, no, serviceType, grpCd, userId);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
