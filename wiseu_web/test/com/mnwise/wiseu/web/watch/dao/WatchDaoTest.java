package com.mnwise.wiseu.web.watch.dao;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;
import com.mnwise.wiseu.web.watch.model.EcareMonitorVO;

/**
 * WatchDao 단위테스트
 */
public class WatchDaoTest extends BaseDaoTestCase {
    @Autowired private EcareDao ecareDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml",
            // "file:**/conf/**/**-applicationContext.xml"};
            "file:**/conf/watch/watch-applicationContext.xml"
        };
    }

    public WatchDaoTest() {
    }

//    public WatchDaoTest(String runTest) {
//        super(runTest);
//    }

//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new WatchDaoTest("testInsertDeleteEcareMonitor"));
//        suite.addTest(new WatchDaoTest("testGetEcareMonitor"));
//        suite.addTest(new WatchDaoTest("testGetEcareMonitorStatusList"));
//        suite.addTest(new WatchDaoTest("testGetEcareMsgByEcareNo"));
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

        Map<String, String> map = new HashMap<>();
        map.put("userId", ecareMonitorVO.getUserId());
        map.put("svcId", svcId);

        int ret = ecareDao.getEcareMsgByEcareNo(map);

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
}
