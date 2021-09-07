package com.mnwise.wiseu.web.common.mockup;

import java.util.HashMap;

import com.mnwise.ASE.agent.util.TextReader;

public class ScrPropertiesMockup {
    private HashMap<String, Object> map;

    public ScrPropertiesMockup() {
        map = new HashMap<>();
    }

    public void addResource(Object obj) {
    }

    public void clear() {
    }

    public void dump() {
    }

    public void freeAllResource() {
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public Object get(String key, TextReader tr) {
        return new Object();
    }

    public String getCurrentDate(String format) {
        return "";
    }

    public String getEncryptedString(String key) {
        return "";
    }

    public String getFieldName(String semanticDesc) {
        return "";
    }

    public int getInt(String name) {
        return 0;
    }

    public long getLong(String name) {
        return 0L;
    }

    public boolean getTrace() {
        return false;
    }

    public int getUniqueIndex() {
        return 0;
    }

    public String getURLEncodedString(String key) {
        return "";
    }

    public void put(Object key, Object value) {
    }

    public void recovery() {
    }

    public void remove(Object key) {
    }

    public String safe_getString(String key) {
        return "";
    }

    public void setRecovery(String line) {
    }

    public void setTrace() {
    }

    public void sleep(int sec) {
    }
}
