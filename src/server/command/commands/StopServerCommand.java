package server.command.commands;

import server.Server;
import server.command.Command;
import server.player.Penguin;
import server.player.StaffRank;

public class StopServerCommand extends Command
{
	public StopServerCommand() 
	{
		super("stopserver", StaffRank.ADMIN);
	}

	public void execute(Server server, Penguin client, String[] args) 
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
}