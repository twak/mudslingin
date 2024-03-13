/* $Id: GameValues.java,v 1.23 2004/04/14 12:16:43 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.awt.Color;
import java.io.*;

/**
Stores all other game state information. This is not sychronised
with the clients and is independent. Because of the number of 
values, these are all global variables. Bad programming practise
I know, but these values are used a lot and speed is improved from
accessor methods using globals.
*/
public class GameValues implements Serializable
{
    /* The map size */
    public  int mapSizeX, mapSizeY;
    /* The file used to create this Game */
    public  String mudFile;
    /* Do we show any counters? */
    public  transient Text propCounter = null, frameCounter, fpsCounter;
    /* Where we show println's */
    public  transient  Text systemOut;
    /* If we display the println output to the screen */
    public  boolean showPrint = false;
    /* The file that was called at the start eg: "default.mud"*/
    public  String  levelSetUpFile;
    /* The size of the squares on the grid */
    public static final int SPOTLIGHTS = 20;
    /* Set this to true to end the game now */
    public  boolean quitNow;
    /* Are we in menu mode at the moment? */
    public  boolean menuMode = false;
    /* The frame rate we are playing this at, right now */
    public  int fps = 30;
    /* The background colour */
    public  Color bgColour = Color.cyan;
    /* What the * does this do */
    public  boolean r;
    /* Positive values blow towards the right
       Negative to the left    */
    public  double wind;
    /* Gravity */
    public  double gravity;
    /* Is this a networked game? */
    public  boolean isNetworked = false;
    /* flag for waiting for other player at start of net game */
    public boolean netReady = false;
    /* Server to use - will be an input option */
    public  String server = "http://www.twak.co.uk/cgi-bin/mudsling/";
    public  int port = 15001;
    /* Do we have sound or not ? */
    public boolean sound = true;
    /* Data stream for sending data */
    //public  OutputStream outStream;
    /* Data stream for recieving data */
    //public  InpuStrem inStream;

    /**
    builds a game value with specified size
    @param x width
    @param y height
     */
    public GameValues(int x, int y, String mF)
    {
	mapSizeX = x;
	mapSizeY = y;
	mudFile = mF;
    }

}
