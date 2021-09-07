package com.mnwise.wiseu.web.base;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.util.StringUtil;

@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class MyBatisInterceptor implements Interceptor {
    final static private Logger log = LoggerFactory.getLogger(MyBatisInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String id = null;

        try {
            StatementHandler handler = (StatementHandler)invocation.getTarget();
            MetaObject metaObj = SystemMetaObject.forObject(handler);
            MappedStatement mstmt = (MappedStatement) metaObj.getValue("delegate.mappedStatement");
            id = mstmt.getId();
            if(StringUtil.startsWith(id, "mapper.")) {
                id = id.substring(7);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.debug("{}", id);
        return invocation.proceed(); // 쿼리 실행
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
