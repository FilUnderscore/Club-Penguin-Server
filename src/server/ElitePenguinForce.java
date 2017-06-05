package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import server.epf.EPFMessage;
import server.util.FileUtil;
import server.util.Logger;

public class ElitePenguinForce 
{
	private static List<EPFMessage> EPFMessages;
	
	@SuppressWarnings("unchecked")
	public static void initializeEPF(File epfFile)
	{
		if(EPFMessages != null)
			return;
		
		try
		{
			EPFMessages = new ArrayList<>();
			
			Element element = new SAXBuilder().build(new FileInputStream(epfFile)).getRootElement();
		
			for(Element child : ((List<Element>)element.getChildren("messages")))
			{
				String text = child.getChild("text").getValue();
				long timestamp = Long.valueOf(child.getChild("timestamp").getValue());
				int mascotID = Integer.valueOf(child.getChild("mascot").getValue());
				
				EPFMessages.add(new EPFMessage(text, timestamp, mascotID));
			}
			
			Logger.info("Loaded " + EPFMessages.size() + " EPF Messages!", null);
		}
		catch(FileNotFoundException e)
		{
			Logger.warning("No Elite Penguin Force configuration found, creating file.", null);
		
			FileUtil.writeToFile(epfFile, "<epf>", "<messages>", "<message>", "<text>", "EPF has not been configured!", "</text>", "<timestamp>", System.currentTimeMillis(), "</timestamp>", "<mascot>", 7, "</mascot>", "</message>", "</messages>", "</epf>");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isInitialized()
	{
		return EPFMessages != null;
	}
	
	public static List<EPFMessage> getEPFMessages()
	{
		return Collections.unmodifiableList(EPFMessages);
	}
}