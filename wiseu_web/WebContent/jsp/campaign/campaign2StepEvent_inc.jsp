<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>2단계] 캠페인 메세지 작성 화면 출력 (공통)
 - URL : /campaign/campaign2Step.do
 - Controller : com.mnwise.wiseu.web.campaign.web.Scenario2StepFormController
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    function initCommonEventBind() {
        // 금칙어 검사 버튼 클릭 (메일, SMS/LMS/MMS, 브랜드톡, PUSH)
        $('#badWordCheckBtn').on("click", function(event) {
            editorIfrm.tinyMCE.triggerSave();

            var imsiChannelType = $("input[name='campaignVo.channelType']").val();

            var imsiTemplate = null;
            if("${scenarioVo.abTestType}" == "T") {  // A/B 템플릿 사용시
                if($('#editorIfrm').get(0).contentWindow.templateB) {  // 본문B 선택시
                    imsiTemplate = $('#editorIfrm').contents().find("textarea[name=templateAb]").val();
                } else {  // 본문A 선택시
                    imsiTemplate = $('#editorIfrm').contents().find("textarea[name=template]").val();
                }
            } else { // A/B 미사용 또는 A/B 제목 사용시
                if(imsiChannelType == 'P') {  // PUSH
                    imsiTemplate = $('#editorIfrm').contents().find("textarea[name=template]").val() + $('#editorIfrm').contents().find("textarea[name=template_pop]").val();
                } else {
                    imsiTemplate = $('#editorIfrm').contents().find("textarea[name=template]").val();
                }
            }

            if(imsiTemplate != null) {
                $("input[name=imsiTemplate]").val(imsiTemplate);
            }

            $("input[name=imsiChannelType]").val(imsiChannelType);
            $.mdf.popupSubmit("#editorFrm", '/common/badWordCheckPopup.do', "badWordPopup", 600, 480);  // /common/badword_popup.do

            $("input[name=imsiTemplate]").val("");
        });

        // 링크 유효성 검사 버튼 클릭 (메일)
        $('#linkCheckBtn').on("click", function(event) {
            editorIfrm.tinyMCE.triggerSave();

            var imsiTemplate = null;
            if("${scenarioVo.abTestType}" == "T") {  // A/B 템플릿 사용시
                //본문B 선택일때
                if($('#editorIfrm').get(0).contentWindow.templateB) {  // 본문B 선택시
                    imsiTemplate = $('#editorIfrm').contents().find("textarea[name=templateAb]").val();
                } else {  // 본문A 선택시
                    imsiTemplate = $('#editorIfrm').contents().find("textarea[name=template]").val();
                }
            } else { // A/B 미사용 또는 A/B 제목 사용시
                imsiTemplate = $('#editorIfrm').contents().find("textarea[name=template]").val();
            }

            if(imsiTemplate != null) {
                $("input[name=imsiTemplate]").val(imsiTemplate);
            }

            $.mdf.popupSubmit("#editorFrm", '/common/linkCheckPopup.do', "linkCheckPopup", 600, 480);  // /common/linkcheck_popup.do

            $("input[name=imsiTemplate]").val("");
        });

        // 테스트 발송 버튼 클릭 (메일, SMS/LMS/MMS, 친구톡/브랜드톡, FAX, PUSH)
        $('#testSendBtn').on("click", function(event) {
            var subType = '${scenarioVo.campaignVo.subType}';
            if(subType != "N") {  // 준실시간이 아닌 경우
                if('${scenarioVo.campaignVo.segmentNo}' == '') {  // 현재 캠페인의 segmentNo가 없는 경우
                    var segmentNo = $("#segmentNo").val();
                    if($.mdf.isBlank(segmentNo)) { // 선택된 대상자가 없는 경우
                        alert('<spring:message code="common.alert.save.msg5"/>');  // 대상자를 선택하셔야  테스트 발송이 가능합니다.
                        return;
                    } else { // 선택된 대상자가 있는 경우 해당 대상자를 저장한다.
                        //CampaignService.setTestCheck('${scenarioVo.campaignVo.campaignNo}', segmentNo);
                        var param = {
                            campaignNo : ${scenarioVo.campaignVo.campaignNo},
                            segmentNo : segmentNo
                        };

                        $.post("/campaign/setTestCheck.json", $.param(param, true), function(result) {
                            if(result.code == "OK") {
                            }
                        });
                    }
                }

                if($("input[name=segmentSize]").val() < 1) {
                    alert('<spring:message code="campaign.error2.msg16"/>');  // 대상자 수가 없습니다.
                    return;
                }
            }

            var campaignLevel = $("input[name='campaignVo.campaignLevel']").val();
            if(campaignLevel < 2) {
                alert('<spring:message code="common.alert.test.msg27" />');  // 메시지 저장 후  테스트발송이 가능합니다.
                return;
            }

            // /common/testSend_popup.do
            var url = '/common/testSendPopup.do?no=${scenarioVo.campaignVo.campaignNo}&serviceType=C&channelType=${scenarioVo.campaignVo.channelType}';
            $.mdf.popupGet(url, 'testSendPopup', 900, 750);
        });

        // 대상자 검색 버튼 클릭
        $('#targetSearchBtn').on("click", function(event) {
            var segmentNo = $("input[name=segmentNo]").val();
            if(segmentNo == undefined || segmentNo == 0) {
                alert('<spring:message code="common.alert.test.msg1" />');  // 대상자를 선택하세요
                return;
            }

            var segmentSize = $("input[name=segmentSize]").val();
            var segType = $("input[name=segType]").val();
            popupTargetList(segmentNo, segType, segmentSize);
        });

        // 대상자 선택 버튼 클릭
        $('#targetSelectBtn').on("click", function(event) {
            if(checkLoaded()) {
                popupTargetSelect("EM", "${scenarioVo.segmentNo}", "${scenarioVo.campaignVo.channelType}");
            }
        });

        // 대상자 등록 버튼 클릭
        $('#targetRegistBtn').on("click", function(event) {
            if(checkLoaded()) {
                popupTargetRegist();
            }
        });

        // 이전단계 버튼 클릭, 1단계 아이콘 클릭
        $('#prevStepBtn, #step1Btn').on("click", function(event) {
            // /campaign/campaign_step_form.do
            location.href="/campaign/campaign1Step.do?scenarioNo=${scenarioVo.scenarioNo}&campaignNo=${scenarioVo.campaignVo.campaignNo}&depthNo=${scenarioVo.campaignVo.depthNo}&channelType=${scenarioVo.campaignVo.channelType}";
        });

        // 다음단계 버튼 클릭
        $('#nextStepBtn').on("click", function(event) {
            onClickNextStepBtn();
        });
    }

    // 우측 상단 3단계 이미지 아이콘 이벤트 설정
    function initStep3Btn() {
        var maxLevel = ${scenarioVo.campaignVo.campaignLevel};
        if(maxLevel > 2) {
            $('#step3Btn').css('cursor','pointer');
            $('#step3Btn').on("click", function(event) {
                onClickNextStepBtn();
            });
        }
    }

    function goView(campaignNo, channelType, depthNo) {
        $("input[name='campaignVo.campaignNo']").val(campaignNo);
        $("input[name='campaignVo.channelType']").val(channelType);
        if($.mdf.isNotBlank(depthNo)) {
            $("input[name='campaignVo.depthNo']").val(depthNo);
        }

        $("#editorFrm").attr('action', '/campaign/campaign_2step_tab_form.do').submit();
    }

    // 에디터에서 호출
    function goSubmit(nextStep) {
        var channel = "${scenarioVo.campaignVo.channelType}";

        if(channel == "M" || channel == "T") {  // 메일, LMS/MMS 인 경우
            var campaignPreface = $('#editorIfrm').contents().find("input[name=campaignPreface]").val();
            $("input[name='campaignVo.campaignPreface']").val(campaignPreface);
        }

        if(channel == "M" || channel == "F") {  // 메일, FAX 인 경우
            var imsiRelationType = $("input[name=imsiRelationType]").val();
            if($.mdf.isNotBlank(imsiRelationType)) {
                $("input[name='campaignVo.relationType']").val(imsiRelationType);
            }

            $("input[name=nextStep]").val(nextStep);
        }

        $("#editorFrm").attr('action', '/campaign/campaign2Step.do').submit();  // /campaign/campaign_2step_form.do
    }
</script>
