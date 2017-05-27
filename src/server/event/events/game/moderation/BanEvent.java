package server.event.events.game.moderation;

import server.event.XTEvent;
import server.player.Penguin;
import server.player.StaffRank;

public class BanEvent extends XTEvent
{
	public BanEvent() 
	{
		super("o#b");
	}

	public void process(Penguin penguin, String[] args) 
	{
		if(penguin.Ranking != StaffRank.MODERATOR)
		{
			return;
		}
		
		penguin.issueBan(Integer.parseInt(args[0]), Integer.parseInt(args[1]), "", -1L);
	}
}