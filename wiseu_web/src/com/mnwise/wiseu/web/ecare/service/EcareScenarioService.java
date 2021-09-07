package com.mnwise.wiseu.web.ecare.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.campaign.dao.TestUserDao;
import com.mnwise.wiseu.web.common.dao.CommonDao;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.common.service.AbstractScenarioService;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.ecare.dao.AddQueryDao;
import com.mnwise.wiseu.web.ecare.dao.CycleItemDao;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerDao;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareDivideScheduleDao;
import com.mnwise.wiseu.web.ecare.dao.EcareKmMapDao;
import com.mnwise.wiseu.web.ecare.dao.EcareLinkTraceDao;
import com.mnwise.wiseu.web.ecare.dao.EcareMultipartFileDao;
import com.mnwise.wiseu.web.ecare.dao.EcareScenarioDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendResultDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateHistoryDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTraceInfoDao;
import com.mnwise.wiseu.web.ecare.dao.EcmScheduleDao;
import com.mnwise.wiseu.web.ecare.model.CycleItemVo;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.model.EcareScheduleVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.template.dao.KakaoProfileDao;
import com.mnwise.wiseu.web.template.model.KakaoProfile;
import static com.mnwise.wiseu.web.common.util.CodeUtil.getErrCdByChannel;
/**
 * 이케어 Service
 */
@Service
public class EcareScenarioService extends AbstractScenarioService {
    private static final Logger log = LoggerFactory.getLogger(EcareScenarioService.class);

    @Autowired private CommonDao commonDao;
    @Autowired private CycleItemDao cycleItemDao;
    @Autowired private EcareDao ecareDao;
    @Autowired private EcareKmMapDao ecareKmMapDao;
    @Autowired private EcareLinkTraceDao ecareLinkTraceDao;
    @Autowired private EcareMultipartFileDao ecareMultipartFileDao;
    @Autowired private EcareScenarioDao ecareScenarioDao;
    @Autowired private EcareSendResultDao ecareSendResultDao;
    @Autowired private EcareTemplateDao ecareTemplateDao;
    @Autowired private EcareTemplateHistoryDao ecareTemplateHistoryDao;
    @Autowired private EcareTraceInfoDao ecareTraceInfoDao;
    @Autowired private EcmScheduleDao ecmScheduleDao;
    @Autowired private EcMsgHandlerDao ecMsgHandlerDao;
    @Autowired private EcMsgHandlerHistoryDao ecMsgHandlerHistoryDao;
    @Autowired private KakaoProfileDao kakaoProfileDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private ServerInfoDao serverInfoDao;
    @Autowired private TagDao tagDao;
    @Autowired private TestUserDao testUserDao;
    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private AddQueryDao addQueryDao;
    
    //[20210727] [ljh] [분할발송 추가]
    @Autowired private EcareDivideScheduleDao ecareDivideScheduleDao;

    @Value("${db.server}")
    private String dbServer;
    // DBMS (재발송 쿼리 생성 시 쿼리를 동적으로 만들기 때문에 DBMS 정보가 필요함)
    @Value("${use.dbms}")
    private String useDbms;
    // 재발송 타게팅 시 키를 아이디로만 할지, 아이디 + 이메일로 할지를 구분
    @Value("${multisend.key.idemail.use:off}")
    private String useMultisendKeyIdemail;

    /**
     * 이케어 사니리오를 생성한다.
     *
     *
     * @param ecareVo
     * @param userVo 로그인한 유저 정보
     * @return 이케어 시나리오
     */
    public EcareScenarioVo createEcareScenario(EcareVo ecareVo, UserVo userVo) {
        String createDt = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String createTm = DateFormatUtils.format(new Date(), "HHmmss");

        EcareScenarioVo ecareScenarioVo = new EcareScenarioVo();
        ecareScenarioVo.setCreateDt(createDt);
        ecareScenarioVo.setCreateTm(createTm);
        ecareScenarioVo.setEcareVo(ecareVo);
        ecareScenarioVo.setGrpCd(userVo.getGrpCd());
        ecareScenarioVo.setHandlerType("G");
        ecareScenarioVo.setLastUpdateDt(createDt);
        ecareScenarioVo.setLastUpdateTm(createTm);
        ecareScenarioVo.setScenarioNm(ecareVo.getEcareNm());
        ecareScenarioVo.setScenarioNo(ecareScenarioDao.selectNewScenarioNo());
        ecareScenarioVo.setServiceType("S");
        ecareScenarioVo.setSubType(ecareVo.getSubType());
        ecareScenarioVo.setTagNo(1);
        ecareScenarioVo.setUserId(userVo.getUserId());

        ecareScenarioDao.insertEcareScenario(ecareScenarioVo);

        return ecareScenarioVo;
    }

    /**
     * 이케어를 삭제한다.
     *
     * @param ecareNo
     * @return
     */
    public void deleteEcareScenarioCommon(int ecareNo) throws Exception  {
        Map<String, Object> map = new HashMap<>();

        Integer ecareNoObject = ecareSendResultDao.selectEcareSendResult(ecareNo);

        EcareVo ecareVo = new EcareVo();
        ecareVo = ecareDao.selectEcareForDelete(ecareNo);
        String serviceType = ecareVo.getServiceType();

        // 이 이케어 번호에 대한 스케쥴번호를 알아야 한다.
        int ecareScheduleNo = ecareVo.getEcmScheduleNo();

        map.put("ecareNo", new Integer(ecareNo));
        map.put("userId", ecareVo.getUserId());

        if(ecareNoObject == null) {
            log.info("이케어 번호 (" + ecareNoObject + ") 번이 삭제됩니다.");

            ecareMultipartFileDao.deleteEcareMultipartFile(ecareNo);
            ecareTraceInfoDao.deleteEcareTraceInfo(ecareNo);
            ecareLinkTraceDao.deleteEcareLinkTrace(ecareNo);
            ecMsgHandlerDao.deleteEcMsgHandlerByPk(ecareNo);
            ecareTemplateDao.deleteEcareTemplate(ecareNo);
            ecareKmMapDao.deleteEcareKMMap(ecareNo);
            testUserDao.deleteEcareTestUser(map);
            ecareDao.deleteEcareMsgByPk(ecareNo);
            // 미발송 이케어 삭제 시 템플릿, 핸들러 히스토리도 삭제한다.
            ecareTemplateHistoryDao.deleteEcareTemplateHistoryAll(ecareNo);
            ecMsgHandlerHistoryDao.deleteEcmsgHandlerHistoryAll(ecareNo);
            addQueryDao.deleteAddQuery(ecareNo);    //추가쿼리삭제
            if(serviceType != null && (serviceType.equals("S") || serviceType.equals("B"))) {
                if(ecareScheduleNo != 0) {
                    cycleItemDao.deleteEcareCycleItemInfo(ecareScheduleNo);
                    ecmScheduleDao.deleteEcmScheduleByPk(ecareScheduleNo);
                }
            }
        } else {
            throw new Exception("[삭제 취소]이미 발송된 내역이 있습니다.");
        }
    }

    /**
     * 선택한 채널에 따라 이케어 번호를 설정한다.
     *
     * @param key
     * @param ecareScenarioVo
     * @return
     */
    public EcareScenarioVo setEcareNo(String key, EcareScenarioVo ecareScenarioVo) {
        if(key.equals("M") && null != ecareScenarioVo.getMailCd()) {
            ecareScenarioVo.getEcareVo().setEcareNo(Integer.parseInt(ecareScenarioVo.getMailCd()));
        } else if(key.equals("S") && null != ecareScenarioVo.getSmsCd()) {
            ecareScenarioVo.getEcareVo().setEcareNo(Integer.parseInt(ecareScenarioVo.getSmsCd()));
        } else if(key.equals("T") && null != ecareScenarioVo.getMmsCd()) {
            ecareScenarioVo.getEcareVo().setEcareNo(Integer.parseInt(ecareScenarioVo.getMmsCd()));
        } else if(key.equals("F") && null != ecareScenarioVo.getFaxCd()) {
            ecareScenarioVo.getEcareVo().setEcareNo(Integer.parseInt(ecareScenarioVo.getFaxCd()));
        } else if(key.equals("A") && null != ecareScenarioVo.getAlimtalkCd()) {
            ecareScenarioVo.getEcareVo().setEcareNo(Integer.parseInt(ecareScenarioVo.getAlimtalkCd()));
        } else if(key.equals("P") && null != ecareScenarioVo.getPushCd()) {
            ecareScenarioVo.getEcareVo().setEcareNo(Integer.parseInt(ecareScenarioVo.getPushCd()));
        }
        return ecareScenarioVo;
    }

