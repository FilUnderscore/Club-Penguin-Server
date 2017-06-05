package server.event.events.client.game.epf;

import server.event.XTEvent;
import server.player.Penguin;
import server.util.Values;

public class EPFGetAgentEvent extends XTEvent
{
	public EPFGetAgentEvent() 
	{
		super("f#epfga");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("epfga", Integer.parseInt(args[0]), Values.getBool(penguin.IsEPF)));
	}
}