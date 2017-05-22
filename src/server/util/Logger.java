package server.util;

import server.Server;

public class Logger 
{
	public static void log(String info, LogType type, Server server)
	{
		System.out.println(Time.getTimestamp() + " [" + type.name() + "] " + "<" + (server != null && server.getServerInfo() != null ? server.getServerInfo().Name : "UNDEFINED") + "> " + info);
	}
	
	public static void info(String msg, Server server)
	{
		log(msg, LogType.INFO, server);
	}
	
	public static void notice(String msg, Server server)
	{
		log(msg, LogType.NOTICE, server);
	}
	
	public static void warning(String msg, Server server)
	{
		log(msg, LogType.WARNING, server);
	}
	
	public static void error(String msg, Server server)
	{
		log(msg, LogType.ERROR, server);
	}
	
	public enum LogType
	{
		INFO,
		NOTICE,
		WARNING,
		ERROR;
	}
}