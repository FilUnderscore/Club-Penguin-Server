package server.command.commands;

import server.Server;
import server.ServerPool;
import server.command.Command;
import server.player.Penguin;
import server.player.StaffRank;

public class StopServerCommand extends Command
{
	public StopServerCommand() 
	{
		super("stopserver", StaffRank.MODERATOR);
	}

	public void execute(Server server, Penguin client, String[] args) 
	{
		ServerPool.stopServers();
	}
}