<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>2단계] 캠페인 메세지 작성 화면 출력 (PUSH)
 - URL : /campaign/campaign2Step.do
 - Controller : com.mnwise.wiseu.web.campaign.web.Scenario2StepFormController
 - 이전 파일명 : campaign_2step_push_form.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.msg"/>)</title><!-- 캠페인 등록 (메시지 작성) -->
<script src="/js/common/common2step.js"></script>
<script src="/js/segment/target.js"></script><!-- 대상자 검색/선택/등록 -->
<%@ include file="/jsp/campaign/campaign2StepEvent_inc.jsp" %><!-- 저작기 2단계 공통 이벤트 -->
<script type="text/javascript">
    var loadStatus = 0;  // 화면로딩상태[0:로딩중, 1:로딩완료]. tinyMCE가 loadSet() 메소드를 호출하여 loadStatus값을 1로 설정

    var rules = {
        segmentSize : {min : 1},
        campaignPreface : {notBlank : true}
    };

    var messages = {
        segmentSize : {min : "<spring:message code='campaign.error2.msg1'/>"}  // 대상자를 선택해야 합니다.
    };

    $(document).ready(function() {
        initCommonEventBind();
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
            if(!checkLoaded()) {
                return;
            }

            var isValidForm = $.mdf.validForm("#editorFrm", rules, messages);
            var isValidEditorForm = editorIfrm.validatePushData();
            if(!isValidForm || !isValidEditorForm) {
                return;
            }

            //${scenarioVo.campaignVo.campaignSts}<c:if test="${scenarioVo.campaignVo.campaignSts eq 'C'}">
                alert('<spring:message code="common.alert.save.c1" />');  // 승인 요청 중인 캠페인은 변경/저장 하실 수 없습니다.
                return;
            // </c:if>

            //<c:if test="${scenarioVo.campaignVo.editAble eq false}">
                alert('<spring:message code="common.alert.save.c2" />');  // 발송 대기, 발송 중, 발송 완료, 분할중지, 발송 에러 또는 승인 요청 중인 캠페인은 변경/저장 하실 수 없습니다.
                return;
            // </c:if>

            if($(editorIfrm.document).find("#htmlToggle").css("display") != 'none') {
                editorIfrm.tinyMCE.execCommand('mceToggleEditor',false,'template');
            }

            editorIfrm.tinyMCE.triggerSave();
            editorIfrm.saveEditor(true);
        });
    }

    function initPage() {
        initStep3Btn();

        // 핸들러 버튼 클릭
        $("#handlerBtn").on("click", function(event) {
            editorIfrm.toggleHandler();
        });
    }

    // 다음단계 버튼 클릭
    function onClickNextStepBtn() {
        if(checkLoaded() == false || $.mdf.validForm("#editorFrm", rules, messages) == false) {
            return;
        }

        var scenarioNo = $("input[name=scenarioNo]").val();
        var campaignNo = '${scenarioVo.campaignVo.campaignNo}';
        var campaignLevel = $("input[name='campaignVo.campaignLevel']").val();
        if(campaignLevel > 2) {
            // /campaign/campaign_3step_form.do
            location.href="/campaign/campaign3Step.do?scenarioNo=" + scenarioNo + "&campaignVo.campaignNo=" + campaignNo + "&depthNo=${depthNo}&channelType=${scenarioVo.campaignVo.channelType}";
        } else {
            if(!editorIfrm.validatePushData()) {
                return;
            }

            if($(editorIfrm.document).find("#htmlToggle").css("display") != 'none') {
                editorIfrm.tinyMCE.execCommand('mceToggleEditor',false,'template');
            }

            editorIfrm.tinyMCE.triggerSave();
            editorIfrm.saveEditor(true);
        }
    }
</script>
</head>

