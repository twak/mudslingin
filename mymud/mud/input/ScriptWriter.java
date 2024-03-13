/* $Id: ScriptWriter.java,v 1.4 2004/02/02 11:15:22 pk2692 Exp $ */

package mud.input;

import mud.*;

import java.util.*;

import java.awt.event.*;

import java.io.*;

import mud.trunk.Player;



public interface ScriptWriter

{

   public ArrayList getKeys(ArrayList k);


   public Player getPlayer();


   public void setPlayer(Player pr);


}
