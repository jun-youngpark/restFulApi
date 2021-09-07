package com.mnwise.wiseu.web.rest.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.mnwise.common.util.StringUtil;

public class FileUtil {
	/**
	 * 다건 발송 파일 읽기
	 * 
	 * @param filePath
	 */
	public static List<String> readSamFile(String filePath) {
		BufferedReader br = null;
		String line = "";
		File file = new File(filePath);
		List<String> dataList = new ArrayList<String>();
		try {
			if (file.exists()) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "euc-kr"));
				while ((line = br.readLine()) != null) {
					if (!StringUtil.isBlank(line)) {
						dataList.add(line);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dataList;
	}
}
