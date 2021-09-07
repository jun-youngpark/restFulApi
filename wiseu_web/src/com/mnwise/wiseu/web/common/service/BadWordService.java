package com.mnwise.wiseu.web.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.BadWordDao;

@Service
public class BadWordService extends BaseService {
    @Autowired private BadWordDao badWordDao;

    public String getBadWordsInfo(String channelType) {
        return badWordDao.selectBadWords(channelType);
    }
}
