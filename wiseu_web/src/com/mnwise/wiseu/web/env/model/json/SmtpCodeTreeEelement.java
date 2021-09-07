
package com.mnwise.wiseu.web.env.model.json;

import com.mysql.jdbc.StringUtils;

public class SmtpCodeTreeEelement {

    private String text;
    private String categoryCd;
    private String id;
    private String categoryNm;
    private String categoryDesc;
    private String expanded;
    private String classes;
    private String activeYn;
    private String hasChildren;
    private String parentId;    //부모 아이디

    public String getParentId() {
        if(org.apache.commons.lang3.StringUtils.isBlank(parentId)) {
            return "#";
        }else {
            return parentId;
        }
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCategoryCd() {
        return categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getCategoryNm() {
        return categoryNm;
    }

    public void setCategoryNm(String categoryNm) {
        this.categoryNm = categoryNm;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getExpanded() {
        return expanded;
    }

    public void setExpanded(String expanded) {
        this.expanded = expanded;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getActiveYn() {
        return activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
