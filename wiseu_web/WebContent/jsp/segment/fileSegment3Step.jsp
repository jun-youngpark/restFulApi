<%-------------------------------------------------------------------------------------------------
 * - [대상자 관리>대상자파일 올리기] 대상자 등록결과확인 <br/>
 * - URL : /segment/upload_result.do <br/>
 * - Controller :com.mnwise.wiseu.web.segment.web.UploadResultController <br/>
 * - 20100610 캠페인등록시 대상자 등록할 경우 대상자 확인해도 세그먼트 정보가 캠페인페이지로 전달안됨(파이어폭스) 파폭일경우 opener사용으로 변경
 * - 이전 파일명 : upload_result.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="segment.msg.reg"/> &gt; <spring:message code="segment.msg.tcheck"/></title><!-- 대상자 등록 > 대상자 확인-->
<%@ include file="/jsp/include/plugin.jsp"%>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 다운로드 버튼 클릭
        $("#downloadBtn").on("click", function(event) {
            var insertedErrFilePath = $("#insertedErrFilePath").val();
            window.location.href="/common/file_download.do?type=importFile&fileDir="+insertedErrFilePath;
        });

        // 대상자 갱신 버튼 클릭
        $("#targetRefreshBtn").on("click", function(event) {
            if(!confirm('<spring:message code="segment.alert.msg.upload.13"/>')) {  // 대상자 갱신은 데이터가 많을 경우 오랜 시간이 걸릴 수 있습니다.\\n 계속 진행하시겠습니까?
                return;
            }

            $.post("/segment/renewalSegment.json?segmentNo=${segmentNo}", null, function(segment) {
                alert('<spring:message code="segment.alert.msg.upload.14"/>');  // 대상자 갱신이 완료되었습니다.

                // [대상자>대상자 파일 올리기] 화면은 아래 로직을 수행. [캠페인>캠페인 등록>2단계>대상자등록(팝업)]의 경우 아래 로직 skip
                if($(location).attr('pathname') == "/segment/upload_result.do") {  // /segment/upload/upload_result.do
                    window.location.href="/segment/segmentList.do";  // /segment/segment.do
                }
            });
        });

        // 대상자 확인 버튼 클릭
        $("#targetCheckBtn").on("click", function(event) {
            var parentObj = (window.dialogArguments == undefined) ? opener : window.dialogArguments;

            parentObj.$("#segmentNo").val("${segmentNo}");
            parentObj.$("#segmentNm").val("${segmentNm}");
            parentObj.$("#segmentSize").val("${segmentSize}");
            parentObj.$("#imsiRelationType").val("N");
            //parent.goTempSave();

            parentObj.editorIfrm.setSemantic(${segmentNo});
            window.close();
        });
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <c:choose>
                    <c:when test="${uploadUrl}">
                        <div class="pop-card row table_option" style="height: 38px;">
                            <div class="col-10 pl-0 ml--3">
                                <h5 class="mb-0"><spring:message code="segment.msg.upload"/></h5><!-- 대상자 파일 올리기 -->
                            </div>
                            <div class="col-2 pr-0 mr--3 justify-content-end">
                                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                            </div><!-- 닫기 -->
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h3 class="mb-0"><spring:message code="segment.msg.upload"/></h3><!-- 대상자 파일 올리기 -->
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="150" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="segment.menu.sql.tno"/></th><!-- 대상자 번호 -->
                            <td class="control-text-sm">${segmentNo}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="segment.menu.sql.target"/></th><!-- 대상자 명 -->
                            <td class="control-text-sm">${segmentNm}</td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="segment.menu.upload.ftotline"/></th><!-- 원본 파일 총 라인 수 -->
                            <td class="control-text-sm">${totalLine} <spring:message code="report.campaign.head.title.cnt"/></td><!-- 건 -->
                        </tr>
                        <tr>
                            <th scope="row">
                                <spring:message code="segment.menu.upload.rtargetcnt"/><!-- 등록된 대상자 수 -->
                                <c:if test="${segmentSts eq '0'}">
                                    &nbsp;&nbsp;&nbsp;[ <font color="blue"><spring:message code="segment.alert.msg.upload.15"/></font> ]
                                    <!-- 업로드 수행 대기중입니다. 잠시후 '대상자 조회' 메뉴에서 완료 여부를 확인해 주세요. -->
                                </c:if>
                            </th>
                            <td class="control-text-sm">${segmentSize} <spring:message code="report.campaign.head.title.cnt"/></td><!-- 건 -->
                        </tr>

                        <c:if test="${insertedErrFilePath ne ''}">
                        <tr>
                            <th><spring:message code="segment.menu.upload.ftargetcnt"/></th><!-- 등록 실패 건 다운로드 -->
                            <td>
                                ${totalLine-segmentSize} <spring:message code="report.campaign.head.title.cnt"/><!-- 건 -->
                                <button class="btn btn-sm btn-outline-primary btn-round ml-3" id="downloadBtn">
                                    <i class="fas fa-download"></i> <spring:message code="button.download" /><!-- 다운로드 -->
                                </button>
                            </td>
                        </tr>
                        <input type="hidden" id="insertedErrFilePath" name="insertedErrFilePath" value="<c:out value="${insertedErrFilePath}" escapeXml="true" />"/>
                        </c:if>

                        <c:set var="reject" value="1"/>
                        <c:if test="${reject ne '0'}">
                        <tr><!-- 대상자 갱신 -->
                            <th>
                                <spring:message code="segment.menu.upload.refresh"/>
                            </th>
                            <td>
                                <button class="btn btn-sm btn-outline-primary btn-round" id="targetRefreshBtn">
                                    <i class="fas fa-sync-alt"></i> <spring:message code="button.target.refresh"/><!-- 대상자 갱신 -->
                                </button>
                            </td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- //card body -->

            <c:if test="${not empty uploadUrl}">
            <div class="pop-card" style="height: 64px;">
                <div class="card-footer">
                    <button type="button" class="btn btn-outline-primary" id="targetCheckBtn">
                        <spring:message code="segment.msg.tcheck"/><!-- 대상자 확인 -->
                    </button>
                </div>
            </div>
            </c:if>


        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>
