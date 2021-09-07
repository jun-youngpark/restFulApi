package com.mnwise.wiseu.web.rest.dao.campaign;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVSCHEDULE 테이블 DAO 클래스
 */
@Repository
public class ScheduleDao extends BaseDao {

	/**
	 * 캠페인 스케줄 정보를 등록한다.(스케줄 번호 자동 생성)
	 *
	 * @param Campaign
	 */
	 public int insertScheduleForFirst(Campaign campaign) {
		 return insert("Schedule.insertScheduleForFirst", campaign);
	 }
	 /**
	 * 캠페인 스케줄 정보를 등록한다.
	 *
	 * @param Campaign
	 */
	 public int insertSchedule(Campaign campaign) {
		 return insert("Schedule.insertSchedule", campaign);
	 }
	 /**
     * 옴니 복사
     */
	 public int copyScheduleForResend(Campaign campaign) {
		 return insert("Schedule.copyScheduleForResend", campaign);
 	 }


}