package com.mnwise.wiseu.web.rest.dao.campaign;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.dto.ReturnDto.CampaignDto;
import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

@Repository
public class CampaignDao extends BaseDao {

	/**
	 * 캠페인 생성(캠페인 번호 자동 생성)
	 */
    public int insertCampaignForFirst(Campaign campaign) throws SQLException {
		return insert("Campaign.insertCampaignForFirst", campaign);
    }
    /**
     * 캠페인 생성
     */
    public int insertCampaign(Campaign campaign) throws SQLException {
    	return insert("Campaign.insertCampaign", campaign);
    }
    /**
     * 캠페인 수정
     */
	public int updateCampaign(Campaign campaign) throws SQLException{
		return update("Campaign.updateCampaign", campaign);

	}
	 /**
     * 캠페인 삭제
     */
	public int deleteCampaign(Campaign campaign) throws SQLException{
		return delete("Campaign.deleteCampaign", campaign);

	}
	 /**
     * 캠페인  조회
     */
	public CampaignDto selectCampaign(Campaign campaign) throws SQLException{
		return (CampaignDto) selectOne("Campaign.selectCampaign", campaign);
	}
	/**
     * 캠페인 옴니 조회
     */
	public List<CampaignDto> findOmniCampaign(Campaign campaign) {
		return (List<CampaignDto>) selectList("Campaign.findOmniCampaign", campaign);
	}

	/**
     * 캠페인 리스트 수
     */
	public int selectCampaignListCount(Campaign campaign) throws SQLException{
		return (int)selectOne("Campaign.selectCampaignListCount", campaign);
	}
	/**
     * 캠페인 리스트 조회
     */
	public List<CampaignDto> selectCampaignList(Campaign campaign) throws SQLException{
		return (List<CampaignDto>) selectList("Campaign.selectCampaignList", campaign);
	}

	/**
     * 캠페인  상태변경
     */
	public int updateState(Campaign campaign) throws SQLException{
		return update("Campaign.updateState", campaign);
	}
	/**
     * 캠페인  옴니 복사
     */
	public int copyCampaignForResend(Campaign campaign) {
        return insert("Campaign.copyCampaignForResend", campaign);
    }

	 /**
     * 새로운 캠페인 번호를 가져온다.
     *
     * @return 새로운 캠페인 번호(MAX + 1)
     */
    public int selectNewCampaignNo() {
        return (Integer) selectOne("Campaign.selectNextCampaignNo");
    }
	public int getEmOmniCount(Campaign campaign) {
		 return (Integer) selectOne("Campaign.getEmOmniCount", campaign);
	}


}