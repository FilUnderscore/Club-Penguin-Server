package server.event.events.client.game.message;

import server.event.XTEvent;
import server.player.Penguin;

public class SendLineMessageEvent extends XTEvent
{
	public SendLineMessageEvent()
	{
		super("u#sl");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendLineMessage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}