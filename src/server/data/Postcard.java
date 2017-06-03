package server.data;

public class Postcard 
{
	private int id;
	private int fromUser;
	private String fromName;
	private int toUser;
	private int mailType;
	private String details;
	private long timestamp;
	private boolean read;
	
	public Postcard(int id, int fromUser, String fromName, int toUser, int mailType, String details, long timestamp, boolean read)
	{
		this.id = id;
		this.fromUser = fromUser;
		this.fromName = fromName;
		this.toUser = toUser;
		this.mailType = mailType;
		this.details = details;
		this.timestamp = timestamp;
		this.read = read;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getFromUser()
	{
		return this.fromUser;
	}
	
	public String getFromUserName()
	{
		return this.fromName;
	}
	
	public int getToUser()
	{
		return this.toUser;
	}
	
	public int getMailType()
	{
		return this.mailType;
	}
	
	public String getDetails()
	{
		return this.details;
	}
	
	public long getTimestamp()
	{
		return this.timestamp;
	}
	
	public boolean isRead()
	{
		return this.read;
	}
	
	public void setRead(boolean b)
	{
		this.read = b;
	}
}