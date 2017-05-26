package server.event.events.game.message;

import server.event.XTEvent;
import server.player.Penguin;

public class SendMessageEvent extends XTEvent
{
	public SendMessageEvent()
	{
		super("m#sm");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendMessage(Integer.parseInt(args[0]), args[2]);
	}
}