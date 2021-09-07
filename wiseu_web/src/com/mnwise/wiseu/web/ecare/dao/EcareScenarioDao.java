package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;

@Repository
public class EcareScenarioDao extends BaseDao {
    public int insertEcareScenario(EcareScenarioVo ecareScenarioVo) {
        return insert("EcareScenario.insertEcareScenario", ecareScenarioVo);
    }

    public int updateEcareScenarioByPk(EcareScenarioVo ecareScenario) {
        return update("EcareScenario.updateEcareScenarioByPk", ecareScenario);
    }


    public int updateEcareScenario2StepInfo(EcareScenarioVo ecareScenarioVo) {
        return update("EcareScenario.updateEcareScenario2StepInfo", ecareScenarioVo);
    }

    public int deleteEcareScenario(EcareScenarioVo scenarioVo) {
        //시나리오 번호가 0일 경우 전체 삭제 될 소지가 있음
        if (scenarioVo.getScenarioNo() == 0) {
            return 0;
        }

        return delete("EcareScenario.deleteEcareScenario", scenarioVo);
    }

    /**
     * 시나리오에 묶여 있는 이케어 기본정보를 가져온다.
     *
     * @param ecareScenarioVo
     * @return
     */
    public EcareScenarioVo selectEcareScenario1StepInfo(EcareScenarioVo ecareScenarioVo) {
        EcareScenarioVo tmp = (EcareScenarioVo) selectOne("EcareScenario.selectEcareScenario1StepInfo", ecareScenarioVo);
        security.securityObject(tmp, "DECRYPT");
        security.securityObjectList(tmp.getEcareList(),  "DECRYPT");
        return tmp;
    }

    /**
     * 이케어 상세 정보를 가져온다.
     *
     * @param ecareScenarioNo
     * @param ecareNo
     * @return
     */
    public EcareScenarioVo selectEcareScenarioDetailInfo(int ecareScenarioNo, int ecareNo, String language) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", ecareScenarioNo);
        paramMap.put("ecareNo", ecareNo);
        paramMap.put("language", language);

        EcareScenarioVo tmp = (EcareScenarioVo) selectOne("EcareScenario.selectEcareScenarioDetailInfo", paramMap);
        security.securityObject(tmp, "DECRYPT");
        security.securityObject(tmp.getEcareVo() , "DECRYPT");

        return tmp;
    }

    /**
     * 이케어 리스트 총 카운트
     *
     * @param ecareScenarioVo
     * @return
     */
    public int getEcareListTotalCount(EcareScenarioVo ecareScenarioVo) {
        Integer count = (Integer) selectOne("EcareScenario.getEcareListTotalCount", ecareScenarioVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 이케어 리스트
     *
     * @param ecareScenarioVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<EcareScenarioVo> getEcareList(EcareScenarioVo ecareScenarioVo) {
        return selectList("EcareScenario.getEcareList", ecareScenarioVo);
    }

    /**
     * 시나리오 번호가 동일하고 NVECARE.RELATION_TYPE 값이 'N', 'S', 'F', 'O' 인 것을 모두 조회한다.
     *
     * @param scenarioVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<EcareScenarioVo> selectOmniChannelEcares(EcareScenarioVo scenarioVo) {
        return selectList("EcareScenario.selectOmniChannelEcares", scenarioVo);
    }

    /**
     * 새로운 시나리오 번호를 생성한다.
     *
     * @return 시나리오 번호
     */
    public int selectNewScenarioNo() {
        return (int) selectOne("EcareScenario.selectMaxEcareScenarioNo");
    }
}