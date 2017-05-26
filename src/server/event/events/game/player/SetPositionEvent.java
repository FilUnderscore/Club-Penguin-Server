package server.event.events.game.player;

import server.event.XTEvent;
import server.player.Penguin;

public class SetPositionEvent extends XTEvent
{
	public SetPositionEvent()
	{
		super("u#sp");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.move(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
	}
}