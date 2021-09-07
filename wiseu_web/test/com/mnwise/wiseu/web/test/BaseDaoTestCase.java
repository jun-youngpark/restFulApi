package com.mnwise.wiseu.web.test;

import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Dao/Service 단위 테스트 작성을 위한 기본 클래스 상속받는 클래스들은 각 업무의 **-applicationContext.xml을 설정하여 사용한다.
 */
public class BaseDaoTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml", "file:**/conf/**/**-applicationContext.xml"
        };
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
//    public BaseDaoTestCase(String name) {
//        super(name);
//    }

    public BaseDaoTestCase() {
        super();
    }

}
