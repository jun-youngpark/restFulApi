package com.mnwise.wiseu.web.rest.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.dao.env.EnvDao;
import com.mnwise.wiseu.web.rest.model.env.DefaultHandler;
import com.mnwise.wiseu.web.rest.parent.BaseService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(value = "txManager")
public class EnvService extends BaseService {
	@Autowired private EnvDao envDao;

	public DefaultHandler selectDefaultHandler(String channel, String serviceType, String abTestYn, String mailType) throws Exception{
		DefaultHandler defaultHandler = new DefaultHandler();
		defaultHandler.setChannel(channel);
		defaultHandler.setServiceType(serviceType);
		defaultHandler.setHandleType(Const.GROOVY);
		defaultHandler.setAbTestYn(abTestYn);
		defaultHandler.setHandleAttr(Const.MailType.NONE.equals(mailType)?"D":mailType);

		DefaultHandler result = Optional
				.ofNullable(envDao.selectDefaultHandler(defaultHandler))
				.orElseThrow(() -> new Exception("Default Handler is null"));
		return result;
	}



}