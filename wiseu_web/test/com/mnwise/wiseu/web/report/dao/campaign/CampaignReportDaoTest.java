package com.mnwise.wiseu.web.report.dao.campaign;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mnwise.wiseu.web.campaign.dao.DomainLogDao;
import com.mnwise.wiseu.web.campaign.dao.LinkTraceDao;
import com.mnwise.wiseu.web.campaign.dao.ReceiptDao;
import com.mnwise.wiseu.web.campaign.dao.RejectDao;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportLinkClickVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 캠페인 리포트 Dao 단위 테스트
 */
public class CampaignReportDaoTest extends BaseDaoTestCase {
    private DomainLogDao domainLogDao;
    private LinkTraceDao linkTraceDao;
    private ReceiptDao receiptDao;
    private RejectDao rejectDao;
    private SendResultDao sendResultDao;
    private TemplateDao templateDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml", "file:**/conf/**/**-applicationContext.xml"
            // ,"file:**/conf/report/report-applicationContext.xml"
        };
    }

    public CampaignReportDaoTest() {
    }

//    public CampaignReportDaoTest(String runTest) {
//        super(runTest);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new CampaignReportDaoTest("testUpdateCampaignList"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignReportListCount"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignReportList"));
//        suite.addTest(new
//        CampaignReportDaoTest("testSelectCampaignReportListForExcel"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignReportBasicInfo"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignSummaryAllList"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignSummaryReceiveList"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignReceiveExcel"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignErrorReportList"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetSpamBlockReportList"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignReportLinkClickList"));
//        suite.addTest(new
//        CampaignReportDaoTest("testGetCampaignReportLinkClickTotal"));
//        suite.addTest(new CampaignReportDaoTest("testGetCampaignReportRejectList"));
//        suite.addTest(new CampaignReportDaoTest("testGetCampaignSummaryDetail"));
//        suite.addTest(new CampaignReportDaoTest("testGetCampaignSummaryReceive"));
//        suite.addTest(new CampaignReportDaoTest("testGetCampaignSummaryReturn"));
//        suite.addTest(new CampaignReportDaoTest("testGetCampaignSummaryReject"));
//        suite.addTest(new CampaignReportDaoTest("testGetCampaignSummarySpam"));
//        suite.addTest(new CampaignReportDaoTest("testGetTemplateList"));
//        suite.addTest(new CampaignReportDaoTest("testGetCampaignReportAllCntInfo"));
//        return suite;
//    }

    public CampaignReportVo getCampaignReportVo() {
        String usertypeCd = "A"; // U : 유저 M : 매니저 A : 관리자
        String grpCd = "01";
        String userId = "admin";
        String searchQstartDt = "20100101";
        String searchQendDt = "20100205";
        String searchWord = "TEST";
        int nowPage = 1;
        int countPerPage = 10;

        CampaignReportVo campaignReportVo = new CampaignReportVo();
        campaignReportVo.setUsertypeCd(usertypeCd);
        campaignReportVo.setGrpCd(grpCd);
        campaignReportVo.setUserId(userId);
        campaignReportVo.setSearchQstartDt(searchQstartDt);
        campaignReportVo.setSearchQendDt(searchQendDt);
        campaignReportVo.setSearchWord(searchWord);
        campaignReportVo.setNowPage(nowPage);
        campaignReportVo.setCountPerPage(countPerPage);

        return campaignReportVo;
    }

    /**
     * 캠페인 리포트 리스트 카운트
     *
     * @param campaignReportVo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignReportListCount() throws Exception {
        CampaignReportVo campaignReportVo = this.getCampaignReportVo();

        assertTrue(sendResultDao.getCampaignReportListCount(campaignReportVo) >= 0);
    }

    /**
     * 캠페인 리포트 리스트
     *
     * @param campaignReportVo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignReportList() throws Exception {
        CampaignReportVo campaignReportVo = this.getCampaignReportVo();
        assertTrue(sendResultDao.getCampaignReportList(campaignReportVo) != null);
    }

    /**
     * 캠페인 리포트 리스트 엑셀 다운로드
     *
     * @param scenarioInfoVo
     * @return
     * @throws Exception
     */
    @Test
    public void testSelectCampaignReportListForExcel() throws Exception {
        CampaignReportVo campaignReportVo = this.getCampaignReportVo();
        assertTrue(sendResultDao.selectCampaignReportListForExcel(campaignReportVo) != null);
    }

    /**
     * 캠페인 리스트 업데이트
     *
     * @param campaignNo
     * @return
     * @throws Exception
     */
    @Test
    public void testUpdateCampaignList() throws Exception {
        int campaignNo = 18;
        assertTrue(sendResultDao.updateCampaignList(campaignNo) >= 0);
    }

    /**
     * 캠페인 리포트 기본정보 가져오기
     *
     * @param campaignNo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignReportBasicInfo() throws Exception {
        int campaignNo = 18;

        assertTrue(sendResultDao.getCampaignReportBasicInfo(campaignNo) != null);
    }

    /**
     * 캠페인 리포트 전체보기 요약분석 발송결과 리스트
     *
     * @return
     * @throws Exception
     *//* public void testGetCampaignSummaryAllList() throws Exception {
        * int scenarioNo = 18;
        * String lang = "ko";
        * assertTrue(campaignReportDao.getCampaignSummaryAllList(map) != null);
        * } */

    /**
     * 캠페인 리포트 요약분석 발송결과
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignSummaryDetail() throws Exception {
        int campaignNo = 52;
        int scenarioNo = 36;

        Map<String, Object> map = new HashMap<>();
        map.put("campaignNo", campaignNo);
        map.put("scenarioNo", scenarioNo);

        assertTrue(sendResultDao.getCampaignSummaryDetail(map) != null);
    }

    /**
     * 캠페인 리포트 전체보기 요약분석 수신확인 결과 리스트
     *
     * @param scenarioNo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignSummaryReceiveList() throws Exception {
        int campaignNo = 46;
        String lang = "ko";
        assertTrue(sendResultDao.getCampaignSummaryReceiveList(campaignNo, lang) != null);
    }

    /**
     * 캠페인 링크클릭 리포트
     *
     * @throws Exception
     */
    @Test
    public void testGetCampaignReportLinkClickList() throws Exception {
        int scenarioNo = 1;
        int campaignNo = 1;

        List list = linkTraceDao.getCampaignReportLinkClickList(scenarioNo, campaignNo);
        assertNotNull(list);
    }

    /**
     * 캠페인 링크클릭 리포트 총합계
     *
     * @throws Exception
     */
    @Test
    public void testGetCampaignReportLinkClickTotal() throws Exception {
        int scenarioNo = 1;
        int campaignNo = 1;

        CampaignReportLinkClickVo campaignReportLinkClickVo = linkTraceDao.getCampaignReportLinkClickTotal(scenarioNo, campaignNo);
        assertNotNull(campaignReportLinkClickVo != null);
    }

    /**
     * 캠페인 수신거부 리포트
     *
     * @throws Exception
     */
    @Test
    public void testGetCampaignReportRejectList() throws Exception {
        int scenarioNo = 1;
        int campaignNo = 1;

        List list = rejectDao.getCampaignReportRejectList(scenarioNo, campaignNo);
        assertNotNull(list);
    }

    /**
     * 캠페인 요약 분석 수신확인 정보
     *
     * @param campaignNo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignSummaryReceive() throws Exception {
        int campaignNo = 100;

        assertTrue(receiptDao.getCampaignSummaryReceive(campaignNo) != null);
    }

    /**
     * 캠페인 요약 분석 리턴메일
     *
     * @param campaignNo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignSummaryReturn() throws Exception {
        int campaignNo = 100;
        assertTrue(sendResultDao.getCampaignSummaryReturn(campaignNo) >= 0);
    }

    /**
     * 캠페인 요약 분석 수신거부
     *
     * @param campaignNo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignSummaryReject() throws Exception {
        int campaignNo = 100;
        assertTrue(rejectDao.getCampaignSummaryReject(campaignNo) >= 0);

    }

    /**
     * 캠페인 요약 분석 스팸차단
     *
     * @param campaignNo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetCampaignSummarySpam() throws Exception {
        int campaignNo = 100;
        String lang = "ko";

        assertTrue(domainLogDao.getCampaignSummarySpam(campaignNo, lang) >= 0);

    }

    /**
     * 캠페인 리포트 도메인 분석
     *
     * @throws Exception
     */
    @Test
    public void testGetCampaignReportDomainList() throws Exception {
        int scenarioNo = 1;
        int campaignNo = 1;
        String lang = "ko";

        List<CampaignReportVo> list = sendResultDao.getCampaignReportDomainList(scenarioNo, campaignNo, lang);
        assertNotNull(list);
    }

    /**
     * 캠페인별 템플릿 목록 조회
     *
     * @param campaignNo 캠페인 번호
     * @return
     * @throws Exception
     */
    @Test
    public void testGetTemplateList() throws Exception {
        int campaignNo = 1;

        assertTrue(templateDao.getTemplateList(campaignNo) != null);
    }

    /**
     * 캠페인 전체요약 총 발송 정보 가져오기 - 그래프에서 데이터를 이용하기 위해 사용
     *
     * @param scenarioNo
     * @return
     * @throws Exception
     *//* public void testGetCampaignReportAllCntInfo() throws Exception {
        * int scenarioNo = 36;
        * assertTrue(campaignReportDao.getCampaignReportAllCntInfo(scenarioNo) != null);
        * } */
}
