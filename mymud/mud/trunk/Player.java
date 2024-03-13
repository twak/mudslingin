/* $Id: Player.java,v 1.49 2004/05/05 12:06:09 tk1748 Exp $ */
package mud.trunk;
import mud.supporting.*;
import java.util.Random;
/**
This class contains information and procedures
relating to the Player
 */
public class Player extends Prop
{
    private String         name;
    private Target         aimAt;
    private DeathOMeter    dom;
    private Prop           weapon;
    private int            start = 0;
    private int            deathfolder = 0; //Use to set folder for death animation
    private int            deathcount = 0; //Used to slow the fist half of the death animation
    private   int            deathnum = 1; //Used as frame number for death animation
    protected int            death = 0; //Tells us if player had died
    private int              startvic = 0;
    private int              victoryfolder = 0; //Use to set folder for victory animation
    private int              victorycount = 0; //Use to slow death animation
    private int              victorynum = 25; //Use for frame number for victory animation
    private   int            weaponDisplayTimer = 0;
    private   int            spritecounter = 20;
    private   Random         randy;
    /* The weapon we are going to fire with */
    private int            myWeapon;
    /* How long the fire key has been held down */
    private int            fireCount = -1;
    /* size of angle change (fractions of a full turn */
    public static int      ANGLE_CHANGE = 60;
    private static double  MIN_ANGLE_CHANGE = Math.PI/ANGLE_CHANGE;
    private static int     MAX_POWER = 120;
    /* The number of different weapons we have */
    private static int     NUM_WEAPONS = 3;
    /* if the fire key is down or up */
    private boolean        aimingUp, aimingDown;
    /* images to display to select weapon */
    private int[]       weaponSprites = new int[] {15,16,19};
    /* if 1 we are a dino, else we are a caveman */
    private int spriteSet;
    /* this defines the current set of sprites that will be
       used */
    private int spritesToUse;
    /* How tall this person is, depth before drown */
    private static int HEIGHT = 50;
    /* Counter for how often we check the water depth */
    private int waterCounter = 0, oldP, veryOldP;

    /* Defition of type of player -
       local - on this machine
       remote_server - place holder for a client on a server
       remote_client - we are on the client machine, joining the server
       remote_bot    -  place holder for a server on a client
    */
    private int type;
    public static final int LOCAL         = -100;
    public static final int REMOTE_SERVER = -99;
    public static final int REMOTE_CLIENT = -98;
    public static final int REMOTE_BOT    = -97;

    /** 
    Constructor to set the positions
    in one swoop
    @param rp the position of the player
    @param s Stage that this player is on
    @param m Sprite this prop is showing
    @param type [0|1] are we a caveman(0) or a dino (1)?
    */
    public Player(RealPos rp, Stage s, int sS)
    {
	/* define what we look like */
	spriteSet = sS;
	whatDoWeLookLike();
	/* This just loads the default sprite now */
	setUpProp(rp, s, s.getSpriteLoader().getSprite(spritesToUse,20));
	/* our default weapon */
	myWeapon = 0;
        /* set our weapon indicator to be a prop */
	weapon = new Prop();
	/* set position off the screen */
	weapon.setUpProp(new RealPos(-300,300),myStage(), s.getSpriteLoader().getSprite("nonexist"));
	/* Add the meter of how dead we are */
	RealPos domPos;
	//  domPos = new RealPos((spriteSet)*690+20,20);
//  	if (rp.getX() < 0) 
	domPos = new RealPos (-100,300);
	dom = new DeathOMeter(domPos, s, HEIGHT);

	/* add a flock of ?birds? to aim at */
	aimAt = new Target(s,this,ANGLE_CHANGE);
	randy = new Random(327136);
	aimingUp   = false;
	aimingDown = false;
	type = LOCAL;
    }

    /**
       This decides what directory to use, given the
       spriteset and the weapon. It sets spritesToUse
     */
    private void whatDoWeLookLike()
    {
	spritesToUse = (spriteSet*3)+myWeapon+1;
    } 

    /**
       Set the location of this player, if
       known in the network sense
     */
    public void setType (int t)
    {
	type = t;
    }
    
    public int getType ()
    {
	return type;
    }

    /** This is called when the player moves */
    public void update()
    {
	/* If the player is moved to a visible location, so
	   is their drownOmeter 
	*/
	dom.changePosition(new RealPos((spriteSet)*690+20,20));
	aimAt.update();
    }

    /**
    Sets the name of this player
    @param s the name to set
    */
    public void setName(String s)
    {
	name = s;
    }

    /** 
    returns the name of the player
    @return name the player name
    */
    public String getName()
    {
	return name;
    }

