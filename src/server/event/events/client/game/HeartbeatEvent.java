package server.event.events.client.game;

import server.event.XTEvent;
import server.player.Penguin;

public class HeartbeatEvent extends XTEvent
{
	public HeartbeatEvent()
	{
		super("u#h");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		if(penguin.PreviousEvent instanceof HeartbeatEvent)
		{
			penguin.IdleMins++;
		}
		else
		{
			penguin.IdleMins = 0;
		}
		
		penguin.MinsPlayed++;
		
		try
		{
			this.getServer().getDatabase().saveMinsPlayed(penguin.Id, penguin.MinsPlayed);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		penguin.sendData(penguin.buildXTMessage("h", args[0]));
	}
}