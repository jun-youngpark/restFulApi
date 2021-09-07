package com.mnwise.wiseu.web.env.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.env.dao.EnvJsonValidatorDao;

@Service
public class EnvJsonValidatorService extends BaseService {
    
    @Autowired private EnvJsonValidatorDao envJsonValidatorDao;

    /**
     * 보안 메일 커버 템플릿 조회
     * @param serviceNo
     * @param serviceType
     * @return
     */
    public String getJonmun(String seq) {
        return envJsonValidatorDao.getJonmun(seq);
    }
}
