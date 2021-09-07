package com.mnwise.wiseu.web.env.model;

/**
 * NVBADWORD 테이블 모델 클래스
 */
public class BadWordVo {
    private String channelType;
    private String badWords;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String mailBadWord;
    private String smsBadWord;
    private String channelUseList;

    // 기본 getter/setter
    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getBadWords() {
        return this.badWords;
    }

    public void setBadWords(String badWords) {
        this.badWords = badWords;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getMailBadWord() {
        return mailBadWord;
    }

    public void setMailBadWord(String mailBadWord) {
        this.mailBadWord = mailBadWord;
    }

    public String getSmsBadWord() {
        return smsBadWord;
    }

    public void setSmsBadWord(String smsBadWord) {
        this.smsBadWord = smsBadWord;
    }

    public String getChannelUseList() {
        return channelUseList;
    }

    public void setChennelUseList(String ChannelUseList) {
        this.channelUseList = ChannelUseList;
    }
}
