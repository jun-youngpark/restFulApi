<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] 캠페인 테스트발송(팝업)
 * - [이케어>이케어 등록>2단계] 이케어 테스트발송(팝업)
 * - URL : /common/testSendPopup.do
 * - Controller : com.mnwise.wiseu.web.common.web.TestMailSendController
 * - 이전 파일명 : testSend_popup.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="campaign.alt.action.tsend"/></title><!-- 테스트 발송 -->
<%@ include file="/jsp/include/plugin.jsp" %>
<%@ include file="/jsp/include/pluginTree.jsp" %>
<script language="javascript">
    var tmplTree;

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // (테스터) 삭제 버튼 클릭
        $('#deleteBtn').on("click", function(event) {
            if($("#testerFrm input:checkbox[name=checkSeqNo]").is(":checked") == false) {
                alert('<spring:message code="common.alert.test.msg17"/>');  // 삭제할 테스트 대상자를 선택하세요
                return;
            }

            if(!confirm('<spring:message code="common.alert.test.msg22"/>')) {  // 삭제하시겠습니까?
                return;
            }

            $("#cmd").val("testerDelete");
            $("#testerFrm").attr('action', '/common/deleteTester.do').submit();
        });

        // 파일 올리기 버튼 클릭 - 하단에 파일업로드 화면 출력
        $('#fileUploadBtn').on("click", function(event) {
            <c:if test="${empty testGrpList}">
                alert('<spring:message code="common.alert.test.msg13"/>');  // 생성된 그룹이 없습니다. 그룹을 생성하세요.
                return;
            </c:if>

            $("#testSendResultDiv").hide();
            $("#testerAddDiv").hide();
            $("#saveBtn").hide();
            $("#uploadDiv").show();
            $("#registBtn").show();
        });

        // (파일업로드) 등록 버튼 클릭
        $('#registBtn').on("click", function(event) {
            var rules = {
                fileUpload : {required : true, extension : "csv"}
            };

            if($.mdf.validForm("#testerFileFrm", rules) == false) {
                return;
            }

            if(!confirm('<spring:message code="common.alert.test.msg20"/>')) {  // 업로드 하시겠습니까?
                return;
            }
            $('#testerFileFrm').submit();
        });

        // (테스터) 추가 버튼 클릭 - 하단에 테스터 입력 화면 출력
        $('#addBtn').on("click", function(event) {
            var gridGrpVoLength = $("#gridGrpVoLength").val();
            if(gridGrpVoLength==0) {
                alert('<spring:message code="common.alert.test.msg13"/>');  // 생성된 그룹이 없습니다. 그룹을 생성하세요.
                return;
            }

            $("#testSendResultDiv").hide();
            $("#uploadDiv").hide();
            $("#registBtn").hide();

            $("#testerGrpNm").val($("#gridGrpNm").val());
            $("#seqNo").val("0");
            $("#testReceiverNm").val("");
            $("#testReceiverEmail").val("");
            $("#testReceiverTel").val("");
            $("#testReceiverFax").val("");

            if($("#cmd").val() != "testerUpdate") {
                $("#testerAddDiv").show();
                $("#saveBtn").show();
            }
            $("#cmd").val("testerInsert");
        });

        // (테스터) 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
            var rules = {
                testReceiverNm : {notBlank : true},
                testReceiverEmail : {email : true},
                testReceiverTel : {digits : true},
                testReceiverFax : {digits : true}
            };

            if(${webExecMode} == "1") {
                rules.testReceiverEmail.notBlank = true;
            } else {
                rules.testReceiverTel.notBlank = true;
            }

            if($.mdf.validForm("#testerFrm", rules) == false) {
                return;
            }

            if(!confirm('<spring:message code="common.alert.test.msg23"/>')) {  // 테스터를 저장하시겠습니까?
                 return;
            }

            var cmd = $("#cmd").val();
            if(cmd == "testerInsert") {
                $("#testerFrm").attr('action', '/common/insertTester.do').submit();
            } else if(cmd == "testerUpdate") {
                $("#testerFrm").attr('action', '/common/updateTester.do').submit();
            }
        });

        // 발송 버튼 클릭
        $('#sendBtn').on("click", function(event) {
            if(testSend()) {
                alert('<spring:message code="common.alert.test.msg9_${webExecMode}"/>');  // 선택한 프로모션이 테스트모드로 발송대기중입니다.\\n바로 발송되니 몇 초 후에 확인해 보세요.
            }

            $("#testSendResultDiv").show();
            $("#uploadDiv").hide();
            $("#registBtn").hide();
            $("#testerAddDiv").hide();
            $("#saveBtn").hide();
        });

        // 발송결과보기 버튼 클릭
        $('#sendResultViewBtn').on("click", function(event) {
            $("#testSendResultDiv").show();
            $("#testerAddDiv").hide();
            $("#saveBtn").hide();
            $("#uploadDiv").hide();
            $("#registBtn").hide();

            var x = ('${nowPage}' == "") ? "1" : '${nowPage}';

            //현재 체크 값을 다시 넘긴다.
            var seqNo = "";
            $("input:checkbox[name='checkSeqNo']:checked").each(function() {
                seqNo += $(this).val()+",";
            });
            // serviceNo=${serviceNo}를 serviceNo=${no}로 변경
            // /common/testSend_popup.do
            var url = "/common/testSendPopup.do?no=${no}&serviceType=${serviceType}&serviceNo=${no}&nowPage="+x+"&countPerPage=10&seqNo="+seqNo+"&channelType=${channelType}";
            document.location.href = url;
        });

        // 테스터 전체 선택 체크박스 클릭시
        $("#table-check-all").on("click", function(event) {
            $.mdf.checkAll("#table-check-all");
        });
    }

    function initPage() {
        initTree();  //트리만들기
        var message = "${message}";

        if(message == "save") {
            alert('<spring:message code="common.alert.test.msg3"/>');  // 저장되었습니다.
        } else if(message == "del") {
            alert('<spring:message code="common.alert.test.msg4"/>');  // 삭제 되었습니다.
        } else if(message == "failGrp") {
            alert('<spring:message code="common.alert.test.msg5"/>');  // 하위 그룹이 존재하여 그룹을 삭제할 수 없었습니다.
        } else if(message == "uploadSizeZero") {
            alert('<spring:message code="common.alert.test.msg6"/>');  // 파일 사이즈가 0 입니다.
        } else if(message == "uploadFail") {
            alert('<spring:message code="common.alert.test.msg7"/>');  // 파일 업로드가 실패하였습니다.
        } else if(message == "uploadSizeFail") {
            alert('<spring:message code="common.alert.test.msg8"/>');  // 파일 업로드 최대 사이즈를 초과했습니다.
        }

        //테스트 발송 결과 보기 시 화면 refresh가 되어서 테스트 대상자를 가져와서 체크해 놓는다. 값 비교하는 부분이 빠져서 추가함.
        if('${seqNo}' != '') {
            $("input:checkbox[name='checkSeqNo']").each(function() {
                var val = $(this).val()+",";
                if('${seqNo}'.indexOf(val) > -1) {
                    $(this).attr('checked', 'checked');
                }
            });
        }
    }

    // 부서 트리 만들기
    function initTree() {
        var theTree = tmplTree;
        if(tmplTree) {
            tmplTree.unbindAllEvent();
        }
        tmplTree = new mdf.Tree("#jstree", {
            context : "",
            root_node_id : "${testGrpVo.testGrpCd}",
            data : {
                id : "${testGrpVo.testGrpCd}",
                text : "${testGrpVo.testGrpNm}"
            },
            command : {
                child : {
                    url : "/common/test_group.json?testGrpCd=${testGrpVo.testGrpCd}&no=${no}&serviceType=${serviceType}&channelType=${channelType}",
                    paramYn : "N"
                },
                select_node : {
                    callback : function(data) {
                        var json = {"testGrpCd" : data.node.id, "no" : '${no}', "serviceType": '${serviceType}' , "channelType": '${channelType}'};
                        $.ajax({
                            url: '/common/get_test_group.json',
                            type : "POST",
                            data : json,
                            dataType : 'json',
                            success : function(node) {
                                setGridBody(node.testerPoolList);
                                setGrpVo(node.testGrpVo);
                                return;
                            }
                        });
                    }
                },
                insert : {
                    url : "/common/insertTestGrp.json"
                },
                remove : {
                    url : "/common/deleteTestGrp.json",
                    validate : validateDeleteGrp
                },
                rename : {
                    url : "/common/updateTestGrp.json"
                }
            },
            plugins : ["contextmenu", "search", "state", "types", "unique", "wholerow"]
        });
    }

    function setGrpVo(testGrpVo) {
        if(!testGrpVo) {
            $("#gridGrpVoLength").val(0);
        } else {
            $("#gridGrpVoLength").val(1);
        }

        $("#gridGrpNm").val(testGrpVo.testGrpNm);
        $("#grpNmTd").text(testGrpVo.testGrpNm);
        $("#gridGprCd").val(testGrpVo.testGrpCd);
        $("#testGrpCd").val(testGrpVo.testGrpCd);
        $("#testerFileFrm > #testGrpCd").val(testGrpVo.testGrpCd);
        $("#gridSuperGrpCd").val(testGrpVo.testSupragrpCd);
        $("#gridSuperGrpNm").val(testGrpVo.testSupragrpNm);
    }

    function setGridBody(loop) {
        $("#gridTesterPoolLength").val(loop.length);
        var elementStr = '';
        for(var i=0; i < loop.length; i++) {
            var seqNo = loop[i].seqNo;
            var testReceiverNm = loop[i].testReceiverNm;
            var testReceiverEmail = loop[i].testReceiverEmail;
            var testReceiverTel = loop[i].testReceiverTel;
            var testReceiverFax = loop[i].testReceiverFax;

            elementStr += "<tr style='cursor: pointer;' onclick=\"javascript:testModify('"+seqNo+"','"+testReceiverNm+"','"+testReceiverEmail+"','"+testReceiverTel+"','"+testReceiverFax+"')\">"
            elementStr += '<th scope="row">'
            elementStr += '<div class="custom-control custom-checkbox" onclick="event.cancelBubble=true">'
            if(loop[i].checked=='Y') {
                elementStr += '<input type="checkbox" name="checkSeqNo" id="checkSeqNo_'+i+'" value="'+seqNo+'" class="custom-control-input" checked />'
            } else {
                elementStr += '<input type="checkbox" name="checkSeqNo" id="checkSeqNo_'+i+'" value="'+seqNo+'" class="custom-control-input" />'
            }
            elementStr += '<label class="custom-control-label" for="checkSeqNo_'+i+'"></label>'
            elementStr += '</div>'
            elementStr += '</th>'
            elementStr += '<td>'+(i+1)+'</td>'
            elementStr += '<td>'+testReceiverNm+'</td>'
            elementStr += '<td class="text-left">'+testReceiverEmail+'</td>'
            elementStr += '<td>'+testReceiverTel+'</td>'
            elementStr += '<td class="text-left">'+testReceiverFax+'</td>'
            elementStr += '</tr>'
        }
        $("#gridBody").html(elementStr);
    }

    // 테스터 목록에서 테스터 클릭 - 하단에 테스터 수정 화면 출력
    function testModify(seqNo,testReceiverNm,testReceiverEmail,testReceiverTel, testReceiverFax) {
        $("#uploadDiv").hide();
        $("#registBtn").hide();
        $("#testSendResultDiv").hide();

        $("#testerGrpNm").val($("#gridGrpNm").val());
        $("#testReceiverNm").val(testReceiverNm);
        $("#testReceiverEmail").val(testReceiverEmail);
        $("#testReceiverTel").val(testReceiverTel);
        $("#testReceiverFax").val(testReceiverFax);
        $("#cmd").val("testerUpdate");

        if($("#seqNo").val() == seqNo) {
            $("#testerAddDiv").toggle();
        } else {
            $("#testerAddDiv").show();
            $("#saveBtn").show();
        }

        $("#seqNo").val(seqNo);
    }

    function testSend() {
        if($("#testerFrm input:checkbox[name=checkSeqNo]").is(":checked") == false) {
            alert('<spring:message code="common.alert.test.msg1"/>');  // 대상자를 선택하세요
            return;
        }

        var seqNo = "";
        $("input:checkbox[name=checkSeqNo]:checked").each(function() {
            seqNo += $(this).val()+",";
        });

        if("${channelType}" == "F") {
            if(!validaterFax(seqNo)) return;
        }

        if(!confirm('<spring:message code="common.alert.test.msg2"/>')) {  // 발송하시겠습니까?
            return;
        }

        var param = {
            checkSeqNo : seqNo,
            no : ${no},
            serviceType : "${serviceType}",
            userId : "${sessionScope.adminSessionVo.userVo.userId}",
            grpCd : "${sessionScope.adminSessionVo.userVo.grpCd}"
        };

        // [webExecMode=1] TesterService.insertTestUser(seqNo,${no},"${serviceType}",'${sessionScope.adminSessionVo.userVo.userId}',callback);
        // [webExecMode=2] TesterService.insertSendTestUser(seqNo,${no},"${serviceType}", '${sessionScope.adminSessionVo.userVo.grpCd}', '${sessionScope.adminSessionVo.userVo.userId}', callback);
        $.post("/common/insertTestUser.json", $.param(param, true), function(result) {
            if(result.code == "OK") {
                alert('<spring:message code="common.alert.test.msg9_${webExecMode}"/>');  // 선택한 프로모션이 테스트모드로 발송대기중입니다.\\n바로 발송되니 몇 초 후에 확인해 보세요.
            }
        });
    }

    function validaterFax(seqNo) {
        var arrSeqNo = seqNo.split(",");
        for(var i=0;i<arrSeqNo.length-1;i++) {
            if($("#fax"+ arrSeqNo[i]).val() == "") {
                alert('<spring:message code="common.alert.test.msg30"/>');  // 선택한 대상자에 팩스번호가 존재하지 않습니다.
                return false;
            }
        }
        return true;
    }

    // (그룹) 삭제 버튼 클릭시 유효성 검사
    function validateDeleteGrp() {
        var grpVoLength = $("#gridGrpVoLength").val();
        if(grpVoLength==0) {
            alert('<spring:message code="common.alert.test.msg10"/>');  // 선택된 그룹이 없습니다.
            return false;
        }

        var testerPoolLength = $("#gridTesterPoolLength").val();
        if(testerPoolLength>0) {
            alert('<spring:message code="common.alert.test.msg11"/>');  // 선택한 그룹에 소속된 테스터가 있어 삭제가 불가능합니다.
            return false;
        }

        var testSupragrpCd= $("#gridSuperGrpCd").val();
        if(testSupragrpCd=='0') {
            alert('<spring:message code="common.alert.test.msg12"/>');  // 최상위 부서는 삭제할 수 없습니다.
            return false;
        }

        return true;
    }
