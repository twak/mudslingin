/* $Id: MenuOptionKey.java,v 1.1 2004/02/14 13:05:07 tk1748 Exp $ */

package mud.trunk;

import mud.input.*;
import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
   Tells the options system to save to disk
 */

public class MenuOptionKey extends MenuItem implements NextKey
{
    private int row, vk;
    private String name;
    private MenuPageOptions MPO;
    private KeyAction myKeyAction;
   /* If hot we have been selected and are waiting
       key input. If changed, a key event that we
       were listening for happned*/
    private boolean hot = false, changed = false;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param c  the name to display
       @param kc the keycode
       @param r The row in MenuPageOption's ks arrays we are.
    */
    public MenuOptionKey    (MenuPage mp, 
			     Stage    s,
			     KeyAction ka,
			     String   c,
			     int      kc,
			     int      r)
    {
	myKeyAction = ka;
	row = r;
	name = c;
	vk = kc;
	try
	{
	    MPO = (MenuPageOptions)mp;
	}
	catch (Exception e)
	{
	    System.err.println("Bad thingy in MenuOptionKey");
	    System.exit(-1);
	}
	setUpMenuPage(mp,s,getName());
    }

    private String getName()
    {
	String v = MPO.getVKString(vk);
	v = v.substring(v.indexOf('_')+1, v.length());
	if (hot) v = "Press Key";
	return (name+"   "+v);
    }

    /**
       This can be called at any time, so 
       changed made by doOwnPhysics below
       @param e the event that happneded
     */
    public void nextKey(KeyEvent e)
    {
	vk = e.getKeyCode();
	changed = true;
    }

    /**
       Overrides a method in menuitem to allow
       us to check if we need to be updated
     */
    public void doOwnPhysics()
    {
	if (changed)
	{
	    hot = false;
	    MPO.setKey(this, row, vk);
	    setText(getName());
	    changed = false;
	}
    }

    /**
       When we are told to quit
       @param int menu system code, -1 = normal
     */
    public int doOnActivate()
    {
	if (!hot)
	{
	    hot = true;
	    myKeyAction.nextKey(this);
	}
	setText(getName());
	
	return -1;
    }
}
