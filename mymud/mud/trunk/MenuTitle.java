/* $Id: MenuTitle.java,v 1.4 2004/04/24 14:30:44 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   This is an unselectable menu item
 */
public class MenuTitle extends MenuItem
{

    /**
       Creates a new Title
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param t  the text ob
    */
    public MenuTitle   (MenuPage mp, 
                        Stage    s,
			String   t)
    {
	setUpMenuPage(mp,s,t);
	setSize(50);
	/* Title is *green* coloured */
	setColour( new Color(1,153,51,255) );
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	System.err.println("Someone has activated a title in MenuTitle");
	return -2;
    }
}

