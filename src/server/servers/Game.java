package server.servers;

import java.util.ArrayList;
import java.util.List;

import server.Server;
import server.ServerInfo;
import server.command.commands.AddInventoryCommand;
import server.command.commands.BanPlayerCommand;
import server.command.commands.StopServerCommand;
import server.event.events.game.client.HeartbeatEvent;
import server.event.events.game.client.JoinRoomEvent;
import server.event.events.game.client.JoinServerEvent;
import server.event.events.game.client.friends.BuddyAcceptEvent;
import server.event.events.game.client.friends.BuddyFindEvent;
import server.event.events.game.client.friends.BuddyRequestEvent;
import server.event.events.game.client.friends.GetBuddiesEvent;
import server.event.events.game.client.friends.RemoveBuddyEvent;
import server.event.events.game.client.inventory.AddItemEvent;
import server.event.events.game.client.inventory.GetInventoryEvent;
import server.event.events.game.client.mail.MailCheckedEvent;
import server.event.events.game.client.mail.MailGetEvent;
import server.event.events.game.client.mail.MailSendEvent;
import server.event.events.game.client.mail.MailStartEvent;
import server.event.events.game.client.message.SendEmoteEvent;
import server.event.events.game.client.message.SendJokeEvent;
import server.event.events.game.client.message.SendMessageEvent;
import server.event.events.game.client.message.SendSafeMessageEvent;
import server.event.events.game.client.moderation.BanEvent;
import server.event.events.game.client.moderation.KickEvent;
import server.event.events.game.client.moderation.MuteEvent;
import server.event.events.game.client.player.GetPlayerEvent;
import server.event.events.game.client.player.SendActionEvent;
import server.event.events.game.client.player.SendFrameEvent;
import server.event.events.game.client.player.SetPositionEvent;
import server.event.events.game.client.player.SnowballEvent;
import server.event.events.game.client.player.UpdatePlayerEvent;
import server.player.Penguin;
import server.util.Logger;

public class Game extends Server
{
	protected List<Penguin> IglooMap;
	
	public Game(ServerInfo info)
	{
		super(info);
		
		this.IglooMap = new ArrayList<>();
		
		save();
	}
	
	public List<Penguin> getIglooMap()
	{
		return this.IglooMap;
	}

	public void init() throws Exception
	{
		super.init();
		
		this.Database.updateServer(this.ServerInfo);
	}

	public void onDisconnect(Penguin client)
	{
		Logger.info("Client Disconnected - " + client.getSocket().getInetAddress().getHostAddress() + ":" + client.getSocket().getPort(), this);
		
		this.IglooMap.remove(client);
	
		client.handleBuddyOffline();
		client.removePlayerFromRoom();	
		
		this.Clients.remove(client);
		
		client = null;
		
		save();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() throws Exception
	{
		clear();
		
		for(Penguin client : this.Clients)
		{
			client.kickStop();
		}
		
		this.Clients.clear();
		
		this.Threads.shutdownNow();
		
		if(this.ServerThread != null)
			this.ServerThread.stop();
	}
	
	public void registerCommands()
	{
		this.CommandManager.registerCommand(new StopServerCommand());
		this.CommandManager.registerCommand(new AddInventoryCommand());
		this.CommandManager.registerCommand(new BanPlayerCommand());
	}

	public void registerEvents() 
	{
		this.EventManager.registerEvent(new JoinServerEvent());
		this.EventManager.registerEvent(new GetInventoryEvent());
		this.EventManager.registerEvent(new JoinRoomEvent());
		this.EventManager.registerEvent(new SetPositionEvent());
		this.EventManager.registerEvent(new HeartbeatEvent());
		this.EventManager.registerEvent(new UpdatePlayerEvent());
		this.EventManager.registerEvent(new GetPlayerEvent());
		this.EventManager.registerEvent(new AddItemEvent());
		this.EventManager.registerEvent(new SnowballEvent());
		this.EventManager.registerEvent(new SendFrameEvent());
		this.EventManager.registerEvent(new SendActionEvent());
		this.EventManager.registerEvent(new SendMessageEvent());
		this.EventManager.registerEvent(new SendJokeEvent());
		this.EventManager.registerEvent(new SendSafeMessageEvent());
		this.EventManager.registerEvent(new SendEmoteEvent());
		this.EventManager.registerEvent(new GetBuddiesEvent());
		this.EventManager.registerEvent(new BuddyRequestEvent());
		this.EventManager.registerEvent(new BuddyAcceptEvent());
		this.EventManager.registerEvent(new RemoveBuddyEvent());
		this.EventManager.registerEvent(new BuddyFindEvent());
		this.EventManager.registerEvent(new MailStartEvent());
		this.EventManager.registerEvent(new MailGetEvent());
		this.EventManager.registerEvent(new MailCheckedEvent());
		this.EventManager.registerEvent(new MailSendEvent());
		this.EventManager.registerEvent(new KickEvent());
		this.EventManager.registerEvent(new MuteEvent());
		this.EventManager.registerEvent(new BanEvent());
	}
}