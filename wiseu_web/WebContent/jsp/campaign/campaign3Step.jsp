<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>3단계] 캠페인 수행 화면 출력(FAX외 모든 채널)
 - URL : /campaign/campaign3Step.do
 - Controller : com.mnwise.wiseu.web.campaign.web.Scenario3StepFormController
 - 이전 파일명 : campaign_3step_form.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.exec"/>)</title><!-- 캠페인 등록 (캠페인 수행) -->
<script src="/plugin/progress/jquery.progressbar-1.1.0.js"></script>
<script src='/plugin/date/jquery.jclock-2.2.1.js'></script>
<script src="/js/segment/sendCnt.js"></script>
<script src="/js/common/campaign.js"></script>
<script type="text/javascript">
    var abTargetCnt = 0;  // A/B테스트 대상자 명수
    var abTestRate = 0;  // A/B 테스트 대상자 비율

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 1단계 아이콘 클릭
        $('#step1Btn').on("click", function(event) {
            // /campaign/campaign_step_form.do
            window.location = '/campaign/campaign1Step.do?scenarioNo=${scenarioVo.scenarioNo}&campaignNo=${campaignNo}&depthNo=${depthNo}&channelType=${channelType}';
        });

        // 이전단계 버튼 클릭, 2단계 아이콘 클릭
        $('#prevStepBtn, #step2Btn').on("click", function(event) {
            // /campaign/campaign_2step_form.do
            location.href = "/campaign/campaign2Step.do?scenarioNo=${scenarioVo.scenarioNo}&campaignVo.campaignNo=${campaignNo}&depthNo=${depthNo}&campaignVo.channelType=${channelType}";
        });

        // 발송유형 선택
        $("input:radio[name=sendType]").on("change", function(event) {
            if(this.value == 'V') {  // V:분할발송 예약, R:예약발송 예약
                $('#divideSendTr').show();
                $('#reserveSendTr').hide();
            } else {
                $('#divideSendTr').hide();
                $('#reserveSendTr').show();
            }
        });

        // 분할 버튼 클릭
        $('#divideBtn').on("click", function(event) {
            var rules = {
                "campaignVo.divideCnt" : {digits : true, range : [2, 10]},
                divideHour : {digits : true, range : [0, 23]},
                divideMin : {digits : true, range : [0, 59]}
            };

            if($.mdf.validForm("#campaignFrm", rules) == false) {
                return;
            }

            var divideCnt = $('#divideCnt').val();
            var targetCnt = ${scenarioVo.segmentSize};
            var divideTargetCnt = Math.floor(targetCnt / divideCnt);  // 분할된 스케쥴 별 대상자 수

            // 대상자 수 보다 분할 횟수가 많을 경우
            if(divideTargetCnt == 0) {
                alert('<spring:message code="campaign.alert.msg.exec.20" />');  // 대상자수 보다 분할 횟수가 많습니다.
                return;
            }

            var divideHour = $('#divideHour').val();
            var divideMin = $('#divideMin').val();

            // 발송 간격이 0시 0분이 아닌지 체크
            if((divideHour+divideMin) <= 0) {
                alert('<spring:message code="campaign.alert.msg.exec.13" />');  // 발송간격은 0분 이상 입력하셔야 합니다.
                return;
            }

            var sendDt = $('#divideStartDt').val()
            if($.mdf.isBlank(sendDt)) {
                alert("<spring:message code='campaign.alert.msg4.divide' />");  // 분할발송 일자가 입력되지 않아 오늘 날짜로 자동 설정됩니다.
                sendDt = $.mdf.toDateString(new Date());
            } else {
                sendDt = sendDt.replace(/-/g, '');
            }
            var sendTm = $('#divideSendStartTm').val().replace(/:/g, '');
            var divideDateTime = new Date(sendDt.substr(0, 4), Number(sendDt.substr(4, 2))-1, sendDt.substr(6, 2), sendTm.substr(0,2), sendTm.substr(2,2), sendTm.substr(4,2));


            var divideRemainderCnt = targetCnt % divideCnt;  // 분할 후 나머지 대상자 (남는 대상자는 마지막 분할 스케쥴로 처리)
            var scheduleArrayList = [];
            for(var i=0; i<divideCnt; i++) {
                scheduleArrayList[i] = {
                      "divideSeq" : i
                    , "client" : 'EM'
                    , "serviceNo" : ${campaignNo}
                    , "startDt" : $.mdf.toDateTimeString(divideDateTime)
                    , "targetCnt" : ((i+1) == divideCnt) ? divideTargetCnt + divideRemainderCnt : divideTargetCnt
                }

                divideDateTime = $.mdf.addHours(divideDateTime, divideHour);
                divideDateTime = $.mdf.addMinutes(divideDateTime, divideMin);
            }

            var param = {
                campaignNo : ${campaignNo},
                divideScheduleList : scheduleArrayList,
                divideInterval : (parseInt(divideHour)*60) + parseInt(divideMin),
                startDate : $('#startDate').val() + " " + $('#startTm').val(),
                endDate : $('#endDate').val() + " " + $('#endTm').val(),
                channelType : "${channelType}"
            };

            // CampaignService.setRegistCampaignDivideScheduleInfo('${campaignNo}', scheduleArrayList, divideInterval, startDate, endDate, {callback : function() {
            $.ajax({
                cache : false,
                type : "post",
                url : "/campaign/setRegistCampaignDivideScheduleInfo.json",
                contentType: "application/json",
                data : JSON.stringify(param),
                dataType : "json",
                success : function(result) {
                    if(result.code == "OK") {
                        location.reload();
                    }
                }
            });
        });
    }

    function initPage() {
        if('${scenarioVo.segmentSize}' < 2) {
            $('#sendType_V').attr('disabled', 'false');
            $('#divideTextDiv').text('(<spring:message code="campaign.menu.divide.g"/>)');  // 1건 이상 분할발송이 가능합니다.
        }

        // A/B 테스트 발송 처리
        var abTestCond = "${scenarioVo.campaignVo.abTestCond}";  // A/B선택 조건

        $('#jclock').jclock({seedTime : ${seedTime}, format : '%Y-%m-%d %H:%M:%S'});

        if($.mdf.isBlank(abTestCond)) {
            $("select[name=abTestCond]").val('O');  // A/B선택 조건 설정 - 오픈율(O)
        } else {
            $("select[name=abTestCond]").val(abTestCond); // A/B선택 조건
        }

        var divideInterval = "${scenarioVo.campaignVo.divideInterval}";  // A/B 발송 분할 시간(분)
        if($.mdf.isBlank(divideInterval) || divideInterval=='0') {
            $("select[name=divideInterval]").val('3');
        } else {
            $("select[name=divideInterval]").val(divideInterval);
        }

        new mdf.Date("#sendStartDt");  // 발송일시-발송일
        new mdf.Time("#sendStartTm");  // 발송일시-발송시간
        new mdf.Date("#startDate");  // 반응추적 시작일
        new mdf.Date("#endDate");  // 반응추적 종료일
        new mdf.Date("#divideStartDt");  //
        new mdf.Time("#divideSendStartTm");  //  발송시간
    }

    // 생성된 분할예약 리스트를 배열로 만들어 반환한다.
    function getDivideSchedule() {
        var scheduleSize = $('#divideScheduleGroup tr').length;
        var scheduleArray = [];

        for(var i=0; i<scheduleSize-1; i++) {
            scheduleArray[i] = {
                  "divideSeq" : i
                , "client" : 'EM'
                , "serviceNo" : ${campaignNo}
                , "startDt" : ($('input[name=divideSendDt_'+i+']').val() + $('input[name=divideSendTm_'+i+']').val()).replace(/[-\/\.\:]/g, "")
                , "targetCnt" : $('input[name=divideSendTarget_'+i+']').val()
            }
        }
        return scheduleArray;
    }

    // 분할예약 기능 이용 AB 테스트
    function getAbDivideSchedule() {
        var scheduleArray = [];

        var selCnt = parseInt(abTargetCnt);
        var etcCnt = parseInt(${scenarioVo.segmentSize})-parseInt(abTargetCnt);
        var targetArr = [selCnt,etcCnt];  // A/B테스트시 선택된 대상자수, 전체대상자 - A/B선택된 대상자

        //발송시작일시
        var sendStartDate = $('#sendStartDt').val() + $('#sendStartTm').val();

        var scheduleSize = ("${scenarioVo.abTestType}" != "N") ? 2 : 0;  // AB테스트 사용시 2
        for(var i=0; i<scheduleSize; i++) {
            scheduleArray[i] = {
                  "divideSeq" : i
                , "client" : 'EM'
                , "serviceNo" : parseInt(${campaignNo})
                , "startDt" : sendStartDate.replace(/[-\/\.\:]/g, "")
                , "targetCnt" : targetArr[i]
            }
        }

        return scheduleArray;
    }

    var varCampaignSts;
    var varCampaignStsNm;
    var varApprovalSts;
    var varApprovalStsNm;
    var varChannel;
    var varCampaignNo;
    var varDepthNo;
    var timeover = false;

    // 발송 버튼 클릭
    function goRun(channel, campaignNo, campaignSts) {
        varChannel = channel;
        varCampaignNo = campaignNo;
        varCampaignSts = campaignSts;

        if(varChannel == 'C' && ${webExecMode} == 2) {  // wiseMOKA 친구톡인 경우
            if(varCampaignSts == "I" || varCampaignSts == "R") {
                if(isTimeOver()) {
                    timeover = true;
                    alert('<spring:message code="campaign.alert.schedule"/>');  // 발송 시간이 이미 경과했습니다. 시간을 다시 설정해주세요.
                    return;
                }

                varCampaignSts = "R";
                varCampaignStsNm = '<spring:message code="campaign.status.R"/>';  // 발송대기

                saveSchedule(channel, campaignNo, varCampaignSts)
            } else if(varCampaignSts == "P") {
                varCampaignSts = "P";
                varCampaignStsNm = '<spring:message code="campaign.status.P"/>';  // 보류
            }

            //CampaignService.setCampaignStsInfo(varCampaignNo, varCampaignSts, saveMokaStatCallback);
            var param = {
                campaignNo : varCampaignNo,
                campaignSts : varCampaignSts
            };

            $.post("/campaign/setCampaignStsInfo.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    callbackRunByMokaFrt(result.value);
                }
            });
        } else {
            if(varCampaignSts == "I" || varCampaignSts == "R") {
                if(isTimeOver()) {
                    timeover = true;
                    alert('<spring:message code="campaign.alert.schedule"/>');  // 발송 시간이 이미 경과했습니다. 시간을 다시 설정해주세요.
                    return;
                }

                varCampaignSts = "R";
                varCampaignStsNm = '<spring:message code="campaign.status.R"/>';  // 발송대기

                saveSchedule(channel, campaignNo, varCampaignSts)
            } else if(varCampaignSts == "P") {
                varCampaignSts = "P";
                varCampaignStsNm = '<spring:message code="campaign.status.P"/>';  // 보류

                //CampaignService.setCampaignStsInfo(varCampaignNo, varCampaignSts, callback);
                var param = {
                    campaignNo : varCampaignNo,
                    campaignSts : varCampaignSts
                };

                $.post("/campaign/setCampaignStsInfo.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        callbackRun(result.value);
                    }
                });
            }
        }
    }

    function isTimeOver() {
        var currentTime = $("#jclock").text().replace(/-/g, '').replace(/:/g, '').replace(' ', '');
        var reservedTime = $('#sendStartDt').val() + $('#sendStartTm').val();
        reservedTime = reservedTime.replace(/-/g, '').replace(/:/g, '');

        return parseInt(currentTime) > parseInt(reservedTime);
    }

    function callbackRun(dataFromServer) {
        if(dataFromServer == "blockChange") {
            alert('<spring:message code="campaign.alert.msg.exec.1"/>');  // 이미 상태 변경이 반영된 상태입니다.\\n브라우저를 새로고침 해 보시기 바랍니다.
        } else if(dataFromServer == "notSchedule") {
            alert('<spring:message code="campaign.alert.msg.exec.2"/>');  // 스케쥴이 저장되어 있지 않습니다. 스케쥴을 먼저 저장하세요.
        } else {
            $("input[name=campaignSts]").val(dataFromServer);
            $('#campaignStsDiv').text(varCampaignStsNm);
            $("#execBtn").attr("onclick","goRun('" + varChannel + "', '" + varCampaignNo + "','" + dataFromServer + "')");
        }

        location.reload();
    }

    function callbackRunByMokaFrt(dataFromServer) {
        if(dataFromServer == "blockChange") {
            alert("<spring:message code='campaign.alert.msg.exec.11'/>");  // 분할횟수는 2~10회 분할 가능합니다.
        } else if(dataFromServer == "notSchedule") {
            alert("<spring:message code='campaign.alert.msg.exec.12'/>");  // 발송간격은 숫자만 입력하셔야 합니다.
        } else {
            $("input[name=campaignSts]").val(dataFromServer);
        }

        if(varCampaignStsNm == '<spring:message code="campaign.status.R"/>') {  // 발송대기
            sendStartProgress();

            $("input[name=frmsendStartDt]").val($("#sendStartDt").val());
            $("input[name=frmsendStartTm]").val($("#sendStartTm").val());
            $("#campaignFrm").attr('action', '/common/sendMessage.do').submit();
        } else {
            location.reload();
        }
    }

    // 승인 요청 버튼 클릭
    function goApproval(channel, campaignNo, campaignSts, approvalSts) {
        varCampaignSts = campaignSts;
        varChannel = channel;
        varCampaignNo = campaignNo;
        varApprovalSts = approvalSts;
        if(varCampaignSts == "I") {
            alert('<spring:message code="campaign.alert.msg.exec.3" />');  // 캠페인이 미작성 상태입니다.\\n먼저 캠페인을 저장해야 합니다.
            return;
        }

        if(varApprovalSts == "C") {
            varApprovalStsNm = '<spring:message code="campaign.status.C"/>';  // 승인요청
        } else if(varApprovalSts == "A") {
            varApprovalStsNm = '<spring:message code="campaign.status.A"/>';  // 승인완료
        } else if(varApprovalSts == "D") {
            varApprovalStsNm = '<spring:message code="campaign.status.D"/>';  // 승인완료
        }

        //CampaignService.setApprovalStsInfo(varCampaignNo, varApprovalSts, callbackApproval);
        var param = {
            campaignNo : varCampaignNo,
            approvalSts : varApprovalSts
        };

        $.post("/campaign/setApprovalStsInfo.json", $.param(param, true), function(result) {
            if(result.code == "OK") {
                callbackApproval(result.value);
            }
        });
    }

    function callbackApproval(dataFromServer) {
        if(dataFromServer == "blockChange") {
            alert('<spring:message code="campaign.alert.msg.exec.1" />');  // 이미 상태 변경이 반영된 상태입니다.\\n브라우저를 새로고침 해 보시기 바랍니다.
        } else if(dataFromServer == "notSchedule") {
            alert('<spring:message code="campaign.alert.msg.exec.2" />');  // 스케쥴이 저장되어 있지 않습니다. 스케쥴을 먼저 저장하세요.
        } else {
            $("input[name='approvalSts']").val(dataFromServer);
        }
        $("#execBtn").attr("onclick","goRun('" + varChannel + "', '" + varCampaignNo + "','" + dataFromServer + "')");
        location.reload();
    }

    // 저장 버튼 클릭 - 발송 스케줄 정보 저장
    function saveSchedule(channelType, campaignNo, campaignStsToChange) {
    	if(isTimeOver()) {
            alert('<spring:message code="campaign.alert.schedule"/>');  // 발송 시간이 이미 경과했습니다. 시간을 다시 설정해주세요.
            return;
        }
        var channelCampaignSts = $("input[name=campaignSts]").val();
        if(channelCampaignSts == 'R') {
           alert('<spring:message code="campaign.alert.msg.exec.4" />');  // 발송 대기 중인 캠페인의 스케쥴을 변경 하실 수는 없습니다.
           return false;
        }

        if(channelCampaignSts == 'W') {
           alert('<spring:message code="campaign.alert.msg.exec.5" />');  // 발송 중인 캠페인의 스케쥴을 변경 하실 수는 없습니다.
           return false;
        }

        var channelApprovalSts = $("input[name=approvalSts]").val();
        if(channelApprovalSts == 'C') {
            alert('<spring:message code="campaign.alert.msg.exec.6" />');  // 승인 요청 중인 캠페인의 스케쥴을 변경 하실 수는 없습니다.
            return false;
        }

        var sendType = $('input:radio[name=sendType]:checked').val();

        if(sendType == 'V') {
            saveDivideSchedule(channelType, campaignNo, campaignStsToChange);
        } else {
            saveReserveSchedule(channelType, campaignNo, campaignStsToChange);
        }
    }

    function saveDivideSchedule(channelType, campaignNo, campaignStsToChange) {
        // 분할한 예약정보가 있는지 체크
        var scheduleSize = $('#divideScheduleGroup tr').length;
        if(scheduleSize-1 < 2) {
            alert('<spring:message code="campaign.alert.msg.exec.16" />');  // 분할발송 스케쥴을 등록해주세요.
            return;
        }

        // 발송 시작 시간이 순차적인지 체크
        var temp;
        for(var i=0; i<scheduleSize-1; i++) {
            var sendDtm = $('input[name=divideSendDt_'+i+']').val() + $('input[name=divideSendTm_'+i+']').val();

            if(i==0) {
                temp = sendDtm;
                continue;
            }

            if($.mdf.greaterThan(temp.replace(/[-\/\.\:]/g, ""), sendDtm.replace(/[-\/\.\:]/g, ""))) {
                alert('<spring:message code="campaign.alert.msg.exec.15" />');  // 분할발송은 순차적으로 설정하셔야 합니다. \\n발송 시작 시간을 확인해주세요.
                return;
            }

            temp = sendDtm;
        }

        // 대상자 건수가 분할된 대상자 건수와 일치하는지 체크
        var totalDivideCnt = 0;
        for(var i=0; i<scheduleSize-1; i++) {
            var divideCnt = $('input[name=divideSendTarget_'+i+']').val();
            // 분할대상자는 0건은 허용하지 않음.
            if(parseInt(divideCnt) <= 0) {
                alert('<spring:message code="campaign.alert.msg.exec.19" />');  // 각 분할발송 스케쥴 대상자 건수는 1건 이상 입력하셔야 합니다.
                return;
            }
            totalDivideCnt += parseInt(divideCnt);
        }

        if(totalDivideCnt != '${scenarioVo.segmentSize}') {
            alert('<spring:message code="campaign.alert.msg.exec.18" />');  // 분할 대상자 건수가 정확하지 않습니다.
            return;
        }

        var startDate = $('#startDate').val() + " " + $('#startTm').val();
        var endDate = $('#endDate').val() + " " + $('#endTm').val();
        var divideInterval = (parseInt($('#divideHour').val())*60) + parseInt($('#divideMin').val());
        var statusToChange = (campaignStsToChange === undefined) ? '' : campaignStsToChange;

        var param = {
            campaignNo : ${campaignNo},
            divideScheduleList : getDivideSchedule(),
            divideInterval : divideInterval,
            startDate : startDate,
            endDate : endDate,
            campaignSts : statusToChange
        };

        // CampaignService.setRegistCampaignDivideScheduleInfo('${campaignNo}', getDivideSchedule(), divideInterval, startDate, endDate, statusToChange, {callback : function(dataFromServer) {
        $.ajax({
            cache : false,
            type : "post",
            url : "/campaign/setRegistCampaignDivideScheduleInfo.json",
            contentType: "application/json",
            data : JSON.stringify(param),
            dataType : "json",
            success : function(result) {
                if(result.code == "OK") {
                    callbackSchedule(result.value, 'M');
                }
            }
        });
    }

    function saveReserveSchedule(channelType, campaignNo, campaignStsToChange) {

        var rules = {
            "campaignVo.sendStartDt" : {notBlank : true },
            "campaignVo.sendStartTm" : {notBlank : true, digits : true},
            "campaignVo.traceInfoVo.startDt" : {notBlank : true},
            "campaignVo.traceInfoVo.endDt" : {notBlank : true}
        };

        if($.mdf.validForm("#campaignFrm", rules) == false) {
            return;
        }

        // A/B테스트 발송시 유효성 체크
        var abTestCond = "";
        var divideCnt = 1;
        var divideInterval = 0;

        var abTestType = "${scenarioVo.abTestType}";
        if(abTestType == "S" || abTestType == "T") {  // A/B테스트 발송시
            if(abTestRate == 0 || abTestRate == null || abTestRate == 100) {
                alert('<spring:message code="campaign.alert.msg2.divide" />');  // 테스트 비율은 0 또는 100 % 설정이 불가능 합니다.
                return false;
            }

            if(abTargetCnt <= 1 || (parseInt(abTargetCnt) == ${scenarioVo.segmentSize})) {
                alert('<spring:message code="campaign.alert.msg3.divide" />');  // 테스트 대상자는 최소2명 이상부터 가능합니다.
                return false;
            }

            divideCnt = 2;
            divideInterval = parseInt($("#divideInterval").val());
            abTestCond = $("#abTestCond").val();
        }

        var startDate;
        var endDate;
        if(channelType == 'M') {
            startDate = $('#startDate').val() + " " + $('#startTm').val();
            endDate = $('#endDate').val() + " " + $('#endTm').val();
        }

        var param = {
            campaignNo : ${campaignNo},
            channelType : channelType,
            sendStartDate : $('#sendStartDt').val() + " " + $('#sendStartTm').val(),
            startDate : startDate,
            endDate : endDate,
            divideCnt : divideCnt,
            divideInterval : divideInterval,
            divideScheduleList : getAbDivideSchedule(),
            abTestCond : abTestCond,
            abTestRate : abTestRate,
            campaignSts : (campaignStsToChange === undefined) ? '' : campaignStsToChange
        };

        // CampaignService.setRegistCampaignScheduleInfo(campaignNo,channelType,sendStartDate,startDate,endDate,divideCnt,divideInterval,getAbDivideSchedule(),abTestCond,abTestRate,statusToChange,{callback : function(dataFromServer) {
        $.ajax({
            cache : false,
            type : "post",
            url : "/campaign/setRegistCampaignScheduleInfo.json",
            contentType: "application/json",
            data : JSON.stringify(param),
            dataType : "json",
            success : function(result) {
                if(result.code == "OK") {
                    callbackSchedule(result.value, channelType);
                }
            }
        });
    }

    function callbackSchedule(dataFromServer, channelType) {
        if(dataFromServer == 1) {
            $('#saveResultDiv').html("<spring:message code='campaign.alert.msg.save.save1'/>");  // 캠페인을 저장했습니다.
        } else if(dataFromServer == 300) {
            alert('<spring:message code="campaign.alert.msg.exec.6" />');
        } else {
            $('#saveResultDiv').html("<spring:message code='campaign.alert.msg.save.save2'/>");  // 캠페인 스케쥴 저장 중에 문제가 생겼습니다.
        }

        location.reload();
    }

    function goView(depth) {
        varDepthNo = depth;
        // /campaign/campaign_3step_form.do
        location.href = "/campaign/campaign3Step.do?scenarioNo=${scenarioVo.scenarioNo}&depthNo="+varDepthNo+"&campaignNo=${scenarioVo.campaignVo.campaignNo}&channelType=${channelType}";
    }
