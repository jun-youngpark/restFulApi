package com.mnwise.wiseu.web.rest.service.ecare;

import static com.mnwise.wiseu.web.base.util.CodeUtil.getSuccessCodeByChannel;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.dao.common.DbInfoDao;
import com.mnwise.wiseu.web.rest.dao.ecare.AddQueryDao;
import com.mnwise.wiseu.web.rest.dao.ecare.EcareDao;
import com.mnwise.wiseu.web.rest.dao.ecare.EcareScenarioDao;
import com.mnwise.wiseu.web.rest.dao.ecare.EcareTraceInfoDao;
import com.mnwise.wiseu.web.rest.dao.ecare.EcmScheduleDao;
import com.mnwise.wiseu.web.rest.dao.handler.HandlerDao;
import com.mnwise.wiseu.web.rest.dao.segement.SegmentDao;
import com.mnwise.wiseu.web.rest.dao.segement.SemanticDao;
import com.mnwise.wiseu.web.rest.dao.template.TemplateDao;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.dto.ReturnDto.EcareDto;
import com.mnwise.wiseu.web.rest.model.ecare.AddQuery;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.model.ecare.EcmSchedule;
import com.mnwise.wiseu.web.rest.model.segment.Segment;
import com.mnwise.wiseu.web.rest.parent.BaseService;

@Service
public class EcareService extends BaseService {
	@Autowired private EcareDao ecareDao;
	@Autowired private EcareScenarioDao ecareScenarioDao;
	@Autowired private EcmScheduleDao ecmScheduleDao;
	@Autowired private TemplateDao templateDao;
	@Autowired private HandlerDao handlerDao;
	@Autowired private EcareTraceInfoDao ecareTraceInfoDao;
	@Autowired private AddQueryDao addQueryDao;
	@Autowired private DbInfoDao dbInfoDao;
	@Autowired private SegmentDao segmentDao;
	@Autowired private SemanticDao semanticDao;

	private static final String SELECT_NVREALTIMEACCEPT_JONMUN_SQL ="SELECT JONMUN FROM NVREALTIMEACCEPT WHERE SEQ = ${seq}";
    private static final String UPDATE_NVREALTIMEACCEPT_SEND_FG_SQL = "UPDATE NVREALTIMEACCEPT SET SEND_FG = #{_SEND_FG}, LIST_SEQ = #{_LIST_SEQ}, ERROR_MSG = #{_ERROR_MESSAGE} WHERE SEQ = ${seq}";

    @Value("${omni.timeout.hour:3}")
    private String timeout;
    @Value("${omni.send.interval:10}")
    private String interval;
    @Value("${db.server}")
    private String dbServer;

	@Transactional(value = "txManager" , rollbackFor = Exception.class)
	public JSONObject create(Ecare ecare) throws Exception{
		if(ecareScenarioDao.insertEcareScenarioForFirst(ecare) > 0) {
			ecmScheduleDao.insertScheduleForFirst(ecare);	//스케줄
			ecareDao.insertEcareForFirst(ecare); 	//이케어
			if(isScheduleOrScheduleMin(ecare)) {
				ecare.setSegmentNo(segmentDao.selectNextSegmentNo());
	            createSegment(ecare);
        	}
		    if(isNrealTime(ecare)) {	//준실시간 addQuery 추가
	            createAddQueryForNearRealtime(ecare.getEcareNo());
	        }
	        if(ecare.getChannelType().equals(Const.Channel.EMAIL)) {//TRACE
	        	ecareTraceInfoDao.insertTraceInfo(makeTraceInfo(ecare.getEcareNo()));
	        }
	        templateDao.insertEcTemplate(ecare);			//캠페인
	        handlerDao.insertEcHandler(ecare);			//핸들러
		}else{
			throw new Exception("EcareScenario Create Error");
		};

		// Response
		return new DataMap<>()
				.put("ecareNo",ecare.getEcareNo())
				.build();
	}

	public JSONObject update(Ecare ecare) throws Exception{
		if(ecareDao.updateEcare(ecare) > 0) {
			if(isNotBlank(ecare.getTemplate())) {
				templateDao.updateEcTemplate(ecare);
			}
		}else {
			throw new Exception("Ecare number ("+ecare.getEcareNo()+") does not exist.");
		}
		// Response
		return new DataMap<>()
				.put("ecareNo",ecare.getEcareNo())
				.build();
	}

	public JSONObject delete(Ecare ecare) throws Exception{
		Ecare getEcare = Optional
				.ofNullable(ecareDao.selectEcare(ecare))
				.orElseThrow(() -> new Exception("Ecare number ("+ecare.getEcareNo()+") does not exist."));

		if(StringUtils.equalsIgnoreCase("N",getEcare.getUseYn())
			&& StringUtils.equalsIgnoreCase("N",ecare.getUseYn())){
			throw new Exception("The Ecare has been deleted.("+ecare.getEcareNo()+")");
		}

		int result = ecareDao.deleteEcare(ecare);
		// Response
		return new DataMap<>()
				.put("ecareNo",ecare.getEcareNo())
				.put("useYn",ecare.getUseYn())
				.build();
	}

