<%-------------------------------------------------------------------------------------------------
 * - [환경설정>도메인 관리] 도메인 관리 <br/>
 * - URL : /env/domain.do <br/>
 * - Controller :com.mnwise.wiseu.web.env.web.EnvLogFileDownloadController <br/>
 * - 이전 파일명 : env_domain.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title><spring:message code="env.msg.domain"/></title><!-- 도메인관리 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if(!confirm('<spring:message code="common.alert.3"/>')) {  // 저장 하시겠습니까?
                return;
            }

            var param = $.mdf.serializeObject('#envFrm');
            $.post("/env/updateDomain.do", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    alert("<spring:message code='common.alert.1'/>");  // 저장 되었습니다.
                    document.location.reload("/env/domain.do");
                } else {
                    alert(result.message)
                }
            });
        });
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="container-fluid">
            <div class="card mb-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="mb-0"><spring:message code="env.msg.domain"/></h3><!-- 도메인관리 -->
                    </div>

                    <form action="/env/updateDomain" name="envFrm" id="envFrm" method="post">
                    <div class="card-body gridWrap">
                        <div class="table-responsive">
                            <table class="table table-sm dataTable table-fixed">
                                <colgroup>
                                    <col width="150" />
                                    <col width="*" />
                                </colgroup>
                                <tbody>
                                <tr>
                                    <th scope="row"><em class="required">required</em><spring:message code="common.menu.email"/> <spring:message code="env.msg.domain"/></th><!-- 이메일 도메인관리 -->
                                    <td>
                                        <textarea class="form-control" name="domains" id="domains" rows="5" placeholder="@naver.com,@gmail.com" >${domainList}</textarea>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div><!-- // Light table -->
                    </div><!-- //card-body -->

                    <c:if test="${sessionScope.write eq 'W'}">
                    <div class="card-footer pb-4 btn_area"><!-- button area -->
                        <button  id="saveBtn" class="btn btn-outline-primary">
                            <spring:message code="button.save"/><!-- 저장 -->
                        </button>
                    </div>
                    </c:if>
                </div>
            </div><!--e.container-fluid-->
        </div><!-- e.page content -->
    </div><!-- e.본문 e.main-panel -->
</div>
</body>
</html>
