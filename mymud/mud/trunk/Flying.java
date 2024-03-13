/* $Id: Flying.java,v 1.20 2004/05/01 12:10:37 tk1748 Exp $ */
package mud.trunk;
import mud.supporting.*;
/**
A Flying Prop to be Displayed on the Screen
*/ 
public class Flying extends Prop
{

    double v_Power;
    double h_Power;
    double gravity;
    RealPos rp;
    double mass;

    /**
    One of them damn stupid things
    */
    protected Flying()
    {
    }
    /**
    A constructor of a flying prop
    @param rp the position it is created
    @param s the stage to be created on
    @param power the power it is created with 
    @param angle the angle the prop is fired at
     */
    public Flying(RealPos rpa, Stage s,double power,double angle,double gravit)
    {
	/* G: new thing in Prop as a constructor
	   prop should quit with an error if sprite or
	   position is undefined
	*/
	setUpProp(rpa, s, s.getSpriteLoader().getSprite("nonExistant"));
        rp = new RealPos(rpa.getX(),rpa.getY());     

        double anglea =angle;
        v_Power = power *(Math.cos(anglea));
        h_Power = power *(Math.sin(anglea));  
        gravity = gravit;
        mass =10;
    }
    /**
    Method called by physics do make the object move
    */
    public void runPhysics(double wind,double grav)
    {
        gravity = grav;  
	double x = rp.getX();
        double y = rp.getY();
        x= x +(h_Power/10)+(wind/102) ;
        y = y +v_Power/10 +mass* gravity/102;
        rp.set(x,y);
        changePosition(rp);
        v_Power = v_Power +mass*gravity/102;
        h_Power =h_Power +wind/102;
    }
    
    public void doPhysics(double wind,double grav)
    {
      runPhysics(wind,grav);
    }
    /**
    called when a prop collides with a Mud Bomb
    @param rp the position of the collision
    */
    

    public void onMudBombCollision(RealPos rpx) 
    {
	changePosition(rpx);
	h_Power = 0;
	v_Power = 0;
	
    }
    /**
    called when a prop collides with a Water Bomb
    @param rp the position of the collision
    */
    public void onWaterBombCollision(RealPos rpx)
    {
        finalCurtain();

    }
    /**
    called when a prop collides with the Mud Floor
    @param rp the position of the collision
     */
    public void onCollisionWithMud(RealPos rpx) 
    {
	finalCurtain();
    }

    /**
    called when a prop collides with the Water
    @param rp the position of the collision
     */
    public void onCollisionWithWater(RealPos rpx) 
    {
	System.err.println("Flying reports W"+this+ "@"+rpx);
	finalCurtain();
    }

    /**
    called when a prop collides with the water floor
    @param rp the position of the collision
    */
    public void onWaterFloorCollision(RealPos rpx)
    {
        finalCurtain();
    }
    /**
    called when the prop collides with a player
    @param rp the position of the collision
    */
    public void onPlayerCollision(RealPos rpx) 
    {
	finalCurtain();
    }

        /**
    called when the prop collides with the right wall
    @param rp the position of the collision
    */
    public void onCollisionWithWall(RealPos rpx) 
    {
        changePosition(rpx);
	h_Power = 0 - h_Power;
    }
        /**
    called when the prop collides with the bottom 
    @param rp the position of the collision
    */
    public void onCollisionWithCeiling(RealPos rpx) 
    {
        changePosition(rpx);
	v_Power = -v_Power;
    }

    /**
    called when the prop collides with the bottom 
    @param rp the position of the collision
    */
    public void onCollisionWithFloor(RealPos rpx) 
    {
        changePosition(rpx);
	/* make like we hit mud */
	onCollisionWithMud(rpx);
    }
}
