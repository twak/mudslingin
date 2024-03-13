// default port is 1500.
// this server handles only 1 connection.

package mud.input;
import java.net.*;
import java.io.*;
import java.util.*;

import mud.*;
import mud.trunk.Player;
import javax.swing.*;
import java.awt.event.*;

public class NetClient extends Thread
{
    int port;
    ObjectOutputStream output;
    ObjectInputStream input;
    Socket socket = null;
    Client client;
    private ArrayList data;

   public NetClient(Client cln, Socket s)
   {    
       socket = s;
       client = cln;
       data = new ArrayList ();
   }

   public synchronized void wakeUp(ArrayList al)
   {
	for (int i = 0; i < al.size(); i++)
	{
	   data.add(al.get(i));
	}
	
        notifyAll();
   }



   public synchronized void run()
   {
       ArrayList al = new ArrayList();

       while (true)
       {
          try
          {
              output = new ObjectOutputStream(socket.getOutputStream());
	       // input = new ObjectInputStream(socket.getInputStream());
	      output.writeObject(data);
	      if (al.size()!= 0) { System.err.println("Reading "+ al.size());}
	      //output.writeObject(keys);
	      //output.flush();
              //al = keys;
          }
          catch (IOException e)
          {
             System.out.println(e);
          }
	  data.clear();
	  try
	  {
	      wait();
	  }
	  catch (Exception e)
	  {
	      System.err.println("Cant sleep");
	      e.printStackTrace();
	      /* Do nothing */
	  }
	  }
   }


}
		

		
		
