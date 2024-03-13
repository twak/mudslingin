/* $Id: Background.java,v 1.1 2004/01/29 23:58:32 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.IntPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;

/**
   Class to load the background iamge from the disk and to stream it too
   and from the disk when saving.
 */
public class Background implements Serializable
{
    transient private Image image;
    transient private String fileName;

    public Background(String file, Stage stg)
    {
	if (file == null)
	{
	    image = null;
	}
	else
	{
	    fileName = file;
	    image = fromFile(fileName);
	    
	    if (stg.getGameValue().mapSizeX != ((BufferedImage)image).getWidth())
	    {
		System.err.println("\""+fileName+"\" File is wrong width");
		System.exit(-1);
	    }

	    if (stg.getGameValue().mapSizeY != ((BufferedImage)image).getHeight())
	    {
		System.err.println("\""+fileName+"\" File is wrong height");
		System.exit(-1);
	    }
	}
    }

    /**
       The image of the background
       @return background
     */
    public Image getImage()
    {
	return image;
    }

    /**
       Streams the object to the disk when saving
    */
    private void writeObject(java.io.ObjectOutputStream out) 
                                      throws IOException
    {
	out.writeObject(fileName);
    }

    /**
       Streams the object from the disk when loading
    */
    private void readObject(java.io.ObjectInputStream in)     
	                              throws IOException, ClassNotFoundException
    {
	fileName = (String)in.readObject();
	if (fileName != null)
	{
	    image = fromFile(fileName);
	}
	else
	{
	    image = null;
	}
    }

    private Image fromFile (String file)
    {
	System.out.println("Trying to read from file "+file);

	Image output = null;

	try
	{
	    output = ImageIO.read( getClass().getClassLoader().getResourceAsStream( "mud/"+fileName.replace( '\\', '/' )) );
	}
	catch (java.io.FileNotFoundException e)
	{
	    System.out.println("File not found" + file);
	    System.exit(-1);
	}
	catch (java.io.IOException e)
	{
	    System.out.println("Error in image file "+file);
	    System.exit(-1);
	}
	catch (SecurityException e)
	{
	    System.out.println("Security will not allow operation on file "+file);
	    System.exit(-1);
	}
	catch (IllegalArgumentException e)
	{
	    System.out.println("fromFile in Sprite Loader passed null argument");
	    System.exit(-1);
	}
	return output;
    }




}
