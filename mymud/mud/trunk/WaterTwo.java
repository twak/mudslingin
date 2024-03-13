package mud.trunk;

import mud.supporting.IntPos;
import mud.supporting.RealPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;


public class WaterTwo implements Serializable, WaterInterface
{

    /*
      Creates water map for given file
      and fills in "contact map"
    */
    private Stage gameStage;
    private DataMap mudFloorCopy = new DataMap();
    DataMap waterMap = new DataMap();
    private MudFloorInterface floor;
    public LinkedList airContact;
    private LinkedList removedPixels;
    private LinkedList addedPixels;
    private Hashtable movedPixels;
    private aContact moPix;
    protected static final int[] black = {0};//new int[1];
    protected static final int[] white = {1};//new int[1];
    public IntPos cpos = new IntPos();
    public IntPos dpos = new IntPos();
    static int inc = -1;
    public final static int RIGHT = 1, LEFT = 2, DOWN = 4, NONE = -1;
    
    protected WaterColumn[] waterfall = new WaterColumn[802];
    WaterColumn c1,c2,c3;
    protected int averageCount, averageTop, lastTop = -1, blobBuffer = 0;
    int promin, promax, prosize = 0;
    protected int blobMin = -1, blobMax = -1, blobSize = -1, blobPressure = 0;
 
    public WaterTwo(String filename, Stage stg, MudFloorInterface cfloor)
    {
	floor = cfloor;
	gameStage = stg;
	mudFloorCopy = cfloor.mudMap;
	waterMap.setStats(100, 100, -1, -1);
	waterMap.FillDataMap(filename, stg, new Color (100,100,255,255));
	popContactMap();
	WaterColumn wf;
	for (int i = 0; i < 800; i++) 
	{
	    wf =  new WaterColumn();
	     waterfall[i] = wf.makeWaterColumn(stg, waterMap, i, this);
	}
    }

    /*
      Removes water where there is mud
    */
    public void popContactMap()
    {
	int x, y;
	cpos = new IntPos();
	dpos = new IntPos();

	for(x=0; x<800; x++)
	    for(y=0; y<600; y++)
	    {
		cpos.set(x, y);
		if ((isWater(cpos)) && floor.isMud(cpos))
		{
		    setWater(x,y, waterMap.white[0]);
		}
	    }
    }


    /*
      Returns true or false for water at a given position
    */
    public void setWater(int x,int y, int borw) 
    {
	if(borw == 1)
	{
	    (waterMap.rdMap).setPixel(x,y, waterMap.white);
	}
	else if(borw == 0)
	{
	    (waterMap.rdMap).setPixel(x,y, waterMap.black);
	}
    }

    public boolean isWater(IntPos x)
    {
	if(waterMap.valueAt(x) == 1)
	{
	    return false;
	}else
	{
	    return true;
	}
    }

    /*
      Returns the water
    */

    public Image getImage()
    {
	Image mwater = waterMap.returnImage();
	return mwater;
    }

    /*
      Determines whether or not a pixel is able to move

      Only looks at mud at the moment, should do water too
    */

    public int canMove(IntPos pixelPos)
    {
	IntPos out = null;
	pixelPos.incY(1);
	if (!(floor.isMud(pixelPos)))
	{
	    return DOWN;
	}
	else
	{
	    pixelPos.incX(1);
	    if (!(floor.isMud(pixelPos)))
	    {
		return LEFT;	
	    }
	    else
	    {
		pixelPos.incX(-2);
		if (!(floor.isMud(pixelPos)))
		{
		    return RIGHT;		
		}
		else
		{
		    return NONE;
		}
	    }
	}
    }

    public boolean isAir(IntPos pixelPos)
    {
        if (!floor.isMud(pixelPos) && !isWater(pixelPos))
            {
                return true;
            }else
            {
                return false;
            }
    }

    public IntPos getNextPixel(IntPos currentPix)
    {
	return null;
    }

    public boolean hasPixelMoved(int px, int py, LinkedList moved_list)
    {
	return false;
    }

    public IntPos getTopPixel(int heightY, int heightX)
    {
	return null;
    }


    public int getMoveType(IntPos movePixel)
    {
	return 0;
    }

    public void updateContact()
    {	
    }

    public void doMove(int moveType, IntPos mPix)
    {
    }

	public void removeMud(IntPos p)
	{
	    WaterColumn wc = waterfall[p.getX()];
	    if (wc!= null)
	    {
		waterfall[p.getX()].removeMud();
	    }
	}

    /**
       removes a column
     */
    public void killCol(int i)
    {
	c1 = waterfall[i];
	c2 = waterfall[i-1];
	c3 = waterfall[i+1];
	if (c1.min != -1)
	{
	    if (i > 0)
	    {
		if (c2 != null)
		{
		    c2.min = c1.min;
		    c2.max = c1.max;
		    c2.size = c1.size;
		    c2.buffer = c1.buffer;
		}
	    }
	    if (i < 799)
	    {
		if (waterfall[i+1] != null)
		{
		    c3.min = c1.min;
		    c3.max = c1.max;
		    c3.size = c1.size;
		    c3.buffer = c1.buffer;
		}
	    }
	}
	waterfall[i] = null;
    }

