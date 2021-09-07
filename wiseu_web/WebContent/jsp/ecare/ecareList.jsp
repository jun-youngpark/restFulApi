<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 리스트] 이케어 리스트 - 리스트
 * - [이케어>이케어 리스트] 이케어 리스트 - 수행상태(선택박스)
 * - [이케어>이케어 리스트] 이케어 리스트 - 서비스타입(선택박스)
 * - [이케어>이케어 리스트] 이케어 리스트 - 검색
 * - [이케어>이케어 리스트] 이케어 리스트 - 태그
 * - URL : /ecare/ecare.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcareScenarioController
 * - 이전 파일명 : ecare_list.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<title><spring:message code="common.menu.ecare_${webExecMode}"/>&gt; <spring:message code="ecare.list_${webExecMode}"/></title>
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
    var targetStatus = false;
    var targetEcare = 0;

    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if(!$("input:radio[name=ecareNo]").is(":checked")){
                alert("<spring:message code='ecare.alert.msg.del.2_${webExecMode}'/>");
                return;
            }

            var ecareSts = $("#ecareSts").val();
            if(ecareSts == "R") {
                alert("<spring:message code='ecare.alert.msg.del.3_${webExecMode}'/>");
                return;
            }

            if(!confirm("<spring:message code='ecare.alert.msg.del.4'/>")){
                return;
            }

            var param = $.mdf.serializeObject('#actionFrm');
            $.post("/ecare/deleteEcare.do", $.param(param, true), function(result) {
            	if(result.code==='OK'){
            		location.reload();
            	}else{
            		alert(result.message);
            	}
            });
        });

        // 대상자 갱신 버튼 클릭
        $("#targetRefreshBtn").on("click", function(event) {
            if(targetStatus){
                alert("현재 "+targetEcare+"번 서비스의 대상자 갱신이 진행중입니다.");
                return;
            }

            if(!$("input:radio[name=ecareNo]").is(":checked")){
                alert("<spring:message code='ecare.alert.msg.update.2_${webExecMode}'/>");
                return;
            }

            if($("#checkedServiceType").val() == 'R'){
                alert("<spring:message code='ecare.alert.msg.update.3_${webExecMode}'/>");
                return;
            }

            if($("#checkedServiceType").val() == 'SN'){
                alert("<spring:message code='ecare.alert.msg.update.4_${webExecMode}'/>");
                return;
            }

            var relationType = $("#relationType").val();
            if(relationType == 'S' || relationType == 'F' || relationType == 'O') {
                alert("<spring:message code='ecare.alert.msg.cannot.update.segment'/>");
                return;
            }

            if($("#segmentNo").val() == '' || $("#segmentNo").val() == 0){
                alert("<spring:message code='ecare.alert.msg.select.2_${webExecMode}'/>");
                return;
            }

            var ecareNo = $("input[name='ecareNo']:radio:checked").val();
            $("#targetCnt_"+ecareNo).html("<img src='/images/ajax/plugin/wait_small_fbisk.gif' style='vertical-align: middle; margin-bottom: 2px;' />");

            targetStatus = true;
            targetEcare = ecareNo;

            $.post("/segment/renewalSegment.json?segmentNo=" + $("#segmentNo").val(), null, function(segment) {
                if($.mdf.isBlank(segment.errorMsg)) {
                    $("#targetCnt_" + targetEcare).html("<img src='/images/common/check.png' style='width:15px;height:15px' />&nbsp;"+$.number(segment.segmentSize)+" <spring:message code='common.menu.persons'/>");
                    $(this).delay(2000, function() {
                        $("#targetCnt_" + targetEcare).html($.number(segment.segmentSize)+" <spring:message code='common.menu.persons'/>" + "&nbsp;");
                    });
                } else {
                    alert(segment.errorMsg);
                }

                targetStatus = false;
                taragetEcare = 0;
            });
        });

        // 상태변경 버튼 클릭
        $("#statusChangeBtn").on("click", function(event) {
            if(!$("input:radio[name=ecareNo]").is(":checked")){
                alert("<spring:message code='ecare.alert.msg.change.1_${webExecMode}'/>");
                return;
            }

            var ecareNo = $("input[name='ecareNo']:radio:checked").val();
            var ecareLevel = $("#ecareLevel").val();
            var ecareSts = $("#ecareSts").val();
            var scenarioNo = $("#scenarioNo").val();
            var channelType = $("#channelType").val();
            var depthNo = $("#depthNo").val();

            if(ecareSts == 'I') { // 작성중인 경우
                if(ecareLevel < 3) { // 이케어 작성이 끝나지 않은 경우 (3단계 이케어 수행까지 진행되지 않은 경우)
                    if(!confirm("<spring:message code='ecare.alert.msg.change.2_${webExecMode}'/>")) {
                        return;
                    }

                    // /ecare/ecare_step_form.do
                    window.location.href="/ecare/ecare2Step.do?scenarioNo="+scenarioNo+"&ecareVo.ecareNo="+ecareNo+"&depthNo="+depthNo+"&ecareVo.channelType="+channelType;
                    return;
                } else {
                    if(!confirm("<spring:message code='ecare.alert.msg.change.2_${webExecMode}'/>")) {
                        return;
                    }

                    // /ecare/ecare_3step_form.do
                    window.location.href="/ecare/ecare2Step.do?scenarioNo="+scenarioNo+"&ecareVo.ecareNo="+ecareNo+"&depthNo="+depthNo+"&channelType="+channelType;
                    return;
                }
            } else if(ecareSts == 'P' || ecareSts == 'T') {
                if(!confirm("<spring:message code='ecare.alert.msg.change.3'/>")) {
                    return;
                }

                $("#ecareSts").val("R");
            } else if(ecareSts == 'R') {
                if(!confirm("<spring:message code='ecare.alert.msg.change.4'/>")) {
                    return;
                }
                $("#ecareSts").val("P");
            }

            $("#actionFrm").attr('action', '/ecare/changeEcareSts.do').submit();
        });

        // 서비스 점검 콤보박스 선택
        $("#serviceCheck").on("change", function(event) {
            var checkYn = $('#serviceCheck option:selected').val();
            if(checkYn === 'Y') {
                if(!confirm('<spring:message code="ecare.check.confirm.1_${webExecMode}"/>')) {
                    return;
                }
            } else if(checkYn === 'N') {
                if(!confirm('<spring:message code="ecare.check.confirm.2_${webExecMode}"/>')) {
                    return;
                }
            } else {
                return;
            }

            $("#actionFrm").attr('action', '/ecare/checkService.do?serviceCheckYn=' + checkYn).submit();
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
            event.preventDefault();
            if(document.actionFrm.searchColumn.value == "ecareNo") {
                var word = $("#searchWord").val();
                if($.mdf.isBlank(word) || isNaN(word)) {
                    alert("<spring:message code='ecare.alert.msg.select.1_${webExecMode}'/>");
                    return;
                }
            }

            $("#actionFrm").attr('action', '/ecare/ecareList.do').submit();  // /ecare/ecare.do
        });
    }

    // 태그 검색
    function goTagSearch(tagNo) {
        $("#tagNo").val(tagNo);
        $("#actionFrm").attr('action', '/ecare/ecareList.do').submit();  // /ecare/ecare.do
    }

    // 이케어명 클릭
    function goView(scenarioNo,ecareNo,depthNo,channelType) {
        // /ecare/ecare_step_form.do
        window.location.href="/ecare/ecare2Step.do?scenarioNo="+scenarioNo+"&ecareVo.ecareNo="+ecareNo+"&depthNo="+depthNo+"&ecareVo.channelType="+channelType;
    }

    // 이케어 선택시 Hidden 값 셋팅
    function ecareValue(scenarioNo, ecareLevel, ecareSts, depthNo, channelType, segmentNo, serviceType, relationType){
        $("#scenarioNo").val(scenarioNo);
        $("#ecareLevel").val(ecareLevel);
        $("#ecareSts").val(ecareSts);
        $("#depthNo").val(depthNo);
        $("#channelType").val(channelType);
        $("#segmentNo").val(segmentNo);
        $("#checkedServiceType").val(serviceType);
        $("#relationType").val(relationType);
    }

    // 정렬
    function goOrder(column){
        if($("#orderSort").val() == '' || $("#orderSort").val() == "ASC") {
            $("#orderSort").val("DESC");
        } else {
            $("#orderSort").val("ASC");
        }

        $("#actionFrm").attr('action', '/ecare/ecareList.do').submit();  // /ecare/ecare.do
    }

    // 검증 용 일시정지 시 마임 미리보기
    function goSuspendMimeView(channelType, serviceNo) {
        var url = "/common/suspendMimeList.do?cmd=suspendMimeList&channelType="+channelType+"&serviceNo="+serviceNo;
        var test2 = window.open(url, 'suspend', "scrollbars=yes,toolbar=no,width=820,height=600,resizable=no,menubar=no");
        if(window.focus) {
            test2.focus();
        }
    }

    // 핸들러/템플릿 다운로드 버튼 클릭
    function downloadContents(contentsType){
        $("#contentType").val(contentsType);
        $("#actionFrm").attr('action', '/ecare/downloadContents.do').submit();
    }
