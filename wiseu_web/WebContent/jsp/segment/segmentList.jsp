<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 조회] 대상자 리스트 <br/>
 * - URL : /segment/segmentList.do <br/>
 * - Controller :com.mnwise.wiseu.web.segment.web.SegmentListController <br/>
 * - [대상자>대상자 조회] 대상자 리스트 - 삭제  <br/>
 * - 20100712 : 대상자 갱신시에 에러날경우 에러메시지를 리턴하도록 한다(errorMsg추가) ecare_list.jsp,SegmentServiceImpl.java,SegmentVo.java 같이 수정됨
 * - 20100825 : 대상자 삭제시에 대상자가 사용중이거나 하위대상자가 존재하면 active_yn 값을 N으로 변경 되었던 것을 변경 안하고 관련 메시지 출력하도록 변경
 * - 20101110 : 태그검색 현재페이지가 리턴되어 검색이 제대로 되지 않아 파라미터중 nowPage 값을 제거.
 * - 이전 파일명 : segment_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="segment.msg.lists"/></title><!-- 대상자 리스트 -->
<script src="/js/segment/target.js"></script>
<script type="text/javascript">
    var segNo = 0;

    // 대상자 갱신
    var targetStatus = false;
    var targetSegment = 0;

    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            var cnt = 0;
            var userid = $("#userId").val();
            var editorId = "";
            var editorName = "";
            $("input[name='radioSegmentNo']:radio:checked").each(function() {
                segNo = this.value;
                segmentNo = segNo;
                cnt++;
            });

            editorId = $("#user_" + segNo).val();
            editorName = $("#name_" + segNo).val();

            if(cnt == 1) {
                if(userid != editorId && 'A' != '${sessionScope.adminSessionVo.userVo.userTypeCd}') {
                    alert("'" + editorId + "'(" + editorName + ") "+'<spring:message code="segment.alert.msg.exec.4"/>');  // 계정만 삭제 가능 합니다.
                    return;
                } else {
                    $.post("/segment/deleteCheck.json?segmentNo=" + segNo, null, function(result) {
                        if(result.code = "OK") {
                            if(result.value == "0") {
                                if(confirm('<spring:message code="template.alert.msg.2"/>')) {  // 삭제하시겠습니까?
                                    var url = "/segment/deleteSegment.do?segmentNo="+segNo;
                                    document.location.href = url;
                                }
                            } else if(result.value == "1") {
                                alert('<spring:message code="segment.alert.msg.del1"/>');  // 하위 대상자가 존재하여 삭제할 수 없습니다.
                            } else if(result.value == "2") {
                                alert('<spring:message code="segment.alert.msg.del2"/>');  // 서비스에서 사용중인 대상자는 삭제할 수 없습니다.
                            } else if(result.value == "3") {
                                alert('<spring:message code="segment.alert.msg.del3"/>');  // 서비스에서 사용중이며 하위 대상자가 존재하여 삭제할 수 없습니다.
                            } else {
                                var url = "/segment/updateUnactive.do?segmentNo="+segNo;
                                document.location.href = url;
                            }
                        }
                    });
                }
            } else {
                alert('<spring:message code="segment.alert.msg.exec.1"/>');  // 하나의 대상자를 선택을 하셔야 합니다.
            }
        });

        // 수행이력 버튼 클릭
        $("#usingHisBtn").on("click", function(event) {
            var segmentNo = "";
            var cnt = 0;
            $("input[name='radioSegmentNo']:radio:checked").each(function() {
                var segNo = this.value;
                segmentNo = segNo;
                cnt++;
            });

            if(cnt == 1) {
                var url = '/segment/usingHistoryPopup.do?segmentNo='+segmentNo ;  // /segment/popupUsing.do
                $.mdf.popupGet(url, 'usingPopup', 618, 400);
            } else {
                alert('<spring:message code="segment.alert.msg.exec.1"/>');  // 하나의 대상자를 선택을 하셔야 합니다.
            }
        });

        // 권한할당 버튼 클릭
        $("#permitAssignBtn").on("click", function(event) {
            var segNo = "";
            var segType = "";
            var cnt = 0;

            $("input[name='radioSegmentNo']:radio:checked").each(function() {
                segNo = this.value;
                segType = $(this).attr("segType");
                cnt++;
            });

            if(cnt == 1) {
                if(segType == "S") {
                    alert('<spring:message code="segment.alert.msg.exec.5"/>');  // 하위 대상자는 권한 할당을 할 수 없습니다.
                    return;
                }

                var url = '/segment/permitAssignPopup.do?segmentNo='+segNo;  // /segment/popupPermission.do
                $.mdf.popupGet(url, 'permitPopup', 420, 520);
            } else if(cnt > 1) {
                alert('<spring:message code="segment.alert.msg.exec.1"/>');  // 하나의 대상자를 선택을 하셔야 합니다.
            } else {
                alert('<spring:message code="segment.alert.msg.exec.2"/>');  // 대상자를 선택을 하셔야 합니다
            }
        });

        // 복사 버튼 클릭
        $("#copyBtn").on("click", function(event) {
            var segNo = "";
            var cnt = 0;
            $("input[name='radioSegmentNo']:radio:checked").each(function() {
                segNo = this.value;
                cnt++;
            });

            if(cnt == 1) {
                document.location.href = '/segment/copySegment.do?segmentNo=' + segNo;
            } else if(cnt > 1) {
                alert('<spring:message code="segment.alert.msg.exec.6"/>');  // 복사 할 대상자를 하나 만 선택을 하셔야 합니다.
            } else {
                alert('<spring:message code="segment.alert.msg.exec.7"/>');  // 복사 할 대상자를 선택을 하셔야 합니다.
            }
        });

        // 대상자 갱신 버튼 클릭
        $("#targetRefreshBtn").on("click", function(event) {
            if(targetStatus) {
                alert(targetSegment+'<spring:message code="segment.alert.msg.exec.8"/>');  // 번 대상자의 대상자 갱신이 진행중입니다.
                return;
            }

            if($("#actionFrm input:radio[name=radioSegmentNo]").is(":checked") == false) {
                alert('<spring:message code="segment.alert.msg.exec.9"/>');  // 대상자 수를 갱신할 대상자를 선택하세요.
                return;
            }

            var cnt = 0;
            var segNo = 0;
            $("input[name='radioSegmentNo']:radio:checked").each(function() {
                segNo = this.value;
                cnt++;
            });

            if(cnt != 1) {
                alert('<spring:message code="segment.alert.msg.exec.10"/>');  // 하나의 대상자만 선택해 주세요
                return;
            }

            $("#segSize_"+segNo).html("<img src='/images/ajax/plugin/wait_small_fbisk.gif' style='vertical-align: middle; margin-bottom: 2px;' />");

            targetStatus = true;
            targetSegment = segNo;

            $.post("/segment/renewalSegment.json?segmentNo=" + segNo, null, function(segment) {
                if($.mdf.isBlank(segment.errorMsg)) {
                    $("#segSize_" + segment.segmentNo).html("<img src='/images/common/check.png' style='width:15px;height:15px' />"+$.number(segment.segmentSize));

                    $(this).delay(2000, function() {
                        $("#segSize_" + segment.segmentNo).html($.number(segment.segmentSize) + "");
                    });
                } else {
                    alert(segment.errorMsg);
                }

                targetStatus = false;
                targetSegment = 0;
            });
        });

        // 대상자 보기 버튼 클릭
        $("#targetViewBtn").on("click", function(event) {
            var segmentNo = "";
            var cnt = 0;
            var segType = "";
            $("input[name='radioSegmentNo']:radio:checked").each(function() {
                var segNo = this.value;
                segType = $(this).attr("segType");
                segmentNo = segNo;
                cnt++;
            });

            if(cnt == 1) {
                popupTargetList(segmentNo, segType);
            } else if(cnt > 1) {
                alert('<spring:message code="segment.alert.msg.exec.1"/>');  // 하나의 대상자를 선택을 하셔야 합니다.
            } else {
                alert('<spring:message code="segment.alert.msg.exec.2"/>');  // 대상자를 선택을 하셔야 합니다
            }
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
           $("#searchForm").submit();
           return false;
        });
    }

    // 태그 검색
    function goTagSearch(tagNo) {
        $("#actionFrm input[name=tagNo").val(tagNo);
        $("#actionFrm input[name=cmd]").val("list");

        $("#actionFrm").attr('action', '/segment/segmentList.do').submit();  // /segment/segment.do
    }

    function goView(segmentNo) {
        $("#actionFrm input[name=segmentNo]").val(segmentNo);
        $("#actionFrm").attr('action', '/segment/sqlSegment1Step.do').submit();  // /segment/sqlSegment1step.do
    }

