<%-------------------------------------------------------------------------------------------------
 * - [환경설정>PUSH 설정] APP 관리
 * - URL : /env/pushSetUp.do
 * - Controller : com.mnwise.wiseu.web.push.web.PushController
 * - 이전 파일명 : env_push_setting.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<% // 캐시 없애기(새로고침 중복전송 방지)
response.setHeader("Pragma","no-cache");
response.addHeader("Cache-Control","no-store");
response.setDateHeader("Expires",0);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="env.msg.pushapp"/></title><!-- PUSH 앱 관리 -->
<%@ include file="/jsp/include/pluginTree.jsp" %>
<script type="text/javascript">
    var tmplTree;

    $(document).ready(function() {
        initEventBind();
        addValidRule();
        initPage();
    });

    function initEventBind() {
        // (APP) 추가 버튼 클릭
        $("#addAppBtn").on("click", function(event) {
            event.preventDefault();

            $("#addAppBtn").hide();
            $("#addAppTr").show();
        });

        // (APP) 저장 버튼 클릭
        $("button[name=saveAppBtn]").each(function(i) {
            $(this).on('click', function(event) {
                event.preventDefault();

                if($.mdf.validForm("#updateAppForm") == false) {
                    return;
                }

                if(!confirm('<spring:message code="common.alert.3" />')) {  // 저장 하시겠습니까?
                    return;
                }

                var workType = $("#updateAppForm input[name='workType[" + i + "]']").val();
                if(workType == "update") {
                    $('#appForm input[name=USE_YN]').val($("#updateAppForm input:checkbox[name='useYn[" + i + "]']").is(":checked"));
                    $('#appForm input[name=USE_TEST_MODE]').val($("#updateAppForm input:checkbox[name='useTestMode[" + i + "]']").is(":checked"));
                }

                $('#appForm input[name=workType]').val(workType);
                $('#appForm input[name=PUSH_APP_ID]').val($("#updateAppForm input[name='appId[" + i + "]']").val());
                $('#appForm input[name=PUSH_APP_NM]').val($("#updateAppForm input[name='appNm[" + i + "]']").val());
                $('#appForm').submit();
            });
        });

        // (APP) 삭제 버튼 클릭
        $("#deleteAppBtn").on("click", function(event) {
            event.preventDefault();

            $("#addAppBtn").show();
            $("#addAppTr").hide();
        });

        // (메시지 유형) 추가 버튼 클릭
        $('#addMsgTypeBtn').on("click", function(event) {
            event.preventDefault();

            $("#addMsgTypeBtn").hide();
            $("#addMsgTypeTr").show();
        });

        // (메시지 유형) 저장 버튼 클릭
        $("button[name=saveMsgTypeBtn]").each(function(i) {
            $(this).on('click', function(event) {
                event.preventDefault();

                if($.mdf.validForm("#updateMsgTypeForm") == false) {
                    return;
                }

                if(!confirm('<spring:message code="common.alert.3" />')) {  // 저장 하시겠습니까?
                     return;
                }

                var workType = $("#updateMsgTypeForm input[name='workType[" + i + "]']").val();
                if(workType == "update") {
                    $('#msgTypeForm input[name=USE_YN]').val($("#updateMsgTypeForm input:checkbox[name='msgUseYn[" + i + "]']").is(":checked"));
                }

                $('#msgTypeForm input[name=workType]').val(workType);
                $('#msgTypeForm input[name=CD]').val($("#updateMsgTypeForm input[name='msgTypeCd[" + i + "]']").val());
                $('#msgTypeForm input[name=CD_DESC]').val($("#updateMsgTypeForm input[name='msgTypeDesc[" + i + "]']").val());
                $('#msgTypeForm').submit();
            });
        });

        // (메시지 유형) 삭제 버튼 클릭
        $("#deleteMsgTypeBtn").on("click", function(event) {
            event.preventDefault();

            $("#addMsgTypeBtn").show();
            $("#addMsgTypeTr").hide();
        });
    }

    function addValidRule() {
        $("#updateAppForm").validate();

        $("#updateAppForm input[name*='appId']").each(function() {
            $(this).rules("add", {notBlank: true, digits : true, maxlength: 20});
        });

        $("#updateAppForm input[name*='appNm']").each(function() {
            $(this).rules("add", {notBlank: true});
        });

        $("#updateMsgTypeForm").validate();

        $("#updateMsgTypeForm input[name*='msgTypeCd']").each(function() {
            $(this).rules("add", {notBlank: true, alphaDigit : true});
        });

        $("#updateMsgTypeForm input[name*='msgTypeDesc']").each(function() {
            $(this).rules("add", {notBlank: true});
        });
    }

    function initPage() {
        if($.mdf.isNotBlank('${workType}') && $.mdf.isNotBlank('${resultCnt}')) {
            if(0 < parseInt('${resultCnt}')) {
                alert('<spring:message code="common.alert.1" />');  // 저장 되었습니다.
            } else {
                alert('<spring:message code="common.alert.10" />');  // 변경된 정보가 없습니다.\\n확인 후 다시 시도바랍니다.
            }
        }

        initTree();
    }

    function initTree() {
        var theTree = tmplTree;
        if(tmplTree) {
            tmplTree.unbindAllEvent();
        }

        tmplTree = new mdf.Tree("#jstree", {
            context : "",
            root_node_id : "01",
            data : {
                id : "AP00",
                text : "AP00"
            },
            command : {
                child : {
                    url : "/env/codeTree/env_push_setting.json"
                },
                select_node : {
                    callback : function(data) {
                        $.ajax({
                            url: '/env/selectPushCodeList.json',
                            type : "POST",
                            data : {cdCat : data.node.id},
                            dataType : 'json',
                            success : function(result) {
                                if(result.flag > 0) {    // success
                                    $('#treeDatabody').empty();
                                    $(result.codeList).each(function(i, row) {
                                        $("#treeDatabody").append("<tr><td>" + row.cd + "</td><td class='text-left'>" + row.val + "</td></tr>");
                                    });
                                }
                            }
                        });
                    }
                }
            },
            plugins : [ "search", "state", "types", "unique", "wholerow"]
        });
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card mb-4">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="env.title.push.setting" /></h3><!-- PUSH 설정 -->
            </div>

            <div class="card-body"><!-- APP 관리 -->
                <h1 class="h3 text-primary mb-1"><spring:message code="env.subtitle.push.app.management" /></h1><!-- APP 관리 -->
                <div class="alert alert-secondary mb-0" role="alert">
                    <i class="fas fa-exclamation-circle"></i> <spring:message code="env.help.push.app.1" /><!-- 알림을 받을 APP ID는 <b>PUSH 시스템</b>에 미리 등록해야 합니다. -->
                </div>
                <div class="table-responsive">
                    <form id="updateAppForm" name="updateAppForm">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="25%" />
                            <col width="*" />
                            <col width="120" />
                            <col width="120" />
                            <col width="15%" />
                        </colgroup>
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" class="control-text-sm"><spring:message code="env.menu.header.app.id" /></th><!-- APP ID -->
                            <th scope="col"><spring:message code="env.menu.header.app.desc" /></th><!-- APP Description -->
                            <th scope="col"><spring:message code="env.menu.header.use" /> </th><!-- 사용 여부 -->
                            <th scope="col"><spring:message code="env.menu.header.ios.test.mode" /></th><!-- iOS 테스트 모드 -->
                            <th scope="col">
                                <button id="addAppBtn" class="btn btn-sm btn-outline-primary btn-round">
                                    <i class="fas fa-plus"></i> <spring:message code="button.add" /><!-- 추가 -->
                                </button>
                            </th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach var="app" items="${pushAppList}" varStatus="status">
                        <input type="hidden" name="workType[${status.index}]" value="update">
                        <tr>
                            <td><input class="form-control form-control-sm" value="${app.PUSH_APP_ID}" type="text" name="appId[${status.index}]" disabled="disabled"/></td>
                            <td class="text-left"><input class="form-control form-control-sm" value="${app.PUSH_APP_NM}" maxlength="50" type="text" name="appNm[${status.index}]"/></td>
                            <td>
                                <div class="custom-toggle-wrap use-toogle">
                                    <label class="custom-toggle">
                                        <input type="checkbox" name="useYn[${status.index}]" <c:if test="${app.USE_YN eq 'Y'}">checked</c:if>/>
                                        <span class="custom-toggle-slider rounded-circle" data-label-off="No" data-label-on="Yes"></span>
                                    </label>
                                </div>
                            </td>
                            <td>
                                <div class="custom-toggle-wrap">
                                    <label class="custom-toggle">
                                        <input type="checkbox" name="useTestMode[${status.index}]" <c:if test="${app.USE_TESTMODE eq 'Y' }">checked</c:if>/>
                                        <span class="custom-toggle-slider rounded-circle" data-label-off="No" data-label-on="Yes"></span>
                                    </label>
                                </div>
                            </td>
                            <td>
                                <button name="saveAppBtn" class="btn btn-sm btn-outline-primary btn-round mx-0">
                                    <i class="fas fa-save"></i> <spring:message code="button.save"/><!-- 저장 -->
                                </button>
                            </td>
                        </tr>
                        </c:forEach>
                        <c:set var="nextIndex" value="${pushAppList == null ? 0 : pushAppList.size()}" />
                        <input type="hidden" name="workType[${nextIndex}]" value="insert">
                        <tr id="addAppTr" class="dp-none">
                            <td><input class="form-control form-control-sm" maxlength="20" type="text" name="appId[${nextIndex}]"></td>
                            <td><input class="form-control form-control-sm" maxlength="50" type="text" name="appNm[${nextIndex}]"> </td>
                            <td colspan="2" class="text-center"><spring:message code="common.button.use" /></td><!-- 사용 -->
                            <td>
                                <button class="btn btn-sm btn-outline-primary btn-round" name="saveAppBtn">
                                    <i class="fas fa-save"></i> <spring:message code="button.save"/><!-- 저장 -->
                                </button>
                                <button class="btn btn-sm btn-outline-danger btn-round" id="deleteAppBtn">
                                    <i class="fas fa-minus"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    </form>
                </div><!-- //Light table -->
            </div><!-- //card body -->

            <div class="card-body mt-3"><!-- 메세지 유형관리 -->
                <h1 class="h3 text-primary mb-1"><spring:message code="env.subtitle.messge.type.management" /></h1><!-- 메시지 유형 관리 -->
                <div class="alert alert-secondary mb-0" role="alert">
                    <i class="fas fa-exclamation-circle"></i> <spring:message code="env.help.push.message.type.1" /><!-- 알림 메시지 유형은 <b>PUSH 시스템</b>에 미리 등록해야 합니다. -->
                </div>
                <div class="table-responsive">
                    <form id="updateMsgTypeForm" name="updateMsgTypeForm">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="25%" />
                            <col width="*" />
                            <col width="120" />
                            <col width="15%" />
                        </colgroup>
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" class="control-text-sm"><spring:message code="env.menu.header.id" /></th><!-- ID -->
                            <th scope="col"><spring:message code="env.menu.header.message.type" /></th><!-- 메시지 유형 -->
                            <th scope="col"><spring:message code="env.menu.header.use" /> </th><!-- 사용 여부 -->
                            <th scope="col" class="control-text-sm">
                                <button id="addMsgTypeBtn" class=" btn btn-sm btn-outline-primary btn-round">
                                    <i class="fas fa-plus"></i> <spring:message code="button.add" /><!-- 추가 -->
                                </button>
                            </th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach var="msgType" items="${msgTypeList}" varStatus="status">
                        <input type="hidden" name="workType[${status.index}]" value="update">
                        <tr>
                            <td><input class="form-control form-control-sm" value="${msgType.CD}" type="text" name="msgTypeCd[${status.index}]" disabled="disabled"/></td>
                            <td class="text-left"><input class="form-control form-control-sm" value="${msgType.CD_DESC}" type="text" name="msgTypeDesc[${status.index}]" disabled="disabled"/></td>
                            <td>
                                <div class="custom-toggle-wrap use-toogle">
                                    <label class="custom-toggle">
                                        <input type="checkbox" name="msgUseYn[${status.index}]" <c:if test="${msgType.USE_YN eq 'y'}">checked</c:if>/>
                                        <span class="custom-toggle-slider rounded-circle" data-label-off="No" data-label-on="Yes"></span>
                                    </label>
                                </div>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary btn-round" name="saveMsgTypeBtn">
                                    <i class="fas fa-save"></i> <spring:message code="button.save"/><!-- 저장 -->
                                </button>
                            </td>
                        </tr>
                        </c:forEach>
                        <c:set var="nextIndex" value="${msgTypeList == null ? 0 : msgTypeList.size()}" />
                        <input type="hidden" name="workType[${nextIndex}]" value="insert">
                        <tr id="addMsgTypeTr" class="dp-none">
                            <td><input class="form-control form-control-sm" type="text" name="msgTypeCd[${nextIndex}]" maxlength="2"></td>
                            <td><input class="form-control form-control-sm" type="text" name="msgTypeDesc[${nextIndex}]" maxlength="50"></td>
                            <td><spring:message code="common.button.use" /></td><!-- 사용 -->
                            <td>
                                <button class="btn btn-sm btn-outline-primary btn-round" name="saveMsgTypeBtn">
                                    <i class="fas fa-save"></i> <spring:message code="button.save"/><!-- 저장 -->
                                </button>
                                <button class="btn btn-sm btn-outline-danger btn-round" id="deleteMsgTypeBtn">
                                    <i class="fas fa-minus"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    </form>
                </div><!-- //Light table -->
            </div><!-- //card body -->

            <div class="card-body mb-3">
                <h1 class="h3 text-primary mb-1"><spring:message code="env.msg.code" /></h1><!-- 코드 관리 -->
                <div class="container-fluid my-2">
                    <div class="row treeWrap tree-high">
                        <div class="col-3 pr-0"><!-- 트리 -->
                            <div id="jstree" class="treeview_top treeview_basic"></div>
                        </div>
                        <div class="col-9 pl-0  overflow-auto"><!-- col-9  코드설명 -->
                            <div class="table-responsive" class="overflow-auto" >
                                <table class="table table-sm dataTable table-fixed" style="margin-top: -1px !important;">
                                    <thead class="thead-light">
                                    <tr>
                                        <th scope="col" width="100"><spring:message code="env.menu.header.code" /></th><!-- 코드 -->
                                        <th scope="col"><spring:message code="env.menu.header.description" /></th><!-- 설명 -->
                                    </tr>
                                    </thead>
                                    <tbody id="treeDatabody" class="list">
                                </table>
                            </div>
                        </div><!-- // col-9  코드설명 -->
                    </div><!-- // row -->
                </div>
            </div><!-- // 코드 관리 -->
        </div><!--e.container-fluid-->
    </div><!-- e.page content -->
</div>

<form name="msgTypeForm" id="msgTypeForm" method="post" action="/env/pushSetUp.do"><!-- /env/env_push_setting.do -->
    <input type="hidden" name="workType" />
    <input type="hidden" name="CD_DESC" />
    <input type="hidden" name="CD" />
    <input type="hidden" name="USE_YN" />
    <input type="hidden" name="type" value="msg" />
</form>

<form name="appForm" id="appForm" method="post" action="/env/pushSetUp.do"><!-- /env/env_push_setting.do -->
    <input type="hidden" name="workType" />
    <input type="hidden" name="PUSH_APP_ID" />
    <input type="hidden" name="PUSH_APP_NM" />
    <input type="hidden" name="USE_TEST_MODE" />
    <input type="hidden" name="USE_YN" />
    <input type="hidden" name="type" value="app" />
</form>
</body>
</html>