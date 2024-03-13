/* $Id: Audience.java,v 1.15 2004/02/12 13:27:12 tk2550 Exp $ */
package mud.display;

import mud.*;
import mud.trunk.*;
import mud.supporting.*;
import mud.input.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
The Audience class handles graphics output
*/

public class Audience extends Thread implements StageAccess
{
    private ArrayList allProps;
    private Scene scene;
    private KeyAction myKeyAction;
    private Stage myStage;

    /**
    Creates a new Audience
    @param k The KeyAction that the Audience will use
    */
    public Audience(KeyAction k, boolean fullScreen)
    {
	   System.out.println("Loading...");
	   myKeyAction = k;
	   scene = new Scene(myKeyAction, this, fullScreen);
    }
 
    /**
    The audience prepares its method here
    @param s The stage the audience is using
    */
    public void makeUp(Stage s)
    {
	   myStage = s;
	   allProps = s.allProps();
	   /* Redraw all */
	   //s.checkLighting();
	   scene.addWindowListener(new WindowEventDemo());
    }

    /**
    The audience runs this every frame
    @param s The stage the audience is using
    */
    synchronized public void perform(Stage s)
    {
       myStage = s;
       allProps  = s.allProps();
       int mapX = s.getGameValue().mapSizeX;
       int mapY = s.getGameValue().mapSizeY;
       scene.redraw(allProps,s.getMudFloor(),mapX,mapY,s);
       try 
       {
	   wait();
       } catch (InterruptedException e)
       {
       }

    }

    synchronized public void goodMorning()
    {
	notifyAll();
    }

    /**
    A method for outputting a list of all the props
    and their co-ordinates, for use as a debugging tool
    */
    private void textOutput()
    {  
	int n = 0;
	Prop prop;
	IntPos position;

	while (n != allProps.size())
	{
	    prop = (Prop) allProps.get(n);
	    position = new IntPos(prop.getPosition());
	    System.out.println("Prop no: "+ n +" is at ("+ position.getX() +","+position.getY() +")" );
	    n++;
	 }
    }
  
    /**
    This is called when the audience is no longer
    needed when the game is exiting
    */
    public void goHome()
    {
       scene.close();
    }
    
    public class WindowEventDemo extends WindowAdapter
    {

    	public WindowEventDemo()
    	{
        }
        
        public void windowDeiconified(WindowEvent e) {
        	if (myStage != null)
    		{	myStage.checkLighting();
    			allProps  = myStage.allProps();
       			int mapX = myStage.getGameValue().mapSizeX;
       			int mapY = myStage.getGameValue().mapSizeY;
       			scene.redraw(allProps,myStage.getMudFloor(),mapX,mapY,myStage);
       		}
    	}
    	
    	public void windowClosing(WindowEvent e) {
        	System.out.println("WindowListener method called: windowClosing.");
			if (myStage != null)
        	{	myStage.getGameValue().quitNow = true;
        	}
        }

    	
    	public void windowClosed(WindowEvent e) {
        	System.out.println("WindowListener method called: windowClosed.");
        	if (myStage != null)
        	{	myStage.getGameValue().quitNow = true;
        	}
    	}
    }    
    
}



