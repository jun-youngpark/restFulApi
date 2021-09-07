<%-------------------------------------------------------------------------------------------------
 * - [이케어>컨텐츠 변경이력] 컨텐츠(템플릿/핸들러) 내용조회(팝업)
 * - URL : /ecare/contsViewPopup.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcareContsHistoryController
 * - 이전 파일명 : ecare_conts_history_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code='ecare.conts.sear'/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        $("#preView").css("display", "none");
        $("#tab1").attr("src", "/images${lang}/ecare/tab_btn_viewsource_on.gif");
    }

    //에디터/HTML 변환
    function toggleEditor(val) {
        if(val === 1 || val === 2) {
            if(val === 1) {
                $("#sourceView").css("display", "inline");
            } else {
                $("#sourceView").css("display", "none");
            }

            if(val === 2) {
                $("#preView").css("display", "inline");
            } else {
                $("#preView").css("display", "none");
            }
        }
    }
</script>
</head>
<body>
<div class="card pop-card">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="ecare.conts.sear"/></h5></div><!-- 컨텐츠 내용 조회 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <div class="table-responsive gridWrap">
            <table class="table table-sm dataTable table-fixed">
            <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><spring:message code='ecare.conts.contens.sel'/></th>
                <td>
                    <c:choose>
                        <c:when test="${contsHistoryVo.seg eq ' ' and contsHistoryVo.channelType ne 'P'}"><spring:message code='ecare.conts.contents'/></c:when>
                        <c:when test="${contsHistoryVo.seg eq ' ' and contsHistoryVo.channelType eq 'P'}"><spring:message code='common.menu.inappmsg' /></c:when>
                        <c:when test="${fn:toLowerCase(contsHistoryVo.seg) eq 'cover'}"><spring:message code='ecare.conts.cover'/></c:when>
                        <c:when test="${fn:toLowerCase(contsHistoryVo.seg) eq 'preface'}"><spring:message code='ecare.conts.preface'/></c:when>
                        <c:when test="${fn:toLowerCase(contsHistoryVo.seg) eq 'popup'}"><spring:message code='ecare.conts.contents'/></c:when>
                        <c:otherwise><spring:message code='ecare.conts.h'/></c:otherwise>
                    </c:choose>
                </td>
                <th scope="row"><spring:message code='ecare.conts.ver'/></th>
                <td>
                    <c:choose>
                        <c:when test="${contsHistoryVo.contsFlag eq 'T'}">
                            ${contsHistoryVo.tmplVer}
                        </c:when>
                        <c:otherwise>
                            ${contsHistoryVo.handlerVer}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th scope="row"><spring:message code="ecare.conts.num_${webExecMode}"/></th>
                <td>${contsHistoryVo.ecareNo}</td>
                <th scope="row"><spring:message code="ecare.conts.name_${webExecMode}"/></th>
                <td>${contsHistoryVo.ecareNm}</td>
            </tr>
            <tr>
                <th scope="row"><spring:message code='ecare.conts.fixedHu'/></th>
                <td>${contsHistoryVo.userNm}</td>
                <th scope="row"><spring:message code='ecare.conts.fixedTime'/></th>
                <td>
                    <fmt:parseDate value="${contsHistoryVo.lastupdateDt}${contsHistoryVo.lastupdateTm}" var="modifyDtm" pattern="yyyyMMddHHmmss" />
                    <fmt:formatDate value="${modifyDtm}" pattern="yy-MM-dd HH:mm:ss" />
                </td>
            </tr>
            <tr>
                <th scope="row"><spring:message code='ecare.conts.reason'/></th>
                <td colspan="3">${contsHistoryVo.historyMsg}</td>
            </tr>
            </tbody>
            </table>
        </div><!-- //Light table -->

        <!-- 편집기 -->
        <c:if test="${contsHistoryVo.contsFlag eq 'T'}">
        <ul class="nav nav-tabs tab01 mb-1"><!-- tab -->
            <li class="nav-item">
                <a class="nav-link active" id="tab" data-toggle="tab" onclick="toggleEditor(1);">
                    <spring:message code="button.view.source"/><!-- 소스보기 -->
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="tab" data-toggle="tab" onclick="toggleEditor(2);">
                    <spring:message code="button.preview"/><!-- 미리보기 -->
                </a>
            </li>
        </ul>
        </c:if>

        <div class="text-area" style="height: 400px;">
            <c:choose>
                <c:when test="${contsHistoryVo.contsFlag eq 'T'}">
                    <textarea cols="10" class="form-control" id="sourceView" style="height: 396px;" readonly>${contsHistoryVo.template}</textarea>
                    <div id="preView" style="display:none;">${contsHistoryVo.previewTemplate}</div>
                </c:when>
                <c:otherwise>
                    <textarea cols="10" class="form-control" style="height: 396px;" readonly>${contsHistoryVo.source}</textarea>
                </c:otherwise>
            </c:choose>
        </div>
        <!-- //편집기 -->
    </div>
</div><!--content end -->
</body>
</html>