    /**
    Man, does the physics of the man
    */
    public void doPhysics()
    {
    if(death ==0)
    {
        /* if they are holding down the fire key, count how
	    long they have been holding it for */

	    if (fireCount >= 0) 
	    {
         fireCount++;
	        if (fireCount > MAX_POWER) fireCount = 0;
            /* this should make a stupid noise */
            if (fireCount > 4*MAX_POWER/5) System.out.println(name+": My arm getting tired");
            aimAt.setDistance(fireCount*4+40);
        }
        /* Adjust the target as necessary */
	    if (aimingUp  ) 
	    {
	        aimAt.increaceAngle(1);
            if (aimAt.getAngle() < 3*Math.PI/2)
	        {
	            int frame = new Long(Math.round((aimAt.getAngle()-(Math.PI/2))*40/Math.PI)).intValue();
	            changeSprite(myStage().getSpriteLoader().getSprite(spritesToUse,frame));
	            spritecounter++;
	        }
	        else
	        {
                /* Undo Change */
	            aimAt.increaceAngle(-1);
            }

	        if (spritecounter > 39)
	        {
	            spritecounter--;
	        }
	    }
	    if (aimingDown)
	    {  
	        aimAt.increaceAngle(-1);
	        if (aimAt.getAngle() > Math.PI/2)
	        {
		        int frame = new Long(Math.round((aimAt.getAngle()-(Math.PI/2))*40/Math.PI)).intValue();
		        changeSprite(myStage().getSpriteLoader().getSprite(spritesToUse,frame));
		        spritecounter--;
	        }
	        else
	        {
	            aimAt.increaceAngle(1);
	        }
	        if(spritecounter < 1)
	        {
		        spritecounter++;
	        }
	    }
	    /* Deal with displaying type of projectile */
	    weaponDisplayTimer--;
	    if (weaponDisplayTimer == 0)
	    {
	        /* Hide it away offstage */
	        weapon.changePosition(new RealPos(-300,-300));
	    }


	    /* "are we nearly dead yet?" - we only check every 30 
	        frames 
	    */
	    if (waterCounter > 30)
	    {
	        int p =((WaterThree)myStage().getWater()).waterDepth(new IntPos(new RealPos(getPosition().getX()+75,getPosition().getY()+100)));
	        dom.setDepth(p);
	    
	        if (p > HEIGHT && oldP > HEIGHT && veryOldP > HEIGHT)
	        {
		        death = 1;		        
	        }
	        waterCounter = 0;
	        veryOldP = oldP;
	        oldP = p;
	    }

            waterCounter++;
    }
    if(death == 1)
    {
        for (int q = 0; q < 2; q++)
        {
            if (myStage().allPlayers().get(q)!=this)((Player)myStage().allPlayers().get(q)).onWin();
        }
        if (start == 0)
        {
            if(spriteSet == 0)
            {
                changeSprite(myStage().getSpriteLoader().getSprite(1,spritecounter));	
                if(spritecounter > 1)
                {
                    spritecounter--;
                    changeSprite(myStage().getSpriteLoader().getSprite(1,spritecounter));
                }
	            else
	            {
		            Prop p = new Prop();
		            p.setUpProp(new RealPos(getPosition().getX()+42,getPosition().getY()+69),myStage(),myStage().getSpriteLoader().getSprite(8,23));
		            changeSprite(myStage().getSpriteLoader().getSprite(8,01));
		            changePosition(new RealPos(getPosition().getX()-18.5,getPosition().getY()+41));
		            start = 1;
                    deathfolder = 8;
	            }
            }
            else if(spriteSet == 1)
            {
                changeSprite(myStage().getSpriteLoader().getSprite(4,spritecounter));	
                if(spritecounter < 40)
                {
                    spritecounter++;
                    changeSprite(myStage().getSpriteLoader().getSprite(4,spritecounter));
                }
	            else
	            {
		            Prop p = new Prop();
		            p.setUpProp(new RealPos(getPosition().getX()+42,getPosition().getY()+39),myStage(),myStage().getSpriteLoader().getSprite(9,23));
		            changeSprite(myStage().getSpriteLoader().getSprite(9,01));
		            changePosition(new RealPos(getPosition().getX()+105,getPosition().getY()+41));
		            start = 1;
                    deathfolder = 9;
                }
            }
        }
        else if(start == 1)
        {
            if(deathnum > 6 && deathnum != 23)
            {
                changeSprite(myStage().getSpriteLoader().getSprite(deathfolder,deathnum));
                deathcount = 0;
                deathnum++;
            }
            if(deathcount == 10)
            {               
                changeSprite(myStage().getSpriteLoader().getSprite(deathfolder,deathnum));
                deathcount = 0;
                deathnum++;
            }
            if(deathnum == 23)
            {
                myStage().gameOver();
            }
            deathcount++;
        }
    }
    }
    
