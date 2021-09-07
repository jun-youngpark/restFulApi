<%-------------------------------------------------------------------------------------------------
 * - [템플릿>브랜드톡 템플릿 리스트] 브랜드톡템플릿 리스트
 * - URL : /template/brtTemplateList.do (페이지출력)
 * - JSP : /template/brandtalk_templateList.jsp
 * - 이전 파일명 : brandtalkTemplateList.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<title><spring:message code="template.brandtalk.list.title" /></title><!-- 브랜드톡 템플릿 리스트 -->
<script type='text/javascript'>
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            var contsArray = [];
            if(isCheckedTemplate() == false) {
                alert('<spring:message code="template.alert.nocheckeditem"/>');  // 처리할 템플릿을 선택해주세요.
                return;
            }

            if(confirm('<spring:message code="template.alert.msg.2"/>')) {
                $("input[name=contsNoArr]").each(function(idx) {
                    if($(this).prop("checked") == true) {
                        if('R' !== $(this).attr('status')) {
                            alert('<spring:message code="template.alert.delete.unable"/>');  // 템플릿 상태가 대기이고 템플릿 검수 상태가 승인이 아닌 경우에만 삭제할 수 있습니다.
                            return;
                        }
                        contsArray.push({"contsNo": $(this).val(), "code": $(this).attr('code')});
                    }
                });
                if(contsArray==''){ return false;}
                var param = { contsNoList : contsArray }
                $.ajax({
                    cache : false,
                    type : "post",
                    url : "/template/deleteBrandtalkTemplate.json",
                    contentType: "application/json",
                    data : JSON.stringify(param),
                    dataType : "json",
                    success : function(result) {
                        if (result.lastIndexOf('200', 0) === 0) {
                            alert('<spring:message code="template.brandtalk.bulk.delete"/>');  // 삭제되었습니다.
                            location.reload();
                        } else {
                            alert(result.substring(4));
                        }
                    }
                });
             }
        });

        // 템플릿 다운로드 버튼 클릭
        $("#downLoadBtn").on("click", function(event) {
            $("#searchForm").attr('action', '/template/brandtalkTemplateDownLoad.do').submit();
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            $("#searchForm input[name=nowPage]").val('1');
            $("#searchForm").attr('action', '/template/brtTemplateList.do').submit();  // /template/brandtalkTemplateList.do
        });
    }

    function initPage() {
        selectOption();
    }

    function selectOption() {
        $("#searchForm select[name=kakaoTemplateUseYn]").val("${kakaoTemplateUseYn}");  // 사용여부
        $("#searchForm select[name=senderKey]").val("${selectedSenderKey}");  // 카카오톡 채널ID
        $("#searchForm select[name=kakaoTemplateStatus]").val("${kakaoTemplateStatus}");  // 템플릿 상태
    }

    function isCheckedTemplate() {
        return $("#searchForm input:checkbox[name=contsNoArr]").is(":checked");
    }

    function goView(contsNo) {
        $("#searchForm input[name=contsNo]").val(contsNo);
        $("#searchForm").attr('action', '/template/brtTemplateView.do').submit();  // /template/brandtalkTemplateView.do
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <form id="searchForm" name="searchForm" action="/template/brtTemplateList.do" method="post"><!-- /template/brandtalkTemplateList.do -->
        <input type="hidden" name="cmd" />
        <input type="hidden" name="contsNo" />
        <input type="hidden" name="countPerPage" value="${countPerPage}" />
        <input type="hidden" name="nowPage" value="${nowPage}" />

        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="template.brandtalk.list.title"/></h3><!-- 브랜드톡 템플릿 리스트 -->
            </div>

            <div class="card-body px-0 pb-0">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-3"><!-- buttons -->
                        <button id="deleteBtn" class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-trash"></i> <spring:message code="button.delete" /><!-- 삭제 -->
                        </button>
                        <button id="downLoadBtn" class="btn btn-sm btn-outline-primary btn-round">
                            <i class="fas fa-download"></i> <spring:message code="button.download.all.template" /><!-- 템플릿 다운로드 -->
                        </button>
                    </div>

                    <div class="col-9 searchWrap"><!-- search -->
                        <div class="form-group searchbox">
                            <select class="form-control form-control-sm" name="kakaoTemplateUseYn" id="kakaoTemplateUseYn">
                                 <option value="A"><spring:message code="template.use.yn" /></option><!-- 사용여부 -->
                                 <option value="Y"><spring:message code="template.use.y" /></option><!-- 사용 -->
                                 <option value="N"><spring:message code="template.use.n" /></option><!-- 미사용 -->
                            </select>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" name="senderKey" id="senderKey">
                                <option value=""><spring:message code="template.column.yellowid" /></option><!-- 카카오톡 채널ID -->
                                <c:forEach var="kakaoProfile" items="${kakaoProfileList}" varStatus="status">
                                    <option value="${kakaoProfile.kakaoSenderKey}">${kakaoProfile.kakaoYellowId}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" name="kakaoTemplateStatus" id="kakaoTemplateStatus">
                                <option value=""><spring:message code="template.column.kakaotmplstatus" /></option><!-- 템플릿 상태 -->
                                <c:forEach var="map" items="${kakaoTemplateStatusMap}">
                                    <option value="${map.key}">${map.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <button id="searchBtn" class="btn btn-sm btn-outline-info btn_search">
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
                            <th scope="col" width="45"><spring:message code="campaign.table.select"/></th><!-- 선택 -->
                            <th scope="col" width="15%"><spring:message code="template.column.yellowid"/></th><!-- 카카오톡 채널ID -->
                            <th scope="col" width="19%"><spring:message code="template.column.tmplcd"/></th><!-- 템플릿 코드 -->
                            <th scope="col" width="18%"><spring:message code="template.column.tmplnm"/></th><!-- 템플릿명 -->
                            <th scope="col" width="*"><spring:message code="template.menu.mcontent"/></th><!-- 템플릿 내용 -->
                            <th scope="col" width="60"><spring:message code="template.use.yn"/></th><!-- 사용여부 -->
                            <th scope="col" width="75"><spring:message code="template.column.kakaotmplstatus"/></th><!-- 템플릿 상태 -->
                            <th scope="col" width="60"><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                        </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="templateVo" items="${templateList}" varStatus="status">
                    <tr style="cursor: pointer;" onclick="javascript:goView('${templateVo.contsNo}')">
                        <th scope="row" onclick="event.cancelBubble=true">
                            <div class="custom-control custom-checkbox">
                                <input type="checkbox" id="table-check-all_${status.count}" name="contsNoArr" value="${templateVo.contsNo}" code="${templateVo.code}"
                                    status="${templateVo.kakaoTmplStatus}" class="custom-control-input" />
                                <label class="custom-control-label" for="table-check-all_${status.count}"></label>
                            </div>
                        </th>
                        <td class="text-left" title="${templateVo.kakaoYellowId}">${templateVo.kakaoYellowId}</td>
                        <td class="text-left" title="${templateVo.code}">${templateVo.code}</td>
                        <td class="text-left" title="${templateVo.templateName}">${templateVo.templateName}</td>
                        <td class="text-left" style="overflow:hidden; text-overflow:ellipsis; white-space:nowrap; max-width: 300px;" title="${templateVo.content}">
                            ${templateVo.content}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${templateVo.useYn eq 'Y'}"><spring:message code="template.use.y"/></c:when>
                                <c:when test="${templateVo.useYn eq 'N'}"><spring:message code="template.use.n"/></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td>${templateVo.kakaoTmplStatusNm}</td>
                        <td>
                            <c:choose>
                                <c:when test="${templateVo.authType eq 'U'}"><spring:message code="template.type.user"/></c:when>
                                <c:when test="${templateVo.authType eq 'G'}"><spring:message code="template.type.group"/></c:when>
                                <c:when test="${templateVo.authType eq 'A'}"><spring:message code="template.type.all"/></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    </c:forEach>

                    <c:if test="${empty templateList}">
                    <tr>
                        <td colspan="8"><spring:message code="common.msg.nodata" /></td><!-- 검색결과가 없습니다. -->
                    </tr>
                    </c:if>
                    </tbody>
                </table>
            </div><!-- e.Light table -->
        </div><!-- e.card -->
    </form>
    </div><!-- e.page content -->

     <!-- 페이징 -->
    <c:import url="/common/page.do">
        <c:param name="viewPath" value="/common/page_ajax" />
        <c:param name="actionPath" value="/template/brtTemplateList.do" /><%-- /template/brandtalkTemplateList.do --%>
        <c:param name="method" value="get" />
        <c:param name="total" value="${totalCount}" />
        <c:param name="countPerPage" value="${countPerPage}" />
        <c:param name="nowPage" value="${nowPage}" />
        <c:param name="hiddenParam" value="countPerPage:${countPerPage}" />
        <c:param name="hiddenParam" value="searchWord:${searchWord}" />
        <c:param name="hiddenParam" value="fileType:${fileType}" />
        <c:param name="hiddenParam" value="searchQstartDt:${searchQstartDt}" />
        <c:param name="hiddenParam" value="searchQendDt:${searchQendDt}" />
        <c:param name="hiddenParam" value="senderKey:${selectedSenderKey}" />
        <c:param name="hiddenParam" value="kakaoInspStatus:${kakaoInspStatus}" />
        <c:param name="hiddenParam" value="kakaoTemplateStatus:${kakaoTemplateStatus}" />
        <c:param name="hiddenParam" value="kakaoTemplateUseYn:${kakaoTemplateUseYn}" />
    </c:import>
</div><!-- //main-panel -->
</body>
</html>