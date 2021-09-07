package com.mnwise.wiseu.web.template.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

import static com.mnwise.wiseu.web.template.util.KakaoButtonUtils.APP_LINK;
import static com.mnwise.wiseu.web.template.util.KakaoButtonUtils.WEB_LINK;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoButton implements Mappable {
    private int ordering;
    private String name;
    private String linkType;
    private String linkTypeName;
    private String linkMo;
    private String linkPc;
    private String linkIos;
    private String linkAnd;

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkTypeName() {
        return linkTypeName;
    }

    public void setLinkTypeName(String linkTypeName) {
        this.linkTypeName = linkTypeName;
    }

    public String getLinkMo() {
        return linkMo;
    }

    public void setLinkMo(String linkMo) {
        this.linkMo = linkMo;
    }

    public String getLinkPc() {
        return linkPc;
    }

    public void setLinkPc(String linkPc) {
        this.linkPc = linkPc;
    }

    public String getLinkIos() {
        return linkIos;
    }

    public void setLinkIos(String linkIos) {
        this.linkIos = linkIos;
    }

    public String getLinkAnd() {
        return linkAnd;
    }

    public void setLinkAnd(String linkAnd) {
        this.linkAnd = linkAnd;
    }

    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"ordering\":").append(ordering).append(",");
        sb.append("\"name\":\"").append(name).append("\",");
        sb.append("\"linkType\":\"").append(linkType).append("\",");
        sb.append("\"linkTypeName\":\"").append(linkTypeName).append("\",");
        sb.append("\"linkMo\":\"").append(linkMo).append("\",");
        sb.append("\"linkPc\":\"").append(linkPc).append("\",");
        sb.append("\"linkIos\":\"").append(linkIos).append("\",");
        sb.append("\"linkAnd\":\"").append(linkAnd).append("\"");
        sb.append("}");

        return sb.toString().replaceAll("\"null\"", "null");
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        int index = ordering - 1;
        map.put("buttons[" + index + "].ordering", String.valueOf(ordering));
        map.put("buttons[" + index + "].name", name);
        map.put("buttons[" + index + "].linkType", linkType);

        if (WEB_LINK.equals(linkType)) {
            map.put("buttons[" + index + "].linkMo", linkMo);

            if (linkPc != null) {
                map.put("buttons[" + index + "].linkPc", linkPc);
            }
        } else if (APP_LINK.equals(linkType)) {
            if (linkIos != null) {
                map.put("buttons[" + index + "].linkIos", linkIos);
            }
            if (linkAnd != null) {
                map.put("buttons[" + index + "].linkAnd", linkAnd);
            }
            if (linkMo != null) {
                map.put("buttons[" + index + "].linkMo", linkMo);
            }
            if (linkPc != null) {
                map.put("buttons[" + index + "].linkPc", linkPc);
            }
        }

        return map;
    }
}
