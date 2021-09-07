<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] IFRAME내 저작기 화면 출력 (메일)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : campaign_mail_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>

<%
    String campaignNo = request.getParameter("campaignNo"); // 캠페인번호
    String segmentNo = request.getParameter("segmentNo"); // 세그먼트번호
    String handlerType = request.getParameter("handlerType"); // 핸들러타입(G:그루비, S:스크립트)
    String abTestType = request.getParameter("abTestType"); // 핸들러타입(G:그루비, S:스크립트)
    if(segmentNo == null || segmentNo.equals("")) segmentNo = "0"; // 세그먼트번호가 없으면 임시로 0 셋팅
%>
<html>
<head>
<%@ include file="/jsp/include/plugin.jsp" %>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script src="/js/editor/linktrace.js"></script>
<script src="/js/editor/multipartfile.js"></script>
<script src='/js/editor/jquery.insertAtCaret.js'></script>
<script src="/js/editor/${editorJsFileName}"></script><!-- 채널별 JS 파일 호출 -->
<script type="text/javascript">
    var language = '${lang}';

    var campaignNo = <%=campaignNo%>; // 캠페인번호
    var segmentNo = <%=segmentNo%>; // 세그먼트번호
    var handlerType = "<%=handlerType%>"; // 핸들러타입(G:그루비, S:스크립트)
    var abTestType = "<%=abTestType%>"; // 핸들러타입(G:그루비, S:스크립트)

    // tinyMCE 렌더링
    //function tinyMCEInit() {
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
            extended_valid_elements : "title[*],td[*|nowrap],style[*],object[*],param[*],embed[*],center[*],img[*],area[*],a[*]", // 유효성 체크 제외할 태그들
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
                    isTitleFocusAb = false;
                });
            }
        });
    //}

    // tinyMCE 렌더링
    //function tinyMCEInit2() {
        tinyMCE.init({
            mode : "textareas", // 모드 설정(tinyMCE 홈피 참조)
            language : "${sessionScope.adminSessionVo.language}", // 언어(en:영어, ko:한국어)
            theme : "advanced", // 테마 설정(tinyMCE 홈피 참조)
            plugins : "fullpage", // 플러그인
            editor_selector : "mceEditorTest", // tinyMCE 모드
            editor_deselector : "mceNoEditorTest", // textarea 모드
            theme_advanced_fonts : '<spring:message code="editor.font" />',
            theme_advanced_font_sizes : "1,2,3,4,5,6",
            forced_root_block : 0,
            force_br_newlines : 0, // 새로운 한줄 라인을 <br> 태그로 표시할 지 여부(0:false, 1:true)
            force_p_newlines : 1, // 새로운 한줄 라인을 <p> 태그로 표시할 지 여부(0:false, 1:true)
            visual : 0, // 템플릿 테두리 점선 표시 여부
            convert_fonts_to_spans : false, // font 태그를 <span font-style> 태그로 변경하지 않게 함.
            extended_valid_elements : "title[*],td[*|nowrap],style[*],object[*],param[*],embed[*],center[*],img[*],area[*],a[*]", // 유효성 체크 제외할 태그들
            convert_urls : 0, // 템플릿에 있는 url 값의 convert 를 사용할 지 여부(0:false, 1:true)
            cleanup : 1, // 이것을 사용하게 되면 템플릿이 표준에 맞게 변하진 않으나 tinyMCE 속성값이 붙음(0:false, 1:true)
            fullpage_default_title : "", // <title> 태그 초기값
            init_instance_callback : "tinyMCECallback2", // tinyMCE 렌더링 완료 시 호출될 함수

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
                    isTitleFocusAb = false;
                });
            }
        });
    //}

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        $("#handler-tab").on("click", function(event) {
            toggleEditor('H');
            parent.resizeTopIframe(585);
        });

        $("#template-tab").on("click", function(event) {
            toggleEditor('1');
            parent.resizeTopIframe(790);
        });

        $("#templateB-tab").on("click", function(event) {
            toggleEditor('5');
            parent.resizeTopIframe(790);
        });

        $("#edit-tab").on("click", function(event) {
            toggleEditor('2');
            parent.resizeTopIframe(790);
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

        //A/B테스트 기능 사용이면 편집 탭 숨김처림
        if(abTestType != "N") {
            $("#tab2").hide();
        } else {
            $("#tab2").show();
        }

        $.extend(true, DefaultHandlerManager, {serviceGubun : 'EM', serviceNo : <%=campaignNo%>});  // 기본 핸들러 매니저 설정
    }

    // tinyMCE 렌더링이 끝나면 호출
    function tinyMCECallback() {
        reload();
        window.setTimeout(parent.loadSet, 1000);
    }

    // tinyMCE 렌더링이 끝나면 호출
    function tinyMCECallback2() {
        reloadAb();
        window.setTimeout(parent.loadSet, 1000);
    }

    // editorArea 호출
    function editorAreaPopup() {
        if($("#editorFrm input[name=tagType]").val() != 'campaign_default') {
            var openURL = "/editor/editAreaPopup.do?type=campaign";
            openWin = window.open(openURL, 'editorAreaPopup', 'width=800, height=600, resizable=yes, status=no,  menubar=no, toolbar=no');
            openWin.focus();
        } else {
            alert('<spring:message code="editor.popup.alert.error"/>');  // 본문에서는 사용하실 수 없습니다.
        }
    }

    // 저작기 탭
    var tinyMCEmode = true;
    var tinyMCEmodeTest = false;
    var templateA = false;
    var templateB = false;

    function toggleEditor(val) {
        if(val == 1) { // 원본
            $("#editorFrm input[name=tagType]").val('campaign_default');
            tinyMCE.get('template').show();
            if(abTestType == "T") {
                tinyMCE.get('templateAb').hide();
            }
            tinyMCE.get('template').show();
            $("#templateTool").show();
            $("#templateAbTool").hide();
            tinyMCEmode = true;
            templateA = true;
            templateB = false;
            $("#attachTab").show();
            //$("#optionBtn").show();
        } else if(val == 2) { // 편집
            if(tinyMCEmode == true) {
                tinyMCE.get('template').hide();

                if(abTestType == "T") {
                    tinyMCE.get('templateAb').hide();
                }
            }

            $("#template").css("display", "inline");
            $("#handler").css("display", "none");
            $("#textmode").css("display", "none");
            tinyMCEmode = false;
            $("#editorFrm input[name=tagType]").val('campaign_template');
            $("#templateTool").hide();
            $("#attachTab").hide();
            $("#option").addClass("dp-none");
            //$("#optionBtn").show();
        } else if(val == 5) { // AB테스트 본문B클릭
            $("#editorFrm input[name=tagType]").val('campaign_default');
            tinyMCE.get('template').hide();
            tinyMCE.get('templateAb').show();
            $("#templateTool").hide();
            $("#templateAbTool").show();
            $("#attachTab").show();
            //$("#optionBtn").show();

            //mce편집기 loading
            if(tinyMCEmodeTest == false) {
                reloadAb();
                tinyMCEmodeTest = true;
            }

            tinyMCEmode = true;
            templateA = false;
            templateB = true;
        } else if(val == 'H') { // 핸들러
            if(tinyMCEmode == true) {
                tinyMCE.get('template').hide();
                if(abTestType == "T") {
                    tinyMCE.get('templateAb').hide();
                }
            }
            $("#template").css("display", "none");
            $("#handler").css("display", "inline");
            $("#textmode").css("display", "none");
            $("#attachTab").hide();
            //$("#optionBtn").hide();
            tinyMCEmode = false;
            $("#editorFrm input[name=tagType]").val('campaign_handler');
        } else if(val == 4) { // 템플릿 backdoor
            if(tinyMCEmode == true) {
                tinyMCE.get('template').hide();
            }

            $("#template").css("display", "none");
            $("#handler").css("display", "none");
            $("#textmode").css("display", "inline");
            $("#tab1").attr("src", "/images${lang}/editor/edit_btn_05_off.gif");
            $("#tab2").attr("src", "/images${lang}/editor/tab_off_html.gif");
            $("#tab3").attr("src", "/images${lang}/editor/tab_off_handler.gif");
            $("#tab4").attr("src", "/images${lang}/editor/Editor_tab_04_on.gif");
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

    var linktrace = null;
    var linktraceAb = null;
    function reload() {
        var ed = tinyMCE.get('template');
        ed.setProgressState(1); // Show progress
        window.setTimeout(function() {
            // 핸들러, 핸들러목록 감추기
            $("#template-tab").click();

            ed.setProgressState(0); // Hide progress

            $.mdf.resizeIframe("#editorIfrm");

            if(templateArr.length > 0) {
                tinyMCE.get("template").setContent(templateArr[0].template);
                $("#editorFrm textarea[name=textmode]").val(templateArr[0].dbTemplate);
            }
        }, 500);

        if(abTestType == "N") {
            $("#tabContent").removeClass("dp-none");
        }
    }

    function reloadAb() {
        var ed = tinyMCE.get('templateAb');
        ed.setProgressState(1); // Show progress
        window.setTimeout(function() {
            ed.setProgressState(0); // Hide progress
            $.mdf.resizeIframe("#editorIfrm");
            if(templateArr.length > 0) {
                tinyMCE.get("templateAb").setContent(templateArr[1].template);
                $("#editorFrm textarea[name=textmodeTest]").val(templateArr[1].dbTemplate);
            }
        }, 500);
        $("#tabContent").removeClass("dp-none");
    }

    // 제목에 포커스 체크 - 개인화인자 넣기 위함
    var isTitleFocus = false;
    function titleFocus() {
        isTitleFocus = true;
        sTitleFocusAb = false;
    }

    var isTitleFocusAb = false;
    function titleFocusAb() {
        isTitleFocusAb = true;
        isTitleFocus = false;
    }
</script>

</head>
<body class="m-0">
    <form id="editorFrm" name="editorFrm" method="post">
    <input type="hidden" name="no" value="<%=campaignNo%>" />
    <input type="hidden" name="segmentNo" value="<%=segmentNo%>" />
    <input type="hidden" name="handlerType" value="<%=handlerType%>" />
    <input type="hidden" name="channel" value="M" />
    <input type="hidden" name="segIndex" value="0" />
    <input type="hidden" name="seg" value=" " />
    <input type="hidden" name="templateType" value="1" />
    <input type="hidden" name="linkSeq" value="0" />
    <input type="hidden" name="linkSeqAb" value="0" />
    <input type="hidden" name="tagType" value="campaign_default" />
    <input type="hidden" name="abTestType" value="${abTestType}" />
    <input type="hidden" name="campaignVo.handlerSeq" id="handlerSeq" value="0" />
    <input type="hidden" name="templateContent" id="templateContent" />
    <input type="hidden" name="templateAbContent" id="templateAbContent" />
    <input type="hidden" name="fileAlias" id="fileAlias"/>

    <div class="table-responsive gridWrap">
        <table class="table table-sm dataTable table-fixed" style="margin-top: -1px !important;">
            <colgroup>
                <col width="12%" />
                <col width="*" />
            </colgroup>
            <tbody>
            <tr>
                <c:choose>
                    <c:when test="${abTestType eq 'S'}">
                        <th scope="row"><em class="required"></em><spring:message code="campaign.divide.subjectA" /></th><!-- 제목(A) -->
                    </c:when>
                    <c:otherwise>
                        <th scope="row"><em class="required"></em><spring:message code="campaign.menu.subject" /></th><!-- 제목 -->
                    </c:otherwise>
                </c:choose>
                <td colspan="5">
                    <input type="text" name="campaignPreface" id="campaignPreface" onfocus="javascript:titleFocus()" maxlength="100" class="form-control form-control-sm"/>
                </td>
            </tr>

            <c:if test="${abTestType eq 'S'}">
            <tr>
                <th scope="row"><em class="required"></em><spring:message code="campaign.divide.subjectB" /></th><!-- 제목(B) -->
                <td colspan="5">
                    <input type="text" name="campaignPrefaceAb" id="campaignPrefaceAb" onfocus="javascript:titleFocusAb()" maxlength="100" class="form-control form-control-sm"/>
                </td>
            </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <ul class="nav nav-tabs tab01 dp-none" id="tabContent"><!-- 본문/편집/핸들러 탭 -->
        <c:if test="${abTestType ne 'T'}">
        <li class="nav-item">
            <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true" >
                <spring:message code="editor.text"/><!-- 본문 -->
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="edit-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="edit" aria-selected="false">
                <spring:message code="editor.edit"/><!-- 편집 -->
            </a>
        </li>
        </c:if>

        <c:if test="${abTestType eq 'T'}">
        <li class="nav-item">
            <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true" >
                <spring:message code="editor.text"/>A<!-- 본문 A-->
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="templateB-tab" data-toggle="tab" href="#templateBTab" role="tab" aria-controls="templateB" aria-selected="false">
                <spring:message code="editor.text"/>B<!--  본문 B-->
            </a>
        </li>
        </c:if>

        <li class="nav-item">
            <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false">
                <spring:message code="editor.handler"/><!--  핸들러-->
            </a>
        </li>
    </ul><!-- //tab -->

    <div class="tab-content"><!-- tab content -->
        <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab"><!-- 본문/본문A tab -->
            <div class="row" id="templateTool"><!-- 본문/본문A 저작기 툴바 -->
                <div class="col-10 search_input my-2">
                    <select name="personalizeFactor" id="personalizeFactor" onchange="javascript:selectSemantic(this);this.options[0].selected=true;" class="form-control form-control-sm"></select>
                    <select class="form-control form-control-sm" onchange="javascript:selectOpen(this);this.options[0].selected=true;">
                    <option value="0"><spring:message code="editor.import"/></option><!-- 불러오기 -->
                        <option value="1"><spring:message code="editor.import.zip"/></option><!-- ZIP 파일 불러오기 -->
                        <option value="2"><spring:message code="editor.import.html"/></option><!-- HTML 파일 불러오기 -->
                        <option value="3"><spring:message code="editor.import.template"/></option><!-- 템플릿 불러오기 -->
                        <option value="4"><spring:message code="editor.import.url"/></option><!-- URL 불러오기 -->
                        <option value="5"><spring:message code="editor.import.attach"/></option><!-- 파일 첨부하기 -->
                    </select>
                    <select class="form-control form-control-sm" onchange="javascript:selectLink(this);this.options[0].selected=true;" >
                        <option value="0"><spring:message code="editor.link"/></option><!-- 링크 -->
                        <option value="1"><spring:message code="editor.link.hyper"/></option><!-- 하이퍼링크 -->
                        <option value="2"><spring:message code="editor.link.trace"/></option><!-- 링크추적 -->
                    </select>
                    <button type="button" onclick="setupReject();" class="btn btn-sm btn-outline-danger btn_mail_reject">
                        메일 수신거부 아이콘
                    </button>
                </div>
                <div class="col-2 my-2 justify-content-end"><!-- 옵션 버튼-->
                     <button type="button" class="btn btn-sm btn-outline-primary" id="optionBtn" onclick="option(); return false;">
                        <spring:message code="button.option" /><!-- 옵션 -->
                     </button>
                </div>
            </div>

            <div class="edit_area"><!-- 편집 textarea -->
                <textarea class="mceEditor form-control" id="template" name="template" style="width: 100%; height: 500px; word-break:break-all; margin: 0;"></textarea>
            </div>

            <div class="edit_area"><!-- 템플릿 저작기 편집창 -->
                <textarea id="textmode" name="textmode" style="width: 100%; height: 500px; word-break:break-all; display: none; margin: 0;" class="mceNoEditor"></textarea>
                <c:if test="${abTestType eq 'T'}">
                    <textarea id="textmodeTest" name="textmodeTest" style="width: 100%; height: 500px; word-break:break-all; display: none; margin: 0;" class="mceNoEditorTest"></textarea>
                </c:if>
            </div>
        </div><!-- //본문/본문A tab -->

        <div class="tab-pane" id="templateBTab" role="tabpanel" aria-labelledby="template-tab"><!-- 본문B tab -->
            <div class="row my-2" id="templateAbTool" style="display: none;"><!-- 본문B 저작기 툴바 -->
                <div class="col-10 search_input">
                    <select name="personalizeFactorAb" id="personalizeFactorAb" onchange="javascript:selectSemanticAb(this);this.options[0].selected=true;" class="form-control form-control-sm"></select>
                    <select class="form-control form-control-sm" onchange="javascript:selectOpenAb(this);this.options[0].selected=true;">
                        <option value="0"><spring:message code="editor.import"/></option><!-- 불러오기 -->
                        <option value="1"><spring:message code="editor.import.zip"/></option><!-- ZIP 파일 불러오기 -->
                        <option value="2"><spring:message code="editor.import.html"/></option><!-- HTML 파일 불러오기 -->
                        <option value="3"><spring:message code="editor.import.template"/></option><!-- 템플릿 불러오기 -->
                        <option value="4"><spring:message code="editor.import.url"/></option><!-- URL 불러오기 -->
                        <option value="5"><spring:message code="editor.import.attach"/></option><!-- 파일 첨부하기 -->
                    </select>
                    <select class="form-control form-control-sm" onchange="javascript:selectLinkAb(this);this.options[0].selected=true;" >
                        <option value="0"><spring:message code="editor.link"/></option><!-- 링크 -->
                        <option value="1"><spring:message code="editor.link.hyper"/></option><!-- 하이퍼링크 -->
                        <option value="2"><spring:message code="editor.link.trace"/></option><!-- 링크추적 -->
                    </select>
                    <button type="button" onclick="setupRejectAb();" class="btn btn-outline-danger btn-sm btn_mail_reject"/>
                </div>
                <div class="col-2 justify-content-end"><!-- 옵션 버튼-->
                     <button type="button" class="btn btn-sm btn-outline-primary" id="optionBtn" onclick="option(); return false;">
                        <spring:message code="button.option" /><!-- 옵션 -->
                     </button>
                </div>
            </div>

            <c:if test="${abTestType eq 'T'}"><!-- AB 템플릿 Textarea-->
                <textarea id="templateAb" name="templateAb" style="width: 100%; height: 500px; word-break:break-all; display: none; margin: 0;" class="mceEditorTest"></textarea>
            </c:if>
        </div>

        <div class="card-body attrfiles_status" id="attachTab"><!-- 첨부파일 -->
            <div class="row">
                <div class="col-12">
                    <i class="fas fa-paperclip"></i> <spring:message code="editor.menu.attachfile"/> :
                    <b id="fileCount">0</b> <spring:message code="editor.menu.attachfileItem"/><!-- 개 -->
                </div>
            </div>
            <div class="row">
                <table>
                    <tr>
                        <td id="multifileTagId" ></td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="tab-pane" id="editTab" role="tabpanel" aria-labelledby="edit-tab"><!-- 편집 tab -->
        </div>

        <div class="tab-pane" id="handlerTab" role="tabpanel" aria-labelledby="handler-tab"><!-- 핸들러 tab -->
            <div class="edit_area mt-2"><!-- 핸들러 textarea -->
                <textarea class="form-control" id="handler" name="handler" rows="18"></textarea>
            </div>
            <div class="row mt-3 mb-1 align-items-center">
                <div class="col-3 mx-3">
                    <div class="row align-items-center">
                        <label class="form-control-label mr-1"><spring:message code="common.default.handler" /></label><!-- 기본 핸들러-->
                        <select name="handlerList" id="handlerList" class="form-control form-control-sm d-inline-block"></select>
                    </div>
                </div>
                <div class="col-3 align-items-center"><!-- en:col-3 -->
                    <div class="custom-control custom-switch" id="handlerWork">
                        <input type="checkbox" class="custom-control-input" id="handlerSwich" />
                        <label class="custom-control-label" for="handlerSwich"><spring:message code="editor.option.handler.3"/></label><!-- 핸들러수정-->
                    </div>
                </div>
            </div>
        </div>
    </div><!-- //tab content -->

    <!-- 옵션시작 -->
    <div class="card-body px-0 dp-none" id="option" >
        <h1 class="h3 text-primary mt-3 mb-0"><spring:message code="button.option"/></h1><!-- 옵션 -->
        <div class="table-responsive gridWrap">
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
                                <button type="button" onclick="addSlot(1, null)" class="btn btn-sm btn-outline-primary">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                                <button type="button" onclick="removeSlot(1)" class="btn btn-sm btn-outline-primary">
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
                                <button type="button" onclick="sender('add', 'name');" class="btn btn-sm btn-outline-primary">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                                <button type="button" onclick="sender('remove', 'name');" class="btn btn-sm btn-outline-primary">
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
                                <button type="button" onclick="addSlot(2, null)" class="btn btn-sm btn-outline-primary">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                                <button type="button" onclick="removeSlot(2)" class="btn btn-sm btn-outline-primary">
                                    <spring:message code="button.cancel"/><!-- 취소 -->
                                </button>
                            </div>
                        </div>
                    </td>
                    <th scope="row"><spring:message code="editor.option.smail"/></th><!-- 발신메일 -->
                    <td>
                        <div class="form-row align-items-center">
                            <div class="col-9"><input type="text" id="senderEmail" readonly="readonly" class="form-control form-control-sm" /></div>
                            <div class="col-3">
                                <button type="button" onclick="sender('add', 'email')" class="btn btn-sm btn-outline-primary">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                                <button type="button" onclick="sender('remove', 'email')" class="btn btn-sm btn-outline-primary">
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
                                <button type="button" onclick="retmailReceiver('add')" class="btn btn-sm btn-outline-primary">
                                    <spring:message code='button.add'/><!-- 추가 -->
                                </button>
                                <button type="button" onclick="retmailReceiver('remove')" class="btn btn-sm btn-outline-primary">
                                    <spring:message code="button.cancel"/><!-- 취소 -->
                                </button>
                            </div>
                        </div>
                    </td>
                    <th scope="row" id="lang_menu"><spring:message code="editor.option.lang"/></th><!-- 다국어설정 -->
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
        </div>
    </div><!-- // 옵션-->
</form>
</body>
</html>
