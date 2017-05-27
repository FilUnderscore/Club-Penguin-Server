package server.data;

public class Mail 
{
	private String subject;
	private boolean in_catalog;
	
	public Mail(String subject, boolean in_catalog)
	{
		this.subject = subject;
		this.in_catalog = in_catalog;
	}
	
	public String getSubject()
	{
		return this.subject;
	}
	
	public boolean isInCatalog()
	{
		return this.in_catalog;
	}
}