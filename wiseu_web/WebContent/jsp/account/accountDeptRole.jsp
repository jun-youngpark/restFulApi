<%-------------------------------------------------------------------------------------------------
 * - [사용자 관리>부서 권한 관리] 부서 권한 관리 <br/>
 * - URL : /account/accountDeptRole.do <br/>
 * - Controller : com.mnwise.wiseu.web.account.web.AccountDeptPermissionController
 * - 이전 파일명 : account_dept_permission.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="account.msg.permission" /></title>
<%@ include file="/jsp/include/pluginTree.jsp" %>
<script type="text/javascript">
    var tmplTree;

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 권한 리스트 체크박스 이벤트
        // 승인 체크박스 클릭시 이벤트
        $(document).on("click", "input[id*=X_]", function(event) {
            var idx = this.id;
            idx = idx.substr(2);
            var stat = this.checked;
        });

        // 쓰기 체크박스 클릭시 이벤트
        $(document).on("click", "input[id*=W_]", function(event) {
            var idx = this.id;
            idx = idx.substr(2);
            var stat = this.checked;
            if(stat == false) {
                idx = idx.substr(0, idx.length - 2);
                $("input[id*=PW_" + idx + "]").each(function() {
                    this.checked = false;
                });
            } else {
                $("#R_" + idx).attr('checked', true);
            }
        });

        // 읽기 체크박스 클릭시 이벤트
        $(document).on("click", "input[id*=R_]", function(event) {
            var idx = this.id;
            idx = idx.substr(2);
            var stat = this.checked;
            if(stat == false) {
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
        $(document).on("click", "input[id*=P]", function(event) {
            var idx = this.id;
            var ch = idx.substr(1, 1);
            var n = ch == 'R' ? 1 : 2;
            idx = idx.substr(3);
            var stat = this.checked;
            if(stat == true) {
                switch (n) {
                case 2:
                    checking("W_" + idx, stat);
                case 1:
                    checking("R_" + idx, stat);
                }
            } else {
                switch (n) {
                case 1:
                    checking("R_" + idx, stat);
                case 2:
                    checking("W_" + idx, stat);
                }
            }
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if($("#permissionListSize").val() == 0) {
                alert('<spring:message code="account.alert.permission.dept"/>');  // 권한을 설정 할 수 없는 최상위 부서입니다. 하위 부서를 선택하세요.
                return;
            }

            if(!confirm("<spring:message code='account.alert.permission.req'/>")) {  // 권한변경 요청을 하시겠습니까?
                return;
            }

            var param = $.mdf.serializeObject('#deptFrm');

            $.post("/account/updateDeptPermission.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    alert("<spring:message code='account.alert.permission.save'/>");  // 권한 리스트가 저장되었습니다.
                    window.location.reload();
                }
            });
        });
    }

    function initPage() {
        initTree();
    }


    // 권한 리스트 체크박스 관련 이벤트
    function checking(idx, stat) {
        $("input[id*=" + idx + "]").each(function() {
            this.checked = stat;
        });
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
            types: {
                "INTERNAL": {
                    "icon": "/assets/img/ico_tree_group.png"
                }
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

    //선택한 부서 정보를 보여준다
    function selectCallback(result) {
        if(result.supraGrpCd != null) {
            $("#grid_supraGrpNm").text(result.supraGrpNm);
        }
        $("#grid_grpNm").text(result.grpNm);
        $("#grid_reqDeptCd").text(result.supragrpCd);

        $.post("/account/deptRoleList.do?grpCd=" + result.grpCd, null, function(result) {  // /account/account_dept_permission_list.do
            $("#roleListDiv").html(result);
        });
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="account.msg.permission" /></h3>
            </div>

            <div class="card-body">
                <div class="container-fluid my-2">
                    <div class="row treeWrap overflow-hidden">
                        <div class="col-3 px-0">
                            <div id="jstree" class="treeview_top"></div><!-- ◀ 스크롤 제거시 class=" overflow-auto 제거" -->
                        </div>
                        <!-- col-9  부서정보 -->
                        <div class="col-9 px-0" >
                            <div class="overflow-auto" >
                                <div class="table-responsive">
                                    <table class="table table-sm dataTable">
                                        <thead class="thead-light">
                                        <tr class="border-right-0 border-top-0 control-text-sm text-left">
                                            <th scope="col" colspan="2" class="border-top-0 text-left"><spring:message code="account.menu.dept.info" /></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <th width="20%;" class="th-bg text-right"><spring:message code="account.menu.dept.sgrp"/><!-- 상위부서 --></th>
                                            <td class="text-left control-text-sm" id="grid_supraGrpNm"></td>
                                        </tr>
                                        <tr>
                                            <th class="th-bg text-right"><spring:message code="account.menu.dept.name"/><!-- 부서명 --></th>
                                            <td class="text-left control-text-sm" id="grid_grpNm"></td>
                                        </tr>

                                        <c:if test="${smsIndividualCharge eq 'on'}">
                                        <tr>
                                            <th class="th-bg text-right"><spring:message code="campaign.menu.reqdept"/><!-- 요청부서 --></th>
                                            <td class="text-left control-text-sm" id="grid_reqDeptCd"></td>
                                        </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <!-- // col-9  부서정보 -->
                    </div>
                </div>
            </div><!-- // card body -->

            <h4 class="h3 text-primary ml-2 mb-0"><spring:message code='account.menu.permission.authority' /></h4><!-- 권한리스트 -->
            <div id="roleListDiv" class="authorityWrap"></div>

            <div class="card-footer pb-0 btn_area"><!-- button area -->
                <button type="button" class="btn btn-outline-primary" id="saveBtn">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div>
        </div><!-- // card -->
    </div>
</div>
</iframe>
</body>
</html>
