<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계>캠페인 미리보기(팝업)] 하단 미리보기 내용 출력(메일)
 * - [이케어>이케어 등록>2단계>이케어 미리보기(팝업)] 하단 미리보기 내용 출력(메일)
 * - URL : /common/previewConts.do
 * - Controller : com.mnwise.wiseu.web.common.web.MailMimeViewController
 * - 이전 파일명 : mail_mime_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%@ include file="/jsp/include/plugin.jsp" %>
<script src="/js/common/base64.js"></script>
<script type="text/javascript">
    function showAttachHtml(fileNm, mode) {
        var deFileNm = Base64.decode(fileNm);
        var url = "";
        if(mode == 'preview' && (deFileNm.indexOf('.html') > -1 || deFileNm.indexOf('.htm') > -1)) {
            url += "/common/previewConts.do";  // /common/previewMime.do
        } else {
            url += "/common/mime_attachDown.do";
        }

        var customerNm = "${mimeVo.customerNm}"
        url += "?cmd=preview&serviceNo=${mimeVo.serviceNo}&customerKey=${mimeVo.customerKey}&customerNm="+ encodeURIComponent(customerNm)
            + "&customerEmail=${mimeVo.customerEmail}&serviceType=${mimeVo.type}&seg=${mimeVo.seg}&fileNm="+encodeURIComponent(fileNm);

        $.mdf.popupGet(url, 'previewPopup', 820, 600);
    }

    <c:if test="${mimeVo.resultView eq true}">
    function reSend() {
        $("input[name=targetMail]").val($("#targetMail").val());
        $("#reSendFrm").submit();
    }
    </c:if>

    <c:if test="${mimeVo.resendButton eq true}">
    var isSubmitted = false;
    function reSendHistory() {
        if(isSubmitted == false) {
            isSubmitted = true;
            $("input[name=targetMail]").val($("#targetMail").val());
            $("#reSendHistoryFrm").submit();
        }
    }
    </c:if>
</script>

<!-- 발송마임보기 재발송 버튼 -->
<c:if test="${!empty mimeVo.resultView}">
<div class="form-inline m-2">
    <input type="text" class="form-control form-control-sm w-25 mx-2" id="targetMail" />
    <button type="button" class="btn btn-sm btn-outline-primary" onclick="javascript:reSend(); return false;">
        <spring:message code="button.send"/><!-- 발송 -->
    </button>
    <form id="reSendFrm" name="reSendFrm" action="/common/resendConts.do" method="post"><!-- /common/resendMime.do -->
        <input type="hidden" name="cmd" value="reSend" />
        <input type="hidden" name="type" value="${mimeVo.type}" />
        <input type="hidden" name="serviceNo" value="${mimeVo.serviceNo}" />
        <input type="hidden" name="customerKey" value="${mimeVo.customerKey}" />
        <input type="hidden" name="customerNm" value="${mimeVo.customerNm}" />
        <input type="hidden" name="customerEmail" value="${mimeVo.customerEmail}" />
        <input type="hidden" name="resultSeq" value="${mimeVo.resultSeq}" />
        <input type="hidden" name="targetMail" />
    </form>
</div>
</c:if>

<!-- 발송결과보기 재발송 버튼. 미리등록된 준실시간 서비스로 대상자 정보를 넘겨 발송한다. -->
<c:if test="${mimeVo.resendButton eq true}">
<div class="form-inline m-2">
    <span class="font-weight-bold">* <spring:message code="common.menu.resend.message2" /> : </span>
    <input type="text" class="form-control form-control-sm w-25 mx-2" value="${mimeVo.customerEmail}" id="targetMail" />
    <button type="button" class="btn btn-sm btn-outline-primary" onclick="javascript:reSendHistory(); return false;">
        <spring:message code="button.send"/><!-- 발송 -->
    </button>
    <form id="reSendHistoryFrm" name="reSendHistoryFrm" action="/common/resendHistoryMime.do" method="post">
        <input type="hidden" name="cmd" value="reSendHistory" />
        <input type="hidden" name="serviceNo" value="${mimeVo.serviceNo}" />
        <input type="hidden" name="condition" value="${mimeVo.condition}" />
        <input type="hidden" name="targetMail" />
    </form>
</div>
</c:if>

<table class="table table-sm dataTable table-fixed border-top-0 border-bottom-0" style="margin-top: 0 !important; margin-bottom: 0 !important;">
    <colgroup>
        <col width="100" />
        <col />
    </colgroup>
    <tbody>
        <tr>
            <th>TO</th>
            <td>&quot;${mimeVo.to}&quot;</td>
        </tr>
        <tr>
            <th>FROM</th>
            <td>&quot;${mimeVo.from}&quot;</td>
        </tr>
        <tr>
            <th>SUBJECT</th>
            <td>${mimeVo.subject}</td>
        </tr>
        <!-- 첨부파일 보기 -->
        <c:if test="${!empty mimeVo.attachFiles}">
        <tr>
            <td colspan="2">
                <c:forEach var="attach" items="${mimeVo.attachFileNames}" varStatus="i">
                <a href="javascript:showAttachHtml('${attach.value}','down');"><i class="fas fa-download"></i></a>
                <a href="javascript:showAttachHtml('${attach.value}','preview');">${attach.key}</a>
                </c:forEach>
            </td>
        </tr>
        </c:if>
    </tbody>
</table>
<!-- 메일내용 -->
<div>${mimeVo.mime}</div>
