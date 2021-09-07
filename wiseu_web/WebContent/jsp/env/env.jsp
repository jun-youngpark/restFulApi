<%-------------------------------------------------------------------------------------------------
  * - [환경설정>서버환경정보] 서버환경정보 <br/>
  * - URL : /env/env.do <br/>
  * - Controller :com.mnwise.wiseu.web.env.web.EnvServerInfoController  <br/>
  * - 이전 파일명 : env_serverinfo.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title><spring:message code="env.msg.serverinfo"/></title><!-- 서버환경 정보 -->
<script>
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                retryCnt                  : {notBlank : true, digits : true, range : [0, 10]},
                resendIncludeReturnmailYn : {notBlank : true},
                spoolPreservePeriod       : {notBlank : true, digits : true, range : [0, 100]},
                logPreservePeriod         : {notBlank : true, digits : true, range : [0, 100]}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            if(!confirm('<spring:message code="common.alert.3"/>')) {  // 저장 하시겠습니까?
                return;
            }

            var param = $.mdf.serializeObject('#updateForm');
            $.post("/env/updateServerInfo.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    alert("<spring:message code='common.alert.1'/>");  // 저장 되었습니다.
                    document.location.reload("/env/env.do");
                } else {
                    alert("<spring:message code='env.alert.svc.err3'/>");  // 저장중에 오류가 발생 하였습니다.
                }
            });
        });
    }
</script>
</head>
<body>
<form id="updateForm" name="updateForm" action="/env/env.do" method="post">
<input type="hidden" name="resendIncludeMailKeyYn" value="N"/>
<input type="hidden" name="resultFileDownloadYn" value="N"/>
<input type="hidden" name="sucsResultFileDownloadYn" value="N"/>
<input type="hidden" name="durationTime" value="10"/>
<input type="hidden" name="b4SendVerifyYn" value="N"/>
<input type="hidden" name="b4SendApproveYn" value="N"/>
<input type="hidden" name="b4RealSendTestSendYn" value="N"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-3">
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="env.msg.serverinfo"/></h3><!-- 서버환경 정보 -->
            </div>

            <div class="card-body gridWrap">
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="235" />
                            <col width="*" />
                            <col width="265" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row" class="min-w250"><spring:message code="env.menu.serverinfo.1"/></th><!-- 수신확인경로 -->
                            <td colspan="3">
                                <input class="form-control form-control-sm" type="text" name="openclickPath" value="${envServerInfoVo.openclickPath}">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="env.menu.serverinfo.3"/></th><!-- 링크확인경로 -->
                            <td colspan="3">
                                <input class="form-control form-control-sm" type="text" name="linkPath" value="${envServerInfoVo.linkPath}">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="env.menu.serverinfo.4"/></th><!-- 수신거부경로 -->
                            <td colspan="3">
                                <input class="form-control form-control-sm" type="text" name="rejectPath" value="${envServerInfoVo.rejectPath}">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.18"/></th><!-- 메일 재발송시 제외 대상 에러코드 -->
                            <td colspan="3">
                                <input class="form-control form-control-sm" type="text" value="${envServerInfoVo.resendErrorCd}" name="resendErrorCd">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.24"/></th><!-- 팩스 재발송시 제외 대상 에러코드 -->
                            <td>
                                <input class="form-control form-control-sm" type="text" value="${envServerInfoVo.resendFaxErrorCd}" name="resendFaxErrorCd">
                            </td>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.25"/></th><!-- 모바일 재발송시 제외 대상 에러코드 -->
                            <td>
                                <input class="form-control form-control-sm" type="text" value="${envServerInfoVo.resendSmsErrorCd}" name="resendSmsErrorCd">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.26"/></th><!-- 알림톡 재발송시 제외 대상 에러코드 -->
                            <td>
                                <input class="form-control form-control-sm" type="text" value="${envServerInfoVo.resendAltalkErrorCd}" name="resendAltalkErrorCd">
                            </td>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.27"/></th><!-- PUSH 재발송시 제외 대상 에러코드 -->
                            <td>
                                <input class="form-control form-control-sm" type="text" value="${envServerInfoVo.resendPushErrorCd}" name="resendPushErrorCd">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.serverinfo.5"/></th><!-- SMTP발송 재시도 횟수 -->
                            <td>
                                <input class="form-control form-control-sm w-25" value="${envServerInfoVo.retryCnt}" name="retryCnt">
                            </td>
                            <th scope="row" class="ls--1px"><em class="required">required</em><spring:message code="env.menu.serverinfo.16"/></th><!-- 이케어 재발송시 리턴된 이메일 포함여부 -->
                            <td>
                                <input class="form-control form-control-sm w-25" type="text" maxlength="1" value="${envServerInfoVo.resendIncludeReturnmailYn}" name="resendIncludeReturnmailYn">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.serverinfo.19"/></th><!-- 스풀 파일 보존 기간(일) -->
                            <td>
                                <input class="form-control form-control-sm w-25" type="text" maxlength="4" value="${envServerInfoVo.spoolPreservePeriod}" name="spoolPreservePeriod">
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.serverinfo.20"/></th><!-- 로그 파일 보존 기간(일)  -->
                            <td>
                                <input class="form-control form-control-sm w-25" type="text" maxlength="4" value="${envServerInfoVo.logPreservePeriod}" name="logPreservePeriod">
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.10"/></th><!-- ASE 링크추적 머지 파라미터 -->
                            <td colspan="3">
                                <textarea class="form-control font-size-13" type="text" name="aseLinkMergeParam" rows="4">${envServerInfoVo.aseLinkMergeParam}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.11"/></th><!-- ASE 수신거부 머지 파라미터 -->
                            <td colspan="3">
                                <textarea class="form-control font-size-13" type="text" name="aseRejectMergeParam" rows="4" >${envServerInfoVo.aseRejectMergeParam}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.12"/></th><!-- ASE 수신확인 머지 파라미터 -->
                            <td colspan="3">
                                <textarea class="form-control font-size-13" type="text" name="aseOpenScriptlet" rows="4">${envServerInfoVo.aseOpenScriptlet}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.13"/></th><!-- Groovy 링크추적 머지 파라미터 -->
                            <td colspan="3">
                                <textarea class="form-control font-size-13" type="text" name="groovyLinkMergeParam" rows="4">${envServerInfoVo.groovyLinkMergeParam}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.14"/></th><!-- Groovy 수신거부 머지 파라미터 -->
                            <td colspan="3">
                                <textarea class="form-control font-size-13" type="text" name="groovyRejectMergeParam" rows="4">${envServerInfoVo.groovyRejectMergeParam}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="env.menu.serverinfo.15"/></th><!-- Groovy 수신확인 머지 파라미터 -->
                            <td colspan="3">
                                <textarea class="form-control font-size-13" type="text"name="groovyOpenScriptlet" rows="4">${envServerInfoVo.groovyOpenScriptlet}</textarea>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div><!-- //card-body -->

            <c:if test="${sessionScope.write eq 'W'}">
                <div class="card-footer pb-4 btn_area"><!-- button area -->
                    <button type="button" class="btn btn-outline-primary" id="saveBtn">
                        <spring:message code="button.save"/><!-- 저장 -->
                    </button>
                </div>
            </c:if>
        </div><!--//card-->
    </div>
</div>
</form>
</body>
</html>
