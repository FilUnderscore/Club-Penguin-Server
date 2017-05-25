package server.commands;

import server.Server;
import server.command.Command;
import server.player.Penguin;
import server.player.StaffRank;

public class StopServerCommand extends Command
{
	public StopServerCommand() 
	{
		super("srvstop", StaffRank.MODERATOR);
	}

	public void execute(Server server, Penguin client, String[] args) 
	{
		
	}
}