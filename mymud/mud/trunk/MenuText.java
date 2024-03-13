/* $Id: MenuText.java,v 1.2 2004/03/04 16:04:03 tk1748 Exp $ */

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
  Displays a place for a user to enter a name as text
 */

public class MenuText extends MenuItem implements NextKey
{

    /* Maximum length of this string item */
    private int length;
    private MenuOptionInterface MPO;
    private int number;
    private KeyAction ka;
    private StringBuffer words = new StringBuffer();
    private boolean changed = false;
    /* The ip in string form */
    private String IP, header;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param ip  the name to display
       @param h the header to come before the naem
    */
    public MenuText           (MenuOptionInterface mp, 
			       Stage    s,
			       String   ip,
			       String   h)
    {
	header = h;
	IP = ip;
	length = 16;
	/* Add a carrot */
	words.append(ip);
	ka = s.getDirector().getExecutive().getKeyAction();

	MPO = mp;

	setUpMenuPage((MenuPage)mp,s,header+words);
    }

    /**
       Creates a new Item of a menu group, as above but allows
       different maximum lengths
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param h  the name to display
       @param l length of this text tiem
    */
    public MenuText           (MenuOptionInterface mp, 
			       Stage    s,
			       String   ip,
			       String   h,
			       int      l)
    {
	length = l;
	header = h;
	IP = ip;
	
	/* Add a carrot */
	words.append(ip);
	ka = s.getDirector().getExecutive().getKeyAction();

	MPO = mp;

	setUpMenuPage((MenuPage)mp,s,header+words);
    }


    /**
       Gets the ip string right now
     */
    private String getText()
    {
	return IP;
    }

    /**
       Called to catch
       @param e a key event of what's justa happened
     */
    public void nextKey(KeyEvent e)
    {
	char c = e.getKeyChar();
	if (e.getKeyCode() ==  KeyEvent.VK_ESCAPE) System.exit(0);
	if (e.getKeyCode() == KeyEvent.VK_ENTER)
	{
	    /* remove carrot */
	    words.deleteCharAt(words.length()-1);

	    IP=words.toString();

	    MPO.text(IP);
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
	else if (Character.isLetterOrDigit(c) || c == '_' || c == '.')
	{
	    /* Limit messagelength */
	    if (words.length() < length)
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
       Our chance to be run once a frame
    */
    public void doOwnPhysics()
    {
	if (changed)
	{
	    setText(header+words);
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
