/* $Id: Mudsling.java,v 1.7 2004/03/04 12:47:33 tk1748 Exp $ */
package mud;

import java.applet.Applet;
import mud.trunk.*;

/**
   This is the calling class that constructs a new game
   common proxy options for uob  "wwwcache.bris.ac.uk" "8080" 

 */
public class Mudsling
{

    /**
    calls the Executive with the specified settings file
    @param args the filename is in args[0]
    */
    public static void main (String[] args)
    {
	/* Short & quick hack for now, later these will be options */
	System.getProperties().put( "proxySet", "true" );
	System.getProperties().put( "proxyHost", "wwwcache.bris.ac.uk" );
	System.getProperties().put( "proxyPort", "8080" );

	if (args.length == 0)
	{
	    System.out.println("Starting Default Mudslingin'....");
	    Executive showOnTheRoad = new Executive("data/welcome.mud");
	} else if (args.length == 1)
	{
	    System.out.println("Starting Mudslingin' with \""+args[0]+"\"");
	    Executive showOnTheRoad = new Executive(args[0]);
	}
	else
	{
	    System.err.println("Usage: java Mudsling");
	    System.err.println("   or: java Mudsling filename");
	}
	System.exit(0);
    }
}
