package com.mnwise.wiseu.web.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.util.DateUtil;

/**
 * CSV 컨버터 - Object 형태의 데이타를 csv, excel 형태로 변환한다.
 */
public class DataDownloadConverter {
    private static final Logger log = LoggerFactory.getLogger(DataDownloadConverter.class);

    /**
     * CSV 다운로드
     *
     * @param response
     * @param fileName
     * @param fields
     * @param titles
     * @param encryptFields
     * @param list
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void csvDownload(HttpServletResponse response, String fileName, String[] fields, String[] titles, String[] encryptFields, List list) throws IOException {
        fileName = fileName + "_" + DateUtil.dateToString("yyyyMMdd", new Date()) + ".csv";

        response.setContentType("application/download");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        listToCsv(response.getOutputStream(), fields, titles, encryptFields, list);
    }

    /**
     * CSV 웹 다운로드
     *
     * @param response
     * @param fileName
     * @param fields
     * @param titles
     * @param list
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void csvDownload(HttpServletResponse response, String fileName, String[] fields, String[] titles, List list) throws IOException {
        csvDownload(response, fileName, fields, titles, null, list);
    }

    /**
     * @param response
     * @param fileName
     * @param fields
     * @param list
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void csvDownload(HttpServletResponse response, String fileName, String[] fields, List list) throws IOException {
        csvDownload(response, fileName, fields, null, null, list);
    }

    /**
     * List 객체를 csv 타입으로 출력한다. fields 목록을 넘기면 해당 목록에 포함되는 컬럼을 배열순으로 출력한다.
     *
     * @param out OutStream 객체
     * @param fields 출력할 필드 목록이다. 전체를 출력한다면 null을 넘긴다.
     * @param titles 필드별 타이틀명이다. 타이틀명이 필요없다면 null을 넘긴다.
     * @param encryptFields 개인정보 유출 보호 대상컬럼
     * @param list
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void listToCsv(OutputStream out, String[] fields, String[] titles, String[] encryptFields, List list) throws IOException {
        if(list == null || list.size() == 0) {
            return;
        }

        Object obj;
        for(int i = 0; i < list.size(); i++) {
            obj = list.get(i);
            Field[] field = ReflectionUtil.getAllDeclaredFields(obj.getClass());
            AccessibleObject.setAccessible(field, true);

            if(i == 0) {
                printTitles(out, titles);
            }

            if(fields != null && fields.length > 0) {
                printCsvSelectField(out, fields, obj, field, encryptFields);
            } else {
                printCsvAllField(out, obj, field, encryptFields);
            }
            out.write(printObjectToBytes("\n"));
        }

        out.flush();
    }

    /**
     * 전체 컬럼 출력
     *
     * @param out
     * @param obj
     * @param field
     */
    private void printCsvAllField(OutputStream out, Object obj, Field[] field, String[] encryptFields) {
        Object value;
        for(int j = 0, k = 0; j < field.length; j++) {
            try {
                if(k > 0) {
                    out.write(printObjectToBytes("\t"));
                }
                value = ReflectionUtil.getFieldValue(field[j], obj);

                // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에 '''를
                // 붙여준다.
                if(String.valueOf(value).trim().startsWith("=")) {
                    value = "'" + value;
                }

                if(encryptFields != null && encryptFields.length > 0) {
                    printEncryptFields(out, field, encryptFields, value, j);
                } else {
                    out.write(printObjectToBytes(value));
                }
                k++;
            } catch(Exception e) {
                log.error(null, e);
                continue;
            }
        }
    }

    /**
     * 선택된 컬럼만 출력
     *
     * @param out 출력 스트림
     * @param fields 출력할 필드명
     * @param obj 데이타가 담긴 객체
     * @param field 필드 정보
     * @throws IOException
     */
    private void printCsvSelectField(OutputStream out, String[] fields, Object obj, Field[] field, String[] encryptFields) throws IOException {
        Object value;
        for(int j = 0, k = 0; j < fields.length; j++) {
            for(int l = 0; l < field.length; l++) {
                if(fields[j].equals(field[l].getName())) {
                    if(k > 0) {
                        out.write(printObjectToBytes("\t"));
                    }
                    value = ReflectionUtil.getFieldValue(field[l], obj);

                    // 셀 값이 '=' 문자로 시작하는 경우 엑셀을 실행하면 해당 셀의 함수가 자동 실행되는 취약점 때문에
                    // '''를 붙여준다.
                    if(String.valueOf(value).trim().startsWith("=")) {
                        value = "'" + value;
                    }

                    if(encryptFields != null && encryptFields.length > 0) {
                        printEncryptFields(out, field, encryptFields, value, l);
                    } else {
                        out.write(printObjectToBytes(value));
                    }
                    k++;
                }
            }
        }
    }

    private void printEncryptFields(OutputStream out, Field[] field, String[] encryptFields, Object value, int l) throws IOException {
        boolean isEncrypt = false;

        for(int i = 0; i < encryptFields.length; i++) {
            if(encryptFields[i].equals(field[l].getName())) {
                isEncrypt = true;
                break;
            }
        }
        if(isEncrypt) {
            /**
             * 개인정보 보호로 암호화하여 - 처리할경우 out.write("-".getBytes());로 변경한다
             */
            // out.write("-".getBytes());
            out.write(printObjectToBytes(value));
        } else {
            out.write(printObjectToBytes(value));
        }
    }

    private byte[] printObjectToBytes(Object value) {
        if(value == null) {
            return "".getBytes();
        }
        byte[] val = null;
        try {
            val = value.toString().getBytes("UTF-16LE");
        } catch(UnsupportedEncodingException uee) {
            val = value.toString().getBytes();
        }

        return val;
    }

    /**
     * 컬럼 타이틀 출력
     *
     * @param out OutputStream 객체
     * @param titles 타이틀명
     * @throws IOException
     */
    private void printTitles(OutputStream out, String[] titles) throws IOException {
        if(titles != null) {
            StringBuilder sb = new StringBuilder();
            byte[] byteVal = null;
            for(int j = 0; j < titles.length; j++) {
                if(j > 0) {
                    sb.append("\t");
                }
                sb.append(titles[j]);
            }
            sb.append("\n");
            try {
                byteVal = sb.toString().getBytes("UTF-16LE");
                out.write(0xFF); // UTF-16LE BOM
                out.write(0xFE);
            } catch(UnsupportedEncodingException uee) {
                byteVal = sb.toString().getBytes();
            }
            out.write(byteVal);
            ;
        }
    }
}
