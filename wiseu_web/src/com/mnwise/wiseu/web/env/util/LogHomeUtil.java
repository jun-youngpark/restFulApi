package com.mnwise.wiseu.web.env.util;

import java.util.ArrayList;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.common.util.PropertyUtil;

public class LogHomeUtil {
    private static ArrayList<String> logHomeDirList = new ArrayList<String>();

    static {
        String[] logPath = PropertyUtil.getProperty("log.download.dir", "").split(";");
        for(int i = 0; i < logPath.length; i++) {
            logHomeDirList.add(logPath[i]);
        }
    }

    public static boolean startsWithLogHomeDir(String filePath) throws Exception {
        String path = StringUtil.defaultString(filePath).replace("\\", "/");
        for(String logHomeDir : logHomeDirList) {
            if(path.startsWith(logHomeDir)) {
                return true;
            }
        }
        return false;
    }
}
