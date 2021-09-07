<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>메시지 작성] 친구톡 템플릿 리스트
 * - URL : /editor/frtContsList <br/>
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorMmsController <br/>
 * - 이전 파일명 : frtalk_image_editor.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<head>
<script type="text/javascript">
    $(document).ready(function() {
    });

    function insertContents(obj, index, contsNo) {
        //상단 대메뉴 campaing인지 ecare 인지 확인
        var top_menu = top.document.referrer.split('/')[3];
        if(top_menu == 'campaign') {
        	parent.$("input[name='campaignVo.kakaoImageNo']").val(contsNo);
        } else {
            $("input[name='ecareVo.kakaoImageNo']").val(contsNo);
        }
        var tinyMce = tinyMCE.get("template");
        var template = tinyMce.getContent({ format: "text"})
        template = template.replace(/[<]img[^>]+[>]/g, '');

        var imgTag = obj.innerHTML;
        imgTag = imgTag.replace('>', ' style=\"width: 100%;\" >')
        tinyMce.setContent(imgTag + template);
    }
</script>
</head>

<div class="tmplWrap tmpl-image">
<ul>
    <c:forEach var="list" items="${mmsContentsList}">
        <li>
            <input type="hidden" id="filePath${i}" value="${list.filePath}">
            <input type="hidden" id="fileName${i}" value="${list.fileName}">
            <input type="hidden" id="fileSize${i}" value="${list.fileSize}">
            <div class="image-area" onclick="javascript:insertContents(this, '${i}', '${list.contsNo}');">
                   <img src="${list.filePreviewPath}" />
            </div>
        </li>
        </c:forEach>
 </ul>
</div>

<!-- 페이징 -->
<c:import url="/common/page.do">
    <c:param name="viewPath" value="/common/page" />
    <c:param name="actionPath" value="/editor/frtContsList.do" /><%-- /editor/mms_contents_list.do --%>
    <c:param name="total" value="${totalCount}" />
    <c:param name="countPerPage" value="20" />
    <c:param name="nowPage" value="${nowPage}" />
    <c:param name="hiddenParam" value="C" />
</c:import>
