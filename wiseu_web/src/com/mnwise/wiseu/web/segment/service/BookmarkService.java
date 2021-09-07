package com.mnwise.wiseu.web.segment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.model.BookmarkVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;

@Service
public class BookmarkService extends BaseService {
    @Autowired private SegmentDao segmentDao;

    public List<SegmentVo> getBookmarkList(BookmarkVo bookmarkVo) {
        PagingUtil.setPagingRowcount(bookmarkVo);

        String bookmarkKind = StringUtil.defaultString(bookmarkVo.getBookmarkKind());
        if(bookmarkKind.equals("D")) {
            return segmentDao.selectDefaultBookmarkList(bookmarkVo);
        }

        if(bookmarkKind.equals("A")) {
            bookmarkVo.setSegmentType(null);
        } else {
            bookmarkVo.setSegType(bookmarkKind);
        }

        return segmentDao.selectBookmarkList(bookmarkVo);

    }

    public int getBookmarkListTotalCount(BookmarkVo bookmarkVo) {
        String bookmarkKind = StringUtil.defaultString(bookmarkVo.getBookmarkKind());

        if(bookmarkKind.equals("D")) {
            return segmentDao.selectDefaultBookmarkListTotalCount(bookmarkVo);
        }

        if(bookmarkKind.equals("A")) {
            bookmarkVo.setSegmentType(null);
        } else {
            bookmarkVo.setSegType(bookmarkKind);
        }

        return segmentDao.selectBookmarkListTotalCount(bookmarkVo);
    }
}
