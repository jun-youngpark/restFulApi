package com.mnwise.wiseu.web.base.util;

import java.util.List;

import org.springframework.jdbc.support.JdbcUtils;

import com.mnwise.wiseu.web.rest.common.FileUtil;
import com.mnwise.wiseu.web.rest.model.custom.CustomBatch;

public class camelCaseUtils {

	public static void main(String[] args) {
		List<String> dataList = FileUtil.readSamFile("D:/camel.txt");
		for (String data : dataList) {
			System.out.println("protected String "+JdbcUtils.convertUnderscoreNameToPropertyName(data)+";");
		}
	}
}
