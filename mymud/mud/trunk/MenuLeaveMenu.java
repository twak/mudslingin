/* $Id: MenuLeaveMenu.java,v 1.1 2004/01/27 20:11:53 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;


/**
   Causes the menu system to quit back to the game
 */
public class MenuLeaveMenu extends MenuItem
{
    /**
       Creates a new Item of a menu item
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to be diplayed
       @param p  the page to turn to
    */
    public MenuLeaveMenu(MenuPage mp, 
                         Stage    s,
		         String     n)
    {
	setUpMenuPage(mp,s,n);
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	return -2;
    }
}




