/* $Id: BillBoard.java,v 1.2 2004/03/04 15:57:42 tk1748 Exp $ */
package mud.broadcast;

import mud.*;
import mud.trunk.*;
import java.util.*;
import java.io.*; 
import java.net.*; 

/**
  This communicates with a mudsling http billboard to return a list of
  available "shows" or servers that are awaiting a client. This
  implements all the http code we need, so you can post to a billboard
  or read a billboard. Wonderful, just like Lord of the Dance...

  Almost redundent, really should communicate straight with threads. doh.
 */
public class BillBoard implements FeedBack
{

    /* This is non-null if there is an interested
       party in our results */
    FeedBack feedBack = null;
    String server;
    int port;
    PostBillBoard pBB;

    /**
       Creates a new billboard to post too/examine
       @param s the board to look at
     */
    public BillBoard(String s, int p)
    {
	server = s;
	port = p;
    }

    /**
       This initalises the server reading thread, all games
       are returned using newFlyer
       @param fb the class to report new games too
     */
    public void read(FeedBack fb)
    {
	ReadBillBoard rBB = new ReadBillBoard(server, this);
	feedBack = fb;
	rBB.start();
    }

    /**
       This is called if a new flyer appears
       @param f flyer found.
     */
    public void newFlyer(Flyer f)
    {
	/* report back on the changes */
	feedBack.newFlyer(f);
    }

    /**
       Posts an advert for the current game at the
       end of this billboard, and starts a thread
       listening for responses
       @param fileSelected the file to play
       @param gameNAme the game to play
       @param fb the place to send errors
     */
    public void post(String fileSelected, String gameName, FeedBack fb, Stage s )
    {
        pBB = new PostBillBoard(server, fileSelected, gameName, fb, port,s);
	pBB.start();
    }

    /**
       aborts connection attempt
     */
    public void quit()
    {
	pBB.quit();
    }
}
