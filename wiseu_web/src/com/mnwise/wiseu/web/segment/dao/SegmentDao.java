package com.mnwise.wiseu.web.segment.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.common.security.aria.Aria;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.TargetQueryInfo;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.segment.model.BookmarkVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.UsingSegmentVo;

/**
 * NVSEGMENT 테이블 DAO 클래스
 *
 * - 20100824 : 대상자 미리보기 팝업에서 DB 별로 코덱정보가 다를 수 있으므로 기존 APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져와 처리하도록 변경
 */
@Repository
public class SegmentDao extends BaseDao {
    public int insertSegment(SegmentVo segment) {
        if(StringUtil.isBlank(segment.getEditorId())) {
            segment.setEditorId(segment.getUserId());
        }

        return insert("Segment.insertSegment", segment);
    }

    public int copySegment(SegmentVo segment) {
        return insert("Segment.copySegment", segment);
    }

    public int copySegmentForResend(SegmentVo segment) {
        return insert("Segment.copySegmentForResend", segment);
    }

    public int copySegmentForOmniSend(SegmentVo segment) {
        return insert("Segment.copySegmentForOmniSend", segment);
    }

    public int insertPermission(SegmentVo segment) {
        return insert("Segment.insertPermission", segment);
    }

    public int insertLinkClickSegment(SegmentVo segment) {
        return insert("Segment.insertLinkClickSegment", segment);
    }

    public int updateSegmentByPk(SegmentVo segment) {
        return update("Segment.updateSegmentByPk", segment);
    }

    public int updateSegmentInfoAfterImportFile(SegmentVo segmentVo) {
        return update("Segment.updateSegmentInfoAfterImportFile", segmentVo);
    }

    public int updateSegmentSize(SegmentVo segment) {
        return update("Segment.updateSegmentSize", segment);
    }

    /**
     * 세그먼트 테스트 쿼리를 수정한다.
     *
     * @param segmentVo
     */
    public int updateUpdateQuery(SegmentVo segmentVo) {
        return update("Segment.updateUpdateQuery", segmentVo);
    }

    /**
     * 세그먼트 테스트 쿼리를 수정한다.
     *
     * @param segmentVo
     */
    public int updateTestQuery(SegmentVo segmentVo) {
        return update("Segment.updateTestQuery", segmentVo);
    }

    /**
     * 세그먼트 리스트에서 발송내역이 있을경우 DB에서 삭제가 불가능한데 ACTIVE_YN 의 FLAG 처리를 통하여 리스트에서 없앨수 있다.
     *
     * @param segmentNo
     */
    public int updateActiveYnToN(int segmentNo) {
        return update("Segment.updateActiveYnToN", segmentNo);
    }

    /**
     * 선택한 대상자의 쿼리 업데이트
     *
     * @param segmentVo
     * @return
     */
    public int updateSegmentSql(SegmentVo segmentVo) {
        return update("Segment.updateSegmentSql", segmentVo);
    }

    /**
     * 세그먼트의 수행상태를 업데이트 한다.
     *
     * @param segmentVo 세그먼트 번호와 상태값
     * @return
     */
    public int updateSegmentSts(SegmentVo segmentVo) {
        return update("Segment.updateSegmentSts", segmentVo);
    }

    public int updateSqlfilter(SegmentVo segmentVo) {
        return update("Segment.updateSqlfilter", segmentVo);
    }

    public int deleteSegmentByPk(int segmentNo) {
        return delete("Segment.deleteSegmentByPk", segmentNo);
    }

