package com.mnwise.wiseu.web.template.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mnwise.wiseu.web.template.model.Mappable;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import com.mnwise.common.util.JsonUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.template.model.KakaoButton;

@Component
public class KakaoButtonUtils {
    private static final Logger log = LoggerFactory.getLogger(KakaoButtonUtils.class);

    public static final String APP_LINK = "AL";
    public static final String WEB_LINK = "WL";
    public static final String ADD_CHANNEL = "AC";

    private static MessageSourceAccessor messageSourceAccessor;
    private static WiseuLocaleChangeInterceptor localeChangeInterceptor;

    private KakaoButtonUtils() {
    }

    @Autowired
    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        KakaoButtonUtils.messageSourceAccessor = messageSourceAccessor;
    }

    @Autowired
    public void setLocaleChangeInterceptor(WiseuLocaleChangeInterceptor localeChangeInterceptor) {
        KakaoButtonUtils.localeChangeInterceptor = localeChangeInterceptor;
    }

    public static String convertJsonToHtmlTrElement(String json) throws Exception {
        if(StringUtil.isBlank(json)) {
            return "";
        }

        @SuppressWarnings("unchecked")
        List<KakaoButton> buttonList = JsonUtil.toList(json, KakaoButton.class);

        Locale locale = localeChangeInterceptor.getLocale();

        StringBuilder sb = new StringBuilder();
        for(KakaoButton button : buttonList) {
            sb.append("<tr id=\"kakaoButtonList\" class=\"kakaoButtonList\">");
            String linkType = button.getLinkType();
            String rowspan= APP_LINK.equals(linkType)?" rowspan=\"2\"":"";
            sb.append(" <td class=\"ls--1px\""+rowspan+"> ");
            //링크타입 추가 시 마다 message resource 추가 필요
            sb.append(messageSourceAccessor.getMessage("template.kakao.link.type." + linkType, null, linkType , locale));
            sb.append("    <input data=\"linkType\" id=\"linkType\" type=\"hidden\" class=\"form-control form-control-sm\"  value=\"").append(linkType).append("\" readonly />");
            sb.append("  </td>");
            sb.append("  <th"+rowspan+" > ");
            sb.append(messageSourceAccessor.getMessage("template.kakao.button.name", locale));
            sb.append("  </th>");
            sb.append("  <td"+rowspan+" > ");
            sb.append("    <input data=\"name\" id=\"name\" type=\"text\" class=\"form-control form-control-sm\" value=\"").append(button.getName()).append("\" readonly />");
            sb.append("  </td>");
            if(APP_LINK.equals(linkType)) {
                sb.append("<th>");
                sb.append(messageSourceAccessor.getMessage("template.kakao.link.ios", locale));
                sb.append("</th>");
                sb.append("<td>");
                sb.append("  <input data=\"linkIos\" id=\"linkIos\" class=\"form-control form-control-sm\" type=\"text\" value=\"").append(button.getLinkIos()).append("\" readonly />");
                sb.append("</td>");
                sb.append("<th>");
                sb.append(messageSourceAccessor.getMessage("template.kakao.link.android", locale));
                sb.append("</th>");
                sb.append("<td>");
                sb.append("  <input data=\"linkAnd\" id=\"linkAnd\" class=\"form-control form-control-sm\" type=\"text\" value=\"").append(button.getLinkAnd()).append("\" readonly />");
                sb.append("</td>");
                sb.append("</tr>");
                sb.append("<tr id=\"kakaoButtonSubList\" class=\"kakaoButtonList\">");
                sb.append("<th>");
                sb.append(messageSourceAccessor.getMessage("template.kakao.link.mobile", locale));
                sb.append("</th>");
                sb.append("<td>");
                sb.append("  <input data=\"linkMo\" id=\"linkMo\" class=\"form-control form-control-sm\" type=\"text\" value=\"").append(button.getLinkMo()).append("\" readonly />");
                sb.append("</td>");
                sb.append("<th>");
                sb.append(messageSourceAccessor.getMessage("template.kakao.link.pc", locale));
                sb.append("</th>");
                sb.append("<td>");
                sb.append("  <input data=\"linkPc\"  id=\"linkPc\" type=\"text\" class=\"form-control form-control-sm\" value=\"").append(button.getLinkPc()).append("\" readonly />");
                sb.append("</td>");
            }
            else if(WEB_LINK.equals(linkType)) {
                sb.append("<th>");
                sb.append(messageSourceAccessor.getMessage("template.kakao.link.mobile", locale));
                sb.append("</th>");
                sb.append("<td>");
                sb.append("  <input  data=\"linkMo\" id=\"linkMo\" class=\"form-control form-control-sm\" type=\"text\" value=\"").append(button.getLinkMo()).append("\" readonly />");
                sb.append("</td>");
                sb.append("<th>");
                sb.append(messageSourceAccessor.getMessage("template.kakao.link.pc", locale));
                sb.append("</th>");
                sb.append("<td>");
                sb.append("  <input  data=\"linkPc\" id=\"linkPc\" type=\"text\" class=\"form-control form-control-sm\" value=\"").append(button.getLinkPc()).append("\" readonly />");
                sb.append("</td>");
            }else {
                sb.append("<td colspan=\"10\"> </td>");
            }
            sb.append("</tr>");
        }
        return sb.toString();
    }

    public static String getColspan(String linkType) {
        if(WEB_LINK.equals(linkType) || APP_LINK.equals(linkType)) {
            return "1";
        } else {
            return "5";
        }
    }

    public static List<NameValuePair> convertButtonToHttpParamList(List<Mappable> mappableList) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (Mappable mappable : mappableList) {
            mapList.add(mappable.toMap());
        }

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        return nameValuePairs;
    }

    @SuppressWarnings("unchecked")
    public static List<KakaoButton> convertJsonToKakaoButtonList(String json) {
        if(StringUtil.isBlank(json)) {
            return new ArrayList<>();
        } else {
            try {
                return JsonUtil.toList(json, KakaoButton.class);
            } catch(Exception e) {
                log.error(e.getMessage(), e);
                return new ArrayList<>();
            }
        }
    }
}
