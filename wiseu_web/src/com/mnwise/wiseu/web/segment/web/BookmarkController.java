package com.mnwise.wiseu.web.segment.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.segment.model.BookmarkVo;
import com.mnwise.wiseu.web.segment.service.BookmarkService;

@Controller
public class BookmarkController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BookmarkController.class);

    @Autowired private BookmarkService bookmarkService;

    /**
     * - [캠페인>캠페인 등록>메시지 작성(2단계)] 대상자 선택 (팝업)
     * - [이케어>이케어 등록>메시지 작성(2단계)] 대상자 선택 (팝업)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value={"/segment/segmentSelectPopup.do", "/target/segmentSelectPopup.do"}, method={RequestMethod.GET, RequestMethod.POST})  // /bookmark/bookmark_popup.do
    public ModelAndView list(@RequestParam(defaultValue="A") String bookmarkKind, String type, String serviceType, @RequestParam(defaultValue="") String channelType, String serviceNo, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();

            BookmarkVo bookmarkVo = new BookmarkVo();
            bookmarkVo.setUserVo(userVo);
            bookmarkVo.setBookmarkKind(bookmarkKind);

            PagingUtil.setPagingInfo(request, bookmarkVo);
            // 각 하위 컨트롤러에서 검색 필드를 설정하기 위해 호출
            if(bookmarkVo != null && StringUtil.isNotBlank(bookmarkVo.getSearchColumn()) && StringUtil.isNotBlank(bookmarkVo.getSearchWord())) {
                setSearchInfo(bookmarkVo);
            }

            if(channelType.equals(Channel.MAIL)) {
                bookmarkVo.setSemanticKey("E");
            } else if(StringUtil.equalsAny(channelType, new String[] {Channel.SMS, Channel.LMSMMS, Channel.ALIMTALK, Channel.FRIENDTALK, Channel.PUSH, Channel.BRANDTALK})) {
                bookmarkVo.setSemanticKey("S");
            } else if(channelType.equals(Channel.FAX)) {
                bookmarkVo.setSemanticKey("F");
            }

            int bookmarkListTotalCount = bookmarkService.getBookmarkListTotalCount(bookmarkVo);
            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (bookmarkListTotalCount / aCountPerPage); // 몫
            int rCountPerPage = (bookmarkListTotalCount % aCountPerPage); // 나머지

            if(bookmarkVo.getNowPage() <= qCountPerPage) {
                bookmarkVo.setListCnt(userVo.getListCountPerPage());
            } else {
                bookmarkVo.setListCnt(rCountPerPage);
            }

            ModelAndView mav = new ModelAndView("/segment/segmentSelectPopup");  // /segment/bookmark/bookmark_list
            mav.addObject("bookmarkListTotalCount", String.valueOf(bookmarkListTotalCount));

            if(bookmarkListTotalCount > 0) {
                mav.addObject("bookmarkList", bookmarkService.getBookmarkList(bookmarkVo));
            }

            PagingUtil.transferPagingInfo(request, bookmarkListTotalCount);

            mav.addObject("bookmarkKind", bookmarkKind);
            mav.addObject("type", type);
            mav.addObject("serviceType", serviceType);
            mav.addObject("channelType", channelType);
            mav.addObject("serviceNo", serviceNo);

            return mav;
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    private void setSearchInfo(BookmarkVo bookmarkVo) {
        String[] arySearchColumn = null;
        if(bookmarkVo.getSearchColumn().indexOf("|") != -1) {
            arySearchColumn = StringUtil.split(bookmarkVo.getSearchColumn(), "|");
        } else {
            arySearchColumn = new String[1];
            arySearchColumn[0] = bookmarkVo.getSearchColumn();
        }

        for(int idx = 0; idx < arySearchColumn.length; idx++) {
            if(arySearchColumn[idx].trim().equals("segmentNm")) {
                bookmarkVo.setSegmentNm(bookmarkVo.getSearchWord());
            } else if(arySearchColumn[idx].trim().equals("fileToDb")) {
                bookmarkVo.setFileToDbYn(bookmarkVo.getSearchWord());
            }
        }
    }
}
