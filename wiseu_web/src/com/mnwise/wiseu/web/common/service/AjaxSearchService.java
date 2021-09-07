package com.mnwise.wiseu.web.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.AjaxSearchDao;
import com.mnwise.wiseu.web.common.model.AjaxSearchVo;

@Service
public class AjaxSearchService extends BaseService {
    @Autowired private AjaxSearchDao ajaxSearchDao;

    public List<AjaxSearchVo> getAjaxSearchList(AjaxSearchVo paramAjaxSearchVo) {
        return ajaxSearchDao.selectListSearch(paramAjaxSearchVo);
    }
}
