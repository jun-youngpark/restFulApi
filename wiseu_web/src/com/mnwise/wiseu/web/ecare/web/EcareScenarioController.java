package com.mnwise.wiseu.web.ecare.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.BeanUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.service.CdMstService;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.ecare.service.EcareService;

/**
 * 이케어 리스트 Controller
 */
@Controller
public class EcareScenarioController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareScenarioController.class);

    @Autowired private CdMstService cdMstService;
    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private EcareService ecareService;

    private static final String CD_ECARE_STS = "200111";
    private static final String CD_SERVICE_TYPE = "200131";

    /**
     * - [이케어>이케어 리스트] 이케어 리스트 - 리스트
     * - [이케어>이케어 리스트] 이케어 리스트 - 수행상태(선택박스)
     * - [이케어>이케어 리스트] 이케어 리스트 - 서비스타입(선택박스)
     * - [이케어>이케어 리스트] 이케어 리스트 - 검색
     * - [이케어>이케어 리스트] 이케어 리스트 - 태그
     *
     * @param tagNo
     * @param serviceType
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/ecareList.do", method={RequestMethod.GET, RequestMethod.POST})  // /ecare/ecare.do
    public ModelAndView list(@RequestParam(defaultValue="-1") int tagNo, @RequestParam(defaultValue="") String serviceType,
                             HttpServletRequest request) throws Exception {
        try {
            EcareScenarioVo ecareScenarioVo = new EcareScenarioVo();
            BeanUtil.populate(ecareScenarioVo, request.getParameterMap());
            ecareScenarioVo.setTagNo(tagNo);

            // 세션정보 담기
            UserVo userVo = getLoginUserVo();
            ecareScenarioVo.setUserVo(userVo);
            ecareScenarioVo.setCountPerPage(userVo.getListCountPerPage());
            ecareScenarioVo.setEndRow(userVo.getListCountPerPage() * ecareScenarioVo.getNowPage());

            // 정렬
            String orderColumn = StringUtil.defaultString(request.getParameter("orderColumn"), "a.scenario_no");
            String orderSort = StringUtil.defaultString(request.getParameter("orderSort"), "DESC");

            ecareScenarioVo.setOrderColumn(orderColumn);
            ecareScenarioVo.setOrderSort(orderSort);

            // 이케어 실행상태
            String ecareSts = StringUtil.defaultString(request.getParameter("ecareVo.ecareSts"), "");
            ecareScenarioVo.getEcareVo().setEcareSts(ecareSts);

            // 이케어 서비스 별 검색
            // 실시간 : serviceType=R, subType=
            // 스케쥴 : serviceType=S, subType=S
            // 준실시간(스케쥴) : serviceType=S, subType=R
            // 준실시간(DB인터페이스) : serviceType=S, subType=N
            ecareScenarioVo.setServiceType(serviceType);

            if(serviceType.startsWith("S")) {
                ecareScenarioVo.setServiceType("S");
                if(serviceType.equals("SS")) {
                    ecareScenarioVo.setSubType("S");
                } else if(serviceType.equals("SR")) {
                    ecareScenarioVo.setSubType("R");
                } else if(serviceType.equals("SN")) {
                    ecareScenarioVo.setSubType("N");
                }
            }

            // 페이징을 위한 리스트의 TOTAL ROW 를 가져온다.
            int totalRowCnt = ecareScenarioService.getEcareListTotalCount(ecareScenarioVo);
            ecareScenarioVo.setCountPerPage(userVo.getListCountPerPage());

            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (totalRowCnt / aCountPerPage); // 몫
            int rCountPerPage = (totalRowCnt % aCountPerPage); // 나머지

            if(ecareScenarioVo.getNowPage() <= qCountPerPage) {
                ecareScenarioVo.setListCnt(userVo.getListCountPerPage());
            } else {
                ecareScenarioVo.setListCnt(rCountPerPage);
            }

            // 현재 이케어 리스트가 존재할 경우 페이지에 해당하는 리스트를 가져온다.
            List<EcareScenarioVo> ecareScenarioList = null;
            if(totalRowCnt > 0) {
                ecareScenarioList = ecareScenarioService.getEcareList(ecareScenarioVo);
            }

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(ecareScenarioVo.getNowPage());
            pgBean.setTotalRowCnt(totalRowCnt);
            pgBean.setPageSize(ecareScenarioVo.getCountPerPage());
            // 이케어 삭제 action 이후에 아래 파라미터를 제거하지 않으면 페이징 할때마다 alert문구가 생김
            pgBean.setNotUseParameter("deleteCount=");
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            ModelAndView mav = new ModelAndView("/ecare/ecareList");  // /ecare/ecare_list
            // nv_cd_mst 에서 코드명을 가져오기 위해 language를 전달한다.
            mav.addObject("ecareStsList", cdMstService.getCdMstList(CD_ECARE_STS, userVo.getLanguage()));
            mav.addObject("ecareServiceTypeList", cdMstService.getCdMstList(CD_SERVICE_TYPE, userVo.getLanguage()));
            mav.addObject("ecareScenarioList", ecareScenarioList);
            mav.addObject("ecareScenarioVo", ecareScenarioVo);
            mav.addObject("orderColumn", orderColumn);
            mav.addObject("orderSort", orderSort);
            mav.addObject("rowCnt", totalRowCnt);
            mav.addObject("paging", paging);
            mav.addObject("nowPage", ecareScenarioVo.getNowPage());
            mav.addObject("webExecMode", super.webExecMode);

            // 이케어 등록 메뉴의 실행 권한을 저장
            Map<String, Map<String, String>> roleMap = getLoginRoleMap();
            mav.addObject("execPerm", roleMap.get("/ecare/ecare1Step.do") != null ? roleMap.get("/ecare/ecare1Step.do").get("execute") : null);  // /ecare/ecare_step_form.do

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>이케어 리스트] 삭제 버튼 클릭
     *
     * @param ecareNo
     * @param nowPage
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/deleteEcare.do", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteEcare(int ecareNo, int nowPage, HttpServletRequest request) throws Exception {
        try {
            EcareScenarioVo scenarioVo = new EcareScenarioVo();
            BeanUtil.populate(scenarioVo, request.getParameterMap());

            scenarioVo.getEcareVo().setEcareNo(ecareNo);
            ecareScenarioService.setDeleteEcare(scenarioVo);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("searchWord", scenarioVo.getSearchWord());
            returnData.put("searchColumn", scenarioVo.getSearchColumn());
            returnData.put("nowPage", nowPage);
            return new ResultDto(ResultDto.OK);
            }catch(Exception e) {
                log.error(null, e);
                return new ResultDto(ResultDto.FAIL, e.getMessage());
            }
    }

    /**
     * [이케어>이케어 리스트] 상태변경 버튼 클릭
     *
     * @param ecareNo
     * @param nowPage
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/changeEcareSts.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView changeEcareSts(int ecareNo, int nowPage) throws Exception {
        try {
            ecareScenarioService.updateEcareStsChange(ecareNo);

            ModelAndView mav = new ModelAndView(new RedirectView("/ecare/ecareList.do"));
            mav.addObject("nowPage", nowPage);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>이케어 리스트] 서비스 점검 콤보박스 선택 클릭<br/>
     * - '실행' 상태인 전체 이케어를 '점검' 상태로 변경하거나 그 반대 설정을 진행<br/>
     * - 관리자 권한을 가진 계정만 실행할 수 있음
     *
     * @param serviceCheckYn
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/checkService.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView checkService(String serviceCheckYn) throws Exception {
        try {
            if("A".equals(getLoginUserVo().getUserTypeCd())) {
                ecareService.changeAllEcareStatus(serviceCheckYn);
            }

            return new ModelAndView(new RedirectView("/ecare/ecareList.do"));  // /ecare/ecare.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>이케어 리스트] 핸들러/템플릿 다운로드<br/>
     * contentType 파라미터 값에 따라 핸들러 또는 템플릿을 zip 파일로 다운로드 한다.<br/>
     * 해당 기능은 관리자 계정만 실행할 수 있음.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/downloadContents.do", method={RequestMethod.GET, RequestMethod.POST})
    public void downloadContents(String contentType, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if("A".equals(getLoginUserVo().getUserTypeCd())) {
                response.setContentType("application/download");
                response.setHeader("Pragma", "public");
                response.setHeader("Cache-Control", "max-age=0");

                String path = System.getProperty("wiseu.home") + "/upload/download/" + DateUtil.getNowDateTime("yyyyMMddHHmmssSSS");
                OutputStream out = null;
                FileInputStream in = null;
                try {
                    File zipFile = ecareService.makeZipFile(contentType, path);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFile.getName() + "\";");

                    in = new FileInputStream(zipFile);
                    out = response.getOutputStream();
                    IOUtil.copyLarge(in, out);
                } catch(Exception e) {
                    throw e;
                } finally {
                    IOUtil.closeQuietly(in);
                    IOUtil.closeQuietly(out);

                    FileUtil.forceDelete(path);
                }
            }
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
