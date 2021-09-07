<%-------------------------------------------------------------------------------------------------
 * Title       : 캠페인 리포트 상단 공통 인클루드
 * Description : 캠페인 리포트 상단의 공통 인클루드이다. 인클루드에서 사용할
 *               데이타는 리포트 인터셉터에서 실어서 보내준다.
 * - 이전 파일명 : campaign_report_head_inc.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initHeadEventBind();
    });

    function initHeadEventBind() {
        // 목록 버튼 클릭
        $("#listBtn").on("click", function(event) {
            var form = $('<form>', {action : '/report/campaign/campaignRptList.do', method : 'post', target : '_self'});

            form.append($('<input>', {type: 'hidden', name: 'searchChannel',  value: '${param.channelType}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchQstartDt', value: '${param.searchQstartDt}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchQendDt',   value: '${param.searchQendDt}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchWord',     value: '${param.searchWord}'}));

            form.appendTo('body').submit();
        });

        // 템플릿 보기 버튼 클릭
        $('#templateViewBtn').on("click", function(event) {
            if('${campaignReportBasicVo.scenarioNo}' == "") {
                alert("<spring:message code='report.campaign.head.alert.msg1' />");  // 캠페인을 선택하지 않으셨습니다. 목록에서 캠페인을 선택한 이후에 템플릿 보기를 진행해 주세요.
                return;
            }

            var seg = $.mdf.defaultIfBlank($("#seg").val(), "");

            var url = "/report/campaign/templateViewPopup.do?campaignNo=${campaignReportBasicVo.campaignNo}&seg=" + seg;  // /report/campaign/template_view.do
            $.mdf.popupGet(url, "tmplViewPopup", 800, 600);
        });
    }
</script>

