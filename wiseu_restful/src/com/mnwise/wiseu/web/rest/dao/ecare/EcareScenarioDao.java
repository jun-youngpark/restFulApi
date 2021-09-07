package com.mnwise.wiseu.web.rest.dao.ecare;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVSCENARIO 테이블 DAO 클래스
 */
@Repository
public class EcareScenarioDao extends BaseDao {

	/**
	 * 이케어 시나리오 기본정보를 등록한다.(시나리오 번호 자동 생성)
	 *
	 * @param Ecare
	 */
	public int insertEcareScenarioForFirst(Ecare Ecare) throws Exception {
		return insert("EcareScenario.insertEcareScenarioForFirst", Ecare);
	}
	 /**
	 * 캠페인 시나리오 기본정보를 등록한다.
	 *
	 * @param Ecare
	 */
	public int insertEcareScenario(Ecare Ecare) {
		return insert("EcareScenario.insertEcareScenario", Ecare);
	}


}