<%-------------------------------------------------------------------------------------------------
 * - [템플릿>브랜드톡 템플릿 등록] 브랜드톡템플릿 등록
 * - URL : /template/brtTemplateReg.do (페이지출력)
 * - URL : /template/brtTemplateReg.do (저장버튼을 누를시)
 * - 이전파일명 : brandtalkTemplateReg.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.brandtalk.reg.title" /></title><!-- 브랜드톡 템플릿 등록 -->
<script src="/js/template/kakao_template.js"></script>
<script type="text/javascript">
    //휴대폰번호,이미지 체크 정규식
    var expPhone =/(080)-([0-9]{3,4})-([0-9]{4})$/;

    $(document).on('click','#delBtn',function() {
        $(this).closest("tr").remove()
    });

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 템플릿 길이 필터링
        $('#content').keyup(function (event) {
            event.preventDefault();
            getBytes();
        });

        $("input[name=messageType]").on("click", function(event) {
            getBytes();
        });

        $("input[name=contentType]").on("click", function(event) {
            var contentType = $(this).val();
            if(contentType=='F') {
                $("#content").attr("placeholder","");
            } else {
                // * 변수형 선택 시 템플릿 내용과 버튼 링크에 (WL/AL) 변수 입력이 가능합니다.
                // * 변수는 #{변수명} 규칙으로 입력해야하며, 변수명은 발송 시 활용되는 값으로 최대 20자 이내 한/영/숫자/'-','_'로만 작성 가능합니다.
                // * 동일한 변수값을 사용할 경우 변수명을 중복으로 입력할 수 있으며, 변수는 중복 제외 최대 20개까지 입력이 가능합니다.
                $("#content").attr("placeholder","<spring:message code='template.alert.msg.brandtalk.variableType' />");
            }
        });

        // (버튼) 추가 버튼 클릭
        $("#addBtn").on("click", function(event) {
            var rowCount = $('.kakaoButtonList').length;
            if(rowCount > 4) {
                alert('<spring:message code="template.alert.kakao.button.exceed.limit" />');  // 버튼은 5개까지 만들 수 있습니다.
                return;
            }
            var linkType = $("#buttonLinkType").val();
            $('#kakao_button_table > tbody:last-child').append(makeKakaoButton(linkType));
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                templateName     : {notBlank : true, maxbyte : 50},
                unsubscribeContent : {notBlank : true, dnd : true},
                brandTalkImageFile : {notBlank : true, extension : "jpg,png"},
                imageLink : {notBlank : true, url : true},
                content     : {
                    notBlank : true,
                    maxlength : function() {
                        var contentLength = $("textarea[name=content]").val().length;
                        var msgType = $("input:radio[name=messageType]:checked").val();

                        if(msgType == 'I' && contentLength > 1000) {
                            return 1000;
                        } else if(msgType == 'W' && contentLength > 400) {
                            return 400;
                        }

                        return 10000;
                    }
                }
            };

            $("#updateForm").addKkoButtonValidRule(rules); //kakao_template.js
            if($.mdf.validForm("#updateForm") == false) {
                return;
            }
            $("#button").val(convertKakaoButtonsToJson())
            $("#updateForm").ajaxForm({
                url : "/template/brtTemplateReg.do",  // /template/brandtalkTemplateReg.do
                type : "POST",
                processData: false,
                dataType: "json",
                success : function(result) {
                    if(result.code == "OK") {
                        alert('<spring:message code="template.alert.kakao.request.success"/>');  // 요청이 처리되었습니다.
                        location.href="/template/brtTemplateList.do";
                    } else {
                        alert(result.message);
                    }
                },
                error: function(request,status,error) {
                    $.mdf.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                    returnBrandTalkTemplate(error);
                }
            });
            $("#updateForm").submit();
        });
    }

    function initPage() {
        $("input:radio[name=authType][value=U]").prop("checked", true);  // 공유유형 선택 - 사용자(U)
        $("input:radio[name=messageType][value=I]").prop("checked", true);  // 메세지 유형 선택 - 기본 이미지(I)
        $("input:radio[name=contentType][value=F]").prop("checked", true);  // 텍스트 유형 선택 - 고정형(F)
        $("#contentLen").html('1000');
    }


    function getBytes() {
        var content = $("textarea[name=content]").val();
        var msgType = $(":input:radio[name=messageType]:checked").val();
        if(msgType == 'I') {
            $('#contentLength').html(content.length + '/1000자');
            if(content.length > 1000) {
                $(this).val(content.substr(0, 1000));
            }
        } else {
            if(content.length > 400) {
                $(this).val(content.substr(0, 400));
            }
            $('#contentLength').html(content.length + '/400자');
        }
    }

    function returnBrandTalkTemplate(result) {
        if(result.code === 'OK'){
            alert("<spring:message code='template.alert.kakao.request.success'/>");  // 요청이 처리되었습니다.
            window.location = '/template/brtTemplateList.do';  // /template/brandtalkTemplateList.do
        } else{
            alert(result.message);
        }
    }
