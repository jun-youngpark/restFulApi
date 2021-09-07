package com.mnwise.wiseu.web.env.dao;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * jsonValidation DAO
 */
@Repository
public class EnvJsonValidatorDao  extends BaseDao {
    

    /**
     * 전문 조회
     * @param ecareNo
     * @return
     */
    public String getJonmun(String seq) {
        String jonmun = (String) selectOne("MakeData.getJonmun", seq);
        return jonmun;
    }
}
