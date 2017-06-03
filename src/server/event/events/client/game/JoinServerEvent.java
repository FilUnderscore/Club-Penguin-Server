package server.event.events.client.game;

import server.event.XTEvent;
import server.event.events.client.game.data.GetLastRevisionEvent;
import server.event.events.client.game.stamps.GetPlayerStampsEvent;
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
		
		new GetPlayerStampsEvent(this, penguin, args);
		
		new GetLastRevisionEvent(this, penguin, args);
		
		penguin.sendData(penguin.buildXTMessage("lp", args[0], penguin.getClientString(), penguin.Coins, Values.getBool(penguin.SafeMode) + (penguin.SafeMode ? ("%" + penguin.SafeModeEggTimerMins) : ""), penguin.MembershipDaysLeft, (System.currentTimeMillis() / 1000L), penguin.Age, penguin.BannedAge, penguin.MinsPlayed)); //Load Player
		
		try
		{
			penguin.loadRoomData();
			
			penguin.joinRoom(penguin.Room, penguin.X, penguin.Y);
		}
		catch(Exception e)
		{
			penguin.joinRoom(100, 330, 330);
		}
	}
}