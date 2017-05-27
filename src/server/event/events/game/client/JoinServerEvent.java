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
		
		penguin.sendData(penguin.buildXTMessage("gps", args[0], penguin.Id)); //Get Stamps
		
		penguin.sendData(penguin.buildXTMessage("glr", args[0], 3555)); //Get Last Revision
		
		penguin.sendData(penguin.buildXTMessage("lp", args[0], penguin.getClientString(), penguin.Coins, Values.getBool(penguin.SafeMode) + (penguin.SafeMode ? ("%" + penguin.SafeModeEggTimerMins) : ""), penguin.MembershipDaysLeft, (System.currentTimeMillis() / 1000L), penguin.Age, penguin.BannedAge, penguin.MinsPlayed)); //Load Player
		
		penguin.joinRoom(100, 330, 330);
	}
}