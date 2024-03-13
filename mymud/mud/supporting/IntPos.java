/* $Id: IntPos.java,v 1.5 2004/03/09 11:03:11 tk1748 Exp $ */
package mud.supporting;

import java.io.*;

/**
This represents a discrete position on our game space
*/
public class IntPos implements Serializable 
{
    private int xPos, yPos;
    
    /**
       Makes a position at x,y
       @param x x-coord of position
       @param y y-coord of position
     */
    public IntPos(int x, int y)
    {
	xPos = x;
	yPos = y;
    }

    /**
       Creates a point at the origin
    */
    public IntPos()
    {
	xPos = 0;
	yPos = 0;
    }

    /**
    Creates a continuous point in game
    space from a discrete one
    @param i integer position to convert
    */
    public IntPos(RealPos i)
    {
	xPos = fromDouble(i.getX());
	yPos = fromDouble(i.getY());
    }

    /**
    returns the horizontal component
    of the position
    */
    public int getX()
    {
	return xPos;
    }


    /**
    returns the verticle component 
    of the position
    */
    public int getY()
    {
	return yPos;
    }


    /**
    Sets the position
    @param x x-coordinate(horizontal)
    @param y y-coordinate(verticle)
    */
    public void set(int x, int y)
    {
	xPos = x;
	yPos = y;
    }

    /**
    Increments x value
    @param x x-ammound to increment (horizontal)
    */
    public void incX(int x)
    {
	xPos = xPos + x;
    }

    /**
    Increments Y value
    @param y y-ammound to increment (horizontal)
    */
    public void incY(int y)
    {
	yPos = yPos + y;
    }


    /**
    sets the horizontal position
    @param x the value to assign to x-coord.
    */
    public IntPos setX(int x)
    {
	xPos = x;
	return this;
    }


    /** 
    sets the y-position
    @param y the value to set the verticle loction to
    */
    public IntPos setY(int y)
    {
	yPos = y;
	return this;
    }

    private int fromDouble(double d)
    {
	return new Long(Math.round(d)).intValue();
    }

    /**
       Displays in (x,y) format
     */
    public String toString()
    {
	return "("+xPos+","+yPos+")";
    }
}
