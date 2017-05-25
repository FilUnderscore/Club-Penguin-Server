package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class SnowballEvent extends XTEvent
{
	public SnowballEvent()
	{
		super("u#sb");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.throwSnowball(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
	}
}