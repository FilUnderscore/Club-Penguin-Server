package server.command;

import server.Server;
import server.player.Penguin;

public abstract class Command 
{
	public abstract void execute(Server server, Penguin client, String[] args);
}