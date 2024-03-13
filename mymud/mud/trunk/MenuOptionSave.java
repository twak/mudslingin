/* $Id: MenuOptionSave.java,v 1.4 2004/05/02 15:05:47 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   Tells the options system to save to disk
 */

public class MenuOptionSave extends MenuItem
{

    private int number;
    private MenuPageOptions MPO;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the number to return
    */
    public MenuOptionSave   (MenuPage mp, 
			     Stage    s,
			     int      n)
    {
	number = n;
	try
	{
	    MPO = (MenuPageOptions)mp;
	}
	catch (Exception e)
	{
	    System.err.println("Bad thingy in MenuOptionSAve");
	    System.exit(-1);
	}
	setUpMenuPage(mp,s,"save settings (fullscreen requires restart)");
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	/* Set the game prot */
	myStage().getGameValue().port = MPO.getGamePort();
	try
	{
	    MPO.writeOut();
	}
	catch (java.io.IOException e)
	{
	    /* Its broken */
	    return 7;
	}
	return number;
    }
}
