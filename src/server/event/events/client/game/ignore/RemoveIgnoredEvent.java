package server.event.events.client.game.ignore;

import server.event.XTEvent;
import server.player.Penguin;

public class RemoveIgnoredEvent extends XTEvent
{
	public RemoveIgnoredEvent() 
	{
		super("n#rn");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.unignorePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}