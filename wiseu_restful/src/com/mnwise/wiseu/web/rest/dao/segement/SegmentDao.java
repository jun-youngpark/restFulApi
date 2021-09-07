package com.mnwise.wiseu.web.rest.dao.segement;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.segment.Segment;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVSEGMENT 테이블 DAO 클래스
 *
 * - 20100824 : 대상자 미리보기 팝업에서 DB 별로 코덱정보가 다를 수 있으므로 기존 APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져와 처리하도록 변경
 */
@Repository
public class SegmentDao extends BaseDao {
	 /**
     * 대상자(세그먼트) 정보를 가져온다.
     *
     * @param segmentNo
     * @return
     */
    public Segment selectSegmentByPk(int segmentNo) {
    	Segment tmp = (Segment) selectOne("Segment.selectSegmentByPk", segmentNo);
        return tmp;
    }

    public int insertSegment(Segment segment) {
        return insert("Segment.insertSegment", segment);
    }
    /**
     * 새로운 세그먼트 번호를 가져온다.
     *
     * @return
     */
    public int selectNextSegmentNo() {
        int nextSegmentNo = (Integer) selectOne("Segment.selectNextSegmentNo");
        return nextSegmentNo == 0 ? 1 : nextSegmentNo;
    }

    public int copySegmentForResend(Segment segment) {
        return insert("Segment.copySegmentForResend", segment);
    }

}
