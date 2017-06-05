package server.event.events.client.game.epf;

import server.event.XTEvent;
import server.player.Penguin;
import server.util.Values;

public class EPFSetAgentEvent extends XTEvent
{
	public EPFSetAgentEvent() 
	{
		super("f#epfsa");
	}

	public void process(Penguin penguin, String[] args) 
	{
		penguin.sendData(penguin.buildXTMessage("epfsa", Integer.parseInt(args[0]), Values.getBool(penguin.IsEPF)));
	}
}