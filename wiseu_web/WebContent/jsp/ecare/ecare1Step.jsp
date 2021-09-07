<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>1단계] 이케어 기본정보 등록 화면 출력
 * - URL : /ecare/ecare_step_form.do
 * - Controller : com.mnwise.wiseu.web.ecare.web.Ecare1StepController
 * - 이전 파일명 : ecare_1step_form.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><spring:message code="ecare.title_${webExecMode}"/></title>
<script type="text/javascript">
    var rules = {
        ecareNm : { notBlank : true, maxlength : 33 }
    };

    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        $("#nextStepBtn").on("click", function() {
            if(!$.mdf.validForm('#editorFrm', rules)){
                return;
            }

            $("#editorFrm").submit();
        });

        //채널 별 설정
        $("input[name='channelType']").on("change", function() {
            if($(this).val() == 'M') {
                $("#mailType-tr").removeClass("dp-none");
            } else {
                $("#mailType-tr").addClass("dp-none");
            }
        });
    }
</script>
</head>

<body onload="document.editorFrm.ecareNm.focus();">
    <form:form id="editorFrm" name="editorFrm" modelAttribute="ecareVo" action="/ecare/ecare1Step.do">
    <div class="main-panel">
        <div class="container-fluid mt-4 mb-2"><!-- Page content -->
            <div class="card">
                <div class="card-header">
                    <h3 class="mb-0"><spring:message code="ecare.msg.create_${webExecMode}"/> (<spring:message code="ecare.msg.basic"/>)</h3>
                </div>
                <div class="card-body">
                    <div class="row align-items-center py-1 table_option">
                        <div class="col-12 row pr-0 justify-content-end"><!-- step -->
                            <ul class="stepWrap">
                                <li class="current"><div class="box"><span class="txt">STEP</span><strong>1</strong></div></li>
                                <li id="step2Btn"><div class="box"><span class="txt">STEP</span><strong>2</strong></div></li>
                            </ul>
                        </div>
                    </div>
                    <spring:hasBindErrors name="ecareVo"><!-- 경고창 -->
                    <div class="col-12 alert alert-warning mb-0" role="alert">
                        <c:forEach var="error" items="${errors.allErrors}">
                            <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                            <span class="alert-text"><spring:message message="${error}" /><br></span>
                        </c:forEach>
                    </div>
                    </spring:hasBindErrors>

                    <div class="table-responsive gridWrap">
                        <table class="table table-sm dataTable table-fixed">
                            <colgroup>
                                <col width="13%" />
                                <col width="37%" />
                                <col width="13%" />
                                <col width="37%" />
                            </colgroup>
                            <tbody>
                                <tr><!-- 이케어명 -->
                                    <th scope="row"><em class="required"></em><spring:message code="ecare.menu.name_${webExecMode}"/></th>
                                    <td colspan="3"><form:input path="ecareNm" class="form-control form-control-sm" /></td>
                                </tr>
                                <tr><!-- 채널 -->
                                    <th scope="row"><em class="required"></em><spring:message code="ecare.menu.channel"/></th>
                                    <td colspan="3">
                                        <c:forTokens items="${channelList}" var="channelVar" delims=",">
                                            <div class="custom-control custom-radio custom-control-inline">
                                                <form:radiobutton path="channelType" value="${channelVar}" class="custom-control-input" id="channel_${channelVar}"/>
                                                <label class="custom-control-label" for="channel_${channelVar}"><spring:message code="common.channel.${channelVar}"/></label>
                                            </div>
                                        </c:forTokens>
                                    </td>
                                </tr>
                                <tr id="mailType-tr"><!-- 메일 유형 -->
                                    <th scope="row"><em class="required"></em><spring:message code="ecare.menu.mailtype"/></th>
                                    <td colspan="3">
                                        <c:forEach items="${mailTypeList}" var="mailType">
                                            <div class="custom-control custom-radio custom-control-inline">
                                                <form:radiobutton path="mailType" value="${mailType}" class="custom-control-input" id="mailtype_${mailType}"/>
                                                <label class="custom-control-label" for="mailtype_${mailType}"><spring:message code="ecare.mailtype.${fn:toLowerCase(mailType)}" /></label>
                                            </div>
                                        </c:forEach>
                                    </td>
                                </tr>
                                <tr><!-- 유형 -->
                                    <th scope="row"><em class="required"></em><spring:message code="ecare.menu.type"/></th>
                                    <td colspan="3">
                                        <c:forEach items="${subTypeList}" var="subType">
                                            <div class="custom-control custom-radio custom-control-inline">
                                                <form:radiobutton path="subType" value="${subType}" class="custom-control-input" id="subtype_${subType}"/>
                                                <label class="custom-control-label" for="subtype_${subType}"><spring:message code="ecare.type.S${subType}" /></label>
                                            </div>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div><!-- //Light table -->
                </div><!-- //card body -->

                <div class="card-footer pb-4 btn_area"><!-- button area -->
                    <div class="row">
                        <div class="col-12 justify-content-end">
                            <button type="button" class="btn btn-outline-info" id="nextStepBtn">
                                <spring:message code="button.nextstep"/> <i class="fas fa-chevron-right"></i><!-- 다음단계 -->
                            </button>
                        </div>
                    </div>
                </div><!-- e.button area -->
            </div><!-- e.page content -->
        </div>
    </div><!-- e.main-panel -->
    </form:form>
</body>
</html>
