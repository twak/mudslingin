/* $Id: SceneSetter.java,v 1.32 2004/05/05 10:57:01 tk2550 Exp $ */
package mud.display;

import mud.trunk.*;
import mud.supporting.*;
import java.util.ArrayList;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


/**
The SceneSetter class draws the window
*/
class SceneSetter extends JPanel
{  
    private ArrayList allProps;
    private MudFloorInterface mudFloor;
    private boolean trans;
    private int originX = 0;
    private int originY = 0;
    private Stage myStage;
    private WaterInterface water;

    private Audience audience;

    /**
    Creates a new SceneSetter
    @param tempScale Whether or not centering is to occur
    */
    SceneSetter(boolean tempTrans, Audience a)
    {
	trans = tempTrans;
	audience = a;
    }
    
    public void update(Graphics g)
    {
	paint(g);
    }


    /**
    Paint the Graphics object
    @param g The Graphics object to be drawn
    */

    synchronized public void paintComponent(Graphics g)
    {
       //super.paintComponent(g);
       Graphics2D g2 = (Graphics2D) g;
       Prop prop;
       IntPos position;
       Image image;
       int width, height, scaledWidth, scaledHeight;
       int scaleFactor = 2;
       int mapX = 0;
       int mapY = 0;
       ArrayList aud = new ArrayList();
       GameValues settings;
       int squareWidth = 0;
       int squareHeight = 0;
       IntPos pos;
       ArrayList spotProps = new ArrayList();
       SpotLight currentSpot = null;
       ArrayList drawnSoFar = new ArrayList();

       Color grass = new Color(128,0,0);
       
       int n = 0;
       int x = 0;
       int y = 0;
       
       if (myStage != null)
       {
          aud = myStage.forAudience();
          settings = myStage.getGameValue();
          squareWidth  = settings.mapSizeX/GameValues.SPOTLIGHTS;
          squareHeight = settings.mapSizeY/GameValues.SPOTLIGHTS;
       }

       if (trans)
	 g2.translate(originX,originY);
       
       // This is where my graphics engine is going to be
      

       if (aud != null)
       {
		    x = 0;
       	
       	    while (x < aud.size())
       	    {
       	   	    if((currentSpot = (SpotLight) aud.get(x)) != null)
       	   	    {
       	   	       pos = currentSpot.getPosition();
       	   	    
       	   	       if ((image = myStage.getBackground()) != null)
		           {
			      image = ((SpotLight) aud.get(x)).getImageBack();
		      	      //image = ((BufferedImage)image).getSubimage(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
		      	      g2.drawImage(image,squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight,this);
		           }
		           else
		           {
       	   	   	      g2.setColor(myStage.getGameValue().bgColour);
       	   	   	      g2.fillRect(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
       	   	       }
       	   	    }
		        x++;
		    }



	    x = 0;

		    while (x < aud.size())
		    {
       	   	   
       	   	   y = 0;
       	   	   
       	   	   if ((currentSpot = (SpotLight) aud.get(x))!=null)
       	   	   {
       	   	      spotProps = currentSpot.getPropsAudience();
       	   	   
       	   	      if (spotProps != null)
       	   	      {
       	   	   	      while (y < spotProps.size())
       	   	          {
       	   	   	          prop = (Prop) spotProps.get(y);
       	   	   	        
       	   	   	          if ((prop!=null)&&(!drawnSoFar.contains(prop))&&(prop instanceof Sun))
       	   	   	          {
       	   	   	       	     drawnSoFar.add(prop);
       	   	   	       	  
       	   	   	             position = new IntPos(prop.getPosition());
       	   	   	   
	      		             if((image = prop.getSprite().getImage()) != null)
					 g2.drawImage(image,position.getX(),position.getY(),this);       	   	       
       	   	   	          }
       	   	   	   
       	   	   	          y++;
       	   	   	       }
 
       	   	   	   }

       	   	   }
       	   	   x++;
       	   }



	    x = 0;

		    while (x < aud.size())
		    {
       	   	   
       	   	   y = 0;
       	   	   
       	   	   if ((currentSpot = (SpotLight) aud.get(x))!=null)
       	   	   {
       	   	      spotProps = currentSpot.getPropsAudience();
       	   	   
       	   	      if (spotProps != null)
       	   	      {
       	   	   	      while (y < spotProps.size())
       	   	          {
       	   	   	          prop = (Prop) spotProps.get(y);
       	   	   	        
       	   	   	          if ((prop!=null)&&(!drawnSoFar.contains(prop))&&(prop instanceof Cloud))
       	   	   	          {
       	   	   	       	     drawnSoFar.add(prop);
       	   	   	       	  
       	   	   	             position = new IntPos(prop.getPosition());
       	   	   	   
	      		             if((image = prop.getSprite().getImage()) != null)
					 g2.drawImage(image,position.getX(),position.getY(),this);       	   	       
       	   	   	          }
       	   	   	   
       	   	   	          y++;
       	   	   	       }
 
       	   	   	   }

       	   	   }
       	   	   x++;
       	   }



		    
		    x = 0;

       	    while (x < aud.size())
       	    {
       	   	    if((currentSpot = (SpotLight) aud.get(x)) != null)
       	   	    {
       	   	       pos = ((SpotLight)aud.get(x)).getPosition();
       	   	
       	   	       if (currentSpot.isMuddy())
       	   	       {
       	   	          if ((mudFloor != null)&&((image = mudFloor.getImage()) != null))
		    	      {   
				      image = ((SpotLight)aud.get(x)).getImageMud();
		        	      //image = ((BufferedImage)image).getSubimage(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
		        	      g2.drawImage(image,squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight,this);
				      //g2.drawRect(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
				      //g2.setColor(grass);
				      //g2.drawLine(squareWidth*pos.getX()+15,squareHeight*pos.getY()+10,squareWidth*(pos.getX()+1)-10,squareHeight*(pos.getY()+1)-10);

		    	      }
       	   	       }
		       if (currentSpot.isWet())//(water != null)&&((image = water.getImage()) != null))
		       {
			       image = currentSpot.getImageWater();//((BufferedImage)image).getSubimage(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
			       g2.drawImage(image,squareWidth*pos.getX(),squareHeight*pos.getY(),this);
			       //g2.drawRect(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
		       }
       	   	    }
		        x++;
	        }

		    x = 0;

		    while (x < aud.size())
		    {
       	   	   
       	   	   y = 0;
       	   	   
       	   	   if ((currentSpot = (SpotLight) aud.get(x))!=null)
       	   	   {
       	   	      spotProps = currentSpot.getPropsAudience();
       	   	   
       	   	      if (spotProps != null)
       	   	      {
			  /* g2.setColor(Color.red);
       	   	   	      pos = ((SpotLight)aud.get(x)).getPosition();
       	   	   	      g2.drawRect(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);*/
       	   	   	      while (y < spotProps.size())
       	   	          {
       	   	   	          prop = (Prop) spotProps.get(y);
       	   	   	        
       	   	   	          if ((prop!=null)&&(!drawnSoFar.contains(prop)))
       	   	   	          {
       	   	   	       	     drawnSoFar.add(prop);

       	   	   	             position = new IntPos(prop.getPosition());
       	   	   	   
	      		             if((image = prop.getSprite().getImage()) != null)
       	   	   	                g2.drawImage(image,position.getX(),position.getY(),this);
       	   	   	                
       	   	   	             
       	   	   	          }
       	   	   	   
       	   	   	          y++;
       	   	   	       }
 
       	   	   	   }

       	   	   }
       	   	   x++;
       	   }
       }

       if((trans)&&(myStage != null))
	   {
	       g2.setColor(grass);
	       g2.fillRect(800,0,200,600);
	       g2.fillRect(-200,0,200,600);
	   }
       
       audience.goodMorning();
       
       // New graphics engine ends here

       // Old graphics engine starts here

       /*if ((mudFloor != null)&&((image = mudFloor.getImage()) != null))
       {   
	   mapX = image.getWidth(this);
	   mapY = image.getHeight(this);
	   if (false)
	   {   width    = image.getWidth(this);
	       height   = image.getHeight(this);
	       scaledWidth  = width * scaleFactor;
	       scaledHeight = height * scaleFactor;
	       g2.drawImage(image,0,0,scaledWidth,scaledHeight,this);
	   }
	   else
	       g2.drawImage(image,0,0,this);
       }

       n = 0;

       if (allProps != null)
       {       
           while (n != allProps.size())
           {
	      prop = (Prop) allProps.get(n);
	      position = new IntPos(prop.getPosition());
	      if((image = prop.getSprite().getImage()) != null)
	      {  
		  if (false)
		  {   width    = image.getWidth(this);
		      height   = image.getHeight(this);
		      scaledWidth  = width * scaleFactor;
		      scaledHeight = height * scaleFactor;
		      g2.drawImage(image,position.getX(),position.getY(),
				   scaledWidth,scaledHeight,this);
		  }
		  else
		      g2.drawImage(image,position.getX(),position.getY(),this);
	      }
	      n++;
	   }
	   }*/
       
       // Old graphics engine ends here
       
       
       
/**       GameValues settings = myStage.getGameValue();
       int squareWidth  = settings.mapSizeX/GameValues.SPOTLIGHTS;
       int squareHeight = settings.mapSizeY/GameValues.SPOTLIGHTS;
       ArrayList phy = myStage.forPhysics();
       ArrayList aud = myStage.forAudience();
       IntPos pos;*/


/*      g2.setColor(Color.red);

       for (int q = 0; q < aud.size(); q++)
       {
	   pos = ((SpotLight)aud.get(q)).getPosition();
	   g2.drawRect(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
       }
/*
  g2.setColor(Color.blue);

       for (int q = 0; q < phy.size(); q++)
       {
	   pos = ((SpotLight)phy.get(q)).getPosition();
	   g2.drawRect(squareWidth*pos.getX(),squareHeight*pos.getY(),squareWidth,squareHeight);
       }
*/
       if (trans)
       {  
	  // g2.setColor(Color.gray);
          // g2.fillRect(-originX,0,originX,mapY);
	  // g2.fillRect(mapX,0,originX,mapY);

	  g2.translate(-originX,-originY);
       }
    }
    
    /**
    Redraws the entire screen
    @param allProps the ArrayList of props to be drawn
    @param mf the MudFloor to be drawn
    @param mapX the width of the map
    @param mapY the height of the map
    */
    void redraw(ArrayList tempProps, MudFloorInterface mf, int tempX, int tempY, Stage s)
    {
       myStage = s;
       originX = tempX;
       originY = tempY;

       water = s.getWater();
       mudFloor = mf;
       allProps = tempProps;
       repaint();
    }
}
