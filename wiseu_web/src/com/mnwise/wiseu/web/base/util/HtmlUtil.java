package com.mnwise.wiseu.web.base.util;

public class HtmlUtil {

    /**
     * 템플릿의 htmlCode를 부등호로 치환하여 반환한다.
     *
     * @param template 템플릿
     * @return 치환이 끝난 문자열
     */
    public static String htmlCodeConverter(String template) {
        template = template.replaceAll("&nbsp;", " ");
        template = template.replaceAll("&lt;", "<");
        template = template.replaceAll("&gt;", ">");
        template = template.replaceAll("&amp;", "&");
        template = template.replaceAll("&quot;", "\"");
        template = template.replaceAll("&middot;", "·");
        template = template.replaceAll("&hellip;", "…");
        return template;
    }

}
