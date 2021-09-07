package com.mnwise.wiseu.web.segment.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UploadSegVo {
    private String importedFilePath;
    private String[] headers;
    private String[] firstData;
    private String key;
    private String[] keys;
    private String[] keysName;
    private int totalLine;
    private int cnum;
    private final Map<String, String> map;

    private String inMetaData;
    private String delimiter;
    private String vmail;
    private String vphone;

    private SegmentVo segmentVo;
    private boolean largeUpload;

    // 20100303 한순모 한글처리때문에 추가
    private String language = null;
    private String tabUse;

    private String errorMsg;


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public UploadSegVo() {
        // 한글과 영문이로만 구성하도록 한다. db에서 가져오도록 수정 하는 것이 좋으나 시간 관계상 이렇게 처리한다.
        // 등록 순서대로 가져오기 위해 LinkedHashMap 사용
        map = new LinkedHashMap<String, String>();
        map.put("아이디", "CUSTOMER_ID");
        map.put("이름", "CUSTOMER_NM");
        map.put("이메일", "CUSTOMER_EMAIL");
        // map.put("가입년","SLOT");
        // map.put("가입월","SLOT");
        // map.put("가입일","SLOT");
        map.put("기타", "SLOT");
        map.put("고객휴대폰", "CUSTOMER_TEL");
        map.put("팩스", "CUSTOMER_FAX");
        map.put("로그부가정보1", "CUSTOMER_SLOT1");
        map.put("로그부가정보2", "CUSTOMER_SLOT2");
        map.put("발신이름", "SENDER_NM");
        map.put("발신메일", "SENDER_EMAIL");
        map.put("콜백번호", "CALL_BACK");
        map.put("반송메일", "RETMAIL_RECEIVER");
        map.put("세그", "SEG");
        map.put("전문", "SENTENCE");
        segmentVo = new SegmentVo();
    }

    public UploadSegVo(String language) {
        map = new LinkedHashMap<String, String>();
        this.language = language;
        if(language.equals("ko")) {
            map.put("아이디", "CUSTOMER_ID");
            map.put("이름", "CUSTOMER_NM");
            map.put("이메일", "CUSTOMER_EMAIL");
            // map.put("가입년","SLOT");
            // map.put("가입월","SLOT");
            // map.put("가입일","SLOT");
            map.put("기타", "SLOT");
            map.put("고객휴대폰", "CUSTOMER_TEL");
            map.put("팩스", "CUSTOMER_FAX");
            map.put("로그부가정보1", "CUSTOMER_SLOT1");
            map.put("로그부가정보2", "CUSTOMER_SLOT2");
            map.put("발신이름", "SENDER_NM");
            map.put("발신메일", "SENDER_EMAIL");
            map.put("콜백번호", "CALL_BACK");
            map.put("반송메일", "RETMAIL_RECEIVER");
            // 현재 사용안함(추후 사용 예정)
            // map.put("세그", "SEG");
            // map.put("전문", "SENTENCE");
        } else {
            // 한글과 영문이로만 구성하도록 한다.
            // 등록 순서대로 가져오기 위해 LinkedHashMap 사용
            map.put("ID", "CUSTOMER_ID");
            map.put("NAME", "CUSTOMER_NM");
            map.put("EMAIL", "CUSTOMER_EMAIL");
            // map.put("가입년","SLOT");
            // map.put("가입월","SLOT");
            // map.put("가입일","SLOT");
            map.put("etc", "SLOT");
            map.put("PHONE", "CUSTOMER_TEL");
            map.put("FAX", "CUSTOMER_FAX");
            map.put("Additional Info1", "CUSTOMER_SLOT1");
            map.put("Additional Info2", "CUSTOMER_SLOT2");
            map.put("Sender name", "SENDER_NM");
            map.put("Sender email", "SENDER_EMAIL");
            map.put("Callback", "CALL_BACK");
            map.put("Return email", "RETMAIL_RECEIVER");
            // 현재 사용안함(추후 사용 예정)
            // map.put("세그", "SEG");
            // map.put("전문", "SENTENCE");

        }
        segmentVo = new SegmentVo();
    }

    public String getInMetaData() {
        return inMetaData;
    }

    public void setInMetaData(String inMetaData) {
        this.inMetaData = inMetaData;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getImportedFilePath() {
        return importedFilePath;
    }

    public void setImportedFilePath(String importedFilePath) {
        this.importedFilePath = importedFilePath;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public int getColumnSize() {
        return this.headers.length;
    }

    public String getDefaultColumnName() {
        if(map.get(this.key) == null)
            return "";
        return (String) map.get(key);
    }

    public String getSelectOption() {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name='keys' class='form-control form-control-sm form-control-inline' onchange='changeField(" + cnum + ")';>");
        if(headers == null) {
            if(language != null && !language.equals("ko"))
                sb.append("<option value=''>choose</option>");
            else
                sb.append("<option value=''>선택</option>");
        }

        Set<String> set = map.keySet();
        Object[] mapKeys = set.toArray();

        /** 씨티은행 커스터마이징 ******************************************************/
        // 헤더정보가 포함될 때 select box에서 selected 될 값이 무엇인지 확인한다.
        String selectField = "";
        for(int j = 0; j < mapKeys.length; j++) {
            String selkey = (String) mapKeys[j];
            if(selkey.equals(this.key)) {
                selectField = selkey;
            }
        }

        // 헤더가 포함되었을때, select 될 값이 없을 경우 자동으로 기타를 selected 한다.
        if(headers != null && selectField.equals("")) {
            selectField = "기타";
        }
        for(int i = 0; i < mapKeys.length; i++) {
            String key = (String) mapKeys[i];
            String selected = "";

            if(key.equals(selectField)) {
                selected = " selected";
            }
            sb.append("<option value='" + (String) map.get(key) + "'" + selected + ">" + key + "</option>");
        }

        sb.append("</select>");
        /**
         * 씨티은행 커스터마이징 END
         ******************************************************/
        /**
         * 기존소스 for (int i = 0; i < mapKeys.length; i++) { String key = (String) mapKeys[i]; String selected = ""; if (key.equals(this.key)) { selected = " selected"; } else { // 기타 옵션인 경우 if
         * (this.key.equals("")) { selected = " selected"; } else { selected = ""; } } sb.append("<option value='" + (String) map.get(key) + "'" + selected + ">" + key + "</option>"); }
         * sb.append("</select>");
         */
        return sb.toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstDataByColumn() {
        return firstData[cnum];
    }

    public String[] getFirstData() {
        return this.firstData;
    }

    public void setFirstData(String[] firstData) {
        this.firstData = firstData;
    }

    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public int getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(int totalLine) {
        this.totalLine = totalLine;
    }

    public SegmentVo getSegmentVo() {
        return segmentVo;
    }

    public void setSegmentVo(SegmentVo segmentVo) {
        this.segmentVo = segmentVo;
    }

    public String[] getKeysName() {
        return keysName;
    }

    public void setKeysName(String[] keysName) {
        this.keysName = keysName;
    }

    public String getVmail() {
        return vmail;
    }

    public void setVmail(String vmail) {
        this.vmail = vmail;
    }

    public String getVphone() {
        return vphone;
    }

    public void setVphone(String vphone) {
        this.vphone = vphone;
    }

    public boolean isLargeUpload() {
        return largeUpload;
    }

    public void setLargeUpload(boolean largeUpload) {
        this.largeUpload = largeUpload;
    }

    public String getTabUse() {
        return tabUse;
    }

    public void setTabUse(String tabUse) {
        this.tabUse = tabUse;
    }

}
