package com.mnwise.wiseu.web.segment.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.common.util.DBConnectionUtil;
import com.mnwise.wiseu.web.segment.dao.SegGenealogyDao;
import com.mnwise.wiseu.web.segment.dao.SegmentCheckDao;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.xsqlparser.SqlAnalyzer;

/**
 * 대상자 관리 서비스
 */
@Service
public class SqlSegmentService extends BaseService implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(SqlSegmentService.class);

    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private SegGenealogyDao segGenealogyDao;
    @Autowired private SegmentCheckDao segmentCheckDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private TagDao tagDao;

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    /**
     * 세그먼트 정보를 가져온다.
     *
     * @param segmentNo
     * @return
     */
    public SegmentVo selectSegmentByPk(int segmentNo) {
        return segmentDao.selectSegmentByPk(segmentNo);
    }

    /**
     * 1단계 정보 저장
     * 세그먼트 기본정보 저장
     */
    public int saveSqlSegment1step(SegmentVo segmentVo) {
        int tagNo = tagDao.selectTagNoWithInsert("nvsegmenttag", segmentVo.getTagNm());
        segmentVo.setTagNo(tagNo);
        segmentVo.setLastUpdateDt(DateFormatUtils.format(new Date(), "yyyyMMdd"));

        if(segmentVo.getSegmentNo() > 0) {
            // 세그먼트 기본정보 갱신
            segmentDao.updateSegmentByPk(segmentVo);
            return segmentVo.getSegmentNo();
        } else {
            // 세그먼트 기본정보 추가
            segmentVo.setSegmentNo(segmentDao.selectNextSegmentNo());
            segmentDao.insertSegment(segmentVo);
            return segmentVo.getSegmentNo();
        }

    }

    public void saveBookMarkSeg(SegmentVo segmentVo) {
        int cnt = segmentCheckDao.selectBookMarkCnt(segmentVo.getSegmentNo());
        if(segmentVo.getBookmarkSeg().equalsIgnoreCase("Y") && cnt == 0) {
            segmentCheckDao.insertSegmentCheck(segmentVo.getSegmentNo(), segmentVo.getUserId());
        } else if(segmentVo.getBookmarkSeg().equalsIgnoreCase("N") && cnt > 0) {
            segmentCheckDao.deleteSegmentCheckBySegmentNo(segmentVo.getSegmentNo());
        }
    }

    public int getSegmentSize(SegmentVo segmentVo) {
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());
        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());

        // 고객 DB가 INFORMIX이고 버젼이 10.x 미만인 경우 인라인뷰 쿼리가 동작하지 않으므로 DB 종류가 INFORMIX일 경우 일반 쿼리로 분기하여 처리
        String countQuery = "";
        if(dbInfoVo.getDbKind().toUpperCase().startsWith("INFORMIX")) {
            countQuery = " SELECT COUNT(*) " + segmentVo.getSqlHead().substring(segmentVo.getSqlHead().toUpperCase().indexOf("FROM")) + " " + segmentVo.getSqlBody() + " " + segmentVo.getSqlTail();
        } else {
            countQuery = " SELECT COUNT(*) FROM (" + segmentVo.getSqlHead() + segmentVo.getSqlBody() + segmentVo.getSqlTail() + ") A ";
        }
        countQuery = countQuery.replace("AND RESULT_SEQ = \'$RSEQ$\'", "").replace("SEND_FG = \'H\'", "SEND_FG = \'R\'");

        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        int returnInteger = 0;
        try {
            stmt = conn.createStatement();
            log.debug("\n\n" + countQuery + "\n\n");
            rs = stmt.executeQuery(countQuery);
            // rs.last();
            // returnInteger =rs.getRow();
            if(rs.next()) {
                returnInteger = rs.getInt(1);
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }
        return returnInteger;
    }

    public DbInfoVo selectDbInfo(SegmentVo segmentVo) {
        return dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());
    }

    public List<SemanticVo> selectSemanticList(int segmentNo) {
        return semanticDao.selectSemanticList(segmentNo);
    }

    public SegmentVo getSemantic1step(SegmentVo segmentVo) throws Exception {
        SegmentVo returnVo = new SegmentVo();
        List<SemanticVo> semanticList = new ArrayList<>();
        // dbInfoVo, semanticVo 두개를 returnVo에 넣고 리턴하자

        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());
        returnVo.setDbInfoVo(dbInfoVo);
        returnVo.setDbinfoSeq(segmentVo.getDbinfoSeq());

        String query = segmentVo.getSqlContext();
        returnVo.setSqlContext(query);

        if(dbInfoVo.getDbKind().equalsIgnoreCase("SYBASE")) {
            query = query + " at isolation 0";
        }

        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());
        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        if(query != null) {
            query = query.replaceAll("&lt;&gt;", "<>");
            query = query.replace("AND RESULT_SEQ = \'$RSEQ$\'", "").replace("SEND_FG = \'H\'", "SEND_FG = \'R\'");
        }

        try {
            /* NVSEMANTIC 정보 저장 시 컬럼명만 추출하지 않고 Table alias 명도 포함해서 parsing 되도록 SqlAnalyzer 로직추가 예) 대상자쿼리 : SELECT A.USER_ID, A.NAME_KOR, A.EMAIL FROM NVUSER A semantic 항목 1 : A.USER_ID semantic 항목 2 :
             * A.NAME_KOR semantic 항목 3 : A.EMAIL */
            SqlAnalyzer sa = new SqlAnalyzer();
            String[] columnName = null;
            int colCount = 0;

            sa.parse(query);
            columnName = sa.getFieldNames();

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            colCount = rsmd.getColumnCount();

            for(int i = 1; i <= colCount; i++) {
                SemanticVo semanticVo = new SemanticVo();
                semanticVo.setFieldSeq(i);
                semanticVo.setFieldNm(columnName[i - 1]);
                semanticList.add(semanticVo);
            } // END : for (int iii = 0; iii < parse_size_field; iii++) {

            returnVo.setSemanticList(semanticList);
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return returnVo;
    }

    /**
     * DB정보 리스트 가져오기
     */
    public List<DbInfoVo> selectDbInfoList() {
        return dbInfoDao.selectDbInfoList();
    }

    /**
     * - [대상자>대상자 등록] 대상자 쿼리등록 - 대상자 쿼리 체크 <br/>
     * - [대상자>대상자 등록] 대상자 쿼리등록 - 업데이트 쿼리 체크 <br/>
     */
    public String checkTargetQuery(int dbInfoSeq, String query) {
        String returnMsg = "";
        String returnQuery = "[Query]\n" + query + "\n\n";
        String returnResult = "[result]\n";
        query = query.replace('\r', ' ').replace('\n', ' ').trim();
        query = query.replace("AND RESULT_SEQ = \'$RSEQ$\'", "").replace("SEND_FG = \'H\'", "SEND_FG = \'R\'");
        // update 쿼리문 체크를 위한 분기 처리
        boolean updateFlag = false;
        if(query.trim().toLowerCase().startsWith("update")) {
            updateFlag = true;
            if(query.toLowerCase().indexOf("where") > 0) {
                query += " and 1=2 ";
            } else {
                query += " where 1=2 ";
            }
        }

        // messageSource를 가져온다.
        MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
        WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

        String verifymsg1 = msAccessor.getMessage("segment.alert.msg.verify.1", new String[] {
            returnQuery
        }, localeChangeInterceptor.getLocale());
        String verifymsg2 = msAccessor.getMessage("segment.alert.msg.verify.2", localeChangeInterceptor.getLocale());
        String verifymsg3 = msAccessor.getMessage("segment.alert.msg.verify.3", localeChangeInterceptor.getLocale());

        if(query.equals("")) {
            return verifymsg1;
        }

        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(dbInfoSeq);

        if(dbInfoVo.getDbKind().equalsIgnoreCase("SYBASE")) {
            query = query + " at isolation 0";
        }

        log.debug(">> 대상자 쿼리 추출 완료");

        DBConnectionUtil dbConnectionUtil = new DBConnectionUtil(dbInfoVo.getDriverNm(), dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());
        Connection conn = dbConnectionUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        int rsUpdate = 0;

        try {
            if (query.trim().endsWith(";")) {
                throw new IllegalArgumentException("invalid query");
            }

            stmt = conn.createStatement();
            if(updateFlag) { // update 쿼리
                rsUpdate = stmt.executeUpdate(query.replace("$RSEQ$", ""));
                log.info("update Cnt : " + rsUpdate);
                log.info("update Query : " + query);
                returnResult += "Test Update Cnt : " + rsUpdate + "\n";
                returnResult += "Test Update Query : " + query + "\n";
            } else { // select 쿼리
                rs = stmt.executeQuery(query.replace("$RSEQ$", ""));
                if(rs.next()) {
                    //2019.11.18 NVSCHEDULEACCEPT때 SEQ 컬럼 체크
                    SqlAnalyzer sa = new SqlAnalyzer();
                    sa.parse(query);
                    if(sa.getSqlHead().toUpperCase().indexOf("NVSCHEDULEACCEPT") > -1) {
                        try {
                            rs.getString("SEQ"); //조회만 시도.
                        }catch(Exception e) {
                            returnMsg += "not exist SEQ column!\n";
                            returnResult = returnResult + "error\n" + e.getMessage();
                            return returnMsg + returnQuery + returnResult;
                        }
                    }
                }
                log.info("select Query : " + query);
            }

            returnMsg += verifymsg2;
            returnResult = returnResult + "Success\n";
        } catch(Exception e) {
            returnMsg += verifymsg3;
            returnResult = returnResult + "error\n" + e.getMessage();
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }
        return returnMsg + returnQuery + returnResult;
    }

    /**
     * 2단계 Semantic 정보 저장
     */
    public SegmentVo saveSqlSegment2step(SegmentVo segmentVo) {
        List<SemanticVo> semanticList = segmentVo.getSemanticList();
        for(SemanticVo semanticVo : semanticList) {
            semanticVo.setSegmentNo(segmentVo.getSegmentNo());
            semanticDao.insertSemantic(semanticVo);
        }

        return segmentVo;
    }

    public String getBookMarkYn(int segmentNo) {
        int cnt = segmentCheckDao.selectBookMarkCnt(segmentNo);
        return cnt > 0 ? "Y" : "N";
    }

    /**
     * 기본 세그먼트 할당 이력을 삭제한다.
     *
     * @param segmentNo
     */
    public void deleteGenealogy(int segmentNo) {
        segGenealogyDao.deleteSegGenealogyBySegmentNo(segmentNo);
    }

    /**
     * - [대상자>대상자 조회] 대상자 리스트 - 삭제 <br/>
     */
    public int deleteCheck(int no, UserVo userVo) {
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(no);
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());
        segmentVo.setDbinfoSeq(dbInfoVo.getDbInfoSeq());

        Map<String, Object> param = new HashMap<>();
        param.put("segmentVo", segmentVo);
        param.put("userVo", userVo);

        int usingCnt = segmentDao.selectUsingList(param).size();
        int subSegCnt = segGenealogyDao.getSubSegmentCnt(segmentVo.getSegmentNo());

        if (usingCnt > 0 && subSegCnt > 0) {
            return 3;
        } else if (usingCnt > 0 && subSegCnt == 0) {
            return 2;
        } else if (usingCnt == 0 && subSegCnt > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 세그먼트 수행 및 하위 대상자 존재 여부 검사
     */
    public int modSegmentCheck(int segmentNo) throws Exception {
        // 선택한 대상자의 정보를 가져온다.
        SegmentVo segmentVo = segmentDao.selectSegmentByPk(segmentNo);

        // 선택한 대상자의 dbinfo 정보를 가져온다.
        DbInfoVo dbInfoVo = dbInfoDao.selectDbInfoByPk(segmentVo.getDbinfoSeq());

        // DB Seq 번호가 없을 경우 에러 날 수 있음.
        // ex) 테스트계 -> 운영계 이관 또는 디비 정보 삭제시
        if(dbInfoVo == null) {
            return -1;
        }

        segmentVo.setDbinfoSeq(dbInfoVo.getDbInfoSeq());
        // 캠페인 / 이케어의 수행 내역이 있는 지 여부를 검사한다.
        int usingCnt = (segmentDao.selectSendList(segmentNo)).size();

        // 하위 대상자가 존재하는 지 여부를 검사한다.
        int subSegCnt = segGenealogyDao.getSubSegmentCnt(segmentVo.getSegmentNo());

        if(usingCnt > 0 && subSegCnt > 0) { // 수행내역과 하위 대상자가 모두 존재할 경우
            return 3;
        } else if(usingCnt > 0 && subSegCnt == 0) { // 수행내역이 존재하고 하위대상자가 존재하지 않을 경우
            return 2;
        } else if(usingCnt == 0 && subSegCnt > 0) { // 수행내역은 존재하지 않고 하위대상자만 존재할 경우
            return 1;
        } else { // 둘다 존재하지 않은 경우
            return 0;
        }
    }

    /**
     * 선택한 대상자의 쿼리 업데이트
     *
     * @param segmentVo
     * @return
     */
    public int updateSegmentSql(SegmentVo segmentVo) {
        return segmentDao.updateSegmentSql(segmentVo);
    }
}
