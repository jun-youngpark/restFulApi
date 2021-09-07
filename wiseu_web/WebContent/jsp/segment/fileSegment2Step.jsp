<%-------------------------------------------------------------------------------------------------
 * [대상자 관리>대상자파일 올리기>2단계] 대상자 기본정보 및 컬럼지정 화면 출력 (CSV/XLS/XLSX)
 * [공통팝업>대상자관리>2단계] 대상자 기본정보 및 컬럼지정 (팝업) 화면 출력 (CSV/XLS/XLSX)
 * - URL : /target/fileSegment2Step_csv.do, /segment/fileSegment2Step_csv.do (CSV)
 * - URL : /target/fileSegment2Step_xls.do, /segment/fileSegment2Step_xls.do (XLS)
 * - URL : /target/fileSegment2Step_xlsx.do, /segment/fileSegment2Step_xlsx.do (XLSX)
 * - Controller : com.mnwise.wiseu.web.segment.upload.web.CSVFormController (CSV)
 * - Controller : com.mnwise.wiseu.web.segment.upload.web.XLSFormController (XLS)
 * - Controller : com.mnwise.wiseu.web.segment.upload.web.XLSXFormController (XLSX)
 * - 이전 파일명 : csvimport.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Import File - <spring:message code="segment.msg.reg"/>  &gt; <spring:message code="segment.msg.dinfo"/>, <spring:message code="segment.msg.colappoint"/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script src="/plugin/progress/jquery.progressbar-1.1.0.js"></script>
<script src="/js/segment/csvInsertedCnt.js"></script>
<script type="text/javascript">
    window.name = "upload";

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
    	 // 닫기 버튼 클릭
        $("#closeBtn").on("click", function(event) {
            window.close();
        });
        // 다음단계 버튼 클릭
        $("#nextStepBtn").on("click", function(event) {
            if($('#nextStepBtn').attr('class') == 'clicked') {
                alert('<spring:message code="segment.alert.msg.upload.5"/>');  // 파일 업로드 중에는 중복 클릭이 안됩니다.
                return false;
            }

            var rules = {
                'segmentVo.segmentNm'   : {notBlank : true, maxbyte : 100},
                'segmentVo.tagNm'       : {maxbyte : 50},
                'segmentVo.segmentDesc' : {maxbyte : 250}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var maxCnum = $("input[name=maxCnum]").val();

            // 업로드 된 파일의 항목이 1개일 경우
            if(maxCnum == 0) {
                if($("select[name=keys]").val() == "") {
                    alert('<spring:message code="segment.alert.msg.upload.6"/>');  // 대상자 칼럼을 지정하셔야 합니다.
                    return;
                }

                if($("select[name=keys]").val() != "CUSTOMER_ID") {
                    alert('<spring:message code="segment.alert.msg.upload.7"/>');  // 하나의 필드만 올릴 경우 아이디를 선택해야합니다..
                    return;
                }
            } else {
                var valid_CUSTOMER_ID = false;
                var valid_CUSTOMER_NM = false;
                var valid_CUSTOMER_EMAIL = false;
                var valid_CUSTOMER_TEL = false;
                var valid_CUSTOMER_FAX = false;
                var keyNameArry = new Array(maxCnum + 1);
                var etcChkCnt = 0;
                for(i = 0; i <= maxCnum; i++) {
                    keyNameArry[i] = $("select[name=keys] option:selected").eq(i).text();
                    if(keyNameArry[i] == '<spring:message code="segment.menu.upload.choice"/>') {  // 선택
                        alert('<spring:message code="segment.alert.msg.upload.6"/>');  // 대상자 칼럼을 지정하셔야 합니다.
                        return false;
                    }

                    if(keyNameArry[i] == '기타' || keyNameArry[i] == 'etc') {
                        // 기타항목을 6개 이하로 갯수 제한하는 로직 추가
                        etcChkCnt++;
                        if(etcChkCnt > 6) {
                            alert('<spring:message code="segment.alert.msg.upload.17"/>');  // 기타 항목이 6개를 초과하였습니다.
                            return false;
                        }
                        if($("input[name=keysName]").eq(i).val() == '') {
                            alert('<spring:message code="segment.alert.msg.upload.9"/>');  // 기타를 선택한 경우에는 설명을 입력하셔야 합니다.
                            return false;
                        }
                    }

                    var keyValue = $("select[name=keys]").eq(i).val();
                    if(keyValue == "CUSTOMER_ID") {
                        valid_CUSTOMER_ID = true;
                    } else if(keyValue == "CUSTOMER_NM") {
                        valid_CUSTOMER_NM = true;
                    } else if(keyValue == "CUSTOMER_EMAIL") {
                        valid_CUSTOMER_EMAIL = true;
                    } else if(keyValue == "CUSTOMER_TEL") {
                        valid_CUSTOMER_TEL = true;
                    } else if(keyValue == "CUSTOMER_FAX") {
                        valid_CUSTOMER_FAX = true;
                    }
                }

                if(valid_CUSTOMER_ID == false || valid_CUSTOMER_NM == false || (valid_CUSTOMER_EMAIL == false && valid_CUSTOMER_TEL == false && valid_CUSTOMER_FAX == false)) {
                    alert("<spring:message code='segment.alert.msg.reg.8'/>");  // 대상자의 기준이 되는 아이디, 이름과 연락처가 선택되지 않았거나, 중복 선택 되었습니다.
                    return;
                }

                var dupArray = new Array();
                var uniqueArray = $.mdf.unique(keyNameArry);

                for(var j = 0; j < uniqueArray.length; j++) {
                    unique = uniqueArray[j];
                    if(unique == '기타' || unique == 'etc')
                        continue;
                    if($.mdf.countMatches(keyNameArry, unique) > 1) {
                        dupArray.push(unique);
                    }
                }

                if(dupArray != '') {
                    alert(dupArray + '<spring:message code="segment.alert.msg.upload.10"/>');  // 등록된 대상자가 파일이 현재 선택한 파일형식과 맞지 않거나 대상자 수가 0 이므로, 대상자를 등록할 수 없습니다.
                    return false;
                }
            }

            csvUploadstartProgress();
            $('#updateForm').submit();
            $('#nextStepBtn').attr("class", "clicked");
        });
    }

    function initPage() {
        $("#insertProgress").progressBar();
        var totalLine = '${uploadSegVo.totalLine}';
        if(totalLine == -1) {
            $("#nextStepBtn").css('display','none');
        }
    }

    function changeField(cnum) {
        // 업로드 된 파일의 항목이 1개일 경우
        if($("input[name=maxCnum]").val() == 0) {
            var v = $("select[name=keys] option:selected").text();
            if(v != '기타' || v != 'etc') {
                $("input[name=keysName]").val(v);
            }
        } else {
            var v = $("select[name=keys] option:selected").eq(cnum).text();
            if(v != '기타' || v != 'etc') {
                $("input[name=keysName]").eq(cnum).val(v);
            }
        }
    }
