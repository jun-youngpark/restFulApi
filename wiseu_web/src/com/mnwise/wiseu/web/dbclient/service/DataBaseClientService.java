package com.mnwise.wiseu.web.dbclient.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.xCryptionAria;
import com.mnwise.wiseu.web.common.dao.DbInfoDao;
import com.mnwise.wiseu.web.dbclient.dao.DataBaseClientDao;
import com.mnwise.wiseu.web.dbclient.model.DataBaseClientVo;

@Service
public class DataBaseClientService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(DataBaseClientService.class);

    @Autowired private DataBaseClientDao dataBaseClientDao;
    @Autowired private DbInfoDao dbInfoDao;

    @Value("${cipher}")
    private String cipher;

    public List<DataBaseClientVo> getDbinfo() {
        List<DataBaseClientVo> returnDbList = dbInfoDao.getDbinfo();
        if(cipher.equalsIgnoreCase("on")) {
            for(int i = 0; i < returnDbList.size(); i++) {
                try {
                    ((DataBaseClientVo) returnDbList.get(i)).setJdbcPassWord(xCryptionAria.decrypt(((DataBaseClientVo) returnDbList.get(i)).getJdbcPassWord()));
                } catch(Exception e) {
                    log.error(null, e);
                }
            }
        }
        return returnDbList;
    }

    public List<String> getResultMetaData(DataBaseClientVo dbClientVo) {
        return dataBaseClientDao.getResultMetaData(dbClientVo);
    }

    public List<String> getResultData(DataBaseClientVo dbClientVo) {
        return dataBaseClientDao.getResultData(dbClientVo);
    }

    public int setResultData(DataBaseClientVo diClientVo) {
        return dataBaseClientDao.setResultData(diClientVo);
    }
}