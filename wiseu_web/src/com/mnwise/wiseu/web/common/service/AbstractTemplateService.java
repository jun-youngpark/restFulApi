package com.mnwise.wiseu.web.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.template.dao.MobileContentsDao;
import com.mnwise.wiseu.web.template.model.MobileVo;

public class AbstractTemplateService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(AbstractTemplateService.class);

    @Autowired protected MobileContentsDao mobileContentsDao;
    @Autowired protected TagDao tagDao;

    @Value("${import.upload.dir}")
    protected String importDir;

    public String getImportedFilePath(String fileName) {
        return importDir + "/" + fileName;
    }

    public void saveFile(final InputStream in, final String fileName, String fileDir) throws Exception {
        if(StringUtils.isBlank(fileDir)) {
            fileDir = importDir;
        }
        File uploadFile = new File(importDir + "/" + fileName);
        log.debug(importDir + "/" + fileName);
        FileOutputStream out = null;

        try {
            FileUtil.forceMkdir(new File(uploadFile.getParent()));
            out = new FileOutputStream(uploadFile);
            IOUtil.copyLarge(in, out);
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }
    }

    public int selectNextContsNo() {
        return mobileContentsDao.selectNextContsNo();
    }

    public void insertMobileContent(MobileVo mobileVo) {
        String tagNm = mobileVo.getTagNm();
        mobileVo.setTagNm((tagNm == null || tagNm.equals("")) ? null : tagNm);
        int tagNo = tagDao.selectTagNoWithInsert("nvmobilecontentstag", mobileVo.getTagNm());
        mobileVo.setTagNo(tagNo);
        mobileContentsDao.insertMobileContents(mobileVo);
    }

    /**
     * 알림톡 정보를 조회한다.
     *
     * @param paramVo
     * @return
     */
    public MobileVo findAlimtalkTemplateInfo(MobileVo mobileVo) {
        return mobileContentsDao.findAlimtalkTemplateInfo(mobileVo);
    }

    /**
     * 카카오 템플릿 검수 상태가 등록(REG)인 템플릿 목록을 조회한다.
     *
     * @param userId
     * @return
     */
    public List<MobileVo> findRegStatusTemplate(String userId) {
        return mobileContentsDao.findRegStatusTemplate(userId);
    }

    /**
     * 카카오 템플릿 공유 유형 업데이트
     *
     * @param vo
     * @return
     */
    public int updateKakaoTemplateAuthType(MobileVo vo) {
        return mobileContentsDao.updateKakaoTemplateAuthType(vo);
    }

    /**
     * 카카오 템플릿을 업데이트
     *
     * @param vo
     * @return
     */
    public int updateKakaoTemplate(MobileVo vo) {
        return mobileContentsDao.updateKakaoTemplate(vo);
    }

    /**
     * 발신 프로필 키와 템플릿 코드에 해당하는 알림톡 템플릿을 삭제한다.
     *
     * @param mobileVo
     * @return
     */
    public int deleteAlimtalkTemplate(MobileVo mobileVo) {
        return mobileContentsDao.deleteAlimtalkTemplate(mobileVo);
    }

}
