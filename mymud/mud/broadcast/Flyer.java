/* $Id: Flyer.java,v 1.2 2004/03/04 15:57:42 tk1748 Exp $ */
package mud.broadcast;

import mud.*;
import java.util.regex.*;

/**
  This stores all the data that we need to select and connect to
  a server. Reads it all in as strings
*/

public class Flyer
{
    String name, level;
    /* We make a difference between actual IP as seen by the
       server and the one given by the client to detect NATage */
    byte[] IP, IPExt;
    int port;

    static public int GOOD = 0776, EOF = 3530, BAD= 309;

    /**
       This is just a dummy, you want the Show(int) below!
     */
    protected void Flyer()
    {
	System.err.println("Dont use this");
    }

    /**
       This returns the string that is displayed for
       this flyer
       @return what our flyer is advertising
    */
    public String toString()
    {
	return name+": "+level;
    }


    /**
       Constructs a new show from a output
       line from the
       @param in the output line from server
       eg: "1078056147|137.222.10.58|127.0.0.1|itsNew|harry|1500 OK"
       @return code constant saying either OK, BAD or EOF
     */
    public int show(String in)
    {
	String[] splitUp;

	/* Reg-ex-to split up string to parts */
	splitUp = in.split("[|]");
	if (splitUp[0].compareTo("OK")==0) return EOF;
	if (splitUp.length != 6) 
	{
	    System.err.println("Warning: Dont understand line from server "+in);
	    return BAD;
	}
	/* Parse name */
	name = splitUp[4];

	/* Parse level */
	level = splitUp[3];

	/* Parse IP */
	IP    = doIP(splitUp[2]);
	IPExt = doIP(splitUp[1]);
	/* doIP returns null on an error */
	if (IP == null || IPExt == null)
	{
	    return BAD;
	}

	/* Parse port */
	try
	{
	    port = new Integer(splitUp[5]).intValue();
	}
	catch (NumberFormatException e)
	{
	    return BAD;
	}
	/* All is well, and set up */
	return GOOD;
    }


    private byte[] doIP(String w)
    {
	String[] splitUp;
	 splitUp = w.split("[.]");
	 /* Does this mean ip v6?*/
	 byte[] ip = new byte[splitUp.length];

	 try
	 {
	     for (int i = 0; i < splitUp.length; i++)
	     {
		 ip[i] = (byte)(new Integer(Integer.parseInt(splitUp[i]))).intValue();
	     }
	 }
	 catch (Exception e)
	 {
	     return null;
	 }
	 return ip;
    }	

    /**
       returns the address given by the client
       @return the address as an array of bytes
    */
    public byte[] getAddress()
    {
	return IP;
    }

    /**
       Gives the ip address as it appears to the
       server
       @return the address as an array of bytes
     */
    public byte[] getAddressExt()
    {
	return IPExt;
    }

    /**
       @return the name given by the server
     */
    public String getName()
    {
	return name;
    }

    /**
       @return The level, as given by the server
     */
    public String getLevel()
    {
	return level;
    }

    /**
       @return the port the server is running on
     */
    public int getPort()
    {
	return port;
    }
}
