<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 전체 요약(메일) - 템플릿 보기(팝업)<br/>
 * - URL : /report/campaign/templateViewPopup.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.TemplateViewController<br/>
 *
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 링크클릭 분석(메일) - 컨텐츠 반응분석(팝업)<br/>
 * - URL : /report/campaign/templateViewPopupByLinkClick.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignLinkClickController<br/>
 * - 이전 파일명 : template_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<c:forEach var="loop" items="${campaignReportLinkClickList}">
<c:if test="${loop.gid eq 1}">
    <c:set var="totalCnt" scope="page" value="${loop.totalCnt}"/>
</c:if>
</c:forEach>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.linkclick.title.alt.2"/> : NO.${scenarioInfoVo.campaignInfoVo.campaignNo} ${scenarioInfoVo.campaignInfoVo.campaignName}</title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        $("a").removeAttr("href"); // 하이퍼링크 제거
        $("area").removeAttr("href"); // 하이퍼링크 제거
        $("link").removeAttr("href"); // 하이퍼링크 제거
        var percent = []; // 배열을 초기화 한다.
        // linkTitle html을 dom으로 만든다.
        <c:forEach var="loop" items="${campaignReportLinkClickList}">
        <c:if test="${loop.gid eq 0}">
            var link = $('${loop.linkClickTitle}');
            //이미지 링크인 경우
            if(typeof($(link).attr('src')) != 'undefined') { // IMG 태그인 경우
                //image element Array를 배열에 넣는다.
                percent.push(new Array($(link).attr('src'),
                        "${loop.totalCnt}<spring:message code='report.campaign.head.title.cnt'/>(<fmt:formatNumber type='number' value='${loop.totalCnt / totalCnt * 100}' maxFractionDigits='1' />%)", "img"));
            }

            if($(link).attr('href') != '' && typeof($(link).attr('href')) != 'undefined') { // A 태그인 경우
                //image element Array를 배열에 넣는다.
                //LINK_URL 정보를 url encode 한다
                //IE 에서 http://www.mnwise.com 과 같은 링크에서는 맨 뒤에 /가 붙여지는 관계로 아래와 같이 처리함.
                var href = $(link).attr('href');
                if(href.lastIndexOf("/") == href.length) {
                    href = href.substring(0, href.lastIndexOf("/"));
                }

                percent.push(new Array(encodeURIComponent(href),
                        "${loop.totalCnt}<spring:message code='report.campaign.head.title.cnt'/>(<fmt:formatNumber type='number' value='${loop.totalCnt / totalCnt * 100}' maxFractionDigits='1' />%)", "a"));
            }
        </c:if>
        </c:forEach>

        for(i=0; i < percent.length; i++) {
            if(percent[i][2] == "img") {
                $("img[src=" + percent[i][0] + "]").css("opacity", "0.3");
                $("img[src=" + percent[i][0] + "]").wrap("<div style='position:relative; left:0px; top:0px;' id=" + percent[i][0] + "></div>");
                $("div[id=" + percent[i][0] + "]").append("<div style='position:absolute; left:10px; top:10px; color:red; font-weight:bolder; font-size: 11px; background-color: #ffffff; border:1px solid red'>" + percent[i][1] + "</div>");
            } else {
                $("a[href*=" + percent[i][0] + "]").css("opacity", "0.3");
                $("a[href*=" + percent[i][0] + "]").wrap("<span style='position:relative; left:0px; top:0px;' id=" + percent[i][0] + "></span>");
                $("span[id=" + percent[i][0] + "]").append("<span style='position:absolute; left:0px; top:0px; color:red; font-weight:bolder; font-size: 11px; background-color: #ffffff; border:1px solid red'>" + percent[i][1] + "</span>");
            }
        }
    }
</script>
</head>
<body>
${template}
</body>
</html>