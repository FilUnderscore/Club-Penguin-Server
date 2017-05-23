package server.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil 
{
	public static <T> String toString(List<T> list)
	{
		String str = "";
		
		if(list.size() > 0)
		{
			str += list.get(0).toString();
			
			for(int i = 1; i < list.size(); i++)
			{
				str += "," + list.get(i).toString();
			}
		}
		
		return str;
	}
	
	public static List<Integer> toInt(String str)
	{
		List<Integer> list = new ArrayList<>();
		
		if(str.length() > 0)
		{
			String[] oneArr = str.split(",");
			
			for(String arr : oneArr)
			{
				list.add(Integer.parseInt(arr));
			}
		}
		
		return list;
	}
}