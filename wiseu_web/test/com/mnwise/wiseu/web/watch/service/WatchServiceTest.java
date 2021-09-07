package com.mnwise.wiseu.web.watch.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mnwise.wiseu.web.test.BaseDaoTestCase;
import com.mnwise.wiseu.web.watch.model.EcareMonitorVO;

/**
 * WatchService 단위테스트
 */
public class WatchServiceTest extends BaseDaoTestCase {

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml",
            // "file:**/conf/**/**-applicationContext.xml"};
            "file:**/conf/watch/watch-applicationContext.xml"
        };
    }

    public WatchServiceTest() {
    }

//    public WatchServiceTest(String runTest) {
//        super(runTest);
//    }

//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new WatchServiceTest("testInsertDeleteEcareMonitor"));
//        suite.addTest(new WatchServiceTest("testGetEcareMonitor"));
//        suite.addTest(new WatchServiceTest("testGetEcareMonitorStatusList"));
//        suite.addTest(new WatchServiceTest("testGetEcareMsgByEcareNo"));
//
//        return suite;
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
    @Test
    public void testGetEcareMsgByEcareNo() throws Exception {
        EcareMonitorVO ecareMonitorVO = initVO();

        String svcId = "wee001";

        int ret = watchService.getEcareMsgByEcareNo(ecareMonitorVO.getUserId(), svcId);

        assertTrue("값이 0이 나올수도 있음", ret >= 0);
    }

    private EcareMonitorVO initVO() {
        String userId = "admin";
        int ecareNo = 1;

        EcareMonitorVO ecareMonitorVO = new EcareMonitorVO();
        ecareMonitorVO.setUserId(userId);
        ecareMonitorVO.setEcareNo(ecareNo);
        return ecareMonitorVO;
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    private WatchService watchService;

    public void setWatchService(WatchService watchService) {
        this.watchService = watchService;
    }
}
