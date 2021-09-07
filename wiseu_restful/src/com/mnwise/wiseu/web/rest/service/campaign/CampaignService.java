package com.mnwise.wiseu.web.rest.service.campaign;


import static com.mnwise.wiseu.web.base.util.CodeUtil.getSuccessCodeByChannel;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.common.util.ChannelUtil;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.dao.campaign.CampaignDao;
import com.mnwise.wiseu.web.rest.dao.campaign.ScenarioDao;
import com.mnwise.wiseu.web.rest.dao.campaign.ScheduleDao;
import com.mnwise.wiseu.web.rest.dao.campaign.SendResultDao;
import com.mnwise.wiseu.web.rest.dao.campaign.TraceInfoDao;
import com.mnwise.wiseu.web.rest.dao.common.CommonDao;
import com.mnwise.wiseu.web.rest.dao.handler.HandlerDao;
import com.mnwise.wiseu.web.rest.dao.segement.SegmentDao;
import com.mnwise.wiseu.web.rest.dao.segement.SemanticDao;
import com.mnwise.wiseu.web.rest.dao.template.TemplateDao;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.dto.ReturnDto.CampaignDto;
import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.model.segment.Segment;
import com.mnwise.wiseu.web.rest.parent.BaseService;

import org.json.simple.JSONObject;

@Service
public class CampaignService extends BaseService {

	@Autowired private CampaignDao campaignDao;
	@Autowired private ScenarioDao scenarioDao;
	@Autowired private ScheduleDao scheduleDao;
	@Autowired private TraceInfoDao traceInfoDao;
	@Autowired private TemplateDao templateDao;
	@Autowired private HandlerDao handlerDao;
	@Autowired private SendResultDao sendResultDao;
	@Autowired private SegmentDao segmentDao;
	@Autowired private SemanticDao semanticDao;
	@Autowired private CommonDao commonDao;
	@Value("${omnichannel.message.channel:M}")
	private String useOmnichannel;
	@Value("${campaign.channel.use.list:M}")
	private String useCampaignchannel;

	/**
     * 캠페인 등록
     *
     * @param Campaign
     * @return JSONObject
     */
	@Transactional(value = "txManager" , rollbackFor = Exception.class)
	public JSONObject create(Campaign campaign) throws Exception{
		if(scenarioDao.insertScenarioForFirst(campaign) > 0) {
			campaignDao.insertCampaignForFirst(campaign);	//캠페인
			scheduleDao.insertScheduleForFirst(campaign);	//스케줄
	        traceInfoDao.insertTraceInfo(makeTraceInfo(campaign.getCampaignNo()));
	        templateDao.insertEmTemplate(campaign);			//캠페인
	        handlerDao.insertEmHandler(campaign);			//핸들러
		}else{
			throw new Exception("Scenario Create Error");
		};

		// Response
		return new DataMap<>()
				.put("campaignNo",campaign.getCampaignNo())
				.build();
	}

	/**
     * 캠페인 수정
     *
     * @param Campaign
     * @return JSONObject
     */
	@Transactional(value = "txManager" , rollbackFor = Exception.class)
	public JSONObject update(Campaign campaign) throws Exception{
		if(campaignDao.updateCampaign(campaign) > 0) {
			if(isNotBlank(campaign.getTemplate())) {
				templateDao.updateEmTemplate(campaign);
			}
		}else {
			throw new Exception("Campaign number ("+campaign.getCampaignNo()+") does not exist.");
		}
		// Response
		return new DataMap<>()
				.put("campaignNo",campaign.getCampaignNo())
				.build();
	}

	/**
     * 캠페인 삭제
     *
     * @param Campaign
     * @return JSONObject
     */
	public JSONObject delete(Campaign campaign) throws Exception{
		CampaignDto getCampaign = Optional
				.ofNullable(campaignDao.selectCampaign(campaign))
				.orElseThrow(() -> new Exception("Campaign number ("+campaign.getCampaignNo()+") does not exist."));

		if(StringUtils.equalsIgnoreCase("N",getCampaign.getUseYn())
			&& StringUtils.equalsIgnoreCase("N",campaign.getUseYn())){
			throw new Exception("The campaign has been deleted.("+campaign.getCampaignNo()+")");
		}

		int result = campaignDao.deleteCampaign(campaign);
		// Response
		return new DataMap<>()
				.put("campaignNo",campaign.getCampaignNo())
				.put("useYn",campaign.getUseYn())
				.build();
	}

