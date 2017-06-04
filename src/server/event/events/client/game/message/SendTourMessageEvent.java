package server.event.events.client.game.message;

import server.event.XTEvent;
import server.player.Penguin;

public class SendTourMessageEvent extends XTEvent
{
	public SendTourMessageEvent()
	{
		super("u#st");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendTourMessage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}