    /**
     * 시나리오에 딸린 이케어 채널 정보를 가져온다.
     *
     * @param ecareNo
     * @return
     */
    public List<EcareVo> getEcareScenariochannelInfo(int ecareNo) {
        return ecareDao.getEcareChannelInfo(ecareNo);
    }

    /**
     * 이케어를 삭제한다.
     *
     * @param ecareScenarioVo
     */
    public void setDeleteEcare(EcareScenarioVo ecareScenarioVo) throws Exception{
        deleteEcareScenarioCommon(ecareScenarioVo.getEcareVo().getEcareNo());
        @SuppressWarnings("rawtypes")
        List rValue = ecareDao.getEcareChannelInfo(ecareScenarioVo.getScenarioNo());

        // 시나리오에 물려 있는 이케어가 하나도 없는 경우라면 시나리오도 삭제한다.
        if(rValue.size() == 0) {
            ecareScenarioDao.deleteEcareScenario(ecareScenarioVo);
        }
    }

    /**
     * 이케어 상세 정보를 가져온다.
     *
     * @param ecareScenarioNo
     * @param ecareNo
     * @param userId
     * @return
     * @throws Exception
     */
    public EcareScenarioVo getEcareScenario2StepInfo(int ecareScenarioNo, int ecareNo, String userId, String language) throws Exception {
        EcareScenarioVo ecareScenarioVo = ecareScenarioDao.selectEcareScenarioDetailInfo(ecareScenarioNo, ecareNo, language);
        final EcareVo ecareVo = ecareScenarioVo.getEcareVo();
        updateSenderInfo(ecareVo, ecareScenarioVo); //발신자 정보가 없을경우 업데이트
        EcareScheduleVo ecareScheduleVo;
        if (ecareVo.getEcmScheduleNo() > 0) {
            ecareScheduleVo = ecmScheduleDao.selectEcmScheduleByPk(ecareVo.getEcmScheduleNo());
            if (ecareScheduleVo == null) {
                ecareScheduleVo = new EcareScheduleVo();
            }
            ecareVo.setCycleItemList(cycleItemDao.selectCycleItemInfo(ecareVo.getEcmScheduleNo()));
        } else {
            ecareScheduleVo = new EcareScheduleVo();
        }
        //[20210727] [ljh] [스케줄일 경우 분할발송 정보를 가져온다]
        
        if(ecareScenarioVo.getSubType().equalsIgnoreCase("S")) {
            ecareScenarioVo.setSegmentSize(segmentDao.selectSegmentByPk(ecareScenarioVo.getSegmentNo()).getSegmentSize());
            
            // 분할예약 정보를 가져온다.
            if("Y".equals(ecareVo.getDivideYn())) {
                ecareScenarioVo.getEcareVo().setEcareDivideScheduleList((ecareDivideScheduleDao.getEcareDivideSchedule(ecareVo.getEcareNo())));
            }
        }
        
        // 발송 날짜
        if (StringUtil.isBlank(ecareScheduleVo.getSendStartDt())) {
            ecareScheduleVo.setSendStartDt(DateUtil.getNowDate());
        }

        if (StringUtil.isBlank(ecareScheduleVo.getSendEndDt())) {
            ecareScheduleVo.setSendEndDt(DateUtil.addDays(ecareScheduleVo.getSendStartDt(), 7, "yyyyMMdd"));
        }

        String invokeTm = StringUtil.defaultIfBlank(ecareScheduleVo.getInvokeTm(), DateUtil.getNowDateTime("HHmmss"));
        ecareScheduleVo.setInvokeTm(invokeTm.substring(0, 4) + "00");

        ecareVo.setEcareScheduleVo(ecareScheduleVo);

        return ecareScenarioVo;
    }

    /**
     * 이케어 상세 정보를 변경한다.
     *
     * @param ecareScenarioVo
     */
    public void setRegistEcareScenario2StepInfo(EcareScenarioVo ecareScenarioVo) {
        EcareVo ecareVo = ecareScenarioVo.getEcareVo();
        ecareVo.setSegmentNo(ecareScenarioVo.getSegmentNo());
        ecareVo.setTargetCnt(ecareScenarioVo.getSegmentSize());

        String nowDt = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String nowTm = DateFormatUtils.format(new Date(), "HHmmss");

        ecareScenarioVo.setLastUpdateDt(nowDt);
        ecareScenarioVo.setLastUpdateTm(nowTm);

        ecareVo.setLastUpdateDt(nowDt);
        ecareVo.setLastUpdateTm(nowTm);

        ecareDao.updateEcareScenario2StepCommonInfo(ecareVo);
        ecareScenarioDao.updateEcareScenario2StepInfo(ecareScenarioVo);

        ecareDao.updateEcare2StepInfo(ecareVo);

        // 반응추적기간 인서트 하기~ 위해서 있는지 없는지 우선 검사하자.
        String termType = ecareTraceInfoDao.selectEcareTraceInfo(ecareVo.getEcareNo());
        if(termType == null) {
            ecareTraceInfoDao.insertEcareTraceInfoByEcare(ecareVo);
        } else {
            ecareTraceInfoDao.updateEcareTraceInfo(ecareVo);
        }
    }

