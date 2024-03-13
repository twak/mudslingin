/* $Id: MenuItem.java,v 1.11 2004/04/22 16:19:52 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;

/**
   This is the base class for all items in a menuPage.
   They are the individual items that can be selected
   in a menu
 */
public class MenuItem extends Prop
{

    private     Object value;
    protected   boolean selected, onStage;
    private     String name;
    private     Sprite mySprite;
    protected   Color myColour;
    private     Font font;
    private     FontMetrics fm;
    /* this is just a reference so we can get fm */
    private    transient Graphics2D g;
    protected   int currentSize, sizeToBe;
    private     RealPos myPos;

    /**
       Tells extending classes that they are in the
       menu and are allowed to move when in menu mode
    */
    protected MenuItem()
    {
	iAmAMenu();
    }

    /**
       Creates a new Item of a menu group
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to be displayed
    */
    public MenuItem(MenuPage mp, Stage s, String n)
    {
	setUpMenuPage(mp,s,n);
	iAmAMenu();
    }

    /**
       Initialises the menu item
       @param mp the menu page we are to be displayed on
       @param s  the stage to be displayed on
       @param n  the name to be displayed   
     */
    public void setUpMenuPage(MenuPage mp, Stage s, String n)
    {
	name = n;
	myColour = Color.ORANGE;
	currentSize = 30;
	sizeToBe    = 30;
	myPos = new RealPos(0,0);
        BufferedImage tmp = getGC().createCompatibleImage( 1,1, Transparency.BITMASK);
	g = tmp.createGraphics();
	/* this needs to check that "arial" exists" */
	font = new Font("arial", Font.BOLD, currentSize);
	fm = g.getFontMetrics(font);
	mySprite = new Sprite(getImage(n),s.getSpriteLoader(), "default.gif",-1,-1);
	setUpProp(myPos, s, mySprite);
        value = new Boolean(false);
	mp.add(this);
	onStage = true;
    }

    public void remove(MenuPage mp)
    {
	mp.remove(this);
    }

    /**
       Moves the item so its center (equally
       between each corner) is at the position
       @param rp the centre position
    */
    public void setMiddle(RealPos rp)
    {
	if (rp==null)
	{
	    rp = myPos;
	}
	else
	{
	    myPos = rp;
	}

	/* Horizontal and Verticle offset */
	double hoff = ((BufferedImage)mySprite.getImage()).getWidth();
	double voff = ((BufferedImage)mySprite.getImage()).getHeight();
	hoff = hoff/2;
	voff = voff/2;

	/* Distance from edge to middle */
	double x = rp.getX()-hoff;
	double y = rp.getY()-voff;

	changePosition(new RealPos(x,y));
    }

    /**
       Called when this item is selected by the user. Item
        dependent code is located in doOnEnter()
    */
    public final void onEnter()
    {
	doOnEnter();
	mySprite.setImage(getImage(name));
//	changeSprite(mySprite);
    }

    /**
       Called when this item is left by the user. Item
        dependent code is located in doOnLeave()
    */
    public final Object onLeave()
    {
	doOnLeave();

	mySprite.setImage(getImage(name));
//	changeSprite(mySprite);

	return value;
    }

    /** 
      Method to be overwritten to define behavour
      when selected.
    */
    public void doOnEnter()
    {
	myColour = Color.RED;
	selected = true;
	sizeToBe = 40;
    }

    /** 
      Method to be overwritten to define behavour
      when selected.
    */
    public void doOnLeave()
    {
	myColour = Color.ORANGE;
	selected = false;
	sizeToBe = 30;
    }

    /**
       This gets called when someone activates a menu item
       @return the result of the activation
     */
    public final int onActivate()
    {
	myStage().playSound(0,1);
	return doOnActivate();
    }

    /**
       Over written method when someone pushed the fire key
       @return result of activation
     */
    public int doOnActivate()
    {
	System.out.println("Someone's pushed my button");
	return -1;
    }

    /** 
      Method to be overwritten to define behavour
      player pushed "previous weapon"
    */
    public void doOnLeft()
    {
	System.out.println("Eeeeh-ergh");
    }

    /** 
      Method to be overwritten to define behavour
      player pushed "next weapon"
    */
    public void doOnRight()
    {
	System.out.println("Eeeeh-ergh");
    }

    /**
       Moves and resizes self as needed
    */
    public final void doPhysics()
    {
	/* If any itmes want ot do their won stuff*/
	doOwnPhysics();

	if (sizeToBe != currentSize)
	{
	    if (sizeToBe > currentSize)
	    {
		currentSize+=5;
	    }
	    else
	    {
		currentSize-=2;
	    }

	    /* Appy changes to font */
	    font = new Font("arial", Font.BOLD, currentSize);
	    fm = g.getFontMetrics(font);

	    /* propogate & register change */
	    mySprite.setImage(getImage(name));
            /* re center the text */
	    changeSprite(mySprite);

	    setMiddle(null);
	}
    }
    /**
       Method to be overridden allowing items
       to do things each frame
     */
    public void doOwnPhysics()
    {
    }

    /**
      Returns the value of this menu object
    */
    public Object getValue()
    {
	return value;
    }


    /**
       This forces this menu item to get back
       onto 
       @param the stage
     */
    public void backOnStage(Stage the)
    {
	onStage = true;
	setUpProp(new RealPos(0,-200), the, mySprite);
    }

    /**
       Sets the size of this menu item's text
     */
    public void setSize(float s)
    {
	font = font.deriveFont(s); 
	fm = g.getFontMetrics(font);

	mySprite.setImage(getImage(name));
	changeSprite(mySprite);
    }

    /**
       sets flag if we are on stage
       @param b the flag
     */
    public void setOnStage(boolean b)
    {
	onStage = b;
    }

    /**
       Change what is displayed
       @param n The text to display
     */
    public void setText(String n)
    {
	name = n;
	mySprite.setImage(getImage(name));
	if (onStage)
	{
	    changeSprite(mySprite);
	}
	setMiddle(null);
    }

    /**
       Sets the value of this object
     */
    public void setValue(Object v)
    {
	value = v;
 	mySprite.setImage(getImage(name + value.toString()));
	changeSprite(mySprite);
    }

    /**
       Sets the colour of the menu itme. This is
       only really ment for the MenuTitle file
     */
    public void setColour(Color c)
    {
	myColour = c;
	mySprite.setImage(getImage(name));
	changeSprite(mySprite);
    }

    /** 
    Converts text into a displayable image
    @param text the text to convert
    @param size the font size
    @return output the image
    */
    protected BufferedImage getImage(String text)
    {
	int width = fm.stringWidth(text);
	int height = fm.getAscent() + fm.getDescent();

        BufferedImage output = getGC().createCompatibleImage( width, height, Transparency.BITMASK);

	//BufferedImage output = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB );
	Graphics2D g = output.createGraphics();
	g = output.createGraphics();

	g.setFont(font);
       
	g.setColor(myColour);
	g.drawString(text,0,fm.getAscent());
	return output;

    }

}



