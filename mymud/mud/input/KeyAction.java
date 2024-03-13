/* $Id: KeyAction.java,v 1.12 2004/03/05 02:29:11 tk1748 Exp $ */

package mud.input;


import mud.*;
import mud.trunk.*;
import mud.trunk.Player;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

//press the direction keys and recieve a text printout on the command line
//press the SPACE to printout the list of commands entered so far


   public class KeyAction implements KeyListener
   {      
       ArrayList al ;
       private KeyEvent mine;
       private Socket socket = null;

       /**
	  nextKey is the class that implements NextKey
	  that wants to know about all key input
       */
       private NextKey nextKey     = null;
       private boolean lock = false, stayLock = false;
       private boolean server = false;
       private int number = -1;

      public KeyAction()
      {
        al  = new ArrayList();
      }

      public void keyPressed(KeyEvent e) 
      {
	  mine = e;
	  if (!lock)
	  {
	      if (e.getKeyCode() == e.VK_ESCAPE)
	      {
		  System.err.println("Escape requested, quitting");
		  System.exit(-1);
	      }
	      else
	      {
		  al.add(e);
	      }
	  }            
	  else
	  {
	      /* For when we need to be notified
		 of key changes */
	      nextKey.nextKey(e);
	      if (!stayLock)
	      {
		  lock = false;
		  nextKey = null;
	      }
	  }

      }
 
      void printList()
      {
         int i = 0;
         while(i < al.size())
         { 
            System.out.println(al.get(i));
            i++;
         }

      }

       public ArrayList getKey()
       {
	   return al;
       }


      public void keyReleased(KeyEvent e) 
      {
	  if (!lock)
	  {
	      e.setKeyCode((-e.getKeyCode()));
	      al.add(e);
	  }
      }
 
      public void keyTyped(KeyEvent e) 
      {
      }

      void clearKeys()
      {
         al.clear();
      }

       /**
	  This is called when another system
	  want exclusive information on the
	  next key input
       */
       public void nextKey(NextKey nk)
       {
	   /* pretend to release the key
	    */
	   mine.setKeyCode((-mine.getKeyCode()));
	   al.add(mine);

	   lock = true;
	   nextKey = nk;
       }

       public void stayLock(NextKey nk)
       {
	   mine.setKeyCode((-mine.getKeyCode()));
	   al.add(mine);

	   lock = true;
	   stayLock = true;
	   nextKey = nk;
       }

       public void unLock()
       {
	   lock = false;
	   stayLock = false;
	   nextKey = null;
       }

       /**
	  sets the socket for the next game
	*/
       public void setSocket(Socket s)
       {
	   socket = s;
       }

       /**
	  Gets the socket for the next game
	*/
       public Socket getSocket()
       {
	   return socket;
       }

       /**
	  Sets the player number in the next game
	*/
       public void setNumber(int i)
       {
	   number = i;
       }

       /**
	  Gets the number that this player is
	*/
       public int getNumber()
       {
	   return number;
       }

       /**
	  Will this machine run the physics next
	  game?
	*/
       public void setServer(boolean s)
       {
	   server = s;
       }

       /**
	  if machine will act as a server in the next game
	*/
       public boolean isServer()
       {
	   return server;
       }
}

