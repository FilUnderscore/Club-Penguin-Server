package server.player;

import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONObject;

import server.Cache;
import server.Configuration;
import server.Filter;
import server.Server;
import server.ServerType;
import server.api.CPServerAPI;
import server.data.Crumbs;
import server.data.Postcard;
import server.event.Event;
import server.player.moderation.Ban;
import server.player.moderation.Mute;
import server.util.ListUtil;
import server.util.Values;

public class Penguin 
{
	public Server Server;
	public Socket Socket;
	
	public int Id;
	public String Username;
	
	//Auth
	public String RandomKey; //Used to hash passwords (Client/Server MD5-Swap)
	public String LoginKey; //Used to authenticate with Login (Client/Server MD5-Swap)

	public int Coins;
	
	public long JoinDate;

	public int Color;
	public int Head;
	public int Face;
	public int Neck;
	public int Body;
	public int Hands;
	public int Feet;
	public int Flag;
	public int Photo;
	
	public int MembershipStatus;
	
	public boolean IsEPF;
	
	public StaffRank Ranking;
	
	public List<Mute> Mutes;
	public List<Ban> Bans;
	
	/**
	 * Room (id)
	 */
	public int Room;
	
	/**
	 * Penguin Position
	 */
	public int X, Y;
	
	/**
	 * Current Frame in SWF
	 */
	public int Frame;
	
	/**
	 * Current Animation in SWF
	 */
	public int Action;
	
	/**
	 * Igloo Type (id)
	 */
	public int Igloo;
	
	/**
	 * Floor Type (id)
	 */
	public int Floor;
	
	/**
	 * Music (id)
	 */
	public int Music;

	public int Furniture;
	
	public List<Integer> Inventory;
	
	public HashSet<Integer> OwnedFurniture;
	
	public ArrayList<Integer> OwnedIgloos;
	
	public List<Integer> Friends;
	
	public List<Integer> Ignored;
	
	public List<Postcard> Mail;
	
	public ArrayList<Integer> Stamps;
	
	public ArrayList<Integer> RecentStamps;
	
	public String StampbookCover;
	
	/**
	 * Parental Controls
	 */
	public boolean SafeMode = false;
	public int SafeModeEggTimerMins = 1440;
	
	/**
	 * Age (in days since registration)
	 */
	public int Age;
	public int BannedAge;
	
	/**
	 * If zero, tutorial loads.
	 */
	public int MinsPlayed = 0;
	
	/**
	 * Default Value (1440) to stop timer showing up for permanent.
	 */
	public int MembershipDaysLeft = 1440;
	
	/**
	 * Events
	 */
	
	public Event PreviousEvent;
	
	/**
	 * If IdleMins reaches 10, disconnect client and only add per heartbeat
	 */
	public int IdleMins = 0;
	
	protected Penguin()
	{
	}
	
	public Penguin(Socket socket, Server server)
	{
		this.Socket = socket;
		this.Server = server;
	}
	
	public void sendData(String data)
	{
		data += '\u0000';
		
		try
		{
			this.Socket.getOutputStream().write(data.getBytes("UTF-8"));
			this.Socket.getOutputStream().flush();
		}
		catch(Exception e)
		{
			//Error while sending data to SWF Client.
			e.printStackTrace();
		}
	}
	
	public void sendRoom(String data)
	{
		for(Penguin client : this.Server.getClients())
		{
			if(client.getRoom() == this.Room && client.Id != this.Id)
			{
				if(!client.Ignored.contains(this))
				{
					client.sendData(data);
				}
			}
		}
	}
	
	public int getRoom()
	{
		return this.Room;
	}
	
	public void sendError(int i)
	{
		sendData("%xt%e%-1%" + i + "%");
	}
	
