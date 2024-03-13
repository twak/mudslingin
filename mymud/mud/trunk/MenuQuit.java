/* $Id: MenuQuit.java,v 1.1 2004/01/27 20:11:53 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;


public class MenuQuit extends MenuItem
{

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param t  the text ob
    */
    public MenuQuit   (MenuPage mp, 
                        Stage    s)
    {
	setUpMenuPage(mp,s,"Quit Mudsling");
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	myStage().getGameValue().quitNow = true;
	return -2;
    }


    /**
       Returns true if the menus are currently
       being displayed, false otherwise
       @return output if the menus are displayed
     */
    private boolean isDisplayed()
    {
	boolean output = myStage().getGameValue().propCounter != null;
	return output;
    }
}
