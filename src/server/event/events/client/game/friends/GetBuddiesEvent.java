package server.event.events.client.game.friends;

import server.event.XTEvent;
import server.player.Penguin;

public class GetBuddiesEvent extends XTEvent
{
	public GetBuddiesEvent() 
	{
		super("b#gb");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("gb", args[0], penguin.getBuddyString()));
	}
}