<div class="card-body pb-0 px-0">
    <div class="row align-items-center py-1 table_option">
        <div class="col-11"><!-- tabs -->
            <ul class="nav nav-tabs tab02" id="tabs-text" role="tablist">
                <li class="nav-item">
                    <a class="nav-link mb-sm-3 mb-md-0 ${menuInfo[2][2]}" href="${menuInfo[2][0]}&channelType=${campaignReportBasicVo.channelType}&searchQstartDt=${searchQstartDt}&searchQendDt=${searchQendDt}&searchWord=${searchWord}">
                        <spring:message code="report.campaign.head.alt.allsummary"/><!-- 전체요약 -->
                    </a>
                </li>
                <%--
                <li class="nav-item">
                    <a class="nav-link mb-sm-3 mb-md-0" href="summary_view.html">
                        <spring:message code="report.campaign.head.alt.summary"/><!-- 요약분석 -->
                    </a>
                </li>
                --%>
                <li class="nav-item">
                    <a class="nav-link mb-sm-3 mb-md-0 ${menuInfo[1][2]}" href="${menuInfo[1][0]}&channelType=${campaignReportBasicVo.channelType}&searchQstartDt=${searchQstartDt}&searchQendDt=${searchQendDt}&searchWord=${searchWord}&resultSeq=${campaignReportBasicVo.resultSeq}">
                        <spring:message code="report.campaign.head.alt.error"/><!-- 오류분석 -->
                    </a>
                </li>
                <!-- 이메일 한정 -->
                <c:if test="${campaignReportBasicVo.channelType eq 'M'}">
                    <li class="nav-item">
                        <a class="nav-link mb-sm-3 mb-md-0 ${menuInfo[4][2]}" href="${menuInfo[4][0]}&channelType=${campaignReportBasicVo.channelType}&searchQstartDt=${searchQstartDt}&searchQendDt=${searchQendDt}&searchWord=${searchWord}&resultSeq=${campaignReportBasicVo.resultSeq}">
                           <spring:message code="report.campaign.head.alt.domain"/><!-- 도메인별 분석 -->
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link mb-sm-3 mb-md-0 ${menuInfo[5][2]}" href="${menuInfo[5][0]}&channelType=${campaignReportBasicVo.channelType}&searchQstartDt=${searchQstartDt}&searchQendDt=${searchQendDt}&searchWord=${searchWord}&resultSeq=${campaignReportBasicVo.resultSeq}">
                            <spring:message code="report.campaign.head.alt.link"/><!-- 링크클릭 분석 -->
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link mb-sm-3 mb-md-0 ${menuInfo[6][2]}" href="${menuInfo[6][0]}&channelType=${campaignReportBasicVo.channelType}&searchQstartDt=${searchQstartDt}&searchQendDt=${searchQendDt}&searchWord=${searchWord}&resultSeq=${campaignReportBasicVo.resultSeq}">
                           <spring:message code="report.campaign.head.alt.return"/><!-- 리턴메일 분석 -->
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link mb-sm-3 mb-md-0 ${menuInfo[7][2]}" href="${menuInfo[7][0]}&channelType=${campaignReportBasicVo.channelType}&searchQstartDt=${searchQstartDt}&searchQendDt=${searchQendDt}&searchWord=${searchWord}&resultSeq=${campaignReportBasicVo.resultSeq}">
                           <spring:message code="report.campaign.head.alt.reject"/><!-- 수신거부 분석 -->
                        </a>
                    </li>
                    <c:if test="${abTestType ne 'N' }">
                        <li class="nav-item">
                            <a class="nav-link mb-sm-3 mb-md-0 ${menuInfo[9][2]}" href="${menuInfo[9][0]}&channelType=${campaignReportBasicVo.channelType}&searchQstartDt=${searchQstartDt}&searchQendDt=${searchQendDt}&searchWord=${searchWord}&resultSeq=${campaignReportBasicVo.resultSeq}">
                             <spring:message code="report.campaign.head.alt.abtest"/><!-- AB 테스트 -->
                            </a>
                        </li>
                    </c:if>
                </c:if>
            </ul>
        </div>
        <div class="col-1 justify-content-end" style="padding-top: 10px;">
            <!-- /report/campaign/campaign_list.do -->
            <button type="button" class="btn btn-sm btn-outline-primary" id="listBtn">
                <i class="fas fa-list"></i> <spring:message code="button.list"/><!-- 목록 -->
            </button>
        </div>
    </div>

    <div class="table-responsive gridWrap">
        <table class="table table-sm dataTable table-fixed">
            <colgroup>
                <col width="12%" />
                <col width="*" />
                <col width="11%" />
                <col width="20%" />
                <col width="11%" />
                <col width="15%" />
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><spring:message code="report.campaign.list.title.name" /></th><!-- 캠페인명 -->
                <td colspan="3">
                    ${campaignReportBasicVo.scenarioNm}
                    <c:if test="${campaignReportBasicVo.channelType eq 'M'}">
                        <c:if test="${fn:length(templateList)>1}">
                            <div class="d-inline-block w-auto">
                                <select id="seg" name="seg" class="form-control form-control-sm">
                                    <c:set var="segName" value="" />
                                    <c:set var="segSelected" value="" />
                                    <c:forEach var="loop" items="${templateList}" varStatus="status">
                                        <c:choose>
                                            <c:when test="${loop.seg == ' '}">
                                                <spring:message code="report.campaign.head.default.template" /><!-- 기본 템플릿 -->
                                                <c:set var="segName" value="<spring:message code='report.campaign.head.default.template' />" /><!-- 기본 템플릿 -->
                                                <c:set var="segSelected" value="selected" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="segName" value="${loop.seg}" />
                                                <c:set var="segSelected" value="" />
                                            </c:otherwise>
                                        </c:choose>

                                        <c:choose>
                                            <c:when test="${abTestType eq 'T' }">
                                                <c:choose>
                                                    <c:when test="${segName eq 'AB'}">
                                                        <option value="${loop.seg}" ${segSelected}>B</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${loop.seg}" ${segSelected}>A</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${loop.seg}" ${segSelected}>${segName}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>

                        <button class="btn btn-sm btn-outline-primary btn-round ml-3" id="templateViewBtn">
                            <i class="fas fa-eye"></i> <spring:message code="button.view.template" /><!-- 템플릿 보기 -->
                        </button>
                    </c:if>
                </td>
                <th scope="row"><spring:message code='campaign.menu.cno' /></th><!-- 캠페인 번호 -->
                <td>
                    <em class="txt_channel ${campaignReportBasicVo.channelType}">
                        <c:choose>
                            <c:when test="${campaignReportBasicVo.channelType eq 'M'}">E</c:when>
                            <c:when test="${campaignReportBasicVo.channelType eq 'T'}">M</c:when>
                            <c:otherwise>${campaignReportBasicVo.channelType}</c:otherwise>
                        </c:choose>
                    </em>
                    <span>${campaignReportBasicVo.campaignNo}</span>
                </td>
            </tr>
            <tr>
                <th scope="row"><spring:message code='report.campaign.head.title.form' /></th><!-- 캠페인 형태 -->
                <td>${campaignReportBasicVo.relationTypeNm}</td>
                <th scope="row"><spring:message code='report.campaign.head.title.grp' /></th><!-- 요청 부서 -->
                <td>${campaignReportBasicVo.grpNm}</td>
                <th scope="row"><spring:message code='report.campaign.head.title.user' /></th><!-- 담당자 -->
                <td>${campaignReportBasicVo.nameKor} </td>
            </tr>
            <tr>
                <th scope="row"><spring:message code='report.campaign.head.title.send.date' /></th><!-- 발송수행일 -->
                <td>
                    <fmt:parseDate value="${campaignReportBasicVo.sendstartDt}${campaignReportBasicVo.sendstartTm}" var="sendstartDtm" pattern="yyyyMMddHHmmss" />
                    <fmt:formatDate value="${sendstartDtm}" pattern="yyyy-MM-dd HH:mm" />
                    ~
                    <fmt:parseDate value="${campaignReportBasicVo.sendEndDt}${campaignReportBasicVo.sendEndTm}" var="sendEndDtm" pattern="yyyyMMddHHmmss" />
                    <fmt:formatDate value="${sendEndDtm}" pattern="yyyy-MM-dd HH:mm" />
                </td>
                <th scope="row"><spring:message code='report.campaign.head.title.send.time' /></th><!-- 발송시간 -->
                <td>${campaignReportBasicVo.sendDurationTm}</td>
                <th scope="row"><spring:message code="report.campaign.head.title.status" /></th><!-- 상태 -->
                <td>${campaignReportBasicVo.campaignStsNm}</td>
            </tr>
            <tr>
                <th scope="row"><spring:message code='report.campaign.head.title.send.status' /></th><!-- 발송현황 -->
                <td colspan="5">
                    <span class="mr-3"><spring:message code='report.campaign.head.title.target.cnt'/> : <fmt:formatNumber type="number" value="${campaignReportBasicVo.targetCnt}" /><spring:message code='report.campaign.head.title.cnt' /></span>
                    <span class="mr-3"><spring:message code='report.campaign.head.title.send.cnt' /> : <fmt:formatNumber type="number" value="${campaignReportBasicVo.sendCnt}" /> <spring:message code='report.campaign.head.title.cnt' /></span>
                    <span class="mr-3"><spring:message code='report.campaign.head.title.success.cnt' /> : <fmt:formatNumber type="number" value="${campaignReportBasicVo.successCnt}" /> <spring:message code='report.campaign.head.title.cnt' /></span>
                    <span><spring:message code='report.campaign.head.title.fail.cnt' /> : <fmt:formatNumber type="number" value="${campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt}" /> <spring:message code='report.campaign.head.title.cnt' /></span>

                    <c:if test="${abTestType ne 'N'}">
                    <span><spring:message code='campaign.divide.test' /> : <fmt:formatNumber type="number" value="${campaignReportBasicVo.abTestTarget}" /> <spring:message code='report.campaign.head.title.cnt' /></span>
                    </c:if>
                </td>
            </tr>
            </tbody>
        </table>
    </div><!-- e.Light table -->
</div>