</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="segment.msg.lists"/></h3>
            </div>

            <form id="searchForm" name="searchForm" action="/segment/segmentList.do" method="post"><!-- /segment/segment.do -->
            <input type="hidden" id="userId" value="${segmentVo.userId}" />
            <input type="hidden" name="searchColumn" value="SEGMENT_NM" id="id_searchColumn"/>
            <div class="card-body pb-0 px-0">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-7">
                        <c:if test="${sessionScope.write eq 'W'}">
                        <button class="btn btn-sm btn-outline-primary btn-round" id="deleteBtn">
                            <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                        </button>
                        </c:if>

                        <c:if test="${webExecMode eq '1' }">
                        <button class="btn btn-sm btn-outline-primary btn-round" id="usingHisBtn">
                            <i class="fas fa-history"></i> <spring:message code="button.history.execute"/><!-- 수행 이력 -->
                        </button>
                        </c:if>

                        <c:if test="${sessionScope.write eq 'W' and webExecMode eq '1' }">
                        <button class="btn btn-sm btn-outline-primary btn-round" id="permitAssignBtn">
                            <i class="fas fa-unlock-alt"></i> <spring:message code="button.permission.assign"/><!-- 권한 할당 -->
                        </button>
                        <button class="btn btn-sm btn-outline-primary btn-round" id="copyBtn">
                            <i class="fas fa-copy"></i> <spring:message code="button.copy"/><!-- 복사 -->
                        </button>
                        </c:if>

                        <button class="btn btn-sm btn-outline-primary btn-round" id="targetRefreshBtn">
                            <i class="fas fa-sync-alt"></i> <spring:message code="button.target.refresh"/><!-- 대상자 갱신 -->
                        </button>
                        <button class="btn btn-sm btn-outline-primary btn-round" id="targetViewBtn">
                            <i class="fas fa-file-alt"></i> <spring:message code="button.target.view"/><!-- 대상자 보기 -->
                        </button>
                    </div>
                    <div class="col-5 searchWrap">
                        <div class="form-group searchbox search_input">
                            <input type="search" class="form-control form-control-sm" aria-controls="datatable-basic" name="searchWord" id="searchWord" value="${param.searchWord}" placeholder="<spring:message code='segment.menu.sql.target'/>"/>
                            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                <spring:message code="button.search"/><!-- 검색 -->
                            </button>
                        </div>
                    </div><!-- //search -->
                </div><!-- e.search area & buttons -->
            </div><!-- e.card body -->
            </form>

            <div class="table-responsive">
                <form id="actionFrm" name="actionFrm" action="/segment/segmentList.do" method="post"><!-- /segment/segment.do -->
                <input type="hidden" name="cmd" id="cmd" />
                <input type="hidden" name="tagNo" id="tagNo" />
                <input type="hidden" name="segmentNo" id="segmentNo" />
                <input type="hidden" name="searchWord" value="${searchWord}">
                <input type="hidden" name="searchColumn" value="${searchColumn}">
                <input type="hidden" name="orderColumn" value="${orderColumn}">
                <input type="hidden" name="orderSort" value="${orderSort}">
                <input type="hidden" name="countPerPage" value="${countPerPage}">

                <table class="table table-sm dataTable table-hover table-fixed">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 선택 -->
                        <th scope="col" width="6%" id="SEGMENT_NO"><spring:message code="common.menu.no"/></th><!-- No -->
                        <!-- 태그  display 여부 css파일 dp-tag 수정 -->
                        <th scope="col" width="10%" id="TAG_NO" class="dp-tag"><spring:message code="common.menu.tag"/></th><!-- 태그 -->
                        <th id="SEGMENT_NM"><spring:message code="segment.menu.sql.target"/></th><!-- 대상자명 -->
                        <th scope="col" width="9%"><spring:message code="segment.menu.sql.ctarget"/></th><!-- 대상자수 -->
                        <th scope="col" width="60"><spring:message code="common.menu.type"/></th><!-- 유형 -->
                        <c:if test="${webExecMode eq '1' }">
                        <th scope="col" width="90"><spring:message code="segment.menu.sql.adep"/></th><!-- 할당부서수 -->
                        </c:if>
                        <th scope="col" width="60"><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                        <th scope="col" width="10%"><spring:message code="common.menu.creator"/></th><!-- 작성자 -->
                        <th scope="col" width="10%"><spring:message code="common.menu.cdate"/></th><!-- 작성일 -->
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${!empty segmentList}">
                    <c:forEach var="segmentVo" items="${segmentList}" varStatus="status">
                    <tr <c:choose>
                            <c:when test="${'F' eq segmentVo.segType}">onclick="javascript:popupTargetList('${segmentVo.segmentNo}', 'F')"</c:when>
                            <c:when test="${'F' ne segmentVo.segType}">onclick="javascript:goView('${segmentVo.segmentNo}')"</c:when>
                        </c:choose>>
                        <th scope="row" onclick="event.cancelBubble=true"><!-- 선택 체크박스-->
                            <div class="custom-control custom-radio">
                                <input type="radio" id="radio_${status.count}" class="custom-control-input" name="radioSegmentNo" value="${segmentVo.segmentNo}" segType="${segmentVo.segType}"/>
                                <label class="custom-control-label" for="radio_${status.count}"></label>
                            </div>
                        </th>
                        <td>${segmentVo.segmentNo}</td><!-- No -->
                        <td class="dp-tag">${segmentVo.tagNm}</td><!-- 태그 -->

                        <td class="text-left"><!-- 대상자명 -->
                            <c:choose>
                                <c:when test="${empty segmentVo.segmentNm}"></c:when>
                                <c:otherwise>${segmentVo.segmentNm}</c:otherwise>
                            </c:choose>

                            <c:if test="${segmentVo.fieldKey eq 'S'}">
                                <img src="/images/common/sms_icon.gif" />
                            </c:if>
                            <input type="hidden" name="fieldKey" value="${segmentVo.fieldKey}" />
                        </td>
                        <td class="text-right" id="segSize_${segmentVo.segmentNo}"><fmt:formatNumber type="number" value="${segmentVo.segmentSize}" /></td><!-- 대상자수 -->
                        <td id="${segmentVo.segmentNo}"><!-- 유형 -->
                            <c:choose>
                                <c:when test="${'F' eq segmentVo.segType}"><spring:message code="segment.type.F"/></c:when>
                                <c:when test="${'S' eq segmentVo.segType}"><spring:message code="segment.type.S"/></c:when>
                                <c:when test="${'Q' eq segmentVo.segType}"><spring:message code="segment.type.Q"/></c:when>
                                <c:when test="${'L' eq segmentVo.segType}"><spring:message code="segment.type.L"/></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>

                        <c:if test="${webExecMode eq '1' }">
                        <td>${segmentVo.permissionCount}</td><!-- 할당부서수 -->
                        </c:if>

                        <td>${segmentVo.shareYn}</td><!-- 공유유형 -->
                        <td><!-- 작성자 -->
                            ${segmentVo.nameKor}
                            <input type="hidden" id="user_${segmentVo.segmentNo}" value="${segmentVo.userId}" />
                            <input type="hidden" id="name_${segmentVo.segmentNo}" value="${segmentVo.nameKor}" />
                        </td>
                        <td><!-- 작성일 -->
                            <fmt:parseDate value="${segmentVo.lastUpdateDt}" var="lastUpdateDt" pattern="yyyymmdd" />
                            <fmt:formatDate value="${lastUpdateDt}" pattern="yy-mm-dd" />
                        </td>
                    </tr>
                    </c:forEach>
                    </c:if>

                    <c:if test="${empty segmentList}" >
                    <tr>
                        <td colspan="9"><spring:message code="common.msg.nodata"/></td><!-- 검색결과가 없습니다. -->
                    </tr>
                    </c:if>
                    </tbody>
                </table>
                </form>
            </div><!-- e.Light table -->

            <!-- 페이징 -->
            ${paging}
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>