/* $Id: ShortPipe.java,v 1.2 2004/03/12 15:12:39 tk1748 Exp $ */
package mud.supporting;

import java.util.*;

/**
   A pipe for shorts. This has nothing to do
   with shoving dwarves down tubes.
 */
public class ShortPipe
{
    ArrayList myBuffer = new ArrayList(100);
	
    /**
       Adds a short to the pipe
       @param o the short to add
     */
    public synchronized void write(Short s)
    {
	//System.err.print("+");
	myBuffer.add(s);
    }

    /**
       reads from the pipe
       @return the next short in the pipe
     */
    public synchronized Short read()
    {
	if (myBuffer.size() > 0) 
	{
	    //System.err.print("-");
	    return (Short)myBuffer.remove(0);
	}
	else
	{
	    return null;
	}
    }

    public int size()
    {
	return myBuffer.size();
    }
}

