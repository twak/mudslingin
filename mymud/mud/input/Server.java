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

public class Server implements ISP
{
    
   int port;
   ServerSocket server_socket;
   ObjectOutputStream output;
   ObjectInputStream input;
   Socket socket = null;
    ArrayList newList;
	
   public Server(Socket s)
   {
      port = 15000;

      newList = new ArrayList(1);

      NetListener n = new NetListener(this, s);

      n.start();
   }

    public void giveData(ArrayList al)
    {
	for (int i = 0; i< al.size(); i++)
	{
	    newList.add(al.get(i));
	}
    }
    
    public ArrayList getKey(ArrayList keys)
    {
	ArrayList al = new ArrayList(1);
	
	for (int i = 0; i< newList.size(); i++)
	{
	    al.add(newList.get(i));
	}
	newList.clear();
        return al;
    }
}
		

		
		
