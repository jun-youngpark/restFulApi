package com.mnwise.wiseu.web.editor.util;

import com.mnwise.common.util.StringUtil;

public class MailTemplate {

    public static String rearrange(String link) {
        if(link == null)
            return null;

        // 20151002 템플릿 내 수신확인, 링크클릭, 수신거부 테그내 특수기호 재변환
        link = StringUtil.unescapeXss(link);

        StringBuffer sb = new StringBuffer();
        String[] parameters = link.split("&");
        String key = null;
        String value = null;

        for(int i = 0; i < parameters.length; i++) {
            key = parameters[i].split("=")[0];
            value = parameters[i].split("=")[1];

            if(i != 0)
                sb.append("\r\nsb.append(\"&");
            else
                sb.append("sb.append(\"");

            sb.append(key).append("=\")\r\nsb.append(").append(value).append(")");
        }

        return sb.toString();
    }
}
