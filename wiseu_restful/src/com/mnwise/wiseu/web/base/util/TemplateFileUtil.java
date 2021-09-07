package com.mnwise.wiseu.web.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.wiseu.web.base.util.ZipUtil;
import com.mnwise.wiseu.web.rest.model.custom.CustomContents;
import com.mnwise.wiseu.web.base.util.SmartEncodingInputStream;

/**
 * 템플릿 파일 업로드
 */
public class TemplateFileUtil {
    private static final Logger log = LoggerFactory.getLogger(TemplateFileUtil.class);

    public static Map<String, String> saveFile(InputStream in, String fileName, String creatDt, int ContsNo, String previewMode,String rootDir, String subDir) {
        Map<String, String> returnMap = new HashMap<String, String>();
        /* file 경로 설정 templateUploadDir/템플릿번호/생성날짜 */
        File uploadFile = new File(
            rootDir + File.separator + subDir + File.separator + creatDt + File.separator + ContsNo + File.separator + fileName);

        if(log.isDebugEnabled())
            log.debug("[uploadFile path] " + uploadFile.getPath());

        /* file 경로 설정 */
        if(previewMode != null) {
            String preViewUrl = subDir + "/" + creatDt + "/" + ContsNo + "/" + fileName;
            returnMap.put("filePreviewPath", preViewUrl);
        }
        returnMap.put("filePath", uploadFile.getPath());
        returnMap.put("fileNm", uploadFile.getName());
        FileOutputStream out = null;
        try {
            FileUtil.forceMkdir(new File(uploadFile.getParent()));
            out = new FileOutputStream(uploadFile);
            // 파일 복사
            IOUtil.copyLarge(in, out);
        } catch(FileNotFoundException e1) {
            log.debug("saveFile() - File Not Found." + e1);
            returnMap.put("isFail", "true");
        } catch(IOException e2) {
            log.debug("saveFile() - Error while saving file." + e2);
            returnMap.put("isFail", "true");
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }
        return returnMap;
    }

    public synchronized static File unZipFile(File zipFile) throws Exception {
        File htmlFile = null;
        ZipUtil.decompressZip(zipFile.getParent() + "/" + zipFile.getName().substring(0, zipFile.getName().lastIndexOf(".")), zipFile);
        File[] tmp = zipFile.getParentFile().listFiles();
        htmlFile = serchFile(tmp);
        return htmlFile;
    }

    private static File serchFile(File[] fileList) {
        File returnFile = null;
        for(int i = 0; i < fileList.length; i++) {
            String name = fileList[i].getPath();
            name = name.substring(name.lastIndexOf(".") + 1);
            if(fileList[i].isDirectory()) {
                returnFile = serchFile(fileList[i].listFiles());
                if(returnFile != null)
                    break;
            } else if(name.toLowerCase().equals("html") || name.toLowerCase().equals("htm")) {
                returnFile = new File(fileList[i].getPath());
                break;
            }
        }
        return returnFile;
    }

    public static void deleteFile(File _file) {
        if(_file.isDirectory()) {
            File listFile[] = _file.listFiles();
            for(int i = 0; i < listFile.length; i++) {
                deleteFile(listFile[i]);
            }
        } else {
            _file.delete();
        }
    }

