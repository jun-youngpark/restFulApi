package com.mnwise.wiseu.web.editor.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class SmsVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    String contsTxt;
    String contsNm;
    String createDt;
    String createTm;

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getCreateTm() {
        return createTm;
    }

    public void setCreateTm(String createTm) {
        this.createTm = createTm;
    }

    public String getContsNm() {
        return contsNm;
    }

    public void setContsNm(String contsNm) {
        this.contsNm = contsNm;
    }

    public String getContsTxt() {
        return contsTxt;
    }

    public void setContsTxt(String contsTxt) {
        this.contsTxt = contsTxt;
    }

}
