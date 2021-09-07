<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 조회] 대상자 보기(팝업) <br/>
 * - URL : /segment/targetListPopup.do <br/>
 * - Controller : com.mnwise.wiseu.web.segment.web.SegmentListController <br/>
 * - 이전 파일명 : target_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="segment.msg.lists"/></title><!-- 대상자 리스트 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/jsp/include/plugin.jsp"%>
<script language="javascript">
    var editorStatus = 0;  // 레이어상태(default 0 : hidden상태)
    var selectRow = -1;

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 전체보기 버튼 클릭
        $("#allViewBtn").on("click", function(event) {
            event.preventDefault();

            $("#searchColumn").val("");
            $("#searchWord").val("");

            $("#targetListFrm").submit();
        });

        // 추가 버튼 클릭
        $("#addBtn").on("click", function(event) {
            if(editorStatus == 0) {
                editorStatus = 1;
                $("#editorLayer").css("display", "block");
            }

            selectRow = -1;

            // 각 필드의 값을 초기화 한다.
            <c:forEach var="loop" items="${sementicList}" varStatus="i">
                fieldNm = "${loop.fieldNm}";
                // 추가일 경우 K값도 입력 가능하도록 변경한다.
                if("${fn:trim(loop.fieldKey)}" == "K") {
                    $("#keyText").html("<em class=\"required\"></em>${loop.fieldDesc}");
                    $("#"+fieldNm.toUpperCase()).attr("style","border-width:1px;background-color:#ffffff;");
                    $("#"+fieldNm.toUpperCase()).attr("readOnly",false);
                    $("#"+fieldNm.toUpperCase()).focus();
                }
                $("#"+fieldNm.toUpperCase()).val("");
            </c:forEach>

            $("#deleteBtn").attr("style","display:none");
            $('#editorFrm').attr('action', "/segment/insertTarget.do");
        });

        // 검색어 엔터키 입력
        $("#searchWord").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            if($("#searchWord").val() == '') {
                $("#allViewBtn").trigger("click");
                return false;
            }

            $("#searchColumn").val($("#searchColumn option:selected").val());
            $("#targetListFrm").submit();
        });

        // (레이어) 닫기 이미지 클릭
        $("#layerCloseImg").on("click", function(event) {
            if(editorStatus==1) {
                editorStatus = 0;
            }
            $("#editorLayer").css("display", "none");
        });

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if(!confirm("<spring:message code='segment.alert.msg.list.5'/>")) {  // 선택한 대상자를 삭제하시겠습니까?
                return;
            }

            $('#editorFrm').attr('action', "/segment/deleteTarget.do").submit();
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            // Validation Check - fieldKey 값이 K,N,E 인 아이디,이름,이메일에 대해서 체크한다.
            <c:forEach var="loop" items="${sementicList}" varStatus="i">
                  var filedNm = $("#${loop.fieldNm}").val();
                if(filedNm == "") {
                   alert("<spring:message code='segment.alert.msg.list.2' arguments='${loop.fieldDesc}'/>");  // {0}은 필수 입력항목입니다.
                   return;
                }
               // 이메일 형식 체크
               if("${loop.fieldKey}" == "E") {
                    if(!$.mdf.isEmail($("#${loop.fieldNm}").val())) {
                        alert("<spring:message code='segment.alert.msg.list.3'/>");  // 올바른 이메일 형식이 아닙니다.
                        return;
                    }
               }
            </c:forEach>

            if(!confirm("<spring:message code='segment.alert.msg.list.4'/>")) {  // 선택한 대상자를 저장하시겠습니까?
                return;
            }

            $("#editorFrm").submit();
        });

        // 대상자 목록에서 대상자 선택
        $('#targetListTable tbody').on('click', 'tr', function(event) {
            $('#targetListTable tr.selected').removeClass('selected');
            $(this).addClass('selected');
        });
    }

    function initPage() {
        if("${message}" == "fail") {
            alert("<spring:message code='segment.alert.msg.list.6'/>");  // 대상자 리스트 조회에 실패하였습니다.
            window.close();
        } else if("${message}" == "success") {
            alert("<spring:message code='common.alert.test.msg3'/>");  // 저장되었습니다.
            //opener.location.reload(); 새로고침이 필요없어서 주석 처리함.
        } else if("${message}" == "deleteSuccess") {
            alert("<spring:message code='common.alert.test.msg4'/>");  // 삭제 되었습니다.
        } else if("${message}" == "updateFail") {
            alert("<spring:message code='segment.alert.msg.list.7'/>");  // 대상자 정보 수정에 실패하였습니다.
        } else if("${message}" == "deleteFail") {
            alert("<spring:message code='segment.alert.msg.list.8'/>");  // 대상자 삭제에 실패하였습니다.
            //opener.location.reload();
        } else if("${message}" == "checkFail") {
            alert("<spring:message code='segment.alert.msg.list.9'/>");  // 중복된 ID값이 존재하여 대상자를 추가할 수 없습니다.
        }

        if(${param.gotoList != 'Y'}) {
            $("#allViewBtn").trigger("click");
        }

        new mdf.DataTable("#targetListTable");
    }

    // 수정 / 삭제 레이어 활성화
    function callEditorLayer(row) {
        // 기존에 선택된 라인과 현재 선택된 라인이 같고 현재 레이어가 열린 상태인 경우 레이어를 닫는다.
        if(selectRow == row && editorStatus == 1) {
            $("#editorLayer").css("display", "none");
            editorStatus = 0;
        } else { // 레이어를 열고 값을 셋팅한다.
            var fieldNm = "";
            <c:forEach var="loop" items="${sementicList}" varStatus="i">
                fieldNm = "${loop.fieldNm}";

                $("#"+fieldNm.toUpperCase()).val($("#column${loop.fieldSeq}_"+row).val());

                // 수정시 fieldKey 값이 K 인 ID에 해당하는 값은 유일키이므로 수정되서는 안된다.
                // 따라서 input 박스의 readOnly 옵션과 border를 0px로 하여 수정되지 않는 필드로 표시
                if("${fn:trim(loop.fieldKey)}" == "K") {
                    $("#keyText").html("<em class=\"required\"></em>${loop.fieldDesc}");
                    $("#"+fieldNm.toUpperCase()).attr("style","border:0px;background-color:#f3f3f3;");
                    $("#"+fieldNm.toUpperCase()).attr("readOnly",true);
                }
            </c:forEach>
            $("#del").attr("style","display:blcok");
            $("#editorLayer").css("display", "block");
            editorStatus = 1;

            $('#editorFrm').attr('action', "/segment/updateTarget.do");
        }
        selectRow = row;
    }

    // 수정 / 삭제 레이어  감추기
    function layerHidden() {
        editorStatus = 0;
        $("#editorLayer").css("display", "none");
    }
