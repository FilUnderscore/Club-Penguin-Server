package server.event.events.client.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import server.ServerType;
import server.api.CPServerAPI;
import server.event.ChildEvent;
import server.event.XMLEvent;
import server.player.Penguin;
import server.util.Crypto;
import server.util.ListUtil;

public class LoginEvent extends ChildEvent
{
	public LoginEvent(XMLEvent event, Penguin penguin, Object packet)
	{
		super(event, penguin, packet);
	}
	
	public void process(Penguin penguin, Element packet) 
	{
		try
		{
			String username = packet.getChild("body").getChild("login").getChild("nick").getValue();
			String clientHash = packet.getChild("body").getChild("login").getChild("pword").getValue();
			
			if(username.matches("/^[[:alpha:]]+$/"))
			{
				penguin.sendError(100);
				return;
			}
			
			if(clientHash.length() < 32)
			{
				penguin.sendError(101);
				return;
			}
			
			if(!this.getEvent().getServer().getDatabase().checkUserExists(username))
			{
				penguin.sendError(100);
				return;
			}
			
			if(this.getEvent().getServer().getDatabase().getInvalidLogins(username) >= 5)
			{
				penguin.sendError(150);
				return;
			}
			
			if(this.getEvent().getServer().getServerInfo().Type == ServerType.LOGIN)
			{
				String hash = Crypto.encryptPass(this.getEvent().getServer().getDatabase().getCurrentPassword(username), penguin.RandomKey);
				
				if(!clientHash.equalsIgnoreCase(hash))
				{
					this.getEvent().getServer().getDatabase().updateInvalidLogins(username, this.getEvent().getServer().getDatabase().getInvalidLogins(username) + 1);
					penguin.sendError(101);
					return;
				}
			}
			else
			{
				String hash = Crypto.encryptLoginKey(this.getEvent().getServer().getDatabase().getLoginKey(username), penguin.RandomKey);
				
				if(!clientHash.equalsIgnoreCase(hash))
				{
					penguin.sendError(1000); //or 101
					return;
				}
			}
			
			int clientId = this.getEvent().getServer().getDatabase().getClientIdByUsername(username);
			
			penguin.Id = clientId;
			
			penguin.loadModerationData();
			
			if(penguin.getRecentBan() != null && penguin.getRecentBan().getExpireTime() == -1)
			{
				penguin.sendError(603);
				return;
			}
			else if(penguin.getRecentBan() != null && !penguin.getRecentBan().hasExpired() && penguin.getRecentBan().getExpireTime() != -1)
			{
				penguin.sendData(penguin.buildXTMessage("e", 601, (Hours.hoursBetween(new DateTime(), new DateTime(penguin.getRecentBan().getExpireTime())))));
				return;
			}
			
			String loginKey = Crypto.encodeMD5(new StringBuilder(penguin.RandomKey).reverse().toString());
			
			this.getEvent().getServer().getDatabase().updateLoginKey(username, loginKey);
			
			String friends = "";
			
			try
			{
				List<Integer> friendData = this.getEvent().getServer().getDatabase().getOnlineClientFriendsById(clientId);
				Map<Integer, List<Integer>> friendMap = new HashMap<>();
				
				if(friendData.size() > 0)
				{	
					for(int i = 0; i < friendData.size(); i++)
					{
						int friendID = friendData.get(i);
						Penguin friend = CPServerAPI.getAPI().getPenguin(friendID);
						
						if(friend != null)
						{
							int serverID = friend.Server.getServerInfo().Id;
							
							List<Integer> friendTempList = new ArrayList<>();
							
							if(friendMap.containsKey(serverID))
								friendTempList = friendMap.get(serverID);
							
							friendTempList.add(friendID);
							
							friendMap.put(serverID, friendTempList);
						}
					}
					
					int i = 0;
					
					for(int key : friendMap.keySet())
					{
						if(i == 0)
						{
							friends += key + "|" + ListUtil.toString(friendMap.get(key));
						}
						else
						{
							friends += "," + key + "|" + ListUtil.toString(friendMap.get(key));
						}
						
						i++;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			penguin.sendData(penguin.buildXTMessage("l", -1, clientId, loginKey, friends, CPServerAPI.getAPI().getServerPopulationString()));
			
			penguin.LoginKey = loginKey;
			
			if(this.getEvent().getServer().getServerInfo().Type == ServerType.GAME)
			{
				penguin.Username = username;
				
				penguin.loadPenguin();
				penguin.loadIgloo();
				penguin.loadStamps();
				
				penguin.handleBuddyOnline();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}