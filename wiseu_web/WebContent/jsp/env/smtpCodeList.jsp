<%-------------------------------------------------------------------------------------------------
 * - [환경설정>코드관리] 코드관리 - 코드 목록 <br/>
 * - URL : /env/smtpCodeList.do <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvSmtpCodeController  <br/>
 * - 이전 파일명 : env_smtpcodelist.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 추가 버튼 클릭
        $("#addBtn").on("click", function(event) {
            event.preventDefault();
            $("#addBtn").hide();
            $("#addTr").show();
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            event.preventDefault();

            var rules = {
                errorCd   : {notBlank : true, digits : true, range : [0, 999]},
                errorDesc : {notBlank : true, maxlength : 50}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var param = {
                categoryCd : '${categoryCd}',
                errorCd : $("input[name=errorCd]").val(),
                errorDesc : $("input[name=errorDesc]").val(),
                spamYn : $("input[name=spamYn]:checked").val()
            };

            if($.mdf.isBlank(param.spamYn)) param.spamYn = 'N';

            $.post("/env/insertSmtpCodeList.json", $.param(param, true), function(result) {
                if(result.code == "OK" && result.rowCount == 1) {
                    alert("<spring:message code='env.alert.smtpcode.msg4'/>");  // 등록되어 있는 에러코드입니다.
                } else {
                    alert("<spring:message code='env.alert.smtpcode.reg3'/>");  // 새로운 에러코드 정보가 추가되었습니다.
                    document.location.reload();
                }
            });
        });

        // (입력폼) 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            event.preventDefault();
            $.mdf.resetForm("#updateForm");
            $("#addTr").hide();
            $("#addBtn").show();
        });
    }

    function deleteCode(errorCd, categoryCd) {
        if(confirm("<spring:message code='env.alert.smtpcode.del3'/>")) {  // 해당 에러 코드 정보를 삭제하시겠습니까?
            var param = {
                categoryCd : categoryCd,
                errorCd : errorCd
            };

            $.post("/env/deleteSmtpCodeList.json", $.param(param, true), function(result) {
                alert("<spring:message code='env.alert.smtpcode.del4'/>");  // 해당 에러코드 정보가 삭제되었습니다.
                document.location.reload();
            });
        }
    }
</script>

<form id="updateForm" name="updateForm">
<table class="table table-sm dataTable table-fixed" id="smtpErr" style="margin-top: -1px !important;">
    <thead class="thead-light">
    <tr>
        <th scope="col" width="10%" class="control-text-sm"><spring:message code="env.menu.smtpcode.code"/></th><!-- 에러 코드 -->
        <th scope="col"><spring:message code="env.menu.smtpcode.des"/></th><!-- 설명 -->
        <th scope="col" width="10%"><spring:message code="env.menu.smtpcode.spam"/></th><!-- 스팸 -->
        <th scope="col" width="16%" class="control-text-sm">
            <c:if test="${sessionScope.write eq 'W'}">
                <c:if test="${!empty categoryCd}">
                    <button id="addBtn" class="btn btn-sm btn-outline-primary btn-round">
                        <i class="fas fa-plus"></i> <spring:message code="button.add" /><!-- 추가 -->
                    </button>
                </c:if>
            </c:if>
        </th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="codeVo" items="${codeList}" varStatus="status">
    <tr>
        <td scope="row">${codeVo.errorCd}</td>
        <td class="text-left">${codeVo.errorDesc}</td>
        <td>${codeVo.spamYn}</td>
        <td>
            <c:if test="${sessionScope.write eq 'W'}">
            <button class="btn btn-sm btn-outline-danger" onClick="deleteCode('${codeVo.errorCd}', '${codeVo.categoryCd}')">
                <i class="fas fa-minus"></i> <spring:message code="button.delete"/><!-- 삭제 -->
            </button>
            </c:if>
        </td>
    </tr>
    </c:forEach>
    <tr id="addTr" class="dp-none"><!-- 추가 버튼 클릭시 출력될 입력폼 -->
        <td><input type="text" class="form-control form-control-sm" name="errorCd"/></td>
        <td><input type="text" class="form-control form-control-sm" name="errorDesc"/></td>
        <td class="pl-4">
            <div class="custom-control custom-checkbox custom-control-inline mr-0">
                <input type="checkbox" name="spamYn" id="spam" value="Y" class="custom-control-input"/>
                <label class="custom-control-label" for="spam"></label>
            </div>
        </td>
        <td>
            <button class="btn btn-sm btn-outline-primary btn-round" id="saveBtn">
                <i class="fas fa-save"></i> <spring:message code="button.save"/><!-- 저장 -->
            </button>
            <button class="btn btn-sm btn-outline-danger" id="deleteBtn">
                <i class="fas fa-minus"></i> <spring:message code="button.delete"/><!-- 삭제 -->
            </button>
        </td>
    </tr>
    </tbody>
</table>
</form>