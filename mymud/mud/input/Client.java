/* $Id: Client.java,v 1.11 2004/03/08 18:05:38 tk1748 Exp $ */


// default port is 1500

package mud.input;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

import mud.*;
import mud.trunk.Player;

public class Client implements ISP
{


   int port = 15000;
   Socket socket = null;
   
   String lineToBeSent;
   byte ipAddress[] = new byte[4];
   ArrayList newList;
   NetClient nc;

   public Client(Socket s)
   {
      socket = s;
      newList = new ArrayList();
      nc = new NetClient(this, s);
      nc.start();
   }


   public ArrayList getKey(ArrayList keys)
   {

      ArrayList al = new ArrayList();

	for (int i = 0; i< keys.size(); i++)
	{
	    newList.add(keys.get(i));
	    al.add(keys.get(i));
	}   

	nc.wakeUp(newList);
	newList.clear();
	
      return al;

   }

   void clearNewList()
   {
      newList.clear();
   }
}
