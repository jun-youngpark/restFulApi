<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>2단계] 캠페인 메세지 작성 화면 출력 (FAX)
 - URL : /campaign/campaign2Step.do
 - Controller : com.mnwise.wiseu.web.campaign.web.Scenario2StepFormController
 - 이전 파일명 : campaign_2step_fax_form.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8"/>
<title><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.msg"/>)</title><!-- 캠페인 등록 (메시지 작성) -->
<script src="/js/common/common2step.js"></script>
<script src="/js/segment/target.js"></script><!-- 대상자 검색/선택/등록 -->
<%@ include file="/jsp/campaign/campaign2StepEvent_inc.jsp" %><!-- 저작기 2단계 공통 이벤트 -->
<script type="text/javascript">
    var loadStatus = 0;  // 화면로딩상태[0:로딩중, 1:로딩완료]. tinyMCE가 loadSet() 메소드를 호출하여 loadStatus값을 1로 설정

    var rules = {
        segmentSize : {min : 1},
        "campaignVo.senderTel" : {telnum: true, notBlank: true, maxlength: 50}
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
        // 미리보기 버튼 클릭
        $('#previewBtn').on("click", function(event) {
            var segmentNo = $("input[name='segmentNo']").val();
            if(segmentNo == undefined || segmentNo == 0) {
                alert('<spring:message code="common.alert.preview.msg1" />');  // 대상자를 선택하셔야 미리보기가 가능합니다.
                return;
            }

            var campaignLevel = $("input[name='campaignVo.campaignLevel']").val();
            if(campaignLevel < 2) {
                alert('<spring:message code="common.alert.preview.msg2" />');  // 메시지 저장 후  미리보기가 가능합니다.
                return;
            }

            var url = "/common/previewMainPopup.do?cmd=previewList&serviceNo=${scenarioVo.campaignVo.campaignNo}&serviceType=EM";  // /common/mime_view.do
            $.mdf.popupGet(url, 'previewPopup', 1000, 770);
        });

        // 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
            if(checkLoaded() == false || $.mdf.validForm("#editorFrm", rules, messages) == false) {
                return;
            }

            if(${scenarioVo.campaignVo.campaignSts eq 'C'}=== true) {
                alert('<spring:message code="common.alert.save.c1" />');  // 승인 요청 중인 캠페인은 변경/저장 하실 수 없습니다.
                return;
            }

            if(${scenarioVo.campaignVo.editAble} === false) {
                alert('<spring:message code="common.alert.save.c2" />');  // 발송 대기, 발송 중, 발송 완료, 분할중지, 발송 에러 또는 승인 요청 중인 캠페인은 변경/저장 하실 수 없습니다.
                return;
            }

            var param = {
                campaignNo : ${scenarioVo.campaignVo.campaignNo},
                campaignSts : "I"
            };

            //CampaignService.setCampaignStsInfo(${scenarioVo.campaignVo.campaignNo}, "I");
            $.post("/campaign/setCampaignStsInfo.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                }
            });

            if(editorIfrm.tinyMCEmode == false) editorIfrm.toggleEditor(1);
            $("input[name='campaignVo.campaignLevel']").val("2");

            editorIfrm.tinyMCE.triggerSave();
            editorIfrm.saveEditor(true);
        });
    }

    function initPage() {
        initStep3Btn();
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
            //CampaignService.isCampaignTestSend(campaignNo, {callback : function(returnValue) {
            $.post("/campaign/isCampaignTestSend.json?campaignNo=" + campaignNo, null, function(result) {
                if(result.code == "OK" || '${useTestSend}' == 'off') {
                    if(editorIfrm.tinyMCEmode == false) editorIfrm.toggleEditor(1);
                    editorIfrm.tinyMCE.triggerSave();
                    editorIfrm.saveEditor(true, "nextStep");
                } else {
                    alert('<spring:message code="common.alert.save.msg2" />');  // [다음 단계]로 이동하려면 [테스트 발송]을 해야 하며 발송이 완료되어야 합니다.\\n발송 완료까지  잠시 기다리세요.
                }
            });
        }
    }
</script>
</head>

<body>
<form:form id="editorFrm" name="editorFrm" commandName="scenarioVo" action="/campaign/campaign2Step.do" method="post"><!-- /campaign/campaign_2step_form.do -->
<form:hidden path="scenarioNo"/>
<form:hidden path="campaignVo.campaignNo"/>
<form:hidden path="campaignVo.depthNo"/>
<form:hidden path="campaignVo.channelType"/>
<form:hidden path="campaignVo.campaignNm"/>
<form:hidden path="campaignVo.sendingMode"/>
<form:hidden path="campaignVo.campaignSts"/>
<form:hidden path="campaignVo.campaignLevel"/>
<form:hidden path="campaignVo.templateType"/>
<form:hidden path="campaignVo.relationType"/>
<form:hidden path="campaignVo.retryCnt"/>
<form:hidden path="segmentNo"/>
<form:hidden path="campaignVo.userId"/>
<form:hidden path="campaignVo.grpCd"/>
<form:hidden path="campaignVo.editorId"/>
<input type="hidden" name="stepLevel" value="2"/>
<input type="hidden" name="imsiChannelType"/>
<input type="hidden" name="imsiTemplate"/>
<input type="hidden" name="imsiRelationType"/>
<input type="hidden" name="nextStep" />
<input type="hidden" name="campaignNo" value="${scenarioVo.campaignVo.campaignNo}"/>
<input type="hidden" name="campaignVo.campaignPreface"/>
<input type="hidden" name="campaignVo.senderNm" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.msg" />)</h3><!-- 캠페인 등록 (메시지 작성) -->
            </div>

            <div class="card-body">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-9"><!-- buttons -->
                        <button type="button" class="btn btn-sm btn-outline-primary" id="previewBtn">
                            <i class="fas fa-eye"></i> <spring:message code="campaign.alt.action.preview"/><!-- 미리보기 -->
                        </button>
                        <button type="button" class="btn btn-sm btn-outline-primary" id="testSendBtn">
                            <i class="fas fa-paper-plane"></i> <spring:message code="campaign.alt.action.tsend"/><!-- 테스트 발송 -->
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
                            <td>${scenarioVo.campaignVo.campaignNm}</td>
                            <th scope="row"><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 -->
                            <td><em class="txt_channel f">F</em> <span>${scenarioVo.campaignVo.campaignNo}</span></td>
                        </tr>
                        <tr>
                            <c:if test="${scenarioVo.campaignVo.depthNo eq 1}">
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.target"/></th><!-- 대상자 -->
                            <td>
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
                            </c:if>
                            <th scope="row"><!-- 발신자 번호 -->
                                <spring:message code="campaign.menu.sfax"/>
                            </th>
                            <td><form:input path="campaignVo.senderTel" cssClass="form-control form-control-sm w-50"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div><!-- //card-body -->

            <div class="card-body"><!-- message -->
                <iframe id="editorIfrm" name="editorIfrm" scrolling="no"
                    src="/editor/editor.do?type=campaign&campaignNo=${scenarioVo.campaignVo.campaignNo}&segmentNo=${scenarioVo.segmentNo}&handlerType=${scenarioVo.handlerType}&channelType=${scenarioVo.campaignVo.channelType}&useMultiLang=${useMultiLang}&&webExecMode=${scenarioVo.webExecMode}"
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
