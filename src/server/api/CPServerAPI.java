package server.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import server.Console;
import server.Filter;
import server.Server;
import server.ServerPool;
import server.command.Command;
import server.data.Crumbs;
import server.event.Event;
import server.player.Penguin;

public final class CPServerAPI 
{
	private static CPServerAPI INSTANCE;
	
	private Crumbs crumbs;
	private ServerPool pool;
	private Console console;
	
	private List<Event> events = new ArrayList<>();
	private List<Command> commands = new ArrayList<>();
	
	public final void load()
	{
		this.load(false);
	}
	
	public final void load(boolean loadFromDB)
	{
		this.load(loadFromDB, null);
	}
	
	public final void load(boolean loadFromDB, File filterFile)
	{
		this.crumbs = new Crumbs();
		
		if(filterFile != null)
			Filter.initializeFilter(filterFile);
		
		if(loadFromDB)
			this.pool = new ServerPool(loadFromDB);
		else
		{
			this.pool = new ServerPool(loadFromDB);
		}
		
		this.console = new Console();
	}
	
	public final void registerServer(Server server)
	{
		this.pool.registerServer(server);
	}
	
	public final void registerEvent(Event event)
	{
		this.events.add(event);
	}
	
	/**
	 * 
	 * @param cmd
	 */
	public final void registerCommand(Command cmd)
	{
		this.commands.add(cmd);
	}
	
	public final List<Event> getEvents()
	{
		return Collections.unmodifiableList(this.events);
	}
	
	public final List<Command> getCommands()
	{
		return Collections.unmodifiableList(this.commands);
	}
	
	/**
	 * Attempt to retrieve Penguin from all servers on JVM instance.
	 * 
	 * @param penguinId - ID of the Penguin (Player)
	 * 
	 * @return Penguin (Player) instance if not null.
	 */
	public final Penguin getPenguin(int penguinId)
	{
		for(Server server : getServers())
		{
			if(server.getPenguin(penguinId) != null)
			{
				return server.getPenguin(penguinId);
			}
		}
		
		return null;
	}
	
	/**
	 * Retrieve Server (World) Population {@link java.lang.String data} from {@link server.ServerPool ServerPool} class.
	 */
	public final String getServerPopulationString()
	{
		return this.pool.getWorldPopulationString();
	}
	
	/**
	 * Retrieve Server List from {@link server.ServerPool ServerPool} class.
	 * 
	 * @return List containing all Servers.
	 */
	public final List<Server> getServers()
	{
		return this.pool.getServers();
	}
	
	public final Server getServer(int serverId)
	{
		for(Server server : this.getServers())
		{
			if(server.getServerInfo().Id == serverId)
			{
				return server;
			}
		}
		
		return null;
	}
	
	/**
	 * Get API Instance to access with.
	 * 
	 * @return Static API class instance
	 */
	public static CPServerAPI getAPI()
	{
		if(INSTANCE == null)
			INSTANCE = new CPServerAPI();
		
		return INSTANCE;
	}
}