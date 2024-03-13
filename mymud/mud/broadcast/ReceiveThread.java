/* $Id: ReceiveThread.java,v 1.6 2004/04/30 15:26:42 tk1748 Exp $ */
package mud.broadcast;

import mud.*;
import mud.supporting.*;
import mud.trunk.*;
import mud.input.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
  This collects changes in the state of the stage
  from accross the network.

  A note to put in the write up:
  This routine was very slow the dump was
TRACE 131:
	java.net.SocketInputStream.socketRead0(SocketInputStream.java:Native method)
	java.net.SocketInputStream.read(SocketInputStream.java:129)
	java.io.BufferedInputStream.fill(BufferedInputStream.java:183)
	java.io.BufferedInputStream.read1(BufferedInputStream.java:222)
	java.io.BufferedInputStream.read(BufferedInputStream.java:277)
	java.io.FilterInputStream.read(FilterInputStream.java:90)
	mud.broadcast.ReceiveThread.run(ReceiveThread.java:73)

CPU SAMPLES BEGIN (total = 8496) Tue Mar 02 22:48:48 2004
rank   self  accum   count trace method
   1 54.39% 54.39%    4621    65 sun.awt.windows.WToolkit.eventLoop
   2 34.13% 88.52%    2900   131 java.net.SocketInputStream.socketRead0
   3  2.66% 91.18%     226   130 java.net.SocketInputStream.socketRead0
   4  1.48% 92.67%     126   109 java.net.SocketInputStream.socketRead0

for runhprof.

The problem was that on windows the socketRead0 routines calls uses a windows method
once per byte. This gave us a massive lag.

http://forum.java.sun.com/thread.jsp?forum=37&thread=371265&tstart=0&trange=15

The solution so far has been to run once a frame and check how many bytes are available
before reading them. Still a massive overhead tho, and we're working on it...

We sleep for a while, then we 

Solution has profile:
CPU SAMPLES BEGIN (total = 8745) Wed Mar 03 00:43:59 2004
rank   self  accum   count trace method
   1 51.46% 51.46%    4500    62 sun.awt.windows.WToolkit.eventLoop
   2 41.26% 92.72%    3608   129 java.lang.Thread.sleep
   3  3.10% 95.81%     271   105 java.net.SocketInputStream.socketRead0
   4  0.89% 96.71%      78   130 sun.awt.windows.Win32BlitLoops.Blit
   5  0.47% 97.18%      41    98 java.lang.Thread.sleep

*/

public class ReceiveThread extends Thread
{
    
    private InputStream in  ;
    private BufferedInputStream bis;
    private Stage        stage;
    private SellTicket   sellTicket;
    private ShortPipe pipe;
    private static final int CHECK_TIME = 0;
    private Receiver boss;
    private boolean stopNow = false;
    ArrayList al;
    private boolean lock = false;

    public ReceiveThread (Socket a, Stage s, Receiver b)
    {
	boss = b;
	pipe = new ShortPipe();
	stage = s;
	sellTicket = s.getTicket();
	al = new ArrayList();
	/* Create a output stream ro write too */
	try
	{
	    in = a.getInputStream();
	    //bis = new BufferedInputStream(in,300);
	}
	catch (IOException e)
	{
	    /* This needs to be replaced with something
	       to inform the user that the network is broken */
	    System.err.println("Receive thread");
	    s.getDirector().networkError();
	}
    }

    /**
       This is so that others can read form the pipe
     */
    protected ShortPipe getPipe()
    {
	return pipe;
    }


    public void stopNow()
    {
	stopNow = true;
    }

    public short[] getData()
    {
	while (lock) System.err.print(".");
	lock = true;
	Object[] o = al.toArray();
	lock = false;

	short[] out = new short[al.size()];

	for (int i = 0; i  < o.length; i++)
	{
	    out[i] = ((Short)o[i]).shortValue();
	}
	al.clear();
	return  out;
    }

    /**
       Attemts to send all the outstanding command
       accross the network
     */
    public void run()
    {
	boolean stop = false;
	Short s;
	short  i2, i3;
	int i1,n, g,h, available;

	byte[] tmp = new byte[1],buff = new byte[2];
	try
	{
	    while(!stopNow)
  	    {
		    i1 = (short)in.read();//buff,0,available);
		    i2 = (short)in.read();
//  		    for (int i = 0; i < available; i=i+2)
//  		    {
			i3 =(short)(((i1 & 0xFF) << 8) + (i2 & 0xFF));
		    while (lock && !stopNow)System.err.print(".");
		    lock = true;
		    al.add(new Short(i3));
		    lock = false;
		    //pipe.write(new Short(i3));
//  		    }
		    if (i2 == -1)
		    {
			/* End of the game */
			stage.getDirector().networkError();
			stopNow = true;
		    }
	    }
	}
	catch (Exception e)
	{
	    System.err.println("ReceiveThread run n/work error");
	    stage.getDirector().networkError();
	}
    }

    /**
       The old method of running, kept here so I dont
       loose it before the writeup...
     */
    synchronized public void oldrun()
    {
	boolean stop = false;
	Short s;
	short  i2, i3;
	int i1,n, g,h, available;

	byte[] tmp = new byte[1],buff = new byte[2];
	try
	{
	    while(!stopNow)
  	    {
		available = in.available();
		if (available != 0)
		{
		    if (available % 2 == 1) available= available - 1;
		    buff = new byte[available];

		    /* A short is 2 bytes */

		    i1 = in.read(buff,0,available);
		    for (int i = 0; i < available; i=i+2)
		    {
			i3 =(short)(((buff[i] & 0xFF) << 8) + (buff[i+1] & 0xFF));
			pipe.write(new Short(i3));
		    }
		    if (i1 == -1)
		    {
			/* End of the game */
			stage.getDirector().networkError();
		    }
		}
//  		try 
//  		{
//  		    /* wait for notfyAll */
//  		    //wait();
//  		} 
//  		catch (InterruptedException e)
//  		{
//  		}
	    }
	}
	catch (Exception e)
	{
	    System.err.println("ReceiveThread run n/work error");
	    stage.getDirector().networkError();
	}
    }
}
