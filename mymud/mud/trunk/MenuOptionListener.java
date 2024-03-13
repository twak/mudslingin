/* $Id: MenuOptionListener.java,v 1.2 2004/03/01 17:57:22 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   A way of dynamically creating menu pages listeners
 */

public class MenuOptionListener extends MenuItem
{


    private MenuOptionInterface MPO;
    private int number;
    private boolean doPhys = false;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to display
       @param m a number, returned on activate
    */

    public MenuOptionListener (MenuOptionInterface mp, 
			       Stage            s ,
			       String           n ,
			       int              m)
    {
	MPO = mp;
	number = m;
	setUpMenuPage((MenuPage)mp,s,n);
	if (mp instanceof MenuPageServerFile)
	{
	    doPhys = true;
	}
    }

    public void doOwnPhysics()
    {
	if (doPhys)
	{
	    ((MenuPage)MPO).doPhysics();
	}
    }


    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	MPO.message(number);
	return -1;
    }
}
