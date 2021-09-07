<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] IFRAME내 저작기 화면 출력 (FAX)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : campaign_fax_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%
    String campaignNo = request.getParameter("campaignNo"); // 캠페인번호
    String segmentNo = request.getParameter("segmentNo"); // 세그먼트번호
    String handlerType = request.getParameter("handlerType"); // 핸들러타입(G:그루비, S:스크립트)

    if(segmentNo == null || segmentNo.equals("")) segmentNo = "0"; // 세그먼트번호가 없으면 임시로 0 셋팅
%>
<html>
<head>
<%@ include file="/jsp/include/plugin.jsp" %>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script src='/js/editor/jquery.insertAtCaret.js'></script>
<script src="/js/editor/${editorJsFileName}"></script><!-- 채널별 JS 파일 호출 -->
<script type="text/javascript">
    var language = '${lang}';

    var campaignNo = <%=campaignNo%>; // 캠페인번호
    var segmentNo = <%=segmentNo%>; // 세그먼트번호
    var handlerType = "<%=handlerType%>"; // 핸들러타입(G:그루비, S:스크립트)

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        $('#template-tab').on("click", function(event) {
        	toggleEditor('T');
            //parent.resizeTopIframe(500);
        });
        $("#edit-tab").on("click", function(event) {
            toggleEditor('E');
        });
        $('#handler-tab').on("click", function(event) {
            //parent.resizeTopIframe(485);
        });
    }

    function initPage() {
        // 서버정보(수신확인 등) 조회
        getCampaignInfo();

        //옵션 다국어설정 사용 유무
        if("${useMultiLang}" != 'on') {
            $("#lang_menu").hide();
            $("#charset").hide();
        }

        $.extend(true, DefaultHandlerManager, {serviceGubun : 'EM', serviceNo : <%=campaignNo%>});  // 기본 핸들러 매니저 설정
    }

    // tinyMCE 렌더링
    function tinyMCEInit() {
        tinyMCE.init({
            mode : "textareas", // 모드 설정(tinyMCE 홈피 참조)
            language : "${sessionScope.adminSessionVo.language}", // 언어(en:영어, ko:한국어)
            theme : "advanced", // 테마 설정(tinyMCE 홈피 참조)
            plugins : "fullpage", // 플러그인
            editor_selector : "mceEditor", // tinyMCE 모드
            editor_deselector : "mceNoEditor", // textarea 모드
            theme_advanced_fonts : '<spring:message code="editor.font" />',
            theme_advanced_font_sizes : "1,2,3,4,5,6",
            forced_root_block : 0,
            force_br_newlines : 0, // 새로운 한줄 라인을 <br> 태그로 표시할 지 여부(0:false, 1:true)
            force_p_newlines : 1, // 새로운 한줄 라인을 <p> 태그로 표시할 지 여부(0:false, 1:true)
            visual : 0, // 템플릿 테두리 점선 표시 여부
            convert_fonts_to_spans : false, // font 태그를 <span font-style> 태그로 변경하지 않게 함.
            extended_valid_elements : "td[*|nowrap],style[*],object[*],param[*],embed[*],center[*],img[*],area[*],a[*]", // 유효성 체크 제외할 태그들
            convert_urls : 0, // 템플릿에 있는 url 값의 convert 를 사용할 지 여부(0:false, 1:true)
            cleanup : 1, // 이것을 사용하게 되면 템플릿이 표준에 맞게 변하진 않으나 tinyMCE 속성값이 붙음(0:false, 1:true)
            fullpage_default_title : "", // <title> 태그 초기값
            init_instance_callback : "tinyMCECallback", // tinyMCE 렌더링 완료 시 호출될 함수

            // 메뉴 옵션
            theme_advanced_buttons1 : "mysplitbutton,bold,italic,underline,strikethrough,|,cut,copy,paste,pastetext,pasteword,|,justifyleft,justifycenter,justifyright,justifyfull,fontselect,fontsizeselect,bullist,numlist,forecolor,backcolor,print",
            theme_advanced_buttons2 : "",
            theme_advanced_buttons3 : "",
            theme_advanced_buttons4 : "",
            theme_advanced_toolbar_location : "top", // 툴바 위치
            theme_advanced_toolbar_align : "left", // 툴바 정렬 위치
            theme_advanced_statusbar_location : "none", // 상태바 표시
            theme_advanced_resizing : 0, // 에디터 크기 조절 여부(0:false, 1:true)
            tab_focus : ":prev,:next", // tab 키로 포커스 이동
            content_css : "/css/tinymce.css", // css 위치
            setup : function(ed) { // tinyMCE 렌더링 완료 후 추가된 이벤트
                ed.onClick.add(function(ed, e) {
                    isTitleFocus = false;
                });
            }
        });
    }

    // tinyMCE 렌더링이 끝나면 호출
    function tinyMCECallback() {
        reload();
        window.setTimeout(parent.loadSet, 1000);
    }

    // 저작기 탭
    var tinyMCEmode = true;
    function toggleEditor(val) {
        if(val === 'T') { // 원본
            if(tinyMCEmode == false) {
                $("#template").css("display", "inline");
                tinyMCE.get('template').show();
            }

            tinyMCEmode = true;
        } else if(val == 'E') { // 편집
            if(tinyMCEmode == true) {
                tinyMCE.get('template').hide();
            }

            tinyMCEmode = false;
        } else if(val == 'H') { // 핸들러
            if(tinyMCEmode == true) {
                tinyMCE.get('template').hide();
            }

            tinyMCEmode = false;
        } else if(val == 4) { // 템플릿 backdoor
            if(tinyMCEmode == true) {
                tinyMCE.get('template').hide();
            }

            tinyMCEmode = false;
        }

        $.mdf.resizeIframe("#editorIfrm"); // iframe 높이를 다시 설정
    }

    // 2step 저장
    var isRealSave = false; // true:메시지저장, false:임시저장
    var nextStep = ""; // level(1step, 2step, 3step)
    function saveEditor(val, val2) {
        isRealSave = val;
        nextStep = val2;
        updateCampaign();
    }

    function reload() {
        var ed = tinyMCE.get('template');

        ed.setProgressState(1); // Show progress
        window.setTimeout(function() {
            ed.setProgressState(0); // Hide progress
            $.mdf.resizeIframe("#editorIfrm");
            if(templateArr.length > 0) {
                tinyMCE.get("template").setContent(templateArr[0].template);
                $("#editorFrm textarea[name=textmode]").val(templateArr[0].dbTemplate);
            }
        }, 500);
    }
