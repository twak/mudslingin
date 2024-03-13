/* $Id: DeathOMeter.java,v 1.1 2004/04/22 16:19:52 tk1748 Exp $ */
package mud.trunk;
import mud.supporting.*;
import java.util.Random;
/**
The meter that shows how dead a player is
 */
public class DeathOMeter extends Prop
{

    int maxDepth;
    /*
      Images are assumed to start from number 1
     */
    static int IMAGES = 11;

    /** 
    Constructor to set the positions
    in one swoop
    @param rp the position of the player
    @param s Stage that this player is on
    @param m Sprite this prop is showing
    @param type [0|1] are we a caveman(0) or a dino (1)?
    */
    public DeathOMeter(RealPos rp, Stage s, int d)
    {
	maxDepth = d;
	setUpProp(rp, s, s.getSpriteLoader().getSprite(7,1));
    }

    protected void setDepth(int in)
    {
	int num;
	num = (int)Math.round(((double)in/(double)maxDepth)*IMAGES);
	/* capping to avoid strange results */
	if (num <= 0) num = 1;
	if (num > IMAGES) num = IMAGES;
	changeSprite(myStage().getSpriteLoader().getSprite(7,num));
    }
}
