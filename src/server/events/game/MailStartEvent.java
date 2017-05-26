package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class MailStartEvent extends XTEvent
{
	public MailStartEvent() 
	{
		super("l#mst");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.loadMail(Integer.parseInt(args[0]));
	}
}
