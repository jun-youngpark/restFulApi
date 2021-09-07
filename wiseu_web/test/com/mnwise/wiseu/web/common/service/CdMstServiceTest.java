package com.mnwise.wiseu.web.common.service;

import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 공통 코드 마스터 서비스 테스트
 */
public class CdMstServiceTest extends BaseDaoTestCase {

    private CdMstService cdMstService;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/common/common-applicationContext.xml"
        };
    }

    public CdMstServiceTest() {
        super();
    }

//    public CdMstServiceTest(String name) {
//        super(name);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new CdMstServiceTest("testGetCdMstList"));
//        return suite;
//    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setCdMstService(CdMstService cdMstService) {
        this.cdMstService = cdMstService;
    }

}
