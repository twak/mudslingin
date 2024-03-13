/* temport file to make mudballs until we get some input */
package mud.trunk;

import mud.supporting.*;
import java.util.Random;


public class MudFountain extends Prop
{

    Random randy;
    int change;
    private int birdcount=1;
    private int sprite = 4;
	int counter;

    public MudFountain(RealPos rpa, Stage s)
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
	RealPos i = getPosition();
	counter ++;
        if (counter == 0)
	{

	//changePosition(new RealPos(i.getX()+change,i.getY()));
    
		counter = -speed;
		if (randy.nextInt(100) > 40)
		{
		//  new Mudbomb(new RealPos(i.getX(),
//  			   i.getY()),
//  			   myStage(),
//  			   randy.nextInt(100),
//  			   randy.nextInt(360),
//  			   10);
		}
		else
		{
		    new Waterbomb(new RealPos(i.getX(),
					      i.getY()),
				  myStage(),
				  randy.nextInt(300),
				  randy.nextInt(360),
				  5);
		}
		if (i.getX() > 500) change = -1;
		if (i.getX() < 300) change = +1;

	}

	//changePosition(new RealPos(i.getX()+change,i.getY()));
    }
}

