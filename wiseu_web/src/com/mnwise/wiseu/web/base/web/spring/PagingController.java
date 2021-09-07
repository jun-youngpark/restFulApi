package com.mnwise.wiseu.web.base.web.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.util.PagingUtil;

@Controller
public class PagingController extends BaseController {
    @RequestMapping(value="/common/page.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleRequest(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String viewPath = ServletRequestUtils.getStringParameter(request, "viewPath", "common/page");
        String actionPath = ServletRequestUtils.getStringParameter(request, "actionPath");
        String target = ServletRequestUtils.getStringParameter(request, "target");
        String method = ServletRequestUtils.getStringParameter(request, "method","post");
        String returnDocumentId = ServletRequestUtils.getStringParameter(request, "returnDocumentId");

        String[] hiddenParam = ServletRequestUtils.getStringParameters(request, "hiddenParam");
        map.put("hiddenList", PagingUtil.getHiddenList(hiddenParam));

        int totalCount = ServletRequestUtils.getIntParameter(request, "total", 0);
        int cntPerPage = ServletRequestUtils.getIntParameter(request, "countPerPage", 1);
        if(cntPerPage == 0)
            cntPerPage = 10;
        int cntPageListPerPage = ServletRequestUtils.getIntParameter(request, "listPerPage", 10);
        int nowPage = ServletRequestUtils.getIntParameter(request, "nowPage", 1);

        int totalPage = (int) ((totalCount - 1) / cntPerPage) + 1;
        if(totalPage == 0)
            totalPage = 1;

        int nowBlock = 0;

        nowBlock = (int) ((nowPage - 1) / cntPageListPerPage);

        map.put("actionPath", actionPath);
        map.put("nowPage", Integer.valueOf(nowPage));
        map.put("totalCount", Integer.valueOf(totalCount));
        map.put("cntPerPage", Integer.valueOf(cntPerPage));
        map.put("target", target);
        map.put("method", method);
        /* if (nowBlock > 0) { // Prev Page map.put("firstPage", new Integer( 1 ) ); } */
        map.put("firstPage", Integer.valueOf(1));
        int ePage = (nowPage - 1) / 10;
        if(nowPage > 1 && ePage > 0) {
            // map.put("prevPage", Integer.valueOf(nowPage - 1));
            // map.put("prevPage", Integer.valueOf(prevPage(nowPage)));
            map.put("prevPage", Integer.valueOf((ePage - 1) * 10) + 1);
        }

        List<Integer> pageList = new ArrayList<Integer>();
        int startPage = nowBlock * cntPageListPerPage + 1;
        int endPage = cntPageListPerPage * (nowBlock + 1);

        if(endPage > totalPage)
            endPage = totalPage;

        for(int indexI = startPage; indexI <= endPage; indexI++) {
            pageList.add(Integer.valueOf(indexI));
        }
        map.put("pageList", pageList);

        int tPage = (totalPage - 1) / 10;
        if(nowPage < totalPage && ePage < tPage) {
            // map.put("nextPage", Integer.valueOf(nowPage + 1));
            // map.put("nextPage", Integer.valueOf(nextPage(nowPage)));
            map.put("nextPage", Integer.valueOf((ePage + 1) * 10) + 1);
        }
        /* if( nowBlock < totalBlock ) { map.put("lastPage", new Integer( totalPage ) ); } */
        map.put("lastPage", Integer.valueOf(totalPage));
        map.put("returnDocumentId", returnDocumentId);

        return new ModelAndView(viewPath, map);
    }

    public static int nextPage(int nowPage) {
        int nextPage = 0;

        nextPage = (nowPage - (nowPage % 10) + 11);
        if(nowPage % 10 == 0)
            nextPage = nextPage - 10;

        return nextPage;
    }

    public static int prevPage(int nowPage) {
        int prevPage = 0;

        prevPage = (nowPage - (nowPage % 10) - 9);
        if(nowPage % 10 == 0)
            prevPage = prevPage - 10;
        if(prevPage < 0)
            prevPage = 1;

        return prevPage;
    }
}
