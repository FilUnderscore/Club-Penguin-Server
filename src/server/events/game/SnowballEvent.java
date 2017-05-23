package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class SnowballEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("u#sb"))
		{
			penguin.throwSnowball(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}
	}
}