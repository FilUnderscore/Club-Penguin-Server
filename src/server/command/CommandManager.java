package server.command;

import java.util.HashMap;
import java.util.Map;

import server.Server;
import server.api.CPServerAPI;
import server.player.Penguin;
import server.util.Logger;
import server.util.StringUtil;

public class CommandManager 
{
	private Server server;
	
	protected Map<String, Command> Commands;
	
	public static String COMMAND_PREFIX = "!";
	
	public CommandManager(Server server)
	{
		this.server = server;
	
		this.Commands = new HashMap<>();
	}
	
	private void registerAPICommands()
	{
		for(Command command : CPServerAPI.getAPI().getCommands())
		{
			this.registerCommand(command);
		}
	}
	
	public void init()
	{
		registerAPICommands();
		
		Logger.info("{CommandManager} Loaded " + this.Commands.size() + " Commands!", this.server);
	}
	
	public void registerCommand(Command cmd)
	{
		if(!this.Commands.containsKey(cmd.getCommand()))
		{
			this.Commands.put(cmd.getCommand(), cmd);
		}
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
		if(!cmd.startsWith(COMMAND_PREFIX))
		{
			return false;
		}
		
		cmd = cmd.substring(1);
		String shortenedCmd = cmd.split(" ")[0];
		
		if(this.commandExists(shortenedCmd))
		{
			if(client != null && client.Ranking.ordinal() > this.Commands.get(shortenedCmd).getRank().ordinal())
			{
				return false;
			}
			
			Logger.info("Player [" + client.Id + "] '" + client.Username + "' executed command '" + cmd + "'", this.server);
			
			String args = StringUtil.getArguments(1, cmd.split(" ").length - 1, cmd.split(" "));
			
			this.Commands.get(shortenedCmd).execute(this.server, client, args.split(" "));
			
			return true;
		}
		
		return false;
	}
}