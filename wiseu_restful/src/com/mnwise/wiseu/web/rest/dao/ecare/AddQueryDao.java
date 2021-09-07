package com.mnwise.wiseu.web.rest.dao.ecare;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.ecare.AddQuery;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVADDQUERY 테이블 DAO 클래스
 */
@Repository
public class AddQueryDao extends BaseDao {
    /**
     * NAADDQUERY테이블에서 부가데이터 쿼리를 가져온다.
     *
     * @param ecareNo
     * @return
     */
    public List<AddQuery> selectAddQuery(int ecareNo) {
        return (List<AddQuery>) selectList("AddQuery.selectAddQuery", ecareNo);
    }
    /**
     * NAADDQUERY테이블에서 부가데이터 쿼리를 가져온다.
     *
     * @param addQuery
     * @return
     */
    public int insertAddQuery(AddQuery addQuery) {
        return insert("AddQuery.insertAddQuery", addQuery);
    }
    /**
     * NAADDQUERY테이블에서 부가데이터 쿼리를 가져온다.
     *
     * @param ecareNo
     * @return
     */
    public int deleteAddQuery(int ecareNo) {
        return update("AddQuery.deleteAddQuery", ecareNo);
    }
}