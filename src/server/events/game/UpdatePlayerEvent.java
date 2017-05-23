package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class UpdatePlayerEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.startsWith("s#up"))
		{
			penguin.sendClothingUpdate(type.split("#")[1], Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
	}
}