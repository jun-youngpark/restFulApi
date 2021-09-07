package com.mnwise.wiseu.web.ecare.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.AddQueryVo;

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
    public List<AddQueryVo> selectAddQuery(int ecareNo) {
        return selectList("AddQuery.selectAddQuery", ecareNo);
    }
    /**
     * NAADDQUERY테이블에서 부가데이터 쿼리를 가져온다.
     *
     * @param addQueryVo
     * @return
     */
    public int insertAddQuery(AddQueryVo addQueryVo) {
        return insert("AddQuery.insertAddQuery", addQueryVo);
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