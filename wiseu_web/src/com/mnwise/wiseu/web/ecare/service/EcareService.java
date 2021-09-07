package com.mnwise.wiseu.web.ecare.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.Const.EcareSubType;
import com.mnwise.wiseu.web.base.Const.MailType;
import com.mnwise.wiseu.web.base.util.ZipUtil;
import com.mnwise.wiseu.web.campaign.model.DivideSchedule;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.ecare.dao.AddQueryDao;
import com.mnwise.wiseu.web.ecare.dao.CycleItemDao;
import com.mnwise.wiseu.web.ecare.dao.EcMsgHandlerDao;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareDivideScheduleDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTraceInfoDao;
import com.mnwise.wiseu.web.ecare.dao.EcmScheduleDao;
import com.mnwise.wiseu.web.ecare.model.AddQueryVo;
import com.mnwise.wiseu.web.ecare.model.CycleItemVo;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.model.EcareScheduleVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.ecare.model.ScheduleParam;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;

/**
 * 이케어 서비스
 */
@Service
public class EcareService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(EcareService.class);

    private static final String SELECT_NVREALTIMEACCEPT_JONMUN_SQL ="SELECT JONMUN FROM NVREALTIMEACCEPT WHERE SEQ = ${seq}";
    private static final String UPDATE_NVREALTIMEACCEPT_SEND_FG_SQL = "UPDATE NVREALTIMEACCEPT SET SEND_FG = #{_SEND_FG}, LIST_SEQ = #{_LIST_SEQ}, ERROR_MSG = #{_ERROR_MESSAGE} WHERE SEQ = ${seq}";

    @Autowired private CycleItemDao cycleItemDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private EcareDao ecareDao;
    @Autowired private EcareTemplateDao ecareTemplateDao;
    @Autowired private EcmScheduleDao ecmScheduleDao;
    @Autowired private EcMsgHandlerDao ecMsgHandlerDao;
    @Autowired private ServerInfoDao serverInfoDao;
    @Autowired private AddQueryDao addQueryDao;
    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private EcareTraceInfoDao ecareTraceInfoDao;
    
    @Autowired private EcareDivideScheduleDao ecareDivideScheduleDao;

    @Value("${security.mail.use:off}")
    protected String useSecurityMail;
    @Value("${pdf.mail.use:off}")
    protected String usePdfMail;

    /**
     * 이케어 검증 부서를 등록한다.
     *
     * @param ecareNo
     * @param verifyYn
     * @param verifyGrpCd
     * @return
     */
    public String setEcareVerifyInfo(int ecareNo, String verifyYn, String verifyGrpCd) {
        EcareVo ecareVo = new EcareVo();
        ecareVo.setEcareNo(ecareNo);
        ecareVo.setVerifyYn(verifyYn);
        ecareVo.setVerifyGrpCd(verifyGrpCd);

        ecareDao.updateEcareVerifyInfo(ecareVo);

        return ecareVo.getVerifyYn();
    }

    /**
     * 이케어 상태를 변경한다.
     *
     * @param ecareNo
     * @param ecareSts
     * @return
     */
    public String setEcareStsInfo(int ecareNo, String ecareSts) {
        EcareVo ecareVo = ecareDao.selectEcareInfo(ecareNo);
        // 승인 요청 중인 이케어 인지를 우선 체크한다.
        //if ("C".equals(ecareSts) && ecareSts.equals(ecareVo.getEcareSts())) {
            //ecareSts = "I";  // 승인 취소 이므로 중지 상태로 변경한다.
        //}

        // 이미 다른 사람(또는 다른 브라우저)에서 상태가 변경된 경우
        if(ecareSts.equals(ecareVo.getEcareSts())) {
            return "blockChange";
        }

        ecareVo.setEcareNo(ecareNo);
        ecareVo.setEcareSts(ecareSts);

        String serviceType = ecareVo.getServiceType();
        String subType = ecareVo.getSubType();
        // 준실시간, 실시간 인 경우에는 발송 스케쥴 정보를 체크하지 않는다.
        if(serviceType.equals("S") && !"N".equals(subType)) {
            String ecareCycleCd = ecareDao.selectEcareCycleCdInfo(ecareNo);
            if(ecareCycleCd == null) {
                return "notSchedule";
            }
        }
        // ecareDao.updateEcareStsInfo(ecareVo);
        ecareDao.updateEcareStsInfo(ecareVo);
        return ecareSts;
    }

    /**
     * 이케어 스케쥴을 저장한다.
     * TODO 이케어 수행 화면에서 해당 메소드가 호출될 때 scenarioNo 값에 ecareNo 값이 호출된다.
     * 씨티은행 청구서 관련하여 accountDt 가 NVECAREMSG 쪽에 저장되어야 하므로, serviceType 을 받아온다.
     */
    public void setRegistEcareScheduleInfo(int scenarioNo, int scheduleNo, String queryString, String serviceType) {
        StringTokenizer st = new StringTokenizer(queryString, "&");
        String[] temp = null;
        ScheduleParam scheduleParam = new ScheduleParam();
        MutablePropertyValues values = new MutablePropertyValues();
        while(st.hasMoreTokens()) {
            temp = StringUtils.delimitedListToStringArray(st.nextToken(), "=");
            try {
                if(values.contains(temp[0])) {
                    PropertyValue pv = values.getPropertyValue(temp[0]);
                    Object obj = pv.getValue();
                    String value = (String) obj + "," + java.net.URLDecoder.decode(temp[1], "UTF-8");
                    values.addPropertyValue(temp[0], value);
                } else {
                    values.addPropertyValue(temp[0], java.net.URLDecoder.decode(temp[1], "UTF-8"));
                }
            } catch(UnsupportedEncodingException e) {
                log.error(null, e);
            }
        }

        DataBinder binder = new DataBinder(scheduleParam);
        binder.bind(values);

        EcareScheduleVo ecareScheduleVo = new EcareScheduleVo();
        ecareScheduleVo.setEcmScheduleNo(scheduleNo);
        ecareScheduleVo.setCycleCd(scheduleParam.getCycleCd());
        ecareScheduleVo.setSendStartDt(scheduleParam.getSendStartDt().replaceAll("-", ""));
        ecareScheduleVo.setSendEndDt(scheduleParam.getSendEndDt().replaceAll("-", ""));
        ecareScheduleVo.setInvokeTm(scheduleParam.getInvokeTm().replaceAll(":", ""));

        // 초기화
        ecareScheduleVo.setDay(0);
        ecareScheduleVo.setScheWeeknumber(0);
        ecareScheduleVo.setWeekday("");
        ecareScheduleVo.setInvokeEveryMin("N");
        ecareScheduleVo.setStartTm(null);
        ecareScheduleVo.setEndTm(null);
        ecareScheduleVo.setTermMin(null);

        if(scheduleParam.getCycleCd().equals("3")) {
            if(scheduleParam.getMonthOpt() != null) {
                if(scheduleParam.getMonthOpt().equals("STATIC")) {
                    /**
                     * TODO 발송일자를 입력값이 있으면 0으로 셋팅하도록 되어 있었다. DAY에 제대로 insert가 안되므로 수정한다.
                     **/
                    /* if (scheduleParam.getDay() > 0) { ecareScheduleVo.setDay(0); } else { ecareScheduleVo.setDay(scheduleParam.getDay()); } */
                    ecareScheduleVo.setDay(scheduleParam.getDay());
                } else if(scheduleParam.getMonthOpt().equals("CONTEXT")) {
                    ecareScheduleVo.setScheWeeknumber(scheduleParam.getScheWeeknumber());
                    ecareScheduleVo.setWeekday(scheduleParam.getWeekday());
                }
            }
        } else if(scheduleParam.getCycleCd().equals("1")) {
            if(StringUtil.isNotEmpty(scheduleParam.getMinStartTm())) {
                ecareScheduleVo.setInvokeEveryMin("Y");
                ecareScheduleVo.setStartTm(scheduleParam.getMinStartTm().replaceAll(":", ""));
                ecareScheduleVo.setEndTm(scheduleParam.getMinEndTm().replaceAll(":", ""));
                ecareScheduleVo.setTermMin(scheduleParam.getTermMin());
            }
        }

        // NVECMSCHEDULE UPDATE
        ecmScheduleDao.updateEcareScheduleInfo(ecareScheduleVo);

        // NVCYCLEITEM UPDATE
        cycleItemDao.deleteEcareCycleItemInfo(scheduleNo);

        // NVCYCLEITEM 테이블에 필요없는 데이터인데도 없다면 조인 쿼리를 못하는 문제로 한번만 옵션일 경우 값을 강제로
        // 넣어준다.
        if(scheduleParam.getCycleCd().equals("4")) {
            CycleItemVo cycleItemVo = new CycleItemVo();
            cycleItemVo.setEcmScheduleNo(scheduleNo);
            cycleItemVo.setCycleItem("ONTIME");
            cycleItemVo.setCheckYn("Y");
            cycleItemDao.insertCycleItem(cycleItemVo);
        } else {
            if(scheduleParam.getCycleItem() != null) {
                String[] cycleItems = scheduleParam.getCycleItem().split(",");
                for(int i = 0; i < cycleItems.length; i++) {
                    CycleItemVo cycleItemVo = new CycleItemVo();
                    cycleItemVo.setEcmScheduleNo(scheduleNo);
                    cycleItemVo.setCycleItem(cycleItems[i]);
                    cycleItemVo.setCheckYn("Y");
                    cycleItemDao.insertCycleItem(cycleItemVo);
                }
            }

        }
    }

    /**
     * 선택한 대상자로 현재 이케어의 대상자를 변경한다.
     *
     * @param ecareNo
     * @param segmentNo
     */
    public void updateTargetChgToOrg(int ecareNo, int segmentNo) {
        EcareVo ecareVo = ecareDao.selectEcareInfo(ecareNo);
        int orgSegmentNo = ecareVo.getSegmentNo();
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(segmentNo);
        List<SemanticVo> semanticList = semanticDao.selectSemanticListBySegmentNo(segmentNo);

        // 대상자 쿼리 복사
        segmentDao.updateSegmentByPk(
                new SegmentVo.Builder(orgSegmentNo)
                        .setSqlHead(segmentVo.getSqlHead())
                        .setSqlBody(segmentVo.getSqlBody())
                        .setSqlTail(segmentVo.getSqlTail())
                        .setUpdateQuery(segmentVo.getUpdateQuery())
                        .setLastUpdateDt(DateUtil.dateToString("yyyyMMdd", new Date()))
                        .build()
        );

        if(semanticList != null) {
            semanticDao.deleteSemanticBySegmentNo(orgSegmentNo);//삭제 후 적재
            for(SemanticVo semanticVo : semanticList) {
                semanticVo.setSegmentNo(orgSegmentNo);
                semanticDao.insertSemantic(semanticVo);     //(NVSEMANTIC) 정보를 입력한다.
            }
        }
    }


    /**
     * 서비스 아이디가 등록된게 있는지를 확인한다.
     *
     * @param serviceID
     * @return
     */
    public int checkServiceID(String serviceID) {
        return ecareDao.checkServiceID(serviceID);
    }

    /**
     * 조건에 맞는 전체 이케어를 '점검' 혹은 '실행' 상태로 변경
     *
     * @param checkYn
     */
    public void changeAllEcareStatus(final String checkYn) {
        if(checkYn == null || checkYn.length() == 0) {
            return;
        }

        final EcareVo ecareVo = new EcareVo();
        final char yN = checkYn.charAt(0);
        if(yN == 'Y') {
            ecareVo.setPrevEcareSts("R");
            ecareVo.setEcareSts("T");
        } else if(yN == 'N') {
            ecareVo.setPrevEcareSts("T");
            ecareVo.setEcareSts("R");
        } else {
            return;
        }

        ecareDao.updateAllEcareSts(ecareVo);
    }

    /**
     * 등록된 모든 템플릿 또는 핸들러를 파일로 다운로드 한 후 zip파일로 압축한다.
     *
     * @param contentType
     * @param path
     * @return
     * @throws Exception
     */
    public File makeZipFile(final String contentType, String path) throws Exception {
        final File dir = new File(path);
        FileUtil.forceMkdir(dir);

        if("T".equals(contentType)) {
            final List<TemplateVo> list = ecareTemplateDao.selectTemplateList();

            for(TemplateVo templateVo : list) {
                writeFile(path + "/" + getFileName(templateVo), templateVo.getTemplate());
            }
        } else if("H".equals(contentType)) {
            final List<HandlerVo> list = ecMsgHandlerDao.selectHandlerList();

            for(HandlerVo handlerVo : list) {
                writeFile(path + "/" + "ecare_" + handlerVo.getNo() + ".han", handlerVo.getHandler());
            }
        } else {
            throw new IllegalArgumentException("content type is not 'T' or 'H'. received argunent [" + contentType + "]");
        }

        final File zipFile = new File(path + "/" + ("T".equals(contentType) ? "Template" : "Handler") + ".zip");
        ZipUtil.compressZip(FileUtil.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE), zipFile.getAbsolutePath());

        return zipFile;
    }

    /**
     * 이케어 템플릿 파일명을 설정
     *
     * - 본문: ecare_{이케어 번호}.tem
     * - 커버: ecare_{이케어 번호}_COVER.tem
     * - 머릿말: ecare_{이케어 번호}_PREFACE.tem
     *
     * @param templateVo
     * @return
     */
    private String getFileName(TemplateVo templateVo) {
        String fileName = "ecare_" + templateVo.getNo();
        if(StringUtil.isBlank(templateVo.getSeg())) {
            fileName += ".tem";
        } else {
            fileName += "_" + templateVo.getSeg() + ".tem";
        }
        return fileName;
    }

    /**
     * 원하는 내용을 로컬 서버 파일에 기록
     *
     * @param filePath
     * @param content
     * @throws IOException
     */
    private void writeFile(String filePath, String content) throws IOException {
        FileWriter out = null;

        try {
            out = new FileWriter(filePath);
            out.write(StringUtil.defaultString(content, " "));
        } catch(IOException e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }


    /**
     * 이케어 등록 화면에서 선택할 기본 옵션으로 설정해서 EcareVo를 생성한다.
     *
     * @return 기본 옵션을 설정한 EcareVo
     */
    public EcareVo getEcareVoWithDefaultValue() {
        EcareVo ecareVo = new EcareVo();
        ecareVo.setChannelType("M");
        ecareVo.setMailType(MailType.NONE);
        ecareVo.setSubType(EcareSubType.NREALTIME);

        return ecareVo;
    }

    /**
     * 이케어 서브 타입 목록을 생성한다.
     *
     * @return
     */
    public List<String> getSubTypeList() {
        List<String> subTypeList = new LinkedList<>();
        subTypeList.add(EcareSubType.NREALTIME);
        subTypeList.add(EcareSubType.SCHEDULE_MINUTE);
        subTypeList.add(EcareSubType.SCHEDULE);

        return subTypeList;
    }

    /**
     * 이케어 Email 채널의 메일 타입 목록을 생성한다. 보안메일(SecurityMail)과 PDF 는 옵션 항목이다.
     *
     * @return 이케어 메일 타입 목록
     */
    public List<String> getMailTypeList() {
        List<String> mailTypeList = new LinkedList<>();
        mailTypeList.add(MailType.NONE);

        if (useSecurityMail.equals("on")) {
            mailTypeList.add(MailType.SECUMAIL);
        }

        if (usePdfMail.equals("on")) {
            mailTypeList.add(MailType.HTML2PDF);
        }

        return mailTypeList;
    }

    /**
     * 이케어를 생성한다. 서비스에 필요한 서비스, 스케줄, 대상자, 템플릿, 반응추적 정보 등이 생성된다.
     *
     * @param ecareVo 이케어 정보
     * @param ecareScenarioVo 시나리오 정보
     */
    public void createEcare(EcareVo ecareVo, EcareScenarioVo ecareScenarioVo) throws SQLException {
        EcareScheduleVo scheduleVo = new EcareScheduleVo();
        scheduleVo.setEcmScheduleNo(ecmScheduleDao.selectNextEcmScheduleNo());
        ecmScheduleDao.insertEcmSchedule(scheduleVo);

        ecareVo.setCreateDt(ecareScenarioVo.getCreateDt());
        ecareVo.setCreateTm(ecareScenarioVo.getCreateTm());
        ecareVo.setDepthNo(1);
        ecareVo.setEcareNo(ecareDao.selectNextEcareNo());
        ecareVo.setEcareScheduleVo(scheduleVo);
        ecareVo.setEcmScheduleNo(scheduleVo.getEcmScheduleNo());
        ecareVo.setGrpCd(ecareScenarioVo.getGrpCd());
        ecareVo.setLastUpdateDt(ecareScenarioVo.getLastUpdateDt());
        ecareVo.setLastUpdateTm(ecareScenarioVo.getLastUpdateTm());
        ecareVo.setRetryCnt(serverInfoDao.selectServerInfo().getRetryCnt());
        ecareVo.setScenarioNo(ecareScenarioVo.getScenarioNo());
        ecareVo.setServiceType(ecareScenarioVo.getServiceType());
        ecareVo.setTermType("1");
        ecareVo.setUserId(ecareScenarioVo.getUserId());

        if (isScheduleOrScheduleMin(ecareVo)) {
            ecareVo.setSegmentNo(segmentDao.selectNextSegmentNo());
            createSegment(ecareVo);
        }

        ecareDao.insertEcare1StepInfo(ecareVo);

        if (ecareVo.getSubType().equals(EcareSubType.NREALTIME)) {
            createAddQueryForNearRealtime(ecareVo.getEcareNo());
        }

        if (ecareVo.getChannelType().equals(Channel.MAIL)) {
            ecareTraceInfoDao.insertEcareTraceInfoByEcare(ecareVo);
        }

        createTemplate(ecareVo);
    }

    private boolean isScheduleOrScheduleMin(EcareVo ecareVo) {
        return !ecareVo.getSubType().equals(EcareSubType.NREALTIME);
    }

    /**
     * 준실시간 타입에서 사용하는 SQL 2개 자동 생성
     * <ul>
     *     <li>
     *         {@link #SELECT_NVREALTIMEACCEPT_JONMUN_SQL}
     *     </li>
     *     <li>
     *         {@link #UPDATE_NVREALTIMEACCEPT_SEND_FG_SQL}
     *     </li>
     * </ul>
     *
     * @param ecareNo 이케어 번호
     * @throws SQLException
     */
    public void createAddQueryForNearRealtime(int ecareNo) throws SQLException {
        int seq = dbInfoDao.selectDbinfoSeq();

        addQueryDao.insertAddQuery(new AddQueryVo.Builder(ecareNo)
                .setQueryType("DATA")
                .setExecuteType("BYTARGET")
                .setResultId(Const.RESULT_ID_PREFIX + "1")
                .setQuerySeq(1)
                .setDbInfoSeq(seq)
                .setQuery(SELECT_NVREALTIMEACCEPT_JONMUN_SQL)
                .build());

        addQueryDao.insertAddQuery(new AddQueryVo.Builder(ecareNo)
                .setQueryType("RESULT")
                .setExecuteType("BYTARGET")
                .setResultId("")
                .setQuerySeq(2)
                .setDbInfoSeq(seq)
                .setQuery(UPDATE_NVREALTIMEACCEPT_SEND_FG_SQL)
                .build());
    }

    /**
     * 대상자를 생성한다.
     *
     * @param ecareVo 이케어 정보
     */
    public void createSegment(EcareVo ecareVo) throws SQLException {
        SegmentVo segmentVo = new SegmentVo.Builder(ecareVo.getSegmentNo())
                .setUserId(ecareVo.getUserId())
                .setGrpCd(ecareVo.getGrpCd())
                .setSegmentNm("[" + ecareVo.getEcareNo() + "] " + ecareVo.getEcareNm())
                .setDbinfoSeq(dbInfoDao.selectDbinfoSeq())
                .setActiveYn("Y")
                .setShareYn("Y")
                .setFileToDbYn("N")
                .setSegmentType("N")
                .setLastUpdateDt(DateUtil.dateToString("yyyyMMdd", new Date()))
                .build();

        segmentDao.insertSegment(segmentVo);
    }

    /**
     * 템플릿을 생성한다. Email 메일 타입이 보안메일이나 PDF면 템플릿을 추가로 생성한다. Push 채널은 추가로 POPUP 템플릿을 생성한다.
     *
     * @param ecareVo 이케어 정보
     */
    private void createTemplate(EcareVo ecareVo) {
        List<TemplateVo> templateList = new ArrayList<>();
        templateList.add(getTemplateVo(ecareVo, " "));
        String channelType = ecareVo.getChannelType();

        if (channelType.equals(Channel.MAIL)) {
            String mailType = ecareVo.getMailType();

            if (!mailType.equals(MailType.NONE)) {
                templateList.add(getTemplateVo(ecareVo, mailType));
            }
        } else if (channelType.equals(Channel.PUSH)) {
            templateList.add(getTemplateVo(ecareVo, "POPUP"));
        }

        for (TemplateVo templateVo : templateList) {
            ecareTemplateDao.insertEditorEcareTemplate(templateVo);
        }
    }

    private TemplateVo getTemplateVo(EcareVo ecareVo, String seg) {
        TemplateVo template = new TemplateVo();
        template.setNo(ecareVo.getEcareNo());
        template.setSeg(seg);
        template.setTemplate(" ");

        return template;
    }
    /**
     * 분할발송 정보를 저장한다 
     *
     * 
     */
    
    public int setRegistEcareDivideScheduleInfo(int ecareNo, DivideSchedule[] divideScheduleList, int divideInterval, String startDate, String endDate, String ecareSts, String channelType, String cycleCd) {
        EcareVo ecareVo = ecareDao.selectEcareInfo(ecareNo);
        if("C".equals(ecareVo.getEcareSts())) {
            return 300;
        }
        ecareVo.setEcareNo(ecareNo);
        if(StringUtil.isBlank(ecareSts)) {
            ecareVo.setEcareSts("P");
        } else {
            ecareVo.setEcareSts(ecareSts);
        }
        
        EcareScheduleVo ecareScheduleVo = ecmScheduleDao.selectEcmScheduleByPk(ecareVo.getEcmScheduleNo());
        // 발송 시작일
        ecareScheduleVo.setEcmScheduleNo(ecareVo.getEcmScheduleNo());
        ecareScheduleVo.setSendStartDt((divideScheduleList[0].getStartDt()).substring(0, 8));
        ecareScheduleVo.setInvokeTm((divideScheduleList[0].getStartDt()).substring(8));
        ecareScheduleVo.setCycleCd(cycleCd);
        ecareVo.setEcareScheduleVo(ecareScheduleVo);

        // 분할발송 여부
        ecareVo.setDivideYn("Y");
        // 분할간격
        ecareVo.setDivideInterval(divideInterval);
        // 분할횟수
        ecareVo.setDivideCnt(divideScheduleList.length);

        

        // 등록되어있는 분할예약 스케쥴을 삭제하고 새로 등록함.
        ecareDivideScheduleDao.deleteEcareDivideSchedule(ecareNo);

        for(int i = 0; i < divideScheduleList.length; i++) {
            divideScheduleList[i].setClient("EC");
            ecareDivideScheduleDao.insertDivideSchedule(divideScheduleList[i]);
        }
        
        ecareScheduleVo.setSendEndDt(ecareScheduleVo.getSendStartDt());
        ecmScheduleDao.updateEcareScheduleInfo(ecareScheduleVo);
        
        // CYCLEITEM
        CycleItemVo cycleItem = new CycleItemVo();
        cycleItem.setEcmScheduleNo(ecareScheduleVo.getEcmScheduleNo());
        cycleItem.setCycleItem("DIVIDE");
        cycleItem.setCheckYn("Y");
        
        cycleItemDao.deleteEcareCycleItemInfo(cycleItem.getEcmScheduleNo());
        cycleItemDao.insertCycleItem(cycleItem);
        
        return ecareDao.updateEcareScheduleInfo(ecareVo);
    }
}
