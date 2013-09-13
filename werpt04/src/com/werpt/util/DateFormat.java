package com.werpt.util;

import java.text.SimpleDateFormat;

public class DateFormat {
	public static String getCurTime(){
		long curTime=System.currentTimeMillis();
		SimpleDateFormat formator=new SimpleDateFormat("HH:mm:ss ");
		String s= formator.format(curTime);
		return s;
		
	}
}
