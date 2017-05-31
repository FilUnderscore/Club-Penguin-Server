package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.json.JSONObject;

import server.data.Postcard;
import server.util.ListUtil;
import server.util.Logger;

public class Database 
{
	protected Server Server;
	protected Connection Connection;
	
	public Database()
	{
		
	}
	
	public Database(Server server)
	{
		this.Server = server;
	}
	
	public void connectMySQL(String dbUser, String dbPass, String dbHost, String dbName) throws Exception
	{
		this.Connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + dbName, dbUser, dbPass);
		
		if(this.Connection == null)
		{
			Logger.warning("Failed to connect to MySQL server.", this.Server);
		}
		else
		{
			Logger.info("Successfully connected to MySQL server.", this.Server);
		}
	}
	
	public boolean checkUserExists(String username) throws Exception
	{
		return this.Connection.prepareStatement("SELECT * FROM `users` WHERE username = '" + username + "';").executeQuery().next();
	}
	
	public String getCurrentPassword(String username) throws Exception
	{
		ResultSet set = this.Connection.prepareStatement("SELECT * FROM `users` WHERE username = '" + username + "';").executeQuery();
		
		if(set.next())
		{
			return set.getString("password");
		}
		
		return "";
	}
	
	public int getInvalidLogins(String username) throws Exception
	{
		ResultSet set = this.Connection.prepareStatement("SELECT * FROM `users` WHERE username = '" + username + "';").executeQuery();
		
		if(set.next())
		{
			if(Hours.hoursBetween(new DateTime(set.getLong("invalidLoginsTimestamp")), new DateTime()).getHours() >= 1)
			{
				this.updateInvalidLogins(username, 0);
				
				return 0;
			}
			
			return set.getInt("invalidLogins");
		}
		
		return 0;
	}
	
	public void updateInvalidLogins(String username, int count) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET invalidLogins = '" + count + "' WHERE username = '" + username + "';").executeUpdate();
	
		this.Connection.prepareStatement("UPDATE `users` SET invalidLoginsTimestamp = '" + new java.sql.Timestamp(System.currentTimeMillis()).toString() + "' WHERE username = '" + username + "';").executeUpdate();
	}
	
	public void updateLoginKey(String username, String loginKey) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET loginKey = '" + loginKey + "' WHERE username = '" + username + "';").executeUpdate();
	}
	
	public void updatePenguinClothing(List<Integer> clothes, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET clothing = '" + ListUtil.toString(clothes) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updatePenguinInventory(List<Integer> inventory, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET inventory = '" + ListUtil.toString(inventory) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateCurrentCoins(int newCoins, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET coins = '" + newCoins + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updatePenguinModStatus(int newModStatus, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET moderation = '" + newModStatus + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateFurnitureInventory(List<Integer> newInventory, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `igloos` SET ownedFurniture = '" + ListUtil.toString(newInventory) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateIglooInventory(List<Integer> newInventory, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `igloos` SET ownedIgloos = '" + ListUtil.toString(newInventory) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateIglooType(int iglooTypeId, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `igloos` SET igloo = '" + iglooTypeId + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateFloorType(int floorTypeId, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `igloos` SET floor = '" + floorTypeId + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateIglooFurniture(List<Integer> iglooFurniture, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `igloos` SET furniture = '" + ListUtil.toString(iglooFurniture) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateIglooMusic(int iglooMusicId, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `igloos` SET music = '" + iglooMusicId + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updatePenguinStamps(List<Integer> stamps, List<Integer> recentlyEarnedStamps, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `stamps` SET stamps = '" + ListUtil.toString(stamps) + "', recentStamps = '" + ListUtil.toString(recentlyEarnedStamps) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateStampbookCover(String cover, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `stamps` SET cover = '" + cover + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateFriends(List<Integer> friends, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET friends = '" + ListUtil.toString(friends) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public void updateIgnoreList(List<Integer> ignored, int userId) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET ignored = '" + ListUtil.toString(ignored) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public List<Integer> getPenguinInventoryById(int userId) throws Exception
	{
		ResultSet results = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + userId + "';").executeQuery();
		
		if(results.next())
		{
			return ListUtil.toInt(results.getString("inventory"));
		}
		
		return new ArrayList<>();
	}
	
	public List<Integer> getPenguinClothesById(int userId) throws Exception
	{
		ResultSet results = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + userId + "';").executeQuery();
		
		if(results.next())
		{
			return ListUtil.toInt(results.getString("clothing"));
		}
		
		return new ArrayList<>();
	}
	
	public List<Integer> getStampsById(int userId) throws Exception
	{
		//TODO: Convert to List<Stamp>
		return new ArrayList<>();
	}
	
	public String getStampbookCoverById(int userId) throws Exception
	{
		ResultSet results = this.Connection.prepareStatement("SELECT * FROM `stamps` WHERE id = '" + userId + "';").executeQuery();
		
		if(results.next())
		{
			return results.getString("cover");
		}
		
		return "";
	}
	
	public List<ServerInfo> getServerList() throws Exception
	{
		List<ServerInfo> servers = new ArrayList<>();
		
		ResultSet results = this.Connection.prepareStatement("SELECT * FROM `servers`;").executeQuery();
		
		while(results.next())
		{
			ServerInfo server = new ServerInfo(results.getInt("id"), results.getString("name"), results.getString("address"), results.getInt("port"), results.getBoolean("safeChatMode"));
			server.setPopulation(results.getInt("population"));
			
			servers.add(server);
		}
		
		return servers;
	}
	
	public List<ServerInfo> getOnlineServerList() throws Exception
	{
		List<ServerInfo> servers = new ArrayList<>();
		
		ResultSet results = this.Connection.prepareStatement("SELECT * FROM `servers`;").executeQuery();
		
		while(results.next())
		{
			if(results.getBoolean("online"))
			{
				ServerInfo server = new ServerInfo(results.getInt("id"), results.getString("name"), results.getString("address"), results.getInt("port"), results.getBoolean("safeChatMode"));
				server.setPopulation(results.getInt("population"));
				
				servers.add(server);
			}
		}
		
		return servers;
	}
	
	public void updateServer(ServerInfo info) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `servers` SET online = '" + 1 + "', population = '" + info.Count + "' WHERE id = '" + info.Id + "';").executeUpdate();
	}
	
	public void clearServer(int id) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `servers` SET online = '" + 0 + "', population = '" + 0 + "' WHERE id = '" + id + "';").executeUpdate();
	}
	
	public String getLoginKey(String username) throws Exception
	{
		ResultSet results = this.Connection.prepareStatement("SELECT * FROM `users` WHERE username = '" + username + "';").executeQuery();
	
		if(results.next())
		{
			return results.getString("loginKey");
		}
		
		return "";
	}
	
	public int getClientIdByUsername(String username) throws Exception
	{
		ResultSet results = this.Connection.prepareStatement("SELECT * FROM `users` WHERE username = '" + username + "';").executeQuery();
	
		if(results.next())
		{
			return results.getInt("id");
		}
		
		return 0;
	}
	
	public List<Integer> getClientFriendIdsById(int userId) throws Exception
	{
		List<Integer> friends = new ArrayList<>();
		
		ResultSet selfRes = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + userId + "';").executeQuery();
		
		if(selfRes.next())
		{
			friends = ListUtil.toInt(selfRes.getString("friends"));
		}
		
		return friends;
	}
	
	public List<Integer> getClientIgnoredIdsById(int userId) throws Exception
	{
		List<Integer> ignored = new ArrayList<>();
		
		ResultSet selfRes = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + userId + "';").executeQuery();
		
		if(selfRes.next())
		{
			ignored = ListUtil.toInt(selfRes.getString("ignored"));
		}
		
		return ignored;
	}
	
	public void saveIgnoredList(int userId, List<Integer> ignored) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET ignored = '" + ListUtil.toString(ignored) + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public List<Integer> getClientFriendRequestIdsById(int userId) throws Exception
	{
		List<Integer> requests = new ArrayList<>();
		
		ResultSet selfRes = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + userId + "';").executeQuery();
		
		if(selfRes.next())
		{
			requests = ListUtil.toInt(selfRes.getString("friendRequests"));
		}
		
		return requests;
	}

	public List<Integer> getOnlineClientFriendsById(int clientId) throws Exception
	{
		List<Integer> friends = new ArrayList<>();
		
		ResultSet selfRes = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + clientId + "';").executeQuery();
		
		if(selfRes.next())
		{
			for(int friendId : getClientFriendIdsById(clientId))
			{
				ResultSet friendRes = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + friendId + "';").executeQuery();
				
				if(friendRes.next())
				{
					if(friendRes.getBoolean("online"))
					{
						friends.add(friendId);
					}
				}
				
				friendRes.close();
			}
		}
		
		selfRes.close();
		
		return friends;
	}
	
	public void logChatMessage(int userId, String msg) throws Exception
	{
		this.Connection.prepareStatement("INSERT INTO `chathistory` (id,text) VALUES ('" + userId + "','" + msg + "');").executeUpdate();
	}
	
	public void saveMinsPlayed(int userId, int minsPlayed) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET minsPlayed = '" + minsPlayed + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public int getMinsPlayed(int userId) throws Exception
	{
		ResultSet result = this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + userId + "';").executeQuery();
		
		if(result.next())
		{
			return result.getInt("minsPlayed");
		}
		
		return -1;
	}
	
	public void updateOnlineStatus(int userId, boolean online) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET online = '" + (online ? "1" : "0") + "' WHERE id = '" + userId + "';").executeUpdate();
	}
	
	public ResultSet getUserDetails(int userId) throws Exception
	{
		return this.Connection.prepareStatement("SELECT * FROM `users` WHERE id = '" + userId + "';").executeQuery();
	}
	
	public ResultSet getIglooDetails(int userId) throws Exception
	{
		return this.Connection.prepareStatement("SELECT * FROM `igloos` WHERE id = '" + userId + "';").executeQuery();
	}
	
	public ResultSet getStampsDetails(int userId) throws Exception
	{
		return this.Connection.prepareStatement("SELECT * FROM `stamps` WHERE id = '" + userId + "';").executeQuery();
	}
	
	public String getPlayerString(int userId) throws Exception
	{
		String str = "";
		
		ResultSet details = this.getUserDetails(userId);
		
		if(details.next())
		{
			String username = details.getString("username");
			JSONObject clothing =  new JSONObject(details.getString("clothing"));
			JSONObject ranking = new JSONObject(details.getString("ranking"));
			
			str += userId + "|";
			str += username + "|";
			str += "1|";
			str += clothing.getInt("color") + "|";
			str += clothing.getInt("head") + "|";
			str += clothing.getInt("face") + "|";
			str += clothing.getInt("neck") + "|";
			str += clothing.getInt("body") + "|";
			str += clothing.getInt("hand") + "|";
			str += clothing.getInt("feet") + "|";
			str += clothing.getInt("pin") + "|";
			str += clothing.getInt("background") + "|";
			str += "0|0|0|";
			str += (ranking.getInt("rank") * 146);
		}
		
		return str;
	}
	
	public List<Postcard> getUserPostcards(int userId) throws Exception
	{
		List<Postcard> postcards = new ArrayList<>();
		
		ResultSet query = this.Connection.prepareStatement("SELECT * FROM `mail` WHERE toUser = '" + userId + "';").executeQuery();
		
		while(query.next())
		{
			postcards.add(new Postcard(query.getInt("id"), query.getInt("fromUser"), query.getString("fromName"), query.getInt("toUser"), query.getInt("mailType"), query.getString("details"), query.getTimestamp("timestamp").getTime(), query.getBoolean("mailRead")));
		}
		
		return postcards;
	}
	
	public void addPostcard(int receiverID, int senderID, String senderName, int postcardType, String details) throws Exception
	{
		this.Connection.prepareStatement("INSERT INTO `mail` (toUser,fromUser,fromName,mailType,details) VALUES ('" + receiverID + "','" + senderID + "','" + senderName + "','" + postcardType + "','" + details + "');").executeUpdate();
	}
	
	public void deletePostcardByReceipient(int postcardId, int receipient)
	{
		//TODO: Implement Mail
	}
	
	public void deletePostcardsBySender(int receipient, int sender)
	{
		//TODO: Implement Mail
	}
	
	public void updatePostcardRead(int playerID, int cardType) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `mail` SET mailRead = '" + 1 + "' WHERE toUser = '" + playerID + "' AND mailType = '" + cardType + "';").executeUpdate();
	}
	
	public void updateCoins(int userId, int coins) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET coins = '" + coins + "' WHERE id = '" + userId + "';").executeUpdate();
	}

	public void updateModerationData(int userId, String json) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET moderation = '" + json + "' WHERE id = '" + userId + "';").executeUpdate();
	}
}