<%-------------------------------------------------------------------------------------------------
 * - [템플릿>템플릿 리스트] 템플릿 리스트 <br/>
 * - URL : /template/templateList.do <br/>
 * - Controller :com.mnwise.wiseu.web.template.web.TemplateListController <br/>
 * - 이전 파일명 : template_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.msg.lists"/></title><!-- 템플릿 리스트 -->
<script type='text/javascript'>
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if($("input:radio[name=contsNo]").is(":checked") == false) {
                alert('<spring:message code="template.alert.msg.1"/>');  // 삭제 할 템플릿을 선택하세요.
                return false;
            }

            if(confirm('<spring:message code="template.alert.msg.2"/>')) {  // 삭제하시겠습니까?
                $("#searchForm").attr("action", "/template/deleteTemplate.do").submit();
            }
        });

        // 검색어 엔터키 입력
        $("#searchWord").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            $("#searchForm input[name=nowPage]").val("1");
            $("#searchForm").submit();
        });
    }

    function initPage() {
        new mdf.Date("#searchQstartDt");  // 작성일-시작일
        new mdf.Date("#searchQendDt");  // 작성일-종료일
    }

    // 템플릿명 클릭
    function goView(contsNo) {
        document.location.href = "/template/templateView.do?contsNo=" + contsNo;  // /template/viewTemplate.do
    }
</script>
</head>
<body>
    <div class="main-panel">
        <div class="container-fluid mt-4 mb-2"><!-- Page content -->
            <div class="card">
                <div class="card-header"><!-- title -->
                    <h3 class="mb-0"><spring:message code="template.msg.lists"/></h3><!-- 템플릿 리스트 -->
                </div>

                <form id="searchForm" name="searchForm" action="/template/templateList.do" method="post"><!-- /template/template.do -->
                <input type="hidden" name="tagNo" value="${contentVo.tagNo}" />
                <input type="hidden" name="countPerPage" value="${countPerPage}"/>
                <input type="hidden" name="nowPage" value="${nowPage}"/>

                <div class="card-body px-0 pb-0">
                    <div class="row align-items-center py-1 table_option">
                        <div class="col-3"><!-- buttons -->
                            <c:if test="${sessionScope.write eq 'W'}">
                                <button class="btn btn-sm btn-outline-primary btn-round" id="deleteBtn">
                                    <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                                </button>
                            </c:if>&nbsp;
                        </div>
                        <div class="col-9 searchWrap"><!-- search -->
                            <div class="form-group searchbox">
                                <span class="form-control-label txt"><spring:message code="campaign.table.cdate"/></span><!-- 작성일 -->
                                <div class="input_datebox">
                                    <input type="hidden" name="searchQstartDt" id="searchQstartDt" value="${searchQstartDt}" maxlength="10">
                                </div>
                                <span class="form-control-label">~</span>
                                <div class="input_datebox">
                                    <input type="hidden" name="searchQendDt" id="searchQendDt" value="${searchQendDt}" maxlength="10">
                                </div>
                            </div>
                            <div class="form-group searchbox search_input pl-0">
                                <input type="search" class="form-control form-control-sm" name="searchWord" id="searchWord" value="${param.searchWord}" aria-controls="datatable-basic" placeholder="<spring:message code='template.table.tname'/>"/>
                                <button id="searchBtn" class="btn btn-sm btn-outline-info btn_search">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </div>
                        </div><!-- //search -->
                    </div><!-- e.search area & buttons -->
                </div><!-- e.card body -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable table-hover table-fixed">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 선택 -->
                            <th scope="col" width="6%"><spring:message code="campaign.table.no"/></th><!-- No -->
                            <th scope="col" width="10%" class="dp-tag"><spring:message code="campaign.menu.tag"/></th><!-- 태그  display 여부 css파일 dp-tag 수정 -->
                            <th scope="col" width="*"><spring:message code="template.table.tname"/></th><!-- 템플릿명 -->
                            <th scope="col" width="10%"><spring:message code="campaign.table.type"/></th><!-- 유형 -->
                            <th scope="col" width="10%"><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                            <th scope="col" width="15%"><spring:message code="campaign.table.creator"/></th><!-- 작성자 -->
                            <th scope="col" width="10%"><spring:message code="campaign.table.cdate"/></th><!-- 작성일 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${!empty templateList}">
                        <c:forEach var="templateVo" items="${templateList}" varStatus="status">
                        <tr style="cursor: pointer;" onclick="javascript:goView('${templateVo.contsNo}')">
                            <th scope="row" onclick="event.cancelBubble=true"><!-- 라디오 -->
                                <div class="custom-control custom-radio">
                                    <input type="radio" id="contsNo_${status.count}" class="custom-control-input" name="contsNo" value="${templateVo.contsNo}"/>
                                    <label class="custom-control-label" for="contsNo_${status.count}"></label>
                                </div>
                            </th>
                            <td>${templateVo.contsNo}</td><!-- No -->
                            <td class="dp-tag">${templateVo.tagNm}</td><!-- 태그  display 여부 css파일 dp-tag 수정 -->
                            <td class="text-left">${templateVo.contsNm}</td><!-- 템플릿명 -->
                            <td><!-- 유형 -->
                                <c:choose>
                                    <c:when test="${templateVo.fileType eq 'H'}">HTML</c:when>
                                    <c:when test="${templateVo.fileType eq 'I'}">IMAGE</c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                            <td><!-- 공유유형 -->
                                <c:choose>
                                    <c:when test="${templateVo.authType eq 'U'}"><spring:message code="template.type.user"/></c:when>
                                    <c:when test="${templateVo.authType eq 'G'}"><spring:message code="template.type.group"/></c:when>
                                    <c:when test="${templateVo.authType eq 'A'}"><spring:message code="template.type.all"/></c:when>
                                </c:choose>
                            </td>
                            <td>${templateVo.userNm}</td><!-- 작성자 -->
                            <td>${templateVo.createDt}</td><!-- 작성일 -->
                        </tr>
                        </c:forEach>
                        </c:if>

                        <c:if test="${empty templateList}" >
                        <tr>
                            <td colspan="8"><spring:message code="common.msg.nodata"/></td><!-- 검색결과가 없습니다. -->
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- e.Light table -->
                </form>

                <!-- 페이징 -->
                <c:import url="/common/page.do">
                    <c:param name="viewPath" value="/common/page"/>
                    <c:param name="actionPath" value="/template/templateList.do"/><%-- /template/template.do --%>
                    <c:param name="total" value="${totalCount}"/>
                    <c:param name="countPerPage" value="${countPerPage}"/>
                    <c:param name="nowPage" value="${nowPage}"/>
                    <c:param name="tagNo" value="${tagNo}"/>
                    <c:param name="hiddenParam" value="countPerPage:${countPerPage}"/>
                    <c:param name="hiddenParam" value="searchColumn:${searchColumn}"/>
                    <c:param name="hiddenParam" value="searchWord:${searchWord}"/>
                    <c:param name="hiddenParam" value="channelStatus:${channelStatus}"/>
                    <c:param name="hiddenParam" value="searchQstartDt:${searchQstartDt}"/>
                    <c:param name="hiddenParam" value="searchQendDt:${searchQendDt}"/>
                </c:import>
            </div><!-- e.card -->
        </div><!-- e.page content -->
    </div><!-- e.main-panel -->
</body>
</html>