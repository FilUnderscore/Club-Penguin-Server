package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import server.util.ListUtil;
import server.util.Logger;

public class Database 
{
	protected Server Server;
	protected Connection Connection;
	
	public Database(Server server)
	{
		this.Server = server;
	}
	
	public void connectMySQL(String dbUser, String dbPass, String dbHost, String dbName) throws Exception
	{
		this.Connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + dbName, dbUser, dbPass);
		
		if(this.Connection == null)
		{
			Logger.warning("Failed to connect to MySQL server.", null);
		}
		else
		{
			Logger.info("Successfully connected to MySQL server.", null);
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
	
	public String getBannedStatus(String username) throws Exception
	{
		ResultSet set = this.Connection.prepareStatement("SELECT * FROM `users` WHERE username = '" + username + "';").executeQuery();
		
		if(set.next())
		{
			//String moderationStatus = set.getString("moderation");
			//JSONObject decodedStatus = new JSONObject(moderationStatus);
			//return decodedStatus.getString("isBanned");
			return "false";
		}
		
		return "";
	}
	
	public int getInvalidLogins(String username) throws Exception
	{
		ResultSet set = this.Connection.prepareStatement("SELECT * FROM `users` WHERE username = '" + username + "';").executeQuery();
		
		if(set.next())
		{
			return set.getInt("invalidLogins");
		}
		
		return 0;
	}
	
	public void updateInvalidLogins(String username, int count) throws Exception
	{
		this.Connection.prepareStatement("UPDATE `users` SET invalidLogins = '" + count + "' WHERE username = '" + username + "';").executeUpdate();
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
		this.Connection.prepareStatement("UPDATE `users` SET buddies = '" + ListUtil.toString(friends) + "' WHERE id = '" + userId + "';").executeUpdate();
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
	
	public void saveServer(ServerInfo info) throws Exception
	{
		if(this.Connection.prepareStatement("SELECT * FROM `servers` WHERE id = '" + info.Id + "';").executeQuery().next())
		{
			removeServer(info.Id);
		}
			
		this.Connection.prepareStatement("INSERT INTO `servers` (id, name, address, port, safeChatMode, population) VALUES ('" + info.Id + "','" + info.Name + "','" + info.Address + "','" + info.Port + "','" + (info.SafeChatMode ? "1" : "0") + "','" + info.Count + "');").executeUpdate();
	}
	
	public void removeServer(int id) throws Exception
	{
		this.Connection.prepareStatement("DELETE FROM `servers` WHERE id = '" + id + "';").executeUpdate();
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
	
	public List<Integer> getClientFriendsById(int userId) throws Exception
	{
		//TODO: Convert to List<Penguin>
		return new ArrayList<>();
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
	
	public int getUnreadPostcardCount(int userId)
	{
		//TODO: Implement Mail
		return 0;
	}
	
	public int getReceivedPostcardCount(int userId)
	{
		//TODO: Implement Mail
		return 0;
	}
	
	public List<Integer> getUserPostcards(int userId)
	{
		//TODO: Implement Mail (return List<Postcard>)
		return new ArrayList<>();
	}
	
	public int addPostcard(int receipient, String sender, int senderID, String postcardNotes, int postcardType, int timestamp)
	{
		//TODO: Implement Mail
		return 0;
	}
	
	public void deletePostcardByReceipient(int postcardId, int receipient)
	{
		//TODO: Implement Mail
	}
	
	public void deletePostcardsBySender(int receipient, int sender)
	{
		//TODO: Implement Mail
	}
	
	public void updatePostcardRead(int userId)
	{
		//TODO: Implement Mail
	}
}