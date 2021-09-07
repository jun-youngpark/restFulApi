package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.campaign.model.SendResultVo;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReactionResult2Vo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportBasicVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportReceiveVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignSendResultVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;

/**
 * NVSENDRESULT 테이블 DAO 클래스
 */
@Repository
public class SendResultDao extends BaseDao implements ApplicationContextAware {
    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public int insertSendResultByMessage(MessageVo messageVo) {
        return insert("SendResult.insertSendResultByMessage", messageVo);
    }

    /**
     * 캠페인 리스트 업데이트
     *
     * @param campaignNo
     * @return
     */
    public int updateCampaignList(int campaignNo) {
        return update("SendResult.updateCampaignList", campaignNo);
    }

    public int selectSendResultCountByPk(Map<String, Object> paramMap) {
        Integer count = (Integer) selectOne("SendResult.selectSendResultCountByPk", paramMap);
        return (count == null) ? 0 : count;
    }

    /**
     * 캠페인 에러 상세내역을 가져온다.
     *
     * @return
     */
    public String getCampaignError(Map<String, Object> paramMap) {
        return (String) selectOne("SendResult.getCampaignError", paramMap);
    }

    /**
     * 멀티채널 캠페인을 생성할 때 상위 캠페인이 종료 상태라면 NVSENDRESULT.RESULT_SEQ 값을 조회하여<br>
     * 대상자 SQL에 사용한다.
     *
     * @param campaignNo 상위 캠페인 번호
     * @return
     */
    public long selectResultSeq(int campaignNo) {
        return (Long) selectOne("SendResult.selectResultSeq", campaignNo);
    }

    public int emSuspendResultSeq(int campaignNo) {
        return (Integer) selectOne("SendResult.emSuspendResultSeq", campaignNo);
    }

    /**
     * 캠페인 요약 분석 리턴메일
     *
     * @param campaignNo
     * @return
     */
    public int getCampaignSummaryReturn(int campaignNo) {
        return (Integer) selectOne("SendResult.getCampaignSummaryReturn", campaignNo);
    }

    public CampaignSendResultVo selectSendResult(ScenarioInfoVo scenarioInfoVo) {
        // 다국어 처리를 위해 공통 코드 테이블을 조회하는 언어를 넘겨준다.
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        scenarioInfoVo.setLanguage(localeChangeInterceptor.getLanguage());
        return (CampaignSendResultVo) selectOne("SendResult.selectCampaignSendResult", scenarioInfoVo);
    }

