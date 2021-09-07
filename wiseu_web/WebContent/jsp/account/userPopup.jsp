<%-------------------------------------------------------------------------------------------------
 * - [사용자>사용자 관리] 사용자 등록/수정 (팝업) <br/>
 * - URL : /account/userPopup.do <br/>
 * - Controller :com.mnwise.wiseu.web.account.web.AccountUserPopupController <br/>
 * - 이전 파일명 : account_user_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="account.msg.userinfo" /></title><!-- 사용자 정보 -->
<%@ include file="/jsp/include/plugin.jsp"%>
<script language="javascript">
    var rules = {
        userId    : {notBlank : true, alphaDigit : true},
        nameKor   : {notBlank : true, maxbyte : 50},
        <c:if test="${!readonly}">
        // 신규 사용자 등록시
        passWd    : {notBlank : true, rangelength : [8, 14], containsUpperCase : true, containsLowerCase : true, containsDigit : true, containsSpecialChar : true},
        </c:if>
        <c:if test="${webExecMode eq '1'}">
        email     : {notBlank : true, email : true, maxbyte : 100},
        </c:if>
        telNo     : {telnum : true},
        userClass : {maxbyte : 50}
    };

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 중복체크 버튼 클릭
        $("#duplicateCheckBtn").on("click", function(event) {
            if($.mdf.validElement("#updateForm", "input[name=userId]", rules) == false) {
                return;
            }

            var param = {
                userId : $("input[name=userId]").val()
            };

            $.post("/account/checkUserIdDuplication.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    if(result.value == "0") {
                        if(confirm("<spring:message code='account.alert.user.reg.confirm'/>")) {  // 사용 가능한 아이디 입니다!\\n이 아이디를 사용 하시겠습니까?
                            $("input[name=userId]").attr("readonly", "readonly");
                            $("#idChk").val("ok");
                        } else {
                            $("#idChk").val("no");
                        }
                    } else {
                        alert("<spring:message code='account.alert.user.reg.msg1'/>");  // 이미 등록된 아이디 입니다.
                        $("#idChk", "form[name=updateForm]").attr("value", "no");
                    }
                }
            });
        });

        // (비밀번호) 수정/최소 버튼 클릭
        $("#modifyBtn").on("click", function(event) {
            if($.mdf.isNotBlank($("input[name=passWd]").attr("readonly"))) {  // readonly 상태인 경우
                $("input[name=passWd]").removeAttr("readonly");
                $("input[name=pwChg]").val("true");
                $('#modifyBtn').text('<spring:message code="button.cancel" />');  // 버튼명을 취소로 변경
                rules.passWd = {notBlank : true, rangelength : [8, 14], containsUpperCase : true, containsLowerCase : true, containsDigit : true, containsSpecialChar : true};
            } else {
                $("input[name=passWd]").attr("readonly", true);
                $("input[name=pwChg]").val("false");
                $('#modifyBtn').text('<spring:message code="button.modify" />');  // 버튼명을 수정으로 변경
                rules.passWd = null;
            }
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            if($("#idChk").val() == 'no' && "${readonly}" != 'true') {
                alert("<spring:message code='account.alert.user.reg.msg2'/>");  // 사용자를 등록하려면 사용자ID 중복체크를 하셔야 합니다.
                return;
            }

            var url;
            var okMsg;
            if("${readonly}" == 'true') {  // 수정
                url = "/account/updateUser.json";
                okMsg = "<spring:message code='account.alert.user.reg.update'/>";  // 사용자 정보가 수정되었습니다.
            } else {  // 등록
                url = "/account/insertUser.json";
                okMsg = "<spring:message code='account.alert.user.reg.add'/>";  // 사용자 정보가 추가 되었습니다.
            }

            var param = $.mdf.serializeObject('#updateForm');
            $.post(url, $.param(param, true), function(result) {
                if(result.code == "OK") {
                    // 크롬 버전 업그레이드 후 대상자 추가 안되는 현상으로 callback 메서드로 부모창 처리
                    setTimeout(function() {
                        alert(okMsg);
                        window.opener.location.reload();
                        window.close();
                    },500);
                }
            });
        });

        // 권한보기 체크박스 클릭
        $("#permisionCheck").on("click", function(event) {
            $('#permissionDiv').toggle();
            if($('#userPermision').val() == 'on') {
                $('#userPermision').val('off');
            } else {
                $('#userPermision').val('on');
            }
        });

        // 권한 리스트 체크박스 이벤트
        // 승인 체크박스 클릭시 이벤트
        $("input[id*=X_]").on("click", function(event) {
            var idx = this.id
            idx = idx.substr(2);
            var stat = this.checked;
        });

        // 쓰기 체크박스 클릭시 이벤트
        $("input[id*=W_]").on("click", function(event) {
            var idx = this.id.substr(2);
            if(this.checked == false) {
                idx = idx.substr(0, idx.length - 2);
                $("input[id*=PW_" + idx + "]").each(function() {
                    this.checked = false;
                });
            } else {
                $("#R_" + idx).attr('checked', true);
            }
        });

        // 읽기 체크박스 클릭시 이벤트
        $("input[id*=R_]").on("click", function(event) {
            var idx = this.id.substr(2);
            if(this.checked == false) {
                if(idx == "_01") {
                    $("#X" + idx + "03").attr('checked', false);
                } else if(idx == "_02") {
                    $("#X" + idx + "02").attr('checked', false);
                }

                $("#W_" + idx).attr('checked', false);
                idx = idx.substr(0, idx.length - 2);
                $("input[id*=PR_" + idx + "]").each(function() {
                    this.checked = false;
                });
                $("input[id*=PW_" + idx + "]").each(function() {
                    this.checked = false;
                });
                $("input[id*=PX_" + idx + "]").each(function() {
                    this.checked = false;
                });
            }
        });

        // 상위목록 체크박스 클릭
        $("input[id*=P]").on("click", function(event) {
            var idx = this.id.substr(3);
            var n = (this.id.substr(1, 1) == 'R') ? 1 : 2;
            if(this.checked == true) {
                switch (n) {
                case 2:
                    checking("W_" + idx, true);
                case 1:
                    checking("R_" + idx, true);
                }
            } else {
                switch (n) {
                case 1:
                    checking("R_" + idx, false);
                case 2:
                    checking("W_" + idx, false);
                }
            }
        });
    }

    function initPage() {
        <c:if test="${empty userVo.userTypeCd}">
          $("input[name=userTypeCd][value=U]").attr("checked", true);
        </c:if>

        <c:if test="${(isaRole ne 'M') and  (isaRole ne 'A')}">
            $("#roleListTable :input[type=checkbox]").attr("disabled", "disabled");
        </c:if>
    }

    // 권한 리스트 체크박스 관련 이벤트
    function checking(idx, stat) {
        $("input[id*=" + idx + "]").each(function() {
            this.checked = stat;
        });
    }
