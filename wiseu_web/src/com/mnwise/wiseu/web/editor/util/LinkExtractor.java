package com.mnwise.wiseu.web.editor.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.AreaTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PatternMatchUtils;

import com.mnwise.common.util.StringUtil;

public class LinkExtractor {
    private static final Logger log = LoggerFactory.getLogger(LinkExtractor.class);

    private final static String sTag = "<";
    private final static String eTag = ">";

    /**
     * 문자열 내에 있는 링크 정보를 축출한다.
     *
     * @param s
     * @param errCode
     * @return
     */
    public Map<String, Object> linkExtractFromString(String s, String[] errCode) {
        Lexer lexer = new Lexer(s);
        Parser parser = new Parser(lexer);
        return linkExtract(parser, errCode);
    }

    /**
     * 링크 정보를 축출한다.
     *
     * @param parser
     * @param errCode
     * @return
     */
    private Map<String, Object> linkExtract(Parser parser, String[] errCode) {
        Map<String, Object> map = new HashMap<String, Object>();

        PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
        factory.registerTag(new AreaTag());
        parser.setNodeFactory(factory);

        NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
        NodeList linkList = null;
        try {
            linkList = parser.extractAllNodesThatMatch(linkFilter);
        } catch(ParserException e) {
            log.error(null, e);
        }
        List<LinkInfo> aList = new ArrayList<>();
        if(linkList != null) {
            for(int i = 0; i < linkList.size(); i++) {
                LinkInfo lInfo = new LinkInfo();
                LinkTag linklTag = (LinkTag) linkList.elementAt(i);
                lInfo.setTagType("a");
                // 링크추적이 걸린 경우 LINK_URL을 URL로 대체한다.
                String link = linklTag.getLink();

                // 수신거부 같은 wiseU 내부 스크립트 링크인 경우는 제외한다.
                if(link.startsWith("{$")) {
                    continue;
                }
                String param = link.substring(link.indexOf("?") + 1);
                StringTokenizer st;
                try {
                    st = new StringTokenizer(param.replaceAll("&amp;", "&"), "&");
                    while(st.hasMoreTokens()) {
                        String temp = st.nextToken();
                        if(temp.startsWith("LINK_URL")) {
                            link = StringUtil.split(URLDecoder.decode(temp, "utf-8"), "=")[1];
                            break;
                        }
                    }
                } catch(UnsupportedEncodingException e) {
                    log.error(null, e);
                }

                lInfo.setLinkUrl(link);
                lInfo.setLinkText(linklTag.getLinkText());
                int httpStatusCode = SimpleURLCheck.getHttpStatusCode(link);
                lInfo.setHttpStatusCode(httpStatusCode);
                if(errCode != null) {
                    if(PatternMatchUtils.simpleMatch(errCode, String.valueOf(httpStatusCode))) {
                        aList.add(lInfo);
                    }
                } else {
                    aList.add(lInfo);
                }
            }
        }

        parser.reset();

        NodeFilter areaFilter = new NodeClassFilter(AreaTag.class);
        NodeList areaList = null;
        try {
            areaList = parser.extractAllNodesThatMatch(areaFilter);
        } catch(ParserException e) {
            log.error(null, e);
        }

        List<LinkInfo> arList = new ArrayList<>();
        if(areaList != null) {
            for(int i = 0; i < areaList.size(); i++) {
                LinkInfo lInfo = new LinkInfo();
                AreaTag areaTag = (AreaTag) areaList.elementAt(i);
                lInfo.setTagType("area");
                // 링크추적이 걸린 경우 LINK_URL을 URL로 대체한다.
                String link = areaTag.getHref();
                // href에 값을 넣지 않았을 경우
                if(link == null)
                    continue;
                String param = null;
                if(link.indexOf("?") > -1) {
                    param = link.substring(link.indexOf("?") + 1);
                } else {
                    param = link;
                }

                StringTokenizer st;
                try {
                    st = new StringTokenizer(param.replaceAll("&amp;", "&"), "&");
                    while(st.hasMoreTokens()) {
                        String temp = st.nextToken();
                        if(temp.startsWith("LINK_URL")) {
                            link = StringUtil.split(URLDecoder.decode(temp, "utf-8"), "=")[1];
                            break;
                        }
                    }
                } catch(UnsupportedEncodingException e) {
                    log.error(null, e);
                }

                lInfo.setLinkUrl(link);
                lInfo.setLinkText(areaTag.getText());
                int httpStatusCode = SimpleURLCheck.getHttpStatusCode(link);
                lInfo.setHttpStatusCode(httpStatusCode);
                if(errCode != null) {
                    if(PatternMatchUtils.simpleMatch(errCode, String.valueOf(httpStatusCode))) {
                        arList.add(lInfo);
                    }
                } else {
                    arList.add(lInfo);
                }
            }
        }

        parser.reset();

        NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
        NodeList imageList = null;
        try {
            imageList = parser.extractAllNodesThatMatch(imageFilter);
        } catch(ParserException e) {
            log.error(null, e);
        }
        List<LinkInfo> iList = new ArrayList<>();
        if(imageList != null) {
            for(int i = 0; i < imageList.size(); i++) {
                LinkInfo lInfo = new LinkInfo();

                ImageTag imagelTag = (ImageTag) imageList.elementAt(i);
                lInfo.setTagType("image");
                if(imagelTag.getImageURL().indexOf("ReceiveConfirm") > -1) {
                    continue;
                }

                lInfo.setLinkUrl(imagelTag.getImageURL());
                lInfo.setLinkText(sTag + imagelTag.getText() + eTag);
                int httpStatusCode = SimpleURLCheck.getHttpStatusCode(imagelTag.getImageURL());
                lInfo.setHttpStatusCode(httpStatusCode);
                if(errCode != null) {
                    if(PatternMatchUtils.simpleMatch(errCode, String.valueOf(httpStatusCode))) {
                        iList.add(lInfo);
                    }
                } else {
                    iList.add(lInfo);
                }
            }
        }
        map.put("alinkList", aList);
        map.put("areaList", arList);
        map.put("imageList", iList);

        return map;
    }
}
