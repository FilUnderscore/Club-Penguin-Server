package server.event;

import org.jdom.Element;

import server.player.Penguin;

public class ChildEvent 
{
	private Event event;
	
	public ChildEvent(Event event, Penguin penguin, Object packet)
	{
		this.event = event;
		
		if(this.event instanceof XTEvent)
		{
			process(penguin, (String[])packet);
		}
		else if(this.event instanceof XMLEvent)
		{
			process(penguin, (Element)packet);
		}
	}
	
	public void process(Penguin penguin, String[] data)
	{
		
	}
	
	public void process(Penguin penguin, Element packet)
	{
		
	}
	
	public final Event getEvent()
	{
		return this.event;
	}
}