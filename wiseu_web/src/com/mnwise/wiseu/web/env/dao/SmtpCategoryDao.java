package com.mnwise.wiseu.web.env.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.env.model.json.SmtpCodeTreeEelement;

/**
 * NVSMTPCATEGORY 테이블 DAO 클래스
 */
@Repository
public class SmtpCategoryDao extends BaseDao {
    /*public int insertSmtpCategory(SmtpCategory smtpCategory) {
        return insert("SmtpCategory.insertSmtpCategory", smtpCategory);
    }

    public int updateSmtpCategoryByPk(SmtpCategory smtpCategory) {
        return update("SmtpCategory.updateSmtpCategoryByPk", smtpCategory);
    }*/

    public int deleteSmtpCategoryByPk(String categoryCd) {
        return delete("SmtpCategory.deleteSmtpCategoryByPk", categoryCd);
    }

    /*public SmtpCategory selectSmtpCategoryByPk(String categoryCd) {
        return selectOne("SmtpCategory.selectSmtpCategoryByPk", categoryCd);
    }*/

    /**
     * 환경설정 - 코드관린에서 SMTP 코드 그룹 리스트를 가져온다.
     *
     * @param pcategoryCd
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SmtpCodeTreeEelement> selectSmtpCategoryList(String pcategoryCd) {
        return selectList("SmtpCategory.selectSmtpCategoryList", pcategoryCd);
    }
}