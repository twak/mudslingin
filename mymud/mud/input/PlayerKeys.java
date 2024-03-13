/* $Id: PlayerKeys.java,v 1.4 2004/03/08 17:00:21 tk1748 Exp $ */
package mud.input;
import mud.*;
import mud.trunk.Player;

public class PlayerKeys
{

   private Player P;
   private int Key;


   public PlayerKeys(Player pr, int k)
   {
      P = pr;
      Key = k;  
   }

   public Player getPlayer()
   {
      return P;
   }

   public int getKey()
   {
      return Key;
   }

}
