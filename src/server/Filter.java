package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import server.util.FileUtil;
import server.util.Logger;

public final class Filter 
{
	private static List<String> FilteredWords;
	
	@SuppressWarnings("unchecked")
	public static void initializeFilter(File filterFile)
	{
		if(FilteredWords != null)
			return;
		
		try
		{
			FilteredWords = new ArrayList<>();
			
			Element element = new SAXBuilder().build(new FileInputStream(filterFile)).getRootElement();
			
			for(Element child : ((List<Element>)element.getChildren()))
			{
				FilteredWords.add(child.getName());
			}
			
			Logger.info("Filter initialized!", null);
		}
		catch(FileNotFoundException e)
		{
			Logger.warning("No Filter configuration found, creating file. Filtering features will not work unless values are changed within " + filterFile.getName() + "!", null);
			
			FileUtil.writeToFile(filterFile, "<filter>", "<word1/>", "<word2/>", "<word3/>", "</filter>");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isInitialized()
	{
		return FilteredWords != null;
	}
	
	public static boolean contains(String string)
	{
		return FilteredWords.contains(string);
	}
}