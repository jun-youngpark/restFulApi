<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 고객이력] 캠페인 고객이력
 * - URL : /campaign/campaignSendLog.do
 * - Controller : com.mnwise.wiseu.web.campaign.web.CampaignPerHistoryController
 * - 이전 파일명 : campaign_per_history.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<title><spring:message code="campaign.msg.history"/></title><!-- 캠페인 고객이력 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 채널 콤보박스 선택
        $("select[name=channel]").on("change", function(event) {
            onChangeChannel();
        });

        // 검색조건 엔터키 입력
        $("#searchForm").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            event.preventDefault();

            var rules = {
                searchQstartDt : {notBlank : true},
                searchQendDt : {notBlank : true},
                campaignNo : {digits : true, maxlength : 15}
            };

            if($.mdf.validForm("#searchForm", rules) == false) {
                return;
            }

            if(!confirm("<spring:message code='common.alert.history.msg2'/>")) {  // 발송기간 이외의 검색 조건이 없을 경우 조회에 상당한 시간이 소요될 수 있습니다. \\n 계속 하시겠습니까?
                return;
            }

            $("#searchForm").attr('action', '/campaign/campaignSendLog.do').submit();  // /campaign/campaign_per_history.do
        });

        // 고객이력 목록에서 이력 선택
        $('#sendLogTable tbody').on('click', 'tr', function(event) {
            $('#sendLogTable tr.selected').removeClass('selected');
            $(this).addClass('selected');
        });
    }

    function initPage() {
        onChangeChannel();

        new mdf.Date("#searchQstartDt");  // 발송기간-시작일
        new mdf.Date("#searchQendDt");  // 발송기간-종료일
        new mdf.DataTable("#sendLogTable");
    }

    // 채널 콤보박스 선택
    function onChangeChannel() {
        var channel = $("#searchForm select[name=channel]").val();
        switch(channel) {
            case 'M' :
                $("#receiverInfo").html('<spring:message code="common.menu.remail"/>');  // 수신자 이메일
                $("#customerName").html('<spring:message code="common.menu.rname"/>');  // 수신자 이름
                $("#customerId").html('<spring:message code="common.menu.rid"/>');  // 수신자ID
                break;

            case 'F' :
            case 'S' :
            case 'T' :
            case 'P' :
            case 'C' :
            case 'B' :
                $("#receiverInfo").html('<spring:message code="common.menu.number"/>');  // 수신자 전화번호
                $("#customerName").html('<spring:message code="common.menu.info1"/>');  // 부가정보1
                $("#customerId").html('<spring:message code="common.menu.info2"/>');  // 부가정보2
                break;
        }

        $.ajax({
            url : "/campaign/selectErrorCdList.json?channelType="+channel,
            type : "post",
            async: false,
            success : function(sendErrList) {
                $("#errorCd").children('option:not(:first)').remove();
                $("#errorCd").append("<option>=====================</option>");
                $("#errorCd").append("<option value='FAIL'>[*] Fail</option>");
                $(sendErrList).each(function(i, sendErrVo) {
                    $("#errorCd").append("<option value='"+ sendErrVo.errorCd +"'>[" + sendErrVo.errorCd + "]" + sendErrVo.errorDesc + "</option>");
                });
            }
        });
    }

    function viewTemplate(serviceNo, customerKey, customerNm, customerEmail, resultSeq) {
        var url = "/common/replayMime.do?cmd=resultView&type=EM&serviceNo=" + serviceNo + "&customerKey=" + customerKey
                + "&customerNm=" + encodeURIComponent(customerNm) + "&customerEmail=" + customerEmail + "&resultSeq=" + resultSeq;

        $.mdf.popupGet(url, 'tmplViewPopup', 820, 600);
    }

    function viewTemplateMime(serviceNo, customerKey, customerNm, customerEmail, resultSeq) {
        var url = "/common/replayMime.do?cmd=resultView&type=EM&serviceNo=" + serviceNo + "&customerKey=" + customerKey
                + "&customerNm=" + encodeURIComponent(customerNm) + "&customerEmail=" + customerEmail + "&resultSeq=" + resultSeq;

        $.mdf.popupGet(url, 'tmplViewPopup', 820, 600);
    }

    // 다시보기 클릭
    function replayTemplate(serviceNo, listSeq, customerKey, resultSeq, errorCd, abTestType, slot1, seq) {
        if('' === $.trim(listSeq)) {
            alert('<spring:message code="common.alert.no.listseq"/>');  // 조회 결과에 List SEQ 값이 없습니다.
            return;
        }

        $.mdf.resetForm("#replayForm");

        $("#replayForm input[name=client]").val("EM");
        $("#replayForm input[name=serviceNo]").val(serviceNo);
        if($.mdf.isNotBlank(seq)) {
            $("#replayForm input[name=seq]").val(customerKey);
        }
        $("#replayForm input[name=customerKey]").val(customerKey);
        $("#replayForm input[name=resultSeq]").val(resultSeq);
        $("#replayForm input[name=listSeq]").val($.trim(listSeq));
        $("#replayForm input[name=subType]").val("S");
        $("#replayForm input[name=errorCd]").val(errorCd);
        $("#replayForm input[name=channel]").val($("#searchForm select[name=channel]").val());

        $.mdf.popupSubmit("#replayForm", '/resend/replayMainPopup.do', 'replayPopup', 830, 730);  // /resend/previous_template.do
    }
