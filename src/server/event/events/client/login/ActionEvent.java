package server.event.events.client.login;

import org.jdom.Element;

import server.event.XMLEvent;
import server.player.Penguin;
import server.util.Logger;

public class ActionEvent extends XMLEvent
{
	public void process(Penguin penguin, Element packet) 
	{
		if(packet.getAttribute("t") != null && packet.getAttributeValue("t").equalsIgnoreCase("sys"))
		{
			switch(packet.getChild("body").getAttributeValue("action").toLowerCase())
			{
			case "login":
				new LoginEvent(this, penguin, packet);
				break;
			case "verchk":
				new VersionCheckEvent(this, penguin, packet);
				break;
			case "rndk":
				new RandomKeyEvent(this, penguin, packet);
				break;
			case "getserverlist":
				new GetServerListEvent(this, penguin, packet);
				break;
			default:
				Logger.warning("Undefined event '" + packet.getChild("body").getAttributeValue("action").toLowerCase(), this.getServer());
				break;
			}
		}
	}
}