package server.util;

public class StringUtil 
{
	public static String getArguments(int index, int length, String[] text)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = index; i < length; i++)
		{
			sb.append(text[i] + " ");
		}
		
		return sb.toString();
	}
}