package com.mnwise.wiseu.web.ecare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateHistoryDao;
import com.mnwise.wiseu.web.ecare.model.EcareContsHistoryVo;

/**
 * 이케어 컨텐츠 변경이력
 */
@Service
public class EcareContsHistoryService extends BaseService {
    @Autowired private EcareTemplateHistoryDao ecareTemplateHistoryDao;
    @Autowired private EcMsgHandlerHistoryDao ecMsgHandlerHistoryDao;

    /**
     * 검색된 컨텐츠 변경이력 총 건수를 구해온다.
     *
     * @param contsVo 컨텐츠 구분, 이케어번호, 이케어명 검색조건을 받는 VO
     * @return 검색된 컨텐츠 변경이력 총 카운트
     */
    public int getContsTotalCount(final EcareContsHistoryVo contsVo) {
        final String flag = contsVo.getContsFlag();

        if("T".equals(flag)) {
            return ecareTemplateHistoryDao.selectContsTemplateTotalCount(contsVo);
        } else if("H".equals(flag)) {
            return ecMsgHandlerHistoryDao.selectContsHandlerTotalCount(contsVo);
        } else {
            throw new IllegalArgumentException("Conts Flag is not 'T' or 'H'. Conts Flag Value is [" + flag + "]");
        }
    }

    /**
     * 검색된 컨텐츠 변경이력 리스트를 가져온다.
     *
     * @param contsVo 컨텐츠 구분, 이케어번호, 이케어명 검색조건을 받는 VO
     * @return 검색된 컨텐츠 변경이력 리스트
     */
    public List<EcareContsHistoryVo> getContsHistoryList(final EcareContsHistoryVo contsVo) {
        PagingUtil.setPagingRowcount(contsVo);

        final String flag = contsVo.getContsFlag();
        if("T".equals(flag)) {
            return ecareTemplateHistoryDao.selectContsTemplateHistoryList(contsVo);
        } else if("H".equals(flag)) {
            return ecMsgHandlerHistoryDao.selectContsHandlerHistoryList(contsVo);
        } else {
            throw new IllegalArgumentException("Conts Flag is not 'T' or 'H'. Conts Flag Value is [" + flag + "]");
        }
    }

    /**
     * 선택된 컨텐츠 히스토리 정보를 가져온다.
     *
     * @param contsVo 선택된 컨텐츠 조회 정보를 담는 VO
     * @return 선택된 컨텐츠 히스토리 VO
     */
    public EcareContsHistoryVo getContsHistoryInfo(final EcareContsHistoryVo contsVo) {
        if(StringUtil.isEmpty(contsVo.getSeg()) == false) {
            return ecareTemplateHistoryDao.selectContsTemplateHistoryInfo(contsVo);
        } else {
            return ecMsgHandlerHistoryDao.selectContsHandlerHistoryInfo(contsVo);
        }
    }
}