package mud.trunk;

import mud.supporting.*;


/**
This is envisioned to be a flock of birds that displays the angle
that our player is aiming at by the angle , and the strength of the
shot by the distance from the player 
*/

public class Target extends Prop
{
    /* Angle - number of denominatior to Player.ANGLE_CHANGE's
       numerator */
    private        int       angle;
    private        int       sprite = 4;
    private        int       birdcount;
    private        double    distance = 40;
    private        boolean   angleChange = false;
    private        Player    myMan;
    /* Values for fast trig look up tables */
    static private double[] sin = null;
    static private double[] cos = null;
    /**
      Creates a target for the player to aim at
      @param s the stage to put the target on
      @param a the angle (radians) to start at
      @param p the player who's target this is
     */
    public Target(Stage s, Player p, double a)
    {
	myMan = p;
	angle = new Long(Math.round(a)).intValue();
	if (sin == null) setUpTrig();

	RealPos x = calcPosition(angle);

	setUpProp(x, s, s.getSpriteLoader().getSprite(0,4));
	angleChange = true;
    }


    /**
       Initialises the trig tables, not clever but quick enough for me
     */
    private void setUpTrig()
    {
	sin = new double[Player.ANGLE_CHANGE*2];
	cos = new double[Player.ANGLE_CHANGE*2];
	  for (int i = 0; i< Player.ANGLE_CHANGE*2; i ++)
	{
	    sin[i] = Math.sin(i*Math.PI/Player.ANGLE_CHANGE);
	    cos[i] = Math.cos(i*Math.PI/Player.ANGLE_CHANGE);
	}
    }


    /**
    Calculates the co-ordinates of the target at angle a
    form player p
    @param p the player
    @param a the angle the player is aiming in radians.
    */
    private RealPos calcPosition(int a)
    {
	return new RealPos(distance*sin[a] + myMan.getPosition().getX()+30,
			   distance*cos[a] + myMan.getPosition().getY()+30);
    }


    /*
    Called when the position of the player moves, such
    as while initialising
    */
    public void update()
    {
	/* tells it to update next time doPhysics is run */
	angleChange = true;
    }


    /**
    sets the angle of the target, relative to the player
    @param a the angle the player is aiming in radians.
    */
   //   public void setAngle(double a)
//      {
//  	angleChange = true;
//  	angle = a;
//      }

    /**
    sets the distance of the target from the player
    @param d the distance
    */
    public void setDistance(double d)
    {
	angleChange = true;
	distance = d;
    }

    /**
    Increaces the angle by a specified amount
    @param a the amount to change the angle by in radians
    */
    public void increaceAngle(int a)
    {
	angleChange = true;
	angle = angle + a;
	if (angle >= Player.ANGLE_CHANGE*2) angle = 0;
	if (angle < 0) angle =  Player.ANGLE_CHANGE*2-1;
    }

    /**
    If necessary this moves the position of the target
     */
    public void doPhysics()
    {
        if (angleChange)
	{
	    changePosition(calcPosition(angle));
	    angleChange = false;
	}
	birdcount++;
	if (birdcount == 2)
	{
	    birdcount = 0;
	    sprite++;
	}
	if(sprite > 12)
	{
	    sprite = 4;
	}
	changeSprite(myStage().getSpriteLoader().getSprite(0,sprite));
    }

    /** 
    returns the angle that this target
    is pointing at (in radians)
    @return angle the angle
    */
    public double getAngle()
    {
	return angle*(Math.PI/Player.ANGLE_CHANGE);
    }
    
}