</script>
</head>

<body>
<form:form id="campaignFrm" name="campaignFrm" commandName="scenarioVo" action="/campaign/campaign3Step.do" method="post"><!-- /campaign/campaign_3step_form.do -->
<form:hidden path="scenarioNo" />
<form:hidden path="searchColumn" />
<form:hidden path="searchWord" />
<form:hidden path="orderColumn" />
<form:hidden path="orderSort" />
<form:hidden path="countPerPage" />
<form:hidden path="nowPage" />
<form:hidden path="finishYn" />
<form:hidden path="campaignVo.campaignNm" />
<form:hidden path="tagNo" />
<form:hidden path="grpCd" />

<c:forEach items="${scenarioVo.campaignList}" var="campaignVo">
    <input type="hidden" name="frmsendStartTm" id="frmsendStartTm" />
    <input type="hidden" name="frmsendStartDt" id="frmsendStartDt" />
    <input type="hidden" name="serviceNo" id="serviceNo" value="${campaignVo.campaignNo}"/>
    <input type="hidden" name="channel" id="channel" value="${campaignVo.channelType}"/>
    <input type="hidden" name="depthNo" id="depthNo" value="${campaignVo.depthNo}"/>
    <input type="hidden" name="serviceType" id="serviceType" value="em"/>
    <c:set var="editAble" value="${campaignVo.editAble}"/>
    <c:set var="campaignSts" value="${campaignVo.campaignSts}"/>
    <c:set var="campaignLevel" value="${campaignVo.campaignLevel}"/>
