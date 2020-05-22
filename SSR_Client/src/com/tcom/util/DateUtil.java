package com.tcom.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 시간관련 유틸리티
 */
public class DateUtil {
    /**
     * 현재 시간을 지정된 포맷으로 출력
     *
     * @return yyyyMMddHHmm
     */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date();
		return sdf.format(date);
	}

    /**
     * 현재 시간을 초단위까지 출력
     * @return yyyyMMddHHmmss
     */
	public static String getCurrentTime14() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		return sdf.format(date);
	}


    /**
     * 현재시간 timestamp 출력
     * @return
     */
	public static long getCurrentTimeMilliSecond()
	{
		return (new Date()).getTime();
	}

    /**
     * 현재 시간 출력 (HH:mm)
     * @param time
     * @return
     */
	public static String getTimeString(String time) {
		String reTime = null;
		if (time != null && time.length() == 12) {
			reTime = time.substring(8, 10) + ":" + time.substring(10);
		}
		return reTime;
	}

	/**
     * 날짜를 formatting (2005.08.18)
     **/
	public static String getDateString(String form) {
		SimpleDateFormat sdf = new SimpleDateFormat(form);//"yy.MM.dd" 등등
		return sdf.format(new Date());
	}

	public static Date getDate(String dateStr) {
		if (dateStr == null || "".equals(dateStr)) return null;
		try {
			String pattern = "yyyyMMdd";
			if (dateStr.length() == 10) {
				if (dateStr.indexOf(".") == 4) {
					pattern = "yyyy.MM.dd";
				} else if (dateStr.indexOf("-") == 4) {
					pattern = "yyyy-MM-dd";
				} else if (dateStr.indexOf("/") == 4) {
					pattern = "yyyy/MM/dd";
				}
			} else if (dateStr.length() == 8) {
				if (dateStr.indexOf(".") == 2) {
					pattern = "yy.MM.dd";
				} else if (dateStr.indexOf("-") == 2) {
					pattern = "yy-MM-dd";
				} else if (dateStr.indexOf("/") == 2) {
					pattern = "yy/MM/dd";
				} else {
					pattern = "yyyyMMdd";
				}
			} else if (dateStr.length() == 6) {
				pattern = "yyMMdd";
			}
			return new SimpleDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

}
