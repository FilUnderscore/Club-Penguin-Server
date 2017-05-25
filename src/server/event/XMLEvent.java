package server.event;

import java.io.ByteArrayInputStream;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import server.player.Penguin;

public abstract class XMLEvent extends Event
{
	public final XMLEvent processEvent(Penguin penguin, String packet)
	{
		try
		{
			if(packet.startsWith("<"))
			{
				process(penguin, new SAXBuilder().build(new ByteArrayInputStream(packet.getBytes("UTF-8"))).getRootElement());
				return this;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public abstract void process(Penguin penguin, Element packet);
}