package server.event.events.client.game.epf;

import server.event.XTEvent;
import server.player.Penguin;

public class EPFGetFieldOpEvent extends XTEvent
{
	public EPFGetFieldOpEvent() 
	{
		super("f#epfgf");
	}

	public void process(Penguin penguin, String[] args) 
	{
		
	}
}