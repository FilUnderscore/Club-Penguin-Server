package server.event;

import server.player.Penguin;

public abstract class XTEvent extends Event
{
	private String extension;
	
	public XTEvent(String extension)
	{
		this.extension = extension;
	}
	
	public final XTEvent processEvent(Penguin penguin, String packet)
	{
		if(packet.startsWith("%"))
		{
			String[] data = packet.split("%");
			
			String[] args = new String[data.length - 4];
			
			for(int i = 4; i < data.length; i++)
			{
				args[i - 4] = data[i];
			}
			
			String ext = data[3];
			
			if(ext.equalsIgnoreCase(this.extension))
			{
				process(penguin, args);
				return this;
			}
			else if(ext.startsWith(this.extension))
			{
				process(penguin, ext, args);
				return this;
			}
			
			return null;
		}
		else
		{
			return null;
		}
	}
	
	public void process(Penguin penguin, String type, String[] args)
	{
		
	}
	
	public abstract void process(Penguin penguin, String[] args);
}