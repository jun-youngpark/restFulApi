package com.mnwise.wiseu.web.campaign.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import org.springframework.stereotype.Repository;

/**
 * NVMIMEINFOLOG 테이블 DAO 클래스
 */
@Repository
public class MimeInfoLogDao extends BaseDao {

    public MimeViewVo selectEMMimeInfo(MimeViewVo mimeVo) {
        security.securityObject(mimeVo, "ENCRYPT");
        MimeViewVo tempVo = (MimeViewVo) selectOne("MimeInfoLog.selectMimeInfoLog", mimeVo);

        mimeVo.setStartIndex(tempVo.getStartIndex());
        mimeVo.setEndIndex(tempVo.getEndIndex());
        mimeVo.setMimePath(tempVo.getMimePath());

        return mimeVo;
    }

}