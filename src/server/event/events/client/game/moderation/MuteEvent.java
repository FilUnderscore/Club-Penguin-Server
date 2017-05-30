package server.event.events.client.game.moderation;

import server.event.XTEvent;
import server.player.Penguin;
import server.player.StaffRank;

public class MuteEvent extends XTEvent
{
	public MuteEvent() 
	{
		super("o#m");
	}

	public void process(Penguin penguin, String[] args) 
	{
		if(penguin.Ranking != StaffRank.MODERATOR)
		{
			return;
		}
		
		penguin.issueMute(Integer.parseInt(args[0]), Integer.parseInt(args[1]), "", -1L);
	}
}