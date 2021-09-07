package com.mnwise.wiseu.web.template.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTemplateResult {
    private List<KakaoTemplateVo> list;
    private boolean hasNext;

    public List<KakaoTemplateVo> getList() {
        return list;
    }

    public void setList(List<KakaoTemplateVo> list) {
        this.list = list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
