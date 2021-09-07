package com.mnwise.wiseu.web.common.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.InetUtil.EnDec;
import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.sun.mail.util.BASE64DecoderStream;

public class ReadingMime {
    private static final Logger log = LoggerFactory.getLogger(ReadingMime.class);

    private String filePath;
    private long startOffset;
    @SuppressWarnings("unused")
    private long endOffset;
    private String mime;
    private byte[] buf;
    @SuppressWarnings("unused")
    private int level = 0;
    private Map<String, Object> attachFiles;
    private StringBuffer m_sbMailContent = null;
    private String subject;
    private String from;
    private String to;
    @SuppressWarnings("unused")
    private String attachFileNm;
    @SuppressWarnings("unused")
    private boolean attachView = false;

    public ReadingMime(String filePath, long startOffset, long endOffset, String attachFileNm) {
        this.filePath = filePath;
        this.attachFileNm = attachFileNm;
        this.startOffset = startOffset;
        this.endOffset = endOffset;

        int offSetLength = (endOffset - startOffset < 0) ? 0 : Math.max((int) (endOffset - startOffset), 0);

        buf = new byte[offSetLength];
        m_sbMailContent = new StringBuffer();
        attachFiles = new HashMap<String, Object>();

        if(attachFileNm != null) {
            attachView = true;
        }
    }

    public ReadingMime(String filePath, String attachFileNm) throws Exception {
        this.filePath = filePath;
        this.attachFileNm = attachFileNm;
        this.startOffset = 0;

        if(StringUtil.isBlank(filePath)) {
            throw new Exception("file path is blank.");
        }

        try {
            File file = new File(filePath);
            int tempLength = (file == null) ? 0 : Math.max((int) (file.length()), 0);
            buf = new byte[tempLength];
        } catch(Exception e) {
            throw new Exception("File not exist. [filePath=" + filePath + "]");
        }

        m_sbMailContent = new StringBuffer();
        attachFiles = new HashMap<String, Object>();
        if(attachFileNm != null) {
            attachView = true;
        }
    }

    public String getSubject() {
        return subject;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Map<String, Object> getAttachFiles() {
        return attachFiles;
    }

    public void mimeReading() throws Exception {
        File f = new File(filePath);

        RandomAccessFile raf = new RandomAccessFile(f, "r");
        raf.seek(startOffset);
        raf.read(buf);

        mime = new String(buf);
        IOUtil.closeQuietly(raf);
    }

    public void mimeParse() throws Exception {
        byte[] buf = mime.getBytes();
        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()), new ByteArrayInputStream(buf));

