/* $Id: BuyTicket.java,v 1.8 2004/05/02 15:05:46 tk1748 Exp $ */
package mud.trunk;

import mud.broadcast.*;
import mud.supporting.*;
import java.io.*;

/**
   This is fed tickets by Receive Thread in broadcast, it
   makes the changes to stage dependent on what happens
   on the server.
   <BR><BR>
   unforntunately this is the one place in the whole thing
   that would have worked well as a thread, by sync. issues
   would have killed us dead in the water.
   Because of the TCP/IP garenteed delivery or your
   money back thing, it is assumed all tickets are delivered
   <BR>
   in the code, wad is global used for passing a list of 
   values
 */
public class BuyTicket
{

    private short[] wad;
    private int[] wadi;
    private Stage stage;
    private short currentTicketType, wadPosition;
    /* Common value */
    private Prop go;


    /**
       Constructor
     */
    public BuyTicket(Stage s)
    {
	/* First ticket should be a descriptor */
	currentTicketType = -1;
	stage = s;
	wad = null;
    }

    /**
       This is called when a ticket is added to the network.
       This is called in sequence, so buffering happens elsewhere
       @param ticket the ticket just recieved.
     */
    public void soldTicket(short[] tickets)
    {
	for (int i = 0; i < tickets.length; i++)
	{
	    /* ignore end of turn markers for now */
	    if (tickets[i]!=32767)
	    {
		    forEach(tickets[i]);
	    }
	    else
	    {
		    /* new frame */
		    stage.getWater().doPhysics();
		    stage.getMudFloor().doPhysics();
	    }
	}
    }
    private void forEach(short ticket)
    {
	if (currentTicketType == -1)
	{
	    /* New change item */
	    currentTicketType = ticket;
// 	    /* Special case to allow variable length encoding */
// 	    if (ticket == SellTicket.ADD_WATER)
// 	    {
// 		currentTicketType = -69;
// 	    }
// 	    else if (ticket == SellTicket.DEL_WATER)
// 	    {
// 		currentTicketType = -70;
// 	    }
// 	    else
	    if (ticket == SellTicket.SPEAK)
	    {
	         System.err.println("I hear voices"+ticket);
		currentTicketType = -71;
	    }
	    try
	    {
		wad = new short[SellTicket.lengths[currentTicketType]];
	    }
	    catch (Exception e)
	    {
		System.err.println("Invalid instruction recieved "+ticket);
		currentTicketType = -1;
	    }
	    wad[0] = currentTicketType;
	    wadPosition = 1;
	}
// 	else if (currentTicketType == -69)
// 	{
// 	    /* This tells us how much water we will be adding */
// 	    currentTicketType = SellTicket.ADD_WATER;
// 	    wad = new short [2+(ticket*2)]; 
// 	    wad[0] = SellTicket.ADD_WATER;
// 	    wadPosition = 2;
// 	}
// 	else if (currentTicketType == -70)
// 	{
// 	    /* This tells us how much water we will be deleting */
// 	    currentTicketType = SellTicket.DEL_WATER;
// 	    wad = new short [2+(ticket*2)]; 
// 	    wad[0] = SellTicket.DEL_WATER;
// 	    wadPosition = 2;
// 	}
	else if (currentTicketType == -71)
	{
	    /* This tells us how long the string is */
	    currentTicketType = SellTicket.SPEAK;
	    wad = new short [ticket]; 
	    wad[0] = SellTicket.SPEAK;
	    wadPosition = 2;
	System.err.println("Client set ticket"+ticket);
	}
	else
	{
	    /* We are continuing an old change item */
	    if (wadPosition >= wad.length)
	    {
		/* Too much data for this change...*/
		System.err.println("too much data.... e-ergh");
	    }
	    else
	    {
		wad[wadPosition] = ticket;
		wadPosition++;
		if (wadPosition == wad.length)
		{
		    /* End of change, process */
		    doWad();
		    currentTicketType = -1;
		}
	    }
	}
    }

