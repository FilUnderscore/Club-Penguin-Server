package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import server.util.Logger;

public class Configuration
{
	/***
	 * Database
	 **/
	
	
		/**
		 * MySQL Server Host
		 */
		public String SQL_HOST;
		
		/**
		 * MySQL Server Username
		 */
		public String SQL_USER;
		
		/**
		 * MySQL Server Password
		 */
		public String SQL_PASS;
		
		/**
		 * MySQL Database Name
		 */
		public String SQL_DB;
	
		
	/***
	 * Ports
	 **/
	
		
		/**
		 * Default "Login" Port configured in SWF
		 */
		public int LOGIN_SERVER_PORT = 6112;
		
		/**
		 * Default "Unlock Items Online" Port configured in SWF
		 */
		public int REDEMPTION_SERVER_PORT = 6114;
	
		
	/***
	 * Game
	 **/
	
		
		/**
		 * Version of the Game that the Server supports, configured in SWF.
		 */
		public int GAME_VERSION = 153;
	
	private File file;
		
	public Configuration()
	{
		
	}
		
	public Configuration(File file)
	{
		this.file = file;
	}
	
	public void readConfig(Server server)
	{
		if(this.file == null)
			return;
		
		try
		{
			Element element = new SAXBuilder().build(new FileInputStream(file)).getRootElement();
		
			Element database = element.getChild("database");
		
			this.SQL_HOST = database.getChild("hostname").getValue();
			this.SQL_USER = database.getChild("username").getValue();
			this.SQL_PASS = database.getChild("password").getValue();
			this.SQL_DB = database.getChild("dbName").getValue();
			
			Element ports = element.getChild("ports");
			
			this.LOGIN_SERVER_PORT = Integer.parseInt(ports.getChild("login").getValue());
			this.REDEMPTION_SERVER_PORT = Integer.parseInt(ports.getChild("redemption").getValue());
			
			Element game = element.getChild("game");
			
			this.GAME_VERSION = Integer.parseInt(game.getChild("version").getValue());
			
			Logger.info("Loaded Configuration!", server);
		}
		catch(FileNotFoundException e)
		{
			Logger.info("No Server Configuration found. Some features might not work unless manually implemented.", server);
		}
		catch(JDOMException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Postcard cost
	 */
	public static int POSTCARD_SEND_COST = 10;
}