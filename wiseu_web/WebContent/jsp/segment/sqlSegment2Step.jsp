<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 등록>2단계] 대상자 쿼리컬럼 지정 <br/>
 * - URL : /segment/sqlSegment2Step.do, /target/sqlSegment2Step.do <br/>
 * - Controller :com.mnwise.wiseu.web.segment.web.Segment2StepController <br/>
 * - 이전 파일명 : sqlSegment2step.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="segment.msg.regquery"/> - 2 step</title><!-- 쿼리 등록 - 2 step -->
<script src='/plugin/form/jquery.autogrow-textarea.js'></script>
<script type="text/javascript">
    window.name = "upload";
    var segNo = ${segmentVo.segmentNo};

    $(document).ready(function() {
        initEventBind();
        addValidRule();
        initPage();
    });


    function initEventBind() {
        $("select[name*=fieldKey_]").each(function(i) {
            $(this).on('change', function(event) {
                event.preventDefault();

                var selectOption = $(this).val();
                var fieldKey = $(this).attr("name");
                var fieldSeq = fieldKey.substring(fieldKey.lastIndexOf('_') + 1);
                if(selectOption.substr(0,1) == '_') {
                    $("input[name=fieldDesc_"+fieldSeq+"]").val('<spring:message code="segment.status.ainfo"/>').blur();  // 추가정보
                } else {
                    $("input[name=fieldDesc_"+fieldSeq+"]").val($(this).find("option:selected").text()).blur();
                }
            });
        });

        // 취소 버튼 클릭
        $("#cancelBtn").on("click", function(event) {
            location.href="/segment/segmentList.do";  // /segment/segment.do
        });

        // 닫기 버튼 클릭
        $("#closeBtn").on("click", function(event) {
            window.close();
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if($.mdf.validForm("#updateForm") == false) {
                return;
            }

            if(segNo == 0) {
                var k = 0,n = 0,e = 0,s = 0,c = 0,f = 0,a = 0,b = 0,c = 0,x = 0,y = 0,z = 0;
                $("select[name*=fieldKey_] option:selected").each(function() {
                    var key = $(this).val();
                    //alert(key);
                    if(key != '') {
                        key = key.substr(0,1);
                        if(key == 'K') {  // 아이디
                            k++;
                        } else if(key == 'N') {  // 이름
                            n++;
                        } else if(key == 'E') {  // 이메일
                            e++;
                        } else if(key == 'S') {  // SMS
                            s++;
                        } else if(key == 'C') {  // 콜백번호
                            c++;
                        } else if(key == 'F') {  // FAX
                            f++;
                        } else if(key == 'A') {  // 로그부가정보1
                            a++;
                        } else if(key == 'B') {  // 로그부가정보2
                            b++;
                        } else if(key == 'X') {  // 발신이름
                            x++;
                        } else if(key == 'Y') {  // 발신메일
                            y++;
                        } else if(key == 'Z') {  // 반송메일
                            z++;
                        }
                    }
                });

                if(k ==1 && n ==1 && (e == 1 || s == 1 || f == 1) && !(k > 1 || n > 1 || e > 1 || f > 1 || s > 1 || a > 1 || b > 1 || c > 1 || x > 1 || y > 1 || z > 1)) {
                    if(e > 1 || s > 1) {
                        alert("<spring:message code='segment.alert.msg.reg.6'/>");  // 대상자의 기준이 되는 아이디, 이름과 이메일 또는 SMS가 중복 선택 되었습니다.
                    } else {
                        $("#updateForm").submit();
                    }
                } else if(k > 1 || n > 1 || e > 1 || f > 1 || s > 1 || a > 1 || b > 1 || c > 1 || x > 1 || y > 1 || z > 1) {
                    alert("<spring:message code='segment.alert.msg.reg.7'/>");  // 추가정보를 제외한 키구분은 한개만 선택이 가능 합니다.
                    return;
                } else {
                    alert("<spring:message code='segment.alert.msg.reg.8'/>");  // 대상자의 기준이 되는 아이디, 이름과 연락처가 선택되지 않았거나, 중복 선택 되었습니다.
                    return;
                }
            } else {
                alert("<spring:message code='segment.alert.msg.reg.10'/>");  // 등록된 대상자는 수정할 수 없습니다.
                return;
            }
        });

        // 이전단계 버튼 클릭
        $("#prevStepBtn").on("click", function(event) {
            if("${action}".indexOf("target") == -1) {
                history.back(-1);
            } else {
                location.href="/target/sqlSegment1Step.do?tabUse=Y&tabType=S&segmentNo=${segmentVo.segmentNo}";  // /segment/target/sqlSegment1step.do
            }
        });
    }

    function addValidRule() {
        $("#updateForm").validate();

        $("#updateForm select[name*=fieldKey_]").each(function() {
            $(this).rules("add", {selected : true});
        });

        $("#updateForm input[name*=fieldDesc_]").each(function() {
            $(this).rules("add", {notBlank : true});
        });
    }

    function initPage() {
        $('textarea').autogrow();

        if(segNo > 0) {
            $("select[name*=fieldKey_]").each(function() {
                $(this).attr("disabled", true);
            });

            $("input[name*=fieldDesc_]").each(function() {
                $(this).attr("disabled", true);
            });
        } else { // 추가정보를 기본 선택 으로 변경
            $("select[name*=fieldKey_]").each(function(i) {
                $(this).val("_" + (i + 1));
            });
        }
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
                            <c:param name="tabUse" value="${tabUse}"/>
                            <c:param name="tabType" value="${tabType}"/>
                        </c:import>
                    </c:when>
                    <c:otherwise>
                       <h3 class="mb-0"><spring:message code="segment.msg.reg"/></h3><!-- 대상자 등록 -->
                    </c:otherwise>
                </c:choose>
            </div>

            <form id="updateForm" name="updateForm" action="${action}" method="post" target="upload">
            <input type="hidden" name="segmentNo"   value="${segmentVo.segmentNo}"/>
            <input type="hidden" name="userId"      value="${segmentVo.userId}"/>
            <input type="hidden" name="nameKor"     value="${segmentVo.nameKor}"/>
            <input type="hidden" name="grpCd"       value="${segmentVo.grpCd}"/>
            <input type="hidden" name="dbinfoSeq"   value="${segmentVo.dbinfoSeq}"/>
            <input type="hidden" name="segmentDesc" value="${segmentVo.segmentDesc}"/>
            <input type="hidden" name="segmentNm"   value="${segmentVo.segmentNm}"/>
            <input type="hidden" name="serverNm"    value="${segmentVo.serverNm}"/>
            <input type="hidden" name="sqlHead"     value="${fn:escapeXml(segmentVo.sqlWebHead)}"/>
            <input type="hidden" name="sqlBody"     value="${fn:escapeXml(segmentVo.sqlWebBody)}"/>
            <input type="hidden" name="sqlTail"     value="${fn:escapeXml(segmentVo.sqlWebTail)}"/>
            <input type="hidden" name="sqlContext"  value="${fn:escapeXml(segmentVo.sqlContext)}"/>
            <input type="hidden" name="segmentSize" value="${segmentVo.segmentSize}"/>
            <input type="hidden" name="segmentType" value="${segmentVo.segmentType}"/>
            <input type="hidden" name="shareYn"     value="${segmentVo.shareYn}"/>
            <input type="hidden" name="categoryCd"  value="${segmentVo.categoryCd}"/>
            <input type="hidden" name="segmentSts"  value="${segmentVo.segmentSts}"/>
            <input type="hidden" name="bookmarkSeg" value="${segmentVo.bookmarkSeg}"/>
            <input type="hidden" name="tagNo"       value="${segmentVo.tagNo}"/>
            <input type="hidden" name="tagNm"       value="${segmentVo.tagNm}"/>
            <input type="hidden" name="psegmentNo"  value="${segmentVo.psegmentNo}"/>
            <input type="hidden" name="plinkSeq"    value="${segmentVo.plinkSeq}"/>

            <div class="card-body">
                <div class="alert alert-secondary" role="alert">
                    <p class="txt_info01">
                        <span class="alert-text"><spring:message code="segment.menu.sql.info"/></span>
                        <!-- ※ 이케어 대상자일 경우, 준실시간 인터페이스 테이블(NVREALTIMEACCEPT)을 사용하지 않는다면 아이디 이외에 고객 키값을 "로그부가정보1", "로그부가정보2" 로 지정하여 "이케어 고객이력" 메뉴에서 결과보기 및 재발송 기능을 사용하실 수 있습니다.<br>
                             ※ 고객키가 아이디를 포함해 3개가 초과되면 결과보기 및 재발송 기능은 사용할 수 없습니다. -->
                    </p>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="20%" />
                            <col width="20%" />
                            <col width="*" />
                        </colgroup>
                        <thead class="thead-light">
                        <tr>
                            <th scope="row"><spring:message code="segment.menu.sql.colname"/></th><!-- 컬럼명 -->
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.sql.keykind"/></th><!-- 키구분 -->
                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.sql.coldes"/></th><!-- 컬럼설명 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                        <c:when test="${!empty segmentVo.semanticList}">
                        <c:forEach var="semanticVo" items="${segmentVo.semanticList}" varStatus="status">
                        <tr>
                            <td class="text-left"><!-- 컬럼명 -->
                                ${semanticVo.fieldNm}
                                <input type="hidden" name="fieldNm" value="${semanticVo.fieldNm}_${semanticVo.fieldSeq}"/>
                            </td>
                            <td><!-- 키구분 -->
                                <select class="form-control form-control-sm" name="fieldKey_${semanticVo.fieldSeq}">
                                    <option value=""><spring:message code="segment.menu.sql.choice"/></option>
                                    <option value="_${semanticVo.fieldSeq}"  <c:if test="${' ' eq semanticVo.fieldKey || (empty semanticVo.fieldKey && semanticVo.segmentNo > 0)}">selected</c:if>><spring:message code="segment.status.ainfo"/></option>
                                    <option value="K_${semanticVo.fieldSeq}" <c:if test="${'K' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.K"/></option>
                                    <option value="N_${semanticVo.fieldSeq}" <c:if test="${'N' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.N"/></option>
                                    <option value="E_${semanticVo.fieldSeq}" <c:if test="${'E' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.E"/></option>
                                    <option value="S_${semanticVo.fieldSeq}" <c:if test="${'S' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.S"/></option>
                                    <option value="C_${semanticVo.fieldSeq}" <c:if test="${'C' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.C"/></option>
                                    <option value="F_${semanticVo.fieldSeq}" <c:if test="${'F' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.F"/></option>
                                    <option value="A_${semanticVo.fieldSeq}" <c:if test="${'A' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.A"/></option>
                                    <option value="B_${semanticVo.fieldSeq}" <c:if test="${'B' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.B"/></option>
                                    <option value="X_${semanticVo.fieldSeq}" <c:if test="${'X' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.X"/></option>
                                    <option value="Y_${semanticVo.fieldSeq}" <c:if test="${'Y' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.Y"/></option>
                                    <option value="Z_${semanticVo.fieldSeq}" <c:if test="${'Z' eq semanticVo.fieldKey}">selected</c:if>><spring:message code="segment.status.Z"/></option>
                                </select>
                            </td>
                            <td><!-- 컬럼설명 -->
                                <input class="form-control form-control-sm" name="fieldDesc_${semanticVo.fieldSeq}" type="text" value="<c:choose><c:when test='${!empty semanticVo.fieldDesc}'>${semanticVo.fieldDesc}</c:when><c:otherwise>${semanticVo.fieldNm}</c:otherwise></c:choose>" />
                            </td>
                        </tr>
                        </c:forEach>
                        </c:when>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="15%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>

                        <c:if test="${!empty segmentVo.semanticList}">
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="segment.menu.sql.tquery"/></th><!-- 테스트 발송 대상자 쿼리 -->
                            <td>
                                <div class="form-group">
                                    <textarea class="form-control text-wrap" name="testQuery" style="height:130px;"><c:if test="${!empty segmentVo.webTestQuery}">${fn:escapeXml(segmentVo.webTestQuery)}</c:if></textarea>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="segment.menu.sql.uquery"/></th><!-- 업데이트 쿼리 -->
                            <td>
                                <div class="form-group">
                                    <textarea class="form-control text-wrap" name="updateQuery" style="height:130px;"><c:if test="${!empty segmentVo.webUpdateQuery}">${fn:escapeXml(segmentVo.webUpdateQuery)}</c:if></textarea>
                                </div>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${empty segmentVo.semanticList}">
                        <tr>
                            <td>
                                <spring:message code="segment.menu.sql.ncinfo"/><!-- 등록된 컬럼 정보가 없습니다. -->
                            </td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- //card body -->
            </form><!-- e.form -->

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <div class="col offset-sm-4 col-4 text-center">
                        <c:if test="${segmentVo.segmentNo == 0}">
                            <button type="button" class="btn btn-outline-primary" id="saveBtn">
                                <spring:message code='button.save'/><!-- 저장 -->
                            </button>
                        </c:if>
                        <c:choose>
                            <c:when test="${actionGubun ne 'target'}">
                                <c:if test="${segmentVo.segmentNo == 0}">
                                <button type="button" class="btn btn-outline-primary" id="cancelBtn" >
                                    <spring:message code="button.cancel"/><!-- 취소 -->
                                </button>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn btn-outline-primary" id="closeBtn">
                                    <spring:message code="button.close"/><!-- 닫기 -->
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col col-12">
                        <button type="button" class="btn btn-outline-info" id="prevStepBtn">
                            <i class="fas fa-chevron-left"></i> <spring:message code="button.prevstep"/><!-- 이전단계 -->
                        </button>
                    </div>
                </div>
            </div><!-- e.button area -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>
