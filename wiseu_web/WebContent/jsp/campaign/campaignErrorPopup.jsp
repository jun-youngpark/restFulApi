<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 리스트] 캠페인 리스트 - 에러 로그(팝업) <br/>
 *   캠페인 리스트에서 수행상태가 발송에러를 클릭시 호출
 *
 * - URL : /campaign/campaignErrorPopup.do <br/>
 * - Controller : com.mnwise.wiseu.web.campaign.web.CampaignErrorPopupController
 * - 이전 파일명 : campaign_error_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="campaign.title.error.log" /></title><!-- 에러 로그 -->
<%@ include file="/jsp/include/plugin.jsp"%>
</head>

<body>
<div class="card pop-card">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="campaign.title.error.log" /></h5></div><!-- 에러 로그 -->
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
                    <col width="80%" />
                </colgroup>
                <tbody>
                <tr>
                    <th scope="row"><strong><spring:message code="campaign.menu.cno" /></th><!-- 캠페인 번호 -->
                    <td>${campaignNo}</td>
                </tr>
                <tr>
                    <th scope="row"><spring:message code="campaign.menu.cname" /></th><!-- 캠페인명 -->
                    <td>${scenarioNm}</td>
                </tr>
                <tr>
                    <th colspan="2" class="text-center"><spring:message code="campaign.menu.error.message" /></th><!-- 메시지 -->
                </tr>
                <tr>
                    <td colspan="2">
                    <textarea rows="3" cols="10" class="form-control">${errMsg}</textarea>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
