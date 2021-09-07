<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>2단계] 금칙어 검사 (팝업)
 - [이케어>이케어 등록>2단계] 금칙어 검사 (팝업)
 - URL : /common/badWordCheckPopup.do
 - Controller : com.mnwise.wiseu.web.common.web.BadWordController
 * - 이전 파일명 : badword_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="campaign.alt.action.badword"/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
</head>
<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="campaign.alt.action.badword"/></h5></div><!-- 금칙어 검사 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
       <div class="table-responsive gridWrap">
           <table class="table table-sm dataTable table-fixed">
                <colgroup>
                    <col width="20%" />
                    <col width="*" />
                </colgroup>
                <tbody>
                <tr>
                    <th scope="row"><spring:message code="env.msg.ban"/></th><!-- 금칙어 -->
                    <td>
                        ${checkedBadWord}
                        <c:if test="${empty checkedBadWord}">
                            <spring:message code="common.alert.save.msg3" /><!-- 금칙어가 없습니다. -->
                        </c:if>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>