<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>1단계] 캠페인 기본정보 등록 화면 출력
 - URL : /campaign/campaign1Step.do
 - Controller : com.mnwise.wiseu.web.campaign.web.Scenario1StepFormController
 - 이전 파일명 : campaign_1step_form.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.basic"/>) </title><!-- 캠페인 등록 (기본정보) -->
<script type="text/javascript">
    var rules = {
        scenarioNm : {notBlank : true, maxbyte : 100},
        channels   : {required : true},
        abTestType : {
            required : function() {
                return getChannel() == 'M';
            }
        },
        reqDept : {
            notBlank : function() {
                return $.mdf.equalsAny(getChannel(), ['S', 'T']);
            }
        },
        reqUser : {
            notBlank : function() {
                return $.mdf.equalsAny(getChannel(), ['S', 'T']);
            }
        },
        handlerType : {required : true},
        "tagNm" : {maxbyte : 50}
    };

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 채널 선택
        $("input:radio[name=channels]").on("change", function(event) {
            if('${campaignAbTest}' =='on'){
                if(this.value == 'M') {
                    $('#abTestTr').show();
                } else {
                    $('#abTestTr').hide();
                    $("input:radio[name=abTestType][value=N]").prop("checked", true);  // A/B 테스트 발송 선택 - 미사용(N)
                }
            }
            var smsIndividualCharge = '${scenarioVo.smsIndividualCharge}';
            // SMS/MMS/LMS 부서별 개별 과금 부서코드 입력창 on
            if(smsIndividualCharge == 'on' && $.mdf.equalsAny(this.value, ['S', 'T'])) {
                $("#reqDeptUserTr").show();
            } else {
                $("#reqDeptUserTr").hide();
            }
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            //승인이 필요한 경우에는 저장을 막는다.
            if('${scenarioVo.needAgree}' == 'true') {
                alert("<spring:message code='campaign.alert.msg.exec.17'/>");  // 승인 처리 중인 캠페인은 수정 할 수 없습니다.
                return false;
            }

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            $("#updateForm").submit();
        });

        // 다음단계 버튼 클릭
        $("#nextStepBtn").on("click", function(event) {
            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var scenarioNo = $("#updateForm input[name=scenarioNo]").val();
            if(scenarioNo == 0) {
                $("#updateForm input[name=nextStep]").val("nextStep");
                $("#updateForm").submit();
            } else {
                // /campaign/campaign_2step_form.do
                location.href="/campaign/campaign2Step.do?scenarioNo=" + scenarioNo + "&campaignVo.campaignNo=${scenarioVo.campaignVo.campaignNo}&depthNo=${depthNo}&campaignVo.channelType=${channelType}";
            }
        });

        // 단축키 Ctrl+g
        $(document).bind('keydown', 'Ctrl+g', function(event) {
            $('#handlerTypeTr').toggle();
        });
    }

    function initPage() {
        var smsIndividualCharge = '${scenarioVo.smsIndividualCharge}';
        var channelType = $("input:radio[name=channels]:checked").val();
        if("${scenarioVo.serviceTypeNoChange}" == 'true') {
            $("input[name=channels]").attr('disabled', true);
            $("input[name=handlerType]").attr('disabled', true);
            $("input[name=abTestType]").attr('disabled', true);
            if(smsIndividualCharge == 'on' && $.mdf.equalsAny(channelType, ['S', 'T'])) {
                $("#reqDeptUserTr").show();
            }
        }

        var maxLevel = ${scenarioVo.maxCampaignLevel};
        if(maxLevel > 1) {
            $('#step2Btn').css('cursor','pointer');
            $('#step2Btn').on("click", function(event) {
                // /campaign/campaign_2step_form.do
                window.location = "/campaign/campaign2Step.do?scenarioNo=${scenarioVo.scenarioNo}&campaignVo.campaignNo=${campaignNo}&depthNo=${depthNo}&campaignVo.channelType=${channelType}";
            });

            if(maxLevel > 2) {
                $('#step3Btn').css('cursor','pointer');
                $('#step3Btn').on("click", function(event) {
                    // /campaign/campaign_3step_form.do
                    window.location = "/campaign/campaign3Step.do?scenarioNo=${scenarioVo.scenarioNo}&campaignVo.campaignNo=${campaignNo}&depthNo=${depthNo}&channelType=${channelType}";
                });
            }
        }

        if(channelType != 'M') {
            $('#abTestTr').hide();
        }
    }

    // 사용자가 선택한 채널 값을 가져온다.
    function getChannel() {
        if("${scenarioVo.channelUseSize}" === "1") {
            return $("input[name=channels]").val();
        } else {
            return $("input:radio[name=channels]:checked").val();
        }
    }

    function goView(scenarioNo,campaignNo,depthNo,channelType) {
        // /campaign/campaign_2step_form.do
        window.location.href="/campaign/campaign2Step.do?scenarioNo="+scenarioNo+"&campaignVo.campaignNo="+campaignNo+"&depthNo="+depthNo+"&campaignVo.channelType="+channelType;
    }
