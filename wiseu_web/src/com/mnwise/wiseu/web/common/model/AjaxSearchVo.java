package com.mnwise.wiseu.web.common.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

public class AjaxSearchVo extends BaseVo {
    private static final long serialVersionUID = 1L;
    private String searchColumn;
    private String q;
    private String searchedWord;
    private int key;

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getSearchedWord() {
        return searchedWord;
    }

    public void setSearchedWord(String searchedWord) {
        this.searchedWord = searchedWord;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

}
