package server.event.events.client.game.friends;

import server.event.XTEvent;
import server.player.Penguin;

public class BuddyFindEvent extends XTEvent
{
	public BuddyFindEvent()
	{
		super("b#bf");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.findFriend(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}