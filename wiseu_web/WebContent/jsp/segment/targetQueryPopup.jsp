<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 조회>쿼리 대상자 조회(step1)] 대상자 쿼리 SQL 수정(팝업)
 * - URL : /segment/targetQueryPopup.do
 * - Controller :com.mnwise.wiseu.web.segment.web.Segment1StepController
 * - 이전 파일명 : sqlModify_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="segment.menu.sql.query"/></title><!-- 대상자 쿼리 -->
<%@ include file="/jsp/include/plugin.jsp"%>
<script language="javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
            // Head 에 전체 쿼리가 다 들어갈 수 있으므로, sqlHead만  체크한다.
            if($("#sqlHead").val() == '') {
                alert('<spring:message code="segment.alert.msg.modify.1"/>');  // sqlHead를 입력해 주세요
                return;
            }
            if(!confirm('<spring:message code="segment.alert.msg.modify.2"/>')) {  // 수정하시겠습니까?
                return;
            }
            $('#segmentPopupFrm').submit();
            opener.location.reload();
        });
    }
</script>
</head>
<body>
    <form id="segmentPopupFrm" name="segmentPopupFrm" action="/segment/updateTargetQuery.do" method="post">
    <input type="hidden" name="segmentNo" id="segmentNo" value="${segmentNo}" />
        <div class="card pop-card">
            <div class="card-header"><!-- title -->
                <div class="row table_option">
                    <div class="col-10"><h5 class="mb-0"><spring:message code="segment.menu.sql.query"/></h5></div><!-- 대상자 쿼리 -->
                    <div class="col-2 justify-content-end">
                        <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                    </div><!-- 닫기 -->
                </div>
            </div>

            <div class="card-body">
                <!-- 대상자 쿼리 수정-->
                <div>
                <h2 class="h5"><spring:message code="segment.menu.sql.target"/> : ${segmentVo.segmentNm}</h2><!-- 대상자명 -->
                    <h2 class="h5 text-primary">SQL HEADER (<spring:message code="segment.menu.sql.popup.modifyquery"/>)</h2><!-- 수정이 불가합니다 -->
                    <div class="form-group">
                        <textarea id="sqlHead" name="sqlHead" class="form-control" cols="10" readonly="readonly" style="height:85px;">${segmentVo.sqlHead}</textarea>
                    </div>
                    <h2 class="h5 text-primary">SQL BODY</h2>
                    <div class="form-group">
                        <textarea id="sqlBody" name="sqlBody" class="form-control" cols="10" style="height:85px;">${segmentVo.sqlBody}</textarea>
                    </div>
                    <h2 class="h5 text-primary">SQL TAIL</h2>
                    <div class="form-group">
                        <textarea id="sqlTail" name="sqlTail" class="form-control" cols="10" style="height:85px;">${segmentVo.sqlTail}</textarea>
                    </div>
                </div>
                <!-- //대상자 쿼리 수정-->
            </div>

            <div class="card-footer">
                <button type="button" class="btn btn-outline-primary" id="saveBtn">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div>
        </div>
    </form>
</body>
</html>