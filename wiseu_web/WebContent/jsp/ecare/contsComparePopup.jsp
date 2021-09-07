<%-------------------------------------------------------------------------------------------------
 * - [이케어>컨텐츠 변경이력] 컨텐츠(템플릿/핸들러) 비교(팝업)
 * - URL : /ecare/contsComparePopup.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcareContsHistoryController
 * - 이전 파일명 : ecare_conts_history_compare.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="ecare.conts.campare"/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<style type="text/css">
    ins {background-color: #c6ffc6; text-decoration: none;}
    del {background-color: #ffc6c6;}
</style>
<script src="/plugin/text/diff_match_patch.js"></script>
<script src="/plugin/text/jquery.pretty-text-diff-1.0.3.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        $("#diffBox tr").prettyTextDiff({
             cleanup: true,
             originalContent: $('.compare_txt1').html(),
             changedContent: $('.compare_txt2').html(),
             diffContainer: ".compare_result"
        });
    }
</script>
</head>
<body>
<c:if test="${contsFlag eq 'T'}">
    <c:set var="conts1Nm" value="T"/>
    <c:set var="conts1Ver" value="${compareDataVo1.tmplVer}"/>
    <c:choose>
        <c:when test="${compareDataVo1.seg eq ' ' and compareDataVo1.channelType eq 'P'}">
            <c:set var="conts1Nm"><spring:message code='common.menu.inappmsg' /></c:set>
        </c:when>
        <c:when test="${fn:toLowerCase(compareDataVo1.seg) eq 'cover'}">
            <c:set var="conts1Nm"><spring:message code="ecare.conts.cover"/></c:set>
        </c:when>
        <c:when test="${fn:toLowerCase(compareDataVo1.seg) eq 'preface'}">
            <c:set var="conts1Nm"><spring:message code="ecare.conts.preface"/></c:set>
        </c:when>
        <c:otherwise>
        <%-- <c:when test="${compareDataVo1.seg eq ' ' and compareDataVo1.channelType ne 'P'}">
             <c:when test="${fn:toLowerCase(compareDataVo1.seg) eq 'popup'}"> --%>
            <c:set var="conts1Nm"><spring:message code="ecare.conts.contents"/></c:set>
        </c:otherwise>
    </c:choose>

    <c:set var="conts2Nm" value="T"/>
    <c:set var="conts2Ver" value="${compareDataVo2.tmplVer}"/>
    <c:choose>
        <c:when test="${compareDataVo2.seg eq ' ' and compareDataVo2.channelType eq 'P'}">
            <c:set var="conts2Nm"><spring:message code='common.menu.inappmsg' /></c:set>
        </c:when>
        <c:when test="${fn:toLowerCase(compareDataVo2.seg) eq 'cover'}">
            <c:set var="conts2Nm"><spring:message code="ecare.conts.cover"/></c:set>
        </c:when>
        <c:when test="${fn:toLowerCase(compareDataVo2.seg) eq 'preface'}">
            <c:set var="conts2Nm"><spring:message code="ecare.conts.preface"/></c:set>
        </c:when>
        <c:otherwise>
        <%-- <c:when test="${compareDataVo2.seg eq ' ' and compareDataVo2.channelType ne 'P'}">
             <c:when test="${fn:toLowerCase(compareDataVo2.seg) eq 'popup'}"> --%>
            <c:set var="conts2Nm"><spring:message code="ecare.conts.contents"/></c:set>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${contsFlag ne 'T'}">
    <c:set var="conts1Nm" ><spring:message code="ecare.conts.h"/></c:set>
    <c:set var="conts1Ver" value="${compareDataVo1.handlerVer}"/>
    <c:set var="conts2Nm" ><spring:message code="ecare.conts.h"/></c:set>
    <c:set var="conts2Ver" value="${compareDataVo2.handlerVer}"/>
</c:if>

<div class="card pop-card" id="diffBox">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="ecare.conts.campare"/></h5></div><!-- 컨텐츠 비교 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <form id="contsViewFrm" name="contsViewFrm" action="/ecare/contsComparePopup.do"><!-- ecare_conts_history_compare.do -->
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-sm dataTable">
                <thead class="thead-light">
                <tr>
                    <th width="33%">
                        <spring:message code="ecare.title_${webExecMode}"/> ${compareDataVo1.ecareNo}<spring:message code="ecare.channel.num"/>&nbsp;
                        ${conts1Nm}&nbsp;-&nbsp;<spring:message code="ecare.conts.ver"/>&nbsp;${conts1Ver}
                        <br/>
                        (<fmt:parseDate value="${compareDataVo1.lastupdateDt}${compareDataVo1.lastupdateTm}" var="modifyDtm" pattern="yyyyMMddHHmmss" />
                            <fmt:formatDate value="${modifyDtm}" pattern="yy-MM-dd HH:mm:ss" />)
                    </th>
                    <th width="33%">
                        <spring:message code="ecare.title_${webExecMode}"/> ${compareDataVo2.ecareNo}<spring:message code="ecare.channel.num"/>&nbsp;
                        ${conts2Nm}&nbsp;-&nbsp;<spring:message code="ecare.conts.ver"/>&nbsp;${conts2Ver}
                        <br/>
                        (<fmt:parseDate value="${compareDataVo2.lastupdateDt}${compareDataVo2.lastupdateTm}" var="modifyDtm" pattern="yyyyMMddHHmmss" />
                            <fmt:formatDate value="${modifyDtm}" pattern="yy-MM-dd HH:mm:ss" />)
                    </th>
                    <th width="33%">
                        <spring:message code="ecare.conts.campare2"/>
                    </th>
                </tr>
                </thead>
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${contsFlag eq 'T'}">
                                <textarea class="compare_txt1 original border" id="textmode" name="textmode" style="width:98%;  height: 560px; word-break: break-all; font-size: 11px; display: inline; resize: none;" readonly="readonly">${compareDataVo1.template}</textarea>
                            </c:when>
                            <c:otherwise>
                                <textarea class="compare_txt1 original border" id="textmode" name="textmode" style="width:98%;  height: 560px; word-break: break-all; font-size: 11px; display: inline; resize: none;" readonly="readonly">${compareDataVo1.source}</textarea>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${contsFlag eq 'T'}">
                                 <textarea class="compare_txt2 changed border" id="textmode" name="textmode" style="width: 98%; height: 560px; word-break: break-all; font-size: 11px; display: inline; resize: none;" readonly="readonly">${compareDataVo2.template}</textarea>
                            </c:when>
                            <c:otherwise>
                                <textarea class="compare_txt2 changed border" id="textmode" name="textmode" style="width: 98%; height: 560px; word-break: break-all; font-size: 11px; display: inline; resize: none;" readonly="readonly">${compareDataVo2.source}</textarea>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div class="compare_result border" id="textmode" name="textmode" style="text-align:left; overflow:scroll; width: 100%; height: 560px; word-break: break-all; font-size: 11px; background-color: #eeeeee;" readonly="readonly"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    </form>
</div>
</body>
</html>