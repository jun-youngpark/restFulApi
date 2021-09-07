<%-------------------------------------------------------------------------------------------------
 * - [대상자 관리>대상자 조회] 대상자 리스트 - 수행이력 <br/>
 * - URL : /segment/usingHistoryPopup.do <br/>
 * - Controller : com.mnwise.wiseu.web.segment.web.SegmentListController <br/>
 * - 이전 파일명 : usingPopup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="segment.msg.history"/></title><!-- 수행이력 -->
<%@ include file="/jsp/include/plugin.jsp" %>
</head>
<body>
<div class="card pop-card">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="segment.msg.history"/></h5></div><!-- 수행이력 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-sm dataTable table-fixed">
                <colgroup>
                    <col width="20%" />
                    <col width="15%" />
                    <col width="*" />
                    <col width="20%" />
                </colgroup>
                <thead class="thead-light">
                    <tr>
                        <th><spring:message code="common.menu.type"/></th><!-- 유형 -->
                        <th><spring:message code="common.menu.no"/></th><!-- No -->
                        <th><spring:message code="ecare.table.sname_${webExecMode}"/></th><!-- 서비스명 -->
                        <th><spring:message code="common.menu.status"/></th><!-- 상태 -->
                    </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${!empty usingList}">
                        <c:forEach var="usingSegmentVo" items="${usingList}" varStatus="status">
                            <tr>
                                <td>${usingSegmentVo.type}</td>
                                <td>${usingSegmentVo.serviceNo}</td>
                                <td class="text-left">${usingSegmentVo.serviceNm}</td>
                                <td>${usingSegmentVo.statusNm}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4"><spring:message code="segment.menu.history.nodata"/></td><!-- 수행이력이 없습니다 -->
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div><!-- //Light table -->
    </div>
</div>
</body>
</html>