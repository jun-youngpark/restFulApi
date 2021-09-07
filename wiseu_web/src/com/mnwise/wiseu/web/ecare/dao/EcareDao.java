package com.mnwise.wiseu.web.ecare.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MailPreviewVo;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.editor.model.EcareEditorVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.watch.model.EcareMonitorVO;

/**
 * NVECAREMSG 테이블 DAO 클래스
 */
@Repository
public class EcareDao extends BaseDao {

    public int insertEcare1StepInfo(EcareVo ecareVo) {
        return insert("Ecare.insertEcare1StepInfo", ecareVo);
    }

    public int copyEcareForResend(EcareScenarioVo ecareScenario) {
        return insert("Ecare.copyEcareForResend", ecareScenario);
    }

    public int copyEcareForOmni(EcareScenarioVo ecareScenario) {
        return insert("Ecare.copyEcareForOmni", ecareScenario);
    }

    /**
     * 이케어 상세 정보를 변경한다.
     * 삭제예정
     * @param ecareVo
     */
    public int updateEcare2StepInfo(EcareVo ecareVo) {
        security.securityObject(ecareVo, "ENCRYPT");
        return update("Ecare.updateEcare2StepInfo", ecareVo);
    }

    /**
     * 테스트 발송시 ServiceType이 E NVECAREMSG의 SENDING_MODE 를 T로 바꿔준다.
     *
     * @param no
     * @return
     */
    public int updateTestModeEcare(int no) {
        return update("Ecare.updateTestModeEcare", no);
    }

    /**
     * 이케어 검증 부서를 등록한다.
     *
     * @param ecareVo
     * @return
     */
    public int updateEcareVerifyInfo(EcareVo ecareVo) {
        return update("Ecare.updateEcareVerifyInfo", ecareVo);
    }

    /**
     * 이케어 상태 업데이트
     *
     * @param ecareVo
     * @return
     */
    public int updateEcareStsInfo(EcareVo ecareVo) {
        return update("Ecare.updateEcareStsInfo", ecareVo);
    }

    /**
     * 조건에 맞는 전체 이케어 상태를 변경
     *
     * @param ecareVo
     * @return
     */
    public int updateAllEcareSts(EcareVo ecareVo) {
        return update("Ecare.updateAllEcareSts", ecareVo);
    }

    /**
     * 한 시나리오 번호를 가진 서비스들에게 공통정보 저장하기 (세그먼트 번호, 스케쥴 번호)
     *
     * @param ecareVo
     */
    public int updateEcareScenario2StepCommonInfo(EcareVo ecareVo) {
        return update("Ecare.updateEcare2StepCommonInfo", ecareVo);
    }

    /**
     * 이케어 상태변경
     *
     * @param ecareNo
     * @return
     */
    public int updateEcareStsChange(int ecareNo) {
        return update("Ecare.updateEcareStsChange", ecareNo);
    }

    public int updateEcareMsgForResend(EcareScenarioVo ecareScenario) {
        return update("Ecare.updateEcareMsgForResend", ecareScenario);
    }

    /**
     * 이케어 저작기 추가정보 저장
     * @param ecareEditor
     * @return
     */
    public int updateEditorEcare(EcareEditorVo ecareEditor) {
        return update("Ecare.updateEditorEcare", ecareEditor);
    }

    public int deleteEcareMsgByPk(int ecareNo) {
        return delete("Ecare.deleteEcareMsgByPk", ecareNo);
    }

    public MailPreviewVo selectEcarePreview(int ecareNo) {
        MailPreviewVo mailPreview = (MailPreviewVo) selectOne("Ecare.selectEcarePreview", ecareNo);
        security.securityObject(mailPreview, "DECRYPT");
        return mailPreview;
    }

    public MailPreviewVo selectEcarePreviousView(Map<String, Object> paramMap) {
        MailPreviewVo mailPreview = (MailPreviewVo) selectOne("Ecare.selectEcarePreviousView", paramMap);
        security.securityObject(mailPreview, "DECRYPT");
        return mailPreview;
    }

