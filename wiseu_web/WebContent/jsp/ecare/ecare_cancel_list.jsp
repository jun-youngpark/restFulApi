<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 취소이력] 이케어 취소이력
 * - URL : /ecare/cancel_list.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcareCancelListController
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<html>
<head>
<title><spring:message code="ecare.msg.history_${webExecMode}" /></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script type="text/javascript">

$(document).ready(function(){
	$("#searchBtn").bind("click", search);
	//initSearchData();	// 검색어가 존재하는 경우 UI 초기설정
	
	$.get("/ecare/selectEcareList.json", $.param({}, true), function(result) {
		selectEcareCallback(result);
    });
});

// 이케어번호 목록 셋팅
function selectEcareCallback(data){
	$("#ecareNo").html("<option value='0'>전체</option>");
	$.each(data, function(index, ecareVo){
		var opt = "";
		if(Number("${ecareNo}") == Number(ecareVo.ecareNo)){
			opt = "<option value='" + ecareVo.ecareNo + "' selected>";
		}else{
			opt = "<option value='" + ecareVo.ecareNo + "'>";
		}
		opt += ecareVo.ecareNo + ": " + ecareVo.ecareNm;
		opt += "</option>";
		$("#ecareNo").append(opt);	
	});
}

function search(){
	$("#cancelFrm").submit();
}

// 검색어가 존재하는 경우 UI 초기설정
function initSearchData(){
	$form.optionSelect(document.cancelFrm.ecareNo, "${ecareNo}");
	$form.optionSelect(document.cancelFrm.canFg, "${canFg}");
	$form.optionSelect(document.cancelFrm.rsltCd, "${rsltCd}");
}


// 취소이력에서 row를 선택할 때 하단 정보(발송요청/취소이력) 출력
function printDetails(seq, reqDt, ecareNo){
	$("#interfaceLayer").show();
	var param = {
		seq : seq,
		reqDt : reqDt,
		ecareNo : ecareNo
	};
	
	// 발송요청 정보 조회
	$.get("/ecare/selectInterface.json", $.param(param, true), function(result) {
		$(".result").hide();
		if(result.length == 0){	// 발송요청건이 없을 경우
			$("#noneData").show();
		}else{	// 발송요청건이 1건이상 존재
			$("#interfaceLayer").show();
			printInterface(result);
		}
    });
}

// 발송로그 검색
function selectSendLog(ecareNo, resultSeq, listSeq, customerKey){
	var param = {
		ecareNo : ecareNo,
		resultSeq : resultSeq,
		listSeq : listSeq,
		customerKey : customerKey
	};
	
	$.get("/ecare/selectSendLog.json", $.param(param, true), function(result) {
		if(result == null || listSeq == null){
			$("#sendLogLayer").hide();
			$("#noneSendLog").show();
		}else{
			$("#sendLogLayer").show();
			$("#noneSendLog").hide();
			printSendLog(result);
		}
    });
}

// 발송요청 정보 조회내용 출력
function printInterface(data){
	$("#interfaceList tbody").html("");
	
	$.each(data, function(index, target){
		var ecareNo = Number(target.ecareNo);
		var resultSeq = target.resultSeq;
		var listSeq = target.listSeq;
		var customerKey = target.receiverId;
		var reservedDate = target.reservedDate;
		console.log("reserved.date = " + reservedDate);
		if($.trim(reservedDate) != ""){
			reservedDate = reservedDate.substring(0,8) + "<br/>" + reservedDate.substring(8);
		}
		
		var tag = "<tr>";
		tag += "<td>" + target.seq + "</td>";
		tag += "<td>" + target.reqDt + "</td>";
		tag += "<td>" + target.reqTm + "</td>";
		tag += "<td>" + target.ecareNo + "</td>";
		tag += "<td>" + reservedDate + "</td>";
		tag += "<td>" + target.channel + "</td>";
		tag += "<td>" + target.receiverId + "</td>";
		tag += "<td>" + target.receiverNm + "</td>";
		tag += "<td>" + target.receiver + "</td>";
		tag += "<td>" + target.sendFg + "</td>";
		tag += "<td>" + target.errorMsg + "</td>";
		tag += "</tr>";
		$("#interfaceList tbody").append(tag);
		
		selectSendLog(ecareNo, resultSeq, listSeq, customerKey);	
		
	});
}

