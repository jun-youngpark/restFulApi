package com.mnwise.wiseu.web.rest.web.v1;

import org.json.simple.JSONObject;
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
import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare.createEcOmni;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare.updateEcState;
import com.mnwise.wiseu.web.rest.parent.BaseController;
import com.mnwise.wiseu.web.rest.service.EnvService;
import com.mnwise.wiseu.web.rest.service.ecare.EcareService;
@RestController
@RequestMapping("/api/v1/ecare/")
@CheckFunction(prefix = "api.v1.ecare")
public class EcareController extends BaseController {

	@Autowired
	private EcareService ecareService;
	@Autowired
	private EnvService envSerivce;

	/**
	 * 이케어 생성
	 */
	@RequestMapping(value = "/create" , method={RequestMethod.GET,RequestMethod.POST} )
	@LoggingApi(funcNm = "이케어 생성")
	public ResponseEntity<?> create(@RequestBody @Validated(create.class) final Ecare ecare) throws Exception  {
		String type = Const.SubType.SCHDULE_M.equals(ecare.getSubType())?Const.SubType.SCHDULE:ecare.getSubType();
		ecare.setDefaultHandler(envSerivce.selectDefaultHandler(ecare.getChannelType(),type, "N", ecare.getMailType()));
		JSONObject data = ecareService.create(ecare);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }


	/**
	 * 이케어 옴니 생성
	 */
	@RequestMapping(value = "/createOmni" , method={RequestMethod.GET,RequestMethod.POST} )
	@LoggingApi(funcNm = "이케어 옴니 생성")
	public ResponseEntity<?> createOmni(@RequestBody @Validated(createEcOmni.class) final Ecare ecare) throws Exception{
		ecare.setDefaultHandler(envSerivce.selectDefaultHandler(ecare.getChannelType(),"S", "N", "D"));
		JSONObject data = ecareService.createOmni(ecare);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

	/**
	 * 이케어 수정
	 */
    @RequestMapping(value = "/update" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "이케어 수정")
    public ResponseEntity<?> update(@RequestBody @Validated(update.class) final Ecare ecare) throws Exception{
    	JSONObject data = ecareService.update(ecare);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

    /**
	 * 이케어 삭제
	 */
    @RequestMapping(value = "/delete" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "이케어 삭제")
    public ResponseEntity<?> delete(@RequestBody @Validated(delete.class) final Ecare ecare) throws Exception{
    	JSONObject	data = ecareService.delete(ecare);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }


    /**
	 * 이케어 리스트 조회
	 */
    @RequestMapping(value = "/list" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "이케어 리스트 조회")
    public ResponseEntity<?> list(@RequestBody @Validated(list.class) final Ecare ecare) throws Exception{
		JSONObject data = ecareService.list(ecare);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

    /**
     * 이케어 상태 변경 수정
     */
    @RequestMapping(value = "/updateState" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "이케어 상태 변경 수정")
    public ResponseEntity<?> updateState(@RequestBody @Validated(updateEcState.class) final Ecare ecare) throws Exception{
		JSONObject data = ecareService.updateState(ecare);
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }

    /**
   	 * 이케어 리스트 조회
   	 */
    @RequestMapping(value = "/get" , method={RequestMethod.GET,RequestMethod.POST} )
    @LoggingApi(funcNm = "이케어 단건 조회")
    public ResponseEntity<?> get(@RequestBody @Validated(get.class) final Ecare ecare) throws Exception{
    	JSONObject data = ecareService.get(ecare);
    	data.put("omniList", ecareService.getOmniList(ecare));
		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    }


}