    /**
       This is the bit that is passes all the tickets in
       order and will choose which routine will process them
     */
    private void doWad()
    {
	short type = wad[0];
	switch(type)
	{
	case SellTicket.MOVE: 
	    move();
	    break;
	case SellTicket.SPRITE:
	    sprite();
	    break;
	case SellTicket.ADD: 
	    add();
	    break;
	case SellTicket.DEL: 
	    del();
	    break;
	case SellTicket.ADD_MUD: 
	    addMud();
	    break;
	case SellTicket.DEL_MUD: 
	    delMud();
	    break;
	case SellTicket.ADD_WATER:
	    addWater();
	    break;
	case SellTicket.DEL_WATER:
	    delWater();
	    break;
	case SellTicket.SPEAK:
	    speak();
	    break;
	case SellTicket.END_GAME:
	    end();
	    break;
	case SellTicket.NEW_FRAME:
	    /* Should happen */
	    System.err.println("OOPS");
	    break;
	}
    }

    /**
       Puts the nth prop into go
       @param propNo the n in nth
     */
    private void get(short propNo)
    {
	try
	{
	    //System.err.println(stage.allProps().get((int)propNo).getClass());
	    go =(Prop)stage.allProps().get((int)propNo);
	}
	catch (java.lang.IndexOutOfBoundsException e)
	{
	    System.err.println("Warning networking is broken in BuyTicket");
	}
    }

    /**
       Start of many methods for implementing changes recieved
       on the stage. All of these do the get(wad[0]) bit as
       the syntax may eventually change
       - move
     */
    private void move()
    {
	get(wad[1]);
	if (go instanceof NetProp)
	{
	    ((NetProp)go).actualPosition(new RealPos((int)wad[2],(int)wad[3]));
	}
	else
	{
	    go.changePosition(new RealPos((int)wad[2],(int)wad[3]));
	}
    }

    /**
       changes sprite
     */
    private void sprite()
    {
	get(wad[1]);
	go.changeSprite(stage.getSpriteLoader().getSprite((int)wad[2],(int)wad[3]));
    }

    /**
       adds a new prop
     */
    private void add()
    {
	/* we could check that wad[1] was the right prop no, b wtf? */
	go = new NetProp(new RealPos((int)wad[2],(int)wad[3]),stage,(int)wad[4],(int)wad[5]);
    }

    /**
       deletes an old prop
     */
    private void del()
    {
	get(wad[1]);
	go.finalCurtain();
    }

    /**
       Add mud in a circle...
    */
    private void addMud()
    {
	stage.mudBomb(new RealPos((int)wad[1],(int)wad[2]), (int)wad[3]);
    }

    /** 
	& remove mud in a circle
    */
    private void delMud()
    {
	stage.mudExplode(new RealPos((int)wad[1],(int)wad[2]), (int)wad[3]);
    }
    
    /**
       adds the Water from a list
       size is *2 +2 because we have 2 shorts for
       each position, and +2 for descriptor & size
     */
    private void addWater()
    {
	stage.waterBomb(new RealPos((int)wad[1],(int)wad[2]), (int)wad[3]);
// 	int size = (wad[1]*2+2);
// 	for (int i = 2; i < size; i++)
// 	{
// 	    // add water at wad[i] wad[i+1]
// 	}
    }

    /**
       End of the game
     */
    private void end()
    {
        stage.gameOver((int)wad[1]);
    }

    /**
       ...likewise removes the water form a list 
    */
    private void delWater()
    {
	stage.waterGo(new RealPos((int)wad[1],(int)wad[2]), (int)wad[3]);
	// int size = (wad[1]*2+2);
// 	for (int i = 2; i < size; i++)
// 	{
// 	    // del water at wad[i] wad[i+1]
// 	}
    }

    private void speak()
    {
	System.err.println("The man is alive");
    }
}
