package server.event.events.client.game.message;

import server.event.XTEvent;
import server.player.Penguin;

public class SendMascotMessageEvent extends XTEvent
{
	public SendMascotMessageEvent()
	{
		super("u#sma");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendMascotMessage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}