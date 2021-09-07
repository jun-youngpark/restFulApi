package com.mnwise.wiseu.web.report.web.campaign;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.editor.service.EditorCampaignService;
import com.mnwise.wiseu.web.report.model.campaign.CampaignAbTestVo;

import gui.ava.html.image.generator.HtmlImageGenerator;

/**
 * 캠페인 A/B 발송 분석 리포트
 */
@Controller
public class CampaignAbTestSummaryController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignAbTestSummaryController.class);

    @Autowired private EditorCampaignService editorCampaignService;

    @Value("${campaign.ab.preview}")
    private String campaignAbPreviewDir;

    /**
     * 캠페인 A/B 발송 분석 리스트
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/abTestRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/ab_summary.do
    public ModelAndView list(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                             @RequestParam(defaultValue="N") String abTestType, @RequestParam(defaultValue="") String abTestCond,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            addTabMenu(request);

            CaseInsensitiveMap userInfo = campaignReportService.selectCreateUserInfo(scenarioNo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(userInfo.get("GRP_CD").toString(), userInfo.get("USER_ID").toString())) {
                return new ModelAndView("");
            }

            int realOpenLinkCnt = campaignReportService.getCampaignReportAbRealCnt(campaignNo, abTestCond);

            String choiceResult = "A";
            List<CampaignAbTestVo> campaignReportAbTestList = campaignReportService.getCampaignReportAbTestList(campaignNo, abTestCond);
            for(int i = 0, tcnt = 0; i < campaignReportAbTestList.size(); i++) {
                int temCnt = campaignReportAbTestList.get(i).getOpenCnt();

                if(temCnt > tcnt) {
                    tcnt = temCnt;
                    choiceResult = campaignReportAbTestList.get(i).getAbType();
                }
            }

            CampaignAbTestVo campaignAbTestVo = new CampaignAbTestVo();
            //null 이 아닐경우
            if(!campaignReportAbTestList.isEmpty()) {
                int arr = "A".equals(choiceResult) ? 0 : 1;

                campaignAbTestVo.setAbType(campaignReportAbTestList.get(arr).getAbType());
                campaignAbTestVo.setSendCnt(campaignReportAbTestList.get(arr).getSendCnt());
                campaignAbTestVo.setOpenCnt(campaignReportAbTestList.get(arr).getOpenCnt());
                campaignAbTestVo.setTotRate(campaignReportAbTestList.get(arr).getTotRate());

                List<TemplateVo> templist = editorCampaignService.selectEditorCampaignTemplate(campaignNo, 0);
                String reTemplate = "";
                if("T".equals(abTestType)) {
                    reTemplate = templist.get(arr).getTemplate().replaceAll("(\r\n|\r|\n|\n\r)", "<br />");
                } else {
                    reTemplate = templist.get(0).getTemplate().replaceAll("(\r\n|\r|\n|\n\r)", "<br />");
                }

                Pattern pattern = Pattern.compile("\\{\\$<br />com.mnwise.*?<br />\\$\\}<br />");
                Matcher matcher = pattern.matcher(reTemplate);
                StringBuffer sb = new StringBuffer();
                while(matcher.find()) {
                    matcher.appendReplacement(sb, "");
                }
                matcher.appendTail(sb);

                HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
                imageGenerator.loadHtml(sb.toString().replaceAll("\"", "'").replaceAll("\\\\", "/").replaceAll("<br />", ""));
                imageGenerator.setSize(new Dimension(1024, 768));

                try {
                    String path = this.campaignAbPreviewDir;
                    UserVo userVo = getLoginUserVo();
                    if(!"ko".equals(userVo.getLanguage())) {
                        path = path.replaceAll("images", "images_" + userVo.getLanguage());
                    }

                    File imgPath = new File(path);

                    if(!imgPath.isDirectory()) {
                        FileUtil.forceMkdir(new File(path));
                    }

                    File imgFile = new File(path + campaignNo + "_" + choiceResult + ".png");

                    if(!imgFile.isFile()) {
                        if("A".equals(choiceResult)) {
                            imageGenerator.saveAsImage(path + campaignNo + "_A.png"); // 저장 이미지파일 경로
                        } else {
                            imageGenerator.saveAsImage(path + campaignNo + "_B.png"); // 저장 이미지파일 경로
                        }
                    }
                } catch(Exception e) {
                    log.error(null, e);
                }
            } //end if

            ModelAndView mav = new ModelAndView("report/campaign/abTestRpt");  // report/campaign/ab_summary_view
            mav.addObject("campaignReportAbTestList", campaignReportAbTestList);
            mav.addObject("abTestType", abTestType);
            mav.addObject("campaignNo", campaignNo);
            mav.addObject("campaignAbTestVo", campaignAbTestVo);
            mav.addObject("realOpenLinkCnt", realOpenLinkCnt);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 캠페인 에러코드별 대상자 리스트 엑셀 다운로드
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    //@RequestMapping(value="/report/campaign/excelAbSummary.do", method={RequestMethod.GET, RequestMethod.POST})
    public void excel(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo, HttpServletResponse response) throws Exception {
        ServletOutputStream out = null;

        try {
            String fileName = "reject_" + DateUtil.getNowDateTime() + ".csv";

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            out = response.getOutputStream();
            campaignReportService.makeCsvCampaignReportRejectDetailList(out, scenarioNo, campaignNo);
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }
}
