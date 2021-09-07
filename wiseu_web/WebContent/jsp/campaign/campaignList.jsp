<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 리스트] 캠페인 리스트
 * - [캠페인>캠페인 리스트] 캠페인 리스트 - 수행 상태 선택
 * - [캠페인>캠페인 리스트] 캠페인 리스트 - 승인요청만 보기 버튼 클릭
 * - [캠페인>캠페인 리스트] 캠페인 리스트 - 검색 버튼 클릭
 * - [캠페인>캠페인 리스트] 캠페인 리스트 - 태그 클릭
 * - URL : /campaign/campaignList.do
 * - Controller : com.mnwise.wiseu.web.campaign.web.ScenarioController
 * - 이전 파일명 : campaign_list.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<title><spring:message code="campaign.list" /></title><!-- 캠페인 리스트 -->
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type='text/javascript'>
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if($("#actionFrm input:radio[name=campaignNo]").is(":checked") == false) {
                alert('<spring:message code="campaign.alert.msg.del.1"/>');  // 삭제할 캠페인을 선택하세요.
                return;
            }

            var campaignSts = $("#campaignSts").val();

            switch(campaignSts) {
            case "R" :
                alert('<spring:message code="campaign.alert.msg.del.2"/>');  // 발송 대기 중인  캠페인은 삭제 할 수 없습니다.
                return;

            case "W" :
                alert('<spring:message code="campaign.alert.msg.del.3"/>');  // 발송 중인  캠페인은 삭제 할 수 없습니다.
                return;

            case "E" :
                alert('<spring:message code="campaign.alert.msg.del.4"/>');  // 발송 종료된 캠페인은 삭제 할 수 없습니다.
                return;

            case "O" :
                alert('<spring:message code="campaign.alert.msg.del.5"/>');  // 발송 에러가 난  캠페인은 삭제 할 수 없습니다.
                return;
            }

            if(!confirm('<spring:message code="campaign.alert.msg.del"/>')) {  // 삭제하시겠습니까?
                return;
            }

            $("#actionFrm").attr('action', '/campaign/deleteCampaign.do').submit();
        });

        // 복사 버튼 클릭
        $("#copyBtn").on("click", function(event) {
            if($("#actionFrm input:radio[name=campaignNo]").is(":checked") == false) {
                alert('<spring:message code="campaign.alert.msg.copy"/>');  // 복사할 캠페인을 선택하세요.
                return;
            }

            if($("#actionFrm input[name=depthNo]").val() > 1) {
                alert('<spring:message code="campaign.alert.msg.copy.1"/>');  // 재발송 캠페인은 복사할 수 없습니다.
                return;
            }

            $("#actionFrm").attr('action', '/campaign/copyCampaign.do').submit();
        });

        // 상태변경 버튼 클릭
        $("#statusChangeBtn").on("click", function(event) {
            if($("#actionFrm input:radio[name=campaignNo]").is(":checked") == false) {
                alert('<spring:message code="campaign.alert.msg.change"/>');  // 상태를 변경 할 캠페인을 선택하세요.
                return;
            }

            var campaignNo = $("input[name='campaignNo']:radio:checked").val();
            var campaignSts = $("#campaignSts").val();

            if(campaignSts == "I") { // 캠페인이 작성중일 경우
                var campaignLevel = $("#campaignLevel").val();
                var scenarioNo = $("#scenarioNo").val();
                var channelType = $("#channelType").val();
                var depthNo = $("#depthNo").val();
                if(campaignLevel < 3) {  // 캠페인 레벨이 3보다 작으면 즉, 캠페인 작성단계를 캠페인 수행까지 진행하지 않은 캠페인인 경우
                    if(!confirm('<spring:message code="campaign.alert.msg.change.1"/>')) {  // 작성중 상태인 캠페인입니다.\\n수정 화면으로 이동하시겠습니까?
                        return;
                    }
                    // /campaign/campaign_step_form.do
                    window.location.href="/campaign/campaign1Step.do?scenarioNo="+scenarioNo+"&campaignNo="+campaignNo+"&depthNo="+depthNo+"&channelType="+channelType;
                    return;
                } else { // 캠페인 수행까지 진행한 캠페인인 경우
                    if(!confirm('<spring:message code="campaign.alert.msg.change.2"/>')) {  // 작성중 상태인 캠페인입니다.\\n프로모션 수행으로 이동하시겠습니까?
                        return;
                    }

                    // /campaign/campaign_3step_form.do
                    window.location.href="/campaign/campaign3Step.do?scenarioNo="+scenarioNo+"&campaignVo.campaignNo="+campaignNo+"&depthNo="+depthNo+"&channelType="+channelType;
                    return;
                }
            } else if(campaignSts == "P") { // 캠페인이 보류 인 경우
                <c:choose>
                    <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'U'}">
                        <c:choose>
                            <c:when test="${execute eq 'X'}">
                                if(!confirm('<spring:message code="campaign.alert.msg.change.3"/>')) {  // 발송대기 상태로 변경하시겠습니까?
                                    return;
                                }
                                $("#campaignSts").val("R");
                            </c:when>
                            <c:otherwise>
                                alert('<spring:message code="campaign.alert.msg.change.4"/>');  // 실행/발송 권한이 없으므로, 상태를 변경할 수 없습니다.
                                return;
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        if(!confirm('<spring:message code="campaign.alert.msg.change.3"/>')) {  // 발송대기 상태로 변경하시겠습니까?
                            return;
                        }
                        $("#campaignSts").val("R");
                    </c:otherwise>
                </c:choose>
            } else if(campaignSts == "R") { // 캠페인이 발송대기 인 경우
                <c:choose>
                    <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'U'}">
                        <c:choose>
                            <c:when test="${execute eq 'X'}">
                                if(!confirm('<spring:message code="campaign.alert.msg.change.5"/>')) {  // 보류 상태로 변경하시겠습니까?
                                    return;
                                }
                                $("#campaignSts").val("P");
                            </c:when>
                            <c:otherwise>
                                alert('<spring:message code="campaign.alert.msg.change.4"/>');  // 실행/발송 권한이 없으므로, 상태를 변경할 수 없습니다.
                                return;
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        if(!confirm('<spring:message code="campaign.alert.msg.change.5"/>')) {  // 보류 상태로 변경하시겠습니까?
                            return;
                        }
                        $("#campaignSts").val("P");
                    </c:otherwise>
                </c:choose>
            } else if(campaignSts == "D") { // 캠페인이 승인 거부인 경우
                <c:choose>
                    <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'U'}">
                        <c:choose>
                            <c:when test="${execute eq 'X'}">
                                if(!confirm('<spring:message code="campaign.alert.msg.change.7"/>')) {  // 승인 요청 상태로 변경하시겠습니까?
                                    return;
                                }
                                $("#approvalSts").val("C");
                            </c:when>
                            <c:otherwise>
                                alert('<spring:message code="campaign.alert.msg.change.4"/>');  // 실행/발송 권한이 없으므로, 상태를 변경할 수 없습니다.
                                return;
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        if(!confirm('<spring:message code="campaign.alert.msg.change.7"/>')) {  // 승인 요청 상태로 변경하시겠습니까?
                            return;
                        }
                        $("#approvalSts").val("C");
                    </c:otherwise>
                </c:choose>
            } else if(campaignSts == "C") { // 캠페인이 승인 요청 상태인 경우
                <c:choose>
                    <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'U'}">
                        <c:choose>
                            <c:when test="${execute eq 'X'}">
                                if(!confirm('<spring:message code="campaign.alert.msg.change.6"/>')) {  // 승인 상태로 변경하시겠습니까?
                                    return;
                                }
                                $("#approvalSts").val("A");
                            </c:when>
                            <c:otherwise>
                                alert('<spring:message code="campaign.alert.msg.change.4"/>');  // 실행/발송 권한이 없으므로, 상태를 변경할 수 없습니다.
                                return;
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        if(!confirm('<spring:message code="campaign.alert.msg.change.6"/>')) {  // 승인 상태로 변경하시겠습니까?
                            return;
                        }
                        $("#approvalSts").val("A");
                    </c:otherwise>
                </c:choose>
            } else if(campaignSts == "A") {
                $("#approvalSts").val("A"); //승인 상태인 경우는 그대로 승인 상태로 둔다.
            }

            $("#actionFrm").attr('action', '/campaign/changeCampaignSts.do').submit();
        });

        // 승인요청만 보기 버튼 클릭
        $("#reqViewBtn").on("click", function(event) {
            $("#selectCampaignSts > option[value='C']").attr("selected", "true");
            $("#actionFrm").attr('action', '/campaign/campaignList.do').submit();  // /campaign/campaign.do
            return false;
        });

        // 검색어 엔터키 입력
        $("#searchWord").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            if(document.actionFrm.searchColumn.value == "campaignNo") {
                var word = $("#searchWord").val();
                if($.mdf.isBlank(word) || isNaN(word)) {
                    alert("<spring:message code='campaign.alert.msg.search.no'/>");
                    return;
                }
            }
            event.preventDefault();
            $("#actionFrm").attr('action', '/campaign/campaignList.do').submit();  // /campaign/campaign.do
        });
    }

    function initPage() {
        if('${scenarioVo.campaignVo.approvalSts}' == '') {
            $("#selectCampaignSts > option[value='${scenarioVo.campaignVo.campaignSts}']").attr("selected", "true");
        } else {
            $("#selectCampaignSts > option[value='${scenarioVo.campaignVo.approvalSts}']").attr("selected", "true");
        }

        new mdf.Date("#searchQstartDt");  // 작성일-시작일
        new mdf.Date("#searchQendDt");  // 작성일-종료일
    }

    // 태그 검색
    function goTagSearch(tagNo) {
        $("#tagNo").val(tagNo);
        $("#actionFrm").attr('action', '/campaign/campaignList.do').submit();  // /campaign/campaign.do
    }

    // 캠페인명(발송제목) 클릭
    function goView(scenarioNo,campaignNo,depthNo,channelType) {
        // /campaign/campaign_step_form.do
        window.location.href="/campaign/campaign1Step.do?scenarioNo="+scenarioNo+"&campaignNo="+campaignNo+"&depthNo="+depthNo+"&channelType="+channelType;
    }

    // 캠페인 선택시 Hidden 값 셋팅
    function campaignValue(scenarioNo,campaignLevel,campaignSts,depthNo,channelType, segmentNo) {
        $("#scenarioNo").val(scenarioNo);
        $("#campaignLevel").val(campaignLevel);
        $("#campaignSts").val(campaignSts);
        $("#depthNo").val(depthNo);
        $("#channelType").val(channelType);
        $("#segmentNo").val(segmentNo);
    }

    // 에러 상세내역 팝업
    function goErrorPopup(campaignNo,resultSeq,scenarioNm,campaignPreface) {
        $("#errPopupFrm input[name=campaignNo]").val(campaignNo);
        $("#errPopupFrm input[name=resultSeq]").val(resultSeq);
        $("#errPopupFrm input[name=scenarioNm]").val(scenarioNm);
        $("#errPopupFrm input[name=campaignPreface]").val(campaignPreface);

        $.mdf.popupSubmit("#errPopupFrm", "/campaign/campaignErrorPopup.do", "errorPopup", 525, 320);  // /campaign/campaign_error_popup.do
    }
