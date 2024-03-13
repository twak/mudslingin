/* $Id: MenuPageServerFile.java,v 1.3 2004/03/05 02:29:52 tk1748 Exp $ */
package mud.trunk;

import mud.*;
import mud.trunk.*;
import mud.supporting.*;
import mud.input.*;
import mud.broadcast.*;
import java.io.*;
import java.util.*;

/**
   Displays all the menus to get the server
   off the ground, displays 3 pages, each with different
   routines in this class. Calls bill board to do the work
 */
public class MenuPageServerFile extends MenuPage implements MenuOptionInterface, FeedBack
{
    /* Executive*/
    private Executive executive;

    private BillBoard BB;

    /* The file that was selected */
    private String fileSelected, niceName, gameName = "__not_named__";
    /* List of all multiplayer files */
    private Vector files;
    private boolean netError = false;
    /* If we can show network errors now */
    private boolean sensible = false;
    private ArrayList flyers = null;
    /**
       creates a new menupage
     */
    public MenuPageServerFile (Executive e)
    {
	executive = e;
    }

    /**
       Called just before we enter the stage
     */
    public void setUp(Stage s)
    {
	myStage = s;
	drawFileList();
    }

    /**
       Draws a list of *.mmud files
     */
    public void drawFileList()
    {
	clearPage();
	files = getFiles();
	String name;
	new MenuTitle      (this,myStage,"Select Map");

	if (files.size() == 0)
	{
	    /* No games found */
	    new MenuNavigator  (this,myStage," no levels found ",8);
	}
	else
	{
	    for (int i = 0; i< files.size(); i++)
	    {
		name = ((File)files.get(i)).getName();
		/* Cut file extension off */
		new MenuOptionListener(this,myStage, 
                        name.substring(0,name.length()-5), i);
	    }
	}
	new MenuNavigator  (this,myStage,"main menu",8);
    }

    /**
       Draws the select game name...
     */
    public void drawStartGame()
    {
	/* Clear the MenuItems, so we can start again ... */
	clearPage();
	new MenuTitle         (this, myStage, "new network game options"                      );
	new MenuOptionListener(this, myStage, "start game",                       -1);
	new MenuOptionListener(this, myStage, "level:"+niceName,                  -2);
	new MenuText          (this, myStage, gameName      ,"game description: "   );
	new MenuNavigator     (this, myStage, "main menu",8);

    }

    /**
       Draws the waiting ... page
     */
    public void drawConnecting()
    {
	/* We can show errors */
	sensible = true;
	/* Clear the MenuItems, so we can start again ... */
	clearPage();
	new MenuTitle         (this,myStage," advertising game... ");
	new MenuOptionListener(this, myStage, "back",-3);
	/* Read the options from the server */
	BB = new BillBoard(myStage.getGameValue().server,myStage.getGameValue().port);
	BB.post(gameName, niceName, this, myStage);
    }

    /**
       Called once per go by the menuOptionListeners added above...
     */
    public void doPhysics()
    {
	/* Check that net errors would make sense */
	if (netError && sensible) netError();
	netError = false;
    }

    /**
       Error was thrown by networking procs
     */
    public void netError()
    {
	/* Clear the MenuItems, so we can start again ... */
	clearPage();
	new MenuTitle         (this, myStage, "error starting network");
	new MenuNavigator     (this, myStage, "back",8);
    }

    /**
       This is called when one of our options is selected
       @param i the file selected
     */
    public void message (int i)
    {
	if (i >= 0)
	{
	    String name = ((File)files.get(i)).getName();
	    /* Cut file extension off */
	    niceName = name.substring(0,name.length()-5);
	    fileSelected = "./data/"+name;
	    drawStartGame();
	}
	else if (i==-1)
	{
	    drawConnecting();
	}
	else if (i==-2)
	{
	    drawFileList();
	}
	else if (i==-3)
	{
	    /* Quits server connection attempt */
	    BB.quit();
	    drawFileList();
	    sensible = false;
	}
    }

    /**
       Routine that gets a list of multiplayer level files
       @return the files
     */
    private Vector getFiles()
    {
	Vector output = new Vector(10);

	File file = new File("./data");

	if (!file.exists())
	{
	    System.err.println("Game directory not found");
	    System.exit(-1);
	}

	if (file.isDirectory()) 
	{
	    File[] fileNames = file.listFiles();
	    if (fileNames != null) 
	    {
		for (int i=0; i<fileNames.length; i++)  
		{
		    if (fileNames[i].getName().endsWith(".mmud"))
		    {
			output.add(fileNames[i]);
		    }
		}
	    }
	}
	return output;
    }

    /**
       This is only called when we are getting the name of
       the game from the user
       @param in the name given by the user
     */
    public void text(String in)
    {
	gameName = in;
    }

    /**
       This is called with null if there is a network error
       @param f ignored if not null!?
     */
    public void newFlyer(Flyer f)
    {
	if (f == null)
	{
	    netError = true;
	}
    }
}
