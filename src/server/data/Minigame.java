package server.data;

public class Minigame 
{
	private String name;
	private int room_id;
	private int music_id;
	private int stamp_group_id;
	private String path;
	private boolean is_as3;
	private boolean show_player_in_room;

	public Minigame(String name, int room_id, int music_id, int stamp_group_id, String path, boolean is_as3, boolean show_player_in_room)
	{
		this.name = name;
		this.room_id = room_id;
		this.music_id = music_id;
		this.stamp_group_id = stamp_group_id;
		this.path = path;
		this.is_as3 = is_as3;
		this.show_player_in_room = show_player_in_room;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getRoomId()
	{
		return this.room_id;
	}
	
	public int getMusicId()
	{
		return this.music_id;
	}
	
	public int getStampGroupId()
	{
		return this.stamp_group_id;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	/**
	 * Is the game made with Actionscript 3?
	 */
	public boolean isAS3()
	{
		return this.is_as3;
	}
	
	/**
	 * Remove player from room on Game initiation?
	 */
	public boolean showPlayerInRoom()
	{
		return this.show_player_in_room;
	}
}