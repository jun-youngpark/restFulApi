package com.mnwise.wiseu.web.env.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.BadWordDao;
import com.mnwise.wiseu.web.env.model.BadWordVo;

@Service
public class EnvBadWordService extends BaseService {
    @Autowired private BadWordDao badWordDao;

    /**
     * 환경설정 - 금칙어 관리 데이터를 가져온다.
     *
     * @return
     */
    public List<BadWordVo> selectBadWord() {
        return badWordDao.selectAllBadWordList();
    }

    /**
     * 환경설정 - 금칙어 관리 데이터를 변경한다.
     *
     * @param badWord
     */
    public void updateBadWord(BadWordVo badWord) {
        badWordDao.updateBadWordByPk(badWord);
    }
}
