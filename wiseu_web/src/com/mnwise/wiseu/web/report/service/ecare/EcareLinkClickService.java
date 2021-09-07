package com.mnwise.wiseu.web.report.service.ecare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.EcareLinkTraceDao;
import com.mnwise.wiseu.web.report.model.LinkTraceResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

@Service
public class EcareLinkClickService extends BaseService {
    @Autowired private EcareLinkTraceDao ecareLinkTraceDao;

    public Map<String, Object> selectLinkTraceResultList(EcareScenarioInfoVo scenarioInfoVo, int sendCnt, int openCnt) {
        List<LinkTraceResultVo> linkTraceResultList = ecareLinkTraceDao.selectLinkTraceResultList(scenarioInfoVo);
        // 링크클릭 반응결과의 발송대비, 오픈 대비를 위한 send 및 open count 값을 세팅한다.

        int totalUniqueLinkClickCnt = 0;
        int totalAllLinkClickCnt = 0;

        if(linkTraceResultList != null) {
            for(int i = 0; i < linkTraceResultList.size(); i++) {
                ((LinkTraceResultVo) linkTraceResultList.get(i)).setSendCnt(sendCnt);
                ((LinkTraceResultVo) linkTraceResultList.get(i)).setOpenCnt(openCnt);

                totalUniqueLinkClickCnt += ((LinkTraceResultVo) linkTraceResultList.get(i)).getUniqueLinkCnt();
                totalAllLinkClickCnt += ((LinkTraceResultVo) linkTraceResultList.get(i)).getAllLinkCnt();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("linkTraceResultList", linkTraceResultList);
        map.put("totalUniqueLinkClickCnt", new Integer(totalUniqueLinkClickCnt));
        map.put("totalAllLinkClickCnt", new Integer(totalAllLinkClickCnt));

        return map;
    }
}
