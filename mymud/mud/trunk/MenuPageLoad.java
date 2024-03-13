/* $Id: MenuPageLoad.java,v 1.1 2004/01/27 20:11:53 tk1748 Exp $ */
package mud.trunk;

import mud.*;
import mud.trunk.*;
import mud.supporting.*;
import java.util.Vector;
import java.io.*;

/**
  This is a menu page that displays all the save files in the current
  directory
 */
public class MenuPageLoad extends MenuPage
{
    /* We need the executive to be able to
       load a game */
    Executive executive;

    /**
       creates a new menupage
     */
    public MenuPageLoad (Executive e)
    {
	executive = e;
    }

    public void setUp(Stage s)
    {
	myStage = s;
	clearPage();
	/* This by default adds the pages */
	makeMenu(getFiles());
	/* we then pull them off the stage */
	leaveStage();
    }

    /**
       Creates a menu from a set of saved files
     */
    private void makeMenu(Vector input)
    {
	new MenuTitle (this,myStage,"Select file to load");

	if (input.size() > 0)
	{
	    for (int i=0; i<input.size(); i++)  
	    {
		new MenuLoad (this,myStage,((File)input.get(i)),executive);
	    }
	}
	else
	{
	    new MenuItem (this,myStage,"No files found");
	}

	new MenuNavigator (this,myStage,"don't load a file",0);
    }

    /**
       Routine that gets a list of files that may be loaded
       @return the files
     */
    private Vector getFiles()
    {
	Vector output = new Vector(10);

	File file = new File("./saves");

	if (!file.exists())
	{
	    System.err.println("Save game directory not found");
	    System.exit(-1);
	}

	if (file.isDirectory()) 
	{
	    File[] fileNames = file.listFiles();
	    if (fileNames != null) 
	    {
		for (int i=0; i<fileNames.length; i++)  
		{
		    if (fileNames[i].getName().endsWith(".save"))
		    {
			output.add(fileNames[i]);
		    }
		}
	    }
	}
	return output;
    }
}
