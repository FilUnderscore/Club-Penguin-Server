package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class SendJokeEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("u#sj"))
		{
			penguin.sendJokeMessage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
	}
}