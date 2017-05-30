package server.event.events.client.game.friends;

import server.event.XTEvent;
import server.player.Penguin;

public class RemoveBuddyEvent extends XTEvent
{
	public RemoveBuddyEvent() 
	{
		super("b#rb");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.removeFriend(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}	
}