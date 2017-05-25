package server.events.game;

import server.event.XTEvent;
import server.player.Penguin;

public class SendJokeEvent extends XTEvent
{
	public SendJokeEvent()
	{
		super("u#sj");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendJokeMessage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}