package server.event.events.client.game.player;

import server.event.XTEvent;
import server.player.Penguin;

public class SendFrameEvent extends XTEvent
{
	public SendFrameEvent()
	{
		super("u#sf");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendFrameUpdate(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}