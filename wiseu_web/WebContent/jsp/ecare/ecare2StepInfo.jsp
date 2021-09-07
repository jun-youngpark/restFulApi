<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<head>
<script src='/plugin/date/jquery.jclock-2.2.1.js'></script>
<script type="text/javascript">
    $(document).ready(function() {
        initEcareEventBind();
        initEcarePage();
    });

    function initEcarePage() {
        var cycleCd = '${ecareScenarioVo.ecareVo.ecareScheduleVo.cycleCd}';
        if($.mdf.isBlank(cycleCd)){
            $("#cycleCd_1").prop("checked", "true");
        }
        cycleChange(cycleCd)

        $('#jclock').jclock({ seedTime: ${seedTime}, format: '%Y-%m-%d %H:%M:%S' });

        <c:forEach items="${ecareScenarioVo.ecareVo.cycleItemList}" var="cycleItem">
            $('#${cycleItem.cycleItem}:radio').attr("checked", true);
            $('#${cycleItem.cycleItem}:checkbox').attr("checked", true);
        </c:forEach>
        var failRadioElm = '${ecareScenarioVo.ecareVo.failbackSendYn}';

        if(failRadioElm === 'N') {
        	 $("#failbackSubject").attr("disabled", true).val("");
             $("#senderTel").attr("disabled", true).val("");
        }

        if('${ecareScenarioVo.ecareVo.ecareScheduleVo.day}' > 0) {
            $("#monthOpt_1").prop("checked", "true");
        }

        if('${ecareScenarioVo.ecareVo.ecareScheduleVo.scheWeeknumber}' > 0) {
            $("#monthOpt_2").prop("checked", "true");
        }

        if('${ecareScenarioVo.ecareVo.ecareScheduleVo.weekday}' !='') {
            $("#weekday > option[value=${ecareScenarioVo.ecareVo.ecareScheduleVo.weekday}]").attr("selected", "true");
        }

        if('${ecareScenarioVo.ecareVo.ecareScheduleVo.scheWeeknumber}' !='') {
            $("#scheWeeknumber > option[value=${ecareScenarioVo.ecareVo.ecareScheduleVo.scheWeeknumber}]").attr("selected", "true");
        }

        new mdf.Date("#sendStartDt");  // 발송일시-시작일
        new mdf.Date("#sendEndDt");  // 발송일시-종료일
        new mdf.Time("#invokeTm");  // 발송일시-시작시간
        new mdf.Time("#minStartTm");  // 스케줄 설정-시작시간
        new mdf.Time("#minEndTm");  // 스케줄 설정-종료시간
        new mdf.Time("#divideSendStartTm");  //  분할 시작 시간
        new mdf.Date("#divideStartDt");  //
        var divideCnt;
        var targetCnt;
        //[20210727] [ljh] [분할 버튼 클릭]
        $('#divideBtn').on("click", function(event) {
            var rules = {
                "ecareScenarioVo.ecareVo.divideCnt" : {digits : true, range : [2, 10]},
                divideHour : {digits : true, range : [0, 23]},
                divideMin : {digits : true, range : [0, 59]}
            };

            if($.mdf.validForm("#ecareFrm", rules) == false) {
                return;
            }
            divideCnt = $('#divideCnt').val();
            targetCnt = $("input[name='segmentSize']").val();
            
            if($("input[name='segmentSize']").val() == 0){
            	$.ajax({
                    cache : false,
                    type : "post",
                    url : "/segment/renewalSegment.json?segmentNo="+$("input[name='segmentNo']").val(),
                    contentType: "application/json",
                    async: false,
                    success : function(result) {
                        $("input[name='segmentSize']").val(result.segmentSize);
                        targetCnt = result.segmentSize;
                    },
                   error : function(result) {
                	   return;
                   }
                });
            }
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
                    , "client" : 'EC'
                    , "serviceNo" : "${ecareScenarioVo.ecareVo.ecareNo}"
                    , "startDt" : $.mdf.toDateTimeString(divideDateTime)
                    , "targetCnt" : ((i+1) == divideCnt) ? divideTargetCnt + divideRemainderCnt : divideTargetCnt
                }

                divideDateTime = $.mdf.addHours(divideDateTime, divideHour);
                divideDateTime = $.mdf.addMinutes(divideDateTime, divideMin);
            }

            var param = {
                ecareNo : ${ecareScenarioVo.ecareVo.ecareNo},
                divideScheduleList : scheduleArrayList,
                divideInterval : (parseInt(divideHour)*60) + parseInt(divideMin),
                startDate : $('#startDate').val() + " " + $('#startTm').val(),
                endDate : $('#endDate').val() + " " + $('#endTm').val(),
                channelType : "${channelType}",
                cycleCd : "5"
            };

            $.ajax({
                cache : false,
                type : "post",
                url : "/ecare/setRegistEcareDivideScheduleInfo.json",
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

    function initEcareEventBind() {
        // 적용버튼 클릭
        $('#ecareSave').on("click", function(event) {
            event.preventDefault();
            ecareSave(false);
        });

        //상태변경 클릭 이벤트
        $('#ecareSts').on("change", function(event) {
            var val= this.checked;
            if(val===true){
                if(confirm('<spring:message code="ecare.2step.alert.change.status"/>')){
                    $('#editorAccordion .collapse').addClass('show');  //전체 펼치기
                    //상태 변경 전 전체 저장
                    $.when(ecareSave(true)).done(function(result1){
                        $.when(editorSaveHandler(true), targetSave(true)).done(function(result1, result2){
                            saveStat('S');
                        }).fail(function(error) {
                            $('#ecareSts').removeAttr("checked");
                            if(error !== "noAlram"){
                                alert(error);
                            }
                        });
                    }).fail(function(error) {
                        $('#ecareSts').removeAttr("checked");
                        if(error !== "noAlram"){
                            alert(error);
                        }
                    });
                }else{
                    $('#ecareSts').removeAttr("checked");
                }
            }else{
                saveStat('S');
            }
        });

        $("input[name='ecareVo.ecareScheduleVo.cycleCd']").on("click", function(event) {
            var cycleCd = $(this).val();
            cycleChange(cycleCd)
            $("input:radio[name='cycleItem']").prop("checked", false);
            $("input:checkbox[name='cycleItem']").prop("checked", false);
            $("input:radio[name='ecareVo.ecareScheduleVo.monthOpt']").prop("checked", false);
        });

        //알림톡|친구톡 우회발송 여부
        $("input:radio[name='ecareVo.failbackSendYn']").on("change", function(event) {
            if($("input:radio[name='ecareVo.failbackSendYn']:checked").val()=='Y'){
            	 $("#failbackSubject").attr("disabled", false);
                 $("#senderTel").attr("disabled", false);
            }else{
                $("#failbackSubject").attr("disabled", true).val("");
                $("#senderTel").attr("disabled", true).val("");
            }
        });

    }

    function cycleChange(cycleCd){
        if(cycleCd == "1") {
            $("#scheduleLayer").show();
            $("#scheduleDayLayer").show();
            $("#scheduleWeekLayer").hide();
            $("#scheduleMonthLayer").hide();
            $("#divideSendTr").hide();
            
        } else if(cycleCd == "2") {
            $("#scheduleLayer").show();
            $("#scheduleWeekLayer").show();
            $("#scheduleDayLayer").hide();
            $("#scheduleMonthLayer").hide();
            $("#divideSendTr").hide();
        } else if(cycleCd == "3") {
            $("#scheduleLayer").show();
            $("#scheduleMonthLayer").show();
            $("#scheduleDayLayer").hide();
            $("#scheduleWeekLayer").hide();
            $("#divideSendTr").hide();
        } else if(cycleCd == "4") {
            $("#scheduleLayer").show();
            $("#scheduleMonthLayer").hide();
            $("#scheduleDayLayer").hide();
            $("#scheduleWeekLayer").hide();
            $("#divideSendTr").hide();
        } else if(cycleCd == "5") {
            $("#scheduleLayer").hide();
            $("#divideSendTr").show();
        }
    }

    function ecareSave(allSave){
        var deferred = $.Deferred(); //전체 저장을 위한 함수
        if(editAble === false) {
            alert($.i18n.prop("common.alert.save.e1_"+webExecMode));
            return deferred.reject("noAlram");
        }
        if(valid2stepInfo() === false){
            return deferred.reject($.i18n.prop("vaildation.check.fail"));  //유효성 체크 실패
        };

        if($("input[name='ecareVo.ecareScheduleVo.cycleCd']:checked").val() == 5){ //분할발송일 경우 분할버튼을 눌러야함
	        var scheduleSize = $('#divideScheduleGroup tr').length;
	        if(scheduleSize-1 < 2) {
	            alert('<spring:message code="campaign.alert.msg.exec.16" />');  // 분할발송 스케쥴을 등록해주세요.
	            return;
	        }
        }
        var param = $.mdf.serializeObject('#ecareFrm');
        $.post("/ecare/ecare2StepInfo.json", $.param(param, true), function(result) {
              ajaxCallback(result, allSave, deferred); //common2step.js 전체적용일 경우 deferred 성공/실패 return
        return deferred;
        });
    }

    function valid2stepInfo(){
        var rules ={   // 유효성 체크
            'ecareVo.senderEmail' : { required : true, email : true, maxlength:50 },
            'ecareVo.senderTel' : { required : true, telnum : true, maxlength:11 },
            'ecareVo.senderNm' : { required : true, maxlength:25 },
            'ecareVo.failbackSendYn' : { required : true },
            'ecareVo.ecareNm' : { required : true , maxlength:50 },
            'ecareVo.failbackSubject' : { required : true },
            'ecareVo.retryCnt' : { required : true, range : [1, 9] },
            'ecareVo.reqDept' : { required : true},
            'cycleItem' : { required : true },
            'ecareVo.ecareScheduleVo.monthOpt' : { required : true },
            'ecareVo.ecareScheduleVo.startTm' : { required : true , maxlength:6 },
            'ecareVo.ecareScheduleVo.endTm' : { required : true , maxlength:6 },
            'ecareVo.ecareScheduleVo.termMin' : { required : true , maxlength:2 },
            'ecareVo.ecareScheduleVo.sendStartDt' : { required : true, date: true},
            'ecareVo.ecareScheduleVo.sendEndDt' : { required : true, date: true},
            'ecareVo.ecareScheduleVo.day' : { range : [1, 31] },
            'minStartTmEntry' : { required :true},
            'minEndTmEntry' : { required :true },
            'chrgNm' : { maxlength :15 },
            'brcNm' : { maxlength :15 }
        };
        var startTm = $("input[name='ecareVo.ecareScheduleVo.startTm']").val();
        var endTm = $("input[name='ecareVo.ecareScheduleVo.endTm']").val();
        if($.mdf.isBlank(startTm)){
            $("input[name='ecareVo.ecareScheduleVo.startTm']").val("000000");
        }
        if($.mdf.isBlank(endTm)){
            $("input[name='ecareVo.ecareScheduleVo.endTm']").val("000000");
        }
        var messages ={  // 유효성 체크 메세지
            'ecareVo.ecareScheduleVo.monthOpt':{
                required: $.i18n.prop("ecare.save.vaildate.period")
            }
        }
        return $.mdf.validForm('#ecareFrm', rules, messages);
    }
    
        
    function scheduleCallback(dataFromServer, channelType) {
        if(dataFromServer == 1) {
            document.getElementById('showResult').innerHTML = "&nbsp;&nbsp;&nbsp;저장완료";
        } else if(dataFromServer == 300) {
            alert('<spring:message code="campaign.alert.msg.exec.6" />');
        } else {
            document.getElementById('showResult').innerHTML = "&nbsp;&nbsp;&nbsp;저장실패";
        }

        
    }
</script>
</head>

<form name="ecareFrm" id="ecareFrm" method="post">
<input type="hidden" name="ecareVo.channelType" value="${ecareScenarioVo.ecareVo.channelType}">
<input type="hidden" name="ecareVo.subType" value="${ecareScenarioVo.ecareVo.subType}">
<input type="hidden" name="ecareVo.ecareNo" value="${ecareScenarioVo.ecareVo.ecareNo}">
<input type="hidden" name="scenarioNo" value="${ecareScenarioVo.scenarioNo}">
<input type="hidden" name="ecareVo.ecareScheduleVo.ecmScheduleNo" value="${ecareScenarioVo.ecareVo.ecmScheduleNo}">
<input type="hidden" name="handlerUpdateYn" value="N">
<input type="hidden" name="divideYn" id="divideYn" value="${ecareScenarioVo.ecareVo.divideYn}">

<div class="card">
    <div class="card-header bg-default toogleEvt" id="headingThree" data-toggle="collapse" data-target="#collapseThree" aria-expanded='true'>
        <h5 class="text-secondary pt-3">[STEP3] <spring:message code="ecare.2step.title.info" /></h5><!-- 발송정보 -->
    </div>
    <div id="collapseThree" class="collapse show" aria-labelledby="headingThree">
        <!-- card body -->
        <div class="card-body px-3 ">
            <div class="mt-2 justify-content-end">
                <button class="btn btn-sm btn-outline-primary btn-round mr-2" id="ecareSave">
                    <i class="fas fa-check"></i> <spring:message code="button.apply"/><!-- 적용 -->
                </button>
            </div>
            <!-- 서비스정보 -->
            <div class="col-6 align-items-center py-1"><!-- title -->
                <h4 class="h3 text-primary m-0"><spring:message code="ecare.2step.title.service"/></h4>
            </div>
            <div class="table-responsive gridWrap mb-3">
                <table class="table table-sm dataTable table-fixed">
                    <thead class="thead-light">
                        <colgroup>
                            <col width="11%" />
                            <col width="22%" />
                            <col width="11%" />
                            <col width="22%" />
                            <col width="11%" />
                            <col width="23%" />
                        </colgroup>
                    </thead>
                    <tr>
                        <th><em class="required"></em><spring:message code="ecare.menu.name_${webExecMode}"/></th><!-- 이케어명 -->
                        <td><input type="text" class="form-control form-control-sm" name="ecareVo.ecareNm" value="${ecareScenarioVo.ecareVo.ecareNm}"/></td>
                        <th><spring:message code="ecare.menu.group"/></th><!-- 담당부서-->
                        <td><input type="text" class="form-control form-control-sm" name="chrgNm" value="${ecareScenarioVo.chrgNm}"/></td>
                        <th><spring:message code="ecare.menu.user"/></th><!-- 담당자-->
                        <td><input type="text" class="form-control form-control-sm" name="brcNm" value="${ecareScenarioVo.brcNm}"/></td>
                    </tr>
                    <tr>
                        <th><em class="required"></em><spring:message code="ecare.deploytype" /></th><!-- 배포유형 -->
                        <td>
                            <select class="form-control form-control-sm" name="ecareVo.deployType">
                                <option value="REAL" ${ecareScenarioVo.ecareVo.deployType eq 'REAL' ? "selected" : ""}><spring:message code="ecare.deploytype.real" /></option>
                                <option value="SIMUL" ${ecareScenarioVo.ecareVo.deployType eq 'SIMUL' ? "selected" : ""}><spring:message code="ecare.deploytype.simul" /></option>
                            </select>
                        </td>
                        <th><em class="required"></em><spring:message code="ecare.menu.status"/></th><!-- 수행상태 -->
                        <td>
                            <c:choose>
                                <c:when test="${sessionScope.execute eq 'X'}">
                                    <div class="custom-toggle-wrap ">
                                        <label class="custom-toggle">
                                            <input type="checkbox" id="ecareSts" name="ecareSts" <c:if test="${ecareScenarioVo.ecareVo.ecareSts eq 'R'}">checked="checked"</c:if>>
                                            <span class="custom-toggle-slider rounded-circle" data-label-off="<spring:message code="ecare.status.E"/>" data-label-on="<spring:message code="ecare.status.R"/>"></span>
                                        </label>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <span id="EcareStsNm">${ecareScenarioVo.ecareVo.ecareStsNm}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td colspan="2"> </td>
                        </td>
                    </tr>
                </table>
            </div>

            <!-- 발송 스케줄 -->
            <c:if test="${serviceType eq 'schedule' || serviceType eq 'scheduleM'}">
            <div class="col-6 align-items-center py-1"><!-- title -->
                <h4 class="h3 text-primary m-0"><spring:message code="ecare.2step.title.schedule"/></h4>
            </div>
            <div class="table-responsive gridWrap mb-3">
                <table class="table table-sm dataTable table-fixed">
                    <thead class="thead-light">
                        <colgroup>
                            <col width="11%" />
                            <col width="22%" />
                            <col width="11%" />
                            <col width="*" />
                        </colgroup>
                    </thead>

                    <!-- 스케줄 설정  -->
                    <c:if test="${serviceType eq 'schedule'}">
                    <tr>
                        <th><spring:message code="ecare.menu.starttime3"/></th><!-- 서비스기간 -->
                        <td>
                            <div id='' class="periodWrap d-flex align-items-center">
                            <div class="input_datebox"><input type="hidden" name="ecareVo.ecareScheduleVo.sendStartDt" id="sendStartDt" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.sendStartDt}" maxlength="10"></div>
                            <span class="txt">~</span>
                            <div class="input_datebox"><input type="hidden" name="ecareVo.ecareScheduleVo.sendEndDt" id="sendEndDt" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.sendEndDt}" maxlength="10"></div>
                            </div>
                        </td>
                        <th><em class="required"></em><spring:message code="ecare.menu.sconfig"/></th><!-- 스케줄설정 -->
                        <td colspan="5">
                            <div class="custom-control custom-radio custom-control-inline ">
                                <input type="radio" id="cycleCd_1" name="ecareVo.ecareScheduleVo.cycleCd" class="custom-control-input" value="1" ${ecareScenarioVo.ecareVo.ecareScheduleVo.cycleCd eq '1'?"checked":""}>
                                <label class="custom-control-label" for="cycleCd_1"><spring:message code="ecare.schedule.daily"/></label>
                            </div>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="cycleCd_2" name="ecareVo.ecareScheduleVo.cycleCd" class="custom-control-input" value="2" ${ecareScenarioVo.ecareVo.ecareScheduleVo.cycleCd eq '2'?"checked":""}>
                                <label class="custom-control-label" for="cycleCd_2"><spring:message code="ecare.schedule.weekly"/></label>
                            </div>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="cycleCd_3" name="ecareVo.ecareScheduleVo.cycleCd" class="custom-control-input" value="3" ${ecareScenarioVo.ecareVo.ecareScheduleVo.cycleCd eq '3'?"checked":""}>
                                <label class="custom-control-label" for="cycleCd_3"><spring:message code="ecare.schedule.monthly"/></label>
                            </div>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="cycleCd_4" name="ecareVo.ecareScheduleVo.cycleCd" class="custom-control-input" value="4" ${ecareScenarioVo.ecareVo.ecareScheduleVo.cycleCd eq '4'?"checked":""}>
                                <label class="custom-control-label" for="cycleCd_4"><spring:message code="ecare.schedule.ones"/></label>
                            </div>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="cycleCd_5" name="ecareVo.ecareScheduleVo.cycleCd" class="custom-control-input" value="5" ${ecareScenarioVo.ecareVo.ecareScheduleVo.cycleCd eq '5'?"checked":""}>
                                <label class="custom-control-label" for="cycleCd_5"><spring:message code="campaign.menu.divide"/><!-- 분할발송 --></label>
                            </div>
                            
                            <error><label for="ecareVo.ecareScheduleVo.cycleCd" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                        </td>
                    </tr>
                    <!------------------------------------------------------------------ -->
                    <tr class="division-send" id="divideSendTr" style="display: none">
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.divide"/></th><!-- 분할발송 -->
                            <td colspan="7">
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
                                                <input type="hidden" name="divideSendStartDt" id="divideStartDt" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.sendStartDt}" maxlength="10">
                                            </div>
                                        </td>
                                        <td><!-- 발송시작시간 -->
                                            <input class="form-control form-control-sm" type="hidden" name="divideSendStartTm" id="divideSendStartTm" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.invokeTm}" maxlength="8">
                                        </td>
                                        <td><!-- 발송간격 -->
                                            <div class="col-6 pr-0">
                                                <div class="form-inline">
                                                    <input class="form-control form-control-sm w-50 d-inline-block" name="divideHour" id="divideHour" value="<fmt:parseNumber value='${ecareScenarioVo.ecareVo.divideInterval / 60}' integerOnly='true' />" maxlength="2">
                                                    <span class="mx-2 d-inline-block"><spring:message code="campaign.menu.divide.e"/></span><!-- 시간 -->
                                                </div>
                                            </div>
                                            <div class="col-6 pl-0">
                                                <div class="form-inline">
                                                    <input class="form-control form-control-sm w-50 d-inline-block" name="divideMin" id="divideMin" value="${ecareScenarioVo.ecareVo.divideInterval % 60}" maxlength="2">
                                                    <span class="ml-2 d-inline-block"><spring:message code="campaign.menu.divide.f"/></span><!-- 분 -->
                                                <div>
                                            </div>
                                        </td>
                                        <td><!-- 분할횟수 -->
                                            <div class="form-inline">
                                                <input class="form-control form-control-sm d-inline-block w-50" name="ecareScenarioVo.ecareVo.divideCnt" id="divideCnt" value="${ecareScenarioVo.ecareVo.divideCnt}">
                                                <button type="button" id='divideBtn' class="btn btn-sm btn-outline-primary ml-2">
                                                    <spring:message code="button.divide" /><!-- 분할 -->
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                    <tbody id="divideScheduleGroup">
                                    <tr>
                                    </tr>

                                    <c:forEach items="${ecareScenarioVo.ecareVo.ecareDivideScheduleList}" var="ecareDivideVo" varStatus="status">
                                    <tr>
                                        <td class="text-center">${status.index+1}</td>
                                        <td>
                                            <div class='d-flex align-items-center'>
                                                <fmt:parseDate value="${ecareDivideVo.startDt}" var="startDtm" pattern="yyyyMMddHHmmss"/>
                                                <input id='divideSendDt_${status.index}' name='divideSendDt_${status.index}' class='form-control form-control-sm d-inline-block w-100px' maxlength='10' type='text' value='<fmt:formatDate value="${startDtm}" pattern="yyyy-MM-dd"/>' readonly='readonly' disabled='disabled'/>&nbsp;
                                                <input id='divideSendTm_${status.index}' name='divideSendTm_${status.index}' class='form-control form-control-sm d-inline-block w-80px' maxlength='8' type='text' value='<fmt:formatDate value="${startDtm}" pattern="HH:mm:ss"/>' readonly='readonly' disabled='disabled'/>
                                            </div>
                                        </td>
                                        <td class="text-center">
                                            <spring:message code="common.menu.ctarget" /><!-- 대상수 -->
                                        </td>
                                        <td>
                                            <input id='divideSendTarget_${status.index}' name='divideSendTarget_${status.index}' class='form-control form-control-sm d-inline-block w-50' type='text' value='${ecareDivideVo.targetCnt}' readonly='readonly' disabled='disabled'/>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr id="scheduleLayer">
                        <th><spring:message code="ecare.menu.starttime"/></th><!-- 최초 발송 시작 시간 -->
                        <td>
                            <input type="hidden" name="ecareVo.ecareScheduleVo.invokeTm" id="invokeTm" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.invokeTm}" maxlength="8">
                        </td>
                        <th><spring:message code="ecare.menu.dconfig"/></th><!-- 세부설정 -->
                        <td colspan="5">
                            <div id="scheduleDayLayer"><!-- 일단위 세부 정보 -->
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="FULLDAY" name="cycleItem" class="custom-control-input" value="FULLDAY">
                                    <label class="custom-control-label" for="FULLDAY"><spring:message code="ecare.schedule.week"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="WORKDAY" name="cycleItem" class="custom-control-input" value="WORKDAY">
                                    <label class="custom-control-label" for="WORKDAY"><spring:message code="ecare.schedule.weekday1"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="RWORKDAY" name="cycleItem" class="custom-control-input" value="RWORKDAY">
                                    <label class="custom-control-label" for="RWORKDAY"><spring:message code="ecare.schedule.weekday2"/></label>
                                </div>
                                <error><label for="cycleItem" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                            </div>
                            <div id="scheduleWeekLayer" style="display: none">  <!-- 주단위 세부정보 -->
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="checkbox" class="custom-control-input" id="MON" name="cycleItem" value="MON">
                                    <label class="custom-control-label" for="MON"><spring:message code="env.menu.offday.mon"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="checkbox" class="custom-control-input" id="TUE" name="cycleItem" value="TUE">
                                    <label class="custom-control-label" for="TUE"><spring:message code="env.menu.offday.tue"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="checkbox" class="custom-control-input" id="WED" name="cycleItem" value="WED">
                                    <label class="custom-control-label" for="WED"><spring:message code="env.menu.offday.wed"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="checkbox" class="custom-control-input" id="THU" name="cycleItem"  value="THU">
                                    <label class="custom-control-label" for="THU"><spring:message code="env.menu.offday.thu"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="checkbox" class="custom-control-input" id="FRI" name="cycleItem"  value="FRI">
                                    <label class="custom-control-label" for="FRI"><spring:message code="env.menu.offday.fri"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="checkbox" class="custom-control-input" id="SAT" name="cycleItem" value="SAT">
                                    <label class="custom-control-label" for="SAT"><spring:message code="env.menu.offday.sat"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="checkbox" class="custom-control-input" id="SUN" name="cycleItem" value="SUN">
                                    <label class="custom-control-label" for="SUN"><spring:message code="env.menu.offday.sun"/></label>
                                </div>
                                <error><label for="cycleItem" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                            </div>
                            <div id="scheduleMonthLayer" style="display: none"><!-- 월단위 -->
                                <table class="table table-sm dataTable fixed">
                                    <tr>
                                        <td>
                                            <div class="form-inline">
                                                <div class="custom-control custom-radio custom-control-inline mt-1">
                                                    <input type="radio" class="custom-control-input" id="monthOpt_1" name="ecareVo.ecareScheduleVo.monthOpt"  value="STATIC">
                                                    <label class="custom-control-label" for="monthOpt_1"><spring:message code="ecare.menu.date"/></label>
                                                </div>
                                                <div class="custom-control custom-control-inline px-0">
                                                    <input type="text" class="form-control form-control-sm d-inline w-25" name="ecareVo.ecareScheduleVo.day" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.day}" />
                                                    <span class="d-inline mt-2"><spring:message code="report.campaign.odl.summary.day"/></span>
                                                </div>
                                                <div class="custom-control custom-radio custom-control-inline mt-1">
                                                    <input type="radio" id="monthOpt_2" name="ecareVo.ecareScheduleVo.monthOpt" class="custom-control-input" value="CONTEXT">
                                                    <label class="custom-control-label" for="monthOpt_2"><spring:message code="report.campaign.search.date"/></label>
                                                </div>
                                                <div class="custom-control custom-control-inline pl-0">
                                                    <select id="scheWeeknumber" name="ecareVo.ecareScheduleVo.scheWeeknumber" class="form-control form-control-sm d-inline w-auto" >&nbsp;
                                                        <option value="1"><spring:message code="ecare.week.1"/></option>
                                                        <option value="2"><spring:message code="ecare.week.2"/></option>
                                                        <option value="3"><spring:message code="ecare.week.3"/></option>
                                                        <option value="4"><spring:message code="ecare.week.4"/></option>
                                                        <option value="5"><spring:message code="ecare.week.5"/></option>
                                                    </select>
                                                    <select id="weekday" name="ecareVo.ecareScheduleVo.weekday" class="form-control form-control-sm d-inline w-auto pl-3" >
                                                        <option value="MON" ><spring:message code="env.menu.offday.mon"/></option>
                                                        <option value="TUE" ><spring:message code="env.menu.offday.tue"/></option>
                                                        <option value="WED" ><spring:message code="env.menu.offday.wed"/></option>
                                                        <option value="THU" ><spring:message code="env.menu.offday.thu"/></option>
                                                        <option value="FRI" ><spring:message code="env.menu.offday.fri"/></option>
                                                        <option value="SAT" ><spring:message code="env.menu.offday.sat"/></option>
                                                        <option value="SUN" ><spring:message code="env.menu.offday.sun"/></option>
                                                    </select>
                                                </div>
                                                <error><label for="ecareVo.ecareScheduleVo.day" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                                            </div>
                                                <error><label for="ecareVo.ecareScheduleVo.monthOpt" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="JAN" name="cycleItem" class="custom-control-input" value="JAN">
                                                <label class="custom-control-label" for="JAN"><spring:message code="env.menu.offday.january"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="FEB" name="cycleItem" class="custom-control-input" value="FEB">
                                                <label class="custom-control-label" for="FEB"><spring:message code="env.menu.offday.february"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="MAR" name="cycleItem" class="custom-control-input" value="MAR">
                                                <label class="custom-control-label" for="MAR"><spring:message code="env.menu.offday.march"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="APR" name="cycleItem" class="custom-control-input" value="APR">
                                                <label class="custom-control-label" for="APR"><spring:message code="env.menu.offday.april"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="MAY" name="cycleItem" class="custom-control-input" value="MAY">
                                                <label class="custom-control-label" for="MAY"><spring:message code="env.menu.offday.may"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="JUN" name="cycleItem" class="custom-control-input" value="JUN">
                                                <label class="custom-control-label" for="JUN"><spring:message code="env.menu.offday.june"/></label>
                                            </div>
                                            </br>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="JUL" name="cycleItem" class="custom-control-input" value="JUL">
                                                <label class="custom-control-label" for="JUL"><spring:message code="env.menu.offday.july"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="AUG" name="cycleItem" class="custom-control-input" value="AUG">
                                                <label class="custom-control-label" for="AUG"><spring:message code="env.menu.offday.august"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="SEP" name="cycleItem" class="custom-control-input" value="SEP">
                                                <label class="custom-control-label" for="SEP"><spring:message code="env.menu.offday.september"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="OCT" name="cycleItem" class="custom-control-input" value="OCT">
                                                <label class="custom-control-label" for="OCT"><spring:message code="env.menu.offday.october"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="NOV" name="cycleItem" class="custom-control-input" value="NOV">
                                                <label class="custom-control-label" for="NOV"><spring:message code="env.menu.offday.november"/></label>
                                            </div>
                                            <div class="custom-control custom-checkbox custom-control-inline">
                                                <input type="checkbox" id="DEC" name="cycleItem" class="custom-control-input" value="DEC">
                                                <label class="custom-control-label" for="DEC"><spring:message code="env.menu.offday.december"/></label>
                                            </div>
                                            <error><label for="cycleItem" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                                        </td>
                                    </tr>
                                    
                                </table>
                            </div>
                        </td>
                    </tr>
                    </c:if>

                    <!-- 스케줄(분) 설정  -->
                    <c:if test="${serviceType eq 'scheduleM'}">
                    <input type="hidden"  name="ecareVo.ecareScheduleVo.cycleCd" value="1"/>
                    <tr>
                        <th><em class="required"></em><spring:message code="ecare.menu.starttime3"/></th><!-- 서비스기간 -->
                        <td>
                            <div class="periodWrap d-flex align-items-center">
                            <div class="input_datebox"><input type="hidden" name="ecareVo.ecareScheduleVo.sendStartDt" id="sendStartDt" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.sendStartDt}" maxlength="10"></div>
                            <span class="txt">~</span>
                            <div class="input_datebox"><input type="hidden" name="ecareVo.ecareScheduleVo.sendEndDt" id="sendEndDt" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.sendEndDt}" maxlength="10"></div>
                        </td>
                        <!-- 스케줄(분) cyclecd 1(매일)고정 -->
                        <th rowspan="2"><em class="required"></em><spring:message code="ecare.menu.sconfig"/></th><!-- 스케줄설정 -->
                        <td colspan="5">
                            <div class="custom-control custom-radio custom-control-inline ">
                                <input type="radio" id="FULLDAY" name="cycleItem" class="custom-control-input" value="FULLDAY">
                                <label class="custom-control-label" for="FULLDAY"><spring:message code="ecare.schedule.week"/></label>
                            </div>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="WORKDAY" name="cycleItem" class="custom-control-input" value="WORKDAY">
                                <label class="custom-control-label" for="WORKDAY"><spring:message code="ecare.schedule.weekday1"/></label>
                            </div>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="RWORKDAY" name="cycleItem" class="custom-control-input" value="RWORKDAY">
                                <label class="custom-control-label" for="RWORKDAY"><spring:message code="ecare.schedule.weekday2"/></label>
                            </div>
                            <error><label for="cycleItem" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                        </td>
                    </tr>
                    <tr>
                        <th><em class="required"></em><spring:message code="ecare.menu.starttime"/></th><!-- 최초 발송 시작 시간 -->
                        <td>
                            <input type="hidden" name="ecareVo.ecareScheduleVo.invokeTm" id="invokeTm" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.invokeTm}" maxlength="8">
                        </td>
                        <td colspan="5">
                            <div class="form-row align-items-center pl-2">
                                <div class="float-left d-inline-block mr-2 py-1 font-weight-bolder"><spring:message code="ecare.menu.starttime2"/></div>
                                <div class="float-left d-inline-block mr-4">
                                    <input type="hidden" name="ecareVo.ecareScheduleVo.startTm" id="minStartTm" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.startTm}" maxlength="6"/>
                                    <label for="minStartTm" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                                </div>
                                <div class="float-left d-inline-block mr-2 py-1 font-weight-bolder"><spring:message code="ecare.menu.endtime"/></div>
                                <div class="float-left d-inline-block mr-4">
                                    <input type="hidden" name="ecareVo.ecareScheduleVo.endTm" id="minEndTm" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.endTm}" maxlength="6"/>
                                    <label for="minEndTm" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                                </div>
                                <div class="float-left d-inline-block mr-2 py-1 font-weight-bolder"><spring:message code="ecare.menu.term"/></div>
                                <div class="float-left d-inline-block">
                                    <input type="text" name="ecareVo.ecareScheduleVo.termMin" id="termMin" value="${ecareScenarioVo.ecareVo.ecareScheduleVo.termMin}" class="form-control form-control-sm w-50" maxlength="2"
                                        onKeyPress="if((event.keyCode<48)||(event.keyCode>57)) event.returnValue=false;" />
                                    <label for="termMin" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                                </div>
                            </div>
                        </td>
                    </tr>
                    </c:if>
                </table>
            </div>
            </c:if>

            <!-- 채널옵션 -->
            <c:if test="${ecareScenarioVo.ecareVo.channelType eq 'S'
                          or ecareScenarioVo.ecareVo.channelType eq 'T'
                          or ecareScenarioVo.ecareVo.channelType eq 'M'
                          or ecareScenarioVo.ecareVo.channelType eq 'A' }">
            <div class="col-6 align-items-center py-1"><!-- title -->
                <h4 class="h3 text-primary m-0"><spring:message code="ecare.2step.title.channel.option"/></h4>
            </div>
            <div class="table-responsive gridWrap mb-3">
                <table class="table table-sm dataTable table-fixed">
                    <thead class="thead-light">
                        <colgroup>
                            <col width="11%" />
                            <col width="22%" />
                            <col width="11%" />
                            <col width="22%" />
                            <col width="11%" />
                            <col width="23%" />
                        </colgroup>
                    </thead>

                    <!-- 채널 별 정보 문자(sms.lms)-->
                    <c:if test="${ecareScenarioVo.ecareVo.channelType eq 'S' or ecareScenarioVo.ecareVo.channelType eq 'T'}">
                    <tr>
                        <c:choose>
                            <c:when test="${ecareScenarioVo.smsIndividualCharge eq 'on'}">
                                <c:set var="smsColSpan" value="2"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="smsColSpan" value="4"/>
                            </c:otherwise>
                        </c:choose>
                        <th><em class="required"></em><spring:message code="ecare.menu.ssms"/></th><!-- 발신자 번호 -->
                        <td>
                            <input type="text" class="form-control form-control-sm"  name="ecareVo.senderTel" value="${ecareScenarioVo.ecareVo.senderTel}"  />
                        </td>
                        <c:if test="${ecareScenarioVo.smsIndividualCharge eq 'on'}">
                            <th><em class="required"></em><spring:message code="ecare.menu.reqdept"/></th><!--요청부서  -->
                            <td><input type="text" class="form-control form-control-sm" name="ecareVo.reqDept" value="${ecareScenarioVo.ecareVo.reqDept}" /></td>
                        </c:if>
                        <td colspan="${smsColSpan}"> </td>
                    </tr>
                    </c:if>

                    <!-- 채널 별 정보 문자(이메일)-->
                    <c:if test="${ecareScenarioVo.ecareVo.channelType eq 'M'}">
                    <tr>
                        <th><em class="required"></em><spring:message code="ecare.menu.semail"/></th><!-- 발신자 이메일-->
                        <td>
                            <input type="text" class="form-control form-control-sm"  name="ecareVo.senderEmail" value="${ecareScenarioVo.ecareVo.senderEmail}"  />
                        </td>
                        <th><em class="required"></em><spring:message code="ecare.menu.sname"/></th><!-- 발신자 이름-->
                        <td>
                            <input type="text" class="form-control form-control-sm"  name="ecareVo.senderNm" value="${ecareScenarioVo.ecareVo.senderNm}"  />
                        </td>
                        <th><spring:message code="ecare.menu.je"/></th><!-- 발송시도횟수 -->
                        <td>
                            <input type="text" class="form-control form-control-sm w-25" name="ecareVo.retryCnt" value="${ecareScenarioVo.ecareVo.retryCnt}" maxlength="1"/>
                        </td>
                    </tr>
                    <tr>
                        <th><spring:message code="ecare.menu.susin"/></th><!-- 수신확인 -->
                        <td>
                            <select class="form-control form-control-sm w-75" name="ecareVo.emailOpenType">
                                <option value="BODY" ${ecareScenarioVo.ecareVo.emailOpenType eq 'BODY' ?"selected":""}><spring:message code="email.opentype.body"/></option><!--  본문만 -->
                                <option value="ATTACH" ${ecareScenarioVo.ecareVo.emailOpenType eq 'ATTACH'?"selected":""}><spring:message code="email.opentype.attach"/></option><!--  첨부내용만 -->
                                <option value="NONE" ${ecareScenarioVo.ecareVo.emailOpenType eq 'NONE'?"selected":""}><spring:message code="email.opentype.none"/></option><!-- 사용안함 -->
                            </select>
                        </td>
                        <th><spring:message code="ecare.menu.tracking"/></th><!-- 반응추적기간 -->
                        <td>
                            <select class="form-control form-control-sm w-75" name="ecareVo.termType">
                                <option value="1" ${ecareScenarioVo.ecareVo.termType == 1?"selected":""}><spring:message code="ecare.tracking.1wk"/></option><!--  1주-->
                                <option value="2" ${ecareScenarioVo.ecareVo.termType == 2?"selected":""}><spring:message code="ecare.tracking.2wks"/></option><!--  2주-->
                                <option value="3" ${ecareScenarioVo.ecareVo.termType == 3?"selected":""}><spring:message code="ecare.tracking.3wks"/></option><!--  3주-->
                                <option value="4" ${ecareScenarioVo.ecareVo.termType == 4?"selected":""}><spring:message code="ecare.tracking.4wks"/></option><!--  4주-->
                            </select>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    </c:if>

                    <!-- 채널 별 정보 문자(알림톡)-->
                    <c:if test="${ecareScenarioVo.ecareVo.channelType eq 'A' or ecareScenarioVo.ecareVo.channelType eq 'C'}">
                    <tr>
                        <th><spring:message code="ecare.menu.sms.send.yn"/></th><!-- 실패건 문자발송 -->
                        <td>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="failbackSendYn_Y" name="ecareVo.failbackSendYn" class="custom-control-input" value="Y" ${ecareScenarioVo.ecareVo.failbackSendYn eq 'Y' ? "checked" : ""} >
                                <label class="custom-control-label" for="failbackSendYn_Y"><spring:message code="ecare.menu.item.sms.send.y" /></label>
                            </div>
                            <div class="custom-control custom-radio custom-control-inline">
                                <input type="radio" id="failbackSendYn_N" name="ecareVo.failbackSendYn" class="custom-control-input" value="N" ${ecareScenarioVo.ecareVo.failbackSendYn eq 'N' ? "checked" : ""} >
                                <label class="custom-control-label" for="failbackSendYn_N"><spring:message code="ecare.menu.item.sms.send.n" /></label>
                            </div>
                        </td>
                        <th><em class="required"></em><spring:message code="ecare.menu.sms.subject"/></th><!-- LMS/MMS 제목 -->
                        <td><input type="text" id="failbackSubject" name="ecareVo.failbackSubject" class="form-control form-control-sm"  value="${ecareScenarioVo.ecareVo.failbackSubject}" ></td>
                        <th class="ls--1px"><em class="required"></em><spring:message code="ecare.menu.resendsendertel"/></th><!-- 실패시 발신자번호 -->
                        <td><input type="text" id="senderTel" name="ecareVo.senderTel" class="form-control form-control-sm" value="${ecareScenarioVo.ecareVo.senderTel}"></td>
                    </tr>
                    </c:if>
                </table>
            </div>
            </c:if>
        </div><!-- // card body -->
    </div>
</div>
</form>