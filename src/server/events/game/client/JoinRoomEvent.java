package server.events.game.client;

import server.event.XTEvent;
import server.player.Penguin;

public class JoinRoomEvent extends XTEvent
{
	public JoinRoomEvent()
	{
		super("j#jr");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.joinRoom(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	}
}