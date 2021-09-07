package com.mnwise.wiseu.web.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String getToDate() {
	    Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    return formatter.format(currentTime);
	}

	public static String getToTime() {
	    Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
	    return formatter.format(currentTime);
	}

}
