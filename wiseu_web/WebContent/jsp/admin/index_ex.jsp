<%-------------------------------------------------------------------------------------------------
 * - [로그인] 로그인 (IT관리자용)<br/>
 * - URL : /admin/index_ex.do <br/>
 * - Controller : com.mnwise.wiseu.web.admin.web.AdminExController <br/>
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="blank" />
<title>wiseU v5</title>

<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 로그인 버튼 클릭
        $("#loginBtn").on("click", function(event) {
            $("#form").submit();
        });
    }

    function initPage() {
        var options = {
            beforeSubmit : showRequest,
            dataType : 'json',
            success : onSuccess
        };

        $('#form').ajaxForm(options);
        $('#adminId').focus();
    }

    function showRequest() {
        if($('#adminId').val() == '') {
            alert('아이디를 입력하세요.');
            $('#adminId').focus();
            return false;
        }

        if($('#adminPwd').val() == '') {
            alert('비밀번호를 입력하세요.');
            $('#adminPwd').focus();
            return false;
        }
    }

    function onSuccess(data) {
        if(data.login.result == "true") {
            location.href = data.login.goPath;
        } else {
            $("form")[0].reset();
            alert(data.login.errMsg);
            $('#adminId').focus();
        }
    }
</script>
</head>

<body>
<div class="wrap">
        <!-- Header -->
        <div class="main-color py-8 text-center mb-4">
            <h1 class="text-white mb-0 ">Make It Worth '◡'</h1>
            <p class="text-lead text-white">당신의 비즈니스 파트너</p>
            <div class="text-center text-muted mb-4">
                <img src="/images/logo/wiseU_new_wh.png" width="100">
            </div>
        </div>
        <!-- // Header -->
        <!-- login box-->
        <div class=" mt--8 pb-5 login-box-wrap">
            <div class="card-body login-box p-4">
                <div class="text-center text-muted mb-3">
                    LOGIN <i class="ni ni-user-run"></i>
                </div>
                <form method="post" id="form" name="loginFrm" action="/admin/login.do">
                    <div class="form-group mb-3">
                        <div class="input-group input-group-merge">
                            <div class="input-group-prepend">
                                <span class="input-group-text"><i class="ni ni-single-02"></i></span>
                            </div>
                            <input class="form-control" name="adminId" id="adminId" type="text">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group input-group-merge ">
                            <div class="input-group-prepend">
                                <span class="input-group-text"><i class="ni ni-lock-circle-open"></i></span>
                            </div>
                            <input class="form-control" type="password" name="adminPwd" id="adminPwd" placeholder="Password">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group input-group-merge">
                             <select class="form-control form-control-sm" id="language" name="language">
                                <c:forEach var="lang" items="${wiseu:getProperty('language', 'ko')}">
                                    <option value="${lang}"><spring:message code="common.language.${lang}" /></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn main-color my-2 px-4 text-white" id="loginBtn">
                            <spring:message code="button.login" /><!-- 로그인 -->
                        </button>
                    </div>
                </form>
            </div>
            <p class="copyrights text-center pt-2">
            <spring:message code="common.web.mode.name_${wiseu:getProperty('web.exec.mode', '1')}" /> 7.0 Copyright 2020. M&amp;WISE All rights reserved
            </p>
            <!--card-body-->
        </div>
        <!-- login box-->
        <!-- Footer -->
        <footer class="pt-7" id="footer-main">
            <div class=" text-center login-box-footer">
                본 사이트는 IE 9 이상 브라우저 지원하며, Chrome 및 IE 11 이상의 브라우저에 최적화되어 있습니다.
            </div>
        </footer>
    </div>
</body>
</html>
