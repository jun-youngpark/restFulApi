package com.mnwise.wiseu.web.campaign.service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.Sprintf;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.Const.Client;
import com.mnwise.wiseu.web.base.Const.ServiceType;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.campaign.dao.ApplicationDao;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.DivideScheduleDao;
import com.mnwise.wiseu.web.campaign.dao.LinkTraceDao;
import com.mnwise.wiseu.web.campaign.dao.MultipartFileDao;
import com.mnwise.wiseu.web.campaign.dao.ScenarioDao;
import com.mnwise.wiseu.web.campaign.dao.ScheduleDao;
import com.mnwise.wiseu.web.campaign.dao.SendLogDao;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.campaign.dao.TestUserDao;
import com.mnwise.wiseu.web.campaign.dao.TraceInfoDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.model.TraceInfoVo;
import com.mnwise.wiseu.web.channel.dao.PushServiceInfoDao;
import com.mnwise.wiseu.web.common.dao.CommonDao;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.common.service.AbstractScenarioService;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.editor.model.DefaultHandlerVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.env.service.EnvDefaultHandleService;
import com.mnwise.wiseu.web.report.model.campaign.CampaignAbTestVo;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.template.dao.KakaoProfileDao;
import com.mnwise.wiseu.web.template.model.KakaoProfile;

/**
 * 시나리오와 관련된 캠페인 정보 Service
 */
@Service
public class ScenarioService extends AbstractScenarioService implements ApplicationContextAware {
    @Autowired private ApplicationDao applicationDao;
    @Autowired private CommonDao commonDao;
    @Autowired private CampaignDao campaignDao;
    @Autowired private DivideScheduleDao divideScheduleDao;
    @Autowired private EnvDefaultHandleService envDefaultHandleService;
    @Autowired private KakaoProfileDao kakaoProfileDao;
    @Autowired private LinkTraceDao linkTraceDao;
    @Autowired private MultipartFileDao multipartFileDao;
    @Autowired private PushServiceInfoDao pushServiceInfoDao;
    @Autowired private ScenarioDao scenarioDao;
    @Autowired private ScheduleDao scheduleDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private SendLogDao sendLogDao;
    @Autowired private SendResultDao sendResultDao;
    @Autowired private ServerInfoDao serverInfoDao;
    @Autowired private TagDao tagDao;
    @Autowired private TemplateDao templateDao;
    @Autowired private TestUserDao testUserDao;
    @Autowired private TraceInfoDao traceInfoDao;

