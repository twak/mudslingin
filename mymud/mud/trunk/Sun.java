/* $Id: Sun.java,v 1.5 2004/05/05 12:32:26 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;

/**
   A prop that looks like a sun, and represents the
   time left in the game. When the game finishes calls
   the default next game

   This is hard-coded for a game of 800 x 600;
 */
public class Sun extends Prop
{

    private int time;
    private int length;
    private double widthSun;
    static final double WIDTH  = 800;
    static final double HEIGHT = 600;
    /** 
    Makes a new sun that will set when the game ends
    @param s Stage that this player is on
    @param l The length of the game
    */
    public Sun(Stage s, int l)
    {
	time = 0;
	length = l;
	RealPos rp = positionFn();
	/* This just loads the default sprite now */
	setUpProp(rp, s, s.getSpriteLoader().getSprite(0,17));
	Sprite sp = getSprite();
	BufferedImage im = (BufferedImage)sp.getImage();
	widthSun = (double)im.getWidth()/2;
    }

    public int getTime()
    {
	return time;
    }

    public void setTime(int t)
    {
	time = t;
    }

    /**
    Man, does the physics of the man
    */
    public void doPhysics()
    {
	time++;
	
	changePosition(positionFn());
	
	if (time == length)
	{
	    /* end of game */
	    myStage().gameOver();
	}
    }

    /**
       Function to return the position
       of the sun by the time of day, or game.
       Polynomial curve at present
       @param t the current time;
     */
    private RealPos positionFn()
    {
	double x = ((WIDTH / length) * time)-widthSun;
	double y = (x-(WIDTH/2));
	/*           amount of curve
		           distance from top of screen */
	y = ((y * y)/10000)+10;

	return new RealPos(x,y);
    }
}





