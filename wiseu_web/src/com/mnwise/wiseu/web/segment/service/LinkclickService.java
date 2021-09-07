package com.mnwise.wiseu.web.segment.service;

import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.campaign.dao.LinkTraceDao;
import com.mnwise.wiseu.web.campaign.dao.ScenarioDao;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.segment.dao.LinkResultDao;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.LinkclickVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;

@Service
public class LinkclickService extends BaseService {
    @Autowired private LinkResultDao linkResultDao;
    @Autowired private LinkTraceDao linkTraceDao;
    @Autowired private ScenarioDao scenarioDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;

    public List<LinkclickVo> getServiceList(String serviceType, Object scenarioVo) {
        PagingUtil.setPagingRowcount(scenarioVo);
        if(serviceType.equals("EM")) {
            return scenarioDao.selectCampaignList((ScenarioVo) scenarioVo);
        }

        return null;
    }

    public int getServiceListTotalCount(String serviceType, ScenarioVo scenarioVo) {
        int totalCount = 0;
        if(serviceType.equals("EM")) {
            totalCount = scenarioDao.selectCampaignListTotalCount(scenarioVo);
        }
        return totalCount;
    }

    @SuppressWarnings("rawtypes")
    public List getServiceLinkInfo(String serviceType, int serviceNo) {
        List list = null;

        if(serviceType.equals("EM")) {
            list = linkTraceDao.selectLinkCampaignInfo(serviceNo);
        }

        return list;
    }

    public int getServiceLinkTargetCount(LinkclickVo linkClickVo) {
        int totalCount = 0;
        if(linkClickVo.getServiceType().equals("EM")) {
            totalCount = linkResultDao.selectCampaignTargetCount(linkClickVo);
        }

        return totalCount;
    }

    /**
     * 링크클릭 반응 대상자 세그먼트 생성 2010-01-05 hjChang 수정
     */
    public int setRegistSegmentInfo(LinkclickVo linkclickVo, String mode, String userId, String grpCd) {
        SegmentVo segmentVo = new SegmentVo();
        StringBuffer query = new StringBuffer();
        int segmentNo = -1;
        StringBuffer plinkSeq = new StringBuffer();

        // 반응대상지 필터 쿼리 생성.
        //SECUDB NONE
        //NVLINKRESULT : 해당 필드 없음
        query.append("SELECT CUSTOMER_ID FROM  NVLINKRESULT");
        query.append(" WHERE  CAMPAIGN_NO = ");
        query.append(linkclickVo.getServiceNo());
        query.append(" AND (LINK_SEQ IN ( ");

        for(int i = 0; i < linkclickVo.getLinkSeqArray().length; i++) {
            plinkSeq.append(linkclickVo.getLinkSeqArray()[i]);
            if(i < linkclickVo.getLinkSeqArray().length - 1) {
                plinkSeq.append(",");
            }
        }

        query.append(plinkSeq.toString());
        query.append(" ) ) GROUP BY CUSTOMER_ID");

        // 업데이트의 경우에는 filter 쿼리만 업댓하고 끝.
        if(mode.equals("UPDATE")) {
            segmentVo.setSegmentNo(linkclickVo.getSegmentNo());
            segmentVo.setSqlfilter(query.toString());
            segmentDao.updateSqlfilter(segmentVo);
            return linkclickVo.getSegmentNo();
        }

        // 해당 캠페인의 세그먼트 정보를 가져옴.
        SegmentVo pSegmentVo = segmentDao.selectSegmentByCampaignNo(linkclickVo.getServiceNo());
        segmentVo.setSegmentNm(pSegmentVo.getSegmentNm() + "[반응]");
        segmentVo.setPsegmentNo(pSegmentVo.getSegmentNo());
        segmentVo.setUserId(userId);
        segmentVo.setGrpCd(grpCd);
        segmentVo.setSegmentSize(linkclickVo.getSegmentSize());
        segmentVo.setSqlfilter(query.toString());
        segmentVo.setPlinkSeq(plinkSeq.toString());
        segmentVo.setTagNo(pSegmentVo.getTagNo());
        segmentVo.setSegmentType("N");
        segmentVo.setSegType("L");

        // SELECT - INSERT 쿼리를 이용하여 세그먼트 생성.
        segmentNo = segmentDao.selectNextSegmentNo();
        segmentVo.setSegmentNo(segmentNo);
        segmentDao.insertLinkClickSegment(segmentVo);

        // 기존 세그먼트의 시맨틱 정보 복사.
        semanticDao.copySemantic(segmentVo.getPsegmentNo(), segmentVo.getSegmentNo());

        return segmentNo;
    }

    public LinkclickVo getCheckedLinkClickInfo(String serviceType, int segmentNo) {
        LinkclickVo linkClickVo = new LinkclickVo();
        linkClickVo.setSegmentNo(segmentNo);
        linkClickVo.setServiceType(serviceType);

        SegmentVo segmentVo = null;
        if(linkClickVo.getServiceType().equals("EM")) {
            segmentVo = segmentDao.selectSegmentByPk(segmentNo);
        }

        if(segmentVo == null)
            segmentVo = new SegmentVo();

        String plinkSeq = segmentVo.getPlinkSeq();
        if(plinkSeq != null) {
            int linkSeqNo = StringUtil.countMatches(plinkSeq, ",") + 1;
            int[] linkSeqArray = new int[linkSeqNo];

            StringTokenizer st = new StringTokenizer(plinkSeq, ",");
            int i = 0;
            while(st.hasMoreElements()) {
                linkSeqArray[i] = Integer.parseInt(st.nextToken());
                i++;
            }
            linkClickVo.setLinkSeqArray(linkSeqArray);
        }
        return linkClickVo;
    }

    public SegmentVo selectSegmentInfo(int segmentNo) {
        return segmentDao.selectSegmentByPk(segmentNo);
    }
}
