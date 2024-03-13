/* $Id: Speak.java,v 1.6 2004/03/08 18:05:51 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import mud.*;
import mud.input.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;
/**
   This happens when a player "talks" or sends a 
   message across the network

   This object is not surrently save-proof. It was made
   for the multiplayer game, which at the current time
   does not supprt saving
 */
public class Speak extends Prop implements NextKey
{
    private     KeyAction ka;
    /* Time a message is displayed for in frames */
    private static int    TIME_OUT = 400;
    /* Counter for displaying */
    private     int       timeOut=-1;
    /* Our message to the world */
    private     StringBuffer words = new StringBuffer();

    private transient    Sprite       mySprite;
    protected   Color        myColour = Color.white;
    private     Font         font;
    private     FontMetrics  fm;
    /* this is just a reference so we can get fm */
    private    transient Graphics2D g;
	/* Changed is if we have changed our value, silent is if we are not on stage (client)*/
    private boolean          changed = false, silent = false;
    private Stage myStage;
    private Player player;
    private static int YOFF = 140;

    /** 
    Constructor: this is for when the person is writing
    @param s the stage the player is on
    @param p the player that is speaking
    */
    public Speak(Stage s, Player p)
    {
	player = p;
	RealPos rp = new RealPos(p.getPosition().getX(),
				 p.getPosition().getY() );
	/* The offset of the words */
	rp.increment(0,YOFF);
	words.append('I');
	BufferedImage tmp = getGC().createCompatibleImage( 1,1, Transparency.BITMASK);
	g = tmp.createGraphics();
	/* EVENTUALLY this needs to check that "arial" exists"*/
	font = new Font("arial", Font.BOLD, 13);
	fm = g.getFontMetrics(font);
	mySprite = new Sprite(getImage("I"),s.getSpriteLoader(), "default.gif",-1,-1);
	setUpProp(rp, s, mySprite);
        ka = s.getDirector().getExecutive().getKeyAction();
	ka.stayLock(this);
	myStage = s;
    }

    /**
	This is the consturctor for a speak that has already been said and doesnt
	need keyboard shananegans
	@param s the stage to put tthe speak on
	@param p the play who speaked
	@param said what was said
    */
    public Speak(Stage s, Player p, String said)
    {
	player = p;
	RealPos rp = new RealPos(p.getPosition().getX(),
				 p.getPosition().getY() );
	/* The offset of the words */
	rp.increment(0,YOFF);
	words = new StringBuffer(said);
	BufferedImage tmp = getGC().createCompatibleImage( 1,1, Transparency.BITMASK);
	g = tmp.createGraphics();
	/* EVENTUALLY this needs to check that "arial" exists"*/
	font = new Font("arial", Font.BOLD, 13);
	fm = g.getFontMetrics(font);
	mySprite = new Sprite(getImage("I"),s.getSpriteLoader(), "default.gif",-1,-1);
	setUpProp(rp, s, mySprite);

	changed = true;
	timeOut = TIME_OUT;        

	myStage = s;
    }

    /**
	Dummy constructor that doesnt add to stage
     */
    public Speak(Stage s, Player p, int diff)
    {
	silent = true;
	player = p;
	RealPos rp = new RealPos(p.getPosition().getX(),
				 p.getPosition().getY() );
	/* The offset of the words */
	rp.increment(0,YOFF);
	words.append('I');
	BufferedImage tmp = getGC().createCompatibleImage( 1,1, Transparency.BITMASK);
	g = tmp.createGraphics();
	/* EVENTUALLY this needs to check that "arial" exists"*/
	font = new Font("arial", Font.BOLD, 13);
	fm = g.getFontMetrics(font);
	mySprite = new Sprite(getImage("I"),s.getSpriteLoader(), "default.gif",-1,-1);
	//setUpProp(rp, s, mySprite);
        ka = s.getDirector().getExecutive().getKeyAction();
	ka.stayLock(this);
	myStage = s;
    }


    /**
	@return the string we are sayin
    */
    public String getWords()
    {
	return words.toString();
    }
     
    /**
	Are we not still in input mode
	@return are we?
    */
    public boolean isFinal()
    {
	return timeOut > 0;
    }

    /**
    Man, does the physics of the man
    */
    public void doPhysics()
    {
	if (timeOut > 0) 
	{
	    timeOut--;
	} else if (timeOut == 0)
	{
	    finalCurtain();
	}
	if (changed)
	{
	    mySprite.setImage(getImage(words.toString()));
	    setPosition();
            /* re center the text */
	    changeSprite(mySprite);
	    changed = false;
	}
    }

    /**
       This is for networking, so we can tell when we should
       send this prop
     */
    private void setValue()
    {
	myStage.clientBroadcast(words.toString());//addSpeak(new RealPos (100,100),words.toString());
    }

    public void setPosition()
    {
	RealPos rp = getPosition();

	/* Horizontal and Verticle offset */
	double hoff = ((BufferedImage)mySprite.getImage()).getWidth();
	double voff = ((BufferedImage)mySprite.getImage()).getHeight();

        /* Distance from edge to middle */
	double x = rp.getX();
	double y = rp.getY();
	
        if (x+hoff > 800)x = (800-hoff);

	changePosition(new RealPos(x,y));
    }

    /**
       Called when someone puts text into this object, called
       out of sequence, we add the letter to the our string buffer
       of what we learnt
     */
    public void nextKey(KeyEvent e)
    {
	char c = e.getKeyChar();
	if (e.getKeyCode() ==  KeyEvent.VK_ESCAPE) System.exit(0);
	if (e.getKeyCode() == KeyEvent.VK_ENTER)
	{
	    words.deleteCharAt(words.length()-1);
	    changed = true;
	    ka.unLock();
	    /* Notify network if necessary */
	    setValue();
	    /* on client we dont talk */
	    if (!silent)new Speak(myStage, player, words.toString());
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
	else	if (c != KeyEvent.CHAR_UNDEFINED)
	{
	    /* Limit messagelength */
	    if (words.length() < 100)
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
	//System.err.println(words.toString());
    }

    /** 
    Converts text into a displayable image
    @param text the text to convert
    @return output the image
    */
    protected BufferedImage getImage(String text)
    {
	if (text.compareTo("")==0) text = " ";
	int width = fm.stringWidth(text);
	int height = fm.getAscent() + fm.getDescent();

        BufferedImage output = getGC().createCompatibleImage( width, height, Transparency.BITMASK);

	Graphics2D g = output.createGraphics();
	g = output.createGraphics();

	g.setFont(font);
       
	g.setColor(myColour);
	g.drawString(text,0,fm.getAscent());
	return output;

    }

}





