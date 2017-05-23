package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class SendMessageEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("m#sm"))
		{
			penguin.sendMessage(Integer.parseInt(args[0]), args[2]);
		}
	}
}