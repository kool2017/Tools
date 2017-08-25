/**
 * @PROJECT 
 * @DATE 2017-07-09 21:30:24
 * @AUTHOR LUYU
 */
package com.kool.tlwp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.kool.tlwp.exception.AppException;

/**
 * @DESCRIBE some tools of Date to transfer Date type,transfer Date form, calculate Date 
 * @AUTHOR LUYU
 * @DATE 2017-07-09 21:30:24
 *
 */
public class DateUtils {
	public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.S";
	public static final String FORMAT_DATEANDTIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_TIMESTAMP_WITHOUT_SPLIT = "yyyyMMddHHmmssS";
	public static final String FORMAT_DATE_AND_TIME_WITHOUT_SPLIT = "yyyyMMddHHmmss";
	public static final String FORMAT_SHORT_DATE_AND_TIME_WITHOUT_SPLIT = "yyMMddHHmmss";
	public static final String FORMAT_DATE_WITHOUT_SPLIT = "yyyyMMdd";
	public static final String FORMAT_SHORT_DATE_WITHOUT_SPLIT = "yyMMdd";
	public static final String FORMAT_TIME_WITHOUT_SPLIT = "HHmmss";
	
	public static Date getCurrentDate(){
		long lCur = System.currentTimeMillis();
		Date dt = new Date(lCur);
		return dt;
	}
	
	/**
	 * 
	 * @DESCRIBE return a date string in form @format
	 * @DATE 2017-07-09 21:33:47
	 *
	 * @return
	 * @throws AppException 
	 */
	public static String getCurrentTimeString(String format) throws AppException{
		if (null == format || format.trim().length() == 0) {
			format = FORMAT_TIMESTAMP;
		}
		String sDt = null;
		Date dt = getCurrentDate();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sDt = sdf.format(dt);
		} catch (Exception e) {
			throw new AppException("时间格式错误",e);
		}
		return sDt;
	}
	
	public static void delay(int millis) throws AppException{
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			throw new AppException("延时器出错");
		}
		
	}
}
