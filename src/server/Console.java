package server;

import java.util.Scanner;

import server.api.CPServerAPI;

public class Console 
{
	private Thread ConsoleThread;
	
	public Console()
	{
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
						
						for(Server server : CPServerAPI.getAPI().getServers())
						{
							try 
							{
								server.stop();
							} 
							catch (Exception e) 
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