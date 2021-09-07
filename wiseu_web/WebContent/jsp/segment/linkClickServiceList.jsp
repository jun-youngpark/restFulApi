<%-------------------------------------------------------------------------------------------------
 * [캠페인>캠페인 등록>메시지 작성>대상자 등록(팝업)>반응대상자 탭] 반응대상자 목록
 * - URL : /target/linkClickServiceList.do <br/>
 * - Controller : com.mnwise.wiseu.web.segment.linkclick.web.LinkclickController <br/>
 * - 이전 파일명 : service_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<title><spring:message code="segment.msg.reaction"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    window.name = "upload";
    var saveYn ="N";
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 캠페인 목록에서 캠페인 선택
        $('#campaignListTable tbody').on('click', 'tr', function(event) {
            $('#campaignListTable tr.selected').removeClass('selected');
            $(this).addClass('selected');
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if($("input:checkbox:checked").length == 0) {
                alert('<spring:message code="segment.alt.link.click1" />');  // 반응 대상 리스트에서 항목을 하나 이상 선택해야 합니다.
                return;
            }

            $("#linkClickFrm input[name=serviceNo]").val($("#linkListFrm input[name=serviceNo]").val());
            $("#linkClickFrm input[name=cmd]").val("save");
            var param = $.mdf.serializeObject('#linkClickFrm');
            $.post('/segment/linkclick/insertLinkClickSegment.do', $.param(param, true), function(result) {
                $("#linkListDiv").html(result);
                saveYn = "Y";
            });

        });

        // 확정 버튼 클릭
        $("#fixBtn").on("click", function(event) {
            if($("input:checkbox:checked").length == 0) {
                alert('<spring:message code="segment.alt.link.click1" />');  // 반응 대상 리스트에서 항목을 하나 이상 선택해야 합니다.
                return;
            }
            if(saveYn === 'N'){
            	alert('반응 대상 리스트를 먼저 저장하세요.');
            	return;
            }

            var segmentNm = $("#linkClickFrm input[name=segmentNm]").val();
            var segmentSize = $("#linkClickFrm input[name=segmentSize]").val();
            var segmentNo = $("#linkClickFrm input[name=segmentNo]").val();
            if(segmentNm != '') {
                opener.$("input[name='segmentNm']").setValue(segmentNm);
                opener.$("input[name='segmentSize']").setValue(segmentSize);
                opener.$("input[name='segmentNo']").setValue(segmentNo);
                opener.$("input[name='segType']").setValue("L");
                opener.editorIfrm.setSemantic(segmentNo);
                //SQLFILTER를 이용하여 발송을 하려면 캠페인 RELATION_TYPE 이 R 또는 L 이어야 한다.
                //캠페인 테이블에 L 값을 업데이트 함.
                opener.$("input[name='imsiRelationType']").setValue("L");
                alert('<spring:message code="segment.alt.link.click" />');  // 반응대상자를 등록하였습니다.
            }

            window.close();
        });
    }

    function initPage() {
        new mdf.DataTable("#campaignListTable");
    }

    // 리스트내 캠페인 제목 클릭
    function goLinkList(serviceNo) {
        $("#linkListFrm input[name=serviceNo]").val(serviceNo);
        $("#linkListFrm input[name=serviceType]").val('${serviceType}');
        var param = $.mdf.serializeObject('#linkListFrm');
        $.post("/segment/linkClickRptListDiv.do", $.param(param, true), function(result) {  // /segment/linkclick/linkclick_popup.do
            $("#linkListDiv").html(result);
        });
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title 탭 -->
                <div class="row table_option">
                    <div class="col-10 pl-0">
                        <c:import url="/segment/segment_addtab_include.do">
                            <c:param name="viewPath" value="segment/segmentAddTab_inc"/><%-- segment/segment_add_tab_include --%>
                        </c:import>
                    </div>
                    <div class="col-2 pr-0 justify-content-end">
                        <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                    </div><!-- 닫기 -->
                </div>
            </div>

            <div class="card-body">
                <div class="table-responsive overflow-x-hidden">
                    <table class="table table-hover table-sm dataTable table-fixed" id="campaignListTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="100"><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 -->
                            <th scope="col" width="*"><spring:message code="segment.menu.link.cname"/></th><!-- 캠페인 제목 -->
                            <th scope="col" width="100"><spring:message code="segment.menu.link.date"/></th><!-- 날짜 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${!empty serviceList}">
                        <c:forEach var="serviceList" items="${serviceList}" varStatus="i">
                        <tr style="cursor: pointer;" onclick="goLinkList('${serviceList.serviceNo}')">
                            <td scope="row">${serviceList.serviceNo}</td>
                            <td class="text-left">
                                <c:if test="${serviceNo eq serviceList.serviceNo}"></c:if>
                                ${serviceList.scenarioNm}
                            </td>
                            <td>
                                <fmt:parseDate value="${serviceList.createDt}" pattern="yyyyMMdd" var="createDt"/>
                                <fmt:formatDate value="${createDt}" pattern="yyyy-MM-dd" />
                            </td>
                        </tr>
                        </c:forEach>
                        </c:if>
                        </tbody>
                    </table>

                    <!-- 페이징 -->
                    <c:import url="/common/page.do">
                        <c:param name="viewPath" value="/common/page"/>
                        <c:param name="actionPath" value="/target/linkClickServiceList.do"/><%-- /segment/target/linkservice.do --%>
                        <c:param name="target" value="upload"/>
                        <c:param name="total" value="${totalCount}"/>
                        <c:param name="nowPage" value="${nowPage}"/>
                        <c:param name="countPerPage" value="${countPerPage}"/>
                        <c:param name="nowPage" value="${nowPage}"/>
                        <c:param name="hiddenParam" value="searchColumn:${searchColumn}"/>
                        <c:param name="hiddenParam" value="searchWord:${searchWord}"/>
                        <c:param name="hiddenParam" value="serviceType:${serviceType}"/>
                        <c:param name="hiddenParam" value="tabUse:${tabUse}"/>
                        <c:param name="hiddenParam" value="tabType:L"/>
                    </c:import>
                </div>
                <div id="linkListDiv" class="table-responsive overflow-auto overflow-x-hidden" style="height: 190px;">
                </div>
            </div>

            <div class="pop-card" style="height: 64px;">
                <div class="card-footer text-center">
                    <button type="button" class="btn btn-outline-primary" id="saveBtn">
                        <spring:message code="button.save"/><!-- 저장 -->
                    </button>
                    <button type="button" class="btn btn-outline-primary" id="fixBtn">
                        <spring:message code="button.fix" /><!-- 확정 -->
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<form id="linkListFrm" name="linkListFrm" method="post">
    <input type="hidden" name="serviceType" value="${serviceType}">
    <input type="hidden" name="serviceNo" value="${serviceNo}">
    <input type="hidden" name="segmentNo" value="${segmentNo}">
    <input type="hidden" name="cmd">
</form>
</body>
</html>
