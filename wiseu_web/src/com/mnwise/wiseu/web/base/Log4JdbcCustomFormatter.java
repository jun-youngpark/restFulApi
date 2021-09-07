package com.mnwise.wiseu.web.base;

import java.io.BufferedReader;
import java.io.StringReader;

import net.sf.log4jdbc.Slf4jSpyLogDelegator;
import net.sf.log4jdbc.Spy;
import net.sf.log4jdbc.tools.LoggingType;

public class Log4JdbcCustomFormatter extends Slf4jSpyLogDelegator {
    private LoggingType loggingType = LoggingType.DISABLED;
    private String margin = "";
    private String sqlPrefix = "";

    public Log4JdbcCustomFormatter() {
    }

    public LoggingType getLoggingType() {
        return loggingType;
    }

    public void setLoggingType(LoggingType loggingType) {
        this.loggingType = loggingType;
    }

    public String getSqlPrefix() {
        return sqlPrefix;
    }

    public void setSqlPrefix(String sqlPrefix) {
        this.sqlPrefix = sqlPrefix;
    }

    public int getMargin() {
        return margin.length();
    }

    public void setMargin(int n) {
        margin = String.format("%1$" + n + "s", "");
    }

    @Override
    public String sqlOccured(Spy spy, String methodCall, String rawSql) {
        try {
            StringBuffer sql = new StringBuffer("\n        ");
            BufferedReader in = new BufferedReader(new StringReader(rawSql));
            String lineBuf = null;
            while((lineBuf = in.readLine()) != null) {
                if(lineBuf.trim().length() > 0) {
                    sql.append(lineBuf).append("\n");
                }
            }

            getSqlOnlyLogger().info(sql.toString());

            return sql.toString();
        } catch(Exception e) {
            return rawSql;
        }
    }

    @Override
    public String sqlOccured(Spy spy, String methodCall, String[] sqls) {
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < sqls.length; i++) {
            sql.append(sqlOccured(spy, methodCall, sqls[i])).append(String.format("%n", new Object[0]));
        }
        return sql.toString();
    }
}
