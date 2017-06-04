package server.command.commands;

import server.Server;
import server.api.CPServerAPI;
import server.command.Command;
import server.player.Penguin;
import server.player.StaffRank;

public class StopServersCommand extends Command
{
	public StopServersCommand() 
	{
		super("stopservers", StaffRank.SYSTEM);
	}

	public void execute(Server server, Penguin client, String[] args) 
	{
		for(Server s : CPServerAPI.getAPI().getServers())
		{
			try
			{
				s.stop();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		System.exit(0);
	}
}