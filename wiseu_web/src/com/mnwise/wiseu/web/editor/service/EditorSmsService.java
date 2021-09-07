package com.mnwise.wiseu.web.editor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.editor.model.SmsVo;
import com.mnwise.wiseu.web.template.dao.MobileContentsDao;

@Service
public class EditorSmsService extends BaseService {
    @Autowired private MobileContentsDao mobileContentsDao;

    public int selectEditorSmsContentsListMax(SmsVo smsVo) {
        return mobileContentsDao.selectEditorSmsContentsListMax(smsVo);
    }

    /**
     * SMS 컨텐츠 목록
     */
    public List<SmsVo> selectEditorSmsContentsList(SmsVo smsVo) {
        PagingUtil.setPagingRowcount(smsVo);
        return mobileContentsDao.selectEditorSmsContentsList(smsVo);
    }
}
