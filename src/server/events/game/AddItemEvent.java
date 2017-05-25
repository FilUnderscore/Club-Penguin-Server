package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class AddItemEvent extends XTEvent
{
	public AddItemEvent() 
	{
		super("i#ai");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.addItem(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}