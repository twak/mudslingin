/* $Id: ServerConnect.java,v 1.2 2004/03/04 15:57:43 tk1748 Exp $ */

package mud.broadcast;

import mud.*;
import mud.supporting.*;
import mud.broadcast.*;
import mud.trunk.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
   Thread to connect to client from server (we are the client)
 */

public class ServerConnect extends Thread
{

    byte[] addr;
    int port;
    MenuClientItem MCI;
    Socket gameSocket;

    public ServerConnect(byte[] ia, int p, MenuClientItem mci)
    {
	addr = ia;
	port = p;
	MCI = mci;
    }

    public void run()
    {
	InetAddress ia;

	try
	{
	    ia = InetAddress.getByAddress(addr);
	    gameSocket = new Socket(ia, port);
	    gameSocket.setTcpNoDelay(true);
	    MCI.success(gameSocket);
	}
	catch (UnknownHostException e)
	{
	    MCI.error(1);
	    System.out.println(e);
	}
	catch (IOException e)
	{	    
	    MCI.error(2);
	    System.out.println(e);
	}
	
    }
}
