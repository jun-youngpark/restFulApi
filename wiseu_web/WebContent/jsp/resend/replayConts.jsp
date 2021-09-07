<%-------------------------------------------------------------------------------------------------
 * 발송 요약정보, 재발송버튼, 다시보기
 * - 이전 파일명 : previous_template_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<%@ include file="/jsp/include/plugin.jsp"%>
<script src="/js/common/base64.js"></script>
<script type="text/javascript">
    function showAttachHtml(fileNm, mode) {
        var deFileNm = Base64.decode(fileNm);
        var url = "/resend/replayConts.do?";
        if(! (mode == 'preview' && (deFileNm.indexOf('.html') > -1 || deFileNm.indexOf('.htm') > -1))) {
             url += "&mode=down";
        }

        var subType = "${hisVo.subType}";
        if('${hisVo.subType}' =='N') {
            url += "&seq=${hisVo.seq}";
        } else {
            url += "&customerKey=${hisVo.customerKey}";
        }

        url += "&serviceNo=${hisVo.serviceNo}&resultSeq=${hisVo.resultSeq}&listSeq=${hisVo.listSeq}&subType=${hisVo.subType}"
            + "&errorCd=${hisVo.errorCd}&channel=M&client=${mimeVo.type}&fileNm=" + encodeURIComponent(fileNm);
        $.mdf.popupGet(url, 'previewPopup', 820, 600);
    }
</script>

<table class="table table-sm dataTable table-fixed border-bottom-0">
    <tbody>
    <!-- 메일 채널 -->
    <c:if test="${channel eq 'M'}">
    <tr><!-- To -->
        <th width="15%"><spring:message code="common.menu.header.to" /></th>
        <td width="85%">&quot;${mimeVo.to}&quot;</td>
    </tr>
    <tr><!-- From -->
        <th><spring:message code="common.menu.header.from" /></th>
        <td>&quot;${mimeVo.from}&quot;</td>
    </tr>
    <tr><!-- 제목 -->
        <th><spring:message code="common.menu.header.subject" /></th>
        <td>${mimeVo.subject}</td>
    </tr>

    <c:if test="${!empty mimeVo.attachFiles}">
    <tr><!-- 첨부파일 -->
        <td colspan="2">
            <c:forEach var="attach" items="${mimeVo.attachFileNames}" varStatus="i">
                <a href="javascript:showAttachHtml('${attach.value}','down');"><i class="fas fa-download"></i></a>
                <a href="javascript:showAttachHtml('${attach.value}','preview');">${attach.key}</a>
            </c:forEach>
        </td>
    </tr>
    </c:if>

    <tr><!-- 메일 내용 -->
        <td colspan="4">
            <div class="img">${mimeVo.mime}</div>
        </td>
    </tr>
    </c:if><!-- // 메일 채널 -->

    <!-- FAX/문자/알림톡 채널 -->
    <c:if test="${channel eq 'S' || channel eq 'F' || channel eq 'A'}">
    <tr><!-- FAX/문자/알림톡 내용 -->
        <td colspan="4">
            <div class="img pre-line">${content}</div>
        </td>
    </tr>
    </c:if>

    <!-- LMS/MMS 채널 -->
    <c:if test="${channel eq 'T'}">
    <tr>
        <th width="15%"><spring:message code="common.menu.header.subject" /></th><!-- 제목 -->
        <td width="85%">${subject}</td>
    </tr>
    <tr>
        <td colspan="4">
            <div class="img pre-line">${content}
            <c:forEach var="imgPath" items="${imgPaths}" >
                <c:if test="${not empty imgPath}">
                    <img style="width: 50%; height: 250px" src="<c:out value="${imgPath}" escapeXml="false"/>"></img>
                </c:if>
            </c:forEach>
            </div>
        </td>
    </tr>
    </c:if><!-- // LMS/MMS 채널 -->

    <!-- 친구톡/브랜드톡 채널 -->
    <c:if test="${channel eq 'C' || channel eq 'B'}">
    <tr>
        <td colspan="4">
            <div class="img pre-line">${content}</div>
        </td>
    </tr>
     <c:if test="${not empty imgPath}">
    <tr>
        <td colspan="4">
            <div class="img">
                <img style="width: 50%; height: 250px" src="<c:out value="${imgPath}" escapeXml="false"/>"></img>
            </div>
        </td>
    </tr>
    </c:if>
    </c:if><!-- // 친구톡/브랜드톡 채널 -->

    <!-- 푸시 채널 -->
    <c:if test="${channel eq 'P'}">
    <c:set var="enter" value="\n" />
    <tr>
        <th width="15%"><spring:message code="common.menu.header.subject" /></th>
        <td width="85%">${content.TITLE}</td>
    </tr>
    <tr>
        <th><spring:message code="common.menu.header.os" /></th>
        <td>${content.OS_INFO}</td>
    </tr>
    <tr>
        <th><spring:message code="common.menu.header.receive.noti" /></th>
        <td>
            <c:set var="openDt" value="" />
            <c:choose>
                <c:when test="${not empty content.OPEN_DT}">
                    <fmt:parseDate value="${content.OPEN_DT}" var="openDt" pattern="yyyyMMddHHmmss" />
                </c:when>
                <c:otherwise>
                    <fmt:parseDate value="${content.CLICK_DT}" var="openDt" pattern="yyyyMMddHHmmss" />
                </c:otherwise>
            </c:choose>
            <fmt:formatDate value="${openDt}" pattern="yyyy-MM-dd HH:mm:ss" />
        </td>
    </tr>
    <tr>
        <th><spring:message code="common.menu.header.noti.message" /></th>
        <td>${fn:replace(content.POP_CONTENTS, enter, '<br/>')}</td>
    </tr>
    <c:if test="${'Y' eq PUSH_POP_BIG_IMG_USE}">
    <tr>
        <th><spring:message code="common.menu.header.large.image" /></th>
        <td>${content.STS_URL}</td>
    </tr>
    </c:if>
    <tr>
        <th><spring:message code="common.menu.header.app.message" /></th>
        <td>${fn:replace(content.CONTENTS,enter,'<br/>')}</td>
    </tr>
    </c:if><!-- //푸시 채널 -->
    </tbody>
</table>