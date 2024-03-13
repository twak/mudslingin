/* $Id: Text.java,v 1.14 2004/03/01 17:57:23 tk1748 Exp $*/
package mud.trunk;

import mud.supporting.*;
import mud.trunk.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.*;

//import java.text.*;

/**
A Basic prop to display text on the stage.
*/

public class Text extends Prop
{

    private int counter;
    private String text;
    private Sprite mySprite;

    /**
    Displays text as a prop on the Stage
    @param rp the position of the text
    @param s  the stage for this text to appear on
    @param c  the number of frames the writing should appear for. If negative or zero the prop is never removed from the stage.
    @param t  what should be displayed
    */
    public Text(RealPos rp, Stage s, int c, String t)
    {
	text = t;
	mySprite = new Sprite(getImage(),s.getSpriteLoader(), "default.gif",-1,-1);
	setUpProp(rp , s,  mySprite);
	counter = c;
	changeSprite(mySprite);
    }

    /**
    Constructor that positions text at the top left of the
    screen for 60 frames
    @param s the stage
    @param t the message to display
    */
    public Text(Stage s, String t)
    {
	text = t;
	mySprite = new Sprite(getImage(),s.getSpriteLoader(), "default.gif",-1,-1);
	setUpProp(new RealPos(0,0) , s, mySprite);

	counter = 60;
	mySprite.setImage(getImage());
	changeSprite(mySprite);
    }

    /** 
    Converts text into a displayable image
    */
    private BufferedImage getImage()
    {
	/* The *6 is a guess at the average width of a letter */
	int width = text.length()*10+1;

        BufferedImage output = getGC().createCompatibleImage( width, 20, Transparency.BITMASK);
	
	Graphics2D g = output.createGraphics();

	/* Convert our string */
	//AttributedString out = new AttributedString(text);
	// we do it that way for proper text, for now a shortcut
	g.setColor(java.awt.Color.ORANGE);
	g.drawString(text,0,20);
	return output;
    }

    /**
    Changes the string and the time it is on stage for
    @param s the new message
    @param time the number of frames to be on stage for
    */
    public void setString(String s, int time)
    {
	text = s;
	counter = time;
	mySprite.setImage(getImage());
	changeSprite(mySprite);
    }

    /**
    Overrides the physics method to make this
    text item dissapear after a certain time
    */
    public void doPhysics()
    {
	counter--;
	if (counter == 0)
	{
	    text = "";
	    mySprite.setImage(getImage());
	    changeSprite(mySprite);
        }
    }
}