</script>
</head>

<body>
<form id="updateForm" name="updateForm" action="/account/userPopup.do" method="post"><!-- /account/account_user_popup.do -->
<input type="hidden" id="idChk" name="idChk" value="no" />
<input type="hidden" id="userPermision" name="userPermision" value="${userPermision}" />
<input type="hidden" id="grpCd" name="grpCd" value="${UserGrpVo.grpCd}" />
<input type="hidden" id="grpNm" name="grpNm" value="${UserGrpVo.grpNm}" />
<input type="hidden" name="isaRole" value="${isaRole}" />
<input type="hidden" name="pwChg" value="false" />
<!-- 금융권 처럼 사번을 사용하는 경우가 있(그 용도로 남겨 둠) -->
<input type="hidden" name="soeIdChk" />
<input type="hidden" name="soeid" />
<input type="hidden" name="geid"/>
<!-- Maker, Checker 미 사용으로 등록 radio 버턴을 주석처리하고 hidden으로 기본값을 넘김. -->
<input type="hidden" name="userRole" value="N" />
<input type="hidden" name="workDoc" value="by Admin" />

<div class="card pop-card overflow-y-auto">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="account.msg.userinfo" /></h5></div><!-- 사용자 정보 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body mb-0" style="max-height: 320px;">
        <div class="table-responsive gridWrap">
            <table class="table table-sm dataTable table-fixed">
                <colgroup>
                    <col width="12%" />
                    <col width="*" />
                    <col width="10%" />
                    <col width="*" />
                    <col width="12%" />
                    <col width="*" />
                </colgroup>
                <tbody>
                <tr>
                    <th scope="row"><em class="required">required</em><spring:message code="account.menu.user.id" /></th><!-- 사용자 ID-->
                    <td>
                        <div class="form-row">
                            <c:if test="${readonly eq 'true'}">
                            <div class="col-12">
                                <input type="text" class="form-control form-control-sm" name="userId" maxlength="15" value="${userVo.userId}" readonly />
                            </div>
                            </c:if>
                            <c:if test="${readonly ne 'true'}">
                            <div class="col-7">
                                <input type="text" class="form-control form-control-sm" name="userId" maxlength="15" value="${userVo.userId}" style="ime-mode:disabled;" />
                            </div>
                            <div class="col-5">
                                <button type="button" class="btn btn-sm btn-outline-primary" id="duplicateCheckBtn">
                                    <spring:message code="button.check.duplication" /><!-- 중복체크 -->
                                </button>
                            </div>
                            </c:if>
                        </div>
                    </td>
                    <th scope="row"><em class="required">required</em><spring:message code="account.menu.user.name" /></th><!-- 성명 -->
                    <td><input type="text" class="form-control form-control-sm" name="nameKor" value="${userVo.nameKor}" <c:if test="${readonly eq 'true' }">readonly</c:if> /></td>
                    <th scope="row"><spring:message code="account.menu.user.dname" /></th><!-- 부서명 -->
                    <td>${UserGrpVo.grpNm}</td>
                </tr>
                <c:if test="${isaRole eq 'A'}">
                <tr>
                    <th scope="row"><em class="required">required</em><spring:message code="account.menu.user.passwd" /></th><!-- 비밀번호 -->
                    <td colspan="5" class="control-text-sm">
                        <div class="form-row">
                            <c:choose>
                                <c:when test="${userVo.userId eq null}">
                                    <div class="col-4">
                                        <input type="password" name="passWd" class="form-control form-control-sm" required autocomplete="off" />
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-3">
                                        <input type="password" name="passWd" class="form-control form-control-sm" autocomplete="off" readonly="readonly" />
                                    </div>
                                    <div class="col-1">
                                        <button type="button" class="btn btn-sm btn-outline-primary" id="modifyBtn">
                                            <spring:message code="button.modify" /><!-- 수정 -->
                                        </button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="col-8 text-primary">
                                 <i class="fas fa-exclamation-circle"></i> <spring:message code="env.alert.user.msg1" /><!-- 8~14자 영문 대문자, 영문 소문자, 숫자, 특수문자 -->
                            </div>
                        </div>
                    </td>
                </tr>
                </c:if>
                <tr>
                    <th scope="row"><c:if test="${webExecMode eq '1' }"><em class="required">required</em></c:if><spring:message code="account.menu.user.email" /></th><!-- 이메일 -->
                    <td><input type="text" class="form-control form-control-sm" required name="email" value="${userVo.email}" /></td>
                    <th scope="row"><spring:message code="account.menu.user.contact" />	<!-- 전화번호 --></th>
                    <td><input type="text" class="form-control form-control-sm" name="telNo" value="${userVo.telNo}" /></td>
                    <th scope="row"><spring:message code="account.menu.user.position" /></th><!-- 직급 -->
                    <td><input type="text" class="form-control form-control-sm" name="userClass" value="${userVo.userClass}" /></td>
                </tr>
                <tr>
                    <th scope="row"><em class="required">required</em><spring:message code="account.menu.user.permission" /></th><!-- 권한 -->
                    <td colspan="3">
                        <%-- 관리자일 경우만 admin 권한을 보여준다 --%>
                        <c:if test="${userVo.userTypeCd eq 'A'}">
                        <div class="custom-control custom-radio custom-control-inline form-control-sm">
                            <input type="radio" class="custom-control-input" id="userTypeCd_A" name="userTypeCd" value="A" <c:if test="${userVo.userTypeCd eq 'A'}">checked</c:if> />
                            <label class="custom-control-label" for="userTypeCd_A">ADMIN</label>
                        </div>
                        </c:if>

                        <c:forEach var="loop" items="${userTypeCd}">
                        <c:if test="${loop.cd ne 'A'}">
                        <%-- 권한은 관리자 권한을 제외하고 보여준다. --%>
                        <div class="custom-control custom-radio custom-control-inline form-control-sm">
                            <input type="radio" class="custom-control-input" id="userTypeCd_${loop.val}" name="userTypeCd" value="${loop.cd}" <c:if test="${isaRole ne 'A'}">disabled</c:if> <c:if test="${userVo.userTypeCd eq loop.cd}">checked</c:if> />
                            <label class="custom-control-label" for="userTypeCd_${loop.val}">${loop.val}</label>
                        </div>
                        </c:if>
                        </c:forEach>
                    </td>
                    <th scope="row"><spring:message code="account.menu.permission.permissionview" /></th><!-- 권한보기 -->
                    <td>
                        <div class="custom-control custom-checkbox custom-control-inline">
                            <input type="checkbox" name="permisionCheck" id="permisionCheck" <c:if test="${userPermision eq 'on'}">checked</c:if> <c:if test="${isaRole ne 'A' and readonly eq 'false' }">disabled="true"</c:if>  class="custom-control-input" />
                            <label class="custom-control-label" for="permisionCheck"><span class="hide">권한보기 체크</span></label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><spring:message code="template.column.yellowid" /></th><!-- 카카오톡 채널ID -->
                    <td class="border-right-0" colspan="5">
                        <c:forEach items="${adminKakaoSenderKeyList}" var="kakaoSenderItem" varStatus="itemStatus">
                            <c:if test="${(itemStatus.index+1) % 3 == 0}"><br/></c:if>
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input class="custom-control-input" name="kakaoKey" type="checkbox" value="${kakaoSenderItem.KAKAO_SENDER_KEY}" id="kakaoKey_${itemStatus.count}"
                                    <c:forEach items="${userKakaoSenderKeyList}" var="userKakaoSenderItem">
                                        <c:if test="${userKakaoSenderItem.KAKAO_SENDER_KEY eq kakaoSenderItem.KAKAO_SENDER_KEY}">checked</c:if>
                                    </c:forEach>
                                />
                                <label class="custom-control-label" for="kakaoKey_${itemStatus.count}">${kakaoSenderItem.KAKAO_YELLOW_ID}</label>
                            </div>
                        </c:forEach>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div><!-- //Light table -->

    <!-- 권한보기 card body-->
    <div class="card-body authority-wrap overflow-y-hidden" id="permissionDiv" style="display:${userPermisionView}">
        <h4 class="h3 text-primary mb-0"><spring:message code='account.menu.permission.authority' /></h4><!-- 권한리스트 -->
        <div class="alert alert-secondary mb-1" role="alert">
            <i class="fas fa-circle fa-xs"></i> <spring:message code="account.menu.permission.modifyguide" /><!-- 부여된 권한이 없을 경우에는 부서기본권한이 부여됩니다. 그리고 '실행/발송'권한이 있으면 리스트 읽기 권한이 자동으로 부여됩니다. -->
        </div>

        <div class="authorityWrap">
            <div class="table-responsive overflow-auto">
                <table class="table table-sm dataTable table-fixed" id="roleListTable">
                    <thead class="thead-light">
                    <tr>
                        <th width="280"><spring:message code="account.menu.permission.menu" /></th><!-- 메뉴 -->
                        <th><spring:message code="account.menu.permission.permission" /></th><!-- 권한 -->
                    </tr>
                    </thead>

                    <tbody><!-- 캠페인 checkId,name, label 읽기 = c_r_n // 쓰기 = c_w_n-->
                    <c:forEach var="permissionRoleVo" items="${permissionList}" varStatus="status">
                    <input type="hidden" name="menuCd" value="${permissionRoleVo.menuCd}" />
                    <c:if test="${empty permissionRoleVo.pmenuCd}">
                    <tr onmouseout="this.style.backgroundColor='';"><!-- 상위목록 -->
                        <td class="th-bg text-left font-weight-bold">${permissionRoleVo.menuNm}</td>
                        <td class="th-bg text-left font-weight-bold">
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" id="PR_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="R"
                                    <c:if test="${permissionRoleVo.readAuth eq 'R'}">checked</c:if> class="custom-control-input" />
                                <label class="custom-control-label" for="PR_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.read" /></label><!-- 읽기 -->
                            </div>
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" id="PW_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="W"
                                    <c:if test="${permissionRoleVo.writeAuth eq 'W'}">checked</c:if> class="custom-control-input" />
                                <label class="custom-control-label" for="PW_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.write" /></label><!-- 쓰기 -->
                            </div>
                        </td>
                    </tr>
                    </c:if>

                    <c:if test="${!empty permissionRoleVo.pmenuCd}">
                    <tr onmouseover="this.style.backgroundColor='#F3F3F3';" onmouseout="this.style.backgroundColor='';"><!-- 하위목록 -->
                        <td class="text-left pl-5">${permissionRoleVo.menuNm}</td>
                        <td class="text-left">
                            <c:if test="${permissionRoleVo.menuCd ne '0301' and permissionRoleVo.menuCd ne '0302' and permissionRoleVo.menuCd ne '0305'}">
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" class="custom-control-input" id="R_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="R" <c:if test="${permissionRoleVo.readAuth eq 'R'}">checked</c:if>>
                                <label class="custom-control-label" for="R_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.read" /></label><!-- 읽기 -->
                            </div>
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" class="custom-control-input" id="W_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="W" <c:if test="${permissionRoleVo.writeAuth eq 'W'}">checked</c:if>>
                                <label class="custom-control-label" for="W_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.write" /></label><!-- 쓰기 -->
                            </div>
                            </c:if>

                            <c:if test="${permissionRoleVo.menuCd eq '0002'}">
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                                <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                            </div>
                            </c:if>

                            <c:if test="${permissionRoleVo.menuCd eq '0103'}">
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                                <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                            </div>
                            <input type="hidden" value="X_${permissionRoleVo.menuCd}">
                            </c:if>

                            <c:if test="${permissionRoleVo.menuCd eq '0202'}">
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                                <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                            </div>
                            <input type="hidden" value="X_${permissionRoleVo.menuCd}">
                            </c:if>

                            <c:if test="${permissionRoleVo.menuCd eq '0802'}">
                            <div class="custom-control custom-checkbox custom-control-inline">
                                <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                                <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                            </div>
                            <input type="hidden" value="X_${permissionRoleVo.menuCd}">
                            </c:if>
                        </td>
                    </tr>
                    </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div><!-- //권한보기 card body-->

    <div class="card-footer">
        <c:if test="${(isaRole eq 'M') or (isaRole eq 'A')}">
            <button type="button" class="btn btn-outline-primary" id="saveBtn">
                <spring:message code="button.save"/><!-- 저장 -->
            </button>
        </c:if>
    </div>
</div>
</form>
</body>
</html>
