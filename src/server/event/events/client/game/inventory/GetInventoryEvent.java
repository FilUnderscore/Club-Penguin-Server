package server.event.events.client.game.inventory;

import server.event.XTEvent;
import server.player.Penguin;

public class GetInventoryEvent extends XTEvent
{
	public GetInventoryEvent() 
	{
		super("i#gi");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("gi", args[0] + penguin.getInventoryString()));
	}
}