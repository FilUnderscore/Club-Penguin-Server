package server.event.events.game.client.message;

import server.event.XTEvent;
import server.player.Penguin;

public class SendSafeMessageEvent extends XTEvent
{
	public SendSafeMessageEvent()
	{
		super("u#ss");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendSafeMessage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}