</script>
</head>

<body>
<!-- /campaign/campaign_per_history.do -->
<form id="searchForm" name="searchForm" action="/campaign/campaignSendLog.do" method="post"><!-- /campaign/campaign_per_history.do -->
<input type="hidden" name="cmd" id="cmd" />
<input type="hidden" name="resultSeq" id="resultSeq" />
<input type="hidden" name="errmsg" id="errmsg" />
<input type="hidden" name="reportNo" id="reportNo" />
<input type="hidden" name="recordSeq" id="recordSeq" />
<input type="hidden" name="listSeq"	id="listSeq" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card ">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="campaign.msg.history"/></h3><!-- 캠페인 고객이력 -->
            </div>

            <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="10%" />
                            <col width="16%" />
                            <col width="10%" />
                            <col width="16%" />
                            <col width="12%" />
                            <col width="*" />
                            <col width="100" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="resend.category"/></th><!-- 발송유형 -->
                            <td>
                                <select class="form-control form-control-sm"name="resendSts">
                                    <option value="All" <c:choose><c:when test="${searchResendSts eq 'All' }">selected</c:when></c:choose>><spring:message code="common.send.type.all" /></option><!-- 전체 -->
                                    <option value="Ori" <c:choose><c:when test="${searchResendSts eq 'Ori' }">selected</c:when></c:choose>><spring:message code="common.send.type.original" /></option><!-- 본발송 -->
                                    <option value="Re" <c:choose><c:when test="${searchResendSts eq 'Re' }">selected</c:when></c:choose>><spring:message code="common.send.type.resend" /></option><!-- 재발송 -->
                                </select>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="ecare.channel" /></th><!-- 채널 -->
                            <td>
                                <select class="form-control form-control-sm" name="channel">
                                <c:if test="${fn:contains(useChannelList, 'M')}">
                                    <option value="M" <c:choose><c:when test="${channel eq 'M' }">selected</c:when></c:choose>><spring:message code="common.channel.M"/></option><!-- EMAIL -->
                                </c:if>
                                <c:if test="${fn:contains(useChannelList, 'S')}">
                                    <option value="S" <c:choose><c:when test="${channel eq 'S' }">selected</c:when></c:choose>><spring:message code="common.channel.S"/></option><!-- SMS -->
                                </c:if>
                                <c:if test="${fn:contains(useChannelList, 'T')}">
                                    <option value="T" <c:choose><c:when test="${channel eq 'T' }">selected</c:when></c:choose>><spring:message code="common.channel.T"/></option><!-- LMS/MMS -->
                                </c:if>
                                <c:if test="${fn:contains(useChannelList, 'C')}">
                                    <option value="C" <c:choose><c:when test="${channel eq 'C' }">selected</c:when></c:choose>><spring:message code="common.channel.C"/></option><!-- 친구톡 -->
                                </c:if>
                                <c:if test="${fn:contains(useChannelList, 'B')}">
                                    <option value="B" <c:choose><c:when test="${channel eq 'B' }">selected</c:when></c:choose>><spring:message code="common.channel.B"/></option><!-- 브랜드톡 -->
                                </c:if>
                                <c:if test="${fn:contains(useChannelList, 'P')}">
                                    <option value="P" <c:choose><c:when test="${channel eq 'P' }">selected</c:when></c:choose>><spring:message code="common.channel.P"/></option><!-- PUSH -->
                                </c:if>
                                <c:if test="${fn:contains(useChannelList, 'F')}">
                                    <option value="F" <c:choose><c:when test="${channel eq 'F' }">selected</c:when></c:choose>><spring:message code="common.channel.F"/></option><!-- FAX -->
                                </c:if>
                                </select>
                             </td>
                            <th scope="row"><em class="required">required</em><spring:message code="common.menu.period"/></th><!-- 발송기간 -->
                            <td class="border-right-0">
                                <div class="periodWrap d-flex align-items-center">
                                    <div class="input_datebox">
                                        <input type="hidden" id="searchQstartDt" name="searchQstartDt" value="${searchQstartDt}" maxlength="10"/>
                                    </div>
                                    <span class="txt">~</span>
                                    <div class="input_datebox">
                                        <input type="hidden" id="searchQendDt" name="searchQendDt" value="${searchQendDt}" maxlength="10"/>
                                    </div>
                                </div>
                            </td>
                            <td rowspan="3" class="text-center">
                                <button class="btn btn-outline-info btn_search" id="searchBtn">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" id="customerName"><spring:message code="common.menu.rname"/></th><!-- 수신자 이름 -->
                            <td><input type="text" class="form-control form-control-sm" name="customerNm" value="${param.customerNm}" /></td>
                            <th scope="row" id="customerId"><spring:message code="common.menu.rid"/></th><!-- 수신자 ID -->
                            <td><input type="text" class="form-control form-control-sm" name="customerKey" value="${param.customerKey}"/></td>
                            <th scope="row" id="receiverInfo"><spring:message code="common.menu.remail"/></th><!-- 수신자 이메일 -->
                            <td><input type="text" class="form-control form-control-sm" name="customerEmail" value="${param.customerEmail}" /></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 -->
                            <td><input type="text" class="form-control form-control-sm" name="campaignNo" id="campaignNo" value="${param.campaignNo}" /></td>
                            <th scope="row"><spring:message code="campaign.table.cname"/></th><!-- 캠페인명 -->
                            <td><input type="text" class="form-control form-control-sm" name="campaignNm" value="${param.campaignNm}" /></td>
                            <th scope="row"><spring:message code="common.menu.sresult"/></th><!-- 발송결과 -->
                            <td>
                                <select name="errorCd" id="errorCd" class="form-control form-control-sm">
                                    <option value=""><spring:message code="common.menu.all"/></option><!-- 전체 -->
                                    <option value="">===================================================</option>
                                    <option value="FAIL" <c:if test="${errorCd eq 'FAIL' }">selected</c:if>>[*] <spring:message code="report.campaign.list.title.fail" /></option><!-- 실패 -->
                                    <c:forEach items="${sendErrList}" var="sendErrVo">
                                    <option value="${sendErrVo.errorCd}" <c:if test="${param.errorCd == sendErrVo.errorCd}">selected</c:if>>[${sendErrVo.errorCd}] ${sendErrVo.errorDesc}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->

                <div class="table-responsive overflow-x-hidden">
                    <!-- 문자, 카카오 채널 선택시 -->
                    <c:choose>
                    <c:when test="${channel eq 'S' || channel eq 'T' || channel eq 'C' || channel eq 'B' || channel eq 'A'}">
                    <table class="table table-xs dataTable table-hover table-fixed" id="sendLogTable">
                        <thead class="thead-light">
                            <tr>
                                <th scope="col" width="6%">No</th>
                                <th scope="col" width="6%"><spring:message code="campaign.menu.cno.br"/></th><!-- 캠페인번호 -->
                                <th scope="col" width="12%"><spring:message code="campaign.table.cname"/></th><!-- 캠페인명 -->
                                <th scope="col" width="50"><spring:message code="ecare.channel.trans" /></th><!-- 전송유형 -->
                                <th scope="col" width="95"><spring:message code="ecare.channel.personal_${webExecMode}"/></th><!-- 수신자 전화번호 -->
                                <th scope="col" width="55"><spring:message code="campaign.table.sdate"/></th><!-- 발송일시 -->
                                <th scope="col" width="*"><spring:message code="ecare.channel.content"/></th><!-- 발송내용 -->
                                <th scope="col" width="7%"><spring:message code="common.menu.sresult"/></th><!-- 발송결과 -->
                                <th scope="col" width="7%"><spring:message code="ecare.channel.resultcheck_${webExecMode}"/></th><!-- 대체발송여부 -->
                                <th scope="col" width="8%"><spring:message code="ecare.channel.sender"/>ID</th><!-- 발신자ID -->
                                <th scope="col" width="90"><spring:message code="ecare.channel.sender"/> <spring:message code="ecare.channel.num" /></th><!-- 발신자 번호 -->

                                <c:if test="${mimeSave eq 'on'}">
                                <th scope="col" width="46"><spring:message code="common.template.view.br"/></th><!-- 템플릿 보기 -->
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="perHistoryVo" items="${perHistoryList}" varStatus="perHistoryLoop">
                        <tr style="cursor: pointer;" onclick='javascript:replayTemplate("${perHistoryVo.campaignNo}", "${perHistoryVo.listSeq}","${perHistoryVo.customerKey}", "${perHistoryVo.resultSeq}", "${perHistoryVo.errorCd}","${perHistoryVo.abTestType}","${perHistoryVo.slot1}")'>
                            <td scope="row">${pgBean.totalRowCnt - (pgBean.currentPage - 1) * pgBean.pageSize - perHistoryLoop.index}</td><!-- No -->
                            <td title="${perHistoryVo.campaignNo}"><!-- 캠페인 NO -->
                                <nobr>${perHistoryVo.campaignNo}</nobr>
                            </td>
                            <td class="text-left" title="${perHistoryVo.campaignNm}"><!-- 캠페인명 -->
                                <c:choose>
                                    <c:when test="${perHistoryVo.resendSts eq 'T'}"><font style="color: green; ">[<spring:message code="resend.type.target" />]</font><!-- 단건재발송 --></c:when>
                                    <c:when test="${perHistoryVo.resendSts eq 'F'}"><font style="color: blue; ">[<spring:message code="resend.type.fail" />]</font><!-- 실패재발송 --></c:when>
                                </c:choose>
                                ${perHistoryVo.campaignNm}
                            </td>

                            <td><!-- 전송유형 -->
                                <c:choose>
                                    <c:when test="${channel eq 'S'}">SMS</c:when>
                                    <c:when test="${channel eq 'T'}">LMS<br/>/MMS</c:when>
                                    <c:when test="${channel eq 'B' || channel eq 'C'}">
                                        <spring:message code="common.channel.${channel}" />
                                        <c:choose>
                                            <c:when test="${perHistoryVo.msgType == 'S'}"><br/>→SMS</c:when>
                                            <c:when test="${perHistoryVo.msgType == 'L'}"><br/>→LMS</c:when>
                                            <c:when test="${perHistoryVo.msgType == 'T'}"><br/>→MMS</c:when>
                                        </c:choose>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td title="${wiseu:mailMask(perHistoryVo.customerEmail)}"><!-- 수신자 전화번호 -->
                                <nobr>${wiseu:mailMask(perHistoryVo.customerEmail)}</nobr>
                            </td>
                            <td title="[${perHiostyrVo.sendDt}][${perHistoryVo.sendTm}]"><!-- 발송일시 -->
                                <fmt:parseDate value="${perHistoryVo.sendDt}${perHistoryVo.sendTm}" var="sendDtm" pattern="yyyyMMddHHmmss"/>
                                <fmt:formatDate value="${sendDtm}" pattern="yy-MM-dd HH:mm:ss"/>
                            </td>
                            <td class="text-left text-break" title="${perHistoryVo.dispSndMsg}"><!-- 발송내용 -->
                                <c:choose>
                                    <c:when test="${perHistoryVo.resendSts eq 'T'}">
                                        <font style="color: green;">[<spring:message code="resend.type.target"/>]<!-- 단건재발송 --></font>
                                    </c:when>
                                </c:choose>
                                ${perHistoryVo.dispSndMsg}
                            </td>
                            <td title="[${perHistoryVo.errorCd}][${perHistoryVo.errMsgKor}]${perHistoryVo.errMsg}"><!-- 발송결과 -->
                                <c:choose>
                                    <c:when test="${channel eq 'A'}">${perHistoryVo.dispAltalkSendResult}</c:when>
                                    <c:when test="${channel eq 'C'}">${perHistoryVo.dispFrtalkSendResult}</c:when>
                                    <c:when test="${channel eq 'B'}">${perHistoryVo.dispBrtalkSendResult}</c:when>
                                    <c:otherwise>${perHistoryVo.dispSmsSendResult}</c:otherwise>
                                </c:choose>
                            </td>

                            <c:choose>
                            <c:when test="${channel eq 'S' || channel eq 'T'}">
                                <td><!-- 대체발송여부 -->
                                    <c:choose>
                                        <c:when test="${fn:trim(perHistoryVo.slot1) eq 'Y'}"><nobr>[${perHistoryVo.slot1}]</nobr></c:when>
                                        <c:otherwise><nobr>-</nobr></c:otherwise>
                                    </c:choose>
                                </td>
                            </c:when>
                            <c:otherwise><!-- 우회발송결과 -->
                                <td title="[${perHistoryVo.smsRsltCd}]${perHistoryVo.smsRsltMsg}">
                                   ${perHistoryVo.dispAlternateSmsSendResult}
                                </td>
                            </c:otherwise>
                            </c:choose>

                            <td class="text-left" title="${perHistoryVo.reqUserId}"><!-- 발신자사번 -->
                                <nobr>${perHistoryVo.reqUserId}</nobr>
                            </td>
                            <td title="${perHistoryVo.sender}"><!-- 발신자번호 -->
                                <nobr>${perHistoryVo.sender}</nobr>
                            </td>

                            <c:if test="${mimeSave eq 'on'}"><!-- 템플릿 보기 -->
                            <td onclick="event.cancelBubble=true">
                                <a href='javascript:viewTemplateMime("${perHistoryVo.campaignNo}", "${perHistoryVo.sendSeq}", "${perHistoryVo.customerNm}" ,"${perHistoryVo.customerEmail}", "${perHistoryVo.resultSeq}")'>
                                    <i class="fas fa-file-download fa-2x"></i>
                                </a>
                            </td>
                            </c:if>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    </c:when>

                    <c:otherwise><!-- 메일, FAX, PUSH 채널 선택시 -->
                    <table class="table table-xs table-hover dataTable table-fixed" id="sendLogTable">
                        <thead class="thead-light">
                            <tr>
                                <th scope="col" width="6%">No</th>
                                <th scope="col" width="6%"><spring:message code="campaign.menu.cno.br"/></th><!-- 캠페인번호 -->
                                <th scope="col" width="15%"><spring:message code="campaign.table.cname"/></th><!-- 캠페인명 -->
                                <th scope="col" width="15%"><!-- 수신자 이메일 -->
                                    <c:choose>
                                        <c:when test="${channel eq 'M'}"><spring:message code="common.menu.remail"/></c:when>
                                        <c:otherwise><spring:message code="common.menu.number"/><!-- 수신자 전화번호 --></c:otherwise>
                                    </c:choose>
                                </th>
                                <th scope="col" width="12%">
                                    <c:choose>
                                        <c:when test="${channel eq 'M'}"><spring:message code="common.menu.rname"/><!-- 수신자 이름 --></c:when>
                                        <c:otherwise><spring:message code="common.menu.info1"/><!-- 부가정보1 --></c:otherwise>
                                    </c:choose>
                                </th>
                                <th scope="col" width="12%">
                                    <c:choose>
                                        <c:when test="${channel eq 'M'}"><spring:message code="common.menu.rid"/><!-- 수신자ID --></c:when>
                                        <c:otherwise><spring:message code="common.menu.info2"/><!-- 부가정보1 --></c:otherwise>
                                    </c:choose>
                                </th>
                                <th scope="col" width="55"><spring:message code="campaign.table.sdate"/></th><!-- 발송일시 -->
                                <th scope="col" width="*"><spring:message code="common.menu.sresult"/></th><!-- 발송결과 -->

                                <c:if test="${channel eq 'M' || channel eq 'P'}">
                                <th scope="col" width="80"><spring:message code="common.menu.receipt.br"/></th><!-- 수신확인 en:width -->
                                </c:if>

                                <c:if test="${mimeSave eq 'on'}">
                                <th scope="col" width="36"><spring:message code="common.template.view.br"/></th><!-- 템플릿 보기 -->
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="perHistoryVo" items="${perHistoryList}" varStatus="perHistoryLoop">
                        <tr style="cursor: pointer;" onclick='javascript:replayTemplate("${perHistoryVo.campaignNo}", "${perHistoryVo.listSeq}","${perHistoryVo.customerKey}", "${perHistoryVo.resultSeq}", "${perHistoryVo.errorCd}","${perHistoryVo.abTestType}","${perHistoryVo.slot1}","${perHistoryVo.seq}")'>
                            <td scope="row">${pgBean.totalRowCnt - (pgBean.currentPage - 1) * pgBean.pageSize - perHistoryLoop.index}</th>
                            <td title="${perHistoryVo.campaignNo}"><!-- 캠페인 NO -->
                                <nobr>${perHistoryVo.campaignNo}</nobr>
                            </td>
                            <td class="text-left" title="${perHistoryVo.campaignNm}"><!-- 캠페인명 -->
                                <c:choose>
                                    <c:when test="${perHistoryVo.resendSts eq 'T'}"><font style="color: green; ">[<spring:message code="resend.type.target" />]</font></c:when>
                                    <c:when test="${perHistoryVo.resendSts eq 'F'}"><font style="color: blue; ">[<spring:message code="resend.type.fail" />]</font></c:when>
                                </c:choose>
                                ${perHistoryVo.campaignNm}
                            </td>


                            <td class="text-left" title="${wiseu:mailMask(perHistoryVo.customerEmail)}"><!-- 수신자 이메일 -->
                                <nobr>${wiseu:mailMask(perHistoryVo.customerEmail)}</nobr>
                            </td>

                            <c:choose>
                                <c:when test="${channel eq 'M'}">
                                    <td title="${wiseu:nameMask(perHistoryVo.customerNm)}"><!-- 수신자 이름 -->
                                        <nobr>${wiseu:nameMask(perHistoryVo.customerNm)}</nobr>
                                    </td>
                                    <td class="text-left" title="${perHistoryVo.customerKey}"><!-- 수신자ID -->
                                        <nobr>${perHistoryVo.customerKey}</nobr>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td class="text-left" title="${perHistoryVo.slot1}"><!-- 부가정보1 -->
                                        <nobr>${perHistoryVo.slot1}</nobr>
                                    </td>
                                    <td class="text-left" title="${perHistoryVo.slot2}"><!-- 부가정보2 -->
                                        <nobr>${perHistoryVo.slot2}</nobr>
                                    </td>
                                </c:otherwise>
                            </c:choose>

                            <td title="[${perHiostyrVo.sendDt}][${perHistoryVo.sendTm}]"><!-- 발송일시 -->
                                <fmt:parseDate value="${perHistoryVo.sendDt}${perHistoryVo.sendTm}" var="sendDtm" pattern="yyyyMMddHHmmss"/>
                                <fmt:formatDate value="${sendDtm}" pattern="yy-MM-dd HH:mm:ss"/>
                            </td>
                            <td class="text-left" title="[${perHistoryVo.errorCd}][${perHistoryVo.errMsgKor}]${perHistoryVo.errMsg}"><!-- 발송결과 -->
                                <c:choose>
                                    <c:when test="${fn:trim(perHistoryVo.errMsgKor) ne ''}">${perHistoryVo.errMsgKor}</c:when>
                                    <c:otherwise>${perHistoryVo.errMsg}</c:otherwise>
                                </c:choose>
                            </td>

                            <c:if test="${channel eq 'M' || channel eq 'P'}">
                            <td id="receipt_${cnt}" height="29"><!-- 수신확인 -->
                                <fmt:parseDate value="${perHistoryVo.receiptDate}" var="receiptDtm" pattern="yyyyMMddHHmmss"/>
                                <span title="<fmt:formatDate value="${receiptDtm}" pattern="yy-MM-dd HH:mm:ss" />" id="fg_${cnt}" class="log_fg">${perHistoryVo.logSendFg}</span>
                            </td>
                            </c:if>

                            <c:if test="${mimeSave eq 'on'}"><!-- 템플릿 보기 -->
                            <td onclick="event.cancelBubble=true">
                                <a href='javascript:viewTemplateMime("${perHistoryVo.campaignNo}", "${perHistoryVo.sendSeq}", "${perHistoryVo.customerNm}" ,"${perHistoryVo.customerEmail}", "${perHistoryVo.resultSeq}")'>
                                    <i class="fas fa-file-download fa-2x"></i>
                                </a>
                            </td>
                            </c:if>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    </c:otherwise>
                    </c:choose>
                </div><!-- e.Light table -->

                <!-- 페이징 -->
                ${paging}
            </div><!-- e.card -->
        </div><!-- e.page content -->
    </div>
</div><!-- e.main-panel -->
</form>

<form id="replayForm" name="replayForm" method="post">
    <input type="hidden" name="client" value="EM" />
    <input type="hidden" name="serviceNo" />
    <input type="hidden" name="seq" />
    <input type="hidden" name="customerKey" />
    <input type="hidden" name="resultSeq" />
    <input type="hidden" name="listSeq" />
    <input type="hidden" name="subType" value="S" />
    <input type="hidden" name="errorCd" />
    <input type="hidden" name="channel" />
</form>
</body>
</html>

