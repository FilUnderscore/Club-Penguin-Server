package server.event.events.game.client;

import server.event.XTEvent;
import server.player.Penguin;
import server.player.StaffRank;
import server.util.Values;

public class JoinServerEvent extends XTEvent
{
	public JoinServerEvent()
	{
		super("j#js");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("js", args[0], penguin.Id, Values.getBool(penguin.IsEPF), Values.getBool(penguin.Ranking == StaffRank.MODERATOR), 0));
	}
}