    /**
     * 이케어 수행 단계 정보를 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public EcareScenarioVo getEcareScenario3StepInfo(EcareScenarioVo scenarioVo) {
        EcareScenarioVo ecareScenarioVo = ecareScenarioDao.selectEcareScenario1StepInfo(scenarioVo);

        List<EcareVo> ecareList = ecareScenarioVo.getEcareList();
        List<String> channelList = new ArrayList<>();
        if(ecareList != null) {
            for(int i = 0, n = ecareList.size(); i < n; i++) {
                EcareVo ecareVo = ecareList.get(i);
                final String channelType = ecareVo.getChannelType();
                channelList.add(channelType);
                final int ecareNo = ecareVo.getEcareNo();

                if(channelType.equals(Channel.MAIL)) {
                    ecareScenarioVo.setMailCd(String.valueOf(ecareNo));
                } else if(channelType.equals(Channel.SMS)) {
                    ecareScenarioVo.setSmsCd(String.valueOf(ecareNo));
                } else if(channelType.equals(Channel.LMSMMS)) {
                    ecareScenarioVo.setMmsCd(String.valueOf(ecareNo));
                } else if(channelType.equals(Channel.FAX)) {
                    ecareScenarioVo.setFaxCd(String.valueOf(ecareNo));
                }else if(ChannelUtil.isKakao(channelType)) {
                    ecareScenarioVo.setAlimtalkCd(String.valueOf(ecareNo));
                }
            }
        }
        ecareScenarioVo.setChannels(channelList.toArray(new String[channelList.size()]));

        // 스케쥴 정보 가져오기
        if(ecareList != null) {
            for(int i = 0, n = ecareList.size(); i < n; i++) {
                EcareVo ecareVo = ecareList.get(i);

                EcareScheduleVo ecareScheduleVo;
                if(ecareVo.getEcmScheduleNo() > 0) {
                    ecareScheduleVo = ecmScheduleDao.selectEcmScheduleByPk(ecareVo.getEcmScheduleNo());
                    if(ecareScheduleVo == null) {
                        ecareScheduleVo = new EcareScheduleVo();
                    }
                    ecareVo.setCycleItemList(cycleItemDao.selectCycleItemInfo(ecareVo.getEcmScheduleNo()));
                } else {
                    ecareScheduleVo = new EcareScheduleVo();
                }

                // 발송 날짜
                String sendStartString = null;
                Date date = new Date();
                if(!StringUtil.isEmpty(ecareScheduleVo.getSendStartDt())) {
                    DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
                    sendStartString = ecareScheduleVo.getSendStartDt() + " " + ecareScheduleVo.getInvokeTm();
                    try {
                        date = df.parse(sendStartString);
                    } catch(ParseException e) {
                        log.error(null, e);
                    }
                }

                sendStartString = DateFormatUtils.format(date, "yyyy-MM-dd HHmm00");
                ecareScheduleVo.setSendStartDt(sendStartString.split(" ")[0]);

                date = DateUtil.addDays(date, 7);
                String sendEndString;
                if(!StringUtil.isEmpty(ecareScheduleVo.getSendEndDt())) {
                    DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
                    sendEndString = ecareScheduleVo.getSendEndDt() + " " + ecareScheduleVo.getInvokeTm();
                    try {
                        date = df.parse(sendEndString);
                    } catch(ParseException e) {
                        log.error(null, e);
                    }
                }

                sendEndString = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:00");
                ecareScheduleVo.setSendEndDt(sendEndString.split(" ")[0]);
                ecareScheduleVo.setInvokeTm(sendEndString.split(" ")[1]);

                ecareVo.setEcareScheduleVo(ecareScheduleVo);

            }
        }

        return ecareScenarioVo;
    }

    /**
     * 재발송(발송성공,실패,수신확인 등등) 이케어를 생성한다.
     *
     * @param paramScenarioVo
     * @param resendMode
     * @param serverInfo
     * @return
     * @throws Exception
     */
    public int setRegistResendEcare(EcareScenarioVo paramScenarioVo, String resendMode, ServerInfoVo serverInfo) throws Exception {
        EcareScenarioVo scenarioVo = ecareScenarioDao.selectEcareScenario1StepInfo(paramScenarioVo);
        // 사용자 정보를 가져오기 위함...
        scenarioVo.setUserVo(paramScenarioVo.getUserVo());
        // 이케어 복사(추후 만들어 질지 모름)와 함께 사용하기 위해서 NewScenarioNo를 현재 ScenarioNo로 세팅함.
        scenarioVo.setNewScenarioNo(scenarioVo.getScenarioNo());
        List<EcareVo> ecareList = scenarioVo.getEcareList();
        int newEcareNo = 0;

        if(ecareList != null) {
            // 하나의 이케어를 가져온다. WHERE SCENARIO_NO, ECARE_NO로 조회
            for(int i = 0, n = ecareList.size(); i < n; i++) {
                EcareVo ecareVo = ecareList.get(i);
                scenarioVo.setEcareVo(ecareVo);
                // 재발송 요청 시 받은 result_seq를 scenarioVo에 넣어준다.
                scenarioVo.getEcareVo().setResultSeq(paramScenarioVo.getEcareVo().getResultSeq());
                HandlerVo handlerVo = ecMsgHandlerDao.selectEcMsgHandlerByPk(ecareVo.getEcareNo());
                List<TemplateVo> templateList = ecareTemplateDao.selectEditorEcareTemplate(ecareVo.getEcareNo());
                scenarioVo.getEcareVo().setHandlerVo(handlerVo);
                scenarioVo.getEcareVo().setTemplateList(templateList);
                if(resendMode.equals("resendSuccess") || resendMode.equals("resendFail")) {
                    scenarioVo.getEcareVo().setRelationType("R");
                } else {
                    scenarioVo.getEcareVo().setRelationType("L");
                }

                newEcareNo = resendEcare(scenarioVo, resendMode, serverInfo);
            }
        }
        return newEcareNo;
    }