    @SuppressWarnings("unchecked")
    public List<Integer> selectCampaignNoList(Map<String, Object> paramMap) {
        return selectList("SendResult.selectCampaignNoByTagNo", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<CampaignSendResultVo> selectSendResultList(Map<String, Object> paramMap) {
        return selectList("SendResult.selectCampaignSendResultList", paramMap);
    }

    /**
     * 캠페인 월별 통계 데이터를 가져온다.
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendResultVo> selectCampaignMonthlyStat(Map<String, Object> paramMap) {
        return selectList("SendResult.selectCampaignMonthlyStat", paramMap);
    }

    /**
     * 캠페인 전체요약 총 발송 정보 가져오기 - 그래프에서 데이터를 이용하기 위해 사용
     *
     * @param paramMap
     * @return
     */
    public CampaignSendResultVo getCampaignReportAllCntInfo(Map<String, Object> paramMap) {
        return (CampaignSendResultVo) selectOne("SendResult.getCampaignSendResultAll", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<CampaignReactionResult2Vo> selectReactionResult2List(Map<String, Object> paramMap) {
        return selectList("SendResult.selectSummaryReactionResult2List", paramMap);
    }

    /**
     * 캠페인 월별 통계 데이터를 가져온다. TOTAL
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendResultVo> selectCampaignTOTMonthlyStat(Map<String, Object> paramMap) {
        return selectList("SendResult.selectCampaignTOTMonthlyStat", paramMap);
    }

    /**
     * 캠페인 일별 통계 데이터를 가져온다. (FAX OR MAIL)
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendResultVo> selectCampaignDailyStat(Map<String, Object> paramMap) {
        return selectList("SendResult.selectCampaignDailyStat", paramMap);
    }

    /**
     * 캠페인 일별 통계 데이터를 가져온다. (TOTAL)
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendResultVo> selectCampaignTOTDailyStat(Map<String, Object> paramMap) {
        return selectList("SendResult.selectCampaignTOTDailyStat", paramMap);
    }


    /**
     * 캠페인 리포트 전체보기 요약분석 발송결과 리스트
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportVo> getCampaignSummaryAllList(Map<String, Object> paramMap) {
        return selectList("SendResult.getCampaignSummaryAllList", paramMap);
    }

    /**
     * 캠페인 리포트 전체요약 수신확인 결과 리스트
     *
     * @param scenarioNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportReceiveVo> getCampaignSummaryReceiveList(int scenarioNo, String lang) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioNo);
        paramMap.put("lang", lang);

        return selectList("SendResult.getCampaignSummaryReceiveList", paramMap);
    }

    /**
     * 캠페인 리포트 리스트 카운트
     *
     * @param campaignReportVo
     * @return
     */
    public int getCampaignReportListCount(CampaignReportVo campaignReportVo) {
        return (Integer) selectOne("SendResult.getCampaignReportListCount", campaignReportVo);
    }

    /**
     * 캠페인 리포트 리스트 엑셀 다운로드
     *
     * @param campaignReportVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportVo> selectCampaignReportListForExcel(CampaignReportVo campaignReportVo) {
        return selectList("SendResult.selectCampaignReportListForExcel", campaignReportVo);
    }

    /**
     * 캠페인 리포트 리스트
     *
     * @param campaignReportVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportVo> getCampaignReportList(CampaignReportVo campaignReportVo) {
        return selectList("SendResult.getCampaignReportList", campaignReportVo);
    }

    /**
     * 캠페인 리포트 기본정보 가져오기
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportBasicVo getCampaignReportBasicInfo(int campaignNo) {
        // 해당 언어의 코드명을 NV_CD_MST 테이블에서 가져온다.
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("language", localeChangeInterceptor.getLanguage());

        CampaignReportBasicVo tmp = (CampaignReportBasicVo) selectOne("SendResult.getCampaignReportBasicInfo", paramMap);
        security.securityObject(tmp, "DECRYPT");

        return tmp;
    }

    /**
     * 캠페인 리포트 기본정보 가져오기
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportBasicVo getCampaignSMSReportBasicInfo(int campaignNo) {
        // 해당 언어의 코드명을 NV_CD_MST 테이블에서 가져온다.
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("language", localeChangeInterceptor.getLanguage());

        CampaignReportBasicVo tmp = (CampaignReportBasicVo) selectOne("SendResult.getCampaignSMSReportBasicInfo", paramMap);
        security.securityObject(tmp, "DECRYPT");

        return tmp;
    }

    /**
     * 캠페인 친구톡 리포트 기본정보 가져오기
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportBasicVo getCampaignKakaoReportBasicInfo(int campaignNo) {
        // 해당 언어의 코드명을 NV_CD_MST 테이블에서 가져온다.
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("language", localeChangeInterceptor.getLanguage());

        CampaignReportBasicVo tmp = (CampaignReportBasicVo) selectOne("SendResult.getCampaignKakaoReportBasicInfo", paramMap);
        security.securityObject(tmp, "DECRYPT");

        return tmp;
    }

    /**
     * 캠페인 리포트 요약분석 발송결과
     *
     * @param map
     * @return
     */
    public CampaignReportVo getCampaignSummaryDetail(Map<String, Object> map) {
        return (CampaignReportVo) selectOne("SendResult.getCampaignSummaryDetail", map);
    }

    /**
     * 캠페인 리포트 요약분석 발송결과 - 팩스 없어서 추가
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportVo> getCampaignFaxSummaryDetail(Map<String, Object> map) {
        return selectList("SendResult.getCampaignFaxSummaryDetail", map);
    }

    /**
     * 캠페인 리포트 요약분석 발송결과
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportVo> getCampaignSMSSummaryDetail(Map<String, Object> map) {
        return selectList("SendResult.getCampaignSMSSummaryDetail", map);
    }

    /**
     * 캠페인 카카오톡  리포트 요약분석 발송결과
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportVo> getCampaignKakaoSummaryDetail(Map<String, Object> map) {
        return selectList("SendResult.getCampaignKakaoSummaryDetail", map);
    }

    /**
     * 캠페인 리포트 - 도메인 분석 리스트
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportVo> getCampaignReportDomainList(int scenarioNo, int campaignNo, String lang) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioNo);
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("lang", lang);

        return selectList("SendResult.getCampaignReportDomainList", paramMap);
    }

}