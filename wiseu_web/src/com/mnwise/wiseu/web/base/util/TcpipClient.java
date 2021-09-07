package com.mnwise.wiseu.web.base.util;

import static com.mnwise.common.util.StringUtil.CRLF;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.InetUtil.BASE64Encoder;
import com.mnwise.common.io.IOUtil;

public class TcpipClient extends Thread {
    private static final Logger log = LoggerFactory.getLogger(TcpipClient.class);

    private Socket sock;
    private BufferedReader brReader;
    private PrintWriter pwWriter;
    private BufferedOutputStream bos;

    /**
     * 생성자
     */
    public TcpipClient() {
    }

    /**
     * LBS에 접속하여 서버측 메시지를 수신
     *
     * @param strHost LBS가 설치된 시스템의 IP
     * @param iPort 포트
     */
    public String open(String strHost, int iPort) throws IOException {
        try {
            sock = new Socket(strHost, iPort);
            log.debug("Lts Connection IP:" + strHost + " Port:" + iPort);
            // InetAddress ipaddress = InetAddress.getByName(strHost);
            // InetSocketAddress socketaddress = new
            // InetSocketAddress(ipaddress, iPort);

            // sock = new Socket();
            // sock.connect(socketaddress, 6000); // 5초후에 Connection 을 끊는다

            sock.setReceiveBufferSize(10 * 1024);
            brReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            pwWriter = new PrintWriter(sock.getOutputStream(), true);
            bos = new BufferedOutputStream(sock.getOutputStream());

            return brReader.readLine();
        } catch(IOException ioe) {
            log.error("com.mnwise.lbs.TcpipClient.open(\"" + strHost + "\", " + iPort + "): " + ioe.toString());
            throw ioe;
        }
    }

    /**
     * @param sAID String
     * @throws IOException
     * @return String
     */
    public Socket getConn() {
        return this.sock;
    }

    /**
     * @return PrintWriter
     */
    public PrintWriter getPrintWriter() {
        return this.pwWriter;
    }

    /**
     * AID( adapter ID를 인자로 넘겨줌 ) 현재 AID 는 NVECAREMSG테이블의 ECARE_NO를 사용
     *
     * @param sAID
     * @return 리턴 메시지
     */
    public String setAID(String sAID) throws IOException {
        try {
            pwWriter.print("AID:" + sAID);
            pwWriter.print(CRLF);
            pwWriter.flush();

            String returnMessage = brReader.readLine();
            log.debug("[SND] AID:" + sAID);
            log.debug("[RCV] returnMessage:" + returnMessage);
            return returnMessage;
        } catch(IOException ioe) {
            log.error("com.mnwise.lbs.TcpipClient.setAID(\"" + sAID + "\"): " + ioe.toString());
            throw ioe;
        }
    }

    /**
     * 인자(value값)가 단일라인로 처리되는 경우 이 메소드 사용 ex) name: 최재철 ---> setArg("name", "최재철")
     *
     * @param sKey (키값)
     * @param sVal (value값)
     * @return 리턴 메시지
     */
    public String setArg(String sKey, String sVal) throws IOException {
        try {
            pwWriter.print(sKey + ":" + sVal);
            pwWriter.print(CRLF);
            pwWriter.flush();

            String returnMessage = brReader.readLine();
            log.debug("[SND] " + sKey + ":" + sVal);
            log.debug("[RCV] returnMessage:" + returnMessage);

            return returnMessage;
        } catch(IOException ioe) {
            log.error("com.mnwise.lbs.TcpipClient.setArg(\"" + sKey + "\", \"" + sVal + "\"): " + ioe.toString());
            throw ioe;
        }
    }

    /**
     * 인자(value값)이 여러줄일경우 아래의 메소드 사용 예) setData("content","안녕하세요\r\n방갑습니다.\r\n최재철입니다.)
     *
     * @param sKey
     * @param sVal
     * @return 리턴 메시지
     */
    public String setData(String sKey, String sVal) throws IOException {
        try {
            pwWriter.print(sKey);
            pwWriter.print(CRLF);
            pwWriter.flush();
            pwWriter.print(sVal);
            pwWriter.print(CRLF);
            pwWriter.print(CRLF);
            pwWriter.print(".");
            pwWriter.print(CRLF);
            pwWriter.flush();

            return brReader.readLine();
        } catch(IOException ioe) {
            log.error("com.mnwise.lbs.TcpipClient.setData(\"" + sKey + "\"...,): " + ioe.toString());
            throw ioe;
        }
    }

