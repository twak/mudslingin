package mud.trunk;

import mud.supporting.IntPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;


public interface MudFloorInterface
{

        DataMap mudMap = new DataMap();

	/*
		Returns the mudFloor
	*/
	public Image getImage();

	public void setWater(WaterInterface w);

	/*
		Returns true or false for mud at postion
	*/
	public boolean isMud(IntPos x);

	/*
		Adds a pixel of mud too the floor at the given intPos
	*/
       public void addMud(IntPos p);

       /**
         removes mud at the given
         @param p position
       */
       public void removeMud(IntPos p);
    

    /*
    	updates the mud floor for this turn. At the
		moment this does nothing
	*/
	public void doPhysics ();
}
