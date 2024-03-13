/* $Id: PostBillBoard.java,v 1.3 2004/03/08 18:04:59 tk1748 Exp $ */
package mud.broadcast;

import mud.*;
import mud.input.*;
import mud.trunk.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
  This makes a net connection and reads from our
  server. Note that here "server" Note that server here
  means billboard or game list, not the server in the client
  /server pair
 */
public class PostBillBoard extends Thread
{

    private FeedBack feedBack;
    private String server, gameName,level;
    private boolean quit = true;
    private int port;
    private ServerSocket serverSocket;
    private Socket gameSocket;
    /* Timeout for waiting for a client in ms, before we
       judder and check the quit flag*/
    private static final int TIMEOUT = 300;
    private Stage myStage;

    public PostBillBoard(String s, String gn, String l, FeedBack fb, int p, Stage st)
    {
	server = s+"server.pl";
	gameName = gn;
	level    = l;
	feedBack = fb;
	port = p;
	myStage = st;
    }

    /**
       The thready bit that reads the words from the
       server, creates a flyer then passes it back
       Code from java net tutorial...
     */
    public void run()
    {
	InetAddress addrReal;
	String addr;
	String toSend;
	/* Add all our options to the server */
	System.err.println("Trying to connect to \""+server+"\"");


	try
	{
	    addrReal = InetAddress.getLocalHost ();
	}
	catch (UnknownHostException e)
	{
	    feedBack.newFlyer(null);	
	    return;
	}

	addr = addrReal.toString();
	addr = addr.substring(addr.indexOf('/')+1,addr.length());

	/* And now we post using the POST html standard CGI thingimy */
	toSend = server+"?ip=" + addr + "&name=" + gameName + "&level=" + level + "&port=" + port;

	/* Make up the URL */
	try
	{
	    URL url = new URL(toSend);
	    
	    BufferedReader in = new BufferedReader(
		                new InputStreamReader(
			        url.openStream()      ));
	    /* Note: we do not care what the server says */
	}
	catch(IOException e)
	{
	    /* We give null feedback on a network error 
	       as it could be common */
	    feedBack.newFlyer(null);
	    return;
	}
	/**
	   Now we wait for the server
	 */
	quit = false;
	while (!quit)
	{
	    try
	    { 
		/* set quit false, here, normally fails on timeout
		   so doesn't get changed ...*/
		quit = false;
		/* set timeout, so we can check for 
		   the user wanting to quit */
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(TIMEOUT);
		gameSocket = serverSocket.accept();
		/* Done with the server socket now */
		serverSocket.close();
		//gameSocket.setTcpNoDelay(true);
		/* We may want another game later ?*/
		//gameSocket.setReuseAddress(true);
                /* now we have a server socket, we have to get it into KeyAction */
		KeyAction ka = myStage.getDirector().getExecutive().getKeyAction();
		/* Huzah!. Set the variables wanted */
		ka.setSocket(gameSocket);
		/* the fact we are server */
		ka.setServer(true);
		/* the fact that server play in slot 1 */
		ka.setNumber(1);
		/* And now, we load the new level */
		myStage.getDirector().getExecutive().loadMud("data/"+level+".mmud",myStage);
		/* We break of the connect-attempt loop */
		quit = true;
	    }
	    catch (SocketTimeoutException e)
	    {
		/* We have to close it so we can open it
		   again .. */
		try
		{
		    serverSocket.close();
		}
		catch (IOException f)
		{ /* Dont care */
		}
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
		feedBack.newFlyer(null);
		return;
	    }
	    
	}

	/* Finally remove server from listing on billboard */
	toSend = server+"?ip=" + addr;
	try
	{
	    URL url = new URL(toSend);
	    
	    BufferedReader in = new BufferedReader(
		                new InputStreamReader(
			        url.openStream()      ));
	    /* Note: we do not care what the server says */
	}
	catch(IOException e)
	{
	    /* We give null feedback on a network error 
	       as it could be common */
	    feedBack.newFlyer(null);
	    return;
	}


    }

    /**
       Tells us to quit waiting for client
     */
    public void quit()
    {
	quit = true;
    }

}