    public MessageVo getEcareDetail(int serviceNo) {
        MessageVo tmp = (MessageVo) selectOne("Ecare.getEcareDetail", serviceNo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    public EcareVo selectEcareForDelete(int ecareNo) {
        return (EcareVo) selectOne("Ecare.selectEcareForDelete", ecareNo);
    }

    /**
     * 시나리오에 딸린 이케어 채널 정보를 가져온다.
     *
     * @param scenarioNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<EcareVo> getEcareChannelInfo(int scenarioNo) {
        return selectList("Ecare.getEcareChannelInfo", scenarioNo);
    }

    /**
     * 이케어 정보를 가져온다.
     *
     * @param ecareNo
     * @return
     */
    public EcareVo selectEcareInfo(int ecareNo) {
        return (EcareVo) selectOne("Ecare.selectEcareInfo", ecareNo);
    }

    /**
     * 선택한 이케어의 NVECAREMSG.DEPTH 값보다 1 크고,
     * NVECAREMSG.RELATION_TYPE 값이 'S', 'F', 'O' 중 하나인 이케어를 조회한다.
     *
     * @param ecareScenarioVo scenarioNo, depthNo 값이 설정된 객체
     */
    @SuppressWarnings("unchecked")
    public List<EcareVo> selectSubEcareList(EcareScenarioVo ecareScenarioVo) {
        return selectList("Ecare.selectSubEcareList", ecareScenarioVo);
    }

    /**
     * 새로운 이케어 번호를 가져온다.
     *
     * @return 새로운 이케어 번호(MAX + 1)
     */
    public int selectNextEcareNo() {
        return (int) selectOne("Ecare.selectNextEcareNo");
    }

    public String selectEcareNmByPk(int resendEcareNo) {
        return (String) selectOne("Ecare.selectEcareNmByPk", resendEcareNo);
    }

    /**
     * 이케어 반송 메일 주소를 가져온다.
     *
     * @param ecareScenarioVo
     * @return
     */
    public String selectEcareRetmailReceiver(EcareScenarioVo ecareScenarioVo) {
        String retmailReceiver = (String) selectOne("Ecare.selectEcareRetmailReceiver", ecareScenarioVo);
        return (retmailReceiver == null) ? "" : retmailReceiver;
    }

    /**
     * 이케어 발신자 이메일을 가져온다.
     *
     * @param ecareScenarioVo
     * @return
     */
    public String selectEcareSenderEmail(EcareScenarioVo ecareScenarioVo) {
        return (String) selectOne("Ecare.selectEcareSenderEmail", ecareScenarioVo);
    }

    /**
     * 이케어 발송자 전화번호를 가져온다.
     *
     * @param ecareScenarioVo
     * @return
     */
    public String selectEcareSenderTel(EcareScenarioVo ecareScenarioVo) {
        return (String) selectOne("Ecare.selectEcareSenderTel", ecareScenarioVo);
    }

    /**
     * 이케어 발신자 이름을 가져온다.
     *
     * @param ecareScenarioVo
     * @return
     */
    public String selectEcareSenderNm(EcareScenarioVo ecareScenarioVo) {
        return (String) selectOne("Ecare.selectEcareSenderNm", ecareScenarioVo);
    }

    public int selectResendEcareNo(int ecareNo) {
        return (Integer) selectOne("Ecare.selectResendEcareNo", ecareNo);
    }

    public String selectEcareCycleCdInfo(int ecareNo) {
        return (String) selectOne("Ecare.selectEcareCycleCdInfo", ecareNo);
    }

    /**
     * 서비스 아이디가 등록된게 있는지를 확인한다.
     *
     * @param serviceID
     * @return
     */
    public int checkServiceID(String serviceID) {
        Integer count = (Integer) selectOne("Ecare.checkServiceID", serviceID);
        return (count == null) ? 0 : count;
    }

    public EcareEditorVo selectEditorEcare(int ecareNo) {
        EcareEditorVo tmp = (EcareEditorVo) selectOne("Ecare.selectEditorEcare", ecareNo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 사용자 아이디와 서비스 아이디로 ecare_no 를 조회
     *
     * @param map 사용자 아이디
     * @return
     */
    public int getEcareMsgByEcareNo(Map<String, String> map) {
        return (Integer) selectOne("Ecare.getEcareMsgByEcareNo", map);
    }

    @SuppressWarnings("unchecked")
    public List<EcareMonitorVO> getEcareMonitorStatusList2(String userId) {
        return selectList("Ecare.getEcareMonitorStatusList2", userId);
    }

    @SuppressWarnings("unchecked")
    public List<EcareVo> getScenarioChannelList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("Ecare.getScenarioChannelList", scenarioInfoVo);
    }

    public String getChannel(int ecareNo) {
        return (String)selectOne("Ecare.getChannel", ecareNo);
    }

    public int selectEcareReportListCount(EcareScenarioInfoVo ecareScenarioInfoVo) {
        Integer count = (Integer) selectOne("Ecare.selectEcareReportListCount", ecareScenarioInfoVo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<EcareScenarioInfoVo> selectEcareReportList(EcareScenarioInfoVo ecareScenarioInfoVo) {
        List<EcareScenarioInfoVo> ecareInfo = selectList("Ecare.selectEcareReportList", ecareScenarioInfoVo);

        for(EcareScenarioInfoVo tmp : ecareInfo) {
            security.securityObject(tmp.getEcareInfoVo().getSendResultVo(), "DECRYPT");
        }

        return ecareInfo;
    }

    public EcareScenarioInfoVo selectScenarioRealtimeInfo(int ecareNo) {
        EcareScenarioInfoVo ecareScenarioInfo = (EcareScenarioInfoVo) selectOne("Ecare.selectScenarioRealtimeInfo", ecareNo);

        if(ecareScenarioInfo != null) {
            security.securityObject(ecareScenarioInfo.getUserVo(), "DECRYPT");
        }

        return ecareScenarioInfo;
    }

    public EcareScenarioInfoVo selectScenarioScheduleInfo(int ecareNo) {
        EcareScenarioInfoVo ecareScenarioInfo = (EcareScenarioInfoVo) selectOne("Ecare.selectScenarioScheduleInfo", ecareNo);

        if(ecareScenarioInfo != null) {
            security.securityObject(ecareScenarioInfo.getUserVo(), "DECRYPT");
        }

        return ecareScenarioInfo;
    }

    /**
     * 리포트 > 이케어 월별 발송 통계에서 이케어리스트 dwr로 가져와 셀렉트박스의 옵션으로 뿌려준다
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<EcareVo> selectEcareType(Map<String, Object> paramMap) {
        return selectList("Ecare.selectEcareType", paramMap);
    }

    public List<EcareVo> selectEcareList() {
        return selectList("Ecare.selectEcareList");
    }
    
    /**
     * 이케어 분할발송 스케줄 변경
     *
     * @param ecareVo
     * @return
     */
    public int updateEcareScheduleInfo(EcareVo ecareVo) {
        return update("Ecare.updateEcareScheduleInfo", ecareVo);
    }

}