<body>
<form:form id="editorFrm" name="editorFrm" commandName="scenarioVo" action="/campaign/campaign2Step.do" method="post"><!-- /campaign/campaign_2step_form.do -->
<form:hidden path="scenarioNo" />
<form:hidden path="campaignVo.campaignNo" />
<form:hidden path="campaignVo.channelType" />
<form:hidden path="campaignVo.campaignNm" />
<form:hidden path="campaignVo.sendingMode" />
<form:hidden path="campaignVo.campaignSts" />
<form:hidden path="campaignVo.campaignLevel" />
<form:hidden path="campaignVo.templateType" />
<form:hidden path="segmentNo" />
<form:hidden path="campaignVo.userId" />
<form:hidden path="campaignVo.grpCd" />
<form:hidden path="campaignVo.editorId" />
<input type="hidden" name="stepLevel" value="2" />
<input type="hidden" name="campaignVo.campaignPreface">
<input type="hidden" name="imsiChannelType">
<input type="hidden" name="imsiTemplate">
<input type="hidden" name="nextStep" />
<input type="hidden" name="campaignVo.senderNm" />
<input type="hidden" name="campaignVo.senderTel" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.msg" />)</h3><!-- 캠페인 등록 (메시지 작성) -->
            </div>

            <div class="card-body">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-9"><!-- buttons -->
                        <button type="button" class="btn btn-sm btn-outline-primary" id="badWordCheckBtn">
                            <i class="fas fa-comment-slash"></i> <spring:message code="button.check.badword"/><!-- 금칙어 검사 -->
                        </button>
                        <button type="button" class="btn btn-sm btn-outline-primary" id="testSendBtn">
                            <i class="fas fa-paper-plane"></i> <spring:message code="button.send.test"/><!-- 테스트 발송 -->
                        </button>
                    </div>
                    <div class="col-3 justify-content-end"><!-- step -->
                        <ul class="stepWrap">
                            <li id="step1Btn" style="cursor: pointer;"><div class="box"><span class="txt">STEP</span><strong>1</strong></div></li>
                            <li id="step2Btn" class="current"><div class="box"><span class="txt">STEP</span><strong>2</strong></div></li>
                            <li id="step3Btn"><div class="box"><span class="txt">STEP</span><strong>3</strong></div></li>
                        </ul>
                    </div>
                </div>

                <c:if test="${scenarioVo.campaignVo.editAble eq false}">
                <div class="col-12 alert alert-warning mb-0" role="alert"><!-- 실행중일 경우 메세지 -->
                    <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                    <span class="alert-text"><spring:message code="campaign.alt.msg.modify.sending.err"/></span><!-- 발송이 종료되거나 발송 중인 캠페인은 변경/저장 하실 수 없습니다. -->
                </div>
                </c:if>

                <spring:hasBindErrors name="scenarioVo"><!-- 경고창 -->
                <div class="col-12 alert alert-warning mb-0" role="alert">
                    <c:forEach var="error" items="${errors.allErrors}">
                        <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                        <span class="alert-text"><spring:message message="${error}" /><br></span>
                    </c:forEach>
                </div>
                </spring:hasBindErrors>

                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="11%" />
                            <col width="54%" />
                            <col width="11%" />
                            <col width="24%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cname"/></th><!-- 캠페인명 -->
                            <td> ${scenarioVo.campaignVo.campaignNm}</td>
                            <th scope="row"><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 -->
                            <td><em class="txt_channel p">P</em> <span>${scenarioVo.campaignVo.campaignNo}</span></td>
                        </tr>
                        <c:if test="${scenarioVo.campaignVo.depthNo eq 1}">
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.target"/></th><!-- 대상자 -->
                            <td colspan="3">
                                <div class="form-row align-items-center">
                                    <div class="col-5">
                                        <form:input path="segmentNm" readonly="true" cssClass="form-control form-control-sm" />
                                    </div>
                                    <div class="col-2">
                                        <form:input path="segmentSize" readonly="true" class="form-control form-control-sm d-inline-block w-75" />
                                        <span class="unit d-inline-block"><spring:message code="campaign.menu.persons"/></span><!-- 명 -->
                                    </div>
                                    <div class="col-5 pl-0">
                                        <button id="targetSearchBtn" class="btn btn-outline-primary btn-sm mr-1" type="button">
                                            <i class="fas fa-search"></i> <spring:message code="button.search"/><!-- 검색 -->
                                        </button>
                                        <button id="targetSelectBtn" class="btn btn-outline-primary btn-sm mr-1" type="button">
                                            <spring:message code="button.target.select"/><!-- 대상자 선택 -->
                                        </button>
                                        <button id="targetRegistBtn" class="btn btn-outline-primary btn-sm mr-1" type="button">
                                            <spring:message code="button.target.regist"/><!-- 대상자 등록 -->
                                        </button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </c:if>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="common.menu.subject" /></th><!-- 제목 -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm" name="campaignPreface" id="campaignPreface" /></td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- //card body -->

            <div class="card-body message_contents"><!-- message -->
                <iframe id="editorIfrm" name="editorIfrm" scrolling="no" style="height:620px !important;"
                    src="/editor/editor.do?type=campaign&campaignNo=${scenarioVo.campaignVo.campaignNo}&segmentNo=${scenarioVo.segmentNo}&handlerType=${scenarioVo.handlerType}&channelType=${scenarioVo.campaignVo.channelType}&useMultiLang=${useMultiLang}"
                ></iframe>
            </div>

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <div class="col offset-sm-4 col-4 text-center">
                        <button type="button" class="btn btn-outline-primary" id="saveBtn">
                            <spring:message code="button.save"/><!-- 저장 -->
                        </button>
                    </div>
                    <div class="col col-4">
                        <button type="button" class="btn btn-outline-info" id="prevStepBtn">
                            <i class="fas fa-chevron-left"></i> <spring:message code="button.prevstep"/><!-- 이전단계 -->
                        </button>
                        <button type="button" class="btn btn-outline-info" id="nextStepBtn">
                            <spring:message code="button.nextstep"/> <i class="fas fa-chevron-right"></i><!-- 다음단계 -->
                        </button>
                    </div>
                </div>
            </div><!-- e.button area -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form:form>
</body>
</html>