    /**
     * 이미지 경로를 변경합니다.
     *
     * <pre>
     * 변경이력 : 20101129 파일 업로드 경로는 날짜+시간 인데 이미지 링크 경로는 날짜로 잡혀있어서 날짜+시간으로 수정
     * </pre>
     */
    public synchronized static File imgReplace(File htmlFile, CustomContents contentVo, String defaultUrl, String zipfileName) throws Exception {
        log.info(" * 템플릿 관리 - HTML IMG SRC REPLACE... (IMG SRC)");
        log.info(" * 템플릿 관리 - HTML SCRIPT SRC REPLACE... (SCRIPT SRC)");
        log.info(" * 템플릿 관리 - HTML LINK SRC REPLACE... (LINK SRC)");

        defaultUrl = defaultUrl + "/" + contentVo.getCreateDt() + contentVo.getCreateTm() + "/" + contentVo.getContsNo() + "/" + zipfileName.substring(0, zipfileName.lastIndexOf(".")) + "/";

        File imageDir = new File(htmlFile.getParent() + "/image");
        if(imageDir.exists()) {
            defaultUrl += "image/";
        } else {
            imageDir = new File(htmlFile.getParent() + "/images");
            if(imageDir.exists())
                defaultUrl += "images/";
        }

        log.info(" * URL - " + defaultUrl);

        SmartEncodingInputStream in = null;
        String charsetName = null;
        int i = -1;
        StringBuffer sb = new StringBuffer();

        try {
            in = new SmartEncodingInputStream(new FileInputStream(htmlFile), SmartEncodingInputStream.BUFFER_LENGTH_4KB, Charset.forName("EUC-KR"), true);
            charsetName = in.getEncoding().displayName();
            byte[] b = new byte[4096];

            log.debug(charsetName + " 문자셋 HTML 파일");

            while((i = in.read(b)) != -1) {
                if(charsetName.equalsIgnoreCase("UTF-8")) {
                    sb.append(new String(b, 0, i, "UTF-8"));
                } else {
                    sb.append(new String(b, 0, i, charsetName));
                }
            }
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(in);
        }

        // BOM('\uFEFF') 포함 UTF-8로 인코딩된 HTML 파일은 Parser에서 정상적오로 파싱하지 못해 BOM을
        // 제거함.
        String template = sb.toString();
        if(template.charAt(0) == '\uFEFF') {
            template = template.substring(1);
        }
        Parser parser = new Parser(template);
        NodeFilter[] nodeFilter = new NodeFilter[] {
            new TagNameFilter("html")
        };
        NodeList html = null;
        html = parser.parse(nodeFilter[0]);

        Lexer lexer = null;
        Node nLine = null;
        String tagName = null;
        Tag tag = null;
        StringBuffer htmlBuffer = new StringBuffer();

        SimpleNodeIterator sni = html.elements();
        while(sni.hasMoreNodes()) {
            lexer = new Lexer(sni.nextNode().getPage());

            while((nLine = lexer.nextNode()) != null) {
                if(nLine instanceof Tag) {
                    tag = (Tag) nLine;
                    tagName = tag.getTagName();
                    String background = tag.getAttribute("background");

                    if(tagName.equals("IMG") && !tag.isEndTag()) {
                        log.info("Find img Tag");

                        if(tag.getAttribute("src") != null) {
                            String sLine = tag.getAttribute("src");
                            String imagePath = defaultUrl + sLine.substring(sLine.lastIndexOf("/") + 1);
                            log.debug("1step toHTML(img) : " + tag.toHtml());
                            tag.setAttribute("src", imagePath);
                            log.debug("2step toHTML(img) : " + tag.toHtml());
                        }

                        htmlBuffer.append(tag.toHtml());

                    } else if(tagName.equals("SCRIPT") && !tag.isEndTag()) {
                        log.info("Find script Tag");

                        if(tag.getAttribute("src") != null) {
                            String sLine = tag.getAttribute("src");
                            String scriptPath = defaultUrl + sLine.substring(sLine.lastIndexOf("/") + 1);
                            log.debug("1step toHTML(script) : " + tag.toHtml());
                            tag.setAttribute("src", scriptPath);
                            log.debug("2step toHTML(script) : " + tag.toHtml());
                        }

                        htmlBuffer.append(tag.toHtml());

                    } else if(tagName.equals("LINK") && !tag.isEndTag()) {
                        log.info("Find css Tag");

                        if(tag.getAttribute("href") != null) {
                            String sLine = tag.getAttribute("href");
                            String cssPath = defaultUrl + sLine.substring(sLine.lastIndexOf("/") + 1);
                            log.debug("1step toHTML(css) : " + tag.toHtml());
                            tag.setAttribute("href", cssPath);
                            log.debug("2step toHTML(css) : " + tag.toHtml());
                        }

                        htmlBuffer.append(tag.toHtml());

                    } else if(background != null) {
                        String sLine = tag.getAttribute("background");
                        String imagePath = defaultUrl + sLine.substring(sLine.lastIndexOf("/") + 1);
                        log.debug("1step toHTML(default) : " + tag.toHtml());
                        tag.setAttribute("background", imagePath);
                        log.debug("2step toHTML(default) : " + tag.toHtml());

                        htmlBuffer.append(tag.toHtml());

                    } else {
                        htmlBuffer.append(nLine.toHtml());
                    }
                } else {
                    htmlBuffer.append(nLine.toHtml());
                } // if(nLine instanceof Tag)
            } // while((nLine = lexer.nextNode()) != null)

        } // while(sni.hasMoreNodes())

        File returnFile = writeFile(htmlBuffer, htmlFile.getParent() + "/" + htmlFile.getName() + "convert.html", charsetName);
        htmlFile.delete();

        return returnFile;
    }

    private static File writeFile(StringBuffer html, String path, String charset) throws Exception {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(html.toString().getBytes(charset));
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }

        return new File(path);
    }

}
