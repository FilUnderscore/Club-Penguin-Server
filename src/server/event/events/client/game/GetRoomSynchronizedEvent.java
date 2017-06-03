package server.event.events.client.game;

import server.event.XTEvent;
import server.player.Penguin;

public class GetRoomSynchronizedEvent extends XTEvent
{
	public GetRoomSynchronizedEvent()
	{
		super("j#grs");
	}

	public void process(Penguin penguin, String[] args) 
	{
		
	}
}