</script>
</head>
<body>
<div class="card pop-card">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><!-- 대상자 리스트(명) -->
                <h5 class="mb-0"><spring:message code="segment.msg.lists"/><em>(<fmt:formatNumber type="number" value="${targetListTotalCount}" /> <spring:message code="common.menu.persons"/>)</em></h5>
            </div>
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body mb-0">
        <form id="targetListFrm" name="targetListFrm" action="/segment/targetListPopup.do" method="post"><!-- /segment/segmentpopup.do -->
        <input type="hidden" name="segmentNo" id="segmentNo" value="${segmentVo.segmentNo}" />
        <input type="hidden" name="segType" id="segType" value="${segmentVo.segType}" />
        <input type="hidden" name="gotoList" value="Y" />
        <input type="hidden" name="serviceType" value="${serviceType}" />

        <div class="row align-items-center py-1 table_option">
            <div class="col-6"><!-- buttons -->
                <button class="btn btn-sm btn-outline-primary btn-round" id="allViewBtn">
                    <i class="fas fa-search"></i> <spring:message code="button.view.all"/><!-- 전체보기 -->
                </button>
                <c:forEach var="menuVo" items="${userMenuList }" varStatus="status">
                    <c:if test="${menuVo.menuLinkUrl eq '/segment/segmentList.do' && menuVo.writeAuth eq 'W'}"><!-- /segment/segment.do -->
                        <c:if test="${segmentVo.segType eq 'F'}">
                            <button class="btn btn-sm btn-outline-primary btn-round" id="addBtn">
                                <i class="fas fa-plus"></i> <spring:message code="button.add"/><!-- 추가 -->
                            </button>
                        </c:if>
                      </c:if>
                </c:forEach>
            </div>
            <div class="col-6 searchWrap"><!-- search -->
                <div class="form-group searchbox search_input">
                    <select class="form-control form-control-sm" name="searchColumn" id="searchColumn">
                        <c:forEach var="semanticVo" items="${sementicList}" varStatus="status">
                            <option value="${semanticVo.fieldNm}" ${semanticVo.fieldNm eq param.searchColumn?"selected":""}>${semanticVo.fieldDesc}</option>
                        </c:forEach>
                    </select>
                    <input type="search" class="form-control form-control-sm" aria-controls="datatable-basic" name="searchWord" id="searchWord" maxlength="30" value="${param.searchWord}" />
                    <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                        <spring:message code="button.search"/><!-- 검색 -->
                    </button>
                </div>
            </div><!-- //search -->
        </div><!-- e.search area & buttons -->

        <div class="table-responsive gridWrap">
            <table class="table table-sm dataTable table-fixed">
                <colgroup>
                    <col width="16%" />
                    <col width="*" />
                    <col width="15%" />
                    <col width="30%" />
                </colgroup>
                <tbody>
                <tr>
                    <th scope="row"><spring:message code="segment.menu.sql.target"/></th><!-- 대상자명 -->
                    <td>${segmentVo.segmentNm}</td>
                    <th scope="row"><spring:message code="common.menu.type"/></th><!-- 유형 -->
                    <td>
                        <c:choose>
                            <c:when test="${'F' eq segmentVo.segType}"><spring:message code="segment.type.F"/></c:when>
                            <c:when test="${'S' eq segmentVo.segType}"><spring:message code="segment.type.S"/></c:when>
                            <c:when test="${'Q' eq segmentVo.segType}"><spring:message code="segment.type.Q"/></c:when>
                            <c:when test="${'L' eq segmentVo.segType}"><spring:message code="segment.type.L"/></c:when>
                            <c:when test="${'R' eq segmentVo.segType}"><spring:message code="segment.type.R"/></c:when>
                            <c:when test="${'O' eq segmentVo.segType}"><spring:message code="segment.type.O"/></c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><spring:message code="segment.menu.sql.dbserver"/></th><!-- DB 서버 -->
                    <td>${dbInfoVo.serverNm}</td>
                    <th scope="row"><spring:message code="segment.menu.sql.share"/></th><!-- 공유 -->
                    <td>${segmentVo.shareYn}</td>
                </tr>
                <tr>
                    <th scope="row"><spring:message code="segment.menu.sql.regdate"/></th><!-- 등록일 -->
                    <td>
                        <fmt:parseDate value="${segmentVo.lastUpdateDt}" var="lastUpdateDt" pattern="yyyymmdd" />
                        <fmt:formatDate value="${lastUpdateDt}" pattern="yy-mm-dd" />
                    </td>
                    <th scope="row"><spring:message code="common.menu.creator"/></th><!-- 작성자 -->
                    <td>${segmentVo.nameKor}</td>
                </tr>
                </tbody>
            </table>
        </div><!-- //Light table -->
        <div class="table-responsive mt-0">
            <table class="table table-sm dataTable table-hover" id="targetListTable">
                <thead class="thead-light">
                <c:choose>
                         <c:when test="${serviceType eq 'EC'}"> <!-- 이케어의 경우 컬럼명 표시 -->
                            <tr>
	                           <c:forEach var="semancticHeader" items="${semancticHeaders}" >
	                               <th scope="row">${semancticHeader}</th>
	                            </c:forEach>
                            </tr>
                         </c:when>
                         <c:otherwise>  <!-- 캠페인의 경우 semantic 표시 -->
                            <tr>
		                   <c:forEach var="loop" items="${sementicList}" varStatus="i">
		                       <th scope="row">${loop.fieldDesc}</th>
		                       <c:choose>
		                           <c:when test="${loop.fieldKey eq 'N'}"><c:set var="nameFieldIndex" value="${i.index}"/></c:when>
		                           <c:when test="${loop.fieldKey eq 'E'}"><c:set var="emailFieldIndex" value="${i.index}"/></c:when>
		                           <c:when test="${loop.fieldKey eq 'S'}"><c:set var="smsFieldIndex" value="${i.index}"/></c:when>
		                           <c:when test="${loop.fieldKey eq 'F'}"><c:set var="faxFieldIndex" value="${i.index}"/></c:when>
		                       </c:choose>
		                   </c:forEach>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </thead>
                <tbody>

                <c:forEach var="rows" items="${queryDataList}" varStatus="r">
                <tr <c:if test="${segmentVo.segType eq 'F'}">style="cursor:pointer"</c:if>>
                    <c:forEach var="loop" items="${rows}" varStatus="i">
                        <input type="hidden" name="columnSeq_${i.index+1}_${r.index}" id="columnSeq_${i.index+1}_${r.index}" value="${i.index}" />
                        <input type="hidden" name="column${i.index+1}_${r.index}" id="column${i.index+1}_${r.index}" value="${loop}" />
                        <td id="td_${i.index+1}_${r.index}" class="text-left"
                            <c:if test="${segmentVo.segType eq 'F'}">onClick="javascript:callEditorLayer(${r.index});"</c:if>>
                            <c:choose>
                                <c:when test="${i.index eq nameFieldIndex}">${wiseu:nameMask(loop)}</c:when>
                                <c:when test="${i.index eq emailFieldIndex}">${wiseu:mailMask(loop)}</c:when>
                                <c:when test="${i.index eq smsFieldIndex}">${wiseu:telMask(loop)}</c:when>
                                <c:when test="${i.index eq faxFieldIndex}">${wiseu:telMask(loop)}</c:when>
                                <c:otherwise>${loop}</c:otherwise>
                            </c:choose>
                        </td>
                    </c:forEach>
                </tr>
                </c:forEach>
                </tbody>
            </table>
        </div><!-- //Light table -->

        <!-- 페이징 -->
        <div class="mb--1">${paging}</div>
        </form>
    </div><!-- e.card body -->
