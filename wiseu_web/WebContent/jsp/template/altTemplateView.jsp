<%-------------------------------------------------------------------------------------------------
 * - [템플릿>알림톡 템플릿 리스트] 알림톡 템플릿 내용조회 <br/>
 * - URL : /template/altTemplateView.do<br/>
 * - Controller : com.mnwise.wiseu.web.template.web.AlimtalkTemplateListController <br/>
 * - 이전 파일명 : alimtalk_template_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.alimtalk.modify"/></title><!-- 알림톡 템플릿 수정 -->
<c:if test="${'Y' eq popupYn}"><!--  에디터 상세보기 일 경우 JS 추가-->
<%@ include file="/jsp/include/plugin.jsp" %>
</c:if>
<script src="/js/template/kakao_template.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {

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
            $('#quick_reply_table > tbody:last-child').append(makeKakaoButton(linkType , 'quickReplies'));
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

        // (문의) 등록 버튼 클릭
        $("#registBtn").on("click", function(event) {
            var comment = $('#comment').val();
            if(comment === '') {
                alert('<spring:message code="template.alert.comment.nodata"/>');  // 댓글을 입력하세요.
                return;
            }

            if(confirm('<spring:message code="template.confirm.save"/>')) {  // 저장하시겠습니까?
                $("#editorFrm").attr('action', '/template/altCommentUpload.do').attr('target', 'commnetIFrame').submit();  // /template/alimtalkCommentUpload.do
            }
        });

        // (문의) 파일추가 버튼 클릭
        $("#fileAddBtn").on("click", function(event) {
            event.preventDefault();
            if($("input[name*=attachment_]").length>=5) {
                alert('<spring:message code="template.alert.kakao.attach.exceed.limit"/>');  // 파일은 최대 5개까지 첨부 가능합니다.
                return;
            }
            addFileInputForm();
        })

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if("R" !== "${mobileVo.kakaoTmplStatus}" || "APR" === "${mobileVo.kakaoInspStatus}") {
                alert('<spring:message code="template.alert.delete.unable"/>');  // 템플릿 상태가 대기이고 템플릿 검수 상태가 승인이 아닌 경우에만 삭제할 수 있습니다.
                return;
            }
            if(confirm('<spring:message code="template.alert.msg.2"/>')) {  // 삭제하시겠습니까?
                callAjax("/template/deleteAlimtalkTemplate.json", '${mobileVo.contsNo}', "N");
            }
        });

        // 검수요청 버튼 클릭
        $("#reqBtn").on("click", function(event) {
            if(confirm('<spring:message code="template.alert.request.specific"/>')) {  // 선택한 템플릿을 검수요청하시겠습니까?
                callAjax("/template/inspectAlimtalkTemplate.json", $("input[name=contsNo]").val(), "Y");
            }
        });

        // 검수요청취소 버튼 클릭
        $("#cancelReqBtn").on("click", function(event) {
            if(confirm('<spring:message code="template.alert.cancel.request.specific"/>')) {  // 선택한 템플릿을 검수요청 취소 하시겠습니까?
                callAjax("/template/cancleInspectAlimtalkTemplate.json", $("input[name=contsNo]").val(), "Y");
            }
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if("R" !== "${mobileVo.kakaoTmplStatus}" || ("REG" !== "${mobileVo.kakaoInspStatus}" && "REJ" !== "${mobileVo.kakaoInspStatus}")) {
                alert('<spring:message code="template.alert.modify.unable"/>');  // 템플릿 설명, 카테고리, 공유 유형, 사용 여부 \\n정보만 수정이 가능합니다. \\n템플릿 상태가 대기고 템플릿 검수 상태가 등록 또는 \\n반려인 경우에만 템플릿 정보를 수정 할 수 있습니다.
            }
            if(confirm('<spring:message code="template.confirm.save"/>')) {  // 저장하시겠습니까?
                $("#buttons").val(convertKakaoButtonsToJson())
                $("#kakaoQuickReplies").val(convertKakaoButtonsToJson('quickReplies'))
                var type = $("select[name=kakaoEmType]").val();
                if(type !== 'IMAGE'){
                    $("#filePath").val("");
                    $("#filePreviewPath").val("");
                    $("#fileNm").val("");
                }
                $("#editorFrm").ajaxForm({
                    url : "/template/updateAlimtalkTemplate.json",  // /template/brandtalkTemplateReg.do
                    type : "POST",
                    processData: false,
                    dataType: "json",
                    success : function(result) {
                         if(result.code == "OK") {
                               alert('<spring:message code="template.alert.kakao.request.success"/>');  // 요청이 처리되었습니다.
                               location.reload();
                          } else {
                             alert(result.message);
                          }
                    },
                    error: function(request,status,error) {
                        console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                    }
                }).submit();
            }
        });
    }

    var modifyPossible = "Y";
    function initPage() {
        var kakaoInspStatus = "${mobileVo.kakaoInspStatus}";
        if(kakaoInspStatus === 'REQ' || kakaoInspStatus === 'APR') {
            modifyPossible = "N";
            $("input[name='kakaoTmplCd']").attr('disabled', true);
            $("input[name='contsNm']").attr('disabled', true);
            $("input[name='kakaoEmTitle']").attr('disabled', true);
            $("input[name='kakaoEmSubtitle']").attr('disabled', true);
            $("input[name='kakaoSecurityYn']").attr('disabled', true);
            $("select[name='kakaoTmplMsgType']").attr('disabled', true);
            $("select[name='kakaoEmType']").attr('disabled', true);
            $("select[name='categoryGroup']").attr('disabled', true);
            $("select[name='categoryCd']").attr('disabled', true);
            $("textarea[name='content']").attr('disabled', true);
            $("textarea[name='kakaoTmplEx']").attr('disabled', true);
            $("textarea[name='kakaoTmplAd']").attr('disabled', true);
            $("#addBtn").attr('disabled', true);
            $("#addReplyBtn").attr('disabled', true);
            $("#image").attr('disabled', true);
        }

        var kakaoEmType = "${mobileVo.kakaoEmType}";
        if (kakaoEmType === 'TEXT') {
            $("tr[name=emphasizeRow]").removeClass("dp-none");
        }else if (kakaoEmType === 'IMAGE') {
            $("tr[name=kakaoImageRow]").removeClass("dp-none");
        }

        var kakaoTmplMsgType = "${mobileVo.kakaoTmplMsgType}";
        if (kakaoTmplMsgType === 'EX' || kakaoTmplMsgType === 'MI') {
            $("tr[name=extraRow]").removeClass("dp-none");
            if ($("#buttonLinkType option[value='AC']").val() !== 'AC') {
                $("#buttonLinkType").append('<option value="AC"><spring:message code="template.kakao.link.type.AC" /></option>');
            }
        }
        if (kakaoTmplMsgType === 'AD' || kakaoTmplMsgType === 'MI') {
            $("tr[name=adRow]").removeClass("dp-none");
            if ($("#buttonLinkType option[value='AC']").val() !== 'AC') {
                $("#buttonLinkType").append('<option value="AC"><spring:message code="template.kakao.link.type.AC" /></option>');
            }
        }

        //popup일 경우 버튼 display None 처리
        if('Y'=='${popupYn}'){
            $(':button').hide();
            $(".popupHide").hide();
        }
    }

    var fileInputCount = 0;
    function deleteFileInputForm(fileInputCount) {
        $("#div" + fileInputCount).remove();
    }

    function addFileInputForm() {
        var str = '<div class="form-row" id="div'+ ++fileInputCount +'">'
                + '  <div class="col-6">'
                + '    <div class="custom-file">'
                + '      <input type="file" class="custom-file-input" id="customFile_'+ fileInputCount + '" lang="ko" name="file_'+ fileInputCount +'">'
                + '      <label class="custom-file-label custom-file-label-sm" for="customFile_'+ fileInputCount +'"></label>'
                + '    </div>'
                + '  </div>'
                + '  <div class="col-6">'
                + '    <button class="btn btn-sm btn-outline-primary ml-2" name="fileDelete" type="button" onclick="deleteFileInputForm('+ fileInputCount +');">'
                + '      <spring:message code="button.delete"/>'
                + '    </button>'
                + '  </div>'
                + '</div>';

        $("#fileDiv").append(str);

        $("a[name='fileDelete']").on("click", function(event) {
            event.preventDefault();
            deleteFileInputForm($(this));
        });
        $('html, body').stop().animate( { scrollTop : '+=100' } );
    }

    function callAjax(url, contsNo, isModify) {
        var param = {
            contsNoArr : [contsNo]
        };

        $.post(url, $.param(param, true), function(result) {
            if(result.code == "OK") {
                alert('<spring:message code="template.alert.kakao.request.success"/>');  // 요청이 처리되었습니다.
                if('Y' == isModify) {
                    location.reload();
                } else {
                    window.location = '/template/altTemplateList.do';  // /template/alimtalkTemplate.do
                }
            } else {
                alert(result.message);
            }
        });
    }

    function submitCallback(result) {
        if (result.code == "OK") {
            alert('<spring:message code="template.alert.kakao.request.success"/>');  // 요청이 처리되었습니다.
            location.reload();
        } else {
            alert(result.message);
        }
    }
