<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] 템플릿 불러오기 (팝업) - 템플릿 리스트<br/>
 * - URL : /editor/importTemplateListPopup.do <br/>
 * - Controller : com.mnwise.wiseu.web.template.web.TemplateListController
 * - 이전 파일명 : template_editor.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="editor.import.template"/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type='text/javascript'>
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
            event.preventDefault();
            $("#searchForm").submit();
        });

    }

    // 템플릿을 가져온다.
    function goView(contsNo) {
        $("#editorFrm input[name=contsNo]").val(contsNo);
        $("#editorFrm").submit();
        openTemplate();
    }

    // 템플릿 불러오기 브라우저 호환성 문제로 수정.
    function openTemplate() {
        $("#preview").load(function () {
            var msg = $("#preview").contents().find('#msg');
            var msg = msg.val();
            if(msg != null) {
                // 기존 템플릿 초기화
                if('${templateType}' == "ab" && opener.frameElement.contentWindow.templateB == true) {
                    window.opener.tinyMCE.get('templateAb').setContent("");
                    window.opener.tinyMCE.get('templateAb').setContent($("#preview").contents().find('#code_template').html());
                } else {
                    window.opener.tinyMCE.get('template').setContent("");
                    window.opener.tinyMCE.get('template').setContent($("#preview").contents().find('#code_template').html());
                }
                alert(msg);
                window.close();
            } else {
                alert('<spring:message code="editor.alert.template.loaderr"/>');  // 템플릿 불러오기 중 에러가 발생하였습니다.
            }
        });
    }

</script>
</head>
<body>
<div class="card pop-card overflow-y-auto">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="editor.menu.template.list"/></h5></div><!-- 템플릿 리스트 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body pb-1">
        <form id="searchForm" name="searchForm" action="/editor/importTemplateListPopup.do" method="post" class="mb-1"><!-- /editor/template_editor.do -->
        <input type="hidden" name="templateType" value="${param.templateType}">
        <div class="row mr-1 justify-content-end">
            <div class="search_input">
                <input type="search" class="form-control form-control-sm" aria-controls="datatable-basic" name="searchWord" maxlength="30" value="${param.searchWord}" placeholder="<spring:message code='template.column.tmplnm'/>"/>
                <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                    <spring:message code="button.search"/><!-- 검색 -->
                </button>
            </div>
        </div>
        </form>

        <form id="editorFrm" name="editorFrm" method="post" action="/template/editor/importTemplate.do" target="preview"><!-- /template/editor/template_preview_editor.do -->
        <input type="hidden" name="templateType" value="editor">
        <input type="hidden" name="contsNo">
        <input type="hidden" name="searchWord" value="${contentVo.searchWord}">
        <input type="hidden" name="nowPage" value="${nowPage}">

        <div class="table-responsive">
            <table class="table table-sm dataTable table-hover table-fixed">
                <thead class="thead-light">
                <tr>
                    <th scope="col" width="10%" class="dp-tag"><spring:message code="common.menu.tag"/></th><!-- 태그 -->
                    <th scope="col" width="*"><spring:message code="editor.menu.template.tname"/></th><!-- 템플릿명 -->
                    <th scope="col" width="80"><spring:message code="editor.menu.template.ttype"/></th><!-- 템플릿 유형 -->
                    <th scope="col" width="70"><spring:message code="editor.menu.template.share"/></th><!-- 공유유형 -->
                    <th scope="col" width="15%"><spring:message code="common.menu.creator"/></th><!-- 작성자 -->
                    <th scope="col" width="80"><spring:message code="common.menu.cdate"/></th><!-- 작성일 -->
                </tr>
                </thead>
                <tbody>
                <c:forEach var="contentVo" items="${templateList}" varStatus="status">
                <tr style="cursor: pointer;" onclick="javascript:goView('${contentVo.contsNo}');">
                    <td class="dp-tag">${contentVo.tagNm}</td><!-- 태그 -->
                    <td class="text-left">${contentVo.contsNm}</td><!-- 템플릿명 -->
                    <td><!-- 템플릿 유형 -->
                        <c:if test="${contentVo.fileType eq 'H'}">HTML</c:if>
                        <c:if test="${contentVo.fileType eq 'I'}">IMAGE</c:if>
                    </td>
                    <td><!-- 공유유형 -->
                        <c:choose>
                            <c:when test="${contentVo.authType eq 'U'}"><spring:message code="template.type.user"/></c:when>
                            <c:when test="${contentVo.authType eq 'G'}"><spring:message code="template.type.group"/></c:when>
                            <c:when test="${contentVo.authType eq 'A'}"><spring:message code="template.type.all"/></c:when>
                        </c:choose>
                    </td>
                    <td class="text-left">${contentVo.userId}</td><!-- 작성자 -->
                    <td>${contentVo.createDt}</td><!-- 작성일 -->
                </tr>
                </c:forEach>

                <c:if test="${empty templateList}">
                <tr>
                    <td colspan="6"><spring:message code="common.msg.nodata"/></td><!-- 검색결과가 없습니다. -->
                </tr>
                </c:if>
                </tbody>
            </table>
        </div><!-- e.Light table -->
        </form>

        <!-- 페이징 -->
        <c:import url="/common/page.do">
            <c:param name="viewPath" value="/common/page" />
            <c:param name="actionPath" value="/editor/importTemplateListPopup.do?templateType=${param.templateType}" /><%-- /editor/template_editor.do --%>
            <c:param name="total" value="${totalCount}" />
            <c:param name="countPerPage" value="${countPerPage}" />
            <c:param name="nowPage" value="${nowPage}" />
            <c:param name="hiddenParam" value="countPerPage:${countPerPage}" />
            <c:param name="hiddenParam" value="searchColumn:${searchColumn}" />
            <c:param name="hiddenParam" value="searchWord:${searchWord}" />
            <c:param name="hiddenParam" value="channelStatus:${channelStatus}" />
        </c:import>
    </div><!-- e.card body -->
</div>

<iframe id="preview" name="preview" src="#" width="0" height="0" frameborder="0" style="visibility:hidden; display:none;"></iframe>
</body>
</html>
