/* $Id: Director.java,v 1.13 2004/05/02 15:05:32 tk1748 Exp $ */
package mud.director;

import mud.*;
import mud.trunk.*;
import mud.input.*;
import mud.supporting.*;
import java.io.*;
import java.util.ArrayList;


/**
The director class handles user/AI input by
dispatching events to props
*/

public class Director implements StageAccess
{

    /* these two for capturing output */
    private ByteArrayOutputStream os;
    private PrintStream osPrint;

    private StageManager coordinator;
    private Executive    executive;
    private ScriptEditor scripteditor;
    private Genre genre;
    private Genre swap;
    private int   counter;

    private boolean FRAME,PROPS, netError = false;
    private double  FPS;
    private Integer startMenu;
    private Stage   stage;

    public static int MENU_PAUSE = 29;
    public static int MENU = 30;
    public static int QUIT = 31;
    public static int NORM = 32;

    /** 
    Creates a new Director
    @param c StageManager object controling this director, so
    we can make StageManager stop this performance.
    @param e the Executive this Director was called from
    @param se the scripteditor to get the key strokes from
    @param g  the genre/game type to use
    */
    public Director(StageManager c, 
                    Executive e, 
                    ScriptEditor se, 
                    Genre g,
		    Integer sM)
    {
	coordinator = c;
	executive   = e;
	scripteditor = se;
	genre = g;
	startMenu = sM;
    }

    /**
    The director prepairs its method here
    @param s The stage the Director is working on
     */
    public void makeUp(Stage s)
    {
	/* Here we reset the network settings, so
	   that any new game is, by default of the
	   sinlge player type */
	executive.getKeyAction().setNumber(-1);
	executive.getKeyAction().setSocket(null);
	executive.getKeyAction().setServer(false);

	/* Get ready to capture output streams */
	os = new ByteArrayOutputStream();
	osPrint = new PrintStream(os);
	System.setOut(osPrint);

	/* Set the menu system-by default we are not in a menu*/
	s.getGameValue().menuMode = false;

	if (!s.getGameValue().isNetworked)
	{
	if (startMenu == null)
	{
	    swap = new MenuGenre(s, executive);
	}
	else
	{
	    int menuInt = startMenu.intValue();
	    swap = genre;
	    genre = new MenuGenre(s, executive, menuInt);
	    s.getGameValue().menuMode = false;
	}
	/* Networked stuff cannot have text doofits */
	s.getGameValue().systemOut = new Text(s, ".");
	}

	counter = 0;
	/* This is so we can put errors here if it goes wrong */
	stage = s;
    }

    private void dealWithOutputStreams(Stage s)
    {
	/* we want to tunnel os.tostring -> a prop then reset it */
	if (s.getGameValue().showPrint) 
	{
	    if (s.getGameValue().systemOut != null)
	    {
		if (os.toString().length() > 0)
		{
		    s.getGameValue().systemOut.setString(os.toString(),30);
		}
	    }
	    else
	    {
		s.getGameValue().systemOut = new Text(new RealPos(3,550),s, 30,os.toString());
	    }
	    os.reset();
	}
    }

    /**
    The director runs this every frame
    @param s The stage the Director is working on
     */
    public void perform(Stage s)
    {
	int result;
	Genre tmp;

	/* deal with key input */

	ArrayList inputs = scripteditor.getKeys();

	result = genre.events(s,inputs);

	/* If there's a networking error, panic and go back to main screen */
	if (netError)
	{
	    /* Newline for dots */
	    System.err.println("");
	    executive.loadMud("data/netError.mud",s);
	    s.getGameValue().quitNow = true;
	    netError = false;
	}
	if (result == MENU)
	{
	    /* Switch the current genre for the menu
	       or vice-versa */
	    tmp   = swap;
	    swap  = genre;
            genre = tmp;
	}

	/* To capture System.out...*/
	dealWithOutputStreams(s);

	/* Frame & prop counters 
	   the fps counter is in executive */
	counter++;
	if (s.getGameValue().frameCounter != null) s.getGameValue().frameCounter.setString("Frame: "+counter            ,2);
	if (s.getGameValue().propCounter  != null) s.getGameValue().propCounter .setString("Props: "+s.allProps().size(),2);
    }


    /**
       When we should show a menu ASAP, game is finished
       /timeout or something. not used at moment.
     */
    public void goToMenu(Stage s, int m)
    {
	if (!(genre instanceof MenuGenre))
	{ 
	    swap  = genre;
	    genre =  new MenuGenre(s, executive, m);
	}
    }

    /**
    This is called after the closeCurtains call
    so the director can tidy up and leave
    */
    public void goHome()
    {
	/* close all links, to return memory */
	os=null;
	osPrint=null;
	
	coordinator=null;
	executive=null;
    }

    /**
	This is called if the Client of a client
	server pair wants to speak
	@param s the string to say
    */
    public void sendSpeak(String s)
    {
	scripteditor.addText(s);
    }

    /**
       Gets the currenlty working executive
       @return the exec in his fancy suit
     */
    public Executive getExecutive()
    {
	return executive;
    }

    /**
       This is called when an error occurs
       in the networking. Note that this is
       unscnchronsided, and may be called at any
       time
     */
    public void networkError()
    {
	
	/* EVENTUALLY this should go to a nice error 
	   message, but how ? ... */
	if (!(genre instanceof MenuGenre)) netError = true;
    }
}
