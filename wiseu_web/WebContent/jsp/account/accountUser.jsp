<%-------------------------------------------------------------------------------------------------
 * - [사용자>사용자 관리] 사용자 관리 <br/>
 * - URL : /account/accountUser.do <br/>
 * - Controller :com.mnwise.wiseu.web.account.web.AccountUserController <br/>
 * - 이전 파일명 : account_user.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="account.msg.user"/></title><!-- 사용자관리 -->
<%@ include file="/jsp/include/pluginTree.jsp" %>
<script language="javascript">
    var tmplTree;

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 추가 버튼 클릭
        $("#addBtn").on("click", function(event) {
            if("${grpCd}" == "01") {
                alert("<spring:message code='account.alert.user.select1'/>");  // 부서를 선택하세요
                return;
            }

            popupUser();
        });

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            var userIds = "";
            $("input[name='checkUserList']:checkbox:checked").each(function() {
                if(userIds == "") {
                    userIds = $(this).val();
                } else {
                    userIds = userIds + "," + $(this).val();
                }
            });

            if(userIds == "") {
                alert("<spring:message code='account.alert.permission.select1'/>");  // 삭제할 대상자를 선택하세요.
                return;
            }

            if(!confirm("<spring:message code='common.alert.4'/>")) {  // 삭제 하시겠습니까?
                return;
            }

            accountForm.userId.value = userIds;
            accountForm.workDoc.value = $("#workDoc").val();

            var param = $.mdf.serializeObject('#accountForm');
            $.post("/account/deleteUser.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    alert(result.value + "<spring:message code='account.alert.user.update'/>");  // 명의 대상자에 대한 정보를 갱신하였습니다.
                    $("#workDoc").val("")
                    document.location.reload();
                }
            });
        });

        // 검색어 엔터키 입력
        $("#serchID").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            onClickSearchBtn($("#grpCd").val(), $("#serchID").val());
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
                                $("#tdGrpNm").text(node.grpNm);
                                $("#tdReqDeptCd").text(node.supragrpCd);
                                $("#grpCd").val(node.grpCd);
                                onClickSearchBtn(node.grpCd);
                                return;
                            }
                        });
                    }
                }
            },
            plugins : [ "search", "state", "types", "unique", "wholerow"]
        });
    }

    // 검색 버튼 클릭
    function onClickSearchBtn(grpCd, serchID) {
        var url = "/account/userList.do?grpCd=" + grpCd;
        if($.mdf.isNotBlank(serchID)) {
            url += "&serchID=" + serchID;
        }

        $.post(url, null, function(result) {  // /account/account_user_list.do
            $("#userListDiv").html(result);
        });
    }

    // 사용자 추가/수정 팝업창
    function popupUser(userId, mode) {
        var url = '/account/userPopup.do?grpCd=' + $("#grpCd").val();  // /account/account_user_popup.do
        if($.mdf.isNotBlank(userId)) {
            url += '&userId=' + userId;
        }

        if($.mdf.isNotBlank(mode)) {
            url += '&mode=' + mode;
        }

        $.mdf.popupGet(url, 'userPopup', 1000, 800);
    }

    // 로그인 시도 횟수 초기화
    function resetLoginCnt(userId) {
        if(confirm("<spring:message code='account.alert.user.reset.login'/>")) {  // 로그인 시도 횟수를 초기화 하시겠습니까?
            var param = {
                userId : userId
            };

            $.post("/account/resetLoginCnt.json", $.param(param, true), function(result) {
                if(result.code == "OK") {
                    alert("<spring:message code='account.alert.user.reset.unlock'/>");  // 잠금이 해제 되었습니다.
                    $('#status_'+userId).html('Active(0)');
                    $('#reset_'+userId).html('-');
                }
            });
        }
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="account.msg.user"/></h3><!-- 사용자관리 -->
            </div>

            <div class="card-body">
                <div class="container-fluid my-2">
                    <div class="row treeWrap overflow-hidden">
                        <!-- col-3  Card body 트리 -->
                        <div class="col-3 px-0">
                            <div id="jstree" class="treeview_top"></div><!-- ◀ 스크롤 제거시 class=" overflow-auto 제거" -->
                        </div>

                        <div class="col-9 px-0"><!-- col-9  부서정보 -->
                            <div class="overflow-auto">
                                <!-- ◀ 스크롤 제거시 class=" overflow-auto 제거" -->
                                <div class="table-responsive">
                                    <table class="table table-sm dataTable table-fixed">
                                        <thead class="thead-light">
                                        <tr>
                                            <th width="20%;" scope="col" class="border-right-0 border-top-0 control-text-sm text-left">
                                                <spring:message code="account.menu.dept.info"/><!--부서정보  -->
                                            </th>
                                            <th scope="col" class="border-right-0 border-top-0">
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <th class="th-bg text-right"><spring:message code="account.menu.dept.name"/></th><!--부서명  -->
                                            <td class="text-left control-text-sm" id="tdGrpNm"></td>
                                        </tr>

                                        <c:if test="${smsIndividualCharge eq 'on'}">
                                        <tr>
                                            <th class="th-bg text-right"><spring:message code="campaign.menu.reqdept"/></th><!-- 요청 부서코드 -->
                                            <td class="text-left control-text-sm" id="tdReqDeptCd"></td>
                                        </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div><!-- // col-9  부서정보 -->
                    </div><!-- // row -->
                </div>

                <h4 class="h3 text-primary mt-3 mb-0"><spring:message code="account.msg.userlist"/></h4><!-- 사용자 리스트 -->
                <div class="row align-items-center py-1 table_option">
                    <div class="col-6"><!-- buttons -->
                        <c:if test="${(UserVo.userRole eq 'M') or (UserVo.userRole eq 'A')}">
                            <button type="button" class="btn btn-sm btn-outline-primary btn-round" id="deleteBtn">
                                <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                            </button>
                            <button type="button" class="btn btn-sm btn-outline-primary btn-round" id="addBtn">
                                <i class="fas fa-plus"></i> <spring:message code="button.add"/><!-- 추가 -->
                            </button>
                        </c:if>
                    </div>
                    <form id="accountForm" name="accountForm" action="/account/userList.do" method="post"><!-- /account/account_user_list.do -->
                    <input type="hidden" name="grpCd" id="grpCd" />
                    <input type="hidden" name="userId" id="userId" />
                    <input type="hidden" name="makerId" id="makerId" value="${sessionScope.adminSessionVo.userVo.userId}"/>
                    <input type="hidden" name="workDoc" id="workDoc" />
                    <input type="hidden" name="userRole" id="userRole" value="${UserVo.userRole}"/>
                    <div class="col-6 searchWrap">
                        <div class="form-group searchbox search_input">
                            <c:if test="${serchYn ne 'N'}">
                                <input type="search" class="form-control form-control-sm" aria-controls="datatable-basic" id="serchID" maxlength="10" value="${param.serchID}" placeholder="<spring:message code='account.menu.user.id'/>"/>
                                <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </c:if>
                         </div>
                    </div>
                    </form>
                </div>
                <div class="table-responsive" id="userListDiv"><!-- 사용자 리스트-->
                </div>
            </div><!-- // card body -->
        </div><!-- // card -->
    </div>
</div>
</body>
</html>