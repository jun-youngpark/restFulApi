package com.mnwise.wiseu.web.rest.dao.handler;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

@Repository
public class HandlerDao extends BaseDao {

	/**
	 * 핸들러 테이블 적재(캠페인)
	 */
    public int insertEmHandler(Campaign campaign) throws Exception {
		return insert("Handler.insertEmHandler", campaign);
    }
    /**
     * 핸들러 테이블 적재(캠페인)
     */
    public int insertOmniEmHandler(Campaign campaign) throws Exception {
    	return insert("Handler.insertOmniEmHandler", campaign);
    }


    /**
	 * 핸들러 테이블 적재(이케어)
	 */
    public int insertEcHandler(Ecare ecare) throws Exception {
		return insert("Handler.insertEcHandler", ecare);
    }
    /**
     * 핸들러 옴니 적재(이케어)
     */
    public int insertOmniEcHandler(Ecare ecare) throws Exception {
    	return insert("Handler.insertOmniEcHandler", ecare);
    }

}