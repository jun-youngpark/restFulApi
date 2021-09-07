package com.mnwise.wiseu.web.rest.dao.campaign;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVSENDRESULT 테이블 DAO 클래스
 */
@Repository
public class SendResultDao extends BaseDao  {

    /**
     * 멀티채널 캠페인을 생성할 때 상위 캠페인이 종료 상태라면 NVSENDRESULT.RESULT_SEQ 값을 조회하여<br>
     * 대상자 SQL에 사용한다.
     *
     * @param campaignNo 상위 캠페인 번호
     * @return
     */
    public long selectResultSeq(int campaignNo) {
        return (Long) selectOne("SendResult.selectResultSeq", campaignNo);
    }


}