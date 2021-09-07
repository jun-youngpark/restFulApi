<%-------------------------------------------------------------------------------------------------
  * - [템플릿>템플릿 등록] 템플릿 등록 화면 출력
  * - URL : /template/templateReg.do
  * - Controller : com.mnwise.wiseu.web.template.web.TemplateRegController
  *
  * - [템플릿>템플릿 등록] 템플릿 등록/수정
  * - URL : /template/templateUpload.do
  * - Controller : com.mnwise.wiseu.web.template.web.TemplateUploadController
  * - 이전 파일명 : template_reg.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.msg.reg"/></title><!-- 템플릿 등록 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                contsNm   : {notBlank : true, maxbyte : 50},
                fileNm    : {required : true, extension : "htm,html,jpg,gif,zip", noSpaceFilename : true},
                contsDesc : {maxbyte : 100},
                tagNm     : {maxbyte : 50}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            $('#updateForm').submit();
        });
    }

    function initPage() {
        $("input:radio[name=authType][value=U]").prop("checked", true);  // 공유유형 선택 - 사용자(U)
        $("#tagNm").result(function(event, data, formatted) {
            if(data)
                $(this).parent().next().find("input").val(data[1]);
        });
    }
</script>
</head>
<body>
<form:form id="updateForm" name="updateForm" commandName="command" action="/template/templateUpload.do" enctype="multipart/form-data" method="post">
<input type="hidden" name="contsNo" value="${contentVo.contsNo}" />
<input type="hidden" name="grpCd" value="${contentVo.grpCd}"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="template.msg.reg"/></h3>
            </div>

                    <c:forEach var="error" items="${errors.allErrors}">
                    <div class="col-12 alert alert-warning mb-0" role="alert">
                        <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                        <span class="alert-text"><spring:message message="${error}" /><br></span>
                    </div>
                    </c:forEach>

            <div class="card-body gridWrap">
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="13%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="template.table.tname"/></th><!-- 템플릿명 -->
                            <td><input type="text" class="form-control form-control-sm" name="contsNm" maxlength="50" value="${contentVo.contsNm}" /></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="template.table.share"/></th><!-- 공유유형  -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_U" value="U" class="custom-control-input" checked="checked">
                                    <label class="custom-control-label" for="authType_U"><spring:message code="template.type.user"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_G" value="G" class="custom-control-input">
                                    <label class="custom-control-label" for="authType_G"><spring:message code="template.type.group"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_A" value="A" class="custom-control-input">
                                    <label class="custom-control-label" for="authType_A"><spring:message code="template.type.all"/></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="template.menu.filechoice"/></th><!-- 파일선택 -->
                            <td>
                                <div class="custom-file custom-file-sm w-50">
                                    <input type="file" class="custom-file-input" id="fileNm" name="fileNm">
                                    <label class="custom-file-label custom-file-label-sm mb-0" for="fileNm"></label>
                                </div>
                            </td>
                        </tr>
                        <tr class="dp-tag">
                            <th scope="row"><spring:message code="campaign.menu.tag"/></th><!-- 태그  display 여부 css파일 dp-tag 수정 -->
                            <td><input type="text" class="form-control form-control-sm w-50" name="tagNm" id="tagNm" value="${contentVo.tagNm}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="template.menu.tdes"/></th><!-- 설명 -->
                            <td><input type="text" class="form-control form-control-sm" name="contsDesc" value="${contentVo.contsDesc}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="campaign.table.creator"/></th><!-- 작성자 -->
                            <td class="control-text-sm">${contentVo.userId}</td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div><!-- //card-body -->

            <c:if test="${sessionScope.write eq 'W'}">
                <div class="card-footer pb-4 btn_area"><!-- button area -->
                    <button type="button" class="btn btn-outline-primary" id="saveBtn">
                        <spring:message code="button.save"/><!-- 저장 -->
                    </button>
                </div>
            </c:if>
        </div><!-- //card  -->
    </div>
</div>
</form:form>
</body>
</html>