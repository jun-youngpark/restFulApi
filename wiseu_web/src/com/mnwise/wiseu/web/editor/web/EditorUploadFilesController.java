package com.mnwise.wiseu.web.editor.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.formobjects.DataImportForm;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.editor.model.MultipartFileVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.editor.service.EditorCampaignService;
import com.mnwise.wiseu.web.editor.service.EditorEcareService;
import com.mnwise.wiseu.web.editor.service.EditorTemplateFileService;
import com.mnwise.wiseu.web.editor.service.EditorUploadService;

/**
 * 저작기 파일업로드(ZIP 파일 불러오기, HTML 파일 불러오기, 파일 첨부하기) 처리 Controller
 */
@Controller
public class EditorUploadFilesController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorUploadFilesController.class);

    @Autowired private EditorCampaignService editorCampaignService;
    @Autowired private EditorEcareService editorEcareService;
    @Autowired private EditorTemplateFileService editorTemplateFileService;
    @Autowired private EditorUploadService editorUploadService;
    @Autowired private AjaxFileUploadMultipartResolver editorMultipartResolver;

    /**
     * [공통팝업>불러오기>ZIP 파일 불러오기 (팝업)] 전송 버튼 클릭
     * [공통팝업>불러오기>HTML 파일 불러오기 (팝업)] 전송 버튼 클릭
     * [공통팝업>불러오기>파일 첨부하기 (팝업)] 전송 버튼 클릭
     *
     * @param dataImportForm
     * @param request
     * @param errors
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/editor/upload.do", method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView onSubmit(@ModelAttribute("command") DataImportForm dataImportForm, HttpServletRequest request, BindingResult errors) throws Exception {
        try {
            MultipartHttpServletRequest multipartRequest = null;
            try {
                multipartRequest = editorMultipartResolver.resolveMultipart(request);
            } catch(MaxUploadSizeExceededException e) {
                long fileSizeMax = editorMultipartResolver.getFileUpload().getSizeMax();
                errors.rejectValue("file", "editor.import.zip.oversize", new String[] {getFileSizeUnit(fileSizeMax)}, "bundle editor.import.zip.oversize not found");
                log.warn("Maximum upload size -> " + fileSizeMax);

                return new ModelAndView("editor/importPopup_file");
            }

            if(StringUtil.isNotBlank(multipartRequest.getParameter("cancel"))) {
                log.info("Canceling operation.");
                return new ModelAndView("editor/importPopup_file");
            }

            MultipartFile multipartFile = multipartRequest.getFile("file");
            if(multipartFile.getSize() == 0) {
                errors.rejectValue("file", "errors.upload.size.zero", null, "bundle errors.upload.size.zero not found");
                log.warn("파일 사이즈가 0입니다.");
                return new ModelAndView("editor/importPopup_file");
            }

            String type = multipartRequest.getParameter("type");
            String uploadType = multipartRequest.getParameter("uploadType");
            int no = ServletRequestUtils.getIntParameter(multipartRequest, "no", 0);
            String templateType = multipartRequest.getParameter("templateType");

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("no", no);
            returnData.put("type", type);
            returnData.put("uploadType", uploadType);
            returnData.put("templateType", templateType);

            String fileName = null;
            boolean existFile = false;
            File file = null;
            int seq = 0;

            // 파일이면 파일이 존재하는 확인
            if(uploadType.equalsIgnoreCase("file")) {
                if(type.equals("EM")) {
                    seq = editorCampaignService.selectEditorCampaignMultipartfileMax();
                } else {
                    //seq = editorEcareService.selectEditorEcareMultipartfileMax(no);
                }

                @SuppressWarnings("rawtypes")
                List listFile = editorCampaignService.selectEditorCampaignMultipartfile(no);
                fileName = multipartFile.getOriginalFilename();
                long fileSize = multipartFile.getSize();

                for(int i = 0; i < listFile.size(); i++) {
                    MultipartFileVo multipartfileVo = (MultipartFileVo) listFile.get(i);

                    if(multipartfileVo.getFileName().equals(fileName) && multipartfileVo.getFileSize() == fileSize) {
                        existFile = true;
                        break;
                    }
                }
            } else if(uploadType.equalsIgnoreCase("html")) { // HTML 불러오기
                String fileNm = multipartFile.getOriginalFilename();
                int idx = fileNm.lastIndexOf(".");
                if(idx > -1) {
                    String ext = fileNm.substring(idx + 1);
                    if(ext.equalsIgnoreCase("html") == false) {  // 확장자가 'html'이 아닐 경우 예외 처리
                        errors.rejectValue("file", "errors.upload.ext.denied", null, "Attach Denied File");
                        return new ModelAndView("editor/importPopup_file", returnData);
                    }
                } else {
                    errors.rejectValue("file", "errors.upload.ext.denied", null, "Attach Denied File");
                    return new ModelAndView("editor/importPopup_file", returnData);
                }

                BufferedInputStream in = null;
                ByteArrayOutputStream out = null;

                try {
                    in = new BufferedInputStream(multipartFile.getInputStream());
                    out = new ByteArrayOutputStream();

                    UniversalDetector detector = new UniversalDetector(null);
                    int nread;
                    byte[] b = new byte[4096];

                    while((nread = in.read(b)) > 0 && !detector.isDone()) {
                        detector.handleData(b, 0, nread);
                        out.write(b, 0, nread);
                    }
                    detector.dataEnd();

                    String charsetName = detector.getDetectedCharset();
                    if(log.isDebugEnabled()) {
                        if(charsetName != null) {
                            log.debug(charsetName + " 문자셋 HTML 파일");
                        } else {
                            log.debug("No encoding detected.");
                            charsetName ="UTF-8";
                        }
                    }
                    detector.reset();
                    returnData.put("html", new String(out.toByteArray(),charsetName));
                } catch(Exception e) {
                    log.error(null, e);
                } finally {
                    IOUtil.closeQuietly(out);
                    IOUtil.closeQuietly(in);
                }
                // HTML 파일은 여기서 리턴
                return new ModelAndView("editor/importPopup_file", returnData);
            }

            // ZIP 템플릿 업로드나 파일첨부일 때 수행
            if(existFile) {
                MultipartFileVo multipartfileVo = new MultipartFileVo();
                multipartfileVo.setFileName(fileName);
                multipartfileVo.setMsg(fileName + " 파일이 이미 존재합니다.");

                returnData.put("multipartfileVo", multipartfileVo);
            } else {
                // 파일 업로드 - ZIP 템플릿 파일이나 첨부파일 일 때
                String date = DateUtil.getNowDateTime("yyyyMM");
                Map<String, File> map = editorUploadService.saveFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), type, uploadType, no, seq, date);

                Iterator<String> iter = map.keySet().iterator();
                if(iter.hasNext()) {
                    fileName = iter.next();
                    file = map.get(fileName);

                    if(file == null) {
                        errors.rejectValue("file", "errors.upload.ext.denied", null, "Attach Denied File");
                        return new ModelAndView("editor/importPopup_file", returnData);
                    }
                }

                // ZIP 템플릿 일 때
                if(uploadType.equalsIgnoreCase("template")) {
                    TemplateVo templateVo = editorTemplateFileService.getTemplateFile(type, no, file.getPath());
                    templateVo.setUploadType(uploadType);
                    returnData.put("uploadType", uploadType);
                    returnData.put("templateVo", templateVo);
                } else { // 파일 첨부일 때
                    MultipartFileVo multipartfileVo = new MultipartFileVo();
                    multipartfileVo.setNo(no);
                    multipartfileVo.setSeq(seq);
                    multipartfileVo.setFileName(fileName);
                    multipartfileVo.setFileAlias(file.getPath().substring(file.getPath().lastIndexOf(File.separator) + 1));
                    multipartfileVo.setFilePath(file.getPath());
                    multipartfileVo.setFileType(Const.AttachFileType.UPLOAD);
                    long fileSize = file.length();
                    String fileUnit = "B";

                    while(true) {
                        if(fileSize > 1024) {
                            if(fileUnit.equals("B")) {
                                fileUnit = "KB";
                            } else if(fileUnit.equals("KB")) {
                                fileUnit = "MB";
                            } else if(fileUnit.equals("MB")) {
                                fileUnit = "GB";
                            }

                            fileSize = fileSize / 1024;
                        } else {
                            break;
                        }
                    }

                    multipartfileVo.setFileSize(fileSize);
                    multipartfileVo.setFileUnit(fileUnit);

                    if(type.equals("EM")) {
                        editorCampaignService.insertEditorCampaignMultipartfile(multipartfileVo);
                    } else {
                        // editorEcareService.insertEditorEcareMultipartfile(multipartfileVo);
                    }

                    returnData.put("multipartfileVo", multipartfileVo);
                }
            }

            return new ModelAndView("editor/importPopup_file", returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 파일 사이즈를 얻는다.
     *
     * @param fileSize
     * @return
     */
    public String getFileSizeUnit(long fileSize) {
        String[] units = {
            "Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"
        };
        String fileUnit = null;

        if(fileSize == 0)
            return "0 Bytes";
        else if(fileSize == 1)
            return "1 Byte";
        else {
            for(int i = 0; i < units.length; i++) {
                if(fileSize > 1024) {
                    fileSize = fileSize / 1024;
                } else {
                    fileUnit = String.valueOf(fileSize) + units[i];
                    break;
                }
            }
        }

        return fileUnit;
    }

    @RequestMapping(value="/editor/getTemplateFile.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public TemplateVo getTemplateFile(String type, int no, String filePath) {
        try {
            return editorTemplateFileService.getTemplateFile(type, no, filePath);
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }
}
