<%-------------------------------------------------------------------------------------------------
 * - [로그인] 로그인 메일 인증 기능 <br/>
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
            login();
        });
        $("#sendMail").on("click", function(event) {
            authMailSend();
        });
    }
    function dailyMissionTimer(duration) {
        var endTime = new Date();
        endTime.setMinutes(endTime.getMinutes()+5);
        var timer, minutes, seconds;
        var interval = setInterval(function(){
            timer = (endTime.getTime() - new Date().getTime()) / 1000;
            minutes = parseInt(timer / 60 % 60, 10);
            seconds = parseInt(timer % 60, 10);
            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;
            $("#timer").text(minutes + ":" + seconds);
            if (--timer < 0) {
                timer = 0;
                clearInterval(interval);
            }
        }, 1000);
    }
    var ck = false;

    function login() {
         var param = $.mdf.serializeObject('#form');
         $.mdf.postJSON("/auth/check.do", JSON.stringify(param), function(data) {
             if(data.result == "false"){
                 alert(data.errMsg);
                 ck = false;
                 return false;
             }else if(data.result == "true"){
                    var options = {
                            beforeSubmit : showRequest,
                            type : 'post',
                            dataType : 'json',
                            success : onSuccess,
                            url:'/admin/login.do'
                      };
                 $("#form").ajaxSubmit(options);
             }
         });
    }

    function authMailSend(){
        if(ck){
            return false;
        }
        ck = true;
        var param = $.mdf.serializeObject('#form');
        $.mdf.postJSON('/auth/mail/send.do', JSON.stringify(param), function(data) {
            if(data.result == "false"){
                alert(data.errMsg);
            }else if(data.result == "true"){
                   alert(data.errMsg);
                   dailyMissionTimer(5);
                   $("#login_block").show();
                   $("#auth_block").hide();
                   $("#password").attr('readOnly','true');
                   $("#adminId").attr('readOnly','true');
            }
            ck = false;
        });

      /*   var options = {
                beforeSubmit : showRequest,
                type : 'post',
                dataType : 'json',
                url:'/auth/mail/send.do',
                success : function(data){
                    if (data.result == "true") {
                        alert(data.errMsg);
                        dailyMissionTimer(5);
                        $("#login_block").show();
                        $("#auth_block").hide();
                    } else {
                        alert(data.errMsg);
                    }
                },complete : function(){
                    ck = false;
                }
        }
        $("#form").ajaxSubmit(options); */
    }

    function initPage() {
        $("#login_block").hide();
        //$('#adminId').focus();
        //focusedFnc() ;
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
        if($('#mail').val() =='') {
            alert('Please enter mail');
            $('#mail').focus();
            return false;
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
                    $('#authCheck').val('Y');
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
        <div class="main-color py-8 text-center mb-4">
            <h1 class="text-white mb-0 ">Make It Worth '◡'</h1>
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
                <form id="form" name="loginFrm" method="post">
                <input type="hidden" id="pullLoginUser" name="pullLoginUser" value="" />
                <input type="hidden" id="authCheck" name="authCheck" value="N" />
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
                    <div class="text-center" id="login_block" >
                        <input id="token" name="token" type="text" size="25" class="form-control" placeholder="인증코드를 입력해주세요."/>
                        <span id="timer" class="text-danger"></span>
                         <button type="button" class="btn main-color my-2 px-4 text-white" id="loginBtn" onclick="return false;">
                            <spring:message code="button.login" /><!-- 로그인 -->
                        </button>
                    </div>

                    <div class="text-center" id="auth_block">
                        <input id="mail" name="mail" type="text" size="25" class="form-control" placeholder=" Mail-address" autocomplete="off" />
                        <button type="button" class="btn main-color my-2 px-4 text-white" id="sendMail">
                            인증메일 보내기
                        </button>
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
            <div class=" text-center login-box-footer">
                본 사이트는 IE 9 이상 브라우저 지원하며, Chrome 및 IE 11 이상의 브라우저에 최적화되어 있습니다.
            </div>
        </footer>
    </div>
</body>
</html>