    public void onWin()
    {   
        if(startvic == 0)
        {
            if(spriteSet == 0)
            {
                changeSprite(myStage().getSpriteLoader().getSprite(1,spritecounter));	
                if(spritecounter > 1)
                { 
                    spritecounter--;
                    changeSprite(myStage().getSpriteLoader().getSprite(1,spritecounter));
                }
	            else
	            {
		            Prop r = new Prop();
		            r.setUpProp(new RealPos(getPosition().getX()+42,getPosition().getY()+69),myStage(),myStage().getSpriteLoader().getSprite(8,23));
		            changeSprite(myStage().getSpriteLoader().getSprite(8,24)); 
		            changePosition(new RealPos(getPosition().getX()-18.5,getPosition().getY()+41));
                    victoryfolder = 8;
                    startvic = 1;
                }
            }
            else if(spriteSet == 1)
            {
                changeSprite(myStage().getSpriteLoader().getSprite(4,spritecounter));	
                if(spritecounter < 40)
                {
                    spritecounter++;
                    changeSprite(myStage().getSpriteLoader().getSprite(4,spritecounter));
                }
	            else
	            {
		            Prop r = new Prop();
		            r.setUpProp(new RealPos(getPosition().getX()+42,getPosition().getY()+39),myStage(),myStage().getSpriteLoader().getSprite(9,23));
		            changeSprite(myStage().getSpriteLoader().getSprite(9,24));
		            changePosition(new RealPos(getPosition().getX()+105,getPosition().getY()+41));
		            startvic = 1;
                    victoryfolder = 9;
                }
            }
        }    
        if (startvic == 1)
        {
            if(victorycount == 10)
            {               
                changeSprite(myStage().getSpriteLoader().getSprite(victoryfolder,victorynum));
                victorycount = 0;
                victorynum++;
            }
            if(victorynum == 33)
            {
                victorynum = 24;
            }
            victorycount++;
        }
    }
   

    /**
	Tells us which frame to use for the angle
	From 0 -> 40 on angles 
	@return the angle
    */
    public int getFrame()
    {
	int out = 3;
	return out;
    }

    /**
    when the aim up key is pressed
    @param keyPos position of key true = up, false = down
    */
    public void onAimUp(boolean keyPos)
    {
        aimingUp = keyPos;
    }

    /**
    when the aim down key is pressed
    @param keyPos position of key true = up, false = down
    */
    public void onAimDown(boolean keyPos)
    {
        aimingDown = keyPos;
    }

    /** 
       called when the fire key goes down
     */
    public void onStartFire()
    {
	if (fireCount < 0)
	{
	    fireCount = 0;
	}
    }

    /**
       called when the fire key goes up
     */
    public void onEndFire()
    {
	if (fireCount > 0)
	{
	    double power = fireCount * 12;
	    if (power > 60)
	    {
		switch (myWeapon)
		{
		    /* dummy so that we have three sections, and the bombs can
		       be added in in a second*/
		case 2:
		    new Bangbomb(new RealPos(getPosition().getX()+50,getPosition().getY()+50), myStage(), power, aimAt.getAngle(),10.0);
		    break;
		case 1:
		    new Waterbomb(new RealPos(getPosition().getX()+50,getPosition().getY()+50), myStage(), power, aimAt.getAngle(),10.0);
		    break;
		case 0:
		    new Mudbomb  (new RealPos(getPosition().getX()+50,getPosition().getY()+50), myStage(), power, aimAt.getAngle(),10.0);
		    break;
		default:
		    System.err.println("Un-allowed weapon number "+myWeapon);
		}
	    }
	    else
	    {
		/* not enough power sound */
		myStage().playSound(1,1);
	    }
	}
	fireCount = -1;
	aimAt.setDistance(40);
    }


    /**
    Changes the weapon value by i
    @param i the ammount to increace/decrease weapon by. For
             now may only be +/- 1
    */
    public void weaponChange(int i)
    {
	if (fireCount == -1) 
	{
	    myWeapon += i;
            if (myWeapon == -1) myWeapon = NUM_WEAPONS-1;
	    myWeapon = myWeapon % NUM_WEAPONS;
	    /* Check to see what we should look like */
	    whatDoWeLookLike();
	    int frame = new Long(Math.round((aimAt.getAngle()-(Math.PI/2))*40/Math.PI)).intValue();
	    changeSprite(myStage().getSpriteLoader().getSprite(spritesToUse,frame));
	    weapon.changeSprite(myStage().getSpriteLoader().getSprite(0,weaponSprites[myWeapon]));
	    weapon.changePosition(getPosition());
	    weaponDisplayTimer = 30;
	}
	else
	{
	    /* Make a stupid noise - we can't change our
	       weapon once we've started firing */
	    myStage().playSound(1,1);
	    System.out.println("ET_ERRGH");
	}
    }

    /** 
    Called when a mudbomb hits this player
    @param rpx the position of the colision
    */
    public void onMudBombCollision(RealPos rpx)
    {
	System.out.println("OW ive been hit...ug");
    }
    public void onWaterBombCollision(RealPos rpx)
    {
	System.out.println("OW ive been hit...ug");
    }
}





