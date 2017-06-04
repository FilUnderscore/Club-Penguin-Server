package server.event.events.client.game.message;

import server.event.XTEvent;
import server.player.Penguin;

public class SendQuickMessageEvent extends XTEvent
{
	public SendQuickMessageEvent()
	{
		super("u#sq");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendQuickMessage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}