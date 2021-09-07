package com.mnwise.wiseu.web.segment.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mnwise.wiseu.web.account.model.UserVo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.model.TargetQueryInfo;
import com.mnwise.wiseu.web.common.util.DBConnectionUtil;
import com.mnwise.wiseu.web.common.util.QueryExecutor;
import com.mnwise.wiseu.web.common.util.SecurityUtil;
import com.mnwise.wiseu.web.ecare.model.EcareTargetDto;
import com.mnwise.wiseu.web.segment.dao.FileUploadDao;
import com.mnwise.wiseu.web.segment.dao.SegGenealogyDao;
import com.mnwise.wiseu.web.segment.dao.SegmentCheckDao;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.wiseu.web.segment.model.TargetVo;
import com.mnwise.wiseu.web.segment.model.UsingSegmentVo;
import com.mnwise.xsqlparser.SqlAnalyzer;

/**
 * 세그먼트 서비스
 */
@Service
public class SegmentService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(SegmentService.class);

    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private FileUploadDao fileUploadDao;
    @Autowired private SegGenealogyDao segGenealogyDao;
    @Autowired private SegmentCheckDao segmentCheckDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;

    /**
     * 전체 세그먼트 건수를 가져온다.
     *
     * @param segmentVo
     * @return
     */
    public int getSegmentListTotalCount(SegmentVo segmentVo) {
        return segmentDao.selectSegmentTotalCount(segmentVo);
    }

    /**
     * 세그먼트 리스트를 가져온다.
     *
     * @param segmentVo
     * @return
     */
    public List<SegmentVo> selectSegmentList(SegmentVo segmentVo) {
        return segmentDao.selectSegmentList(segmentVo);
    }

    /**
     * - [이케어>이케어 리스트]대상자 갱신<br/>
     * - DWR : SegmentService.renewalSegment <br/>
     * 대상자를 갱신하고 대상자 수를 가져온다.
     *
     * - 20100712 : 대상자 갱신시에 에러날경우 에러메시지를 리턴하도록 한다(errorMsg추가) segment_list.jsp,ecare_list.jsp,SegmentVo.java 같이 수정됨
     * @param segmentNo
     * @return
     */
    public SegmentVo renewalSegment(int segmentNo) {
        SegmentVo segmentVo = new SegmentVo();
        segmentVo.setSegmentNo(segmentNo);
        try {
            // 대상자 쿼리 작성
            String query = makeSegmentTargetQuery(segmentVo);
            String countQuery = "";

            DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());
            DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo);
            JdbcTemplate jdbcTemplate = dbConnectionUtil.getJdbcTemplate();

            // 특수문자 처리
            query = query.replaceAll("&gt;", ">");
            query = query.replaceAll("&lt;", "<");

            // 고객 DB가 INFORMIX이고 버젼이 10.x 미만인 경우 인라인뷰 쿼리가 동작하지 않으므로 DB 종류가 INFORMIX일 경우 일반 쿼리로 분기하여 처리
            if(dbInfoVo.getDbKind().toUpperCase().startsWith("INFORMIX")) {
                countQuery = " SELECT COUNT(*) " + query.substring(query.toUpperCase().indexOf("FROM"));
            } else {
                countQuery = " SELECT COUNT(*) FROM ( " + query + " ) a";
            }
            if(StringUtil.isBlank(query)) {
                System.out.println("segmentVo.setSegmentSize(0);");
                segmentVo.setSegmentSize(0);
            }else {
                int returnInteger = jdbcTemplate.queryForObject(countQuery, Integer.class);
                log.info("countQuery2 =================> " + countQuery);
                log.info("returnInteger =========== > " + returnInteger);
                segmentVo.setSegmentSize(returnInteger);
                segmentDao.updateSegmentSize(segmentVo);
            }
            return segmentVo;
        } catch(Exception e) {
            segmentVo.setErrorMsg(e.getCause().getMessage());
            log.error(null, e);
            return segmentVo;
        }
    }

    /**
     * 대상자 쿼리를 생성한다.
     *
     * @param paramsegmentVo
     * @return
     */
    private String makeSegmentTargetQuery(SegmentVo paramsegmentVo) {
        //SECUDB-DONE
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(paramsegmentVo.getSegmentNo());
        String rtnQuery = null;

        // SegmentVo 를 리턴하기 위해 추가
        paramsegmentVo.set(segmentVo);

        // 대상자 쿼리를 구성
        StringBuffer sb = new StringBuffer();
        sb.append(segmentVo.getSqlHead() != null ? segmentVo.getSqlHead() : ""); // NVSEGMENT.SQLHEAD
        sb.append(" ");
        // sb.append(sFromSql); // NVSCHEDULE.FROMSQL
        sb.append(" ");
        sb.append(segmentVo.getSqlBody() != null ? segmentVo.getSqlBody() : ""); // NVSEGMENT.SQLBODY
        sb.append(" ");
        sb.append(segmentVo.getSqlTail() == null ? "" : segmentVo.getSqlTail()); // NVSEGMENT.SQLTAIL

        // 재발송 로직 체크
        if(segmentVo.getSqlfilter() != null) {
            List<SemanticVo> semanticList = semanticDao.selectSementicInfoForTarget(segmentVo);
            String keyColumNm = null;
            for(SemanticVo semanticVo : semanticList) {
                if(semanticVo.getFieldKey().trim().equals("K")) {
                    keyColumNm = semanticVo.getFieldNm();
                    break;
                }
            }
            sb.insert(0, "SELECT * FROM (");
            sb.append(") a  WHERE ").append(keyColumNm).append(" IN (").append(segmentVo.getSqlfilter()).append(") ");
        }

        rtnQuery = sb.toString();

        // 행 분리자가 포함되어 있으면 공백으로 치환
        rtnQuery = rtnQuery.replace('\r', ' ').replace('\n', ' ').trim();

        // 재발송인데 wiseU DB가 아닌 다른 DB일경우 SQLFILTER에 해당하는 쿼리로 건수를 가져오게한다
        if(paramsegmentVo.getSqlfilter() != null && paramsegmentVo.getDbinfoSeq() != 1) {
            rtnQuery = paramsegmentVo.getSqlfilter();
            // 재발송이기때문에 로컬 DB의 건수로 가져와야한다.
            paramsegmentVo.setDbinfoSeq(1);
        }
        // 미래크레디트 대부 김수미 추가
        rtnQuery = rtnQuery.replace("AND RESULT_SEQ = \'$RSEQ$\'", "").replace("SEND_FG = \'H\'", "SEND_FG = \'R\'");

        return rtnQuery;
    }

    /**
     * 세그먼트 수행 이력을 확인한다.
     *
     * @param segmentVo
     * @return
     */
    public List<UsingSegmentVo> selectUsingList(SegmentVo segmentVo, UserVo userVo) {
        Map<String, Object> param = new HashMap<>();
        param.put("segmentVo", segmentVo);
        param.put("userVo", userVo);

        return segmentDao.selectUsingList(param);
    }

    /**
     * 대상자 보기 팝업에서 대상자 리스트를 가져온다.
     *
     * @param segmentVo
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectTargetList(SegmentVo segmentVo) throws Exception {
        String oriQuery = makeSegmentTargetQuery(segmentVo);

        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());

        String query = null;

        // 원래 대상자에서 SQL필터에 등록된 고객 데이터만 뽑아오는 쿼리 생성.
        StringBuffer sbQuery = new StringBuffer();
        // 고객 DB가 INFORMIX이고 버젼이 10.x 미만인 경우 인라인뷰 쿼리가 동작하지 않으므로 DB 종류가 INFORMIX일 경우 일반 쿼리로 분기하여 처리
        if(dbInfoVo.getDbKind().toUpperCase().startsWith("INFORMIX")) {
            sbQuery.append(oriQuery);
            if(oriQuery.toString().toUpperCase().indexOf("WHERE") == -1) {
                sbQuery.append(" WHERE 1 = 1 ");
            }
        } else {
            sbQuery.append("SELECT * FROM (");
            sbQuery.append(oriQuery);
            sbQuery.append(" ) A");
            sbQuery.append(" WHERE 1 = 1 ");
        }

        // 검색조건
        if(segmentVo.getSearchWord() != null && segmentVo.getSearchWord().length() > 0) {
            if("M".equals(segmentVo.getSegType())) {
                String keyword = SecurityUtil.ariaEncrypt(segmentVo.getSearchWord().trim()).trim();

                sbQuery.append(" AND ")
                .append(" ( ")
                .append("   TARGET_LST1 like '%")
                .append(  keyword )
                .append("%'")
                .append(" OR ")
                .append("   TARGET_LST2 like '%")
                .append(  keyword )
                .append("%'")
                .append(" ) ");

            } else {
                sbQuery.append(" AND ");
                sbQuery.append(segmentVo.getSearchColumn());
                sbQuery.append(" like '%");
                // 검색 시 검색어 암호화
                sbQuery.append(security.securityWithColumn(segmentVo.getSearchWord().trim(), segmentVo.getSearchColumn(), "ENCRYPT"));
                sbQuery.append("%'");
            }

        }

        query = sbQuery.toString();
        query = query.replaceAll("&gt;", ">").replaceAll("&lt;", "<");

        int pageSize = segmentVo.getCountPerPage();
        int startRow = (segmentVo.getNowPage() - 1) * pageSize;

        Map<String, Object> returnMap = new HashMap<>();
        // 대상자 DB 별 코덱정보를 타게끔 수정
        QueryExecutor qe = new QueryExecutor(dbInfoVo.getEncoding(), dbInfoVo.getDecoding());
        qe.executeQuery(dbInfoVo, query, pageSize, startRow);
        String[] header = qe.getColumnHeader();

        returnMap.put("header", header);

        List<String[]> queryData = qe.getQueryData();

        // 멀티채널 캠페인의 대상자는 리스트 파일 내용이 저장되어 있음
        // 리스트 파일 내용 중에 고객 데이터만 추줄하여 본발송 대상자와 같은 형태로 전송한다.
        if("M".equals(segmentVo.getSegType())) {
            List<Object[]> omniChannelList = new ArrayList<>();

            for(String[] data : queryData) {
                // 대상자 목록을 TARGET_LST1 에서 가져옴으로 복호화 처리 필요 함.
                String[] strings = SecurityUtil.ariaDecrypt((data[3].replaceAll("^\\d+\t\\d{3}\t", ""))).split("\t");
                omniChannelList.add(strings);
            }
            returnMap.put("queryDataList", omniChannelList);
        } else {
            returnMap.put("queryDataList", queryData);
        }

        return returnMap;
    }

    /**
     * 대상자 보기 팝업에서 대상자 전체 건수를 가져온다.
     *
     * @param segmentVo
     * @return
     * @throws Exception
     */
    public int selectTargetListTotalCount(SegmentVo paramsegmentVo) throws Exception {
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(paramsegmentVo.getSegmentNo());

        if(segmentVo.getSqlfilter() != null) {
            int cnt = 0;
            PreparedStatement pstmt = getConnection().prepareStatement(" SELECT COUNT(*) FROM (" + segmentVo.getSqlfilter() + ") A");
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                cnt = rs.getInt(1);

            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);

            return cnt;
        }

        StringBuilder countQuery = new StringBuilder();
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());

        // 검색어를 이용하여 검색 할 경우
        StringBuilder SearchQuery = new StringBuilder();
        if("M".equals(segmentVo.getSegType()) && paramsegmentVo.getSearchWord() != null && paramsegmentVo.getSearchWord().length() > 0) {
            String keyword = SecurityUtil.ariaEncrypt(paramsegmentVo.getSearchWord().trim()).trim();

            SearchQuery.append(" AND ")
            .append(" ( ")
            .append("   TARGET_LST1 like '%")
            .append(  keyword )
            .append("%'")
            .append(" OR ")
            .append("   TARGET_LST2 like '%")
            .append(  keyword )
            .append("%'")
            .append(" ) ");
        }


        /* 고객 DB가 INFORMIX이고 버젼이 10.x 미만인 경우 인라인뷰 쿼리가 동작하지 않으므로 DB 종류가 INFORMIX일 경우 일반 쿼리로 분기하여 처리함 */
        if(dbInfoVo.getDbKind().toUpperCase().startsWith("INFORMIX")) {
            countQuery.append(" SELECT COUNT(*) FROM ( ")
            .append(segmentVo.getSqlHead().substring(segmentVo.getSqlHead().toUpperCase().indexOf("FROM")))
            .append(" ")
            .append(segmentVo.getSqlBody() != null ? segmentVo.getSqlBody() : "")
            .append(" ")
            .append(segmentVo.getSqlTail() == null ? "" : segmentVo.getSqlTail())
            .append(" ");

            countQuery.append(" ) A WHERE 1=1 ");

            // 검색 시 검색어 암호화
            if(paramsegmentVo.getSearchWord() != null && paramsegmentVo.getSearchWord().length() > 0) {
                if("M".equals(segmentVo.getSegType())) {
                    countQuery.append(SearchQuery.toString());
                } else {
                    countQuery.append("AND " + paramsegmentVo.getSearchColumn() + " like '%" + security.securityWithColumn(paramsegmentVo.getSearchWord().trim(), paramsegmentVo.getSearchColumn(), "ENCRYPT"));
                    countQuery.append("%'");
                }
            }
        } else {
            // 대상자 쿼리를 만들자.
            countQuery.append(" SELECT COUNT(*) FROM ( ")
            .append(segmentVo.getSqlHead() != null ? segmentVo.getSqlHead() : "")
            .append(" ")
            .append(segmentVo.getSqlBody() != null ? segmentVo.getSqlBody() : "")
            .append(" ")
            .append(segmentVo.getSqlTail() == null ? "" : segmentVo.getSqlTail())
            .append(" ");

            countQuery.append(" ) A WHERE 1=1 ");

            // 서브쿼리 없는 INFOMIX를 적용하면서 기존 쿼리에 인라인뷰에 있는 WHERE를 WHERE 로 인식해서 AND
            // 조건을 붙이는 문제가 있었음. SELECT COUNT(*) FROM (SELECT ID FROM TBL ) A AND
            // .... 이런식임.
            // 검색 시 검색어 암호화
            if(paramsegmentVo.getSearchWord() != null && paramsegmentVo.getSearchWord().length() > 0) {

                if("M".equals(segmentVo.getSegType())) {
                    countQuery.append(SearchQuery.toString());

                } else {
                    countQuery.append(" AND " + paramsegmentVo.getSearchColumn() + " like '%" + security.securityWithColumn(paramsegmentVo.getSearchWord().trim(), paramsegmentVo.getSearchColumn(), "ENCRYPT")
                    + "%'");
                }
            }

        }

        if(log.isDebugEnabled()) {
            log.debug("JDBC Connection");
            log.debug(String.format("Driver : %s", dbInfoVo.getDriverNm()));
            log.debug(String.format("DSN    : %s", dbInfoVo.getDriverDsn()));
            log.debug(String.format("UserID : %s", dbInfoVo.getDbUserId()));
            // log.debug(String.format("Pass : %s", dbInfoVo.getDbPassword()));
        }

        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());
        JdbcTemplate jdbcTemplate = dbConnectionUtil.getJdbcTemplate();

        //특수문자
        String strCountQuery = countQuery.toString().replaceAll("&gt;" , ">").replaceAll("&lt;", "<");

        return jdbcTemplate.queryForObject(strCountQuery, Integer.class);
    }

    /**
     * 기본 세그먼트 정보를 삭제한다.
     *
     * @param segmentVo
     */
    public void deleteSegmentCheckBySegmentNo(int segmentNo) {
        segmentCheckDao.deleteSegmentCheckBySegmentNo(segmentNo);
    }

    /**
     * 세그먼트를 삭제한다.
     *
     * @param segmentVo
     */
    public void deleteSegment(int segmentNo) {
        semanticDao.deleteSemanticBySegmentNo(segmentNo);
        segmentDao.deleteSegmentByPk(segmentNo);
    }

    /**
     * 선택한 부서에 해당 세그먼트의 사용 권한을 할당한다.
     *
     * @param segmentVo
     */
    public void permissionSegment(SegmentVo segmentVo) {
        segmentVo.setLastUpdateDt(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        String[] grpCd = segmentVo.getGrpCdArr();
        int segmentNo = segmentVo.getSegmentNo();
        SegmentVo subChkVo = segmentDao.selectSegmentByPk(segmentNo);
        for(int i = 0; i < grpCd.length; i++) {
            segmentVo.setGrpCd(grpCd[i]);
            segmentVo.setPsegmentNo(segmentDao.selectNextSegmentNo());
            segmentDao.insertPermission(segmentVo);
            semanticDao.copySemantic(segmentVo.getSegmentNo(), segmentVo.getPsegmentNo());
            segGenealogyDao.insertSegGenealogy(segmentVo.getPsegmentNo(), 1, segmentVo.getSegmentNo());
            if(StringUtil.isNotEmpty(subChkVo.getSegmentType()) && "S".equals(subChkVo.getSegType())) {
                segGenealogyDao.copySegGenealogy(segmentVo.getSegmentNo(), segmentVo.getPsegmentNo());
            }
        }
    }

    /**
     * 해당 세그먼트에 권한이 할당된 정보를 가져온다.
     *
     * @param segmentVo
     * @return
     */
    public List<SegmentVo> assignPermission(int segmentNo) {
        return segmentDao.assignPermission(segmentNo);
    }

    /**
     * 대상자를 복사한다.
     *
     * @param segmentVo
     */
    public void setRegistSegmentCopy(SegmentVo segmentVo) {
        String nowDt = DateFormatUtils.format(new Date(), "yyyyMMdd");
        segmentVo.setLastUpdateDt(nowDt);
        int newsegmentNo = segmentDao.selectNextSegmentNo();
        segmentVo.setNewSegmentNo(newsegmentNo);
        segmentDao.copySegment(segmentVo);
        semanticDao.copySemantic(segmentVo.getSegmentNo(), segmentVo.getNewSegmentNo());
        segmentCheckDao.copySegmentCheck(segmentVo);
    }

    /**
     * 세그먼트 테스트 쿼리를 수정한다.
     *
     * @param segmentVo
     */
    public void updateTestQuery(SegmentVo segmentVo) {
        segmentDao.updateTestQuery(segmentVo);
    }

    /**
     * 세그먼트 업데이트 쿼리를 수정한다.
     *
     * @param segmentVo
     */
    public void updateUpdateQuery(SegmentVo segmentVo) {
        segmentDao.updateUpdateQuery(segmentVo);
    }

    /**
     * 세그먼트 리스트에서 발송내역이 있을경우 DB에서 삭제가 불가능한데 ACTIVE_YN 의 FLAG 처리를 통하여 리스트에서 없앨수 있다.
     *
     * @param segmentNo
     */
    public void updateListDelete(int segmentNo) {
        segmentDao.updateActiveYnToN(segmentNo);
    }

    /**
     * 디비 정보를 가져온다.
     *
     * @param dbinfoSeq
     * @return
     */
    public DbInfoVo getDbInfo(int dbinfoSeq) {
        return dbInfoDao.selectDbInfoByPk(dbinfoSeq);
    }

    /**
     * Semantic 정보를 가져온다.
     *
     * @param segmentVo
     * @return
     */
    public List<SemanticVo> getSementicInfoForTarget(SegmentVo segmentVo) {
        return semanticDao.selectSementicInfoForTarget(segmentVo);
    }

    /**
     * 대상자 수정
     *
     * @param targetVo
     * @return
     */
    public int updateSegmentTarget(TargetVo targetVo) {
        return fileUploadDao.updateSegmentTarget(targetVo);
    }

    /**
     * 대상자 삭제
     *
     * @param targetVo
     * @return
     */
    public int deleteSegmentTarget(int targetNo, String customerId) {
        return fileUploadDao.deleteFileUploadByPk(targetNo, customerId);
    }

    /**
     * 대상자 삭제(TARGET_NO에 해당하는 데이터)
     *
     * @param targetNo
     * @return
     */
    public int deleteTargetData(int targetNo) {
        return fileUploadDao.deleteFileUploadByTargetNo(targetNo);
    }

    /**
     * 대상자 입력
     *
     * @param targetVo
     * @return
     */
    public int insertSegmentTarget(TargetVo targetVo) {
        return fileUploadDao.insertFileUpload(targetVo);
    }

    /**
     * 대상자 추가시 중복된 CUSTOMER_ID 값이 있는지 체크한다.
     *
     * @param targetNo 타겟 NO
     * @param customerId 대상자 ID
     * @return
     */
    public int getCustomerIdCount(int targetNo, String customerId) {
        return fileUploadDao.getCustomerIdCount(targetNo, customerId);
    }

    /**
     * 비동기로 업로드를 수행할 세그먼트 목록을 조회한다.
     *
     * @return
     */
    public List<SegmentVo> getSegmentListForAsync() {
        return segmentDao.getSegmentListForAsync();
    }

    /**
     * 비동기로 업로드중 WAS 다운이 발생할 경우 최초 기동시에 기존 업로드 중인 데이터가 있는지 확인.
     *
     * @return
     */
    public List<SegmentVo> getUploadRunListForAsync() {
        return segmentDao.getUploadRunListForAsync();
    }

    /**
     * 세그먼트의 수행상태를 업데이트 한다.
     *
     * @param segmentVo 세그먼트 번호와 상태값
     * @return
     */
    public int updateSegmentForSegmentSts(SegmentVo segmentVo) {
        return segmentDao.updateSegmentSts(segmentVo);
    }

    /**
     * segmentNo에 해당하는 시맨틱 필드명을 조회한다.
     *
     * @param segmentNo
     * @return
     * @throws Exception
     */
    public String[] selectSegmentField(int segmentNo) throws Exception {
        List<String> list = semanticDao.selectSegmentField(segmentNo);
        String[] saReturn = new String[list.size()];
        for(int i = 0, n = list.size(); i < n; i++) {
            saReturn[i] = list.get(i);
        }
        return saReturn;
    }

    public void mergeTargetKey(EcareTargetDto target, SemanticVo[] semanticVo) throws Exception{
        int segmentNo = target.getSegmentNo();
        try {
            semanticDao.deleteSemanticBySegmentNo(segmentNo);
            for(SemanticVo param :semanticVo) {
                //필수키 (NVSEMANTIC) 정보를 입력한다.
                semanticDao.insertSemantic(
                    new SemanticVo.Builder(segmentNo).setFieldNm(param.getFieldNm())
                    .setFieldSeq(param.getFieldSeq())
                    .setFieldKey(param.getFieldKey())
                    .setFieldDesc(param.getFieldDesc())
                    .build());
            }
        }catch(Exception e) {
            log.error("{}" , e);
            throw new Exception("target Key data Update or Insert Error");
        }
    }


    public void mergeTarget(EcareTargetDto target) throws Exception{
        int segmentNo = target.getSegmentNo();
        String body = "";
        String tail = "";
        String head = "";
        try {
                //대상자(NVSEGMENT) 정보를 입력한다.
                SqlAnalyzer sa = new SqlAnalyzer();
                if(!sa.parse(target.getSqlHeadBody())) {
                    head = target.getSqlHeadBody();
                } else {
                    head =sa.getSqlHead();
                    body= sa.getSqlBody();
                    tail = sa.getSqlTail();
                }

                segmentDao.updateSegmentByPk(
                    new SegmentVo.Builder(segmentNo)
                    .setDbinfoSeq(target.getDbInfoSeq())
                    .setSqlHead(head)
                    .setSqlBody(body)
                    .setSqlTail(tail)
                    .setUpdateQuery(target.getUpdateQuery())
                    .setLastUpdateDt(DateUtil.dateToString("yyyyMMdd", new Date())).build()
                );

        }catch(Exception e) {
            log.error("{}" , e);
            throw new Exception("target data Update or Insert Error");
        }
    }

    /**
     * NVSEGMENT테이블에서 대상자 쿼리를 가져온다.
     *
     * @param segmentNo
     * @return
     */
    public TargetQueryInfo selectTargetQueryInfo(int segmentNo) {
        return segmentDao.selectTargetQueryInfo(segmentNo);
    }

}