</div>

<%-- 추가 / 수정 / 삭제시 사용될 레이어 시작 --%>
<div id="editorLayer" class="dp-none" style="left:300px; top:50px; width: 350px; position: absolute; z-index: 11;">
    <form id="editorFrm" name="editorFrm" method="post">
    <input type="hidden" name="nowPage" id="nowPage" value="${nowPage}" />
    <input type="hidden" name="segmentNo" id="segmentNo" value="${segmentVo.segmentNo}" />
    <input type="hidden" name="targetNo" id="targetNo" value="${targetNo}" />
    <input type="hidden" name="segType" id="segType" value="${segmentVo.segType}" />
    <input type="hidden" name="gotoList" value="Y" />

    <div class="card pop-card" style="height: 400px; border: 1px solid #c1c1c1;">
        <div class="card-header"><!-- title -->
            <div class="row table_option">
                <div class="col-10">
                    <h5 class="mb-0"><spring:message code="segment.msg.target"/></h5><!-- 대상자 추가 -->
                </div>
                <div class="col-2 justify-content-end">
                    <img src="/images/common/close.png" id="layerCloseImg" style="cursor: pointer;">
                </div><!-- 닫기 -->
            </div>
        </div>
        <div class="card-body">
            <div class="table-responsive gridWrap">
                <table class="table table-sm dataTable table-fixed">
                    <colgroup>
                        <col width="110" />
                        <col width="*" />
                    </colgroup>
                    <tbody>
                    <tr>
                        <th scope="row" id="keyText"></th>
                        <td><input type="text" class="form-control form-control-sm" id="CUSTOMER_ID" name="CUSTOMER_ID" readOnly /></td>
                    </tr>

                    <c:forEach var="loop" items="${sementicList}" varStatus="status">
                        <c:if test="${fn:trim(loop.fieldKey) ne 'K'}">
                            <tr>
                                <th scope="row"><em class="required"></em>${loop.fieldDesc}</th>
                                <td><input type="text" class="form-control form-control-sm" id="${loop.fieldNm}" name="${loop.fieldNm}" /></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div><!-- //Light table -->
        </div><!-- e.card body -->

        <c:forEach var="menuVo" items="${userMenuList}" varStatus="status">
        <c:if test="${menuVo.menuLinkUrl eq '/segment/segmentList.do' && menuVo.writeAuth eq 'W'}"><!-- /segment/segment.do -->
        <div id="divbtn" class="card-footer">
            <button type="button" id="deleteBtn" class="btn btn-outline-primary">
                <spring:message code="button.delete"/><!-- 삭제 -->
            </button>
            <button type="button" id="saveBtn" class="btn btn-outline-primary">
                <spring:message code="button.save"/><!-- 저장 -->
            </button>
        </div>
        </c:if>
        </c:forEach>
    </div>
    </form>
</div><!-- e.editorLayer -->
</body>
</html>
