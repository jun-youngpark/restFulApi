package com.mnwise.wiseu.web.editor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.template.dao.MobileContentsDao;
import com.mnwise.wiseu.web.template.model.MobileVo;

@Service
public class EditorKakaoService extends BaseService {
    @Autowired private MobileContentsDao mobileContentsDao;

    public List<MobileVo> selectKakaoContentsList(MobileVo kakaoVo) throws Exception{
        PagingUtil.setPagingRowcount(kakaoVo);
        return mobileContentsDao.selectKakaoContentsList(kakaoVo);
    }

    public MobileVo selectKakaoContent(MobileVo kakaoVo) throws Exception{
        return mobileContentsDao.selectKakaoContent(kakaoVo);
    }

    public int selectKakaoContentsListMax(MobileVo kakaoVo) throws Exception{
        return  mobileContentsDao.selectKakaoContentsListMax(kakaoVo);
    }
}