    private static final String AB_TEST_TEMPLATE = "T";
    private static final String AB_TEST_SUBJECT = "S";
    private static final String AB_TEST_CHOICE_A = "A";
    private static final String STATUS_END = "E";

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    /**
     * 캠페인 기본정보를 저장한다.
     *
     * @param scenarioVo
     * @param campaignNo
     */
    public void setRegistScenario1StepInfo(ScenarioVo scenarioVo, int campaignNo) {
        // 태그번호를 가져온다.
        int tagNo = tagDao.selectTagNoWithInsert("nvcamptag", scenarioVo.getTagNm());
        scenarioVo.setTagNo(tagNo);

        // 캠페인 생성시간,마지막 수정시간 등을 셋팅한다.
        scenarioVo.setCreateDt(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        scenarioVo.setCreateTm(DateFormatUtils.format(new Date(), "HHmmss"));
        scenarioVo.setLastUpdateDt(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        scenarioVo.setLastUpdateTm(DateFormatUtils.format(new Date(), "HHmmss"));

        // 캠페인이 수정 모드일 경우
        if(scenarioVo.getScenarioNo() > 0) {
            // 다른 채널에 이미 SEGMENT_NO가 있다면 가져온다.
            int segmentNo = campaignDao.selectSegmentNo(scenarioVo.getScenarioNo());

            // 세그먼트 번호가 존재할 경우
            if(segmentNo > 0) {
                scenarioVo.setSegmentNo(segmentNo);
            }

            // Update 한다/
            String[] channels = scenarioVo.getChannels();
            Map<String, String> useChannel = new HashMap<>();
            for(int i = 0, n = channels.length; i < n; i++) {
                useChannel.put(channels[i], " ");
            }

            String[] initChannels = scenarioVo.getInitChannels().split(",");
            for(int i = 0, n = initChannels.length; i < n; i++) {
                if(useChannel.get(initChannels[i]) != null) {
                    useChannel.put(initChannels[i], "UPDATE");
                } else {
                    useChannel.put(initChannels[i], "DELETE");
                }
            }

            // 시나리오 정보를 Update 한다.
            scenarioDao.updateScenario1StepInfo(scenarioVo);

            for(Iterator<String> iter = useChannel.keySet().iterator(); iter.hasNext();) {
                String key = iter.next();
                Object value = useChannel.get(key);

                scenarioVo.getCampaignVo().setChannelType(key);

                if(scenarioVo.getFinishYn().equals("N")) {
                    if(value.equals("INSERT")) {
                        // Insert Method 호출
                        insertScenario1StepInfoCommon(scenarioVo);

                    } else if(value.equals("UPDATE")) {
                        campaignDao.updateCampaign1StepInfo(setCampaignNo(key, scenarioVo));
                    } else if(value.equals("DELETE")) {
                        // Delete Method 호출
                        deleteScenarioCommon(setCampaignNo(key, scenarioVo));
                    }
                }
            }

        } else {
            // 환경 설정에 저장된 재시도 횟수 저장
            scenarioVo.getCampaignVo().setRetryCnt(serverInfoDao.selectServerInfo().getRetryCnt());

            // 저장한다.

            int maxScenarioNo = scenarioDao.selectMaxScenarioNo(scenarioVo);

            scenarioVo.setScenarioNo(maxScenarioNo);

            scenarioDao.insertScenario1StepInfo(scenarioVo);

            String[] channels = scenarioVo.getChannels();
            for(int i = 0, n = channels.length; i < n; i++) {
                scenarioVo.getCampaignVo().setChannelType(channels[i]);
                insertScenario1StepInfoCommon(scenarioVo);
            }

        }
    }

    public ScenarioVo setCampaignNo(String key, ScenarioVo scenarioVo) {
        final CampaignVo campaignVo = scenarioVo.getCampaignVo();
        if(key.equals("M") && null != scenarioVo.getMailCd()) {
            campaignVo.setCampaignNo(Integer.parseInt(scenarioVo.getMailCd()));
        } else if(key.equals("S") && null != scenarioVo.getSmsCd()) {
            campaignVo.setCampaignNo(Integer.parseInt(scenarioVo.getSmsCd()));
        } else if(key.equals("T") && null != scenarioVo.getMmsCd()) {
            campaignVo.setCampaignNo(Integer.parseInt(scenarioVo.getMmsCd()));
        } else if(key.equals("F") && null != scenarioVo.getFaxCd()) {
            campaignVo.setCampaignNo(Integer.parseInt(scenarioVo.getFaxCd()));
        } else if(key.equals("P") && null != scenarioVo.getPushCd()) {
            campaignVo.setCampaignNo(Integer.parseInt(scenarioVo.getPushCd()));
        } else if(key.equals("C") && null != scenarioVo.getFriendtalkCd()) {
            campaignVo.setCampaignNo(Integer.parseInt(scenarioVo.getFriendtalkCd()));
        }
        return scenarioVo;
    }

    /**
     * 캠페인 기본정보를 Insert 한다
     *
     * @param scenarioVo
     */
    public void insertScenario1StepInfoCommon(ScenarioVo scenarioVo) {
        int nextCampaignNo = campaignDao.selectNewCampaignNo();

        scenarioVo.getCampaignVo().setCampaignNo(nextCampaignNo);
        TraceInfoVo traceInfoVo = new TraceInfoVo();
        traceInfoVo.setTraceType("TRACE");
        scenarioVo.getCampaignVo().setTraceInfoVo(traceInfoVo);
        campaignDao.insertCampaign1StepInfo(scenarioVo);
        scheduleDao.insertCamSchedule1StepInfo(scenarioVo);
        traceInfoDao.insertCamTraceInfo1StepInfo(scenarioVo);
    }

    /**
     * 캠페인 상세 정보를 변경한다.
     *
     * @param scenarioVo
     */
    public void setRegistScenario2StepInfo(ScenarioVo scenarioVo) {
        CampaignVo campaignVo = scenarioVo.getCampaignVo();
        // 수정자 정보를 넣는다.
        campaignVo.setEditorId(scenarioVo.getUserVo().getUserId());
        campaignVo.setSegmentNo(scenarioVo.getSegmentNo());
        campaignVo.setTargetCnt(scenarioVo.getSegmentSize());

        String nowDt = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String nowTm = DateFormatUtils.format(new Date(), "HHmmss");

        scenarioVo.setLastUpdateDt(nowDt);
        scenarioVo.setLastUpdateTm(nowTm);

        campaignVo.setLastUpdateDt(nowDt);
        campaignVo.setLastUpdateTm(nowTm);

        scenarioDao.updateScenario2StepInfo(scenarioVo);
        campaignDao.updateCampaign2StepInfo(campaignVo);
        traceInfoDao.registTraceInfo(campaignVo);
    }

    /**
     * 캠페인 기본정보를 가져온다.
     *
     * @param paramScenarioVo
     * @return
     */
    public ScenarioVo getScenario1StepInfo(ScenarioVo paramScenarioVo) {
        // 캠페인 기본 정보를 받아온다.
        ScenarioVo scenarioVo = scenarioDao.selectScenarioBasicInfo(paramScenarioVo);

        // 위에서 받아온 기본정보 중 NVCAMPAIGN에 있는 각 채널별 캠페인 정보를 가져온다.
        List<CampaignVo> campaignList = scenarioVo.getCampaignList();
        List<String> channelList = new ArrayList<>();

        // 채널이 존재할 경우 시나리오에 생성되어 있는 채널 정보를 channelList과 각 채널 정보에 정보를 셋팅한다.
        if(campaignList != null) {
            for(int i = 0, n = campaignList.size(); i < n; i++) {
                CampaignVo campaignVo = campaignList.get(i);
                final String channelType = campaignVo.getChannelType();
                channelList.add(channelType);

                final String campaignNo = String.valueOf(campaignVo.getCampaignNo());

                if(Channel.MAIL.equals(channelType))
                    scenarioVo.setMailCd(campaignNo);
                else if(Channel.SMS.equals(channelType))
                    scenarioVo.setSmsCd(campaignNo);
                else if(Channel.LMSMMS.equals(channelType))
                    scenarioVo.setMmsCd(campaignNo);
                else if(Channel.FAX.equals(channelType))
                    scenarioVo.setFaxCd(campaignNo);
                else if(Channel.PUSH.equals(channelType))
                    scenarioVo.setPushCd(campaignNo);
                else if(Channel.FRIENDTALK.equals(channelType))
                    scenarioVo.setFriendtalkCd(campaignNo);
                else if(Channel.BRANDTALK.equals(channelType))
                    scenarioVo.setBrandtalkCd(campaignNo);
            }
        }
        scenarioVo.setChannels(channelList.toArray(new String[channelList.size()]));
        return scenarioVo;
    }

    /**
     * 캠페인 상세 정보를 가져온다.
     *
     * @param paramScenarioNo
     * @param paramCampaignNo
     * @return
     * @throws ParseException
     */
    public ScenarioVo getScenario2StepInfo(int paramScenarioNo, int paramCampaignNo) throws ParseException {
        ScenarioVo scenarioVo = scenarioDao.selectScenarioDetailInfo(paramScenarioNo, paramCampaignNo);

        final CampaignVo campaignVo = scenarioVo.getCampaignVo();
        final String channelType = campaignVo.getChannelType();
        final String senderEmail = campaignVo.getSenderEmail();
        if(Channel.MAIL.equals(channelType)) {
            if(campaignVo.getRetryCnt() == 0) {
                campaignVo.setRetryCnt(serverInfoDao.selectServerInfo().getRetryCnt());
            }
            if(StringUtil.isEmpty(campaignVo.getSenderNm())) {
                campaignVo.setSenderNm(campaignDao.selectCampaignSenderNm(scenarioVo));
            }

            EmailValidator validator = EmailValidator.getInstance();
            if(!validator.isValid(senderEmail)) {
                campaignVo.setSenderEmail(campaignDao.selectCampaignSenderEmail(scenarioVo));
            }
            if(!validator.isValid(campaignVo.getRetmailReceiver())) {
                campaignVo.setRetmailReceiver(campaignDao.selectCampaignRetmailReceiver(scenarioVo));
            }
        } else if(ChannelUtil.isUsePhoneNumber(channelType)) {
            if(StringUtil.isEmpty(campaignVo.getSenderTel())) {
                campaignVo.setSenderTel(campaignDao.selectCampaignSenderTel(scenarioVo));
            }
        } else if(Channel.FAX.equals(channelType)) {
            // senderFax(발신자 팩스번호)은 senderEmail에,
            // receiverFax(수신 팩스번호)은 retmailReceiver에 저장되어 있음.
            if(StringUtil.isEmpty(senderEmail) || !senderEmail.matches("[0-9-]+")) {
                campaignVo.setSenderEmail(campaignDao.selectCampaignSenderEmail(scenarioVo));
            }

            if(StringUtil.isEmpty(campaignVo.getRetmailReceiver()) || !campaignVo.getRetmailReceiver().matches("[0-9-]+")) {
                campaignVo.setRetmailReceiver(campaignDao.selectCampaignRetmailReceiver(scenarioVo));
            }
        } else if(Channel.PUSH.equals(scenarioVo.getCampaignVo().getChannelType())) {
            if(StringUtil.isEmpty(scenarioVo.getCampaignVo().getSenderTel())) {
                campaignVo.setSenderTel(campaignDao.selectCampaignSenderTel(scenarioVo));
            }
        }

        // 발송 날짜
        String sendStartString = null;
        Date date = new Date();
        if(!StringUtil.isEmpty(campaignVo.getSendStartDt())) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
            sendStartString = campaignVo.getSendStartDt() + " " + campaignVo.getSendStartTm();
            date = df.parse(sendStartString);
        }
        sendStartString = DateFormatUtils.format(date, "yyyyMMdd HHmm00");
        campaignVo.setSendStartDt(sendStartString.split(" ")[0]);
        campaignVo.setSendStartTm(sendStartString.split(" ")[1]);

        // 반응추적 start
        String startString = null;
        TraceInfoVo tv = traceInfoDao.selectTraceInfo(campaignVo.getCampaignNo());
        if(!StringUtil.isEmpty(tv.getStartDt())) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
            startString = tv.getStartDt() + " " + tv.getStartTm();
                date = df.parse(startString);
            }
        startString = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:00");
        campaignVo.getTraceInfoVo().setStartDt(startString.split(" ")[0]);
        campaignVo.getTraceInfoVo().setStartTm(startString.split(" ")[1]);

        // 반응추적 end
        String endString = null;
        if(!StringUtil.isEmpty(tv.getEndDt())) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
            endString = tv.getEndDt() + " " + tv.getEndTm();
                date = df.parse(endString);
                endString = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        } else {
            endString = DateFormatUtils.format(DateUtil.addDays(date, 7), "yyyy-MM-dd") + " 23:59:59";
        }

