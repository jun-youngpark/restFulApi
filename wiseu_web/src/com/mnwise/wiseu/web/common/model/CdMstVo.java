package com.mnwise.wiseu.web.common.model;

import java.io.Serializable;

/**
 * NV_CD_MST 테이블 모델 클래스
 */
public class CdMstVo implements Serializable {
    private static final long serialVersionUID = -1L;

    private String cdCat;
    private String cd;
    private String lang;
    private String parCdCat;
    private String val;
    private String cdDesc;
    private String useCol;
    private String useYn;
    private String regDtm;
    private String modDtm;
    private int cdOrd;

    // 기본 getter/setter
    public String getCdCat() {
        return this.cdCat;
    }

    public void setCdCat(String cdCat) {
        this.cdCat = cdCat;
    }

    public String getCd() {
        return this.cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getParCdCat() {
        return this.parCdCat;
    }

    public void setParCdCat(String parCdCat) {
        this.parCdCat = parCdCat;
    }

    public String getVal() {
        return this.val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getCdDesc() {
        return this.cdDesc;
    }

    public void setCdDesc(String cdDesc) {
        this.cdDesc = cdDesc;
    }

    public String getUseCol() {
        return this.useCol;
    }

    public void setUseCol(String useCol) {
        this.useCol = useCol;
    }

    public String getUseYn() {
        return this.useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getRegDtm() {
        return this.regDtm;
    }

    public void setRegDtm(String regDtm) {
        this.regDtm = regDtm;
    }

    public String getModDtm() {
        return this.modDtm;
    }

    public void setModDtm(String modDtm) {
        this.modDtm = modDtm;
    }

    public int getCdOrd() {
        return this.cdOrd;
    }

    public void setCdOrd(int cdOrd) {
        this.cdOrd = cdOrd;
    }
}
