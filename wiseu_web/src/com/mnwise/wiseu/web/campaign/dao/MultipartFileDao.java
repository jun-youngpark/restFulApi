package com.mnwise.wiseu.web.campaign.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NVMULTIPARTFILE 테이블 DAO 클래스
 */
@Repository
public class MultipartFileDao extends BaseDao {
    public int insertMultipartFile(MultipartFileVo multipartFile) {
        return insert("MultipartFile.insertMultipartFile", multipartFile);
    }

    public int copyMultipartFile(CampaignVo campaignVo) {
        return insert("MultipartFile.copyMultipartFile", campaignVo);
    }

    public int deleteMultipartFileAll(ScenarioVo scenarioVo) {
        return delete("MultipartFile.deleteMultipartFileAll", scenarioVo);
    }

    public int deleteEditorCampaignMultipartFile(MultipartFileVo multipartFile) {
        return delete("MultipartFile.deleteEditorCampaignMultipartFile", multipartFile);
    }

    public MultipartFileVo selectEditorCampaignMultipartFileOne(MultipartFileVo multipartFile) {
        return (MultipartFileVo) selectOne("MultipartFile.selectEditorCampaignMultipartFileOne", multipartFile);
    }

    @SuppressWarnings("unchecked")
    public List<MultipartFileVo> selectEditorCampaignMultipartFile(int campaignNo) {
        return selectList("MultipartFile.selectEditorCampaignMultipartFile", campaignNo);
    }

    public int selectEditorCampaignMultipartfileMax() {
        return (Integer) selectOne("MultipartFile.selectEditorCampaignMultipartFileMax");
    }

}