</script>
</head>

<body>
<!-- /campaign/campaign_step_form.do -->
<form:form id="updateForm" name="updateForm" commandName="scenarioVo" action="/campaign/campaign1Step.do?campaignNo=${scenarioVo.campaignVo.campaignNo}&depthNo=${depthNo}&channelType=${channelType}" method="post">
<form:hidden path="scenarioNo"/>
<form:hidden path="searchColumn"/>
<form:hidden path="searchWord"/>
<form:hidden path="orderColumn"/>
<form:hidden path="orderSort"/>
<form:hidden path="countPerPage"/>
<form:hidden path="finishYn"/>
<form:hidden path="campaignVo.campaignNm"/>
<form:hidden path="campaignVo.campaignLevel"/>
<form:hidden path="tagNo"/>
<form:hidden path="userId"/>
<form:hidden path="grpCd"/>
<input type="hidden" name="page" value="${nowPage}">
<input type="hidden" name="stepLevel" value="1"/>
<input type="hidden" name="nextStep" />
<form:hidden path="siteNm"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="campaign.msg.create"/> (<spring:message code="campaign.msg.basic"/>)</h3><!-- 캠페인 등록 (기본정보) -->
            </div>

            <div class="card-body gridWrap">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-12 row pr-0 justify-content-end"><!-- step -->
                        <ul class="stepWrap">
                            <li class="current"><div class="box"><span class="txt">STEP</span><strong>1</strong></div></li>
                            <li id="step2Btn"><div class="box"><span class="txt">STEP</span><strong>2</strong></div></li>
                            <li id="step3Btn"><div class="box"><span class="txt">STEP</span><strong>3</strong></div></li>
                        </ul>
                    </div>
                </div>

                <spring:hasBindErrors name="scenarioVo"><!-- 경고창 -->
                <div class="col-12 alert alert-warning" role="alert">
                    <c:forEach var="error" items="${errors.allErrors}">
                        <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                        <span class="alert-text"><spring:message message="${error}" /><br></span>
                    </c:forEach>
                </div>
                </spring:hasBindErrors>

                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="13%" />
                            <col width="37%" />
                            <col width="13%" />
                            <col width="37%" />
                        </colgroup>
                        <tbody>

                        <c:if test="${scenarioVo.scenarioNo > 0}">
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 -->
                            <td colspan="3" class="control-text-sm">${scenarioVo.campaignVo.campaignNo}</td>
                        </tr>
                        </c:if>

                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.cname"/></th><!-- 캠페인명 -->
                            <td colspan="3"><form:input path="scenarioNm" cssClass="form-control form-control-sm" readonly="${scenarioVo.noChangeToString}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.channel"/></th><!-- 채널 -->
                            <td colspan="3" class="text-left">
                                <input type="hidden" name="initChannels" value="<c:forEach items="${scenarioVo.campaignList}" var="s">${s.channelType},</c:forEach>"/>
                                <div class="form-inline">
                                    <c:if test="${fn:contains(scenarioVo.channelUseList, 'M')}">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="channel_M" name="channels" value="M" class="custom-control-input" <c:if test="${scenarioVo.campaignVo.channelType eq 'M'}">checked</c:if>/>
                                        <label class="custom-control-label" for="channel_M"><spring:message code="common.channel.M"/></label>
                                    </div>
                                    </c:if>

                                    <c:if test="${fn:contains(scenarioVo.channelUseList, 'S')}">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="channel_S" name="channels" value="S" class="custom-control-input" <c:if test="${scenarioVo.campaignVo.channelType eq 'S'}">checked</c:if>/>
                                        <label class="custom-control-label" for="channel_S"><spring:message code="common.channel.S"/></label>
                                    </div>
                                    </c:if>

                                    <c:if test="${fn:contains(scenarioVo.channelUseList, 'T')}">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="channel_T" name="channels" value="T" class="custom-control-input" <c:if test="${scenarioVo.campaignVo.channelType eq 'T'}">checked</c:if>/>
                                        <label class="custom-control-label" for="channel_T"><spring:message code="common.channel.T"/></label>
                                    </div>
                                    </c:if>

                                    <c:if test="${fn:contains(scenarioVo.channelUseList, 'F')}">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="channel_F" name="channels" value="F" class="custom-control-input" <c:if test="${scenarioVo.campaignVo.channelType eq 'F'}">checked</c:if>/>
                                        <label class="custom-control-label" for="channel_F"><spring:message code="common.channel.F"/></label>
                                    </div>
                                    </c:if>
                                    <c:if test="${fn:contains(scenarioVo.channelUseList, 'B')}">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="channel_B" name="channels" value="B" class="custom-control-input" <c:if test="${scenarioVo.campaignVo.channelType eq 'B'}">checked</c:if>/>
                                        <label class="custom-control-label" for="channel_B"><spring:message code="common.channel.B"/></label>
                                    </div>
                                    </c:if>

                                    <c:if test="${fn:contains(scenarioVo.channelUseList, 'C')}">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="channel_C" name="channels" value="C" class="custom-control-input" <c:if test="${scenarioVo.campaignVo.channelType eq 'C'}">checked</c:if>/>
                                        <label class="custom-control-label" for="channel_C"><spring:message code="common.channel.C"/></label>
                                    </div>
                                    </c:if>

                                    <c:if test="${fn:contains(scenarioVo.channelUseList, 'P')}">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="channel_P" name="channels" value="P" class="custom-control-input" <c:if test="${scenarioVo.campaignVo.channelType eq 'P'}">checked</c:if>/>
                                        <label class="custom-control-label" for="channel_P"><spring:message code="common.channel.P"/></label>
                                    </div>
                                    </c:if>
                                </div>
                            </td>
                        </tr>

                        <c:if test="${campaignAbTest eq 'on'}">
                        <tr id="abTestTr">
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.divide.test"/></th><!-- A/B 테스트 발송 -->
                            <td colspan="3" class="text-left">
                                <div class="form-inline">
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="abTestType_N" name="abTestType" class="custom-control-input" value="N"<c:if test="${scenarioVo.abTestType eq 'N'}"> checked="checked"</c:if>/>
                                        <label class="custom-control-label" for="abTestType_N"><spring:message code="campaign.divide.none"/></label><!-- 미사용 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="abTestType_S" name="abTestType" class="custom-control-input" value="S"<c:if test="${scenarioVo.abTestType eq 'S'}"> checked="checked"</c:if>/>
                                        <label class="custom-control-label" for="abTestType_S"><spring:message code="campaign.divide.sub"/></label><!-- 제목 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                        <input type="radio" id="abTestType_T" name="abTestType" class="custom-control-input" value="T"<c:if test="${scenarioVo.abTestType eq 'T'}"> checked="checked"</c:if>/>
                                        <label class="custom-control-label" for="abTestType_T"><spring:message code="campaign.divide.temp"/></label><!-- 템플릿 -->
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${scenarioVo.smsIndividualCharge eq 'on' }">
                        <tr class="dp-none" id="reqDeptUserTr">
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.reqdept"/></th>
                            <td><form:input path="reqDept" cssClass="form-control form-control-sm" readonly="${scenarioVo.noChangeToString}"/></td>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.requser"/></th>
                            <td><form:input path="reqUser" cssClass="form-control form-control-sm" readonly="${scenarioVo.noChangeToString}"/></td>
                        </tr>
                        </c:if>

                        <tr class="dp-none" id="handlerTypeTr">
                            <th scope="row"><spring:message code="campaign.menu.htype"/></th><!--핸들러 타입-->
                            <td colspan="3">
                                <c:choose>
                                    <c:when test="${scenarioVo.handlerType eq null}">
                                        <div class="form-inline">
                                            <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                                <input type="radio" id="handlerType_G" name="handlerType" class="custom-control-input" value="G" checked="checked"/>
                                                <label class="custom-control-label" for="handlerType_G">groovy</label>
                                            </div>
                                            <div class="custom-control custom-radio custom-control-inline form-control-sm">
                                                <input type="radio" id="handlerType_S" name="handlerType" class="custom-control-input" value="S"/>
                                                <label class="custom-control-label" for="handlerType_S">script</label>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${scenarioVo.handlerType eq 'G'}">Groovy</c:if>
                                        <c:if test="${scenarioVo.handlerType eq 'S'}">Script</c:if>
                                        <form:hidden path="handlerType"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.tag"/></th><!-- 태그 -->
                            <td colspan="3">
                                <form:input path="tagNm" id="id_searchWord" cssClass="form-control form-control-sm w-50" readonly="${scenarioVo.noChangeToString}"/>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cdes"/></th><!-- 캠페인 설명 -->
                            <td colspan="3"><form:input path="scenarioDesc" cssClass="form-control form-control-sm" readonly="${scenarioVo.noChangeToString}"/></td>
                        </tr>

                        <!-- 담당부서, 담당자, 작성일, 최종수정일 -->
                        <c:choose>
                        <c:when test="${scenarioVo.scenarioNo > 0}">
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.group"/></th><!-- 담당부서 -->
                            <td class="control-text-sm">${scenarioVo.grpNm}</td>
                            <th scope="row"><spring:message code="campaign.menu.user"/></th><!-- 담당자 -->
                            <td class="control-text-sm">${scenarioVo.userVo.nameKor}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.cdate"/></th><!-- 작성일 -->
                            <td class="control-text-sm">${scenarioVo.createDtToDateStr}</td>
                            <th scope="row"><spring:message code="campaign.menu.udate"/></th><!-- 최종수정일 -->
                            <td class="control-text-sm">${scenarioVo.lastUpdateDtToDateStr}</td>
                        </tr>
                        </c:when>
                        <c:otherwise>
                        <tr>
                            <th scope="row"><spring:message code="campaign.menu.group"/></th><!-- 담당부서 -->
                            <td class="control-text-sm">${sessionScope.adminSessionVo.userVo.grpNm}</td>
                            <th scope="row"><spring:message code="campaign.menu.user"/></th><!-- 담당자 -->
                            <td class="control-text-sm">${sessionScope.adminSessionVo.userVo.nameKor}</td>
                        </tr>
                        </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <c:if test="${wiseu:getProperty('omnichannel.message.use', 'off') eq 'on' and fn:length(scenarioList) > 1}">
            <div class="card-body mb-2">
                <h1 class="h3 text-primary mt-3 mb-2"><spring:message code="campaign.omnichannel.message.workflow" /></h1><!-- 옴니채널 메시지 워크플로우 -->
                <div class="table-responsive col-12 p-0">
                    <table class="table table-hover table-sm dataTable table-fixed">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="6%"><spring:message code="ecare.channel.num" /></th><!-- 번호 -->
                            <th scope="col"><spring:message code="campaign.table.cname" /></th><!--  캠페인명 -->
                            <th scope="col" width="5%"><spring:message code="campaign.menu.channel" /></th><!-- 채널 -->
                            <th scope="col" width="7%"><spring:message code="campaign.table.status"/></th><!-- 수행상태 -->
                            <th scope="col" width="10%"><spring:message code="campaign.table.ctarget"/></th><!-- 대상자수 -->
                            <th scope="col" width="100"><spring:message code="campaign.table.sdate"/></th><!-- 발송일시 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="loop" items="${scenarioList}" varStatus="i">
                        <tr style="cursor: pointer;" onclick="javascript:goView('${loop.scenarioNo}','${loop.campaignVo.campaignNo}','${loop.campaignVo.depthNo}','${loop.campaignVo.channelType}')">
                            <td>${loop.campaignVo.campaignNo}</td><!-- 번호 -->
                            <td class="text-left"><!--  캠페인명 -->
                                <c:if test="${loop.campaignVo.depthNo > 1}">
                                    <c:forEach var="depthNo" begin="1" end="${loop.campaignVo.depthNo}" step="1">&nbsp;</c:forEach><!-- Depth별 들여쓰기 -->
                                    <img src="/images/common/relation/type_${loop.campaignVo.relationType}.png" style="vertical-align: middle;"/>
                                </c:if>
                                ${loop.scenarioNm}
                            </td>
                            <td><!-- 채널 -->
                                <em class="txt_channel ${loop.campaignVo.channelType}">
                                <c:choose>
                                    <c:when test="${loop.campaignVo.channelType eq 'M'}">E</c:when>
                                    <c:when test="${loop.campaignVo.channelType eq 'T'}">M</c:when>
                                    <c:otherwise>${loop.campaignVo.channelType}</c:otherwise>
                                </c:choose>
                                </em>
                            </td>
                            <td>${loop.campaignVo.campaignStsNm}</td><!-- 수행상태 -->
                            <td class="text-right"><!-- 대상자수 -->
                                <c:if test="${loop.campaignVo.depthNo eq 1}">
                                    <fmt:formatNumber type="number" value="${loop.campaignVo.targetCnt}" />
                                    <c:if test="${not empty loop.campaignVo.targetCnt}"> <spring:message code="campaign.menu.persons"/></c:if>
                                </c:if>
                            </td>
                            <td><!-- 발송일시 -->
                               <fmt:parseDate value="${loop.campaignVo.sendstartDtm}" pattern="yyyyMMddHHmmss" var="sendDtm"/>
                               <fmt:formatDate value="${sendDtm}" pattern="yy-MM-dd HH:mm" />
                            </td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            </c:if>

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <c:if test="${sessionScope.write eq 'W'}">
                    <div class="col offset-sm-4 col-4 text-center">
                        <button type="button" class="btn btn-outline-primary" id="saveBtn">
                            <spring:message code="button.save"/><!-- 저장 -->
                        </button>
                    </div>
                    </c:if>

                    <c:if test="${sessionScope.write eq 'W' or scenarioVo.scenarioNo > 0}">
                    <div class="col col-4">
                        <button type="button" class="btn btn-outline-info" id="nextStepBtn">
                            <spring:message code="button.nextstep"/> <i class="fas fa-chevron-right"></i><!-- 다음단계 -->
                        </button>
                    </div>
                    </c:if>
                </div>
            </div><!-- e.button area -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form:form>

<form id="pageFrm" name="pageFrm" method="post">
    <input type="hidden" name="campaignNo">
    <input type="hidden" name="delIdxs" value="${scenarioVo.campaignVo.campaignNo}">
    <input type="hidden" name="mode">
    <input type="hidden" name="searchColumn" value="${searchColumn}">
    <input type="hidden" name="searchWord" value="${searchWord}">
    <input type="hidden" name="orderColumn" value="${orderColumn}">
    <input type="hidden" name="orderSort" value="${orderSort}">
    <input type="hidden" name="countPerPage" value="${countPerPage}">
    <input type="hidden" name="page" value="${nowPage}">
</form>
</body>
</html>
