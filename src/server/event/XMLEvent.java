package server.event;

import java.io.ByteArrayInputStream;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import server.player.Penguin;

public abstract class XMLEvent extends Event
{
	public final void processEvent(Penguin penguin, String packet)
	{
		try
		{
			if(packet.startsWith("<"))
			{
				process(penguin, new SAXBuilder().build(new ByteArrayInputStream(packet.getBytes("UTF-8"))).getRootElement());
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	public abstract void process(Penguin penguin, Element packet);
}