<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 등록] 대상자 쿼리등록 <br/>
 * - URL : /segment/sqlSegment1Step.do, /target/sqlSegment1Step.do <br/>
 * - Controller :com.mnwise.wiseu.web.segment.web.Segment1StepController <br/>
 * - [대상자>대상자 등록] 대상자 쿼리등록 - 대상자 쿼리 체크  <br/>
 * - 20100827 : 하위대상자는 쿼리수정버튼이 나오지 않도록 변경.(하위대상자는 수정할 수 없다.)
 *				textarea 창이 크기고정이 안되어 테이블에 컬럼크기 지정.
 * - 이전 파일명 : sqlSegment1step.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="segment.msg.regquery"/> - 1 step</title><!-- 쿼리 등록 - 1 step -->
<script src='/plugin/form/jquery.autogrow-textarea.js'></script>
<script src='/js/segment/target.js'></script>
<script type="text/javascript">
    window.name = "upload";

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 업데이트 쿼리체크 버튼 클릭
        $("#updateQueryCheckBtn").on("click", function(event) {
            checkTargetQuery($("textarea[name=updateQuery]").val());
        });

        // (업데이트) 쿼리 수정 버튼 클릭
        $('#updateQueryModifyBtn').on("click", function(event) {
            var segNo = ${segmentVo.segmentNo};
            var url = '/segment/updateQueryPopup.do?segmentNo=${segmentVo.segmentNo}';  // /segment/popupUpdateQuery.do
            $.mdf.popupGet(url, 'updateQueryPopup', 710, 370);
        });

        // (대상자) 쿼리체크 버튼 클릭
        $("#targetQueryCheckBtn").on("click", function(event) {
            checkTargetQuery($("textarea[name=sqlContext]").val());
        });


        // (대상자) 쿼리 수정 버튼 클릭
        $("#targetQueryModifyBtn").on("click", function(event) {
            var param = {
                segmentNo : "${segmentVo.segmentNo}"
            };

            $.post("/segment/checkUpdateQuery.json?", $.param(param, true), function(result) {
                if(result.code = "OK") {
                    if(result.value == "1") {  // 수행내역은 존재하지 않고 하위대상자만 존재할 경우
                        alert("<spring:message code='segment.alert.msg.reg.2'/>");  // 하위 대상자가 존재하여 쿼리를 수정할 수 없습니다.
                        return;
                    } else if(result.value == "2") {  // 수행내역이 존재하고 하위대상자가 존재하지 않을 경우
                        alert("<spring:message code='segment.alert.msg.reg.3'/>");  // 수행내역이 존재하여 쿼리를 수정할 수 없습니다.
                        return;
                    } else if(result.value == "3") {  // 수행내역과 하위 대상자가 모두 존재할 경우
                        alert("<spring:message code='segment.alert.msg.reg.4'/>");  // 수행내역과 하위 대상자가 존재하여 쿼리를 수정할 수 없습니다.
                        return;
                    } else {
                        var url = "/segment/targetQueryPopup.do?segmentNo=${segmentVo.segmentNo}";  //   // /segment/popupTargetQuery.do
                        $.mdf.popupGet(url, 'targetQueryPopup', 720, 566);
                    }
                } else {
                    alert(result.message);
                    return;
                }
            });
        });

        // (테스트 발송 대상자) 쿼리 수정 버튼 클릭
        $('#testQueryModifyBtn').on("click", function(event) {
            var segNo = ${segmentVo.segmentNo};
            var url = '/segment/testQueryPopup.do?segmentNo=${segmentVo.segmentNo}';  // /segment/popupTestQuery.do
            $.mdf.popupGet(url, 'testQueryPopup', 710, 370);
        });

        // 다음단계 버튼 클릭
        $("#nextStepBtn").on("click", function(event) {
            var rules = {
                segmentNm   : {notBlank : true, maxbyte : 100},
                sqlContext  : {notBlank : true},
                tagNm       : {maxbyte : 50},
                segmentDesc : {maxbyte : 250}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var segmentNo = $("input[name=segmentNo]").val();
            if(segmentNo == 0) {
                var param = {
                    dbInfoSeq : $("select[name=dbInfoSeq]").val(),
                    query : $("textarea[name=sqlContext]").val()
                };

                $.post("/segment/checkTargetQuery.json", $.param(param, true), function(result) {
                    if(result.code = "OK") {
                        alert(result.value);
                        if(result.value != null && result.value.indexOf("Success") > -1) {
                            $("#updateForm").submit();
                        }
                    } else {
                        alert(result.message);
                    }
                    return false;
                });
            } else {
                $("select[name=dbInfoSeq]").attr("disabled", false);
                $("#updateForm").submit();
            }
        });

        // 확정 버튼 클릭
        $("#fixBtn").on("click", function(event) {
            var segmentNo = $("input[name=segmentNo]").val();
            var segmentNm = $("input[name=segmentNm]").val();
            var size = '${segmentVo.segmentSize}';
            var parentObj = (window.dialogArguments == undefined) ? opener : window.dialogArguments;
            parentObj.$("input[name=segmentNo]").val(segmentNo);
            parentObj.$("input[name=segmentNm]").val(segmentNm);
            parentObj.$("input[name=segmentSize]").val(size);
            parentObj.$("#imsiRelationType").val("N");

            parentObj.editorIfrm.setSemantic(segmentNo);
            window.close();
        });
    }

    function initPage() {
        //textarea auto resize
        $('textarea').autogrow();

        <c:if test="${!empty segmentVo.sqlHead}">
            $("textarea[name=sqlContext]").attr("readonly", true);
            $("textarea[name=testQuery]").attr("readonly", true);
            $("textarea[name=updateQuery]").attr("readonly", true);
            $("select[name=dbInfoSeq]").attr("disabled", true);
        </c:if>
    }

    function checkTargetQuery(query) {
        var param = {
            dbInfoSeq : $("select[name=dbInfoSeq]").val(),
            query : query
        };

        $.post("/segment/checkTargetQuery.json", $.param(param, true), function(result) {
            if(result.code = "OK") {
                alert(result.value);
                if(result.value != null && result.value.indexOf("Success") > -1) {
                    return true;
                }
            } else {
                alert(result.message);
            }

            return false;
        });
    }
