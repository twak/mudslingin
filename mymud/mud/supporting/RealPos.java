/* $Id: RealPos.java,v 1.4 2003/12/09 02:14:01 tk1748 Exp $ */
package mud.supporting;

import java.io.*;

/**
This represents a position on our game space
*/
public class RealPos implements Serializable 
{
    private double xPos, yPos;
    

    /**
       Makes a position at x,y
       @param x x-coord of position
       @param y y-coord of position
     */
    public RealPos(double x, double y)
    {
	xPos = x;
	yPos = y;
    }


    /**
       Creates a point at the origin
    */
    public RealPos()
    {
	xPos = 0;
	yPos = 0;
    }


    /**
    Creates a continuous point in game
    space from a discrete one
    @param i integer position to convert
    */
    public RealPos(IntPos i)
    {
	xPos = fromInt(i.getX());
	yPos = fromInt(i.getY());
    }


    /** 
    Sets the x-coord
    @param x the value to set it to.
    */
    public RealPos setX(double x)
    {
	xPos = x;
	return this;
    }


    /**     Sets the y-coord 
    @param y the value to set it to.
    */
    public RealPos setY(double y)
    {
        yPos = y;
	return this;
    }

    /** 
    Sets the position
    @param x the horizontal position
    @param y the verticle position
    */
    public void set(double x, double y)
    {
	xPos = x;
	yPos = y;
    }

    /**
    Increments the position in either direction
    @param x the ammount to change the horizontal postition
    @param y the ammount to change the verticle position
    */
    public void increment(double x, double y)
    {
	xPos += x;
	yPos += y;
    }


    /**
    returns the horizontal component
    of the position
    */
    public double getX()
    {
	return xPos;
    }


    /**
    returns the verticle component 
    of the position
    */
    public double getY()
    {
	return yPos;
    }

    private double fromInt(int i)
    {
	return new Double(i).doubleValue();
    }

    /**
       Displays in (x,y) format
     */
    public String toString()
    {
	return "("+xPos+","+yPos+")";
    }
}
