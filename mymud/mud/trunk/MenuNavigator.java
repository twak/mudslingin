/* $Id: MenuNavigator.java,v 1.2 2004/03/01 17:57:22 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;


/**
   Allows one menu page to call another page by its 
   reference number, defined in director/MenuGenre.java
 */
public class MenuNavigator extends MenuItem
{


    int nextPage;
    boolean doPhys = false;
    MenuPage MP;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to be diplayed
       @param p  the page to turn to
    */
    public MenuNavigator(MenuPage mp, 
                         Stage    s,
		         String   n,
                         int      p)
    {
	setUpMenuPage(mp,s,n);
	nextPage = p;
	MP = mp;
	if (mp instanceof MenuPageClient || mp instanceof MenuPageServerFile)
	{
	    doPhys = true;
	}
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	return nextPage;
    }

    /**
       Calls doOwnPhysics if necessary
    */
    public void doOwnPhysics()
    {
	MP.doPhysics();
    }
}




