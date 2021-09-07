package com.mnwise.wiseu.web.ecare.model;

/**
 *
 * 발송취소 클래스
 *
 */
public class CancelVo {
    private String canReqDtm;    // 발송취소요청일시
    private String canSeq;       // 발송취소요청 일련번호 
    private String orgSeq;       // 원 SEQ
    private String orgReqDt;     // 원 요청일자
    private int ecareNo;         // 이케어번호
    private String canFg;        // 취소요청 상태코드
    private String canDtm;       // 취소요청일시
    private String acceptDtm;    // 취소접수일시
    private String rsltCd;       // 취소 결과코드
    private String rsltMsg;      // 취소 결과메시지
    
    private String searchQstartDt;  // 검색을 위한 시작일자
    private String searchQendDt;    // 검색을 위한 종료일자
    private int listCountPerPage;   // 페이지당 출력할 리스트 개수
    private int nowPage;            // 현재 페이지
    
    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }
    public int getNowPage() {
        return nowPage;
    }
    public void setListCountPerPage(int listCountPerPage) {
        this.listCountPerPage = listCountPerPage;
    }
    public int getListCountPerPage() {
        return listCountPerPage;
    }
    public String getCanReqDtm() {
        return canReqDtm;
    }
    public void setCanReqDtm(String canReqDtm) {
        this.canReqDtm = canReqDtm;
    }
    public String getCanSeq() {
        return canSeq;
    }
    public void setCanSeq(String canSeq) {
        this.canSeq = canSeq;
    }
    public String getOrgSeq() {
        return orgSeq;
    }
    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }
    public String getOrgReqDt() {
        return orgReqDt;
    }
    public void setOrgReqDt(String orgReqDt) {
        this.orgReqDt = orgReqDt;
    }
    public int getEcareNo() {
        return ecareNo;
    }
    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }
    public String getCanFg() {
        return canFg;
    }
    public void setCanFg(String canFg) {
        this.canFg = canFg;
    }
    public String getCanDtm() {
        return canDtm;
    }
    public void setCanDtm(String canDtm) {
        this.canDtm = canDtm;
    }
    public String getAcceptDtm() {
        return acceptDtm;
    }
    public void setAcceptDtm(String acceptDtm) {
        this.acceptDtm = acceptDtm;
    }
    public String getRsltCd() {
        return rsltCd;
    }
    public void setRsltCd(String rsltCd) {
        this.rsltCd = rsltCd;
    }
    public String getRsltMsg() {
        return rsltMsg;
    }
    public void setRsltMsg(String rsltMsg) {
        this.rsltMsg = rsltMsg;
    }
    public String getSearchQstartDt() {
        return searchQstartDt;
    }
    public void setSearchQstartDt(String searchQstartDt) {
        this.searchQstartDt = searchQstartDt;
    }
    public String getSearchQendDt() {
        return searchQendDt;
    }
    public void setSearchQendDt(String searchQendDt) {
        this.searchQendDt = searchQendDt;
    }
    
    
}
