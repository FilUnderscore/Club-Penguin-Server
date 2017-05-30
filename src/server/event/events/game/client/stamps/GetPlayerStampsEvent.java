package server.event.events.game.client.stamps;

import server.event.ChildEvent;
import server.event.XTEvent;
import server.player.Penguin;

public class GetPlayerStampsEvent extends ChildEvent
{
	public GetPlayerStampsEvent(XTEvent event, Penguin penguin, Object packet) 
	{
		super(event, penguin, packet);
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("gps", args[0], penguin.Id));
	}
}