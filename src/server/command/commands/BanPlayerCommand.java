package server.command.commands;

import server.Server;
import server.command.Command;
import server.player.Penguin;
import server.player.StaffRank;

public class BanPlayerCommand extends Command
{
	public BanPlayerCommand()
	{
		super("banplayer", StaffRank.MODERATOR);
	}

	public void execute(Server server, Penguin client, String[] args) 
	{
		StringBuilder reason = new StringBuilder();
		
		for(int i = 2; i < args.length; i++)
		{
			reason.append(args[i] + " ");
		}
		
		client.issueBan(-1, Integer.parseInt(args[0]), reason.toString(), Integer.parseInt(args[1]));
	}
}