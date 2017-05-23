package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class HeartbeatEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("u#h"))
		{
			penguin.sendData(penguin.buildXTMessage("h", args[0]));
		}
	}
}