package server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import server.player.Penguin;
import server.player.StaffRank;
import server.util.Crypto;
import server.util.Logger;
import server.util.Time;

public abstract class Server 
{
	protected ServerInfo ServerInfo;
	protected Configuration Configuration;
	
	protected Database Database;
	
	protected ServerSocket ServerSocket;
	protected Thread ServerThread;
	
	protected ExecutorService Threads;
	
	protected List<Penguin> Clients;
	
	public Server(ServerInfo info)
	{
		this.ServerInfo = info;
	
		if(this instanceof Game)
		{
			this.ServerInfo.Type = ServerType.GAME;
		}
		else if(this instanceof Login)
		{
			this.ServerInfo.Type = ServerType.LOGIN;
		}
		
		this.Configuration = new Configuration(new File("config.cfg"));
		
		this.Threads = Executors.newCachedThreadPool();
		
		this.Database = new Database(this);
		
		try
		{
			this.Database.connectMySQL(this.Configuration.SQL_USER, this.Configuration.SQL_PASS, this.Configuration.SQL_HOST, this.Configuration.SQL_DB);
		}
		catch(Exception e)
		{
			Logger.warning("There was an error while attempting to connect to MySQL database: " + e.getMessage(), this);
			e.printStackTrace();
		}
		
		this.Clients = new ArrayList<>();
		
		try
		{
			init();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public abstract void init() throws Exception;
	
	public void onConnection(Socket socket)
	{
		Logger.info("Client Connected - " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort(), this);
		
		Penguin client = new Penguin(socket, this);
		
		this.Clients.add(client);
		
		this.ServerInfo.setPopulation(this.Clients.size());
		
		try
		{
			this.Database.saveServer(this.ServerInfo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		while(true)
		{
			try
			{
				byte[] data = new byte[65536];
				
				int read = socket.getInputStream().read(data);
				
				//There is data to be read.
				if(read > 0)
				{
					handleData(data, client);
				}
				else if(read <= 0 && Time.exceeded(0, 0))
				{
					onDisconnect(client);
					break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Logger.error("There was an error while reading data from a Socket: " + e.getMessage(), this);
				onDisconnect(client);
				break;
			}
		}
	}
	
	public abstract void onDisconnect(Penguin client);
	
	public void handleData(byte[] dataArr, Penguin client) throws Exception
	{
		String data = new String(dataArr, StandardCharsets.UTF_8);
		
		String[] packets = data.split("\0");
		
		for(String packet : packets)
		{
			Logger.info("Incoming Data: " + packet, this);
			String packetType = packet.substring(0, 1);
			switch(packetType)
			{
			case "<":
				handleXMLData(packet, client);
				break;
			case "%":
				handleXTData(packet, client);
				break;
			default:
				onDisconnect(client);
				break;
			}
		}
	}
	
	public void handleXMLData(String data, Penguin client) throws Exception
	{
		if(data.equalsIgnoreCase("<policy-file-request/>"))
		{
			handleCrossDomainPolicy(client, this.ServerInfo.Port);
			return;
		}
		
		Element element = new SAXBuilder().build(new ByteArrayInputStream(data.getBytes("UTF-8"))).getRootElement();
		
		if(element == null)
		{
			onDisconnect(client);
			return;
		}
		
		if(element.getChild("policy-file-request") != null)
		{
			handleCrossDomainPolicy(client, this.ServerInfo.Port);
			return;
		}
		
		if(element.getAttributeValue("t").equalsIgnoreCase("sys"))
		{
			String action = element.getChild("body").getAttributeValue("action");
			
			if(action.equalsIgnoreCase("login")) /** Client -> Server (Sending Credentials to be Authorized) **/
			{
				this.handleGameLogin(element, client);
			}
			else if(action.equalsIgnoreCase("verChk")) /** Client -> Server (Checking Client Version against Requested Server Game Version) **/
			{
				handleVersionCheck(element, client);
			}
			else if(action.equalsIgnoreCase("rndK")) /** Client -> Server (Requesting Random Key) **/
			{
				client.RandomKey = Crypto.generateRandomKey();
				client.sendData("<msg t='sys'><body action='rndK' r='-1'><k>" + client.RandomKey + "</k></body></msg>");
			}
			else if(action.equalsIgnoreCase("getServerList")) /** Client -> Server (Requesting Server List) **/
			{
				client.sendData("<msg t='sys'><body action='clearServerList' r='0'></body></msg>");
				
				List<ServerInfo> serverList = this.Database.getServerList();
				
				for(ServerInfo server : serverList)
				{
					String txt = "<server><id>" + server.Id + "</id><name>" + server.Name + "</name><ip>" + server.Address + "</ip><port>" + server.Port + "</port><population>" + server.Population + "</population><safe>" + server.SafeChatMode + "</safe></server>";
					client.sendData("<msg t='sys'><body action='addServerListEntry' r='0'>" + txt + "</body></msg>");
				}
			}
		}
		else
		{
			System.out.println("Not Sys: " + element.getAttributeValue("t"));
		}
	}
	
	public void handleXTData(String packet, Penguin client)
	{
		String[] cmds = packet.split("%");
		
		String cmd = cmds[3];
		
		if(cmd.contains("s#up"))
		{
			client.sendUpdate(cmd.split("#")[1], Integer.parseInt(cmds[4]), Integer.parseInt(cmds[5]));
			return;
		}
		
		switch(cmd)
		{
		case "j#js": //Join Server
			client.sendData("%xt%js%-1%" + client.Id + "%" + (client.IsEPF ? "1" : "0") + "%" + (client.Ranking == StaffRank.MODERATOR ? "1" : "0") + "%0%");
			//client.sendData("%xt%lp%-1%" + client.getClientString() + "%" + client.Coins + "%0%1440%100%1%4%1%7");
			break;
		case "j#jr": //Join Room
			client.joinRoom(Integer.parseInt(cmds[5]), Integer.parseInt(cmds[6]), Integer.parseInt(cmds[7]));
			break;
		case "u#sp": //Set Player Position
			client.move(Integer.parseInt(cmds[4]), Integer.parseInt(cmds[5]), Integer.parseInt(cmds[6]));
			break;
		case "j#grs": //Get Room Synchronized
			break;
		case "u#gp": //Get Player
			client.sendData("%xt%gp%1%" + client.Id + "|" + client.Username + "|" + client.MembershipStatus + "|" + client.Color + "|" + client.Head + "|" + client.Face + "|" + client.Neck + "|" + client.Body + "|" + client.Hands + "|" + client.Feet + "|" + client.Flag + "|" + client.Photo + "%");
			break;
		case "i#gi": //Get Inventory
			client.sendData("%xt%gps%-1%" + client.Id + "%9|10|11|14|20|183%");
			client.sendData("%xt%glr%-1%3555%"); //Revision?
			client.sendData("%xt%lp%-1%" + client.getClientString() + "%" + client.Coins + "%" + (client.SafeMode ? "1" : "0") + "%" + client.SafeModeEggTimerMins + "%" + (System.currentTimeMillis() / 1000L) + "%" + client.Age + "%" + client.BannedAge + "%" + client.MinsPlayed + "%" + client.MembershipDaysLeft + "%");
			client.sendData("%xt%gi%-1" + client.getInventoryString());
			client.joinRoom(100, 330, 330);
			break;
		case "i#ai": //Add Item
			int cost = 0;
			
			if(cmds.length == 7)
			{
				cost = Integer.parseInt(cmds[6]);
			}
			
			client.addItem(Integer.parseInt(cmds[4]), Integer.parseInt(cmds[5]), cost);
			break;
		case "u#h": //Keep Alive
			client.sendData("%xt%h%" + cmds[4] + "%");
			break;
		case "u#sf": //Set Frame
			client.sendFrameUpdate(Integer.parseInt(cmds[4]), Integer.parseInt(cmds[5]));
			break;
		case "m#sm": //Send Message
			client.sendMessage(Integer.parseInt(cmds[4]), cmds[6]);
			break;
		case "f#epfgf": //Get Field-Op (EPF)
			break;
		default:
			//client.sendError(10005);
			System.out.println("Unhandled Command: " + cmd + "\n\n");
			break;
		}
	}
	
	public static void handleCrossDomainPolicy(Penguin client, int port)
	{
		client.sendData("<cross-domain-policy><allow-access-from domain='*' to-ports='" + port + "'/></cross-domain-policy>");
	}
	
	public static void handleVersionCheck(Element data, Penguin client)
	{
		int version = Integer.parseInt(data.getChild("body").getChild("ver").getAttributeValue("v"));
		
		if(version == server.Configuration.GAME_VERSION)
		{
			client.sendData("<msg t='sys'><body action='apiOK' r='0'></body></msg>"); /** Up-to-date SWF Game Version **/
		}
		else
		{
			client.sendData("<msg t='sys'><body action='apiKO' r='0'></body></msg>"); /** Outdated SWF Game Version **/
		}
	}
	
	public void handleGameLogin(Element data, Penguin client)
	{
		try
		{
			String username = data.getChild("body").getChild("login").getChild("nick").getValue();
			String password = data.getChild("body").getChild("login").getChild("pword").getValue();
			
			System.out.println("Nick: " + username);
			System.out.println("Pass: " + password);
			
			if(username.matches("/^[[:alpha:]]+$/"))
			{
				client.sendError(100);
				return;
			}
			
			if(password.length() < 32)
			{
				client.sendError(101);
				return;
			}
			
			boolean exists = this.Database.checkUserExists(username);
			if(!exists)
			{
				client.sendError(100);
				return;
			}
			
			int invalidLogins = this.Database.getInvalidLogins(username);
			if(invalidLogins >= 5)
			{
				client.sendError(150);
				return;
			}
			
			if(this.ServerInfo.Type == ServerType.LOGIN)
			{
				String hash = Crypto.encryptPass(Database.getCurrentPassword(username), client.RandomKey);
				
				if(!password.equalsIgnoreCase(hash))
				{
					int curInvalidAttempts = this.Database.getInvalidLogins(username);
					curInvalidAttempts++;
					this.Database.updateInvalidLogins(username, curInvalidAttempts);
					client.sendError(101);
					return;
				}
			}
			else
			{
				String hash = Crypto.encryptLoginKey(this.Database.getLoginKey(username), client.RandomKey);
				
				if(!password.equalsIgnoreCase(hash))
				{
					client.sendError(101);
					return;
				}
			}
			
			String bannedStatus = this.Database.getBannedStatus(username);
			if(bannedStatus == "PERMBANNED")
			{
				client.sendError(603);
				return;
			}
			else if(bannedStatus == "TEMPBANNED")
			{
				//TODO
				//Round time to ban and send error (601%'time')
			}
			
			String loginKey = Crypto.encodeMD5(new StringBuilder(client.RandomKey).reverse().toString());
			
			this.Database.updateLoginKey(username, loginKey);
			
			int clientId = this.Database.getClientIdByUsername(username);
			
			//%   xt  % l  % -1 %   1    % abcd... %    0    %      0      %
			//protocol,type,rmid,clientid,loginKey,friends(|),worldpopulation(|)
			client.sendData("%xt%l%-1%" + clientId + "%" + loginKey + "%0%");
			
			client.Id = clientId;
			client.LoginKey = loginKey;
			
			if(this.ServerInfo.Type == ServerType.GAME)
			{
				client.Username = username;
				
				client.loadPenguin();
				client.loadIgloo();
				client.loadStamps();
				
				client.handleBuddyOnline();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Logger.error("There was an error while attempting to log a Client in: " + e.getMessage(), this);
		}
	}

	public abstract void stop() throws Exception;

	public final void sendData(String data, Penguin client)
	{
		for(Penguin c : getClients())
		{
			if(c.Room == client.Room && c.Id != client.Id)
			{
				c.sendData(data);
			}
		}
	}
	
	public final List<Penguin> getClients()
	{
		return this.Clients;
	}
	
	public final ServerInfo getServerInfo()
	{
		return this.ServerInfo;
	}
	
	public final Database getDatabase()
	{
		return this.Database;
	}
}