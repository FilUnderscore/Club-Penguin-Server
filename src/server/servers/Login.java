package server.servers;

import java.io.File;

import server.Configuration;
import server.Server;
import server.ServerInfo;
import server.player.Penguin;
import server.util.Logger;

public class Login extends Server
{
	public Login(ServerInfo info)
	{
		super(info, new Configuration(new File("config.xml")));
	}
	
	public void onDisconnect(Penguin client)
	{
		Logger.info("Client Disconnected - " + client.getSocket().getInetAddress().getHostAddress() + ":" + client.getSocket().getPort(), this);
		
		this.Clients.remove(client);
		
		client = null;
	}

	@SuppressWarnings("deprecation")
	public void stop()
	{
		for(Penguin client : this.Clients)
		{
			client.kickStop();
		}
		
		this.Clients.clear();
		
		this.Threads.shutdownNow();
		
		this.ServerThread.stop();
	}
	
	public void registerCommands()
	{	
	}

	public void registerEvents() 
	{
	}
}