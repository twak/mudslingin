/* $Id: SellTicket.java,v 1.11 2004/05/02 15:05:47 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.io.*;
import java.util.ArrayList;

/**
   This stores all the changes that happen on stage during the
   last turn in a que, from here they are sent down the network

   This also acts as a buffer of size Q_SIZE to compenstate in 
   differences between network speed, and fps etc...
 */
public class SellTicket
{
 //     private final static int Q_SIZE = 5000;
//      private int qStart, qEnd;
//      private short[] ticketWad = new short[Q_SIZE];
    private ShortPipe pipe;

    public  static final short MOVE     = 1;
    public static final short SPRITE    = 2;
    public static final short ADD_MUD   = 3;
    public static final short DEL_MUD   = 4;
    public static final short ADD_WATER = 5;
    public static final short DEL_WATER = 6;
    public static final short SPEAK     = 7;
    public static final short DEL       = 9;
    public static final short ADD       = 10;
    public static final short END_GAME  = 11;
    /* This is an importantly different value */
    public static final short NEW_FRAME = Short.MAX_VALUE;


    /* These remember what state we are in for sending water data */
    private static final int COLS_SEC = 10;
    private int currentCol;

    public ArrayList waterAdds;
    public ArrayList waterDels;
    private ArrayList bits = new ArrayList(100);

    /* This stores how many args each should take. Note leading 0... */
    public static final int[] lengths = {0,4,4,4,4,4,4,-1,2,2,6,2};

    private OutputStream out;
    private Stage myStage;

    public SellTicket(Stage s)
    {
	myStage = s;
	waterAdds = new ArrayList(500);
	waterDels = new ArrayList(500);
	//  qStart = 0;
//  	qEnd   = 0;
	pipe = new ShortPipe();
    }

    /**
       A prop has been added to stage
       @param p the prop to add to the stage
       @param pn the prop number that this prop
       is added as
     */
    public void addProp(Prop p, int pn)
    {
	addTicket(ADD);
	addTicket((short)pn);
	addTicket((short)p.getPosition().getX());
	addTicket((short)p.getPosition().getY());
	addTicket((short)p.getSprite().getDirectory());
	addTicket((short)p.getSprite().getNumber());
    }

    /**
       This adds a mudball of diameter d at positoin to
       @param p the position
       @param diam the diameter
     */
    public void addMud(RealPos p, int diam)
    {
	addTicket(ADD_MUD);
	addTicket((short)p.getX());
	addTicket((short)p.getY());
	addTicket((short)diam); 
    }

    /**
       This removes a mudball of diameter d at positoin to
       @param p the position
       @param diam the diameter
     */
    public void delMud(RealPos p, int diam)
    {
	addTicket(DEL_MUD);
	addTicket((short)p.getX());
	addTicket((short)p.getY());
	addTicket((short)diam); 
    }

    /** 
	This deletes a prop accross the network
	only the prop number are needed as this
	just removes the prop from the stage
	@param pn the prop number to remove
    */
    public void del(int pn)
    {
	addTicket(DEL);
	addTicket((short)pn);
    }

    /**
       A prop has moved around on stage
     */
    public void move(int pn, RealPos to)
    {
	addTicket(MOVE);
	addTicket((short)pn);
	addTicket((short)to.getX());
	addTicket((short)to.getY());
    }

    /**
       A prop has changed its sprite
     */
    public void sprite(int pn, Sprite s)
    {
	/* We can ignore if the directory is 
	   negative as this sprite was made at
	   run-time, so is (hopefully) text or
	   a special effect
	 */
	if (s.getDirectory() != -1)
	{
	    addTicket(SPRITE);
	    addTicket((short)pn);
	    addTicket((short)s.getDirectory());
	    addTicket((short)s.getNumber());
	}
    }

    /**
       Someone said something. This uses a short
       to store a byte, but efficency doesnt
       really matter here
     */
    public void speak(RealPos rp, String s)
    {
        System.err.println("Sell Ticket "+s);
	byte [] out = s.getBytes();
	addTicket(SPEAK);
	addTicket((short)(out.length+2));
	for (int i = 0; i < out.length; i++)
	{
	    addTicket((short)out[i]);
	}
    }

