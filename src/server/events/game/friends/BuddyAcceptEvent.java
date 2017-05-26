package server.events.game.friends;

import server.event.XTEvent;
import server.player.Penguin;

public class BuddyAcceptEvent extends XTEvent
{
	public BuddyAcceptEvent() 
	{
		super("b#ba");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.acceptFriendRequest(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}