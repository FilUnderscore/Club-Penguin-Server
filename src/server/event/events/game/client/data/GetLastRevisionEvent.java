package server.event.events.game.client.data;

import server.event.ChildEvent;
import server.event.XTEvent;
import server.player.Penguin;

public class GetLastRevisionEvent extends ChildEvent
{
	public GetLastRevisionEvent(XTEvent event, Penguin penguin, Object packet) 
	{
		super(event, penguin, packet);
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("glr", args[0], 3555)); //Get Last Revision
	}
}