package com.mnwise.wiseu.web.env.model;

public class TreeNode {
    /** 노드유형 : 루트노드 */
    public static final String NODE_TYPE_ROOT = "ROOT";
    /** 노드유형 : 중간노드 */
    public static final String NODE_TYPE_INTERNAL = "INTERNAL";
    /** 노드유형 : 말단노드 */
    public static final String NODE_TYPE_LEAF = "LEAF";

    /* 노드ID */
    private String id;

    /** 노드명 */
    private String text;

    /** 노드상태 */
    private TreeNodeState state;

    /** 부모노드 */
    private String parent;

    /** type */
    private String type;

    //private ArrayList<TreeNode> children;
    private boolean children;

    public TreeNode() {

    }

    public TreeNode(String id, String text, String parent) {
        this.id = id;
        this.text = text;
        this.parent= parent;
        this.type= parent.equals("#")? NODE_TYPE_ROOT : NODE_TYPE_INTERNAL;
        this.state = new TreeNodeState();
    }

    public TreeNode(String id, String text, String parent,TreeNodeState state) {
        this.id = id;
        this.text = text;
        this.parent= parent;
        this.type= parent.equals("#")? NODE_TYPE_ROOT : NODE_TYPE_INTERNAL;
        this.state = new TreeNodeState();
    }

    public TreeNode(String id, String text, String parent, String hasChildren, TreeNodeState state) {
        this.id = id;
        this.text = text;
        this.parent= parent;
        this.type= parent.equals("#")? NODE_TYPE_ROOT : NODE_TYPE_INTERNAL;
        this.state = state;
        this.children= hasChildren.equals("true")? true: false;
    }

    public boolean isChildren() {
        return children;
    }

    public void setChildren(boolean children) {
        this.children = children;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TreeNodeState getState() {
        return state;
    }

    public void setState(TreeNodeState state) {
        this.state = state;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }



}
