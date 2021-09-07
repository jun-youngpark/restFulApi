package com.mnwise.wiseu.web.env.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.env.dao.DurationInfoDao;

/**
 * DB 환경 변수를 관리한다.
 */
@Service
public class EnvServerInfoService extends BaseService {
    @Autowired private DurationInfoDao durationInfoDao;
    @Autowired private ServerInfoDao serverInfoDao;

    /**
     * 환경 변수를 DB에서 가져온다.
     *
     * @return
     */
    public ServerInfoVo selectEnvServerInfo() {
        return serverInfoDao.selectServerInfo();
    }

    /**
     * 환경 변수를 변경한다.
     *
     * @param envServerInfoVo
     */
    public void updateEnvServerInfo(ServerInfoVo serverInfoVo) {
        serverInfoDao.updateEnvServerInfo(serverInfoVo);
        durationInfoDao.updateEnvServerInfoDurationTime(Integer.parseInt(serverInfoVo.getDurationTime()));
    }
}
