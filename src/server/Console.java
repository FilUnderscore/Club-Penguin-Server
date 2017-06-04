package server;

import java.util.Scanner;

import server.api.CPServerAPI;
import server.player.Penguin;
import server.util.Logger;
import server.util.StringUtil;

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
			@SuppressWarnings("resource")
			public void run()
			{
				Scanner scanner = new Scanner(System.in);
				
				String text;
				
				while((text = scanner.nextLine()) != null)
				{
					String[] args = text.split(" ");
					
					if(args[0].equalsIgnoreCase("execute"))
					{
						Server server = CPServerAPI.getAPI().getServer(Integer.parseInt(args[1]));
						
						String arg = StringUtil.getArguments(2, args.length, args);
						
						Logger.info(arg, server);
						
						if(server != null)
						{
							server.CommandManager.runCommand(Penguin.createPenguin(-1, "Console", server), arg);
						}
						else
						{
							Logger.info("Server '" + args[1] + "' does not exist!", null);
						}
					}
					else
					{
						Logger.info("[Console] Command '" + args[0] + "' does not exist!", null);
					}
				}
			}
		};
		
		this.ConsoleThread.start();
	}
}