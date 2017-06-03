package server.data;

public class Item 
{
	private int paper_item_id;
	private int type;
	private int cost;
	private boolean is_member;
	private String label;
	private String prompt;
	private int layer;
	
	public Item(int paper_item_id, int type, int cost, boolean is_member, String label, String prompt, int layer)
	{
		this.paper_item_id = paper_item_id;
		this.type = type;
		this.cost = cost;
		this.is_member = is_member;
		this.label = label;
		this.prompt = prompt;
		this.layer = layer;
	}
	
	public int getPaperItemId()
	{
		return this.paper_item_id;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public int getCost()
	{
		return this.cost;
	}
	
	public boolean isMemberItem()
	{
		return this.is_member;
	}
	
	public String getLabel()
	{
		return this.label;
	}
	
	public String getPrompt()
	{
		return this.prompt;
	}
	
	public int getLayer()
	{
		return this.layer;
	}
}