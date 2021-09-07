package com.mnwise.wiseu.web.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.CdMstDao;
import com.mnwise.wiseu.web.common.model.CdMstVo;

/**
 * 공통 코드 마스터 조회용 서비스
 */
@Service
public class CdMstService extends BaseService {
    @Autowired private CdMstDao cdMstDao;

    /**
     * 공통 코드 마스터에서 cd_cat 코드값과 language로 목록을 조회한다.
     *
     * @param cdCat 코드 카테고리
     * @param language 사용 언어
     * @return
     * @throws Exception
     */
    public List<CdMstVo> getCdMstList(String cdCat, String language) throws Exception {
        return cdMstDao.getCdMstList(cdCat, language);
    }
}
