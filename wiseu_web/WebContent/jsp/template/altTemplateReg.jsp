<%-------------------------------------------------------------------------------------------------
  * - [템플릿>알림톡 템플릿 등록] 알림톡 템플릿 등록 화면 출력
  * - URL : /template/altTemplateReg.do
  * - Controller : com.mnwise.wiseu.web.template.web.AlimtalkTemplateRegController
  * - 이전 파일명 : alimtalk_template_reg.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.msg.reg" /></title><!-- 템플릿 등록 -->
<script src="/js/template/kakao_template.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 등록방식 라디오버튼 선택
        $('input[name=uploadType]').on('click', function(event) {
            var uploadType = $('input[name="uploadType"]:checked').val();

            if(uploadType == 'S') {  // 단건
                $(".single_div").removeClass("dp-none");
                $(".mass_div").addClass("dp-none");
            } else if(uploadType == 'F') {  // 파일
                $(".mass_div").removeClass("dp-none");
                $(".single_div").addClass("dp-none");
            }
        });

        // 등록 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if($('input[name=uploadType]:checked').val() == 'S') {  // 등록방식이 단건인 경우
                var rules = {
                    contsNm     : {notBlank : true, maxbyte : 50},
                    kakaoTmplCd : {notBlank : true, alphaDigitUnderscore : true, rangelength : [5,20]},
                    content     : {notBlank : true, maxlength : 1000},
                    categoryGroup     : {selected : true},
                    image      : {required : true, extension : "jpg,png"}
                };
                $("#updateForm").addKkoButtonValidRule(rules); //kakao_template.js
                if($.mdf.validForm("#updateForm") == false) {
                    return;
                }

                $("#buttons").val(convertKakaoButtonsToJson())
                $("#kakaoQuickReplies").val(convertKakaoButtonsToJson('quickReplies'))

                $("#updateForm").ajaxForm({
                    url : "/template/createAlimtalkTemplate.json",  // /template/brandtalkTemplateReg.do
                    type : "POST",
                    processData: false,
                    contentType: false,
                    dataType: "json",
                    success : function(result) {
                        if(result.code == "OK") {
                            alert("<spring:message code='template.alert.kakao.request.success'/>");  // 요청이 처리되었습니다.
                            window.location = '/template/altTemplateList.do';  // /template/alimtalkTemplate.do
                        } else {
                            alert(result.message);
                        }
                    },
                    error: function(request,status,error) {
                        console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                    }
                }).submit();

            } else {  // 등록방식이 파일인 경우
                var rules = {
                    fileName : {notBlank : true, extension : "xlsx,xls"}
                };

                if($.mdf.validForm("#updateForm", rules) == false) {
                    return;
                }

                var param = {
                    fileEx : $.mdf.getExtension($("input[name=fileName]").val()),
                    fileType : "A"
                };

                $.post("/template/validateFile.json", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        $("#updateForm").attr('action', '/template/alimtalkUpload.do').submit();
                    } else {
                        alert(result.message);
                    }
                });
            }
        });

        // 엑셀 다운로드 버튼 클릭
        $("#downloadBtn").on("click", function(event) {
            $("#updateForm").append("<input type='hidden' name='formDownload' value='Y'>");
            $("#updateForm").attr('action', '/template/alimtalkUpload.do').submit();
            $("#updateForm input[name=formDownload]").remove();
        });

        // (버튼) 추가 버튼 클릭
        $("#addBtn").on("click", function(event) {
            var rowCount = $('#kakao_button_table #kakaoButtonList.kakaoButtonList').length;
            if(rowCount > 4) {
                alert('<spring:message code="template.alert.kakao.button.exceed.limit" />');  // 버튼은 5개까지 만들 수 있습니다.
                return;
            }

            var linkType = $("#buttonLinkType").val();
            $('#kakao_button_table > tbody:last-child').append(makeKakaoButton(linkType));
        });

        // (바로연결) 추가 버튼 클릭
        $("#addReplyBtn").on("click", function(event) {
            var rowCount = $('#quick_reply_table #kakaoButtonList.kakaoButtonList').length;
            if(rowCount > 9) {
                alert('<spring:message code="template.alert.kakao.quickreplies.exceed.limit" />');  // 바로연결은 10개까지 만들 수 있습니다.
                return;
            }

            var linkType = $("#quickReplyType").val();
            $('#quick_reply_table > tbody:last-child').append(makeKakaoButton(linkType, 'quickReplies'));
        });

        // 카테고리 그룹
        $("select[name=categoryGroup]").on("change", function(event) {
            getCategoryCdList();
        });

        // 메시지 유형
        // 채널 추가 버튼은 '광고형'이나 '복합형'에서만 사용 가능
        $("select[name=kakaoTmplMsgType]").on("change", function(event) {
            var type = $("select[name=kakaoTmplMsgType]").val();
            if(type === 'BA') {
                $("tr[name=extraRow]").hide();
                $("tr[name=adRow]").hide();
                $("textarea[name=kakaoTmplEx]").val('');
                $("textarea[name=kakaoTmplAd]").val('');

                if ($("#buttonLinkType option[value='AC']").val() === 'AC') {
                    $("#buttonLinkType option[value='AC']").remove();
                }
            } else if(type === 'EX') {
                $("tr[name=extraRow]").show();
                $("tr[name=adRow]").hide();
                $("textarea[name=kakaoTmplAd]").val('');

                if ($("#buttonLinkType option[value='AC']").val() === 'AC') {
                    $("#buttonLinkType option[value='AC']").remove();
                }
            } else if(type === 'AD') {
                $("tr[name=extraRow]").hide();
                $("tr[name=adRow]").show();
                $("textarea[name=kakaoTmplEx]").val('');

                if ($("#buttonLinkType option[value='AC']").val() !== 'AC') {
                    $("#buttonLinkType").append('<option value="AC"><spring:message code="template.kakao.link.type.AC" /></option>');
                }
            } else if(type === 'MI') {
                $("tr[name=extraRow]").show();
                $("tr[name=adRow]").show();

                if ($("#buttonLinkType option[value='AC']").val() !== 'AC') {
                    $("#buttonLinkType").append('<option value="AC"><spring:message code="template.kakao.link.type.AC" /></option>');
                }
            }
        });

        // 강조 유형
        $("select[name=kakaoEmType]").on("change", function(event) {
            var type = $("select[name=kakaoEmType]").val();
            if(type === 'NONE') {
                $("tr[name=emphasizeRow]").hide();
                $('input[name=kakaoEmTitle]').val('');
                $('input[name=kakaoEmSubtitle]').val('');
                $("tr[name=kakaoImageRow]").hide();
            } else if(type === 'TEXT') {
                $("tr[name=emphasizeRow]").show();
                $("tr[name=kakaoImageRow]").hide();
            } else if(type === 'IMAGE') {
                $("tr[name=emphasizeRow]").hide();
                $("tr[name=kakaoImageRow]").show();
                $('input[name=kakaoEmTitle]').val('');
                $('input[name=kakaoEmSubtitle]').val('');
            }
        });
    }

    function initPage() {
        $("input:radio[name=uploadType][value=S]").prop("checked", true);  // 등록방식 선택 - 단건(S)
        $("input:radio[name=authType][value=U]").prop("checked", true);  // 공유유형 선택 - 사용자(U)
        $("input:radio[name=kakaoSecurityYn][value=N]").prop("checked", true);  // 보안 템플릿 설정 선택 - 일반 템플릿(N)
    }

