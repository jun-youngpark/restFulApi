package com.mnwise.wiseu.web.editor.model;

public class HandlerVo {
    private int no;
    private int seq;
    private String serviceType;
    private String handler;
    private String defaultYn;
    private String handlerDesc;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDefaultYn() {
        return defaultYn;
    }

    public void setDefaultYn(String defaultYn) {
        this.defaultYn = defaultYn;
    }

    public String getHandlerDesc() {
        return handlerDesc;
    }

    public void setHandlerDesc(String handlerDesc) {
        this.handlerDesc = handlerDesc;
    }
}
