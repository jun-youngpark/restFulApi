package com.mnwise.wiseu.web.editor.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.base.util.ZipUtil;
import com.mnwise.wiseu.web.editor.model.TemplateVo;

@Service
public class EditorTemplateFileService extends BaseService implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EditorTemplateFileService.class);

    @Value("${template.url}")
    private String templateUrl;
    @Value("${template.image.attribute}")
    private String templateImageAttribute;
    @Value("${template.ext}")
    private String templateExt;

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    /**
     * 템플릿 이미지 경로를 절대경로로 바꿔준다.
     */
    public String changeImagePathTemplate(String template, String imagePath) {
        if(StringUtil.isBlank(template)) {
            return "";
        }
        StringBuffer sb = null;
        try {
            Parser parser = new Parser(template.toString());
            NodeFilter[] nodeFilter = new NodeFilter[] {
                new TagNameFilter("html")
            };
            NodeList nodeList = parser.parse(nodeFilter[0]);
            String[] attributeList = templateImageAttribute.split(",");
            String[] extList = templateExt.split(",");
            Tag tag = null;
            String changeImagePath = null;

            sb = new StringBuffer();
            SimpleNodeIterator sni = nodeList.elements();
            int idx;
            while(sni.hasMoreNodes()) {
                Node node = sni.nextNode();

                if(node instanceof Tag) {
                    tag = (Tag) node;

                    Lexer lexer = new Lexer(tag.getPage());

                    Node nLine = null;
                    String sLine = null;
                    while((nLine = lexer.nextNode()) != null) {
                        if(nLine instanceof Tag) {
                            tag = (Tag) nLine;

                            for(int i = 0; i < attributeList.length; i++) {
                                sLine = tag.getAttribute(attributeList[i]);

                                if(sLine != null) {
                                    for(int j = 0; j < extList.length; j++) {
                                        if(sLine.toLowerCase().endsWith(extList[j])) {
                                            idx = sLine.lastIndexOf("/");
                                            if(idx == -1) {
                                                idx = 0;
                                            }
                                            sLine = sLine.substring(idx);
                                            changeImagePath = templateUrl + imagePath + sLine;
                                            tag.setAttribute(attributeList[i], changeImagePath);
                                        }
                                    }
                                }
                            }

                            sb.append(tag.toHtml());

                        } else {
                            sb.append(nLine.toHtml());
                        }
                    }
                }

            }

        } catch(Exception e) {
            log.error(null, e);
        }

        return sb.toString();
    }

    public String getTemplateContent(File htmlFile) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        String result = null;
        try {
            in = new BufferedInputStream(new FileInputStream(htmlFile));
            UniversalDetector detector = new UniversalDetector(null);
            out = new ByteArrayOutputStream();

            int nread;
            byte[] b = new byte[4096];

            while((nread = in.read(b)) > 0 && !detector.isDone()) {
                detector.handleData(b, 0, nread);
                out.write(b, 0, nread);
            }
            detector.dataEnd();

            String charsetName = detector.getDetectedCharset();
            detector.reset();

            if(log.isDebugEnabled())
                if(charsetName != null) {
                    log.debug(charsetName + " 문자셋 HTML 파일");
                } else {
                    log.debug("No encoding detected.");
                }
            result = new String(out.toByteArray(),charsetName);
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(in);
        }
        return result;
    }

    /**
     * Zip 폴더에 /images, html 형태
     *
     * @param
     * @return
     */
    public TemplateVo getTemplateFile(String type, int no, String filePath) {
        int result = 0;
        TemplateVo templateVo = new TemplateVo();

        try {
            File zipFile = new File(filePath);
            String decompressPath = zipFile.getPath().substring(0, zipFile.getPath().lastIndexOf("."));
            String imagePath = ZipUtil.decompressZip(decompressPath, zipFile);  // zip 파일 압축 해제
            String template = null;

            if(imagePath == null) {
                result = -1;
            } else {
                File[] fileList = new File(decompressPath).listFiles();
                for(int i = 0; i < fileList.length; i++) {
                    if(fileList[i].isFile()) {
                        // HTML 파일
                        if(fileList[i].getName().endsWith("html") || fileList[i].getName().endsWith("htm")) {
                            template = getTemplateContent(fileList[i]);
                            result = 0;
                            break;
                        }

                    }
                }
                // zip 파일 압축해제시 에러가 나면 imagePath가 null이기 때문에 try 블럭내에서 처리하도록 변경
                imagePath = imagePath.replaceAll("\\\\", "/");
                imagePath = imagePath.replaceAll(super.templateUploadDir, "");
                templateVo.setTemplate(changeImagePathTemplate(template, imagePath));
            }
        } catch(Exception e) {
            result = -1;
            log.error(null, e);
        }

        templateVo.setResult(result);
        if(result == -1) {
            MessageSourceAccessor msAccessor = (MessageSourceAccessor) ctx.getBean("messageSourceAccessor");
            WiseuLocaleChangeInterceptor localeChangeInterceptor = (WiseuLocaleChangeInterceptor) ctx.getBean("localeChangeInterceptor");

            String zip = msAccessor.getMessage("editor.import.zip.alert3", localeChangeInterceptor.getLocale());
            templateVo.setExceptionMsg(zip);
        }

        return templateVo;
    }
}
