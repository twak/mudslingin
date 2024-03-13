/* $Id: NetProp.java,v 1.4 2004/03/05 02:29:52 tk1748 Exp $ */
package mud.trunk;
import mud.supporting.*;
import java.util.ArrayList;

/**
   This is what is really on stage when u play a network game.
   Its main feature is a buffering system that guesses the next
   position of the prop from the last SIZE psoitions. This
   is so when the network gos balls up.
   <br><br>
   Algoritm used is that at the start of a frame we set the position
   of the prop to a guess based on the last positions. We store this
   position on top of our history guess, eg: If the network delivers a new
   position then it overwrites our guess in history[0].

   If there is a large jump in position then our values are deemed wrong
   and we flush the history.

   When we correct after a change, we add only a proportion of the new position
   to the old position, make it soo much smoother
 */
public class NetProp extends Prop
{

    /* How long into the past we remember */
    private static final int SIZE = 5;
    /* history of positions, index 0 is most recent */
    private RealPos[] history = new RealPos[SIZE];
    /* tolerance to similar positions - manhatten distance */
    private static final int TOL = 70;
    /* We can only move a maximum of: when guessing */
    private static final int MAX_MOVE = 100; 
    /* When re-correcting, we add this proportion of the old position
       back in - lower is smoother+wronger*/
    private static final double PROP = 0.2;

    /** 
    Constructor to set the positions
    in one swoop
    @param rp the position of the player
    @param s Stage that this player is on
    @param a sprite folder
    @param b sprite sprite
    */
    public NetProp(RealPos rp, Stage s, int a, int b)
    {
	/*initialise history */
	flushHistory();
	/* This just loads the default sprite now */
	setUpProp(rp, s, s.getSpriteLoader().getSprite(a,b));
	history[0] = rp;
    }

    /**
       This guesses the position for this prop based on
       last. This is taken as a count of how long one frame is
     */
    public void guessPosition()
    {
	RealPos rp = doGuess();
	/* Shuft it all down one */
	for (int i = SIZE-1; i > 0; i--)
	{
	    history[i] = history[i-1];
	}
	history[0] = rp;
	//rp = new RealPos(0,0);
	changePosition(rp);
    }

    /**
       This is called to change a props position
       @param rp the position just recieved over the network
     */
    public void actualPosition(RealPos rp)
    {
	if (Math.abs(rp.getX()-history[0].getX()) > TOL ||
	    Math.abs(rp.getY()-history[0].getY()) > TOL)
	{
	    /* bad match, so flush the pipe */
	    flushHistory();
	}
	if (history[0]!=null)
	{
	    rp = new RealPos(PROP*rp.getX()+(1-PROP)*history[0].getX(),
	    	     PROP*rp.getY()+(1-PROP)*history[0].getY());
	}
	history[0] = rp;
	changePosition(rp);
    }

    /**
       Does the maths to guess the next position.
       Concentrate: Finds the 2nd derivative and
       uses it to predict the next position
     */
    private RealPos doGuess()
    {
	RealPos rp;

  	if (history[0] != null)
	{
	    if (history[1] != null)
	    {
		/* This is 2nd derivative code, even worse for some reason...*/
		//  if (history[2] != null)
//  		{
//  		    double xChange = history[1].getX()-history[0].getX();
//  		    double yChange = history[1].getY()-history[0].getY();
//  		    double xxChange = history[2].getX()-history[1].getX();
//  		    double yyChange = history[2].getY()-history[1].getY();
//  		    double dx = xxChange-xChange;
//  		    double dy = yyChange-yChange;

//  		    System.err.println(" "+(history[0].getX()-xChange+dx)+" "+history[0].getX()+" "+history[1].getX()+" "+history[2].getX()+"        "+xChange+"  "+dx);

//  		    rp = new RealPos(history[0].getX()-xChange+dx,
//  				     history[0].getY()-yChange+dy);
//  		}
//  		else
//  		{
		    double xChange = history[1].getX()-history[0].getX();
		    double yChange = history[1].getY()-history[0].getY();
		    /* Cap maximum movement */
		    if (Math.abs(xChange) > MAX_MOVE)
		    {
			if (xChange > 0)
			{
			    xChange = MAX_MOVE;
			}
			else
			{
			    xChange = -MAX_MOVE;
			}
		    }
		    if (Math.abs(yChange) > MAX_MOVE)
		    {
			if (yChange > 0)
			{
			    yChange = MAX_MOVE;
			}
			else
			{
			    yChange = -MAX_MOVE;
			}
		    }
		    //System.err.println(xChange+" "+history[1].getX()+" "+history[0].getX()+" "+(history[0].getX()-(xChange)));
		    rp = new RealPos(history[0].getX()-(xChange),
				     history[0].getY()-(yChange));
//  		}
	    }
	    else
	    {
		rp = history[0];
	    }
	}
	else
	{
	    /* Otherwise set position off edge EVENTUALLY */
	    rp = new RealPos(-100,-100);
	}
	return rp;
    }

    /**
       clears the history
     */
    private void flushHistory()
    {
	for (int i = 0; i < SIZE; i++)
	{
	    history[i] = null;
	}
    }
    

}
