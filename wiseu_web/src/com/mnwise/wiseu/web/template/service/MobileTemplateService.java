package com.mnwise.wiseu.web.template.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.common.dao.CdMstDao;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.common.service.AbstractTemplateService;
import com.mnwise.wiseu.web.common.util.KakaoRestApiUtil;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.template.model.MobileVo;

import net.sf.json.JSONObject;

@Service
public class MobileTemplateService extends AbstractTemplateService {
    private static final Logger log = LoggerFactory.getLogger(MobileTemplateService.class);

    @Autowired private CdMstDao cdMstDao;
    @Autowired private ServerInfoDao serverInfoDao;

    @Value("${mms.image.valid}")
    private String imgFileExe;
    @Value("${mms.sound.valid}")
    private String soundFileExe;
    @Value("${alimtalk.upload.extension}")
    private String alimTalkExtension;

    public int getMobileContentTotalCount(MobileVo mobileVo) {
        return mobileContentsDao.getMobileContentTotalCount(mobileVo);
    }

    public List<MobileVo> getMobileList(MobileVo mobileVo) {
        PagingUtil.setPagingRowcount(mobileVo);
        return mobileContentsDao.getMobileList(mobileVo);
    }

    public List<MobileVo> getMobileList(Map<String, Object> map) {
        return mobileContentsDao.getMobileList(map);
    }

    public MobileVo getMobileContentsInfo(int contsNo) {
        return mobileContentsDao.getMobileContentsInfo(contsNo);
    }

    public void deleteMobileContents(int contsNo) throws Exception {
        MobileVo vo = mobileContentsDao.selectMobileContentsByPk(contsNo);
        String filePath = vo.getFilePath();
        if(filePath != null) {
            filePath = FilenameUtils.separatorsToUnix(filePath);
            int endIndex = filePath.indexOf("/" + contsNo);
            if(endIndex > -1) {
                filePath = filePath.substring(0, endIndex) + "/" + contsNo;
                FileUtil.forceDelete(filePath);
            }
        }

        String filePreviewPath = vo.getFilePreviewPath();
        if(filePreviewPath != null) {
            filePreviewPath = FilenameUtils.separatorsToUnix(filePreviewPath);
            int endIndex = filePreviewPath.indexOf("/" + contsNo);
            if(endIndex > -1) {
                filePreviewPath = filePreviewPath.substring(0, endIndex) + "/" + contsNo;
                FileUtil.forceDelete(filePreviewPath);
            }
        }

        mobileContentsDao.deleteMobileContentsByPk(contsNo);
    }

    public String fileCheck(String fileEx, String fileType) {
        fileEx = fileEx.toLowerCase();
        if(log.isDebugEnabled()) {
            log.debug("fileEx : " + fileEx + "\nfileType : " + fileType);
            log.debug("imgFileExe : " + imgFileExe + "\nsoundFileExe : " + soundFileExe);
        }

        if(fileType.equalsIgnoreCase("I") || fileType.equalsIgnoreCase("C")) {
            if(imgFileExe.toLowerCase().indexOf(fileEx) > -1) {
                return "true";
            } else {
                return "false|지원하는 이미지 파일이 아닙니다.\n지원되는 이미지파일 : " + imgFileExe;
            }
        } else if(fileType.equalsIgnoreCase("S")) {
            if(soundFileExe.toLowerCase().indexOf(fileEx) > -1) {
                return "true";
            } else {
                return "false|지원하는 사운드 파일이 아닙니다.\n지원되는 이미지파일 : " + soundFileExe;
            }
        } else if(fileType.equalsIgnoreCase("T")) {
            return "true";
        } else if(fileType.equalsIgnoreCase("A")) {
            if(alimTalkExtension.toLowerCase().indexOf(fileEx) > -1) {
                return "true";
            } else {
                return "false|지원하는 파일이 아닙니다.\n지원되는 파일 : " + alimTalkExtension;
            }
        }

        return "false|파일 타입이 사운드 또는 이미지가 아닙니다.";
    }

    public Map<String, String> saveFile(InputStream in, String fileName, MobileVo mobileVo, String previewMode) {
        Map<String, String> returnMap = new HashMap<>();
        String preViewUrl = null;
        File uploadFile = null;
        /* file 경로 설정 */
        // templateUploadDir/템플릿번호/생성날짜
        uploadFile = new File(
            super.mmsUploadPathRoot + "/" + super.mmsUploadPath + "/" + mobileVo.getCreateDt() + "/" + mobileVo.getContsNo() + "/" + fileName);

        if(log.isDebugEnabled())
            log.debug("[uploadFile path] " + uploadFile.getPath());

        /* file 경로 설정 */
        if(previewMode != null) {
            preViewUrl = super.mmsUploadPath + "/" + mobileVo.getCreateDt() + "/" + mobileVo.getContsNo() + "/" + fileName;
            returnMap.put("filePath", preViewUrl);
        } else {
            returnMap.put("filePath", uploadFile.getPath());
        }
        returnMap.put("fileNm", uploadFile.getName());
        FileOutputStream out = null;
        try {
            FileUtil.forceMkdir(new File(uploadFile.getParent()));
            out = new FileOutputStream(uploadFile);
            // 파일 복사
            IOUtil.copyLarge(in, out);
        } catch(FileNotFoundException e) {
            log.error("saveFile() - File Not Found." + e);
            returnMap.put("isFail", "true");
        } catch(Exception e) {
            log.error("saveFile() - Error while saving file." + e);
            returnMap.put("isFail", "true");
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }

        return returnMap;
    }

    public String getImageUploadUrl(String filePath, String fileName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String image = fileToBinary(filePath);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image", image);
        jsonObject.put("name", fileName);
        jsonObject.put("sndDtm", sdf.format(new Date()));
        String jsonData = KakaoRestApiUtil.callRestApi(PropertyUtil.getProperty("kakao.biz.api.template.uploadImage"), jsonObject.toString());

        return jsonData;
    }

    public String fileToBinary(String filePath) {
        File file = new File(filePath);
        String str = new String();
        FileInputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            in = new FileInputStream(file);
        } catch(FileNotFoundException e) {
            log.error("Exception position : FileUtil - fileToString(File file)");
        }

        int len = 0;
        byte[] buf = new byte[1024];
        try {
            while((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }

            byte[] fileArray = out.toByteArray();
            str = new String(Base64.encodeBase64(fileArray));
        } catch(IOException e) {
            log.error("Exception position : FileUtil - fileToString(File file)");
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }

        return str;
    }

    public void modifyMobileContents(MobileVo mobileVo) {
        String tagNm = mobileVo.getTagNm();
        mobileVo.setTagNm((tagNm == null || tagNm.equals("")) ? null : tagNm);
        int tagNo = tagDao.selectTagNoWithInsert("nvmobilecontentstag", mobileVo.getTagNm());
        mobileVo.setTagNo(tagNo);
        mobileContentsDao.modifyMobileContents(mobileVo);
    }
}
