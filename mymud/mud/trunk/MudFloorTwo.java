package mud.trunk;

import mud.supporting.IntPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;


public class MudFloorTwo implements Serializable, MudFloorInterface
{
    Stage stage;
    WaterInterface water;
    protected DataMap mudMap;

    /*
      Creates mud map for mud file
    */
    public MudFloorTwo(String filename, Stage stg, Color col)
    {
	mudMap = new DataMap();
	mudMap.FillDataMap(filename, stg, col);
	stage = stg;
    }

    public void setWater(WaterInterface w)
    {
	water = w;
    }

    /*
      Returns the mudFloor
    */
    public Image getImage()
    {
	Image mfloor = mudMap.returnImage();
	return mfloor;
    }

    /*
      Returns true or false for mud at postion
    */
    public boolean isMud(IntPos x)
    {
	if(mudMap.valueAt(x) == 1)
	{
	    return false;
	}
	else
	{
	    return true;
	}

    }

    /*
      Adds a pixel of mud too the floor at the given intPos
    */
    public void addMud(IntPos p)
    {
	try
	{
	    if (isMud(p)) 
	    {
		return;
	    }
	    else
	    {
		stage.addMud(p);
		(mudMap.rdMap).setPixel(p.getX(), p.getY(), mudMap.black);
	    }
	}
	catch (Exception e)
	{
	}
    }

    /**
       removes mud at the given
       @param p position
    */
    public void removeMud(IntPos p)
    {
	try
	{
	    if (!isMud(p)) 
	    {
		return;
	    }
	    else
	    {
		stage.removeMud(p);
		(mudMap.rdMap).setPixel(p.getX(), p.getY(), mudMap.white);
	    }
	    ((WaterThree)water).removeMud(p);
	}
	catch (Exception e)
	{
	}
    }


    /*
      updates the mud floor for this turn. At the
      moment this does nothing
    */
    public void doPhysics ()
    {
    }
}
