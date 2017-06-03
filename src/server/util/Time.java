package server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time 
{
	public static String getTimestamp()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
	
	public static boolean exceeded(long now, long before)
	{
		return false;
	}
}