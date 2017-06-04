package server.event.events.client.game.inventory;

import server.event.XTEvent;
import server.player.Penguin;

public class CoinsDigUpdateEvent extends XTEvent
{
	public CoinsDigUpdateEvent()
	{
		super("r#cdu");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.handleCoinsDug(Integer.parseInt(args[0]));
	}
}