</script>
</head>

<body>
<form:form id="editorFrm" name="editorFrm" commandName="command" method="post" enctype="multipart/form-data">
<input type=hidden name="uploadType" value="U">
<input type=hidden name="contsNo" value="${mobileVo.contsNo}">
<input type=hidden name="cmd" />
<input type="hidden" id='buttons' name="buttons" />
<input type="hidden" id='kakaoQuickReplies' name="kakaoQuickReplies" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header popupHide">
                <h3 class="mb-0"><spring:message code="template.alimtalk.modify"/></h3><!-- 알림톡 템플릿 수정 -->
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
                            <th><spring:message code="template.column.yellowid" /></th><!-- 플러스친구ID -->
                            <td class="control-text-sm">${mobileVo.kakaoYellowId}</td>
                            <th><em class="required">required</em><spring:message code="template.column.tmplcd" /></th><!-- 템플릿 코드 -->
                            <td><input type="text" id="kakaoTmplCd" name="kakaoTmplCd" class="form-control form-control-sm w-50" value="${mobileVo.kakaoTmplCd}" /></td>
                        </tr>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.menu.mcname" /></th><!-- 템플릿명 -->
                            <td colspan="3"><input type="text" name="contsNm" class="form-control form-control-sm" value="${mobileVo.contsNm}" /></td>
                        </tr>
                        <tr>
                            <th><spring:message code="template.menu.mcdes"/></th><!-- 템플릿 설명 -->
                            <td colspan="3"><input type="text" name="contsDesc" class="form-control form-control-sm" value="${mobileVo.contsDesc}"/></td>
                        </tr>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.table.share" /></th><!-- 공유유형 -->
                            <td colspan="3">
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_U" value="U" class="custom-control-input" <c:if test="${mobileVo.authType eq 'U'}">checked</c:if> />
                                    <label class="custom-control-label" for="authType_U"><spring:message code="template.type.user"/></label><!-- 사용자 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                   <input type="radio" name="authType" id="authType_G" value="G" class="custom-control-input" <c:if test="${mobileVo.authType eq 'G'}">checked</c:if> />
                                   <label class="custom-control-label" for="authType_G"><spring:message code="template.type.group"/></label><!-- 부서 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                   <input type="radio" name="authType" id="authType_A" value="A" class="custom-control-input" <c:if test="${mobileVo.authType eq 'A'}">checked</c:if>/>
                                   <label class="custom-control-label" for="authType_A"><spring:message code="template.type.all"/></label><!-- 전체 -->
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th><em class="required">required</em><spring:message code="template.use.yn"/></th><!-- 사용여부 -->
                            <td colspan="3">
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="useYn" id="useYn_Y" value="Y" <c:if test="${mobileVo.useYn eq 'Y'}">checked</c:if> class="custom-control-input" />
                                    <label class="custom-control-label" for="useYn_Y"><spring:message code="template.use.y"/></label><!-- 사용 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="useYn" id="useYn_N" value="N" <c:if test="${mobileVo.useYn eq 'N'}">checked</c:if> class="custom-control-input" />
                                    <label class="custom-control-label" for="useYn_N"><spring:message code="template.use.n"/></label><!-- 미사용 -->
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th><spring:message code='template.column.kakaotmplstatus' /></th><!-- 템플릿 상태 -->
                            <td class="control-text-sm">${mobileVo.kakaoTmplStatusNm}</td>
                            <th><spring:message code='template.column.kakaoinspstatus' /></th><!-- 검수 상태  -->
                            <td class="control-text-sm">${mobileVo.kakaoInspStatusNm}</td>
                        </tr>
                        <tr>
                            <th><spring:message code="template.content.composer" /></th><!-- 작성자 -->
                            <td class="control-text-sm">${mobileVo.userNm}</td>
                            <th><spring:message code='template.content.created' /></th><!-- 작성 일자 -->
                            <td class="control-text-sm">${mobileVo.createDt} ${mobileVo.createTm}</td>
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.menu.category"/></th><!-- 카테고리 -->
                            <td>
                                <div class="custom-control-inline">
                                    <select class="form-control form-control-sm" name="categoryGroup" id="categoryGroup">
                                        <option value=""><spring:message code="template.menu.category.group"/></option>
                                        <c:forEach var="map" items="${alimTalkTemplateCategoryGroupList}">
                                            <option value="${map.cd}" ${mobileVo.categoryGroup eq map.cd ? "selected" : ""}>${map.val}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="custom-control-inline">
                                    <select class="form-control form-control-sm" name="categoryCd">
                                        <option><spring:message code="template.menu.category"/></option>
                                        <c:forEach var="map" items="${alimTalkTemplateCategoryCdList}">
                                            <option value="${map.cd}" ${mobileVo.categoryCd eq map.cd ? "selected" : ""}>${map.val}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </td>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.secure.template"/></th><!-- 보안 템플릿 설정 -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="kakaoSecurityYn" id="kakaoSecurityYn_N" value="N" class="custom-control-input" <c:if test="${mobileVo.kakaoSecurityYn eq 'N'}">checked</c:if> />
                                    <label class="custom-control-label" for="kakaoSecurityYn_N"><spring:message code="template.menu.general.template"/></label><!-- 일반 템플릿 -->
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="kakaoSecurityYn" id="kakaoSecurityYn_Y" value="Y" class="custom-control-input" <c:if test="${mobileVo.kakaoSecurityYn eq 'Y'}">checked</c:if> />
                                    <label class="custom-control-label" for="kakaoSecurityYn_Y"><spring:message code="template.menu.secure.template"/></label><!-- 보안 템플릿 -->
                                </div>
                            </td>
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.menu.message.type"/></th><!-- 메시지 유형 -->
                            <td>
                                <select class="form-control form-control-sm w-auto" name="kakaoTmplMsgType">
                                    <option value="BA" ${mobileVo.kakaoTmplMsgType eq 'BA' ? "selected" : ""}><spring:message code="template.menu.basic.template"/></option>
                                    <option value="EX" ${mobileVo.kakaoTmplMsgType eq 'EX' ? "selected" : ""}><spring:message code="template.menu.extra.template"/></option>
                                    <option value="AD" ${mobileVo.kakaoTmplMsgType eq 'AD' ? "selected" : ""}><spring:message code="template.menu.ad.template"/></option>
                                    <option value="MI" ${mobileVo.kakaoTmplMsgType eq 'MI' ? "selected" : ""}><spring:message code="template.menu.mix.template"/></option>
                                </select>
                            </td>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.emphasize.type"/></th><!-- 강조 유형 -->
                            <td>
                                <select class="form-control form-control-sm w-auto" name="kakaoEmType">
                                    <option value="NONE" ${mobileVo.kakaoEmType eq 'NONE' ? "selected" : ""}><spring:message code="template.menu.emphasize.none"/></option><!-- 선택 안함 -->
                                    <option value="TEXT" ${mobileVo.kakaoEmType eq 'TEXT' ? "selected" : ""}><spring:message code="template.menu.emphasize.text"/></option><!-- 강조표기 -->
                                    <option value="IMAGE" ${mobileVo.kakaoEmType eq 'IMAGE' ? "selected" : ""}><spring:message code="template.menu.emphasize.image"/></option><!-- 이미지 -->
                                </select>
                            </td>
                        </tr>
                        <tr class="single_div dp-none" name="kakaoImageRow">
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.emphasize.image"/></th><!-- 이미지 -->
                            <td colspan="3">
                                <div class="brandtalk-modify-sms-img brandtalk-img">
                                    <div class="imgwrap">
                                        <div class="image-area"><img src="${mobileVo.filePreviewPath}"/></div>
                                        <input type="hidden" id="filePath" name="filePath" value="${mobileVo.filePath}"/>
                                        <input type="hidden" id="filePreviewPath" name="filePreviewPath" value="${mobileVo.filePreviewPath}"/>
                                        <input type="hidden" id="fileNm" name="fileNm" value="${mobileVo.filePreviewName}"/>
                                    </div>
                                </div>
                               <div class="custom-file w-50 popupHide">
                                  <input type="file" class="custom-file-input ustom-file-input-sm" name="image" id="image">
                                  <label class="custom-file-label custom-file-label-sm" for="image"></label>
                               </div>
                        </td>
                        </tr>
                        <tr class="single_div dp-none" name="emphasizeRow">
                            <th><em class="required">required</em><spring:message code="template.menu.emphasize.title"/></th><!-- 강조 문구 -->
                            <td>
                                <input type="text" class="form-control form-control-sm w-75" name="kakaoEmTitle" maxlength="50" value="${mobileVo.kakaoEmTitle}" />
                            </td>
                            <th class="ls--1px"><em class="required">required</em><spring:message code="template.menu.emphasize.subtitle"/></th><!-- 강조 보조문구 -->
                            <td>
                                <input type="text" class="form-control form-control-sm w-75" name="kakaoEmSubtitle" maxlength="50" value="${mobileVo.kakaoEmSubtitle}" />
                            </td>
                        </tr>
                        <tr class="single_div">
                            <th><em class="required">required</em><spring:message code="template.menu.mcontent" /></th><!-- 템플릿 내용 -->
                            <td colspan="3">
                                <div class="form-row">
                                    <div class="col-3">
                                        <textarea class="form-control textarea-mobile" id="content" name="content">${mobileVo.contsTxt}</textarea>
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
                        <tr class="single_div dp-none" name="extraRow" id="extraRow">
                            <th><em class="required">required</em><spring:message code="template.menu.extra.content" /></th><!-- 부가 정보 -->
                            <td colspan="3">
                                <div class="form-row">
                                    <div class="col-3">
                                        <textarea class="form-control textarea" id="kakaoTmplEx" name="kakaoTmplEx" style="height:150px; background: #f7fafc;">${mobileVo.kakaoTmplEx}</textarea>
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
                        <tr class="single_div dp-none" name="adRow" id="adRow">
                            <th><em class="required">required</em><spring:message code="template.menu.ad.content" /></th><!-- 광고성 메시지 -->
                            <td colspan="3">
                                <div class="form-row">
                                    <div class="col-3">
                                        <textarea class="form-control textarea" id="kakaoTmplAd" name="kakaoTmplAd" style="height:150px; background: #f7fafc;">${mobileVo.kakaoTmplAd}</textarea>
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
                        <tr class="popupHide">
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
                        <tr>
                            <td colspan="4" class="p-0"><!-- 카카오 버튼-->
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
                                    <c:set var="kakaoButtonList" value="${wiseu:convertJsonToKakaoButtonList(mobileVo.kakaoButtons)}"></c:set>
                                    <%@ include file="/jsp/include/kkoButton_inc.jsp" %>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr class="single_div popupHide">
                            <th><spring:message code='template.kakao.quick.replies' /></th><!-- 바로연결 -->
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
                                    <c:set var="kakaoButtonList" value="${wiseu:convertJsonToKakaoButtonList(mobileVo.kakaoQuickReplies)}"></c:set>
                                    <%@ include file="/jsp/include/kkoButton_inc.jsp" %>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="10%">
                            <col width="10%">
                            <col width="10%">
                            <col width="50%">
                            <col width="*">
                        </colgroup>
                        <thead class="thead-light">
                        <tr>
                            <th scope="col"><spring:message code='template.content.composer' /></th><!-- 작성자 -->
                            <th scope="col"><spring:message code='template.column.submittime' /></th><!-- 등록 시간 -->
                            <th scope="col"><spring:message code='template.column.status' /></th><!-- 상태 -->
                            <th scope="col"><spring:message code='template.column.tmplresult' /></th><!-- 결과 및 요청사항 -->
                            <th scope="col"><spring:message code='template.column.attachment' /></th><!-- 첨부파일 -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty mobileVo.kakaoTemplateComments}">
                        <tr>
                            <td colspan="5">
                                <spring:message code='template.msg.alimtalk.comment.nodata' /><!-- 등록된 댓글이 없습니다. -->
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${!empty mobileVo.kakaoTemplateComments}">
                        <c:forEach var="comment" items="${mobileVo.kakaoTemplateComments}" varStatus="status">
                        <tr>
                            <td>${comment.userName}</td>
                            <td>${comment.createdAt}</td>
                            <td>${comment.status}</td>
                            <td class="text-left">${comment.content}</td>
                            <td>
                                <c:forEach items="${comment.attachment}" varStatus="row" var="var" >
                                   <p><a href="${var.filePath}" target="_blank">${var.originalFileName }</a></p>
                                </c:forEach>
                            </td>
                        </tr>
                        </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <c:if test="${'REG' ne mobileVo.kakaoInspStatus && 'N' eq popupYn}">
                <div class="table-responsive gridWrap overflow-x-hidden"><!-- 문의하기 -->
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="10%">
                            <col width="*">
                        </colgroup>
                        <tr>
                            <th><spring:message code='template.column.inquire' /></th><!-- 문의 -->
                            <td colspan="2">
                                <div class="form-row">
                                    <div class="col-10">
                                        <textarea id="comment" name="comment" class="form-control textarea "></textarea>
                                    </div>
                                    <div class="col-2 pt-3">
                                        <button class="btn btn-sm btn-outline-primary" type="button" id="registBtn">
                                            <spring:message code="button.regist"/><!-- 등록 -->
                                        </button>
                                    </div>
                                </div>
                                <div class="form-row mt-2">
                                    <div class="col-1 pt-1">
                                    <button class="btn btn-sm btn-outline-primary" type="button" id="fileAddBtn">
                                        <spring:message code="button.add.file"/><!-- 파일추가 -->
                                    </button>
                                </div>
                                    <div class="col-11 ml--5">
                                <ul class="notice-text text-left">
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.alert.kakao.attach.exceed.limit"/> <spring:message code="template.alert.kakao.attach.size"/></li>
                                    <!-- 파일은 최대 5개까지 첨부 가능합니다. (파일당 최대 사이즈: 50MB) 버튼을 눌러 추가하세요. -->
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.alert.kakao.attach.ext"/></li>
                                    <!-- png, jpg, jpeg, gif, pdf, hwp, doc, docx 파일만 첨부할 수 있습니다. -->
                                </ul>
                                    </div>
                                </div>
                                <div id="fileDiv"></div>
                                <iframe id="commnetIFrame" name="commnetIFrame" style="display:none;visibility:hidden;"></iframe>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                </c:if>
            </div><!-- //card body -->

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <div class="row">
                    <div class="col col-12">
                        <button id="deleteBtn" type="button" class="btn btn-outline-primary">
                            <spring:message code="button.delete"/><!-- 삭제 -->
                        </button>
                        <button id="reqBtn" class="btn btn-outline-primary">
                            <spring:message code="button.this.verify.template" /><!-- 템플릿 검수 요청 -->
                        </button>
                        <button id="cancelReqBtn" class="btn btn-outline-primary">
                            <spring:message code="button.this.cancel.verify.template" /><!-- 템플릿 검수 요청 취소 -->
                        </button>
                        <button id="saveBtn" type="button" class="btn btn-outline-primary">
                            <spring:message code="button.save"/><!-- 저장 -->
                        </button>
                    </div>
                </div>
            </div><!-- e.button area -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form:form>
<%@ include file="/jsp/include/makeKkoButton_inc.jsp" %>
</body>
</html>