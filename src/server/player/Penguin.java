package server.player;

import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import server.Server;
import server.util.Logger;

public class Penguin 
{
	public Server Server;
	public Socket Socket;
	
	public int Id;
	public String Username;
	
	//Auth
	public String RandomKey = "POJ|oCk[dJsEKuja"; //Used to hash passwords with, generated on registration?
	public String LoginKey;

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
	
	public PunishmentStatus Status;
	
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
	
	public HashSet<Integer> Friends;
	
	public HashSet<Integer> Ignored;
	
	public ArrayList<Integer> Stamps;
	
	public ArrayList<Integer> RecentStamps;
	
	public String StampbookCover;
	
	/**
	 * Parental Controls
	 */
	public boolean SafeMode;
	public int SafeModeEggTimerMins;
	
	/**
	 * Age (in days since registration)
	 */
	public int Age;
	public int BannedAge;
	
	public int MinsPlayed;
	
	public int MembershipDaysLeft = 7;
	
	public Penguin(Socket socket, Server server)
	{
		this.Socket = socket;
		this.Server = server;
	}
	
	public void sendData(String data)
	{
		data += '\u0000';
		
		Logger.notice("Sending data: " + data, this.Server);
		
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
			if(client.getRoom() == this.Room)
			{
				client.sendData(data);
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
						//Ignore
					}
				}
				
				this.Inventory = this.Server.getDatabase().getPenguinInventoryById(this.Id);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
		
		return this.Id + "|" + this.Username + "|" + (enabled ? "1" : "0") + "|" + this.Color + "|" + this.Head + "|" + this.Face + "|" + this.Neck + "|" + this.Body + "|" + this.Hands + "|" + this.Feet + "|" + this.Flag + "|" + this.Photo + "|" + this.X + "|" + this.Y + "|" + this.Frame + "|1|" + this.MembershipStatus * 146;
	}
	
	public String getRoomString()
	{
		//String roomString = getClientString();
		String roomString = "";
		
		for(Penguin client : this.Server.getClients())
		{
			if(client.Room == this.Room && client.Id != this.Id)
			{
				roomString += client.getClientString() + "%";
			}
		}
		
		return roomString;
	}
	
	public void joinRoom(int roomID, int x, int y)
	{
		removePlayerFromRoom();
		
		this.Frame = 0;
		
		if(this.Room != 999)
		{
			this.Room = roomID;
			this.X = x;
			this.Y = y;
			this.sendData("%xt%jx%-1%" + roomID + "%");
			this.sendData("%xt%jr%-1%" + roomID + "%" + this.getClientString() + "%" + this.getRoomString());
			this.Server.sendData("%xt%ap%-1%" + this.getClientString() + "%", this);
			return;
		}
		
		//TODO
		//Crumbs see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb joinRoom line 220
	}
	
	public void move(int roomID, int x, int y)
	{
		this.X = x;
		this.Y = y;
		
		this.Server.sendData("%xt%sp%" + roomID + "%" + this.Id + "%" + x + "%" + y + "%", this);
	}
	
	public void sendMessage(int roomID, String text)
	{
		this.Server.sendData("%xt%sm%" + roomID + "%" + this.Id + "%" + text + "%", this);
	}
	
	public void addItem(int roomID, int itemId, int cost)
	{
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
		
		//402: Item not available;
		
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
		
		this.sendData("%xt%ai%" + roomID + "%" + itemId + "%" + cost + "%");
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
		//TODO: Save in MySQL database
	}
	
	public void deductCoins(int amount)
	{
		this.Coins -= amount;
		//TODO: Save in MySQL database
	}
	
	public void removePlayerFromRoom()
	{
		this.sendData("%xt%rp%-1%" + this.Id + "%");
	}
	
	public void handleBuddyOnline()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb handleBuddyOnline
	}
	
	public void handleBuddyOffline()
	{
		//TODO
		//see https://github.com/titshacking/RBSE/blob/master/Core/CPUser.rb handleBuddyOffline
	}
	
	public Socket getSocket()
	{
		return this.Socket;
	}

	public String getInventoryString() 
	{
		String inv = "%";
		
		for(int i : this.Inventory)
		{
			inv += i + "%";
		}
		
		return inv;
	}

	public void sendFrameUpdate(int roomID, int frameID) 
	{
		this.Frame = frameID;
		
		this.Server.sendData("%xt%sf%" + roomID + "%" + this.Id + "%" + frameID, this);
	}
	
	public void sendUpdate(String sub, int roomID, int itemID)
	{
		String str = "%xt%" + sub + "%" + roomID + "%" + this.Id + "%" + itemID + "%";
		
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
		this.Server.sendData(str, this);
	}
}