    /**
     * 재발송(발송성공,실패,수신확인 등등) 이케어를 생성한다.
     *
     * @param scenarioVo
     * @param resendMode
     * @param serverInfo
     * @return
     */
    public int resendEcare(EcareScenarioVo scenarioVo, String resendMode, ServerInfoVo serverInfo) {
        int parentEcareNo = scenarioVo.getEcareVo().getEcareNo();
        int nextEcmScheduleNo = ecmScheduleDao.selectNextEcmScheduleNo();
        int nextEcareNo = ecareDao.selectNextEcareNo();

        // 새로운 이케어 번호, 세그먼트 번호를 담는다.
        scenarioVo.getEcareVo().setNewEcareNo(nextEcareNo);
        // scenarioVo.setSegmentNo(0); //세그먼트가 만들어지기 전에 초기값
        scenarioVo.setSegmentSize(0); // 세그먼트가 만들어지기 전에 초기값

        scenarioVo.getEcareVo().getEcareScheduleVo().setNewEcmScheduleNo(nextEcmScheduleNo);
        scenarioVo.getEcareVo().getEcareScheduleVo().setEcmScheduleNo(scenarioVo.getEcareVo().getEcmScheduleNo());
        scenarioVo.getEcareVo().getCycleItemVo().setEcmScheduleNo(nextEcmScheduleNo);
        scenarioVo.getEcareVo().getCycleItemVo().setCycleItem("ONTIME");
        scenarioVo.getEcareVo().getCycleItemVo().setCheckYn("Y");
        // NVECAREMSG COPY
        security.securityObject(scenarioVo.getEcareVo(), "ENCRYPT");
        ecareDao.copyEcareForResend(scenarioVo);

        // NVECMSCHEDULE COPY
        ecmScheduleDao.copyEcmSchedule(scenarioVo.getEcareVo().getEcareScheduleVo());
        // NVCYCLEITEM INSERT
        cycleItemDao.insertCycleItem(scenarioVo.getEcareVo().getCycleItemVo());
        // NVECARETRACEINFO COPY
        ecareTraceInfoDao.copyEcareTraceInfo(scenarioVo.getEcareVo());
        // NVECARELINKTRACE COPY
        ecareLinkTraceDao.copyEcareLinkTrace(scenarioVo.getEcareVo());
        // NVECMSGHANDLER COPY
        ecMsgHandlerDao.copyEcmHandler(scenarioVo.getEcareVo());
        // TESTUSER COPY
        testUserDao.copyTestUserByEcare(scenarioVo.getEcareVo());
        // scenarioVo.getEcareVo());
        // NVTEMPLATE COPY
        if(scenarioVo.getEcareVo().getTemplateList() != null) {
            @SuppressWarnings("rawtypes")
            List tempalteList = scenarioVo.getEcareVo().getTemplateList();
            for(int i = 0; i < tempalteList.size(); i++) {
                TemplateVo templateVo = (TemplateVo) tempalteList.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("newEcareNo", new Integer(scenarioVo.getEcareVo().getNewEcareNo()));
                map.put("templateVo", templateVo);
                ecareTemplateDao.copyEcareTemplate(map);
            }
        }

        // 세그먼트 관리 (XDB를 사용해야 하는데, 일단은 오랜 기간 DB 조회를 하게 되는 로직을 아래로 빼 놓았다.
        String cntSql = null;
        String sqlHead = null;
        String relationType = "R";

        // 재발송 쿼리 생성 시 문자열 append가 필요한데 DBMS마다 사용법이 다르므로 DBMS 정보를 기준으로 생성한다.
        // 기본값은 ORACLE이다.
        String appendString = "||";
        if(useDbms != null) {
            if(useDbms.equalsIgnoreCase("MSSQL") || useDbms.equalsIgnoreCase("SYBASE")) {
                appendString = "+";
            }
        }

        if(resendMode.equals("resendSuccess")) {
            //SECUDB NONE
            //NVECARESENDLOG : 해당 필드 없음
            sqlHead = "SELECT DISTINCT CUSTOMER_KEY FROM NVECARESENDLOG WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq() + " AND ERROR_CD = 250";
            cntSql = "SELECT COUNT(DISTINCT CUSTOMER_KEY) FROM NVECARESENDLOG WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq()
                + " AND ERROR_CD = 250";
        } else if(resendMode.equals("resendFail")) {
            String SQL = "";
            if(serverInfo != null) {
                useMultisendKeyIdemail = serverInfo.getResendIncludeMailKeyYn().equals("Y") ? "on" : "off";
            }
            if(useMultisendKeyIdemail.equalsIgnoreCase("on")) {
                //SECUDB UNIT
                //NVECARESENDLOG : CUSTOMER_EMAIL, CUSTOMER_NM, MESSAGE_KEY
                SQL = "" + "SELECT CUSTOMER_KEY " + appendString + " ' ' " + appendString + " CUSTOMER_EMAIL AS RESEND_KEY " + " FROM NVECARESENDLOG " + " WHERE ECARE_NO = " + parentEcareNo
                    + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq() + " AND ERROR_CD NOT IN (" + StringUtil.getErrorCodeListForDB(serverInfo.getResendErrorCd()) + ") ";
                if(serverInfo != null && serverInfo.getResendIncludeReturnmailYn().equals("Y")) {
                    //SECUDB UNIT
                    //NVECARESENDLOG : CUSTOMER_EMAIL, CUSTOMER_NM, MESSAGE_KEY
                    //NVECARERETURNMAIL : 해당 필드 없음
                    //NVECAREMSG : 해당 필드 없음
                    SQL += "UNION " + "SELECT SL.CUSTOMER_KEY " + appendString + " ' ' " + appendString + " SL.CUSTOMER_EMAIL AS resend_key  " + "FROM   NVECARESENDLOG SL  , "
                        + "       (SELECT ECARE_NO   , " + "               RESULT_SEQ , " + "               LIST_SEQ   , " + "               CUSTOMER_ID, " + "               RECORD_SEQ "
                        + "       FROM    NVECARERETURNMAIL " + "       WHERE   ECARE_NO IN " + "               (SELECT ECARE_NO " + "               FROM    NVECAREMSG "
                        + "               WHERE   SCENARIO_NO  = " + scenarioVo.getScenarioNo() + "                   AND CHANNEL_TYPE = 'M' " + "               ) " + "           AND RECEIVE_DT "
                        + appendString + " RECEIVE_TM >= " + "               (SELECT SENDSTART_DT " + appendString + " INVOKE_TM " + "               FROM    NVECMSCHEDULE "
                        + "               WHERE   ECMSCHEDULE_NO = " + "                       (SELECT ECMSCHEDULE_NO " + "                       FROM    NVECAREMSG "
                        + "                       WHERE   ECARE_NO = " + parentEcareNo + "                       ) " + "               ) " + "           AND RECEIVE_DT " + appendString
                        + " RECEIVE_TM < " + "               (SELECT SENDSTART_DT " + appendString + " INVOKE_TM " + "               FROM    NVECMSCHEDULE "
                        + "               WHERE   ECMSCHEDULE_NO = " + "                       (SELECT ECMSCHEDULE_NO " + "                       FROM    NVECAREMSG "
                        + "                       WHERE   ECARE_NO = " + nextEcareNo + "                       ) " + "               ) " + "       ) RM " + "WHERE  SL.ECARE_NO     = RM.ECARE_NO "
                        + "   AND SL.RESULT_SEQ   = RM.RESULT_SEQ " + "   AND SL.LIST_SEQ     = RM.LIST_SEQ " + "   AND SL.CUSTOMER_KEY = RM.CUSTOMER_ID " + "   AND SL.RECORD_SEQ   = RM.RECORD_SEQ";
                }

            } else {
                //SECUDB NONE
                //NVECARESENDLOG : 해당 필드 없음
                //NVECARERETURNMAIL : 해당 필드 없음
                //NVECAREMSG : 해당 필드 없음
                SQL = "" + "SELECT CUSTOMER_KEY AS RESEND_KEY " + "  FROM NVECARESENDLOG " + " WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq()
                    + " AND ERROR_CD NOT IN (" + StringUtil.getErrorCodeListForDB(serverInfo.getResendErrorCd()) + ") ";
                if(serverInfo != null && serverInfo.getResendIncludeReturnmailYn().equals("Y")) {
                    SQL += "UNION " + "SELECT CUSTOMER_ID AS RESEND_KEY " + "  FROM NVECARERETURNMAIL " + " WHERE ECARE_NO IN (SELECT ECARE_NO " + "                      FROM NVECAREMSG "
                        + "                     WHERE SCENARIO_NO = " + scenarioVo.getScenarioNo() + " AND CHANNEL_TYPE = 'M') " + "   AND RECEIVE_DT " + appendString + " RECEIVE_TM >= "
                        + "                             (SELECT SENDSTART_DT " + appendString + " INVOKE_TM " + "                                FROM NVECMSCHEDULE "
                        + "                               WHERE ECMSCHEDULE_NO = (SELECT ECMSCHEDULE_NO " + "                                                         FROM NVECAREMSG "
                        + "                                                        WHERE ECARE_NO = " + parentEcareNo + ")) " + "   AND RECEIVE_DT " + appendString + " RECEIVE_TM < "
                        + "                             (SELECT SENDSTART_DT " + appendString + " INVOKE_TM " + "                                FROM NVECMSCHEDULE "
                        + "                               WHERE ECMSCHEDULE_NO = (SELECT ECMSCHEDULE_NO " + "                                                         FROM NVECAREMSG "
                        + "                                                        WHERE ECARE_NO = " + nextEcareNo + "))";
                }

            }
            sqlHead = SQL;
            cntSql = "SELECT COUNT(*) FROM (" + SQL + ") RESEND";
        } else if(resendMode.equals("resultOpen")) {
            //SECUDB NONE
            //NVECARERECEIPT : 해당 필드 없음
            sqlHead = "SELECT DISTINCT CUSTOMER_ID FROM NVECARERECEIPT WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq();
            cntSql = "SELECT COUNT(DISTINCT CUSTOMER_ID) CUSTOMER_ID FROM NVECARERECEIPT WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq();
            relationType = "L";
        } else if(resendMode.equals("resendDuration")) {
            //SECUDB NONE
            //NVECARERECEIPT : 해당 필드 없음
            sqlHead = "SELECT DISTINCT CUSTOMER_ID FROM NVECARERECEIPT WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq() + " AND VALID_CNT = 1";
            cntSql = "SELECT COUNT(DISTINCT CUSTOMER_ID) FROM NVECARERECEIPT WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq() + " AND VALID_CNT = 1";
            relationType = "L";
        } else if(resendMode.equals("resendLink")) {
            //SECUDB NONE
            //NVECARELINKRESULT : 해당 필드 없음
            sqlHead = "SELECT DISTINCT CUSTOMER_ID FROM NVECARELINKRESULT WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq();
            cntSql = "SELECT COUNT(DISTINCT CUSTOMER_ID) FROM NVECARELINKRESULT WHERE ECARE_NO = " + parentEcareNo + " AND RESULT_SEQ = " + scenarioVo.getEcareVo().getResultSeq();
            relationType = "L";
        }

        // 세그먼트 정보를 가져온다.
        int segmentSize = commonDao.selectCountBySql(cntSql);
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(scenarioVo.getEcareVo().getSegmentNo());
        segmentVo.setSegmentSize(segmentSize);
        segmentVo.setSqlfilter(sqlHead);
        // 원본 세그먼트 번호를 가져간다. (세그먼트 복사 시 사용)
        segmentVo.setPsegmentNo(scenarioVo.getEcareVo().getSegmentNo());
        String segmentNm = segmentVo.getSegmentNm();
        int segmentNo = segmentDao.selectNextSegmentNo();
        segmentVo.setSegmentNm("[resend]" + segmentNm);
        segmentVo.setSegmentNo(segmentNo);
        segmentVo.setFileToDbYn("N");
        segmentVo.setShareYn("N");
        segmentVo.setActiveYn("N");
        segmentVo.setSegType(relationType);
        scenarioVo.getEcareVo().setRelationType(relationType);

        // SEGMENT, SCEMANTIC COPY
        segmentDao.copySegmentForResend(segmentVo);
        semanticDao.copySemantic(segmentVo.getPsegmentNo(), segmentVo.getSegmentNo());

        // SEGMENT정보에 따라서 ECAREMSG 테이블 정보를 업데이트 해야 한다. SEGMENT_NO, SEGMENT_SIZE
        scenarioVo.setSegmentNo(segmentNo);
        scenarioVo.setSegmentSize(segmentSize);
        ecareDao.updateEcareMsgForResend(scenarioVo);

        return nextEcareNo;
    }

