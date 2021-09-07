package com.mnwise.wiseu.web.rest.web.v1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mnwise.wiseu.web.rest.annotation.CheckFunction;
import com.mnwise.wiseu.web.rest.annotation.LoggingApi;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.model.Groups.create;
import com.mnwise.wiseu.web.rest.model.Groups.delete;
import com.mnwise.wiseu.web.rest.model.Groups.get;
import com.mnwise.wiseu.web.rest.model.Groups.list;
import com.mnwise.wiseu.web.rest.model.Groups.update;
import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.model.campaign.Campaign.updateState;
import com.mnwise.wiseu.web.rest.parent.BaseController;
import com.mnwise.wiseu.web.rest.service.EnvService;
import com.mnwise.wiseu.web.rest.service.campaign.CampaignService;

import org.json.simple.JSONObject;

@RestController
@RequestMapping("/api/v1/campaign/")
@CheckFunction(prefix = "api.v1.campaign")
public class CampaignController extends BaseController {

    @Autowired private CampaignService campaignService;
    @Autowired private EnvService envSerivce;


	/**
	 * 캠페인 생성
	 */
    @RequestMapping(value = "/create" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "캠페인 생성")
    public ResponseEntity<?> create(@RequestBody @Validated(create.class) final Campaign campaign) throws Exception{
		String abTestYn = "S".equals(campaign.getAbTestType()) || "T".equals(campaign.getAbTestType()) ? "Y" : "N" ;
		campaign.setDefaultHandler(envSerivce.selectDefaultHandler(campaign.getChannelType(), Const.EM, abTestYn,"D"));
		JSONObject data	= campaignService.create(campaign);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }


    /**
	 * 캠페인 옴니채널 생성
	 */
    @RequestMapping(value = "/createOmni" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "캠페인 옴니채널 생성")
    public ResponseEntity<?> createOmni(@RequestBody @Validated(Campaign.createOmni.class) final Campaign campaign) throws Exception {
		campaignService.findAvailableChannels(campaign);
		campaign.setDefaultHandler(envSerivce.selectDefaultHandler(campaign.getChannelType(), Const.EM, "N" ,"D"));
		JSONObject data = campaignService.createOmni(campaign);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

	/**
	 * 캠페인 수정
	 */
    @RequestMapping(value = "/update" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "캠페인 수정")
    public ResponseEntity<?> update(@RequestBody @Validated(update.class) final Campaign campaign) throws Exception{
		JSONObject data = campaignService.update(campaign);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }
    /**
     * 캠페인 상태 변경 수정
     */
    @RequestMapping(value = "/updateState" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "캠페인 상태 변경 수정")
    public ResponseEntity<?> updateState(@RequestBody @Validated(updateState.class) final Campaign campaign) throws Exception {
    	JSONObject data	= campaignService.updateState(campaign);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

    /**
	 * 캠페인 삭제
	 */
    @RequestMapping(value = "/delete" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "캠페인 삭제")
    public ResponseEntity<?> delete(@RequestBody @Validated(delete.class) final Campaign campaign) throws Exception{
		JSONObject data = campaignService.delete(campaign);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

    /**
	 * 캠페인 리스트 조회
	 */
    @RequestMapping(value = "/list" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "캠페인 리스트 조회")
    public ResponseEntity<?> list(@RequestBody @Validated(list.class) final Campaign campaign) throws Exception{
		JSONObject data = campaignService.list(campaign);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

    /**
   	 * 캠페인 단건 조회
   	 */
	@RequestMapping(value = "/get" , method={RequestMethod.GET,RequestMethod.POST} )
	@LoggingApi(funcNm = "캠페인 단건 조회")
	public ResponseEntity<?> get(@RequestBody @Validated(get.class) final Campaign campaign) throws Exception{
		JSONObject data = campaignService.get(campaign);
		data.put("omniList", campaignService.getOmniList(campaign));
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
	}

}
