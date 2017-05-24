package server.command;

import java.util.HashMap;
import java.util.Map;

import server.Server;
import server.player.Penguin;
import server.util.Logger;

public class CommandManager 
{
	private Server server;
	
	protected Map<String, Command> Commands;
	
	public CommandManager(Server server)
	{
		this.server = server;
	
		this.Commands = new HashMap<>();
	}
	
	public void init()
	{
		Logger.info("{CommandManager} Loaded " + this.Commands.size() + " Commands!", this.server);
	}
	
	public boolean commandExists(String cmd)
	{
		if(this.Commands.containsKey(cmd))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean runCommand(Penguin client, String cmd)
	{
		cmd = cmd.substring(1);
		
		if(this.commandExists(cmd))
		{
			this.Commands.get(cmd).execute(this.server, client, cmd.split(" "));
			
			return true;
		}
		
		return false;
	}
}