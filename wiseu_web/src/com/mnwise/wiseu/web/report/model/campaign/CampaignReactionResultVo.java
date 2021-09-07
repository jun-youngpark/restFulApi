package com.mnwise.wiseu.web.report.model.campaign;

import org.springframework.util.PatternMatchUtils;

import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;

public class CampaignReactionResultVo extends BaseVo {

    /**
     * 
     */
    private static final long serialVersionUID = 4152359550095506677L;
    String serviceType = null;
    String reactionType = null;
    String reactionTypeToStr = null;
    int reactionUnique = 0;
    int reactionAll = 0;
    int reactionOverlap = 0;
    private int sendCnt = 0;
    private int openCnt = 0;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public int getReactionUnique() {
        return reactionUnique;
    }

    public void setReactionUnique(int reactionUnique) {
        this.reactionUnique = reactionUnique;
    }

    public int getReactionAll() {
        return reactionAll;
    }

    public void setReactionAll(int reactionAll) {
        this.reactionAll = reactionAll;
    }

    public int getReactionOverlap() {
        return reactionOverlap;
    }

    public void setReactionOverlap(int reactionOverlap) {
        this.reactionOverlap = reactionOverlap;
    }

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public String getByUniqueSend() {
        return FormatUtil.toStrPercent(reactionUnique, sendCnt, 1);
    }

    public String getByUniqueOpen() {
        if("1".equals(reactionType) || openCnt == 0) {
            return "-";
        } else {
            return FormatUtil.toStrPercent(reactionUnique, openCnt, 1);
        }
    }

    public String getByDupSend() {
        if("2".equals(reactionType) || sendCnt == 0) {
            return "-";
        } else {
            return FormatUtil.toStrPercent(reactionAll, sendCnt, 1);
        }
    }

    public String getByDupOpen() {
        String[] allowType = {
            "1", "2"
        };
        if(PatternMatchUtils.simpleMatch(allowType, reactionType) || openCnt == 0) {
            return "-";
        } else {
            return FormatUtil.toStrPercent(reactionAll, openCnt, 1);
        }
    }

    public String getByOverLapSend() {
        if("2".equals(reactionType) || sendCnt == 0) {
            return "-";
        } else {
            return FormatUtil.toStrPercent(reactionOverlap, sendCnt, 1);
        }
    }

    public String getByOverLapOpen() {
        String[] allowType = {
            "1", "2"
        };
        if(PatternMatchUtils.simpleMatch(allowType, reactionType) || openCnt == 0) {
            return "-";
        } else {
            return FormatUtil.toStrPercent(reactionOverlap, openCnt, 1);
        }
    }

    public void setReactionTypeToStr(String reactionTypeToStr) {
        this.reactionTypeToStr = reactionTypeToStr;
    }

    public String getReactionTypeToStr() {
        return reactionTypeToStr;
    }
}
