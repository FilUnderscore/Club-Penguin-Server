package server.event;

import server.Server;
import server.player.Penguin;

public abstract class Event 
{
	private Server server;
	
	public abstract Event processEvent(Penguin penguin, String text);
	
	public final void setServer(Server server)
	{
		if(this.server == null && server != null)
			this.server = server;
		else if(server == null)
			this.server = null;
	}
	
	public final Server getServer()
	{
		return this.server;
	}
}