	public static Penguin loadPenguin(int userId, Server server)
	{
		if(server == null && CPServerAPI.getAPI().getPenguin(userId) != null)
		{
			return CPServerAPI.getAPI().getPenguin(userId);
		}
		
		if(server.getPenguin(userId) != null)
		{
			return server.getPenguin(userId);
		}
		
		if(Cache.getPenguin(userId) != null)
		{
			return Cache.getPenguin(userId);
		}
		
		try
		{
			Penguin penguin = new Penguin();
			
			penguin.Id = userId;
			penguin.Server = server;
			
			penguin.loadPenguin();
			
			Cache.savePenguin(penguin);
			
			return penguin;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Load Client Information.
	 */
	public void loadPenguin()
	{
		try
		{
			ResultSet data = this.Server.getDatabase().getUserDetails(this.Id);
		
			if(data.next())
			{
				this.Username = data.getString("nickname");
				this.Color = data.getInt("color");
				this.Coins = data.getInt("coins");
				
				this.Friends = this.Server.getDatabase().getClientFriendIdsById(this.Id);
				this.Ignored = this.Server.getDatabase().getClientIgnoredIdsById(this.Id);
				
				this.Age = Days.daysBetween(new DateTime(data.getTimestamp("joindate").getTime()), new DateTime()).getDays();
				
				this.MinsPlayed = this.Server.getDatabase().getMinsPlayed(this.Id);
				
				this.Ranking = StaffRank.valueOf(new JSONObject(data.getString("ranking")).getString("rank"));
				
				loadModerationData();
				
				JSONObject membership = new JSONObject(data.getString("membership"));
				
				this.MembershipStatus = membership.getInt("status");
				this.MembershipDaysLeft = membership.getInt("daysLeft");
				
				loadClothingData();
				
				this.Inventory = this.Server.getDatabase().getPenguinInventoryById(this.Id);
				
				//TODO: Add Igloos and Furniture
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadRoomData() throws Exception
	{
		List<Integer> roomData = this.Server.getDatabase().getRoomData(this.Id);
		
		this.Room = roomData.get(0);
		this.X = roomData.get(1);
		this.Y = roomData.get(2);
	}
	
	public void loadClothingData() throws Exception
	{
		List<Integer> clothes = this.Server.getDatabase().getPenguinClothesById(this.Id);
		
		if(clothes.size() > 0)
		{
			try
			{
				this.Color = clothes.get(0);
				this.Head = clothes.get(1);
				this.Face = clothes.get(2);
				this.Neck = clothes.get(3);
				this.Body = clothes.get(4);
				this.Hands = clothes.get(5);
				this.Feet = clothes.get(6);
				this.Flag = clothes.get(7);
				this.Photo = clothes.get(8);
			}
			catch(IndexOutOfBoundsException e)
			{
			}
		}
	}
	
	public void loadModerationData() throws Exception
	{
		ResultSet data = this.Server.getDatabase().getUserDetails(this.Id);
		
		if(data.next())
		{
			this.Bans = new ArrayList<>();
			this.Mutes = new ArrayList<>();
			
			if(data.getString("moderation").length() > 0)
			{
				JSONObject moderation = new JSONObject(data.getString("moderation"));
				
				JSONArray muteArr = moderation.getJSONArray("mutes");
				
				for(int i = 0; i < muteArr.length(); i++)
				{
					JSONObject mute = muteArr.getJSONObject(i);
					
					this.Mutes.add(new Mute(mute.getString("reason"), mute.getLong("expireTime"), mute.getInt("moderatorID")));
				}
				
				JSONArray banArr = moderation.getJSONArray("bans");
				
				for(int i = 0; i < banArr.length(); i++)
				{
					JSONObject ban = banArr.getJSONObject(i);
					
					this.Bans.add(new Ban(ban.getString("reason"), ban.getLong("expireTime"), ban.getInt("moderatorID")));
				}
			}
		}
	}
	
	public void savePenguin()
	{
		try
		{
			this.Server.getDatabase().updateFriends(this.Friends, this.Id);
			this.Server.getDatabase().saveIgnoredList(this.Id, this.Ignored);
			
			this.saveModerationData();
			
			this.Server.getDatabase().saveMinsPlayed(this.Id, this.MinsPlayed);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void saveModerationData() throws Exception
	{
		JSONObject moderation = new JSONObject();
		
		JSONArray muteArr = new JSONArray();
		
		for(int i = 0; i < this.Mutes.size(); i++)
		{
			Mute mute = this.Mutes.get(i);
			
			JSONObject muteObj = new JSONObject();
			
			muteObj.accumulate("reason", mute.getReason());
			muteObj.accumulate("expireTime", mute.getExpireTime());
			muteObj.accumulate("moderatorID", mute.getModeratorId());

			muteArr.put(i, muteObj);
		}
		
		JSONArray banArr = new JSONArray();
		
		for(int i = 0; i < this.Bans.size(); i++)
		{
			Ban ban = this.Bans.get(i);
			
			JSONObject banObj = new JSONObject();
			
			banObj.accumulate("reason", ban.getReason());
			banObj.accumulate("expireTime", ban.getExpireTime());
			banObj.accumulate("moderatorID", ban.getModeratorId());

			banArr.put(i, banObj);
		}
		
		moderation.accumulate("mutes", muteArr);
		moderation.accumulate("bans", banArr);
		
		this.Server.getDatabase().updateModerationData(this.Id, moderation.toString());
	}
	
	public void loadIgloo()
	{
		//TODO
		//Mysql see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb loadIglooInfo
	}
	
	public void loadStamps()
	{
		//TODO
		//Mysql see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb loadStampsInfo
	}
	
	public String getClientString()
	{
		boolean enabled = true;
		
		return this.Id + "|" + this.Username + "|" + (enabled ? "1" : "0") + "|" + this.Color + "|" + this.Head + "|" + this.Face + "|" + this.Neck + "|" + this.Body + "|" + this.Hands + "|" + this.Feet + "|" + this.Flag + "|" + this.Photo + "|" + this.X + "|" + this.Y + "|" + this.Frame + "|" + this.MembershipStatus + "|" + this.MembershipStatus * 146;
	}
	
	public String getRoomString()
	{
		//String roomString = getClientString();
		String roomString = "";
		
		for(Penguin client : this.Server.getClients())
		{
			if(client.Room == this.Room && client.Id != this.Id)
			{
				roomString += "%" + client.getClientString();
			}
		}
		
		return roomString;
	}
	
	public void joinRoom(int roomID, int x, int y)
	{
		removePlayerFromRoom();
		
		this.Frame = 0;
		
		if(Crumbs.getRoom(roomID) != null)
		{
			if(this.Server.getPenguins(roomID).size() <= Crumbs.getRoom(roomID).getMaxUsers())
			{
				this.Room = roomID;
				this.X = x;
				this.Y = y;
				
				this.sendData(buildXTMessage("jx", -1, roomID));
				this.sendData(buildXTMessage("jr", -1, roomID, this.getClientString() + this.getRoomString()));
				
				this.sendRoom(buildXTMessage("ap", -1, this.getClientString()));
				
				try
				{
					this.Server.getDatabase().saveRoomData(this.Id, roomID, x, y);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				this.sendError(210);
			}
		}
		else
		{
			this.sendError(213);
		}
	}
	
	public void move(int roomID, int x, int y)
	{
		this.X = x;
		this.Y = y;
		
		this.sendRoom(this.buildXTMessage("sp", roomID, this.Id, x, y));
	}
	
	public Mute getRecentMute()
	{
		Mute mute = null;
		
		for(Mute m : this.Mutes)
		{
			if(!m.hasExpired())
			{
				mute = m;
				break;
			}
		}
		
		return mute;
	}
	
	public Ban getRecentBan()
	{
		Ban ban = null;
		
		for(Ban b : this.Bans)
		{
			if(!b.hasExpired())
			{
				ban = b;
				break;
			}
		}
		
		return ban;
	}
	
	public void issueBan(int roomID, int userID, String reason, long length)
	{
		Penguin user = CPServerAPI.getAPI().getPenguin(userID);
		
		if(user != null)
		{
			if(user.getRecentBan() != null)
			{
				user.getRecentBan().setExpired(true);
				return;
			}
			
			user.Bans.add(new Ban(reason, (length != -1 ? new DateTime().plus(length).getMillis() : -1), this.Id));
			
			user.savePenguin();
			
			user.sendError(603);
		}
	}
	
	public void issueMute(int roomID, int userID, String reason, long length)
	{
		Penguin user = CPServerAPI.getAPI().getPenguin(userID);
		
		if(user != null)
		{
			if(user.getRecentMute() != null)
			{
				user.getRecentMute().setExpired(true);
				return;
			}
			
			user.Mutes.add(new Mute(reason, (length != -1 ? new DateTime().plus(length).getMillis() : -1), this.Id));
			
			user.savePenguin();
		}
	}
	
	public void sendMessage(int roomID, String text)
	{
		if(this.Ranking == StaffRank.MODERATOR && this.Server.getCommandManager().runCommand(this, text))
		{
			return;
		}
		
		if(this.getRecentMute() != null || text.length() >= 255)
		{
			return;
		}
		
		if(Filter.isInitialized() && Filter.contains(text.toLowerCase()))
		{
			return;
		}
		
		try
		{
			this.Server.getDatabase().logChatMessage(this.Id, text);

			this.sendRoom(this.buildXTMessage("sm", roomID, this.Id, text));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			this.sendError(1000);
		}
	}
	
	public void sendJokeMessage(int roomID, int jokeID)
	{
		this.sendRoom(this.buildXTMessage("sj", roomID, this.Id, jokeID));
	}

	public void sendEmote(int roomID, int emoteID) 
	{
		this.sendRoom(this.buildXTMessage("se", roomID, this.Id, emoteID));
	}
	
	public void sendSafeMessage(int roomID, int safeMsgID)
	{
		this.sendRoom(this.buildXTMessage("ss", roomID, this.Id, safeMsgID));
	}
	
	public void sendMascotMessage(int roomID, int mascotMsgID)
	{
		this.sendRoom(this.buildXTMessage("sma", roomID, this.Id, mascotMsgID));
	}
	
	public void sendTourMessage(int roomID, int tourMsgID)
	{
		this.sendRoom(this.buildXTMessage("st", roomID, this.Id, tourMsgID));
	}
	
	public void sendLineMessage(int roomID, int lineMsgID)
	{
		this.sendRoom(this.buildXTMessage("sl", roomID, this.Id, lineMsgID));
	}
	
	public void sendQuickMessage(int roomID, int quickMsgID)
	{
		this.sendRoom(this.buildXTMessage("sq", roomID, this.Id, quickMsgID));
	}
	
	public String buildXTMessage(String type, Object...args)
	{
		String str = "%xt%" + type + "%";
		
		for(Object arg : args)
		{
			str += arg + "%";
		}
		
		return str;
	}
	
	public void addItem(int roomID, int itemId)
	{
		if(Crumbs.getItem(itemId) == null)
		{
			this.sendError(402);
			return;
		}
		
		int cost = Crumbs.getItem(itemId).getCost();
		
		if(this.Inventory.contains(itemId))
		{
			this.sendError(400);
			return;
		}
		
		if(this.Coins < cost)
		{
			this.sendError(401);
			return;
		}
		
		this.Inventory.add(itemId);
		this.deductCoins(cost);
		
		try 
		{
			this.Server.getDatabase().updatePenguinInventory(this.Inventory, this.Id);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		this.sendData(this.buildXTMessage("ai", roomID, itemId, this.Coins));
	}
	
	public int getCurrentRoomCount()
	{
		int count = 0;
		
		for(Penguin client : this.Server.getClients())
		{
			if(client.Room == this.Room)
			{
				count++;
			}
		}
		
		return count;
	}
	
	public boolean isOnline(int userID)
	{
		for(Penguin client : this.Server.getClients())
		{
			if(client.Id == userID)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Penguin getClientById(int userID)
	{
		for(Penguin client : this.Server.getClients())
		{
			if(client.Id == userID)
			{
				return client;
			}
		}
		
		return null;
	}
	
	public void updateCurrentClothing()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentClothing
	}
	
	public void updateCurrentModStatus()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentModStatus
	}
	
	public void updateCurrentInventory()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentInventory
	}
	
	public void updateCurrentFurnInventory()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentFurnInventory
	}
	
	public void updateCurrentIglooInventory()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentIglooInventory
	}
	
	public void updateCurrentStamps()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentStamps
	}
	
	public void updateCurrentBuddies()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentBuddies
	}
	
	public void updateCurrentIgnoredBuddies()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb updateCurrentIgnoredBuddies
	}
	
	public void addCoins(int amount)
	{
		this.Coins += amount;
	
		saveCoins();
	}
	
	public void deductCoins(int amount)
	{
		this.Coins -= amount;
		
		saveCoins();
	}
	
	private void saveCoins()
	{
		try
		{
			this.Server.getDatabase().updateCoins(this.Id, this.Coins);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void removePlayerFromRoom()
	{
		this.sendRoom("%xt%rp%-1%" + this.Id + "%");
	}
	

	public String getIgnoredString() 
	{
		try
		{
			String str = "";
			
			for(int ignoreId : this.Ignored)
			{
				ResultSet res = this.Server.getDatabase().getUserDetails(ignoreId);
				
				if(res.next())
				{
					String name = res.getString("nickname");
					
					str += ignoreId + "|" + name + "%";
				}
			}
			
			return str;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void ignorePlayer(int roomID, int playerID)
	{
		if(!this.Ignored.contains(playerID))
		{
			this.Ignored.add(playerID);
		}
		
		savePenguin();
		
		this.sendData(this.buildXTMessage("an", roomID, playerID));
	}
	
	public void unignorePlayer(int roomID, int playerID)
	{
		if(this.Ignored.contains(playerID))
		{
			this.Ignored.remove(playerID);
		}
		
		savePenguin();
		
		this.sendData(this.buildXTMessage("rn", roomID, playerID));
	}
	
	public String getBuddyString()
	{
		try
		{
			String str = "";
			
			List<Integer> online = this.Server.getDatabase().getOnlineClientFriendsById(this.Id);
			
			for(int friendId : this.Friends)
			{
				ResultSet res = this.Server.getDatabase().getUserDetails(friendId);
				
				if(res.next())
				{
					String name = res.getString("nickname");
					
					str += friendId + "|" + name + "|" + (online.contains(friendId) ? "1" : "0") + "%";
				}
			}
			
			return str;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void handleBuddyOnline()
	{
		try
		{
			this.Server.getDatabase().updateOnlineStatus(this.Id, true);
		
			this.Friends = this.Server.getDatabase().getClientFriendIdsById(this.Id);
			
			sendFriends(this.buildXTMessage("bon", "-1", this.Id));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendFriends(String data)
	{
		if(this.Friends == null)
			return;
		
		for(int friendId : this.Friends)
		{
			Penguin friend = this.Server.getPenguin(friendId);
			
			if(friend != null)
			{
				friend.sendData(data);
			}
		}
	}
	
	public void handleBuddyOffline()
	{
		try
		{
			this.Server.getDatabase().updateOnlineStatus(this.Id, false);
			
			sendFriends(this.buildXTMessage("bof", "-1", this.Id));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void removeFriend(int roomID, int userID)
	{
		Penguin user = Penguin.loadPenguin(userID, this.Server);
		
		if(user != null)
		{
			ListUtil.removeFromList(this.Id, user.Friends);
			ListUtil.removeFromList(userID, this.Friends);
			
			if(user.Socket != null)
			{
				user.sendData(this.buildXTMessage("rb", roomID, this.Id));
			}
			
			user.savePenguin();
			savePenguin();
		}
	}
	
	public void sendFriendRequest(int roomID, int userID)
	{
		Penguin user = this.Server.getPenguin(userID);
		
		if(user != null)
		{
			user.sendData(this.buildXTMessage("br", roomID, this.Id, this.Username));
			
			user.savePenguin();
			savePenguin();
		}
	}
	
	public void findFriend(int roomID, int friendID)
	{
		Penguin friend = this.Server.getPenguin(friendID);
		
		if(friend != null)
		{
			this.sendData(this.buildXTMessage("bf", roomID, friend.Room));
		}
	}
	
	public void acceptFriendRequest(int roomID, int requestID)
	{
		Penguin request = Penguin.loadPenguin(requestID, this.Server);
		
		if(request != null)
		{
			request.Friends.add(this.Id);
			this.Friends.add(requestID);
			
			if(request.Socket != null)
			{
				request.sendData(this.buildXTMessage("ba", roomID, this.Id, this.Username));
			}
			
			request.savePenguin();
			savePenguin();
		}
	}
	
	public void loadMail(int roomID)
	{
		try
		{
			this.Mail = this.Server.getDatabase().getUserPostcards(this.Id);
		
			int unreadCount = 0;
			
			for(Postcard postcard : this.Mail)
			{
				if(!postcard.isRead())
				{
					unreadCount++;
				}
			}
			
			this.sendData(this.buildXTMessage("mst", roomID, unreadCount, this.Mail.size()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void getMail(int roomID)
	{
		String data = fetchMailData();
		this.sendData(this.buildXTMessage("mg", roomID, data));
	}
	
	public String fetchMailData()
	{
		String str = "";
		
		try
		{
			if(this.Mail == null || this.Mail.size() == 0)
			{
				this.Mail = this.Server.getDatabase().getUserPostcards(this.Id);
			}
			
			for(int i = 0; i < this.Mail.size(); i++)
			{
				Postcard postcard = this.Mail.get(i);
				
				String data = postcard.getFromUserName() + "|" + postcard.getFromUser() + "|" + postcard.getMailType() + "|" + postcard.getDetails() + "|" + postcard.getTimestamp();
				
				if(i == 0)
				{
					str += data;
				}
				else
				{
					str += "%" + data;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return str;
	}
	
	public void kickStop()
	{
		this.sendError(1);
	
		if(this.Server.getServerInfo().Type == ServerType.GAME)
		{
			this.handleBuddyOffline();
		}
	}
	
	public Socket getSocket()
	{
		return this.Socket;
	}

	public String getInventoryString() 
	{
		String inv = "";
		
		if(this.Inventory != null)
		{
			for(int i : this.Inventory)
			{
				inv += "%" + i;
			}
		}
		
		return inv;
	}

	public void sendFrameUpdate(int roomID, int frameID) 
	{
		this.Frame = frameID;
		
		this.sendRoom(this.buildXTMessage("sf", roomID, this.Id, frameID));
	}
	
	public void sendActionUpdate(int roomID, int actionID)
	{
		this.Action = actionID;
		
		this.sendRoom(this.buildXTMessage("sa", roomID, this.Id, actionID));
	}
	
	public void throwSnowball(int roomID, int x, int y)
	{
		this.sendRoom(this.buildXTMessage("sb", roomID, this.Id, x, y));
	}
	
	public void sendClothingUpdate(String sub, int roomID, int itemID)
	{
		String str = this.buildXTMessage(sub, roomID, this.Id, itemID);
		
		switch(sub)
		{
		case "upc": //Color
			this.Color = itemID;
			break;
		case "uph": //Head
			this.Head = itemID;
			break;
		case "upf": //Face
			this.Face = itemID;
			break;
		case "upn": //Neck
			this.Neck = itemID;
			break;
		case "upb": //Body
			this.Body = itemID;
			break;
		case "upa": //Hands
			this.Hands = itemID;
			break;
		case "upe": //Feet
			this.Feet = itemID;
			break;
		case "upp": //Background
			this.Photo = itemID;
			break;
		case "upl": //Pin
			this.Flag = itemID;
			break;
		}
		
		List<Integer> list = new ArrayList<>();
		list.add(this.Color);
		list.add(this.Head);
		list.add(this.Face);
		list.add(this.Neck);
		list.add(this.Body);
		list.add(this.Hands);
		list.add(this.Feet);
		list.add(this.Flag);
		list.add(this.Photo);
		
		try
		{
			this.Server.getDatabase().updatePenguinClothing(list, this.Id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.sendData(str);
		this.sendRoom(str);
	}

	public void checkPostcard(int roomID, int card) 
	{
		try
		{
			this.Server.getDatabase().updatePostcardRead(this.Id, card);
			this.sendData(this.buildXTMessage("mc", roomID, card));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendPostcard(int roomID, int userID, int cardType, String details)
	{
		try
		{
			if(Crumbs.getPostcard(cardType) == null || userID == this.Id)
				return;
			
			this.deductCoins(Configuration.POSTCARD_SEND_COST);
			
			this.sendData(this.buildXTMessage("ms", roomID, this.Coins));
			
			this.Server.getDatabase().addPostcard(userID, this.Id, this.Username, cardType, details);
			
			Penguin user = Server.getPenguin(userID);
			
			if(user != null)
			{
				String data = this.Id + "|" + this.Username + "|" + cardType + "|" + details + "|" + System.currentTimeMillis() + "|" + 0; //(unread)
				user.sendData(this.buildXTMessage("mr", roomID, data));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void handleCoinsDug(int roomID)
	{
		int coins = new Random().nextInt(29) + 1;
		
		this.buildXTMessage("cdu", this.Room, coins, this.Coins);
	}

	public void checkPuffleName(int roomID, String name)
	{
		boolean appropriate = false;
		
		Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = pattern.matcher(name);
		
		if(m.matches())
		{
			appropriate = true;
		}
		
		if(Filter.isInitialized() && Filter.contains(name.toLowerCase()))
		{
			appropriate = false;
		}
		
		this.sendData(this.buildXTMessage("checkpufflename", roomID, name, Values.getBool(appropriate)));
	}
}