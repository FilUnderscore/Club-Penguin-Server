package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.events.game.AddItemEvent;
import server.events.game.GetBuddiesEvent;
import server.events.game.GetInventoryEvent;
import server.events.game.GetPlayerEvent;
import server.events.game.HeartbeatEvent;
import server.events.game.JoinRoomEvent;
import server.events.game.JoinServerEvent;
import server.events.game.SendActionEvent;
import server.events.game.SendEmoteEvent;
import server.events.game.SendFrameEvent;
import server.events.game.SendJokeEvent;
import server.events.game.SendMessageEvent;
import server.events.game.SendSafeMessageEvent;
import server.events.game.SetPositionEvent;
import server.events.game.SnowballEvent;
import server.events.game.UpdatePlayerEvent;
import server.player.Penguin;
import server.util.Logger;

public class Game extends Server
{
	protected List<Penguin> IglooMap;
	
	public Game(ServerInfo info)
	{
		super(info);
		
		this.IglooMap = new ArrayList<>();
		
		save();
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
		
		save();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() throws Exception
	{
		remove();
		
		for(Penguin client : this.Clients)
		{
			client.kickStop();
		}
		
		this.Clients.clear();
		
		this.Threads.shutdownNow();
		
		if(this.ServerThread != null)
			this.ServerThread.stop();
	}

	public void registerEvents() 
	{
		this.EventManager.registerEvent(new JoinServerEvent());
		this.EventManager.registerEvent(new GetInventoryEvent());
		this.EventManager.registerEvent(new JoinRoomEvent());
		this.EventManager.registerEvent(new SetPositionEvent());
		this.EventManager.registerEvent(new HeartbeatEvent());
		this.EventManager.registerEvent(new UpdatePlayerEvent());
		this.EventManager.registerEvent(new GetPlayerEvent());
		this.EventManager.registerEvent(new AddItemEvent());
		this.EventManager.registerEvent(new SnowballEvent());
		this.EventManager.registerEvent(new SendFrameEvent());
		this.EventManager.registerEvent(new SendActionEvent());
		this.EventManager.registerEvent(new SendMessageEvent());
		this.EventManager.registerEvent(new SendJokeEvent());
		this.EventManager.registerEvent(new SendSafeMessageEvent());
		this.EventManager.registerEvent(new SendEmoteEvent());
		this.EventManager.registerEvent(new GetBuddiesEvent());
	}
}