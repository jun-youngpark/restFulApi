<%-------------------------------------------------------------------------------------------------
 * - [대상자 관리>대상자파일 올리기>1단계] 대상자파일 올리기 화면 출력
 * - [공통팝업>대상자관리>1단계] 대상자 등록 (팝업) 화면 출력
 * - URL : /segment/fileSegment1Step.do, /target/fileSegment1Step.do
 * - Controller : com.mnwise.wiseu.web.segment.upload.web.UploadFilesController
 * - 이전 파일명 : upload.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="segment.msg.upload"/></title>
<script src="/js/segment/upload.js"></script>
<script type="text/javascript">
    window.name = "upload";
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 구분자 라디오버튼 클릭
        $("input[name=delimiter]").on("click", function(event) {
            var clickId = $(this).val();
            if(clickId == 'etc') {
                $("input[name=etcDelimiter]").show().focus();
            } else {
                $("input[name=etcDelimiter]").blur().hide();
            }
        });
        // 파일 재선택시 유효성 체크 reset 처리
        $("#file1").on("click", function(event) {
        	$.mdf.resetForm("#updateForm");
        });


        // 다음단계 버튼 클릭
        $("#nextStepBtn").on("click", function(event) {
            var rules = {
                contsNm   : {notBlank : true, maxbyte : 50},
                file      : {required : true, extension : "xls,xlsx,csv", noSpaceFilename : true},
                contsDesc : {maxbyte : 100},
                tagNm     : {maxbyte : 50},
                etcDelimiter : {
                    required : function() {
                        var delimiter = $('input[name=delimiter]:checked').val();
                        var etcText = $("input[name=etcDelimiter]").val();
                        if(delimiter == 'etc' && etcText == '' ||  etcText == null) {
                            return true;
                        }

                        return false;
                    }
                }
            };

            var messages = {
                etcDelimiter : {required : '<spring:message code="segment.alert.msg.upload.16"/>'}	  // 구분자를 입력하세요
            };

            if($.mdf.validForm("#updateForm", rules, messages) == false) {
                return;
            }

             uploadStartProgress();
            $('#nextStepBtn').addClass("dp-none");
            $('#updateForm').submit();
        });
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <c:choose>
                    <c:when test="${tabUse eq 'Y'}">
                        <div class="row table_option">
                            <div class="col-10 pl-0">
                                <c:import url="/segment/segment_addtab_include.do">
                                    <c:param name="viewPath" value="segment/segmentAddTab_inc"/><%-- segment/segment_add_tab_include --%>
                                </c:import>
                            </div>
                            <div class="col-2 pr-0 justify-content-end">
                                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                            </div><!-- 닫기 -->
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h3 class="mb-0"><spring:message code="segment.msg.upload"/></h3><!-- 대상자 파일 올리기 -->
                    </c:otherwise>
                </c:choose>
            </div>

            <spring:hasBindErrors name="command"><!-- 경고창 -->
                <div class="col-12 alert alert-warning mb-0" role="alert">
                    <c:forEach var="error" items="${errors.allErrors}">
                        <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                        <span class="alert-text"><spring:message message="${error}" /><br></span>
                    </c:forEach>
                </div>
            </spring:hasBindErrors>

            <div class="card-body">
                <form:form id="updateForm" name="updateForm" commandName="command" method="post" action="${action}" enctype="multipart/form-data" target="upload">
                <input type="hidden" name="reject" value="0"/>
                <input type="hidden" name="tabUse" value="${tabUse}"/>

                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="18%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.file.choice"/></th><!-- 파일선택 -->
                            <td>
                                <div class="custom-file custom-file-sm w-50">
                                    <spring:bind path="command.file">
                                        <input type="file" name="${status.expression}" value="${status.value}" id="file1" class="custom-file-input" />
                                        <label class="custom-file-label custom-file-label-sm" for="file1"></label>
                                    </spring:bind>
                                </div>
                                <span class="txt_info01 ml-2"><spring:message code="segment.menu.file.filesize" arguments="${uploadSizeMax / 1024 / 1024 }"/></span><!-- 최대 파일 사이즈는 100MB -->
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px" style="white-space: normal;"><spring:message code="segment.menu.file.lineinfo"/></th><!-- 첫번째 라인 필드 정보 포함 -->
                            <td>
                                <div class="custom-control custom-checkbox custom-control-inline">
                                    <input type="checkbox" id="fieldinfo" name="inMetaData" class="custom-control-input" />
                                    <label class="custom-control-label" for="fieldinfo"><spring:message code="segment.menu.file.header"/></label><!-- (헤더정보\:아이디,이름,이메일,고객휴대폰,기타) -->
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.file.valid"/></th><!-- 유효성 체크 -->
                            <td>
                                <div class="custom-control custom-checkbox custom-control-inline">
                                    <input type="checkbox" id="mail-chk" name="vmail" class="custom-control-input" value="Y" checked="checked"/>
                                    <label class="custom-control-label" for="mail-chk"><spring:message code="segment.menu.file.rmail"/></label><!-- 수신자 메일 -->
                                </div>
                                <div class="custom-control custom-checkbox custom-control-inline">
                                    <input type="checkbox" id="phone-chk" name="vphone" class="custom-control-input" value="Y" checked="checked"/>
                                    <label class="custom-control-label" for="phone-chk"><spring:message code="segment.menu.file.rphone"/></label><!-- 수신자 번호 -->
                                </div>
                                <span class="txt_info01 ml-2">※<spring:message code="segment.menu.file.rphone1"/></span><!-- ※수신자번호의 공백,-,(, )문자는 자동으로 제거합니다. -->
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.file.delimiter"/></th><!-- 구분자 -->
                            <td class="form-control-sm">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="delimiter" id="delimiter-comma" value="comma" checked="checked" class="custom-control-input" />
                                        <label class="custom-control-label" for="delimiter-comma"><spring:message code="segment.menu.file.comma"/></label><!-- 콤마 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="delimiter" id="delimiter-tab" value="tab" class="custom-control-input" />
                                        <label class="custom-control-label" for="delimiter-tab"><spring:message code="segment.menu.file.tab"/></label><!-- 탭 -->
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" name="delimiter" id="delimiter-etc" value="etc" class="custom-control-input" />
                                        <label class="custom-control-label" for="delimiter-etc"><spring:message code="segment.menu.file.userdefined"/></label><!-- 사용자 지정 -->
                                    </div>
                                    <div class="custom-control custom-control-inline pl-0">
                                        <input type="text" maxlength="1" name="etcDelimiter" class="form-control form-control-sm w-50 pl-0 dp-none" />
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table><!-- //Light table -->
                </div>
                </form:form><!-- e.form -->

                <div id="progressBar" style="display: none; padding-left: 30px;"><!-- progress bar -->
                    <div id="progressBarText"></div>
                    <div id="progressBarBox">
                        <div id="progressBarBoxContent"></div>
                    </div>
                </div><!--//progress bar  -->
            </div><!-- //card body -->

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <div class="offset-sm-8 col-4 justify-content-end">
                        <c:if test="${sessionScope.write eq 'W'}">
                            <button type="button" class="btn btn-outline-info" id="nextStepBtn">
                                <spring:message code="button.nextstep"/> <i class="fas fa-chevron-right"></i><!-- 다음단계 -->
                            </button>
                        </c:if>
                    </div>
                </div>
            </div><!-- e.button area -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>