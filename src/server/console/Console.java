package server.console;

import java.util.List;
import java.util.Scanner;

import server.Server;

public class Console 
{
	private List<Server> Servers;
	
	private Thread ConsoleThread;
	
	public Console(List<server.Server> servers)
	{
		this.Servers = servers;
		
		init();
	}
	
	public void init()
	{
		this.ConsoleThread = new Thread()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				Scanner scanner = new Scanner(System.in);
				
				String text;
				
				while((text = scanner.nextLine()) != null)
				{
					if(text.equalsIgnoreCase("quit"))
					{
						scanner.close();
						
						for(Server server : Servers)
						{
							try
							{
								server.stop();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						
						System.exit(0);
						ConsoleThread.stop();
					}
				}
			}
		};
		
		this.ConsoleThread.start();
	}
}