// 발송이력 정보 조회내용 출력
function printSendLog(sendLog){
	$("#sendLog tbody").html("");
	
	var tag = "<tr>";
	tag += "<td>" + $string.getDateFormat(sendLog.sendDt) + "</td>";
	tag += "<td>" + sendLog.errorCd + "</td>";
	tag += "<td>" + sendLog.errMsg + "</td>";
	tag += "<td>" + (sendLog.openDt == null ? "N" : "Y") + "</td>";
	tag += "</tr>";
	$("#sendLog tbody").append(tag);	
}
</script>
<style type="text/css">
	.result{
		display: none;
	}
	#noneSendLog{
		margin-bottom:20px;
	}
	
	#cancelList tbody tr{
		cursor: pointer;
	}
</style>
</head>

<body>
<form id="cancelFrm" name="cancelFrm" action="/ecare/cancel_list.do" method="get">
	<div class="main-panel">
	    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
	        <div class="card mb-0">
	            <div class="card-header"><!-- title -->
	                <h3 class="mb-0"><spring:message code="ecare.msg.cancel_list"/></h3>
	            </div>
	            <div class="table-responsive gridWrap">
	                <table class="table table-sm2 dataTable table-fixed" style="width:100%">
	                    <colgroup>
	                        <col width="14%" />
	                        <col width="20%" />
	                        <col width="14%" />
	                        <col width="14%" />
	                        <col width="14%" />
	                        <col width="14%" />
	                        <col width="10%" />
	                    </colgroup>
	                    <tbody>
		                    <tr>
		                        <th scope="row">취소요청일자</th><!-- 취소요청일자 -->
		                        <td>
		                        	<div class="input_datebox">
		                        		<fmt:parseDate var="searchQstartDt" pattern="yyyyMMdd" value="${searchQstartDt}" />
		                				<fmt:formatDate var="searchQstartDt" value="${searchQstartDt }" pattern="yyyy-MM-dd"/>
	                                    <input type="text" id="startDate" name="searchQstartDt" value="${searchQstartDt}" class="form-control form-control-sm datepicker" />
	                                </div>
	                                <span class="txt">~</span>
	                                <div class="input_datebox">
	                                	<fmt:parseDate var="searchQendDt" pattern="yyyyMMdd" value="${searchQendDt}" />
		                				<fmt:formatDate var="searchQendDt" value="${searchQendDt }" pattern="yyyy-MM-dd"/>
	                                    <input type="text" id="endDate" name="searchQendDt" value="${searchQendDt}" class="form-control form-control-sm datepicker" />
	                                </div>
		                        </td>
		                        <th scope="row">취소접수상태</th><!-- 취소접수상태 -->
		                        <td>
		                        	<select name="canFg" id="canFg" class="form-control form-control-sm">
		                        		<option value="">전체</option>
		                        		<option value="R">접수대기 중</option>
		                        		<option value="H">전처리 중</option>
		                        		<option value="E">취소접수 완료</option>
		                        		<option value="O">처리 중 실패</option>
		                        	</select>
		                        </td>
		                        <th scope="row">취소결과</th><!-- 취소결과 -->
		                        <td>
		                        	<select name="rsltCd" id="rsltCd" class="form-control form-control-sm">
		                        		<option value="">전체</option>
		                        		<c:forEach var="cdVo" items="${rsltCdList }">
		                        			<option value="${cdVo.cd }">${cdVo.cdDesc }</option>
		                        		</c:forEach>
		                        	</select>
		                        </td>
		                        <td rowspan="2"> <!-- 검색버튼 -->
		                        	<button class="btn btn-outline-info btn_search" id="searchBtn">
		                                <spring:message code="button.search"/><!-- 검색 -->
		                            </button>
		                        </td>
		                   	</tr>
		                   	<tr>
		                        <th scope="row">이케어번호</th><!-- 이케어번호 -->
		                        <td>
		                        	<select name="ecareNo" id="ecareNo" class="form-control form-control-sm">
		                        		<option value="0">전체</option>
		                        	</select>
		                        </td>
		                        <th scope="row">원 SEQ</th><!-- 대상자 일련번호 -->
		                        <td colspan="3">
		                        	<input type="text" name="seq" id="seq" value="${seq }" class="form-control form-control-sm" />
		                        </td>
		                   	</tr>
		                </tbody>
	                </table>
	        	</div>
	        	
	        	<!-- 발송취소 이력 -->
	        	<div class="table-responsive">
	                <table class="table table-sm2 dataTable table-hover table-fixed" id="cancelList">
	                	<colgroup>
	                        <col width="9%" />
	                        <col width="7%" />
	                        <col width="12%" />
	                        <col width="8%" />
	                        <col width="7%" />
	                        <col width="7%" />
	                        <col width="7%" />
	                        <col width="7%" />
	                        <col width="8%" />
	                        <col width="28%" />
	                    </colgroup>
	                    <thead class="thead-light">
		                    <tr>
		                        <th>번호</th>						<!-- 번호 -->
		                        <th scope="col">취소<br/>요청일시</th>	<!-- 취소요청일시 -->
		                        <th scope="col">원 SEQ</th>		<!-- 원 SEQ -->
		                        <th scope="col">원 요청일자</th>	<!-- 원 요청일자 -->
		                        <th scope="col">취소접수<br/>상태</th>	<!-- 취소접수 상태 -->
		                        <th scope="col">이케어 번호</th>	<!-- 이케어 번호 -->
		                        <th scope="col">취소<br/>접수일시</th>	<!-- 취소 접수일시 -->
		                        <th scope="col">취소<br/>처리일시</th>	<!-- 취소 처리일시 -->
		                        <th scope="col">취소결과</th>		<!-- 취소결과 -->
		                        <th scope="col">취소결과 메시지</th> <!-- 취소결과 메시지 -->
		                    </tr>
		                </thead>
		                <tbody>
		                	<c:forEach items="${cancelList }" var="cancel">
		                		<tr onclick="printDetails('${cancel.orgSeq}', '${cancel.orgReqDt}', '${cancel.ecareNo }')">
		                			<fmt:parseDate var="canReqDtm" pattern="yyyyMMddHHmmss" value="${cancel.canReqDtm }" />
		                			<fmt:formatDate var="canReqDtm" value="${canReqDtm }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                			<fmt:parseDate var="orgReqDt" pattern="yyyyMMdd" value="${cancel.orgReqDt }" />
		                			<fmt:formatDate var="orgReqDt" value="${orgReqDt }" pattern="yyyy-MM-dd"/>
		                			<fmt:parseDate var="acceptDtm" pattern="yyyyMMddHHmmss" value="${cancel.acceptDtm }" />
		                			<fmt:formatDate var="acceptDtm" value="${acceptDtm }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                			<fmt:parseDate var="canDtm" pattern="yyyyMMddHHmmss" value="${cancel.canDtm }" />
		                			<fmt:formatDate var="canDtm" value="${canDtm }" pattern="yyyy-MM-dd HH:mm:ss"/>
		                			
			                		<td>${cancel.canSeq }</td>		   <!-- 번호 -->
			                		<td>${canReqDtm }</td>	   <!-- 취소요청일시 -->
			                		<td>${cancel.orgSeq }</td>		   <!-- 원 SEQ -->
			                		<td>${orgReqDt }</td>	   <!-- 원 요청일자 -->
			                		<td>   <!-- 취소접수 상태 -->
			                			<c:choose>
			                				<c:when test="${cancel.canFg == 'R'}">
			                					접수대기 중
			                				</c:when>
			                				<c:when test="${cancel.canFg == 'H'}">
			                					전처리 중
			                				</c:when>
			                				<c:when test="${cancel.canFg == 'E'}">
			                					취소접수 완료
			                				</c:when>
			                				<c:otherwise>
			                					처리 중 실패
			                				</c:otherwise>
			                			</c:choose>
			                		</td>
			                		<td>${cancel.ecareNo }</td>	  	   <!-- 이케어 번호 -->
			                		<td>${acceptDtm }</td>	   		   <!-- 취소 접수일시 -->
			                		<td>${canDtm }</td>	   	   <!-- 취소 처리일시 -->
			                		<td>${cancel.rsltCd }</td>	   	   <!-- 취소결과 -->
			                		<td>${cancel.rsltMsg }</td>	  	   <!-- 취소결과 메시지 -->
			                	</tr>	
		                	</c:forEach>
		                </tbody>
	                </table>
	            </div>
	            <!-- 페이징 -->
				${paging}
				
				<div id="interfaceLayer" class="result">
					<!-- 발송요청 -->
					<h3 class="mb-0">발송요청</h3>
					<div class="table-responsive">
						<table class="table table-sm2 dataTable table-hover table-fixed" id="interfaceList">
							<colgroup>
								<col width="11%" />
								<col width="7%" />
								<col width="6%" />
								<col width="7%" />
								<col width="8%" />
								<col width="7%" />
								<col width="9%" />
								<col width="8%" />
								<col width="11%" />
								<col width="6%" />
								<col width="20%" />
							</colgroup>
							<thead class="thead-light">
			                    <tr>
			                        <th>SEQ</th>				<!-- SEQ -->
			                        <th>요청일자</th>				<!-- 요청일자 -->
			                        <th>요청일시</th>				<!-- 요청일시 -->
			                        <th>이케어<br/>번호</th>			<!-- 이케어 번호 -->
			                        <th>발송요청<br/>예약일시</th>	<!-- 발송요청 예약일시 -->
			                        <th>발송채널</th>				<!-- 발송채널 -->
			                        <th>수신자 ID</th>			<!-- 수신자 ID -->
			                        <th>수신자<br/>이름</th>			<!-- 수신자 이름 -->
			                        <th>수신자<br/>이메일/번호</th>	<!-- 수신자 이메일/번호 -->
			                        <th>발송상태</th>				<!-- 발송상태 -->
			                        <th>처리결과 메시지</th>			<!-- 처리결과 메시지 -->
		                        </tr>
		                    </thead>
		                    <tbody>
		                    </tbody>
						</table>
					</div>
				</div>

				<div class="col-auto alert alert-warning mb-0 result" role="alert" id="noneData">
                    <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                    <span class="alert-text">발송요청건이 존재하지 않습니다.</span>
                </div>
				
				<div id="sendLogLayer" class="result">
					<!-- 발송이력 -->
					<h3 class="mb-0">발송이력</h3>
					<div class="table-responsive">
						<table class="table table-sm2 dataTable table-hover table-fixed" id="sendLog">
							<colgroup>
								<col width="35%" />
								<col width="20%" />
								<col width="25%" />
								<col width="20%" />
							</colgroup>
							<thead class="thead-light">
								<tr>
									<th>발송일시</th>			<!-- 발송일시 -->
									<th>발송결과</th>			<!-- 발송결과 -->
									<th>발송결과 메시지</th>		<!-- 발송결과 메시지 -->
									<th>수신확인</th>			<!-- 수신확인 -->
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
				
				<div class="col-auto alert alert-warning mb-0 result" role="alert" id="noneSendLog">
                    <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                    <span class="alert-text">발송이력이 존재하지 않습니다.</span>
                </div>
	        </div><!-- e.card -->
	    </div><!-- e.page content -->
	</div><!-- e.main-panel -->
	</form>
</body>
</html>
