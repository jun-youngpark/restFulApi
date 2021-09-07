package com.mnwise.wiseu.web.ecare.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * NVECAREMIMEINFOLOG 테이블 DAO 클래스
 */
@Repository
public class EcareMimeInfoLogDao extends BaseDao {
    private static final Logger log = LoggerFactory.getLogger(EcareMimeInfoLogDao.class);

    public MimeViewVo selectECMimeInfo(MimeViewVo mimeViewVo) {
        security.securityObject(mimeViewVo, "ENCRYPT");
        MimeViewVo tempVo = (MimeViewVo) selectOne("EcareMimeInfoLog.ecareMimeInfoLog", mimeViewVo);

        log.debug("==========================");
        log.debug(" tempVo                 : " + tempVo);
        log.debug(" tempVo.getStartIndex() : " + tempVo.getStartIndex());
        log.debug(" tempVo.getEndIndex()   : " + tempVo.getEndIndex());
        log.debug(" tempVo.getMimePath()   : " + tempVo.getMimePath());
        log.debug("==========================");
        mimeViewVo.setStartIndex(tempVo.getStartIndex());
        mimeViewVo.setEndIndex(tempVo.getEndIndex());
        mimeViewVo.setMimePath(tempVo.getMimePath());

        return mimeViewVo;
    }

}