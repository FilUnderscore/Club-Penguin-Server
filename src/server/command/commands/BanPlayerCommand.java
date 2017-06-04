package server.command.commands;

import server.Server;
import server.command.Command;
import server.player.Penguin;
import server.player.StaffRank;
import server.util.StringUtil;

public class BanPlayerCommand extends Command
{
	public BanPlayerCommand()
	{
		super("banplayer", StaffRank.MODERATOR);
	}

	public void execute(Server server, Penguin client, String[] args) 
	{
		try
		{
			String reason = StringUtil.getArguments(2, args.length, args);
			
			client.issueBan(-1, Integer.parseInt(args[0]), reason, Integer.parseInt(args[1]));
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			client.issueBan(-1, Integer.parseInt(args[0]), "", -1);
		}
	}
}