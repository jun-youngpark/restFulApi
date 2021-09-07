<%------------------------------------------------------------------
 * - [환경설정>검증데이터 생성기]검증데이터 리스트 <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvMakeDataController <br/>
 * - [환경설정>검증데이터 생성기]검증데이터 서비스 리스트<br/>
 * Title  : 테스트데이터발송 리스트
 * Description : 테스트데이터발송 리스트 화면이다.
 * Author  : 이현섭
 * update Date   : 2021-03-22
 * Version  : 1.10
 -----------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><spring:message code="env.make.data.title"/></title>
<script type="text/javascript">
	$(document).ready(function() {
		var channel = "${channel}";
		console.log("init channel = " + channel);
		selectChannel(channel);
		loadSet();
		selSendType("${sendType}");
		
		selStatus("${status}");		// 발송상태 selectBox 초기화
		
		if("${result}" != ""){
			alert("${message}");
		}
		
		var sendObjCd = '${sendObjCd}';
		if(sendObjCd == '' || sendObjCd == null) {
		      	$('input:radio[name="sendObjCd"][value="000"]').attr('checked','checked');
		}
		  
		var templeteType = '${templeteType}';
		if(templeteType == '' || templeteType == null) {
		   $('input:radio[name="templeteType"][value="D"]').attr('checked','checked');
		}
		
		// 검증데이터 생성 버튼 클릭시 이벤트
		$("#makeDataBtn").bind("click", makeData);
		
		// 발송상태 선택박스 변경시 이벤트
		$("#status").change(function(){
			selectChannel(channel);
		});
	});
	
	var loadStatus = 0;
    function loadSet() {
        loadStatus = 1;
    }
    	
    // 검색한 상태값이 있는 경우, 해당 상태값으로 자동선택
    function selStatus(status){
    	console.log('init status = ' + status);
    	$("#status").val(status).attr("selected", "selected");
    }
    
	function errorHandler(arg1,errMsg){
	    console.log("error1 : " + arg1);
	    console.log("errMsg : " + dwr.util.toDescriptiveString(errMsg, 2));
	}
	
	function selectChannel(channel){
		var param = {
			channel : channel,
			status : $("#status option:selected").val()
	    };
		
		$.get("/env/channelServiceList.json", $.param(param, true), function(result) {
			selectCallback(result);
	    });
	}
	
	// 서비스 리스트 callback 함수
	function selectCallback(data) {
		var innerHtml = "";
		
		if (data != null && data.length > 0){
	   		for(var i=0; i<data.length; i++){
				innerHtml += "<option value='" + data[i].ECARENO + "' id='" + data[i].ECARENO + "'>" + data[i].ECARENO + "&nbsp;&nbsp;(" + data[i].ECARENM + ")</option>";
	   		}
	   	}
		$("#serviceNo").html(innerHtml);
		if("${serviceNo}" != null && "${serviceNo}" != ""){
			$("#" + "${serviceNo}").attr("selected", "selected");
		}
		
		// 채널에 따른 생성기 옵션 설정
		var channel = $("#selectChannels").val();
		if(channel == "M" || channel == ""){	// 채널이 이메일 또는 전체인 경우
			$("#receiverEmailLayer").show();
			if(channel == ""){	// 전체
				$("#receiverTelLayer").show();
				$("#receiverFaxLayer").show();
			}else{		// 이메일
				$("#receiverTelLayer").hide();
				$("#receiverFaxLayer").hide();
			}
			
			// 첨부파일 선택 옵션 활성화
			$("#attachLayer").css("background-color", "#fff");
			$("#attachLayer input[name=attach]").prop("disabled", false);
		}else if(channel == "F"){	// FAX
			$("#receiverEmailLayer").hide();
			$("#receiverTelLayer").hide();
			$("#receiverFaxLayer").show();
			attachmentDisabled();
		}else{
			$("#receiverEmailLayer").hide();
			$("#receiverFaxLayer").hide();
			$("#receiverTelLayer").show();
			
			if(channel == "T"){
				// 첨부파일 선택 옵션 활성화
				$("#attachLayer").css("background-color", "#fff");
				$("#attachLayer input[name=attach]").prop("disabled", false);
			}else{
				attachmentDisabled();	// 첨부파일 선택창 비활성화
			}
		}
	}
	
	// 첨부파일 선택 옵션 비활성화	
	function attachmentDisabled(){
		$("#attachLayer").css("background-color", "#f7f7f7");
		$("#attachLayer input[name=attach]").prop("disabled", true);
		$("#attachLayer input[name=attach][value=N]").prop("checked", true);
	}
	
	function makeData(){
		if(loadStatus == 0) {
            alert('<spring:message code="common.alert.save.msg1"/>');
            return;
        }
		var receiverEmail = $("#receiverEmail").val();
		var receiverTel = $("#receiverTel").val();
		var channel = $("#selectChannels").val();
		if((receiverEmail == null || receiverEmail == "") 
				&& (channel == "M" || channel == "")){
			alert('<spring:message code="env.make.data.alert.save.msg1"/>');
			$("#receiverEmail").focus();
			return false;
		}else if((receiverTel == null || receiverTel == "") && channel != "M"){
			alert('<spring:message code="env.make.data.alert.save.msg2"/>');
			$("#receiverTel").focus();
			return false;
		}else if(channel != "M"){
			if(receiverTel.length != 10 && receiverTel.length != 11){
				alert('<spring:message code="env.make.data.alert.save.msg3"/>');
				$("#receiverTel").focus();
				return false;
			}
		}
		var serviceNoFrom = $("#serviceNoFrom").val();
		var serviceNoTo = $("#serviceNoTo").val();
		var sendType = $("input[name=sendType]:checked").val();
		var confirmMsg = "";
		
		if(sendType == "channel"){
			if((serviceNoFrom == null || serviceNoFrom == "") && serviceNoTo != null && serviceNoTo != ""){
				confirmMsg = "서비스번호 1번부터 " + serviceNoTo + "까지";
			}else if (serviceNoFrom != null && serviceNoFrom != "" && (serviceNoTo == null || serviceNoTo == "")){
				confirmMsg = "서비스번호 " + serviceNoFrom + "번 부터 모든 ";
			}else if (serviceNoFrom != null && serviceNoFrom != "" && serviceNoTo != null && serviceNoTo != ""){
				if(serviceNoFrom > serviceNoTo){
					alert('<spring:message code="env.make.data.alert.save.msg4"/>');
					return false;
				}
				confirmMsg = "서비스번호 " + serviceNoFrom + "번 부터 " + serviceNoTo + "까지 ";
			}else if ((serviceNoFrom == null || serviceNoFrom == "") && (serviceNoTo == null || serviceNoTo == "")){
				confirmMsg = "모든 서비스의 ";
			}
		}
		
		if(confirm(confirmMsg + "검증 데이터 발송 처리를 시작하시겠습니까?")){
			loadStatus = 0;
			
			// post 전송
			$("#editorFrm").submit();
		}
	}
	
	function selSendType(sendType){
		if(sendType == '' || sendType == 'service'){
			$("#seviceNoSpan2").show();
			$("#seviceNoSpan1").hide();
		}else{
			$("#seviceNoSpan2").hide();
			$("#seviceNoSpan1").show();
		}
	}
