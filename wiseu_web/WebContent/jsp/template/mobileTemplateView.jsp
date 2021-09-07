<%-------------------------------------------------------------------------------------------------
  * - [템플릿>모바일 템플릿 리스트] 모바일 템플릿 내용조회
  * - URL : /template/mobileTemplateView.do
  * - Controller : com.mnwise.wiseu.web.template.web.MobileTemplateListController
  * - 이전 파일명 : mobile_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.mobiletemplate.content.msg"/></title><!-- 모바일템플릿 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });


    /**
     * Byte 길이 구하기
     * @param template
     */
    function getByte(template) {
        var i;
        var msglen=0;
        for(i = 0; i < template.length; i++) {
            var ch = template.charAt(i);
            if(escape(ch).length > 4) {
                msglen += 2;
            } else {
                msglen++;
            }
        }
        return msglen;
    }

    function initEventBind() {
        $("textarea[name=txta_conts]").keyup(function() {
            var contsLen = getByte(this.value);
            if("D" == "${mobileVo.fileType}" && 80 < contsLen) {
                alert("<spring:message code='template.alert.msg.reg.8' arguments='80'/>");  // 템플릿은 {0} byte를 초과할 수 없습니다.
                this.value = $.mdf.cutByte(this.value, 80);
            } else if("T" == "${mobileVo.fileType}" && 2000 < contsLen) {
                alert("<spring:message code='template.alert.msg.reg.8' arguments='2000'/>");  // 템플릿은 {0} byte를 초과할 수 없습니다.
                this.value = $.mdf.cutByte(this.value, 2000);
            }
            $("span#contsTxtLen").html(getByte(this.value));
        });

        // 삭제 버튼 클릭
        $("#deleteBtn").on("click", function(event) {
            if(confirm('<spring:message code="template.alert.msg.2"/>')) {  // 삭제하시겠습니까?
                document.location.href = "/template/deleteMobileTemplate.do?contsNo=${mobileVo.contsNo}";
            }
        });

        // 파일올리기 버튼 클릭
        $("#fileUploadBtn").on("click", function(event) {
            $("#updateForm").attr('action', '/template/ftrImgUpload.do').attr('target', 'uploadIFrame').submit();  // /template/friendtalkImageUpload.do
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                contsNm                : {notBlank : true, maxbyte : 50},
                fileName               : {extension : "jpg"},  // MMS 이미지
                fileName2              : {extension : "jpg,png"},  // 친구톡 이미지
                fileName3              : {extension : "jpg"},  // 친구톡 발송 실패시 발송할 MMS 이미지
                friendtalkImageUrlLink : {notBlank : true, url : true},
                contsDesc              : {maxbyte : 100},
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var fileType = "${mobileVo.fileType}";
            var fileNm = "";
            if(fileType=="I") {
                fileNm = $("#updateForm input[name=fileName]").val();
            } else if(fileType=="C") {
                fileNm = $("#updateForm input[name=fileName2]").val();
            }

            if(fileNm != "") {
                if(fileType=="C") {
                    if($("#updateForm input[name=friendtalkImageUrl]").val() == "${mobileVo.imageUrl}") {
                        alert('<spring:message code="template.alert.msg.modify.1"/>');  // 친구톡 이미지는 이미지 업로드 후 수정 가능합니다.
                        return;
                    }
                }

                var fileEx = (fileType=="I" || fileType=="C") ? $.mdf.getExtension(fileNm) : "";
                var param = {
                    fileEx : fileEx,
                    fileType : fileType
                };

                $.ajax({
                    url : "/template/validateFile.json",
                    data : param,
                    type : "post",
                    async: false,
                    success : function(result) {
                        if(result.code == "OK") {
                            updateMobileTemplate();
                        } else {
                            alert(result.message);
                        }
                    }
                });
            } else {
                updateMobileTemplate();
            }
        });
    }

    function initPage() {
        //text area 의 크기를 출력
        $("textarea[name='txta_conts']").each(function(obj) {
            $("#contsTxtLen").html($.mdf.getByteLength($(this).val()));
        });
    }

    function updateMobileTemplate() {
        $("#updateForm").attr('action', '/template/updateMobileTemplate.do').attr('target', '_self').submit();
    }

    function uploadImageResult(code, message, url, filepath, filename) {
        if(code == "200") {
            $("#friendtalkImageUrl").val(url);
            alert("<spring:message code='template.alert.kakao.request.success'/>");  // 요청이 처리되었습니다.
        } else {
            alert(message);
        }
    }
