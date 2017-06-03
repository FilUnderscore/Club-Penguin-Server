package server.event.events.client.game.igloo;

import server.event.XTEvent;
import server.player.Penguin;
import server.servers.Game;

public class GetRevisionEvent extends XTEvent
{
	public GetRevisionEvent()
	{
		super("g#gr");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("gr", args[0], ((Game)this.getServer()).getIglooString()));
	}
}