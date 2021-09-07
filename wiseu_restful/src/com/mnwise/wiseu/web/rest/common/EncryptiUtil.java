package com.mnwise.wiseu.web.rest.common;

public class EncryptiUtil {

	/**
	 * 암호화 여부 체크 
	 * @param email
	 * @return
	 */
	public static String emailEncryptYnCheck(String email) {
		String check = "N";
		if (email == null || email.equals("")) {
			// 문자열이 공백인지 확인
			check = "";
		} else {
			try {
				if (email != null && !"".equals(email) && !"@".equals(email)) {
					String temp[] = email.split("@");
					int idx = email.indexOf(" ");
					if (idx == -1 || idx == (email.length() - 1)) {
						if (temp.length != 0 && temp[0] != null && !temp[0].equals("") && temp.length == 2) {
							if (temp[0].length() < 3) {
								check = "N";
							} else {
								if (email.indexOf("=@") > -1) {
									// ==가 있으면 암호화
									check = "Y";
								}
							}
						} else {
							check = "Y";
						}
					} else {
						check = "Y";
					}
				} else {
					check = "Y";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return check;

	}
	
}
