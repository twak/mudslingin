/* $Id: ReadBillBoard.java,v 1.2 2004/03/04 15:57:42 tk1748 Exp $ */
package mud.broadcast;

import mud.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
  This makes a net connection and reads from our
  server. Note that here "server" Note that server here
  means billboard or game list, not the server in the client
  /server pair
 */
public class ReadBillBoard extends Thread
{

    private FeedBack feedBack;
    private String server;

    public ReadBillBoard(String s, FeedBack fb)
    {
	server = s+"server.pl";
	feedBack = fb;
    }

    /**
       The thready bit that reads the words from the
       server, creates a flyer then passes it back
       Code from java net tutorial...
     */
    public void run()
    {
	Flyer output = null;
	String inputLine;
	System.err.println("Trying to connect to \""+server+"\"");


	/* Make up the URL */
	try
	{
	    URL url = new URL(server);
	    
	    BufferedReader in = new BufferedReader(
		                new InputStreamReader(
			        url.openStream()      ));

	    /* Feed all input into new flyers, and 
	       "dispatch the flyers */
	    while ((inputLine = in.readLine()) != null)
	    {
		output = new Flyer();
		/* Add the details to the flyer */
		int errors = output.show(inputLine);
		if (errors==Flyer.BAD)
		{
		    /* We return a null if there is an error */
		    output = null;
		    System.err.println("Error parsing line from server "+inputLine);
		}
		/* report the value back up the chain o' command */
		if (errors!=Flyer.EOF) feedBack.newFlyer(output);
	    }
	}
	catch(IOException e)
	{
	    /* We give null feedback on a network error 
	       as it could be common */
	    feedBack.newFlyer(null);	   
	}
    }

}

