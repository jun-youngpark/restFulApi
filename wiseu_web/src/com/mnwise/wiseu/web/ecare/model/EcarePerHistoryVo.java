package com.mnwise.wiseu.web.ecare.model;

import org.apache.commons.lang3.ArrayUtils;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.model.SearchVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;
import com.mnwise.wiseu.web.common.util.PropertyUtil;

public class EcarePerHistoryVo extends SearchVo {
    private static final long serialVersionUID = 5664359065036521109L;
    private String customerNm;
    private String customerEmail;
    private String sendDt;
    private String sendTm;
    private String errorCd;
    private String errMsg;
    private String errMsgKor;
    private String ecareNo;
    private String resultSeq;
    private String svcId;
    private String ecareNm;
    private String openDt;
    private String data;
    private String svcIdType;
    private String campaignNo;
    private String campaignNm;
    private String reportType;
    private String serviceType;
    private String subType;
    private String paymentDt;
    private String recvStdType;
    private String recvStdNm;
    private String customerSsn;
    private String recevieDt;
    private String listSeq;
    private String recordSeq;
    private String seq;
    private String receiverId;
    private String receiverNm;
    private String receiverEmail;
    private String jobId;
    private String customerKey;
    private String sendSeq;
    private String channel;
    private String slot1;
    private String slot2;
    private Integer resendEcareNo;
    private Integer segmentNo;
    private String[] alimtalkSearchCodes;

    //20170630 KSM 다시보기/재발송
    private String resendSts;
    private String superSeq;

    // [20180316][lyy][발송결과보기 컬럼추가]
    /** 행번호 */
    private int rowNum;
    /** 문자유형(SMS,LMS,MMS) */
    private String msgType;
    /** 발신번호(회신번호) */
    private String sender;
    /** 담당부서 */
    private String reqDeptNm;
    /** 알림톡 발송요청 여부 */
    private String alimtalkYn;
    /** 발송내용 */
    private String sndMsg;

    private String sendResultType;
    /** 알림톡 문자 대체발송 결과코드 */
    private String smsRsltCd;
    /** 알림톡 문자 대체발송 결과메시지 */
    private String smsRsltMsg;
    private String reqDt;
    private String reqTm;
    private String smsSendDt;
    private String smsSendTm;

    private String reqUserId;
    private String reqDeptId;
    private String subject;
    private String sendFg;
    private String sn;

    //수신 확인
    private String receiptDate;
    private String logSendFg;

    public String getReqDeptId() {
        return reqDeptId;
    }

    public void setReqDeptId(String reqDeptId) {
        this.reqDeptId = reqDeptId;
    }

    public String getResendSts() {
        return resendSts;
    }

    public void setResendSts(String resendSts) {
        this.resendSts = resendSts;
    }

    public String getSuperSeq() {
        return superSeq;
    }

    public void setSuperSeq(String superSeq) {
        this.superSeq = superSeq;
    }

