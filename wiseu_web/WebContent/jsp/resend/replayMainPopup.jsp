<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 고객이력] 다시보기(팝업)
 * - URL : /resend/replayMainPopup.do
 * - Controller : com.mnwise.wiseu.web.resend.web.LstResendController
 * - 이전 파일명 : previous_template.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="common.title.previous.view"/></title><!-- 다시보기 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/include/plugin.jsp"%>
<script type='text/javascript'>
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 발송 버튼 클릭
        $("#sendBtn").on("click", function(event) {
            var rules = {
                resendReason  : {notBlank : true},
                resendContact : {notBlank : true}
            };

            var channel = "${channel}";
            if(channel == "M") {
                rules.resendContact.email = true;
            } else if($.mdf.equalsAny(channel, ["S","T","A","C","F","P"])) {
                rules.resendContact.digits = true;
            }

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            if(confirm("<spring:message code='common.confirm.resend' />")) {  // 재발송 하시겠습니까?
                var targetKey;
                <c:if test="${client eq 'EC'}">
                    targetKey = "${subType eq 'N' ? hisVo.seq : hisVo.customerKey}";
                </c:if>
                <c:if test="${client eq 'EM'}">
                    targetKey = "${hisVo.customerKey}";
                </c:if>

                var param = {
                    serviceNo : ${serviceNo},
                    superSeq : ${hisVo.resendSts eq ' ' ? hisVo.resultSeq : hisVo.superSeq},
                    listSeq : "${hisVo.listSeq}",
                    targetKey : targetKey,
                    targetContact : $.trim($('input[name=resendContact]').val()),
                    client : "${client}",
                    subType : "${subType}",
                    channel : "${channel}",
                    resendType : "T",
                    resendReason : $.trim($('input[name=resendReason]').val())
                };

                $.post("/resend/resendRequest.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='common.menu.resend.message' />");  // 재발송이 완료 되었습니다. 해당 결과는 고객이력 페이지의 발송결과를 확인해 주시기 바랍니다.
                    } else {
                        alert("<spring:message code='resend.request.fail' />");  // 재발송이 요청에 실패 하였습니다.
                    }
                });
            }
        });
    }

    function initPage() {
        <c:if test="${empty exception}">
        var param = $.mdf.serializeObject('#previousFrm');
        $.post("/resend/replayConts.do", $.param(param, true), function(result) {
            $('#contsDiv').html(result);
        });
        </c:if>
    }
</script>
</head>

<body>

<c:if test="${!empty exception}">
<div id="exception" class="exception-message">
    <span>Exception Message: ${exception}</span>
</div>
</c:if>

<c:if test="${empty exception}">
<form id="updateForm" name="updateForm">
<div class="card pop-card">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="common.title.previous.view"/></h5></div><!-- 다시보기 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <div class="table-responsive border gridWrap overflow-auto" style="height: 445px;" id="contsDiv"><!-- 내용보기 -->
        </div>
        <h1 class="h3 text-primary mt-3 mb-1"><spring:message code='common.send.type.resend' /></h1><!-- 재발송 -->
        <div class="table-responsive gridWrap">
            <table class="table table-sm dataTable table-fixed">
                <tbody>
                <tr>
                    <th width="15%"><em class="required">required</em><spring:message code="resend.reason" /></th><!-- 재발송 사유 -->
                    <td width="*"><input type="text" class="form-control form-control-sm" name="resendReason" /></td>
                    <th width="15%"><em class="required">required</em><spring:message code="resend.target.receiver" /></th><!-- 재발송 수신처 -->
                    <td width="*"><input type="text" class="form-control form-control-sm" name="resendContact" /></td>
                </tr>
                </tbody>
            </table>
        </div><!-- //Light table -->
    </div><!-- // card-body-->

    <div class="card-footer">
        <button type="button" class="btn btn-outline-primary" id="sendBtn">
            <spring:message code="button.send"/><!-- 발송 -->
        </button>
    </div>
</div>
</form>

<form id="previousFrm" name="previousFrm">
    <input type="hidden" name="client" value="${client}" />
    <input type="hidden" name="resultSeq" value="${hisVo.resultSeq}" />
    <input type="hidden" name="serviceNo" value="${hisVo.serviceNo}" />
    <input type="hidden" name="customerKey" value="${hisVo.customerKey}" />
    <input type="hidden" name="listSeq" value="${hisVo.listSeq}" />
    <input type="hidden" name="errorCd" value="${hisVo.errorCd}" />
    <input type="hidden" name="channel" value="${channel}" />
    <input type="hidden" name="abTestType" value="${abTestType}" />
    <input type="hidden" name="seq" value="${fn:trim(hisVo.seq)}" />
    <input type="hidden" name="slot1" value="${slot1}" />
</form>
</c:if>
</body>
</html>