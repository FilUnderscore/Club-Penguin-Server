package server.event.events.login;

import java.util.List;

import org.jdom.Element;

import server.ServerInfo;
import server.event.ChildEvent;
import server.event.Event;
import server.player.Penguin;

public class GetServerListEvent extends ChildEvent
{
	public GetServerListEvent(Event event, Penguin penguin, Object packet) 
	{
		super(event, penguin, packet);
	}
	
	public void process(Penguin penguin, Element packet) 
	{
		penguin.sendData("<msg t='sys'><body action='clearServerList' r='0'></body></msg>");
		
		try
		{
			List<ServerInfo> serverList = this.getEvent().getServer().getDatabase().getOnlineServerList();
			
			for(ServerInfo server : serverList)
			{
				String txt = "<server><id>" + server.Id + "</id><name>" + server.Name + "</name><ip>" + server.Address + "</ip><port>" + server.Port + "</port><population>" + server.Population + "</population><safe>" + server.SafeChatMode + "</safe></server>";
				penguin.sendData("<msg t='sys'><body action='addServerListEntry' r='0'>" + txt + "</body></msg>");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}