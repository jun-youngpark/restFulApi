package com.mnwise.wiseu.web.rest.parent;

import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.dto.ResultDto;

import org.json.simple.JSONObject;

public class BaseController  {

	public static Object getSuccessResult(JSONObject data) {
		return new ResultDto<>().setCode(Const.ResultCode.SUCCESS)
				.setMessage("success")
				.setData(data);
	}
	 public static Object getFailResult(String errMsg, JSONObject data) {
		 return new ResultDto<>().setCode(Const.ResultCode.FAIL)
				 .setMessage(errMsg)
				 .setData(data);
	 }

	 public static Object getRestExceptionResult(String msg, JSONObject data) {
		 return new ResultDto<>().setCode(Const.ResultCode.FAIL)
				 .setMessage(msg)
				 .setData(data);
	 }

	 public static Object getExceptionResult(String msg, JSONObject data) {
		 return new ResultDto<>().setCode(Const.ResultCode.FAIL)
				 .setMessage(msg + " 처리 중 에러가 발생하였습니다.")
				 .setData(data);
	 }

	 public static Object getNoUseResult() {
		 return new ResultDto<>().setCode(Const.ResultCode.FAIL)
				 .setMessage("해당 API는 사용 중지 중입니다.")
				 .setData(new JSONObject());
	 }
	 public static Object getSuccessResultJson(JSONObject data) {
	     return new ResultDto<>().setCode(Const.ResultCode.SUCCESS)
				.setMessage("success")
				.setData(data);
	 }
}
