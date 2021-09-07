package com.mnwise.wiseu.web.report.web.campaign;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.util.DataDownloadConverter;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignCommonService;
import com.mnwise.wiseu.web.report.service.campaign.CampaignReportService;

/**
 * 캠페인 리스트 - 리스트 Controller
 */
@Controller
public class CampaignListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CampaignListController.class);

    @Autowired private CampaignCommonService campaignCommonService;
    @Autowired private CampaignReportService campaignReportService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;

    /**
     * [리포트>캠페인] 캠페인 리스트 - 리스트
     * [리포트>캠페인] 캠페인 리스트 - 세부결과 엑셀 다운로드
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/campaignRptList.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/campaign_list.do
    public ModelAndView list(CampaignReportVo campaignReportVo, @RequestParam(defaultValue="") String excel,
                             @RequestParam(defaultValue="") String searchQstartDt, @RequestParam(defaultValue="") String searchQendDt,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            campaignReportVo.setCountPerPage(userVo.getListCountPerPage());
            campaignReportVo.setUserId(userVo.getUserId());
            campaignReportVo.setGrpCd(userVo.getGrpCd());
            campaignReportVo.setUsertypeCd(userVo.getUserTypeCd());
            campaignReportVo.setLang(userVo.getLanguage());
            campaignReportVo.setEndRow(userVo.getListCountPerPage() * campaignReportVo.getNowPage());
            campaignReportVo.setSearchQstartDt(searchQstartDt);
            campaignReportVo.setSearchQendDt(searchQendDt);

            // 캠페인에서 사용하는 채널 리스트 쿼리문에서 SMS, MMS 쿼리가 분리되어있어서 T,S 제거 함.
            //campaignReportVo.setUseChannelList((campaignReportService.userChannelList().replaceAll(",+[T|S]|[T|S],+" , "")).split(","));
            campaignReportVo.setUseChannelList(new String[] {"M","F","P"});

            // 엑셀 다운로드
            if(excel.equals("y")) {
                String scenarioNm = messageSourceAccessor.getMessage("report.campaign.list.title.name");
                String sendstartDt = messageSourceAccessor.getMessage("report.campaign.list.title.senddate");
                String sendCnt = messageSourceAccessor.getMessage("report.campaign.list.title.sendcnt");
                String successCnt = messageSourceAccessor.getMessage("report.campaign.list.title.success");
                String failCnt = messageSourceAccessor.getMessage("report.campaign.list.title.fail");
                String openCnt = messageSourceAccessor.getMessage("report.campaign.list.title.open");
                String returnmailCnt = messageSourceAccessor.getMessage("report.campaign.list.title.return");

                // 출력할 필드명
                String[] fields = new String[] {
                    "campaignNo", "scenarioNm", "sendstartDt", "sendCnt", "successCnt", "successCntPer", "failCnt", "failCntPer", "openCnt", "openCntPer", "returnmailCnt", "returnmailCntPer"
                };
                // 출력할 타이틀명
                String[] titles = new String[] {
                    "No", scenarioNm, sendstartDt, sendCnt, successCnt, successCnt + "(%)", failCnt, failCnt + "(%)", openCnt, openCnt + "(%)", returnmailCnt, returnmailCnt + "(%)"
                };

                DataDownloadConverter dc = new DataDownloadConverter();
                dc.csvDownload(response, "campaign_report", fields, titles, null, campaignReportService.selectCampaignReportListForExcel(campaignReportVo));
                return null;
            }

            int rowCnt = campaignReportService.getCampaignReportListCount(campaignReportVo);
            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (rowCnt / aCountPerPage); // 몫
            int rCountPerPage = (rowCnt % aCountPerPage); // 나머지

            if(campaignReportVo.getNowPage() <= qCountPerPage) {
                campaignReportVo.setListCnt(userVo.getListCountPerPage());
            } else {
                campaignReportVo.setListCnt(rCountPerPage);
            }

            List<CampaignReportVo> campaignReportList = null;
            if(rowCnt > 0) {
                campaignReportList = campaignReportService.getCampaignReportList(campaignReportVo);
            }

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(campaignReportVo.getNowPage());
            pgBean.setTotalRowCnt(rowCnt);
            pgBean.setPageSize(campaignReportVo.getCountPerPage());
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            ModelAndView mav = new ModelAndView("report/campaign/campaignRptList");  // report/campaign/campaign_list
            mav.addObject("paging", paging);
            mav.addObject("campaignReportVo", campaignReportVo);
            mav.addObject("campaignReportList", campaignReportList);
            mav.addObject("searchQstartDt", searchQstartDt);
            mav.addObject("searchQendDt", searchQendDt);
            mav.addObject("userChannelList", super.campaignChannelUseList);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [리포트>캠페인] 캠페인 리스트 - 업데이트 아이콘 클릭 (메일,PUSH)
     *
     * @param campaignNo
     * @return
     */
    @RequestMapping(value="/report/campaign/updateCampaignList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateCampaignList(int campaignNo) {
        try {
            int row = campaignReportService.updateCampaignList(campaignNo);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(row);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [리포트>캠페인] 캠페인 리스트 - 업데이트 아이콘 클릭 (문자,FAX,친구톡)
     *
     * @param campaignNo
     * @return
     */
    @RequestMapping(value="/report/campaign/updateSmsCampaignList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateSmsCampaignList(int campaignNo, String sendDate) {
        try {
            int row = campaignReportService.updateSmsCampaignList(campaignNo, sendDate);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setRowCount(row);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [리포트>캠페인] 캠페인 리스트 - 오픈 고객 다운로드
     */
    @RequestMapping(value = "/report/campaign/open_cust_download.do", method = {RequestMethod.GET, RequestMethod.POST})
    public void list(int[] chk, HttpServletResponse response) throws Exception {
        if(chk != null) {
            String filePath = null;
            FileInputStream in = null;
            ServletOutputStream out = null;

            try {
                filePath = campaignCommonService.makeCsvReceiptList(chk);
                File file = new File(filePath);
                String fileName = "open_cust_" + DateUtil.getNowDateTime() + ".zip";

                response.setContentType("application/download");
                response.setHeader("Pragma", "public");
                response.setHeader("Cache-Control", "max-age=0");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                response.setContentLength((int) file.length());

                in = FileUtil.openInputStream(file);
                out = response.getOutputStream();

                IOUtil.copyLarge(in, out);
            } catch(Exception e) {
                log.error(null, e);
                throw e;
            } finally {
                IOUtil.closeQuietly(out);
                IOUtil.closeQuietly(in);
                FileUtil.forceDelete(filePath);
            }
        }
    }

}