    /**
     * 이케어 리스트 총 카운트
     *
     * @param ecareScenarioVo
     * @return
     */
    public int getEcareListTotalCount(EcareScenarioVo ecareScenarioVo) {
        return ecareScenarioDao.getEcareListTotalCount(ecareScenarioVo);
    }

    /**
     * 이케어 리스트
     *
     * @param ecareScenarioVo
     * @return
     */
    public List<EcareScenarioVo> getEcareList(EcareScenarioVo ecareScenarioVo) {
        return ecareScenarioDao.getEcareList(ecareScenarioVo);
    }

    /**
     * 이케어 상태변경
     *
     * @param ecareNo
     * @return
     */
    public int updateEcareStsChange(int ecareNo) {
        return ecareDao.updateEcareStsChange(ecareNo);
    }

    public int selectResendEcareNo(int ecareNo) {
        return ecareDao.selectResendEcareNo(ecareNo);
    }

    public String selectResendEcareNm(int resendEcareNo) {
        return ecareDao.selectEcareNmByPk(resendEcareNo);
    }

    /**
     * 사용자 계정으로 등록된 옐로아이디, 발신프로필키 목록을 조회한다.
     *
     * @param userId
     * @return
     */
    public List<KakaoProfile> getKakaoProfileList(String userId) {
        return kakaoProfileDao.selectKakaoProfileList(userId);
    }

    /**
     * 옴니채널 이케어를 전부 조회한다.
     *
     * @param scenarioVo
     * @return
     */
    public List<EcareScenarioVo> findOmniChannelEcares(EcareScenarioVo scenarioVo) {
        return ecareScenarioDao.selectOmniChannelEcares(scenarioVo);
    }

    /**
     * 옴니채널 메시지용 하위 이케어를 생성한다.<br>
     * 일반 재발송과 다른 점은 상위 이케어과 하위 이케어의 채널이 다를 수 있다는 점이다.
     *
     * @param scenarioNo 상위 이케어의 시나리오 번호
     * @param ecareNo 상위 이케어 번호
     * @param resendParam 이케어 생성에 필요한 파라미터 형식은 'relationType'_'resendMode'_'channel'
     * @param userVo 계정 정보
     * @return
     */
    public int makeOmniChannelSubEcare(int scenarioNo, int ecareNo, String resendParam, UserVo userVo) {
        final EcareScenarioVo scenarioVo = ecareScenarioDao.selectEcareScenarioDetailInfo(scenarioNo, ecareNo, userVo.getLanguage());
        scenarioVo.setNewScenarioNo(scenarioNo);
        scenarioVo.setUserVo(userVo);

        final String[] params = resendParam.split("_");
        final String relationType = params[0];
        final String resendMode = params[1];
        final String channel = params[2];

        final EcareVo ecareVo = scenarioVo.getEcareVo();
        //새 이케어 번호 생성
        ecareVo.setNewEcareNo(ecareDao.selectNextEcareNo());
        //타겟을 추출할 사전 업데이트 쿼리문 생성
        final String updateQuery = makeTargetUpdateSQL(resendMode, ecareVo.getEcareNo(), ecareVo.getNewEcareNo(), ecareVo.getChannelType());

        // 옴니채널 타겟 쿼리생성
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_CONTACT2, TARGET_LST1, TARGET_LST2 FROM ");
        sb.append((Const.RESEND_L_OPEN.equals(resendMode) ? "NVECARERECEIPT" : "NVECARESENDLOG"));
        sb.append(" A, NVRESENDTARGET B ");

        final String headSql = sb.toString();
        final String bodySql = makeTargetSQL(resendMode, ecareVo.getEcareNo(), ecareVo.getNewEcareNo(), ecareVo.getChannelType());

        // 상위 이케어의 relation_tree에 새로 생성하는 이케어의 relation_type을 붙여 이케어간 관계를 나타낸다.
        final String parentRelationTree = ecareVo.getRelationTree();
        ecareVo.setRelationTree(parentRelationTree + relationType);
        ecareVo.setRelationType(relationType);
        ecareVo.setChannelType(channel);
        ecareVo.setSenderEmail(ecareDao.selectEcareSenderEmail(scenarioVo));
        ecareVo.setRetmailReceiver(ecareDao.selectEcareRetmailReceiver(scenarioVo));

        if(scenarioVo.getSegmentNo() == 0) {
            scenarioVo.setSegmentNo(Integer.parseInt("-1"));
        }
        final SegmentVo segmentVo = makeSegmentVo(ecareVo, scenarioVo.getSegmentNo(), "M", headSql, bodySql, updateQuery);

        scenarioVo.setSegmentNo(segmentVo.getSegmentNo());

        // 스케줄 (분) 타입 다차원 발송 이케어 생성
        insertEcareAndRelatedTable(scenarioVo, segmentVo);
        return ecareVo.getNewEcareNo();
    }

    /**
     * 이케어과 대상자, 시맨틱 정보를 저장한다.
     *
     * @param scenarioVo 이케어 시나리오
     * @param segmentVo 대상자
     */
    public void insertEcareAndRelatedTable(EcareScenarioVo scenarioVo, SegmentVo segmentVo) {
        int nextEcmScheduleNo = ecmScheduleDao.selectNextEcmScheduleNo();
        scenarioVo.getEcareVo().getEcareScheduleVo().setEcmScheduleNo(nextEcmScheduleNo);

        ecmScheduleDao.insertEcmSchedule(scenarioVo.getEcareVo().getEcareScheduleVo());
        segmentDao.copySegmentForOmniSend(segmentVo);
        semanticDao.copySemantic(segmentVo.getPsegmentNo(), segmentVo.getSegmentNo());
        ecareDao.copyEcareForOmni(scenarioVo);
        insertTemplate(scenarioVo.getEcareVo().getNewEcareNo(), scenarioVo.getEcareVo().getChannelType(), Const.MailType.NONE);
    }

