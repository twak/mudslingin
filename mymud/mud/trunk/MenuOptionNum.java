/* $Id: MenuOptionNum.java,v 1.1 2004/03/04 12:52:36 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;
import mud.*;
import mud.input.*;
import java.awt.event.KeyEvent;

/**
   A way of dynamically creating menu pages listeners
 */

public class MenuOptionNum extends MenuItem implements NextKey
{


    private MenuPageOptions MPO;
    private int number;
    /* IPv6 is OTT for a second year project, so v4 will do */
    private int num = 0;
    private KeyAction ka;
    private StringBuffer words = new StringBuffer();
    private boolean changed = false;
    /* The ip in string form */
    private String IP;
    /* Name as displayed */
    private String name;
    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to display
       @param m a number, returned on activate
    */
    public MenuOptionNum       (MenuPage mp, 
			        Stage    s,
				String   n,
			        String   ip)
    {
	name = n;
	IP = ip;
	try
	{
	    setIP(IP);
	}
	catch (Exception e)
	{
	    /* Bad ip, default to 0,0,0,0 */
	}
	/* Add a carrot */
	words.append("I");
	ka = s.getDirector().getExecutive().getKeyAction();
	try
	{
	    MPO = (MenuPageOptions)mp;
	}
	catch (Exception e)
	{
	    System.err.println("MenuOptionBool may only be made by MenuPageOption");
	    System.exit(-1);
	}
	setUpMenuPage(mp,s,name+getIPS());
    }


    /**
       Gets the ip string right now
     */
    private String getIPS()
    {
	return new Integer(num).toString();
    }

    public void nextKey(KeyEvent e)
    {
	char c = e.getKeyChar();
	if (e.getKeyCode() ==  KeyEvent.VK_ESCAPE) System.exit(0);
	if (e.getKeyCode() == KeyEvent.VK_ENTER)
	{
	    /* remove carrot */
	    words.deleteCharAt(words.length()-1);
	    try
	    {
		setIP(words.toString());
	    }
	    catch (Exception f)
	    {
		/* Cant parse IP */
		words = new StringBuffer("0");
		num = 0;
		System.out.println("ET_ERR - bad IP");
	    }
	    MPO.gotNumber(new Integer(getIPS()).intValue(),name);
	    changed = true;
	    ka.unLock();
	}
	else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                  e.getKeyCode() == KeyEvent.VK_DELETE       )
	{
	    /* Cant delete from an empty string */
	    if (words.length() > 1)
	    {
		words.deleteCharAt(words.length()-1);
		words.deleteCharAt(words.length()-1);
		words.append('I');
		changed = true;
	    }
	} 
	else if (Character.isDigit(c))
	{
	    /* Limit messagelength */
	    if (words.length() < 16)
	    {
		words.deleteCharAt(words.length()-1);
		words.append(c);
		words.append('I');
	    }
	    else
	    {
		System.out.println("ET_ERGH - too long");
	    }
	    changed = true;
	}

    }

    /**
       Throws excpetion if not a good entry
       @param w the number
     */

    public void setIP(String w) throws Exception
    {
	 num = (new Integer(Integer.parseInt(w))).intValue();
    }	


    /**
       Our chance to be run once a frame
    */
    public void doOwnPhysics()
    {
	if (changed)
	{
	    setText(name+words);
	    changed = false;
	}
    }


    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	ka.stayLock(this);
	words = new StringBuffer("I");
	changed = true;
	doOwnPhysics();
	return -1;
    }
}
