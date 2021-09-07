package com.mnwise.wiseu.web.template.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.template.dao.ContentsDao;
import com.mnwise.wiseu.web.template.model.ContentVo;

@Service
public class TemplateService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(TemplateService.class);

    @Autowired private ContentsDao contentsDao;
    @Autowired private TagDao tagDao;

    @Value("${template.upload.path}")
    private String templateUploadDir;

    /**
     * 등록한 템플릿을 삭제한다.
     *
     * @param contentVo
     */
    public int setDeleteTemplate(ContentVo contentVo) {
        return contentsDao.deleteTemplate(contentVo);
    }

    /**
     * 템플릿 리스트를 가져온다.
     *
     * @param contentVo
     * @return
     */
    public List<ContentVo> getTemplateList(ContentVo contentVo) {
        return contentsDao.selectTemplateList(contentVo);
    }

    public ContentVo getTemplateInfo(int contsNo) {
        return contentsDao.selectTemplateInfo(contsNo);
    }

    /**
     * 템플릿 전체 건수를 가져온다.
     *
     * @param contentVo
     * @return
     */
    public int getTemplateTotalCount(ContentVo contentVo) {
        return contentsDao.selectTemplateTotalCount(contentVo);
    }

    public int selectNextContsNo() {
        return contentsDao.selectNextContsNo();
    }

    public Map<String, String> saveFile(InputStream in, String fileName, ContentVo contentVo, boolean isUpdate) throws Exception {
        Map<String, String> returnMap = new HashMap<>();

        if(!isUpdate)
            contentVo.setContsNo(selectNextContsNo());

        if(log.isDebugEnabled()) {
            log.debug("[ FileName ] " + fileName);
            log.debug("[ contsNo  ] " + contentVo.getContsNo());
            log.debug("[ UserId   ] " + contentVo.getUserId());
        }

        File uploadFile = null;
        /* file 경로 설정 */
        // templateUploadDir/템플릿번호/생성날짜.
        // 날짜경로를 초단위까지생성. 템플릿삭제후재등록시겹치지 않게하기위함
        uploadFile = new File(this.templateUploadDir + "/" + contentVo.getCreateDt() + contentVo.getCreateTm() + "/" + contentVo.getContsNo() + "/" + fileName);

        log.debug("[UploadFile Path] " + uploadFile.getPath());

        /* file 경로 설정 */
        returnMap.put("filePath", uploadFile.getPath());
        returnMap.put("fileNm", uploadFile.getName());
        if(uploadFile.getName().toLowerCase().endsWith(".gif") || uploadFile.getName().toLowerCase().endsWith(".jpg") || uploadFile.getName().toLowerCase().endsWith(".jpeg")
            || uploadFile.getName().toLowerCase().endsWith(".html") || uploadFile.getName().toLowerCase().endsWith(".htm")) {
            String url = this.templateUploadDir + "/" + contentVo.getCreateDt() + contentVo.getCreateTm() + "/" + contentVo.getContsNo() + "/" + uploadFile.getName();

            returnMap.put("fileUrl", url);
        } else {
            returnMap.put("fileUrl", uploadFile.getPath());
        }

        FileUtil.forceMkdir(new File(uploadFile.getParent()));
        FileOutputStream out = new FileOutputStream(uploadFile);

        IOUtil.copyLarge(in, out);  // 파일 복사

        IOUtil.closeQuietly(in);
        IOUtil.closeQuietly(out);

        return returnMap;
    }

    public void setInsertTemplate(ContentVo contentVo) {
        int tagNo = tagDao.selectTagNoWithInsert("nvcontentstag", contentVo.getTagNm());
        contentVo.setTagNo(tagNo);
        contentsDao.insertContents(contentVo);
    }

    public void setUpdateTemplate(ContentVo contentVo) {
        int tagNo = tagDao.selectTagNoWithInsert("nvcontentstag", contentVo.getTagNm());
        contentVo.setTagNo(tagNo);
        contentsDao.updateTemplate(contentVo);
    }
}
