<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 취소요청] 이케어 취소요청
 * - URL : /ecare/cancel_form.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcareCancelFormController
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<html>
<head>
<title><spring:message code="ecare.msg.history_${webExecMode}" /></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script type="text/javascript">
$(document).ready(function() {
	$.get("/ecare/selectEcareList.json", $.param({}, true), function(result) {
		selectCallback(result);
    });
	$("#cancelBtn").bind("click", cancel);
});

function selectCallback(data){
	$("#ecareNo").html("");
	$.each(data, function(index, ecareVo){
		var opt = "<option value='" + ecareVo.ecareNo + "'>";
		opt += ecareVo.ecareNo + ": " + ecareVo.ecareNm;
		opt += "</option>";
		$("#ecareNo").append(opt);	
	});
}

</script>
</head>

<body>
<form id="cancelFrm" name="cancelFrm" action="/ecare/cancel_form.do" method="post">
	<div class="main-panel">
	    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
	        <div class="card mb-0">
	            <div class="card-header"><!-- title -->
	                <h3 class="mb-0"><spring:message code="ecare.msg.cancel_form"/></h3>
	            </div>
	            <div class="table-responsive gridWrap">
	                <table class="table table-sm dataTable table-fixed" style="width:100%">
	                    <colgroup>
	                        <col width="15%" />
	                        <col width="15%" />
	                        <col width="15%" />
	                        <col width="25%" />
	                        <col width="15%" />
	                        <col width="15%" />
	                    </colgroup>
	                    <tbody>
		                    <tr>
		                    	<th scope="row">이케어번호</th><!-- 이케어번호 -->
		                        <td>
		                        	<select name="ecareNo" id="ecareNo" class="form-control form-control-sm">
		                        	</select>
		                        </td>
		                        <th scope="row">원 SEQ</th><!-- 대상자 일련번호 -->
		                        <td>
		                        	<input type="text" name="seq" id="seq" value="${seq }" class="form-control form-control-sm" />
		                        </td>
		                        <th scope="row">발송 요청일자</th><!-- 발송요청일자 -->
		                        <td>
		                        	<div class="input_datebox">
		                        		<fmt:parseDate var="reqDt" pattern="yyyyMMdd" value="${reqDt}" />
		                				<fmt:formatDate var="reqDt" value="${reqDt }" pattern="yyyy-MM-dd"/>
	                                    <input type="text" id="reqDt" name="reqDt" value="${reqDt}" class="form-control form-control-sm datepicker" />
	                                </div>
		                        </td>
		                   	</tr>
		                </tbody>
	                </table>
	        	</div>
	        	<div class="card-footer pb-4 btn_area"><!-- button area -->
	                <div class="row">
	                    <div class="col offset-sm-4 col-4 text-center">
	                        <button type="button" class="btn btn-outline-primary" id="cancelBtn">
	                            발송 취소
	                        </button>
	                    </div>
	                </div>
	            </div><!-- e.button area -->
	        </div><!-- e.card -->
	    </div><!-- e.page content -->
	</div><!-- e.main-panel -->
	</form>
</body>
</html>