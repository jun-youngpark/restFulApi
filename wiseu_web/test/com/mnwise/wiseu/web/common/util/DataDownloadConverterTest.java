package com.mnwise.wiseu.web.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

//import com.mnwise.wiseu.web.eservice.model.EserviceReportVo;

/**
 * Download Convert Test
 */
public class DataDownloadConverterTest extends TestCase {

    @Test
    public void testListToCsv() throws Exception {
        List list = new ArrayList();

        // EserviceReportVo eserviceReportVo = new EserviceReportVo();
        // eserviceReportVo.setCnt(201);
        // eserviceReportVo.setEcareNm("하하하하하하하");
        // eserviceReportVo.setDurationCnt(1234);
        // eserviceReportVo.setStartDt("20091012");
        // eserviceReportVo.setErrorDesc("에러설명");
        // eserviceReportVo.setSendDomain("mnwise.com");
        //
        // list.add(eserviceReportVo);
        //
        // eserviceReportVo = new EserviceReportVo();
        // eserviceReportVo.setCnt(101);
        // eserviceReportVo.setEcareNm("히히히히히");
        // eserviceReportVo.setDurationCnt(221);
        //
        // list.add(eserviceReportVo);

        String[] fields = new String[] {
            "ecareNm", "cnt", "startDt", "durationCnt", "errorDesc", "sendDomain"
        };
        String[] titles = new String[] {
            "이케어명", "cnt", "시작일자", "유효수신", "에러설명"
        };
        String[] encryptFields = new String[] {
            "sendDomain"
        };

        DataDownloadConverter dc = new DataDownloadConverter();
        dc.listToCsv(System.out, fields, titles, encryptFields, list);

        System.out.println("------------------------------");

        dc.listToCsv(System.out, null, titles, encryptFields, list);
    }
}
