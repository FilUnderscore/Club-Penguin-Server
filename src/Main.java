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
		
		Servers.add(new Login(new ServerInfo(1, "127.0.0.1", 6112)));
		
		Servers.add(new Game(new ServerInfo(server.util.Number.getRandom(1, 1000), "Blizzard", "127.0.0.1", server.util.Number.getRandom(0, 65535), false)));
		
		new Console(Servers);
	}
}