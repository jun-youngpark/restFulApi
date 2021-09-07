<%-------------------------------------------------------------------------------------------------
 * - [사용자 관리>부서 관리] 부서 관리 <br/>
 * - URL : /account/accountDept.do <br/>
 * - Controller :com.mnwise.wiseu.web.account.web.AccountDeptController <br/>
 * - 최대부서생성갯수 제한하는 조건이 맞지 않아 오류 수정 (supGrpCd.length == 12 -> 11로 변경)
 * - 이전 파일명 : account_dept.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="account.msg.dept"/></title>
<%@ include file="/jsp/include/pluginTree.jsp" %>
<script type="text/javascript">
    var tmplTree;

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 등록 버튼 클릭
        $("#registBtn").on("click", function(event) {
            // 선택한 부서의 코드가 상위부서 코드가 된다.
            var supGrpCd = $("#grpCd").val();
            if(supGrpCd.length == 11) {
                alert('<spring:message code="account.alert.dept.max"/>');  // 부서는 최대 5개까지만 만들 수 있습니다.
                return;
            }

            var url = "/account/deptPopup.do?supraGrpCd="+supGrpCd+"&userRole=${UserVo.userRole}";
            $.mdf.popupGet(url, 'deptPopup', 510, 290);  // /account/account_dept_popup.do
        });

        // 수정 버튼 클릭
        $("#modifyBtn").on("click", function(event) {
            var url = "/account/deptPopup.do?grpCd=" + $("#grpCd").val();
            $.mdf.popupGet(url, 'deptPopup', 510, 290);  // /account/account_dept_popup.do
        });

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            var supGrpCd = $("#supragrpCd").val();
            if($.mdf.isBlank(supGrpCd)) {
                alert("<spring:message code='account.alert.dept.nodel'/>");  // 최상위 부서는 삭제할 수 없습니다.
                return;
            }

            if(!confirm("<spring:message code='account.alert.dept.del1'/>")) {  // 부서를 삭제하시겠습니까?
                return;
            }

            var param = $.mdf.serializeObject('#deptFrm');
            $.post("/account/deleteUserGrp.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    alert("<spring:message code='account.alert.dept.del'/>");  // 부서가 정상적으로 삭제되었습니다.
                    document.location.href = "/account/accountDept.do?grpCd="+supGrpCd;  // /account/account_dept.do
                } else {
                    alert("<spring:message code='account.alert.dept.faildel'/>");  // 하위부서가 존재하거나 부서원이 존재하여 부서가 삭제되지 않았습니다.
                }
            });
        });
    }

    function initPage() {
        initTree();
    }

    // 부서 트리 만들기
    function initTree() {
        var theTree = tmplTree;
        if(tmplTree) {
            tmplTree.unbindAllEvent();
        }

        tmplTree = new mdf.Tree("#jstree", {
            context : "",
            root_node_id : "01",
            data : {
                id : "01",
                text : "mnwise"
            },
            command : {
                child : {
                    url : "/account/account_dept.json",
                    param : "grpCd"
                },
                select_node : {
                    callback : function(data) {
                        $.ajax({
                            url: '/account/get_account_dept.json',
                            type : "POST",
                            data : {grpCd : data.node.id},
                            dataType : 'json',
                            success : function(node) {
                                selectCallback(node);
                                return;
                            }
                        });
                    }
                }
            },
            plugins : [ "search", "state", "types", "unique", "wholerow"]
        });
    }

    // 선택한 부서 정보를 보여준다
    function selectCallback(result) {
        $("#grpCd").val(result.grpCd);
        $("#grpNm").val(result.grpNm);
        if(result.supraGrpCd != null) {
            $("#supragrpCd").val(result.supraGrpCd);
            $("#grid_supraGrpNm").text(result.supraGrpNm);
        }
        $("#grid_grpNm").text(result.grpNm);
        $("#grid_reqDeptCd").text(result.supragrpCd);
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <form id="deptFrm" name="deptFrm" method="post" action="/account/accountDept.do"><!-- /account/account_dept.do -->
        <input type="hidden" name="grpCd" id="grpCd" value="${userGrpVo.grpCd}"/>
        <input type="hidden" name="grpNm" id="grpNm" value="${userGrpVo.grpNm}"/>
        <input type="hidden" name="userRole" id="userRole" value="${UserVo.userRole}" />
        <input type="hidden" name="supragrpCd" id="supragrpCd" value="${userGrpVo.supraGrpCd}"/>
        <input type="hidden" name="cmd" id="cmd" />

        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="account.msg.dept"/></h3>
            </div>
            <div class="card-body">
                <div class="container-fluid my-2" >
                    <div class="row treeWrap tree-high">
                        <div class="col-3 px-0">
                            <div id="jstree" class="treeview_top"></div><!-- ◀ 스크롤 제거시 class=" overflow-auto 제거" -->
                        </div>
                        <!-- col-9  부서정보 -->
                        <div class="col-9 px-0">
                            <div class="overflow-auto">
                                <div class="table-responsive">
                                    <table class="table table-sm dataTable table-fixed">
                                        <thead class="thead-light">
                                        <tr>
                                            <th width="20%;" scope="col" class="border-right-0 border-top-0 text-left">
                                                <spring:message code="account.menu.dept.sgrp"/><!-- 부서정보 -->
                                            </th>
                                            <th scope="col" class="justify-content-end mr-1 border-top-0">
                                                <c:if test="${(UserVo.userRole eq 'M') or (UserVo.userRole eq 'A')}">
                                                    <button type="button" class="btn btn-sm btn-outline-primary btn-round" id="deleteBtn">
                                                        <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                                                    </button>
                                                    <button type="button" class="btn btn-sm btn-outline-primary btn-round" id="modifyBtn">
                                                        <i class="fas fa-edit"></i> <spring:message code="button.modify"/><!-- 수정 -->
                                                    </button>
                                                    <button type="button" class="btn btn-sm btn-outline-primary btn-round" id="registBtn">
                                                        <i class="fas fa-save"></i> <spring:message code="button.regist"/><!-- 등록 -->
                                                    </button>
                                                </c:if>
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <th class="th-bg text-right"><spring:message code="account.menu.dept.sgrp"/><!-- 상위부서 --></th>
                                            <td class="text-left control-text-sm" id="grid_supraGrpNm"></td>
                                        </tr>
                                        <tr>
                                            <th class="th-bg text-right"><spring:message code="account.menu.dept.name"/><!-- 부서명 --></th>
                                            <td class="text-left control-text-sm" id="grid_grpNm"></td>
                                        </tr>

                                        <c:if test="${userGrpVo.smsIndividualCharge eq 'on'}">
                                        <tr>
                                            <th class="th-bg text-right"><spring:message code="campaign.menu.reqdept"/><!-- 요청부서 --></th>
                                            <td class="text-left control-text-sm" id="grid_reqDeptCd"></td>
                                        </tr>
                                        </c:if>
                                       </tbody>
                                   </table>
                               </div>
                           </div>
                       </div><!-- // col-9  부서정보 -->
                    </div>
                </div>
            </div><!-- // card body -->
        </div><!-- // card -->
        </form>
    </div>
</div>
</body>
</html>