</script>
</head>

<body style="margin: 0px;">
<form id="editorFrm" name="editorFrm" method="post">
<input type="hidden" name="no" value="<%=campaignNo%>" />
<input type="hidden" name="segmentNo" value="<%=segmentNo%>" />
<input type="hidden" name="handlerType" value="<%=handlerType%>" />
<input type="hidden" name="channel" value="F" />
<input type="hidden" name="segIndex" value="0" />
<input type="hidden" name="seg" value=" " />
<input type="hidden" name="templateType" value="1" />
<input type="hidden" name="campaignVo.handlerSeq" id="handlerSeq" value="0" />
<input type="hidden" name="templateContent" id="templateContent" />
<input type="hidden" name="campaignPreface" />

<ul class="nav nav-tabs tab01"><!-- tab -->
    <li class="nav-item">
        <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true" >
            <spring:message code="editor.template"/><!-- 템플릿 -->
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="edit-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="edit" aria-selected="true" >
            <spring:message code="editor.edit"/><!-- 편집-->
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false" >
            <spring:message code="editor.handler"/><!-- 핸들러 -->
        </a>
    </li>
</ul>

<div class="tab-content"><!-- tab content -->
    <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab"><!-- 템플릿 tab -->
        <div class="row my-3" id="templateTool"><!-- 기본 템플릿 저작기  -->
            <div class="col-12 search_input">
                <select name="personalizeFactor" id="personalizeFactor" onchange="javascript:selectSemantic(this);this.options[0].selected=true;" class="form-control form-control-sm"></select>
                <select class="form-control form-control-sm" onchange="javascript:selectOpen(this);this.options[0].selected=true;">
                    <option value="0"><spring:message code="editor.import"/></option><!-- 불러오기 -->
                    <option value="1"><spring:message code="editor.import.zip"/></option><!-- ZIP 파일 불러오기 -->
                    <option value="2"><spring:message code="editor.import.html"/></option><!-- HTML 파일 불러오기 -->
                    <option value="3"><spring:message code="editor.import.template"/></option><!-- 템플릿 불러오기 -->
                    <option value="4"><spring:message code="editor.import.url"/></option><!-- URL 불러오기 -->
                </select>
            </div>
        </div>

        <table class="editor_s" border="0" cellpadding="0" cellspacing="0" width="100%"><!-- //기본 템플릿 저작기-->
            <tr>
                <td>
                    <textarea id="template" name="template" style="width: 100%; height: 500px; word-break:break-all; margin: 0;" class="mceEditor"></textarea>
                    <textarea id="textmode" name="textmode" style="width: 100%; height: 500px; word-break:break-all; display: none;" class="mceNoEditor"></textarea>
                </td>
            </tr>
        </table>
    </div><!-- //템플릿 tab -->


    <div class="tab-pane" id="handlerTab" role="tabpanel" aria-labelledby="handler-tab"><!-- 핸들러 tab -->
        <div class="edit_area mt-2"><!-- 핸들러 -->
            <textarea class="form-control" id="handler" name="handler" rows="25"></textarea>
        </div>

        <div class="row mt-3 mb-1 align-items-center">
            <div class="col-3 mx-3">
                <div class="row align-items-center">
                    <label class="form-control-label mr-1"><spring:message code="common.default.handler" /></label><!-- 기본 핸들러-->
                    <select name="handlerList" id="handlerList" class="form-control form-control-sm d-inline-block">
                    </select>
                </div>
            </div>
            <div class="col-3 align-items-center"><!-- en:col-3 -->
                <div class="custom-control custom-switch" id="handlerWork">
                    <input type="checkbox" class="custom-control-input" id="handlerSwich" />
                    <label class="custom-control-label" for="handlerSwich"><spring:message code="editor.option.handler.3"/></label><!-- 핸들러 수정-->
                </div>
            </div>
        </div>
    </div><!-- //핸들러 tab -->
