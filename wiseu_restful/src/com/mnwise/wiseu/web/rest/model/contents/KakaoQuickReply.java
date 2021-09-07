package com.mnwise.wiseu.web.rest.model.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoQuickReply implements Mappable {
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
        map.put("quickReplies[" + index + "].name", name);
        map.put("quickReplies[" + index + "].linkType", linkType);

        if ("WL".equals(linkType)) {
            map.put("quickReplies[" + index + "].linkMo", linkMo);

            if (linkPc != null) {
                map.put("quickReplies[" + index + "].linkPc", linkPc);
            }
        } else if ("AL".equals(linkType)) {
            if (linkIos != null) {
                map.put("quickReplies[" + index + "].linkIos", linkIos);
            }
            if (linkAnd != null) {
                map.put("quickReplies[" + index + "].linkAnd", linkAnd);
            }
            if (linkMo != null) {
                map.put("quickReplies[" + index + "].linkMo", linkMo);
            }
            if (linkPc != null) {
                map.put("quickReplies[" + index + "].linkPc", linkPc);
            }
        }

        return map;
    }
}
