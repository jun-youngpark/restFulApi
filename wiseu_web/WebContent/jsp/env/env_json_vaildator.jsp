<%------------------------------------------------------------------
 * - [환경설정>검증데이터 생성기]검증데이터 리스트 <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvJsonValidatorController <br/>
 * - [환경설정>검증데이터 생성기]검증데이터 서비스 리스트<br/>
 * Title  : JSON 검증
 -----------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/plugin.jsp" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><spring:message code="env.make.data.title"/></title>
<script type="text/javascript">
	$(document).ready(function() {
		var channel = "${channel}";
		if("${searchMsg}"){
			alert("${searchMsg}");
		}
	});
	function copy_to_clipboard() {    
		  var copyText = document.getElementById('jsonFormat');
		  copyText.select();
		  copyText.setSelectionRange(0, 99999);
		  document.execCommand("Copy");
	}
    function searchSeq(){
    	if(!$("#seq").val()){
    		alert("검색조건이 비어있습니다.");
    		return;
    	}
    	$("#cmd").val("seq");
    	$("#editorFrm").submit();
        
    }
    
    function updateJonmun(){
        if("${seq}" == ""){
            alert("검색조건이 비어있습니다.");
            return;
        }
        if(confirm("반영하시겠습니까? ")){
            let params = $("#searchForm").serialize();
            $.ajax({
                url: "", // 클라이언트가 요청을 보낼 서버의 URL 주소
                data: params,                // HTTP 요청과 함께 서버로 보낼 데이터
                type: "POST",                             // HTTP 요청 방식(GET, POST)
                dataType: "json",                         // 서버에서 보내줄 데이터의 타입
                success: function(row){
                    if(row > 0){
                        alert(row + "건 취소되었습니다")
                        location.reload();
                    }
                }
            });
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
	                <h3 class="mb-0">JSON 검증</h3>
	            </div>
	            <div class="card-body gridWrap">
				    <form name="editorFrm" id="editorFrm" method="post" action="<c:url value="/env/envJsonVaildator.do"/>">
				        <input type="hidden" name="cmd" id="cmd" value="list">
					    <div class="message_form">
					        <table class="table table-sm dataTable table-fixed">
                                <tr >
                                    <th scope="row" width="150">SEQ</th>
                                    <td align="right" class='input-group' style="width: 97%;" valign="top">
                                        <input class="form-control input-group" name="seq" id="seq" size="15" value="${seq}"/>
                                        <div class="input-group-append">
                                            <button type="button" class="btn btn-outline-info btn_search" onclick="searchSeq()" >검색</button>
                                        </div>
                                    </td>
                                </tr>
                                
                            </table>
                            <c:if test="${not empty jsonReport.errWord}">
				                <div class="alert alert-warning" style="padding-left:10px; ">
	                                 <Strong>${jsonReport.errWord}...</Strong><br/>
	                                 <span style="margin-left: 13ex;" id="arrowPosition">↓ ↓ ↓ ↓ ↓ ↓</span><br/>
				                     <span style="margin-left: <c:if test='${20 gt jsonReport.errColumn}'>${20-jsonReport.errColumn}</c:if>ex;" id="jsonLine">${jsonReport.errPoint}</span>
				                </div>
			                </c:if>
                            <label for="comment">JSON 입력</label>
					        <textarea rows="10" style="width:100%" class="form-control" name="json" id="json" >${json}</textarea>
					        <br/>
                            <label for="comment">format JSON</label>
					        <textarea rows="10" style="width:100%" class="form-control"  name="jsonFormat" id="jsonFormat" readonly="readonly">${jsonReport.json}</textarea>
                            <div align="center">
                               <br/>
                               <input value="검증" class="btn btn-primary" type="submit"></input>
                               <input value="미리보기" class="btn btn-primary" type="submit"></input>
                               <input value="전문 수정" class="btn btn-primary" type="button" onclick="updateJonmun"></input>
					        </div>
					    </div>
				    </form>
				</div>
			</div>
		</div>
	</div>
<br/><br/>

</body>
</html>