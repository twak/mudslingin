/* $Id: MenuFPS.java,v 1.2 2004/01/29 23:51:08 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;


public class MenuFPS extends MenuItem
{

    int fps;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param t  the text ob
    */
    public MenuFPS   (MenuPage mp, 
                        Stage    s)
    {
	fps = s.getGameValue().fps;
	setUpMenuPage(mp,s,"speed - "+fps);
    }

    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	fps = roundfps(fps);
	myStage().getGameValue().fps = fps;
	setText("speed - "+fps);
	return -1;
    }

    /**
       Designated the steps that the speed increaces in
     */
    private int roundfps(int input)
    {
	if (input > 1000)
	{
	    return 5;
	}
	else if (input <= 15)
	{
	    input = input + 5;
	}
	else if (input <= 30)
	{
	    input = input + 10;
	}
	else if (input < 50)
	{
	    input = input + 15;
	}
	else
	{
	    input = input + 500;
	}
	return input;
    }
}
