package com.mnwise.migration;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.util.DBConnectionUtil;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 세그먼트 대상 sql 조회 테스트
 */
public class SegmentSqlExecuteTest extends BaseDaoTestCase {
    private static final Logger log = LoggerFactory.getLogger(SegmentSqlExecuteTest.class);

    private DbInfoDao dbInfoDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml", "file:**/conf/env/**-applicationContext.xml"
        };
    }

    public SegmentSqlExecuteTest() {
        super();
    }

//    public SegmentSqlExecuteTest(String name) {
//        super(name);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new SegmentSqlExecuteTest("testGetUserGrpInfo"));
//        return suite;
//    }

    /**
     * 이관 대상 전체 세그먼트 sql 실행 테스트
     *
     * @throws Exception
     */
    @Test
    public void testSegmentAllSqlExecute() throws Exception {
        List dbList = dbInfoDao.selectDataBaseList();

        DbInfoVo dbInfoVo = null;
        Map<String, Object> map = new HashMap<>();

        // sql을 일괄 실행시키 위해 디비 커넥션 생성
        for(int i = 0; i < dbList.size(); i++) {
            dbInfoVo = (DbInfoVo) dbList.get(i);
            DBConnectionUtil dbUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());
            try {
                Connection conn = dbUtil.getConnection();
                if(log.isDebugEnabled()) {
                    log.debug("JDBC Connection");
                    log.debug(String.format("Driver : %s", dbInfoVo.getDriverNm()));
                    log.debug(String.format("DSN    : %s", dbInfoVo.getDriverDsn()));
                    log.debug(String.format("UserID : %s", dbInfoVo.getDbUserId()));
                    log.debug(String.format("Pass   : %s", dbInfoVo.getDbPassword()));
                }

                map.put("" + dbInfoVo.getDbInfoSeq(), conn);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setDbInfoDao(DbInfoDao dbInfoDao) {
        this.dbInfoDao = dbInfoDao;
    }
}
