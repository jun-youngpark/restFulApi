<%-------------------------------------------------------------------------------------------------
  * - [환경설정>개인정보 관리] 개인정보 관리 <br/>
  * - URL : /env/myInfo.do <br/>
  * - Controller :com.mnwise.wiseu.web.env.web.EnvMyInfoController <br/>
  * - 이전 파일명 : env_myinfo.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="env.msg.userinfo"/></title><!-- 개인정보관리 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                passWd           : {notBlank : true, rangelength : [8, 14], containsUpperCase : true, containsLowerCase : true, containsDigit : true, containsSpecialChar : true},
                passWdChk        : {notBlank : true, equalTo: "input[name=passWd]"},
                email            : {notBlank : true, email : true},
                listCountPerPage : {notBlank : true, digits : true, range : [1, 100]}
            };

            var messages = {
                passWdChk : {equalTo : "<spring:message code='env.alert.user.pw'/>"}  // 사용자 암호를 확인하세요.
            };

            if($.mdf.validForm("#updateForm", rules, messages) == false) {
                return;
            }

            // 수정시 3개월안에 사용한 비밀번호는 다시 사용 못함
            //var param = "&passWd=" + pass;
            var param = {
                userId : "${envMyInfoVo.userId}",
                passWd : $("input[name=passWd]").val()
            };
            //Account.checkShaPw('${envMyInfoVo.userId}', pass, function (data) {
            //getDataAjaxCall("/env/${envMyInfoVo.userId}/env_myinfo.do" , param , function (data) {
            $.post("/env/checkShaPw.json", $.param(param, true), function(data) {
                if(data > 0) {
                    alert("<spring:message code='env.alert.user.msg8'/>");  // 3개월 이내에 사용한 이력이 있는 비밀번호 입니다.
                    return false;
                } else {
                    if(!confirm("<spring:message code='common.alert.test.msg21'/>")) {  // 저장하시겠습니까?
                        return;
                    }

                    var param = $.mdf.serializeObject('#updateForm');
                    $.post("/env/updateMyInfo.do", $.param(param, true), function(result) {
                        var message = result.message;
                        var chgMsg = "${sessionScope.passwdChange}";
                        if(message == "save") {
                           if(chgMsg == 'passwdChange') {
                               location.href= '/admin/logout.do';
                               alert("<spring:message code='env.alert.user.msg7'/>");  // 암호가 변경되었습니다. 재로그인을 위해 로그아웃합니다.
                               return false;
                           } else {
                               alert("<spring:message code='common.alert.test.msg3'/>");  // 저장되었습니다.
                               return false;
                           }
                        } else if(message == "DPFAIL") {
                            alert("<spring:message code='account.alert.user.lowercase.pw'/>");  // 비밀번호에 영문 소문자를 포함시켜야 합니다.
                            return false;
                        } else if(message == "UPFAIL") {
                            alert("<spring:message code='account.alert.user.uppercase.pw'/>");  // 비밀번호에 영문 대문자를 포함시켜야 합니다.
                            return false;
                        } else if(message == "NPFAIL") {
                            alert("<spring:message code='account.alert.user.number.pw'/>");  // 비밀번호에 숫자를 포함시켜야 합니다.
                            return false;
                        } else if(message == "SPFAIL") {
                            alert("<spring:message code='account.alert.user.special.pw'/>");  // 비밀번호에 특수문자를 포함시켜야 합니다.
                            return false;
                        } else if(message == "PLFAIL") {
                            alert("<spring:message code='env.alert.user.msg3'/>");  // 비밀번호는 8자리 이상 15자리 미만이어야 합니다.
                            return false;
                        }
                    });
                }
            });
        });
    }

    function initPage() {
        var chgMsg = "${sessionScope.passwdChange}";
        if(chgMsg == 'passwdChange') {
            alert("<spring:message code='env.alert.user.msg6'/>");  // 암호 변경 기간이 만료되었습니다. 비밀번호 변경 후 다시 로그인해 주세요.
        }
    }
</script>
</head>

<body>
<form id="updateForm" name="updateForm" action="/env/updateMyInfo.do" method="post">
<input type="hidden" name="userId" value="${envMyInfoVo.userId}"/>
<input type="hidden" name="userTypeCd" value="${envMyInfoVo.userTypeCd}"/>
<input type="hidden" name="lastUpdateDt" value="${envMyInfoVo.lastUpdateDt}"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="env.msg.userinfo"/></h3><!-- 개인정보관리 -->
            </div>

            <div class="card-body gridWrap">
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="13%" />
                            <col width="40%" />
                            <col width="13%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="env.menu.user.id"/></th><!-- 사용자ID -->
                            <td class="control-text-sm">${envMyInfoVo.userId}</td>
                            <th scope="row"><spring:message code="env.menu.user.type"/></th><!-- 사용자 유형  -->
                            <td>
                                <c:choose>
                                    <c:when test="${envMyInfoVo.userTypeCd eq 'A'}"><spring:message code="env.menu.user.admin"/></c:when>
                                    <c:when test="${envMyInfoVo.userTypeCd eq 'M'}"><spring:message code="env.menu.user.manager"/></c:when>
                                    <c:when test="${envMyInfoVo.userTypeCd eq 'U'}"><spring:message code="env.menu.user.user"/></c:when>
                                    <c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.user.pw"/></th><!-- 비밀번호 -->
                            <td class="text-primary">
                                <input class="form-control form-control-sm w-75" type="password" name="passWd" autocomplete="off">
                                <i class="fas fa-exclamation-circle"></i> <spring:message code="env.alert.user.msg1"/>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.user.cpw"/></th><!-- 비밀번호 확인 -->
                            <td><input class="form-control form-control-sm w-75" type="password" name="passWdChk" autocomplete="off"></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="env.menu.user.name"/></th><!-- 성명 -->
                            <td><input class="form-control form-control-sm w-75" type="text" name="nameKor" value="${envMyInfoVo.nameKor}"></td>
                            <th scope="row"><spring:message code="env.menu.user.title"/></th><!-- 직급 -->
                            <td><input class="form-control form-control-sm w-75" type="text" name="userClass" value="${envMyInfoVo.userClass}"></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="env.menu.user.contact"/></th><!-- 전화번호 -->
                            <td><input class="form-control  form-control-sm w-75" type="text" name="telNo" value="${envMyInfoVo.telNo}"></td>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.user.email"/></th><!-- 이메일 -->
                            <td><input class="form-control form-control-sm" type="text" name="email" value="${envMyInfoVo.email}"></td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><em class="required">required</em><spring:message code="env.menu.user.size"/></th><!-- 페이지리스트사이즈 -->
                            <td><input class="form-control form-control-sm w-25" type="text" name="listCountPerPage" value="${envMyInfoVo.listCountPerPage}"></td>
                            <th scope="row"><spring:message code="env.menu.user.udate"/></th><!-- 최종수정일 -->
                            <td>${envMyInfoVo.lastUpdateDt}</td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div><!-- // card-body -->

            <c:if test="${sessionScope.write eq 'W'}">
            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <button id="saveBtn" class="btn btn-outline-primary">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div>
            </c:if>
        </div>
    </div>
</div>
</form>
</body>
</html>