	/**
     * 캠페인 리스트 조회
     *
     * @param Campaign
     * @return JSONObject
     */
	public JSONObject list(Campaign campaign) throws Exception {
		int size = campaignDao.selectCampaignListCount(campaign);
		List<CampaignDto> list = campaignDao.selectCampaignList(campaign);
		// Response
		return new DataMap<>()
				.put("size",size)
				.put("list",list)
				.build();
	}

	/**
     * 캠페인 단건 조회
     *
     * @param Campaign
     * @return JSONObject
     */
	public JSONObject get(Campaign campaign) throws Exception {
		// Response
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(campaignDao.selectCampaign(campaign), JSONObject.class);
	}

	/**
     * 캠페인 옴니 리스트조회
     *
     * @param Campaign
     * @return JSONObject
     */
	public List<CampaignDto> getOmniList(Campaign campaign) throws Exception {
		// Response
		return campaignDao.findOmniCampaign(campaign);
	}

	/**
     * 캠페인 상태변경
     *
     * @param Campaign
     * @return JSONObject
     */
	public JSONObject updateState(Campaign campaign) throws Exception{
		CampaignDto getCampaign = Optional
				.ofNullable(campaignDao.selectCampaign(campaign))
				.orElseThrow(() -> new Exception("Campaign number ("+campaign.getCampaignNo()+") does not exist."));
		if(getCampaign.getCampaignSts().equals(campaign.getCampaignSts())) {
			throw new Exception("Campaign status is already ("+campaign.getCampaignSts()+")");
		}

		campaignDao.updateState(campaign);

		// Response
		return new DataMap<>().put("campaignNo",campaign.getCampaignNo())
					.build();
	}

	/**
     * 캠페인 옴니 생성
     *
     * @param Campaign
     * @return JSONObject
     */
	public JSONObject createOmni(Campaign campaign) throws Exception{
		CampaignDto superCampaign = Optional
				.ofNullable(campaignDao.selectCampaign(campaign))
				.orElseThrow(() -> new Exception("Campaign number ("+campaign.getCampaignNo()+") does not exist."));

		String resultSeq = "0000000000000000";
		if(Const.STATUS_END.equals(superCampaign.getCampaignSts())) {
            long tmp = sendResultDao.selectResultSeq(campaign.getCampaignNo());
            if(tmp > 0L) {
	                resultSeq = String.valueOf(tmp);
            }
        }
		final String headSql = makeTargetSQL(campaign.getRelationType(), campaign.getCampaignNo(), resultSeq, campaign.getChannelType());
		final Segment segmentVo = makeSegmentVo(superCampaign.getSegmentNo(), "M", headSql);

	    // 상위 캠페인의 relation_tree에 새로 생성하는 캠페인의 relation_type을 붙여 캠페인간 관계를 나타낸다.
		campaign.setRelationTree(superCampaign.getRelationTree() + campaign.getRelationType());
		campaign.setScenarioNo(superCampaign.getScenarioNo());
		campaign.setNewCampaignNo(campaignDao.selectNewCampaignNo());
		campaign.setTargetCnt(segmentVo.getSegmentSize());
		campaign.setSegmentNo(segmentVo.getSegmentNo());

		if(campaignDao.getEmOmniCount(Campaign.builder()
					.scenarioNo(superCampaign.getScenarioNo())
					.depthNo(superCampaign.getDepthNo()+1)
					.campaignNo(campaign.getCampaignNo())
					.relationType(campaign.getRelationType())
					.build()) > 0){
			throw new Exception("이미 등록된 옴니채널 유형이 있습니다.");
		}


		segmentDao.copySegmentForResend(segmentVo);
		semanticDao.copySemantic(segmentVo.getPsegmentNo(), segmentVo.getSegmentNo());
		campaignDao.copyCampaignForResend(campaign);
		traceInfoDao.copyTraceInfo(campaign);
		scheduleDao.copyScheduleForResend(campaign);
		templateDao.copyEmTemplate(campaign);
		handlerDao.insertOmniEmHandler(campaign);

      // Response
   		return new DataMap<>().put("campaignNo",campaign.getNewCampaignNo())
   					.build();
	}


