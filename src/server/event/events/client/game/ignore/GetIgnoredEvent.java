package server.event.events.client.game.ignore;

import server.event.XTEvent;
import server.player.Penguin;

public class GetIgnoredEvent extends XTEvent
{
	public GetIgnoredEvent() 
	{
		super("n#gn");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("gn", args[0], penguin.getIgnoredString()));
	}
}