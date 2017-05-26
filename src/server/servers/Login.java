package server.servers;

import java.net.ServerSocket;
import java.net.Socket;

import server.Server;
import server.ServerInfo;
import server.player.Penguin;
import server.util.Logger;

public class Login extends Server
{
	public Login(ServerInfo info)
	{
		super(info);
	}
	
	public void init() throws Exception
	{
		Logger.info("Starting Club Penguin [Login] Server...", this);
		Logger.info("Written in Java by Fil_", this);
	
		this.ServerSocket = new ServerSocket(this.Configuration.LOGIN_SERVER_PORT);
		
		final Server scope = this;
		
		this.ServerThread = new Thread()
		{
			public void run()
			{
				Logger.info("Server Started - Waiting for Clients to connect!", scope);
				
				while(true)
				{
					try
					{
						//Freezes Thread until a Client has established a Connection!
						Socket socket = ServerSocket.accept();

						Threads.submit(new Runnable()
						{
							public void run()
							{
								onConnection(socket);
							}
						});
					}
					catch(Exception e)
					{
						Logger.error("There was an error while accepting a Client connection: " + e.getMessage(), scope);
					}
				}
			}
		};
		
		this.ServerThread.start();
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