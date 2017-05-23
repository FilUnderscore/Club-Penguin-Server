package server.util;

import java.util.Random;

public class Number 
{
	public static int getRandom(int min, int max)
	{
		int n = new Random().nextInt(max);
		
		if(n < min)
		{
			n = min;
		}
		
		return n;
	}
}