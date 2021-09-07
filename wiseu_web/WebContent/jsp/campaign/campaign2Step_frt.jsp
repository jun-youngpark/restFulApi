<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>2단계] 캠페인 메세지 작성 화면 출력 (친구톡)
 - URL : /campaign/campaign2Step.do
 - Controller : com.mnwise.wiseu.web.campaign.web.Scenario2StepFormController
 - 이전 파일명 : campaign_2step_frtalk_form.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.msg"/>)</title><!-- 캠페인 등록 (메시지 작성) -->
<script src="/js/template/kakao_template.js"></script>
<script src="/js/common/common2step.js"></script>
<script src="/js/segment/target.js"></script><!-- 대상자 검색/선택/등록 -->
<%@ include file="/jsp/campaign/campaign2StepEvent_inc.jsp" %><!-- 저작기 2단계 공통 이벤트 -->
<script type="text/javascript">
    var loadStatus = 0;  // 화면로딩상태[0:로딩중, 1:로딩완료]. tinyMCE가 loadSet() 메소드를 호출하여 loadStatus값을 1로 설정
    var kakaoButtons = '';

    var rules = {
        segmentSize : {min : 1},
        "campaignVo.senderTel" : {telnum: true, maxlength: 50}
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
        // 실패건 문자발송여부 선택
        $("input:radio[name='campaignVo.failbackSendYn']").on("change", function(event) {
            if(this.value === 'Y') {
                $('#lmsSubjectTr').show();
            } else {
                $('#lmsSubjectTr').hide();
                $("input[name='campaignVo.failbackSubject']").val('');
                $("input[name='campaignVo.senderTel']").val('');
            }
        });

        // (버튼) 추가 버튼 클릭
        $("#addBtn").on("click", function(event) {
            var rowCount = $('.kakaoButtonList').length;
            if(rowCount > 4) {
                alert('<spring:message code="template.alert.kakao.button.exceed.limit" />');  // 버튼은 5개까지 만들 수 있습니다.
                return;
            }

            var linkType = $("#buttonLinkType").val();
            $('#kakao_button_table > tbody:last-child').append(makeKakaoButton(linkType));
        });

        // 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
            $("#editorFrm").addKkoButtonValidRule(rules, messages);    //kakao_template.js
            if(checkLoaded() == false || $.mdf.validForm("#editorFrm") == false) {
                return;
            }

            if('${scenarioVo.campaignVo.editAble}' === 'false') {
                alert('<spring:message code="common.alert.save.c2" />');  // 발송 대기, 발송 중, 발송 완료, 분할중지, 발송 에러 또는 승인 요청 중인 캠페인은 변경/저장 하실 수 없습니다.
                return false;
            }

            if('${scenarioVo.campaignVo.channelType}'=='C' && !chkAlimtalkData()) return false;

            var tinyMce = editorIfrm.tinyMCE;
            if(tinyMce.get("template").getContent().match(/[<]img[^>]+[>]/gi) == null) {
                $("input[name='campaignVo.kakaoImageNo']").val(0);
            }

            kakaoButtons = convertKakaoButtonsToJson();
            tinyMce.triggerSave();
            editorIfrm.saveEditor(true); //탬플릿, 핸들러 저장
        });
    }

    function initPage() {
        initStep3Btn();

        var campaignClass = $("input:radio[name='campaignVo.campaignClass']:checked").val();
        if(campaignClass === '') {
            $("input:radio[name='campaignVo.campaignClass'][value=A]").prop("checked", true);  // 광고문구표기여부 선택 - 표기함(A)
        }

        // 실패건 재발송 여부 선택에 따라 LMS/MMS 제목 활성화 상태 변경
        if('${scenarioVo.campaignVo.failbackSendYn}' != 'Y') {
            $('#lmsSubjectTr').hide();
        }
    }

    // 다음단계 버튼 클릭
    function onClickNextStepBtn() {
        $("#editorFrm").addKkoButtonValidRule(rules, messages);    //kakao_template.js
        if(checkLoaded() == false || $.mdf.validForm("#editorFrm") == false) {
            return;
        }

        if(chkAlimtalkData() == false) {
            return false;
        }

        if($("input:radio[name='campaignVo.failbackSendYn']").val() == 'Y') {
            var senderTel = $("input[name='campaignVo.senderTel']").val();
            if(senderTel == '') {
                alert("<spring:message code='campaign.error2.msg17'/>");  // 발신자 번호를 입력해 주세요.
                return;
            }
        }

        var scenarioNo = $("input[name=scenarioNo]").val();
        var campaignNo = '${scenarioVo.campaignVo.campaignNo}';
        var campaignLevel = $("input[name='campaignVo.campaignLevel']").val();
        if(campaignLevel > 2) {
            // /campaign/campaign_3step_form.do
            location.href="/campaign/campaign3Step.do?scenarioNo="+scenarioNo+"&depthNo=${depthNo}&campaignVo.campaignNo=${scenarioVo.campaignVo.campaignNo}&channelType=${scenarioVo.campaignVo.channelType}";
        } else {
            var tinyMce = editorIfrm.tinyMCE;
            if(tinyMce.get("template").getContent().match(/[<]img[^>]+[>]/gi) == null) {
                $("input[name='campaignVo.kakaoImageNo']").val(0);
            }

            kakaoButtons = convertKakaoButtonsToJson();
            tinyMce.triggerSave();
            editorIfrm.saveEditor(true);
        }
    }

    function chkAlimtalkData() {
        var kakaoSenderKey = $("select[name='campaignVo.kakaoSenderKey']").val();
        if($.mdf.isBlank(kakaoSenderKey)) {
            alert("<spring:message code='campaign.alert.yellowid.1'/>");  // 카카오톡 채널ID를 선택하세요.
            return false;
        }

        var failbackSendYn = $("input:radio[name='campaignVo.failbackSendYn']").val();
        if(failbackSendYn === 'Y') {
            var failbackSubject = $("input[name='campaignVo.failbackSubject']").val();
            if(failbackSubject.length > 40) {
                alert("<spring:message code='campaign.alert.failbacksubject.length'/>");  // 제목은 40 바이트를 초과할 수 없습니다.
                return false;
            }
        }
        return true;
    }
