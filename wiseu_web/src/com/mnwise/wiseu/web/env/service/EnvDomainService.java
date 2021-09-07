package com.mnwise.wiseu.web.env.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.env.dao.TopDomainDao;

@Service
public class EnvDomainService extends BaseService {
    @Autowired private TopDomainDao topDomainDao;

    /**
     * 환경설정 - 도메인 관리 도메인 리스트를 가져온다.
     *
     * @return
     */
    public List<String> getDomainList() {
        return topDomainDao.selectAllTopDomainList();
    }

    /**
     * 환경설정 - 도메인 관리 도메인 리스트를 삭제한다.
     *
     * @param envDomainVo
     */
    public void deleteAllDomain() {
        topDomainDao.deleteAllDomain();
    }

    /**
     * 환경설정 - 도메인 관리 도메인 리스트를 추가한다.
     *
     * @param envDomainVo
     */
    public void insertTopDomain(String domainNm) {
        topDomainDao.insertTopDomain(domainNm);
    }
}
