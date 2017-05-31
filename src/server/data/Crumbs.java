package server.data;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import server.util.Logger;

public final class Crumbs 
{
	protected static Map<Integer, Item> Items;
	protected static Map<Integer, Room> Rooms;
	protected static Map<String, Minigame> Minigames;
	protected static Map<Integer, Mascot> Mascots;
	protected static Map<String, Map<Integer, Mail>> Postcards;
	
	static
	{
		try
		{
			initItems();
			initRooms();
			initMinigames();
			initMascots();
			initPostcards();
		}
		catch(Exception e)
		{
			Logger.error("There was an error while trying to load Crumbs: " + e.getMessage(), null);
			e.printStackTrace();
		}
	}
	
	private static void initItems() throws Exception
	{
		Items = new HashMap<>();
		
		JSONArray itemArr = new JSONArray(new String(Files.readAllBytes(Paths.get("res/paper_items.json")), StandardCharsets.UTF_8));
		
		for(int i = 0; i < itemArr.length(); i++)
		{
			JSONObject object = itemArr.getJSONObject(i);
			
			if(object != null)
			{
				Items.put(object.getInt("paper_item_id"), new Item(object.getInt("paper_item_id"), object.getInt("type"), object.getInt("cost"), object.getBoolean("is_member"), object.getString("label"), object.getString("prompt"), object.getInt("layer")));
			}
		}
		
		Logger.info("Loaded " + Items.size() + " Items!", null);
	}
	
	private static void initRooms() throws Exception
	{
		Rooms = new HashMap<>();
		
		JSONObject roomObj = new JSONObject(new String(Files.readAllBytes(Paths.get("res/rooms.json")), StandardCharsets.UTF_8));
		
		for(String key : roomObj.keySet())
		{
			JSONObject object = roomObj.getJSONObject(key);
			
			if(object != null)
			{
				int pin_id = 0, pin_x = 0, pin_y = 0;
				
				if(object.has("pin_id"))
					pin_id = object.getInt("pin_id");
				if(object.has("pin_x"))
					pin_x = object.getInt("pin_x");
				if(object.has("pin_y"))
					pin_y = object.getInt("pin_y");
				
				Rooms.put(object.getInt("room_id"), new Room(object.getInt("room_id"), object.getString("room_key"), object.getString("name"), object.getString("display_name"), object.getInt("music_id"), object.getInt("is_member"), object.getString("path"), object.getInt("max_users"), object.getBoolean("jump_enabled"), object.getBoolean("jump_disabled"), object.get("required_item"), object.getString("short_name"), pin_id, pin_x, pin_y));
			}
		}
		
		Logger.info("Loaded " + Rooms.size() + " Rooms!", null);
	}
	
	private static void initMinigames() throws Exception
	{
		Minigames = new HashMap<>();
		
		JSONObject gameObj = new JSONObject(new String(Files.readAllBytes(Paths.get("res/games.json")), StandardCharsets.UTF_8));
		
		for(String key : gameObj.keySet())
		{
			JSONObject object = gameObj.getJSONObject(key);
		
			if(object != null)
			{
				Minigames.put(key, new Minigame(object.getString("name"), object.getInt("room_id"), object.getInt("music_id"), object.getInt("stamp_group_id"), object.getString("path"), object.getBoolean("is_as3"), object.getBoolean("show_player_in_room")));
			}
		}
		
		Logger.info("Loaded " + Minigames.size() + " Minigames!", null);
	}
	
	private static void initMascots() throws Exception
	{
		Mascots = new HashMap<>();
		
		JSONObject mascotObj = new JSONObject(new String(Files.readAllBytes(Paths.get("res/mascots.json")), StandardCharsets.UTF_8));
		
		JSONArray mascotArr = mascotObj.getJSONArray("mascots");
		
		for(int i = 0; i < mascotArr.length(); i++)
		{
			JSONObject mascot = mascotArr.getJSONObject(i);
			
			if(mascot != null)
			{
				int[] ids = new int[0];
				
				JSONArray idArr = mascot.getJSONArray("ids");
				
				if(idArr != null)
				{
					ids = new int[idArr.length()];
					
					for(int j = 0; j < idArr.length(); j++)
					{
						ids[j] = idArr.getInt(j);
					}
				}
				
				Mascots.put(mascot.getInt("mascot_id"), new Mascot(mascot.getInt("mascot_id"), mascot.getString("name"), (!mascot.isNull("title") ? mascot.getString("title") : ""), mascot.getInt("gift_id"), ids));
			}
		}
		
		Logger.info("Loaded " + Mascots.size() + " Mascots!", null);
	}
	
	private static void initPostcards() throws Exception
	{
		Postcards = new HashMap<>();
		
		int count = 0;
		
		JSONObject postcardObj = new JSONObject(new String(Files.readAllBytes(Paths.get("res/postcards.json")), StandardCharsets.UTF_8));
		
		for(String key : postcardObj.keySet())
		{
			JSONObject postcardobj = postcardObj.getJSONObject(key);
			
			Map<Integer, Mail> postcardList = new HashMap<>();
			
			for(String k : postcardobj.keySet())
			{
				if(k.equalsIgnoreCase("order_position"))
				{
					continue;
				}
				
				JSONObject obj = postcardobj.getJSONObject(k);
				
				postcardList.put(Integer.parseInt(k), new Mail(obj.getString("subject"), obj.getBoolean("in_catalog")));
				
				count++;
			}
			
			Postcards.put(key, postcardList);
		}
		
		Logger.info("Loaded " + count + " Postcards!", null);
	}
	
	public static Item getItem(int itemId)
	{
		return Items.get(itemId);
	}
	
	public static Room getRoom(int roomId)
	{
		return Rooms.get(roomId);
	}
	
	public static Minigame getMinigame(String gameName)
	{
		return Minigames.get(gameName);
	}
	
	public static Mail getPostcard(int postcardID)
	{
		for(String key : Postcards.keySet())
		{
			Map<Integer, Mail> postcard = Postcards.get(key);
			
			if(postcard.containsKey(postcardID))
			{
				return postcard.get(postcardID);
			}
		}
		
		return null;
	}
}