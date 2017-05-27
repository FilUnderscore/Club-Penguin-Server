package server.event.events.game.mail;

import server.event.XTEvent;
import server.player.Penguin;

public class MailCheckedEvent extends XTEvent
{
	public MailCheckedEvent() 
	{
		super("l#mc");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.checkPostcard(Integer.parseInt(args[0]));
	}
}