package server;

public class ServerInfo 
{
	public static int MAX_PLAYERS = 300;
	
	/**
	 * Server Identifier
	 */
	public int Id;
	
	/**
	 * Server Name (displayed on Server List)
	 */
	public String Name;
	
	/**
	 * Server Address (to connect to)
	 */
	public String Address;
	
	/**
	 * Server Port (to connect to, if on the same address)
	 */
	public int Port;
	
	/**
	 * Server Safe Chat Mode Setting (use safe chat (list of words) or normal chat)
	 */
	public boolean SafeChatMode;
	
	/**
	 * Server Client Count [Server Bars (0 = 1 bar, 6 = FULL)]
	 */
	public int Count;
	public int Population;
	
	public ServerType Type;

	public ServerInfo(int id, String address, int port)
	{
		this.Id = id;
		this.Address = address;
		this.Port = port;
	}
	
	public ServerInfo(int id, String name, String address, int port, boolean safeChatMode)
	{
		this.Id = id;
		this.Name = name;
		this.Address = address;
		this.Port = port;
		this.SafeChatMode = safeChatMode;
	}
	
	public void setPopulation(int population)
	{
		this.Count = population;
		this.Population = (int)Math.round(((double)MAX_PLAYERS / (((double)MAX_PLAYERS + 1.0D) - (double)population))); //Rounds to nearest bar.
	}
}