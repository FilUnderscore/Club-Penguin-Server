import java.io.File;

import server.api.CPServerAPI;

public class Main 
{
	public static void main(String[] args)
	{
		CPServerAPI.getAPI().load(true, new File("filter.xml"), new File("epf.xml"));
	}
}