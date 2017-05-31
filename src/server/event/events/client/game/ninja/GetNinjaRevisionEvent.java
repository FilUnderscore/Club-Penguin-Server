package server.event.events.client.game.ninja;

import server.event.XTEvent;
import server.player.Penguin;

public class GetNinjaRevisionEvent extends XTEvent
{
	public GetNinjaRevisionEvent()
	{
		super("ni#gnr");
	}

	public void process(Penguin penguin, String[] args) 
	{
		
	}
}