</c:forEach>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.exec"/>)</h3><!-- 캠페인 등록 (캠페인 수행) -->
            </div>

            <div class="card-body">
                <div class="row pr-3 justify-content-end"><!-- step -->
                    <ul class="stepWrap">
                        <li id="step1Btn" style="cursor: pointer;"><div class="box"><span class="txt">STEP</span><strong>1</strong></div></li>
                        <li id="step2Btn" style="cursor: pointer;"><div class="box"><span class="txt">STEP</span><strong>2</strong></div></li>
                        <li class="current" id="step3Btn" style="cursor: pointer;"><div class="box"><span class="txt">STEP</span><strong>3</strong></div></li>
                    </ul>
                </div>

                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="12%" />
                            <col width="38%" />
                            <col width="12%" />
                            <col width="38%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cname" /></th><!-- 캠페인명 -->
                            <td >${scenarioVo.scenarioNm}</td>
                            <th><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 -->
                            <td>${campaignNo}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.target" /></th><!-- 대상자 -->
                            <td colspan="3">${scenarioVo.segmentNm} - [<fmt:formatNumber value="${scenarioVo.segmentSize}" type="number" /> <spring:message code="campaign.menu.persons"/>]</td><!-- 명 -->
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cdes" /></th><!-- 캠페인 설명 -->
                            <td colspan="3">${scenarioVo.scenarioDesc}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.group" /></th><!-- 담당부서 -->
                            <td>${scenarioVo.grpNm}</td>
                            <th scope="row"><spring:message code="campaign.table.creator" /></th><!-- 작성자 -->
                            <td>${scenarioVo.userVo.nameKor}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cdate" /></th><!-- 작성일 -->
                            <td>${scenarioVo.createDtToDateStr}</td>
                            <th scope="row"><spring:message code="campaign.menu.udate" /></th><!-- 최종수정일 -->
                            <td>${scenarioVo.lastUpdateDtToDateStr}</td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div>

            <c:forEach items="${scenarioVo.campaignList}" var="campaignVo">
            <c:if test="${campaignVo.campaignNo eq campaignNo}">
            <div class="card-body">
                <h1 class="h3 text-primary mb-0"><spring:message code="common.msg.title"/></h1>
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="12%" />
                            <col width="38%" />
                            <col width="12%" />
                            <col width="38%" />
                        </colgroup>
                        <tbody>
                        <c:if test="${channelType eq 'M' || channelType eq 'T'}">
                        <tr>
                            <th><spring:message code="campaign.menu.subject"/></th><!-- 제목 -->
                            <td colspan="3">${campaignVo.campaignPreface}</td>
                        </tr>
                        </c:if>
                        <c:choose>
                        <c:when test="${channelType eq 'M'}">
                        <tr>
                            <th><spring:message code="campaign.menu.semail"/></th><!-- 발신자 이메일 -->
                            <td>${campaignVo.senderEmail} </td>
                            <th><spring:message code="campaign.menu.sname"/></th><!-- 발신자명 -->
                            <td>${campaignVo.senderNm}</td>
                        </tr>
                        <tr class="border-bottom">
                            <th>
                                <spring:message code="campaign.menu.susin"/><!-- 수신확인 -->
                            </th>
                            <td>
                                <c:choose>
                                    <c:when test="${campaignVo.campaignType eq 'Y'}">
                                        <spring:message code="campaign.menu.susin"/><!-- 수신확인 --></c:when>
                                    <c:otherwise><spring:message code="campaign.menu.nsusin"/><!-- 수신확인 안함 --></c:otherwise>
                                </c:choose>
                            </td>
                            <th scope="row"><spring:message code="campaign.menu.stime" /></th><!-- 서버 현재시간 -->
                            <td><span id="jclock"></span></td>
                        </tr>
                        </c:when>
                        <c:when test="${channelType eq 'S' || channelType eq 'T'}">
                        <tr>
                            <th><spring:message code="campaign.menu.sfax"/></th>
                            <td>${campaignVo.senderTel}</td>
                            <th><spring:message code="campaign.menu.sname"/></th>
                            <td>${campaignVo.senderNm}</td>
                        </tr>
                        </c:when>
                        </c:choose>

