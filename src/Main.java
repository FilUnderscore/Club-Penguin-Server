import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.Game;
import server.Login;
import server.Server;
import server.ServerInfo;
import server.console.Console;

public class Main 
{
	private static List<Server> Servers;
	
	public static void main(String[] args)
	{
		Servers = new ArrayList<>();
		
		Server login = new Login(new ServerInfo(1, "127.0.0.1", 6112));
		Servers.add(login);
		
		//Servers.add(new Game(new ServerInfo(server.util.Number.getRandom(1, 1000), "Blizzard", "127.0.0.1", server.util.Number.getRandom(0, 65535), false)));
		//Servers.add(new Game(new ServerInfo(100, "Test Server", "127.0.0.1", 6115, false)));
		
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
		
		new Console(Servers);
	}
}