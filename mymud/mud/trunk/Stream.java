/* temport file to make mudballs until we get some input */
package mud.trunk;

import mud.supporting.*;
import java.util.Random;


public class Stream extends Prop
{

    Random randy;
    int change;
    private int birdcount=1;
    private int sprite = 4;
	int counter;

    public Stream(RealPos rpa, Stage s)
    {
	setUpProp(rpa, s, s.getSpriteLoader().getSprite("caveman1.jpg"));
	randy = new Random(System.currentTimeMillis()+rpa.hashCode());
	change = +1;
	counter = -10;
	speed = 30;
	changeSprite(myStage().getSpriteLoader().getSprite(0,20));
    }


    public void doPhysics()
    {
	counter ++;
        if (counter == 0)
	{
	    counter = -speed;
	    myStage().waterBomb(getPosition(), 30);
	}
    }
}

