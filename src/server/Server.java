package server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.command.CommandManager;
import server.event.EventManager;
import server.player.Penguin;
import server.servers.Game;
import server.servers.Login;
import server.util.Logger;

public abstract class Server 
{
	protected ServerInfo ServerInfo;
	protected Configuration Configuration;
	
	protected Database Database;
	
	protected ServerSocket ServerSocket;
	protected Thread ServerThread;
	
	protected ExecutorService Threads;
	
	protected CommandManager CommandManager;
	protected EventManager EventManager;
	
	protected List<Penguin> Clients;
	
	public Server(ServerInfo info, Configuration config)
	{
		this.ServerInfo = info;
		
		this.CommandManager = new CommandManager(this);
		
		registerCommands();
		
		this.CommandManager.init();
		
		this.EventManager = new EventManager(this);
		
		registerEvents();
		
		this.EventManager.init();
		
		if(this instanceof Game)
		{
			this.ServerInfo.Type = ServerType.GAME;
		}
		else if(this instanceof Login)
		{
			this.ServerInfo.Type = ServerType.LOGIN;
		}
		
		this.Configuration = config;
		this.Configuration.readConfig(this);
		
		this.Threads = Executors.newCachedThreadPool();
		
		this.Database = new Database(this);
		
		try
		{
			this.Database.connectMySQL(this.Configuration.SQL_USER, this.Configuration.SQL_PASS, this.Configuration.SQL_HOST, this.Configuration.SQL_DB);
		}
		catch(Exception e)
		{
			Logger.warning("There was an error while attempting to connect to MySQL database: " + e.toString(), this);
		}
		
		this.Clients = new ArrayList<>();
		
		try
		{
			init();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void init() throws Exception
	{
		this.ServerSocket = new ServerSocket(this.ServerInfo.Port);
		
		final Server scope = this;
		
		this.ServerThread = new Thread()
		{
			public void run()
			{
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
	
	public void onConnection(Socket socket)
	{
		Logger.info("Client Connected - " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort(), this);
		
		Penguin client = new Penguin(socket, this);
		
		this.Clients.add(client);
		
		this.ServerInfo.setPopulation(this.Clients.size());
		
		save();
		
		while(true)
		{
			if(client.IdleMins >= 10)
			{
				onDisconnect(client);
				break;
			}
			
			try
			{
				byte[] data = new byte[65536];
				
				int read = socket.getInputStream().read(data);
				
				//There is data to be read.
				if(read > 0)
				{
					handleData(data, client);
				}
				else if(read == -1)
				{
					onDisconnect(client);
					break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Logger.error("There was an error while reading data from a Socket: " + e.getMessage(), this);
				onDisconnect(client);
				break;
			}
		}
	}
	
	public final void save()
	{
		if(this.ServerInfo.Type == ServerType.GAME)
		{
			try
			{
				this.Database.updateServer(this.ServerInfo);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public final void clear()
	{
		if(this.ServerInfo.Type == ServerType.GAME)
		{
			try
			{
				this.Database.clearServer(this.ServerInfo.Id);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public abstract void registerCommands();
	
	public abstract void registerEvents();
	
	public abstract void onDisconnect(Penguin client);
	
	public void handleData(byte[] dataArr, Penguin client) throws Exception
	{
		for(String packet : new String(dataArr, StandardCharsets.UTF_8).split("\0"))
		{
			this.EventManager.handleEvents(client, packet);
		}
	}

	public abstract void stop() throws Exception;

	public final void sendData(String data, Penguin client)
	{
		for(Penguin c : getClients())
		{
			if(c.Room == client.Room && c.Id != client.Id)
			{
				c.sendData(data);
			}
		}
	}
	
	public final List<Penguin> getClients()
	{
		return this.Clients;
	}
	
	public final Penguin getPenguin(int userId)
	{
		for(Penguin penguin : getClients())
		{
			if(penguin.Id == userId)
			{
				return penguin;
			}
		}
		
		return null;
	}
	
	public final List<Penguin> getPenguins(int roomID)
	{
		List<Penguin> penguins = new ArrayList<>();
		
		for(Penguin penguin : getClients())
		{
			if(penguin.Room == roomID)
			{
				penguins.add(penguin);
			}
		}
		
		return penguins;
	}
	
	public final Configuration getServerConfig()
	{
		return this.Configuration;
	}
	
	public final ServerInfo getServerInfo()
	{
		return this.ServerInfo;
	}
	
	public final CommandManager getCommandManager()
	{
		return this.CommandManager;
	}
	
	public final Database getDatabase()
	{
		return this.Database;
	}
}