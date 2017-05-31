package server.servers;

import java.util.ArrayList;
import java.util.List;

import server.Server;
import server.ServerInfo;
import server.command.commands.AddInventoryCommand;
import server.command.commands.BanPlayerCommand;
import server.command.commands.StopServerCommand;
import server.event.events.client.game.GetRoomSynchronizedEvent;
import server.event.events.client.game.HeartbeatEvent;
import server.event.events.client.game.JoinRoomEvent;
import server.event.events.client.game.JoinServerEvent;
import server.event.events.client.game.epf.EPFGetFieldOpEvent;
import server.event.events.client.game.friends.BuddyAcceptEvent;
import server.event.events.client.game.friends.BuddyFindEvent;
import server.event.events.client.game.friends.BuddyRequestEvent;
import server.event.events.client.game.friends.GetBuddiesEvent;
import server.event.events.client.game.friends.RemoveBuddyEvent;
import server.event.events.client.game.igloo.GetRevisionEvent;
import server.event.events.client.game.ignore.GetIgnoredEvent;
import server.event.events.client.game.inventory.AddItemEvent;
import server.event.events.client.game.inventory.GetInventoryEvent;
import server.event.events.client.game.mail.MailCheckedEvent;
import server.event.events.client.game.mail.MailGetEvent;
import server.event.events.client.game.mail.MailSendEvent;
import server.event.events.client.game.mail.MailStartEvent;
import server.event.events.client.game.message.SendEmoteEvent;
import server.event.events.client.game.message.SendJokeEvent;
import server.event.events.client.game.message.SendMessageEvent;
import server.event.events.client.game.message.SendSafeMessageEvent;
import server.event.events.client.game.moderation.BanEvent;
import server.event.events.client.game.moderation.KickEvent;
import server.event.events.client.game.moderation.MuteEvent;
import server.event.events.client.game.ninja.GetNinjaRevisionEvent;
import server.event.events.client.game.player.GetPlayerEvent;
import server.event.events.client.game.player.SendActionEvent;
import server.event.events.client.game.player.SendFrameEvent;
import server.event.events.client.game.player.SetPositionEvent;
import server.event.events.client.game.player.SnowballEvent;
import server.event.events.client.game.player.UpdatePlayerEvent;
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
		
		this.EventManager.registerEvent(new GetRoomSynchronizedEvent());
		
		this.EventManager.registerEvent(new GetIgnoredEvent());
		
		this.EventManager.registerEvent(new EPFGetFieldOpEvent());
		
		this.EventManager.registerEvent(new GetNinjaRevisionEvent());
		
		this.EventManager.registerEvent(new GetRevisionEvent());
	}

	public String getIglooString()
	{
		String str = "";
		
		for(Penguin penguin : this.IglooMap)
		{
			str += penguin.Id + "|" + penguin.Username + "%";
		}
		
		return str;
	}
}