package com.mnwise.wiseu.web.segment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.segment.dao.FileUploadDao;
import com.mnwise.wiseu.web.segment.dao.SegmentCheckDao;
import com.mnwise.wiseu.web.segment.dao.SegmentDao;
import com.mnwise.wiseu.web.segment.dao.SemanticDao;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;

/**
 * 대상자 업로드 Service
 */
@Service
public class UploadSegService extends BaseService {
    @Autowired private DbInfoDao dbInfoDao;
    @Autowired private FileUploadDao fileUploadDao;
    @Autowired private SegmentCheckDao segmentCheckDao;
    @Autowired private SegmentDao segmentDao;
    @Autowired private SemanticDao semanticDao;
    @Autowired private TagDao tagDao;

    public int selectNextTargetNo() {
        return fileUploadDao.selectNextTargetNo();
    }

    public int bulkDataImport(List<Map<String, Object>> paramMapList) {
        return fileUploadDao.insertBulkFileUpload(paramMapList);
    }

    public int setRegistSegment(SegmentVo segmentVo, String[] cvsHeaders, String[] cvsHeaderNames) throws Exception {
        int tagNo = tagDao.selectTagNoWithInsert("nvsegmenttag", segmentVo.getTagNm());
        segmentVo.setTagNo(tagNo);

        int dbinfoSeq = dbInfoDao.selectDbinfoSeq();
        segmentVo.setDbinfoSeq(dbinfoSeq);

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < cvsHeaders.length; i++) {
            sb.append("A." + cvsHeaders[i] + ",");
        }
        // String sqlTpl = StringUtils.arrayToCommaDelimitedString(cvsHeaders);
        String sqlTpl = sb.toString();
        sqlTpl = sqlTpl.substring(0, sqlTpl.lastIndexOf(","));
        // String sqlHead = "SELECT /*+ index(a pk_nvfileupload) */ " + sqlTpl +
        // " FROM NVFILEUPLOAD A ";
        //SECUDB NONE
        //NVFILEUPLOAD : 해당 필드 없음
        String sqlHead = "SELECT " + sqlTpl + " FROM NVFILEUPLOAD A ";
        segmentVo.setSqlHead(sqlHead);

        segmentVo.setLastUpdateDt(DateFormatUtils.format(new Date(), "yyyyMMdd"));

        if("on".equals(segmentVo.getShareYn()))
            segmentVo.setShareYn("Y");
        else
            segmentVo.setShareYn("N");

        if(!segmentVo.getReject().equals(Const.UPLOAD_REJECT_NO)) {
            segmentVo.setSegType("F");
        }

        // 세그먼트 최대값을 가져와 세그먼트를 저장한다.
        int maxSegmentNo = segmentDao.selectNextSegmentNo();
        segmentVo.setSegmentNo(maxSegmentNo);
        // 세그먼트 정보를 넣는다.
        if(segmentVo.isLargeUpload() == false) {
            segmentVo.setSqlBody(" WHERE TARGET_NO = 0");
            segmentVo.setDelimiter(null);
            segmentVo.setInMetaData(null);
        }
        segmentDao.insertSegment(segmentVo);

        List<SemanticVo> semanticList = new ArrayList<>();

        for(int i = 0; i < cvsHeaders.length; i++) {
            SemanticVo semanticVo = new SemanticVo();
            semanticVo.setSegmentNo(segmentVo.getSegmentNo());
            semanticVo.setFieldSeq(i + 1);
            semanticVo.setFieldNm(cvsHeaders[i]);
            semanticVo.setFieldDesc(cvsHeaderNames[i]);
            semanticVo.setFieldKey(" ");
            if("CUSTOMER_ID".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("K");
            if("CUSTOMER_NM".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("N");
            if("CUSTOMER_EMAIL".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("E");
            if("CUSTOMER_TEL".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("S");
            if("CUSTOMER_FAX".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("F");
            if("CUSTOMER_SLOT1".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("A");
            if("CUSTOMER_SLOT2".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("B");
            if("CALL_BACK".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("C");
            if("SENTENCE".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("D");
            if("SENDER_NM".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("X");
            if("SENDER_EMAIL".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("Y");
            if("RETMAIL_RECEIVER".equals(cvsHeaders[i]))
                semanticVo.setFieldKey("Z");

            // 이건 어떻게 쓰이는 값일까?
            semanticVo.setNullYn("N");

            semanticList.add(semanticVo);
        }

        // 시멘틱 정보를 넣는다.
        for(SemanticVo semanticVo : semanticList) {
            semanticDao.insertSemantic(semanticVo);
        }

        // 기본대상자인 경우 확인
        if("on".equals(segmentVo.getBookmarkSeg())) {
            segmentCheckDao.insertSegmentCheck(segmentVo.getSegmentNo(), segmentVo.getUserId());
        }

        return maxSegmentNo;
    }

    public int importedDataCnt(int targetNo) {
        return fileUploadDao.importedDataCnt(targetNo);
    }

    public int deleteFileUpload(int segmentNo) {
        return fileUploadDao.deleteFileUploadByTargetNo(segmentNo);
    }

    /**
     * 세그먼트 정보의 쿼리정보 및 size, activeYn 값을 update한다.
     */
    public void updateSegmentInfoAfterImportFile(SegmentVo segmentVo) {
        segmentDao.updateSegmentInfoAfterImportFile(segmentVo);
    }
}
