package com.mnwise.wiseu.web.report.service.campaign;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 캠페인 공통 서비스
 */
public class CampaignCommonServiceTest extends BaseDaoTestCase {

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/editor/editor-applicationContext.xml", "file:**/conf/report/report-applicationContext.xml"
        };
    }

    private CampaignCommonService campaignCommonService;

    public CampaignCommonServiceTest() {
        super();
    }

//    public CampaignCommonServiceTest(String name) {
//        super(name);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new CampaignCommonServiceTest("testGetTemplateList"));
//        suite.addTest(new CampaignCommonServiceTest("testGetTemplate"));
//        suite.addTest(new CampaignCommonServiceTest("testMakeCsvReceiptList"));
//        return suite;
//    }

    /**
     * 캠페인별 템플릿 목록 조회
     *
     * @throws Exception
     */
    @Test
    public void testGetTemplateList() throws Exception {
        int campaignNo = 7;

        List<TemplateVo> list = campaignCommonService.getTemplateList(campaignNo);

        assertNotNull(list);
    }

    /**
     * 캠페인 템플릿 조회
     *
     * @throws Exception
     */
    @Test
    public void testGetTemplate() throws Exception {
        TemplateVo templateVo = new TemplateVo();

        // 기본 템플릿 조회
        templateVo.setCampaignNo(1);
        templateVo.setSeg(" ");

        TemplateVo tmp = campaignCommonService.getTemplate(templateVo);
        assertEquals("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", tmp.getSeg(), " ");
        assertNotNull("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", tmp.getTemplate());

        // 추가 템플릿 조회
        templateVo.setCampaignNo(1);
        templateVo.setSeg(" ");

        tmp = campaignCommonService.getTemplate(templateVo);
        try {
            assertEquals("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", tmp.getSeg(), "test");
        } catch(Exception e) {
            assertNull("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", tmp);
        }
    }

    /**
     * 오픈 고객 리스트 조회
     *
     * @throws Exception
     */
    @Test
    public void testMakeCsvReceiptList() throws Exception {
        int campaignNo = 6;
        campaignCommonService.makeCsvReceiptList(new int[] {
            campaignNo
        });

        // assertNotNull("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", list);
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setCampaignCommonService(CampaignCommonService campaignCommonService) {
        this.campaignCommonService = campaignCommonService;
    }

}
