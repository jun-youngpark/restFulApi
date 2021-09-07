<%-------------------------------------------------------------------------------------------------
 * - [템플릿>알림톡 템플릿 리스트] 알림톡 템플릿 리스트 <br/>
 * - URL : /template/altTemplateList.do<br/>
 * - Controller : com.mnwise.wiseu.web.template.web.AlimtalkTemplateListController <br/>
 * - 이전 파일명 : alimtalk_template_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title><spring:message code="template.list.alimtalk" /></title><!-- 알림톡 템플릿 리스트 -->
<script type='text/javascript'>
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 일괄검수요청 버튼 클릭
        $('#allReqBtn').on("click", function(event) {
            if(confirm('<spring:message code="template.alert.request.all"/>')) {  // 모든 템플릿을 검수요청하시겠습니까?
                callAjax("/template/inspectAllAlimtalkTemplate.json", null);
            }
        });

        // 선택 검수 요청 버튼 클릭
        $('#reqBtn').on("click", function(event) {
            if(isCheckedTemplate() == false) {
                alert('<spring:message code="template.alert.nocheckeditem"/>');  // 처리할 템플릿을 선택해주세요.
                return;
            }

            if(confirm('<spring:message code="template.alert.request.specific"/>')) {  // 선택한 템플릿을 검수요청하시겠습니까?
                callAjax("/template/inspectAlimtalkTemplate.json", getContsNoArr());
            }
        });

        // 검수 요청 취소 버튼 클릭
        $('#cancelReqBtn').on("click", function(event) {
            if(isCheckedTemplate() == false) {
                alert('<spring:message code="template.alert.nocheckeditem"/>');  // 처리할 템플릿을 선택해주세요.
                return;
            }

            if(confirm('<spring:message code="template.alert.cancel.request.specific"/>')) {  // 선택한 템플릿을 검수요청 취소 하시겠습니까?
                callAjax("/template/cancleInspectAlimtalkTemplate.json", getContsNoArr());
            }
        });

        // 삭제 버튼 클릭
        $('#deleteBtn').on("click", function(event) {
            if(isCheckedTemplate() == false) {
                alert('<spring:message code="template.alert.nocheckeditem"/>');  // 처리할 템플릿을 선택해주세요.
                return;
            }

            if(confirm('<spring:message code="template.alert.delete.specific"/>')) {  // 선택한 템플릿을 삭제하시겠습니까?
                callAjax("/template/deleteAlimtalkTemplate.json", getContsNoArr());
            }
        });

        // 다운로드 버튼 클릭
        $('#downloadBtn').on("click", function(event) {
            if(isCheckedTemplate() == false) {
                alert('<spring:message code="template.alert.nocheckeditem"/>');  // 처리할 템플릿을 선택해주세요.
                return;
            }

            if(confirm('<spring:message code="template.alert.download.specific"/>')) {  // 선택한 템플릿을 다운로드하시겠습니까?
                $('#searchForm').attr('action', "/template/downloadAlimtalkTemplate.do").submit();
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            $("#nowPage").val('1');
            $('#searchForm').attr('action', "/template/altTemplateList.do").submit();  // /template/alimtalkTemplate.do
        });
    }

    function initPage() {
        selectOption();
    }

    function callAjax(url, contsNoArr) {
        var param = {
            contsNoArr : contsNoArr
        };

        $.post(url, $.param(param, true), function(result) {
            if(result.code == "OK") {
                alert('<spring:message code="template.alert.kakao.request.success"/>');  // 요청이 처리되었습니다.
            } else {
                alert(result.message);
            }

            window.location = '/template/altTemplateList.do';  // /template/alimtalkTemplate.do
        });
    }

    function getContsNoArr() {
        var contsNoArr = [];
        $('input[name=contsNoArr]:checked').each(function () {
            contsNoArr.push(parseInt($(this).val()));
        });

        return contsNoArr;
    }

    function selectOption() {
        $("#searchForm select[name=kakaoTemplateUseYn]").val("${kakaoTemplateUseYn}");  // 사용여부
        $("#searchForm select[name=kakaoSenderKey]").val("${selectedKakaoSenderKey}");  // 카카오톡 채널ID
        $("#searchForm select[name=kakaoInspStatus]").val("${kakaoInspStatus}");  // 검수 상태
        $("#searchForm select[name=kakaoTemplateStatus]").val("${kakaoTemplateStatus}");  // 템플릿 상태
    }

    function isCheckedTemplate() {
        return $("#searchForm input:checkbox[name=contsNoArr]").is(":checked");
    }

    // 템플릿 내용 조회
    function goView(contsNo) {
        document.location.href = "/template/altTemplateView.do?contsNo=" + contsNo;  // /template/viewAlimtalkTemplate.do
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <form id="searchForm" name="searchForm" action="/template/altTemplateList.do" method="post"><!-- /template/alimtalkTemplate.do -->
        <input type="hidden" name="cmd" />
        <input type="hidden" name="contsNo" />
        <input type="hidden" name="tagNo" />
        <input type="hidden" name="countPerPage" value="${countPerPage}" />
        <input type="hidden" name="nowPage" value="${nowPage}" />

        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="template.list.alimtalk"/></h3><!-- 알림톡 템플릿 리스트 -->
            </div>

            <div class="card-body px-0 pb-0">
                <div class="alert alert-secondary mb-0" role="alert">
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="template.list.help3" /><br/>
                    <!-- 체크박스로 여러개의 템플릿을 선택하여 한번에 검수요청 및 삭제할 수 있습니다. -->
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="template.list.help4" /><br/>
                    <!-- 일괄 검수요청 버튼으로 등록 상태의 모든 템플릿을 검수 요청할 수 있습니다. -->
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="template.list.help5" />
                    <!-- 템플릿 동기화 주기는 5분입니다. 현재 리스트에서 보여지는 상태와 클릭 후 상세 내역에서 보여지는 상태는 차이가 있을 수 있습니다. -->
                </div>

                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed" style="margin-bottom: 8px !important;">
                        <colgroup>
                            <col width="6%" />
                            <col width="7%" />
                            <col width="10%" />
                            <col width="10%" />
                            <col width="7%" />
                            <col width="10%" />
                            <col width="7%" />
                            <col width="9%" />
                            <col width="32%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><spring:message code="template.use.yn"/></th><!-- 사용여부 -->
                            <td>
                                <select class="form-control form-control-sm" name="kakaoTemplateUseYn" id="kakaoTemplateUseYn">
                                    <option value="A"><spring:message code="common.option.select" /></option>
                                    <option value="Y"><spring:message code="template.use.y" /></option>
                                    <option value="N"><spring:message code="template.use.n" /></option>
                                </select>
                            </td>
                            <th scope="row"><spring:message code="template.column.yellowid"/></th><!-- 카카오톡 채널ID -->
                            <td>
                                <select class="form-control form-control-sm" name="kakaoSenderKey" id="kakaoSenderKey"	>
                                    <option value=""><spring:message code="common.option.select" /></option>
                                    <c:forEach var="kakaoProfile" items="${kakaoProfileList}" varStatus="status">
                                        <option value="${kakaoProfile.kakaoSenderKey}">${kakaoProfile.kakaoYellowId}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <th scope="row"><spring:message code="template.column.kakaoinspstatus"/></th><!-- 검수 상태  -->
                            <td>
                                <select class="form-control form-control-sm" name="kakaoInspStatus" id="kakaoInspStatus">
                                    <option value=""><spring:message code="common.option.select" /></option>
                                    <c:forEach var="map" items="${kakaoInspStatusMap}">
                                        <option value="${fn:toUpperCase(map.key)}">${map.value}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <th scope="row"><spring:message code="template.column.kakaotmplstatus"/></th><!-- 템플릿 상태 -->
                            <td>
                                <select class="form-control form-control-sm" name="kakaoTemplateStatus" id="kakaoTemplateStatus">
                                    <option value=""><spring:message code="common.option.select" /></option>
                                    <c:forEach var="map" items="${kakaoTemplateStatusMap}">
                                        <option value="${fn:toUpperCase(map.key)}">${map.value}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
	                            <div class="search_input">
	                                <select name="searchColumn" id="searchColumn" class="form-control form-control-sm">
	                                    <option value="contsNm" <c:if test="${searchColumn eq 'contsNm'}">selected</c:if>><spring:message code="template.column.tmplnm"/></option><!-- 템플릿명 -->
	                                    <option value="kakaoTmplCd" <c:if test="${searchColumn eq 'kakaoTmplCd'}">selected</c:if>><spring:message code="template.column.tmplcd"/></option><!-- 템플릿 코드 -->
	                                    <option value="contsTxt" <c:if test="${searchColumn eq 'contsTxt'}">selected</c:if>><spring:message code="template.column.tmpltxt"/></option><!-- 템플릿 코드 -->
	                                </select>
	                                <input type="search"  style="width: 220px;" id="searchWord" name="searchWord" maxlength="30" class="form-control form-control-sm" value="${param.searchWord}"/>
	                                <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
	                                    <spring:message code="button.search"/><!-- 검색 -->
	                                </button>
	                            </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="row align-items-center py-1 table_option">
                    <div class="col-12">
                        <button id='deleteBtn' class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-trash"></i> <spring:message code="button.delete" /><!-- 삭제 -->
                        </button>
                        <button id='allReqBtn' class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-check-double"></i> <spring:message code="button.batch.verification" /><!-- 일괄 검수 요청 -->
                        </button>
                        <button id='reqBtn' class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-check"></i> <spring:message code="button.verify.template" /><!-- 선택 템플릿 검수 요청 -->
                        </button>
                        <button id='cancelReqBtn' class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-ban"></i> <spring:message code="button.cancel.verify.template" /><!-- 선택 템플릿 검수 요청 취소 -->
                        </button>
                        <button id='downloadBtn' class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-download"></i> <spring:message code="button.download.template" /><!-- 선택 템플릿 다운로드 -->
                        </button>
                    </div>
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-sm dataTable table-hover table-fixed">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 선택 -->
                        <th scope="col" width="15%"><spring:message code="template.column.yellowid"/></th><!-- 플러스친구ID -->
                        <th scope="col" width="15%"><spring:message code="template.column.tmplcd"/></th><!-- 템플릿 코드 -->
                        <th scope="col" width="18%"><spring:message code="template.column.tmplnm"/></th><!-- 템플릿명 -->
                        <th scope="col" width="*"><spring:message code="template.menu.mcontent"/></th><!-- 템플릿 내용 -->
                        <th scope="col" width="60"><spring:message code="template.use.yn"/></th><!-- 사용여부 -->
                        <th scope="col" width="60"><spring:message code="template.column.kakaoinspstatus"/></th><!-- 검수 상태 -->
                        <th scope="col" width="60"><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                    </tr>
                    </thead>
                    <tbody>

                    <c:if test="${!empty templateList}">
                    <c:forEach var="templateVo" items="${templateList}" varStatus="status">
                    <tr style="cursor: pointer;" onclick="javascript:goView('${templateVo.contsNo}')">
                        <th scope="row" onclick="event.cancelBubble=true"><!-- 선택 체크박스 -->
                            <div class="custom-control custom-checkbox">
                                <input type="checkbox" id="table-check-all_${status.count}" name="contsNoArr" value="${templateVo.contsNo}" class="custom-control-input" />
                                <label class="custom-control-label" for="table-check-all_${status.count}"></label>
                            </div>
                        </th>
                        <td class="text-left" title="${templateVo.kakaoYellowId}">${templateVo.kakaoYellowId}</td><!-- 플러스친구ID -->
                        <td class="text-left" title="${templateVo.kakaoTmplCd}">${templateVo.kakaoTmplCd}</td><!-- 템플릿 코드 -->
                        <td class="text-left" title="${templateVo.contsNm}">${templateVo.contsNm}</td><!-- 템플릿명 -->
                        <td class="text-left text-truncate text-nowrap" title="${templateVo.contsTxt}">${templateVo.contsTxt}</td><!-- 템플릿 내용 -->
                        <td><!-- 사용여부 -->
                            <c:choose>
                                <c:when test="${templateVo.useYn eq 'Y'}"><spring:message code="template.use.y"/></c:when>
                                <c:when test="${templateVo.useYn eq 'N'}"><spring:message code="template.use.n"/></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td>${templateVo.kakaoInspStatusNm}</td><!-- 검수 상태 -->
                        <td><!-- 공유유형 -->
                            <c:choose>
                                <c:when test="${templateVo.authType eq 'U'}"><spring:message code="template.type.user"/><!-- 사용자 --></c:when>
                                <c:when test="${templateVo.authType eq 'G'}"><spring:message code="template.type.group"/><!-- 부서 --></c:when>
                                <c:when test="${templateVo.authType eq 'A'}"><spring:message code="template.type.all"/><!-- 전체 --></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    </c:forEach>
                    </c:if>

                    <c:if test="${empty templateList}">
                    <tr>
                        <td colspan="8"><spring:message code="common.msg.nodata" /></td><!-- 검색결과가 없습니다. -->
                    </tr>
                    </c:if>
                    </tbody>
                </table>
            </div><!-- e.Light table -->
        </form>
    </div><!-- e.page content -->

    <!-- 페이징 -->
    <c:import url="/common/page.do">
        <c:param name="viewPath" value="/common/page" />
        <c:param name="actionPath" value="/template/altTemplateList.do" /><%-- /template/alimtalkTemplate.do --%>
        <c:param name="total" value="${totalCount}" />
        <c:param name="countPerPage" value="${countPerPage}" />
        <c:param name="nowPage" value="${nowPage}" />
        <c:param name="tagNo" value="${tagNo}" />
        <c:param name="hiddenParam" value="countPerPage:${countPerPage}" />
        <c:param name="hiddenParam" value="searchColumn:${searchColumn}" />
        <c:param name="hiddenParam" value="searchWord:${searchWord}" />
        <c:param name="hiddenParam" value="fileType:${fileType}" />
        <c:param name="hiddenParam" value="searchQstartDt:${searchQstartDt}" />
        <c:param name="hiddenParam" value="searchQendDt:${searchQendDt}" />
        <c:param name="hiddenParam" value="kakaoSenderKey:${kakaoSenderKey}" />
        <c:param name="hiddenParam" value="kakaoInspStatus:${kakaoInspStatus}" />
        <c:param name="hiddenParam" value="kakaoTemplateStatus:${kakaoTemplateStatus}" />
        <c:param name="hiddenParam" value="kakaoTemplateUseYn:${kakaoTemplateUseYn}" />
    </c:import>
</div><!-- e.card -->
</body>
</html>