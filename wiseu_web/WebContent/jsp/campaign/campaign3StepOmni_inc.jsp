<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>3단계] 캠페인 수행 화면 출력 - 옴니채널 이케어 생성, 옴니채널 이케어 목록 출력
 - URL : campaign3Step.do
 - Controller : com.mnwise.wiseu.web.campaign.web.Scenario3StepFormController
 - 이전 파일명 : campaign_3step_omni_inc.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initOmniPage();
    });

    function initOmniPage() {
        var abTestRateVal = ${scenarioVo.campaignVo.abTestRate};
        var max = 100;  //테스트 대상자 비율 조정
        var targetCnt = "${scenarioVo.segmentSize}";

        $("#slider").slider({
            range: "min",
            step : 10,
            value: abTestRateVal,
            min : 0,
            max : max,
            slide: function(event, ui) {
                $("#amount").html(ui.value+"%");
                var re = getRate(targetCnt, ui.value);
                abTargetCnt = re;
                var etc = parseInt(targetCnt) - parseInt(re);
                $("#testCnt").html(ui.value + "%(" + re + "<spring:message code='campaign.divide.person'/>)");  // 명
                $("#etcCnt").html(max-parseInt(ui.value) + "%(" + etc + "<spring:message code='campaign.divide.person'/>)");  // 명

                //유효성 체크
                abTestRate = parseInt(ui.value);
            }
        });

        $("#amount").html($("#slider").slider("value")+"%");

        //A/B테스트 비율 정보가 있으면
        if(abTestRateVal != 0) {
            var reVal = getRate(targetCnt, abTestRateVal);
            var etcVal = parseInt(targetCnt) - parseInt(reVal);

            $("#testCnt").html(abTestRateVal + "%(" + reVal + "<spring:message code='campaign.divide.person'/>)");  // 명
            $("#etcCnt").html(max-parseInt(abTestRateVal) + "%(" + etcVal + "<spring:message code='campaign.divide.person'/>)");  // 명
            abTestRate = abTestRateVal;
            abTargetCnt = reVal;
        } else {
            abTestRate = 0;
        }

        // AB 테스트일 경우, 무조건 '예약발송'으로 설정해서 저장한다.
        var abTestType = "${scenarioVo.abTestType}";
        if(abTestType === 'S' || abTestType === 'T') {
            $("input:radio[name=sendType][value=R]").prop("checked", true);
        }
    }

    function getRate(total, rate) {
        var result = (parseInt(total) * parseInt(rate)) / 100;
        return Math.ceil(result);
    }
</script>
<c:if test="${wiseu:getProperty('omnichannel.message.use', 'off') eq 'on'}">
<div class="card-body"><!--Omni Chennel start-->
    <h1 class="h3 text-primary mb-0"><spring:message code="campaign.omnichannel.message" /></h1><!-- 옴니채널 메시지 -->
    <div class="table-responsive gridWrap">
        <table class="table table-sm dataTable">
            <colgroup>
                <col width="10%" />
                <col width="24%" />
                <col width="10%" />
                <col width="23%" />
                <col width="10%" />
                <col width="23%" />
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><spring:message code="common.relation.s" /></th><!-- 성공 -->
                <td>
                    <c:if test="${empty successSubCampaignList}">
                        <select class="form-control form-control-sm d-inline-block w-auto" id="select_channel_S">
                            <c:forEach var="channel" items="${channelList}">
                                <option value="${channel}"><spring:message code="common.channel.${channel}" /></option>
                            </c:forEach>
                        </select>
                        <button type="button" class="btn btn-sm btn-outline-primary ml-2" onclick="makeSubCampaign('${scenarioVo.scenarioNo}', '${campaignNo}', 'S');">
                            <spring:message code="button.create"/><!-- 생성 -->
                        </button>
                    </c:if>
                    <c:if test="${not empty successSubCampaignList}">
                        <c:forEach var="campaign" items="${successSubCampaignList}" varStatus="status">
                            <a href="/campaign/campaign2Step.do?scenarioNo=${campaign.scenarioNo}&campaignVo.campaignNo=${campaign.campaignNo}&depthNo=${campaign.depthNo}&campaignVo.channelType=${campaign.channelType}"><!-- /campaign/campaign_2step_form.do -->
                                <em class="txt_channel ${campaign.channelType}">${campaign.channelType}</em>${campaign.campaignNm}
                            </a>
                        </c:forEach>
                    </c:if>
                </td>
                <th scope="row"><spring:message code="common.relation.f" /></th><!-- 실패 -->
                <td>
                    <c:if test="${empty failSubCampaignList}">
                        <select class="form-control form-control-sm d-inline-block w-auto" id="select_channel_F">
                            <c:forEach var="channel" items="${channelList}">
                                <option value="${channel}"><spring:message code="common.channel.${channel}" /></option>
                            </c:forEach>
                        </select>
                        <button type="button" class="btn btn-sm btn-outline-primary ml-2" onclick="makeSubCampaign('${scenarioVo.scenarioNo}', '${campaignNo}', 'F');">
                            <spring:message code="button.create"/><!-- 생성 -->
                        </button>
                    </c:if>
                    <c:if test="${not empty failSubCampaignList}">
                        <c:forEach var="campaign" items="${failSubCampaignList}" varStatus="status">
                            <a href="/campaign/campaign2Step.do?scenarioNo=${campaign.scenarioNo}&campaignVo.campaignNo=${campaign.campaignNo}&depthNo=${campaign.depthNo}&campaignVo.channelType=${campaign.channelType}"><!-- /campaign/campaign_2step_form.do -->
                                <em class="txt_channel ${campaign.channelType}">${campaign.channelType}</em>${campaign.campaignNm}
                            </a>
                        </c:forEach>
                    </c:if>
                </td>
                <c:if test="${channelType eq 'M'}">
                <th scope="row"><spring:message code="common.relation.o" /></th><!-- 오픈 -->
                <td>
                    <c:if test="${empty openSubCampaignList}">
                        <select class="form-control form-control-sm d-inline-block w-auto" id="select_channel_O">
                            <c:forEach var="channel" items="${channelList}">
                                <option value="${channel}"><spring:message code="common.channel.${channel}" /></option>
                            </c:forEach>
                        </select>
                        <button type="button" class="btn btn-sm btn-outline-primary ml-2" onclick="makeSubCampaign('${scenarioVo.scenarioNo}', '${campaignNo}', 'O');">
                            <spring:message code="button.create"/><!-- 생성 -->
                        </button>
                    </c:if>
                    <c:if test="${not empty openSubCampaignList}">
                        <c:forEach var="campaign" items="${openSubCampaignList}" varStatus="status">
                            <a href="/campaign/campaign2Step.do?scenarioNo=${campaign.scenarioNo}&campaignVo.campaignNo=${campaign.campaignNo}&depthNo=${campaign.depthNo}&campaignVo.channelType=${campaign.channelType}"><!-- /campaign/campaign_2step_form.do -->
                                <em class="txt_channel ${campaign.channelType}">${campaign.channelType}</em>${campaign.campaignNm}
                            </a>
                        </c:forEach>
                    </c:if>
                </td>
                </c:if>
            </tr>
            </tbody>
        </table>
    </div><!-- //Light table -->
</div><!-- //card body -->
</c:if><!--Omni Chennel End-->