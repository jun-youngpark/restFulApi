<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계>캠페인 미리보기(팝업)] 상단 대상자 목록 출력
 * - [이케어>이케어 등록>2단계>이케어 미리보기(팝업)] 상단 대상자 목록 출력
 * - URL : /common/previewMainPopup.do
 * - Controller : com.mnwise.wiseu.web.common.web.MailMimeViewController
 * - 이전 파일명 : mail_mime_list.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code='segment.preview.title'/></title><!-- 미리보기 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 리스트 숨기기/보기 버튼 클릭
        $('#listBtn').on("click", function(event) {
            $('#viewList').toggle();
            var mode = $('#listMode').val();
            if(mode == 'on') {
                $('#listMode').val('off');
                $('#listBtn').html("<i class='fas fa-long-arrow-alt-up'></i> <spring:message code='button.list.hide'/>"); // 리스트 숨김
                $('#contsDiv').height(480);
            } else {
                $('#listMode').val('on');
                $('#listBtn').html("<i class='fas fa-long-arrow-alt-down'></i> <spring:message code='button.list.show'/>"); // 리스트 보기
                $('#contsDiv').height(620);
            }
            return;
        });

        // 전체보기 버튼 클릭
        $('#allViewBtn').on("click", function(event) {
            $("#previewFrm").submit();
        });

        // 검색어 엔터키 입력
        $("#searchValue").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $('#searchBtn').on("click", function(event) {
            event.preventDefault();
            $("#searchKey").val($("#searchText option:selected").val());
            $("#previewFrm").submit();
        });

        // 대상자 목록에서 대상자 선택
        $('#targetListTable tbody').on('click', 'tr', function(event) {
            $('#targetListTable tr.selected').removeClass('selected');
            $(this).addClass('selected');
        });
    }

    function initPage() {
        <c:if test="${subType eq ''}">
            makePreview("","","","");  // 실시간 인자는 값을 알수 없음
        </c:if>
        new mdf.DataTable("#targetListTable");
    }

    // 대상자 링크 클릭 - 하단 미리보기 내용 출력
    function makePreview(customerKey,customerNm,customerEmail,seg) {
        $("#previewFrm input[name=customerKey]").val(customerKey);
        $("#previewFrm input[name=customerNm]").val(customerNm);
        $("#previewFrm input[name=customerEmail]").val(customerEmail);
        $("#previewFrm input[name=seg]").val(seg);

        var param = $.mdf.serializeObject('#previewFrm');
        $.post("/common/previewConts.do", $.param(param, true), function(result) {
            $('#contsDiv').html(result);
        });
    }
</script>
</head>

<body>
<form id="previewFrm" name="previewFrm" action="/common/previewMainPopup.do" method="post"><!-- /common/mime_view.do -->
<input type="hidden" id="cmd" name="cmd" value="previewList"/>
<input type="hidden" id="customerKey" name="customerKey" />
<input type="hidden" id="customerNm" name="customerNm" />
<input type="hidden" id="customerEmail" name="customerEmail" />
<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}"/>
<input type="hidden" id="servieNo" name="serviceNo" value="${serviceNo}"/>
<input type="hidden" id="seg" name="seg" />
<input type="hidden" name="listMode" id="listMode" value="off" />
<input type="hidden" name="realtimeArg" id="realtimeArg" value="${realtimeArg}" />
<input type="hidden" name="searchKey" id="searchKey" />
<input type="hidden" name="subType" id="subType" value="${subType}" />
<input type="hidden" name="abTestType" id="abTestType" value="${abTestType}" />

