<%-------------------------------------------------------------------------------------------------
 * - [에디터>템플릿 목록 선택하기] 카카오 템플릿 목록 선택<br/>
 * - URL : /editor/brtContentView.do <br/>
 * - Controller :  com.mnwise.wiseu.web.editor.web.EditortKakaoController <br/>
 * Title  : 이케어 등록 이케어 수행 ㅣ2 STEP 화면
 * - 이전 파일명 : kakaoContentView.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<head>
<script type="text/javascript">
    $(document).ready(function() {
    	initBrtViewPage();
    });

    function initBrtViewPage() {
        var contsTxt = $("input[name=contsTxt]").val().replace(/\n/g, "<br/>");
        var filePreviewPath = $("input[name=filePreviewPath]").val();
        var kakaoTemplateCd = $("input[name=kakaoTemplateCd]").val();
        var kakaoButtons = $("textarea[name=kakaoButtons]").val();
        var senderKey = $("input[name=senderKey]").val();
        var contsNo = '${kakaoContent.contsNo}';
        if($.mdf.isNotBlank(contsNo)){
            setEditor(contsTxt, filePreviewPath, kakaoButtons, kakaoTemplateCd, contsNo, senderKey);
        }
    }
</script>
</head>

<form id="editor" name="editor">
    <input type="hidden" id="fileName" name="fileName" value="${kakaoContent.fileName}" >
    <input type="hidden" id="filePath" name="filePath" value="${kakaoContent.filePath}" >
    <input type="hidden" id="filePreviewPath" name="filePreviewPath" value="${kakaoContent.filePreviewPath}" />
    <input type="hidden" id="kakaoTemplateCd" name="kakaoTemplateCd" value="${kakaoContent.kakaoTmplCd}" />
    <input type="hidden" id="senderKey" name="senderKey" value="${kakaoContent.kakaoSenderKey}" />
    <textarea id="kakaoButtons" name="kakaoButtons" class="dp-none">${wiseu:convertJsonToHtmlTrElement(kakaoContent.kakaoButtons)}</textarea>
    <input type="hidden" id="contsTxt" name="contsTxt" value="${kakaoContent.contsTxt}" >
</form>

<div class="table-responsive gridWrap">
    <table class="table table-sm dataTable table-fixed" style="margin-top: 0px !important;">
        <colgroup>
            <col width="135">
            <col width="*">
        </colgroup>
        <tbody>
        <tr>
            <th scope="row"><spring:message code="template.column.yellowid" /></th><!-- 카카오톡 채널ID -->
            <td class="text-break">${kakaoContent.kakaoYellowId}</td>
        </tr>
        <tr>
            <th scope="row"><spring:message code="template.menu.mcname" /></th><!-- 템플릿명 -->
            <td class="text-break">${kakaoContent.contsNm}</td>
        </tr>
        <tr>
            <th scope="row"><spring:message code="template.menu.mcontent" /></th><!-- 템플릿 내용 -->
            <td class="text-break">${kakaoContent.contsTxt}</td>
        </tr>
        <tr>
            <th scope="row"><spring:message code="template.menu.message.type" /></th><!-- 메세지 유형 -->
            <td class="text-break">
                <c:if test="${kakaoContent.messageType eq 'I'}"><spring:message code="template.brandtalk.messageType.I"/><!-- 기본 이미지 --></c:if>
                <c:if test="${kakaoContent.messageType eq 'W'}"><spring:message code="template.brandtalk.messageType.W"/><!-- 와이드 이미지 --></c:if>
            </td>
        </tr>
        <tr>
            <th scope="row" class="ls--1px"><spring:message code="template.brandtalk.unsubscribeContent" /></th><!-- 수신거부 전화번호 -->
            <td class="text-break">${kakaoContent.unsubscribeContent}</td>
        </tr>
        <tr>
            <th scope="row" class="ls--1px"><spring:message code="template.brandtalk.link"/></th><!-- 브랜드톡 이미지 링크 -->
            <td class="text-break">${kakaoContent.imageLink}</td>
        </tr>
        <tr>
            <th scope="row"><spring:message code="template.column.tmplcd"/></th><!-- 템플릿 코드 -->
            <td class="text-break">${kakaoContent.kakaoTmplCd}</td>
        </tr>
        <tr>
            <th scope="row"><spring:message code='template.kakao.button' /></th><!-- 버튼 -->
            <td class="text-break">${kakaoContent.kakaoButtons}</td>
        </tr>
        <tr>
            <th scope="row"><spring:message code='template.brandtalk.contentType' /></th><!-- 텍스트 유형 -->
            <td class="text-break">
                <c:if test="${kakaoContent.contentType eq 'V'}"><spring:message code="template.brandtalk.contentType.V"/><!-- 변수형 --></c:if>
                <c:if test="${kakaoContent.contentType eq 'F'}"><spring:message code="template.brandtalk.contentType.F"/><!-- 고정형 --></c:if>
            </td>
        </tr>
        </tbody>
    </table>
</div>
