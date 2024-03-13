/* $Id: Broadcast.java,v 1.4 2004/03/19 18:48:32 tk1748 Exp $ */
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
The changes are stored in Stage. Basically this is just
a wrapper for BroadcastThread which does the sending
*/

public class Broadcast implements StageAccess
{
    KeyAction myKeyAction;
    BroadcastThread bct;  
    Socket socket;
    SellTicket sellTicket;

    /* Array to prep. to the data for sending from */
    byte[] toGo;

    /**
       Creates a new broadcaster that will
       broadcast through the connection propped
       open by 
       @param ka Input Keys Holder
     */
    public Broadcast(KeyAction ka, Stage s)
    {
	myKeyAction = ka;
	socket = ka.getSocket();

	/* synchronise with the client */
	sellTicket = s.getTicket();
	bct = new BroadcastThread(socket,s);
	bct.start();
    }


    /**
       Wait for client, synchronise timings, then
       Starts a broadcast thread to do the hard work
       @param s The Stage to be used
     */
    public void makeUp(Stage s)
    {
    }


    /**
       Tell the thread to do stuff over the network
       @param s The Stage to be used
     */
    public void perform(Stage s)
    {
	/* so we wait for the client at the start of the game */
	if (!s.getGameValue().netReady)
	{

	}
	else
	{
	short s1;
	Object[] input = sellTicket.nextTicket();
	toGo = new byte[input.length*2];

	for (int i = 0; i < input.length; i++)
	{
	    s1 = ((Short)input[i]).shortValue();
	    toGo[i*2] = (byte)(0xff & (s1 >> 8));
	    toGo[(i*2)+1] = (byte)(0xff &  s1);
	}

	while (bct.going == true) System.err.print(".");

	bct.takeData(toGo);
	/* We may need to check if the thread is alive here?*/
	bct.wakeUpShitHead();
	}
    }


    /**
       Tidy up and go home for tea
    */
    public void goHome()
    {
	bct.stopNow();
	bct.wakeUpShitHead();
	try
	{
	    socket.close();
	}
	catch (IOException e)
	{
	    System.err.println("couldnt close socket");
	    /* Do nothing, if its gone then ti doesn't matter */
	}
    }


}
