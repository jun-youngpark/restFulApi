package com.mnwise.wiseu.web.base.util;

/**
 * modifier 최재철 유저별로 리스트 사이즈를 조정할 수 있게 수정함
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.model.SearchVo;

public abstract class PagingUtil {
    protected static int DEFAULT_CURRENT_PAGE = 1;
    protected static int DEFAULT_COUNT_PER_PAGE = 10;
    // public static HashMap<String, String> m_hmCountPerPagePerUser = new
    // HashMap<String, String>();


    public static void setPagingRowcount(Object object) {
        SearchVo searchVo = (SearchVo) object;

        /** Mysql limit 용 * */
        // int startRow = (searchVo.getNowPage() - 1) *
        // searchVo.getCountPerPage();
        // int endRow = DEFAULT_COUNT_PER_PAGE;

        int startRow = (searchVo.getNowPage() - 1) * searchVo.getCountPerPage() + 1;
        int endRow = searchVo.getNowPage() * searchVo.getCountPerPage();

        ((SearchVo) object).setStartRow(startRow);
        ((SearchVo) object).setEndRow(endRow);
    }

    public static void setPagingInfo(HttpServletRequest request) {
        PagingUtil.setPagingInfo(request, null);
    }

    public static void setPagingInfo(HttpServletRequest request, Object object) {
        int countPerPagePerUser = DEFAULT_COUNT_PER_PAGE;

        if(object != null) {
            SearchVo searchVo = (SearchVo) object;

            UserVo userVo = searchVo.getUserVo();
            if(userVo != null)
                countPerPagePerUser = userVo.getListCountPerPage();
            if(countPerPagePerUser == 0)
                countPerPagePerUser = DEFAULT_COUNT_PER_PAGE;
        }

        String searchColumn = ServletRequestUtils.getStringParameter(request, "searchColumn", null);
        String searchWord = ServletRequestUtils.getStringParameter(request, "searchWord", null);

        String searchQstartDt = ServletRequestUtils.getStringParameter(request, "searchQstartDt", null);
        String searchQendDt = ServletRequestUtils.getStringParameter(request, "searchQendDt", null);

        String orderColumn = ServletRequestUtils.getStringParameter(request, "orderColumn", null);
        String orderSort = ServletRequestUtils.getStringParameter(request, "orderSort", null);

        // int countPerPage = ServletRequestUtils.getIntParameter(request,
        // "countPerPage", DEFAULT_COUNT_PER_PAGE);
        int countPerPage = ServletRequestUtils.getIntParameter(request, "countPerPage", countPerPagePerUser);
        int nowPage = ServletRequestUtils.getIntParameter(request, "nowPage", DEFAULT_CURRENT_PAGE);

        if(object != null) {
            if(StringUtil.isNotBlank(searchQstartDt)) {
                ((SearchVo) object).setSearchQstartDt(searchQstartDt.replaceAll("-", ""));
            }
            if(StringUtil.isNotBlank(searchQendDt)) {
                ((SearchVo) object).setSearchQendDt(searchQendDt.replaceAll("-", ""));
            }
            ((SearchVo) object).setSearchColumn(searchColumn);
            ((SearchVo) object).setSearchWord(searchWord);
            ((SearchVo) object).setOrderColumn(orderColumn);
            ((SearchVo) object).setOrderSort(orderSort);
            ((SearchVo) object).setCountPerPage(countPerPage);
            ((SearchVo) object).setListCnt(countPerPage);
            ((SearchVo) object).setNowPage(nowPage);
        }

        request.setAttribute("searchColumn", searchColumn);
        request.setAttribute("searchWord", searchWord);
        request.setAttribute("searchQstartDt", searchQstartDt);
        request.setAttribute("searchQendDt", searchQendDt);
        request.setAttribute("orderColumn", orderColumn);
        request.setAttribute("orderSort", orderSort);
        request.setAttribute("countPerPage", Integer.valueOf(countPerPage));
        request.setAttribute("nowPage", Integer.valueOf(nowPage));
    }

    /**
     * View로 페이징 정보를 전송합니다.
     *
     * @param request
     * @param totalCount
     */
    public static void transferPagingInfo(HttpServletRequest request, int totalCount) {
        request.setAttribute("totalCount", Integer.valueOf(totalCount));
    }

    /**
     * View로 페이징 정보를 전송합니다.
     *
     * @param request
     */
    protected void transferPagingInfo(HttpServletRequest request) {
        transferPagingInfo(request, 0);
    }

    /**
     * 페이징 시 전달할 Hidden 정보를 설정합니다.
     *
     * @param param
     * @return
     */
    public static List<Map<String, String>> getHiddenList(String[] param) {
        if(param == null || param.length == 0)
            return null;

        List<Map<String, String>> paramList = new ArrayList<Map<String, String>>();
        for(int indexI = 0; indexI < param.length; indexI++) {
            List<String> pair = StringUtil.splitToList(param[indexI], ":");
            if(pair != null && pair.size() == 2) {
                Map<String, String> map = new HashMap<>();
                map.put("name", pair.get(0));
                map.put("value", pair.get(1));
                paramList.add(map);
            }
        }
        return paramList;
    }
}
