package com.mnwise.wiseu.web.rest.dao.env;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.env.DefaultHandler;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

@Repository
public class EnvDao extends BaseDao {

	/**
	 * 기본 핸들러 정보를 가져온다.
	 */
    public DefaultHandler selectDefaultHandler(DefaultHandler defaultHandler) throws SQLException {
		return (DefaultHandler) selectOne("Env.selectDefaultHandler", defaultHandler);
    }



}