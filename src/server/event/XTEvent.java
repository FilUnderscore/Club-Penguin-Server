package server.event;

import server.player.Penguin;

public abstract class XTEvent extends Event
{
	public final void processEvent(Penguin penguin, String packet)
	{
		if(packet.startsWith("%"))
		{
			String[] data = packet.split("%");
			
			String[] args = new String[data.length - 4];
			
			for(int i = 4; i < data.length; i++)
			{
				args[i - 4] = data[i];
			}
			
			process(penguin, data[3], args);
		}
	}
	
	public abstract void process(Penguin penguin, String type, String[] args);
}