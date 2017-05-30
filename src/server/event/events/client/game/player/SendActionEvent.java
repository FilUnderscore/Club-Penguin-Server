package server.event.events.client.game.player;

import server.event.XTEvent;
import server.player.Penguin;

public class SendActionEvent extends XTEvent
{
	public SendActionEvent()
	{
		super("u#sa");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendActionUpdate(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}