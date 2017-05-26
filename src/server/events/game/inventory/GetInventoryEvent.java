package server.events.game.inventory;

import server.event.XTEvent;
import server.player.Penguin;
import server.util.Values;

public class GetInventoryEvent extends XTEvent
{
	public GetInventoryEvent() 
	{
		super("i#gi");
	}
	
	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("gps", args[0], penguin.Id)); //Get Stamps
		
		penguin.sendData(penguin.buildXTMessage("glr", args[0], 3555)); //Get Last Revision
		
		penguin.sendData(penguin.buildXTMessage("lp", args[0], penguin.getClientString(), penguin.Coins, Values.getBool(penguin.SafeMode) + (penguin.SafeMode ? ("%" + penguin.SafeModeEggTimerMins) : ""), penguin.MembershipDaysLeft, (System.currentTimeMillis() / 1000L), penguin.Age, penguin.BannedAge, penguin.MinsPlayed)); //Load Player
		
		penguin.sendData(penguin.buildXTMessage("gi", args[0] + penguin.getInventoryString()));
		
		penguin.joinRoom(100, 330, 330);
	}
}