<%--                         <c:if test="${useDivide eq 'on' and channelType eq 'M'}"><!-- 메일 분할발송 사용시 --> --%>
                        <c:if test="${useDivide eq 'on'}"><!-- 메일 분할발송 사용시 -->
                        <tr <c:if test="${scenarioVo.abTestType eq 'S' || scenarioVo.abTestType eq 'T'}">style="display: none"</c:if>>
                            <th scope="row"><spring:message code="campaign.menu.sendType"/></th><!-- 발송유형 : 메일 -->
                            <td colspan="3">
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" class="custom-control-input" name="sendType" id="sendType_R" value="R" <c:if test="${campaignVo.divideYn ne 'Y'}">checked</c:if>>
                                    <label class="custom-control-label" for="sendType_R"><spring:message code="campaign.menu.reservation"/></label><!-- 예약발송 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline pr-0">
                                    <input type="radio" class="custom-control-input" name="sendType" id="sendType_V" value="V" <c:if test="${campaignVo.divideYn eq 'Y'}">checked</c:if>>
                                    <label class="custom-control-label" for="sendType_V"><spring:message code="campaign.menu.divide"/></label><!-- 분할발송 -->
                                </div>
                                <div class="custom-control custom-control-inline text-danger pl-0 pt-1 ml--2" id="divideTextDiv">
                                </div>
                            </td>
                        </tr>
                        </c:if>
                        <c:if test="${channelType ne 'M'}">
                            <tr>
                                <th scope="row"><spring:message code="campaign.menu.stime" /></th><!-- 서버 현재시간 -->
                                <td colspan="3"><span id="jclock"></span></td>
                            </tr>
                        </c:if>

                        <tr class="division-send" id="divideSendTr" <c:if test="${campaignVo.divideYn ne 'Y' || (campaignVo.divideYn eq 'Y' && scenarioVo.abTestType ne 'N')}">style="display: none"</c:if>>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.divide"/></th><!-- 분할발송 -->
                            <td colspan="5">
                                <table class="table table-sm dataTable table-fixed">
                                    <colgroup>
                                        <col width="20%" />
                                        <col width="30%" />
                                        <col width="30%" />
                                        <col width="20%" />
                                    </colgroup>
                                    <tr>
                                        <th class="text-center"><spring:message code="campaign.menu.divide.a"/></th><!-- 발송일자 -->
                                        <th class="text-center"><spring:message code="campaign.menu.divide.b"/></th><!-- 발송 시작 시간 -->
                                        <th class="text-center"><spring:message code="campaign.menu.divide.c"/></th><!-- 발송 간격 -->
                                        <th class="text-center"><spring:message code="campaign.menu.divide.d"/></th><!-- 분할 횟수 -->
                                    </tr>
                                    <tr>
                                        <td class="text-center"><!-- 발송일자 -->
                                            <div class="input_datebox">
                                                <input type="hidden" name="campaignVo.divideSendStartDt" id="divideStartDt" value="${campaignVo.sendStartDt}" maxlength="10">
                                            </div>
                                        </td>
                                        <td><!-- 발송시작시간 -->
                                            <input class="form-control form-control-sm" type="hidden" name="campaignVo.divideSendStartTm" id="divideSendStartTm" value="${campaignVo.sendStartTm}" maxlength="8">
                                        </td>
                                        <td><!-- 발송간격 -->
                                            <div class="col-6 pr-0">
                                                <div class="form-inline">
                                                    <input class="form-control form-control-sm w-50 d-inline-block" name="divideHour" id="divideHour" value="<fmt:parseNumber value='${campaignVo.divideInterval / 60}' integerOnly='true' />" maxlength="2">
                                                    <span class="mx-2 d-inline-block"><spring:message code="campaign.menu.divide.e"/></span><!-- 시간 -->
                                                </div>
                                            </div>
                                            <div class="col-6 pl-0">
                                                <div class="form-inline">
                                                    <input class="form-control form-control-sm w-50 d-inline-block" name="divideMin" id="divideMin" value="${campaignVo.divideInterval % 60}" maxlength="2">
                                                    <span class="ml-2 d-inline-block"><spring:message code="campaign.menu.divide.f"/></span><!-- 분 -->
                                                <div>
                                            </div>
                                        </td>
                                        <td><!-- 분할횟수 -->
                                            <div class="form-inline">
                                                <input class="form-control form-control-sm d-inline-block w-50" name="campaignVo.divideCnt" id="divideCnt" value="${campaignVo.divideCnt}">
                                                <button type="button" id='divideBtn' class="btn btn-sm btn-outline-primary ml-2">
                                                    <spring:message code="button.divide" /><!-- 분할 -->
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                    <tbody id="divideScheduleGroup">
                                    <tr>
                                    </tr>

                                    <c:forEach items="${campaignVo.campaignDivideScheduleList}" var="campaignDivideVo" varStatus="status">
                                    <tr>
                                        <td class="text-center">${status.index+1}</td>
                                        <td>
                                            <div class='d-flex align-items-center'>
                                                <fmt:parseDate value="${campaignDivideVo.startDt}" var="startDtm" pattern="yyyyMMddHHmmss"/>
                                                <input id='divideSendDt_${status.index}' name='divideSendDt_${status.index}' class='form-control form-control-sm d-inline-block w-100px' maxlength='10' type='text' value='<fmt:formatDate value="${startDtm}" pattern="yyyy-MM-dd"/>' readonly='readonly' disabled='disabled'/>&nbsp;
                                                <input id='divideSendTm_${status.index}' name='divideSendTm_${status.index}' class='form-control form-control-sm d-inline-block w-80px' maxlength='8' type='text' value='<fmt:formatDate value="${startDtm}" pattern="HH:mm:ss"/>' readonly='readonly' disabled='disabled'/>
                                            </div>
                                        </td>
                                        <td class="text-center">
                                            <spring:message code="common.menu.ctarget" /><!-- 대상수 -->
                                        </td>
                                        <td>
                                            <input id='divideSendTarget_${status.index}' name='divideSendTarget_${status.index}' class='form-control form-control-sm d-inline-block w-50' type='text' value='${campaignDivideVo.targetCnt}' readonly='readonly' disabled='disabled'/>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr id="reserveSendTr" <c:if test="${campaignVo.divideYn eq 'Y' && scenarioVo.abTestType eq 'N'}">style="display: none"</c:if>>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.sdate"/></th><!-- 발송일시 -->
                            <td colspan="3">
                                <c:choose>
                                    <c:when test="${not editAble and campaignSts ne 'R'}">${campaignVo.sendStartDtToDateStr}</c:when>
                                    <c:otherwise>
                                        <div class="form-row mx-0">
                                            <div class="input_datebox">
                                                <input type="hidden" name="campaignVo.sendStartDt" id="sendStartDt" value="${campaignVo.sendStartDt}" maxlength="10">
                                            </div>
                                            <div class="pl-1 d-inline-block">
                                                <input type="hidden" name="campaignVo.sendStartTm" id="sendStartTm" value="${campaignVo.sendStartTm}" maxlength="8">
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                        </tr>

                        <c:if test="${channelType eq 'M'}">
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.stracking"/></th><!-- 반응추적 시작일 -->
                            <td>
                                <c:choose>
                                    <c:when test="${not editAble and campaignSts ne 'R'}"> ${campaignVo.traceInfoVo.startDtToDateStr} </c:when>
                                    <c:otherwise>
                                        <div class="form-row mx-0">
                                            <div class="input_datebox">
                                               <input type="hidden" name="campaignVo.traceInfoVo.startDt" id="startDate" value="${campaignVo.traceInfoVo.startDt}" maxlength="10">
                                            </div>
                                        </div>
                                        <input type="hidden" name="campaignVo.traceInfoVo.startTm" id="startTm" value="000000" maxlength="8" class="input_time" readonly />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.etracking"/></th><!-- 반응추적 종료일 -->
                            <td>
                                <c:choose>
                                    <c:when test="${not editAble and campaignSts ne 'R'}"> ${campaignVo.traceInfoVo.endDtToDateStr} </c:when>
                                    <c:otherwise>
                                        <div class="form-row mx-0">
                                            <div class="input_datebox">
                                                <input type="hidden" name="campaignVo.traceInfoVo.endDt" id="endDate" value="${campaignVo.traceInfoVo.endDt}" maxlength="10">
                                            </div>
                                        </div>
                                        <input type="hidden" name="campaignVo.traceInfoVo.endTm" id="endTm" value="235900" maxlength="8" class="input_time" readonly />
                                    </c:otherwise>
                                </c:choose>
                             </td>
                        </tr>
                        </c:if>

                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.status"/></th><!-- 수행상태 -->
                            <c:choose>
                                <c:when test="${sessionScope.execute eq 'X' and campaignVo.approvalSts eq null}">
                                    <td colspan="5">
                                        <span id="campaignStsDiv" class="font-weight-bold">${campaignVo.campaignStsNm}</span>
                                        <input type="hidden" name="campaignSts" value="${campaignSts}" />
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <span id="campaignSts"><b>${campaignVo.campaignStsNm}</b></span>
                                        <input type="hidden" name="campaignSts" value="${campaignSts}" />
                                    </td>
                                    <th scope="row"><spring:message code="campaign.menu.approval"/></th><!-- 승인 상태 -->
                                    <td>
                                        <span id="approvalSts"><b>${campaignVo.approvalStsNm}</b></span>
                                        <input type="hidden" name="approvalSts" value="${campaignVo.approvalSts}" />
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>

                        <!-- A/B 테스트 발송 사용한 캠페인 이메일일 경우만 설정 항목 표시 -->
                        <c:if test="${scenarioVo.abTestType ne 'N'}">
                        <tr>
                            <th scope="row"><spring:message code="campaign.divide.rate"/></th><!-- A/B테스트 비율 -->
                            <td height="40">
                                <br /><div id="slider"></div><br />
                                <spring:message code="campaign.divide.rate"/>(0~100%) : <span id="amount" class="font-weight-bold text-warning"></span>
                            </td>
                            <th scope="row"><spring:message code="campaign.divide.cond"/></th><!-- A/B선택 조건 -->
                            <td colspan="3">
                                <select id="abTestCond" name="abTestCond" class="form-control form-control-sm d-inline-block w-auto" >
                                    <option value="O"><spring:message code="campaign.divide.open"/></option><!-- 오픈율 -->
                                    <option value="C"><spring:message code="campaign.divide.click"/></option><!-- 클릭율 -->
                                </select>
                                <select id="divideInterval" name="divideInterval" class="form-control form-control-sm d-inline-block w-auto" >
                                    <option value="3">3</option>
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="30">30</option>
                                    <option value="60">60</option>
                                    <option value="90">90</option>
                                    <option value="120">120</option>
                                    <option value="150">150</option>
                                    <option value="180">180</option>
                                </select>
                                <spring:message code="campaign.divide.mm"/><!-- 분 -->
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.divide.target"/></th><!-- A/B테스트 대상자 -->
                            <td id="testCnt" ></td>
                            <th scope="row"><spring:message code="campaign.divide.select.target"/></th><!-- 본발송 대상자 -->
                            <td id="etcCnt" colspan="3" ></td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- //card body -->
            </c:if>

            <div class="btn_area"><!-- button area -->
                <c:if test="${editAble or campaignSts eq 'R' or campaignSts eq 'C'}">
                    <c:if test="${campaignLevel eq '3'}">
                        <span id="saveResultDiv" class="font-weight-bold text-primary pr-2"></span>
                        <c:if test="${campaignVo.userId eq sessionScope.adminSessionVo.userVo.userId or sessionScope.adminSessionVo.userVo.userTypeCd eq 'A'}">
                            <c:if test="${sessionScope.write eq 'W'}">
                                <button type="button" class="btn btn-outline-primary" onclick="saveSchedule('${campaignVo.channelType}', ${campaignVo.campaignNo}); return false;">
                                    <spring:message code="ecare.menu.ssave"/><!-- 스케쥴 저장 버튼 -->
                                </button>
                            </c:if>
                        </c:if>
                    </c:if>
                 </c:if>

                <c:choose>
                    <c:when test="${(sessionScope.adminSessionVo.userVo.userTypeCd eq 'U')}" ><!-- USER그륩 -->
                        <c:if test="${campaignVo.userId eq sessionScope.adminSessionVo.userVo.userId}">
                        <c:choose>
                            <c:when test="${sessionScope.execute eq 'X'}"><!-- 실행권한이 있는 사용자(유저 userType=U) -->
                                <c:if test="${campaignVo.campaignSts eq 'R'}"><!-- 실행상태 일경우 -->
                                    <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goRun('S', '${campaignVo.campaignNo}','P'); return false;">
                                        <spring:message code="button.putoff"/><!-- 보류 -->
                                    </button>
                                </c:if>
                                <c:if test="${campaignVo.campaignSts eq 'P'}">
                                    <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goRun('S', '${campaignVo.campaignNo}','R'); return false;">
                                        <spring:message code="button.send"/><!-- 발송 -->
                                    </button>
                                 </c:if>
                            </c:when>
                            <c:otherwise><!-- 실행권한이 없는 사용자(유저 userType=U) -->
                                <c:if test="${sessionScope.write eq 'W'}">
                                    <c:if test="${campaignVo.approvalSts eq null or campaignVo.approvalSts eq 'D'}">
                                        <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goApproval('S', '${campaignVo.campaignNo}','${campaignVo.campaignSts}','C');">
                                            <spring:message code="button.approval.request"/><!-- 승인요청 -->
                                        </button>
                                    </c:if>
                                    <c:if test="${campaignVo.approvalSts eq 'A'}">
                                        <c:if test="${campaignVo.campaignSts eq 'R'}">
                                            <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goRun('S', '${campaignVo.campaignNo}','P'); return false;">
                                                <spring:message code="button.putoff"/><!-- 보류 -->
                                            </button>
                                        </c:if>
                                        <c:if test="${campaignVo.campaignSts eq 'P'}">
                                            <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goRun('S', '${campaignVo.campaignNo}','R'); return false;">
                                                <spring:message code="button.send"/><!-- 발송 -->
                                            </button>
                                        </c:if>
                                    </c:if>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <!-- 매니저,ADMIN 그륩-->
                        <c:if test="${campaignVo.approvalSts eq 'C'}">
                            <!--  승인 요청이 온 것일 경우 -->
                            <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goApproval('S', '${campaignVo.campaignNo}','${campaignVo.campaignSts}','A');">
                                <spring:message code="button.approval"/><!-- 승인 -->
                            </button>
                            <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goApproval('S', '${campaignVo.campaignNo}','${campaignVo.campaignSts}','D');">
                                <spring:message code="button.approval.reject"/><!-- 반려 -->
                            </button>
                        </c:if>
                        <c:if test="${campaignVo.campaignSts eq 'R'}">
                            <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goRun('S', '${campaignVo.campaignNo}','P'); return false;">
                                <spring:message code="button.putoff"/><!-- 보류 -->
                            </button>
                        </c:if>
                        <c:if test="${campaignVo.campaignSts eq 'P'}">
                            <button id="execBtn" type="button" class="btn btn-outline-primary" onclick="goRun('S', '${campaignVo.campaignNo}','R'); return false;">
                                <spring:message code="button.send"/><!-- 발송 -->
                            </button>
                        </c:if>
                    </c:otherwise>
                </c:choose><!-- 매니저,ADMIN 종료-->
            </div><!-- e.button area -->
            </c:forEach>

            <%@ include file="/jsp/campaign/campaign3StepOmni_inc.jsp" %><!-- 옴니채널 메세지 -->

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <div class="col justify-content-end">
                        <button type="button" class="btn btn-outline-info" id="prevStepBtn">
                            <i class="fas fa-chevron-left"></i> <spring:message code="button.prevstep"/><!-- 이전단계 -->
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