</script>
</head>
<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title 탭 -->
                <c:choose>
                    <c:when test="${tabUse eq 'Y'}">
                        <c:import url="/segment/segment_addtab_include.do">
                            <c:param name="viewPath" value="segment/segmentAddTab_inc"/><%-- segment/segment_add_tab_include --%>
                        </c:import>
                    </c:when>
                <c:otherwise>
                   <h3 class="mb-0"><spring:message code="segment.msg.reg"/></h3><!-- 대상자 등록 -->
                </c:otherwise>
                </c:choose>
            </div>

            <form:form id="updateForm" name="updateForm" commandName="segmentVo" action="${action}?tabUse=${param.tabUse}&tabType=${param.tabType}" target="upload" method="post">
            <form:hidden path="segmentNo" />
            <form:hidden path="sqlHead" />
            <form:hidden path="sqlBody" />
            <form:hidden path="sqlTail" />
            <form:hidden path="grpCd" />
            <form:hidden path="userId" />
            <form:hidden path="dbinfoSeq" />
            <form:hidden path="segmentType" />
            <form:hidden path="activeYn" />
            <form:hidden path="segmentSts" />
            <form:hidden path="tagNo" />
            <form:hidden path="psegmentNo" />
            <form:hidden path="plinkSeq" />
            <form:hidden path="fileToDbYn" />

            <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="16%" />
                            <col width="*" />
                            <col width="11%" />
                            <col width="30%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.sql.dbserver"/></th><!-- DB 서버 -->
                            <td>
                                <select name="dbInfoSeq" class="form-control form-control-sm">
                                    <c:if test="${!empty segmentVo.dbInfoList}">
                                        <c:forEach var="dbInfoVo" items="${segmentVo.dbInfoList}" varStatus="status">
                                            <c:choose>
                                                <c:when test="${dbInfoVo.dbInfoSeq eq segmentVo.dbinfoSeq}">
                                                    <option value="${dbInfoVo.dbInfoSeq}" selected="selected">${dbInfoVo.serverNm}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${dbInfoVo.dbInfoSeq}">${dbInfoVo.serverNm}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:if>
                                </select>
                            </td>
                            <th scope="row"><spring:message code="segment.menu.sql.share"/></th><!-- 공유 -->
                            <td>
                                <div class="custom-control custom-checkbox">
                                    <input type="checkbox" id="shareYn_Y" name="shareYn" value="Y" <c:if test="${'Y' eq segmentVo.shareYn}">checked</c:if> class="custom-control-input" />
                                    <label class="custom-control-label" for="shareYn_Y"><span class="hide">공유하기</span></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.sql.target"/></th><!-- 대상자명 -->
                            <td <c:if test="${segmentVo.segmentNo == 0}">colspan="3"</c:if>>
                                <input type="text" class="form-control form-control-sm" name="segmentNm" value="${segmentVo.segmentNm}" />
                            </td>
                            <c:if test="${segmentVo.segmentNo != 0}">
                            <th scope="row"><spring:message code="segment.menu.sql.ctarget"/></th><!-- 대상자수 -->
                            <td>${segmentVo.segmentSize}<spring:message code="segment.menu.sql.persons"/></td><!-- 명 -->
                            </c:if>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="segment.menu.sql.uquery"/></th><!-- 업데이트 쿼리 -->
                            <td colspan="3">
                                <p class="txt_info01"><spring:message code="segment.menu.sql.uquerymsg"/></p>
                                <!-- ※ 업데이트 쿼리는 대상자 쿼리 실행 직전에 실행 됩니다. <br>※ 업데이트 쿼리 체크 시 쿼리는 실제로 수행되지 않습니다. -->
                                <div class="form-group">
                                    <textarea class="form-control text-wrap" name="updateQuery" style="height:130px;"><c:if test="${!empty segmentVo.webUpdateQuery}">${fn:escapeXml(segmentVo.webUpdateQuery)}</c:if></textarea>
                                </div>
                                <div class="btn_area text-left pb-0">
                                    <button type="button" class="btn btn-sm btn-outline-primary" id="updateQueryCheckBtn" alt="<spring:message code='button.check.query'/>">
                                        <i class="fas fa-check"></i> <spring:message code="button.check.query"/><!-- 쿼리체크 -->
                                    </button>
                                    <c:if test="${!empty segmentVo.sqlHead}">
                                        <c:if test="${sessionScope.write eq 'W'}">
                                        <button type="button" class="btn btn-sm btn-outline-primary" id="updateQueryModifyBtn">
                                            <i class="fas fa-edit"></i> <spring:message code="button.modify.query"/><!-- 쿼리 수정 -->
                                        </button>
                                        </c:if>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.sql.query"/></th><!-- 대상자 쿼리 -->
                            <td colspan="3">
                                <div class="form-group">
                                    <textarea class="form-control text-wrap" name="sqlContext" style="height:130px;"><c:if test="${!empty segmentVo.sqlWebHead}">${fn:escapeXml(segmentVo.sqlWebHead)}${fn:escapeXml(segmentVo.sqlWebBody)}${fn:escapeXml(segmentVo.sqlWebTail)}</c:if></textarea>
                                </div>
                                <div class="btn_area text-left pb-0">
                                    <button type="button" class="btn btn-sm btn-outline-primary" id="targetQueryCheckBtn" >
                                        <i class="fas fa-check"></i> <spring:message code="button.check.query"/><!-- 쿼리체크 -->
                                    </button>
                                    <c:if test="${!empty segmentVo.sqlHead}">
                                        <c:if test="${sessionScope.write eq 'W' && segmentVo.segType ne 'S'}">
                                            <button type="button" class="btn btn-sm btn-outline-primary" id="targetQueryModifyBtn">
                                                <i class="fas fa-edit"></i> <spring:message code="button.modify.query"/><!-- 쿼리 수정 -->
                                            </button>
                                        </c:if>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="segment.menu.sql.tquery"/></th><!-- 테스트 발송 대상자 쿼리 -->
                            <td colspan="3">
                                <p class="txt_info01"><spring:message code="segment.menu.sql.tquerymsg"/></p>
                                <!-- ※ 테스트 대상쿼리는 원본쿼리와 컬럼의 순서와 컬럼의 수가 동일하여야 하며, 테스트 쿼리는 쿼리 체크가 되지 않습니다.<br>※ 하위대상자 쿼리는 수정할 수 없습니다. -->
                                <div class="form-group">
                                    <textarea class="form-control text-wrap" name="testQuery" rows="5"><c:if test="${!empty segmentVo.webTestQuery}">${fn:escapeXml(segmentVo.webTestQuery)}</c:if></textarea>
                                </div>
                                <div class="btn_area text-left pb-0">
                                    <c:if test="${!empty segmentVo.sqlHead}">
                                        <c:if test="${sessionScope.write eq 'W'}">
                                            <button type="button" class="btn btn-sm btn-outline-primary" id="testQueryModifyBtn">
                                                <i class="fas fa-edit"></i> <spring:message code="button.modify.query"/><!-- 쿼리 수정 -->
                                            </button>
                                        </c:if>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        <tr class="dp-tag">
                            <th scope="row"><spring:message code="common.menu.tag"/></th><!-- 태그 -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm w-50" name="tagNm" value="${segmentVo.tagNm}" /></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="segment.menu.sql.desc"/></th><!-- 설명 -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm" name="segmentDesc" value="${segmentVo.segmentDesc}" /></td>
                        </tr>
                        <c:if test="${segmentVo.segmentNo != 0}">
                        <tr>
                            <th scope="row"><spring:message code="common.menu.creator"/></th><!-- 작성자 -->
                            <td>${segmentVo.nameKor}</td>
                            <th scope="row"><spring:message code="segment.menu.sql.udate"/></th><!-- 최종수정일 -->
                            <td>${segmentVo.lastUpdateDt}</td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- //card body -->
            </form:form>

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <div class="col offset-sm-4 col-4 text-center">
                        <c:if test="${'true' eq targetUrl}">
                            <button type="button" class="btn btn-outline-primary" id="fixBtn">
                                <spring:message code="button.fix"/><!-- 확정 -->
                            </button>
                        </c:if>
                    </div>
                    <div class="col offset-sm-8 col-4">
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