    public void doPhysics()
    {
	int bottom,top,next;
	int p,q,x,y,current, last, j, count;
	IntPos tmp;
	/*
	  Little perverse, but max is a small number as it is height
	  from top of screen
	*/
	int max = 1000,maxcol = -1, num = 0;

	//averageCount = 0;
	averageTop = 0;


	for (int i = 0; i < 799; i++)
	{
	    if (waterfall[i]!=null)
	    {
		procCols(waterfall[i],waterfall[i+1],i+1);
	    }
	    else
	    {

	    }
	}

	for (int i = 800; i >1; i--)
	{
	    if (waterfall[i]!=null)
	    {
		procCols(waterfall[i],waterfall[i-1],i-1);
	    }
	}
    }

    /* When this is called, current is non null, but last column may be null, so 
	   we pass its column too so we can add it to the array if we create it 

	   the min and max referer to the top srface of the water

	   promin & pro max accumulate this turns min and max in this blob
	   blobMin and blobMax are picked up when we enter a blob (last turns promin&max)
	   
*/
    public void procCols(WaterColumn current, WaterColumn last, int lC)
    {
	IntPos tmp;
	int bottom,top,next;
	int p,q,x,y;

	bottom = current.bottom;
	top = current.top;
	prosize++;

	/**
	   u need to delete current.min and max if first in series
	   (if min and max != 1, then set some globals to last min
	   and last max 
	 */
	if (current.min != -1)
	{
	    blobMin = current.min;
	    blobMax = current.max;
	    blobSize = current.size;
	    blobBuffer = current.buffer;
	    blobPressure = blobMin-blobMax;
	    //System.err.println("  "+blobMin+" "+blobMax+"   "+blobSize);
	    current.min = -1;
	    current.max = 800;
	    current.size = -1;
	    current.buffer = -1;
	}
	if (top < promax) promax = top;
	if (top > promin) promin = top;
	//System.err.println(promax+"   "+promin);

	//System.err.println(buffer);
	if(current.airBottom)
	{
	    current.bottom(true);
	    current.top(false);
	}
	else if (last == null)
	{
	    /* Column to side has no water - 
	       ASSUMES NO WATER IN ADJOINING COLUMN*/

	    y = current.bottom;
	    x = lC;
	    tmp = new IntPos(x,y);
	    if (floor.isMud(tmp))
	    {
		/* Add above our bottom*/
		while (floor.isMud(tmp))
		{
		    y--;
		    tmp.set(x,y);
		    if (y <= top) break;
		}
	    }
	    else
	    {
		/* Else we add at our bottom row, in last column */
	    }

	    if (y > top)
	    {
		current.top(false);
		current = new WaterColumn(gameStage, waterMap, lC, this,y);
		waterfall[lC] = current;//new WaterColumn(gameStage, waterMap, lC, this,y);  
	    }
	    /* current is the last column in the current water
		   lob */
	    //System.err.println("Dumping in col "+current.column);
	    current.min = promin;
	    current.max = promax;
	    current.size = prosize;
	    current.buffer = blobBuffer;
	    blobBuffer = -1;
	    promin = -1;
	    promax = 800;
	    prosize = 0;
	}
//(bottom - top > 20) && 
	else if (top < blobMax+3 && blobBuffer < blobSize/5 && blobPressure > 5)
	{
	    //System.err.println("  "+blobMax+"  "+top+"==<");
	    current.top(false);
	    blobBuffer++;
	}
	// || (bottom-top < 10)
	else if ((top > blobMin-4) && blobBuffer > 0 && current.airTop)
	{
	    current.top(true);
	    blobBuffer--;
	}
	/* And there is water to the side*/
	else 
	{
	    while(last.airTop && last.top > current.top && current.depth >= 0)
	    {
		last.top(true);
		current.top(false);
	    }
	    return;
		      
	    /* Colum to the right */
	}
    }


    /*
      Adds a pixel of water at the given position
    */
    public void addWater(IntPos p)
    {
	(waterMap.rdMap).setPixel(p.getX(), p.getY(), waterMap.black);
	gameStage.addWater(p);
    }



    /*
      Removes a pixel of water at the given position
    */
    public void removeWater(IntPos p)
    {
	(waterMap.rdMap).setPixel(p.getX(), p.getY(), waterMap.white);
	gameStage.removeWater(p);
    }

    /*
      Returns water depth at position x
    */

    public int waterDepth(IntPos x)
    {
	int wdepth = 0;
	IntPos wd = null;
	boolean cont = true;

	while(cont)
	{
	    if (waterMap.valueAt(x) == 0)
	    {
		wd.set(x.getX(), (x.getY()) - 1);
		wdepth++;
	    }else
	    {
		cont = false;
	    }
	}


	return wdepth;
    }
}
