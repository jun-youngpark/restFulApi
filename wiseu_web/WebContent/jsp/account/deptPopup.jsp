<%-------------------------------------------------------------------------------------------------
 * - [사용자 관리>부서 관리] 부서 등록(팝업), 부서 수정(팝업) <br/>
 * - URL : /account/deptPopup.do <br/>
 * - Controller :com.mnwise.wiseu.web.account.web.AccountDeptController <br/>
 * - 이전 파일명 : account_dept_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="account.msg.dreg"/></title>
<%@ include file="/jsp/include/plugin.jsp"%>
<script language="javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                grpNm     : {notBlank : true},
                reqDeptCd : {notBlank : true}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var isInsertWork = $.mdf.isBlank("${userGrpVo.grpCd}");  // 등록작업 여부(true:등록, false:수정)
            var message = isInsertWork ? "<spring:message code='common.alert.3'/>" : "<spring:message code='common.alert.6'/>";  // 저장 하시겠습니까? : 수정하시겠습니까?
            if(confirm(message)) {
                var url = isInsertWork ? "/account/insertUserGrp.json" : "/account/updateUserGrp.json";
                var param = $.mdf.serializeObject('#updateForm');
                $.post(url, $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        if(isInsertWork) {
                            alert("<spring:message code='common.alert.1'/>");  // 저장 되었습니다.
                        } else if(result.message == 'update') {
                            alert("<spring:message code='common.alert.5'/>");  // 수정되었습니다.
                        }

                        opener.location.href="/account/accountDept.do?grpCd=" + result.value;  // /account/account_dept.do
                        window.close();
                    }
                });
            }
        });
    }
</script>
</head>
<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="account.menu.dept.info"/></h5></div><!-- 부서정보 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>
    <div class="card-body">
        <form id="updateForm" name="updateForm" method="post"/>
        <input type="hidden" name="grpCd" value="${userGrpVo.grpCd}" />
        <input type="hidden" name="supraGrpCd" value="${userGrpVo.supraGrpCd}" />
        <input type="hidden" name="userRole" value="${userRole}" />
        <input type="hidden" name="funcCode" />
        <input type="hidden" name="funcDesc" />
        <input type="hidden" name="workDoc" value="by Admin"/>
        <input type="hidden" name="smsIndividualCharge" value="${userGrpVo.smsIndividualCharge}"/>
        <div class="table-responsive gridWrap">
            <table class="table table-sm dataTable table-fixed">
                <colgroup>
                    <col width="30%" /><!-- en:width -->
                    <col width="*" />
                </colgroup>
                <tbody>
                    <tr>
                        <th scope="row"><spring:message code="account.menu.dept.sgrp"/></th><!-- 상위부서-->
                        <td>${userGrpVo.supraGrpNm}</td>
                    </tr>
                    <tr>
                        <th scope="row"><em class="required">required</em><spring:message code="account.menu.dept.name"/></th><!-- 부서명 -->
                        <td><input class="form-control form-control-sm" type="text" name="grpNm" maxlength="15" value="${userGrpVo.grpNm}"></td>
                    </tr>
                     <c:if test="${userGrpVo.smsIndividualCharge eq 'on'}">
                    <tr>
                        <th scope="row"><spring:message code="campaign.menu.reqdept"/></th><!-- 요청부서  -->
                        <td>
                            <input class="form-control form-control-sm" type="text" name="reqDeptCd" maxlength="50" value="${userGrpVo.reqDeptCd}"/>
                        </td>
                    </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        </form>
    </div>
    <div class="card-footer">
        <button type="button" class="btn btn-outline-primary" id="saveBtn" >
            <spring:message code="button.save"/><!-- 저장 -->
        </button>
    </div>
</div>
</body>
</html>
