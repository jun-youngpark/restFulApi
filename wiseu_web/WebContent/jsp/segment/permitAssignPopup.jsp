<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 조회] 대상자 권한할당(팝업) <br/>
 * - URL : /segment/permitAssignPopup.do <br/>
 * - Controller :com.mnwise.wiseu.web.segment.web.SegmentListController <br/>
 * - 이전 파일명 : permission.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="segment.menu.permission.choice"/></title>
<%@include file="/jsp/include/plugin.jsp"%>
<%@include file="/jsp/include/pluginTree.jsp"%>
<script type="text/javascript">
    var tmplTree;

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 선택 버튼 클릭
        $("#selectBtn").on("click", function(event) {
            var cnt = 0;
            // 1. seleted된것들 변수에 담아놓은다
            var arrSelected = $("#jstree").jstree(true).get_selected('full',true);
            //2. each 문을 사용해서 해당 아이템들의 id 값이나 text값을 오른쪽 div에 차례로 뿌려준다
            var grpCdArr = new Array();
            $.each(arrSelected, function(index, item) {
                grpCdArr.push(item.id);
                $.mdf.log(item.id);
                cnt++;
            });

            if(cnt==0) {
                alert('<spring:message code="segment.menu.permission.choice2"/>');  // 권한을 부여할 그룹을 선택하세요.
            } else {
                var param = {
                    segmentNo : '${segmentVo.segmentNo}',
                    grpCdArr : grpCdArr
                };
                $.post("/segment/savePermission.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        window.opener.location = '/segment/segmentList.do';  // /segment/segment.do
                        window.close();
                    }
                });
            }
        });
    }

    function initPage() {
        initTree();
    }

    function initTree() {
        $("#jstree").jstree({
            "checkbox" : {
                "keep_selected_style" : false
            },
            "core" : {
                "data" : {
                    url : function(node) {
                        if(node.id == "#") {
                            return "/segment/permissionTree.json?segmentNo=${segmentVo.segmentNo}";
                        } else {
                            return "/segment/permissionTree.json?segmentNo=${segmentVo.segmentNo}&grpCd="+node.id;
                        }
                    },
                    data : function(node) {
                        return {id : node.id};
                    }
                }
            },
            "plugins" : [ "checkbox",   "wholerow"  ]
        });
    }
</script>
</head>

<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="segment.menu.permission.choice"/></h5></div><!-- 부서 선택 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <div class="treeWrap tree-high"><!-- 트리 -->
            <div id="jstree" class="treeview_top overflow-auto"></div>
         </div>
    </div>

    <div class="card-footer border-top-0 mt-0">
        <button type="button" class="btn btn-outline-primary" id="selectBtn">
            <spring:message code="button.select"/><!-- 선택 -->
        </button>
    </div>
</div>
</body>
</html>