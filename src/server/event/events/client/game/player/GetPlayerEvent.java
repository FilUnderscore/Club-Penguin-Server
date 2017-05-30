package server.event.events.client.game.player;

import server.event.XTEvent;
import server.player.Penguin;

public class GetPlayerEvent extends XTEvent
{
	public GetPlayerEvent() 
	{
		super("u#gp");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		int id = Integer.parseInt(args[1]);
		
		Penguin player;
		
		if(id == penguin.Id)
		{
			player = penguin;
		}
		else
		{
			player = Penguin.loadPenguin(id, this.getServer());
		}
		
		penguin.sendData(penguin.buildXTMessage("gp", args[0], player.Id + "|" + player.Username + "|" + player.MembershipStatus + "|" + player.Color + "|" + player.Head + "|" + player.Face + "|" + player.Neck + "|" + player.Body + "|" + player.Hands + "|" + player.Feet + "|" + player.Flag + "|" + player.Photo));
	}
}