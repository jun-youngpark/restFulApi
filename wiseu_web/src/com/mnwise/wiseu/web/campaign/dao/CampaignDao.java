package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.common.model.MailPreviewVo;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.editor.model.CampaignEditorVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;

@Repository
public class CampaignDao extends BaseDao {
    private static final Logger log = LoggerFactory.getLogger(CampaignDao.class);

    public int insertCampaign1StepInfo(ScenarioVo scenarioVo) {
        return insert("Campaign.insertCampaign1StepInfo", scenarioVo);
    }

    public int copyCampaign(ScenarioVo scenarioVo) {
        return insert("Campaign.copyCampaign", scenarioVo);
    }

    public int copyCampaignForResend(ScenarioVo scenarioVo) {
        return insert("Campaign.copyCampaignForResend", scenarioVo);
    }

    public int updateCampaign1StepInfo(ScenarioVo scenarioVo) {
        return update("Campaign.updateCampaign1StepInfo", scenarioVo);
    }

    /**
     * 캠페인 정보를 변경한다.
     *
     * @param campaignVo
     */
    public int updateCampaign2StepInfo(CampaignVo campaignVo) {
        security.securityObject(campaignVo, "ENCRYPT");
        return update("Campaign.updateCampaign2StepInfo", campaignVo);
    }

    /**
     * 캠페인 상태를 변경한다.
     *
     * @param campaignVo
     * @return
     */
    public int updateCampaignStsInfo(CampaignVo campaignVo) {
        return update("Campaign.updateCampaignStsInfo", campaignVo);
    }

    /**
     * 캠페인 승인 상태를 변경한다.
     *
     * @param campaignVo
     * @return
     */
    public int updateApprovalStsInfo(CampaignVo campaignVo) {
        return update("Campaign.updateApprovalStsInfo", campaignVo);
    }

    /**
     * 캠페인의 세그먼트 번호를 변경한다.
     *
     * @param campaignVo
     * @return
     */
    public int updateCampaignSegmentNoInfo(CampaignVo campaignVo) {
        return update("Campaign.updateCampaignSegmentNoInfo", campaignVo);
    }

    /**
     * 테스트 발송시 ServiceType이 C NVCAMAPAIGN의 SENDING_MODE 를 T로 바꿔준다.
     *
     * @param no
     * @return
     */
    public int updateTestModeCampaign(int no) {
        return update("Campaign.updateTestModeCampaign", no);
    }

    /**
     * 캠페인 스케쥴 정보를 변경한다.
     *
     * @param campaignVo
     * @return
     */
    public int updateCampaignScheduleInfo(CampaignVo campaignVo) {
        return update("Campaign.updateCampaignScheduleInfo", campaignVo);
    }

    /**
     * 캠페인 발송 상태를 변경한다.
     *
     * @param scenarioVo
     */
    public int changeStatus(ScenarioVo scenarioVo) {
        return update("Campaign.changeStatus", scenarioVo);
    }

    public int updateEditorCampaign(CampaignEditorVo campaignEditorVo) {
        return update("Campaign.updateEditorCampaign", campaignEditorVo);
    }

    public int deleteCampaignAll(ScenarioVo scenarioVo) {
        return delete("Campaign.deleteCampaignAll", scenarioVo);
    }

    /**
     * 시나리오 번호에 해당하는 세그먼트를 가져온다.
     *
     * @param scenarioNo
     * @return
     */
    public int selectSegmentNo(int scenarioNo) {
        return (Integer) selectOne("Campaign.selectMaxSegmentNo", scenarioNo);
    }

    public int getMaxCampaignNo(ScenarioInfoVo scenarioInfoVo) {
        return (Integer) selectOne("Campaign.getMaxCampaignNo", scenarioInfoVo);
    }

    /**
     * 새로운 캠페인 번호를 가져온다.
     *
     * @return 새로운 캠페인 번호(MAX + 1)
     */
    public int selectNewCampaignNo() {
        return (Integer) selectOne("Campaign.selectNextCampaignNo");
    }

    /**
     * 시나리오에 딸린 캠페인 채널 정보를 가져온다.
     *
     * @param scenarioNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignVo> selectScenariochannelInfo(int scenarioNo) {
        return selectList("Campaign.getScenarioChannelInfo", scenarioNo);
    }


    /**
     * 캠페인 정보를 가져온다.
     *
     * @param campaignNo
     * @return
     */
    public CampaignVo selectCampaignInfo(int campaignNo) {
        return (CampaignVo) selectOne("Campaign.selectCampaignInfo", campaignNo);
    }

