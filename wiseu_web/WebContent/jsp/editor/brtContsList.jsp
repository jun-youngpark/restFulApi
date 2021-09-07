<%-------------------------------------------------------------------------------------------------
 * - [에디터>템플릿 목록 불러오기] 카카오 템플릿 목록 불러오기<br/>
 * - URL : /editor/brtContsList.do <br/>
 * - Controller :  com.mnwise.wiseu.web.editor.web.EditortKakaoController <br/>
 * Title  : 이케어 등록 이케어 수행 2 STEP 화면
 * - 이전 파일명 : kakaoContentsList.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">

    $(document).ready(function() {
        initContsEventBind();
        initContsPage();
    });

    function initContsEventBind() {
        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
        	 event.preventDefault();
        	 $("#brtContentsFrm input[name=searchColumn]").val($("select[name=searchColumName]").val());
             $("#brtContentsFrm input[name=searchWord]").val($("input[name=searchValue]").val());
             getContentsList();
        });
    }

    function initContsPage() {
    }

    function isLoadBrt(){   // ecareEditor.js 에서 호출
    	  var contsNo = $('#editorFrm input[name=kakaoImageNo]').val();
          viewBrt(contsNo, 'B');   //브랜드톡 상세보기 호출
    }

    function viewBrt(contsNo, fileType) {
        //tinyMCE.activeEditor.setContent("");
        var param = {
        		 contsNo : contsNo,
        		"fileType" : fileType
        }
        $.post("/editor/brtContentView.do", $.param(param, true), function(result) {
            $("#brtViewWrap").html(result);
        });
    }

    function setEditor(contsTxt, filePath, kakaoButtons, kakaoTemplateCd, contsNo, senderKey) {
        //상단 대메뉴 campaing인지 ecare 인지 확인
        var top_menu = top.document.referrer.split('/')[3];
        if(top_menu == 'campaign') {
            parent.$("input[name='campaignVo.kakaoImageNo']").val(contsNo);
            parent.$("input[name='campaignVo.kakaoTmplCd']").val(kakaoTemplateCd);
            parent.$("input[name='campaignVo.kakaoSenderKey']").val(senderKey);
            parent.setBrandTalkButtons(kakaoButtons);   //common2step.js
        } else {
            $('#kakaoImageNo').val(contsNo);
            $('#kakaoTmplCd').val(kakaoTemplateCd);
            $('#kakaoSenderKey').val(senderKey);
            setBrandTalkButtons(kakaoButtons);
        }
        tinyMCE.get('template').setContent('<img height="149px" width="203px" src="' + filePath + '"/>' + contsTxt);
    }

</script>
</head>
<div class="row justify-content-end mt-2 mb-2"><!-- search -->
    <div class="col-12 justify-content-end">
        <div class="search_input">
            <select name="searchColumName" id="searchColumName" class="form-control form-control-sm">
                <option value="contsNm"><spring:message code="template.column.tmplnm"/></option><!-- 템플릿명 -->
                <option value="kakaoTmplCd"><spring:message code="template.column.tmplcd"/></option><!-- 템플릿 코드 -->
            </select>
            <input type="search" id="searchValue" name="searchValue" maxlength="30" class="form-control form-control-sm" value="${param.searchWord}"/>
            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                <spring:message code="button.search"/><!-- 검색 -->
            </button>
        </div>
    </div>
</div>
<!-- //search -->

<div class="row">
    <div class="col-5">
        <div class="table-responsive gridWrap">
            <div  id="brtViewWrap"></div>
            <!-- <iframe name="brtContentViewiframe" id="brtContentViewiframe"  scrolling="yes" frameborder="0" width="100%" height="100%"></iframe> -->
        </div>
    </div>
    <div class="col-7 pl-0">
        <div class="table-responsive overflow-x-hidden">
            <table class="table table-sm dataTable table-hover table-fixed">
                <thead class="thead-light">
                <tr>
                    <th scope="col" width="8%" class="th-center">No</th>
                    <th scope="col" width="30%" class="th-center"><spring:message code="template.column.yellowid" /></th><!-- 카카오톡 채널ID -->
                    <th scope="col" width="*" class="th-center"><spring:message code="template.menu.mcname" /></th><!-- 템플릿명 -->
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${kakaoContentsList}">
                <tr style="cursor: pointer;" onclick="javascript:viewBrt('${list.contsNo}','${list.fileType}')">
                    <td>${list.contsNo}</td>
                    <td>${list.kakaoYellowId}</td>
                    <td class="text-left text-break" title="${list.contsNm}">${list.contsNm}</td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 페이징 -->
        <c:import url="/common/page.do">
            <c:param name="viewPath" value="/common/page_ajax"/>
            <c:param name="actionPath" value="${actionPath}"/>
            <c:param name="method" value="get"/>
            <c:param name="total" value="${totalCount}"/>
            <c:param name="countPerPage" value="${countPerPage}"/>
            <c:param name="nowPage" value="${nowPage}"/>
            <c:param name="hiddenParam" value="fileType:${param.fileType}"/>
            <c:param name="hiddenParam" value="contsTxt:${param.contsTxt}"/>
            <c:param name="hiddenParam" value="subType:${param.subType}"/>
            <c:param name="returnDocumentId" value="tmplWrap"/>
            <c:param name="hiddenParam" value="searchColumn:${searchColumn}" />
            <c:param name="hiddenParam" value="searchWord:${searchWord}" />
        </c:import>
    </div>
</div>
