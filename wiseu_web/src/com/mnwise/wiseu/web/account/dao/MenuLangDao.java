package com.mnwise.wiseu.web.account.dao;

import java.util.List;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVMENU_LANG 테이블 DAO 클래스
 */
@Repository
public class MenuLangDao extends BaseDao {

    /**
     * 현재 사용중인 언어 조회
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getLangList() {
        return selectList("MenuLang.getLangList");
    }


    /**
     * 해당 언어로 저장된 메뉴명 조회
     *
     * @param language
     * @return
     */
    public CaseInsensitiveMap selectMenuNames(String language) {
        return selectMap("MenuLang.selectMenuNames", language, "menu_cd", "menu_nm");
    }
}