package server.event.events.client.game.epf;

import server.event.XTEvent;
import server.player.Penguin;

public class EPFGetMessageEvent extends XTEvent
{
	public EPFGetMessageEvent()
	{
		super("f#epfgm");
	}

	public void process(Penguin penguin, String[] args)
	{
		penguin.sendEPFMessageList(Integer.parseInt(args[0]));
	}
}