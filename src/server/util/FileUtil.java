package server.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileUtil 
{
	public static File createNewFile(File file)
	{
		if(file.exists())
		{
			return file;
		}
		
		try
		{
			file.createNewFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return file;
	}
	
	public static void writeToFile(File file, Object...data)
	{
		try
		{
			file = createNewFile(file);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for(Object dat : data)
			{
				writer.write(String.valueOf(dat));
			}
			
			writer.flush();
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}