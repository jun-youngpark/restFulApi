package com.mnwise.wiseu.web.editor.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.Sprintf;
import com.mnwise.wiseu.web.base.BaseService;

@Service
public class EditorUploadService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(EditorUploadService.class);

    @Value("${multipart.upload.dir}")
    private String multipartUploadDir;
    @Value("${multipart.upload.ext}")
    private String multipartUploadExt;

    /**
     * 지정한 경로에 파일을 생성한다. Map 에 파일은 하나밖에 저장되지 않겠지만, [key, value] 구조를 쓰게 위해 사용함
     *
     * @param inputStream 업로드 할 파일 InputStream
     * @param fileName 업로드 할 파일 절대 경로
     * @param type EM/EC
     * @param uploadType template / attachment
     * @param no campaign_no / ecare_no
     * @param seq 일련번호
     * @param date 현재날짜 - yyyyMM
     * @return
     */
    public Map<String, File> saveFile(final InputStream inputStream, final String fileName, String type, String uploadType, int no, int seq, String date) {
        File file = null;
        Map<String, File> map = new HashMap<>();

        // 템플릿 zip 파일
        if(uploadType.equalsIgnoreCase("template")) {
            if(type.equalsIgnoreCase("EM")) {
                file = new File(super.templateUploadDir + "/campaign/" + Sprintf.format(no, 15, '0') + "/" + fileName);
            } else {
                file = new File(super.templateUploadDir + "/ecare/" + Sprintf.format(no, 15, '0') + "/" + fileName);
            }

            copyFile(inputStream, file);

            map.put(fileName, file);
        } else { // 캠페인 첨부파일
            if(isPermittedExt(fileName)) {
                // 20150701 kpjeong 캠페인과 이케어 번호가 동일할 시 업로드 파일이 겹쳐짐 문제 수정
                if(type.equalsIgnoreCase("EM")) {
                    file = new File(multipartUploadDir + "/EM/" + Sprintf.format(no, 15, '0') +  "/" + no + "_" + date + "_" + seq
                        + fileName.substring(fileName.lastIndexOf(".")));
                } else {
                    file = new File(multipartUploadDir + "/EC/" + Sprintf.format(no, 15, '0') +  "/" + no + "_" + date + "_" + seq
                        + fileName.substring(fileName.lastIndexOf(".")));
                }

                copyFile(inputStream, file);

                map.put(fileName, file);
            } else {
                log.error("saveFile() - File Upload Denied.");
                map.put(fileName, file);
            }
        }

        return map;
    }

    /**
     * 설정 파일에서 허용한 확장자인지 확인
     *
     * @param fileName
     * @return
     */
    private final boolean isPermittedExt(final String fileName) {
        final String[] extList = multipartUploadExt.split(",");
        for(int j = 0, l = extList.length; j < l; j++) {
            if(fileName.toLowerCase().endsWith(extList[j])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 업로드한 파일을 로컬 서버에 복사
     *
     * @param in
     * @param file
     */
    private final void copyFile(final InputStream in, final File file) {
        FileOutputStream out = null;
        try {
            FileUtil.forceMkdir(new File(file.getParent()));
            out = new FileOutputStream(file);

            IOUtil.copyLarge(in, out);
        } catch(FileNotFoundException e) {
            log.error("saveFile() - File Not Found. " + e);
        } catch(IOException e) {
            log.error("saveFile() - Error while saving file. " + e);
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }
    }
}
