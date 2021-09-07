package com.mnwise.wiseu.web.env.service;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;

/**
 * 환경 설정 - DB 관리
 */
@Service
public class EnvDataBaseService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(EnvDataBaseService.class);

    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private SegmentDao segmentDao;

    /**
     * 환경 설정 - DB 관리 정보를 가져온다.
     *
     * @return
     * @throws Exception
     */
    public List<DbInfoVo> selectDataBaseList() throws Exception {
        List<DbInfoVo> returnDbList = dbInfoDao.selectDataBaseList();

        return returnDbList;
    }

    /**
     * 환경 설정 - DB 관리 정보를 삭제한다.
     *
     * @param dbInfo
     * @return
     * @throws Exception
     */
    public int deleteDataBaseList(DbInfoVo dbInfo) throws Exception {
        int count = segmentDao.selectSegmentCountByDbInfoSeq(dbInfo.getDbInfoSeq());
        return (count > 0) ? 0 : dbInfoDao.deleteDbInfoByPk(dbInfo.getDbInfoSeq());
    }

    /**
     * 환경 설정 - DB 관리 정보를 추가한다.
     *
     * @param dbInfo
     * @throws Exception
     */
    public void insertDataBaseList(DbInfoVo dbInfo) throws Exception {
        int dbInfoSeq = dbInfoDao.selectNextDbInfoSeq();

        dbInfo.setDbInfoSeq(dbInfoSeq);
        dbInfoDao.insertDbInfo(dbInfo);
    }

    /**
     * 환경 설정 - DB 관리 정보를 변경한다.
     *
     * @param dbInfo
     * @throws Exception
     */
    public void updateDataBaseList(DbInfoVo dbInfo) throws Exception {
        dbInfoDao.updateDbInfoByPk(dbInfo);
    }

    /**
     * - [환경설정>DB 관리] DB 관리 - DB체크 <br/>
     * - DWR : EnvDataBaseService.dbChk <br/>
     * 환경 설정 - DB 관리에서 DB 등록 여부를 체크한다. database.jsp에서 dwr로 호출
     *
     * @param driverNm
     * @param driverDSN
     * @param userId
     * @param passWd
     * @param testQuery
     * @return
     */
    public ResultDto dbChk(String driverNm, String driverDSN, String userId, String passWd, String testQuery) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        /* if(cipher.equalsIgnoreCase("on")){ passWd = xCryptionAria.encrypt(passWd); } */
        try {
            // DriverManager.registerDriver(getDriver(driverNm));
            Class.forName(driverNm);
            con = DriverManager.getConnection(driverDSN, userId, passWd);
            pstmt = con.prepareStatement(testQuery);
            rs = pstmt.executeQuery();
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(pstmt);
            IOUtil.closeQuietly(con);
        }

        return new ResultDto(ResultDto.OK);
    }

    public Driver getDriver(String driverNm) {
        try {
            if("oracle.jdbc.OracleDriver".equals(driverNm)) {
                return new oracle.jdbc.OracleDriver();
            } else if("oracle.jdbc.driver.OracleDriver".equals(driverNm)) {
                return new oracle.jdbc.driver.OracleDriver();
            }
        } catch(Exception e) {
            log.error("Invalid jdbc driver : " + driverNm);
        }
        return null;
    }
}
