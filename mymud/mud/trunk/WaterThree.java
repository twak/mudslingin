package mud.trunk;

import mud.supporting.IntPos;
import mud.supporting.RealPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;

/**
   This is the third incarnation of the water system
   800 columns of doubly linked WaterColumns Three.
   These are levelleved by the proc cols method later

   All the while here we remain in java's graphics coordinate
   system so the entire shebang works upside down
 */
public class WaterThree implements Serializable, WaterInterface
{

    /*
      Creates water map for given file
      and fills in "contact map"
    */
    private Stage gameStage;
    private DataMap mudFloorCopy;
    DataMap waterMap = new DataMap();
    protected MudFloorInterface floor;
    public LinkedList airContact;
    private LinkedList removedPixels;
    private LinkedList addedPixels;
    private Hashtable movedPixels;
    private aContact moPix;
    protected static final int[] black = {0};
    protected static final int[] white = {1};
    public static IntPos cpos = new IntPos();
    public static IntPos dpos = new IntPos();
    static int inc = -1;
    public final static int RIGHT = 1, LEFT = 2, DOWN = 4, NONE = -1;
    
    protected WaterColumnThree[] waterfall = new WaterColumnThree[802];
    private static WaterColumnThree c1,c2,c3;
    protected int averageCount, averageTop, lastTop = -1, blobBuffer = 0;
    int promin, promax, prosize = 0;
    protected int blobMin = -1, blobMax = -1, blobSize = -1, blobPressure = 0;

    /**
       General constructor
     */
    public WaterThree(String filename, Stage stg, MudFloorInterface cfloor, Color col)
    {
	floor = cfloor;
	gameStage = stg;
	//mudFloorCopy = cfloor.mudMap;
	//waterMap.setStats(100, 100, -1, -1);
	waterMap.FillDataMap(filename, stg, col);
	popContactMap();
	WaterColumnThree wf;
	for (int i = 0; i < 800; i++) 
	{
	    waterfall[i] = new WaterColumnThree(stg, waterMap, i, this);
	    if (!waterfall[i].isWater) waterfall[i] = null;
	}

	/* This links the blobs of water together */
	if (waterfall[0]!=null)waterfall[0].findContacts(null,-1,waterfall[1],1);
	for (int i = 1; i < 799; i++)
	{
	    if (waterfall[i]!=null)
	    {
		waterfall[i].findContacts(waterfall[i-1],i-1,waterfall[i+1],i+1);
	    }
	}
	if (waterfall[799]!=null)waterfall[799].findContacts(waterfall[798],798,null,800);
    }

