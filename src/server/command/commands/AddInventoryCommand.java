package server.command.commands;

import server.Server;
import server.command.Command;
import server.player.Penguin;
import server.player.StaffRank;

public class AddInventoryCommand extends Command
{
	public AddInventoryCommand()
	{
		super("addinv", StaffRank.MODERATOR);
	}

	public void execute(Server server, Penguin client, String[] args) 
	{
		client.addItem(-1, Integer.parseInt(args[1]));
	}
}