        campaignVo.getTraceInfoVo().setEndDt(endString.split(" ")[0]);
        campaignVo.getTraceInfoVo().setEndTm(endString.split(" ")[1]);

        return scenarioVo;
    }

    /**
     * 캠페인 수행 단계 데이터를 가져온다.
     *
     * @param paramScenarioVo
     * @return
     * @throws Exception
     */
    public ScenarioVo getScenario3StepInfo(ScenarioVo paramScenarioVo) throws Exception {
        ScenarioVo scenarioVo = scenarioDao.selectScenarioBasicInfo(paramScenarioVo);
        // 세그먼트 정보 가져오기
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(scenarioVo.getSegmentNo());

        scenarioVo.setSegmentNm(segmentVo.getSegmentNm());
        scenarioVo.setSegmentSize(segmentVo.getSegmentSize());

        List<CampaignVo> campaignList = scenarioVo.getCampaignList();
        List<String> channelList = new ArrayList<>();
        for(CampaignVo campaignVo : campaignList) {
            channelList.add(campaignVo.getChannelType());

            // 발송 날짜
            String nowDateTime = DateUtil.getNowDateTime();
            if(StringUtil.isBlank(campaignVo.getSendStartDt())) {
                campaignVo.setSendStartDt(nowDateTime.substring(0,8));
                campaignVo.setSendStartTm(nowDateTime.substring(8,12) + "00");
            } else {
                campaignVo.setSendStartTm(campaignVo.getSendStartTm().substring(0,4) + "00");
            }
            // 분할예약 정보를 가져온다.
            //[20210810] [ljh] [전채널 분할발송]
            if("Y".equals(campaignVo.getDivideYn())) {
                campaignVo.setCampaignDivideScheduleList((divideScheduleDao.getCampaignDivideSchedule(campaignVo.getCampaignNo())));
            }

            if(Channel.MAIL.equals(campaignVo.getChannelType())) {
                
                // 링크추적 정보를 가져온다.
                TraceInfoVo traceInfoVo = traceInfoDao.selectTraceInfo(campaignVo.getCampaignNo());
                campaignVo.setTraceInfoVo(traceInfoVo);

                // 반응추적 start DB: yyyyMMddHHmmss -> yyyyMMddHHmm00 포멧으로 변환
                if(StringUtil.isBlank(campaignVo.getTraceInfoVo().getStartDt())) {
                    campaignVo.getTraceInfoVo().setStartDt(nowDateTime.substring(0,8));
                    campaignVo.getTraceInfoVo().setStartTm(nowDateTime.substring(8,12) + "00");
                } else {
                    campaignVo.getTraceInfoVo().setStartTm(campaignVo.getTraceInfoVo().getStartTm().substring(0,4) + "00");
                }

                // 반응추적 end
                if(StringUtil.isBlank(campaignVo.getTraceInfoVo().getEndDt())) {
                    campaignVo.getTraceInfoVo().setEndDt(DateUtil.addDays(7, "yyyyMMdd"));
                    campaignVo.getTraceInfoVo().setEndTm("235959");
                }
            }
        }
        scenarioVo.setChannels(channelList.toArray(new String[channelList.size()]));

        return scenarioVo;
    }

    /**
     * 시나리오에 딸린 캠페인 채널 정보를 가져온다.
     *
     * @param scenarioNo
     * @return
     */
    public List<CampaignVo> getScenariochannelInfo(int scenarioNo) {
        return campaignDao.selectScenariochannelInfo(scenarioNo);
    }

    /**
     * 캠페인을 삭제한다.
     *
     * @param scenarioVo
     */
    public void setDeleteCampaign(ScenarioVo scenarioVo) {
        deleteScenarioCommon(scenarioVo);
        List<CampaignVo> rValue = campaignDao.selectScenariochannelInfo(scenarioVo.getScenarioNo());
        // 시나리오에 물려 있는 캠페인이 하나도 없는 경우라면 시나리오도 삭제한다.
        if(rValue.size() == 0) {
            scenarioDao.deleteScenario(scenarioVo);
        }
        // PUSH 정보삭제 deletePushServiceInfo
        if(Channel.PUSH.equals(scenarioVo.getCampaignVo().getChannelType())) {
            pushServiceInfoDao.deletePushServiceInfoByPk(ServiceType.CAMPAIGN, scenarioVo.getCampaignVo().getCampaignNo());
        }
    }

    public void deleteScenarioCommon(ScenarioVo scenarioVo) {
        testUserDao.deleteTestUserAll(scenarioVo);
        multipartFileDao.deleteMultipartFileAll(scenarioVo);
        applicationDao.deleteApplicationAll(scenarioVo);
        linkTraceDao.deleteCampLinkTraceAll(scenarioVo);
        traceInfoDao.deleteCamTraceInfoAll(scenarioVo);
        scheduleDao.deleteCamScheduleAll(scenarioVo);
        templateDao.deleteTemplateAll(scenarioVo);
        campaignDao.deleteCampaignAll(scenarioVo);
        divideScheduleDao.deleteDivideScheduleAll(scenarioVo);
    }

    /**
     * 캠페인을 복사한다.
     *
     * @param paramScenarioVo
     * @throws Exception
     */
    public void setRegistCopyScenario(ScenarioVo paramScenarioVo) throws Exception {
        ScenarioVo scenarioVo = getScenario1StepInfo(paramScenarioVo);
        scenarioVo.setCreateDt(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        scenarioVo.setCreateTm(DateFormatUtils.format(new Date(), "HHmmss"));

        // 캠페인 복사 시 시나리오명과 그 앞에 붙는 문자열("[COPY]")을 포함한 바이트수가 100바이트를 초과할 경우
        // 시나리오명을 자름
        if(scenarioVo.getScenarioNm().getBytes().length > 94) {
            scenarioVo.setScenarioNm(StringUtil.subStringNicely(scenarioVo.getScenarioNm(), 94));
        }

        int newScenarioNo = scenarioDao.copyScenario(scenarioVo);
        scenarioVo.setNewScenarioNo(newScenarioNo);

        List<CampaignVo> campaignList = scenarioVo.getCampaignList();
        if(campaignList != null) {
            for(int i = 0, n = campaignList.size(); i < n; i++) {
                CampaignVo campaignVo = campaignList.get(i);
                if(campaignVo.getDepthNo() != 1)
                    continue;
                scenarioVo.setCampaignVo(campaignVo);
                HandlerVo handlerVo = applicationDao.selectEditorCampaignHandler(campaignVo.getCampaignNo());
                List<TemplateVo> templateList = templateDao.selectEditorCampaignTemplate(campaignVo.getCampaignNo());
                scenarioVo.getCampaignVo().setHandlerVo(handlerVo);
                scenarioVo.getCampaignVo().setTemplateList(templateList);

                int newCampaignNo = copyCampaign(scenarioVo);
                campaignVo.setCampaignNo(newCampaignNo);

                // [20141114][이강욱] 캠페인복사시 첨부파일도 함께 복사
                List<MultipartFileVo> attachList = multipartFileDao.selectEditorCampaignMultipartFile(campaignVo.getCampaignNo());
                for(MultipartFileVo multipartFile : attachList) {
                    String srcPath = multipartFile.getFilePath();
                    File srcFile = new File(srcPath);

                    String destPath = srcFile.getParentFile().getParentFile().getParent() + "/" + Sprintf.format(newCampaignNo, 15, '0');
                    destPath += "/" + DateFormatUtils.format(new Date(), "yyyyMM") + "/" + multipartFile.getFileAlias();
                    File destFile = new File(destPath);

                    FileUtil.copyFile(srcFile, destFile);
                }
            }
        }
    }

    /**
     * 캠페인을 복사한다.
     *
     * @param scenarioVo
     * @return
     */
    public int copyCampaign(ScenarioVo scenarioVo) {
        int nextCampaignNo = campaignDao.selectNewCampaignNo();

        CampaignVo campaignVo = scenarioVo.getCampaignVo();
        campaignVo.setNewCampaignNo(nextCampaignNo);
        //SECUDB NONE
        // 2. NVCAMPAIGN COPY
        campaignDao.copyCampaign(scenarioVo);
        // 3. NVSCHEDULE COPY
        scheduleDao.copySchedule(scenarioVo);
        // 4. NVTRACEINFO COPY
        traceInfoDao.copyTraceInfo(campaignVo);
        // 5. NVLINKTRACE COPY
        linkTraceDao.copyLinkTrace(campaignVo);
        // 6. NVAPPLICATION COPY
        if(campaignVo.getHandlerVo() != null) {
            applicationDao.copyApplication(campaignVo);
        }
        // 7. TESTUSER COPY
        testUserDao.copyTestUserByCampaign(campaignVo);
        // 8. NVMULTIPARTFILE COPY
        multipartFileDao.copyMultipartFile(campaignVo);
        // 9. NVTEMPLATE COPY
        if(scenarioVo.getCampaignVo().getTemplateList() != null) {
            List<TemplateVo> tempalteList = scenarioVo.getCampaignVo().getTemplateList();
            for(int i = 0; i < tempalteList.size(); i++) {
                TemplateVo templateVo = tempalteList.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("newCampaignNo", scenarioVo.getCampaignVo().getNewCampaignNo());
                map.put("templateVo", templateVo);
                templateDao.copyTemplate(map);
            }
        }

        // 10. NVDIVIDESCHEDULE
        divideScheduleDao.copyDivideSchedule(scenarioVo.getCampaignVo());

        // 11. NVPUSHSERVICEINFO
        if(Channel.PUSH.equals(campaignVo.getChannelType())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("svcType", ServiceType.CAMPAIGN);
            paramMap.put("newSvcNo", new Integer(campaignVo.getNewCampaignNo()));
            paramMap.put("svcNo", campaignVo.getCampaignNo());
            pushServiceInfoDao.copyPushServiceInfo(paramMap);
        }

        return nextCampaignNo;
    }

    /**
     * 캠페인 발송 상태를 변경한다.
     *
     * @param scenarioVo
     */
    public void changeStatus(ScenarioVo scenarioVo) {
        campaignDao.changeStatus(scenarioVo);
    }

    /**
     * 캠페인 재발송을 처리한다. - 10.02.10 이현정 : wiseU5.5 리포트 개선작업으로 로직 수정
     *
     * @param paramScenarioVo
     * @param resendMode
     * @param envServerInfoVO
     * @throws Exception
     */
    public int setRegistResendCampaign(ScenarioVo paramScenarioVo, String resendMode, ServerInfoVo serverInfo, String linkSeq) throws Exception {
        ScenarioVo scenarioVo = scenarioDao.selectScenarioBasicInfo(paramScenarioVo);
        scenarioVo.setUserVo(paramScenarioVo.getUserVo());

        String campaignPrefaceAb = scenarioVo.getCampaignVo().getCampaignPrefaceAb();

        // 캠페인 복사와 함께 사용하기 위해서 NewScenarioNo를 현재 ScenarioNo로 세팅함.
        scenarioVo.setNewScenarioNo(scenarioVo.getScenarioNo());

        final List<CampaignVo> campaignVoList = scenarioVo.getCampaignList();

        if(campaignVoList == null) {
            return 0;
        }

        String abChoice = "A";
        int temCnt = 0;
        int tcnt = 0;

        //A/B최종 결과 가져오기
        final CampaignVo parentVo = paramScenarioVo.getCampaignVo();
        List<CampaignAbTestVo> abTestList = null;
        if("O".equals(parentVo.getAbTestCond())) {
            abTestList = sendLogDao.getCampaignReportAbTestOpenList(parentVo.getCampaignNo());
        } else {
            abTestList = sendLogDao.getCampaignReportAbTestLinkClickList(parentVo.getCampaignNo());
        }


        for(int i = 0; i < abTestList.size(); i++) {
            temCnt = abTestList.get(i).getOpenCnt();
            if(temCnt > tcnt) {
                tcnt = temCnt;
                abChoice = abTestList.get(i).getAbType();
            }
        }

        // 핸들러는 무조건 A/B 미사용 핸들러
        String defHandler = ""; //editorCampaignDao.selectEditorDefaultHanlder(parentVo.getHandlerType(), parentVo.getChannelType(), "N");

        int newCampaignNo = 0;
        for(int i = 0, n = campaignVoList.size(); i < n; i++) {
            CampaignVo campaignVo = campaignVoList.get(i);
            Map<String, Object> handlerMap = envDefaultHandleService.getHandlerUseList(Client.EM, campaignVo.getCampaignNo());
            if(null!= handlerMap && handlerMap.size() > 0) {
                @SuppressWarnings("unchecked")
                List<DefaultHandlerVo> handlerList = (List<DefaultHandlerVo>) handlerMap.get("list");
                if(null!=handlerList && handlerList.size() > 0) {
                    DefaultHandlerVo handler = new DefaultHandlerVo();
                    // class casting error 수정
                    handler.setSeq(handlerList.get(0).getSeq());
                    handler = envDefaultHandleService.selectDefaultHandler(handler.getSeq());
                    defHandler = handler.getHandler();
                }
            }

            scenarioVo.setCampaignVo(campaignVo);

            // 핸들러 정보를 가져온다.
            HandlerVo handlerVo = applicationDao.selectEditorCampaignHandler(campaignVo.getCampaignNo());

            // 템플릿 정보를 가져온다.
            List<TemplateVo> templateList = templateDao.selectEditorCampaignTemplate(campaignVo.getCampaignNo());

            //A/B가 템플릿 경우 (템플릿 경우 제목은 A/B 둘다 동일 본문만 다름)
            if(AB_TEST_TEMPLATE.equals(scenarioVo.getAbTestType())) {
                //디폴트 핸들러로 셋팅
                handlerVo.setHandler(defHandler);

                //최종 결과가 A이면
                if(AB_TEST_CHOICE_A.equals(abChoice)) {
                    //A본문으로 셋팅
                    campaignVo.setTemplateList(templateList.subList(0, 1));
                } else {
                    //B본문으로 셋팅
                    campaignVo.setTemplateList(templateList.subList(1, 2));
                }
            } else if(AB_TEST_SUBJECT.equals(scenarioVo.getAbTestType())) {
                //A/B가 제목일 경우  (제목 경우 본문은 A/B 둘다 동일 제목만 다름)
                //디폴트 핸들러로 셋팅
                handlerVo.setHandler(defHandler);

                    //최종 결과가 A이면
                if(AB_TEST_CHOICE_A.equals(abChoice)) {
                    //A본문으로 셋팅
                    campaignVo.setTemplateList(templateList.subList(0, 1));
                } else {
                    //B일 경우만 CampaignPrefaceAb의 제목을 CampaignPreface에 넣는다.
                    campaignVo.setCampaignPreface(campaignPrefaceAb);
                    //A본문으로 셋팅
                    campaignVo.setTemplateList(templateList.subList(0, 1));
                }
            } else {
                //A/B 미사용 경우
                campaignVo.setTemplateList(templateList);
            }
            campaignVo.setHandlerVo(handlerVo);
            campaignVo.setTemplateList(templateList);

            // resendMode 의 값은 relationType_구분값 형식으로 넘긴다. ex) L_open
            // - L은 타겟발송, R은 재발송
            String[] temp = resendMode.split("_");
            campaignVo.setRelationType(temp[0]);
            resendMode = temp[1];

            final Map<String, String> sqlMap = makeTargetSQLForResend(resendMode, campaignVo.getCampaignNo(), linkSeq, serverInfo);

            // 재발송 캠페인을 생성하고 새로 생성한 campaignNo을 받는다.
            newCampaignNo = resendCampaign(scenarioVo, sqlMap);

            // [20141114][이강욱] 캠페인복사시 첨부파일도 함께 복사
            List<MultipartFileVo> attachList = multipartFileDao.selectEditorCampaignMultipartFile(campaignVo.getCampaignNo());
            for(MultipartFileVo multipartFile : attachList) {
                String srcPath = multipartFile.getFilePath();
                File srcFile = new File(srcPath);

                String destPath = srcFile.getParentFile().getParentFile().getParent() + "/" + Sprintf.format(newCampaignNo, 15, '0');
                destPath += "/" + DateFormatUtils.format(new Date(), "yyyyMM") + "/" + multipartFile.getFileAlias();
                File destFile = new File(destPath);

                FileUtil.copyFile(srcFile, destFile);
            }
        }
        return newCampaignNo;
    }

    /**
     * 재발송용 캠페인을 생성한다. 본 캠페인의 데이터를 복사하고 SQLFilter SQL을 생성하여 NVSEGMENT 테이블에 저장한다.
     *
     * @param scenarioVo
     * @param sqlMap
     */
    public int resendCampaign(ScenarioVo scenarioVo, Map<String, String> sqlMap) {
        final CampaignVo campaignVo = scenarioVo.getCampaignVo();

        final SegmentVo segmentVo = segmentDao.selectSegmentByPk(campaignVo.getSegmentNo());
        segmentVo.setSegmentSize(commonDao.selectCountBySql(sqlMap.get("COUNT_SQL")));
        segmentVo.setSqlfilter(sqlMap.get("HEAD_SQL"));
        segmentVo.setPsegmentNo(campaignVo.getSegmentNo());
        segmentVo.setSegmentNm("[" + getResendHeader() + "]" + segmentVo.getSegmentNm());
        segmentVo.setSegmentNo(segmentDao.selectNextSegmentNo());
        segmentVo.setSegType(campaignVo.getRelationType());

        // 새로운 캠페인 번호를 가져온다.
        int nextCampaignNo = campaignDao.selectNewCampaignNo();

        // 새로운 캠페인번호, 세그먼트 번호를 담는다.
        campaignVo.setNewCampaignNo(nextCampaignNo);
        scenarioVo.setSegmentNo(segmentVo.getSegmentNo());
        scenarioVo.setSegmentSize(segmentVo.getSegmentSize());

        // 1. NVSEGMENT와 NVSEMANTIC 재발송 대상 캠페인 것을 copy 해서 생성한다.
        segmentDao.copySegmentForResend(segmentVo);
        semanticDao.copySemantic(segmentVo.getPsegmentNo(), segmentVo.getSegmentNo());
        // 2. NVCAMPAIGN COPY
        security.securityObject(scenarioVo.getCampaignVo(), "ENCRYPT");
        campaignDao.copyCampaignForResend(scenarioVo);

        // 3. NVSCHEDULE COPY
        scheduleDao.copyScheduleForResend(scenarioVo);

        // 4. NVTRACEINFO COPY
        traceInfoDao.copyTraceInfo(campaignVo);

        // 5. NVLINKTRACE COPY
        linkTraceDao.copyLinkTrace(campaignVo);

        // 6. NVAPPLICATION COPY
        applicationDao.copyApplication(campaignVo);

        // 7. TESTUSER COPY
        testUserDao.copyTestUserByCampaign(campaignVo);

        // 8. NVMULTIPARTFILE COPY
        multipartFileDao.copyMultipartFile(campaignVo);

        // 9. NVTEMPLATE COPY - 멀티템플릿이 존재할 수 있으므로 LIST로 가져온다.
        if(campaignVo.getTemplateList() != null) {
            List<TemplateVo> tempalteList = campaignVo.getTemplateList();

            // List 갯수에 따라 템플릿 생성한다.
            for(int i = 0; i < tempalteList.size(); i++) {
                TemplateVo templateVo = tempalteList.get(i);

                //templateVo.setSeg(" ");

                Map<String, Object> map = new HashMap<>();
                map.put("newCampaignNo", campaignVo.getNewCampaignNo());
                map.put("templateVo", templateVo);

                templateDao.copyTemplate(map);
            }
        }

        // 10. NVPUSHSERVICEINFO
        if(Channel.PUSH.equals(campaignVo.getChannelType())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("svcType", ServiceType.CAMPAIGN);
            paramMap.put("newSvcNo", new Integer(campaignVo.getNewCampaignNo()));
            paramMap.put("svcNo", campaignVo.getCampaignNo());
            pushServiceInfoDao.copyPushServiceInfo(paramMap);
        }

        return nextCampaignNo;
    }

    private String getResendHeader() {
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        return msAccessor.getMessage("report.campaign.list.resend", localeChangeInterceptor.getLocale());
    }

    /**
     * 캠페인 재발송용 대상자 SQL을 생성한다.<br>
     * 캠페인 리포트에서 재발송 버튼 클릭 시 사용 <br>
     * 이메일 채널만 지원함
     *
     * @param resendMode
     * @param parentCampaignNo
     * @param linkSeq
     * @param serverInfo
     * @return
     */
    private Map<String, String> makeTargetSQLForResend(String resendMode, int parentCampaignNo, String linkSeq, ServerInfoVo serverInfo) {
        String headSql = "";
        String countSql = "";

        if(Const.RESEND_R_FAIL.equals(resendMode)) { // 실패대상자 (재발송)
            //SECUDB NONE
            // NVSENDLOG : 해당 필드는 없음
            headSql = "SELECT DISTINCT CUSTOMER_KEY FROM NVSENDLOG WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND ERROR_CD NOT IN ("
                + StringUtil.getErrorCodeListForDB(serverInfo.getResendErrorCd()) + ")";
            countSql = "SELECT COUNT(DISTINCT CUSTOMER_KEY) FROM NVSENDLOG WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND ERROR_CD NOT IN ("
                + StringUtil.getErrorCodeListForDB(serverInfo.getResendErrorCd()) + ")";

        } else if(Const.RESEND_L_OPEN.equals(resendMode)) { // 오픈 대상자 (타겟발송)
            //SECUDB NONE
            //NVRECEIPT : 해당 필드 없은
            headSql = "SELECT DISTINCT CUSTOMER_ID FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo;
            countSql = "SELECT COUNT(DISTINCT CUSTOMER_ID) CUSTOMER_ID FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo;

        } else if(Const.RESEND_L_DURATION.equals(resendMode)) { // 10초 이상 확인 (타겟발송)
            //SECUDB NONE
            //NVRECEIPT : 해당 필드 없은
            headSql = "SELECT DISTINCT CUSTOMER_ID FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND READING_DURATION >= 10";
            countSql = "SELECT COUNT(DISTINCT CUSTOMER_ID) FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND READING_DURATION >= 10";

        } else if(Const.RESEND_L_DUPLICATION.equals(resendMode)) { // 1회이상 확인 (타겟발송)
            //SECUDB NONE
            //NVRECEIPT : 해당 필드 없은
            headSql = "SELECT CUSTOMER_ID FROM " + "(SELECT CUSTOMER_ID, COUNT(*) DUPLICATION_CNT FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo + " GROUP BY customer_id ) A "
                + "WHERE DUPLICATION_CNT > 1 ";
            countSql = "SELECT COUNT(*) FROM " + "(SELECT CUSTOMER_ID, COUNT(*) DUPLICATION_CNT FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo + " GROUP BY customer_id ) A "
                + "WHERE DUPLICATION_CNT > 1 ";

        } else if(Const.RESEND_L_LINK.equals(resendMode)) { // 링크 클릭 (타겟발송)
            //SECUDB NONE
            //NVLINKRESULT : 해당 필드 없음
            headSql = "SELECT DISTINCT CUSTOMER_ID FROM NVLINKRESULT WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND LINK_SEQ in (" + linkSeq + ")";
            countSql = "SELECT COUNT(DISTINCT CUSTOMER_ID) FROM NVLINKRESULT WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND LINK_SEQ in (" + linkSeq + ")";

        } else if(Const.RESEND_L_MOBILE.equals(resendMode)) { // 모바일 확인 (타겟발송)
            //SECUDB NONE
            //NVRECEIPT : 해당 필드 없은
            headSql = "SELECT DISTINCT CUSTOMER_ID FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND MOBILE_YN = 'Y'";
            countSql = "SELECT COUNT(DISTINCT CUSTOMER_ID) FROM NVRECEIPT WHERE CAMPAIGN_NO = " + parentCampaignNo + " AND MOBILE_YN = 'Y'";

        }

        final Map<String, String> map = new HashMap<>();
        map.put("HEAD_SQL", headSql);
        map.put("COUNT_SQL", countSql);

        return map;
    }

    /**
     * 재발송 모드에 따라 대상자 SQL과 대상자 카운트 SQL 생성
     *
     * @param resendMode
     * @param campaignNo
     * @param resultSeq
     * @param channel
     * @return Map
     *         <ul>
     *         <li>"HEAD_SQL": 대상자 쿼리</li>
     *         <li>"COUNT_SQL": 대상자 카운트 쿼리</li>
     *         </ul>
     */
    public String makeTargetSQL(String resendMode, int campaignNo, String resultSeq, String channel) {
        if(Channel.MAIL.equals(channel)) {
            return makeEmailTargetSQL(resendMode, campaignNo, resultSeq);
        } else if(Channel.FAX.equals(channel)) {
            return makeFaxTargetSQL(resendMode, campaignNo, resultSeq);
        } else if(Channel.SMS.equals(channel) || Channel.LMSMMS.equals(channel)) {
            return makeSmsTargetSQL(resendMode, campaignNo, resultSeq);
        } else {
            return makeOmniTargetSQL(resendMode, campaignNo, resultSeq, channel);
        }
    }

    /**
     * SMS 채널 캠페인의 멀티채널 하위 대상자 SQL 생성
     *
     * @param resendMode
     * @param campaignNo
     * @param resultSeq
     * @return
     */
    private String makeSmsTargetSQL(String resendMode, int campaignNo, String resultSeq) {
        // A.TRAN_RSLT 컬럼은 char type 임으로 ' 기호 추가 함.
        if(Const.RESEND_R_SUCCESS.equals(resendMode)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM EM_LOG A, NVRESENDTARGET B");
            sb.append(" WHERE A.SERVICE_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND A.TRAN_RSLT IN ('").append(PropertyUtil.getProperty("sms.code.success","0")).append("')");
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.SERVICE_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");
            //SECUDB CHK
            return sb.toString();

        } else if(Const.RESEND_R_FAIL.equals(resendMode)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM EM_TRAN A, NVRESENDTARGET B");
            sb.append(" WHERE A.SERVICE_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.SERVICE_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");
            sb.append(" UNION ALL");
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM EM_LOG A, NVRESENDTARGET B");
            sb.append(" WHERE A.SERVICE_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND A.TRAN_RSLT NOT IN ('").append(PropertyUtil.getProperty("sms.code.success","0")).append("')");
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.SERVICE_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");
            //SECUDB CHK
            return sb.toString();
        }

        return "";
    }

    /**
     * FAX 채널 캠페인의 멀티채널 하위 대상자 SQL 생성
     *
     * @param resendMode
     * @param campaignNo
     * @param resultSeq
     * @return
     */
    private String makeFaxTargetSQL(String resendMode, int campaignNo, String resultSeq) {
        if(Const.RESEND_R_SUCCESS.equals(resendMode) || Const.RESEND_R_FAIL.equals(resendMode)) {
            String inCondition;
            if(Const.RESEND_R_SUCCESS.equals(resendMode)) {
                inCondition = " IN (";
            } else {
                inCondition = " NOT IN (";
            }

            final StringBuilder sb = new StringBuilder();
            //SECUDB CHK
            // NVSENDLOG : 해당 필드는 없음
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM NVSENDLOG A, NVRESENDTARGET B");
            sb.append(" WHERE A.CAMPAIGN_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND A.ERROR_CD ").append(inCondition).append(PropertyUtil.getProperty("fax.code.success")).append(")");
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.CAMPAIGN_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        }

        return "";
    }

    /**
     * EMAIL 채널 멀티채널 하위 대상자 SQL을 생성한다.<br>
     * 이메일 채널은 성공(resendSuccess), 실패(resendFail), 오픈(resendOpen) 모드를 지원한다.
     *
     * @param resendMode
     * @param campaignNo 상위 캠페인 번호
     * @param resultSeq
     * @return
     */
    private String makeEmailTargetSQL(String resendMode, int campaignNo, String resultSeq) {
        if(Const.RESEND_R_SUCCESS.equals(resendMode) || Const.RESEND_R_FAIL.equals(resendMode)) {
            String inCondition;
            if(Const.RESEND_R_SUCCESS.equals(resendMode)) {
                inCondition = " IN (";
            } else {
                inCondition = " NOT IN (";
            }

            final StringBuilder sb = new StringBuilder();
            //SECUDB CHK
            // NVSENDLOG : 해당 필드는 없음
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM NVSENDLOG A, NVRESENDTARGET B");
            sb.append(" WHERE A.CAMPAIGN_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND A.ERROR_CD ").append(inCondition).append(PropertyUtil.getProperty("email.code.success", "250")).append(")");
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.CAMPAIGN_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        } else if(Const.RESEND_L_OPEN.equals(resendMode)) {
            final StringBuilder sb = new StringBuilder();
            //SECUDB CHK
            //NVRECEIPT : 해당 필드 없은
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM NVRECEIPT A, NVRESENDTARGET B");
            sb.append(" WHERE A.CAMPAIGN_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.CAMPAIGN_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        }

        return "";
    }

    /**
     * 캠페인 리스트의 총 Row Count를 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public int getScenarioTotalCount(ScenarioVo scenarioVo) {
        return scenarioDao.getScenarioTotalCount(scenarioVo);
    }

    /**
     * 캠페인 리스트를 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public List<ScenarioVo> getCampaignScenarioList(ScenarioVo scenarioVo) {
        return scenarioDao.getCampaignScenarioList(scenarioVo);
    }

    /**
     * 캠페인 에러 상세내역을 가져온다.
     *
     * @param campaignNo
     * @param resultSeq
     * @return
     */
    public String getCampaignError(int campaignNo, long resultSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("campaignNo", campaignNo);
        map.put("resultSeq", resultSeq);

        return sendResultDao.getCampaignError(map);
    }

    /**
     * 캠페인 번호를 가져온다.
     *
     * @param scenarioNo
     * @param depthNo
     * @return
     */
    public int getCampaignNo(int scenarioNo, int depthNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("scenarioNo", scenarioNo);
        map.put("depthNo", depthNo);

        return campaignDao.getCampaignNo(map);
    }

    /**
     * 옴니채널 메시지용 하위 캠페인을 생성한다.<br>
     * 일반 재발송과 다른 점은 상위 캠페인과 하위 캠페인의 채널이 다를 수 있다는 점이다.
     *
     * @param scenarioNo 상위 캠페인의 시나리오 번호
     * @param campaignNo 상위 캠페인 번호
     * @param resendParam 캠페인 생성에 필요한 파라미터 형식은 'relationType'_'resendMode'_'channel'
     * @param serverInfoVo 재발송 에러코드를 저장하고 있는 객체
     * @param userVo 계정 정보
     * @return
     */
    public int makeOmniChannelSubCampaign(int scenarioNo, int campaignNo, String resendParam, ServerInfoVo serverInfo, UserVo userVo) {
        final ScenarioVo scenarioVo = scenarioDao.selectScenarioDetailInfo(scenarioNo, campaignNo);
        scenarioVo.setNewScenarioNo(scenarioNo);
        scenarioVo.setUserVo(userVo);
        final String[] params = resendParam.split("_");
        final String relationType = params[0];
        final String resendMode = params[1];
        final String channel = params[2];

        String resultSeq = "0000000000000000";
        final CampaignVo campaignVo = scenarioVo.getCampaignVo();
        if(STATUS_END.equals(campaignVo.getCampaignSts())) {
            long tmp = sendResultDao.selectResultSeq(campaignNo);
            if(tmp > 0L) {
                resultSeq = String.valueOf(tmp);
            }
        }

        final String headSql = makeTargetSQL(resendMode, campaignVo.getCampaignNo(), resultSeq, campaignVo.getChannelType());
        final SegmentVo segmentVo = makeSegmentVo(scenarioVo.getSegmentNo(), "M", headSql);

        campaignVo.setNewCampaignNo(campaignDao.selectNewCampaignNo());
        campaignVo.setChannelType(channel);
        campaignVo.setSenderEmail(campaignDao.selectCampaignSenderEmail(scenarioVo));
        campaignVo.setRetmailReceiver(campaignDao.selectCampaignRetmailReceiver(scenarioVo));
        // 상위 캠페인의 relation_tree에 새로 생성하는 캠페인의 relation_type을 붙여 캠페인간 관계를 나타낸다.
        final String parentRelationTree = campaignVo.getRelationTree();
        campaignVo.setRelationTree(parentRelationTree + relationType);
        campaignVo.setRelationType(relationType);

        scenarioVo.setSegmentNo(segmentVo.getSegmentNo());

        insertCampaignAndRelatedTable(scenarioVo, segmentVo);
        return campaignVo.getNewCampaignNo();
    }

    /**
     * 캠페인과 대상자, 시맨틱 정보를 저장한다.
     *
     * @param scenarioVo 캠페인 시나리오
     * @param segmentVo 대상자
     */
    public void insertCampaignAndRelatedTable(final ScenarioVo scenarioVo, final SegmentVo segmentVo) {
        segmentDao.copySegmentForResend(segmentVo);
        semanticDao.copySemantic(segmentVo.getPsegmentNo(), segmentVo.getSegmentNo());
        campaignDao.copyCampaignForResend(scenarioVo);
        traceInfoDao.copyTraceInfo(scenarioVo.getCampaignVo());
        scheduleDao.copyScheduleForResend(scenarioVo);
    }

    /**
     * SegmentVo 객체를 생성하고 데이터를 설정한다.
     *
     * @param segmentNo
     * @param segType
     * @param headSql
     * @return
     */
    private SegmentVo makeSegmentVo(final int segmentNo, final String segType, final String headSql) {
        final SegmentVo segmentVo = segmentDao.selectSegmentByPk(segmentNo);
        segmentVo.setSegmentSize(selectTargetCount(headSql));
        segmentVo.setSqlHead(headSql);
        segmentVo.setSqlBody(" ");
        segmentVo.setPsegmentNo(segmentNo);
        segmentVo.setSegmentNo(segmentDao.selectNextSegmentNo());
        segmentVo.setSegType(segType);

        return segmentVo;
    }

    /**
     * 대상자 SQL Count 쿼리를 실행하여 결과 반환
     *
     * @param headSql
     * @return
     */
    public int selectTargetCount(String headSql) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM (");
        sb.append(headSql);
        sb.append(") Z");

        return commonDao.selectCountBySql(sb.toString());
    }

    /**
     * 선택한 캠페인의 하위 캠페인을 찾아 Map 객체에 저장한다.
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 현재 캠페인 번호
     * @param depthNo 조회할 하위 캠페인 Depth
     * @param map 하위 캠페인을 저장할 Map 객체
     */
    public void findSubCampaignAndStoreMap(int scenarioNo, int campaignNo, int depthNo, Map<String, Object> map) {
        arrangeSubCampaignList(campaignDao.selectSubCampaignList(makeCampaignVo(scenarioNo, campaignNo, depthNo)), map);
    }

    /**
     * 성공, 실패, 오픈으로 하위 캠페인 리스트를 분류한다.
     *
     * @param campaignList 모든 하위  캠페인 리스트
     * @param map 분류한 하위 캠페인을 저정할 객체
     */
    private void arrangeSubCampaignList(final List<CampaignVo> campaignList, final Map<String, Object> map) {
        final List<CampaignVo> successList = new ArrayList<>();
        final List<CampaignVo> failList = new ArrayList<>();
        final List<CampaignVo> openList = new ArrayList<>();

        String relation;
        for(CampaignVo vo : campaignList) {
            relation = vo.getRelationType();
            if(Const.RELATION_SUCCESS.equals(relation)) {
                successList.add(vo);
            } else if(Const.RELATION_FAIL.equals(relation)) {
                failList.add(vo);
            } else if(Const.RELATION_OPEN.equals(relation)) {
                openList.add(vo);
            }
        }

        map.put("successSubCampaignList", successList);
        map.put("failSubCampaignList", failList);
        map.put("openSubCampaignList", openList);
    }

    /**
     * 하위 캠페인 조회에 사용할 CampaignVo 객체 생성
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 현재 캠페인 번호
     * @param depthNo 선택한 캠페인의 DEPTH. 하위 캠페인은 이 값보다 1이 크다.
     * @return
     */
    private CampaignVo makeCampaignVo(int scenarioNo, int campaignNo, int depthNo) {
        final CampaignVo campaignVo = new CampaignVo();
        campaignVo.setScenarioNo(scenarioNo);
        campaignVo.setCampaignNo(campaignNo);
        campaignVo.setDepthNo(depthNo + 1);
        return campaignVo;
    }

    /**
     * 최상위 캠페인에 설정한 대상자의 Semantic Field Key 목록을 바탕으로 생성 가능한 하위 채널 목록을 반환한다.
     *
     * @param scenarioNo
     * @return
     */
    public List<String> findAvailableChannels(int scenarioNo) {
        String[] channelArr = PropertyUtil.getProperty("omnichannel.message.channel").split(",");
        if(channelArr.length == 0) {
            return new ArrayList<>();
        }

        List<String> keyList = semanticDao.selectCampaignSemanticKey(scenarioNo);
        if(keyList.isEmpty()) {
            return new ArrayList<>();
        }

        String fieldKeys = makeFieldKeys(keyList);

        List<String> list = new ArrayList<>();
        String[] campaignUseList = PropertyUtil.getProperty("campaign.channel.use.list").split(",");
        for(String channel : channelArr) {
            if(Arrays.asList(campaignUseList).contains(channel)){   //캠페인 사용채널 목록 체크
                if(isAvailableChannel(fieldKeys, channel)) {
                    list.add(channel);
                }
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
        } else if(ChannelUtil.isUsePhoneNumber(channel)) {
            if(semanticFieldeKeys.indexOf(Const.SEMANTIC_KEY_TELEPHONE) > -1)
                return true;
        } else if(channel.equals(Channel.FAX)) {
            if(semanticFieldeKeys.indexOf(Const.SEMANTIC_KEY_FAX) > -1)
                return true;
        }

        return false;
    }

    /**
     * 옴니채널 메시지 캠페인을 전부 조회한다.
     *
     * @param scenarioVo
     * @return
     */
    public List<ScenarioVo> findOmniChannelCampaigns(ScenarioVo scenarioVo) {
        return scenarioDao.selectOmniChannelCampaigns(scenarioVo);
    }

    /**
     * 멀티채널 캠페인(PUSH) 대상자 SQL 생성
     *
     * @param resendMode
     * @param campaignNo
     * @param resultSeq
     * @return
     */
    private String makeOmniTargetSQL(String resendMode, int campaignNo, String resultSeq, String channel) {
        /*String errorCd ="";
        if(Const.Channel.PUSH.equals(channel)) {
            errorCd = PropertyUtil.getProperty("push.code.success","0");
        }else if(Const.Channel.FRIENDTALK.equals(channel)) {
            errorCd = PropertyUtil.getProperty("friendtalk.code.success","0000");
        }else if(Const.Channel.BRANDTALK.equals(channel)) {
            errorCd = PropertyUtil.getProperty("brandtalk.code.success","0000");
        }else if(Const.Channel.ALIMTALK.equals(channel)) {
            errorCd = PropertyUtil.getProperty("altalk.code.success","0000");
        }*/

        if(Const.RESEND_R_SUCCESS.equals(resendMode) || Const.RESEND_R_FAIL.equals(resendMode)) {
            String inCondition;
            if(Const.RESEND_R_SUCCESS.equals(resendMode)) {
                inCondition = " IN (";
            } else {
                inCondition = " NOT IN (";
            }

            final StringBuilder sb = new StringBuilder();
            //SECUDB CHK
            // NVSENDLOG : 해당 필드는 없음
            sb.append(" SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2");
            sb.append(" FROM NVSENDLOG A, NVRESENDTARGET B");
            sb.append(" WHERE A.CAMPAIGN_NO = ").append(campaignNo);
            sb.append(" AND A.RESULT_SEQ = ").append(resultSeq);
            sb.append(" AND A.ERROR_CD ").append(inCondition).append(PropertyUtil.getProperty("push.code.success")).append(")");
            sb.append(" AND B.CLIENT = 'EM'");
            sb.append(" AND A.CAMPAIGN_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        }

        return "";
    }

    public List<KakaoProfile> getKakaoProfileList(String userId) {
        return kakaoProfileDao.selectKakaoProfileList(userId);
    }
}
