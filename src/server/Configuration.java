package server;

import java.io.File;

public class Configuration
{
	/***
	 * MySQL
	 **/
	
	/**
	 * MySQL Server Host
	 */
	public String SQL_HOST = "127.0.0.1";
	
	/**
	 * MySQL Server Port
	 */
	public int SQL_PORT = 3306;
	
	/**
	 * MySQL Database Name
	 */
	public String SQL_DB = "clubpenguin";
	
	/**
	 * MySQL Server Username
	 */
	public String SQL_USER = "root";
	
	/**
	 * MySQL Server Password
	 */
	public String SQL_PASS = "";
	
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
	public int UNLOCKABLE_SERVER_PORT = 6113;
	
	/***
	 * Game
	 **/
	
	/**
	 * Version of the Game that the Server supports, configured in SWF.
	 */
	public static int GAME_VERSION = 153;
	
	public Configuration(File file)
	{
		
	}
}