    private String makeTargetUpdateSQL(String resendMode, int ecareNo, int newEcareNo, String channel) {
        String timeout = PropertyUtil.getProperty("omni.timeout.hour", "3");
        String interval = PropertyUtil.getProperty("omni.send.interval", "10");

        if(Const.RESEND_R_FAIL.equals(resendMode) || Const.RESEND_R_SUCCESS.equals(resendMode)) {
            String inCondition;

            if(Const.RESEND_R_FAIL.equals(resendMode)) {
                inCondition = " NOT IN (\'";
            } else {
                inCondition = " IN (\'";
            }

            final StringBuilder sb = new StringBuilder();
            //MSSQL UPDATE 문 alias 허용
            if(dbServer.equalsIgnoreCase("MSSQL")) {
                sb.append(" UPDATE A");
                sb.append(" SET SUB_ECARE_NO = ").append(newEcareNo);
                sb.append("     , SUB_RESULT_SEQ = \'$RSEQ$\'");
                sb.append("  FROM NVECARESENDLOG A ");
            }else {
                sb.append(" UPDATE NVECARESENDLOG A");
                sb.append(" SET A.SUB_ECARE_NO = ").append(newEcareNo);
                sb.append("     , A.SUB_RESULT_SEQ = \'$RSEQ$\'");
            }

            // 채널에 따라 성공,실패,오픈 정보가 비동기 형태로 수집되는 경우가 있어서 현재시간 기준으로 10분 이상 지난 건을 기준으로 옴니 채널 타겟 설정
            // 시스템 재기동시 과거 이력을 발송 처리하지 않도록 기준시간을 지정함 (현재시간 3시간 이내 건만 발송)
            if(dbServer.equalsIgnoreCase("DB2")) {
                sb.append(" WHERE ((A.SEND_DT = TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'yyyymmdd')");
                sb.append(" AND A.SEND_TM > TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'hh24miss'))");
                sb.append(" OR (A.SEND_DT > TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'yyyymmdd')");
                sb.append(" AND A.SEND_TM < TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'hh24miss')))");
                sb.append(" AND ((A.SEND_DT = TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'yyyymmdd')");
                sb.append(" AND A.SEND_TM <= TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'hh24miss'))");
                sb.append(" OR (A.SEND_DT < TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'yyyymmdd')");
                sb.append(" AND A.SEND_TM >= TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'hh24miss')))");
            } else if(dbServer.equalsIgnoreCase("MYSQL")) {
                sb.append(" WHERE ((A.SEND_DT = DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%Y%m%d')");
                sb.append(" AND A.SEND_TM > DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%H%i%s'))");
                sb.append(" OR (A.SEND_DT > DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%Y%m%d')");
                sb.append(" AND A.SEND_TM < DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%H%i%s')))");
                sb.append(" AND ((A.SEND_DT = DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%Y%m%d')");
                sb.append(" AND A.SEND_TM <= DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%H%i%s'))");
                sb.append(" OR (A.SEND_DT < DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%Y%m%d')");
                sb.append(" AND A.SEND_TM >= DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%H%i%s')))");
            } else if(dbServer.equalsIgnoreCase("MSSQL")) {
                sb.append(" WHERE ((SEND_DT = CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),112)");
                sb.append(" AND SEND_TM > replace(CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),108),':',''))");
                sb.append(" OR (SEND_DT > CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),112)");
                sb.append(" AND SEND_TM < replace(CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),108),':','')))");
                sb.append(" AND ((SEND_DT = CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),112)");
                sb.append(" AND SEND_TM <= replace(CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),108),':',''))");
                sb.append(" OR (SEND_DT < CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),112)");
                sb.append(" AND SEND_TM >= replace(CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),108),':','')))");
            } else if(dbServer.equalsIgnoreCase("ORACLE")) {
                sb.append(" WHERE ((A.SEND_DT = to_char(sysdate-").append(timeout).append("/24,'yyyymmdd')");
                sb.append(" AND A.SEND_TM > to_char(sysdate-").append(timeout).append("/24,'hh24miss'))");
                sb.append(" OR (A.SEND_DT > to_char(sysdate-").append(timeout).append("/24,'yyyymmdd')");
                sb.append(" AND A.SEND_TM < to_char(sysdate-").append(timeout).append("/24,'hh24miss')))");
                sb.append(" AND ((A.SEND_DT = to_char(sysdate-1/24/60*").append(interval).append(",'yyyymmdd')");
                sb.append(" AND A.SEND_TM <= to_char(sysdate-1/24/60*").append(interval).append(",'hh24miss'))");
                sb.append(" OR (A.SEND_DT < to_char(sysdate-1/24/60*").append(interval).append(",'yyyymmdd')");
                sb.append(" AND A.SEND_TM >= to_char(sysdate-1/24/60*").append(interval).append(",'hh24miss')))");
            }
            sb.append(" AND A.ECARE_NO = ").append(ecareNo);
            sb.append(" AND A.ERROR_CD ").append(inCondition);
            sb.append(getErrCdByChannel(channel)).append("\')");

            sb.append(" AND A.SUB_RESULT_SEQ = 0");
            sb.append(" AND A.SUB_ECARE_NO = 0");

            return sb.toString();
        } else if(Const.RESEND_L_OPEN.equals(resendMode)) {
            final StringBuilder sb = new StringBuilder();
            //MSSQL UPDATE 문 alias 허용
            if(dbServer.equalsIgnoreCase("MSSQL")) {
                sb.append(" UPDATE A");
                sb.append(" SET SUB_ECARE_NO = ").append(newEcareNo);
                sb.append("     , SUB_RESULT_SEQ = \'$RSEQ$\'");
                sb.append("  FROM NVECARERECEIPT A ");
            }else {
                sb.append(" UPDATE NVECARERECEIPT A");
                sb.append(" SET A.SUB_ECARE_NO = ").append(newEcareNo);
                sb.append("     , A.SUB_RESULT_SEQ = \'$RSEQ$\'");
            }

            // 채널에 따라 성공,실패,오픈 정보가 비동기 형태로 수집되는 경우가 있어서 현재시간 기준으로 10분 이상 지난 건을 기준으로 옴니 채널 타겟 설정
            // 시스템 재기동시 과거 이력을 발송 처리하지 않도록 기준시간을 지정함 (현재시간 3시간 이내 건만 발송)
            if(dbServer.equalsIgnoreCase("DB2")) {
                sb.append(" WHERE ((A.OPEN_DT = TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'yyyymmdd')");
                sb.append(" AND A.OPEN_TM > TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'hh24miss'))");
                sb.append(" OR (A.OPEN_DT > TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'yyyymmdd')");
                sb.append(" AND A.OPEN_TM < TO_CHAR(CURRENT TIMESTAMP - ").append(timeout).append(" HOURS, 'hh24miss')))");
                sb.append(" AND ((A.OPEN_DT = TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'yyyymmdd')");
                sb.append(" AND A.OPEN_TM <= TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'hh24miss'))");
                sb.append(" OR (A.OPEN_DT < TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'yyyymmdd')");
                sb.append(" AND A.OPEN_TM >= TO_CHAR(CURRENT TIMESTAMP - ").append(interval).append(" MINUTE, 'hh24miss')))");
            } else if(dbServer.equalsIgnoreCase("MYSQL")) {
                sb.append(" WHERE ((A.OPEN_DT = DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%Y%m%d')");
                sb.append(" AND A.OPEN_TM > DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%H%i%s'))");
                sb.append(" OR (A.OPEN_DT > DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%Y%m%d')");
                sb.append(" AND A.OPEN_TM < DATE_FORMAT(NOW() + INTERVAL -").append(timeout).append(" HOUR, '%H%i%s')))");
                sb.append(" AND ((A.OPEN_DT = DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%Y%m%d')");
                sb.append(" AND A.OPEN_TM <= DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%H%i%s'))");
                sb.append(" OR (A.OPEN_DT < DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%Y%m%d')");
                sb.append(" AND A.OPEN_TM >= DATE_FORMAT(NOW() + INTERVAL -").append(interval).append(" MINUTE, '%H%i%s')))");
            } else if(dbServer.equalsIgnoreCase("MSSQL")) {
                sb.append(" WHERE ((A.OPEN_DT = CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),112)");
                sb.append(" AND A.OPEN_TM > replace(CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),108),':',''))");
                sb.append(" OR (A.OPEN_DT > CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),112)");
                sb.append(" AND A.OPEN_TM < replace(CONVERT(varchar(8), DATEADD(hour, -").append(timeout).append(", GETDATE()),108),':','')))");
                sb.append(" AND ((A.OPEN_DT = CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),112)");
                sb.append(" AND A.OPEN_TM <= replace(CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),108),':',''))");
                sb.append(" OR (A.OPEN_DT < CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),112)");
                sb.append(" AND A.OPEN_TM >= replace(CONVERT(varchar(8), DATEADD(minute, -").append(interval).append(", GETDATE()),108),':','')))");
            } else if(dbServer.equalsIgnoreCase("ORACLE")) {
                sb.append(" WHERE ((A.OPEN_DT = to_char(sysdate-").append(timeout).append("/24,'yyyymmdd')");
                sb.append(" AND A.OPEN_TM > to_char(sysdate-").append(timeout).append("/24,'hh24miss'))");
                sb.append(" OR (A.OPEN_DT > to_char(sysdate-").append(timeout).append("/24,'yyyymmdd')");
                sb.append(" AND A.OPEN_TM < to_char(sysdate-").append(timeout).append("/24,'hh24miss')))");
                sb.append(" AND ((A.OPEN_DT = to_char(sysdate-1/24/60*").append(interval).append(",'yyyymmdd')");
                sb.append(" AND A.OPEN_TM <= to_char(sysdate-1/24/60*").append(interval).append(",'hh24miss'))");
                sb.append(" OR (A.OPEN_DT < to_char(sysdate-1/24/60*").append(interval).append(",'yyyymmdd')");
                sb.append(" AND A.OPEN_TM >= to_char(sysdate-1/24/60*").append(interval).append(",'hh24miss')))");
            }
            sb.append(" AND A.ECARE_NO = ").append(ecareNo);
            sb.append(" AND A.SUB_RESULT_SEQ = 0");
            sb.append(" AND A.SUB_ECARE_NO = 0");
            return sb.toString();
        }

        return "";
    }

