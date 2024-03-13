/* $Id: Scene.java,v 1.25 2004/04/01 14:04:16 tk2550 Exp $ */
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
import javax.imageio.ImageIO;
import java.io.FileInputStream;

/**
The Scene class opens the window
*/

public class Scene extends JFrame
{
   private static GraphicsDevice device;
   private SceneSetter setter;
   private boolean trans = false;

   private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[]
   {
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 8, 0)
   };
   
   /**
   Creates a new Scene
   &param myKeyAction the KeyAction that Scene will use
   */

   Scene(KeyAction myKeyAction, Audience a, boolean fullScreen)
   {
       GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
       device = env.getDefaultScreenDevice();
       
       Image output = null;
       
       try
	   {
	       output = ImageIO.read(new FileInputStream("data/logo.png"));
	   }
	   catch (java.io.FileNotFoundException e)
	   {	System.err.println("data/logo.png not found");
	   }
	   catch (java.io.IOException e)
	   {	System.err.println("data/logo.png contains error");
	   }
	   
       setTitle("Mudslingin");
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       setUndecorated(true);
       setIgnoreRepaint(true);
       setIconImage(output);
       setFocusable(true);

       if (fullScreen)
	   {	
	   	setResizable(true);
	   	device.setFullScreenWindow(this);
	   		
       		if (device.isDisplayChangeSupported())
 	   	{   chooseBestDisplayMode();
           	    trans = false;
       		}
       		else
       		{   trans = true;
       		}
	   }
	   else
	   {	
       		setSize(800,600);
       		setResizable(false);
       		setLocationRelativeTo(null);
	   }
       
       addKeyListener(myKeyAction);

       setter = new SceneSetter(trans,a);
       getContentPane().add(setter);
       show();

       SwingUtilities.invokeLater( new Runnable() {

            public void run()
            {
                repaint();
            }
        });

   }
   
   /**
   Redraws the entire screen
   @param allProps the ArrayList of props to be drawn
   @param mf the MudFloor to be drawn
   @param mapX the width of the map
   @param mapY the height of the map
   */
    void redraw(ArrayList allProps, MudFloorInterface mf, int mapX, int mapY, Stage stage)
   {   
       if (trans)
       {   int originX = (int) (device.getDisplayMode().getWidth() - mapX)/2;
           int originY = (int) (device.getDisplayMode().getHeight() - mapY)/2;
           setter.redraw(allProps,mf,originX,originY, stage);
       }
       else
	   setter.redraw(allProps,mf,0,0, stage);
   }
   
   /**
   Close the window
   */
   void close()
   {
       device.setFullScreenWindow(null);
   }

   /**
   Set the best display mode available on the default output
   */
   public static void chooseBestDisplayMode() {
       DisplayMode best = getBestDisplayMode();
       if (best != null) {
          device.setDisplayMode(best);
       }
    } 

   /**
   Choose the best display mode avilable from BEST_DISPLAY_MODES
   */
   private static DisplayMode getBestDisplayMode() {
       for (int x = 0; x < BEST_DISPLAY_MODES.length; x++) {
           DisplayMode[] modes = device.getDisplayModes();
           for (int i = 0; i < modes.length; i++) {
               if (modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth()
                  && modes[i].getHeight() == BEST_DISPLAY_MODES[x].getHeight()
                  && modes[i].getBitDepth() == BEST_DISPLAY_MODES[x].getBitDepth()
                  ) {
                   return BEST_DISPLAY_MODES[x];
               }
           }
       }
       return null;
    }
    
    
}
