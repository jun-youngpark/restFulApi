package com.mnwise.wiseu.web.common.util;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.mnwise.common.InetUtil.EnDec;
import com.mnwise.mts.mail.util.SimpleMailSender;

public class AuthUtil {
    private String authToken = RandomStringUtils.randomAlphanumeric(4);

    private String dnsServer = PropertyUtil.getProperty("dns.server");
    private String smtpHelo = PropertyUtil.getProperty("smtp.helo");
    private String smtpMailFrom = PropertyUtil.getProperty("smtp.mailfrom");
    private String bindAddress = PropertyUtil.getProperty("bind.address");

    public Map<String, String> sendAuthMail(String receiveMail) throws Exception {
        Map<String, String> retMap = new HashMap<>();
        SimpleMailSender sender = new SimpleMailSender();
        sender.setDnsHost(dnsServer);
        sender.setHelo(smtpHelo);
        sender.setLocalIp(InetAddress.getLocalHost().getHostAddress());
        sender.setMailFrom(smtpMailFrom);
        sender.setRcptTo(receiveMail);
        sender.setBindAddress(bindAddress);
        sender.setTimeoutSec(60);
        sender.sendMessage(getMailContent(smtpMailFrom ,receiveMail, "로그인 이메일 인증 메일"));
        if(sender.getRtnCode().equals("250")) { //성공
            retMap.put("msg", sender.getRtnMsg());
            retMap.put("result", "S");
        } else {
            retMap.put("msg", sender.getRtnMsg());
            retMap.put("result", "F");
        }
        sender.close();
        return retMap;

    }

    public String getRendonToken() {
        return this.authToken;
    }

    private String getMailContent(String from, String to, String subject) {
        String CRLF = "\r\n";
        subject = EnDec.encodeHeader(subject, "utf-8");

        StringBuffer msg = new StringBuffer();
        msg.append("Message-ID: <1258943539048.1@mnwise.com>            ".trim() + CRLF);
        msg.append("From: <" + from + ">                                ".trim() + CRLF);
        msg.append("To: <" + to + ">                                    ".trim() + CRLF);
        msg.append("Subject: " + subject + "                            ".trim() + CRLF);
        msg.append("MIME-Version: 1.0                                   ".trim() + CRLF);
        msg.append("Content-Type: text/html; charset=UTF-8              ".trim() + CRLF);
        msg.append("Content-Transfer-Encoding: 8bit                     ".trim() + CRLF + CRLF);
        msg.append("<html>                                              ".trim() + CRLF);
        msg.append("<head>                                              ".trim() + CRLF);
        msg.append("<title> wiseU 로그인 인증 토큰 전달 </title>                ".trim() + CRLF);
        msg.append("</head>                                             ".trim() + CRLF);
        msg.append("<html>                                              ".trim() + CRLF);
        msg.append("<body>                                              ".trim() + CRLF);
        msg.append("<h1>[이메일 인증]</h1><p> 아래 token을 추가로 입력 </p>        ".trim() + CRLF);
        msg.append("<span style='font-family:consolas;font-size: 15px;'> ".trim() + CRLF);
        msg.append(authToken + CRLF);
        msg.append("</body>                                             ".trim() + CRLF);
        msg.append("</html>                                             ".trim() + CRLF);
        //BASE64Encoder enc = new BASE64Encoder();

        return msg.toString();
    }

    public String getValidTime() {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -5);
        return sdformat.format(cal.getTime());
    }

}
