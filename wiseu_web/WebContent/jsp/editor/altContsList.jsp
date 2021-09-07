<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>메시지 작성] 알림톡 템플릿 리스트
 * - URL : /editor/altContsList.do <br/>
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorAltalkController <br/>
 * - 이전 파일명 : altalk_contents_editor.jsp
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
            $("#altalkContentsFrm input[name=searchColumn]").val($("select[name=searchColumName]").val());
            $("#altalkContentsFrm input[name=searchWord]").val($("input[name=searchValue]").val());
            getContentsList();
        });

        //상세버튼 클릭
        $(".detailBtn").on("click", function (event) {
            var contsNo= $(this).data("contsno");
            var url = "/template/altTemplatePopupView.do?popupYn=Y&contsNo="+contsNo;
            $.mdf.popupGet(url,'detailPopup', 1200, 850,"scrollbars=yes,resizable=yes");
        });

        //템플릿 선택 클릭
        $(".choice").on("click", function (event) {
             var index= $(this).data("index");
             var contsNo= $(this).data("contsno");
             var image= $(this).data("image");
             if(!confirm("<spring:message code='editor.confirm.template.1'/>")) return;  // 선택한 템플릿을 사용하시겠습니까?
             var content = $("input[name='contsTxt["+index+"]'").val();
             var kakaoTmplCd = $("input[name='kakaoTmplCd["+index+"]'").val();
             var kakaoSenderKey = $("input[name='kakaoSenderKey["+index+"]'").val();
             var kakaoButtons = $("textarea[name='kakaoButtons["+index+"]'").val();
             var kakaoQuickReplies = $("textarea[name='kakaoQuickReplies["+index+"]'").val();
             content = content.replace(/\n/g, "<br/>");
             content = setImageTemplate(content , image)   //common.js
             tinyMCE.get('template').setContent(content);
             setAltalkData(kakaoSenderKey, kakaoTmplCd, kakaoButtons, kakaoQuickReplies, contsNo)  //common2step.js
        });
    }

    function initContsPage() {
        $("#altalkContentsFrm input[name=contsTxt]").val("");
        $("#contsTxt").val("${contsTxt}");
    }