    /**
     * 대상자(세그먼트) 정보를 가져온다.
     *
     * @param segmentNo
     * @return
     */
    public SegmentVo selectSegmentByPk(int segmentNo) {
        SegmentVo tmp = (SegmentVo) selectOne("Segment.selectSegmentByPk", segmentNo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    public SegmentVo selectSegmentByCampaignNo(int campaignNo) {
        return (SegmentVo) selectOne("Segment.selectSegmentByCampaignNo", campaignNo);
    }

    public SegmentVo selectSegmentInfo(int serviceNo, String serviceType) {
        if(serviceType.equals("EM")) {
            return (SegmentVo) selectOne("Segment.selectCampaignSegmentQueryInfo", serviceNo);
        }

        return (SegmentVo) selectOne("Segment.selectEcareSegmentQueryInfo", serviceNo);
    }

    /**
     * 해당 세그먼트에 권한이 할당된 정보를 가져온다.
     *
     * @param segmentNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SegmentVo> assignPermission(int segmentNo) {
        return selectList("Segment.selectAssignPermission", segmentNo);
    }

    /**
     * 비동기로 업로드를 수행할 세그먼트 목록을 조회한다.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SegmentVo> getSegmentListForAsync() {
        return selectList("Segment.getSegmentListForAsync");
    }

    /**
     * 비동기로 업로드중 WAS 다운이 발생할 경우 최초 기동시에 기존 업로드 중인 데이터가 있는지 확인.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SegmentVo> getUploadRunListForAsync() {
        return selectList("Segment.getUploadRunListForAsync");
    }

    /**
     * NVSEGMENT테이블에서 대상자 쿼리를 가져온다.
     *
     * @param segmentNo
     * @return
     */
    public TargetQueryInfo selectTargetQueryInfo(int segmentNo) {
        TargetQueryInfo targetQueryInfo = (TargetQueryInfo) selectOne("Segment.selectTargetQueryInfo", segmentNo);

        if("on".equals(PropertyUtil.getProperty("cipher","off"))) {
            targetQueryInfo.setDbPassword(Aria.wiseuDecrypt(targetQueryInfo.getDbPassword()));
        }

        return targetQueryInfo;
    }

    public String getCampaignTargetQuery(int campaignNo) {
        return (String) selectOne("Segment.getCampaignTargetQuery", campaignNo);
    }

    public String getEcareTargetQuery(int ecareNo) {
        return (String) selectOne("Segment.getEcareTargetQuery", ecareNo);
    }

    public int selectSegmentCountByDbInfoSeq(int dbInfoSeq) {
        Integer count = (Integer) selectOne("Segment.selectSegmentCountByDbInfoSeq", dbInfoSeq);
        return (count == null) ? 0 : count;
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

    /**
     * 전체 세그먼트 건수를 가져온다.
     *
     * @param segmentVo
     * @return
     */
    public int selectSegmentTotalCount(SegmentVo segmentVo) {
        Integer count = (Integer) selectOne("Segment.selectSegmentTotalCount", segmentVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 세그먼트 리스트를 가져온다.
     *
     * @param segmentVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SegmentVo> selectSegmentList(SegmentVo segmentVo) {
        if(StringUtil.isEmpty(segmentVo.getOrderColumn())) {
            segmentVo.setOrderColumn(" SEGMENT_NO ");
            segmentVo.setOrderSort(" DESC ");
        }
        List<SegmentVo> tmp = (List<SegmentVo>) selectList("Segment.selectSegmentList", segmentVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 세그먼트 수행 이력을 확인한다.
     *
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UsingSegmentVo> selectUsingList(Map<String, Object> param) {
        List<UsingSegmentVo> tmp = (List<UsingSegmentVo>) selectList("Segment.selectUsingList", param);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    @SuppressWarnings("unchecked")
    public List<Integer> selectSendList(int segmentNo) {
        List<Integer> tmp = (List<Integer>) selectList("Segment.selectSendList", segmentNo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    public int selectBookmarkListTotalCount(BookmarkVo bookmarkVo) {
        Integer count = (Integer) selectOne("Segment.selectBookmarkListTotalCount", bookmarkVo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<SegmentVo> selectBookmarkList(BookmarkVo bookmarkVo) {
        List<SegmentVo> list = selectList("Segment.selectBookmarkList", bookmarkVo);
        for(SegmentVo segment : list) {
            security.securityObject(segment, "DECRYPT");
        }
        return list;
    }

    public int selectDefaultBookmarkListTotalCount(BookmarkVo bookmarkVo) {
        Integer count = (Integer) selectOne("Segment.selectDefaultBookmarkListTotalCount", bookmarkVo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<SegmentVo> selectDefaultBookmarkList(BookmarkVo bookmarkVo) {
        List<SegmentVo> list = selectList("Segment.selectDefaultBookmarkList", bookmarkVo);
        for(SegmentVo segment : list) {
            security.securityObject(segment, "DECRYPT");
        }
        return list;
    }

}
