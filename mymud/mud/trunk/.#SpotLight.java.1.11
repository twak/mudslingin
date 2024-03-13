package mud.trunk;

import java.util.ArrayList;
import mud.supporting.IntPos;
import mud.trunk.*;
import java.io.*;


import mud.trunk.*;
import mud.supporting.*;
import java.util.ArrayList;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


/**
  Several different modules needed to know what was happening in 
  small areas of the screen. To cut the complexity, only local
  props are included in the spotlight. Big objects may be under
  one or more spotlight/s.
  <BR><BR>
  The screen is spilt into n x n spotlights. n is defined in GameValues.
 */

public class SpotLight implements Serializable 
{

    /* a list of props */
    private ArrayList items;
    /* records if there is any mud/water under this spotlight */
    private boolean   muddy, wet;
    private static ArrayList forPhysics, forAudience;
    private IntPos position;
    private transient Image imageMud, imageBack, imageWater;
    /* counters for the ammount of water an mud in this square */
    private int mudCount, waterCount;

    /**
    creates a new spotlight
    */
    protected SpotLight(IntPos i, ArrayList fP, ArrayList fA)
    {
	items = new ArrayList(5);
	forPhysics = fP;
	forAudience = fA;
	muddy = false;
	wet = false;
	mudCount = 0;
	waterCount = 0;
	position = i;
	/* On initialisation add to for Audience, so
           whole screen get redrawn */
	forAudience.add(this);


    }

    public void photo(Stage s)
    {
	GameValues settings = s.getGameValue();

          int squareWidth  = settings.mapSizeX/GameValues.SPOTLIGHTS;
          int squareHeight = settings.mapSizeY/GameValues.SPOTLIGHTS;

	  MudFloorInterface r = s.getMudFloor();
	  imageMud = ((BufferedImage)r.getImage()).getSubimage(squareWidth*position.getX(),squareHeight*position.getY(),squareWidth,squareHeight);
	  WaterInterface w = s.getWater();
	  imageWater = ((BufferedImage)w.getImage()).getSubimage(squareWidth*position.getX(),squareHeight*position.getY(),squareWidth,squareHeight);
	  if (s.getBackground() != null)
	  {
		  imageBack = ((BufferedImage)s.getBackground()).getSubimage(squareWidth*position.getX(),squareHeight*position.getY(),squareWidth,squareHeight);
	  }
    }

    public Image getImageMud()
    {
	return imageMud;
    }

    public Image getImageBack()
    {
	return imageBack;
    }

    public Image getImageWater()
    {
        return imageWater;
    }


    /**
       For the audience to render
       @return the list of props in this spotlight
     */
    public ArrayList getPropsAudience()
    {
	return items;
    }

    /**
       Returns a co-ordinate of where we are
       note: this needs to be timsed by
       spotlight dimensions to get into real
       coords
       @return spotlight number
     */
    public IntPos getPosition()
    {
	return position;
    }

    /**
       do we need to draw/ calculate for the mud?
       @return if there is mud
    */
    public boolean isMuddy()
    {
	return muddy;
    }


    /**
       Does the current square have any water
       @return well does it?
     */
    public boolean isWet()
    {
	return wet;
    }

    /**
       Defines if this spotlight is muddy or not
       @param m well is it muddy?
     */
    protected void setMuddy(boolean m)
    {
	muddy = m;
    }


    protected void addMud()
    {
	muddy = true;
	mudCount++;
    }

    protected void addWater()
    {
	wet = true;
	waterCount++;
    }

    protected void removeWater()
    {
	waterCount--;
	if (waterCount == 0)
	{
	    wet = false;
	}
    }

    protected void removeMud()
    {
	mudCount--;
	if (mudCount == 0)
	{
	    muddy = false;
	}
    }

    protected void setWaterCount(int i)
    {
	wet = true;
	if (i <= 0) wet = false;
	waterCount = i;
    }

    protected void setMudCount(int i)
    {
	muddy = true;
	if (i <= 0) muddy = false;
	mudCount = i;
    }

    /**
       Returns the number of props in this
       spotlight
       @return the number
     */
    public int size()
    {
	return items.size();
    }

    /**
       adds a prop under this spotlight
       @param p prop to add
     */
    protected void add(Prop p)
    {
	if (!items.contains(p))
	{
	    items.add(p);
	    changeChecks();
	}
	//System.out.println("ARSE"+p.getPosition().getX());
    }


    /**
    removes this prop from under this spotlight
    @param p the prop to remove
    */
    protected void remove(Prop p)
    {
	int result = items.indexOf(p);

	if (result >=0)
	{
	    items.remove(result);
	    changeChecks();
	}
	else
	{
	    /* These lines are good for debigging when 
	       it all goes wrong */
	    //System.err.println("Remider to Kelly to fix this"+p.getClass().getName()+"  >>> "+p.getPosition());
	    //System.err.println("Important error. This is kind of tom kelly's fault. When you use changePosition on a prop, create a new RealPos with the position. Otherwise it acts a pointer, and the value is changed outside of changePosition, so we can't tell what has changes, and from there the shit really hits the fan: *** Spotlight.remove called for non-existant prop"+p.getClass().getName()+"  >>> "+p.getPosition());
	}
    }

    /** 
    Checks to see if any lists needs updating 
    */
    protected void changeChecks()
    {
	/* for physics */
	if (forPhysics()) 
	{
	    if (!forPhysics.contains(this))
	    {
		forPhysics.add(this);
	    }
	}
	else
	{
	    if (forPhysics.contains(this)) forPhysics.remove(this);
	}

	/* for the audience */
        if (!forAudience.contains(this))
	{
	    forAudience.add(this);
	}
    }

    /**
      Check for physics
      @return output whether the physics engine is interested in
              this spotlight
    */
    private boolean forPhysics()
    {
	Prop p;

	for (int i = 0; i< items.size(); i++)
	{
	    p = (Prop)items.get(i);
            if (p instanceof Flying)
	    {
		/* Physics runs on mud too & flying too*/
		if (muddy || wet) return true;
		if (items.size() > 1)
		{
		    return true;
		}
		else
		{
		    return false;
		}
	    } 
	}
	return false;
    }
}