    /**
     *  채널 멀티채널 하위 대상자 SQL을 생성한다.<br>
     * 재발송 모드에 따라 대상자 SQL과 대상자 카운트 SQL 생성
     *
     * @param resendMode
     * @param campaignNo 상위 캠페인 번호
     * @param resultSeq
     * @param channelType
     * @return
     */
    private String makeTargetSQL(String resendMode, int campaignNo, String resultSeq, String channelType) {
        if(Const.RELATION_SUCCESS.equals(resendMode) || Const.RELATION_FAIL.equals(resendMode)) {
            String inCondition;
            if(Const.RELATION_SUCCESS.equals(resendMode)) {
                inCondition = " IN ( ";
            } else {
                inCondition = " NOT IN ( ";
            }

            final StringBuilder sb = new StringBuilder();
            //SECUDB CHK
            // NVSENDLOG : 해당 필드는 없음
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM NVSENDLOG A, NVRESENDTARGET B");
            sb.append(" WHERE A.CAMPAIGN_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND A.ERROR_CD ").append(inCondition);
            sb.append(getSuccessCodeByChannel(channelType));
            sb.append(" )");
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.CAMPAIGN_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        } else if(Const.RELATION_OPEN.equals(resendMode)) {
            final StringBuilder sb = new StringBuilder();
            //SECUDB CHK
            //NVRECEIPT : 해당 필드 없은
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM NVRECEIPT A, NVRESENDTARGET B");
            sb.append(" WHERE A.CAMPAIGN_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.CAMPAIGN_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        }

        return "";
    }

    /**
     * SegmentVo 객체를 생성하고 데이터를 설정한다.
     *
     * @param segmentNo
     * @param segType
     * @param headSql
     * @return
     */
    private Segment makeSegmentVo(final int segmentNo, final String segType, final String headSql) {
        final Segment segmentVo = segmentDao.selectSegmentByPk(segmentNo);
        segmentVo.setSegmentSize(selectTargetCount(headSql));
        segmentVo.setSqlHead(headSql);
        segmentVo.setSqlBody(" ");
        segmentVo.setPsegmentNo(segmentNo);
        segmentVo.setSegmentNo(segmentDao.selectNextSegmentNo());
        segmentVo.setSegType(segType);
        return segmentVo;
    }

    /**
     * 대상자 SQL Count 쿼리를 실행하여 결과 반환
     *
     * @param headSql
     * @return
     */
    public int selectTargetCount(String headSql) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM (");
        sb.append(headSql);
        sb.append(") Z");

        return commonDao.selectCountBySql(sb.toString());
    }

    /**
     * 최상위 캠페인에 설정한 대상자의 Semantic Field Key 목록을 바탕으로 생성 가능한 하위 채널 목록을 반환한다.
     *
     * @param scenarioNo
     * @return
     */
    public boolean findAvailableChannels(Campaign campaign) throws Exception{
        String[] channelArr = useOmnichannel.split(",");
        String[] campaignUseList = useCampaignchannel.split(",");
        if(channelArr.length == 0) {
        	throw new Exception("옴니채널 기능을 사용 할 수 없습니다.");
        }
        List<String> keyList = semanticDao.selectCampaignSemanticKey(campaign.getCampaignNo());
        if(keyList.isEmpty()) {
        	throw new Exception("대상자 선택 후 옴니채널 등록이 가능합니다.");
        }
        if(!Arrays.asList(campaignUseList).contains(campaign.getChannelType())){
        	throw new Exception("캠페인에서 지원하지않는 채널입니다.");
        }
        String fieldKeys = makeFieldKeys(keyList);
        if(isAvailableChannel(fieldKeys, campaign.getChannelType())) {
        	return true;
        }else {
        	throw new Exception("해당 서비스는 ["+campaign.getChannelType()+"] 채널 옴니채널 설정이 불가능합니다");
        }
    }

    /**
     * 각 채널별 발송에 필요한 연락처가 Semantic Field Key에 포함되어 있는지 확인한다.
     *
     * @param semanticFieldeKeys
     * @param channel
     * @return
     */
    private boolean isAvailableChannel(String semanticFieldeKeys, String channel) {
        if(channel.equals(Const.Channel.EMAIL)) {
            if(semanticFieldeKeys.indexOf(Const.Semantic.KEY_EMAIL) > -1)
                return true;
        } else if(ChannelUtil.isUsePhoneNumber(channel)) {
            if(semanticFieldeKeys.indexOf(Const.Semantic.KEY_TELEPHONE) > -1)
                return true;
        } else if(channel.equals(Const.Channel.FAX)) {
            if(semanticFieldeKeys.indexOf(Const.Semantic.KEY_FAX) > -1)
                return true;
        }

        return false;
    }

    /**
     * Semantic Field Key 목록을 이어붙여 문자열로 반환한다.
     *
     * @param keyList
     * @return
     */
    private String makeFieldKeys(List<String> keyList) {
        StringBuilder sb = new StringBuilder();
        for(String key : keyList) {
            sb.append(key);
            sb.append(",");
        }
        return sb.toString();
    }

}