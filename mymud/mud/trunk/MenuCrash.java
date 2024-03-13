/* $Id: MenuCrash.java,v 1.1 2004/01/27 20:11:53 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import mud.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   When selected, causes a system re-load of the given data
 */
public class MenuCrash extends MenuItem
{

    /**
       Creates a new load menu item
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to be diplayed
       @param p  the page to turn to
    */
    public MenuCrash    (MenuPage mp, 
                         Stage    s)
    {
	
	setUpMenuPage(mp,s,"crash");
    }

    /**
       When we are told to do stuff, menu system
       is finished
     */
    public int doOnActivate()
    {
	int[] bob;
	bob = new int[3];
	bob[3] = 2;
	return -1;
    }
}




