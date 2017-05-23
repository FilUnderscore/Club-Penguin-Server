package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class AddItemEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("i#ai"))
		{
			penguin.addItem(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
	}
}