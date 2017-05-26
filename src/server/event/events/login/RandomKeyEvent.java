package server.event.events.login;

import org.jdom.Element;

import server.event.ChildEvent;
import server.event.XMLEvent;
import server.player.Penguin;
import server.util.Crypto;

public class RandomKeyEvent extends ChildEvent
{
	public RandomKeyEvent(XMLEvent event, Penguin penguin, Object packet)
	{
		super(event, penguin, packet);
	}
	
	public void process(Penguin penguin, Element packet) 
	{
		penguin.RandomKey = Crypto.generateRandomKey();
		
		penguin.sendData("<msg t='sys'><body action='rndK' r='-1'><k>" + penguin.RandomKey + "</k></body></msg>");
	}
}