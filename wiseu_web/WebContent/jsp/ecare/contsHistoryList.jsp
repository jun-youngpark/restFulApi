<%-------------------------------------------------------------------------------------------------
 * - [이케어>컨텐츠 변경이력] 컨텐츠 변경이력
 * - URL : /ecare/contsHistoryList.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.EcareContsHistoryController
 * - 이전 파일명 : ecare_conts_history.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code='ecare.conts.fixed'/></title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 비교 버튼 클릭
        $("#compareBtn").on("click", function(event) {
            var checkboxArr = $("input:checkbox[name=diff]:checked");
            if (checkboxArr.length !== 2) {
                alert($.i18n.prop("ecare.conts.twosel2"));  // 컨텐츠를 2개 선택해야합니다
                return false;
            }

            var contentType = $("input:radio[name=contentRadio]:checked").val();
            var contentArr = [];
            for (var i = 0; i < 2; i++) {
                var no = checkboxArr[i].value;
                var ecareNo = $("#diff-" + no + "-ecareNo").val();
                var seg = $("#diff-" + no + "-seg").val();
                var content = {"ecareNo": ecareNo, "seg": seg };

                if (contentType === 'T') {
                    content.tmplVer = $("#diff-" + no + "-version").val();
                } else {
                    content.handlerVer = $("#diff-" + no + "-version").val();
                }

                contentArr.push(content)
            }

            var url = "/ecare/contsComparePopup.do?contentType=" + contentType + "&contents=" + encodeURI(JSON.stringify(contentArr));  // /ecare/popupContsCompare.do
            $.mdf.popupGet(url, 'comparePopup', 1200, 700);
        });

        // 검색조건 엔터키 입력
        $("#searchForm").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            event.preventDefault();

            var rules = {
                ecareNo : {digits : true, maxlength : 15}
            };

            if($.mdf.validForm("#searchForm", rules) == false) {
                return;
            }

            $("#searchForm").attr('action', '/ecare/contsHistoryList.do').submit();  // /ecare/ecare_conts_history.do
        });
    }

    function initPage() {
        var contentRadio = '${contentRadio}';
        if(contentRadio == 'T' || contentRadio == null || contentRadio == 'undefined' || contentRadio == '') {
            $("input:radio[name=contentRadio][value=T]").attr("checked", true);
        } else {
            $("input:radio[name=contentRadio][value=H]").attr("checked", true);
        }
    }

    // 이케어명 링크 클릭
    function goView(ecareNo, seg, version, flag) { //컨텐츠 내용조회
        var url = "/ecare/contsViewPopup.do?&ecareNo=" + ecareNo + "&seg=" + seg + "&version=" + version + "&contsFlag=" + flag;  // /ecare/popupContsView.do
        $.mdf.popupGet(url, 'contsViewPopup', 1000, 700);
    }
</script>
</head>

