package server.event.events.game.client.friends;

import server.event.XTEvent;
import server.player.Penguin;

public class BuddyRequestEvent extends XTEvent
{
	public BuddyRequestEvent() 
	{
		super("b#br");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendFriendRequest(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}