    /**
     * 선택한 캠페인의 NVCAMPAIGN.DEPTH 값보다 1 크고,
     * NVCAMPAIGN.RELATION_TYPE 값이 'S', 'F', 'O' 중 하나인 캠페인을 조회한다.
     *
     * @param campaignVo scenarioNo, depthNo 값이 설정된 객체
     */
    @SuppressWarnings("unchecked")
    public List<CampaignVo> selectSubCampaignList(CampaignVo campaignVo) {
        return selectList("Campaign.selectSubCampaignList", campaignVo);
    }

    /**
     * 해당 캠페인의 RELATION_TYPE과 DEPTH_NO 값을 조회한다. <br>
     * RELATION_TYPE 값으로 멀티채널인지 재발송인지 확인하고 DEPTH_NO 값으로 하위 캠페인인지 확인한다.
     *
     * @param serviceNo
     * @return
     */
    public CampaignVo selectRelationAndDepth(int serviceNo) {
        return (CampaignVo) selectOne("Campaign.selectRelationAndDepth", serviceNo);
    }

    /**
     * 부모 캠페인 정보를 조회한다.
     *
     * @param scenarioNo
     * @return
     */
    public CampaignVo selectParentCampaignInfo(int scenarioNo) {
        return (CampaignVo) selectOne("Campaign.selectParentCampaignInfo", scenarioNo);
    }

    /**
     * 캠페인 발송 리턴메일 주소를 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public String selectCampaignRetmailReceiver(ScenarioVo scenarioVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioVo.getScenarioNo());
        paramMap.put("channelType", scenarioVo.getCampaignVo().getChannelType());
        return (String) selectOne("Campaign.selectCampaignRetmailReceiver", paramMap);
    }

    /**
     * 캠페인 발송 담당자 이름을 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public String selectCampaignSenderNm(ScenarioVo scenarioVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", scenarioVo.getUserId());
        paramMap.put("grpCd", scenarioVo.getGrpCd());
        paramMap.put("tagNo", scenarioVo.getTagNo());
        paramMap.put("channelType", scenarioVo.getCampaignVo().getChannelType());
        return (String) selectOne("Campaign.selectCampaignSenderNm", paramMap);
    }

    /**
     * 캠페인 발송 담당자 이메일을 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public String selectCampaignSenderEmail(ScenarioVo scenarioVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioVo.getScenarioNo());
        paramMap.put("channelType", scenarioVo.getCampaignVo().getChannelType());

        return (String) selectOne("Campaign.selectCampaignSenderEmail", paramMap);
    }

    /**
     * 캠페인 발송 담당자 핸드폰 번호를 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public String selectCampaignSenderTel(ScenarioVo scenarioVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", scenarioVo.getUserId());
        paramMap.put("grpCd", scenarioVo.getGrpCd());
        paramMap.put("tagNo", scenarioVo.getTagNo());
        paramMap.put("channelType", scenarioVo.getCampaignVo().getChannelType());
        return (String) selectOne("Campaign.selectCampaignSenderTel", paramMap);
    }

    /**
     * 캠페인 타입(수신확인 여부)을 가져온다.
     *
     * @param campaignNo
     * @return
     */
    public String selectCampaignType(int campaignNo) {
        return (String) selectOne("Campaign.selectCampaignType", campaignNo);
    }

    /**
     * 캠페인 번호를 가져온다.
     *
     * @return
     */
    public int getCampaignNo(Map<String, Object> map) {
        return (Integer) selectOne("Campaign.getCampaignNo", map);
    }

    /**
     * 해당 캠페인이 속한 시나리오의 최대 Depth No 를 가져온다.
     *
     * @param scenarioNo
     * @param campaignNo
     * @return
     */
    public int getCampaignReportMaxDepth(int scenarioNo, int campaignNo, String relationType) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioNo);
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("relationType", relationType);

        return (Integer) selectOne("Campaign.getCampaignReportMaxDepth", paramMap);
    }

    public MailPreviewVo selectCampaignPreview(int campaignNo) {
        MailPreviewVo mailPreview = (MailPreviewVo) selectOne("Campaign.selectCampaignPreview", campaignNo);
        security.securityObject(mailPreview, "DECRYPT");
        return mailPreview;
    }

    public MessageVo getCampaignDetail(int serviceNo) {
        MessageVo message = (MessageVo) selectOne("Campaign.getCampaignDetail", serviceNo);
        security.securityObject(message, "DECRYPT");
        return message;
    }

    /**
     * 캠페인 저작기에 필요한 CAMPAIGN_PREFACE, TEMPLATE_TYPE, SURVEY_NO 필드 값을 조회한다.
     */
    public CampaignEditorVo selectEditorCampaign(int no) {
        CampaignEditorVo campaignEditor = (CampaignEditorVo) selectOne("Campaign.selectEditorCampaign", no);

        try {
            security.securityObject(campaignEditor, "DECRYPT");
        } catch(Exception e) {
            log.error(null, e);
        }

        return campaignEditor;
    }
}
