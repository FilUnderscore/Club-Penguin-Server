package server.event.events.client.game.mail;

import server.event.XTEvent;
import server.player.Penguin;

public class MailSendEvent extends XTEvent
{
	public MailSendEvent() 
	{
		super("l#ms");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendPostcard(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
	}
}