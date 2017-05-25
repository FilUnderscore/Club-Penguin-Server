package server.command;

import server.Server;
import server.player.Penguin;
import server.player.StaffRank;

public abstract class Command 
{
	private String command;
	private StaffRank rank;
	
	public Command(String command, StaffRank rank)
	{
		this.command = command;
		this.rank = rank;
	}
	
	public abstract void execute(Server server, Penguin client, String[] args);

	public final String getCommand()
	{
		return this.command;
	}
	
	public final StaffRank getRank()
	{
		return this.rank;
	}
}