    /**
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


    /**
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

    /**
       This lot are partly historical and basic accessor methods
     */
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
	WaterColumnThree wc = waterfall[p.getX()];
	if (wc!= null)
	{
	    waterfall[p.getX()].removeMud();
	}
    }

    /**
       UNUSED
     */
  public void oldProcCols(WaterColumnThree current, WaterColumnThree last, int lC)
    {
	IntPos tmp;
	int bottom,top,next;
	int p,q,x,y;

	bottom = current.bottom;
	top = current.top;

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
		current = new WaterColumnThree(gameStage, waterMap, lC, this,y);
		waterfall[lC] = current;//new WaterColumnThree(gameStage, waterMap, lC, this,y);  
	    }
	    /* current is the last column in the current water
		   lob */
	    current.min = promin;
	    current.max = promax;
	    current.size = prosize;
	    current.buffer = blobBuffer;
	    blobBuffer = -1;
	    promin = -1;
	    promax = 800;
	    prosize = 0;
	}
	else if (top < blobMax+3 && blobBuffer < blobSize/5 && blobPressure > 5)
	{
	    current.top(false);
	    blobBuffer++;
	}
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

    /**
       UNUSED, for reference only
     */
    public void doOldPhysics()
    {
	int bottom,top,next;
	int p,q,x,y,current, last, j, count;
	IntPos tmp;
	/*
	  Little perverse, but max is a small number as it is height
	  from top of screen
	*/
	int max = 1000,maxcol = -1, num = 0;

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

    public void doPhysics()
    {
	int bottom,top,next;
	int p = 0,q = -10,x,y,current, last, j, count;
	IntPos tmp;
	/*
	  Little perverse, but max is a small number as it is height
	  from top of screen
	*/
	int max = 1000,maxcol = -1, num = 0;

	averageTop = 0;

	for (int i = 795; i > 5; i--)
	{
	    
	    c1 = waterfall[i];
	    while (c1 != null)
	    {
	        //System.err.println(" >>"+c1.column +"  "+c1.top+"  "+c1.bottom);
		//if (c1.leftWC == null) System.err.println("eocl:"+c1.column);
		procCols(c1,c1.leftWC,i-1);
		if (c1 != null) 
		{
		    c1 = c1.above;
		}
	    }
	}

	for (int i = 5; i < 795; i++)
	{
	    
	    c1 = waterfall[i];
	    while (c1 != null)
	    {
	        //System.err.println(" >>"+c1.column +"  "+c1.top+"  "+c1.bottom);
		//if (c1.rightWC == null) System.err.println("eocr:"+c1.column);
		procCols(c1,c1.rightWC,i+1);
		if (c1 != null) 
		{
		    c1 = c1.above;
		}
	    }
	}
    }

    /**
    When this is called, current is non null, but last column may be null, so 
    we pass its column too so we can add it to the array if we create it 
    
    the min and max referer to the top srface of the water
    
    promin & pro max accumulate this turns min and max in this blob
    blobMin and blobMax are picked up when we enter a blob (last turns promin&max)
    
    **last is actually ahead of current**
    
    */
    public void procCols(WaterColumnThree current, WaterColumnThree last, int lC)
    {
	int tmp;
	int bottom,top,next;
	int p,q,x,y;

	bottom = current.bottom;
	top = current.top;
	prosize++;

	if(current.airBottom)
	{
	    /* fall down */
	    current.bottom(true);
	    if (!current.top(false)) return;
	}
	else
	{
	    /**
	       First check if we create a new column
	     */
	    if (true)
	    {
		if (last != null) 
		{
		    if (last.airTop) p = last.bottom+2; else p= -1;
		} else p = -1;
		/* tmp stores the top position where we will
		   add air */
		tmp = Math.max(current.top, p);
		x = lC;
		y = bottom;
		cpos.set(x,y);
		while (!isAir(cpos) && y >= tmp)
		{
		    y--;
		    cpos.set(x,y);
		}
		if (y < tmp)
		{
		    /*full column, do nothing */
		}
		else if ((current.bottom-current.top) > 0)
		{
		    if (last == null) tmp = -10; else tmp = last.bottom+1;
		    /*if (y == 599) y--;*/
		    cpos.set(lC,y);

		    if (tmp == y)
		    {
			if (last.bottom+1 == y)
			{
			    last.bottom(true);
			}
		    }
		    /* this captures the event that we are adding water without
		       a gap to the top of another */
		    else if (isWater(cpos))
		    {
			/* for now do nothing */
		    }
		    else
		    {
			last = new WaterColumnThree(gameStage, waterMap, lC, this, y); 
			last.min = current.min;
			last.max = current.max;
			last.buffer = current.buffer;
		        if (!current.top(false))
			{
			    if (waterfall[lC]  != null)waterfall[lC  ].findContacts(waterfall[lC-1], lC-1, waterfall[lC+1], lC+1); 
			    if (waterfall[lC-1]!= null)waterfall[lC-1].findContacts(waterfall[lC-2], lC-2, waterfall[lC  ], lC  );
			    if (waterfall[lC+1]!= null)waterfall[lC+1].findContacts(waterfall[lC]  , lC  , waterfall[lC+2], lC+2);
			    return;
			}
			else
			{
			    if (lC > 1 && lC < 799)
			    {
				if (waterfall[lC]  != null)waterfall[lC  ].findContacts(waterfall[lC-1], lC-1, waterfall[lC+1], lC+1); 
				if (waterfall[lC-1]!= null)waterfall[lC-1].findContacts(waterfall[lC-2], lC-2, waterfall[lC  ], lC  );
				if (waterfall[lC+1]!= null)waterfall[lC+1].findContacts(waterfall[lC  ], lC  , waterfall[lC+2], lC+2);
			    }
			}
		    }
		}
	    }


	    /* This levels between adjacent columns */
	    if (true)
	    {
		if (last != null)
		{
		    p = 0;  
		    /* P here contains how quickly it levels out (up to 45 degrees), then its slow...  */
		    while(current.airTop && last.top < current.top && (last.bottom-last.top) >= 0 && p < 20)
		    {
			current.top(true);
			if (!last.top(false))return;
			/* this might kill the column */
			/* this shouldn't kill the column */
			p++;
		    }
		    p=0;
		    while(last.airTop && last.top > current.top && (current.bottom-current.top) >= 0 && p < 20)
		    {
			last.top(true);
			if(!current.top(false))return;
			/* this might kill the column */
			/* this shouldn't kill the column */
			p++;
		    }

		    /* This are fast levelling statements */
		    if (top < current.max+2 && current.buffer < 40 && current.min-current.max > 3 && current.bottom - current.top > 1)
		    {
			//  System.err.println("  "+current.max+"  "+top+"==<");
  			current.top(false);
  		        current.buffer++;
		    }
		    if ((top >= current.min-6) && current.buffer > 0 && current.airTop)
		    {
			//  System.err.println("ditchin"+current.column);
  			current.top(true);
  			current.buffer--;
		    }

		    /* Move gross correction values to next wc3 */
		    if((last.leftWC == current || last.rightWC == current) && (lC < 794 || lC > 6) && !last.airBottom)//  && last.bottom - last.top > 2)// && !current.airBottom)

		    {
			/* calculate minimum and maximum */
			last.promax = Math.min(Math.min(current.promax, top),last.promax);
			last.promin = Math.max(Math.max(current.promin, top),last.promin);
			current.promin = -1;
			current.promax = 800;

			/* move the values to the next column */
			last.min = current.min;
			last.max = current.max;
			current.min = -1;
			current.max = 800;
			last.buffer = current.buffer;//+last.buffer;
			current.buffer = 0; 
		    }
		    else
		    {
			/* End of water block, dump values into permenant variables */
			if (current.promin > -1) current.min = current.promin; else current.min = top ;
			if (current.max < 800) current.max = current.promax; else current.max = top;
			/* delete those values we dont want comming back to haunt us next sweep */
			current.promin = -1;
			current.promax = 800;

			/* End of water block, dump values into permenant variables */
// 		    System.err.println("Master Node A");
//   		    System.err.println(current.column+")min "+current.promin+":"+top+"    max:"+current.promax+"buffer "+current.buffer);


		    }
		}
		else
		{
		    /* End of water block, dump values into permenant variables */
// 		    System.err.println("Master Node B");
//   		    System.err.println(current.column+")min "+current.promin+":"+top+"    max:"+current.promax+"buffer "+current.buffer);
		    if (current.promin > -1) current.min = current.promin; else current.min = top ;
		    if (current.max < 800) current.max = current.promax; else current.max = top;
		    /* delete those values we dont want comming back to haunt us next sweep */
		    current.promin = -1;
		    current.promax = 800;
		}
	    }
	    }
    }


    /*
      Adds a pixel of water at the given position. This
      doesn't adjust the data structures, use addWater(int,int,int)
      for that
    */
    public void addWater(IntPos p)
    {
	(waterMap.rdMap).setPixel(p.getX(), p.getY(), waterMap.black);
	gameStage.addWater(p);
    }


    /*
      Removes a pixel of water at the given position, internal
    */
    public void removeWater(IntPos p)
    {
	(waterMap.rdMap).setPixel(p.getX(), p.getY(), waterMap.white);
	gameStage.removeWater(p);
    }


    /**
       This removes water on the screen
       @param column this Column
       @param height this height(from top o screen);
       @param ammount the amount of water to add from above 'height'
     */
    public void removeWater(int column, int height, int ammount)
    {
	int t1, t2, t3;
	/* if out of bounds, return */
	if (column < 0 || column > 799) return;
	int top = height - ammount;
	c1 = waterfall[column];
	/* no water in this column */
	if (c1 == null) return;
	if (ammount == 0) return;
	while (true)
	{
	    if (c1.top <= height)
	    {
		if (c1.bottom >= top)
		{
		    t1 = Math.min(height, c1.bottom);
		    t2 = t1 - c1.top;
		    /* remainder above to add back */
		    t3 = top-c1.top;

		    /* remove water from this column */
		    for (int i = 0; i <= t2; i++)
		    {
			c1.top(false);
		    }
		    if (t3 > 0)
		    {
			c2 = new WaterColumnThree(gameStage, waterMap, column, this, top+1); 
			for (int i = 0; i <= t3; i++)
			{
			    c2.top(true);
			}
		    }
		}
		else
		{
		    /* break out of the while */
		    break;
		}
	    }

	    if (c1.above != null)
	    {
		c1 = c1.above;
	    }
	    else
	    {
		break;
	    }
	}

    }

/*
  This is code for the add water routine that removes water from the
  top of the pile. This may be useful if we need a 'sun' to dry up
  all the rain in the game...

		    t1 = Math.min(height, c1.bottom);
		    t2 = Math.max(top, c1.top);
		    for (int i = 0; i <= t1-t2; i++)
		    {
			c1.top(false);
		    }
 */


    /**
       This adds water to the the screen, and links in the water
       @param column this Column
       @param height this height(from top o screen);
       @param ammount the amount of water to add from above 'height'
     */
    public void addWater(int column, int height, int ammount)
    {
	/* if out of bounds, return */
	if (column < 5 || column > 795) return;
	int top = height - ammount;
	c1 = waterfall[column];

	/* while not there yet, keep going */
	while (height > top)
	{
	    cpos.set(column,height);
	    while (floor.isMud(cpos) && height > top){height--; cpos.set(column,height);}
	    if (height <= top) return;

	    if (c1 != null)
	    {
		while (c1.top > height)
		{
		    c1 = c1.above;
		    if (c1 == null) break;
		}
		if (c1 != null)	if (c1.top > height) c1 = null;
	    }

	    /* here c1. top < h or c1 = null */
	    if (c1 != null)
	    {
		if (c1.bottom >= height)
		{
		    // do nowt, c1 = c1;
		}
		else
		{
		    /* Bottom < h */
		    c1 = new WaterColumnThree(gameStage, waterMap, column, this, height); 
		}
	    }
	    else
	    {
		/* Bottom > h */
		c1 = new WaterColumnThree(gameStage, waterMap, column, this, height); 
	    }
	    while (c1.top > top && c1.airTop) c1.top(true);
	    height = c1.top-1;
	    /* set height to where there is not mud */
	    cpos.set(column,height);
	    while (floor.isMud(cpos) && height >= top){height--; cpos.set(column,height);}
	    if (height < top) 
	    {
		return;
	    }

	    /* find new wct, if any at height */
	    if (c1 != null)
	    {
		c1 = waterfall[column];
		while (c1.top > height)
		{
		    c1 = c1.above;
		    if (c1 == null) break;
		}
		if (c1 != null) if (c1.top > height) c1 = null;
	    }
	}
	/* if the column is null, we dont need to do anything */
    }

    /*
      Returns water depth at the position. This
      also counts mud as water. It adds a air whole
      of size at least 10 that the blokes need to
      breath in
      @param in the position to start at
    */
    public int waterDepth(IntPos in)
    {
	int air = 0;
	int x = in.getX();
	int y = in.getY();
	if (x < 0 || x > 799) return 0;
	c1 = waterfall[x];

	/* find first bit of water */
	cpos.set(x,y);
	while (air < 10)
	{
	    while ((floor.isMud(cpos) || isWater(cpos)) && y > 0){y--; cpos.set(x,y);}
	    air++;
	    y--;
	}
	
	return in.getY() - y - air;
    }

    public int oldWaterDepth(IntPos in)
    {
	int x = in.getX();
	int y = in.getY();
	if (x < 0 || x > 799) return 0;
	c1 = waterfall[x];

	/* find first bit of water */
	cpos.set(x,y);
	while (floor.isMud(cpos) && y > 0){y--; cpos.set(x,y);}

	if (c1!= null)
	{
	    while (c1.bottom >= y)
	    {
		if (c1.top <= y)
		{
		    return y-c1.top;
		}
		if (c1.above != null)
		{
		    c1 = c1.above;
		}
		else
		{
		    return 0;
		}
	    }
	    return 0;
	}
	else
	{
	    /* no water here */
	    return 0;
	}
    }
}



