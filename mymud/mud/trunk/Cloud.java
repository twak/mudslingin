package mud.trunk;

import mud.supporting.*;
import java.util.Random;
import java.awt.Image;

/**
   This makes a thing that look like a cloud move left
   or right according to the wind that is blowing
 */
public class Cloud extends Prop
{

    protected Random randy;
    protected double position;
    /* the distance a cloud will remain on stage
       once it leaves the visable ares */
    protected static double BUFFER_DISTANCE = 100;
    /* This should be 0,1 or 2 */
    protected double  speed;
    protected int     spriteWidth;

    /**
    Creates a new cloud that moves with the wind at a random position
    @param s the stage to float the cloud over
    */
    public Cloud(Stage s)
    {
	RealPos rpa;
	double wind = s.getGameValue().wind;
	randy = new Random(240783);
	spriteWidth = 200;//getSprite().getImage().getWidth();

	speed = randy.nextDouble()*2+1;


	/* decide if we are a new cloud at the left
	   or right had side of the screen */
	fellOfEdge(s);
	/* set position */
	rpa = new RealPos(position, speedToHeight(speed));
	/* add to stage */
	if(speed < 1.2)
	{
	setUpProp(rpa, s, s.getSpriteLoader().getSprite(0,3));
        }
	else
	{
	setUpProp(rpa, s, s.getSpriteLoader().getSprite(0,2));
        }
    }

    /**
    creates a new cloud at a given position
    @param s the Stage to put the cloud on
    @param p the position (left to right) of the cloud
    @param v the speed the cloud is travelling (0= none, 3 = fast)
     */
    public Cloud(Stage s, double p, double v)
    {
	double wind = s.getGameValue().wind;
	randy = new Random(240783);
	spriteWidth = 200;//getSprite().getImage().getWidth();

	speed = v;


	/* decide if we are a new cloud at the left
	   or right had side of the screen */
	fellOfEdge(s);
	/* set position */
	position = p;
	RealPos rpa = new RealPos(position, speedToHeight(speed));
	/* add to stage */
         if(speed < 1.2)
	{
	setUpProp(rpa, s, s.getSpriteLoader().getSprite(0,3));
        }
	else
	{
	setUpProp(rpa, s, s.getSpriteLoader().getSprite(0,2));
        }
    }

    /** Dummy constructor so we extend this class */
    protected Cloud()
    {
    }

    /**
       Cloud has gone too far, add it again where it
       doesn't show but should blow into sight
     */
    protected void fellOfEdge(Stage s)
    {
	/* move it to where it will be blown back into view*/
	double wind = s.getGameValue().wind;
	if (wind < 0)
        {
	    position = s.getGameValue().mapSizeX+BUFFER_DISTANCE;
	}
	else
	{
	    position = (-spriteWidth-BUFFER_DISTANCE);
	}
    }


    /**
       Makes the cloud move
     */
    public void doPhysics()
    {
	doCloudPhysics();
    }

    /**
       Does the moving of this cloud
     */
    protected void doCloudPhysics()
    {
	RealPos i = getPosition();
	double wind = myStage().getGameValue().wind;
	position =position+(wind*speed/8);
	changePosition(new RealPos(position,i.getY()));

	/* Check for cloud being too far gone */
	if (position < 0-spriteWidth -BUFFER_DISTANCE ||
	    position > myStage().getGameValue().mapSizeX+BUFFER_DISTANCE)
	{
	    fellOfEdge(myStage());
	}
    }

    /**
    specifies the distance from the top of the display
    window as a function of out 0-3 speed
    @param i the speed
    */
    protected double speedToHeight(double i)
    {
	return (i*30+10);
    }
}

