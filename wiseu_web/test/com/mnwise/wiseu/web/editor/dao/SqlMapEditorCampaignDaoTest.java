package com.mnwise.wiseu.web.editor.dao;

import org.junit.Test;

import com.mnwise.wiseu.web.editor.model.CampaignEditorVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.LinkTraceVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * Campaign Editor 단위테스트
 */
public class SqlMapEditorCampaignDaoTest extends BaseDaoTestCase {

    protected String[] getConfigLocations() {
        return new String[] {
            "file:WebContent/WEB-INF/applicationContext-test.xml",
            // "file:WebContent/WEB-INF/conf/**/**-applicationContext.xml"};
            "file:WebContent/WEB-INF/conf/editor/editor-applicationContext.xml"
        };
    }

    public SqlMapEditorCampaignDaoTest() {
    }

//    public SqlMapEditorCampaignDaoTest(String runTest) {
//        super(runTest);
//    }

//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//
//        suite.addTest(new SqlMapEditorCampaignDaoTest("testUpdateEditorCampaign"));
//        suite.addTest(new SqlMapEditorCampaignDaoTest("testUpdateEditorCampaignHandler"));
//        suite.addTest(new SqlMapEditorCampaignDaoTest("testUpdateEditorCampaignTemplate"));
//        suite.addTest(new SqlMapEditorCampaignDaoTest("testDeleteEditorCampaignLinktrace"));
//        suite.addTest(new SqlMapEditorCampaignDaoTest("testUpdateEditorCampaignLinktrace"));
//
//        return suite;
//    }

    /**
     * NVCAMPAIGN 테이블에 기본정보 TEMPLATE_TYPE과 메일 제목 UPDATE 단위테스트
     */
    @Test
    public void testUpdateEditorCampaign() throws Exception {
        CampaignEditorVo campaignEditorVo = initCampaignEditorVO();

        // assertEquals(1,editorCampaignDao.updateEditorCampaign(campaignEditorVo.getTemplateType(),campaignEditorVo.getCampaignPreface(),campaignEditorVo.getNo()));
    }

    /**
     * 핸들러 저장 (NVAPPLICATION 테이블에 존재 여부를 확인해서 INSERT/UPDATE) 단위테스트
     */
    @Test
    public void testUpdateEditorCampaignHandler() throws Exception {
        HandlerVo handlerVo = initHandlerVo();

        // assertEquals(1,editorCampaignDao.updateEditorCampaignHandler(handlerVo.getNo(),
        // handlerVo.getHandler(), handlerVo.getType()));
    }

    /**
     * 템플릿 저장 NVTEMPLATE 테이블에 존재 여부를 확인해서 INSERT/UPDATE) 단위테스트
     */
    @Test
    public void testUpdateEditorCampaignTemplate() throws Exception {
        TemplateVo templateVo = initTemplateVo();
        String openclickpath = "open";
        String handlerType = "S";
        // assertEquals(1,editorCampaignDao.updateEditorCampaignTemplate(templateVo.getNo(),
        // templateVo.getSegNo(), templateVo.getTemplate(), templateVo.getSeg(),
        // openclickpath, handlerType));

    }

    /**
     * 링크추적 저장
     */
    @Test
    public void testUpdateEditorCampaignLinktrace() throws Exception {
        LinkTraceVo linktraceVo = initLinktraceVo();
        // assertEquals(1,editorCampaignDao.updateEditorCampaignLinktrace(linktraceVo.getNo(),
        // linktraceVo.getLinkSeq(),
        // linktraceVo.getLinkDesc(),
        // linktraceVo.getLinkUrl(),
        // linktraceVo.getLinkTitle()));
    }

    // =============================================================================
    // Vo setting
    // =============================================================================
    private CampaignEditorVo initCampaignEditorVO() {

        CampaignEditorVo campaignEditorVo = new CampaignEditorVo();

        int templateType = 1;
        String campaignPreface = "단위테스트";
        int no = 2;

        campaignEditorVo.setTemplateType(templateType);
        campaignEditorVo.setCampaignPreface(campaignPreface);
        campaignEditorVo.setNo(no); // 캠페인번호

        return campaignEditorVo;
    }

    private HandlerVo initHandlerVo() {
        HandlerVo handlerVo = new HandlerVo();

        int no = 1;
        String handler = "테스트입니다";
        String type = "1";

        handlerVo.setNo(no);
        handlerVo.setType(type);
        handlerVo.setHandler(handler);

        return handlerVo;
    }

    private TemplateVo initTemplateVo() {
        TemplateVo templateVo = new TemplateVo();

        int no = 1;
        String seg = "seg";
        String template = "테스트입니다";

        templateVo.setNo(no);
        templateVo.setSeg(seg);
        templateVo.setTemplate(template);

        return templateVo;
    }

    private LinkTraceVo initLinktraceVo() {
        LinkTraceVo linktraceVo = new LinkTraceVo();
        int no = 1;
        int linkSeq = 1;
        String linkDesc = "1";
        String linkUrl = "2";
        String linkTitle = "3";

        linktraceVo.setNo(no);
        linktraceVo.setLinkSeq(linkSeq);
        linktraceVo.setLinkDesc(linkDesc);
        linktraceVo.setLinkUrl(linkUrl);
        linktraceVo.setLinkTitle(linkTitle);

        return linktraceVo;
    }
}
