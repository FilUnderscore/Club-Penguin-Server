package server.player;

import org.joda.time.DateTime;

public class Mute 
{
	private String reason;
	private long expireTime;
	private int moderatorID;
	
	public Mute(String reason, long expireTime, int moderatorID)
	{
		this.reason = reason;
		this.expireTime = expireTime;
		this.moderatorID = moderatorID;
	}
	
	public String getReason()
	{
		return this.reason;
	}
	
	public long getExpireTime()
	{
		return this.expireTime;
	}
	
	public int getModeratorId()
	{
		return this.moderatorID;
	}
	
	public boolean hasExpired()
	{
		return new DateTime().isAfter(expireTime);
	}
}