<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="segment.preview.title"/></h5></div><!-- 미리보기 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <div class="row align-items-center py-1 table_option">
            <div class="col-3"><!-- buttons -->
                <c:if test="${subType ne ''}"><!-- 실시간이 아닌경우  -->
                    <button class="btn btn-sm btn-outline-primary" id="listBtn">
                        <i class="fas fa-long-arrow-alt-up"></i> <spring:message code="button.list.hide"/><!-- 리스트 숨김 -->
                    </button>
                </c:if>
                <button class="btn btn-sm btn-outline-primary" id="allViewBtn">
                    <i class="fas fa-search"></i> <spring:message code="button.view.all"/><!-- 전체보기 -->
                </button>
            </div>
            <div class="col-9 searchWrap"><!-- search -->
                <c:if test="${subType ne '' && abTestType ne 'N' }">
                <div class="form-group searchbox search_input"><!-- A/B선택 조건 -->
                    <span class="form-control-label txt"><spring:message code="campaign.divide.cond"/> :</span><!-- A/B선택 조건 -->
                    <div class="custom-control custom-radio custom-control-inline ml-2">
                        <input type="radio" class="custom-control-input" id="abType_A" name="abType" value="A" checked="checked" />
                        <label class="custom-control-label" for="abType_A">A</label>
                    </div>
                    <div class="custom-control custom-radio custom-control-inline">
                        <input type="radio" class="custom-control-input" id="abType_B" name="abType" value="B" checked="checked" />
                        <label class="custom-control-label" for="abType_B">B</label>
                    </div>
                </div>
                </c:if>

                <div class="form-group searchbox search_input pl-4">
                    <select class="form-control form-control-sm" name="searchText" id="searchText" >
                        <option value="customerKey" ${param.searchText eq 'customerKey'?"selected":"" }><spring:message code="common.menu.id"/></option><!-- 아이디 -->
                        <option value="customerName" ${param.searchText eq 'customerName'?"selected":"" }><spring:message code="common.menu.name"/></option><!-- 이름 -->
                        <option value="customerEmail" ${param.searchText eq 'customerEmail'?"selected":"" }><spring:message code="common.menu.email"/></option><!-- 이메일 -->
                    </select>
                    <input type="search" class="form-control form-control-sm" name="searchValue" id="searchValue" value="${param.searchValue}"/>
                    <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                        <spring:message code="button.search"/><!-- 검색 -->
                    </button>
                </div>
            </div>
        </div>

        <div class="alert alert-secondary mt-0 mb-0" role="alert">
            <!-- 아래 대상자 정보를 클릭하면 개인정보가 매핑되어 있는 메일 본문을 그대로 미리보기 할 수 있습니다. -->
            <i class="fas fa-exclamation-circle"></i> <spring:message code="campaign.alt.action.preview2" />
        </div>

        <c:if test="${subType ne ''}"><%-- 실시간이 아닌경우 --%>
        <div id="viewList" class="table-responsive overflow-x-hidden mb--2">
            <table class="table table-xs dataTable table-hover table-fixed" id="targetListTable">
                <thead class="thead-light">
                    <tr>
                        <th scope="col" width="20%"><spring:message code="common.menu.id"/></th><!-- 아이디 -->
                        <th scope="col" width="15%"><spring:message code="common.menu.name"/></th><!-- 이름 -->
                        <th scope="col" width="*"><spring:message code="common.menu.email"/></th><!-- 이메일 -->
                        <c:if test="${'N' eq subType || ('S' eq subType || 'R' eq subType) && isJeonmun eq true}">
                        <th scope="col" width="25%">SEQ</th><!-- SEQ-->
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="dto" items="${list}" varStatus="status">
                    <c:choose>
                    <c:when test="${'N' eq subType || ('S' eq subType || 'R' eq subType) && isJeonmun eq true}">
                    <tr style="cursor: pointer;" onclick="makePreview('${dto.customerSeqFieldName}','${dto.customerNameFieldName}','${dto.customerEmailFieldName}', '${dto.seg}')">
                    </c:when>
                    <c:otherwise>
                    <tr style="cursor: pointer;" onclick="makePreview('${dto.customerKeyFieldName}','${dto.customerNameFieldName}','${dto.customerEmailFieldName}', '${dto.seg}')">
                    </c:otherwise>
                    </c:choose>

                        <td class="text-left">${dto.customerKeyFieldName}</td>
                        <td>${wiseu:nameMask(dto.customerNameFieldName)}</td>
                        <td class="text-left">${wiseu:mailMask(dto.customerEmailFieldName)}</td>
                        <c:if test="${'N' eq subType || ('S' eq subType || 'R' eq subType) && isJeonmun eq true}">
                        <td class="text-left">${dto.customerSeqFieldName}</td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table><!-- e.Light table -->
        </div>
        </c:if>

        <div class="table-responsive border gridWrap overflow-auto" style="height: 480px;" id="contsDiv"><!-- 내용보기 -->
        </div>
    </div>
</div>
</body>
</html>