</script>
</head>

<body id="body">
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="campaign.alt.action.tsend"/></h5></div><!-- 테스트 발송 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <form id="testerFrm" name="testerFrm" action="/common/testSendPopup.do" method="post"><!-- /common/testSend_popup.do -->
        <input type="hidden" name="gridSuperGrpCd" id="gridSuperGrpCd" />
        <input type="hidden" name="gridSuperGrpNm" id="gridSuperGrpNm" />
        <input type="hidden" name="gridGprCd" id="gridGprCd" />
        <input type="hidden" name="gridGrpNm" id="gridGrpNm" />
        <input type="hidden" name="gridTesterPoolLength" id="gridTesterPoolLength" />
        <input type="hidden" name="gridGrpVoLength" id="gridGrpVoLength" />
        <input type="hidden" name="testSupragrpCd" id="testSupragrpCd" />
        <input type="hidden" name="testGrpCd" id="testGrpCd" value="${testGrpVo.testGrpCd}" />
        <input type="hidden" name="no" value="${no}" />
        <input type="hidden" name="serviceType" value="${serviceType}" />
        <input type="hidden" name="cmd" id="cmd" />
        <input type="hidden" name="seqNo" id="seqNo" />

        <div class="row align-items-center py-1 table_option">
            <div class="col-12 justify-content-end"><!-- buttons -->
                <button class="btn btn-sm btn-outline-primary" id="deleteBtn">
                   <i class="fas fa-trash"></i> <spring:message code="button.delete"/><!-- 삭제 -->
                </button>
                <button class="btn btn-sm btn-outline-primary" id="fileUploadBtn">
                    <i class="fas fa-upload"></i> <spring:message code="button.upload"/><!-- 파일올리기 -->
                </button>
                <button class="btn btn-sm btn-outline-primary" id="addBtn">
                    <i class="fas fa-plus"></i> <spring:message code="button.add"/><!-- 추가 -->
                </button>
                <button class="btn btn-sm btn-outline-primary" id="sendBtn">
                    <i class="fas fa-paper-plane"></i> <spring:message code="button.send"/><!-- 발송 -->
                </button>
                <button class="btn btn-sm btn-outline-primary mr-0" id="sendResultViewBtn">
                    <i class="fas fa-eye"></i> <spring:message code="button.view.sendresult"/><!-- 발송 결과보기 -->
                </button>
            </div>
        </div>

        <div class="container-fluid my-2">
            <div class="row treeWrap tree-high">
                <div class="col-4 px-0"><!-- 그룹정보 -->
                    <div id="jstree" class="treeview_top overflow-auto"></div><!-- 부서 tree-->
                </div>
                <div class="col-8 px-0"><!-- 테스터정보 -->
                    <div class="table-responsive mt--1">
                        <table class="table table-hover table-xs dataTable table-fixed">
                            <thead class="thead-light">
                                <tr>
                                    <th width="20">
                                        <div class="custom-control custom-checkbox">
                                            <input type="checkbox" id="table-check-all" class="custom-control-input" />
                                            <label class="custom-control-label" for="table-check-all"></label>
                                        </div>
                                    </th>
                                    <th width="20">No</td>
                                    <th width="40"><spring:message code="common.menu.name"/></td><!-- 이름 -->
                                    <th width="*"><spring:message code="common.menu.email"/></td><!-- 이메일 -->
                                    <th width="20%"><spring:message code="account.menu.user.contact"/></td><!-- 전화번호 -->
                                    <th width="20%"><spring:message code="common.menu.fax"/></td><!-- 팩스 -->
                                </tr>
                            </thead>
                            <tbody id="gridBody" class="mt--1">
                            </tbody>
                        </table>
                    </div><!--table-->
                </div>
            </div>
        </div>

        <!-- 테스터 추가, 파일올리기, 테스트 발송결과 보기 -->
        <!-- 1. 테스터 추가 -->
        <div class="mt-3 mb--1" id="testerAddDiv" style="display:none;">
            <h1 class="h3 text-primary mb-0"><spring:message code="common.menu.alist"/></h1><!-- 테스터 -->
            <div class="table-responsive gridWrap mb-1">
                <table class="table table-sm dataTable table-fixed">
                    <colgroup>
                        <col width="13%" />
                        <col width="*" />
                        <col width="13%" />
                        <col width="*" />
                    </colgroup>
                    <tbody>
                    <tr>
                        <th><em class="required">required</em><spring:message code="common.menu.name"/></th><!-- 이름 -->
                        <td><input type="text" class="form-control form-control-sm w-75" name="testReceiverNm" id="testReceiverNm" maxlength="25"/></td>
                        <th>
                            <c:if test="${webExecMode eq '1'}"><em class="required">required</em></c:if>
                            <spring:message code="common.menu.email"/><!-- 이메일주소 -->
                        </th>
                        <td><input type="text" class="form-control form-control-sm" name="testReceiverEmail" id="testReceiverEmail" maxlength="50"/></td>
                    </tr>
                    <tr>
                        <th>
                            <c:if test="${webExecMode eq '2'}"><em class="required">required</em></c:if>
                            <spring:message code="account.menu.user.contact"/><!-- 전화번호 -->
                        </th>
                        <td><input type="text" class="form-control form-control-sm w-75" name="testReceiverTel" id="testReceiverTel" maxlength="50"/></td>
                        <th>
                            <spring:message code="common.menu.fax"/><!-- 팩스 -->
                        </th>
                        <td><input type="text" class="form-control form-control-sm w-75" name="testReceiverFax" id="testReceiverFax" maxlength="50"/></td>
                    </tr>
                    <tr>
                        <th><spring:message code="common.menu.gname"/></th><!-- 그룹명 -->
                        <td colspan="3"><input type="text" class="form-control form-control-sm w-75" id="testerGrpNm" style="border: 0px;" readonly /></td>
                    </tr>
                    </tbody>
                </table>
            </div><!-- //Light table -->
        </div><!-- // 1. 테스터 추가 -->
        </form>

        <!-- 2. 파일올리기 -->
        <form id="testerFileFrm" name="testerFileFrm" action="/common/uploadTester.do" method="post" enctype="multipart/form-data">
        <input type="hidden" name="testGrpCd" id="testGrpCd" value="${testGrpVo.testGrpCd}" />
        <input type="hidden" name="no" value="${no}" />
        <input type="hidden" name="serviceType" value="${serviceType}" />
        <input type="hidden" name="channelType" value="${channelType}" />

        <div class="mt-3 mb--1" style="display:none;" id="uploadDiv">
            <h1 class="h3 text-primary mb-0"><spring:message code="common.menu.fupload"/></h1><!-- 파일업로드 -->
            <div class="alert alert-secondary mb-0" role="alert">
                <i class="fas fa-exclamation-circle"></i> <spring:message code="common.msg.warning" />
                <!-- 주소록 파일 형식은 '이름','이메일','전화번호','팩스' 순으로 작성하고 없는항목은 공백으로 처리해야 하며 첫줄에 헤더가 들어가서는 안됩니다. -->
            </div>
            <div class="table-responsive gridWrap mb-1">
                <table class="table table-sm dataTable table-fixed">
                    <colgroup>
                        <col width="13%" />
                        <col width="*" />
                    </colgroup>
                    <tbody>
                    <tr>
                        <th><em class="required">required</em><spring:message code="common.menu.fselect"/></th><!-- 파일 선택 -->
                        <td>
                            <div class="custom-file custom-file-sm w-75">
                                <input type="file" class="custom-file-input custom-file-input-sm" name="fileUpload" id="fileUpload">
                                <label class="custom-file-label custom-file-label-sm mb-0" for="fileUpload"></label>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th><spring:message code="common.menu.gname"/></th><!-- 그룹명 -->
                        <td id="grpNmTd" class="form-control-sm">${testGrpVo.testGrpNm}</td>
                    </tr>
                    </tbody>
                </table>
            </div><!-- //Light table -->
        </div><!-- // 2. 파일올리기 -->
        </form>

        <!--3. 테스트 발송결과-->
        <div class="mt-3" id='testSendResultDiv'>
            <h1 class="h3 text-primary mb-0"><spring:message code="common.menu.sresult"/></h1><!-- 발송결과 -->
            <c:import url="/common/testSendResultList.do" charEncoding="utf-8"><%-- /common/tester_popup_send_view.do --%>
                <c:param name="serviceNo" value="${serviceNo}" />
                <c:param name="serviceType" value="${serviceType}" />
            </c:import>
        </div><!--// 3. 테스트 발송결과-->

        <div class="card-footer">
            <button class="btn btn-xs btn-outline-primary" id="saveBtn" style="display:none;">
                <spring:message code="button.save"/><!-- 저장 -->
            </button>
            <button class="btn btn-xs btn-outline-primary" id="registBtn" style="display:none;">
                <spring:message code="button.regist"/><!-- 등록 -->
            </button>
        </div>
    </div>
</div>
</body>
</html>