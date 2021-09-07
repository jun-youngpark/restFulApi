<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>메시지 작성(2단계)] 대상자 선택 (팝업)
 * - [이케어>이케어 등록>메시지 작성(2단계)] 대상자 선택 (팝업)
 * - URL : /segment/segmentSelectPopup.do <br/>
 * - Controller : com.mnwise.wiseu.web.segment.bookmark.web.BookmarkController <br/>
 * - 이전 파일명 : bookmark_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="segment.msg.starget"/></title><!-- 대상자 선택 -->
<%@ include file="/jsp/include/plugin.jsp" %>
<script src="/js/segment/target.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 확정 버튼 클릭
        $("#goReg").on("click", function(event) {
            var segmentNm = $("#searchForm input[name=segmentNm]").val();
            var segmentNo = $("#searchForm input[name=segmentNo]").val();
            var parentObj = (window.dialogArguments == undefined) ? opener : window.dialogArguments;
            var type = $("#searchForm input[name=type]").val();
            if(type === 'EC'){
                var ecareNo = $("#searchForm input[name=serviceNo]").val();
                var param = {
                     ecareNo : ecareNo,
                     segmentNo : segmentNo
                 };
                // 선택한 대상자 정보를 현재 이케어의 대상자로 저장한다
                 $.post("/ecare/updateTargetChgToOrg.json", $.param(param, true), function(result) {
                     if(result.code == "OK") {
                         window.opener.location.reload();
                         window.close();
                     }
                 });
            }else{
                if(segmentNm != '') {
                    parentObj.$("input[name='segmentNm']").setValue(segmentNm);
                    parentObj.$("input[name='segmentSize']").setValue($("#searchForm input[name=segmentSize]").val());
                    parentObj.$("input[name='segType']").setValue($("#searchForm input[name=segType]").val());
                    parentObj.$("input[name='segmentNo']").setValue(segmentNo);
                    parentObj.$("input[name='segmentSize']").removeClass("error");
                    parentObj.$("#segmentSize-error").remove();
                    if($("#searchForm input[name=type]").val() != 'MD') {
                           parentObj.editorIfrm.setSemantic(segmentNo);
                    } else {
                        //멀티 디바이스일 경우 Form에 개인화정보가 있기 때문에 Form에 개인화정보 세팅
                        parentObj.setSemantic(segmentNo);
                    }
                }
                window.close();
            }

        });

        // 닫기 버튼 클릭
        $("#close").on("click", function(event) {
            window.close();
        });

        // 유형 콤보박스 선택
        $("select[name=bookmarkKind]").on("change", function(event) {
            $("#searchForm").attr('action', '/segment/segmentSelectPopup.do').submit();  // /bookmark/bookmark_popup.do
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
            $("#bookmarkFrm input[name=searchColumn]").val($("input[id='searchColumn']").val());
            $("#bookmarkFrm input[name=searchWord]").val($("input[id='searchWord']").val());
            $("#bookmarkFrm input[name=bookmarkKind]").val($("#searchForm select[name=bookmarkKind]").val());
            $("#bookmarkFrm input[name=cmd]").val('list');
            $("#bookmarkFrm input[name=page]").val('1');
            $("#bookmarkFrm").attr('action', '/segment/segmentSelectPopup.do').submit();  // /bookmark/bookmark_popup.do
            return false;
        });
    }

    function initPage() {
        $("#searchForm select[name=bookmarkKind]").val("${bookmarkKind}");
    }

    //대상자 보기 버튼 클릭
    function targetList(segmentNo, segType) {
        if(segmentNo == undefined || segmentNo == 0) {
            alert("<spring:message code='segment.alert.msg.exec.2'/>");
            return false;
        }

        popupTargetList(segmentNo, segType);
        return false;
    };

    function choice(bookmarkNo, bookmarkNm, name, bookmarkSize, lastUpdateDt, segType) {
        var bookmarkNmData = bookmarkNm.replace(/```/g, "'")
        $("#searchForm input[name=segmentNm]").val(bookmarkNmData);
        $("#searchForm input[name=segmentNo]").val(bookmarkNo);
        $("#searchForm input[name=segmentSize]").val(bookmarkSize);
        $("#searchForm input[name=lastUpdateDt]").val(lastUpdateDt);
        $("#searchForm input[name=nameKor]").val(name);
        $("#searchForm input[name=segType]").val(segType);
    }

    function selectCheck(count) {
        $("#chk_"+count).trigger('click');
    }
</script>
</head>

<body>
<div class="card pop-card d-block overflow-y-auto">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="segment.msg.starget"/></h5></div><!-- 대상자 선택 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <form id="searchForm" name="searchForm" action="/target/segmentSelectPopup.do" method="post"><!-- /target/bookmark_popup.do -->
        <input type="hidden" name="serviceType" value="${serviceType}"/>
        <input type="hidden" name="scenarioNo" value="${scenarioNo}">
        <input type="hidden" name="channelType" value="${channelType}">
        <input type="hidden" name="serviceNo" value="${serviceNo}">
        <input type="hidden" name="type" value="${type}">
        <input type="hidden" name="segType"/>
        <input type="hidden" name="segmentNo"/>
        <input type="hidden" name="nameKor"/>
        <input type="hidden" name="segmentSize" />
        <input type="hidden" name="lastUpdateDt"/>
        <input type="hidden" name="segmentNm"/>

        <div class="row align-items-center py-1 table_option">
            <div class="col searchWrap"><!-- search -->
                <div class="form-group searchbox search_input">
                    <span class="form-control-label txt"><spring:message code="common.menu.type"/></span><!-- 유형 -->
                    <select name="bookmarkKind" class="form-control form-control-sm">
                        <option value="A"><spring:message code="common.menu.all"/></option>
                        <option value="F"><spring:message code="segment.type.F"/></option>
                        <option value="S"><spring:message code="segment.type.S"/></option>
                    </select>
                </div>
                <div class="form-group searchbox search_input">
                    <input type="text" class="form-control form-control-sm" name="searchWord" id="searchWord" maxlength="30" value="${param.searchWord}" placeholder="<spring:message code='segment.menu.sql.target'/>"/>
                    <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                        <spring:message code="button.search"/><!-- 검색 -->
                    </button>
                </div>
            </div><!-- //search -->
        </div><!-- e.search area & buttons -->

        <div class="table-responsive">
            <table class="table table-sm dataTable table-hover table-fixed">
                <thead class="thead-light">
                <tr>
                    <th scope="col" width="45"><spring:message code="common.option.select"/></th><!-- 선택-->
                    <th scope="col" width="8%"><spring:message code="common.menu.no"/></th><!-- 대상자번호-->
                    <th scope="col" width="*"><spring:message code="segment.menu.sql.target"/></th><!-- 대상자명-->
                    <th scope="col" width="15%"><spring:message code="common.menu.creator"/></th><!-- 작성자-->
                    <th scope="col" width="11%"><spring:message code="common.menu.ctarget"/></th><!-- 대상자수-->
                    <th scope="col" width="10%"><spring:message code="common.menu.cdate"/></th><!-- 작성일-->
                    <th scope="col" width="90"><spring:message code="button.target.view"/></th><!-- 대상자 보기-->
                </tr>
                </thead>
                <tbody>
                <c:if test="${!empty bookmarkList}">
                <c:forEach var="bookmarkVo" items="${bookmarkList}" varStatus="i">
                <tr>
                    <th scope="row"><!-- 선택-->
                        <div class="custom-control custom-radio">
                            <input type="radio" id="chk_${i.count}" name="bookmark" class="custom-control-input" onclick="choice('${bookmarkVo.segmentNo}', '${fn:replace(bookmarkVo.segmentNm, '\'', '```')}', '${bookmarkVo.nameKor}', '${bookmarkVo.segmentSize}', '${bookmarkVo.lastUpdateDt}', '${bookmarkVo.segType}');"/>
                            <label class="custom-control-label" for="chk_${i.count}"><span class="hide">check</span></label>
                        </div>
                    </th>
                    <td onclick="selectCheck(${i.count})">${bookmarkVo.segmentNo}</td><!-- 대상자번호-->
                    <td class="text-left" onclick="selectCheck(${i.count})">${bookmarkVo.segmentNm}</td><!-- 대상자명-->
                    <td onclick="selectCheck(${i.count})">${bookmarkVo.nameKor}</td><!-- 작성자-->
                    <td class="text-right" onclick="selectCheck(${i.count})"><fmt:formatNumber type="number" value="${bookmarkVo.segmentSize}" /></td><!-- 대상자수-->
                    <td onclick="selectCheck(${i.count})"><!-- 작성일-->
                        <fmt:parseDate value="${bookmarkVo.lastUpdateDt}" var="lastUpdateDt" pattern="yyyymmdd" />
                        <fmt:formatDate value="${lastUpdateDt}" pattern="yy-mm-dd" />
                    </td>
                    <td onclick="selectCheck(${i.count})">
                        <button type="button" class="btn btn-xs btn-outline-primary" onclick="targetList('${bookmarkVo.segmentNo}','${bookmarkVo.segType}'); return false;">
                            <spring:message code="button.target.view"/><!-- 대상자 보기 -->
                        </button>
                    </td>
                </tr>
                </c:forEach>
                </c:if>

                <c:if test="${empty bookmarkList}" >
                <tr>
                    <td colspan="7"><spring:message code="common.msg.nodata"/></td><!-- 검색결과가 없습니다. -->
                </tr>
                </c:if>
                </tbody>
            </table>
        </div><!-- //Light table -->
        </form>

        <div class="mt-3 mb-0"><!-- 페이징 -->
            <c:import url="/common/page.do">
                <c:param name="viewPath" value="/common/page"/>
                <c:param name="actionPath" value="/segment/segmentSelectPopup.do"/><%-- /bookmark/bookmark_popup.do --%>
                <c:param name="total" value="${totalCount}"/>
                <c:param name="countPerPage" value="${countPerPage}"/>
                <c:param name="nowPage" value="${nowPage}"/>
                <c:param name="tagNo" value="${tagNo}"/>
                <c:param name="hiddenParam" value="type:${type}"/>
                <c:param name="hiddenParam" value="bookmarkKind:${bookmarkKind}"/>
                <c:param name="hiddenParam" value="countPerPage:${countPerPage}"/>
                <c:param name="hiddenParam" value="searchColumn:${searchColumn}"/>
                <c:param name="hiddenParam" value="searchWord:${searchWord}"/>
                <c:param name="hiddenParam" value="serviceNo:${serviceNo}"/>
                <c:param name="hiddenParam" value="serviceType:${serviceType}"/>
                <c:param name="hiddenParam" value="scenarioNo:${scenarioNo}"/>
            </c:import>
        </div>
    </div><!-- e.card body -->

    <div class="card-footer pt-0">
        <button type="button" class="btn btn-outline-primary" id="goReg">
            <spring:message code="button.fix" /><!-- 확정 -->
        </button>
    </div>
</div>

<form id="bookmarkFrm" name="bookmarkFrm" action="/target/segmentSelectPopup.do" method="post" class="mb-0"><!-- /target/bookmark_popup.do -->
    <input type="hidden" name="searchColumn" value="segmentNm" id="searchColumn"/>
    <input type="hidden" name="searchWord" />
    <input type="hidden" name="cmd" />
    <input type="hidden" name="bookmarkKind" />
    <input type="hidden" name="segmentNo" value="0"/>
    <input type="hidden" name="serviceType" value="${serviceType}"/>
    <input type="hidden" name="channelType" value="${channelType}"/>
    <input type="hidden" name="type" value="${type}"/>
    <input type="hidden" name="page" value="${nowPage}"/>
</form>
</body>
</html>