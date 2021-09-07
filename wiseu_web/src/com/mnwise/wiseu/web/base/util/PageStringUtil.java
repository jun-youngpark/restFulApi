package com.mnwise.wiseu.web.base.util;

import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 페이징 유틸 클래스
 */
public class PageStringUtil {
    private static final Logger log = LoggerFactory.getLogger(PageStringUtil.class);

    private static String newLine="\n";

    /**
     * 총 레크드 갯수와 링크주소를 받아서 페이징 태그를 만들어서 출력합니다.
     *
     * @param request HttpServletRequest
     * @param pageVariableName 선택 페이지 파라미터 명
     * @param currentPage 선택 페이지
     * @param pageSize 페이지 수
     * @param totalRowCnt 전체 데이터 수
     * @return
     * @throws Exception
     */
    public static String printPaging(HttpServletRequest request, String pageVariableName, int currentPage, int pageSize, int totalRowCnt) throws Exception {
        PageStringBean pgBean = new PageStringBean();
        pgBean.setCurrentPage(currentPage);
        pgBean.setPageSize(pageSize);
        pgBean.setTotalRowCnt(totalRowCnt);
        return printPaging(request, pageVariableName, pgBean);
    }

    /**
     * 총 레크드 갯수와 링크주소를 받아서 페이징 태그를 만들어서 출력합니다.
     *
     * @param request HttpServletRequest 객체
     * @param pageVariableName page no를 넘길 변수명
     * @param pageBean page 계산을 위한 파라미터 객체
     * @return
     * @throws Exception
     */
    public static String printPaging(HttpServletRequest request, String pageVariableName, PageStringBean pageBean) throws Exception {

        int currentPage = 1;
        String link = request.getContextPath() + request.getServletPath();

        Enumeration<String> enumeration = request.getParameterNames();

        StringBuilder sbLink = new StringBuilder(200);
        sbLink.insert(0, "?");

        while(enumeration.hasMoreElements()) {
            String tmp = enumeration.nextElement();
            String as[] = request.getParameterValues(tmp);
            for(int i = 0; i < as.length; i++) {

                if(tmp.equalsIgnoreCase(pageVariableName)) {
                    currentPage = as[i].equals("") ? 1 : Integer.parseInt(as[i]);
                } else {
                    // 파라미터에서 제외해야 하는 경우 처리 (예:성공, 실패 message 파라미터)
                    if(pageBean.getNotUseParameter().indexOf(tmp) > -1) {
                        continue;
                    }
                    sbLink.append(tmp).append("=").append(URLEncoder.encode(as[i], "UTF-8")).append("&amp;");
                }
            }
        }

        // 마지막에 페이지 번호를 넣는다.
        sbLink.append(pageVariableName + "=");

        StringBuffer pageString = new StringBuffer();

        try {

            int totalPageCount = pageBean.getTotalRowCnt() % pageBean.getPageSize();
            if(totalPageCount == 0) {
                totalPageCount = pageBean.getTotalRowCnt() / pageBean.getPageSize();
            } else {
                totalPageCount = pageBean.getTotalRowCnt() / pageBean.getPageSize() + 1;
            }

            // 전체 페이지 블럭수
            int totalBlockCount = 1;

            // 현재 페이지 블럭
            int currentBlock = 1;

            if(totalPageCount % pageBean.getBlockSize() == 0) {
                totalBlockCount = totalPageCount / pageBean.getBlockSize();
            } else {
                totalBlockCount = totalPageCount / pageBean.getBlockSize() + 1;
            }

            if(currentPage % pageBean.getBlockSize() == 0) {
                currentBlock = currentPage / pageBean.getBlockSize();
            } else {
                currentBlock = currentPage / pageBean.getBlockSize() + 1;
            }

            // 최신 페이지 링크
            String firstPageLink = link + sbLink.toString() + 1;
            pageString.append("<div class=\"pagination pagination-sm mb-0 pb-2 pt-1 justify-content-center\">");
            pageString.append(newLine);
            pageString.append("<ul class=\"pagination\">");
            pageString.append(newLine);
            pageString.append("<li class=\"paginate_button page-item previous\" id=\"datatable-basic_previous\"><a href=\""+firstPageLink+"\" aria-controls=\"datatable-basic\" data-dt-idx=\"0\" tabindex=\"0\" class=\"page-link\"><i class=\"fas fa-angle-double-left\"></i></a></li>");
            pageString.append(newLine);

            // 이전 페이지 링크
            String prevPageLink;
            if(currentBlock > 1) {
                prevPageLink = link + sbLink.toString() + ((currentBlock - 1) * pageBean.getBlockSize() - 9);
            } else {
                prevPageLink = link + sbLink.toString() + "#";
            }
            pageString.append("  <li class=\"paginate_button page-item previous\" id=\"datatable-basic_previous\"><a href=\""+prevPageLink+"\" aria-controls=\"datatable-basic\" data-dt-idx=\"0\" tabindex=\"0\" class=\"page-link\"><i class=\"fas fa-angle-left\"></i></a></li>");
            pageString.append(newLine);

            // 현재블럭에 해당하는 페이지 출력 디폴트 10개
            int i = 1;
            int j = 1;
            int pageCnt = 0;
            for(i = ((currentBlock - 1) * pageBean.getBlockSize()) + 1; i <= totalPageCount; i++) {

                if(i == currentPage) {
                    pageString.append("<li class=\"paginate_button page-item active\"><a href=\"#\" aria-controls=\"datatable-basic\" data-dt-idx=\"0\" tabindex=\"0\" class=\"page-link\">"+ i +"</a></li>");
                    pageString.append(newLine);
                } else {
                    pageString.append("<li class=\"paginate_button page-item\"><a href='" + link + sbLink.toString() + i + "' aria-controls=\"datatable-basic\" data-dt-idx=\"0\" tabindex=\"0\" class=\"page-link\">"+ i +"</a></li>");
                    pageString.append(newLine);
                }

                if(j == pageBean.getBlockSize()) {
                    break;
                } else {
                    j++;
                }
                pageCnt++;
            }

            if(pageCnt == 0) {
                    pageString.append("<li class=\"paginate_button page-item active\"><a href=\"#\" aria-controls=\"datatable-basic\" data-dt-idx=\"0\" tabindex=\"0\" class=\"page-link\">1</a></li>");
                    pageString.append(newLine);
            }

            // 다음 페이지 링크
            String nextPageLink = "#";

            if(totalBlockCount > 1) {
                if(currentBlock < totalBlockCount) {
                    nextPageLink = link + sbLink.toString() + ((currentBlock * pageBean.getBlockSize()) + 1);
                }
            } else {
                int nextPageNum = currentPage+1;
                if(totalPageCount>1 && nextPageNum <= totalPageCount ) {
                    nextPageLink = link + sbLink.toString() + (nextPageNum);
                }else {
                    nextPageLink = "#";
                }
            }

            pageString.append("<li class=\"paginate_button page-item next\" id=\"datatable-basic_next\"><a href='" + nextPageLink + "' aria-controls=\"datatable-basic\" data-dt-idx=\"0\" tabindex=\"0\" class=\"page-link\"><i class=\"fas fa-angle-right\"></i></a></li>");
            pageString.append(newLine);
            // 끝 페이지 링크
            String endPageLink = "#";
            totalPageCount = totalPageCount == 0 ? 1 : totalPageCount;  //총 페이지 수가 0인 경우 1로 치환
            endPageLink = link + sbLink.toString() + totalPageCount ;

            pageString.append("<li class=\"paginate_button page-item next\" id=\"datatable-basic_next\"><a href='" + endPageLink + "' aria-controls=\"datatable-basic\" data-dt-idx=\"0\" tabindex=\"0\" class=\"page-link\"><i class=\"fas fa-angle-double-right\"></i></a></li>");
            pageString.append(newLine);
            pageString.append("</ul>");
            pageString.append(newLine);
            pageString.append("</div>");
            pageString.append(newLine);

        } catch(Exception e) {
            log.error("Exception occurred. " + e.getMessage());
        }

        return pageString.toString();
    }
}
