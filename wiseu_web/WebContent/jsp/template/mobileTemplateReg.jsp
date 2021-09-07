<%-------------------------------------------------------------------------------------------------
  * - [템플릿>모바일 템플릿 등록] 모바일 템플릿 등록 화면 출력
  * - URL : /template/mobileTemplateReg.do
  * - Controller :com.mnwise.wiseu.web.template.web.MobileTemplateRegController
  *
  * - [템플릿>모바일 템플릿 등록] 모바일 템플릿 등록
  * - URL : /template/mobileUpload.do
  * - Controller :com.mnwise.wiseu.web.template.web.MobileTemplateUploadController
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="template.mobiletemplate.reg.msg"/></title><!-- 모바일 템플릿 등록 -->
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
        $("textarea[name=contsTxt]").keyup(function() {
            var contsLen = getByte(this.value);
            if("D"==$("input[name='mobile_type']:checked").val() && 90 < contsLen) {
                alert("<spring:message code='template.alert.msg.reg.8' arguments='90'/>");  // 템플릿은 {0} byte를 초과할 수 없습니다.
                this.value = $.mdf.cutByte(this.value, 90);
            } else if("T"==$("input[name='mobile_type']:checked").val() && 2000 < contsLen) {
                alert("<spring:message code='template.alert.msg.reg.8' arguments='2000'/>");  // 템플릿은 {0} byte를 초과할 수 없습니다.
                this.value = $.mdf.cutByte(this.value, 2000);
            }
            $("span#contsTxtLen").html(getByte(this.value));
        });

        $("input:radio[name=mobile_type]").on("change", function(event) {
            var fileType = this.value;
            if(fileType == "T" || fileType == "D") {  // 단문, 장문
                $("#smsTemplate").show();
                $("#friendImage").hide();
                $("#friendImageUrl").hide();
                $("#friendImageLink").hide();
                $("#friendfailImage").hide();
                $("#mmsImage").hide();
            } else if(fileType == "C") {  // 친구톡 이미지
                $("#friendImage").show();
                $("#friendImageUrl").show();
                $("#friendImageLink").show();
                $("#friendfailImage").show();
                $("#smsTemplate").hide();
                $("#mmsImage").hide();
            } else if(fileType == "I") {  // MMS 이미지
                $("#mmsImage").show();
                $("#smsTemplate").hide();
                $("#friendImage").hide();
                $("#friendImageUrl").hide();
                $("#friendImageLink").hide();
                $("#friendfailImage").hide();
            }
        });

        // 파일올리기 버튼 클릭
        $("#fileUploadBtn").on("click", function(event) {
            var rules = {
                fileName2 : {required : true, extension : "jpg,png"}
            }

            if(!$.mdf.validElement("#updateForm", "#frtFileName", rules)){
                return false;
            }

            $("#updateForm").attr('action', '/template/ftrImgUpload.do').attr('target', 'uploadIFrame').submit();
        });

        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            var rules = {
                contsNm                : {notBlank : true, maxbyte : 50},
                fileName               : {required : true, extension : "jpg"},  // MMS 이미지
                fileName2              : {required : true, extension : "jpg,png"},  // 친구톡 이미지
                fileName3              : {extension : "jpg"},  // 친구톡 발송 실패시 발송할 MMS 이미지
                friendtalkImageUrlLink : {notBlank : true, url : true},
                contsDesc              : {maxbyte : 100},
            };

            if($.mdf.validForm("#updateForm", rules) == false) {
                return;
            }

            var fileType = $("input[name='mobile_type']:checked").val();
            var fileNm = "";
            if(fileType=="I") {
                fileNm = $("input[name=fileName]").val();
            } else if(fileType=="C") {
                if($("input[name=friendtalkImageUrl]").val() == "${mobileVo.imageUrl }") {
                    alert('<spring:message code="template.alert.msg.modify.1"/>');  // 친구톡 이미지는 이미지 업로드 후 수정 가능합니다.
                    return;
                }
                fileNm = $("input[name=fileName3]").val();
            }

            if(fileNm != "") {
                var param = {
                    fileEx : $.mdf.getExtension(fileNm),
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
        $("input:radio[name=authType][value=U]").prop("checked", true);  // 공유유형 선택 - 사용자(U)
        $("input:radio[name=mobile_type][value=D]").prop("checked", true);  // 템플릿 유형 선택 - 단문(D)
    }

    function updateMobileTemplate() {
        $("#updateForm").attr('action', '/template/mobileUpload.do').attr('target', '_self').submit();
    }

    function uploadImageResult(code, message, url, filepath, filename) {
        if(code=="200") {
            $("input[name=friendtalkImageUrl]").val(url);
            alert("<spring:message code='template.alert.kakao.request.success'/>");  // 요청이 처리되었습니다.
        } else {
            alert(message);
        }
    }
</script>
</head>
<body>
<form:form id="updateForm" name="updateForm" commandName="command" method="post" enctype="multipart/form-data" class="overflow-x-hidden">
<input type="hidden" name="contsNo" />
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="template.mobiletemplate.reg.msg"/></h3><!-- 모바일 템플릿 등록 -->
            </div>

            <div class="card-body gridWrap ">
                <spring:hasBindErrors name="command"><!-- 경고창 -->
                    <div class="col-12 alert alert-warning mb-0" role="alert">
                        <c:forEach var="error" items="${errors.allErrors}">
                            <span class="alert-icon"><i class="fas fa-exclamation-triangle"></i></span>
                            <span class="alert-text"><spring:message message="${error}" /><br></span>
                        </c:forEach>
                    </div>
                </spring:hasBindErrors>

                <div class="table-responsive overflow-x-hidden">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="14%" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="template.menu.mcname"/></th><!-- 템플릿명 -->
                            <td><input type="text" class="form-control form-control-sm" name="contsNm"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="template.table.share"/></th><!-- 공유유형 -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_U" value="U" class="custom-control-input" />
                                    <label class="custom-control-label" for="authType_U"><spring:message code="template.type.user"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_G" value="G" class="custom-control-input" />
                                    <label class="custom-control-label" for="authType_G"><spring:message code="template.type.group"/></label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" name="authType" id="authType_A" value="A" class="custom-control-input" />
                                    <label class="custom-control-label" for="authType_A"><spring:message code="template.type.all"/></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="template.filetype"/></th><!-- 템플릿 유형 -->
                            <td>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="mobileType_D" name="mobile_type" value="D" class="custom-control-input"/>
                                    <label class="custom-control-label" for="mobileType_D"><spring:message code="template.filetype.d"/>(80 byte)</label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="mobileType_T" name="mobile_type" value="T" class="custom-control-input" />
                                    <label class="custom-control-label" for="mobileType_T"><spring:message code="template.filetype.t"/>(2000 byte)</label>
                                </div>
                                <div class="custom-control custom-radio custom-control-inline">
                                    <input type="radio" id="mobileType_I" name="mobile_type" value="I" class="custom-control-input" />
                                    <label class="custom-control-label" for="mobileType_I"><spring:message code="template.filetype.i"/></label>
                                </div>
                                <c:forEach var="item" items="${wiseu:getProperty('channel.use.list', '')}">
                                    <c:if test="${item eq 'C'}">
                                        <div class="custom-control custom-radio custom-control-inline">
                                            <input type="radio" id="mobile_C" name="mobile_type" value="C" class="custom-control-input" />
                                            <label class="custom-control-label" for="mobile_C"><spring:message code="template.filetype.c"/></label>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                        <tr id="smsTemplate">
                            <th scope="row"><em class="required">required</em><spring:message code="template.menu.mcontent"/></th><!-- 템플릿 내용 (단문, 장문) -->
                            <td class="border-right-0">
                                <div style="width: 236px;">
                                    <textarea class="form-control textarea-mobile" name="contsTxt" required></textarea>
                                    <div class="textarea-font-style"><span class="count-text" id="contsTxtLen">0</span>byte</div>
                                </div>
                            </td>
                        </tr>
                        <tr id="mmsImage" style="display: none;"><!--  MMS 이미지 -->
                            <th scope="row"><em class="required">required</em><spring:message code="template.menu.filechoice"/></th><!-- 파일선택 -->
                            <td class="border-right-0 ">
                                <div class="input-group w-50">
                                    <div class="custom-file">
                                        <input type="file" class="custom-file-input" id="mmsFileName" name="fileName">
                                        <label class="custom-file-label custom-file-label-sm" for="mmsFileName"></label>
                                    </div>
                                </div>
                                <ul class="notice-text text-left mb-0 mt-1 pl-1">
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsfiletypesize"/></li>
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsresolution"/></li>
                                </ul>
                            </td>
                        </tr>
                        <tr id="friendImage" style="display: none;">
                            <th scope="row"><em class="required">required</em><spring:message code="template.filetype.c"/></th><!-- 친구톡 이미지 -->
                            <td class="border-right-0">
                                <div class="form-row">
                                    <div class="col-6 w-50">
                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input" id="frtFileName" name="fileName2">
                                            <label class="custom-file-label custom-file-label-sm mb-0" for="frtFileName"></label>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <button type="button" class="btn btn-sm btn-outline-primary ml-2 mb-2" id="fileUploadBtn">
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
                        <tr id="friendImageUrl" style="display: none;">
                            <th scope="row" class="ls--1px"><spring:message code="template.menu.friendtalkImageUrl"/></th><!-- 친구톡 이미지 URL -->
                            <td><input type="text" class="form-control form-control-sm" name="friendtalkImageUrl" maxlength="100" readonly="readonly"/></td>
                        </tr>
                        <tr id="friendImageLink" style="display: none;">
                            <th scope="row" class="ls--1px"><em class="required">required</em><spring:message code="template.menu.friendtalkImageUrlLink"/></th><!-- 친구톡 이미지 LINK -->
                            <td><input type="text" class="form-control form-control-sm" name="friendtalkImageUrlLink" maxlength="100" /></td>
                        </tr>
                        <!-- 친구톡 실패시 문자 정보-->
                        <tr id="friendfailImage" style="display: none;">
                            <th scope="row" class="ls--1px"><spring:message code="template.menu.friendtalkfailImage"/></th><!-- 친구톡 발송 실패시<br/>&nbsp;발송할 MMS 이미지 -->
                            <td class="border-right-0 ">
                                <div class="input-group w-50">
                                    <div class="custom-file">
                                        <input type="file" class="custom-file-input" id="frtFailFileName" name="fileName3">
                                        <label class="custom-file-label custom-file-label-sm mb-0" for="frtFailFileName"></label>
                                    </div>
                                </div>
                                <ul class="notice-text text-left mb-0 mt-1 pl-1">
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsfiletypesize"/></li>
                                    <li><em class="li-circle">li-circle</em><spring:message code="template.warn.mmsresolution"/></li>
                                </ul>
                            </td>
                        </tr>
                        <tr class="dp-tag"><!-- 태그  display 여부 css파일 dp-tag 수정 -->
                            <th scope="row"><spring:message code="campaign.menu.tag"/></th><!-- 태그 -->
                            <td><input type="text" class="form-control form-control-sm w-50" name="tagNm" id="id_searchWord"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="template.menu.mcdes"/></th><!-- 템플릿 설명 -->
                            <td><input type="text" class="form-control form-control-sm" name="contsDesc"/></td>
                        </tr>
                    </tbody>
                    </table>
                </div><!-- // Light table -->
            </div><!-- // card-body -->

            <div class="card-footer pb-4 btn_area"><!-- button area -->
                <button type="button" id="saveBtn" class="btn btn-outline-primary">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div>
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
<iframe id="uploadIFrame" name="uploadIFrame" style="display:none;visibility:hidden"></iframe>
</form:form>
</body>
</html>