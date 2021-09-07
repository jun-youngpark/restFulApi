<%-------------------------------------------------------------------------------------------------
 * - [공통팝업>불러오기] 링크추적 (팝업)<br/>
 * - URL : /editor/linkPopup?type=linktrace.do <br/>
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorLinkForwardController
 * Title       : 캠페인, 이케어 저작기 링크트레이스 설정
 * - 이전 파일명 : linktrace_editor.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="blank">
<title><spring:message code="editor.link.tracelist"/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    var template = ""; // 링크추적 적용된 템플릿을 담을 변수
    var no = opener.$("#editorFrm input[name=no]").val();
    var linktraceScript = "";
    var rejectScript = "";
    var linktrace = "";

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            onClickSaveBtn();
        });
    }

    function initPage() {
        if($.mdf.isBlank(opener.$("#editorFrm input[name=serviceType]").val())) {
            linktraceScript = "{$=(LinkTrace)$}";
            rejectScript = "{$=(Reject)$}";
        } else {
            linktraceScript = "<\%=(LinkTrace)%>";
            rejectScript = "<\%=(Reject)%>";
        }

        if($.mdf.isBlank(opener.$("#editorFrm input[name=serviceType]").val())) { // 캠페인
            if('${templateType}' == "ab" && opener.frameElement.contentWindow.templateB == true) {
                template = window.opener.tinyMCE.get("templateAb").getContent();
                linktrace = window.opener.linktraceAb;
            } else {
                template = window.opener.tinyMCE.get("template").getContent();
                linktrace = window.opener.linktrace;
            }
        } else { // 이케어
            template = window.opener.$("#editorFrm textarea[name=textmode]").val();
            linktrace = window.opener.linktrace;
        }
        // 템플릿을 iframe 안에 넣는다.
        $("#linkFrm textarea[name=template]").val(template);
        $("#linkFrm").submit();
    }

    // 템플릿이 로드 되면 호출
    function getIframe() {
        selectLinktrace(); // 링크가 있는 태그를 검색

        var allChecked = true;
        $(":checkbox").each(function(i) {
            if(this.value != "on") {
                if(this.checked == false) {
                    allChecked = false;
                }
            }
        });

        if(allChecked == true) {
            $(":checkbox[name='allCheck']").attr("checked", true);
        }
    }

    // 템플릿 링크 및 추적 조회하여 목록 작성
    function selectLinktrace() {
        var url = "";
        var seq;
        if('${templateType}' == "ab" && opener.frameElement.contentWindow.templateB == true) {
           seq = opener.$("#editorFrm input[name=linkSeqAb]").val();
        } else {
           seq = opener.$("#editorFrm input[name=linkSeq]").val();
        }

        linktrace.clear();
        // A 링크 조회
        $("a", $("iframe").contents()).each(function(i) {
            url = $(this).attr("href");

            if(checkUrl(url) == true && linktrace.contains(url) == false) {
                if(url.indexOf(linktraceScript) > -1) {
                    seq = url.substring(url.indexOf("&LINK_SEQ=") + "&LINK_SEQ=".length, url.indexOf("&LINK_URL"));
                    linktrace.add(no, seq, this.innerHTML, this.nodeName, url, $(this).prop("title"), 'n');
                    insertLinklistRow(seq, true);
                } else {
                    seq = parseInt(seq) + 1;
                    linktrace.add(no, seq, this.innerHTML, this.nodeName, url, $(this).prop("title"), 'n');
                    insertLinklistRow(seq, false);
                }
            }
        });

        // AREA 링크 조회
        $("area", $("iframe").contents()).each(function(i) {
            url = $(this).attr("href");

            if(checkUrl(url) == true && linktrace.contains(url) == false) {
                if(url.indexOf(linktraceScript) > -1) {
                    seq = url.substring(url.indexOf("&LINK_SEQ=") + "&LINK_SEQ=".length, url.indexOf("&LINK_URL"));
                    linktrace.add(no, seq, this.innerHTML, this.nodeName, url, $(this).attr("title"), 'n');
                    insertLinklistRow(seq, true);
                } else {
                    seq = parseInt(seq) + 1;
                    linktrace.add(no, seq, this.innerHTML, this.nodeName, url, $(this).attr("title"), 'n');
                    insertLinklistRow(seq, false);
                }
            }
        });

        if(linktrace.size() == 0) {
            var html = '<tr><td colspan="5"><spring:message code="editor.alert.link.msg1"/></td></tr>';  // 링크추적 항목이 없습니다.
            $("#link > tbody").append(html);
        }
    }

    // 수신거부 링크와 mailto는 링크추적 항목에서 제외
    function checkUrl(url) {
        if(url.indexOf(rejectScript) > -1) return false;
        else if(url.indexOf("mailto:") > -1) return false;
        return true;
    }

    // 조회된 링크 목록 생성
    var cnt = 1;
    function insertLinklistRow(seq, linkYn) {
        var html = "<tr>"
            + "<th scope='row'>";
            + "<div class='custom-control custom-checkbox'>";

        if(linkYn == true) {
            html += "<input type='checkbox' name='linkCheck' id='linkCheck_"+seq+"' value='" + linktrace.getLink(seq).linkSeq + "' checked  class='custom-control-input' />" + cnt;
        } else {
            html += "<input type='checkbox' name='linkCheck' id='linkCheck_"+seq+"' value='" + linktrace.getLink(seq).linkSeq + "' class='custom-control-input'/>" + cnt;
        }
        html += "<label class='custom-control-label' for='linkCheck_"+seq+"'></label>"
        html += "</div>"
        html += "</th>"
            // 링크제목
            + "<td class='text-left'>" + linktrace.getLink(seq).linkTitle + "</td>"
            // 링크타입
            + "<td>"
            + "<input type='text' name='type' value='" + linktrace.getLink(seq).linkType + "' readonly class='form-control form-control-sm'/>"
            + "</td>"
            // 링크URL
            + "<td>"
            + "<input type='text' name='url' value='" + linktrace.getLink(seq).linkUrl + "' readonly class='form-control form-control-sm'/>"
            + "</td>"
            // 링크설명
            + "<td>"
            + "<input type='text' name='desc' value='" + linktrace.getLink(seq).linkDesc + "' readonly class='form-control form-control-sm'/>"
            + "</td>"
            + "</tr>";

        $("#link > tbody").append(html);
        cnt++;
    }

    // 링크추적 URL
    function getLinkpath(seq, url, title) {
        var linktag = linktraceScript;

        if(url.indexOf(linktag) > -1) {
            linktag = url;
        } else {
            url = encodeURIComponent(url);
            title = encodeURIComponent(title);
            linktag += '&LINK_SEQ=' + seq + '&LINK_URL=' + url + '&LINKTITLE=' + title;
        }

        return linktag;
    }

    // 링크 해제 URL
    function getUnlinkpath(url) {
        if(url.indexOf(linktraceScript) > -1) {
            var start = url.indexOf("&LINK_URL=") + "&LINK_URL=".length;
            var end = url.indexOf("&LINKTITLE=");

            if(start > -1 && end > -1) {
                url = decodeURIComponent(url.substring(start, end));
            } else {
                return null;
            }
        }

        return url;
    }

    // 링크추적을 설정, 해제 함
    function onClickSaveBtn() {
        if(confirm("<spring:message code='editor.alert.link.msg2'/>") == true) {
            template = template.split("&lt;").join("<").split("&gt;").join(">").split("&amp;").join("&").split("&quot;").join("\"");

            $(":checkbox").each(function(i) {
                // 링크 설정
                if(this.checked == true) {
                    if(this.value != "on") {
                        var seq = this.value;
                        var linkpath = getLinkpath(linktrace.getLink(seq).linkSeq, linktrace.getLink(seq).linkUrl, linktrace.getLink(seq).linkTitle);
                        var url = "";

                        linktrace.setLinkYn(seq, "y");

                        // A 링크추적 설정, iframe 의 HTML 에서 A 태그만 링크 설정
                        $("a", $("iframe").contents()).each(function(i) {
                            url = $(this).attr("href");

                            if(checkUrl(url) == true && url == linktrace.getLink(seq).linkUrl) {
                                template = template.split("href=\"" + url + "\"").join("href='" + linkpath + "'")
                                            .split("href='" + url + "'").join("href='" + linkpath + "'")
                                            .split("href=\"" + encodeURIComponent(url) + "\"").join("href='" + linkpath + "'")
                                            .split("href='" + encodeURIComponent(url) + "'").join("href='" + linkpath + "'")
                                            .split("HREF=\"" + url + "\"").join("HREF='" + linkpath + "'")
                                            .split("HREF='" + url + "'").join("HREF='" + linkpath + "'")
                                            .split("HREF=\"" + encodeURIComponent(url) + "\"").join("HREF='" + linkpath + "'")
                                            .split("HREF='" + encodeURIComponent(url) + "'").join("HREF='" + linkpath + "'");

                                /* DB 에 넣을 땐 링크추적 값을 뺀 URL 를 인서트 */
                                linktrace.setLinkUrl(seq, getUnlinkpath(url));
                            }
                        });

                        // AREA 링크추적 설정, iframe 의 HTML 에서 AREA 태그만 링크 설정
                        $("area", $("iframe").contents()).each(function(i) {
                            url = $(this).attr("href");

                            // 링크 URL 안에 개인화가 있는 문제가 있어 href="" 를 href='' 로 변환
                            if(checkUrl(url) == true && url == linktrace.getLink(seq).linkUrl) {
                                template = template.split("href=\"" + url + "\"").join("href='" + linkpath + "'")
                                            .split("href='" + url + "'").join("href='" + linkpath + "'")
                                            .split("href=\"" + encodeURIComponent(url) + "\"").join("href='" + linkpath + "'")
                                            .split("href='" + encodeURIComponent(url) + "'").join("href='" + linkpath + "'")
                                            .split("HREF=\"" + url + "\"").join("HREF='" + linkpath + "'")
                                            .split("HREF='" + url + "'").join("HREF='" + linkpath + "'")
                                            .split("HREF=\"" + encodeURIComponent(url) + "\"").join("HREF='" + linkpath + "'")
                                            .split("HREF='" + encodeURIComponent(url) + "'").join("HREF='" + linkpath + "'");

                                // DB 에 넣을 땐 링크추적 값을 뺀 URL 를 INSERT
                                linktrace.setLinkUrl(seq, getUnlinkpath(url));
                            }
                        });
                    }
                } else { // 링크 해제
                    if(this.value != "on") {
                        var seq = this.value;
                        var unlinkpath = getUnlinkpath(linktrace.getLink(seq).linkUrl);
                        var url = "";

                        linktrace.setLinkYn(seq, "n");

                        // A 링크 해제, iframe 의 HTML 에서 A 태그만 링크 해제
                        $("a", $("iframe").contents()).each(function(i) {
                            url = $(this).attr("href");

                            if(checkUrl(url) == true && url == linktrace.getLink(seq).linkUrl) {
                                template = template.split("href=\"" + url + "\"").join("href='" + unlinkpath + "'")
                                            .split("href='" + url + "'").join("href='" + unlinkpath + "'")
                                            .split("href=\"" + encodeURIComponent(url) + "\"").join("href='" + unlinkpath + "'")
                                            .split("href='" + encodeURIComponent(url) + "'").join("href='" + unlinkpath + "'")
                                            .split("HREF=\"" + url + "\"").join("HREF='" + unlinkpath + "'")
                                            .split("HREF='" + url + "'").join("HREF='" + unlinkpath + "'")
                                            .split("HREF=\"" + encodeURIComponent(url) + "\"").join("HREF='" + unlinkpath + "'")
                                            .split("HREF='" + encodeURIComponent(url) + "'").join("HREF='" + unlinkpath + "'");

                                // DB 에 넣을 땐 링크추적 값을 뺀 URL 를 인서트
                                linktrace.setLinkUrl(seq, getUnlinkpath(url));
                            }
                        });

                        // AREA 링크 해제, iframe 의 HTML 에서 AREA 태그만 링크 해제
                        $("area", $("iframe").contents()).each(function(i) {
                            url = $(this).attr("href");

                            if(checkUrl(url) == true && url == linktrace.getLink(seq).linkUrl) {
                                template = template.split("href=\"" + url + "\"").join("href='" + unlinkpath + "'")
                                            .split("href='" + url + "'").join("href='" + unlinkpath + "'")
                                            .split("href=\"" + encodeURIComponent(url) + "\"").join("href='" + unlinkpath + "'")
                                            .split("href='" + encodeURIComponent(url) + "'").join("href='" + unlinkpath + "'")
                                            .split("HREF=\"" + url + "\"").join("HREF='" + unlinkpath + "'")
                                            .split("HREF='" + url + "'").join("HREF='" + unlinkpath + "'")
                                            .split("HREF=\"" + encodeURIComponent(url) + "\"").join("HREF='" + unlinkpath + "'")
                                            .split("HREF='" + encodeURIComponent(url) + "'").join("HREF='" + unlinkpath + "'");

                                // DB 에 넣을 땐 링크추적 값을 뺀 URL 를 인서트
                                linktrace.setLinkUrl(seq, getUnlinkpath(url));
                            }
                        });
                    }
                }
            });

            var tagType = window.opener.$("#editorFrm input[name=tagType]").val();
            if(tagType== 'campaign_default') {
                if('${templateType}' == "ab" && opener.frameElement.contentWindow.templateB == true) {
                    window.opener.tinyMCE.get("templateAb").setContent(template);
                } else {
                    window.opener.tinyMCE.get("template").setContent(template);
                }
            } else {
                window.opener.$("#editorFrm textarea[name=textmode]").val(template);
            }
            self.close();
        }
    }

    // 모든 체크박스 선택
    function allCheck(obj) {
        $(":checkbox").each(function(i) {
            if(obj.checked == true) {
                if(this.value != "on") {
                    this.checked = true;
                }
            } else {
                if(this.value != "on") {
                    this.checked = false;
                }
            }
        });
    }
