package com.mnwise.wiseu.web.env.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVTOPDOMAIN 테이블 DAO 클래스
 */
@Repository
public class TopDomainDao extends BaseDao {
    /**
     * 환경설정 - 도메인 관리 도메인 리스트를 추가한다.
     *
     * @param envDomainVo
     */
    public int insertTopDomain(String domainNm) {
        return insert("TopDomain.insertTopDomain", domainNm);
    }

    public int deleteTopDomainByPk(String domainNm) {
        return delete("TopDomain.deleteTopDomainByPk", domainNm);
    }

    /**
     * 환경설정 - 도메인 관리 도메인 리스트를 삭제한다.
     *
     * @param envDomainVo
     */
    public int deleteAllDomain() {
        return delete("TopDomain.deleteAllEnvDomains");
    }

    public String selectTopDomainByPk(String domainNm) {
        return (String) selectOne("TopDomain.selectTopDomainByPk", domainNm);
    }

    /**
     * 환경설정 - 도메인 관리 도메인 리스트를 가져온다.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> selectAllTopDomainList() {
        return selectList("TopDomain.selectAllTopDomainList");
    }

}