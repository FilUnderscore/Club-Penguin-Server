package server.event.events.client.game.mail;

import server.event.XTEvent;
import server.player.Penguin;

public class MailGetEvent extends XTEvent
{
	public MailGetEvent() 
	{
		super("l#mg");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.getMail(Integer.parseInt(args[0]));
	}
}
