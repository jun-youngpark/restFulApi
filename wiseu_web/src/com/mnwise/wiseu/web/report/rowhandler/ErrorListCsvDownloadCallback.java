package com.mnwise.wiseu.web.report.rowhandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.mnwise.common.security.DataSecurity;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.report.model.SendLogVo;

public class ErrorListCsvDownloadCallback implements ResultHandler, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(ErrorListCsvDownloadCallback.class);

    protected DataSecurity security = DataSecurity.getInstance();
    private OutputStream out;
    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public ErrorListCsvDownloadCallback(OutputStream out) throws IOException{
        this.out = out;
    }

    public void createTitle(String titleArray[]) throws IOException {
        out.write(0xFF); // UTF-16LE BOM
        out.write(0xFE);
        for(String title: titleArray) {
            out.write(title.getBytes(Const.UFT16LE));
            out.write(Const.TAB);
        }
        out.write(Const.NEWLINE);
    }

    @Override
    public void handleResult(ResultContext ctx)  {
        try {
            SendLogVo sendLogVo= (SendLogVo) ctx.getResultObject();
            String phoneVal;
            out.write(StringUtil.defaultString(sendLogVo.getCustomerKey()).getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(StringUtil.defaultString(sendLogVo.getCustomerNm()).getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            phoneVal = security.securityWithColumn(sendLogVo.getCustomerEmail(), "PHONE_NUM", "DECRYPT");
            String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
            if(Pattern.matches(regEx, phoneVal)) {
                phoneVal = phoneVal.replaceAll(regEx, "$1-$2-$3");
            }
            out.write(StringUtil.defaultString(phoneVal).getBytes(Const.UFT16LE));
            out.write(Const.TAB);
            out.write(StringUtil.defaultString(sendLogVo.getSendDtm()).getBytes(Const.UFT16LE));
            out.write(Const.NEWLINE);
        }catch(Exception e) {
            log.error(null, e);
        }
    }

}
