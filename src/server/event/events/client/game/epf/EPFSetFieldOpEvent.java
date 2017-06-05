package server.event.events.client.game.epf;

import server.event.XTEvent;
import server.player.Penguin;

public class EPFSetFieldOpEvent extends XTEvent
{
	public EPFSetFieldOpEvent() 
	{
		super("f#epfsf");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.updateFieldOpStatus(Integer.valueOf(args[0]));
	}
}