    /**
       This is called when the game ends
       @param pw the status of player 1
     */
    public void endGame(int pw)
    {
        addTicket(END_GAME);
        addTicket((short)pw);
    }

    /**
       This adds it to the internal que for now
       Eventually it will send the ticket over the
       network if there is space
     */
    private void addTicket(short ticket)
    {
	bits.add(new Short(ticket));
//  	int pos = qStart;
//  	qStart++;
//	pipe.write(new Short(ticket));
	//  if (qStart == Q_SIZE) qStart = 0;
//  	if (qStart == qEnd)
//  	{
//  	    System.err.println("Network buffer Filled up, the shit has hit the fan");
//  	    System.exit(-1);
//  	}
//  	ticketWad[pos] = ticket;
    }

    /** 
	This returns the array of bytes that are ready to be sent
	it also locks the data structure until done() -below- is called
    */ 
    public Object[] nextTicket()
    {
	Object[] out = (Object[])bits.toArray();
	bits.clear();
	return out;
//  	short output;
	//return pipe.read();
//  	if (qStart == qEnd) return null;
//  	output = ticketWad[qEnd];
//  	qEnd++;
//  	if (qEnd == Q_SIZE) qEnd = 0;
//  	return new Short (output);
    }

    /**
       This is called once the broadcast thread is done with the
       byte array
     */
    public void done()
    {

    }

    /**
       Stores a water change to be sent at the end of the frame
       @param rp the position to add water too
     */
    public void addWater(RealPos rp, int diam)
    {
	addTicket(ADD_WATER);
	addTicket((short)rp.getX());
	addTicket((short)rp.getY());
	addTicket((short)diam); 
    }

    /**
       ...likewise removes water
       @param rp the position to remove water from
     */
    public void delWater(RealPos rp, int diam)
    {
	addTicket(DEL_WATER);
	addTicket((short)rp.getX());
	addTicket((short)rp.getY());
	addTicket((short)diam); 
    }

    /**
       Things that must be done on a new frame
       This sends all the water changes accumulated this frame
       Little different as we send a length of changes, and then
       the changes a a continuous blast of bits 
     */
    public void newFrame()
    {
// 	RealPos rp;
// 	WaterThree w3 = (WaterThree)myStage.getWater();
// 	WaterColumnThree[] waterfall = w3.waterfall;

// 	for (int col = currentCol*COLS_SEC; col < (currentCol+1)*COLS_SEC; col++)
// 	{
// 	    if (waterfall[col] == null)
// 	    {

// 	    }
// 	    else
// 	    {

// 	    }
// 	}
// 	/* move onto the next chunk for the next frame */
// 	currentCol++;
// 	if (currentCol > 800/COLS_SEC) currentCol = 0;

	/* add the tickets that represent that water moving */


	/* write the size out to the network 
	if (waterAdds.size() > 0)
	{
	    addTicket(ADD_WATER);
	    addTicket((short)waterAdds.size());
	    for (int i = 0; i < waterAdds.size(); i++)
	    {
		rp = (RealPos)waterAdds.get(i);
		addTicket((short)rp.getX());
		addTicket((short)rp.getY());
	    }
	}
	waterAdds.clear();

	if (waterDels.size() > 0)
	{
	    addTicket(DEL_WATER);
	    addTicket((short)waterDels.size());
	    for (int i = 0; i < waterDels.size(); i++)
	    {
		rp = (RealPos)waterDels.get(i);
		addTicket((short)rp.getX());
		addTicket((short)rp.getY());
	    }
	}
	waterDels.clear();
	 Marker that we are done for now - this
	   should be run when audience runs     */
	addTicket(NEW_FRAME);
    }

    /**
       Just dumps the contents of the Q to the screen
     */
//      public void debug()
//      {
//  	int qE = qEnd;

//  	while (qStart != qE)
//  	{
//  	    qE++;
//  	    if (qE == Q_SIZE) qE = 0;
//  	    System.err.println(" @ pos "+qE+" token "+ticketWad[qE]);
//  	}
//      }
}
