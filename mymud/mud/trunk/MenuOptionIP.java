/* $Id: MenuOptionIP.java,v 1.3 2004/03/08 18:05:51 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;
import java.net.*;
import mud.broadcast.*;
import mud.*;
import mud.broadcast.*;
import mud.input.*;
import java.awt.event.KeyEvent;

/**
   A menu item that returns a Inet address
 */

public class MenuOptionIP extends MenuClientItem implements NextKey
{
    private MenuPageClient MPO;
    private int number;
    /* IPv6 is OTT for a second year project, so v4 will do */
    private int ip1 = 127,ip2 = 0,ip3 = 0,ip4 = 1;
    private KeyAction ka;
    private StringBuffer words = new StringBuffer();
    private boolean changed = false, setConnecting = false;
    /* The ip in string form */
    private String IP;

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to display
       @param m a number, returned on activate
    */
    public MenuOptionIP       (MenuPage mp, 
			       Stage    s,
			       String   ip)
    {
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
	words.append("127.0.0.1I");
	ka = s.getDirector().getExecutive().getKeyAction();
	try
	{
	    MPO = (MenuPageClient)mp;
	}
	catch (Exception e)
	{
	    System.err.println("MenuOptionIP may only be made by MenuPageClient");
	    e.printStackTrace();
	    System.exit(-1);
	}
	setUpMenuPage(mp,s,"set IP: "+getIPS());
    }


    /**
       Gets the ip string right now
     */
    private String getIPS()
    {
	return ip1+"."+ip2+"."+ip3+"."+ip4;
    }

    private byte[] byteIP()
    {
	byte[] out = new byte[4];

	out[0] = (byte)ip1;
	out[1] = (byte)ip2;
	out[2] = (byte)ip3;
	out[3] = (byte)ip4;
	return out;
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
		ka.unLock();
		changed = true;
		setIP(words.toString());
		
		setConnecting = true;
		Socket gameSocket = null;
		/* Call something to do the hard work */
		ServerConnect sc = new mud.broadcast.ServerConnect(byteIP(), myStage().getGameValue().port,this );
		/* Go thread... */
		sc.start();

	    }
	    catch (Exception f)
	    {
		/* Cant parse IP */
		words = new StringBuffer("127.0.0.1");
		ip1 = 127; ip2 = 0; ip3 = 0; ip4 = 1;
		System.out.println("ET_ERR - bad IP");
	    }

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
	else if (Character.isDigit(c) || c == '.')
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

    public void setIP(String w) throws Exception
    {
	 String[] splitUp;
	 splitUp = w.split("[.]");

	 ip1 = (new Integer(Integer.parseInt(splitUp[0]))).intValue();
	 ip2 = (new Integer(Integer.parseInt(splitUp[1]))).intValue();
	 ip3 = (new Integer(Integer.parseInt(splitUp[2]))).intValue();
	 ip4 = (new Integer(Integer.parseInt(splitUp[3]))).intValue();
    }	


    /**
       Our chance to be run once a frame
    */
    public void doOwnPhysics2()
    {
	if (state == 2 || state == 3) setText("set IP FAILED:"+words);
	if (changed)
	{
	    setText("set IP:"+words);
	    if (setConnecting) setText("conecting... ");
	    changed = false;
	}
    }


    /**
       When we are told to quit
     */
    public int doOnActivate()
    {
	ka.stayLock(this);
	words = new StringBuffer("127.0.0.1I");
	changed = true;
	doOwnPhysics2();
	return -1;
    }
}