</script>

</head>
<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="editor.link.tracelist"/></h5></div><!-- 링크추적리스트 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>
    <form id="linkFrm" name="linkFrm" action="/editor/linkTraceList.do" method="post" target="tem" style="display: none;"><!-- /editor/linktrace_template.do -->
        <textarea rows="0" cols="0" name="template" style="display: none;"></textarea>
        <input type="hidden" id="templateType" name="templateType" value="${templateType}" />
    </form>
    <!-- 템플릿을 iframe 에 넣음 - iframe 이 아닌 태그에 템플릿을 넣으면 HTML 가 달라짐 -->
    <iframe id="tem" name="tem" frameborder="0" style="height:300px; display: none;"></iframe>

    <div class="card-body">
        <div class="alert alert-secondary mb-0" role="alert">
            <i class="fas fa-circle fa-xs"></i> <spring:message code="editor.alert.link.msg3" /><br/><!-- 중복되는 링크URL은 목록에 하나만 표시됩니다. 링크추적을 설정하면 중복된 모든 URL에 설정됩니다. -->
            <i class="fas fa-circle fa-xs"></i> <spring:message code="editor.alert.link.msg4" /><!-- 수신거부 링크와 mailto: 링크는 링크추적 항목에서 제외됩니다. -->
        </div>

        <div class="table-responsive">
            <table class="table table-sm dataTable" id="link">
                <thead class="thead-light">
                    <tr>
                        <th scope="col" width="45">
                            <div class="custom-control custom-checkbox">
                                <input type="checkbox" id="checkbox" name="allCheck" onclick="javascript:allCheck(this);" class="custom-control-input" />
                                <label class="custom-control-label" for="checkbox"></label>
                            </div>
                        </th>
                        <th scope="col"><spring:message code="editor.menu.link.lname"/></th><!-- 링크제목 -->
                        <th scope="col" width="80"><spring:message code="common.menu.type"/></th><!--  유형-->
                        <th scope="col"><spring:message code="editor.menu.link.lurl"/></th><!-- 링크 url -->
                        <th width="20%"><spring:message code="editor.menu.link.ldes"/></th><!-- 링크설명 -->
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div><!-- //Light table -->
    </div>
    <div class="card-footer">
        <button type="button" class="btn btn-outline-primary" id="saveBtn">
            <spring:message code="button.save"/><!-- 저장 -->
        </button>
    </div>
</div>
</body>
</html>