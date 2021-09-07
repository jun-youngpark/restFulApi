<%-------------------------------------------------------------------------------------------------
 * - [환경설정>기본핸들러 설정]기본핸들러 리스트 <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvDefaultHandleController <br/>
 * - Description : 핸들러 구분의 경우 serviceType 으로 구분한다.
 *                 캠페인 : ''
 *                 이케어 : 실시간 - R 준실시간 N 스케줄 S
 * - 이전 파일명 : env_handlerList.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title>Handler List</title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
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
            $("#searchForm input[name=nowPage]").val(1);
            $("#searchForm").submit();
        });

        // 체크박스 전체 버튼 클릭
        $("#table-check-all").on("click", function(event) {
            $.mdf.checkAll("#table-check-all");
        });

        // 추가 버튼 클릭
        $("#addBtn").on("click", function(event) {
            $("#editorFrm input[name='seq']").val(0);
            $('#editorFrm').attr('action', "/env/defaultHandlerView.do").submit();  // /env/viewHandler.do
        });

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            var chkCount = $("#editorFrm input:checkbox[name=noArray]:checked").length;
            if(chkCount == 0) {
                alert("<spring:message code='env.alert.handler.del1'/>");  // 삭제 할 템플릿을 하나 이상 선택하세요.
                return false;
            }

            if(!confirm("<spring:message code='env.alert.handler.del2'/>")) {  // 삭제하시겠습니까?
                return;
            }
            $('#editorFrm').attr('action', "/env/deleteHandler.do").submit();
        });

        // 기본핸들러 설정 버튼 클릭
        $("#defaultHandlerSetBtn").on("click", function(event) {
            if(confirm("<spring:message code='env.alert.handler.init'/>") == true) {  // 기존에 기본핸들러가 등록되어 있다면 모두 지워집니다.\\n기본핸들러를 생성하시겠습니까?
                $.post("/env/insertEditorDefaultHandler.json", null, function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='env.alert.handler.save'/>");  // 기본핸들러가 저장되었습니다.
                        location.reload();
                    } else {
                        alert("<spring:message code='env.alert.handler.error'/>");  // 기본핸들러 저장하는 도중 에러가 발생하였습니다.
                    }
                });
            }
        });
    }

    // 핸들러 보기
    function goView(handlerNo) {
        $("#editorFrm input[name='seq']").val(handlerNo);
        $('#editorFrm').attr('action', "/env/defaultHandlerView.do").submit();  // /env/viewHandler.do
    }
</script>

