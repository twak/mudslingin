/* $Id: MenuStats.java,v 1.2 2004/02/04 21:57:12 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;


public class MenuStats extends MenuItem
{
    MenuPage gotoPage;

    Stage stage;
    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param t  the text ob
    */
    public MenuStats   (MenuPage mp, 
                        Stage    s)
    {
	stage = s;
	if (isDisplayed())
	{
	    setUpMenuPage(mp,s,"Show Stats - true");
	}
	else
	{
	    setUpMenuPage(mp,s,"Show Stats - false");
	}
    }

    
    /**
       Take the counters of or onto the stage
     */
    public int doOnActivate()
    {
	if (!isDisplayed())
	{
	    Stage s = myStage();
	    setText("Show Stats - true");
	    myStage().getGameValue().propCounter =
		new Text(new RealPos(700,50),s,1,"");
	    myStage().getGameValue().frameCounter =
		new Text(new RealPos(700,30),s,1,"");
	    myStage().getGameValue().fpsCounter =
		new Text(new RealPos(700,40),s,1,"[FPS]");
	}
	else
	{
	    setText("Show Stats - false");
	    myStage().getGameValue().propCounter.finalCurtain();
	    myStage().getGameValue().propCounter = null;
	    myStage().getGameValue().frameCounter.finalCurtain();
	    myStage().getGameValue().frameCounter = null;
	    myStage().getGameValue().fpsCounter.finalCurtain();
	    myStage().getGameValue().fpsCounter = null;
	}
	return -1;
    }


    /**
       Returns true if the menus are currently
       being displayed, false otherwise
       @return output if the menus are displayed
     */
    private boolean isDisplayed()
    {
	boolean output = stage.getGameValue().propCounter != null;
	return output;
    }
}
