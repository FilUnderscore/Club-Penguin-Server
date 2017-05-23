package server.event;

import java.util.ArrayList;
import java.util.List;

import server.Server;
import server.events.ActionEvent;
import server.events.PolicyFileRequestEvent;
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
		for(Event event : this.Events)
		{
			event.processEvent(client, packet);
		}
	}
}