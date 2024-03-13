/* $Id: MenuSave.java,v 1.3 2004/03/05 02:29:52 tk1748 Exp $ */

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
public class MenuSave extends MenuItem
{

    transient private Executive executive;
    private String fileName;
    private Stage  myStage;
    transient private MenuPage menuPage;

    /**
       Creates a new load menu item
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to be diplayed
       @param p  the page to turn to
    */
    public MenuSave     (MenuPage mp, 
                         Stage    s,
		         File     f)
    {
	menuPage = mp;
	fileName = f.getName();
	setUpMenuPage(mp,s,f.getName().substring(0,f.getName().length()-5));
	myStage = s;
    }

    /**
       When we are told to do stuff, menu system
       is finished
     */
    public int doOnActivate()
    {
	/* Remove menu from the stage because
	   we dont want to save the menu */
	menuPage.leaveStage();
	this.finalCurtain();
	/* Save the game */
	DeusEx de = new DeusEx();
	int result = de.save (fileName,myStage());
	/**
	 Go to fail or sucess menu screen -1 = fail
	 */
	if (result == -1) return 5;
	return 0;
    }
}




