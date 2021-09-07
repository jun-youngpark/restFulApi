package com.mnwise.wiseu.web.account.model.json;

public class TreeEelement {
    private String text;
    private String id;
    private String grpDesc;
    private String expanded;
    private String classes;
    private String hasChildren;
    private String mainUser;
    private String mainUserNm;
    private String activeYn;
    private String superId;

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }
    public String getGrpDesc() {
        return grpDesc;
    }

    public void setGrpDesc(String grpDesc) {
        this.grpDesc = grpDesc;
    }

    public String getMainUserNm() {
        return mainUserNm;
    }

    public void setMainUserNm(String mainUserNm) {
        this.mainUserNm = mainUserNm;
    }

    public String getMainUser() {
        return mainUser;
    }

    public void setMainUser(String mainUser) {
        this.mainUser = mainUser;
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

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getActiveYn() {
        return activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

}