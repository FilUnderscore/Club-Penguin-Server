package server.data;

public class Mascot
{
	public int mascot_id;
	public String name;
	public String title;
	public int gift_id;
	public int[] ids;
	
	public Mascot(int mascot_id, String name, String title, int gift_id, int[] ids)
	{
		this.mascot_id = mascot_id;
		this.name = name;
		this.title = title;
		this.gift_id = gift_id;
		this.ids = ids;
	}
	
	public int getMascotId()
	{
		return this.mascot_id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public int getGiftId()
	{
		return this.gift_id;
	}
	
	public int[] getIds()
	{
		return this.ids;
	}
}