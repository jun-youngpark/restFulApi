<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 고객이력] 이케어 고객이력
 * - URL : /ecare/ecareSendLog.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcarePerHistoryController
 * - 이전 파일명 : ecare_per_history.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<html>
<head>
<title><spring:message code="ecare.msg.history_${webExecMode}" /></title>
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
                ecareNo : {digits : true, maxlength : 15}
            };

            if($.mdf.validForm("#searchForm", rules) == false) {
                return;
            }

            // 검색 버튼 클릭시 입력칸의 조건이 아무것도 없으면 검색이 안되게 함
            if($("#id_searchCustomerEmail").val() == '' && $("#id_searchCustomerNm").val() == '' && $("#id_searchCustomerKey").val() == '' && $("#id_searchEcareNo").val() == '' && $("#id_searchEcareNm").val() == '') {
                alert("<spring:message code='common.alert.history.msg3_${webExecMode}'/>");  // 수신자\ 이름,이메일,ID\ 그리고\ 이케어\ 번호,명\ 중\ 하나의\ 검색조건이라도\ 없을시\ 검색할수\ 없습니다.
                return;
            }

            if(!confirm("<spring:message code='common.alert.history.msg2'/>")) {  // 발송기간 이외의 검색 조건이 없을 경우 조회에 상당한 시간이 소요될 수 있습니다. \\n 계속 하시겠습니까?
                return;
            }

            $("#searchForm").attr('action', '/ecare/ecareSendLog.do').submit();  // /ecare/ecare_per_history.do
        });

        // 고객이력 목록에서 이력 선택
        $('#sendLogTable tbody').on('click', 'tr', function(event) {
            $('#sendLogTable tr.selected').removeClass('selected');
            $(this).addClass('selected');
        });
    }

    function initPage() {
        $(".align_title").mouseenter(function() {
            var checkid2= $(this).children(".receipt_dt").attr("id");
            $("#"+checkid2).css("display","block");
        });

        $(".align_title").mouseleave(function() {
            var checkid2= $(this).children(".receipt_dt").attr("id");
            $("#"+checkid2).css("display","none");
        });

        onChangeChannel();
        $("#id_searchErrorCd").val('${param.errorCd}').prop("selected", true);


        new mdf.Date("#searchQstartDt");  // 발송기간-시작일
        new mdf.Date("#searchQendDt");  // 발송기간-종료일
        new mdf.DataTable("#sendLogTable");
    }

    // 채널 콤보박스 선택 클릭
    function onChangeChannel() {
        var channel = $("#searchForm select[name=channel]").val();

        if(channel == 'M') {
            $("#receiverInfo").html('<spring:message code="common.menu.remail"/>');  // 수신자 이메일
        } else {
            $("#receiverInfo").html('<spring:message code="ecare.channel.personal_${webExecMode}"/>');  // 수신자 전화번호
        }

        switch(channel) {
            case 'M' :
            case 'F' :
                $("#trSn").hide();
                $("#tdSearch").attr("rowspan", 2);
                break;

            case 'S' :
            case 'T' :
            case 'A' :
            case 'C' :
            case 'B' :
            case 'P' :
                //$("#trSn").show();
                //$("#tdSearch").attr("rowspan", 3);
                break;
        }

        $.ajax({
            url : "/ecare/selectErrorCdList.json?channelType="+channel,
            type : "post",
            async: false,
            success : function(sendErrList) {
                $("#id_searchErrorCd").children('option:not(:first)').remove();
                $("#id_searchErrorCd").append("<option>=====================</option>");
                $("#id_searchErrorCd").append("<option value='FAIL'>[*] Fail</option>");
                $(sendErrList).each(function(i, sendErrVo) {
                    $("#id_searchErrorCd").append("<option value='"+ sendErrVo.errorCd +"'>[" + sendErrVo.errorCd + "]" + sendErrVo.errorDesc + "</option>");
                });
            }
        });
    }

    function viewTemplateMime(serviceNo, customerKey, customerNm, customerEmail, resultSeq) {
        var url = "/common/replayMime.do?cmd=resultView&type=EC&serviceNo=" + serviceNo + "&customerKey=" + customerKey
                + "&customerNm=" + encodeURIComponent(customerNm) + "&customerEmail=" + customerEmail + "&resultSeq=" + resultSeq;

        $.mdf.popupGet(url, 'tmplViewPopup', 820, 600);
    }

    // 발송 템플릿보기
    function viewSentTemplate(serviceNo, customerKey, subType, slot1, slot2, seq) {
        if(subType == 'S') {
            var url = "/common/reseltMime.do?cmd=view_result&serviceType=EC" + "&serviceNo=" + serviceNo + "&customerKey=" + customerKey;
            if(slot1 != '%20') {
                url += "&slot1=" + slot1;
            }
            if(slot2 != '%20') {
                url += "&slot2=" + slot2;
            }
            if(seq != '%20') {
                url += "&condition=" + seq;
            }
        } else {
            var url = "/common/reseltMime.do?cmd=view_result&serviceType=EC" + "&serviceNo=" + serviceNo + "&condition=" + seq;
        }

        $.mdf.popupGet(url, 'tmplViewPopup', 820, 600);
    }

    // 다시보기 아이콘 클릭
    function replayTemplate(serviceNo, listSeq, customerKey, resultSeq, subType, errorCd) {
        if('' === $.trim(listSeq)) {
        	alert($.i18n.prop("common.alert.no.listseq"));
            return;
        }

        $.mdf.resetForm("#replayForm");

        $("#replayForm input[name=client]").val("EC");
        $("#replayForm input[name=serviceNo]").val(serviceNo);

        if(subType =='N') {
            $("#replayForm input[name=seq]").val(customerKey);
        } else {
            $("#replayForm input[name=customerKey]").val(customerKey);
        }
        $("#replayForm input[name=resultSeq]").val(resultSeq);
        $("#replayForm input[name=listSeq]").val($.trim(listSeq));
        $("#replayForm input[name=subType]").val(subType);
        $("#replayForm input[name=errorCd]").val(errorCd);
        $("#replayForm input[name=channel]").val('${channel}');

        $.mdf.popupSubmit("#replayForm", '/resend/replayMainPopup.do', 'replayPopup', 830, 730);  // /resend/previous_template.do
    }
