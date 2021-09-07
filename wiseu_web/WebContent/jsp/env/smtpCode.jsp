<%-------------------------------------------------------------------------------------------------
 * - [환경설정>코드관리] 코드관리 <br/>
 * - URL : /env/smtpCode.do <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvSmtpCodeController  <br/>
 * - 이전 파일명 : env_smtpcode.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="env.msg.code"/></title>
<%@ include file="/jsp/include/pluginTree.jsp" %>
<script type="text/javascript">
    var tmplTree;

    $(document).ready(function() {
        initPage();
    });

    function initPage() {
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
                id : "01",
                text : "SMTP"
            },
            command : {
                child : {
                    url : "/env/smtpCodeTree.json"
                },
                select_node : {
                    callback : function(data) {
                        $.post("/env/smtpCodeList.do?categoryCd=" + data.node.id, null, function(result) {
                            $("#codeListDiv").html(result);
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
        <div class="card">
            <div class="card-header"><!-- title  -->
                <h3 class="mb-0"><spring:message code="env.msg.code"/></h3>
            </div>

            <div class="card-body">
                <div class="row treeWrap overflow-hidden" style="height: 675px;">
                    <div class="col-3 px-0"><!-- col-3  Card body 트리 -->
                        <div id="jstree" class="treeview_top treeview_basic"></div>
                    </div>
                    <div class="col-9 px-0"><!-- col-9  코드설명 -->
                        <div class="table-responsive" id="codeListDiv">
                        </div>
                    </div>
                </div>
            </div><!-- // card body -->
        </div><!-- // card -->
    </div>
</div>
</body>
</html>