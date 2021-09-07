package com.mnwise.wiseu.web.base.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;

public class FormatUtil {
    private static final Logger log = LoggerFactory.getLogger(FormatUtil.class);
    private static WiseuLocaleChangeInterceptor localeChangeInterceptor;

    public FormatUtil(WiseuLocaleChangeInterceptor localeChangeInterceptor) {
        FormatUtil.localeChangeInterceptor = localeChangeInterceptor;
    }

    public final static String[] WEEK = {
        "일", "월", "화", "수", "목", "금", "토"
    };
    public final static String[] WEEK_EN = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };
    public static DecimalFormat pdf = new DecimalFormat("###.#");

    public static String toStrPercent(int a, int b, int decimalPointCnt) {
        if(b == 0)
            return "0.0%";
        double amount = (double) a / (double) b;
        NumberFormat numberFormatter = NumberFormat.getPercentInstance(Locale.KOREA);
        numberFormatter.setMinimumFractionDigits(decimalPointCnt);
        String amountOut;
        amountOut = numberFormatter.format(amount);
        return amountOut;
    }

    public static double toNumPercent(int a, int b, int decimalPointCnt) {
        if(b == 0)
            return 0;
        double amount = (double) a / (double) b * 100;
        return Double.parseDouble(pdf.format(amount));
    }

    public static String toStrNumber(int a, int decimalPointCnt) {
        double amount = (double) a;
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.KOREA);
        numberFormatter.setMinimumFractionDigits(decimalPointCnt);
        String amountOut;
        amountOut = numberFormatter.format(amount);
        return amountOut;
    }

    public static String toYmdStrDate(String yyyymmdd) {
        return toYmdStrDate(yyyymmdd, "yyyyMMdd");
    }

    public static String toYmdStrDate(String yyyymmdd, String format) {
        Date date = new Date();
        if(!StringUtil.isEmpty(yyyymmdd)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(yyyymmdd);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                return DateFormatUtils.format(date, "yyyy-MM-dd");
            } catch(ParseException e) {
                log.error(null, e);
            }
        }
        return null;
    }

    public static String toBasicStrDate(String yyyymmdd, String hhmmss) {
        return toBasicStrDate(yyyymmdd, hhmmss, "yyyyMMdd HHmmss");
    }

    public static String toBasicStrDate(String yyyymmdd, String hhmmss, String format) {
        Date date = new Date();
        if(!StringUtil.isEmpty(yyyymmdd)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(yyyymmdd + " " + hhmmss);
                Calendar cal = Calendar.getInstance(localeChangeInterceptor.getLocale());
                cal.setTime(date);

                String week = null;
                if(localeChangeInterceptor.getLanguage().equals("ko")) {
                    week = WEEK[cal.get(Calendar.DAY_OF_WEEK) - 1];
                } else {
                    week = WEEK_EN[cal.get(Calendar.DAY_OF_WEEK) - 1];
                }

                return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm") + " (" + week + ")";
            } catch(ParseException e) {
                log.error(null, e);
            }
        }
        return null;
    }

    public static String toSimpleBasicStrDate(String yyyymmdd, String hhmmss) {
        Date date = new Date();
        if(!StringUtil.isEmpty(yyyymmdd)) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
            try {
                date = df.parse(yyyymmdd + " " + hhmmss);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                return DateFormatUtils.format(date, "yy-MM-dd HH:mm");
            } catch(ParseException e) {
                log.error(null, e);
            }
        }
        return null;
    }

    public static String toWeekday(String yyyymmdd) {
        Date date = new Date();
        if(!StringUtil.isEmpty(yyyymmdd)) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            try {
                date = df.parse(yyyymmdd + " 00:00:00");
                Calendar cal = Calendar.getInstance(localeChangeInterceptor.getLocale());
                cal.setTime(date);

                String week = null;
                if(localeChangeInterceptor.getLanguage().equals("ko")) {
                    week = WEEK[cal.get(Calendar.DAY_OF_WEEK) - 1];
                } else {
                    week = WEEK_EN[cal.get(Calendar.DAY_OF_WEEK) - 1];
                }
                return week;
            } catch(ParseException e) {
                log.error(null, e);
            }
        }
        return null;
    }

    public static Date toBasicDate(String yyyymmdd, String hhmmss) {
        return toBasicDate(yyyymmdd, hhmmss, "yyyyMMdd HHmmss");
    }

    public static Date toBasicDate(String yyyymmdd, String hhmmss, String format) {
        Date date = new Date();
        if(!StringUtil.isEmpty(yyyymmdd)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(yyyymmdd + " " + hhmmss);
            } catch(ParseException e) {
                log.error(null, e);
            }
        }
        return date;

    }

    public static Map<String, Object> getYearAndMonthMap(List<String> dateList) {
        Map<String, Object> map = new HashMap<>();

        TreeSet<String> yearSet = new TreeSet<>();
        TreeSet<String> monthSet = new TreeSet<>();
        if(dateList != null) {
            for(int i = 0; i < dateList.size(); i++) {
                String dateString = dateList.get(i);
                if(StringUtil.isNotEmpty(dateString) && dateString.length() == 8) {
                    yearSet.add(dateString.substring(0, 4));
                    monthSet.add(dateString.substring(4, 6));
                }
            }
        }

        map.put("yearArray", yearSet.toArray(new String[yearSet.size()]));
        map.put("monthArray", monthSet.toArray(new String[yearSet.size()]));

        return map;
    }

    public static String addMinute(String yyyyMMddHHmmss, int mm) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        String strDate = "";

        try {
            date = dateFormat.parse(yyyyMMddHHmmss);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, mm);
            strDate = dateFormat.format(cal.getTime());
        } catch(Exception e) {
            e.getMessage();
        }

        return strDate;
    }

}
