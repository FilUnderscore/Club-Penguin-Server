package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class JoinRoomEvent extends XTEvent
{
	public void process(Penguin penguin, String type, String[] args) 
	{
		if(type.equalsIgnoreCase("j#jr"))
		{
			penguin.joinRoom(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		}
	}
}