</script>
</head>

<body>
<form id="searchForm" name="searchForm" action="/ecare/ecareSendLog.do" method="post"><!-- /ecare/ecare_per_history.do -->
<input type="hidden" name="cmd" id="cmd" />
<input type="hidden" name="resultSeq" id="resultSeq" />
<input type="hidden" name="errmsg" id="errmsg" />
<input type="hidden" name="reportNo" id="reportNo" />
<input type="hidden" name="recordSeq" id="recordSeq" />
<input type="hidden" name="listSeq" id="listSeq" />
<input type="hidden" name="form_subType" id="form_subType" value="${subType}">
<c:set var="nrealtime_type" value="N" />
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="ecare.msg.history_${webExecMode}"/></h3><!-- 이케어 고객이력 -->
            </div>

            <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="10%" />
                            <col width="16%" />
                            <col width="10%" />
                            <col width="16%" />
                            <col width="10%" />
                            <col width="*" />
                            <col width="100" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="ecare.menu.no_${webExecMode}" /></th><!-- 이케어 번호   -->
                            <td><input type="text" name="ecareNo" id="id_searchEcareNo" value="${param.ecareNo}" class="form-control form-control-sm" /></td>
                            <th scope="row"><em class="required"></em><spring:message code="ecare.channel" /></th><!-- 채널 -->
                            <td>
                                <select class="form-control form-control-sm" name="channel">
                                    <c:if test="${fn:contains(useChannelList, 'M')}">
                                        <option value="M" <c:choose><c:when test="${channel eq 'M' }">selected</c:when></c:choose>><spring:message code="common.channel.M"/></option>
                                    </c:if>
                                    <c:if test="${fn:contains(useChannelList, 'S')}">
                                        <option value="S" <c:choose><c:when test="${channel eq 'S' }">selected</c:when></c:choose>><spring:message code="common.channel.S"/></option>
                                    </c:if>
                                    <c:if test="${fn:contains(useChannelList, 'T')}">
                                        <option value="T" <c:choose><c:when test="${channel eq 'T' }">selected</c:when></c:choose>><spring:message code="common.channel.T"/></option>
                                    </c:if>
                                    <c:if test="${fn:contains(useChannelList, 'P')}">
                                        <option value="P" <c:choose><c:when test="${channel eq 'P' }">selected</c:when></c:choose>><spring:message code="common.channel.P"/></option>
                                    </c:if>
                                    <c:if test="${fn:contains(useChannelList, 'A')}">
                                        <option value="A" <c:choose><c:when test="${channel eq 'A' }">selected</c:when></c:choose>><spring:message code="common.channel.A"/></option>
                                    </c:if>
                                    <c:if test="${fn:contains(useChannelList, 'C')}">
                                        <option value="C" <c:choose><c:when test="${channel eq 'C' }">selected</c:when></c:choose>><spring:message code="common.channel.C"/></option>
                                    </c:if>
                                    <c:if test="${fn:contains(useChannelList, 'B')}">
                                        <option value="B" <c:choose><c:when test="${channel eq 'B' }">selected</c:when></c:choose>><spring:message code="common.channel.B"/></option>
                                    </c:if>
                                    <c:if test="${fn:contains(useChannelList, 'F')}">
                                        <option value="F" <c:choose><c:when test="${channel eq 'F' }">selected</c:when></c:choose>><spring:message code="common.channel.F"/></option>
                                    </c:if>
                                </select>
                            </td>
                            <th scope="row"><em class="required"></em><spring:message code="ecare.channel.period" /></th><!-- 발송기간 -->
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
                            <td id="tdSearch" rowspan="2" class="text-center">
                                <button class="btn btn-outline-info btn_search" id="searchBtn">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="ecare.menu.name_${webExecMode}" /></th><!-- 이케어명 -->
                            <td><input type="text" name="ecareNm" id="id_searchEcareNm" value="${param.ecareNm}" class="form-control form-control-sm" /></td>
                            <th scope="row"><span id="receiverInfo"></span></th><!-- 수신자 이메일/전화번호 -->
                            <td><input type="text" name="customerEmail" id="id_searchCustomerEmail" value="${param.customerEmail}" class="form-control form-control-sm" /></td>
                            <th scope="row"><spring:message code="ecare.channel.result" /></th><!-- 발송결과 -->
                            <td>
                                <select name="errorCd" id="id_searchErrorCd" class="form-control form-control-sm">
                                    <option value=""><spring:message code="common.menu.all" /></option>
                                    <option value="">===================================================</option>
                                    <option value="FAIL">[*] Fail</option>
                                    <c:forEach items="${sendErrList}" var="sendErrVo">
                                        <option value="${sendErrVo.errorCd}">[${sendErrVo.errorCd}] ${sendErrVo.errorDesc}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr id="trSn" class="dp-none">
                            <th scope="row"><spring:message code="common.request.id" /></th><!-- 발송요청 ID -->
                            <td colspan="5"><input type="text" name="sn" id="sn" class="form-control form-control-sm w-50" /></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <c:choose>
                <c:when test="${channel eq 'S' || channel eq 'T' || channel eq 'A' || channel eq 'C' || channel eq 'B'}">
                <!-- 문자, 카카오  table -->
                <div class="table-responsive overflow-x-hidden">
                    <table class="table table-xs dataTable table-hover table-fixed" id="sendLogTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="6%">No</th><!-- No -->
                            <th scope="col" width="6%"><spring:message code="ecare.menu.no_${webExecMode}.br" /></th><!-- 이케어번호 -->
                            <th scope="col" width="10%"><spring:message code="ecare.menu.name_${webExecMode}" /></th><!-- 이케어명 -->
                            <th scope="col" width="50"><spring:message code="ecare.channel.trans" /></th><!-- 전송유형 en:width -->
                            <th scope="col" width="10%">
                                <c:choose>
                                    <c:when test="${webExecMode ne '1' || (channel eq 'A' || channel eq 'C')}">
                                       <spring:message code="common.request.id" /><!-- 발송요청 ID -->
                                    </c:when>
                                    <c:otherwise>
                                       <spring:message code="common.menu.rid" /><!-- 수신자ID -->
                                    </c:otherwise>
                                </c:choose>
                            </th>
                            <th scope="col" width="95"><spring:message code="ecare.channel.personal_${webExecMode}" /></th><!-- 수신자 전화번호 -->
                            <th scope="col" width="55"><spring:message code="ecare.menu.sdate" /></th><!-- 발송일시 -->
                            <th scope="col" width="*"><spring:message code="ecare.channel.content" /></th><!-- 발송내용 -->

                            <c:choose>
                            <c:when test="${channel eq 'A' || channel eq 'C'}">
                            <th scope="col" width="7%"><spring:message code="common.channel.${channel}"/><br/><spring:message code="ecare.channel.result"/></th><!-- 알림톡 발송결과 -->
                            <th scope="col" width="7%"><spring:message code="common.channel.S"/><br/><spring:message code="ecare.channel.result"/></th><!-- SMS 발송결과 -->
                            </c:when>
                            <c:otherwise>
                            <th scope="col" width="7%"><spring:message code="ecare.channel.result"/></th><!-- 발송결과 -->
                            <th scope="col" width="7%"><spring:message code="ecare.channel.resultcheck_${webExecMode}" /></th><!-- 대체발송여부 -->
                            </c:otherwise>
                            </c:choose>

                            <th scope="col" width="10%"><spring:message code="ecare.channel.sender" />ID</th><!-- 발신자ID -->

                            <c:if test="${channel eq 'S' || channel eq 'T'}">
                            <th width="80"><spring:message code="ecare.channel.sender" /> <spring:message code="ecare.channel.num" /></th><!-- 발신자 번호 -->
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach var="perHistoryVo" items="${perHistoryList}" varStatus="perHistoryLoop">
                        <tr style="cursor: pointer;" onclick='javascript:replayTemplate("${perHistoryVo.ecareNo}", "${perHistoryVo.listSeq}","${(perHistoryVo.subType eq nrealtime_type) ? perHistoryVo.seq : perHistoryVo.customerKey}", "${perHistoryVo.resultSeq}", "${perHistoryVo.subType}", "${perHistoryVo.errorCd}")'>
                            <td scope="row">${pgBean.totalRowCnt - (pgBean.currentPage - 1) * pgBean.pageSize - perHistoryLoop.index}</td><!-- No -->
                            <td>${perHistoryVo.ecareNo}</td><!-- 이케어번호 -->
                            <td class="text-left" title="[${perHistoryVo.ecareNo}] ${perHistoryVo.ecareNm}"><!-- 이케어명 -->
                                <c:choose>
                                    <c:when test="${perHistoryVo.resendSts eq 'T'}">
                                        <font style="color: green;">[<spring:message code="resend.type.target" />]</font><%-- 단건재발송 --%>
                                    </c:when>
                                </c:choose>
                                ${perHistoryVo.ecareNm}
                            </td>
                            <td><!-- 전송유형 -->
                                <c:choose>
                                    <c:when test="${channel eq 'S'}">SMS</c:when>
                                    <c:when test="${channel eq 'T'}">LMS<br/>/MMS</c:when>
                                    <c:when test="${channel eq 'A' || channel eq 'B'}">
                                        <spring:message code="common.channel.${channel}" />
                                        <c:choose>
                                            <c:when test="${perHistoryVo.msgType == 'S'}"><br/>→SMS</c:when>
                                            <c:when test="${perHistoryVo.msgType == 'L'}"><br/>→LMS</c:when>
                                            <c:when test="${perHistoryVo.msgType == 'T'}"><br/>→MMS</c:when>
                                        </c:choose>
                                    </c:when>
                                </c:choose>
                            </td>

                            <c:choose>
                            <c:when test="${webExecMode ne '1' && (channel eq 'A' || channel eq 'C')}">
                            <td class="text-left" title="${perHistoryVo.sn}"><!-- 수신자ID -->
                                ${perHistoryVo.sn}
                            </td>
                            </c:when>
                            <c:otherwise>
                            <td class="text-left" title="${perHistoryVo.customerKey}">
                                ${perHistoryVo.customerKey}<!-- 수신자ID -->
                            </td>
                            </c:otherwise>
                            </c:choose>

                            <td>${wiseu:telMask(perHistoryVo.customerEmail)}</td><!-- 수신자 전화번호 -->
                            <td><!-- 발송일시 -->
                                <fmt:parseDate value="${perHistoryVo.sendDt}${perHistoryVo.sendTm}" var="sendDtm" pattern="yyyyMMddHHmmss" />
                                <fmt:formatDate value="${sendDtm}" pattern="yy-MM-dd HH:mm:ss" />
                            </td>
                            <td class="text-left"><!-- 발송내용 -->
                                <c:choose>
                                    <c:when test="${perHistoryVo.resendSts eq 'T'}">
                                        <font style="color: green;">[<spring:message code="resend.type.target" />]</font>
                                    </c:when>
                                </c:choose>
                                ${perHistoryVo.dispSndMsg}
                            </td>
                            <td><!-- 발송결과/알림톡 발송결과 -->
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
                                <c:otherwise><!-- SMS 발송결과 -->
                                    <td title="[${perHistoryVo.smsRsltCd}]${perHistoryVo.smsRsltMsg}">
                                        <c:choose>
                                            <c:when test="${fn:trim(perHistoryVo.dispAlternateSmsSendResult) ne ''}">
                                                <nobr>${perHistoryVo.dispAlternateSmsSendResult}</nobr>
                                            </c:when>
                                            <c:otherwise><nobr>-</nobr></c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:otherwise>
                            </c:choose>

                            <td class="text-left" title="${perHistoryVo.reqUserId}"><!-- 발신자ID -->
                                <nobr>${perHistoryVo.reqUserId}</nobr>
                            </td>

                            <c:if test="${channel eq 'S' || channel eq 'T'}">
                            <td title="${perHistoryVo.sender}"><!-- 발신자번호 -->
                                <nobr>${perHistoryVo.sender}</nobr>
                            </td>
                            </c:if>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- e.문자, 알림톡, 친구톡  table-->
                </c:when>

                <c:otherwise>
                <!-- 메일, FAX, PUSH 채널 선택시  table -->
                <div class="table-responsive overflow-x-hidden">
                    <table class="table table-xs dataTable table-hover table-fixed" id="sendLogTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="6%">No</th><!-- No -->
                            <th scope="col" width="6%"><spring:message code="ecare.menu.no_${webExecMode}.br" /></th><!-- 이케어번호 -->
                            <th scope="col" width="20%"><spring:message code="ecare.menu.name_${webExecMode}" /></th><!-- 이케어명 -->
                            <th scope="col" width="18%">
                                <c:choose>
                                    <c:when test="${channel eq 'M'}">
                                        <spring:message code="common.menu.remail"/><!-- 수신자 이메일 -->
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="common.menu.number"/><!-- 수신자 전화번호 -->
                                    </c:otherwise>
                                </c:choose>
                            </th>
                            <th scope="col" width="8%"><spring:message code="common.menu.rname" /></th><!-- 수신자 이름 -->
                            <th scope="col" width="12%"><spring:message code="common.menu.rid" /></th><!-- 수신자ID -->
                            <th scope="col" width="55"><spring:message code="ecare.menu.sdate" /></th><!-- 발송일시-->
                            <th scope="col" width="*"><spring:message code="common.menu.sresult" /></th><!-- 발송결과-->
                            <c:if test="${channel eq 'M' || channel eq 'P'}">
                            <th scope="col" width="6%"><spring:message code="common.menu.receipt.br" /></th><!-- 수신확인-->
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach var="perHistoryVo" items="${perHistoryList}" varStatus="perHistoryLoop">
                        <c:set var="cnt" value="${cnt+1}" />
                        <tr style="cursor: pointer;" onclick='javascript:replayTemplate("${perHistoryVo.ecareNo}", "${perHistoryVo.listSeq}","${(perHistoryVo.subType eq nrealtime_type) ? perHistoryVo.seq : perHistoryVo.customerKey}", "${perHistoryVo.resultSeq}", "${perHistoryVo.subType}", "${perHistoryVo.errorCd}")'>
                            <td scope="row">${pgBean.totalRowCnt - (pgBean.currentPage - 1) * pgBean.pageSize - perHistoryLoop.index}</td><!-- No -->
                            <td>${perHistoryVo.ecareNo}</td><!-- 이케어번호 -->
                            <td class="text-left" title="[${perHistoryVo.ecareNo}] ${perHistoryVo.ecareNm}" ><!-- 이케어명 -->
                                <c:if test="${perHistoryVo.resendSts eq 'F'}">
                                    <font style="color: blue;">[<spring:message code="resend.type.fail" />]</font>
                                </c:if>
                                ${perHistoryVo.ecareNm}
                            </td>
                            <c:choose>
                                <c:when test="${channel eq 'M'}">
                                    <td class="text-left">${wiseu:telMask(perHistoryVo.customerEmail)}</td><!-- 수신자 이메일 -->
                                </c:when>
                                <c:otherwise>
                                    <td>${wiseu:telMask(perHistoryVo.customerEmail)}</td><!-- 수신자 번호 -->
                                </c:otherwise>
                            </c:choose>
                            <td title="${wiseu:nameMask(perHistoryVo.customerNm)}"><!-- 수신자 이름 -->
                                <nobr>${wiseu:nameMask(perHistoryVo.customerNm)}</nobr>
                            </td>
                            <td class="text-left" title="${perHistoryVo.customerKey}"><!-- 수신자ID -->
                                <nobr>${perHistoryVo.customerKey}</nobr>
                            </td>
                            <td><!-- 발송일시 -->
                                <fmt:parseDate value="${perHistoryVo.sendDt}${perHistoryVo.sendTm}" var="sendDtm" pattern="yyyyMMddHHmmss" />
                                <fmt:formatDate value="${sendDtm}" pattern="yy-MM-dd HH:mm:ss" />
                            </td>
                            <td class="text-left"><!-- 발송결과 -->
                                <c:choose>
                                    <c:when test="${fn:trim(perHistoryVo.errMsgKor) ne ''}">${perHistoryVo.errMsgKor}</c:when>
                                    <c:otherwise>${perHistoryVo.errMsg}</c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${channel eq 'M' || channel eq 'P'}">
                            <td id="receipt_${cnt}" ><!-- 수신확인 -->
                                <fmt:parseDate value="${perHistoryVo.receiptDate}" var="receiptDtm" pattern="yyyyMMddHHmmss"/>
                                <span id="fg_${cnt}" title="<fmt:formatDate value="${receiptDtm}" pattern="yy-MM-dd HH:mm:ss" />">${perHistoryVo.logSendFg}</span>
                            </td>
                            </c:if>
                        </tr>
                        </c:forEach>
                    </table>
                </div>
                </c:otherwise>
                </c:choose>
            </div>
            <!-- 페이징 -->
            ${paging}
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>

<form id="replayForm" name="replayForm" method="post">
    <input type="hidden" name="client" value="EC" />
    <input type="hidden" name="serviceNo" />
    <input type="hidden" name="seq" />
    <input type="hidden" name="customerKey" />
    <input type="hidden" name="resultSeq" />
    <input type="hidden" name="listSeq" />
    <input type="hidden" name="subType" />
    <input type="hidden" name="errorCd" />
    <input type="hidden" name="channel" />
</form>
</body>
</html>