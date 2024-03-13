/* $Id: MenuPrint.java,v 1.1 2004/01/27 20:11:53 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   Menu Item that sets whether or not the println's are displayed
 */
public class MenuPrint extends MenuItem
{
    /**
       Creates a new println menu
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param t  the text ob
    */
    public MenuPrint   (MenuPage mp, 
                        Stage    s)
    {
	setUpMenuPage(mp,s,"out.print - true");
	/* Call this twice to initalise the value displayed */
	doOnActivate();
	doOnActivate();
    }

    /**
       Toggles stats display
     */
    public int doOnActivate()
    {
	if (!isDisplayed())
	{
	    setText("out.print - true");
	    myStage().getGameValue().showPrint = true;
	}
	else
	{
	    setText("out.print - false");
	    myStage().getGameValue().showPrint = false;
	}
	return-1;
    }


    /**
       Returns true if the menus are currently
       being displayed, false otherwiprintlns are displayed
       @return output if the prints are displayed
     */
    private boolean isDisplayed()
    {
	boolean output = myStage().getGameValue().showPrint == true;
	return output;
    }
}
