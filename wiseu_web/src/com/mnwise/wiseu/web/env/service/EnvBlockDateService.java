package com.mnwise.wiseu.web.env.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.env.dao.SendBlockDateDao;
import com.mnwise.wiseu.web.env.dao.SendBlockDateInfoDao;

@Service
public class EnvBlockDateService extends BaseService {
    @Autowired private SendBlockDateDao sendBlockDateDao;
    @Autowired private SendBlockDateInfoDao sendBlockDateInfoDao;

    /**
     * NVSENDBLOCKDATE 테이블에서 휴일 목록을 가져온다.
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List selectRestDays() {
        return sendBlockDateDao.selectRestDays();
    }

    /**
     * NVSENDBLOCKDATE 테이블에 주어진 일자를 휴일로 등록
     *
     * @param chkYear
     * @param blockDt
     */
    public void insertRestDays(String chkYear, String blockDt) {
        sendBlockDateDao.insertRestDays(chkYear, blockDt);
    }

    /**
     * NVSENDBLOCKDATE 테이블에 주어진 일자를 휴일 등록가능여부를 확인한다
     *
     * @param chkYear
     * @param blockDt
     * @return
     * @throws Exception
     */
    public boolean confirmRestDays(String chkYear, String blockDt) throws Exception {
        return sendBlockDateDao.confirmRestDays(chkYear, blockDt);
    }

    /**
     * NVSENDBLOCKDATE 테이블에서 등록된 휴일을 삭제
     *
     * @param chkYear
     * @param blockDt
     */
    public void deleteRestDays(String chkYear, String blockDt) {
        sendBlockDateDao.deleteRestDays(chkYear, blockDt);
    }

    /**
     * NVSENDBLOCKDATEINFO 테이블에서 주어진 년도의 주말발송제한 정보를 가져온다
     *
     * @param year
     * @return
     * @throws Exception
     */
    public String selectRegistInfo(int year) throws Exception {
        return sendBlockDateInfoDao.selectRegistInfo(year);
    }
}
