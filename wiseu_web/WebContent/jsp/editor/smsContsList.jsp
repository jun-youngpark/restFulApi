<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>메시지 작성] SMS 템플릿/이미지 리스트
 * - URL : /editor/sms_contents_editor.do <br/>
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorSmsController <br/>
 * - 이전 파일명 : sms_contents_editor.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initContsEventBind();
    });

    function initContsEventBind() {
        $("div[id*=conts]").on("click", function(event) {
            if(!confirm("<spring:message code='editor.confirm.template.1'/>")) return;
            var template = $(this).text().replace(/\n/g, "<br/>");
            tinyMCE.get('template').setContent(template);
        });
        // 검색어 엔터키 입력
        $("#contsTxt").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });
        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            event.preventDefault();
            $("#smsContentsFrm input[name=contsTxt]").val($("#contsTxt").val());
            getContentsList();
        });
    }

</script>
</head>
<div class="row justify-content-end my-3"><!-- search -->
    <div class="col-12 justify-content-end">
        <div class="search_input">
            <input type="search" class="form-control form-control-sm" id="contsTxt" maxlength="30" aria-controls="datatable-basic" value="${param.contsTxt}" placeholder="<spring:message code='template.menu.mcontent'/>"/>
            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                <spring:message code="button.search"/><!-- 검색 -->
            </button>
        </div>
    </div>
</div><!-- //search -->

<div class="tmplWrap"><!-- 템플릿 리스트 -->
     <ul>
     <c:forEach var="list" items="${smsContentsList}">
        <li>
              <div class="text-area" id="conts${i}" class="sms_input hand" readonly="readonly">${list.contsTxt}</div>
         </li>
        </c:forEach>
     </ul>
 </div>

<!-- 페이징 -->
<c:import url="/common/page.do">
    <c:param name="viewPath" value="/common/page_ajax"/>
    <c:param name="actionPath" value="/editor/smsContsList.do"/><%-- /editor/sms_contents_list.do --%>
    <c:param name="total" value="${totalCount}"/>
    <c:param name="countPerPage" value="6"/>
    <c:param name="nowPage" value="${nowPage}"/>
    <c:param name="returnDocumentId" value="tmplWrap"/>
</c:import>
