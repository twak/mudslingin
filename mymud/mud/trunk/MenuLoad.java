/* $Id: MenuLoad.java,v 1.1 2004/01/27 20:11:53 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import mud.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;
import java.io.*;

/**
   When selected, causes a system re-load of the given data
 */
public class MenuLoad extends MenuItem
{

    private Executive executive;
    private String fileName;

    /**
       Creates a new load menu item
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to be diplayed
       @param p  the page to turn to
    */
    public MenuLoad     (MenuPage mp, 
                         Stage    s,
		         File     f,
			 Executive e)
    {
	executive = e;
	fileName = f.getName();
	setUpMenuPage(mp,s,f.getName().substring(0,f.getName().length()-5));
    }

    /**
       When we are told to do stuff, menu system
       is finished
     */
    public int doOnActivate()
    {
	DeusEx de = new DeusEx();
	int result = de.load (fileName, executive);
	/**
	 Go to fail or sucess menu screen -1 = fail
	 */
	if (result == -1) return 2;
	return 3;
    }
}




