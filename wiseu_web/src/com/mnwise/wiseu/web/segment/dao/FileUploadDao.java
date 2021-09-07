package com.mnwise.wiseu.web.segment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.segment.model.TargetVo;

/**
 * NVFILEUPLOAD 테이블 DAO 클래스
 */
@Repository
public class FileUploadDao extends BaseDao {
    private static final Logger log = LoggerFactory.getLogger(FileUploadDao.class);

    /**
     * 대상자 입력
     *
     * @param targetVo
     * @return
     */
    public int insertFileUpload(TargetVo target) {
        security.securityObject(target, "ENCRYPT");
        return insert("FileUpload.insertFileUpload", target);
    }

    public int insertBulkFileUpload(List<Map<String, Object>> paramMapList) {
        int row = 0;

        log.debug("insert size : " + paramMapList.size());
        for(Map<String, Object> paramMap : paramMapList) {
            security.securityMap(paramMap, "ENCRYPT");
            row += insert("FileUpload.insertBulkFileUpload", paramMap);
        }

        return row;
    }

    public int updateFileUploadByPk(TargetVo target) {
        security.securityObject(target, "ENCRYPT");
        return update("FileUpload.updateFileUploadByPk", target);
    }

    /**
     * 대상자 수정
     *
     * @param target
     * @return
     */
    public int updateSegmentTarget(TargetVo target) {
        security.securityObject(target, "ENCRYPT");
        return update("FileUpload.updateSegmentTarget", target);
    }

    /**
     * 대상자 삭제
     *
     * @param targetVo
     * @return
     */
    public int deleteFileUploadByPk(int targetNo, String customerId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("targetNo", targetNo);
        paramMap.put("customerId", customerId);
        return delete("FileUpload.deleteFileUploadByPk", paramMap);
    }

    /**
     * 대상자 삭제(TARGET_NO에 해당하는 데이터)
     *
     * @param targetVo
     * @return
     */
    public int deleteFileUploadByTargetNo(int targetNo) {
        return delete("FileUpload.deleteFileUploadByTargetNo", targetNo);
    }

    public TargetVo selectFileUploadByPk(int targetNo, String customerId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("targetNo", targetNo);
        paramMap.put("customerId", customerId);
        return (TargetVo) selectOne("FileUpload.selectFileUploadByPk", paramMap);
    }

    public int selectNextTargetNo() {
        return (Integer) selectOne("FileUpload.selectNextTargetNo");
    }

    public int importedDataCnt(int targetNo) {
        Integer count = (Integer) selectOne("FileUpload.selectImportedDataCnt", targetNo);
        return (count == null) ? 0 : count;
    }

    /**
     * 대상자 추가시 중복된 CUSTOMER_ID 값이 있는지 체크한다.
     *
     * @param map
     * @return
     */
    public int getCustomerIdCount(int targetNo, String customerId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("targetNo", targetNo);
        paramMap.put("customerId", customerId);
        Integer value =(Integer) selectOne("FileUpload.getCustomerIdCount", paramMap);
        return value== null? 0: value;
    }

}