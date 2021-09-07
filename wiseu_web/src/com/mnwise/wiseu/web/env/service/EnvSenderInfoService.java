package com.mnwise.wiseu.web.env.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.env.dao.UserMailInfoDao;
import com.mnwise.wiseu.web.env.model.EnvSenderInfoVo;

@Service
public class EnvSenderInfoService extends BaseService {
    @Autowired private UserMailInfoDao userMailInfoDao;

    @Value("${channel.use.list}")
    private String channelUseList;

    /**
     * 환경설정 - 메시지 발신자 정보에서 개인정보를 가져온다.
     *
     * @param envSenderInfoVo
     * @return
     */
    public EnvSenderInfoVo selectEnvSenderInfo(EnvSenderInfoVo envSenderInfoVo) {
        envSenderInfoVo = userMailInfoDao.selectUserMailInfoByUserId(envSenderInfoVo.getUserId());
        if(envSenderInfoVo == null)
            envSenderInfoVo = new EnvSenderInfoVo();
        envSenderInfoVo.setChannelUseList(channelUseList);
        return envSenderInfoVo;
    }

    /**
     * 환경설정 - 메시지 발신자 정보에서 개인정보를 변경한다.
     *
     * @param envSenderInfoVo
     */
    public void updateEnvSenderInfo(EnvSenderInfoVo envSenderInfoVo) {
        userMailInfoDao.updateUserMailInfoByUserId(envSenderInfoVo);
    }

    /**
     * 환경설정 - 메시지 발신자 정보에서 개인정보를 추가한다.
     *
     * @param envSenderInfoVo
     */
    public void insertEnvSenderInfo(EnvSenderInfoVo envSenderInfoVo) {
        userMailInfoDao.insertUserMailInfo(envSenderInfoVo);
    }
}
