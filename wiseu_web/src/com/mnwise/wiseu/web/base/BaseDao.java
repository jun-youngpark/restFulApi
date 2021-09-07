package com.mnwise.wiseu.web.base;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mnwise.common.security.DataSecurity;

@Repository
public class BaseDao extends SqlSessionDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(BaseDao.class);

    protected DataSecurity security = DataSecurity.getInstance();

    /*@Autowired
    private SqlMapClient sqlMapClient;

    @Autowired
    private SqlMapClientTemplate sqlMapClientTemplate;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;*/


    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
      super.setSqlSessionFactory(sqlSessionFactory);
    }

    public DataSource getDataSource() {
        return getSqlSession().getConfiguration().getEnvironment().getDataSource();
    }

    protected int insert(String id) {
        return getSqlSession().insert("mapper." + id);
    }

    protected int insert(String id, Object obj) {
        return getSqlSession().insert("mapper." + id, obj);
    }

    protected int insert(String id, Map<String, Object> paramMap) {
        return getSqlSession().insert("mapper." + id, paramMap);
    }

    protected int update(String id) {
        return getSqlSession().update("mapper." + id);
    }

    protected int update(String id, Object obj) {
        return getSqlSession().update("mapper." + id, obj);
    }

    protected int update(String id, Map<String, Object> paramMap) {
        return getSqlSession().update("mapper." + id, paramMap);
    }

    protected int delete(String id) {
        return getSqlSession().delete("mapper." + id);
    }

    protected int delete(String id, Object obj) {
        return getSqlSession().delete("mapper." + id, obj);
    }

    protected int delete(String id, Map<String, Object> paramMap) {
        return getSqlSession().delete("mapper." + id, paramMap);
    }

    protected void select(String id, Object obj, ResultHandler resultHandler) {
        getSqlSession().select("mapper." + id, obj, resultHandler);
    }

    protected Object selectOne(String id) {
        return getSqlSession().selectOne("mapper." + id);
    }

    protected Object selectOne(String id, Object obj) {
        return getSqlSession().selectOne("mapper." + id, obj);
    }

    protected Object selectOne(String id, Map<String, Object> paramMap) {
        return getSqlSession().selectOne("mapper." + id, paramMap);
    }

    protected List selectList(String id) {
        return getSqlSession().selectList("mapper." + id);
    }

    protected List selectList(String id, Object obj) {
        return getSqlSession().selectList("mapper." + id, obj);
    }

    protected List selectList(String id, Map<String, Object> paramMap) {
        return getSqlSession().selectList("mapper." + id, paramMap);
    }

    protected <K, V> Map<K, V> selectMap(String id, String mapKey) {
        return getSqlSession().selectMap("mapper." + id, mapKey);
    }

    protected <K, V> Map<K, V> selectMap(String id, Object obj, String mapKey) {
        return getSqlSession().selectMap("mapper." + id, obj, mapKey);
    }

    protected CaseInsensitiveMap selectMap(String id, Object parameterObject, String keyProp, String valueProp) {
        CaseInsensitiveMap resultMap = new CaseInsensitiveMap();
        List<CaseInsensitiveMap> mapList = selectList(id, parameterObject);
        for(CaseInsensitiveMap map : mapList) {
            resultMap.put(map.get(keyProp), map.get(valueProp));
        }
        return resultMap;
    }
}