</script>
</head>

<body>
<form:form id="updateForm" name="updateForm" commandName="command" method="post" enctype="multipart/form-data">
<input type="hidden" id='buttons' name="buttons" />
<input type="hidden" id='kakaoQuickReplies' name="kakaoQuickReplies" />
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="template.alimtalk.reg.title" /></h3><!-- 알림톡 템플릿 등록 -->
            </div>

            <div class="card-body">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="12%" />
                            <col width="*" />
                            <col width="13%" />
                            <col width="33%" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.upload.type" /></th><!-- 등록방식 -->
                            <td id="flexTd" colspan="3">
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="uploadType_S" name="uploadType" value="S" class="custom-control-input" />
                                    <label class="custom-control-label" for="uploadType_S"><spring:message code="template.upload.single" /></label><!-- 단건 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="uploadType_F" name="uploadType" value="F" class="custom-control-input" />
                                    <label class="custom-control-label" for="uploadType_F"><spring:message code="template.upload.file" /></label><!-- 파일 -->
                                </div>
                            </td>
                        </tr>
                        <!--  등록방식 : 파일 -->
                        <tr class="mass_div dp-none" >
                            <th><spring:message code="template.menu.filechoice"/></th><!-- 파일선택 -->
                            <td colspan="3" class="border-right-0">
                                <div class="form-row">
                                    <div class="col-6">
                                        <div class="custom-file custom-file-sm">
                                            <input type="file" class="custom-file-input custom-file-input-sm" id="fileName" name="fileName" >
                                            <label class="custom-file-label custom-file-label-sm mb-0" for="fileName"></label>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <button class="btn btn-sm btn-outline-primary btn-round ml-2" id="downloadBtn">
                                            <i class="fas fa-file-excel fa-lg"></i> <spring:message code="button.download.excel"/><!-- 엑셀 다운로드 -->
                                        </button>
                                    </div>
                                </div>
                            </td>
                        </tr><!-- //  등록방식 : 파일 -->
                        <!--  등록방식 : 단건 -->
                        <tr class="single_div">
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.column.yellowid" /></th><!-- 플러스친구ID -->
                            <td>
                                <select name="kakaoSenderKey" class="form-control form-control-sm w-75">
                                    <c:forEach var="kakaoProfile" items="${kakaoProfileList}" varStatus="status">
                                        <option value="${kakaoProfile.kakaoSenderKey}">${kakaoProfile.kakaoYellowId}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <th><em class="required">required</em><spring:message code="template.column.tmplcd" /></th><!-- 템플릿 코드 -->
                            <td><input type="text" class="form-control form-control-sm w-75" name="kakaoTmplCd" maxlength="20"/></td>
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.menu.mcname" /></th><!-- 템플릿명 -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm" name="contsNm"/></td>
                        </tr>
                        <tr>
                            <th><spring:message code="template.menu.mcdes"/></th><!-- 템플릿 설명 -->
                            <td colspan="3"><input type="text" class="form-control form-control-sm" name="contsDesc"/></td>
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                            <td colspan="3">
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
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.menu.category"/></th><!-- 카테고리 -->
                            <td>
                                <div class="custom-control-inline">
                                    <select class="form-control form-control-sm" name="categoryGroup" id="categoryGroup">
                                        <option value=""><spring:message code="template.menu.category.group"/></option>
                                        <c:forEach var="map" items="${alimTalkTemplateCategoryGroupList}">
                                            <option value="${map.cd}">${map.val}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="custom-control-inline">
                                    <select class="form-control form-control-sm" name="categoryCd">
                                        <option><spring:message code="template.menu.category"/></option>
                                    </select>
                                </div>
                            </td>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.secure.template"/></th><!-- 보안 템플릿 설정 -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="kakaoSecurityYn" id="kakaoSecurityYn_N" value="N" class="custom-control-input" />
                                    <label class="custom-control-label" for="kakaoSecurityYn_N"><spring:message code="template.menu.general.template"/></label><!-- 일반 템플릿 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="kakaoSecurityYn" id="kakaoSecurityYn_Y" value="Y" class="custom-control-input" />
                                    <label class="custom-control-label" for="kakaoSecurityYn_Y"><spring:message code="template.menu.secure.template"/></label><!-- 보안 템플릿 -->
                                </div>
                            </td>
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.menu.message.type"/></th><!-- 메시지 유형 -->
                            <td>
                                <select class="form-control form-control-sm w-auto" name="kakaoTmplMsgType">
                                    <option value="BA"><spring:message code="template.menu.basic.template"/></option>
                                    <option value="EX"><spring:message code="template.menu.extra.template"/></option>
                                    <option value="AD"><spring:message code="template.menu.ad.template"/></option>
                                    <option value="MI"><spring:message code="template.menu.mix.template"/></option>
                                </select>
                            </td>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.emphasize.type"/></th><!-- 강조 유형 -->
                            <td>
                                <select class="form-control form-control-sm w-auto" name="kakaoEmType">
                                    <option value="NONE"><spring:message code="template.menu.emphasize.none"/></option><!-- 선택 안함 -->
                                    <option value="TEXT"><spring:message code="template.menu.emphasize.text"/></option><!-- 강조표기 -->
                                    <option value="IMAGE"><spring:message code="template.menu.emphasize.image"/></option><!-- 이미지 -->
                                </select>
                            </td>
                        </tr>
                        <tr class="single_div dp-none" name="kakaoImageRow">
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.emphasize.image"/></th><!-- 이미지 -->
                            <td colspan="3">
                               <div class="custom-file w-50">
                                  <input type="file" class="custom-file-input ustom-file-input-sm" name="image" id="image">
                                  <label class="custom-file-label custom-file-label-sm" for="image"></label>
                               </div>
                        </td>
                        </tr>
                        <tr class="single_div dp-none" name="emphasizeRow">
                            <th><em class="required">required</em><spring:message code="template.menu.emphasize.title"/></th><!-- 강조 문구 -->
                            <td>
                                <input type="text" class="form-control form-control-sm w-75" name="kakaoEmTitle" maxlength="50"/>
                            </td>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.emphasize.subtitle"/></th><!-- 강조 보조문구 -->
                            <td>
                                <input type="text" class="form-control form-control-sm w-75" name="kakaoEmSubtitle" maxlength="50"/>
                            </td>
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.menu.mcontent" /></th><!-- 템플릿 내용 -->
                            <td colspan="3">
                                <div class="form-row">
                                    <div class="col-3">
                                        <textarea class="form-control textarea-mobile" id="content" name="content"></textarea>
                                    </div>
                                    <div class="col-9">
                                        <ul class="notice-text pl-3 mb-0">
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.help1" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.help2" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.help3" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.help4" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.help5" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.help6" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.help7" /></li>
                                    </ul>
                                </div>
                                </div>
                            </td>
                        </tr>
                        <tr class="single_div dp-none" name="extraRow">
                            <th><em class="required">required</em><spring:message code="template.menu.extra.content" /></th><!-- 부가 정보 -->
                            <td colspan="3">
                                <div class="form-row">
                                    <div class="col-3">
                                        <textarea class="form-control textarea" id="kakaoTmplEx" name="kakaoTmplEx" style="height:150px; background: #f7fafc;"></textarea>
                                    </div>
                                    <div class="col-9">
                                        <ul class="notice-text pl-3 mb-0">
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.extra.help1" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.extra.help2" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.extra.help3" /></li>
                                    </ul>
                                </div>
                                </div>
                            </td>
                        </tr>
                        <tr class="single_div dp-none" name="adRow">
                            <th><em class="required">required</em><spring:message code="template.menu.ad.content" /></th><!-- 광고성 메시지 -->
                            <td colspan="3">
                                <div class="form-row">
                                    <div class="col-3">
                                        <textarea class="form-control textarea" id="kakaoTmplAd" name="kakaoTmplAd" style="height:150px; background: #f7fafc;"></textarea>
                                    </div>
                                    <div class="col-9">
                                        <ul class="notice-text pl-3 mb-0">
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.ad.help1" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.ad.help2" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.ad.help3" /></li>
                                        <li><em class="li-circle">li-circle</em><spring:message code="template.alimtalk.reg.ad.help4" /></li>
                                    </ul>
                                </div>
                                </div>
                            </td>
                        </tr>
                        <tr class="single_div">
                            <th><spring:message code='template.kakao.button' /></th><!-- 버튼 -->
                            <td colspan="3">
                                <select class="form-control form-control-sm d-inline-block w-auto" id="buttonLinkType">
                                    <option value="WL"><spring:message code='template.kakao.link.type.WL' /></option><!-- 웹 링크 -->
                                    <option value="AL"><spring:message code='template.kakao.link.type.AL' /></option><!-- 앱 링크 -->
                                    <option value="DS"><spring:message code='template.kakao.link.type.DS' /></option><!-- 배송조회 -->
                                    <option value="BK"><spring:message code='template.kakao.link.type.BK' /></option><!-- 봇키워드 -->
                                    <option value="MD"><spring:message code='template.kakao.link.type.MD' /></option><!-- 메시지 전달 -->
                                    <option value="BT"><spring:message code='template.kakao.link.type.BT' /></option><!-- 봇전환 -->
                                    <option value="BC"><spring:message code='template.kakao.link.type.BC' /></option><!-- 상담톡전환 -->
                                </select>
                                <button type="button" id="addBtn" class="btn btn-sm btn-outline-primary ml-2">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                            </td>
                        </tr>
                        <tr class="single_div">
                            <td colspan="4" class="p-0">
                                <table id="kakao_button_table" class="table table-sm dataTable table-fixed" style="margin: -1px !important; border-bottom-style: none;">
                                    <colgroup>
                                        <col width="12%" />
                                        <col width="*" />
                                        <col width="66" /><!-- 버튼명 -->
                                        <col width="*" />
                                        <col width="116" /><!-- 모바일 웹링크/Android 앱링크 -->
                                        <col width="18%" />
                                        <col width="89" /><!-- PC 웹링크/iOS 앱링크 -->
                                        <col width="18%" />
                                    </colgroup>
                                    <tbody>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr class="single_div">
                            <th><spring:message code='template.kakao.quick.replies' /></th><!-- 버튼 -->
                            <td colspan="3">
                                <select class="form-control form-control-sm d-inline-block w-auto" id="quickReplyType">
                                    <option value="WL"><spring:message code='template.kakao.link.type.WL' /></option><!-- 웹 링크 -->
                                    <option value="AL"><spring:message code='template.kakao.link.type.AL' /></option><!-- 앱 링크 -->
                                    <option value="DS"><spring:message code='template.kakao.link.type.DS' /></option><!-- 배송조회 -->
                                    <option value="BK"><spring:message code='template.kakao.link.type.BK' /></option><!-- 봇키워드 -->
                                    <option value="MD"><spring:message code='template.kakao.link.type.MD' /></option><!-- 메시지 전달 -->
                                </select>
                                <button type="button" id="addReplyBtn" class="btn btn-sm btn-outline-primary ml-2">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                            </td>
                        </tr>
                        <tr class="single_div">
                            <td colspan="4" class="p-0">
                                <table id="quick_reply_table" class="table table-sm dataTable table-fixed" style="margin: -1px !important; border-bottom-style: none;">
                                    <colgroup>
                                        <col width="12%" />
                                        <col width="*" />
                                        <col width="66" /><!-- 버튼명 -->
                                        <col width="*" />
                                        <col width="116" /><!-- 모바일 웹링크/Android 앱링크 -->
                                        <col width="18%" />
                                        <col width="89" /><!-- PC 웹링크/iOS 앱링크 -->
                                        <col width="18%" />
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
            </div>
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form:form>
<!-- 카카오 버튼-->
<%@ include file="/jsp/include/makeKkoButton_inc.jsp" %>
</body>
</html>