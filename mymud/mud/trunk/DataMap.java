package mud.trunk;

import mud.supporting.IntPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;

public class DataMap implements Serializable
{

    transient Image dMap;
    transient WritableRaster rdMap;
    protected static final int[] black = {0};//new int[1];
    protected static final int[] white = {1};//new int[1];
    private IndexColorModel icbm;
    /* Defines the colour & transparency of the maps */
    private byte red,green,blue,alpha;

    /**
       Streams the object to the disk when saving
    */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
	System.err.println("Writing thing to disk"+this);
	if (dMap == null) System.err.println("Dmap is null"+this);
	BufferedImage src = ((BufferedImage)dMap);
	boolean[][] data = new boolean[ src.getWidth()][ src.getHeight()];
	double[] gpix = new double[1];

	for (int i = 0; i < src.getWidth(); i++)
	{
	    for(int j = 0; j < src.getHeight(); j++)
	    {
		rdMap.getPixel(i,j, gpix);
		if (gpix[0]==1)
		{
		    data[i][j] = true;
		}
		else
		{
		    data[i][j] = false;
		}
		
	    }
	}

	out.writeObject( new Byte(red));
	out.writeObject( new Byte(green));
	out.writeObject( new Byte(blue));
	out.writeObject( new Byte(alpha));
	out.writeObject(data);
    }

    /**
       Streams the object from the disk when loading
    */
    private void readObject(java.io.ObjectInputStream in)     
                                    throws IOException           , 
	                                   ClassNotFoundException
    {
	red   = ((Byte)in.readObject()).byteValue();//90;
	green = ((Byte)in.readObject()).byteValue();//60;
	blue  = ((Byte)in.readObject()).byteValue();//0;
	alpha = ((Byte)in.readObject()).byteValue();//-1;

	icbm = ICBM();

	icbm = ICBM();

	dMap = 	new BufferedImage(800,600, BufferedImage.TYPE_BYTE_INDEXED ,ICBM());
	boolean[][] data = new boolean[((BufferedImage)dMap).getWidth()][ ((BufferedImage)dMap).getHeight()];
	rdMap = ((BufferedImage)dMap).getRaster();

	red = 90;
	green = 60;
	blue = 0;
	alpha = -1;

	black[0] = 0;
	white[0] = 1;

	data = (boolean[][])in.readObject();

        for (int i = 0; i < ((BufferedImage)dMap).getWidth(); i++)
  	{
 	    for(int j = 0; j < ((BufferedImage)dMap).getHeight(); j++)
  	    {
		if (data[i][j])
		{
		    rdMap.setPixel(i, j, white);
		}
		else
		{
		    rdMap.setPixel(i, j, black);
		}
  	    }
  	}
    }



    /*
      Creates an empty  datamap
    */
    public DataMap()
    {
	/* Set default colours */
	red = 90;
	green = 60;
	blue = 0;
	alpha = -1;
    }

    public void setStats(int r, int g, int b, int a)
    {
	red   = (byte)r;
	green = (byte)g;
	blue  = (byte)b;
	alpha = (byte)a;
    }

    public byte colourInt(int c)
    {
	if (c > 127) c = c - 256;
	return (byte) c;
    }

    /*
      fills the datamap
    */
    public void FillDataMap(String filename, Stage stg, Color col)
    {
	red = colourInt(col.getRed());
	green = colourInt(col.getGreen());
	blue = colourInt(col.getBlue());
	alpha = colourInt(col.getAlpha());

	//alpha = -1;
	icbm = ICBM();
	dMap = fromFile("data/" + filename);

	if (stg.getGameValue().mapSizeX != ((BufferedImage)dMap).getWidth())
	{
	    System.err.println("File width too small");
	    System.exit(-1);
	}

	if (stg.getGameValue().mapSizeY != ((BufferedImage)dMap).getHeight())
	{
	    System.err.println("File height too small");
	    System.exit(-1);
	}

	dMap = oneBitMap((BufferedImage)dMap);
	rdMap = ((BufferedImage)dMap).getRaster();
    }

    /*
      Returns a good color
      @param returns a colour pallete suiatable for display
    */
    private IndexColorModel ICBM()
    {
	/*
	  The Byte thing is contorted sequence of numbers is
	  0,1...126,127,-128,-127...-2.-1

	byte r  [] = new byte[2];r  [black[0]]=red   ;r  [white[0]]= -1;
	byte g  [] = new byte[2];g  [black[0]]=green ;g  [white[0]]= -1;
	byte b  [] = new byte[2];b  [black[0]]=blue  ;b  [white[0]]= -1;
	byte a  [] = new byte[2];a  [black[0]]=alpha ;a  [white[0]]=  0;
	*/
	byte r  [] = new byte[2];r  [black[0]]= red   ;r  [white[0]]= -1;
	byte g  [] = new byte[2];g  [black[0]]= green    ;g  [white[0]]= -1;
	byte b  [] = new byte[2];b  [black[0]]= blue   ;b  [white[0]]= -1;
	byte a  [] = new byte[2];a  [black[0]]= alpha   ;a  [white[0]]=  0;

	IndexColorModel icm = new IndexColorModel(2,2,r,g,b,a);
	return icm;
    }

    /*
      Returns bufferedimage one bit bitmap
    */
    public BufferedImage oneBitMap(BufferedImage src)
    {
	BufferedImage testim = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_INDEXED ,ICBM());
	dMap = testim;
	WritableRaster frast = ((BufferedImage)testim).getRaster();

	int i = 0;
	int j = 0;
	black[0] = 0;
	white[0] = 1;

	for (i = 0; i < src.getWidth(); i++)
	{
	    for(j = 0; j < src.getHeight(); j++)
	    {
		//if (i == 316) System.err.println(j+" * "+(src.getRGB(i, j) == -1));
		if (src.getRGB(i, j) == -1)
		{
		    frast.setPixel(i, j, white);
		}else
		{
		    frast.setPixel(i, j, black);
		}

	    }
	}
	return testim;
    }

    /*
      Toggles a bit int the data map
      on or off at given position
    */
    public void toggleBit(IntPos x)
    {
	try
	{
	double[] gpix = new double[1];
	rdMap.getPixel(x.getX(), x.getY(), gpix);

	if(gpix[0] == 1)
	{
	    rdMap.setPixel(x.getX(), x.getY(), black);
	}else if (gpix[0] == 0)
	{
	    rdMap.setPixel(x.getX(), x.getY(), white);
	}
	}
	catch (Exception e)
	{
	System.err.println("bad");
	}
    }

    /*
      Returns the value of the data map at given
      position. 1 if true, 0 otherwise
    */
    public int valueAt(IntPos x)
    {
	if (rdMap == null) return 0;
	if (x.getX() > -1 && x.getX() <800 && x.getY() >-1 &&  x.getY() < 600 )
	{
	    if (rdMap.getSample(x.getX(),x.getY(),0)==0 )
	    {
		return 0;
	    }
	    else
	    {
		return 1;
	    }
	}
	else
	{
	    return 0;
	}
    }

    /*
      Returns an img of datamap
    */
    public Image returnImage()
    {
	return dMap;
    }

    /*
      Reads in an image from a file
    */
    private Image fromFile (String file)
    {
	System.out.println("Trying to read from file "+file);

	Image output = null;

	try
	{
	    output = ImageIO.read( getClass().getClassLoader().getResourceAsStream( "mud/"+file.replace( '\\', '/' )) );
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

    String filename = null;
    Stage stg;

}
