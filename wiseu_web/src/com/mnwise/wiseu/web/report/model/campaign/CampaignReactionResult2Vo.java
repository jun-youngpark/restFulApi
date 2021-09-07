package com.mnwise.wiseu.web.report.model.campaign;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;

public class CampaignReactionResult2Vo extends BaseVo {
    private static final Logger log = LoggerFactory.getLogger(CampaignReactionResult2Vo.class);

    private static final long serialVersionUID = -3405156978369573606L;
    private int campaignNo;
    private int sendCnt;
    private int openCnt;
    private int durationCnt;
    private int clickCnt;
    private String startDt;
    private String startTm;

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public int getDurationCnt() {
        return durationCnt;
    }

    public void setDurationCnt(int durationCnt) {
        this.durationCnt = durationCnt;
    }

    public int getClickCnt() {
        return clickCnt;
    }

    public void setClickCnt(int clickCnt) {
        this.clickCnt = clickCnt;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getStartTm() {
        return startTm;
    }

    public void setStartTm(String startTm) {
        this.startTm = startTm;
    }

    public String getOpenBySend() {
        return FormatUtil.toStrPercent(openCnt, sendCnt, 1);
    }

    public String getDurationBySend() {
        return FormatUtil.toStrPercent(durationCnt, sendCnt, 1);
    }

    public String getDurationByOpen() {
        return FormatUtil.toStrPercent(durationCnt, openCnt, 1);
    }

    public String getClickBySend() {
        return FormatUtil.toStrPercent(clickCnt, sendCnt, 1);
    }

    public String getClickByOpen() {
        return FormatUtil.toStrPercent(clickCnt, openCnt, 1);
    }

    public String getSendDate() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date date = null;
        try {
            date = df.parse(startDt + " " + startTm);
        } catch(ParseException e) {
            log.error(null, e);
        }
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:00");
    }
}