<body>
<!-- Start Form for Search... -->
<form name="searchForm" id="searchForm" action="/ecare/contsHistoryList.do" method="post"><!-- /ecare/ecare_conts_history.do -->
    <input type="hidden" name="cmd" id="cmd" />
    <input type="hidden" name="compareYn" id="compareYn" value="${compareYn}">

    <div class="main-panel">
        <div class="container-fluid mt-4 mb-2"><!-- Page content -->
            <div class="card">
                <div class="card-header"><!-- title -->
                    <h3 class="mb-0"><spring:message code="ecare.conts.fixed"/></h3><!-- 컨텐츠 변경이력 -->
                </div>
                <div class="card-body">
                    <div class="table-responsive gridWrap">
                        <table class="table table-sm dataTable table-fixed" style="margin-bottom: 0px !important;">
                            <colgroup>
                                <col width="10%" />
                                <col width="*" />
                                <col width="10%" />
                                <col width="18%" />
                                <col width="10%" />
                                <col width="18%" />
                                <col width="80" />
                            </colgroup>
                            <tbody>
                            <tr>
                                <th scope="row"><em class="required">required</em><spring:message code="ecare.conts.service"/></th>
                                <td>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="contentRadio" id="contentTemplate" value="T" class="custom-control-input" />
                                        <label class="custom-control-label" for="contentTemplate"><spring:message code="ecare.conts.t"/></label><!-- 템플릿 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="contentRadio" id="contentHandler" value="H" class="custom-control-input" />
                                        <label class="custom-control-label" for="contentHandler"><spring:message code="ecare.conts.h"/></label><!-- 핸들러 -->
                                    </div>
                                </td>
                                <th scope="row"><spring:message code="ecare.menu.no_${webExecMode}"/></th>
                                <td><input type="text" class="form-control form-control-sm" name="ecareNo" value="${param.ecareNo}" /></td>
                                <th scope="row"><spring:message code="ecare.menu.name_${webExecMode}"/></th>
                                <td><input type="text" class="form-control form-control-sm" name="ecareNm" value="${param.ecareNm}" /></td>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                        <spring:message code="button.search"/><!-- 검색 -->
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div><!-- //Light table -->

                    <div class="row align-items-center py-1 table_option mt-2">
                        <div class="col"><!-- buttons -->
                            <button class="btn btn-sm btn-outline-primary" id="compareBtn">
                                <i class="fas fa-exchange-alt"></i> <spring:message code='ecare.conts.compare'/><!-- 비교 -->
                            </button>
                        </div>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-hover table-sm dataTable table-fixed">
                            <thead class="thead-light">
                            <tr>
                                <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 선택 -->
                                <th scope="col" width="50">No</th>
                                <th scope="col" width="8%"><spring:message code="ecare.menu.no_${webExecMode}" /></th><!-- 이케어 번호 -->
                                <th scope="col" width="*"><spring:message code="ecare.menu.name_${webExecMode}"/></th><!-- 이케어명 -->
                                <th scope="col" width="9%"><spring:message code="ecare.conts.contens.sel" /></th><!-- 컨텐츠구분 -->
                                <th scope="col" width="110"><spring:message code="ecare.conts.ver" /></th><!-- 버전 -->
                                <th scope="col" width="12%"><spring:message code="ecare.conts.fixedHu" /></th><!-- 수정자 -->
                                <th scope="col" width="120"><spring:message code="ecare.conts.fixedTime" /></th><!-- 수정일시 -->
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${!empty contsHistoryList}">
                            <c:forEach var="contsHistoryVo" items="${contsHistoryList}" varStatus="contsHistoryLoop">
                            <tr style="cursor: pointer;" onclick="<c:choose>
                                <c:when test="${contentRadio eq 'T'}">goView('${contsHistoryVo.ecareNo}','${contsHistoryVo.seg}','${contsHistoryVo.tmplVer}', 'T')</c:when>
                                <c:otherwise>goView('${contsHistoryVo.ecareNo}','','${contsHistoryVo.handlerVer}', 'H')</c:otherwise>
                            </c:choose>">
                                <th scope="row" onclick="event.cancelBubble=true"><!-- 선택 체크박스 -->
                                    <div class="custom-control custom-checkbox">
                                        <input type="checkbox" name="diff" id="diff-${contsHistoryVo.no}" value="${contsHistoryVo.no}" class="custom-control-input" />
                                        <label for="diff-${contsHistoryVo.no}" class="custom-control-label"></label>
                                    </div>
                                </th>
                                <td scope="row">${contsHistoryVo.no}</td>
                                <td>${contsHistoryVo.ecareNo}</td><!-- 이케어번호 -->
                                <td class="text-left"><!-- 이케어명 -->
                                    <em class="txt_channel ${contsHistoryVo.channelType}">
                                        <c:choose>
                                             <c:when test="${contsHistoryVo.channelType eq 'M'}">E</c:when>
                                             <c:when test="${contsHistoryVo.channelType eq 'T'}">M</c:when>
                                             <c:otherwise>${contsHistoryVo.channelType}</c:otherwise>
                                         </c:choose>
                                    </em>
                                    <span>${contsHistoryVo.ecareNm}</span>
                                </td>
                                <td><!-- 컨텐츠 구분 -->
                                    <c:choose>
                                        <c:when test="${contentRadio eq 'T' and contsHistoryVo.seg eq ' ' and contsHistoryVo.channelType ne 'P'}"><spring:message code='ecare.conts.contents'/></c:when>
                                        <c:when test="${contentRadio eq 'T' and contsHistoryVo.seg eq ' ' and contsHistoryVo.channelType eq 'P'}"><spring:message code='common.menu.inappmsg' /></c:when>
                                        <c:when test="${contentRadio eq 'T' and fn:toLowerCase(contsHistoryVo.seg) eq 'cover'}"><spring:message code='ecare.conts.cover'/></c:when>
                                        <c:when test="${contentRadio eq 'T' and fn:toLowerCase(contsHistoryVo.seg) eq 'preface'}"><spring:message code='ecare.conts.preface'/></c:when>
                                        <c:when test="${contentRadio eq 'T' and fn:toLowerCase(contsHistoryVo.seg) eq 'popup'}"><spring:message code='ecare.conts.contents'/></c:when>
                                        <c:otherwise><spring:message code='ecare.conts.h'/></c:otherwise>
                                    </c:choose>
                                </td>
                                <td><!-- 버전 -->
                                    <c:choose>
                                        <c:when test="${contentRadio eq 'T'}">
                                            <c:set var="compareVersion" value="${contsHistoryVo.tmplVer}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="compareVersion" value="${contsHistoryVo.handlerVer}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    ${compareVersion}
                                    <input type="hidden" id="diff-${contsHistoryVo.no}-ecareNo" value="${contsHistoryVo.ecareNo}" />
                                    <input type="hidden" id="diff-${contsHistoryVo.no}-seg" value="${contsHistoryVo.seg}" />
                                    <input type="hidden" id="diff-${contsHistoryVo.no}-version" value="${compareVersion}" />
                                </td>
                                <td>${contsHistoryVo.userNm}</td><!-- 수정자 -->
                                <td><!-- 수정일시 -->
                                    <fmt:parseDate value="${contsHistoryVo.lastupdateDt}${contsHistoryVo.lastupdateTm}" var="modifyDtm" pattern="yyyyMMddHHmmss" />
                                    <fmt:formatDate value="${modifyDtm}" pattern="yy-MM-dd HH:mm:ss" />
                                </td>
                            </tr>
                            </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div><!-- e.Light table -->
                    <!-- 페이징 -->
                    ${paging}

                </div>
            </div><!-- e.card -->
        </div><!-- e.page content -->
    </div><!-- e.main-panel -->
</form>
</body>
</html>