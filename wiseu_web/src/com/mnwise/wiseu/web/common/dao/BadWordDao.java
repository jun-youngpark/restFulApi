package com.mnwise.wiseu.web.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.env.model.BadWordVo;

/**
 * NVBADWORD 테이블 DAO 클래스
 */
@Repository
public class BadWordDao extends BaseDao {
    public int insertBadWord(BadWordVo badWord) {
        return insert("BadWord.insertBadWord", badWord);
    }

    public int updateBadWordByPk(BadWordVo badWord) {
        return update("BadWord.updateBadWordByPk", badWord);
    }

    public int deleteBadWordByPk(String channelType) {
        return delete("BadWord.deleteBadWordByPk", channelType);
    }

    public BadWordVo selectBadWordByPk(String channelType) {
        return (BadWordVo) selectOne("BadWord.selectBadWordByPk", channelType);
    }

    /**
     * 환경설정 - 금칙어 관리 데이터를 가져온다.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BadWordVo> selectAllBadWordList() {
        return selectList("BadWord.selectAllBadWordList");
    }

    public String selectBadWords(String channelType) {
        return (String) selectOne("BadWord.selectBadWordInfo", channelType);
    }


}