/* $Id: MenuOptionInt.java,v 1.1 2004/02/14 13:05:07 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   This is an yes/no choice in the menu choice system
 */

public class MenuOptionInt extends MenuItem
{

    private String name;
    private MenuPageOptions MPO;
    private int number, min, stepNo, stepSize, value;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to display
       @param m a number, returned on activate
       @param minn the starting value
       @param stepNumber the nubmer of steps
       @param stepSizee the size of the steps
       @param valuee the value at start
    */
    public MenuOptionInt    (MenuPage mp, 
			     Stage    s,
			     String   n,
			     int      m,
			     int    minn,
			     int    stepNumber,
			     int    stepSizee,
			     int    valuee)
    {
	min = minn;
	value = valuee;
	stepNo = stepNumber;
	stepSize = stepSizee;
	name = n;
	number = m;
	try
	{
	    MPO = (MenuPageOptions)mp;
	}
	catch (Exception e)
	{
	    System.err.println("MenuOptionBool may only be made by MenuPageOption");
	    System.exit(-1);
	}
	setUpMenuPage(mp,s,name());
    }

    private String name()
    {
	return (name+" "+ value);
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	value = value + stepSize;
	if (value > min+stepNo*stepSize)
	{
	    value = min;
	}
	setText(name());
	MPO.intSet(this,value,number);

	return -1;
    }
}
