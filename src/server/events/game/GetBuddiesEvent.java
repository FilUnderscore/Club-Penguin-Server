package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class GetBuddiesEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("b#gb"))
		{
			penguin.sendData(penguin.buildXTMessage("gb", args[0], penguin.getBuddyString()));
		}
	}
}