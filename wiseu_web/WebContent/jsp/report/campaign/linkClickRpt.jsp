<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 링크클릭 분석(메일)<br/>
 * - URL : /report/campaign/linkClickRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignLinkClickController <br/>
 * - 이전 파일명 : linkclick_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.title" /></title><!-- 캠페인 리포트 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 컨텐츠 반응분석 버튼 클릭
        $("#analyBtn").on("click", function(event) {
            // /report/campaign/template_view_popup.do
            var url = '/report/campaign/templateViewPopupByLinkClick.do?scenarioNo=${campaignReportBasicVo.scenarioNo}&campaignNo=${campaignReportBasicVo.campaignNo}';
            $.mdf.popupGet(url, 'analyPopup', 800, 600);
        });

        // 타겟발송 버튼 클릭
        $("#targetSendBtn").on("click", function(event) {
            if($("#campaignReportFrm input:checkbox[name=targetChk]:checked").length == 0) {
                alert("<spring:message code='report.campaign.linkclick.js.targetsend.1' />");  // 타겟발송할 링크를 선택하세요.
                return;
            }

            if(!confirm("<spring:message code='report.campaign.linkclick.js.targetsend.2' />")) {  // 선택한 링크를 클릭한 대상자들에게 타겟발송 하시겠습니까?
                return;
            }

            var linkSeq = "";
            var i = 0;

            $("input[name='targetChk']:checkbox:checked").each(function() {
                if(i == 0) {
                    linkSeq += $(this).val();
                } else {
                    linkSeq += "," + $(this).val();
                }
            });

            $("#resendMode").val("L_resendLink");
            $("#linkSeq").val(linkSeq);

            $("#campaignReportFrm").attr('action', '/campaign/resend.do').submit();
        });
    }
</script>
</head>

<body>
<form id="campaignReportFrm" name="campaignReportFrm" method="post">
<input type="hidden" name="scenarioNo" id="scenarioNo" value="${campaignReportBasicVo.scenarioNo}" />
<input type="hidden" name="campaignNo" id="campaignNo" value="${campaignReportBasicVo.campaignNo}" />
<input type="hidden" name="channelType" id="channelType" value="${campaignReportBasicVo.channelType}" />
<input type="hidden" name="depthNo" id="depthNo" value="${campaignReportBasicVo.depthNo}" />
<input type="hidden" name="resendMode" id="resendMode" />
<input type="hidden" name="linkSeq" id="linkSeq" />
<input type="hidden" name="abTestType" value="${campaignReportBasicVo.abTestType}"/>
<input type="hidden" name="abTestTCond" value="${campaignReportBasicVo.abTestCond}"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.linkclick.title.alt.1"/></h3><!-- 링크클릭반응결과 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-6 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.campaign.linkclick.title.alt.1"/></h4><!-- 링크클릭반응결과 -->
                    </div>
                    <div class="col-6 justify-content-end">
                        <button class="btn btn-sm btn-outline-primary" id="analyBtn">
                            <i class="fas fa-chart-bar"></i> <spring:message code="button.analy.linkclick" /><!-- 컨텐츠 반응분석 -->
                        </button>
                        <button class="btn btn-sm btn-outline-primary" id="targetSendBtn">
                            <i class="fas fa-paper-plane"></i> <spring:message code="button.send.target" /><!-- 타겟 발송 -->
                        </button>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col" rowspan="2" width="40">No</th>
                        <th scope="col" rowspan="2"><spring:message code="report.campaign.list.title.linkurl" /></th><!-- URL/이미지 -->
                        <th scope="col" colspan="2" width="20%"><spring:message code="report.campaign.result.comparison.click.count"/></th><!-- 클릭 수 -->
                        <th scope="col" colspan="2" width="20%"><spring:message code="report.campaign.list.title.oneclick" /></th><!-- 1회 클릭 -->
                        <th scope="col" colspan="2" width="20%"><spring:message code="report.campaign.list.title.moreclick" /></th><!-- 2회 이상 클릭 -->
                        <th scope="col" rowspan="2" width="45"><spring:message code="report.campaign.list.title.select" /></th><!-- 선택 -->
                    </tr>
                    <tr>
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 클릭 수 : 건수 -->
                        <th scope="col">%</th><!-- 클릭 수 : % -->
                        <th scope="col"><spring:message code="report.campaign.list.title.person" /></th><!-- 1회 클릭 : 명 -->
                        <th scope="col">%</th><!-- 1회 클릭 : % -->
                        <th scope="col"><spring:message code="report.campaign.list.title.person" /></th><!-- 2회 이상 클릭 : 명 -->
                        <th scope="col">%</th><!-- 2회 이상 클릭 : % -->
                    </tr>
                    </thead>

                    <c:forEach var="loop" items="${campaignReportLinkClickList}">
                        <c:if test="${loop.gid eq 1}">
                            <c:set var="totalCnt" scope="page" value="${loop.totalCnt}"/>
                            <c:set var="oneClickCnt" scope="page" value="${loop.oneClickCnt}"/>
                            <c:set var="moreClickCnt" scope="page" value="${loop.moreClickCnt}"/>
                        </c:if>
                    </c:forEach>

                    <c:if test="${fn:length(campaignReportLinkClickList) > 0}">
                    <tbody>
                    <c:forEach var="loop" items="${campaignReportLinkClickList}">
                    <c:if test="${loop.gid eq 0}"><!-- 일반 데이터 -->
                    <tr>
                        <td>${loop.linkSeq}</td>
                        <td class="text-left">
                            <c:if test="${loop.linkTitle ne null}">${loop.linkTitle}</c:if>
                            ${loop.linkUrl}
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${loop.totalCnt}" /></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${loop.totalCnt eq 0}">0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" value="${loop.totalCnt / totalCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${loop.oneClickCnt}" /></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${loop.oneClickCnt eq 0}">0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" value="${loop.oneClickCnt / oneClickCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${loop.moreClickCnt}" /></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${loop.moreClickCnt eq 0}">0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" value="${loop.moreClickCnt / moreClickCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <input type="checkbox" name="targetChk" id="targetChk" value="${loop.linkSeq}" class="check" <c:if test="${((empty loop.oneClickCnt) or (loop.oneClickCnt eq 0)) and ((empty loop.moreClickCnt) or (loop.moreClickCnt eq 0))}">disabled</c:if>/>
                        </td>
                    </tr>
                    </c:if>

                    <c:if test="${loop.gid eq 1}"><!-- 합계  -->
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="2"><spring:message code="report.total" /></td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${loop.totalCnt}" /></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${loop.totalCnt eq 0}">0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" value="100" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${loop.oneClickCnt}" /></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${loop.oneClickCnt eq 0}">0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" value="100" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${loop.moreClickCnt}" /></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${loop.moreClickCnt eq 0}">0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" value="100" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td></td>
                    </tr>
                    </tfoot>
                    </c:if>
                    </c:forEach>
                    </c:if>

                    <c:if test="${fn:length(campaignReportLinkClickList) eq 0}">
                    <tbody>
                    <tr>
                        <td colspan="9"><spring:message code="report.campaign.list.linkclick.nodata" /></td><!-- 링크클릭 데이터가 존재하지 않습니다. -->
                    </tr>
                    </tbody>
                    </c:if>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div>
</form>
</body>
</html>