</script>
</head>
<body>
<form id="actionFrm" name="actionFrm" method="post">
<input type="hidden" name="cmd" id="cmd" />
<input type="hidden" name="tagNo" id="tagNo" />
<input type="hidden" name="campaignSts" id="campaignSts" />
<input type="hidden" name="approvalSts" id="approvalSts" />
<input type="hidden" name="scenarioNo" id="scenarioNo" />
<input type="hidden" name="channelType" id="channelType" />
<input type="hidden" name="depthNo" id="depthNo" />
<input type="hidden" name="campaignLevel" id="campaignLevel" />
<input type="hidden" name="changeAllStatus" id="changeAllStatus" />
<input type="hidden" name="channelStatus" id="channelStatus" />
<input type="hidden" name="segmentNo" id="segmentNo" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"> <spring:message code="campaign.list"/></h3><!-- 캠페인 리스트 -->
            </div>

            <div class="card-body px-0 pb-0">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-5"><!-- buttons -->
                        <c:if test="${sessionScope.write eq 'W'}">
                        <button class="btn btn-sm btn-outline-primary btn-round" id="deleteBtn">
                            <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                        </button>
                        <button class="btn btn-sm btn-outline-primary btn-round" id="copyBtn">
                            <i class="fas fa-copy"></i> <spring:message code="button.copy"/><!-- 복사 -->
                        </button>
                        <button  class="btn btn-sm btn-outline-primary btn-round" id="statusChangeBtn">
                            <i class="fas fa-traffic-light"></i> <spring:message code="button.change.status"/><!-- 상태변경 -->
                        </button>
                        <button class="btn btn-sm btn-outline-primary btn-round" id="reqViewBtn">
                            <i class="fas fa-search"></i> <spring:message code="button.approval.request.view"/><!-- 승인요청만 보기 -->
                        </button>
                        </c:if>
                    </div>

                    <div class="col-7 searchWrap"><!-- search -->
                        <div class="form-group searchbox pl-1">
                            <span class="form-control-label txt"><spring:message code="campaign.table.cdate"/></span><!-- 작성일 -->
                            <div class="input_datebox">
                                <input type="hidden" name="searchQstartDt" id="searchQstartDt" value="${searchQstartDt}" maxlength="10"/>
                            </div>
                            <span class="form-control-label">~</span>
                            <div class="input_datebox">
                                <input type="hidden" name="searchQendDt" id="searchQendDt" value="${searchQendDt}" maxlength="10"/>
                            </div>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" id="selectCampaignSts" name="campaignVo.campaignSts">
                                <option value=''><spring:message code="campaign.table.status"/></option><!-- 수행상태 -->
                                <option value='I'><spring:message code="campaign.status.I"/></option>
                                <option value='P'><spring:message code="campaign.status.P"/></option>
                                <option value='R'><spring:message code="campaign.status.R"/></option>
                                <option value='W'><spring:message code="campaign.status.W"/></option>
                                <option value='E'><spring:message code="campaign.status.E"/></option>
                                <option value='C'><spring:message code="campaign.status.C"/></option>
                                <option value='D'><spring:message code="campaign.status.D"/></option>
                                <option value='S'><spring:message code="campaign.status.S"/></option>
                                <option value='V'><spring:message code="campaign.status.V"/></option>
                                <option value='T'><spring:message code="campaign.status.T"/></option>
                                <option value='O'><spring:message code="campaign.status.O"/></option>
                                <option value='A'><spring:message code="campaign.status.A"/></option>
                            </select>
                        </div>
                        <div class="form-group searchbox search_input pl-1">
                            <select class="form-control form-control-sm" name="searchColumn">
                                <option value="scenarioNm" <c:if test="${scenarioVo.searchColumn eq 'scenarioNm'}">selected</c:if>><spring:message code="campaign.table.cname"/></option><!-- 캠페인명 -->
                                <option value="campaignNo" <c:if test="${scenarioVo.searchColumn eq 'campaignNo'}">selected</c:if>><spring:message code="campaign.menu.cno_1"/></option><!-- 캠페인 번호 -->
                            </select>
                            <input type="search" class="form-control form-control-sm" aria-controls="datatable-basic" id="searchWord" name="searchWord" value="${scenarioVo.searchWord}" style="width:130px;"/>
                            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                <spring:message code="button.search"/><!-- 검색 -->
                            </button>
                        </div>
                    </div><!-- //search -->
                </div><!-- e.search area & buttons -->
            </div><!-- e.card body -->

            <div class="table-responsive overflow-x-hidden">
                <table class="table table-sm dataTable table-hover table-fixed">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 선택 -->
                        <th scope="col" width="6%"><spring:message code="campaign.menu.cno.br"/></th><!-- 캠페인번호 -->
                        <th scope="col"><spring:message code="campaign.table.cname"/></th><!-- 캠페인명 -->
                        <th scope="col" width="5%"><spring:message code="campaign.menu.channel"/></th><!-- 채널 -->
                        <th scope="col" width="7%"><spring:message code="campaign.table.status"/></th><!-- 수행상태 -->
                        <th scope="col" width="8%"><spring:message code="campaign.table.type"/></th><!-- 유형 -->
                        <th scope="col" width="9%"><spring:message code="campaign.table.ctarget"/></th><!-- 대상자수 -->
                        <th scope="col" width="9%"><spring:message code="campaign.table.sdate"/></th><!-- 발송일시 -->
                        <c:choose>
                        <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'A'}">
                            <th scope="col" width="10%"><spring:message code="campaign.table.group"/></th><!-- 발송부서 -->
                            <th scope="col" width="9%"><spring:message code="campaign.table.creator"/></th><!-- 작성자 -->
                        </c:when>
                        <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'M'}">
                            <th scope="col" width="9%"><spring:message code="campaign.table.creator"/></th><!-- 작성자 -->
                        </c:when>
                        </c:choose>
                        <th scope="col" width="9%"><spring:message code="campaign.table.cdate"/></th><!-- 작성일 -->
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="loop" items="${scenarioList}" varStatus="i">
                    <tr style="cursor: pointer;" onclick="javascript:goView('${loop.scenarioNo}','${loop.campaignVo.campaignNo}','${loop.campaignVo.depthNo}','${loop.campaignVo.channelType}')">
                        <th scope="row" onclick="event.cancelBubble=true"><!-- 선택 -->
                            <div class="custom-control custom-radio">
                                <input type="radio" id="radio_${i.count}" class="custom-control-input" name="campaignNo" value="${loop.campaignVo.campaignNo}"
                                    onclick="javascript:campaignValue(${loop.scenarioNo},'${loop.campaignVo.campaignLevel}','${loop.campaignVo.campaignSts}','${loop.campaignVo.depthNo}','${loop.campaignVo.channelType}', '${loop.campaignVo.segmentNo}');"/>
                                <label class="custom-control-label" for="radio_${i.count}"></label>
                            </div>
                        </th>
                        <td>${loop.campaignVo.campaignNo}</td><!-- 캠페인번호 -->
                        <td class="text-left"><!-- 캠페인명 -->
                            <c:if test="${loop.campaignVo.depthNo > 1}">
                                <c:forEach var="depthNo" begin="1" end="${loop.campaignVo.depthNo}" step="1">&nbsp;</c:forEach><!-- Depth별 들여쓰기. 재발송 아이콘은 Depth 2부터 -->
                                <img src="/images/common/relation/type_${loop.campaignVo.relationType}.png" style="vertical-align: middle;"/>
                            </c:if>
                            ${loop.scenarioNm}
                        </td>
                        <td><!-- 채널 -->
                            <em class="txt_channel ${loop.campaignVo.channelType}">
                                <c:choose>
                                    <c:when test="${loop.campaignVo.channelType eq 'M'}">E</c:when>
                                    <c:when test="${loop.campaignVo.channelType eq 'T'}">M</c:when>
                                    <c:otherwise>${loop.campaignVo.channelType}</c:otherwise>
                                </c:choose>
                            </em>
                        </td>
                        <td onclick="event.cancelBubble=true"><!-- 수행상태 -->
                            <c:choose>
                                <c:when test="${loop.campaignVo.campaignSts eq 'O'}">
                                    <a href="javascript:goErrorPopup(${loop.campaignVo.campaignNo},${loop.campaignVo.resultSeq},'${loop.scenarioNm}','${loop.campaignVo.campaignPreface}');">
                                        ${loop.campaignVo.campaignStsNm}
                                    </a>
                                </c:when>
                                <c:otherwise>${loop.campaignVo.campaignStsNm}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${loop.campaignVo.relationTypeNm}<c:if test="${loop.campaignVo.divideYn eq 'Y'}"><spring:message code="campaign.divide"/><!-- /분할 --></c:if></td>
                        <td class="text-right" id="targetCnt_${loop.campaignVo.campaignNo}"><!-- 대상자수 -->
                            <c:if test="${loop.campaignVo.relationType ne 'S' and loop.campaignVo.relationType ne 'F' and loop.campaignVo.relationType ne 'O'}">
                                <fmt:formatNumber type="number" value="${loop.campaignVo.targetCnt}" />
                                <c:if test="${not empty loop.campaignVo.targetCnt}"> <spring:message code="campaign.menu.persons"/><!-- 명 --></c:if>
                            </c:if>
                        </td>
                        <td>${loop.campaignVo.sendstartDtm}</td><!-- 발송일시 -->
                        <c:choose>
                            <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'A'}">
                                <td>${loop.grpNm}</td><!-- 발송부서 -->
                                <td>${loop.userVo.nameKor}</td><!-- 작성자 -->
                            </c:when>
                            <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'M'}">
                            <td>${loop.userVo.nameKor}</td><!-- 작성자 -->
                            </c:when>
                        </c:choose>
                        <td>${loop.campaignVo.createDt}</td><!-- 작성일 -->
                    </tr>
                    </c:forEach>
                    <c:if test="${empty scenarioList}">
                        <c:choose>
                            <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'A'}">
                                <c:set var="noDataColSpan" value="11"/>
                            </c:when>
                            <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'M'}">
                                <c:set var="noDataColSpan" value="10"/>
                            </c:when>
                            <c:when test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'U'}">
                                <c:set var="noDataColSpan" value="9"/>
                            </c:when>
                        </c:choose>
                        <tr>
                            <td colspan="${noDataColSpan}"><spring:message code="common.msg.nodata"/></td><!-- 검색결과가 없습니다. -->
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div><!-- e.Light table -->

            <!-- 페이징 -->
            ${paging}
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>

<!-- POST 팝업을 사용하기 위한 팝업 form -->
<form id="errPopupFrm" name="errPopupFrm" method="post">
    <input type="hidden" name="campaignNo" id="campaignNo" />
    <input type="hidden" name="resultSeq" id="resultSql" />
    <input type="hidden" name="scenarioNm" id="scenarioNm" />
    <input type="hidden" name="campaignPreface" id="campaignPreface" />
</form>
</body>
</html>