package server.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crypto 
{
	public static String hash(String str)
	{
		try
		{
			return new String(MessageDigest.getInstance("MD5").digest(str.getBytes(StandardCharsets.UTF_8)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return str;
	}

	public static String generateRandomString(int length)
	{
		String str = "";
		
		while(str.length() < length)
		{
			char[] charz = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', '|' };
			
			str += charz[new Random().nextInt(charz.length)];
		}
		
		return str;
	}
	
	public static String generateRandomKey()
	{
		return generateRandomString(7 + new Random().nextInt(3));
	}
	/*
	public static String encryptPassword(String password, String key) throws Exception
	{
		String swappedHash = swapMD5(HexMD5ForString(swapMD5(password) + key + "Y(02.>'H}t\":E1_root"));
		return swappedHash;
	}
	*/
	
	public static String flipHash(String hash)
	{
		String str = hash.substring(16, 32) + hash.substring(0, 16);
		
		return str;
	}
	
	public static String encryptPass(String pass, String key)
	{
		return flipHash(encodeMD5(flipHash(pass) + key + "Y(02.>\'H}t\":E1"));
	}
	
	public static String encryptLoginKey(String loginKey, String clientKey)
	{
		return flipHash(encodeMD5(loginKey + clientKey)) + loginKey;
	}
	
	public static String encodeMD5(String input)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			byte[] hash = null;
			
			md.update(input.getBytes("UTF-8"));
			
			hash = md.digest();
			
			if(hash != null)
			{
				StringBuilder out = new StringBuilder(2 * hash.length);
				
				for(byte b : hash)
				{
					out.append(String.format("%02x", b & 0xff));
				}
				
				return out.toString();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	/*
	public static String swapMD5(String hash)
	{
		if(hash.length() < 32)
		{
			hash = HexForBytes(hash.getBytes(StandardCharsets.UTF_8));
		}
		
		String swapped = hash.substring(16, 32);
		swapped += hash.subSequence(0, 16);
		return swapped;
	}
	
	public static String encryptPassword(String pass) throws Exception
	{
		String _loc1_ = HexMD5ForString(pass);
		_loc1_ = _loc1_.substring(16, 32) + "" + _loc1_.substring(0, 16);
		return _loc1_;
	}
	
	public static String getLoginHash(String pass, String rKey) throws Exception
	{
		String _loc2_ = encryptPassword(pass).toUpperCase();
		_loc2_ = encryptPassword(_loc2_ + rKey + encryptPassword((_loc2_ = _loc2_ + pass) + encryptPassword(pass = pass + _loc2_)));
		return _loc2_;
	}
	
	public static String HexForByte(byte b) {
	    String Hex = Integer.toHexString((int) b & 0xff);
	    boolean hasTwoDigits = (2 == Hex.length());
	    if(hasTwoDigits) return Hex;
	    else return "0" + Hex;
	}

	public static String HexForBytes(byte[] bytes) {
	    StringBuffer sb = new StringBuffer();
	    for(byte b : bytes) sb.append(HexForByte(b));
	    return sb.toString();
	}

	public static String HexMD5ForString(String text) throws Exception {
	    MessageDigest md5 = MessageDigest.getInstance("MD5");
	    byte[] digest = md5.digest(text.getBytes());
	    return HexForBytes(digest);
	}
	*/
}