        dumpPart(mimeMessage);
    }

    public MimeMessage getMimeMessage(String host) throws Exception {
        byte[] buf = mime.getBytes();

        Properties prop = new Properties();
        prop.put("mail.stmp.host", host);
        prop.put("mail.debug", "true");

        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(prop), new ByteArrayInputStream(buf));

        return mimeMessage;
    }

    @SuppressWarnings("deprecation")
    public void dumpPart(Part p) throws Exception {
        if(p instanceof Message) {
            dumpEnvelope((Message) p);
        }

        String boundary = p.getContentType();
        log.debug("boundary : " + boundary);
        boolean bUnkownCharset = (boundary.toLowerCase().indexOf("charset") != -1) ? false : true;

        // charset 에 대한 선언이 charset= 또는 charset=null 로 오는경우에 임의로 euc-kr로 지정.
        if(!bUnkownCharset) {
            String[] saContentType = boundary.split(";");
            String strCharset = saContentType[1].substring(saContentType[1].indexOf("=") + 1);
            if(strCharset.trim().equals("") || strCharset.trim().equalsIgnoreCase("null")) {
                p.setHeader("CONTENT-TYPE", new StringBuffer().append(saContentType[0]).append(";").append("charset=euc-kr").toString());
            }
        }

        String disp = StringUtil.defaultString(p.getDisposition());

        if(p.isMimeType("text/plain") && !disp.equalsIgnoreCase(Part.ATTACHMENT) && !disp.equalsIgnoreCase(Part.INLINE)) {
            String strTemp = null;
            try {
                strTemp = (String) p.getContent();
            } catch(Exception e) {
                String strLine = null;
                DataInputStream in = new DataInputStream(p.getInputStream());
                while(null != (strLine = in.readLine())) {
                    log.debug(new String(strLine.getBytes("8859_1"), "EUC-KR"));
                }

                IOUtil.closeQuietly(in);
            }

            if(bUnkownCharset) {
                m_sbMailContent.append(new String(strTemp.getBytes("8859_1"), "EUC-KR"));
            } else {
                m_sbMailContent.append(strTemp);
            }
        } else if(p.isMimeType("multipart/alternative")) {
            Object objTemp = p.getContent();
            Multipart mp = (Multipart) objTemp;
            level++;
            int count = mp.getCount();
            if(count == 1) {
                dumpPart(mp.getBodyPart(0));
            } else {
                for(int i = 0; i < count; i++) {
                    BodyPart bp = mp.getBodyPart(i);
                    if(!bp.isMimeType("text/plain")) {
                        dumpPart(mp.getBodyPart(i));
                    }
                }
            }
            level--;
        } else if(p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            level++;
            int count = mp.getCount();
            for(int i = 0; i < count; i++)
                dumpPart(mp.getBodyPart(i));
            level--;
        } else if(p.isMimeType("message/rfc822")) {
            level++;
            dumpPart((Part) p.getContent());
            level--;
        } else if(!p.isMimeType("multipart/*")) {
            if(disp.equals("") || disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE)) {
                // text/plain을 제외한 본문과 첨부 파일 처리
                try {
                    String attacheFile = p.getFileName();

                    // TODO: 마임의 타입은 너무나 많다.. 단순히 본문? 첨부파일? 을 구분하더라도 이미지가
                    // 본문에 첨부형태로 올경우
                    // 상당히 애매하다. 그렇기 때문에 우선은 파일명이 있으면 무조건 첨부파일 링크를 만들어 보여준다.
                    // 단, 본문내용에 이미지 첨부일 경우에는 첨부형태로도 보여주고 파일을 저장후 본문에 링크를 걸어서
                    // 본문에도 보여주도록 처리함.
                    if(attacheFile == null && p.isMimeType("text/html")) {
                        String strTemp = (String) p.getContent();
                        if(bUnkownCharset) {
                            m_sbMailContent.append(new String(strTemp.getBytes("8859_1"), "EUC-KR")).append("\t\n");
                        } else {
                            m_sbMailContent.append(strTemp).append("\t\n");
                        }
                    } else if(attacheFile != null) {
                        if(isEncoded(attacheFile)) {
                            // attacheFile =
                            // EnDec.decodeHeader(attacheFile).trim();
                            attacheFile = MimeUtility.decodeText(attacheFile);
                        } else {
                            attacheFile = (new String(p.getFileName().getBytes("8859_1"), "EUC-KR"));
                        }

                        try {
                            Object obj = p.getContent();

                            if(obj instanceof String) {
                                String str = (String) obj;

                                if(bUnkownCharset) {
                                    int nameIndex = boundary.indexOf("name=\"=?");
                                    if(nameIndex > -1) {
                                        int beginIndex = nameIndex + 8;
                                        String charset = boundary.substring(beginIndex, boundary.indexOf("?", beginIndex));
                                        attachFiles.put(attacheFile, new String(str.getBytes("8859_1"), charset) + "\r\n");
                                    } else {
                                        attachFiles.put(attacheFile, new String(str.getBytes("8859_1"), "EUC-KR") + "\r\n");
                                    }
                                } else {
                                    attachFiles.put(attacheFile, str);
                                }
                            } else if(obj instanceof BASE64DecoderStream) {
                                BASE64DecoderStream in = (BASE64DecoderStream) obj;
                                attachFiles.put(attacheFile, IOUtil.toByteArray(in));
                                IOUtil.closeQuietly(in);
                            }
                        } catch(Exception ex) {
                            log.debug("Attach File error. (1)");
                        }
                    }
                } catch(Exception ex) {
                    log.debug("MimeType error. (1)");
                }
            } // end if strDisposition
        }
    }

    /**
     * 소스라인이 인코딩되어있는 문자열인지 체크한다.<br/>
     * Case 1> '=?' 이 꼭 맨 처음에 오지 않는 경우도 있다.<br/>
     * 예를들면 헤더에 Subject: TEST =?EUC-KR?B?xde9usau?=
     *
     * @param src 입력 문자열.
     * @return 결과 문자열.
     */
    final private static boolean isEncoded(String src) {
        String temp = StringUtil.defaultString(src);

        if(temp.length() > 6) {
            int x1 = temp.indexOf("=?");
            int x2 = temp.indexOf("?=");
            if(x1 > -1 && x2 > -1 && x2 > x1) {
                return true;
            }
        }

        return false;
    }

    public void dumpEnvelope(Message m) throws Exception {

        log.debug("-------------------------------------------------------------------------------------------");
        Address[] a;
        String strFrom = null;
        String strTemp = null;
        // FROM
        try {
            if((a = m.getFrom()) != null) {
                for(int j = 0; j < a.length; j++) {
                    strTemp = a[j].toString();
                    if(strFrom == null) {
                        // strFrom = strTemp.startsWith("=?") ?
                        // EnDec.decodeHeader(strTemp) : strTemp;
                        strFrom = strTemp.startsWith("=?") ? EnDec.decodeHeader(strTemp) : (new String(strTemp.getBytes("8859_1"), "EUC-KR"));
                    } else {
                        // strTemp = strTemp.startsWith("=?") ?
                        // EnDec.decodeHeader(strTemp) : strTemp;
                        strTemp = strTemp.startsWith("=?") ? EnDec.decodeHeader(strTemp) : (new String(strTemp.getBytes("8859_1"), "EUC-KR"));
                        strFrom = strFrom + ";" + strTemp;
                    }
                }
                log.debug("FROM: " + strFrom);
                from = strFrom;
            }
        } catch(Exception e) {
            log.debug("FROM message error. (1)");
        }

        // TO
        String strTo = null;
        try {
            if((a = m.getRecipients(Message.RecipientType.TO)) != null) {

                for(int j = 0; j < a.length; j++) {
                    strTemp = a[j].toString();
                    if(strTo == null) {
                        strTo = strTemp.startsWith("=?") ? EnDec.decodeHeader(strTemp) : (new String(strTemp.getBytes("8859_1"), "EUC-KR"));
                        // strTo = strTemp.startsWith("=?") ?
                        // EnDec.decodeHeader(strTemp) : strTemp;
                    } else {
                        strTemp = strTemp.startsWith("=?") ? EnDec.decodeHeader(strTemp) : (new String(strTemp.getBytes("8859_1"), "EUC-KR"));
                        // strTemp = strTemp.startsWith("=?") ?
                        // EnDec.decodeHeader(strTemp) : strTemp;
                        strTo = strTo + ";" + strTemp;
                    }
                }
                log.debug("TO: " + strTo);
                to = strTo;
            }
        } catch(Exception e) {
            log.debug("TO message error. (1)");
        }

        // SUBJECT
        // strTemp = m.getSubject();
        //
        // log.debug("SUBJECT: " + strTemp);
        // strTemp = new String(strTemp.getBytes("8859_1"),"EUC-KR");
        // log.debug("SUBJECT: " + strTemp);

        String[] strArrTemp = m.getHeader("Subject");
        strTemp = (strArrTemp != null) ? strArrTemp[0] : "";
        // strTemp = strTemp.startsWith("=?") ? EnDec.decodeHeader(strTemp) :
        // strTemp;
        strTemp = strTemp.startsWith("=?") ? EnDec.decodeHeader(strTemp) : (new String(strTemp.getBytes("8859_1"), "EUC-KR"));
        log.debug("SUBJECT: " + strTemp);

        // 20100419 미리보기시 제목이 길어질 경우 "\r\n " 이 삽입되어 중간에 삽입하지 않은 띄어쓰기가 나타나는 현상 해결
        subject = strTemp.replaceAll("\r\n ", "");

        // DATE
        Date d = m.getSentDate();
        log.debug("SendDate: " + (d != null ? d.toString() : "UNKNOWN"));

        // FLAGS
        Flags flags = m.getFlags();
        StringBuffer sb = new StringBuffer();
        Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

        boolean first = true;
        for(int i = 0; i < sf.length; i++) {
            String s;
            Flags.Flag f = sf[i];
            if(f == Flags.Flag.ANSWERED)
                s = "\\Answered";
            else if(f == Flags.Flag.DELETED)
                s = "\\Deleted";
            else if(f == Flags.Flag.DRAFT)
                s = "\\Draft";
            else if(f == Flags.Flag.FLAGGED)
                s = "\\Flagged";
            else if(f == Flags.Flag.RECENT)
                s = "\\Recent";
            else if(f == Flags.Flag.SEEN)
                s = "\\Seen";
            else
                continue; // skip it
            if(first)
                first = false;
            else
                sb.append(' ');
            sb.append(s);
        }

        String[] uf = flags.getUserFlags(); // get the user flag strings
        for(int i = 0; i < uf.length; i++) {
            if(first)
                first = false;
            else
                sb.append(' ');
            sb.append(uf[i]);
        }
        log.debug("FLAGS: " + sb.toString());

        // X-MAILER
        String[] hdrs = m.getHeader("X-Mailer");
        if(hdrs != null)
            log.debug("X-Mailer: " + hdrs[0]);
        else
            log.debug("X-Mailer NOT available");

        String contentType = m.getContentType();
        try {
            log.debug("CONTENT-TYPE: " + (new ContentType(contentType)).toString());
        } catch(ParseException pex) {
            log.debug("BAD CONTENT-TYPE: " + contentType);
        }
    }

    public String getHeader(Part p, String strName) throws MessagingException {
        String astrHeader[] = null;

        astrHeader = p.getHeader(strName);
        return(null != astrHeader ? astrHeader[0] : "");
    }

    public String getMime() {
        return mime;
    }

    public String getContent() {
        return m_sbMailContent.toString();
    }

}
