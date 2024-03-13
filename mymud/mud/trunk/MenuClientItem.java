/* $Id: MenuClientItem.java,v 1.3 2004/03/08 18:05:51 tk1748 Exp $ */

package mud.trunk;

import mud.*;
import mud.supporting.*;
import mud.broadcast.*;
import mud.input.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
   This is an yes/no choice in the menu choice system
 */

public class MenuClientItem extends MenuItem
{

    private String name;
    /* state is for feedback from threads */
    protected int number, state = 0;
    private Flyer flyer;
    private MenuPageClient MPC;
    private Executive executive;
    private Socket gameSocket = null;

    /* Dummy */
    protected MenuClientItem()
    {
    }
    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param f a flyer that contains data to show
       @param c should this menu item pass its dophysics control to
       a menupageclient?
    */
    public MenuClientItem   (MenuPageClient mp, 
			     Stage          s,
			     Flyer          f,
			     Executive      e)
    {
	MPC = mp;
	flyer = f;
	executive = e;
	/* set up value */
	setUpMenuPage(mp,s,name());
    }

    /* Reads offa our flyer and returns the text we display */
    private String name()
    {
	String tmp;
	/* Does it appear to be behind a firewall? */
	if (flyer.getAddress().equals(flyer.getAddressExt()))
	{
	    tmp = "";
	}
	else
	{
	    tmp = "[NAT]";
	}
	return (tmp+" : "+flyer.getName()+" : "+flyer.getLevel()+" ");
    }

    /**
       When we are told to active, we connect to the server
       in a thread and return a socket to somewhere
     */
    public int doOnActivate()
    {
	setText("connecting...");
	Socket gameSocket = null;

	ServerConnect sc = new mud.broadcast.ServerConnect(flyer.getAddress(), flyer.getPort(), this);
	/* Go thread boy... */
	sc.start();

	return -1;
    }

    /**
       Once a term
     */
    public void doOwnPhysics()
    {
	if (state == 1) setText("couldn't find that computer");
	if (state == 2) setText("network error");
	if (state == 3)
	{
	    setText("connected - loading level...");
	    /* Set the socket to use */
	    KeyAction ka = myStage().getDirector().getExecutive().getKeyAction();
	    /* Huzah!. Set the variables wanted */
	    ka.setSocket(gameSocket);
	    /* the fact we are client */
	    ka.setServer(false);
	    /* the fact that client play in slot 1 */
	    ka.setNumber(2);
	    /* And now, we load the new level */
            if (flyer != null)
	    {
		executive.loadMud("data/"+flyer.getLevel()+".mmud",myStage());
	    }
	    else
	    {
		executive = myStage().getDirector().getExecutive();
		executive.loadMud("data/default.mmud",myStage());
	    }
	}
	doOwnPhysics2();
	state = 0;
    }
    /**
       Dummy method to be overwirtten
     */
    public void doOwnPhysics2()
    {
    }

    /* Called back if networking goes wrong */
    public void error(int i)
    {
	state = i;
    }

    public void success(Socket gs)
    {
	gameSocket = gs;
	state = 3;
    }

}
