package com.mnwise.wiseu.web.ecare.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * @Title : 메시지 발송 주기 코드
 * @Description :
 * @Copyright : Copyright (c) 2008
 * @cCompany : 엠앤와이즈
 * @author tosecret
 * @version 1.0
 */
public class SendCycleVo extends BaseVo {
    /**
     * 
     */
    private static final long serialVersionUID = 5606390555292121257L;

    private String cycleCd;

    private String cycleNm;

    public String getCycleCd() {
        return cycleCd;
    }

    public void setCycleCd(String cycleCd) {
        this.cycleCd = cycleCd;
    }

    public String getCycleNm() {
        return cycleNm;
    }

    public void setCycleNm(String cycleNm) {
        this.cycleNm = cycleNm;
    }
}