</script>
</head>
<body>
<form id="updateForm" name="updateForm" action="/template/brtTemplateReg.do" method="post" enctype="multipart/form-data"><!-- /template/brandtalkTemplateReg.do -->
<input type="hidden" id='button' name="button" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="template.brandtalk.reg.title" /></h3><!-- 브랜드톡 템플릿 등록 -->
            </div>
            <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="150" />
                            <col width="*" />
                            <col width="135" />
                            <col width="33%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.column.yellowid" /></th><!-- 카카오톡 채널ID -->
                            <td colspan="3">
                                <select class="form-control form-control-sm w-50" name="senderKey" id="senderKey">
                                    <c:forEach var="kakaoProfile" items="${kakaoProfileList}" varStatus="status">
                                        <option value="${kakaoProfile.kakaoSenderKey}">${kakaoProfile.kakaoYellowId}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.menu.mcname" /></th><!-- 템플릿명 -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm" name="templateName" maxlength="100" /></td>
                        </tr>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_U" value="U" class="custom-control-input" />
                                    <label class="custom-control-label" for="authType_U"><spring:message code="template.type.user"/></label><!-- 사용자 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_G" value="G" class="custom-control-input" />
                                    <label class="custom-control-label" for="authType_G"><spring:message code="template.type.group"/></label><!-- 부서 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_A" value="A" class="custom-control-input" />
                                    <label class="custom-control-label" for="authType_A"><spring:message code="template.type.all"/></label><!-- 전체 -->
                                </div>
                            </td>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.brandtalk.unsubscribeContent" /></th><!-- 수신거부 전화번호 -->
                            <td><input type="text" class="form-control form-control-sm w-75" name="unsubscribeContent" maxlength="20" /></td>
                        </tr>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.menu.message.type" /></th><!-- 메세지 유형 -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="messageType" id="messageType_I" value="I" class="custom-control-input" />
                                    <label class="custom-control-label" for="messageType_I"><spring:message code="template.brandtalk.messageType.I"/></label><!-- 기본 이미지 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="messageType" id="messageType_W" value="W" class="custom-control-input" />
                                    <label class="custom-control-label" for="messageType_W"><spring:message code="template.brandtalk.messageType.W"/></label><!-- 와이드 이미지 -->
                                </div>
                            </td>
                            <th><em class="required">required</em><spring:message code="template.brandtalk.contentType" /></th><!-- 텍스트 유형 -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="contentType" id="contentType_F" value="F" class="custom-control-input" />
                                    <label class="custom-control-label" for="contentType_F"><spring:message code="template.brandtalk.contentType.F"/></label><!-- 고정형 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="contentType" id="contentType_V" value="V" class="custom-control-input" />
                                    <label class="custom-control-label" for="contentType_V"><spring:message code="template.brandtalk.contentType.V"/></label><!-- 변수형 -->
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.brandtalk.image"/></th><!-- 브랜드톡 이미지 -->
                            <td colspan="3">
                                <div class="custom-file w-50">
                                    <input type="file" class="custom-file-input ustom-file-input-sm" name="brandTalkImageFile" id="brandTalkImageFile">
                                    <label class="custom-file-label custom-file-label-sm" for="brandTalkImageFile"></label>
                                </div>
                                <ul class="notice-text text-left mb-0 mt-1">
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help4"/></li>
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help5"/></li>
                                </ul>
                            </td>
                        </tr>
                        <tr>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.brandtalk.link"/></th><!-- 브랜드톡 이미지 링크 -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm" name="imageLink" id="imageLink" /></td>
                        </tr>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.menu.mcontent" /></th><!-- 템플릿 내용 -->
                            <td colspan="3">
                                <div class="form-row">
                                    <div class="col-3">
                                        <textarea class="form-control textarea-mobile" id="content" name="content"></textarea>
                                        <div class="textarea-font-style">
                                            <span class="count-text" id="contentLength"></span>
                                        </div>
                                    </div>
                                    <div class="col-9">
                                        <ul class="notice-text pl-3 mb-0">
                                            <li class="text-danger"><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help1"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help2"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help3"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help4"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help5"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help6"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help7"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help8"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help9"/></li>
                                            <li><em class="li-circle">li-circle</em><spring:message code="template.brandtalk.reg.help10"/></li>
                                        </ul>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code='template.kakao.button' /></th><!-- 버튼 -->
                            <td colspan="3">
                                <select class="form-control form-control-sm d-inline-block w-auto" id="buttonLinkType">
                                    <option value="WL"><spring:message code='template.kakao.link.type.WL' /></option><!-- 웹 링크 -->
                                    <option value="AL"><spring:message code='template.kakao.link.type.AL' /></option><!-- 앱 링크 -->
                                    <option value="DS"><spring:message code='template.kakao.link.type.DS' /></option><!-- 배송조회 -->
                                    <option value="BK"><spring:message code='template.kakao.link.type.BK' /></option><!-- 봇키워드 -->
                                    <option value="MD"><spring:message code='template.kakao.link.type.MD' /></option><!-- 메시지 전달 -->
                                </select>
                                <button type="button" id="addBtn" class="btn btn-sm btn-outline-primary ml-2">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" class="p-0"><!-- 카카오 버튼-->
                                <table id="kakao_button_table" class="table table-sm dataTable table-fixed" style="margin: -1px !important; border-bottom-style: none;">
                                    <colgroup>
                                        <col width="150" />
                                        <col width="8%" />
                                        <col width="65" /><!-- 버튼명 -->
                                        <col width="*" />
                                        <col width="115" /><!-- 모바일 웹링크/Android 앱링크 -->
                                        <col width="19%" />
                                        <col width="90" /><!-- PC 웹링크/iOS 앱링크 -->
                                        <col width="19%" />
                                    </colgroup>
                                    <tbody>

                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- //card body -->

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <button type="button" id="saveBtn" class="btn btn-outline-primary">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div><!-- e.button area -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>
<%@ include file="/jsp/include/makeKkoButton_inc.jsp" %>
</body>
</html>