</script>
</head>

<body>
<form:form id="editorFrm" name="editorFrm" commandName="scenarioVo" action="/campaign/campaign2Step.do" method="post"><!-- /campaign/campaign_2step_form.do -->
<form:hidden path="scenarioNo"/>
<form:hidden path="campaignVo.campaignNo"/>
<form:hidden path="campaignVo.channelType"/>
<form:hidden path="campaignVo.campaignNm"/>
<form:hidden path="campaignVo.sendingMode"/>
<form:hidden path="campaignVo.campaignSts"/>
<form:hidden path="campaignVo.campaignLevel"/>
<form:hidden path="campaignVo.templateType"/>
<form:hidden path="campaignVo.templateSenderKey"/>
<form:hidden path="campaignVo.kakaoImageNo"/>
<form:hidden path="segmentNo"/>
<form:hidden path="campaignVo.userId"/>
<form:hidden path="campaignVo.grpCd"/>
<form:hidden path="campaignVo.editorId"/>
<input type="hidden" name="campaignVo.termType" value="1"/>
<input type="hidden" name="stepLevel" value="2"/>
<input type="hidden" name="campaignVo.campaignPreface"/>
<input type="hidden" name="imsiChannelType"/>
<input type="hidden" name="imsiTemplate"/>
<input type="hidden" name="nextStep" />
<input type="hidden" name="historyMsg" />
<input type="hidden" name="campaignVo.senderNm" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.msg"/>)</h3><!-- 캠페인 등록 (메시지 작성) -->
            </div>

            <div class="card-body">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-9"><!-- buttons -->
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
                            <col width="13%" />
                            <col width="*" />
                            <col width="15%" />
                            <col width="21%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cname"/></th><!-- 캠페인명 -->
                            <td> ${scenarioVo.campaignVo.campaignNm}</td>
                            <th scope="row"><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 -->
                            <td><em class="txt_channel c">C</em> <span>${scenarioVo.campaignVo.campaignNo}</span></td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><em class="required">required</em><spring:message code="campaign.menu.yellowid"/></th><!-- 카카오톡 채널ID -->
                            <td>
                                <form:select path="campaignVo.kakaoSenderKey" cssClass="form-control form-control-sm w-75">
                                    <form:options items="${kakaoProfileList}" itemValue="kakaoSenderKey" itemLabel="kakaoYellowId"/>
                                </form:select>
                            </td>
                            <th scope="row" style="letter-spacing: -1.5px;">
                                <em class="required">required</em><spring:message code="campaign.menu.sms.send.yn"/><!-- 실패건 문자발송여부 -->
                            </th>
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <form:radiobutton path="campaignVo.failbackSendYn" value="Y" cssClass="custom-control-input" id="failbackSendYn_Y" />
                                    <label class="custom-control-label" for="failbackSendYn_Y"><spring:message code="campaign.menu.item.sms.send.y"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <form:radiobutton path="campaignVo.failbackSendYn" value="N" cssClass="custom-control-input" id="failbackSendYn_N" />
                                    <label class="custom-control-label" for="failbackSendYn_N"><spring:message code="campaign.menu.item.sms.send.n"/></label>
                                </div>
                            </td>
                        </tr>
                        <tr id="lmsSubjectTr">
                            <th scope="row" class="ls--1px"><spring:message code="campaign.menu.sms.subject"/></th><!-- LMS/MMS 제목 -->
                            <td><form:input path="campaignVo.failbackSubject" cssClass="form-control form-control-sm"/></td>
                            <th scope="row" style="letter-spacing: -1.5px;"><em class="required">required</em><spring:message code="campaign.menu.resendsendertel"/></th><!-- 실패시 발신자 번호 -->
                            <td><form:input path="campaignVo.senderTel" cssClass="form-control form-control-sm"/></td>
                        </tr>
                        <c:if test="${scenarioVo.campaignVo.depthNo eq 1}">
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.target"/></th><!-- 대상자 -->
                            <td colspan="3">
                                <div class="form-row align-items-center row">
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
                                <label id="segmentSize-error" class="error" for="segmentSize"></label>
                            </td>
                        </tr>
                        </c:if>
                        <tr>
                            <th scope="row"><spring:message code='template.kakao.button' /></th><!-- 버튼 -->
                            <td class="border-right-0">
                                <select id="buttonLinkType" class="form-control form-control-sm d-inline-block w-auto" >
                                    <option value="WL"><spring:message code='template.kakao.link.type.WL' /></option><!-- 웹 링크 -->
                                    <option value="AL"><spring:message code='template.kakao.link.type.AL' /></option><!-- 앱 링크 -->
                                    <option value="DS"><spring:message code='template.kakao.link.type.DS' /></option><!-- 배송조회 -->
                                    <option value="BK"><spring:message code='template.kakao.link.type.BK' /></option><!-- 봇키워드 -->
                                    <option value="MD"><spring:message code='template.kakao.link.type.MD' /></option><!-- 메시지 전달 -->
                                </select>
                                <button id="addBtn" type="button" class="btn btn-sm btn-outline-primary ml-2">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                            </td>
                            <th scope="row" style="letter-spacing: -1.5px;">
                                <em class="required">required</em><spring:message code="campaign.menu.prepend.ad.yn"/><!-- 광고문구표기여부 -->
                            </th>
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <form:radiobutton path="campaignVo.campaignClass" value="A" cssClass="custom-control-input" id="campaignClass_A" />
                                    <label class="custom-control-label" for="campaignClass_A"><spring:message code="campaign.menu.item.prepend.ad.y"/></label><!-- 표기함 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <form:radiobutton path="campaignVo.campaignClass" value="I" cssClass="custom-control-input" id="campaignClass_I"/>
                                    <label class="custom-control-label" for="campaignClass_I"><spring:message code="campaign.menu.item.prepend.ad.n"/></label><!-- 표기안함 -->
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" class="p-0"><!-- 카카오 버튼-->
                                <table id="kakao_button_table" class="table table-sm dataTable table-fixed" style="margin-top: -1px !important;">
                                    <colgroup>
                                        <col width="13%" />
                                        <col width="*" />
                                        <col width="66" /><!-- 버튼명 -->
                                        <col width="*" />
                                        <col width="116" /><!-- 모바일 웹링크/Android 앱링크 -->
                                        <col width="18%" />
                                        <col width="89" /><!-- PC 웹링크/iOS 앱링크 -->
                                        <col width="18%" />
                                    </colgroup>
                                    <tbody>
                                    <%@ include file="/jsp/include/kkoButton_inc.jsp" %>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- // card body -->

            <div class="card-body message_contents"><!-- message -->
                <iframe id="editorIfrm" name="editorIfrm" scrolling="no" style="height:620px !important;"
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

<%@ include file="/jsp/include/makeKkoButton_inc.jsp" %>
</body>
</html>
