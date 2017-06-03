package server.util;

public abstract interface Parseable 
{
	public abstract String toString();
	
	public abstract Parseable parse(String data);
}