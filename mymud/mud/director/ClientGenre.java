/* $Id: ClientGenre.java,v 1.3 2004/05/05 14:57:44 tk1748 Exp $ */
package mud.director;

import mud.trunk.*;
import mud.input.*;
import java.util.ArrayList;

/**
 Defines the game type "multi player" in terms of the events that
 happen per user keystroke, this is for the CLIENT
 */
public class ClientGenre implements Genre
{
    /**
    The events to happen to stage s on commands keys
    @param s stage
    @param keys keystrokes
     */
    public int events(Stage s, ArrayList inputs)
    {
	Player current;
	int action;

	for (int i = 0; i < inputs.size(); i++)
	{
	    if (inputs.get(i) instanceof String)
	    {
	    }
	    else
	    {
		    current = (Player)((PlayerKeys)inputs.get(i)).getPlayer();
		    action = ((PlayerKeys)inputs.get(i)).getKey();
		    /* Client wants to speak */
// 		    if (action == Commands.Speak)
// 		    {
// 			/* Create a prop to talk, but not on stage */
// 			new Speak(s, current, 0);
// 		    }
	
		    /**
		       We load the welcome screen as this
		       is multiplayer land
		     */
		    if (action == Commands.Menu) 
		    {
			s.getDirector().networkError();
		    }
	     }
	    
	}
	return Director.NORM;
    }

}
