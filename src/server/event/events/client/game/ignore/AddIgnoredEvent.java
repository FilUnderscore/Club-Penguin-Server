package server.event.events.client.game.ignore;

import server.event.XTEvent;
import server.player.Penguin;

public class AddIgnoredEvent extends XTEvent
{
	public AddIgnoredEvent(String extension) 
	{
		super("n#an");
	}

	public void process(Penguin penguin, String[] args) 
	{
		
	}
}