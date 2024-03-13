/* $Id: MenuMudFile.java,v 1.2 2004/02/06 01:01:44 tk1748 Exp $ */

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
public class MenuMudFile extends MenuItem
{

    transient private Executive executive;
    private String fileName;
    private Stage  myStage;
    transient private MenuPage menuPage;
    private int whileLoad;

    /**
       Creates a new load menu item
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param file the mud file to load
       @param name the word that will represent this menu
    */
    public MenuMudFile     (MenuPage mp, 
                            Stage    s,
		            String file,
			    String name,
			    Executive e)
    {
	menuPage = mp;
	fileName = file;
	setUpMenuPage(mp,s,name);
	myStage = s;
	executive = e;
	whileLoad = 3;
    }
   /**
       Creates a new load menu item
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param file the mud file to load
       @param name the word that will represent this menu
       @param int the menuPage to display while loading
    */
    public MenuMudFile     (MenuPage mp, 
                            Stage    s,
		            String file,
			    String name,
			    Executive e,
			    int      wl)
    {
	whileLoad = wl;
	menuPage = mp;
	fileName = file;
	setUpMenuPage(mp,s,name);
	myStage = s;
	executive = e;
    }

    /**
       When we are told to do stuff, menu system
       is finished
     */
    public int doOnActivate()
    {
        executive.loadMud("data/"+fileName,myStage);
	return whileLoad;
    }
}




