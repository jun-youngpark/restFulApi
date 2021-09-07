package com.mnwise.wiseu.web.rest.dao.template;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

@Repository
public class TemplateDao extends BaseDao {

	/**
	 * 템플릿 등록(캠페인)
	 */
    public int insertEmTemplate(Campaign campaign) throws Exception {
		return insert("Template.insertEmTemplate", campaign);
    }

    /**
	 * 템플릿 복사(캠페인)
	 */
    public int copyEmTemplate(Campaign campaign) throws Exception {
		return insert("Template.copyEmTemplate", campaign);
    }

    /**
	 * 템플릿 수정(캠페인)
	 */
    public int updateEmTemplate(Campaign campaign) throws Exception {
		return update("Template.updateEmTemplate", campaign);
    }

    /**
	 * 템플릿 등록(이케어)
	 */
    public int insertEcTemplate(Ecare ecare) throws Exception {
		return insert("Template.insertEcTemplate", ecare);
    }
    /**
	 * 템플릿 복사(이케어)
	 */
    public int copyEcTemplate(Ecare ecare) throws Exception {
		return insert("Template.copyEcTemplate", ecare);
    }

    /**
	 * 템플릿 수정(이케어)
	 */
	public int updateEcTemplate(Ecare ecare) {
		return update("Template.updateEcTemplate", ecare);
	}

}