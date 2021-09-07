package com.mnwise.wiseu.web.ecare.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import org.springframework.stereotype.Repository;

/**
 * NVSCHEDULEACCEPT 테이블 DAO 클래스
 */
@Repository
public class ScheduleAcceptDao extends BaseDao {

    public String selectJeonmun(MimeViewVo mimeView) {
        return (String) selectOne("ScheduleAccept.selectJeonmun", mimeView);
    }

}