</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="env.msg.handler"/></h3>
            </div>

            <div class="card-body pb-0">
                <form id="searchForm" name="searchForm" action="/env/defaultHandlerList.do" method="post"><!-- /env/deafulthandler.do -->
                <input type="hidden" name="nowPage" value="${param.nowPage}" />
                <div class="row align-items-center py-2 table_option">
                    <div class="col-3"><!-- buttons -->
                    <c:if test="${sessionScope.write eq 'W'}">
                        <button id="deleteBtn" class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-trash"></i> <spring:message code="button.delete" /><!-- 삭제 -->
                        </button>
                        <button id="addBtn" class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-plus"></i> <spring:message code="button.add" /><!-- 추가 -->
                        </button>
                        <c:if test="${totalCnt <= 0}">
                            <button id="defaultHandlerSetBtn" class="btn btn-sm btn-neutral btn-round">
                                <i class="fas fa-user-edit"></i> <spring:message code="env.msg.handler" />
                            </button>
                        </c:if>
                    </c:if>&nbsp;
                    </div><!-- e.buttons -->

                    <div class="col-9 searchWrap"><!-- search -->
                        <div class="form-group searchbox">
                            <select class="form-control form-control-sm" name="searchServiceType">
                                <option value="" <c:if test="${empty param.searchServiceType}">selected="selected"</c:if>><spring:message code="env.menu.handler.stype" /></option>
                                <option value="EM" <c:if test="${param.searchServiceType eq 'EM'}">selected="selected"</c:if>><spring:message code="common.menu.campaign" /></option>
                                <option value="N" <c:if test="${param.searchServiceType eq 'N'}">selected="selected"</c:if>><spring:message code="common.menu.ecare_1" /> <spring:message code="report.ecare.monthly.nrealtime" /></option>
                                <option value="S" <c:if test="${param.searchServiceType eq 'S'}">selected="selected"</c:if>><spring:message code="common.menu.ecare_1" /> <spring:message code="report.ecare.monthly.schedule" /></option>
                            </select>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" name="searchChannel">
                                <option value="" <c:if test="${empty val}">selected="selected"</c:if>><spring:message code="common.menu.channel.select" /></option>
                                <c:forEach var="channel" items="${channelUseList}">
                                <c:choose>
                                <c:when test="${channel eq 'M'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'M'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                <c:when test="${channel eq 'S'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'S'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                <c:when test="${channel eq 'T'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'T'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                <c:when test="${channel eq 'P'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'P'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                <c:when test="${channel eq 'A'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'A'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                <c:when test="${channel eq 'C'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'C'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                <c:when test="${channel eq 'B'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'B'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                <c:when test="${channel eq 'F'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'F'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group searchbox search_input pl-0">
                            <input type="text" class="form-control form-control-sm" id="searchWord" name="searchWord" value="${param.searchWord}" placeholder="<spring:message code='env.menu.handler.name'/>"/>
                            <button id="searchBtn" class="btn btn-sm btn-outline-info btn_search">
                                <spring:message code="button.search" /><!-- 검색 -->
                            </button>
                        </div>
                    </div><!-- //search -->
                </div><!-- e.search area & buttons -->
                </form>

                <form id="editorFrm" name="editorFrm" action="/env/defaultHandlerList.do" method="post"><!-- /env/deafulthandler.do -->
                <input type="hidden" name="seq" />
                <input type="hidden" name="nowPage" value="${param.nowPage}" />
                <input type="hidden" name="searchServiceType" value="${param.searchServiceType}" />
                <input type="hidden" name="searchChannel" value="${param.searchChannel}" />
                <input type="hidden" name="searchWord" value="${param.searchWord}" />
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-hover table-fixed">
                        <thead class="thead-light">
                        <tr>
                            <th width="20" class="text-center"><!-- 체크박스 -->
                                <div class="custom-control custom-checkbox">
                                    <input class="custom-control-input" id="table-check-all" type="checkbox">
                                    <label class="custom-control-label" for="table-check-all"></label>
                                </div>
                            </th>
                            <th width="60"><spring:message code="env.menu.handler.service"/></th><!-- 구분 -->
                            <th width="80"><spring:message code="env.menu.handler.stype"/></th><!-- 서비스타입 -->
                            <th width="80"><spring:message code="env.menu.handler.channel"/></th><!-- 채널 -->
                            <th width="110"><spring:message code="env.menu.handler.hproperty"/></th><!-- 핸들러 속성 en:width -->
                            <th width="80"><spring:message code="common.menu.handler"/> <spring:message code="env.menu.handler.type"/></th><!-- 핸들러 타입 -->
                            <th width="*"><spring:message code="env.menu.handler.name"/></th><!-- 핸들러명 -->
                            <th width="10%"><spring:message code="env.menu.handler.creator"/></th><!-- 작성자 -->
                            <th width="80"><spring:message code="env.menu.handler.cdate"/></th><!-- 작성일 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${!empty handlerList}">
                            <c:forEach var="envHandlerVo" items="${handlerList}" varStatus="status">
                                <c:if test="${ !(useSecurityMail eq 'off' && envHandlerVo.handleAttr eq 'S') }">
                                    <c:if test="${!(envHandlerVo.serviceType eq 'P') }">
                                        <tr style="cursor:pointer;" onclick="goView('${envHandlerVo.seq}');">
                                            <th scope="row" onclick="event.cancelBubble=true"><!-- 체크박스 -->
                                                <div class="custom-control custom-checkbox">
                                                    <input class="custom-control-input" id="table-check-all_${status.count}" type="checkbox" name="noArray" value="${envHandlerVo.seq}">
                                                    <label class="custom-control-label" for="table-check-all_${status.count}"></label>
                                                </div>
                                            </th>
                                            <td><!-- 구분 -->
                                                <c:choose>
                                                    <c:when test="${empty envHandlerVo.serviceType}"><spring:message code="common.menu.campaign"/></c:when>
                                                    <c:when test="${envHandlerVo.serviceType eq 'P'}"><spring:message code="common.menu.survey"/></c:when>
                                                    <c:otherwise><spring:message code="common.menu.ecare_${webExecMode}"/></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><!-- 서비스타입 -->
                                                <c:choose>
                                                    <c:when test="${envHandlerVo.serviceType eq 'R'}"><spring:message code="ecare.type.R"/></c:when>
                                                    <c:when test="${envHandlerVo.serviceType eq 'N'}"><spring:message code="ecare.type.SN"/></c:when>
                                                    <c:when test="${envHandlerVo.serviceType eq 'S'}"><spring:message code="ecare.type.S"/></c:when>
                                                    <c:otherwise></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><spring:message code="common.channel.${envHandlerVo.channel}"/></td><!-- 채널 -->
                                            <td><!-- 속성 -->
                                                <c:choose>
                                                    <c:when test="${envHandlerVo.handleAttr eq 'D'}">
                                                        <spring:message code="env.menu.handler.default"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <spring:message code="editor.attach.${fn:toLowerCase(envHandlerVo.handleAttr)}" text="No code" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><!-- 타입 -->
                                                <c:choose>
                                                    <c:when test="${envHandlerVo.handleType eq 'G'}">GROOVY</c:when>
                                                    <c:when test="${envHandlerVo.handleType eq 'S'}">SCRIPT</c:when>
                                                </c:choose>
                                            </td>
                                            <td class="text-left">${envHandlerVo.handleNm}</td><!-- 핸들러명 -->
                                            <td>${envHandlerVo.userId}</td><!-- 작성자 -->
                                            <td>${envHandlerVo.createDt}</td><!-- 작성일 -->
                                        </tr>
                                    </c:if>
                                </c:if>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- // Light table -->

                <!-- 페이징 -->
                ${paging}
                </form>
            </div><!-- // Card body -->
        </div><!-- // Card-->
    </div>
</div>
</body>
</html>