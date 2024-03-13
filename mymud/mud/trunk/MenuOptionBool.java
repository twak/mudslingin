/* $Id: MenuOptionBool.java,v 1.1 2004/02/14 13:05:07 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   This is an yes/no choice in the menu choice system
 */

public class MenuOptionBool extends MenuItem
{

    private String name;
    private boolean active;
    private MenuPageOptions MPO;
    private int number;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to display
       @param a the value to initialise too.
       @param m a number, returned on activate
    */
    public MenuOptionBool   (MenuPage mp, 
			     Stage    s,
			     String   n,
			     boolean  a,
			     int      m)
    {
	name = n;
	number = m;
	active = a;
	try
	{
	    MPO = (MenuPageOptions)mp;
	}
	catch (Exception e)
	{
	    System.err.println("MenuOptionBool may only be made by MenuPageOption");
	    System.exit(-1);
	}
	setUpMenuPage(mp,s,name());
    }

    private String name()
    {
	if (active) return (name+" - yes");
	return (name+" - no");
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	if (active) 
	{
	    active = false;
	}
	else
	{
	    active = true;
	}
	setText(name());
	MPO.toggled(this,active,number);

	return -1;
    }
}
