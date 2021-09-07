package com.mnwise.wiseu.web.template.web;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.util.SmartEncodingInputStream;
import com.mnwise.wiseu.web.template.model.ContentVo;
import com.mnwise.wiseu.web.template.service.TemplateService;

@Controller
public class TemplatePreviewController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TemplateListController.class);

    @Autowired private TemplateService templateService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;


    /**
     * - [템플릿>템플릿 등록] 템플릿 보기(팝업) <br/>
     * - JSP : /template/template_preview_content.jsp <br/>
     *
     * - [캠페인>캠페인 등록>2단계] 템플릿 불러오기 (팝업) - 템플릿명 클릭시 템플릿 내용 가져오기
     */
    // /template/template_preview_popup.do, /template/editor/template_preview_editor.do
    @RequestMapping(value={"/template/templatePreviewPopup.do", "/template/editor/importTemplate.do"}, method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView view(ContentVo contentVo, @RequestParam(defaultValue="") String templateType, HttpServletRequest request) throws Exception {
        try {
            contentVo = templateService.getTemplateInfo(contentVo.getContsNo());

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(contentVo.getGrpCd(), contentVo.getUserId(), contentVo.getAuthType())) {
                return new ModelAndView("");
            }

            String msg = messageSourceAccessor.getMessage("editor.alert.template.load", new String[] {contentVo.getContsNm()});

            StringBuffer template = new StringBuffer();
            if(contentVo.getFileType().equals("I")) {
                log.info(contentVo.toString());

                String fileUrlNm = contentVo.getFileUrlNm();
                fileUrlNm = fileUrlNm.replaceAll(super.templateUploadDir, super.templateDefaultUrl);

                log.info(fileUrlNm);

                template.append("<img src=\"" + fileUrlNm + "\" >");
            } else {
                String fileNm = contentVo.getFileUrlNm();
                File templateFile = new File(StringUtil.escapeFilePath(fileNm));

                if(log.isDebugEnabled()) {
                    log.debug("[ FILE EXISTS ] " + templateFile.exists());
                    log.debug("[  FILE PATH  ] " + templateFile.getPath());
                }

                if(!templateFile.exists()) {
                    msg = messageSourceAccessor.getMessage("editor.alert.template.loaderr2", new String[] {contentVo.getContsNm()});
                } else {
                    SmartEncodingInputStream in = null;
                    String charsetName = null;
                    int i = -1;

                    try {
                        in = new SmartEncodingInputStream(new FileInputStream(templateFile), SmartEncodingInputStream.BUFFER_LENGTH_4KB, Charset.forName("EUC-KR"), true);
                        charsetName = in.getEncoding().displayName();

                        byte[] b = new byte[4096];

                        while((i = in.read(b)) != -1) {
                            template.append(new String(b, 0, i, charsetName));
                        }
                    } catch(Exception e) {
                        log.error(null, e);
                    } finally {
                        IOUtil.closeQuietly(in);
                    }
                }
            }

            contentVo.setContent(template.toString());

            ModelAndView mav = null;
            if("editor".equals(templateType)) {  // [캠페인>캠페인 등록>2단계] 템플릿 불러오기 (팝업) - 템플릿명 클릭시 템플릿 내용 가져오기
                mav = new ModelAndView("editor/importTemplate");  // editor/template_preview_editor
            } else {  // [템플릿>템플릿 등록] 템플릿 보기(팝업)
                mav = new ModelAndView("template/templatePreviewPopup");  // template/template_preview_content
            }

            mav.addObject("contentVo", contentVo);
            mav.addObject("msg", msg);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