	public JSONObject updateState(Ecare ecare) throws Exception{
		Ecare getEcare = Optional
				.ofNullable(ecareDao.selectEcare(ecare))
				.orElseThrow(() -> new Exception("Ecare number ("+ecare.getEcareNo()+") does not exist."));
		if(getEcare.getEcareSts().equals(ecare.getEcareSts())) {
			throw new Exception("Ecare status is already ("+ecare.getEcareSts()+")");
		}
		ecareDao.updateState(ecare);
		// Response
		return new DataMap<>()
					.put("EcareNo",ecare.getEcareNo())
					.build();
	}

	public JSONObject createOmni(Ecare ecare) throws Exception{
		Ecare superEcare = Optional
				.ofNullable(ecareDao.selectEcare(ecare))
				.orElseThrow(() -> new Exception("Ecare number ("+ecare.getEcareNo()+") does not exist."));

		if(superEcare.getSegmentNo() == 0) {
			superEcare.setSegmentNo(Integer.parseInt("-1"));
		}

		int nextEcmScheduleNo = ecmScheduleDao.selectNextEcmScheduleNo(); //새 스케줄 번호 생성
		ecare.setNewEcareNo(ecareDao.selectNextEcareNo());	//새 이케어 번호 생성
		  // 상위 캠페인의 relation_tree에 새로 생성하는 캠페인의 relation_type을 붙여 캠페인간 관계를 나타낸다.
		ecare.setRelationTree(superEcare.getRelationTree() + ecare.getRelationType());
		ecare.setScenarioNo(superEcare.getScenarioNo());
		final Segment segmentVo = makeSegmentVo(ecare, superEcare);
		ecare.setTargetCnt(segmentVo.getSegmentSize());
		ecare.setSegmentNo(segmentVo.getSegmentNo());

		ecare.setEcmScheduleNo(nextEcmScheduleNo);
        ecmScheduleDao.insertEcmSchedule(EcmSchedule.builder()
        		.ecmScheduleNo(nextEcmScheduleNo)
        		.ecmScheduleNm(" ")
        		.build()
        );
        segmentDao.copySegmentForResend(segmentVo);
        semanticDao.copySemantic(segmentVo.getPsegmentNo(), segmentVo.getSegmentNo());
        ecareDao.copyEcareForOmni(ecare);
        templateDao.copyEcTemplate(ecare);			//템플릿
        handlerDao.insertOmniEcHandler(ecare);			//핸들러


      // Response
   		return new DataMap<>().put("ecareNo",ecare.getNewEcareNo())
   					.build();
	}


	public String updateAndCheckSchudle(Ecare ecare, EcmSchedule ecmSchedule) throws Exception{
        if(isScheduleOrScheduleMin(ecare)) {
        	EcmSchedule getEcmSchedule = Optional
    				.ofNullable(ecmScheduleDao.selectEcmScheduleByEcareNo(ecare.getEcareNo()))
    				.orElseThrow(() -> new Exception("스케줄이 저장되지 않았습니다. ("+ecare.getEcareNo()+") does not exist."));
        }

        if("1".equals(ecmSchedule.getCycleCd())
                && StringUtil.isNotEmpty(ecmSchedule.getStartTm())) {
        	ecmSchedule.setInvokeEveryMin("Y");
        }else if("3".equals(ecmSchedule.getCycleCd())) {
             if(ecmSchedule.getMonthOpt().equals("STATIC")) {
            	 ecmSchedule.setScheWeeknumber(0);
            	 ecmSchedule.setWeekday("");
             } else if(ecmSchedule.getMonthOpt().equals("CONTEXT")) {
            	 ecmSchedule.setDay(0);
             }
         }
        ecmScheduleDao.updateEcareScheduleInfo(ecmSchedule);

        return "OK";
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

        addQueryDao.insertAddQuery(AddQuery.builder()
        		.ecareNo(ecareNo)
                .queryType("DATA")
                .executeType("BYTARGET")
                .resultId(Const.RESULT_ID_PREFIX + "1")
                .querySeq(1)
                .dbInfoSeq(seq)
                .query(SELECT_NVREALTIMEACCEPT_JONMUN_SQL)
                .build());

        addQueryDao.insertAddQuery(AddQuery.builder()
        		.ecareNo(ecareNo)
                .queryType("RESULT")
                .executeType("BYTARGET")
                .resultId("")
                .querySeq(2)
                .dbInfoSeq(seq)
                .query(UPDATE_NVREALTIMEACCEPT_SEND_FG_SQL)
                .build());
    }


