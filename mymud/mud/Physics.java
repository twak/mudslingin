/* $Id: Physics.java,v 1.51 2004/05/01 12:08:17 tk1748 Exp $ */
package mud;

import mud.supporting.*;
import mud.trunk.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.geom.Rectangle2D.*;
import java.awt.geom.*;

/**
The Physics Class contorls the physics of the props on a stage object
*/

public class Physics implements StageAccess
{
    ArrayList spots;


    /**
       Creates a new Physics Object
    */
    public Physics()
    {

    }    /**
	    The Physics prepares its method here
	    @param s The stage the Physics  is using
	 */
    public void makeUp(Stage s)
    {
        spots = new ArrayList(100);   
    }
    /**
       Calls the main process of the physics object
       @param s The stage the Physics is working on
    */
    public void perform(Stage s)
    {
	GameValues gv = s.getGameValue();
    
        MudFloorInterface floor = s.getMudFloor();
        WaterInterface water = s.getWater();
        
	double grav = gv.gravity;
	double wind = gv.wind;
	RealPos then;
	RealPos now;
        ArrayList positions = new ArrayList();
        ArrayList col = s.allProps();
        int i = 0;
        int j;
        int k = 1;
	while(col.size()>i)
        {
	    if(col.get(i) instanceof Flying && !gv.menuMode)
	    {
		Flying a = (Flying)col.get(i);
		then  = a.getPosition();
		a.doPhysics(wind,grav);
		now = a.getPosition();
		Sprite sp = a.getSprite();
		BufferedImage im = (BufferedImage)sp.getImage();
		double width = (double)im.getWidth();
		double height= (double)im.getHeight();
	
		if(now.getY()+height > 600)
		{
		    a.onCollisionWithFloor(new RealPos(now.getX(), 600 - height));
		}
		if(now.getY()+height < 0)
		{
		    a.onCollisionWithCeiling(new RealPos(now.getX(), 0));
		}
		if(now.getX()+width > 800)
		{
		    a.onCollisionWithWall(new RealPos(800 - width, now.getY()));
		}
		if(now.getX() < 0)
		{
		    a.onCollisionWithWall(new RealPos(0, now.getY()));
		}
 
	    }
	    else
	    {
		if(!gv.menuMode || ((Prop)col.get(i)).isMenu())
		{
		    ((Prop)col.get(i)).doPhysics();
		}

	    } 
	    i++;
	}//endwhile
	if(!gv.menuMode)
	{
	    spots = s.forPhysics();
	    i=0;
      
	    while(i < spots.size())
	    {
		SpotLight spot = (SpotLight)spots.get(i);
		ArrayList items = (ArrayList)spot.getPropsAudience();
          

		j=0;

		while(j < items.size())
		{
		    if(items.get(j) instanceof Flying)
		    {
			Flying a = (Flying)items.get(j);
			RealPos posa  = a.getPosition(); 
			Sprite spa = a.getSprite();
			BufferedImage ima = (BufferedImage)spa.getImage();
			double widtha = (double)ima.getWidth();
			double heighta= (double)ima.getHeight();
			Rectangle2D.Double ball1 = new Rectangle2D.Double();
			ball1.setRect(posa.getX(),posa.getY(),widtha,heighta); 
			if(spot.isMuddy())
			{
                 
			    if (floor.isMud(new IntPos(new Long(Math.round(posa.getX()+(widtha/2))).intValue(),new Long(Math.round(posa.getY()+heighta)-10).intValue())))
			    {
				a.onCollisionWithMud(new RealPos((posa.getX()+(widtha/2)),(posa.getY()+(heighta/2))));
	          
			    }

			} 
			if(spot.isWet())
			{
                 
			    if (water.isWater(new IntPos(new Long(Math.round(posa.getX()+(widtha/2))).intValue(),new Long(Math.round(posa.getY()+heighta)-10).intValue())))
			    {
				a.onCollisionWithWater(new RealPos((posa.getX()+(widtha/2)),(posa.getY()+(heighta/2))));
	          
			    }

			}
			k=0;
			while(k < items.size())
			{ 
			    if(k != j && items.get(k) instanceof Flying)
			    {
				Flying b = (Flying)items.get(k);
				RealPos posb = b.getPosition();
				Sprite spb =b.getSprite();
				BufferedImage imb = (BufferedImage)spb.getImage();
				double widthb = (double)imb.getWidth();
				double heightb= (double)imb.getHeight();
				if(ball1.intersects(posb.getX(),posb.getY(),widthb,heightb))
				{
				    double yPlace;
				    double xPlace;
				    double yInTop = posb.getY() -posa.getY() +heighta;
				    double yInBottom = posa.getY() - posb.getY() +heightb;
				    double xInRight = posb.getX() -posa.getX() +widtha;
				    double xInLeft = posa.getX() -posb.getX() +widthb;
				    double xposa = posa.getX();
				    double yposa = posa.getY();
				    double yposb = posb.getY();
				    double xposb = posb.getX();
				    if(yInTop < yInBottom && yInTop > 0 || yInBottom < 0)
				    {
					yPlace = yInTop;
				    }
				    else
				    {
					yPlace = yInBottom;
				    }
				    if(xInRight < xInLeft && xInRight > 0 || xInLeft < 0)
				    {
					xPlace = xInRight;
				    }
				    else
				    {
					xPlace = xInLeft;
				    }
				    if(xPlace < yPlace)
				    {
					if(posb.getX() > posa.getX())
					{
					    xposa = posa.getX()+widthb;
					}
					else
					{
					    xposb = posb.getX()+widtha;

					}			   
				    }   
				    else
				    {
					if(posb.getY() > posa.getY())
					{
					    yposb = posb.getY()+ heighta;	     
					}
					else
					{
					    yposa = posa.getY() + heightb;
					}
				    }
				/* quick fix, need, er fixin... */
				    if(b instanceof Mudbomb)
				    {
					a.onMudBombCollision(new RealPos(xposa,yposa));
				    }
				    else
				    {
					a.onWaterBombCollision(new RealPos(xposa,yposa));
				    }
				    if(a instanceof Mudbomb)
				    {
					b.onMudBombCollision(new RealPos(xposb,yposb));
				    }
				    else
				    {
					b.onWaterBombCollision(new RealPos(xposb,yposb));
				    }
				    if(a instanceof Bangbomb)
				    {
					b.onMudBombCollision(new RealPos(xposb,yposb));
				    }
				    else
				    {
					b.onMudBombCollision(new RealPos(xposb,yposb));
				    }
		       
				}
		    
			    }
			    else if(k != j && items.get(k) instanceof Player)
			    {
				Player b = (Player)items.get(k);
				RealPos posb = b.getPosition();
				Sprite spb =b.getSprite();
				BufferedImage imb = (BufferedImage)spb.getImage();
				double widthb = (double)imb.getWidth();
				double heightb= (double)imb.getHeight();
				if(ball1.intersects(posb.getX(),posb.getY(),widthb,heightb))
				{
				    double yPlace;
				    double xPlace;
				    double yInTop = posb.getY() -posa.getY() +heighta;
				    double yInBottom = posa.getY() - posb.getY() +heightb;
				    double xInRight = posb.getX() -posa.getX() +widtha;
				    double xInLeft = posa.getX() -posb.getX() +widthb;
				    if(yInTop < yInBottom && yInTop > 0 || yInBottom < 0)
				    {
					yPlace = yInTop;
				    }
				    else
				    {
					yPlace = yInBottom;
				    }
				    if(xInRight < xInLeft && xInRight > 0 || xInLeft < 0)
				    {
					xPlace = xInRight;
				    }
				    else
				    {
					xPlace = xInLeft;
				    }
				    if(xPlace < yPlace)
				    {
					//a.onPlayerCollision(posa);
					if ( a instanceof Mudbomb)
					{
					    b.onMudBombCollision(new RealPos((posa.getX() + widtha),posb.getY()));
					}
					else
					{
					    b.onWaterBombCollision(new RealPos((posa.getX() + widtha),posb.getY()));
					}		    
				    }
				    else
				    {
					//a.onPlayerCollision(posa);
					if ( a instanceof Mudbomb)
					{
					    b.onMudBombCollision(new RealPos(posb.getX(),(posa.getY() +heighta)));
					}
					else
					{
					    b.onWaterBombCollision(new RealPos(posb.getX(),(posa.getY() +heighta)));
					}  
				    } 
				}
			    }
			    k++;
			} 
 
                   
		    }




		    j++;
		}

          
         
		i++;


	    }

 
	    floor.doPhysics();
	    water.doPhysics();
	}

    }
    /**
       This is called when the Physics  is no longer
       needed when the game is exiting
    */
    public void goHome()
    {
   
    }
    
}