    /**
     * 파일을 Byte Array로 받아 로컬에 생성 add by jogaki, 2004/05/25
     *
     * @param sKey String
     * @param sByte byte[]
     * @throws IOException
     * @return boolean
     */
    public boolean setFile(String sKey, byte[] sByte) throws IOException {
        BufferedOutputStream out = null;

        try {
            log.info("File Length = " + sByte.length);
            out = new BufferedOutputStream(new FileOutputStream("c:/novitas/spool/ecarefile/" + sKey));
            out.write(sByte);
            return true;
        } catch(IOException ioe) {
            log.error("com.mnwise.lbs.TcpipClient.setData(\"" + sKey + "\"...,): " + ioe.toString());
            return false;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }

    /**
     * 파일 경로명을 받아 파일을 Stream으로 읽어들인 후 BASE64 Encoding 처리하여 전송 add by jogaki, 2004/05/25
     *
     * @param sKey String
     * @param sVal String
     * @throws IOException
     * @return String
     */
    public String setTransFile(String sKey, String sVal) throws IOException {
        BufferedInputStream in = null;

        try {
            log.info("sVal type : file path");
            pwWriter.print("File:" + sKey);
            pwWriter.print(CRLF);
            pwWriter.flush();

            in = new BufferedInputStream(new FileInputStream(sVal));
            BASE64Encoder b64e = new BASE64Encoder();
            b64e.encodeBuffer(in, pwWriter);

            pwWriter.print(CRLF);
            pwWriter.flush();
            pwWriter.print(".");
            pwWriter.print(CRLF);
            pwWriter.flush();

            return brReader.readLine();
        } catch(IOException ioe) {
            log.error(ioe.toString(), ioe);
            throw ioe;
        } finally {
            IOUtil.closeQuietly(in);
        }
    }

    /**
     * 파일을 Stream으로 받아 BASE64 Encoding 처리하여 전송 add by jogaki, 2004/05/25
     *
     * @param sKey String
     * @param sVal BufferedInputStream
     * @throws IOException
     * @return String
     */
    public String setTransFile(String sKey, BufferedInputStream sVal) throws IOException {
        try {
            log.info("sVal type : BufferedInputStream");
            pwWriter.print("File:" + sKey);
            pwWriter.print(CRLF);
            pwWriter.flush();

            BASE64Encoder b64e = new BASE64Encoder();
            b64e.encodeBuffer(sVal, pwWriter);

            pwWriter.print(CRLF);
            pwWriter.flush();
            pwWriter.print(".");
            pwWriter.print(CRLF);
            pwWriter.flush();

            return brReader.readLine();
        } catch(IOException ioe) {
            log.error(ioe.toString(), ioe);
            throw ioe;
        }
    }

    /**
     * 파일을 Byte Array로 받아 BufferedOutputStream으로 전송(미해결) add by jogaki, 2004/05/25
     *
     * @param sKey String
     * @param sVal byte[]
     * @throws IOException
     * @return String
     */
    public String setTransFile(String sKey, byte[] sVal) throws IOException {
        try {
            log.info("sVal type : Byte Array");
            pwWriter.print("File:" + sKey);
            pwWriter.print(CRLF);
            pwWriter.flush();

            bos.write(sVal);
            bos.flush();

            return brReader.readLine();
        } catch(IOException ioe) {
            log.error(ioe.toString(), ioe);
            throw ioe;
        }
    }

    /**
     * 파일을 Byte Array로 받아 BASE64 Encoding 처리하여 전송 add by jogaki, 2004/05/25
     *
     * @param sKey String
     * @param sVal byte[]
     * @throws IOException
     * @return String
     */
    public String setEncodingTransFile(String sKey, byte[] sVal) throws IOException {
        try {

            log.info("sVal type : Byte Array");
            pwWriter.print("File:" + sKey);
            pwWriter.print(CRLF);
            pwWriter.flush();

            BASE64Encoder b64e = new BASE64Encoder();
            b64e.encodeBuffer(sVal, pwWriter);
            // this.transFile(pwWriter, sVal);

            pwWriter.print(CRLF);
            pwWriter.flush();
            pwWriter.print(".");
            pwWriter.print(CRLF);
            pwWriter.flush();

            return brReader.readLine();
        } catch(IOException ioe) {
            log.error(ioe.toString(), ioe);
            throw ioe;
        }
    }

    /**
     * 호출을 마치고 실행함을 의미
     */
    public String commit() throws IOException {

        try {
            pwWriter.print("COMMIT");
            pwWriter.print(CRLF);
            pwWriter.flush();

            return brReader.readLine();
        } catch(IOException ioe) {
            log.error("com.mnwise.lbs.TcpipClient.commit(): " + ioe.toString());
            throw ioe;
        }
    }

    /**
     * 접속을 끊음
     */
    public void quit() /* throws IOException */ {
        try {
            pwWriter.print("QUIT");
            pwWriter.print(CRLF);
            pwWriter.flush();

            // } catch( IOException ioe ) {
            // throw ioe;
        } finally {
            IOUtil.closeQuietly(pwWriter);
            IOUtil.closeQuietly(brReader);
            IOUtil.closeQuietly(bos);
            IOUtil.closeQuietly(sock);
            sock = null;
            brReader = null;
            pwWriter = null;
            bos = null;
        }
    }

}