</script>
</head>
<body>
<form:form id="updateForm" name="updateForm" commandName="command" method="post" enctype="multipart/form-data">
<input type="hidden" name="contsNo" value="${mobileVo.contsNo}">
<input type="hidden" name="mobile_type" value="${mobileVo.fileType}">
<input type="hidden" name="isModify" value="Y">
<input type="hidden" name="cmd" value="update">

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="template.mobiletemplate.content.msg"/></h3><!-- 모바일템플릿 -->
            </div>

            <div class="card-body gridWrap">
                <div class="table-responsive">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="14%" />
                            <col width="290" />
                            <col width="*" />
                            <col width="13%" />
                            <col width="37%" />
                        </colgroup>
                        <tbody>
                        <tr><!-- 템플릿명 -->
                            <th scope="row"><em class="required">required</em><spring:message code="template.menu.mcname"/></th><!-- 템플릿명 -->
                            <td colspan="4"><input type="text" class="form-control form-control-sm" name="contsNm" value="${mobileVo.contsNm}" /></td>
                        </tr>
                        <tr><!-- 공유유형 -->
                            <th scope="row"><em class="required">required</em><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                            <td colspan="4">
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="U_authType" value="U" <c:if test="${mobileVo.authType eq 'U'}">checked</c:if> class="custom-control-input" />
                                    <label class="custom-control-label" for="U_authType"><spring:message code="template.type.user"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio"name="authType" id="G_authType" value="G" <c:if test="${mobileVo.authType eq 'G'}">checked</c:if> class="custom-control-input" />
                                    <label class="custom-control-label" for="G_authType"><spring:message code="template.type.group"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="A_authType" value="A" <c:if test="${mobileVo.authType eq 'A'}">checked</c:if> class="custom-control-input" />
                                    <label class="custom-control-label" for="A_authType"><spring:message code="template.type.all"/></label>
                                </div>
                            </td>
                        </tr>

                        <c:if test="${mobileVo.fileType eq 'T' || mobileVo.fileType eq 'D'}">
                        <tr><!-- 템플릿 내용 (단문,장문) -->
                            <th scope="row"><em class="required">required</em><spring:message code="template.menu.mcontent"/></th><!-- 템플릿 내용 -->
                            <td colspan="4">
                                <div style="min-width: 236px !important; width: 236px !important;">
                                    <textarea class="form-control textarea-mobile" name="txta_conts" required>${mobileVo.contsTxt}</textarea>
                                    <div class="textarea-font-style"><span class="count-text" id="contsTxtLen">0</span>byte</div>
                                </div>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${mobileVo.fileType eq 'I'}"><!-- MMS 이미지 -->
                        <tr>
                            <th scope="row"><spring:message code="template.filetype.i"/></th>
                            <td height="220" class="border-right-0">
                                <div class="brandtalk-modify-sms-img brandtalk-img">
                                    <div class="imgwrap">
                                        <div class="image-area"><img src="${mobileVo.filePreviewPath}"/></div>
                                    </div>
                                </div>
                            </td>
                            <td colspan="3" class="border-right-0" style="vertical-align: top; padding-top: 20px;">
                                <div class="custom-file">
                                    <input type="file" class="custom-file-input" id="fileName" name="fileName">
                                    <label class="custom-file-label custom-file-label-sm" for="fileName"></label>
                                </div>
                                <ul class="notice-text text-left mb-0 mt-1 pl-1">
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsfiletypesize"/></li>
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsresolution"/></li>
                                </ul>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${mobileVo.fileType eq 'C'}">
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="template.menu.friendtalkImageUrl"/></th><!-- 친구톡 이미지 URL -->
                            <td colspan="4"><input type="text" class="form-control form-control-sm" name="friendtalkImageUrl" id="friendtalkImageUrl" value="${mobileVo.imageUrl }" maxlength="100" readonly="readonly" /></td>
                        </tr>
                        <tr class="mb-0">
                            <th scope="row" class="ls--1px"><em class="required">required</em><spring:message code="template.menu.friendtalkImageUrlLink"/></th><!-- 친구톡 이미지 LINK -->
                            <td colspan="4"><input type="text" class="form-control form-control-sm" name="friendtalkImageUrlLink" id="friendtalkImageUrlLink" value="${mobileVo.imageLink }" maxlength="100" /></td>
                        </tr>
                        <tr class="mt-0">
                            <th scope="row"><spring:message code="template.filetype.c"/></th><!-- 친구톡 이미지 -->
                            <td height="220" class="border-right-0">
                                <div class="brandtalk-modify-img brandtalk-img">
                                    <div class="imgwrap">
                                        <div class="image-area"><img src="${mobileVo.filePreviewPath}" /></div>
                                    </div>
                                </div>
                            </td>
                            <td colspan="3" style="vertical-align: top; padding-top: 20px;">
                                <div class="form-row">
                                    <div class="col-9">
                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input" id="firendFileName" name="fileName2">
                                            <label class="custom-file-label custom-file-label-sm mb-0" for="firendFileName"></label>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <button type="button" id="fileUploadBtn" class="btn btn-sm btn-outline-primary ml-2 mb-2">
                                            <i class="fas fa-upload"></i> <spring:message code="button.upload"/><!-- 파일올리기 -->
                                        </button>
                                    </div>
                                </div>
                                <ul class="notice-text text-left mb-0 mt-1 pl-1">
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.ftfiletypesize"/></li>
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.ftresolution"/></li>
                                </ul>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="ls--1px"><spring:message code="template.menu.friendtalkfailImage"/></th><!-- 친구톡 발송 실패시 발송할 MMS 이미지 -->
                            <td height="220" class="border-right-0">
                                <div class="brandtalk-modify-sms-img brandtalk-img">
                                    <div class="imgwrap">
                                        <div class="image-area"><img src="${mobileVo.detourPreviewPath}" /></div>
                                    </div>
                                </div>
                            </td>
                            <td colspan="3" class="border-right-0" style="vertical-align: top; padding-top: 20px;">
                                <div class="custom-file w-75">
                                    <input type="file" class="custom-file-input"  id="firendfailFileName"  name="fileName3">
                                    <label class="custom-file-label custom-file-label-sm mb-0" for="firendfailFileName"></label>
                                </div>
                                <ul class="notice-text text-left mb-0 mt-1 pl-1">
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsfiletypesize"/></li>
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsresolution"/></li>
                                </ul>
                            </td>
                        </tr>
                        </c:if>

                        <tr class="dp-tag"><!-- 태그  display 여부 css파일 dp-tag 수정 -->
                            <th scope="row"><spring:message code="campaign.menu.tag"/></th><!-- 태그 -->
                            <td colspan="4"><input type="text" class="form-control form-control-sm w-50" name="tagNm" value="${mobileVo.tagNm}" /></td>
                        </tr>
                        <tr><!-- 템플릿 설명 -->
                            <th scope="row"><spring:message code="template.menu.mcdes"/></th><!-- 템플릿 설명 -->
                            <td colspan="4"><input type="text" class="form-control form-control-sm" name="contsDesc" value="${mobileVo.contsDesc}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code='template.content.composer'/></th><!-- 작성자 -->
                            <td colspan="2">${mobileVo.userNm }</td>
                            <th scope="row"><spring:message code='template.content.created'/></th><!-- 작성 일자 -->
                            <td>${mobileVo.createDt} ${mobileVo.createTm}</td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->
            </div><!-- // card-body -->

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <button type="button" class="btn btn-outline-primary" id="deleteBtn">
                    <spring:message code="button.delete"/><!-- 삭제 -->
                </button>
                <button type="button" class="btn btn-outline-primary" id="saveBtn">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div>
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form:form>
<iframe id="uploadIFrame" name="uploadIFrame" style="display:none;visibility:hidden" src="about:blank"></iframe>
</body>
</html>