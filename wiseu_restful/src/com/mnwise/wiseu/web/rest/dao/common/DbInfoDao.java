package com.mnwise.wiseu.web.rest.dao.common;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mnwise.common.security.aria.Aria;
import com.mnwise.wiseu.web.base.util.PropertyUtil;
import com.mnwise.wiseu.web.rest.dao.segement.SegmentDao;
import com.mnwise.wiseu.web.rest.model.common.DbInfo;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVDBINFO 테이블 DAO 클래스
 */
@Repository
public class DbInfoDao extends BaseDao {
    private static final Logger log = LoggerFactory.getLogger(SegmentDao.class);

    public int insertDbInfo(DbInfo dbInfo) {
        // 암호화 설정이 되어있을 경우
        if("on".equals(PropertyUtil.getProperty("cipher","off").toLowerCase())) {
            dbInfo.setDbPassword(Aria.wiseuEncrypt(dbInfo.getDbPassword()));
        }

        return insert("DbInfo.insertDbInfo", dbInfo);
    }

    public int updateDbInfoByPk(DbInfo dbInfo) {
        // 암호화 설정이 되어있을 경우
        if("on".equals(PropertyUtil.getProperty("cipher","off").toLowerCase())) {
            dbInfo.setDbPassword(Aria.wiseuEncrypt(dbInfo.getDbPassword()));
        }
        return update("DbInfo.updateDbInfoByPk", dbInfo);
    }

    public int deleteDbInfoByPk(int dbInfoSeq) {
        return delete("DbInfo.deleteDbInfoByPk", dbInfoSeq);
    }

    public DbInfo selectDbInfoByPk(int dbinfoSeq) {
        DbInfo dbInfo = (DbInfo) selectOne("DbInfo.selectDbInfoByPk", dbinfoSeq);

        // 암호화 설정이 되어있을 경우
        if("on".equals(PropertyUtil.getProperty("cipher","off").toLowerCase())) {
            dbInfo.setDbPassword(Aria.wiseuDecrypt(dbInfo.getDbPassword()));
        }

        return dbInfo;
    }

    public int selectNextDbInfoSeq() {
        return (Integer) selectOne("DbInfo.selectNextDbInfoSeq");
    }

    @SuppressWarnings("unchecked")
    public List<Integer> selectDbinfoSeq(String driverDsn, String dbUserId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("driverDsn", driverDsn);
        paramMap.put("dbUserId", dbUserId);

        return (List<Integer>) selectList("DbInfo.selectDbinfoSeq", paramMap);
    }


    /**
     * NVDBINFO 테이블에서 wiseU DB의 SEQ를 가져온다.
     *
     * @return
     */
    public int selectDbinfoSeq() throws SQLException {
        BasicDataSource basicDataSource = getDataSource().unwrap(BasicDataSource.class);

        DbInfo dbInfoVo = new DbInfo();
        String dsn = basicDataSource.getUrl();
        if(dsn != null) {
            int index = dsn.indexOf('?');
            if(index > -1) {
                dsn = dsn.substring(0, index);
            }
            dbInfoVo.setDriverDsn(dsn.replaceAll(":log4jdbc", ""));
        }
        dbInfoVo.setDbUserId(basicDataSource.getUsername());
        dbInfoVo.setDbPassword(basicDataSource.getPassword());

        List<Integer> list = selectDbinfoSeq(dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId());

        /* 로컬 디비가 없으면 무조건 dbinfo_seq 를 1 로 넣음 */
        if(list != null && list.size() > 0) {
            if(log.isDebugEnabled())
                log.debug("DbInfo_seq: " + list.get(0));
            return list.get(0);
        } else {
            return 1;
        }
    }

    @SuppressWarnings("unchecked")
    public List<DbInfo> selectDbInfoList() {
        List<DbInfo> tmp = (List<DbInfo>) selectList("DbInfo.selectDbInfoList");
        // 암호화 설정이 되어있을 경우
        if("on".equals(PropertyUtil.getProperty("cipher","off").toLowerCase())) {
            for(DbInfo dbInfo : tmp) {
                dbInfo.setDbPassword(Aria.wiseuDecrypt(dbInfo.getDbPassword()));
            }
        }

        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    @SuppressWarnings("unchecked")
    public List<DbInfo> selectDataBaseList() {
        List<DbInfo> tmp = (List<DbInfo>) selectList("DbInfo.selectDbInfoList");

        // 암호화 설정이 되어있을 경우
        if("on".equals(PropertyUtil.getProperty("cipher","off").toLowerCase())) {
            for(DbInfo dbInfo : tmp) {
                dbInfo.setDbPassword(Aria.wiseuDecrypt(dbInfo.getDbPassword()));
            }
        }
        return tmp;
    }


}