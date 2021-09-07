<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>2단계] 이케어 메세지 작성 화면 출력 (메일)
 * - URL : /ecare/ecare2Step.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcareScenario2StepFormController
 * - 이전 파일명 : ecare_2step_mail_form.jsp
 * - 이전 파일명 : ecare_2step_sms_form.jsp
 * - 이전 파일명 : ecare_2step_mms_form.jsp
 * - 이전 파일명 : ecare_2step_push_form.jsp
 * - 이전 파일명 : ecare_2step_fax_form.jsp
 * - 이전 파일명 : ecare_2step_frtalk_form.jsp
 * - 이전 파일명 : ecare_2step_altalk_form.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="ecare.msg.create_${webExecMode}" /> - <spring:message code="ecare.msg.msg" /> : <spring:message code="common.menu.ecare_${webExecMode}" /> NO.${ecareScenarioVo.ecareVo.ecareNo}</title>
<script src="/js/editor/common.js"></script><!-- 링크유효성 검사, 금칙어 검사 -->

<script type="text/javascript">
    var ecareNo = ${ecareScenarioVo.ecareVo.ecareNo};
    var scenarioNo = ${ecareScenarioVo.scenarioNo};
    var serviceType = '${ecareScenarioVo.ecareVo.serviceType}';
    var segmentNo = ${ecareScenarioVo.segmentNo};
    var channelType = '${ecareScenarioVo.ecareVo.channelType}';
    var subType = '${ecareScenarioVo.ecareVo.subType}';
    var mailType = "${ecareScenarioVo.ecareVo.mailType}";
    var userId ="${sessionScope.adminSessionVo.userVo.userId}"
    var webExecMode ="${webExecMode}";
    var editAble =${ecareScenarioVo.ecareVo.editAble};
    var ecareSts ="${ecareScenarioVo.ecareVo.ecareSts}";
    var depthNo ="${ecareScenarioVo.ecareVo.depthNo}";
    $(document).ready(function() {
        initEventBind();
        initPage();
    });
    function initEventBind() {
        // 미리보기 버튼 클릭
        $('#previewBtn').on("click", function(event) {
            var handlerSeq = $("#handlerSeq").val();
            if(handlerSeq== 0) {
            	alert($.i18n.prop("common.alert.preview.msg2"));  // 메시지 저장 후  미리보기가 가능합니다.
                return false;
            }

            var url = "/common/previewMainPopup.do?cmd=previewList&serviceNo=${ecareScenarioVo.ecareVo.ecareNo}&serviceType=EC&subType=${ecareScenarioVo.ecareVo.subType}";
            if(subType === 'N') {
                $.mdf.popupGet(url, 'previewPopup', 1000, 770);
            } else {
                if(segmentNo == undefined || segmentNo == 0) {
                	alert($.i18n.prop("common.alert.preview.msg1"));
                    return false;
                }
                var param = {segmentNo: segmentNo};
                $.post("/ecare/checkTargetQuery.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        $.mdf.popupGet(url, 'previewPopup', 1000, 770);
                    } else {
                    	alert(result.message);
                        return false;
                    }
                });
            }
        });

        // 테스트 발송 버튼 클릭
        $("#testSendBtn").on("click", function(event) {
        	 var handlerSeq = $("#handlerSeq").val();
             if(handlerSeq== 0) {
            	 alert($.i18n.prop("common.alert.test.msg27")); // 메시지 저장 후  테스트발송이 가능합니다.
                 return false;
             }
             var param = {segmentNo: segmentNo};
             $.post("/ecare/checkTargetQuery.json", $.param(param, true), function(result) {
                 if(result.code == "OK") {
                	  var url = "/common/testSendPopup.do?no=${ecareScenarioVo.ecareVo.ecareNo}&serviceType=E";  // /common/testSend_popup.do
                      $.mdf.popupGet(url, 'testSendPopup', 900, 750);
                 }else{
                	 alert(result.message);
                      return false;
                 }
             });
        });

        // 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
   		    $('#editorAccordion .collapse').addClass('show');  //전체 펼치기
            $.when(ecareSave(true)).done(function(result1){
                $.when(editorSaveHandler(true), targetSave(true)).done(function(result1, result2){
                	alert($.i18n.prop("ecare.2step.all.save")); //전체 적용 되었습니다.
                }).fail(function(error) {
                    if(error !== "noAlram"){
                    	alert(error);
                    }
                });
            }).fail(function(error) {
                if(error !== "noAlram"){
                	alert(error);
                }
            });
        });

        // 금칙어 검사 버튼 클릭
        $('#goBadWordEc').on("click", function(event) {
            goBadWordEc();  //common.js
        });
         // 링크 클릭 버튼 클릭
        $('#goLinkCheckEc').on("click", function(event) {
            goLinkCheckEc(); //common.js
        });
        // (핸들러 수정사유) 저장 버튼 클릭
        $('#layerReason #reasonSaveBtn').on("click", function(event) {
            var historyMsg = $('[name=historyPopupMsg]').val();
            if(historyMsg=='') {
            	alert($.i18n.prop("common.alert.save.e5"));
                return false;
            }
            $('[name=historyMsg]').val(historyMsg);
            setEcareStsInfo();
        });

        $("#handler").on("propertychange change keyup paste input", function() {
        	$("#ecareFrm input[name=handlerUpdateYn]").val('Y');
        });

        // (핸들러 수정사유) 닫기 버튼 클릭
        $('#layerReason #closeBtn').on("click", function(event) {
            event.preventDefault();
            $('#layerReason').hide();
        });
    }

    function initPage() {
        //$('a[rel*=facebox]').facebox();
        loadSet();
        $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
                $('.scrollToTop').fadeIn();
            } else {
                $('.scrollToTop').fadeOut();
            }
        });
        //Click event to scroll to top
        $('.scrollToTop').click(function () {
            $('html, body').animate({scrollTop: 0 }, 800);
            return false;
        });
        var writeAuth = '${sessionScope.write}';
        if(writeAuth != 'W'){   //쓰기 권한이 없을 경우
        	hideButtonByAuth(["editorSave","targetSave","ecareSave","saveBtn"]); //common.js
        }
    }

    function setEcareStsInfo() {
        var param = {
            ecareNo : ecareNo,
            ecareSts : "I"
        };
        $.post("/ecare/setEcareStsInfo.json", $.param(param, true), function(result) {
            if(result.code == "OK") {
                $('#layerReason').hide();
                editorSave(false);
            }
        });
    }

    // 화면 로딩 완료 후 호출 이벤트
    var loadStatus = 0;
    function loadSet() {
        loadStatus = 1;
    }

    // 상태변경 버튼 클릭
    function saveStat(btnType) {
        varChannel = channelType;
        varEcareNo = ecareNo;
        varEcareSts = ecareSts;

        // 검증권한 여부 체크
        var userTypeCd = '${sessionScope.adminSessionVo.userVo.userTypeCd}';
        var isVerify = (userTypeCd == 'A' || userTypeCd == 'M' || userTypeCd == 'C') ? true : false;

        if(btnType == 'S') {            // 상태변경 버튼
            if(varEcareSts == "R" || varEcareSts == 'A') {
                varEcareSts = "P";
                varEcareStsNm = "Pending";
            } else if(varEcareSts == "P" || varEcareSts == "T" || varEcareSts == "I") {
                varEcareSts = "R";
                varEcareStsNm = "Executed";
            } else if(varEcareSts == "D") {
                varEcareSts = "I";
                varEcareStsNm = "Uncompleted";
            }
        } else if(btnType = 'V') {      // 검증관련 버튼
            if(varEcareSts == 'C') {
                varEcareStsNm = "Approval requeted";
            } else if(varEcareSts == "A") {
                varEcareStsNm = "Approval";
            } else if(varEcareSts == "D") {
                varEcareStsNm = "Approval rejected";
            }
        }
        //EcareService.setEcareStsInfo(ecareNo, varEcareSts, saveStatCallback);
        var param = {
            ecareNo : ecareNo,
            ecareSts : varEcareSts
        };

        $.post("/ecare/setEcareStsInfo.json", $.param(param, true), function(result) {
            if(result.code == "OK") {
                saveStatCallback(result.value);
            }
        });
    }

    // 승인상태 저장 후 호출
    function saveStatCallback(dataFromServer) {
        if(dataFromServer == "blockChange") {
        	alert($.i18n.prop("ecare.alert.msg.exec.11"));
        } else if(dataFromServer == "notSchedule") {
        	alert($.i18n.prop("ecare.alert.msg.exec.12"));
        }
        location.reload();
    }

