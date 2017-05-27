package server.event.events.game.moderation;

import server.ServerPool;
import server.event.XTEvent;
import server.player.Penguin;
import server.player.StaffRank;

public class KickEvent extends XTEvent
{
	public KickEvent()
	{
		super("o#k");
	}

	public void process(Penguin penguin, String[] args) 
	{
		if(penguin.Ranking != StaffRank.MODERATOR)
		{
			return;
		}
		
		Penguin user = ServerPool.getPenguin(Integer.parseInt(args[1]));
	
		if(user != null)
		{
			user.sendError(610);
		}
	}
}