package com.mnwise.wiseu.web.common.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 공통 코드 마스터 Dao 테스트
 */
public class CdMstDaoTest extends BaseDaoTestCase {
    @Autowired
    private CdMstDao cdMstDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/common/common-applicationContext.xml"
        };
    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
}
