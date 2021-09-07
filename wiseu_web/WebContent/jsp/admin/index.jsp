<%-------------------------------------------------------------------------------------------------
 * - [로그인] 로그인 <br/>
 * - URL : /admin/index.do <br/>
 * - Controller : com.mnwise.wiseu.web.admin.web.AdminController <br/>
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="common.web.mode.name_${wiseu:getProperty('web.exec.mode', '1')}" /></title>
<%@ include file="/jsp/include/plugin.jsp" %>
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

        //엔터키 입력시 submit
        document.addEventListener("keypress", function ovEvent(event){
            if(event.key === 'Enter'){
                $("#loginBtn").trigger('click');
            }
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
        focusedFnc() ;
    }

    function focusedFnc() {
        $("input").focus(function(){
            var $target = $(this).closest(".form-group")
             $target.addClass("focused");
        })
        $('input').blur(function(){
            var $target = $(this).closest(".form-group")
             $target.removeClass("focused");
        });
    }

    function showRequest() {
        // 언어별 alert창 분리화
        if($('#language').val() == 'ko') {
            if ($('#adminId').val() == '') {
                alert('아이디를 입력하세요.');
                $('#adminId').focus();
                return false;
            }
            if ($('#adminPwd').val() == '') {
                alert('비밀번호를 입력하세요.');
                $('#adminPwd').focus();
                return false;
            }
        } else if ($('#language').val() == 'vn') {
            if ($('#adminId').val() == '') {
                alert('Hãy nhập ID định.');
                $('#adminId').focus();
                return false;
            }
            if ($('#adminPwd').val() == '') {
                alert('Vui lòng nhập mật khẩu của bạn.');
                $('#adminPwd').focus();
                return false;
            }
        } else {
            if ($('#adminId').val() == '') {
                alert('Please enter ID');
                $('#adminId').focus();
                return false;
            }
            if ($('#adminPwd').val() == '') {
                alert('Please enter password');
                $('#adminPwd').focus();
                return false;
            }
        }
    }

    function onSuccess(data) {
        if (data.login.result == "true") {
            location.href = data.login.goPath;
        } else {
            if (data.login.pullLoginUser == 'N') {
                if(confirm(data.login.errMsg)) {
                    $('#pullLoginUser').val('Y');
                    $('#form').submit();
                } else {
                    $('#pullLoginUser').val('');
                }
            } else {
                alert(data.login.errMsg);
                location.reload();
            }
        }
    }
</script>
</head>
<body style="background: #f7fafc;">
  <div class="wrap">
        <!-- Header -->
        <div class="main-color text-center py-8 mb-4">
            <h1 class="text-white mb-0">Make It Worth '◡'</h1>
            <p class="text-lead text-white">당신의 비즈니스 파트너</p>
            <div class="text-center text-muted mb-4">
                <img src="/images/logo/wiseU_new_wh.png" alt="" width="100">
            </div>
        </div>
        <!-- // Header -->
        <!-- login box-->
        <div class=" mt--8 pb-5 login-box-wrap">
            <div class="card-body login-box p-4">
                <div class="text-center text-muted mb-3">
                    LOGIN <i class="ni ni-user-run"></i>
                </div>
                <form id="form" name="loginFrm" action="/admin/login.do" method="post" role="form">
                <input type="hidden" id="pullLoginUser" name="pullLoginUser" value="" />
                    <div class="form-group input-icons mb-3">
                        <i class="fas fa-user icon"></i>
                        <input class="form-control input-field" name="adminId" id="adminId" type="text" placeholder="ID">
                    </div>
                    <div class="form-group input-icons">
                        <i class="fas fa-lock-open icon"></i>
                        <input class="form-control input-field" type="password" name="adminPwd" id="adminPwd" placeholder="Password">
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
                        <button type="button" class="btn main-color my-2 px-4 text-white" id="loginBtn">
                            <spring:message code="button.login" /><!-- 로그인 -->
                        </button>
                    </div>
                </form>

            </div>
            <p class="copyrights text-center pt-2">
            <spring:message code="common.web.mode.name_${wiseu:getProperty('web.exec.mode', '1')}" /> 8.0 Copyright 2020. M&amp;WISE All rights reserved
            </p>
            <!--card-body-->

        </div>
        <!-- login box-->
        <!-- Footer -->
        <footer class="pt-5" id="footer-main">
            <div class="text-center login-box-footer">
                본 사이트는 IE 9 이상 브라우저 지원하며, Chrome 및 IE 11 이상의 브라우저에 최적화되어 있습니다.
            </div>
        </footer>
    </div>
</body>
</html>
