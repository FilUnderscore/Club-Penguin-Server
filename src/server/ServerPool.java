package server;
import java.util.ArrayList;
import java.util.List;

import server.player.Penguin;
import server.servers.Game;
import server.servers.Login;
import server.servers.Redemption;
import server.util.Logger;

public class ServerPool 
{
	protected static List<Server> Servers;
	
	static
	{
		Servers = new ArrayList<>();
		
		Server login = new Login(new ServerInfo(1, "Login", "127.0.0.1", 6112));
		
		Servers.add(login);
		Servers.add(new Redemption(new ServerInfo(2, "Redemption", "127.0.0.1", 6114)));
		
		try
		{
			for(ServerInfo server : login.getDatabase().getServerList())
			{
				Servers.add(new Game(server));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		Logger.info("[ServerPool] " + Servers.size() + " Servers started! Waiting for clients to connect.", null);
	}
	
	public static List<Server> getServers()
	{
		return Servers;
	}
	
	public static Server getServer(int serverId)
	{
		for(Server server : Servers)
		{
			if(server.getServerInfo().Id == serverId)
			{
				return server;
			}
		}
		
		return null;
	}
	
	public static Penguin getPenguin(int userId)
	{
		for(Penguin penguin : getPenguins())
		{
			if(penguin.Id == userId)
			{
				return penguin;
			}
		}
		
		return null;
	}
	
	public static List<Penguin> getPenguins()
	{
		List<Penguin> penguins = new ArrayList<>();
		
		for(Server server : Servers)
		{
			for(Penguin penguin : server.getClients())
			{
				penguins.add(penguin);
			}
		}
		
		return penguins;
	}
	
	public static String getWorldPopulationString()
	{
		String str = "";
		
		int i = 0;
		
		for(Server server : getServers())
		{
			ServerInfo info = server.getServerInfo();
			
			if(i == 0)
			{
				str += info.Id + "," + info.Population;
			}
			else
			{
				str += "|" + info.Id + "," + info.Population;
			}
			
			i++;
		}
		
		return str;
	}
	
	public static void stopServers()
	{
		for(Server server : ServerPool.getServers())
		{
			try
			{
				server.stop();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}