<%-------------------------------------------------------------------------------------------------
 * - [리포트>요청 테이블 조회] 요청 테이블 조회 <br/>
 * - URL : /ecare/requestTableView.do <br/>
 * - Controller :com.mnwise.wiseu.web.report.web.ecare.RequestTableController <br/>
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title_${webExecMode}" /></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<style type="text/css">
    .table-hover>tbody>tr.no-hover:hover{background: #ffffff;}
    .no-hover tbody tr:hover{background: #ffffff;}
        #pagelist { border-collapse:collapse; width:500px}
    
</style>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
        $("#pagelist tr:not(.odd)").hide(); 

        $("#pagelist tr.odd").click(function(){
            if($(this).next("tr").css("display") == "none"){
                $("#pagelist tr:not(.odd)").hide(); 
            	$(this).next("tr").show();
            }else{
                $("#pagelist tr:not(.odd)").hide(); 
            }

        });

    });

    function initEventBind() {
        // 검색어 엔터키 입력
        $("#searchWord").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            $("#searchForm").attr('action', '${actionUrl}').submit();
            return false;
        });
    }

    function initPage() {
        new mdf.Date("#searchQstartDt");  // 발송기간-시작일
        new mdf.Date("#searchQendDt");  // 발송기간-종료일
        new mdf.Time("#searchStartReqTm");  // 발송기간-시작시간
        new mdf.Time("#searchEndReqTm");  // 발송기간-종료시간
    }


   	function goJsonValidation(seq){
   		window.open("/env/envJsonVaildator.do?cmd=seq&seq="+seq, "jsonValidation", "width=600, height=900");
   	}
   	

    function replayTemplate(serviceNo, listSeq, customerKey, resultSeq, subType, errorCd, channel) {
        if('' === $.trim(listSeq)) {
            alert("발송 후에 확인 가능합니다.");
            return;
        }

        window.open( '/resend/replayMainPopup.do?client=EC&serviceNo='+serviceNo+'&seq='+customerKey+'&resultSeq='+resultSeq+'&listSeq='+$.trim(listSeq)+'&subType='+subType+'&errorCd='+errorCd+'&channel='+channel, 'replayPopup', 830, 730);  // /resend/previous_template.do
    }
    
    function cancelSend(){
    	 if(!$("input:radio[name=selectRow]").is(":checked")){
             alert("발송 취소할 데이터를 선택 해주세요.");
             return;
         }
    	 let param = $("input[name='selectRow']:radio:checked").val().split("|"); //seq|sendFg|ecareNo
    	 
    	 if(param[1] != "R"){ //발송상태가 R 인경우에만 취소 가능
    		 alert("발송취소가 불가능한 상태입니다.");
    		 return;
    	 }

         if(confirm("[SEQ: " + param[0] + "]에 대하여 취소처리 진행하시겠습니까?")){
	         $("#seq").val(param[0]);
	         $("#ecareNo").val(param[2]);
	         $("#reqDt").val(param[3]);
	         let params = $("#searchForm").serialize();
	         $.ajax({
				url: "/ecare/cancel_form.json", // 클라이언트가 요청을 보낼 서버의 URL 주소
				data: params,                // HTTP 요청과 함께 서버로 보낼 데이터
				type: "POST",                             // HTTP 요청 방식(GET, POST)
				dataType: "json",                         // 서버에서 보내줄 데이터의 타입
				success: function(row){
					if(row == 1){
					    alert("취소요청 되었습니다.")
					}else
						alert("취소요청 실패")
				}
	         });
         
         }
    }
    
    function cancelSendAll(){
        if(confirm("검색된 모든 건에대한 발송 취소 요청을 합니다.\n${realtimeaccptTotalCount}건")){
            let params = $("#searchForm").serialize();
        	$.ajax({
                url: "/ecare/cancelSendAll.json", // 클라이언트가 요청을 보낼 서버의 URL 주소
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
    function resend(){
        if(!$("input:radio[name=selectRow]").is(":checked")){
            alert("재발송할 데이터를 선택 해주세요.");
            return;
        }
        if('${subType}' == 'A'){
            alert("이케어 유형을 선택해주세요.");
            return;
        }
        let param = $("input[name='selectRow']:radio:checked").val().split("|"); //seq|sendFg|ecareNo
        
        if(param[1] == "W"){ //발송상태가 W 인경우에만 취소 가능
            alert("재발송이 불가능한 상태입니다.");
            return;
        }
        $("#seq").val(param[0]);
        $("#ecareNo").val(param[2]);
        let params = $("#searchForm").serialize();
        $.ajax({
           url: "requestTableResend.json", // 클라이언트가 요청을 보낼 서버의 URL 주소
           data: params,                // HTTP 요청과 함께 서버로 보낼 데이터
           type: "POST",                             // HTTP 요청 방식(GET, POST)
           dataType: "json",                         // 서버에서 보내줄 데이터의 타입
           success: function(row){

               if(row == 1){
                   alert("재발송요청 되었습니다.")
                   location.reload();
               }else
                   alert("재발송요청 실패")
           }
       });
                
   }

</script>

</head>
<body>

    <form id="searchForm" name="searchForm" action="${actionUrl}" method="post"><!-- /ecare/ecare_list.do -->

        <input type="hidden" name="searchColumn" value="scenarioNm" id="id_searchColumn"/>
        <input type="hidden" name="ecareNo" id="ecareNo"/>
        <input type="hidden" name="seq" id="seq"/>
        <input type="hidden" name="reqDt" id="reqDt"/>

    <div class="main-panel">
        <div class="container-fluid mt-4 mb-2"><!-- Page content -->
            <div class="card">
                <div class="card-header"><!-- title -->
                    <h3 class="mb-0">요청 테이블 관리</h3>
                </div>

                <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="10%" />
                            <col width="16%" />
                            <col width="10%" />
                            <col width="16%" />
                            <col width="10%" />
                            <col width="13%" />
                            <col width="10%" />
                            <col width="*" />
                            <col width="100" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="ecare.menu.no_${webExecMode}" /></th><!-- 이케어 번호   -->
                            <td><input type="text" name="searchEcareNo" id="searchEcareNo" value="${searchEcareNo}" class="form-control form-control-sm" /></td>
                            <th scope="row"><em class="required"></em><spring:message code="ecare.channel" /></th><!-- 채널 -->
                            <td>
                                <select name="searchChannel" class="form-control form-control-sm"><!-- 채널 -->
                                    <option value=""><spring:message code="campaign.menu.channel" /></option>
                                    <c:forEach var="channel" items="${userChannelList}">
                                    <c:choose>
                                        <c:when test="${channel eq 'M'}"><option value="${channel}" <c:if test="${searchChannel eq 'M'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'S'}"><option value="${channel}" <c:if test="${searchChannel eq 'S'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'T'}"><option value="${channel}" <c:if test="${searchChannel eq 'T'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'A'}"><option value="${channel}" <c:if test="${searchChannel eq 'A'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'C'}"><option value="${channel}" <c:if test="${searchChannel eq 'C'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'P'}"><option value="${channel}" <c:if test="${searchChannel eq 'P'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'F'}"><option value="${channel}" <c:if test="${searchChannel eq 'F'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'B'}"><option value="${channel}" <c:if test="${searchChannel eq 'B'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    </c:choose>
                                    </c:forEach>
                                </select>
                            </td>
                            <th scope="row"><em class="required"></em><spring:message code="ecare.channel.period" /></th><!-- 발송기간 -->
                            <td class="border-right-0" colspan="3">
                                <div class="periodWrap d-flex align-items-center">
                                    <div class="input_datebox">
                                        <input type="hidden" name="searchQstartDt" id="searchQstartDt" value="${searchQstartDt}" maxlength="10" />
                                    </div>
                                        <input  type="hidden" name="searchStartReqTm" id="searchStartReqTm" value="${searchStartReqTm}" maxlength="10" />

                                    <span class="txt">~</span>
                                    <div class="input_datebox">
                                        <input type="hidden" name="searchQendDt" id="searchQendDt" value="${searchQendDt}" maxlength="10" />
                                    </div>
                                        <input type="hidden" name="searchEndReqTm" id="searchEndReqTm" value="${searchEndReqTm}" maxlength="10" />
                                </div>
                                
                            </td>
                            <td id="tdSearch" rowspan="2" class="text-center">
                                <button class="btn btn-outline-info btn_search" id="searchBtn">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">SEQ</th>
                            <td><input type="text" name="searchSeq" id="searchSeq" value="${searchSeq}" class="form-control form-control-sm" /></td>
                            <th scope="row"><span id="receiverInfo">수신자 이메일/전화번호</span></th><!-- 수신자 이메일/전화번호 -->
                            <td><input type="text" name="searchCustomerEmail" id="searchCustomerEmail" value="${searchCustomerEmail}" class="form-control form-control-sm" /></td>
                            <th scope="row">발송 상태</th>
                            <td>
                                <select name="searchSendFg" class="form-control form-control-sm">
                                    <option value="">발송상태</option>
                                    <option value="R" <c:if test="${searchSendFg eq 'R'}">selected="selected"</c:if>>발송대기</option>
                                    <option value="E" <c:if test="${searchSendFg eq 'E'}">selected="selected"</c:if>>발송완료</option>
                                    <option value="O" <c:if test="${searchSendFg eq 'O'}">selected="selected"</c:if>>발송에러</option>
                                    <option value="C" <c:if test="${searchSendFg eq 'C'}">selected="selected"</c:if>>발송취소</option>
                                </select>
                            </td>
                            <th scope="row">이케어 유형</th>
                            <td>
                                <select name="subType" id="subType" class="form-control form-control-sm">
                                    <option value="A" <c:if test="${subType eq 'A'}">selected="selected"</c:if>>전체</option>
                                    <option value="N" <c:if test="${subType eq 'N'}">selected="selected"</c:if>>준실시간</option>
                                    <option value="S" <c:if test="${subType eq 'S'}">selected="selected"</c:if>>스케줄</option>
                                </select>
                            </td>
                        </tr>
                        <tr id="trSn" class="dp-none">
                            <th scope="row"><spring:message code="common.request.id" /></th><!-- 발송요청 ID -->
                            <td colspan="5"><input type="text" name="sn" id="sn" class="form-control form-control-sm w-50" /></td>
                        </tr>
                        </tbody>
                    </table>
                    <div>
                        <button class="btn btn-sm btn-outline-danger" onclick='cancelSend()'>발송취소</button>
                            
                        <button class="btn btn-sm btn-outline-danger" onclick='cancelSendAll()'>전체발송취소</button>

                        <button class="btn btn-sm btn-outline-info" onclick='resend()'>재발송</button>

                        <button class="btn btn-sm btn-outline-info" onclick='location.href="/env/makeData.do"'>검증데이터 생성</button>
                    </div>
                </div>

                <!-- ---------------------------------------List Start--------------------------------------------------- -->

                    <div class="table-responsive">
                        <table class="table table-sm dataTable table-hover table-fixed" id="pagelist">
                            <thead class="thead-light">
                                <tr class="odd">
                                    <th scope="col" width="2%"><spring:message code="ecare.table.select"/></th><!-- 선택 -->
                                    <th scope="col" width="6%"><spring:message code="ecare.menu.no_${webExecMode}.br" /></th><!-- 이케어번호 -->
                                    <th scope="col" width="12%">SEQ</th>
                                    <th scope="col" width="3%"><spring:message code="report.ecare.summary.channel" /></th><!-- 채널 -->
                                    <th scope="col" width="8%"><spring:message code="report.campaign.head.title.grp" /></th><!-- 요청 부서 -->
                                    <th scope="col" width="8%"><spring:message code="report.campaign.head.title.user" /></th><!-- 담당자 -->
                                    <th scope="col" width="8%"><spring:message code="common.menu.remail" /></th> <!-- 수신자 이메일 -->
                                    <th scope="col" width="6%"><spring:message code="watch.menu.sstatus" /></th> <!-- 발송 상태 -->
                                    <th scope="col" width="8%">요청 일시</th>
                                    <th scope="col" width="8%">에러 메시지</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:if test="${!empty requestTableList}">
                            <c:forEach var="requestList" items="${requestTableList}" varStatus="status">
	                            <tr style="cursor:pointer;" class="odd">
	                                <td scope="row">
	                                    <div class="custom-control custom-radio">
		                                    <input type="radio" class="custom-control-input" name='selectRow' id='select_${status.index}' value='${requestList.seq}|${requestList.sendFg}|${requestList.ecareNo}|${requestList.reqDt}'/>
		                                    <label class="custom-control-label" for="select_${status.index}"></label>
		                                </div>
	                                
                                    <td >${requestList.ecareNo}</td>
                                    <td >${requestList.seq}</td>
	                                <td>
	                                    <em class="txt_channel ${requestList.channel}">
	                                        <c:choose>
	                                            <c:when test="${requestList.channel eq 'M'}">E</c:when>
	                                            <c:when test="${requestList.channel eq 'T'}">M</c:when>
	                                            <c:otherwise>${requestList.channel}</c:otherwise>
	                                        </c:choose>
	                                    </em>
	                                </td>
                                    <td>${requestList.reqDeptId}</td><!-- 요청 부서 -->
                                    <td>${requestList.reqUserId}</td><!-- 담당자 -->
                                    <td>${requestList.receiver}</td><!-- 상태 -->
                                    <td>${requestList.sendFg}</td><!-- 상태 -->
                                    <td>
                                        <fmt:parseDate value="${requestList.reqDt}" var="invokeDt" pattern="yyyyMMdd" />
                                        <fmt:formatDate value="${invokeDt}" pattern="YYYY-MM-dd" /> 
                                        <fmt:parseDate value="${requestList.reqTm}" var="invokeTm" pattern="HHmmss" />
                                        <fmt:formatDate value="${invokeTm}" pattern="HH:mm:ss" />
                                    </td>
                                    <td style="text-overflow: ellipsis; overflow: hidden;  white-space: nowrap;">${requestList.errorMsg}</td>
	                            </tr>
	                            <tr class="no-hover detailRow" id="${requestList.seq}">
	                               <td class="text-left" style="padding: 3%;" colspan="10">
                                <div class="card mb-0">
								<div class="card-body">
									<h3>상세정보</h3>
									<div class="table-responsive gridWrap">
										<table
											class="table table-sm dataTable table-fixed no-hover">
											<tbody>
												<tr class="no-hover odd ">
													<th width="10%">수신자명</th>
													<td width="16%">${requestList.receiverNm}</td>
													<th width="10%">수신자 ID</th>
													<td width="16%">${requestList.receiverId}</td>
													<th width="10%">수신자 이메일/번호</th>
													<td width="16%">${requestList.receiver}</td>
												</tr>
											</tbody>
										</table>
										<h3>에러 메시지</h3>
										<textarea class="form-control" rows="10"
											style="resize: none; width: 100%" readonly="readonly">${requestList.errorMsg}</textarea>
										<br>
										<div align="center">
											<button class="btn btn-bg btn-outline-info btn_search" onclick="goJsonValidation('${requestList.seq}')">전문검증</button>
												
											<button class="btn btn-bg btn-outline-info btn_search" onclick='javascript:replayTemplate("${requestList.ecareNo}", "${requestList.listSeq}","${requestList.receiverId}", "${requestList.resultSeq}", "N", "550","${requestList.channel}")'>고객상세이력보기</button>
										</div>
									</div>
								</div>
							</div></tr>
                            </c:forEach>
                            </c:if>

                            <c:if test="${empty requestTableList}" >
                            <tr>
                                <c:choose>
                                    <c:when test="${serviceType eq 'S' and subType eq 'S'}">
                                        <td colspan="8"><spring:message code="report.ecare.list.nodata" /></td><!-- 검색 결과가 없습니다. -->
                                    </c:when>
                                    <c:otherwise>
                                        <td colspan="6"><spring:message code="report.ecare.list.nodata" /></td><!-- 검색 결과가 없습니다. -->
                                    </c:otherwise>
                                </c:choose>
                           </tr>
                           </c:if>
                           </tbody>
                        </table>
                    </div><!-- //Light table -->

                    <!-- 페이징 -->
                    ${paging}
                </div><!-- e.card body -->
            </div><!-- //card -->
        </div><!-- e.page content -->
    </div><!-- e.main-panel -->
    </form>

<form id="pageFrm" name="pageFrm" method="post">
    <input type="hidden" id="seq" name="seq" />
    <input type="hidden" id="ecareNo" name="ecareNo" />
    <input type="hidden" id="cmd" name="cmd" />
    <input type="hidden" id="page" name="page" />        
    <input type="hidden" id="searchColumn" name="searchColumn" value="${searchColumn}">
    <input type="hidden" id="searchWord" name="searchWord" value="${searchWord}">
    <input type="hidden" id="orderColumn" name="orderColumn" value="${orderColumn}">
    <input type="hidden" id="orderSort" name="orderSort" value="${orderSort}">
    <input type="hidden" id="countPerPage" name="countPerPage" value="${countPerPage}">
    <input type="hidden" id="nowPage" name="nowPage" value="${nowPage}">
    <input type="hidden" id="serviceType" name="serviceType" value="${serviceType}">
    <input type="hidden" id="subType" name="subType" value="${subType}">
    <input type="hidden" id="searchStartDt" name="searchStartDt" value="${searchQstartDt}">
    <input type="hidden" id="searchEndDt" name="searchEndDt" value="${searchQendDt}">
    <input type="hidden" id="webExecMode" name="webExecMode" value="${webExecMode}">
    <input type="hidden" id="channel" name="channel" />
</form>
</body>
</html>
