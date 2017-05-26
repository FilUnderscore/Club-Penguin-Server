import server.ServerPool;
import server.console.Console;
import server.data.Crumbs;

public class Main 
{
	public static void main(String[] args)
	{
		new Crumbs();
		
		new Console(ServerPool.getServers());
	}
}