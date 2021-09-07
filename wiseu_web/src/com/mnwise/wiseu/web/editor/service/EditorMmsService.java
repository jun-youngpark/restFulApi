package com.mnwise.wiseu.web.editor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.editor.model.MmsVo;
import com.mnwise.wiseu.web.template.dao.MobileContentsDao;

@Service
public class EditorMmsService extends BaseService {
    @Autowired private MobileContentsDao mobileContentsDao;

    /**
     * MMS 컨텐츠 목록
     */
    public List<MmsVo> selectEditorMmsContentsList(MmsVo mmsVo) {
        PagingUtil.setPagingRowcount(mmsVo);
        return mobileContentsDao.selectEditorMmsContentsList(mmsVo);
    }

    public int selectEditorMmsContentsListMax(MmsVo mmsVo) {
        return mobileContentsDao.selectEditorMmsContentsListMax(mmsVo);
    }

    public String selectEditorMmsContentsFilePreviewPath(String filePath) {
        return mobileContentsDao.selectEditorMmsContentsFilePreviewPath(filePath);
    }
}
