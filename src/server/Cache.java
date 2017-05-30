package server;

import java.util.HashMap;
import java.util.Map;

import server.player.Penguin;

public class Cache 
{
	protected static Map<Integer, Penguin> PenguinCache;
	
	static
	{
		PenguinCache = new HashMap<>();
	}
	
	public static void savePenguin(Penguin penguin)
	{
		if(!PenguinCache.containsKey(penguin.Id))
		{
			PenguinCache.put(penguin.Id, penguin);
		}
	}
	
	public static Penguin getPenguin(int userId)
	{
		return PenguinCache.get(userId);
	}
}