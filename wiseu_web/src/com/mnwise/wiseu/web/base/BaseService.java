package com.mnwise.wiseu.web.base;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.mnwise.common.security.DataSecurity;

public class BaseService {
    @Autowired private BaseDao baseDao;

    protected DataSecurity security = DataSecurity.getInstance();

    @Value("${password.enc.use:''}")
    protected String passwordEncUse;
    @Value("${template.upload.dir}")
    protected String templateUploadDir;
    @Value("${mms.upload.path.root}")
    protected String mmsUploadPathRoot;
    @Value("${mms.upload.path}")
    protected String mmsUploadPath;


    protected Connection getConnection() throws SQLException {
        return baseDao.getDataSource().getConnection();
    }
}
