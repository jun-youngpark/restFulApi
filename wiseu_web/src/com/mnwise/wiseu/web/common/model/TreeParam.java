package com.mnwise.wiseu.web.common.model;

public class TreeParam {
    /** 노드ID */
    private String nodeId;
    /** 부모노드ID */
    private String parentId;
    /** 루트노드ID */
    private String rootId;
    /** 노드명 */
    private String nodeName;
    /** 노드유형 */
    private String nodeType;
    /** 정렬순서 */
    private int sortOrder;
    /** 검색어 */
    private String searchKeyword;

    private boolean loadRoot;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public boolean isLoadRoot() {
        return loadRoot;
    }

    public void setLoadRoot(boolean loadRoot) {
        this.loadRoot = loadRoot;
    }

    /**
     * 모든 멤버변수에 대해 name = value 형태의 문자열을 가져온다.
     *
     * @return 모든 멤버변수에 대한 name = value 형태의 문자열
     */
    public String toString() {
        StringBuffer objectValue = new StringBuffer();

        objectValue.append("TreeParam (")
            .append(super.toString())
            .append(", nodeId=").append(this.nodeId)
            .append(", parentId=").append(this.parentId)
            .append(", rootId=").append(this.rootId)
            .append(", nodeName=").append(this.nodeName)
            .append(", nodeType=").append(this.nodeType)
            .append(", sortOrder=").append(this.sortOrder)
            .append(", searchKeyword=").append(this.searchKeyword)
            .append(", loadRoot=").append(this.loadRoot)
            .append(" )");

        return objectValue.toString();
    }
}
