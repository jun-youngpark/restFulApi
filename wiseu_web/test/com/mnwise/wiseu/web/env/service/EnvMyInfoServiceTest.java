package com.mnwise.wiseu.web.env.service;

import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 테스트 발송
 */
public class EnvMyInfoServiceTest extends BaseDaoTestCase {

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/env/env-applicationContext.xml"
        };
    }

    public EnvMyInfoServiceTest() {
        super();
    }

//    public EnvMyInfoServiceTest(String name) {
//        super(name);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        // suite.addTest(new EnvMyInfoServiceTest("testGetSoeIdExist"));
//        return suite;
//    }

}
