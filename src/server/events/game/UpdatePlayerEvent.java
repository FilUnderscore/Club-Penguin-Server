package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class UpdatePlayerEvent extends XTEvent
{
	public UpdatePlayerEvent()
	{
		super("s#up");
	}
	
	public void process(Penguin penguin, String type, String[] args) 
	{
		penguin.sendClothingUpdate(type.split("#")[1], Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}

	public void process(Penguin penguin, String[] args) 
	{
		
	}
}