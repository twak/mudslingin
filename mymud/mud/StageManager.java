/* $Id: StageManager.java,v 1.22 2004/05/02 15:04:38 tk1748 Exp $ */
package mud;

import mud.trunk.*;
import mud.supporting.*;
import mud.display.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/** 
Controls access to the stage by calling each specified
class implementing StageAccess's perform procedure, in
the order as given.
*/

public class StageManager extends Thread
{

    private Stage   ourStage     ;
    private Vector  toCall       ;
    private final static  boolean DEBUG = false;
    private static int FPS_SAMPLE = 60;
    private Timer timer;
    private ArrayList FPS = new ArrayList(FPS_SAMPLE);

    /**
    Creates a stage with a default attempted frame rate
    @param fr desired frame rate
    @param s  the stage to stage-manage
    */
    public StageManager (Stage s)
    {
	ourStage  = s ;
	toCall = new Vector(7);
	ourStage.getGameValue().quitNow = false;
    }

    public void stageManagerReset (Stage s)
    {
	ourStage  = s ;
	toCall = new Vector(7);
	ourStage.getGameValue().quitNow = false;
    }


    /**
    Adds the object to sequence to be run
    @param performer The StageAccess implementation to add
    */
    public void registerStageAccess(StageAccess performer)
    {
	toCall.add(performer);
    }

    /**
       this is here so we can extend thread, and can sleep easier.
     */
    public void run()
    {
    }


    /**
    Start calling each method in turn. This runs continuously
    until closeCurtains() is called
    */
    public synchronized void openCurtains()
    {
	long timeAtStart, timeTaken, timeAtObjectStart;
	double tL;
	for (int i = 0; i<= FPS_SAMPLE+1; i++) FPS.add(new Double(0));

	/* Calls each StageAccess' set up method */
	for (int i = 0; i < toCall.size(); i++)
	{
	    if (DEBUG) System.err.println(i+": "+toCall.elementAt(i).getClass().getName() +" going into makeUp()");
	    ((StageAccess)toCall.elementAt(i)).makeUp(ourStage);
	}
	timer = new Timer();
	timer.schedule(new RemindTask(),0,1000/ourStage.getGameValue().fps);
	try
	{
	    wait();
	}
	catch (Exception e)
	{
	    System.err.println("Master thread woken prematurely, quitting");
	    System.exit(-1);
	}
    }

    public synchronized void wakeUp()
    {
	notifyAll();
    }

    class RemindTask extends TimerTask
    {
	long timeAtStart, timeTaken, timeAtObjectStart;
	double tL;

	public synchronized void run()
	{
	/* Calls each StageAccess' main method */
	if (ourStage.getGameValue().quitNow == false)
	{
	    timeAtStart = System.currentTimeMillis();
	    
	    perform();

	    for (int i = 0; i < toCall.size(); i++)
	    {
		timeAtObjectStart = System.currentTimeMillis();
		/* Call the StageAccess to perform */
		((StageAccess)toCall.elementAt(i)).perform(ourStage);
		if (DEBUG) System.err.println(i+": "+toCall.elementAt(i).getClass().getName() +" took "+(System.currentTimeMillis()-timeAtObjectStart));
	    }

	    FPS.add(0, new Double (System.currentTimeMillis()));
	    /* Announce to the game state that we are starting a new frame */
	    ourStage.newFrame();	    
	}
	else
	{
	    /* Calls each StageAccess' shutdown method */
	    for (int i = 0; i < toCall.size(); i++)
	    {
		if (!(((StageAccess)toCall.elementAt(i)) instanceof Audience))
		{
		    ((StageAccess)toCall.elementAt(i)).goHome();
		}
	    }
	    if (DEBUG) System.err.println("Cutains Lowered");
	    timer.cancel();
	    /* Wake up the boss */
	    wakeUp();
	}
	}
    }

    /**
       Adds Stage Manager counters to the screen
     */
    private void perform()
    {
	if (ourStage.getGameValue().fpsCounter!=null)
	{
	    double total=((new Double(FPS_SAMPLE)).doubleValue())*1000/ 
		((new Double(System.currentTimeMillis()).doubleValue())-
		((Double)FPS.get(FPS_SAMPLE-1)).doubleValue());

	    String output = (new Double(total)).toString();
	    output = output.substring(0,Math.min(output.length(),4));
	    ourStage.getGameValue().fpsCounter.setString("FPS: "+ output,2);
	}
	    FPS.remove(FPS_SAMPLE-1);
    }

    /**
    Stop calling now. This is for when loading, and we can't wait
    for everything to finish.
    */
    public void closeCurtainsNow()
    {
	ourStage.getGameValue().quitNow = true;
	for (int i = 0; i < toCall.size(); i++)
	{
	    /* Call the StageAccess to perform */
	    ((StageAccess)toCall.elementAt(i)).goHome(); 
	}
	toCall = new Vector();
    }
}