</script>
</head>
<body>
<c:choose>
    <c:when test="${ecareScenarioVo.ecareVo.serviceType eq 'S' && ecareScenarioVo.ecareVo.subType eq 'N'}">
        <c:set var="serviceType" value="nrealtime"/> <!-- 준실시간 -->
        <c:set var="serviceTypeNm"><spring:message code="ecare.type.SN"/></c:set> <!-- 준실시간 -->
    </c:when>
    <c:when test="${ecareScenarioVo.ecareVo.serviceType eq 'S' && ecareScenarioVo.ecareVo.subType eq 'S'}">
        <c:set var="serviceType" value="schedule"/>  <!-- 스케줄 -->
        <c:set var="serviceTypeNm"><spring:message code="ecare.type.S"/></c:set>
    </c:when>
    <c:otherwise>
        <c:set var="serviceType" value="scheduleM"/>  <!-- 스케줄(분) -->
        <c:set var="serviceTypeNm"><spring:message code="ecare.type.SR"/></c:set>
    </c:otherwise>
</c:choose>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card">
            <div class="card-header">
                <div class="row">
                    <h3 class="mb-0">
                        <spring:message code="ecare.msg.create_${webExecMode}"/>
                        (${serviceTypeNm} <spring:message code="common.channel.${ecareScenarioVo.ecareVo.channelType}"/>)
                    </h3>
                </div>
            </div>
            <div class="card-body pb-0">
                <c:if test="${ecareScenarioVo.ecareVo.editAble eq false}">
                <div class="ofBar" role="alert"><!-- 실행중일 경우 메세지 -->
                    <span class="alert-text"><i class="fas fa-exclamation-triangle"></i>
                    <spring:message code="ecare.alert.msg.exec.18_${webExecMode}"/></span>
                </div>
                </c:if>

                <div class="row align-items-center py-1 table_option">
                    <div class="col-12"><!-- 상단 버튼 -->
                        <c:if test="${ecareScenarioVo.ecareVo.channelType eq 'M'}">
                            <button type="button" class="btn btn-sm btn-outline-primary" id="goLinkCheckEc">
                                <i class="fas fa-link"></i> <spring:message code="button.check.link" /><!-- 링크 유효성 검사 -->
                            </button>
                        </c:if>
                        <c:if test="${wiseu:isKakao(ecareScenarioVo.ecareVo.channelType) eq false}">
                            <!-- 금칙어 검사 script 파일 : /editor/common.js  -->
                            <button type="button" class="btn btn-sm btn-outline-primary" id="goBadWordEc">
                                <i class="fas fa-comment-slash"></i> <spring:message code="button.check.badword"/><!-- 금칙어 검사 -->
                            </button>
                        </c:if>
                         <c:if test="${fn:contains('FM', ecareScenarioVo.ecareVo.channelType)}">
                             <button type="button" class="btn btn-sm btn-outline-primary" id="previewBtn">
                                    <i class="fas fa-eye"></i> <spring:message code="button.preview"/><!-- 미리보기 -->
                             </button>
                        </c:if>
                        <c:if test="${serviceType eq 'schedule' or serviceType eq 'scheduleM'}">
                            <button type="button" class="btn btn-sm btn-outline-primary" id="testSendBtn">
                                <i class="fas fa-paper-plane"></i> <spring:message code="button.send.test"/><!-- 테스트 발송 -->
                            </button>
                        </c:if>
                    </div><!-- //buttons -->
                </div>

                <div class="accordion mt-1" id="editorAccordion">
                    <!-- 1. card 컨텐츠 제작-->
                    <div class="card">
                        <div class="card-header bg-default toogleEvt" id="headingOne" data-toggle="collapse" data-target="#collapseOne" aria-expanded='true'>
                            <h5 class="text-secondary pt-3">[STEP1] <spring:message code="ecare.2step.title.contents" /></h5><!--  컨텐츠 제작-->
                        </div>
                        <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" >
                         <%@ include file="/jsp/include/handlerReason_inc.jsp" %>
                         <c:choose>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'M'}">
                                 <%@ include file="/jsp/editor/editor_ecare_mail.jsp" %>
                             </c:when>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'S'}">
                                 <%@ include file="/jsp/editor/editor_ecare_sms.jsp" %>
                             </c:when>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'T'}">
                                 <%@ include file="/jsp/editor/editor_ecare_mms.jsp" %>
                             </c:when>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'A'}">
                                 <%@ include file="/jsp/editor/editor_ecare_alt.jsp" %>
                             </c:when>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'C'}">
                                 <%@ include file="/jsp/editor/editor_ecare_frt.jsp" %>
                             </c:when>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'F'}">
                                 <%@ include file="/jsp/editor/editor_ecare_fax.jsp" %>
                             </c:when>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'P'}">
                                 <%@ include file="/jsp/editor/editor_ecare_push.jsp" %>
                             </c:when>
                             <c:when test="${ecareScenarioVo.ecareVo.channelType eq 'B'}">
                                 <%@ include file="/jsp/editor/editor_ecare_brt.jsp" %>
                             </c:when>
                         </c:choose>
                         <c:if test="${ecareScenarioVo.ecareVo.channelType ne 'M'}">
	                         <div class="alert alert-secondary mb-0" role="alert">
	                            <i class="fas fa-circle fa-xs"></i><spring:message code="ecare.2step.help8" /><%-- 템플릿 내 개인화 태그 사용은 #{변수}형태로 넣어주세요 --%>
	                         </div>
                        </c:if>
                        </div>
                    </div>
                    <!-- // 1. card 컨텐츠 제작 끝-->
                    <%@ include file="/jsp/ecare/ecare2StepTarget.jsp" %><!--  2.card 대상자-->
                    <%@ include file="/jsp/ecare/ecare2StepInfo.jsp" %><!-- 3. card 발송정보 -->
                    <%@ include file="/jsp/ecare/ecare2StepOption.jsp" %><!-- 4. card 고급설정 -->
                    <div class="card-footer pb-5 btn_area">
                       <button type="submit" class="btn btn btn-outline-primary" id="saveBtn">
                           <i class="fas fa-save"></i> <spring:message code="button.save" /><!--  저장-->
                       </button>
                    </div>
                </div>
            </div>
        </div>
        <div id="btn_goTop" class="scrollToTop">
            <button class="btn btn-sm btn-outline-primary btn-round">
                <i class="fas fa-chevron-up"></i><br> Top
            </button>
        </div>
    </div>
</div>
</body>
</html>
