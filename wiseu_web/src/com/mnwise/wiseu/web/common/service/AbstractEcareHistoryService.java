package com.mnwise.wiseu.web.common.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateHistoryDao;
import com.mnwise.wiseu.web.editor.model.HistoryVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;

public class AbstractEcareHistoryService extends BaseService {
    @Autowired private EcareTemplateHistoryDao ecareTemplateHistoryDao;
    @Autowired private EcMsgHandlerHistoryDao ecMsgHandlerHistoryDao;

    /**
     * 이케어 저장 히스토리(템플릿, 핸들러)
     * @param ecareNo
     * @param userId
     * @param appsource
     * @param type
     * @param templateVos
     * @param historyMsg
     * @param nextHandlerVer
     * @return
     * @throws Exception
     */
    protected int insertEcareHistory(int ecareNo, String userId, String appsource, String type, List<TemplateVo> templateVos, String historyMsg, int nextHandlerVer) throws Exception {
        Date date = new Date();
        String lastupdateDt = new SimpleDateFormat("yyyyMMdd").format(date);
        String lastupdateTm = new SimpleDateFormat("HHmmss").format(date);

        HistoryVo historyVo = new HistoryVo();
        historyVo.setEcareNo(ecareNo);
        historyVo.setUserId(userId);
        historyVo.setLastupdateDt(lastupdateDt);
        historyVo.setLastupdateTm(lastupdateTm);
        historyVo.setType(type);
        historyVo.setHandlerVer(nextHandlerVer);
        historyVo.setHistoryMsg(historyMsg);

        // appsource or templateVo가 null이면 안함
        if(appsource != null) {
            historyVo.setAppsource(appsource);
            ecMsgHandlerHistoryDao.insertEcareHandlerHistory(historyVo);
        }

        if(templateVos != null) {
            for(TemplateVo templateVo : templateVos) {
                // 커버,본문 버전 셋팅
                historyVo.setTmplVer(getTmplVer(templateVos, templateVo.getSeg()));
                historyVo.setTemplate(templateVo.getTemplate());
                historyVo.setContsNo(templateVo.getContsNo());
                historyVo.setSeg(templateVo.getSeg());
                historyVo.setKakaoButtons(templateVo.getKakaoButtons());
                ecareTemplateHistoryDao.insertEcareTemplateHistory(historyVo);
            }
        }
        return 1;
    }

    /**
     * 이케어 템플릿의 버전정보를 추출한다.
     */
    protected int getTmplVer(List<TemplateVo> templateVos, String seg) {
        if(templateVos != null) {
            for(TemplateVo templateVo : templateVos) {
                if(StringUtil.equalsIgnoreCase(seg, templateVo.getSeg())) {
                    return templateVo.getTmplVer();
                }
            }
        }
        return 0;
    }


}
