/* $Id: BroadcastThread.java,v 1.5 2004/03/19 10:05:44 tk1748 Exp $ */
package mud.broadcast;

import mud.*;
import mud.supporting.*;
import mud.trunk.*;
import mud.input.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
Here we send all the changes in the stage over the network.
The changes are stored in Stage.
*/

public class BroadcastThread extends Thread
{
    
    private OutputStream out;
    private BufferedOutputStream bos;
    private Stage        stage;
    private SellTicket   sellTicket;
    private boolean stop = false;
    byte[] toGo;
    boolean going = false;

    public BroadcastThread (Socket a, Stage s)
    {
	stage = s;
	sellTicket = s.getTicket();
	/* Create a output stream ro write too */
	try
	{
	    out = a.getOutputStream();
	    //bos = new BufferedOutputStream(out);
	}
	catch (IOException e)
	{
	    /* This needs to be replaced with something
	       to inform the user that the network is broken */
	    System.err.print(".");
	    stage.getDirector().networkError();
	    toGo = new byte[0];
	}
    }

    /**
       This takes data to broadcast
       @param in the data to boradcasr
     */
    protected void takeData(byte[] in)
    {
	toGo = new byte[in.length];
	System.arraycopy(in,0,toGo,0,in.length);
    }

    /**
       Attemts to send all the outstanding commands
       across the network - this should be changed to
       grab all commands, and then send them as an array?
     */
    public void run()
    {
	going = true;
	toBed();
	while (!stop)
	{
	    try
	    {
		out.write(toGo);
		out.flush();
	    }
	    catch (IOException e)
	    {
		stage.getDirector().networkError();
	    }
	    toBed();
	}
    }

    /**
       We sleep until the early morn ...
    */
    private synchronized void toBed()
    {
	try
	{
	    going = false;
	    wait();
	}
	catch (InterruptedException e)
	{
	    /* do nowt */
	    System.err.println("Broken in broadcast thread ");
	}
    }

    /**
       ...until mum makes us get up and go to school
     */
    public synchronized void wakeUpShitHead()
    {
	going = true;
	notifyAll();
    }

    /**
       This forces us to finish out loop
     */
    protected void stopNow()
    {
	stop = true;
    }
}
