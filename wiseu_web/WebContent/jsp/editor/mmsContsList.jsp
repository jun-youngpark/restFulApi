<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>메시지 작성] LMS/MMS 템플릿/이미지 리스트
 * - URL : /editor/mmsContsList <br/>
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorMmsController <br/>
 * - 이전 파일명 : mms_contents_editor.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<head>
<script type="text/javascript">
    $(document).ready(function() {
        initContsEventBind();
        initContsPage();
    });

    function initContsEventBind() {
        $("div[id*=conts]").on("click", function(event) {
            if(!confirm("<spring:message code='editor.confirm.template.1'/>")) return;
            tinyMCE.get('template').setContent($(this).text().replace(/\n/g, "<br/>"));
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
            $("#mmsContentsFrm input[name=contsTxt]").val($("#contsTxt").val());
            getContentsList('T');
        });
    }

    function initContsPage() {
        $("#mmsContentsFrm input[name=contsTxt]").val("");
        if($("#contsTxt").length > 0) {    // 없을때 에러가 나서 변경함.
            $("#contsTxt").val("${contsTxt}");
        }
    }

    function insertContents(obj, index) {
        var filePath = $("#filePath" + index).val();
        var fileName = $("#fileName" + index).val();
        var fileSize = $("#fileSize" + index).val();
        var content = tinyMCE.get("template").getContent({ format: "text"}).replace(/\n/g, "<br/>");
        var imgCnt = content.split("<img").length

        if(imgCnt > 3) {
        	alert('<spring:message code="editor.alert.maximun.attach.file" />');  // 첨부파일은 3 개까지만 가능합니다.
            return false;
        }

        // 배열에 넣는다.
        insertMmsContents(filePath, fileName, fileSize);
        // 배열에 넣은 filePath, fileName, fileSize 로 핸들러 생성
        addMmsContentsHandler(null);
        // 텍스트나 이미지나 사운드 내용을 모두 innerHTML 로 가져올 수 있다.
        tinyMCE.get('template').setContent(content + "\n" + obj.innerHTML);
    }
</script>
</head>
<c:if test="${fileType eq 'T'}">
 <div class="row justify-content-end my-3"><!-- search -->
     <div class="col-12 justify-content-end">
         <div class="search_input">
             <input type="search" class="form-control form-control-sm" id="contsTxt" maxlength="30" aria-controls="datatable-basic" value="${param.contsTxt}" placeholder="<spring:message code='template.menu.mcontent'/>"/>
             <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                 <spring:message code="button.search"/><!-- 검색 -->
             </button>
         </div>
     </div>
 </div>
 <!-- //search -->
 <!-- message sample list -->
 <div class="tmplWrap">
     <ul>
     <c:forEach var="list" items="${mmsContentsList}">
        <li>
              <div class="text-area" id="conts${i}" class="sms_input hand" readonly="readonly">${list.contsTxt}</div>
         </li>
        </c:forEach>
     </ul>
 </div>
</c:if>

<c:if test="${fileType eq 'I'}">
<div class="tmplWrap tmpl-image">
    <ul>
        <c:forEach var="list" items="${mmsContentsList}">
        <li>
            <div class="image-area" onclick="javascript:insertContents(this, '${i}');">
                <img height="149px" width="203px"  src="${list.filePreviewPath}" />
            </div>
            <input type="hidden" id="filePath${i}" value="${list.filePath}">
            <input type="hidden" id="fileName${i}" value="${list.fileName}">
            <input type="hidden" id="fileSize${i}" value="${list.fileSize}">
        </li>
        </c:forEach>
    </ul>
</div>
</c:if>

<!-- 페이징 -->
<c:import url="/common/page.do">
    <c:param name="viewPath" value="/common/page_ajax"/>
    <c:param name="actionPath" value="/editor/mmsContsList.do"/><%-- /editor/mms_contents_list.do --%>
    <c:param name="total" value="${totalCount}"/>
    <c:param name="countPerPage" value="6"/>
    <c:param name="nowPage" value="${nowPage}"/>
    <c:param name="hiddenParam" value="fileType:${fileType}"/>
    <c:param name="returnDocumentId" value="tmplWrap"/>
</c:import>
