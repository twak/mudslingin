/* $Id: Commands.java,v 1.8 2004/02/14 17:21:22 tk1748 Exp $ */


package mud.input;
import mud.trunk.Player;

public class Commands
{

   private Player Pr;
   public static int NO_COMMS = 8;


   static int[] Com = new int[NO_COMMS];
   int[]  ComKey = new int[NO_COMMS];
   boolean[] Released = new boolean[NO_COMMS];

   public static int FireUp = 1;
   public static int AngleUp = 10;
   public static int AngleDown = 11;
   public static int WeaponNext = 12;
   public static int WeaponPrevious = 13;
   public static int Speak = 15;
   public static int Menu = 20;
   public static int Quit = 100;

   Commands(Player p)
   {
      Pr = p;
      Com[0] = Menu;
      Com[1] = FireUp;
      Com[2] = AngleUp;
      Com[3] = AngleDown;
      Com[4] = WeaponNext;
      Com[5] = WeaponPrevious; 
      Com[6] = Quit;
      Com[7] = Speak;

      for (int i = 0 ; i < NO_COMMS; i++)
      {
         Released[i] = true;
      }
     
   } 

   Player getPlayer()
   {
      return Pr;
   }
}
