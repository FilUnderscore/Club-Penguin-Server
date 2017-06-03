package server.event;

import java.util.ArrayList;
import java.util.List;

import server.Server;
import server.api.CPServerAPI;
import server.event.events.client.login.ActionEvent;
import server.event.events.client.login.PolicyFileRequestEvent;
import server.player.Penguin;
import server.util.Logger;

public class EventManager 
{
	protected Server Server;
	
	protected List<Event> Events;
	
	public EventManager(Server server)
	{
		this.Server = server;
		this.Events = new ArrayList<>();
	}
	
	private void registerDefaultEvents()
	{
		this.registerEvent(new PolicyFileRequestEvent());
		this.registerEvent(new ActionEvent());
		
		for(Event event : CPServerAPI.getAPI().getEvents())
		{
			this.registerEvent(event);
		}
	}
	
	public void init()
	{
		registerDefaultEvents();
		
		Logger.info("{EventManager} Loaded " + this.Events.size() + " Events!", this.Server);
	}
	
	public void registerEvent(Event event)
	{
		if(!this.Events.contains(event))
		{
			event.setServer(this.Server);
			
			this.Events.add(event);
		}
	}
	
	public void handleEvents(Penguin client, String packet)
	{	
		Event handledEvent = null;
		
		System.out.println(packet);
		
		for(Event event : this.Events)
		{
			if(event.processEvent(client, packet) != null)
			{
				handledEvent = event;
			}
		}
		
		if(handledEvent == null)
		{
			Logger.warning('\n' + "Event unhandled '" + packet + "'!", this.Server);
		}
		else
		{
			client.PreviousEvent = handledEvent;
		}
	}
}