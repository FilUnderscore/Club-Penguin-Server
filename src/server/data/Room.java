package server.data;

public class Room 
{
	private int room_id;
	private String room_key;
	private String name;
	private String display_name;
	private int music_id;
	private int is_member;
	private String path;
	private int max_users;
	private boolean jump_enabled = false;
	private boolean jump_disabled = true;
	private Object required_item;
	private String short_name;

	private int pin_id;
	private int pin_x;
	private int pin_y;
	
	public Room(int room_id, String room_key, String name, String display_name, int music_id, int is_member, String path, int max_users, boolean jump_enabled, boolean jump_disabled, Object required_item, String short_name)
	{
		this.room_id = room_id;
		this.room_key = room_key;
		this.name = name;
		this.display_name = display_name;
		this.music_id = music_id;
		this.is_member = is_member;
		this.path = path;
		this.max_users = max_users;
		this.jump_enabled = jump_enabled;
		this.jump_disabled = jump_disabled;
		this.required_item = required_item;
		this.short_name = short_name;
	}
	
	public Room(int room_id, String room_key, String name, String display_name, int music_id, int is_member, String path, int max_users, boolean jump_enabled, boolean jump_disabled, Object required_item, String short_name, int pin_id, int pin_x, int pin_y)
	{
		this(room_id, room_key, name, display_name, music_id, is_member, path, max_users, jump_enabled, jump_disabled, required_item, short_name);
		
		this.pin_id = pin_id;
		this.pin_x = pin_x;
		this.pin_y = pin_y;
	}
	
	public int getRoomId()
	{
		return this.room_id;
	}
	
	public String getRoomKey()
	{
		return this.room_key;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDisplayName()
	{
		return this.display_name;
	}
	
	public int getMusicId()
	{
		return this.music_id;
	}
	
	public int isMemberOnly()
	{
		return this.is_member;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public int getMaxUsers()
	{
		return this.max_users;
	}
	
	public boolean isJumpEnabled()
	{
		return this.jump_enabled;
	}
	
	public boolean isJumpDisabled()
	{
		return this.jump_disabled;
	}
	
	public Object getRequiredItem()
	{
		return this.required_item;
	}
	
	public String getShortName()
	{
		return this.short_name;
	}
	
	public int getPinId()
	{
		return this.pin_id;
	}
	
	public int getPinX()
	{
		return this.pin_x;
	}
	
	public int getPinY()
	{
		return this.pin_y;
	}
}