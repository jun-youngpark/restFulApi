<%-------------------------------------------------------------------------------------------------
 * - [환경설정>메시지 발신자 정보] 메시지 발신자 정보 <br/>
 * - URL : /env/senderInfo.do <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvSenderInfoControllerp <br/>
 * - 이전 파일명 : env_senderinfo.jsp
 -------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="env.msg.senderinfo"/></title><!-- 발신자 정보 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                senderNm        : {notBlank : true},
                senderEmail     : {notBlank : true, email : true},
                retmailReceiver : {notBlank : true, email : true},
                senderTel       : {notBlank : true, telnum : true},
                senderFax       : {notBlank : true, telnum : true}
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            if(confirm("<spring:message code='env.alert.senderinfo.save1'/>")) {  // 발신자 정보를 저장하시겠습니까?
                if($.mdf.isBlank($("input[name=userId]").val())) {
                    $('#updateForm').attr('action', "/env/insertSenderInfo.do").submit();
                } else {
                    $('#updateForm').attr('action', "/env/updateSenderInfo.do").submit();
                }
            }
        });
    }

    function initPage() {
        <c:if test="${ret == 'success'}">
        alert("<spring:message code='env.alert.senderinfo.save2'/>");  // 발신자 정보가 저장되었습니다.
        </c:if>
    }
</script>
</head>

<body>
<form id="updateForm" name="updateForm" method="post">
<input type="hidden" name="seqNo" value="1"/>
<input type="hidden" name="userId" value="${envSenderInfoVo.userId}"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="env.msg.senderinfo"/></h3>
            </div>

            <div class="card-body gridWrap">
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="15%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.senderinfo.sender"/></th><!-- 발신자명 -->
                            <td><input type="text" class="form-control form-control-sm w-50" value="${envSenderInfoVo.senderNm}" name="senderNm" /></td>
                        </tr>

                        <c:if test="${fn:contains(envSenderInfoVo.channelUseList, 'M')}">
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.senderinfo.senderemail"/></th><!-- 발신자 이메일 -->
                            <td><input type="text" class="form-control form-control-sm w-50" value="${envSenderInfoVo.senderEmail}" name="senderEmail" /></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.senderinfo.returnemail"/></th><!-- 반송메일 경로 -->
                            <td><input type="text" class="form-control form-control-sm w-50" value="${envSenderInfoVo.retmailReceiver}" name="retmailReceiver" /></td>
                        </tr>
                        </c:if>

                        <c:if test="${fn:contains(envSenderInfoVo.channelUseList, 'S')}">
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.senderinfo.contact"/></th><!-- 발신자 전화번호 -->
                            <td><input type="text" class="form-control form-control-sm w-50" value="${envSenderInfoVo.senderTel}" name="senderTel"/></td>
                        </tr>
                        </c:if>

                        <c:if test="${fn:contains(envSenderInfoVo.channelUseList, 'F')}">
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="env.menu.senderinfo.sfax"/></th><!-- 발신자 팩스번호 -->
                            <td class="border-right-0"><input type="text" class="form-control form-control-sm w-50" value="${envSenderInfoVo.senderFax}" name="senderFax"/></td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- // Light table -->

                <c:if test="${sessionScope.write eq 'W'}">
                <div class="card-footer pb-4 btn_area"><!-- button area -->
                    <button class="btn btn-outline-primary" id="saveBtn">
                        <spring:message code="button.save"/><!-- 저장 -->
                    </button>
                </div>
                </c:if>
            </div><!-- //  grid -->
        </div><!-- // card-->
    </div>
</div>
</form>
</body>
</html>
