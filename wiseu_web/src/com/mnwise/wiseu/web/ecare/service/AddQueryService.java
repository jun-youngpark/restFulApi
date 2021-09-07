package com.mnwise.wiseu.web.ecare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.AddQueryDao;
import com.mnwise.wiseu.web.ecare.model.AddQueryVo;

/**
 * 이케어 부가데이터 서비스
 */
@Service
public class AddQueryService extends BaseService {

    @Autowired private AddQueryDao addQueryDao;

    /**
     * NAADDQUERY테이블에서 부가데이터 쿼리를 가져온다.
     *
     * @param ecareNo
     * @return
     */
    public List<AddQueryVo> selectAddQuery(int ecareNo) {
        return addQueryDao.selectAddQuery(ecareNo);
    }
    /**
     * NAADDQUERY테이블에서 추가/수정
     *
     * @param AddQueryVo
     * @return
     */
    public void mergeAddQuery(AddQueryVo addQueryVo, int ecareNo) {
         addQueryDao.deleteAddQuery(ecareNo);
         addQueryDao.insertAddQuery(addQueryVo);
    }
    /**
     * NAADDQUERY테이블에서 추가/수정
     *
     * @param AddQueryVo[]
     * @return
     */
    public void mergeAddQuery(AddQueryVo[] addQueryVoList, int ecareNo) {
        addQueryDao.deleteAddQuery(ecareNo);
        for(AddQueryVo addQueryVo:addQueryVoList) {
            addQueryDao.insertAddQuery(addQueryVo);
        }
   }

}
