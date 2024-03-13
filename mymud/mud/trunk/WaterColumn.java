package mud.trunk;

import mud.supporting.IntPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;


public class WaterColumn
{
    int bottom = -1;
    int top = -1;
    boolean airTop = true;
    boolean airBottom = true;
    boolean changed = true;
    boolean isWater = false;
    int column;
    static IntPos pixelPos = new IntPos(-1,-1);
    WaterTwo water;
    DataMap waterMap;
    static int gameX;
    static int gameY;
	int depth;
    int min = 800, max = -1, size = -1, buffer = -1;

    public WaterColumn()
    {
    }

    public WaterColumn makeWaterColumn(Stage gameStage, DataMap wM, int x, WaterTwo w)
    {
	    waterMap = wM;
	gameX = gameStage.getGameValue().mapSizeX;
	gameY = gameStage.getGameValue().mapSizeY;
	column = x;
	water = w;

	IntPos position = new IntPos();
	int y = 0;

	while (y < gameY)
	{
	    position.set(x,y);
	    /* 0 is water exists */
	    if (waterMap.valueAt(position) == 0)
	    {
		if (top == -1)
		{
		    top = y;
	            isWater = true;
		}

		bottom = y;
	    }
	    y++;
	}
	
	airAbove();
	airBelow();

	if (!isWater) return null;
	return this;
    }

    /* This makes a new column with size 1 */
    public WaterColumn(Stage gameStage, DataMap wM, int x, WaterTwo w, int h)
    {
	    waterMap = wM;
	gameX = gameStage.getGameValue().mapSizeX;
	gameY = gameStage.getGameValue().mapSizeY;
	column = x;
	water = w;

	top = h;
	bottom = top;
	IntPos botPos = new IntPos(column,bottom);
	water.addWater(botPos);
	
	airAbove();
	airBelow();
    }

    public void offScreen()
    {
	top = -1;
	bottom = -1;
    }

    private void airAbove()
    {
	pixelPos.set(column,top-1);

	if (water.isAir(pixelPos))
	{
	    airTop = true;
	}
	else
	{
	    airTop = false;
	}
    }

    private void airBelow()
    {
        pixelPos.set(column,bottom+1);

	if (water.isAir(pixelPos))
	{
	    airBottom = true;
	}
	else
	{
	    airBottom = false;
	}
    }


    public IntPos getBottom()
    {
	if (bottom == -1) return null;
	return new IntPos(column,bottom);
    }

    public IntPos getTop()
    {
	if (top == -1) return null;
	return new IntPos(column,top);
    }

    /* */
    public void removeMud()
    {
	airAbove();
	airBelow();
    }

    public int relativeDepth(DataMap waterMap, IntPos position)
    {
	int depth = -1;
	int x = position.getX();
	int y = position.getY();

	while (waterMap.valueAt(position) == 1)
	{
	    depth ++;
	    
	    y--;
	    
	    position.set(x,y);
	}

	return depth;
    }

    void top(boolean add)
    {
	if (add)
	{
	    top--;
	    if (top < 600)
	    {
		pixelPos.set(column,top);
		water.addWater(pixelPos);
	    }
	    else
	    {
		/* could unroll to nicer positions */
		offScreen();
	    }
	}
	else
	{	
	    pixelPos.set(column,top);
	    top ++;
	    if (top < 600)
	    {
		water.removeWater(pixelPos);
		if (bottom < top) water.killCol(column);
	    }
	    else
	    {
		/* could unroll to nicer positions */
		offScreen();
	    }
	    	     
	}
	depth = bottom - top;
	airAbove();
    }

	
    void bottom(boolean add)
    {
	if (add)
	{  
	    bottom++;
	    if (bottom < 600)
	    {
	        pixelPos.set(column,bottom);
		water.addWater(pixelPos);
	    }
	    
	}
	else
	{
	    pixelPos.set(column,bottom);
	    bottom --;
	    if (bottom < 600)
	    {
		water.removeWater(pixelPos);
	    }
	    if (bottom <= top) water.killCol(column);
	}
	depth = bottom - top;
	airBelow();
    }
	


    public void doPhysics()
    {
       
    }
}
