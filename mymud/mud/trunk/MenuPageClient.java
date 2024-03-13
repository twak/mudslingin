/* $Id: MenuPageClient.java,v 1.3 2004/03/08 18:05:51 tk1748 Exp $ */
package mud.trunk;

import mud.*;
import mud.trunk.*;
import mud.supporting.*;
import mud.input.*;
import mud.broadcast.*;
import java.io.*;
import java.util.*;

/**
  Displays a list of all available serbvers
  This beasie does:
  1) displays "connecting to server message"
  2) displays list of servers
 */
public class MenuPageClient extends MenuPage implements FeedBack
{
    /* Executive*/
    private Executive executive;
    private BillBoard BB;
    private boolean changed = false, networkError = false;

    /* Unused at the moment */
    private final static int LINES_PAGE = 9;

    private ArrayList flyers = null;
    /**
       creates a new menupage
     */
    public MenuPageClient (Executive e)
    {
	executive = e;
    }

    /**
       Called just before we enter the stage
     */
    public void setUp(Stage s)
    {
	myStage = s;
	/* Read the options from the server */
	BB = new BillBoard(s.getGameValue().server, s.getGameValue().port);
	flyers = new ArrayList();
	BB.read(this);
	drawMenu();
    }

    /**
       Draws the flyers as menu items
     */
    public void drawMenu()
    {
	/* Clear the MenuItems, so we can start again ... */
	clearPage();
	/* Set up title and quit */ 
	new MenuTitle (this, myStage,"Select a game to join");

	new MenuNavigator (this, myStage,"refresh", 15);

	if (networkError)
	{
	    new MenuNavigator (this, myStage,"network error", 8);
	}
	else if (flyers.size() == 0)
	{
	    new MenuNavigator (this, myStage,"no games found", 8);
	}

	for (int i = 0; i < flyers.size(); i++)
	{
	    /* We set a call-back for doPhysics on the first
	       item */
	    new MenuClientItem (this, myStage, (Flyer)flyers.get(i),executive);
	}
	new MenuOptionIP (this, myStage, "manual ip: " );
	new MenuNavigator (this, myStage,"back", 8);
    }

    /**
       This is called when a new server is added to our list
       @param f the flyer returned from the server. If null
                then the server was unreachable
     */
    public void newFlyer (Flyer f)
    {
	if (f == null)
	{
	    networkError = true;
	    return;
	}
	flyers.add(f);
	changed = true;
    }

    /**
       This is called once a tern from the first MenuClientItem
       (If it exists) so we can check loops and add the client
     */
    public void doPhysics()
    {
	if (changed)
	{ 
	    changed = false;
	    drawMenu();
	}
    }
}
