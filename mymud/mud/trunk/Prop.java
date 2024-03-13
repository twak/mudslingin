/* $Id: Prop.java,v 1.19 2004/05/01 15:25:13 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.GraphicsConfiguration;
import java.awt.*;

/**
   A prop is an object (normally on the stage) to be displayed on
   the screen.
*/

public class Prop implements Serializable 
{
    private           RealPos position;
    private           RealPos nxtPos = new RealPos(0,0);
    private           Sprite  me;
    private           Stage   myStage;
    private           int     layer;
    private           int width, height;
    /* multi use variable with speed */
    public            int speed;
    /* Where were we last turn */
    public            RealPos oldPos;
    /* Prop numbers */
    private           int dir,num;
    /* If we animate this prop while menu is
       displayed */
    private           boolean menu = false;
    private static GraphicsConfiguration gc;

    /**
    dummy- called when we extend prop
    */
    public Prop()
    {
	if (gc == null) setUpGC();
    }

    /** 
    Creates a prop
    @param rp position to create the prop
    @param s  Stage that it is displayed on
    @param m  Sprite to display
    */ 
    public void setUpProp(RealPos rp, Stage s, Sprite m)
    {
	position = new RealPos(rp.getX(),rp.getY());
	me = m;
	myStage = s;
	/* Record sizes */
	BufferedImage im = (BufferedImage) me.getImage();
	dir = me.getDirectory();
	num = me.getNumber();
	width = im.getWidth();
	height = im.getHeight();
	/* We now add a prop to the stage here */
	s.addProp(this);
    }

    /**
       This returns the colours and buffering methods
       that are most suitable for this platform
       @return gc the settings
     */
    public GraphicsConfiguration getGC()
    {
	return gc;
    }

    private void setUpGC()
    {
	/* Set up default colour depths */
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice gs = ge.getDefaultScreenDevice();
        gc = gs.getDefaultConfiguration();
    }

    /**
    Tells the prop which stage it is on
    used to load a prop from disk/network
    @param s stage
    */
    public void setStage(Stage s)
    {
	myStage = s;
    }

    /**
    When loading, we don't store the sprite
    loader, so we set it here
    @param sl the sprite loader in the new construct
    */
    public void setSpriteLoader(SpriteLoader sl)
    {
	me.setSpriteLoader(sl);
	changeSprite(sl.getSprite(dir,num));
    }

    /**
       sets this prop as a menu that
       isn't paused when we enter menu
       mode
     */
    public void iAmAMenu()
    {
	menu = true;
    }

    /**
       Asks if this prop is allowed to
       continue operating if we are in menu mode
       @return true if this prop is a menu
     */
    public boolean isMenu()
    {
	return menu;
    }

    /**
    Returns the position
    @return position current prop position
    */
    public final RealPos getPosition()
    {
	return position;
    }

    /** 
    Returns the current sprite
    @return me the sprite depicting this prop now
    */
    public final Sprite getSprite()
    {
	return me;
    }


    /** 
    Moves the prop, and logs the change 
    @param rp the position to move to
    */
    public final void changePosition(RealPos rp)
    {
	RealPos nxtPos = new RealPos(rp.getX(),rp.getY());
	myStage.changeProp(this, nxtPos);
        position=nxtPos;
    }


    /**
    public void changeSprite(Sprite m)
    Changes the width of the sprite
    @param m sprite to look like
    */
    public final void changeSprite(Sprite m)
    {
	me = m;
	dir = me.getDirectory();
	num = me .getNumber();
	int newWidth, newHeight;
	BufferedImage im = (BufferedImage) me.getImage();

	newWidth = im.getWidth();
	newHeight = im.getHeight();
	/* we take the maximum sized prop to refresh
                                  the area underneath */
	width = Math.max (width , newWidth );
	height = Math.max(height, newHeight);
	myStage.changeProp(this, me, width, height);

	width = newWidth;
	height = newHeight;
    }

    /**
    Removes the prop from the stage
    */
    public final void finalCurtain()
    {
	myStage.killProp(this);
    }

    /**
    Over ride this method to make the props appearance change
    once a frame
     */
    public void doPhysics()
    {
    }

    public Stage myStage()
    {
	return myStage;
    }

	/*sets the speed variable*/
	public void setSpeed(int s)
	{
		speed =s ;
	}
}
