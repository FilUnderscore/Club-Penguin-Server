package server.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

import server.Server;

public class Logger 
{
	private static File File;
	private static BufferedWriter FileWriter;
	
	static
	{
		try
		{
			String timestamp = Time.getTimestamp();
			timestamp = timestamp.replaceAll(":", "-");
			
			File = Paths.get("logs/" + timestamp + ".log").toFile();
			System.out.println(File.getAbsolutePath());
			
			if(!Paths.get("logs/").toFile().exists())
			{
				Paths.get("logs/").toFile().mkdirs();
			}
			
			if(!File.exists())
			{
				File.createNewFile();
			}
			
			FileWriter = new BufferedWriter(new FileWriter(File));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String logToFile(String data)
	{
		try
		{
			if(data != null && data.length() > 0)
			{
				FileWriter.write(data);
			}
			
			FileWriter.newLine();
			
			FileWriter.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static void log(String info, LogType type, Server server)
	{
		System.out.println(logToFile(Time.getTimestamp() + " [" + type.name() + "] " + "<" + (server != null && server.getServerInfo() != null ? server.getServerInfo().Name : "Server") + "> " + info));
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