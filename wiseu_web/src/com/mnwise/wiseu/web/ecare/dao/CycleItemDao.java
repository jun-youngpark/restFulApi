package com.mnwise.wiseu.web.ecare.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.CycleItemVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NVCYCLEITEM 테이블 DAO 클래스
 */
@Repository
public class CycleItemDao extends BaseDao {
    /**
     * 이케어 스케쥴 타입(NVCYCLEITEM)을 등록한다.
     *
     * @param cycleItem
     */
    public int insertCycleItem(CycleItemVo cycleItem) {
        return insert("CycleItem.insertCycleItem", cycleItem);
    }

    /**
     * 이케어 스케쥴 타입(NVCYCLEITEM)을 삭제한다.
     *
     * @param ecmScheduleNo
     */
    public int deleteEcareCycleItemInfo(int ecmScheduleNo) {
        return delete("CycleItem.deleteEcareCycleItemInfo", ecmScheduleNo);
    }

    @SuppressWarnings("unchecked")
    public List<CycleItemVo> selectCycleItemInfo(int scheduleNo) {
        return selectList("CycleItem.selectCycleItemInfo", scheduleNo);
    }
}