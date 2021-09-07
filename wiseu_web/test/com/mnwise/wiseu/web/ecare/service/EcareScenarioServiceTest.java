package com.mnwise.wiseu.web.ecare.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 이케어 리스트 테스트
 */
public class EcareScenarioServiceTest extends BaseDaoTestCase {

    private EcareScenarioService ecareScenarioService;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml", "file:**/conf/**/**-applicationContext.xml"
            // ,"file:**/conf/ecare/ecare-applicationContext.xml"
        };
    }

    public EcareScenarioServiceTest() {
        super();
    }

//    public EcareScenarioServiceTest(String name) {
//        super(name);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new EcareScenarioServiceTest("testGetEcareListTotalCount"));
//        suite.addTest(new EcareScenarioServiceTest("testGetEcareList"));
//        suite.addTest(new EcareScenarioServiceTest("testUpdateEcareStsChange"));
//        suite.addTest(new EcareScenarioServiceTest("testUpdateChangeAllStatus"));
//        return suite;
//    }

    /**
     * 이케어 리스트 총 카운트
     *
     * @param ecareScenarioVo
     * @return
     * @throws Exception
     */
    @Test
    public void testGetEcareListTotalCount() throws Exception {
        EcareScenarioVo ecareScenarioVo = new EcareScenarioVo();
        ecareScenarioVo.setTagNo(1);

        assertTrue(ecareScenarioService.getEcareListTotalCount(ecareScenarioVo) >= 0);
    }

    /**
     * 이케어 리스트
     *
     * @param ecareScenarioVo
     * @return
     */
    @Test
    public void testGetEcareList() throws Exception {
        EcareScenarioVo ecareScenarioVo = new EcareScenarioVo();
        ecareScenarioVo.setTagNo(1);

        assertTrue(ecareScenarioService.getEcareList(ecareScenarioVo) != null);
    }

    /**
     * 이케어 상태변경
     *
     * @param ecareNo
     * @return
     */
    @Test
    public void testUpdateEcareStsChange() throws Exception {
        int ecareNo = 300;

        assertTrue(ecareScenarioService.updateEcareStsChange(ecareNo) >= 0);
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setEcareScenarioService(EcareScenarioService ecareScenarioService) {
        this.ecareScenarioService = ecareScenarioService;
    }

}
