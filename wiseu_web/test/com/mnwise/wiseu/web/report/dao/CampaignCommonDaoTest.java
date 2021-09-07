package com.mnwise.wiseu.web.report.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 캠페인 공통 Dao
 */
public class CampaignCommonDaoTest extends BaseDaoTestCase {
    private TemplateDao templateDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/editor/editor-applicationContext.xml", "file:**/conf/report/report-applicationContext.xml"
        };
    }

    public CampaignCommonDaoTest() {
    }

//    public CampaignCommonDaoTest(String runTest) {
//        super(runTest);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new CampaignCommonDaoTest("testGetTemplateList"));
//        suite.addTest(new CampaignCommonDaoTest("testGetTemplate"));
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
        List<TemplateVo> list = templateDao.getTemplateList(campaignNo);
        assertNotNull("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", list);
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

        TemplateVo tmp = templateDao.getTemplate(templateVo);
        assertEquals("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", tmp.getSeg(), " ");
        assertNotNull("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", tmp.getTemplate());

        // 추가 템플릿 조회
        templateVo.setCampaignNo(1);
        templateVo.setSeg(" ");

        tmp = templateDao.getTemplate(templateVo);

        if(tmp != null) {
            assertEquals("해당하는 캠페인 번호는 사전에 등록되어 있어야 함", tmp.getSeg(), " ");
        }

    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }
}