    public String getSlot1() {
        return slot1;
    }

    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }

    public String getSlot2() {
        return this.slot2;
    }

    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }

    public String getSendSeq() {
        return sendSeq;
    }

    public void setSendSeq(String sendSeq) {
        this.sendSeq = sendSeq;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverNm() {
        return receiverNm;
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getListSeq() {
        return listSeq;
    }

    public void setListSeq(String listSeq) {
        this.listSeq = listSeq;
    }

    public String getRecordSeq() {
        return recordSeq;
    }

    public void setRecordSeq(String recordSeq) {
        this.recordSeq = recordSeq;
    }

    public String getRecevieDt() {
        return recevieDt;
    }

    public void setRecevieDt(String recevieDt) {
        this.recevieDt = recevieDt;
    }

    public String getDispRecevieDt() {
        String retDate = recevieDt;
        if(recevieDt != null && recevieDt.length() > 8) {
            retDate = FormatUtil.toBasicStrDate(recevieDt.substring(0, 8), recevieDt.substring(8));
        }
        return retDate;
    }

    public String getCustomerSsn() {
        return customerSsn;
    }

    public void setCustomerSsn(String customerSsn) {
        this.customerSsn = customerSsn;
    }

    public String getRecvStdType() {
        return recvStdType;
    }

    public void setRecvStdType(String recvStdType) {
        this.recvStdType = recvStdType;
    }

    public String getRecvStdNm() {
        return recvStdNm;
    }

    public void setRecvStdNm(String recvStdNm) {
        this.recvStdNm = recvStdNm;
    }

    public String getPaymentDt() {
        return paymentDt;
    }

    public void setPaymentDt(String paymentDt) {
        this.paymentDt = paymentDt;
    }

    public String getDispPaymentDt() {
        return FormatUtil.toYmdStrDate(paymentDt);
    }

    public String getErrMsgKor() {
        return errMsgKor;
    }

    public void setErrMsgKor(String errMsgKor) {
        this.errMsgKor = errMsgKor;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCampaignNm() {
        return campaignNm;
    }

    public void setCampaignNm(String campaignNm) {
        this.campaignNm = campaignNm;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(String campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getSvcIdType() {
        return svcIdType;
    }

    public void setSvcIdType(String svcIdType) {
        this.svcIdType = svcIdType;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSendDt() {
        return sendDt;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    public String getSendTm() {
        return sendTm;
    }

    public void setSendTm(String sendTm) {
        this.sendTm = sendTm;
    }

    public String getErrorCd() {
        return errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getEcareNo() {
        return ecareNo;
    }

    public void setEcareNo(String ecareNo) {
        this.ecareNo = ecareNo;
    }

    public String getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
    }

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public String getEcareNm() {
        return ecareNm;
    }

    public void setEcareNm(String ecareNm) {
        this.ecareNm = ecareNm;
    }

    public String getOpenDt() {
        return openDt;
    }

    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDispSendDt() {
        return FormatUtil.toBasicStrDate(sendDt, sendTm);
    }

    public String getDispOpenDt() {
        String retDate = openDt;
        if(openDt != null && openDt.length() > 8) {
            retDate = FormatUtil.toBasicStrDate(openDt.substring(0, 8), openDt.substring(8));
        }
        return retDate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    // [20180316][lyy][발송결과보기 컬럼추가]
    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReqDeptNm() {
        return reqDeptNm;
    }

    public void setReqDeptNm(String reqDeptNm) {
        this.reqDeptNm = reqDeptNm;
    }

    public String getAlimtalkYn() {
        return alimtalkYn;
    }

    public void setAlimtalkYn(String alimtalkYn) {
        this.alimtalkYn = alimtalkYn;
    }

    public String getSndMsg() {
        return sndMsg;
    }

    public void setSndMsg(String sndMsg) {
        this.sndMsg = sndMsg;
    }

    public String getSmsRsltCd() {
        return smsRsltCd;
    }

    public void setSmsRsltCd(String smsRsltCd) {
        this.smsRsltCd = smsRsltCd;
    }

    public String getSmsRsltMsg() {
        return smsRsltMsg;
    }

    public void setSmsRsltMsg(String smsRsltMsg) {
        this.smsRsltMsg = smsRsltMsg;
    }

    public String getSendFg() {
        return sendFg;
    }

    public void setSendFg(String sendFg) {
        this.sendFg = sendFg;
    }

    public String getReqDt() {
        return reqDt;
    }

    public void setReqDt(String reqDt) {
        this.reqDt = reqDt;
    }

    public String getReqTm() {
        return reqTm;
    }

    public void setReqTm(String reqTm) {
        this.reqTm = reqTm;
    }

    public String getSmsSendDt() {
        return smsSendDt;
    }

    public void setSmsSendDt(String smsSendDt) {
        this.smsSendDt = smsSendDt;
    }

    public String getSmsSendTm() {
        return smsSendTm;
    }

    public void setSmsSendTm(String smsSendTm) {
        this.smsSendTm = smsSendTm;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReqUserId() {
        return reqUserId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getSendResultType() {
        return sendResultType;
    }

    public void setSendResultType(String sendResultType) {
        this.sendResultType = sendResultType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDispAltalkSendResult() {
        if(sendFg != null) {
            switch(sendFg.charAt(0)) {
                case '1':
                    return "<span style='color:green;'>대기</span>";
                case '2':
                case '3':
                    return "<span style='color:blue;'>전송중</span>";
            }
        }
        // errorCd=RSLT_CD
        // [0000]성공-수신확인, [71]UMS발송요청데이타오류, [84]수신거부, [85,86,87,88,89,95]데이타오류, [99]전송취소, [기타]전송실패
        if(isSuccessAltalkSend()) {
            return "성공";
        } else if(isDataError()) {
            return toDispSendFail("데이타오류", errorCd, errMsgKor);
        } else if(isCancel()) {
            return toDispSendFail("전송취소", errorCd, errMsgKor);
        } else if(isReject()) {
            return toDispSendFail("수신거부", errorCd, errMsgKor);
        }else if(isReady()) {
            return toDispSendFail("발송중", errorCd, errMsgKor);
        }

        return toDispSendFail("실패", errorCd, errMsgKor);
    }

    public String getDispFrtalkSendResult() {
        if(sendFg != null) {
            switch(sendFg.charAt(0)) {
                case '1':
                    return "<span style='color:green;'>대기</span>";
                case '2':
                case '3':
                    return "<span style='color:blue;'>전송중</span>";
            }
        }
        if(isSuccessFrtalkSend()) {
            return "성공";
        } else if(isDataError()) {
            return toDispSendFail("데이타오류", errorCd, errMsgKor);
        } else if(isCancel()) {
            return toDispSendFail("전송취소", errorCd, errMsgKor);
        } else if(isReject()) {
            return toDispSendFail("수신거부", errorCd, errMsgKor);
        }else if(isReady()) {
            return toDispSendFail("발송중", errorCd, errMsgKor);
        }
        return toDispSendFail("실패", errorCd, errMsgKor);
    }

    public String getDispBrtalkSendResult() {
        if(sendFg != null) {
            switch(sendFg.charAt(0)) {
                case '1':
                    return "<span style='color:green;'>대기</span>";
                case '2':
                case '3':
                    return "<span style='color:blue;'>전송중</span>";
            }
        }
        if(isSuccessBrtalkSend()) {
            return "성공";
        } else if(isDataError()) {
            return toDispSendFail("데이타오류", errorCd, errMsgKor);
        } else if(isCancel()) {
            return toDispSendFail("전송취소", errorCd, errMsgKor);
        } else if(isReject()) {
            return toDispSendFail("수신거부", errorCd, errMsgKor);
        }else if(isReady()) {
            return toDispSendFail("발송중", errorCd, errMsgKor);
        }
        return toDispSendFail("실패", errorCd, errMsgKor);
    }
    // 직접 문자발송 결과
    public String getDispSmsSendResult() {


        String resultCd = errorCd;
        String resultMsg = errMsgKor;

        if(StringUtil.isBlank(resultCd)) {
            return "<span style='color:green;'>\ub300\uae30</span>"; // 대기
        }


        //ERROR_CD = TRAN_RSLT

        if(ArrayUtils.contains(PropertyUtil.getProperty("alimtalk.other.code.success").split(","), resultCd)) {

            return "\uc131\uacf5"; // 성공
        }

        return toDispSendFail("\uc2e4\ud328", resultCd, resultMsg); // 실패
    }

    // 대체 문자발송 결과
    public String getDispAlternateSmsSendResult() {
        if(StringUtil.isBlank(smsRsltCd)) {
            return "";
        }

        //ERROR_CD = TRAN_RSLT
        if(ArrayUtils.contains(PropertyUtil.getProperty("alimtalk.other.code.success").split(","), smsRsltCd)) {
            return "\uc131\uacf5"; // 성공
        }
        /* if(StringUtil.equals(smsRsltCd, "0")) {
         * return "\uc131\uacf5"; // 성공
         * } */

        return toDispSendFail("\uc2e4\ud328", smsRsltCd, smsRsltMsg); // 실패
    }

    /**
     * HTML 태그 제거한 메시지내용 (친구톡 이미지 태그로 고객이력조회시 화면 깨짐 방지)
     */
    public String getDispSndMsg() {
        if(StringUtil.isNotBlank(sndMsg)) {
            return sndMsg.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        }

        return sndMsg;
    }

    public boolean isSuccessAltalkSend() {
        return ArrayUtils.contains(PropertyUtil.getProperty("alimtalk.code.success").split(","), errorCd);
    }
    public boolean isSuccessFrtalkSend() {
        return ArrayUtils.contains(PropertyUtil.getProperty("friendtalk.code.success").split(","), errorCd);
    }
    public boolean isSuccessBrtalkSend() {
        return ArrayUtils.contains(PropertyUtil.getProperty("brandtalk.code.success").split(","), errorCd);
    }

    public boolean isCancel() {
        return false;
        //return StringUtil.equals(errorCd, "TODO:취소코드");
    }

    public boolean isReject() {
        return false;
        //return StringUtil.equals(errorCd, "TODO:수신거부코드");
    }

    public boolean isDataError() {
        return false;
        //return StringUtil.contains(",71,85,86,87,88,89,95,", "," + errorCd + ",");  // TODO:콤마로 구분된 데이타오류코드 목록
    }

    public boolean isReady() {
        return ArrayUtils.contains(new String[]{"010"}, errorCd);
    }

    public String toDispSendFail(String title, String errorCode, String errorMessage) {
        return "<div style='color:red;'>" + title + "</div> <div style='font-size:10px;color:grey;text-align:left;padding:0 3px 0 3px;' class='datanowrap' title='" + errorMessage + "'>" + "["
            + errorCode + "]" + errorMessage + "</div>";
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubType() {
        return subType;
    }

    public void setResendEcareNo(Integer resendEcareNo) {
        this.resendEcareNo = resendEcareNo;
    }

    public int getResendEcareNo() {
        return resendEcareNo;
    }

    public void setSegmentNo(Integer segmentNo) {
        this.segmentNo = segmentNo;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public String[] getAlimtalkSearchCodes() {
        return alimtalkSearchCodes;
    }

    public void setAlimtalkSearchCodes(String[] alimtalkSearchCodes) {
        this.alimtalkSearchCodes = alimtalkSearchCodes;
    }
    public String getReceiptDate() {
        return receiptDate;
    }
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }
    public String getLogSendFg() {
        return logSendFg;
    }
    public void setLogSendFg(String log_send_fg) {
        this.logSendFg = log_send_fg;
    }
}
