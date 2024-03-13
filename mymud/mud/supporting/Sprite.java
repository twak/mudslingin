package mud.supporting;
import java.awt.Image;
import java.io.*;
/**
Place holder class until someone
codes Sprite

if the dir and num are -1 then this
is a dummy sprite that is generated
in game time (like text)

*/

public class Sprite implements Serializable 
{

    transient private Image me;
    transient SpriteLoader allSprites;
    private String meSource;
    /* Which directory number, and other number
       this sprite is */
    private int dir, num;

    /**
    creates a dummy sprite UNUSED?
    @param s a SpriteLoader 
    */
 
    public Sprite(SpriteLoader s)
    {
        allSprites = s;
        me = allSprites.getImage("data/default.png");
	meSource = "data/default.png";
       /* Creates a default sprite in Image me*/
    }

    /**
    Creates a sprite from a filename UNUSED?
    @param d the directory number that this sprite came from
    @param n the number of this sprite
    */
    public Sprite(String filename, SpriteLoader s, int d, int n)
    {
	dir = d;
	num = n;
        allSprites = s;
        me = allSprites.getImage(filename);
	meSource = filename;
        /* Set up Image me to be loaded from
           file filename */
    }

    /**
    Creates a new sprite with the specifed image
    not guaranteed to load up again...
    */
    public Sprite (Image i, 
		   SpriteLoader s, 
		   String source, 
		   int d, 
		   int n)
    {
	dir = d;
	num = n;
	me = i;
	allSprites = s;
	meSource = source;
    }


    /**
    Returns the current Image of this prop
    @return me the Image
    */
 
    public Image getImage()
    {
        return me;
    }

    /** 
    Shows images not loaded by the imageLoader
    @param image the image to set
    */
    public void setImage(Image image)
    {
	me = image;
    }

    /**
    Sets the sprite loader after we are
    loaded from network/disk
    @param sl the spriteLoader to use
    */
    public void setSpriteLoader(SpriteLoader sl)
    {
	System.err.println("Trying to load " + meSource);
	allSprites = sl;
        me = allSprites.getImage(meSource);
    }

    /**
    Returns the directory that this sprite came from
    @return the directory no.
    */
    public int getDirectory()
    {
	return dir;
    }


    /**
    Returns the sprite number (in the directory number)
    that this sprite came from
    @return the sprite number
    */
    public int getNumber()
    {
	return num;
    }
}

