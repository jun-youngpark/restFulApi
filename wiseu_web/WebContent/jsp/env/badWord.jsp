<%-------------------------------------------------------------------------------------------------
  * - [환경설정>금칙어 관리] 금칙어 관리 <br/>
  * - URL : /env/badWord.do <br/>
  * - Controller :com.mnwise.wiseu.web.env.web.EnvBadWordController <br/>
  * - 이전 파일명 : env_badword.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title><spring:message code="env.msg.forbidden"/></title><!-- 금칙어관리 -->
<script>
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if(!confirm('<spring:message code="common.alert.3"/>')) {  // 저장 하시겠습니까?
                return;
            }
            $('#editorFrm').attr('action', "/env/updateBadWord.do").submit();
        });
    }
</script>
</head>
<body>
<form id="editorFrm" name="editorFrm" action="/env/badWord.do" method="post">
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="env.msg.forbidden"/></h3><!-- 금칙어관리 -->
            </div>

            <div class="card-body gridWrap">
                <div class="alert alert-secondary mb-0" role="alert">
                    <i class="fas fa-exclamation-circle"></i> <spring:message code="env.help.forbiddenwords.1" /><!-- 여러 단어를 구분할 때는 콤마(,)를 사용하세요. -->
                </div>
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="200" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <c:if test="${fn:contains(envBadWordVo.channelUseList, 'M')}">
                        <tr>
                            <th scope="row"><spring:message code="common.menu.email"/> <spring:message code="env.msg.ban"/></th><!-- 이메일 금칙어 -->
                            <td>
                                <textarea class="form-control" id="template" name="mailBadWord" rows="8">${envBadWordVo.mailBadWord}</textarea>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${fn:contains(envBadWordVo.channelUseList, 'S') or fn:contains(envBadWordVo.channelUseList, 'T')}">
                        <tr><!-- SMS, LMS/MMS, PUSH 금칙어 -->
                            <th scope="row" class="ls--1px"><spring:message code="common.channel.S"/>, <spring:message code="common.channel.T"/>, <spring:message code="common.channel.P"/> <spring:message code="env.msg.ban"/></th>
                            <td>
                                <textarea class="form-control" id="template" name="smsBadWord" rows="8">${envBadWordVo.smsBadWord}</textarea>
                            </td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div><!-- //card-body -->

            <c:if test="${sessionScope.write eq 'W'}">
            <div class="card-footer pb-4 btn_area"><!-- button area -->
               <button id="saveBtn" class="btn btn-outline-primary">
                   <spring:message code="button.save"/><!-- 저장 -->
               </button>
            </div>
            </c:if>
        </div><!-- e.card -->
    </div>
</div><!-- e.main-panel -->
</form>
</body>
</html>
