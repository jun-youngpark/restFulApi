<%-------------------------------------------------------------------------------------------------
 * - [템플릿>템플릿 등록] 템플릿 등록정보 조회 <br/>
 * - URL :/template/templateView.do<br/>
 * - Controller : com.mnwise.wiseu.web.template.web.TemplateRegController <br/>
 * - 이전 파일명 : template_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.msg.modify"/></title><!-- 템플릿 수정 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 템플릿 보기 버튼 클릭, (템플릿 URL 파일명) 링크 클릭
        $("#templateViewBtn, #templateLink").on("click", function(event) {
            var url = "/template/templatePreviewPopup.do?contsNo=${contentVo.contsNo}";  // /template/template_preview_popup.do
            $.mdf.popupGet(url, 'tmplViewPopup', 830, 700);
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                contsNm   : {notBlank : true, maxbyte : 50},
                fileNm    : {extension : "htm,html,jpg,gif,zip", noSpaceFilename : true},
                contsDesc : {maxbyte : 100},
                tagNm     : {maxbyte : 50}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var com = $("input[name=com]").val();
            if(com == "modify") {
                if('${userVo.userTypeCd}' != 'A' && '${userVo.userId}' != '${contentVo.userId}') {
                    alert('<spring:message code="template.alert.msg.4"/>');  // 템플릿을 등록한 사용자만 수정할 수 있습니다.
                    return;
                }
            }

            $("#updateForm").attr("action", "/template/templateUpload.do").submit();
        });

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            var userId = '${userVo.userId}';
            if('${userVo.userTypeCd}'=='A' || '${contentVo.userId}'==userId) {
                if(!confirm('<spring:message code="template.alert.msg.2"/>')) {  // 삭제하시겠습니까?
                    return;
                }
                $("#updateForm").attr("action", "/template/deleteTemplate.do?contsNo=" + $("#updateForm input[name=contsNo]").val()).submit();
            } else {
                alert('<spring:message code="template.alert.msg.3"/>');  // 템플릿을 등록한 사용자만 지울 수 있습니다.
            }
        });
    }

    function initPage() {
        $("input:radio[name=authType][value=${contentVo.authType}]").prop("checked", true);  // 공유유형 선택
    }
</script>
</head>
<body>
<form id="updateForm" name="updateForm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="com" value="${contentVo.command}">
    <input type="hidden" name="contsNo" value="${contentVo.contsNo}">
    <input type="hidden" name="cmd" />
    <input type="hidden" name="grpCd" value="${contentVo.grpCd}">

    <div class="main-panel">
        <div class="container-fluid mt-4 mb-2"><!-- Page content -->
            <div class="card">
                <div class="card-header">
                    <h3 class="mb-0"><spring:message code="template.msg.modify"/></h3><!-- 템플릿 수정 -->
                </div>

                <div class="card-body gridWrap">
                    <div class="table-responsive">
                        <table class="table table-sm dataTable table-fixed">
                            <colgroup>
                                <col width="13%" />
                                <col width="37%" />
                                <col width="13%" />
                                <col width="37%" />
                            </colgroup>
                            <tbody>
                            <tr>
                                <th scope="row"><em class="required">required</em><spring:message code="template.table.tname"/></th><!-- 템플릿명 -->
                                <td colspan="3"><input type="text" class="form-control form-control-sm" name="contsNm" value="${contentVo.contsNm}"/></td>
                            </tr>
                            <tr>
                                <th scope="row"><em class="required">required</em><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                                <td colspan="3">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="authType" id="authType_U" value="U" class="custom-control-input" />
                                        <label class="custom-control-label" for="authType_U"><spring:message code="template.type.user"/></label><!-- 사용자 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="authType" id="authType_G" value="G" class="custom-control-input" />
                                        <label class="custom-control-label" for="authType_G"><spring:message code="template.type.group"/></label><!-- 부서 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="authType" id="authType_A" value="A" class="custom-control-input" />
                                        <label class="custom-control-label" for="authType_A"><spring:message code="template.type.all"/></label><!-- 전체 -->
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th scope="row"><spring:message code="template.menu.filechoice"/></th><!-- 파일선택 -->
                                <td colspan="3">
                                    <div class="custom-file custom-file-sm">
                                        <input type="file" class="custom-file-input" id="fileNm" name="fileNm">
                                        <label class="custom-file-label custom-file-label-sm mb-0" for="fileNm"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th scope="row"><spring:message code="template.menu.turl"/></th><!-- 템플릿 URL -->
                                <td colspan="3" class="control-text-sm">
                                    <button type="button" class="btn btn-sm btn-outline-primary" id="templateViewBtn">
                                        <i class="fas fa-eye"></i> <spring:message code="button.view.template"/><!-- 템플릿 보기 -->
                                    </button>
                                    <a class="font-weight-700" href="#" id="templateLink">${contentVo.fileNm}</a>
                                </td>
                            </tr>
                            <tr class="dp-tag">
                                <th scope="row"><spring:message code="campaign.menu.tag"/></th> <!-- 태그  display 여부 css파일 dp-tag 수정 -->
                                <td colspan="3"><input type="text" class="form-control form-control-sm w-50" name="tagNm" value="${contentVo.tagNm}" /></td>
                            </tr>
                            <tr>
                                <th scope="row"><spring:message code="template.menu.tdes"/></th><!-- 설명 -->
                                <td colspan="3"><input type="text" class="form-control form-control-sm" name="contsDesc" value="${contentVo.contsDesc}" /></td>
                            </tr>
                            <tr>
                                <th scope="row"><spring:message code="campaign.table.creator"/></th><!-- 작성자 -->
                                <td class="control-text-sm">${contentVo.userNm}</td>
                                <th scope="row"><spring:message code="campaign.table.cdate"/></th><!-- 작성일 -->
                                <td class="control-text-sm">${contentVo.createDt} ${contentVo.createTm}</td>
                           </tr>
                           </tbody>
                       </table>
                    </div><!-- // Light table -->
                </div><!-- //card-body -->

                <c:if test="${sessionScope.write eq 'W'}">
                <div class="card-footer pb-4 btn_area"><!-- button area -->
                    <button type="button" class="btn btn-outline-primary" id="deleteBtn">
                        <spring:message code="button.delete"/><!-- 삭제 -->
                    </button>
                    <button type="button" class="btn btn-outline-primary" id="saveBtn">
                        <spring:message code="button.save"/><!-- 저장 -->
                    </button>
                </div>
                </c:if>
            </div><!-- e.Card -->
        </div>
    </div>
</form><!-- e.form -->
</body>
</html>