</div><!-- //tab content -->

<!-- 옵션시작 -->
<div class="card-body" id="option" style="display: none;">
    <h4 class="h3 mb-3"><spring:message code="button.option"/></h4><!-- 옵션 -->
    <div class="table-responsive">
        <table class="table table-sm dataTable">
            <colgroup>
                <col width="15%" />
                <col width="*" />
                <col width="15%" />
                <col width="30%" />
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><spring:message code="editor.option.person"/></th><!-- 개인화정보 -->
                <td colspan="3">
                     <select id="optionItem" name="optionItem" onclick="javascript:optionValue = this.value;" class="col-3 form-control form-control-sm"></select>
                </td>
            </tr>
            <tr>
                <th scope="row"><spring:message code="editor.option.slot1"/></th><!-- 로그부가정보1 -->
                <td>
                    <div class="form-row align-items-center">
                        <div class="col-9"><input type="text" id="slot1Value" readonly="readonly" class="form-control form-control-sm" /></div>
                        <div class="col-3">
                            <button type="button" onclick="addSlot(1, null)" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code='button.add'/><!-- 추가 -->
                            </button>
                            <button type="button" onclick="removeSlot(1)" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code="button.cancel"/><!-- 취소 -->
                            </button>
                        </div>
                    </div>
                </td>
                <th scope="row"><spring:message code="editor.option.sname"/></th><!-- 발신이름 -->
                <td>
                    <div class="form-row align-items-center">
                        <div class="col-9"><input type="text" id="senderNm" readonly="readonly" class="form-control form-control-sm" /></div>
                        <div class="col-3">
                            <button type="button" onclick="sender('add', 'name');" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code='button.add'/><!-- 추가 -->
                            </button>
                            <button type="button" onclick="remove('add', 'name');" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code="button.cancel"/><!-- 취소 -->
                            </button>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <th scope="row"><spring:message code="editor.option.slot2"/></th><!-- 로그부가정보2 -->
                <td>
                    <div class="form-row align-items-center">
                        <div class="col-9"><input type="text" id="slot2Value" readonly="readonly" class="form-control form-control-sm" /></div>
                        <div class="col-3">
                            <button type="button" onclick="addSlot(1, null)" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code='button.add'/><!-- 추가 -->
                            </button>
                            <button type="button" onclick="removeSlot(2)" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code="button.cancel"/><!-- 취소 -->
                            </button>
                        </div>
                    </div>
                </td>
                <th scope="row"><spring:message code="editor.option.smail"/></th>
                <td>
                    <div class="form-row align-items-center">
                        <div class="col-9"><input type="text" id="senderEmail" readonly="readonly" class="form-control form-control-sm" /></div>
                        <div class="col-3">
                            <button type="button" onclick="sender('add', 'email')" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code='button.add'/><!-- 추가 -->
                            </button>
                            <button type="button" onclick="sender('remove', 'email')" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code="button.cancel"/><!-- 취소 -->
                            </button>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <th scope="row"><spring:message code="editor.option.rmail"/></th><!-- 반송메일 -->
                <td>
                    <div class="form-row align-items-center">
                        <div class="col-9"><input type="text" id="retmail" readonly="readonly" class="form-control form-control-sm" /></div>
                        <div class="col-3">
                            <button type="button" onclick="retmailReceiver('add')" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code='button.add'/><!-- 추가 -->
                            </button>
                            <button type="button" onclick="retmailReceiver('remove')" class="btn btn-sm btn-outline-secondary w-100">
                                <spring:message code="button.cancel"/><!-- 취소 -->
                            </button>
                        </div>
                    </div>
                </td>
                <th scope="row" id="lang_menu" ><spring:message code="editor.option.lang"/></th><!-- 다국어설정 -->
                <td>
                     <select id="charset" onchange="javascript:selectCharset(this);" class="col form-control form-control-sm">
                        <option value="US-ASCII"><spring:message code="editor.menu.lang.en"/>(US-ASCII)</option><!-- 영어 -->
                        <option value="EUC-KR"><spring:message code="editor.menu.lang.ko"/></option><!-- 한국어 -->
                        <option value="GB2323"><spring:message code="editor.menu.lang.chs"/>(GB2323/base64)</option><!-- 중국어간체 -->
                        <option value="BIG5"><spring:message code="editor.menu.lang.cht"/>(BIG5/base64)</option><!-- 중국어번체 -->
                        <option value="SHIFT-JIS"><spring:message code="editor.menu.lang.jp"/>(SHIFT-JIS/base64)</option><!-- 일본어 -->
                        <option value="UTF-8">UTF-8(UTF-8/base64)</option>
                    </select>
                </td>
            </tr>
            </tbody>
        </table>
    </div><!-- //Light table -->
</div>
</form>

</body>
</html>