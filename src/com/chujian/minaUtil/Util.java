package com.chujian.minaUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qin
 * π§æﬂ¿‡
 */
public class Util {
	public static String getSysTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());

	}
}
