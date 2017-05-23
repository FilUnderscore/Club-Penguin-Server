package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class SendEmoteEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("u#se"))
		{
			penguin.sendEmote(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
	}
}