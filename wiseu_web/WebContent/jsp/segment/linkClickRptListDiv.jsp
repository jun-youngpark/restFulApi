<%-------------------------------------------------------------------------------------------------
 * [캠페인>캠페인 등록>메시지 작성>대상자 등록(팝업)>반응대상자 탭] 반응대상자 목록 하단의 URL/이미지 목록 출력
 * - URL : /target/linkClickRptListDiv.do, /segment/linkClickRptListDiv.do
 * - Controller : com.mnwise.wiseu.web.segment.linkclick.web.LinkclickController <br/>
 * - 이전 파일명 : linkclick_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<form id="linkClickFrm" name="linkClickFrm" method="post" taget="_self">
<input type="hidden" name="serviceType" value="EM"/>
<input type="hidden" name="serviceNo" value="${serviceNo}"/>
<input type="hidden" name="segmentNo" value="${segmentNo}"/>
<input type="hidden" name="segmentNm" value="${segmentNm}"/>
<input type="hidden" name="segmentSize" value="${segmentSize}"/>
<input type="hidden" name="checkedLinkSeqCnt" value="${checkedLinkSeqCnt}"/>
<input type="hidden" name="cmd"/>

<table class="table table-hover table-sm dataTable table-fixed">
    <thead class="thead-light">
        <tr>
            <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 선택 -->
            <th scope="col" width="*"><spring:message code="segment.menu.link.url"/></th><!-- URL/이미지 -->
            <th scope="col" width="90"><spring:message code="segment.menu.link.rcnt"/></th><!-- 반응 건수 -->
        </tr>
    </thead>
    <tbody>
    <c:if test="${!empty linkInfoList}">
        <c:forEach var="linkInfoVo" items="${linkInfoList}" varStatus="i">
        <tr>
             <th scope="row">
                <c:set target="${linkclickVo}" property="checkLinkSeq" value="${linkInfoVo.linkSeq}"/>
                <input type="checkbox" name="linkSeqArray" value="${linkInfoVo.linkSeq}" <c:if test="${linkclickVo.checkByLinkSeq}"> checked </c:if>>
                <!-- <label class="custom-control-label" for="checkbox1"><span class="hide">선택</span></label> -->
             </th>
            <td class="text-left">${linkInfoVo.linkTitle}</td>
            <td>${linkInfoVo.linkCnt}</td>
        </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty linkInfoList}">
        <tr>
            <td colspan=3><spring:message code="segment.menu.link.nodata"/></td><!-- 링크 목록이 없습니다. -->
        </tr>
    </c:if>
    </tbody>
</table>
</form>
