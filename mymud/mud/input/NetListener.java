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

public class NetListener extends Thread
{
    int port;
    ServerSocket server_socket;
    ObjectOutputStream output;
    ObjectInputStream input;
    Socket socket = null;
    Server server;

   public NetListener(Server ser, Socket s)
   {    
       server = ser;
       socket = s;
   }


    public void run()
    {
	ArrayList al = new ArrayList();
 
	try
	{
    
	    while (true)
	    {
		input = new ObjectInputStream(socket.getInputStream());
		// input = new ObjectInputStream(socket.getInputStream());
		al = (ArrayList) input.readObject();
		//if (al.size()!= 0) { System.err.println("Reading "+ al.size());}
		//output.writeObject(keys);
		//output.flush();
		//al = keys;
		server.giveData(al);
	    }	    
	}
	catch (ClassNotFoundException e) {
	    System.out.println(e);
	}
	catch (IOException e)
	{
	    System.out.println(e);
	}

    }
   


}
		

		
		
