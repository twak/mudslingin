/* $Id: Receiver.java,v 1.8 2004/04/22 11:43:29 tk1748 Exp $ */
package mud.broadcast;

import mud.*;
import mud.supporting.*;
import mud.trunk.*;
import mud.input.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
   We collect all the changes in the stage from the
   network here.
   <BR>
   this _used to_ implement a SIZE sized buffer to elimate network
   jaggedness
*/

public class Receiver implements StageAccess
{
    private KeyAction myKeyAction;
    private ReceiveThread bcr;
    private ShortPipe pipe;
    private Socket socket;
    /* Vars for timing out after not data for so
       long */
    private int timeSinceData = 0;
    private static int FRAMES_TILL_QUIT;
    /* Buffer of frames */
    private ArrayList buffer = new ArrayList(10);
    /* This cant be 1:, ideal size of above buffer 0 disables */
    private static int SIZE = 0;
    /* Is this an odd-numbered frame ? */
    private boolean oddFrames = false;
    private GameValues set;
    private Stage myStage;

    /**
       Creates a new broadcaster that will
       broadcast through the connection propped
       open by 
       @param ka Input Keys Holder
     */
    public Receiver(KeyAction ka, Stage s)
    {
	myStage = s;
	myKeyAction = ka;
	socket = myKeyAction.getSocket();
        set = s.getGameValue();
	bcr = new ReceiveThread(socket,s,this);

	bcr.start();
	/* 6 secs till we quit */
	FRAMES_TILL_QUIT = s.getGameValue().fps*6;
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
    synchronized public void perform(Stage s)
    {
	/* run through the props, average last few frames
	   changes, and make those changes again */
	ArrayList a = s.allProps();
	Prop p;

	for (int i = 0; i < a.size(); i++)
	{
	    p = (Prop)a.get(i);
	    if (p instanceof NetProp)
	    {
		((NetProp)p).guessPosition();
	    }
	}
	
	short[] tmp = bcr.getData();
	if (tmp.length == 0)
	{
	    timeSinceData++;
	    /* This is so we wait for the server before starting */
	    if (!s.getGameValue().netReady)
	    {
		/* signiture string */
	        s.clientBroadcast("hello fellow mudslinger");
	    }
	}
	else
	{
			set.netReady = true;
			timeSinceData = 0;
			s.getBuyer().soldTicket(tmp);
	}

	if (timeSinceData > FRAMES_TILL_QUIT)
	{
	    System.err.println("lost contact with server, quitting");
	    s.getDirector().getExecutive().loadMud("data/netError.mud",s);
	    s.getGameValue().quitNow = true;
	}

//This is a data buffer if we ever need it again (bloody well hope not)
//  	if (SIZE != 0)
//  	{
//  	    /* Check the buffer isn't too out of date, only dump one
//  	       frame extra every other frame*/
//  	    if ((buffer.size() > SIZE+1 && oddFrames) || buffer.size() > SIZE+2)
//  	    {
//  		/* Excessive packets are just dumped straight */
//  		while (buffer.size() > SIZE+3)
//  		{
//  		    ArrayList toGo = ((ArrayList)buffer.get(  buffer.size()-1  ));
//  		    for (int i = 0; i < toGo.size(); i++)
//  		    {
//  			/***s.getBuyer().soldTicket(((Short)toGo.get(i)).shortValue());***/
//  			//System.err.print("*");
//  		    }
//  		    buffer.remove(buffer.size()-1);
//  		}
//  	    }
//  	    /* so we dont show, but do buffer first SIZE frames */
//  	    //if (buffer.size() >= SIZE)
//  	    //{
//  		/* Netrk stuff done, now we just pass the info along */
//  		ArrayList toGo = ((ArrayList)buffer.get(  buffer.size()-1  ));
//  		for (int i = 0; i < toGo.size(); i++)
//  		{
//  		    /***s.getBuyer().soldTicket(((Short)toGo.get(i)).shortValue());***/
//  		    //System.err.print(".");
//  		}
//  		buffer.remove(buffer.size()-1);
//  		//}
//  	}

//  	if (oddFrames) oddFrames = false; else oddFrames = true;

//  	//System.err.println(" "+buffer.size());
    }

    /**
       Tidy up and go home for tea
    */
    public void goHome()
    {
	try
	{
	    bcr.stopNow();
	    socket.close();
	}
	catch (IOException e)
	{
	    /* Do nothing, if its gone then it doesn't matter */
	}
    }


}