</script>
</head>

<body>
<form id="updateForm" name="updateForm" action="${action}" method="post" target="upload">
<input type="hidden" name="importedFilePath" value="${uploadSegVo.importedFilePath}"/>
<input type="hidden" name="totalLine" value="${uploadSegVo.totalLine}"/>
<input type="hidden" name="inMetaData" value="${uploadSegVo.inMetaData}"/>
<input type="hidden" name="delimiter" value="${uploadSegVo.delimiter}"/>
<input type="hidden" name="vmail" value="${uploadSegVo.vmail}"/>
<input type="hidden" name="vphone" value="${uploadSegVo.vphone}"/>
<input type="hidden" name="reject" value="${reject}" />
<input type="hidden" name="tabUse" value="${param.tabUse}" />
<input type="hidden" name="segmentVo.dbInfoSeq" value="1"/>
<input type="hidden" name="segmentVo.fileToDbYn" value="Y"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <c:choose>
                    <c:when test="${fn:startsWith(action,'/target/')}"><%-- /segment/target/ --%>
                        <div class="pop-card row table_option" style="height: 38px;">
                            <div class="col-10 pl-0 ml--3">
                                <h5 class="mb-0"><spring:message code="segment.msg.upload"/></h5><!-- 대상자 파일 올리기 -->
                            </div>
                            <div class="col-2 pr-0 mr--3 justify-content-end">
                                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                            </div><!-- 닫기 -->
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h3 class="mb-0"><spring:message code="segment.msg.upload"/></h3><!-- 대상자 파일 올리기 -->
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="card-body">
                <h4 class="h3 text-primary mb-2"><spring:message code="segment.msg.dinfo"/></h4><!-- 대상자 기본정보 -->
                <div class="alert alert-secondary mb-1" role="alert">
                    <c:choose>
                        <c:when test="${uploadSegVo.totalLine == -1}">
                            <span class="text-danger"><i class="fas fa-exclamation-circle"></i> ${uploadSegVo.errorMsg}</span>
                        </c:when>
                        <c:when test="${uploadSegVo.totalLine == 0}">
                            <!-- 등록된 대상자가 파일이 현재 선택한 파일형식과 맞지 않거나 대상자 수가 0 이므로, 대상자를 등록할 수 없습니다. -->
                            <span class="text-danger"><i class="fas fa-exclamation-circle"></i> <spring:message code="segment.alert.msg.upload.10"/></span>
                        </c:when>
                        <c:otherwise>
                            <!-- 입력 데이터 총 수 \: {0}건 (공백 줄은 제외함) -->
                            <span class="text-warning"><i class="fas fa-exclamation-circle"></i> <spring:message code="segment.alert.msg.upload.11" arguments="${uploadSegVo.totalLine}"/></span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${uploadSegVo.totalLine > 0}">
	                <div class="table-responsive gridWrap">
	                    <table class="table table-sm dataTable table-fixed">
	                        <colgroup>
	                            <col width="200" />
	                            <col width="45%" />
	                            <col width="100" />
	                            <col width="*" />
	                        </colgroup>
	                        <tbody>
	                        <tr>
	                            <th scope="row"><em class="required">required</em><spring:message code="segment.menu.sql.target"/></th><!-- 대상자명 -->
	                            <td><input type="text" class="form-control form-control-sm" name="segmentVo.segmentNm"/></td>
	                            <th scope="row"><spring:message code="segment.menu.sub.share"/></th>
	                            <td>
	                                <div class="custom-control custom-checkbox custom-control-inline">
	                                    <input type="checkbox" id="shareYn" name="segmentVo.shareYn" class="custom-control-input" /><!-- 공유 -->
	                                    <label class="custom-control-label" for="shareYn"><span class="hide">공유 선택</span></label>
	                                </div>
	                            </td>
	                        </tr>
	                        <tr class="dp-tag">
	                            <th scope="row"><spring:message code="common.menu.tag"/></th><!-- 태그 -->
	                            <td colspan="3"><input type="text" class="form-control form-control-sm w-50" name="segmentVo.tagNm"/></td>
	                        </tr>
	                        <tr>
	                            <th scope="row"><spring:message code="segment.menu.sql.desc"/></th><!-- 설명 -->
	                            <td colspan="3"><input type="text" class="form-control form-control-sm" name="segmentVo.segmentDesc" /></td>
	                        </tr>
	                        </tbody>
	                    </table><!-- //Light table -->
	                </div>

	                <h4 class="h3 text-primary mt-3 mb-0"><spring:message code="segment.msg.colappoint"/></h4><!-- 대상자 칼럼 지정 -->
	                <div class="table-responsive gridWrap">
	                    <table class="table table-sm dataTable table-fixed">
	                        <colgroup>
	                            <col width="200" />
	                            <col width="150" />
	                            <col width="*" />
	                        </colgroup>
	                        <tbody>

	                        <c:if test="${uploadSegVo.inMetaData eq 'on'}">
	                        <c:forEach var="key" items="${uploadSegVo.headers}" varStatus="status">
	                        <c:set value="${key}" target="${uploadSegVo}" property="key"/>
	                        <c:set value="${(status.count)-1}" target="${uploadSegVo}" property="cnum"/>
	                        <tr>
	                            <th scope="row"><em class="required">required</em>${uploadSegVo.firstDataByColumn}</th>
	                            <td>${uploadSegVo.selectOption}</td>
	                            <td><input type="text" name="keysName" value="${key}" class="form-control form-control-sm w-75"/></td>
	                        </tr>
	                        </c:forEach>
	                        </c:if>

	                        <c:if test="${uploadSegVo.inMetaData ne 'on'}">
	                        <c:forEach var="key" items="${uploadSegVo.firstData}" varStatus="status">
	                        <c:set value="${key}" target="${uploadSegVo}" property="key"/>
	                        <c:set value="${(status.count)-1}" target="${uploadSegVo}" property="cnum"/>
	                        <tr>
	                            <th scope="row"><em class="required">required</em>${uploadSegVo.firstDataByColumn}</th>
	                            <td>${uploadSegVo.selectOption}</td>
	                            <td><input type="text" name="keysName" value="${key}" class="form-control form-control-sm w-75"/></td>
	                        </tr>
	                        </c:forEach>
	                        </c:if>
	                        </tbody>
	                    </table>
	                </div><!-- //Light table -->
	                 <div class="progress-wrapper pt-0 w-50">
	                    <span class="progressBar" id="insertProgress">0%</span>
	                    <spring:message code="segment.alert.msg.upload.12"/>:${uploadSegVo.totalLine}
	                </div>
                </c:if>
            </div><!-- //card body -->


            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <div class="offset-sm-8 col-4 justify-content-end">
                        <c:if test="${uploadSegVo.totalLine > 0}">
                        <button type="button" class="btn btn-outline-info" id="nextStepBtn">
                            <spring:message code="button.nextstep"/> <i class="fas fa-chevron-right"></i><!-- 다음단계 -->
                        </button>
                        </c:if>
                        <c:if test="${uploadSegVo.totalLine == 0}">
                              <button type="button" class="btn btn-outline-primary" id="closeBtn">
                                    <spring:message code="button.close"/><!-- 닫기 -->
                                </button>
                        </c:if>
                    </div>
                </div>
            </div><!-- e.button area -->


         </div><!-- e.card -->
     </div><!-- e.page content -->
</div><!-- e.main-panel -->

<input type="hidden" name="maxCnum" value="${uploadSegVo.cnum}"/><!-- select 옵션의 총 수를 표시하는 값으로 하단에 위치해야 한다. -->
</form>
</body>
</html>