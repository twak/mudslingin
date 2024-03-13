/* $Id: ScriptEditor.java,v 1.7 2004/03/08 18:05:38 tk1748 Exp $ */

package mud.input;

import mud.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import mud.trunk.Player;

public class ScriptEditor
{

   private ArrayList Scripts;
   private KeyAction K = new KeyAction();
   String speech = null;

   //Constructor method

   public ScriptEditor(KeyAction Ks)
   {
      K = Ks;
      Scripts = new ArrayList();
   }



   // Add each new ScripWriter to the Scripts hash table

   public void addScriptWriter(ScriptWriter s)
   {
      Scripts.add(s);
   }


   // Return the key inputs from all the players in the last thirtieth of a second
   public ArrayList getKeys()
   {
      int i = 0, j = 0;
      ArrayList a = new ArrayList();
      ArrayList ky;
      ScriptWriter sw;     

      while(i < Scripts.size())
      {
         sw = (ScriptWriter)(Scripts.get(i));
         if (sw instanceof ServerScriptWriter)
	 {
	     if (((ServerScriptWriter)sw).getISP() instanceof Client && speech != null)
	     {
	        ((ServerScriptWriter)sw).setText(speech);
	        speech = null;
	     }
	 }
         ky = sw.getKeys(K.getKey());
         for (j = 0; j < ky.size(); j++)
         {
            a.add(ky.get(j));
         }
         i++;
      }

      K.clearKeys();
      return a;
   }

   public void addText(String s)
   {
      speech = s;
   }
}










