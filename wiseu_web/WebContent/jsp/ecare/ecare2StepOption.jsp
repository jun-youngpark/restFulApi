<%-------------------------------------------------------------------------------------------------
 - [이케어>이케어 등록>2단계] 이케어 옵션 화면 출력 - 옴니채널 이케어 생성, 옴니채널 이케어 목록 출력
 - URL : /ecare/ecare2Step.do
 - Controller : com.mnwise.wiseu.web.ecare.web.EcareScenario2StepFormController
 - 이전 파일명 : ecare_3step_omni_inc.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script src='/js/common/ecare.js'></script>
<script type="text/javascript">

$(document).ready(function() {
    initOptionPage();
});

function initOptionPage() {
	getOmniChannelList();
}
function getOmniChannelList(){
	  var param = {
              scenarioNo: scenarioNo,
              ecareNo: ecareNo ,
              depthNo: depthNo,
              channelType : channelType,
              subType: subType
      }
      $.post("/include/ecareOmniChannel.do", $.param(param, true), function(result) {
          $("#omniWrap").html(result);
          var writeAuth = '${sessionScope.write}';
          if(writeAuth != 'W'){   //쓰기 권한이 없을 경우
        	  hideButtonByAuth(["makeSuccessEcareBtn","makeFailEcareBtn","makeOpenEcareBtn"]) //common.js
          }
      });
}
function goView(scenarioNo,ecareNo,depthNo,channelType) {
        window.location.href="/ecare/ecare2Step.do?scenarioNo="+scenarioNo+"&ecareVo.ecareNo="+ecareNo+"&depthNo="+depthNo+"&ecareVo.channelType="+channelType;
    }
</script>
    <div class="card">
         <div class="card-header bg-default toogleEvt" id="headingFour"  data-toggle="collapse"
              data-target="#collapseFour" aria-expanded='true'>
              <h5 class="text-secondary pt-3"><spring:message code="ecare.2step.title.option"/><!-- 고급설정 --></h5>
          </div>
          <div id="collapseFour" class="collapse show" aria-labelledby="headingFour">
                   <!-- card body -->
               <div class="card-body px-3 ">
                    <div class="mt-2 justify-content-end">
                        <!-- <button class="btn btn-sm btn-outline-primary btn-round mr-2">
                            <i class="fas fa-check"></i> 적용
                        </button> -->
                    </div>
                    <c:if test="${wiseu:getProperty('omnichannel.message.use', 'off') eq 'on'}">
                    <!-- 옴니채널 메시지 워크플로우  -->
                    <h1 class="h3 text-primary my-0"><spring:message code="campaign.omnichannel.message.workflow" /></h1>
                       <!-- Light table -->
                       <div class="table-responsive mb-2">
                        <table class="table table-sm dataTable">
                            <thead class="thead-light">
                                <tr>
                                    <th scope="col"><spring:message code="ecare.channel.num" /></th><!-- 번호 -->
                                    <th scope="col"><spring:message code="ecare.table.sname_1" /></th><!--  서비스명 -->
                                    <th scope="col"><spring:message code="ecare.channel" /></th><!-- 채널 -->
                                    <th scope="col"><spring:message code="ecare.table.status"/></th><!-- 수행상태 -->
                                    <th scope="col"><spring:message code="ecare.table.ctarget"/></th><!-- 대상자수 -->
                                </tr>
                            </thead>
                            <tbody>
                             <c:forEach var="loop" items="${scenarioList}" varStatus="i">
                                <tr>
                                    <td>${loop.ecareVo.ecareNo}</td><!-- 번호 -->
                                    <td class="text-left"><!--  서비스명 -->
                                        <c:if test="${loop.ecareVo.depthNo > 1}">
                                            <c:forEach var="depthNo" begin="1" end="${loop.ecareVo.depthNo}" step="1">&nbsp;</c:forEach><!-- Depth별 들여쓰기 -->
                                            <img src="/images/common/relation/type_${loop.ecareVo.relationType}.png" style="vertical-align: middle;"/>
                                        </c:if>
                                       <a href="javascript:goView('${loop.scenarioNo}','${loop.ecareVo.ecareNo}','${loop.ecareVo.depthNo}','${loop.ecareVo.channelType}')" style="display: inline-block;">${loop.scenarioNm}</a>
                                    </td>
                                    <td><!-- 채널 -->
                                        <em class="txt_channel ${loop.ecareVo.channelType}">
                                        <c:choose>
                                            <c:when test="${loop.ecareVo.channelType eq 'M'}">E</c:when>
                                            <c:when test="${loop.ecareVo.channelType eq 'T'}">M</c:when>
                                            <c:otherwise>${loop.ecareVo.channelType}</c:otherwise>
                                        </c:choose>
                                        </em>
                                    </td>
                                    <td>${loop.ecareVo.ecareStsNm}</td><!-- 수행상태 -->
                                    <td><!-- 대상자수 -->
                                       <c:if test="${loop.ecareVo.depthNo eq 1}">
                                            <fmt:formatNumber type="number" value="${loop.ecareVo.targetCnt}" /><c:if test="${not empty loop.ecareVo.targetCnt}"> <spring:message code="campaign.menu.persons"/></c:if>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <!-- //옴니채널 메시지 워크플로우   -->
                    <!-- 옴니채널 메시지 -->
                    <h1 class="h3 text-primary my-0"><spring:message code="campaign.omnichannel.message" /></h1>
                    <div class="col-12 alert alert-secondary mb-0" role="alert">
                        <c:if test="${(serviceType eq 'schedule' or serviceType eq 'scheduleM') and notOmniEcare}">
                            <i class="fas fa-circle fa-xs"></i> <spring:message code="ecare.2step.help6" /><!-- DB에 저장된 필수키 설정에 따라 선택 가능한 채널이 바뀝니다. 필수키가 없으면 선택이 안됩니다.-->
                        </c:if>
                         <c:if test="${serviceType eq 'nrealtime' or (not notOmniEcare)}">
                            <i class="fas fa-circle fa-xs"></i> <spring:message code="ecare.2step.help7" /><!-- 준실시간의 경우 현재 이케어의 채널만 생성이 가능합니다. -->
                        </c:if>
                    </div>
                    <div class="table-responsive gridWrap mb-3" id="omniWrap">
                    </div>
                    <!--// 옴니채널 메시지 -->
                </c:if>
            </div>
        </div>
    </div>