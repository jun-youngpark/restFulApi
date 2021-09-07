package com.mnwise.wiseu.web.base.model;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;

public class SearchVo extends BaseVo {
    private static final long serialVersionUID = 1L;
    protected int nowPage;
    protected int countPerPage = 10; // default
    protected int totalRowCnt;
    protected String searchColumn;
    protected String searchWord;
    protected String orderColumn;
    protected String orderSort;
    protected int startRow;
    protected int endRow;

    protected String searchQstartDt;
    protected String searchQendDt;

    protected String channelStatus;

    protected int mssqlTop;

    protected String userId;

    protected UserVo userVo;

    protected int listCnt;

    // added by parkhj 2019.06.03
    protected String[] useChannelList;


    public String[] getUseChannelList() {
        return useChannelList;
    }

    public void setUseChannelList(String[] useChannelList) {
        this.useChannelList = useChannelList;
    }

    public int getListCnt() {
        return listCnt;
    }

    public void setListCnt(int totalCnt) {
        this.listCnt = totalCnt;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMssqlTop() {
        return countPerPage * nowPage;
    }

    public void setMssqlTop(int mssqlTop) {
        this.mssqlTop = mssqlTop;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public Integer getNowPage() {
        return nowPage <= 0 ? 1 : nowPage;
    }

    public void setNowPage(int p_nowPage) {
        this.nowPage = p_nowPage;
    }
    public void setNowPage(Integer p_nowPage) {
        if(null==p_nowPage) {
            this.nowPage = 1;
        }else {
            this.nowPage = p_nowPage;
        }
    }
    public void setNowPage(String p_nowPage) {
        if(null==p_nowPage) {
            this.nowPage = 1;
        }
        else if(p_nowPage instanceof String) {
            try {
                this.nowPage = Integer.parseInt(StringUtil.defaultIfBlank(p_nowPage,"1"));
            } catch(Exception e) {
                this.nowPage = 1;
            }
        }
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderSort() {
        return orderSort;
    }

    public void setOrderSort(String orderSort) {
        this.orderSort = orderSort;
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public int getCountPerPage() {
        return countPerPage;
    }

    public void setCountPerPage(int countPerPage) {
        this.countPerPage = countPerPage;
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

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }

    public int getTotalRowCnt() {
        return totalRowCnt;
    }

    public void setTotalRowCnt(int totalRowCnt) {
        this.totalRowCnt = totalRowCnt;
    }
}