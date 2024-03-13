


package mud.input;

import mud.*;
import mud.trunk.Player;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class ServerScriptWriter implements ScriptWriter
{

   private ArrayList KeySet;
   Player Play;
   Commands CommandKey;
   PlayerKeys playKeys, lastPushed;
   ISP Network;
   String speech = null;

   public ServerScriptWriter(Player p, ISP netwrk)
   {
      Play = p;
      CommandKey = new Commands(p);
      lastPushed = null;
      Network = netwrk;
   }

   /**
	Tells us if were a server or client
	@return the ISP this SSW was made with
    */
   public ISP getISP()
   {
	return Network;
   }


   public void setText(String s)
   {
      if (Network instanceof Client)
      {
	  speech = s;
      }
      else
      {
	System.err.println("Invalid call to setTect in SSW");
      }
   }

   //Add the ID of the key input to the KeySet

   public ArrayList getKeys(ArrayList  k)
   {
      KeySet = new ArrayList();

      if ( speech != null)
      {
         k.add(speech);
         speech = null;
      }

      k = Network.getKey(k);
      //if (k.size() != 0) { System.err.println("receiving " + k.size());}
      if (lastPushed != null)
      {
	      KeySet.add(lastPushed);
      } 

      KeyEvent e;

      for(int i = 0; i < k.size() ; i++)
      {
         if (k.get(i) instanceof String)
	 {
            speech = (String)k.get(i);
         }
        else
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

      if (speech != null)
      {
	  KeySet.add(speech);
	  speech = null;
      }

      return KeySet;

   }


	public ArrayList addIt(ArrayList l, PlayerKeys o)
	{

		for (int i = 0; i < l.size(); i++)
		{
			if (o.getKey() == -((PlayerKeys)l.get(i)).getKey())
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
