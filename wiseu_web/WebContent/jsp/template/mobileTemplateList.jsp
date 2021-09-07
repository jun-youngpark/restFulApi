<%-------------------------------------------------------------------------------------------------
 * - [템플릿>모바일 템플릿 리스트] 모바일 템플릿 리스트
 * - URL : /template/mobileTemplateList.do
 * - Controller : com.mnwise.wiseu.web.template.web.MobileTemplateListController
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title><spring:message code="template.mobiletemplate.list.msg"/></title><!-- 모바일 템플릿 리스트 -->
<script type='text/javascript'>
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if($("#searchForm input[name=contsNo]").val().length === 0) {
                alert('<spring:message code="template.alert.msg.1"/>');  // 삭제 할 템플릿을 선택하세요.
                return false;
            }

            if(confirm('<spring:message code="template.alert.msg.2"/>')) {  // 삭제하시겠습니까?
                $("#searchForm").attr('action', '/template/deleteMobileTemplate.do').submit();
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
            $("#searchForm input[name=nowPage]").val('1');
            $("#searchForm").attr('action', '/template/mobileTemplateList.do').submit();  // /template/mobileTemplate.do
        });
    }

    function initPage() {
        new mdf.Date("#searchQstartDt");  // 작성일-시작일
        new mdf.Date("#searchQendDt");  // 작성일-종료일
        new mdf.DataTable("#templateTable");
    }

    // 태그 클릭
    function goTagSearch(tagNo) {
        $("#searchForm input[name=tagNo]").val(tagNo);
        $("#searchForm input[name=nowPage]").val('1');
        $("#searchForm").attr('action', '/template/mobileTemplateList.do').submit();  // /template/mobileTemplate.do
    }

    function setContsNo(contsNo) {
        $("#searchForm input[name=contsNo]").val(contsNo);
    }

    // 템플릿명 클릭
    function goView(contsNo) {
        document.location.href = "/template/mobileTemplateView.do?contsNo=" + contsNo;  // /template/viewMobileTemplate.do
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
        <form id="searchForm" name="searchForm" method="post">
            <input type="hidden" name="contsNo" />
            <input type="hidden" name="tagNo" />
            <input type="hidden" name="countPerPage" value="${countPerPage}"/>
            <input type="hidden" name="nowPage" value="${nowPage}"/>

            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="template.mobiletemplate.list.msg"/></h3><!-- 모바일 템플릿 리스트 -->
            </div>

            <div class="card-body px-0 pb-0">
                <div class="row align-items-center py-1 table_option">
                    <c:if test="${sessionScope.write eq 'W'}">
                        <div class="col-3"><!-- buttons -->
                            <button class="btn btn-sm btn-outline-primary btn-round" id="deleteBtn">
                                <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                            </button>
                        </div>
                    </c:if>

                    <div class="col-9 searchWrap"><!-- search -->
                        <div class="form-group searchbox">
                            <span class="form-control-label txt"><spring:message code="campaign.table.cdate"/></span><!-- 작성일 -->
                            <div class="input_datebox">
                                <input type="hidden" name="searchQstartDt" id="searchQstartDt" value="${searchQstartDt}" maxlength="10" />
                            </div>
                            <span class="form-control-label">~</span>
                            <div class="input_datebox">
                                <input type="hidden" name="searchQendDt" id="searchQendDt" value="${searchQendDt}" maxlength="10" />
                            </div>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" name="fileType">
                                <option value=""><spring:message code="template.filetype"/></option><!-- 템플릿 유형 -->
                                <option value="D" <c:if test="${'D' eq fileType}">selected="selected"</c:if>><spring:message code="template.filetype.d"/></option>
                                <option value="T" <c:if test="${'T' eq fileType}">selected="selected"</c:if>><spring:message code="template.filetype.t"/></option>
                                <option value="I" <c:if test="${'I' eq fileType}">selected="selected"</c:if>><spring:message code="template.filetype.i"/></option>
                                <option value="C" <c:if test="${'C' eq fileType}">selected="selected"</c:if>><spring:message code="template.filetype.c"/></option>
                            </select>
                        </div>
                        <div class="form-group searchbox search_input pl-0">
                            <input type="search" class="form-control form-control-sm" name="searchWord" id="searchWord" value="${param.searchWord}" placeholder="<spring:message code='template.table.tname'/>"/>
                            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                <spring:message code="button.search"/><!-- 검색 -->
                            </button>
                        </div>
                    </div><!-- //search -->
                </div><!-- e.search area & buttons -->
            </div><!-- e.card body -->

            <div class="table-responsive overflow-x-hidden">
                <table class="table table-sm table-hover dataTable" id="templateTable">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 라디오 -->
                        <th scope="col" width="6%"><spring:message code="campaign.table.no"/></th><!-- No -->
                        <th scope="col" width="10%" class="dp-tag"><spring:message code="campaign.menu.tag"/></th> <!-- 태그  display 여부 css파일 dp-tag 수정 -->
                        <th scope="col" width="*"><spring:message code="template.table.tname"/></th><!-- 템플릿명 -->
                        <th scope="col" width="10%"><spring:message code="campaign.table.type"/></th><!-- 유형 -->
                        <th scope="col" width="10%"><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                        <th scope="col" width="15%"><spring:message code="campaign.table.creator"/></th><!-- 작성자 -->
                        <th scope="col" width="10%"><spring:message code="campaign.table.cdate"/></th><!-- 작성일 -->
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="templateVo" items="${templateList}" varStatus="status">
                    <tr style="cursor: pointer;" onclick="javascript:goView('${templateVo.contsNo}')">
                        <th scope="row" onclick="event.cancelBubble=true">
                            <div class="custom-control custom-radio">
                                <input type="radio" id="radio1_${status.count}" class="custom-control-input" name="templateItem" value="${templateVo.contsNo}"
                                    onclick="setContsNo('${templateVo.contsNo}');" />
                                <label class="custom-control-label" for="radio1_${status.count}"></label>
                            </div>
                        </th>
                        <td>${templateVo.contsNo}</td>
                        <td class="dp-tag"><c:if test="${empty templateVo.tagNm}">&nbsp;</c:if>${templateVo.tagNm}</td>  <!-- 태그  display 여부 css파일 dp-tag 수정 -->
                        <td class="text-left">${templateVo.contsNm}</td>
                        <td>
                            <c:choose>
                                <c:when test="${templateVo.fileType eq 'D'}"><spring:message code="template.filetype.d"/></c:when>
                                <c:when test="${templateVo.fileType eq 'T'}"><spring:message code="template.filetype.t"/></c:when>
                                <c:when test="${templateVo.fileType eq 'I'}"><spring:message code="template.filetype.i"/></c:when>
                                <c:when test="${templateVo.fileType eq 'S'}"><spring:message code="template.filetype.s"/></c:when>
                                <c:when test="${templateVo.fileType eq 'C'}"><spring:message code="template.filetype.c"/></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${templateVo.authType eq 'U'}"><spring:message code="template.type.user"/></c:when>
                                <c:when test="${templateVo.authType eq 'G'}"><spring:message code="template.type.group"/></c:when>
                                <c:when test="${templateVo.authType eq 'A'}"><spring:message code="template.type.all"/></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td>${templateVo.userNm}</td>
                        <td>${templateVo.createDt}</td>
                    </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div><!-- e.Light table -->
            </form>

            <!-- 페이징 -->
            <c:import url="/common/page.do">
                <c:param name="viewPath" value="/common/page"/>
                <c:param name="actionPath" value="/template/mobileTemplateList.do"/><%-- /template/mobileTemplate.do --%>
                <c:param name="total" value="${totalCount}"/>
                <c:param name="countPerPage" value="${countPerPage}"/>
                <c:param name="nowPage" value="${nowPage}"/>
                <c:param name="tagNo" value="${tagNo}"/>
                <c:param name="hiddenParam" value="countPerPage:${countPerPage}"/>
                <c:param name="hiddenParam" value="searchWord:${searchWord}"/>
                <c:param name="hiddenParam" value="fileType:${fileType}"/>
                <c:param name="hiddenParam" value="searchQstartDt:${searchQstartDt}"/>
                <c:param name="hiddenParam" value="searchQendDt:${searchQendDt}"/>
            </c:import>
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>