<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initResendEventBind();
    });

    function initResendEventBind() {
        // 재발송 버튼 클릭
        $("#resendBtn").on("click", function(event) {
            var rules = {
                resendReason : {notBlank : true}
            };

            if($.mdf.validForm("#reportFrm", rules) == false) {
                return;
            }

            var resendTargetCnt = $("#targetCnt").val();

            if(resendTargetCnt == 0) {
                alert("<spring:message code='resend.target.count.zero' />");  // 재발송 대상자 없습니다.
                return;
            }

            if(confirm(resendTargetCnt + " <spring:message code='resend.send.confirm' />") == true) {  // 건을 재발송 하시겠습니까?
                var param = $.mdf.serializeObject("#reportFrm");
                $.post("/resend/resendRequest.json", $.param(param, true), function(result) {
                     $.mdf.log(result);
                     if(result.code == "OK") {
                         alert("<spring:message code='common.menu.resend.message'/>");  // 재발송이 완료 되었습니다. 해당 결과는 고객이력 페이지의 발송결과를 확인해 주시기 바랍니다.
                     } else {
                         alert("<spring:message code='resend.request.fail'/>");  // 재발송이 요청에 실패 하였습니다.
                     }
                });
             } else {
                 return;
             }
        });
    }

    // 재발송 아이콘 클릭 - 발송일련번호에 해당하는 정보를 가져와 재발송 사유 입력 폼을 출력
    function viewResendForm(resultSeq, superSeq, serviceNo, channel, failCnt){
        $("#divResend").toggle();

        var params = "resultSeq="+resultSeq+"&serviceNo="+serviceNo+"&channel="+channel+"&client=EC";

        $("#bounceCnt").val(failCnt);
        $("#superSeq").val(resultSeq);

        $.ajax({
            cache:false,
            type:"get",
            url:"/resend/getResendTargetCnt.json",
            data:params,
            success:function(result){
                $.mdf.log(result);
                $("#resultSeq").val(result.resultSeq);
                $("#targetCnt").val(result.resendTargetCnt);
            },
            error:function(e){
                alert("<spring:message code='resend.request.fail'/>");  // 재발송이 요청에 실패 하였습니다.
            }
        });
    }
</script>

<c:if test="${lstResendUse eq 'on'}">
<c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and (scenarioInfoVo.ecareInfoVo.subType eq 'R' or scenarioInfoVo.ecareInfoVo.subType eq 'S')}">
<input type="hidden" name="superSeq" id="superSeq" value="" />
<input type="hidden" name="client" value="EC" />
<input type="hidden" name="resendType" value="F" />
<input type="hidden" name="serviceNo" value="${scenarioInfoVo.ecareInfoVo.ecareNo}" />
<input type="hidden" name="channel" value="${scenarioInfoVo.ecareInfoVo.channelType}" />

<div class="card-body px-0 pb-0 mt-3 mb-0 dp-none" id="divResend">
    <h4 class="h3 text-primary mb-0"><spring:message code="common.send.type.resend"/></h4><!-- 재발송 -->

    <div class="table-responsive gridWrap">
        <table class="table table-sm dataTable table-fixed">
            <colgroup>
                <col width="11%" />
                <col width="*" />
                <col width="11%" />
                <col width="20%" />
                <col width="11%" />
                <col width="15%" />
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><spring:message code="report.campaign.result.comparison.resultseq" /></th><!-- 발송일련번호 -->
                <td><input type="text" class="form-control form-control-sm" name="resultSeq" id="resultSeq" readonly="readonly"/></td>
                <th scope="row"><spring:message code="resend.fail.target.count" /></th><!-- 실패 대상자 수 -->
                <td><input type="text" class="form-control form-control-sm" name="bounceCnt" id="bounceCnt" readonly="readonly"/></td>
                <th scope="row"><spring:message code="resend.target.count" /></th><!-- 재발송 대상자 수 -->
                <td><input type="text" class="form-control form-control-sm" name="targetCnt" id="targetCnt" readonly="readonly"/></td>
            </tr>
            <tr>
                <th scope="row"><em class="required">required</em><spring:message code="resend.reason" /></th><!-- 재발송 사유 -->
                <td colspan="5"><input type="text" class="form-control form-control-sm" name="resendReason" id="resendReason"/></td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="btn_area">
        <button type="button" class="btn btn-outline-primary" id="resendBtn">
            <i class="fas paper-plane"></i> <spring:message code="common.send.type.resend"/><!-- 재발송 -->
        </button>
    </div>
</div>
</c:if>
</c:if>

