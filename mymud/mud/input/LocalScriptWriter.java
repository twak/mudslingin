/* $Id: LocalScriptWriter.java,v 1.8 2004/04/27 13:46:08 tk1748 Exp $ */


package mud.input;

import mud.*;
import mud.trunk.Player;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class LocalScriptWriter implements ScriptWriter
{

   private ArrayList KeySet;
   Player Play;
   Commands CommandKey;
   PlayerKeys playKeys, lastPushed;

   public LocalScriptWriter(Player p)
   {
      Play = p;
      CommandKey = new Commands(p);
      lastPushed = null;
   }


   //Add the ID of the key input to the KeySet

   public ArrayList getKeys(ArrayList  k)
   {
      KeySet = new ArrayList();

      if (lastPushed != null)
      {
	      KeySet.add(lastPushed);
      } 

      KeyEvent e;

      for(int i = 0; i < k.size() ; i++)
      {
         e = (KeyEvent)(k.get(i));

         for ( int j = 0 ; j < CommandKey.NO_COMMS ; j++)
         {
            if ((e.getKeyCode() == CommandKey.ComKey[j])&&(CommandKey.Released[j]))
            {
               playKeys = new PlayerKeys(Play, CommandKey.Com[j]);
               KeySet = addIt(KeySet,playKeys);////KeySet.add(playKeys);
               CommandKey.Released[j] = false;
            }
            else if (e.getKeyCode() == (-CommandKey.ComKey[j]))
            {
               playKeys = new PlayerKeys(Play, (-CommandKey.Com[j]));
	       KeySet = addIt(KeySet,playKeys);
               //KeySet.add(playKeys);
               CommandKey.Released[j] = true;
            }
         }

      }
 
      if (KeySet.size() == 1 && lastPushed != null)
      {
	      lastPushed = (PlayerKeys) KeySet.get(KeySet.size()-1);
	      KeySet.remove (KeySet.size()-1);
      }
      else
      {
	      lastPushed = null;
      }
      return KeySet;
   }


	public ArrayList addIt(ArrayList l, PlayerKeys o)
	{

		for (int i = 0; i < l.size(); i++)
		{
		    /* The o.getKey() > 0 bit is new here */
			if (o.getKey() == -((PlayerKeys)l.get(i)).getKey() && o.getKey() > 0)
			{
				l.remove(i);
				return l;
			}
		}
		l.add(o);
		return l;
	}

   public void setKeys(int cmnd, int Vkeys)
   {

      for ( int i = 0 ; i < CommandKey.NO_COMMS ; i++)
      {
         if (cmnd == CommandKey.Com[i])
         {
            CommandKey.ComKey[i] = Vkeys;
         }
      }

   }


   void clearKeys()
   {
      KeySet.clear();
   }
  
   public Player getPlayer()
   {
      return Play;
   }

   public void setPlayer(Player pr)
   {
      Play = pr;
   }
}
