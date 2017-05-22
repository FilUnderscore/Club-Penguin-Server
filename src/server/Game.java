package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.player.Penguin;
import server.util.Logger;

public class Game extends Server
{
	protected List<Penguin> IglooMap;
	
	public Game(ServerInfo info)
	{
		super(info);
		
		this.IglooMap = new ArrayList<>();
	}
	
	public List<Penguin> getIglooMap()
	{
		return this.IglooMap;
	}

	public void init() throws Exception
	{
		Logger.info("Starting Club Penguin [Game] Server...", this);
		Logger.info("Written in Java by Fil_", this);
	
		this.ServerSocket = new ServerSocket(this.ServerInfo.Port);
		
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
	
		this.Database.saveServer(this.ServerInfo);
	}

	public void onDisconnect(Penguin client)
	{
		Logger.info("Client Disconnected - " + client.getSocket().getInetAddress().getHostAddress() + ":" + client.getSocket().getPort(), this);
		
		this.IglooMap.remove(client);
	
		client.handleBuddyOffline();
		client.removePlayerFromRoom();	
		
		this.Clients.remove(client);
		
		client = null;
	}
	
	@SuppressWarnings("deprecation")
	public void stop() throws Exception
	{
		this.Database.removeServer(this.ServerInfo.Id);
		
		for(Penguin client : this.Clients)
		{
			client.sendError(1);
		}
		
		this.Clients.clear();
		
		this.Threads.shutdownNow();
		
		this.ServerThread.stop();
	}
}