</script>
</head>

<body>
    <!--컨텐츠 영역 start-->
	<div class="main-panel">
		<div class="container-fluid mt-4 mb-2"><!-- Page content -->
        	<div class="card">
	            <div class="card-header"><!-- title -->
	                <h3 class="mb-0"><spring:message code="env.msg.makeData"/></h3>
	            </div>
	            
	            <div class="card-body gridWrap">
				    <form name="editorFrm" id="editorFrm" method="post" action="<c:url value="/env/registData.do"/>">
					    <div class="message_form">
					        <table class="table table-sm dataTable table-fixed">
						        <colgroup>
								  <col width="10%">
								  <col width="20%">
								  <col width="10%">
								  <col width="25%">
								  <col width="10%">
								  <col width="25%">
						        </colgroup>
						        <tr>
						        	<th scope="row"><spring:message code="common.menu.channel.select" /></th>
					                <td align="right" valign="top">
					                    <select name="channel" id="selectChannels" onChange="selectChannel(this.value);" class="form-control">
					                    	<option value=""><spring:message code="common.menu.all" /></option>
						                    <c:if test="${fn:contains(channelList, 'M')}">
						                        <option value="M" <c:choose><c:when test="${channel eq 'M' }">selected</c:when></c:choose>><spring:message code="common.channel.M" /></option>
						                    </c:if>
						   					<c:if test="${fn:contains(channelList, 'S')}">
						                        <option value="S" <c:choose><c:when test="${channel eq 'S' }">selected</c:when></c:choose>><spring:message code="common.channel.S" /></option>
						                    </c:if>
						                    <c:if test="${fn:contains(channelList, 'T')}">
						                        <option value="T" <c:choose><c:when test="${channel eq 'T' }">selected</c:when></c:choose>><spring:message code="common.channel.T" /></option>
						                    </c:if>
						                    <c:if test="${fn:contains(channelList, 'P')}">
						                        <option value="P" <c:choose><c:when test="${channel eq 'P' }">selected</c:when></c:choose>><spring:message code="common.channel.P" /></option>
						                    </c:if>
						                    <c:if test="${fn:contains(channelList, 'A')}">
						                        <option value="A" <c:choose><c:when test="${channel eq 'A' }">selected</c:when></c:choose>><spring:message code="common.channel.A" /></option>
						                    </c:if>
						                    <c:if test="${fn:contains(channelList, 'C')}">
						                       	<option value="C" <c:choose><c:when test="${channel eq 'C' }">selected</c:when></c:choose>><spring:message code="common.channel.C" /></option>
						                    </c:if>
						                    <c:if test="${fn:contains(channelList, 'F')}">
						                       	<option value="F" <c:choose><c:when test="${channel eq 'F' }">selected</c:when></c:choose>><spring:message code="common.channel.F" /></option>
						                    </c:if>
					                	</select>
					                </td>
					                <th scope="row"><spring:message code="env.make.data.send.method"/></th>
					                <td valign="top" id="sendTypeTd">
				                		<div class="col-md-12 custom-control-inline" style="margin-right:0px; padding-right:0px;">
						                	<div class="form-group">
							                	<div class="custom-control custom-radio custom-control-inline">
				                                    <input type="radio" name="sendType" id="sendType_channel" value="channel" class="custom-control-input" <c:if test="${sendType eq 'channel' }"> checked="checked"</c:if> onchange="selSendType(this.value)">
				                                    <label class="custom-control-label" for="sendType_channel"><spring:message code="env.make.data.send.method.channel"/></label>
				                                </div>
			                                    <div class="custom-control custom-radio custom-control-inline">
				                                    <input type="radio" name="sendType" id="sendType_service" value="service" class="custom-control-input" <c:if test="${sendType eq '' or sendType eq 'service'}"> checked="checked"</c:if> onchange="selSendType(this.value)">
				                                    <label class="custom-control-label" for="sendType_service"><spring:message code="env.make.data.send.method.service"/></label>
				                                </div>
						             	    </div>
					             	    </div>
					                </td>
					                <th scope="row"><spring:message code="env.make.data.send.status"/></th>
					                <td valign="top">
					                	<select name="status" id="status" class="form-control">
					                		<option value=""><spring:message code="env.make.data.send.status.total"/></option>
					                		<option value="W"><spring:message code="env.make.data.send.status.wait"/></option>
					                		<option value="R"><spring:message code="env.make.data.send.status.run"/></option>
					                	</select>
					                </td>
					           	</tr>
					           	<tr>
					           		<th width="5%" scope="row"><spring:message code="env.make.data.send.cnt"/></th>
					                <td align="left" valign="top">
					                    <select class="form-control form-control-sm col-6" name="sendCnt">
					                    	<option value="1" <c:choose><c:when test="${sendCnt eq '1' }">selected</c:when></c:choose>>1</option>
					                    	<option value="10" <c:choose><c:when test="${sendCnt eq '10' }">selected</c:when></c:choose>>10</option>
					                    	<option value="20" <c:choose><c:when test="${sendCnt eq '20' }">selected</c:when></c:choose>>20</option>
					                    	<option value="30" <c:choose><c:when test="${sendCnt eq '30' }">selected</c:when></c:choose>>30</option>
					                    	<option value="40" <c:choose><c:when test="${sendCnt eq '40' }">selected</c:when></c:choose>>40</option>
					                    	<option value="50" <c:choose><c:when test="${sendCnt eq '50' }">selected</c:when></c:choose>>50</option>
					                    	<option value="60" <c:choose><c:when test="${sendCnt eq '60' }">selected</c:when></c:choose>>60</option>
					                    	<option value="70" <c:choose><c:when test="${sendCnt eq '70' }">selected</c:when></c:choose>>70</option>
					                    	<option value="80" <c:choose><c:when test="${sendCnt eq '80' }">selected</c:when></c:choose>>80</option>
					                    	<option value="90" <c:choose><c:when test="${sendCnt eq '90' }">selected</c:when></c:choose>>90</option>
					                    	<option value="100" <c:choose><c:when test="${sendCnt eq '100' }">selected</c:when></c:choose>>100</option>
					                    	<option value="1000" <c:choose><c:when test="${sendCnt eq '1000' }">selected</c:when></c:choose>>1000</option>
					                    	<option value="10000" <c:choose><c:when test="${sendCnt eq '10000' }">selected</c:when></c:choose>>1만</option>
					                    	<option value="100000" <c:choose><c:when test="${sendCnt eq '100000' }">selected</c:when></c:choose>>10만</option>
						                </select>
					                </td>
					                <th scope="row"><spring:message code="env.make.data.service.no"/></th>
					                <td>
					                	<div class="form-group custom-control-inline" style="display:none;margin:0px;" id="seviceNoSpan1">
					                   	 	<input type="text" name="serviceNoFrom" id="serviceNoFrom" value="${serviceNoFrom }" class="form-control form-control-sm" maxlength="5" style="width:80px;"/>&nbsp;~&nbsp; 
					                  	  	<input type="text" name="serviceNoTo" id="serviceNoTo" value="${serviceNoTo }" class="form-control form-control-sm" maxlength="5" style="width:80px;"/>
							            </div>
						                <div class="form-group" style="display:none" id="seviceNoSpan2">
						                    <select class="form-control form-control-sm" name="serviceNo" id="serviceNo"></select>
						                </div>
					                </td>
					                <th scope="row"><spring:message code="env.make.data.attachment.use"/></th>
					                <td valign="top" id="attachLayer">
					                	<div class="col-md-12 custom-control-inline">
						                	<div class="form-group">
							                	<div class="custom-control custom-radio custom-control-inline">
				                                    <input type="radio" name="attach" id="attach_N" value="N" class="custom-control-input" <c:if test="${attach eq '' or attach eq 'N' }"> checked="checked"</c:if>>
				                                    <label class="custom-control-label" for="attach_N"><spring:message code="env.make.data.attachment.use.N"/></label>
				                                </div>
			                                    <div class="custom-control custom-radio custom-control-inline">
				                                    <input type="radio" name="attach" id="attach_Y" value="Y" class="custom-control-input" <c:if test="${attach eq 'Y'}"> checked="checked"</c:if>>
				                                    <label class="custom-control-label" for="attach_Y"><spring:message code="env.make.data.attachment.use.Y"/></label>
				                                </div>
						             	    </div>
						             	</div>
					                </td>
					            </tr>
					           	<tr id="receiverTelLayer">
					           		<th scope="row"><spring:message code="env.make.data.receiver.tel"/></th>
					                <td align="left" valign="top" colspan="5">
					                    <input type="text" name="receiverTel" id="receiverTel" value="${receiverTel}" class="form-control form-control-sm col-3"  maxlength="11"/>
					                </td>
					            </tr>
					            <tr id="receiverEmailLayer" style="display:none;">
					           		<th scope="row"><spring:message code="env.make.data.receiver.email"/></th>
					                <td align="left" valign="top" colspan="5">
					                    <input type="text" name="receiverEmail" id="receiverEmail" value="${receiverEmail}" class="form-control form-control-sm col-3"  maxlength="50"/>
					                </td>
					            </tr>
					            <tr id="receiverFaxLayer" style="display:none;">
					           		<th scope="row"><spring:message code="env.make.data.receiver.fax"/></th>
					                <td align="left" valign="top" colspan="5">
					                    <input type="text" name="receiverFax" id="receiverFax" value="${receiverFax}" class="form-control form-control-sm col-3"  maxlength="50"/>
					                </td>
					            </tr>
					        </table>
					    </div>
					    <div class="col offset-sm-4 col-4 text-center" style="margin-top:10px;">
					    	<c:if test="${sessionScope.write eq 'W'}">
								<button class="btn btn-sm btn-outline-primary btn-round" id="makeDataBtn" type="button">
									<spring:message code="env.make.data.button.name"/>
                   				</button>
							</c:if>
					    </div>
				    </form>
				</div>
			</div>
		</div>
	</div>
<br/><br/>

<%-- placeholder 가 지원되지 않는 브라우저까지 처리를 위해 플러그인 사용. --%>
<script type="text/javascript" src="<c:url value="/js/ajax/plugin/jquery.placeholder-1.8.7.js" />"></script>
</body>
</html>