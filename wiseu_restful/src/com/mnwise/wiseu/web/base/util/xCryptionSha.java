package com.mnwise.wiseu.web.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 20150819 암호화 복잡도 적용을 위해 sha 알고리즘 추가 sha 암호화 및 sha (salt) 암호화 방식 제공
 */
public class xCryptionSha {
    private static final Logger log = LoggerFactory.getLogger(xCryptionSha.class);

    public static String encrypt_sha256(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error(null, e);
        }
        return generatedPassword;
    }

    public static String encryptWithSalt_sha256(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error(null, e);
        }
        return generatedPassword;
    }

    public static String encrypt_sha384(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error(null, e);
        }
        return generatedPassword;
    }

    public static String encryptWithSalt_sha384(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error(null, e);
        }
        return generatedPassword;
    }

    public static String encrypt_sha512(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error(null, e);
        }
        return generatedPassword;
    }

    public static String encryptWithSalt_sha512(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error(null, e);
        }
        return generatedPassword;
    }

    // salt Key 생성
    public static String getSalt() throws NoSuchAlgorithmException {
        return encrypt_sha256(getTimeBase()).substring(0, 20);
    }

    // salt Key 생성시 참조 메서드
    public static String getTimeBase() {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddhhmmssSSSSSS");
        Date date = new Date();
        String s = simpledateformat.format(date);
        return s;
    }

    public static boolean confirmWithSalt_sha256(String password, String salt, String HashPassword) {
        return encryptWithSalt_sha256(password, salt).equals(HashPassword);
    }

    public static boolean confirm_sha256(String password, String HashPassword) {
        return encrypt_sha256(password).equals(HashPassword);
    }

    public static boolean confirmWithSalt_sha384(String password, String salt, String HashPassword) {
        return encryptWithSalt_sha384(password, salt).equals(HashPassword);
    }

    public static boolean confirm_sha384(String password, String HashPassword) {
        return encrypt_sha384(password).equals(HashPassword);
    }

    public static boolean confirmWithSalt_sha512(String password, String salt, String HashPassword) {
        return encryptWithSalt_sha512(password, salt).equals(HashPassword);
    }

    public static boolean confirm_sha512(String password, String HashPassword) {
        return encrypt_sha512(password).equals(HashPassword);
    }

}
