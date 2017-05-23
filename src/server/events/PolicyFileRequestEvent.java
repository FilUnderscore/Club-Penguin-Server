package server.events;

import org.jdom.Element;

import server.event.XMLEvent;
import server.player.Penguin;

public class PolicyFileRequestEvent extends XMLEvent
{
	public void process(Penguin penguin, Element packet) 
	{
		if(packet.getName().equalsIgnoreCase("policy-file-request"))
		{
			penguin.sendData("<cross-domain-policy><allow-access-from domain='*' to-ports='" + this.getServer().getServerInfo().Port + "'/></cross-domain-policy>");
		}
	}
}