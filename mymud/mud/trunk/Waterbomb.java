package mud.trunk;
import mud.supporting.*;
/**
The mudbomb Prop to be displayed on screen
*/
public class Waterbomb extends Flying
{

    /**    
    The constuctor creates a Waterbomb
    @param rpa The real position of the object
    @param s The stage the object should be displyed on
    @param power The power with which the Waterbomb is fired
    @param angle The angle at which the Waterbomb is fired
    @param massx The mass the Waterbomb has
    */
    public Waterbomb(RealPos rpa, Stage s, double power, double angle,double massx)
    {
        //set the sprite to nothing
	setUpProp(rpa, s, s.getSpriteLoader().getSprite(0,14));
        rp = new RealPos(rpa.getX(),rpa.getY()); 
       
        //set up the power and mass
        double anglea  =  angle;
        v_Power = power *(Math.cos(anglea));
        h_Power = power *(Math.sin(anglea));  
        mass = massx;
    }
  
   
    /**
    The physics to be run on on the object
    @param wind The wind acting on the Waterbomb
    @param gravity The gravity acting on the object
    */
    public void doPhysics(double wind, double gravity)
    {
        //run the physics in the Flying class
	runPhysics(wind,gravity);
    }	
    /**
    Called when a collision with a Mudbomb has occured
    @param rpx The highest position the collision took place
    */
   public void onMudBombCollision(RealPos rpx) 
    {
	/* make like we hit mud */
	onCollisionWithMud(rpx);
    } 

    /**
    Called when a collision with another Waterbomb has occured
    @param rpx The highest position the collision took place
    */
    public void onWaterBombCollision(RealPos rpx)
    {
	/* make like we hit mud */
	onCollisionWithMud(rpx);    
    }
    /**
    Called when a collision with a Player has occured
    @param rpx The highest position the collision took place
    */
    public void onCollisionWithPlayer(RealPos rpx)
    {
      finalCurtain();
    
    }  
    /**
    Called when a collision with a Wall has occured
    @param rpx The highest position the collision took place
    */ 
    
    public void onCollisionWithWall(RealPos rpx)
    {
	changePosition(rpx);
	h_Power =0 - h_Power;
    }
    /**
    Called when a collision with a ceiling/floor has occured
    @param rpx The highest position the collision took place
    */
    public void onCollisionWithCeiling(RealPos rpx)
    {
	changePosition(rpx);
	v_Power = 0 - v_Power;
    }
     /**
    Called when a collision with the mudfloor has occured
    @param rpx The highest position the collision took place
    */
    public void onCollisionWithMud(RealPos rpx)
    {
	myStage().playSound(0,3);
	myStage().waterBomb(rpx, 30);
	finalCurtain();
    }
    /**
    Called when a collision with any Water has occured
    @param rpx The highest position the collision took place
    */
    public void onCollisionWithWater(RealPos rpx)
    {
    	myStage().playSound(0,3);
	myStage().waterBomb(rpx, 30);
	finalCurtain();
    }
}  
        
        
      
      
