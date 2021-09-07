<%-------------------------------------------------------------------------------------------------
 - [이케어>이케어 등록>2단계] 옴니채널 이케어 가능 채널
 - URL : /ecare/ecare2Step.do
 - Controller : com.mnwise.wiseu.web.ecare.web.EcareScenarioFormAjaxController
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<table class="table table-sm dataTable">
	<colgroup>
    <col style="width:15%;" />
    <col style="width:30%;" />
	<col style="width:15%;" />
	<col style="width:40%;" />
	</colgroup>
	<tbody>
		<tr>
		    <th scope="row"><spring:message code="common.relation.s" /></th>
		    <td>
		        <c:if test="${empty successSubEcareList}">
		            <select class="form-control form-control-sm d-inline-block w-auto" id="select_channel_S">
		                <c:forEach var="channel" items="${channelList}">
		                    <option value="${channel}"><spring:message code="common.channel.${channel}" /></option>
		                </c:forEach>
		            </select>
		            <button type="button" id="makeSuccessEcareBtn" class="btn btn-sm btn-outline-primary ml-2" onclick="makeSubEcare('${param.scenarioNo}', '${param.ecareNo}', 'S');">
		                <spring:message code="button.create"/><!-- 생성 -->
		            </button>
		        </c:if>
		        <c:if test="${not empty successSubEcareList}">
		            <c:forEach var="ecare" items="${successSubEcareList}" varStatus="status">
		                <a href="/ecare/ecare2Step.do?scenarioNo=${ecare.scenarioNo}&ecareVo.ecareNo=${ecare.ecareNo}&depthNo=${ecare.depthNo}&ecareVo.channelType=${ecare.channelType}"><!-- /ecare/ecare_2step_form.do -->
		                    <em class="txt_channel ${ecare.channelType}">${ecare.channelType}</em>${ecare.ecareNm}
		                </a>
		            </c:forEach>
		        </c:if>
		    </td>
		    <th scope="row"><spring:message code="common.relation.f" /></th>
		    <td>
		     <c:if test="${empty failSubEcareList}">
		        <select class="form-control form-control-sm d-inline-block w-auto" id="select_channel_F">
		            <c:forEach var="channel" items="${channelList}">
		                <option value="${channel}"><spring:message code="common.channel.${channel}" /></option>
		            </c:forEach>
		        </select>
		        <button type="button" id="makeFailEcareBtn" class="btn btn-sm btn-outline-primary ml-2" onclick="makeSubEcare('${param.scenarioNo}', '${param.ecareNo}', 'F');">
		            <spring:message code="button.create"/><!-- 생성 -->
		        </button>
		    </c:if>
		    <c:if test="${not empty failSubEcareList}">
		        <c:forEach var="ecare" items="${failSubEcareList}" varStatus="status">
		            <a href="/ecare/ecare2Step.do?scenarioNo=${ecare.scenarioNo}&ecareVo.ecareNo=${ecare.ecareNo}&depthNo=${ecare.depthNo}&ecareVo.channelType=${ecare.channelType}"><!-- /ecare/ecare_2step_form.do -->
		                <em class="txt_channel ${ecare.channelType}">${ecare.channelType}</em>${ecare.ecareNm}
		            </a>
		        </c:forEach>
		    </c:if>
		    </td>
		    <c:if test="${channelType eq 'M'}">
		    <th scope="row"><spring:message code="common.relation.o" /></th><!-- 오픈 -->
		    <td>
		        <c:if test="${empty openSubEcareList}">
		            <select class="form-control form-control-sm d-inline-block w-auto" id="select_channel_O">
		                <c:forEach var="channel" items="${channelList}">
		                    <option value="${channel}"><spring:message code="common.channel.${channel}" /></option>
		                </c:forEach>
		            </select>
		            <button type="button" id="makeOpenEcareBtn" class="btn btn-sm btn-outline-primary ml-2" onclick="makeSubEcare('${param.scenarioNo}', '${param.ecareNo}', 'O');">
		                <spring:message code="button.create"/><!-- 생성 -->
		            </button>
		        </c:if>
		        <c:if test="${not empty openSubEcareList}">
		            <c:forEach var="ecare" items="${openSubEcareList}" varStatus="status">
		                <a href="/ecare/ecare2Step.do?scenarioNo=${ecare.scenarioNo}&ecareVo.ecareNo=${ecare.ecareNo}&depthNo=${ecare.depthNo}&ecareVo.channelType=${ecare.channelType}"><!-- /ecare/ecare_2step_form.do -->
		                    <em class="txt_channel ${ecare.channelType}">${ecare.channelType}</em>${ecare.ecareNm}
		                </a>
		            </c:forEach>
		        </c:if>
		    </td>
		    </c:if>
		</tr>
  </tbody>
</table>