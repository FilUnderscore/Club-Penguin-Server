package server.event.events.client.game.puffle;

import server.event.XTEvent;
import server.player.Penguin;

public class CheckPuffleNameEvent extends XTEvent
{

	public CheckPuffleNameEvent() 
	{
		super("p#checkpufflename");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.checkPuffleName(Integer.parseInt(args[0]), args[1]);
	}
}