    /**
     * 대상자를 생성한다.
     *
     * @param ecareVo 이케어 정보
     */
    public void createSegment(Ecare ecare) throws SQLException {
    	segmentDao.insertSegment(Segment.builder()
	        		.segmentNo(ecare.getSegmentNo())
	                .userId(ecare.getUserId())
	                .grpCd(ecare.getGrpCd())
	                .segmentNm("[" + ecare.getEcareNo() + "] " + ecare.getEcareNm())
	                .dbinfoSeq(dbInfoDao.selectDbinfoSeq())
	                .activeYn("Y")
	                .shareYn("Y")
	                .fileToDbYn("N")
	                .segmentType("N")
	                .lastUpdateDt(DateUtil.dateToString("yyyyMMdd", new Date()))
	                .build()
                );
    }
	public int selectEcareNoBySvcId(String svcId) throws Exception{
		Object result = ecareDao.selectEcareNoBySvcId(svcId);
		if(result == null){
			throw new Exception("svcId에 해당 되는 서비스가 없습니다. ["+svcId+"]");
		}else{
			return (int) result;
		}
	}



	private String makeHeadSQL(String relationType) {
        // 옴니채널 타겟 쿼리생성
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT TARGET_KEY, TARGET_NM, TARGET_CONTACT, TARGET_LST1, TARGET_LST2 FROM ");
        sb.append((Const.RELATION_OPEN.equals(relationType) ? "NVECARERECEIPT" : "NVECARESENDLOG"));
        sb.append(" A, NVRESENDTARGET B ");
        return sb.toString();
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
        if(Const.RELATION_FAIL.equals(resendMode) || Const.RELATION_SUCCESS.equals(resendMode)) {
            String inCondition;
            if(Const.RELATION_FAIL.equals(resendMode)) {
                inCondition = " NOT IN (\'";
            } else {
                inCondition = " IN (\'";
            }

            final StringBuilder sb = new StringBuilder();
            sb.append(" WHERE A.SUB_RESULT_SEQ = \'$RSEQ$\'");
            sb.append(" AND A.SUB_ECARE_NO = ").append(newEcareNo);
            sb.append(" AND A.ECARE_NO = ").append(ecareNo);
            sb.append(" AND A.ERROR_CD ").append(inCondition);
            sb.append(getSuccessCodeByChannel(channel)).append("\')");
            sb.append(" AND B.CLIENT = 'EC'");
            sb.append(" AND A.ECARE_NO = B.SERVICE_NO AND A.RESULT_SEQ = B.RESULT_SEQ AND A.LIST_SEQ = B.LIST_SEQ");

            return sb.toString();
        } else if(Const.RELATION_OPEN.equals(resendMode)) {
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
	private Segment makeSegmentVo(Ecare ecare, Ecare superEcare) {
			final String updateQuery = makeTargetUpdateSQL(ecare.getRelationType(), ecare.getEcareNo(), ecare.getNewEcareNo(), ecare.getChannelType());
			final String headSql = makeHeadSQL(ecare.getRelationType());
			final String bodySql = makeTargetSQL(ecare.getRelationType(), ecare.getEcareNo(), ecare.getNewEcareNo(), ecare.getChannelType());
			// 옴니발송시에는 새로운 대상자 쿼리가 해당 기능을 대체함
	        Segment segment = segmentDao.selectSegmentByPk(superEcare.getSegmentNo());
	        segment.setSegmentNo(segmentDao.selectNextSegmentNo());
	        segment.setSegmentNm("[Omni]" + ecare.getNewEcareNo() + "_" + ecare.getChannelType() + "_" + ecare.getRelationTree());
	        segment.setSqlHead(headSql);
	        segment.setSqlBody(bodySql);
	        segment.setLastUpdateDt(new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
	        segment.setPsegmentNo(superEcare.getSegmentNo());
	        segment.setSegType("M");
	        segment.setUpdateQuery(updateQuery);
	        return segment;
    }

	private String makeTargetUpdateSQL(String resendMode, int ecareNo, int newEcareNo, String channel) {

        if(Const.RELATION_FAIL.equals(resendMode) || Const.RELATION_SUCCESS.equals(resendMode)) {
            String inCondition;

            if(Const.RELATION_FAIL.equals(resendMode)) {
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
            sb.append(getSuccessCodeByChannel(channel)).append("\')");

            sb.append(" AND A.SUB_RESULT_SEQ = 0");
            sb.append(" AND A.SUB_ECARE_NO = 0");

            return sb.toString();
        } else if(Const.RELATION_OPEN.equals(resendMode)) {
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

	public JSONObject list(Ecare ecare) throws Exception{
		int size = ecareDao.selectEcareListCount(ecare);
		List<Ecare> list = ecareDao.selectEcareList(ecare);
		// Response
		return new DataMap<>()
				.put("size",size)
				.put("list",list)
				.build();
	}

	public JSONObject get(Ecare ecare) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(ecareDao.selectEcareOne(ecare), JSONObject.class);
	}

	public List<EcareDto> getOmniList(Ecare ecare) {
		// Response
		return ecareDao.getOmniList(ecare);
	}

	public void vaildUpdateSts(Ecare ecare) throws Exception{
		if(isSchedule(ecare)) {
			isNullException(ecare.getScheduleSubType(), "스케줄/스케줄분 타입은 scheduleType 값이 필수 입니다.");
		}
		if(isScheduleOrScheduleMin(ecare)) {
			isNullException(ecare.getScheduleSubType(), "스케줄/스케줄분 타입은 scheduleSubType 값이 필수 입니다.");
		}

	}

}