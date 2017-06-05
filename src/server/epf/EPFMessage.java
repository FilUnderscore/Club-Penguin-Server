package server.epf;

public class EPFMessage 
{
	private String text;
	private long timestamp;
	private int mascotID;
	
	public EPFMessage(String text, long timestamp, int mascotID)
	{
		this.text = text;
		this.timestamp = timestamp;
		this.mascotID = mascotID;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public long getTimestamp()
	{
		return this.timestamp;
	}
	
	public int getMascotID()
	{
		return this.mascotID;
	}
}