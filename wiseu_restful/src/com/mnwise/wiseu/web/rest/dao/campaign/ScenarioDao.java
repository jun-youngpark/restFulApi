package com.mnwise.wiseu.web.rest.dao.campaign;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVSCENARIO 테이블 DAO 클래스
 */
@Repository
public class ScenarioDao extends BaseDao {

	/**
	 * 캠페인 시나리오 기본정보를 등록한다.(시나리오 번호 자동 생성)
	 *
	 * @param Campaign
	 */
	public int insertScenarioForFirst(Campaign campaign) throws Exception {
		return insert("Scenario.insertScenarioForFirst", campaign);
	}
	 /**
	 * 캠페인 시나리오 기본정보를 등록한다.
	 *
	 * @param Campaign
	 */
	public int insertScenario(Campaign campaign) {
		return insert("Scenario.insertScenario", campaign);
	}
}