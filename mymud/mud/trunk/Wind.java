package mud.trunk;

import mud.supporting.*;
import java.util.Random;
import java.awt.Image;

/**
   This class chnages the wind every frame. It takes
   a random number in +- WIND_RANGE and chnages the
   wind to it gradually (by a maximum of MAX_WIND_CHANGE)
   when it gets there it picks another target and so on.
 */
public class Wind extends Cloud
{

    /* how smooth the wind is*/
    private        double MAX_WIND_CHANGE = 5;
    /* how fast the fastest wind is */
    private        double WIND_RANGE = 150;
    private double windTarget;
    /* is true if, last frame the wind was less than
       windTarget */
    private boolean lessThan;
    private        int NUM_CLOUDS = 10;

    /**
    Creates a wind object. Only one of these should
    be on screen at one time, controls the wind and 
    the clouds
    @param s the stage to float the cloud over
    @param w the inital wind
    */
    public Wind(Stage s, double jitter, double max, int cloudNo)
    {
	MAX_WIND_CHANGE = jitter;
	WIND_RANGE = max;
	NUM_CLOUDS = cloudNo;
	RealPos rpa;
	double wind = s.getGameValue().wind;
	randy = new Random(System.currentTimeMillis());
	spriteWidth = 200;//getSprite().getImage().getWidth();

	/* Our wind cloud always moves at speed 2 ...*/
	speed = 2;

	/* decide if we are a new cloud at the left
	   or right had side of the screen */
	position = s.getGameValue().mapSizeX/2;
	/* set position */
	rpa = new RealPos(position, speedToHeight(speed));
	/* add to stage */
        if (s == null)
	{
	    System.err.println("STAGE IS NULL");
	}
        if (s.getSpriteLoader() == null)
	{
	    System.err.println("No sprite loader");
	}

	setUpProp(rpa, s, s.getSpriteLoader().getSprite(0,3));

	/* how much each addition cloud speeds up on the previous */
	double speedInc = 3/(new Double (NUM_CLOUDS).doubleValue());
	/* Add some other clouds to the stage */

	for (int i = 1; i < NUM_CLOUDS+1; i++)
	{
	    double cloudSpeed = i*speedInc;
	    new Cloud(myStage(), randy.nextDouble()*(myStage().getGameValue().mapSizeX+(2*BUFFER_DISTANCE))-BUFFER_DISTANCE, cloudSpeed);
	}

	/* decide how windy it _should_ be */
	windTarget = getWindTarget();
	if (wind < windTarget)
	{
	    lessThan = false;
	}
	else
	{
	    lessThan = true;
	}

    }

    /**
       Makes the cloud move
     */
    public void doPhysics()
    {
	doCloudPhysics();
	doWind();
    }

    private void doWind()
    {
	double wind = myStage().getGameValue().wind;

	if (wind < windTarget)
	{
	    wind = wind + randy.nextDouble()*MAX_WIND_CHANGE;
	    if (!lessThan)
	    {
		windTarget = getWindTarget();
	    }
		lessThan = true;
	}
	else
	{
	    wind = wind - randy.nextDouble()*MAX_WIND_CHANGE;
	    if (lessThan)
	    {
		windTarget = getWindTarget();
	    }
		lessThan = false;
	}
	/* Print out the wind */
	//System.out.println(myStage().getGameValue().wind);
	myStage().getGameValue().wind = wind;
    }

    private double getWindTarget()
    {
	double x = (((randy.nextDouble())*WIND_RANGE*2)-WIND_RANGE);
	//System.out.println(">>"+x);
	return x;
    }
}