</script>
</head>

<body>
<form id="actionFrm" name="actionFrm" method="post">
    <input type="hidden" name="cmd" id="cmd" />
    <input type="hidden" name="scenarioNo" id="scenarioNo" />
    <input type="hidden" name="ecareLevel" id="ecareLevel" />
    <input type="hidden" name="ecareSts" id="ecareSts" />
    <input type="hidden" name="depthNo" id="depthNo" />
    <input type="hidden" name="channelType" id="channelType" />
    <input type="hidden" name="orderSort" id="orderSort" value="${orderSort}" />
    <input type="hidden" name="orderColumn" id="orderColumn" value="${orderColumn}" />
    <input type="hidden" name="segmentNo" id="segmentNo" />
    <input type="hidden" name="checkedServiceType" id="checkedServiceType" />
    <input type="hidden" name="relationType" id="relationType" />
    <input type="hidden" name="tagNo" id="tagNo" />
    <input type="hidden" name="contentType" id="contentType" />
    <input type="hidden" name="webExecMode" id="webExecMode" value="${webExecMode}" />
    <input type="hidden" name="nowPage" id="nowPage" value="${nowPage}" />

    <div class="main-panel">
        <div class="container-fluid mt-4 mb-2"><!-- Page content -->
            <div class="card">
                <div class="card-header"><!-- title -->
                    <h3 class="mb-0"><spring:message code="ecare.list_${webExecMode}"/></h3>
                </div>

                <div class="card-body px-0 pb-0">
                    <div class="row align-items-center py-1 table_option">
                        <div class="col-7"><!-- buttons -->
                            <c:if test="${sessionScope.write eq 'W'}">
                                <button id="deleteBtn" class="btn btn-sm btn-outline-primary btn-round">
                                    <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                                </button>
                                <button id="targetRefreshBtn" class="btn btn-sm btn-outline-primary btn-round">
                                    <i class="fas fa-sync-alt"></i> <spring:message code="button.target.refresh"/><!-- 대상자 갱신 -->
                                </button>
                            </c:if>
                            <c:if test="${execPerm eq 'X' and webExecMode ne 2}">
                                <button id="statusChangeBtn" class="btn btn-sm btn-outline-primary btn-round">
                                    <i class="fas fa-traffic-light"></i> <spring:message code="button.change.status"/><!-- 상태변경 -->
                                </button>
                            </c:if>
                            <c:if test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'A' and webExecMode ne 2}">
                                <button onclick="downloadContents('H')" class="btn btn-sm btn-outline-primary btn-round">
                                    <i class="fas fa-download"></i> <spring:message code="button.handler"/> <spring:message code="button.download"/><!-- 핸들러 다운로드 -->
                                </button>
                                <button onclick="downloadContents('T')" class="btn btn-sm btn-outline-primary btn-round">
                                    <i class="fas fa-download"></i> <spring:message code="button.template"/> <spring:message code="button.download"/><!-- 템플릿 다운로드 -->
                                </button>
                                <select class="form-control form-control-sm font-size-13" id="serviceCheck" name="serviceCheckYn">
                                    <option value=''><spring:message code="ecare.check.service"/></option>
                                    <option value='Y'><spring:message code="ecare.check.start"/></option>
                                    <option value='N'><spring:message code="ecare.check.end"/></option>
                                </select>
                            </c:if>
                        </div>

                        <div class="col-5 searchWrap"><!-- search -->
                            <c:if test="${ webExecMode ne 2}">
                            <div class="form-group searchbox pl-0">
                                <select class="form-control form-control-sm" id="selectEcareSts" name="ecareVo.ecareSts"">
                                    <option value=''><spring:message code="ecare.menu.status"/></option>
                                    <c:forEach var="loop" items="${ecareStsList}">
                                        <option value="${loop.cd}" <c:if test="${ecareScenarioVo.ecareVo.ecareSts eq loop.cd}">selected</c:if>>${loop.val}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group searchbox pl-1">
                                <select class="form-control form-control-sm" id="selectServiceType" name="serviceType"">
                                    <option value=''><spring:message code="ecare.table.type"/></option>
                                    <c:forEach var="loop" items="${ecareServiceTypeList}">
                                        <option value="${loop.cd}" <c:if test="${ecareScenarioVo.serviceType.concat(ecareScenarioVo.subType) eq loop.cd}">selected</c:if>>${loop.val}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            </c:if>
                            <div class="form-group searchbox search_input pl-1">
                                <select class="form-control form-control-sm" name="searchColumn" id="searchColumn">
                                    <option value="scenarioNm" <c:if test="${param.searchColumn eq 'scenarioNm'}">selected</c:if>><spring:message code="ecare.table.sname_${webExecMode}"/></option>
                                    <option value="ecareNo" <c:if test="${param.searchColumn eq 'ecareNo'}">selected</c:if>><spring:message code="ecare.menu.no_${webExecMode}"/></option>
                                </select>
                                <!-- form에 input text 가 한개만 있으면 enter값에 자동으로 submit 됨.. enter 이벤트를 위해 의미없는 값 추가-->
                                <input type="text" name="gabarge" style="display: none;"/>
                                <input type="text" class="form-control form-control-sm" name="searchWord" id="searchWord"
                                    value="${param.searchWord}" maxlength="30" style="width:120px;"/>
                                <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </div>
                        </div><!-- //search -->
                    </div><!-- e.search area & buttons -->
                </div><!-- e.card body -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable table-hover table-fixed">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="45"><spring:message code="ecare.table.select"/></th><!-- 선택 -->
                            <th scope="col" width="8%" id="orderNo"><spring:message code="ecare.menu.no_${webExecMode}" /></th><!-- 이케어번호 -->
                            <th scope="col" width="*"><spring:message code="ecare.menu.cname_${webExecMode}"/></th><!-- 이케어명 -->
                            <th scope="col" width="5%"><spring:message code="ecare.menu.channel"/></th><!-- 채널 -->
                            <th scope="col" width="7%"><spring:message code="ecare.menu.status"/></th><!-- 수행상태 -->

                            <c:if test="${ webExecMode ne 2}"><!-- 유형 -->
                            <th scope="col" width="8%"><spring:message code="ecare.table.type"/></th>
                            </c:if>

                            <th scope="col" width="8%"><spring:message code="common.menu.ctarget"/></th><!-- 대상자수 -->
                            <th scope="col" width="8%">
                                <c:choose>
                                    <c:when test="${webExecMode eq 2}"><spring:message code="ecare.menu.sdate"/><!-- 발송일시 --></c:when>
                                    <c:otherwise><spring:message code="ecare.table.stime"/><!-- 발송시간 --></c:otherwise>
                                </c:choose>
                            </th>
                            <th scope="col" width="10%"><spring:message code="ecare.menu.group"/></th><!-- 담당부서 -->
                            <th scope="col" width="9%"><spring:message code="ecare.menu.user"/></th><!-- 담당자 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="loop" items="${ecareScenarioList}" varStatus="i">
                        <tr style="cursor: pointer;" onclick="javascript:goView('${loop.scenarioNo}','${loop.ecareVo.ecareNo}','${loop.ecareVo.depthNo}','${loop.ecareVo.channelType}')">
                            <th scope="row" onclick="event.cancelBubble=true"><!-- 선택 -->
                                <div class="custom-control custom-radio">
                                    <input type="radio" class="custom-control-input" name="ecareNo" value="${loop.ecareVo.ecareNo}" id="radio_${i.count}"
                                        onclick="javascript:ecareValue(${loop.scenarioNo},'${loop.ecareVo.ecareLevel}','${loop.ecareVo.ecareSts}','${loop.ecareVo.depthNo}','${loop.ecareVo.channelType}','${loop.ecareVo.segmentNo}','${loop.serviceType}${loop.subType}', '${loop.ecareVo.relationType}');" />
                                    <label class="custom-control-label" for="radio_${i.count}"></label>
                                </div>
                            </th>
                            <td>${loop.ecareVo.ecareNo}</td><!-- 이케어번호 -->
                            <td class="text-left"><!-- 이케어명 -->
                                <c:if test="${loop.ecareVo.depthNo > 1}">
                                    <c:forEach var="depthNo" begin="1" end="${loop.ecareVo.depthNo}" step="1">&nbsp;</c:forEach><!-- Depth별 들여쓰기. 재발송 아이콘은 Depth 2부터 -->
                                    <img src="/images/common/relation/type_${loop.ecareVo.relationType}.png" style="vertical-align: middle"/>
                                </c:if>
                                ${loop.scenarioNm}
                            </td>
                            <td><!-- 채널 -->
                                <em class="txt_channel ${loop.ecareVo.channelType}">
                                <c:choose>
                                    <c:when test="${loop.ecareVo.channelType eq 'M'}">E</c:when>
                                    <c:when test="${loop.ecareVo.channelType eq 'T'}">M</c:when>
                                    <c:otherwise>${loop.ecareVo.channelType}</c:otherwise>
                                </c:choose>
                                </em>
                            </td>
                            <td <c:if test="${loop.ecareVo.ecareSts eq 'S'}">onclick="event.cancelBubble=true"</c:if>><!-- 수행상태 -->
                                <c:if test="${loop.ecareVo.ecareSts eq 'S'}"><a href="javascript:goSuspendMimeView('EC','${loop.ecareVo.ecareNo}')">${loop.ecareVo.ecareStsNm}</a></c:if>
                                <c:if test="${loop.ecareVo.ecareSts ne 'S'}">${loop.ecareVo.ecareStsNm}</c:if>
                            </td>

                            <c:if test="${ webExecMode ne 2}">
                            <td>${loop.serviceTypeNm}</td><!-- 유형 -->
                            </c:if>

                            <td class="text-right" id="targetCnt_${loop.ecareVo.ecareNo}"><!-- 대상자수 -->
                                <c:if test="${loop.serviceType ne 'R' and loop.ecareVo.relationType ne 'S' and loop.ecareVo.relationType ne 'F' and loop.ecareVo.relationType ne 'O'}">
                                    <fmt:formatNumber type="number" value="${loop.ecareVo.targetCnt}" />
                                    <c:if test="${not empty loop.ecareVo.targetCnt}"><spring:message code="common.menu.persons"/></c:if>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${ webExecMode eq 2}">
                                        <c:if test="${loop.ecareVo.ecareScheduleVo.sendStartDt ne null}">
                                            <fmt:parseDate value="${loop.ecareVo.ecareScheduleVo.sendStartDt}" var="sendStartDt" pattern="yyyyMMdd" />
                                            <fmt:formatDate value="${sendStartDt}" pattern="yyyy-MM-dd" /><br />
                                        </c:if>
                                        <fmt:parseDate value="${loop.ecareVo.ecareScheduleVo.invokeTm}" var="invokeTm" pattern="HHmmss" />
                                        <fmt:formatDate value="${invokeTm}" pattern="HH:mm:ss" /><!-- 발송일시 -->
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:parseDate value="${loop.ecareVo.ecareScheduleVo.invokeTm}" var="invokeTm" pattern="HHmmss" />
                                        <fmt:formatDate value="${invokeTm}" pattern="HH:mm:ss" /><!-- 발송시간 -->
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${loop.brcNm}</td><!-- 담당부서 -->
                            <td>${loop.chrgNm}</td><!-- 담당자 -->
                        </tr>
                        </c:forEach>

                        <c:if test="${empty ecareScenarioList}">
                        <tr>
                            <td colspan="10"><spring:message code="common.msg.nodata"/></td>
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
</html>