    /**
     * 재발송 모드에 따라 대상자 SQL과 대상자 카운트 SQL 생성
     * 이케어의 옴니채널 하위 대상자 SQL 생성
     *
     * @param resendMode
     * @param ecareNo
     * @param newEcareNo
     * @param channel
     * @return Map
     *         <ul>
     *         <li>"HEAD_SQL": 대상자 쿼리</li>
     *         <li>"COUNT_SQL": 대상자 카운트 쿼리</li>
     *         </ul>
     */
    public String makeTargetSQL(String resendMode, int ecareNo, int newEcareNo, String channel) {
        if(Const.RESEND_R_FAIL.equals(resendMode) || Const.RESEND_R_SUCCESS.equals(resendMode)) {
            String inCondition;
            if(Const.RESEND_R_FAIL.equals(resendMode)) {
                inCondition = " NOT IN (\'";
            } else {
                inCondition = " IN (\'";
            }

            final StringBuilder sb = new StringBuilder();
            sb.append(" WHERE A.SUB_RESULT_SEQ = \'$RSEQ$\'");
            sb.append(" AND A.SUB_ECARE_NO = ").append(newEcareNo);
            sb.append(" AND A.ECARE_NO = ").append(ecareNo);
            sb.append(" AND A.ERROR_CD ").append(inCondition);
            sb.append(getErrCdByChannel(channel)).append("\')");
            sb.append(" AND B.CLIENT = 'EC'");
            sb.append(" AND A.ECARE_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        } else if(Const.RESEND_L_OPEN.equals(resendMode)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" WHERE A.SUB_RESULT_SEQ = \'$RSEQ$\'");
            sb.append(" AND A.SUB_ECARE_NO = ").append(newEcareNo);
            sb.append(" AND A.ECARE_NO = ").append(ecareNo);
            sb.append(" AND B.CLIENT = 'EC'");
            sb.append(" AND A.ECARE_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        }
        return "";
    }

    /**
     * SegmentVo 객체를 생성하고 데이터를 설정한다.
     *
     * @param segmentNo
     * @param segType
     * @param headSql
     * @return
     */
    private SegmentVo makeSegmentVo(EcareVo ecareVo, final int segmentNo, final String segType, final String headSql, final String bodySql, String updateQuery) {
        // 옴니발송시에는 새로운 대상자 쿼리가 해당 기능을 대체함
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(segmentNo);
        segmentVo.setSegmentNo(segmentDao.selectNextSegmentNo());
        segmentVo.setSegmentNm("[Omni]" + ecareVo.getNewEcareNo() + "_" + ecareVo.getChannelType() + "_" + ecareVo.getRelationTree());
        segmentVo.setSqlHead(headSql);
        segmentVo.setSqlBody(bodySql);
        segmentVo.setLastUpdateDt(new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
        segmentVo.setPsegmentNo(segmentNo);
        segmentVo.setSegType(segType);
        segmentVo.setUpdateQuery(updateQuery);

        return segmentVo;
    }

    /**
     * 선택한 이케어의 하위 이케어를 찾아 Map 객체에 저장한다.
     *
     * @param ecareScenarioVo 시나리오 번호
     * @param map 하위 이케어을 저장할 Map 객체
     */
    public void findSubEcareAndStoreMap(EcareScenarioVo ecareScenarioVo, ModelAndView map) {
        arrangeSubEcareList(ecareDao.selectSubEcareList(ecareScenarioVo), map);
    }

    /**
     * 성공, 실패, 오픈으로 하위 이케어 리스트를 분류한다.
     *
     * @param ecareList 모든 하위 이케어 리스트
     * @param map 분류한 하위 이케어를 저정할 객체
     */
    private void arrangeSubEcareList(final List<EcareVo> ecareList, ModelAndView map) {
        final List<EcareVo> successList = new ArrayList<>();
        final List<EcareVo> failList = new ArrayList<>();
        final List<EcareVo> openList = new ArrayList<>();

        String relation;
        for(EcareVo vo : ecareList) {
            relation = vo.getRelationType();
            if(Const.RELATION_SUCCESS.equals(relation)) {
                successList.add(vo);
            } else if(Const.RELATION_FAIL.equals(relation)) {
                failList.add(vo);
            } else if(Const.RELATION_OPEN.equals(relation)) {
                openList.add(vo);
            }
        }

        map.addObject("successSubEcareList", successList);
        map.addObject("failSubEcareList", failList);
        map.addObject("openSubEcareList", openList);
    }


    /**
     * 최상위 이케어에 설정한 대상자의 Semantic Field Key 목록을 바탕으로 생성 가능한 하위 채널 목록을 반환한다.
     *
     * @param scenarioNo
     * @return
     */
    public List<String> findAvailableChannels(int scenarioNo, String channelType) {
        String[] channelArr = PropertyUtil.getProperty("ecare.channel.use.list").split(",");    //이케어 사용 채널목록
        if(channelArr.length == 0) {
            return new ArrayList<>();
        }

        List<String> keyList = semanticDao.selectEcareSemanticKey(scenarioNo);
        if(keyList.isEmpty()) {
            return keyList;
        }

        String fieldKeys = makeFieldKeys(keyList);

        List<String> list = new ArrayList<>();
        for(String channel : channelArr) {
            if(isAvailableChannel(fieldKeys, channel)) {
                list.add(channel);
            }
        }

        return list;
    }

    /**
     * Semantic Field Key 목록을 이어붙여 문자열로 반환한다.
     *
     * @param keyList
     * @return
     */
    private String makeFieldKeys(List<String> keyList) {
        StringBuilder sb = new StringBuilder();
        for(String key : keyList) {
            sb.append(key);
            sb.append(",");
        }
        return sb.toString();
    }

    /**
     * 각 채널별 발송에 필요한 연락처가 Semantic Field Key에 포함되어 있는지 확인한다.
     *
     * @param semanticFieldeKeys
     * @param channel
     * @return
     */
    private boolean isAvailableChannel(String semanticFieldeKeys, String channel) {
        if(channel.equals(Channel.MAIL)) {
            if(semanticFieldeKeys.indexOf(Const.SEMANTIC_KEY_EMAIL) > -1)
                return true;
        } else if(channel.equals(Channel.SMS) || channel.equals(Channel.LMSMMS)
                || channel.equals(Channel.PUSH) || ChannelUtil.isKakao(channel)) {
            if(semanticFieldeKeys.indexOf(Const.SEMANTIC_KEY_TELEPHONE) > -1)
                return true;
        } else if(channel.equals(Channel.FAX)) {
            if(semanticFieldeKeys.indexOf(Const.SEMANTIC_KEY_FAX) > -1)
                return true;
        }

        return false;
    }



    /**
     * 이케어 상세 정보를 변경한다.
     *
     * @param ecareVo
     */
    public void updateEcare2StepInfo(EcareVo ecareVo) throws Exception {
        try {
                ecareDao.updateEcare2StepInfo(ecareVo);
        }catch(Exception e) {
            log.error("{}" , e);
            throw new Exception("Ecare Update Error");
        }
    }


    /**
     * 이케어 반응추적기간 정보를 변경한다.
     *
     * @param ecareVo
     */
    public void updateEcareTraceInfo2StepInfo(EcareVo ecareVo) throws Exception {
        try {
            // 반응추적기간 인서트 하기~ 위해서 있는지 없는지 우선 검사하자.
            String termType = ecareTraceInfoDao.selectEcareTraceInfo(ecareVo.getEcareNo());
            if (termType == null) {
                ecareTraceInfoDao.insertEcareTraceInfoByEcare(ecareVo);
            } else {
                ecareTraceInfoDao.updateEcareTraceInfo(ecareVo);
            }
        }catch(Exception e) {
            log.error("{}" , e);
            throw new Exception("EcareScenario Update Error");
        }
    }
    /**
     * 이케어 시나리오 상세 정보를 변경한다.
     *
     * @param ecareScenarioVo
     */
    public void updateEcareScenario2StepInfo(EcareScenarioVo ecareScenarioVo) throws Exception {
        try {
            ecareScenarioVo.setLastUpdateDt(DateFormatUtils.format(new Date(), "yyyyMMdd"));
            ecareScenarioVo.setLastUpdateTm(DateFormatUtils.format(new Date(), "HHmmss"));
            ecareScenarioDao.updateEcareScenarioByPk(ecareScenarioVo);
        }catch(Exception e) {
            log.error("{}" , e);
            throw new Exception("EcareScenario Update Error");
        }
    }

    /**
     * 이케어 스케줄 정보를 변경한다.
     *
     * @param ecareScheduleVo
     *
     */
    public void updateEcareSchedule2StepInfo(EcareScheduleVo ecareScheduleVo) throws Exception {
        try {
               if("1".equals(ecareScheduleVo.getCycleCd())
                       && StringUtil.isNotEmpty(ecareScheduleVo.getStartTm())) {
                    ecareScheduleVo.setInvokeEveryMin("Y");
               }else if("3".equals(ecareScheduleVo.getCycleCd())) {
                    if(ecareScheduleVo.getMonthOpt().equals("STATIC")) {
                        ecareScheduleVo.setScheWeeknumber(0);
                        ecareScheduleVo.setWeekday("");
                    } else if(ecareScheduleVo.getMonthOpt().equals("CONTEXT")) {
                        ecareScheduleVo.setDay(0);
                    }
                }
                ecmScheduleDao.updateEcareScheduleInfo(ecareScheduleVo);
        }catch(Exception e) {
            log.error("{}" , e);
            throw new Exception("EcareScenario data Update Error");
        }
    }

    /**
     * 이케어 발송주기 정보를 변경한다.
     *
     * @param cycleItems
     *  이전 함수 EcareService.setRegistEcareScheduleInfo
     */
    public void updateEcareCycle2StepInfo(EcareScheduleVo ecareScheduleVo, String[] cycleItems) throws Exception {
        try {
            // NVCYCLEITEM UPDATE
            cycleItemDao.deleteEcareCycleItemInfo(ecareScheduleVo.getEcmScheduleNo());

            CycleItemVo cycleItemVo = new CycleItemVo();
            cycleItemVo.setEcmScheduleNo(ecareScheduleVo.getEcmScheduleNo());
            cycleItemVo.setCheckYn("Y");
            if (ecareScheduleVo.getCycleCd().equals("4")) { // NVCYCLEITEM 테이블에 필요없는 데이터인데도 없다면 조인 쿼리를 못하는 문제로 한번만 옵션일 경우 값을 강제로
                cycleItemVo.setCycleItem("ONTIME");
                cycleItemDao.insertCycleItem(cycleItemVo);
            } else if (ecareScheduleVo.getCycleCd().equals("5")) { // 분할발송 조건 추가
                cycleItemVo.setCycleItem("DIVIDE");
                cycleItemDao.insertCycleItem(cycleItemVo);
            } else {
                for (String cycleItem : cycleItems) {
                    cycleItemVo.setCycleItem(cycleItem);
                    cycleItemDao.insertCycleItem(cycleItemVo);
                }
            }
        }catch(Exception e) {
            log.error("{}" , e);
            throw new Exception("EcareCycleItem Update Error");
        }
    }

    /**
     * 발신자 정보가 없을경우 환경설정에서 읽은 후 업데이트
     *
     * @param ecareVo
     * @param ecareScenarioVo
     * @return
     * @throws Exception
     */
    public void updateSenderInfo(EcareVo ecareVo, EcareScenarioVo ecareScenarioVo) throws Exception{
        // 발신자 이메일, 반송 이메일 정보 가져오기
        EcareVo param = new EcareVo();  //업데이트 파라미터
        final String channelType = ecareVo.getChannelType();
        if(Channel.MAIL.equals(channelType)) {
            EmailValidator validator = EmailValidator.getInstance();
            if(StringUtil.isBlank(ecareVo.getSenderEmail()) || !validator.isValid(ecareVo.getSenderEmail())) {
                param.setSenderEmail(ecareVo.getEnvSenderEmail());
                ecareVo.setSenderEmail(ecareVo.getEnvSenderEmail());
            }
            if(StringUtil.isBlank(ecareVo.getRetmailReceiver()) || !validator.isValid(ecareVo.getRetmailReceiver())) {
                param.setRetmailReceiver(ecareVo.getEnvRetmailReceiver());
                ecareVo.setRetmailReceiver(ecareVo.getEnvRetmailReceiver());
            }
            if(StringUtil.isBlank(ecareVo.getSenderNm())) {
                param.setSenderNm(ecareVo.getEnvSenderNm());
                ecareVo.setSenderNm(ecareVo.getEnvSenderNm());
            }
            if(ecareVo.getRetryCnt() == 0) {
                ServerInfoVo serverInfo = serverInfoDao.selectServerInfo();
                param.setRetryCnt(serverInfo.getRetryCnt());
                ecareVo.setRetryCnt(serverInfo.getRetryCnt());
            }
        } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)
                || Channel.ALIMTALK.equals(channelType) || Channel.FRIENDTALK.equals(channelType)) {
            if(StringUtil.isEmpty(ecareVo.getSenderTel())) {
                param.setSenderTel(ecareVo.getEnvSenderTel());
                ecareVo.setSenderTel(ecareVo.getEnvSenderTel());
            }
        } else if(Channel.FAX.equals(channelType)) {
            // senderFax(발신자 팩스번호)은 senderEmail에,
            // receiverFax(수신 팩스번호)은 retmailReceiver에 저장되어 있음.
            if(StringUtil.isBlank(ecareVo.getSenderEmail()) || !ecareVo.getSenderEmail().matches("[0-9-]+")) {
                param.setSenderEmail(ecareVo.getEnvSenderFax());
                ecareVo.setSenderEmail(ecareVo.getEnvSenderFax());
            }
            if(StringUtil.isBlank(ecareVo.getRetmailReceiver()) || !ecareVo.getRetmailReceiver().matches("[0-9-]+")) {
                param.setRetmailReceiver(ecareVo.getEnvSenderFax());
                ecareVo.setRetmailReceiver(ecareVo.getEnvSenderFax());
            }
        }
        param.setEcareNo(ecareVo.getEcareNo());
        ecareDao.updateEcare2StepInfo(param);
    }

    /**
     * 템플릿을 생성한다. Email 메일 타입이 보안메일이나 PDF면 템플릿을 추가로 생성한다. Push 채널은 추가로 POPUP 템플릿을 생성한다.
     *
     * @param ecareNo 이케어 번호
     * @param channelType 채널 타입
     * @param mailType 메일 타입
     */
    private void insertTemplate(int ecareNo, String channelType, String mailType) {
        List<TemplateVo> templateList = new ArrayList<>();
        templateList.add(getTemplateVo(ecareNo, " "));

        if (channelType.equals(Channel.MAIL)) {
            if (!mailType.equals(Const.MailType.NONE)) {
                templateList.add(getTemplateVo(ecareNo, mailType));
            }
        } else if (channelType.equals(Channel.PUSH)) {
            templateList.add(getTemplateVo(ecareNo, "POPUP"));
        }

        for (TemplateVo templateVo : templateList) {
            ecareTemplateDao.insertEditorEcareTemplate(templateVo);
        }
    }

    private TemplateVo getTemplateVo(int ecareNo, String seg) {
        TemplateVo template = new TemplateVo();
        template.setNo(ecareNo);
        template.setSeg(seg);
        template.setTemplate(" ");

        return template;
    }
    /**
     * 이케어 목록을 가져온다.
     * @return
     */
    public List<EcareVo> selectEcareList(){
        return ecareDao.selectEcareList();
    }
}
