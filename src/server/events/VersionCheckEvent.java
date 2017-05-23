package server.events;

import org.jdom.Element;

import server.Configuration;
import server.event.ChildEvent;
import server.event.XMLEvent;
import server.player.Penguin;

public class VersionCheckEvent extends ChildEvent
{
	public VersionCheckEvent(XMLEvent event, Penguin penguin, Object packet)
	{
		super(event, penguin, packet);
	}
	
	public void process(Penguin penguin, Element packet) 
	{
		int version = Integer.parseInt(packet.getChild("body").getChild("ver").getAttributeValue("v"));
		
		if(version == Configuration.GAME_VERSION)
		{
			penguin.sendData("<msg t='sys'><body action='apiOK' r='0'></body></msg>"); /** Up-to-date SWF Game Version **/
		}
		else
		{
			penguin.sendData("<msg t='sys'><body action='apiKO' r='0'></body></msg>"); /** Outdated SWF Game Version **/
		}
	}
}