</script>
</head>
<div class="row justify-content-end mt-2 mb-2"><!-- search -->
    <div class="col-12 justify-content-end">
        <div class="search_input">
            <select name="searchColumName" id="searchColumName" class="form-control form-control-sm">
                <option value="contsNm" <c:if test="${searchColumn eq 'contsNm'}">selected</c:if>><spring:message code="template.column.tmplnm"/></option><!-- 템플릿명 -->
                <option value="kakaoTmplCd" <c:if test="${searchColumn eq 'kakaoTmplCd'}">selected</c:if>><spring:message code="template.column.tmplcd"/></option><!-- 템플릿 코드 -->
                <option value="contsTxt" <c:if test="${searchColumn eq 'contsTxt'}">selected</c:if>><spring:message code="template.column.tmpltxt"/></option><!-- 템플릿 코드 -->
            </select>
            <input type="search" id="searchValue" name="searchValue" maxlength="30" class="form-control form-control-sm" value="${param.searchWord}"/>
            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                <spring:message code="button.search"/><!-- 검색 -->
            </button>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-12 pl-0">
        <div class="table-responsive overflow-x-hidden">
            <table class="table table-sm dataTable table-hover table-fixed" id="kakaoContentTable">
              <colgroup>
                    <col width="6%" />
                    <col width="15%" />
                    <col width="20%" />
                    <col width="*" />
                    <col width="10%" />
                    <col width="10%" />
                    <col width="10%" />
                    <col width="6%" />
                </colgroup>
                <thead class="thead-light">
                <tr>
                    <th scope="col" class="th-center">No</th>
                    <th scope="col"><spring:message code="template.column.yellowid" /></th><!-- 카카오톡 채널ID -->
                    <th scope="col"><spring:message code="template.column.tmplcd" /></th><!-- 템플릿코드-->
                    <th scope="col"><spring:message code="template.menu.mcname" /></th><!-- 템플릿명 -->
                    <th scope="col"><spring:message code="template.menu.message.type" /></th><!-- 메시지유형-->
                    <th scope="col"><spring:message code="template.menu.emphasize.text" /></th><!-- 강조표기 -->
                    <th scope="col"><spring:message code="template.menu.secure.template" /></th><!-- 보안템플릿 -->
                    <th scope="col"><spring:message code="common.detail" /></th><!-- 상세내용 -->
                </tr>
                </thead>
                <tbody>
                    <c:forEach var="kakaoContent" items="${kakaoContentsList}" varStatus="status">
                    <tr style="cursor: pointer;" data-index="${status.index}" data-contsno="${kakaoContent.contsNo}"
                           data-image="${kakaoContent.filePreviewPath}"  class="choice">

                        <input type="hidden" name="contsTxt[${status.index}]" value="${kakaoContent.contsTxt}" >
                        <input type="hidden" name="kakaoSenderKey[${status.index}]" value="${kakaoContent.kakaoSenderKey}">
                        <input type="hidden" name="kakaoTmplCd[${status.index}]" value="${kakaoContent.kakaoTmplCd}">
                        <textarea style="display: none;" name="kakaoButtons[${status.index}]">${wiseu:convertJsonToHtmlTrElement(kakaoContent.kakaoButtons)}</textarea>
                        <textarea style="display: none;" name="kakaoQuickReplies[${status.index}]">${wiseu:convertJsonToHtmlTrElement(kakaoContent.kakaoQuickReplies)}</textarea>
                        <td>${kakaoContent.contsNo}</td>
                        <td class="text-left">${kakaoContent.kakaoYellowId}</td>
                        <td class="text-left text-truncate text-nowrap">${kakaoContent.kakaoTmplCd}</td>
                        <td class="text-left text-truncate text-nowrap" title="${kakaoContent.contsNm}">${kakaoContent.contsNm}</td>
                        <td class="text-left">
                            <c:if test="${kakaoContent.kakaoTmplMsgType eq 'BA'}">
                                <spring:message code="template.menu.basic.template"/>
                            </c:if>
                            <c:if test="${kakaoContent.kakaoTmplMsgType eq 'EX'}">
                                <spring:message code="template.menu.extra.template"/>
                            </c:if>
                            <c:if test="${kakaoContent.kakaoTmplMsgType eq 'AD'}">
                                <spring:message code="template.menu.ad.template"/>
                            </c:if>
                            <c:if test="${kakaoContent.kakaoTmplMsgType eq 'MI'}">
                                <spring:message code="template.menu.mix.template"/>
                            </c:if>
                        </td>
                        <td class="text-left">
                            <c:if test="${kakaoContent.kakaoEmType eq 'NONE'}">
                                <spring:message code="template.menu.emphasize.none"/>
                            </c:if>
                            <c:if test="${kakaoContent.kakaoEmType eq 'TEXT'}">
                                <spring:message code="template.menu.emphasize.text"/>
                            </c:if>
                            <c:if test="${kakaoContent.kakaoEmType eq 'IMAGE'}">
                                <spring:message code="template.menu.emphasize.image"/>
                            </c:if>
                        </td>
                        <td class="text-left">
                            <c:if test="${kakaoContent.kakaoSecurityYn eq 'Y'}">
                                <spring:message code="template.menu.secure.template"/>
                            </c:if>
                            <c:if test="${kakaoContent.kakaoSecurityYn eq 'N'}">
                                <spring:message code="template.menu.general.template"/>
                            </c:if>
                        </td>
                        <td onclick="event.cancelBubble=true" class="detailBtn" data-contsno="${kakaoContent.contsNo}"><i class="fas fa-search"></i></td>
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
            <c:param name="hiddenParam" value="kakaoInspStatus:${param.kakaoInspStatus}"/>
            <c:param name="hiddenParam" value="contsTxt:${param.contsTxt}"/>
            <c:param name="hiddenParam" value="subType:${param.subType}"/>
            <c:param name="returnDocumentId" value="tmplWrap"/>
            <c:param name="hiddenParam" value="searchColumn:${searchColumn}" />
            <c:param name="hiddenParam" value="searchWord:${searchWord}" />
        </c:import>
    </div>
</div>
