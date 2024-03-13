/* $Id: MultiPlayerGenre.java,v 1.6 2004/05/05 14:57:44 tk1748 Exp $ */
package mud.director;

import mud.trunk.*;
import mud.input.*;
import java.util.ArrayList;

/**
 Defines the game type "multi player" in terms of the events that
 happen per user keystroke, this happens on the server....
 */
public class MultiPlayerGenre implements Genre
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
	        /* We have recieved a string accross the netowrk */
		if (((String)inputs.get(i)).compareTo("hello fellow mudslinger")==0)s.getGameValue().netReady = true; else
		{
			System.err.println((String)inputs.get(i));
			new Speak(s,(Player)s.allPlayers().get(1), (String)inputs.get(i));
		}
	    }
	    else
	    {
	    current = (Player)((PlayerKeys)inputs.get(i)).getPlayer();
	    action = ((PlayerKeys)inputs.get(i)).getKey();

	    if (Math.abs(action)== Commands.AngleUp  ) current.onAimUp  (action > 0);
	    if (Math.abs(action)== Commands.AngleDown) current.onAimDown(action > 0);

	    if (action ==   Commands.FireUp) current.onStartFire()  ;
	    if (action ==  -Commands.FireUp) current.onEndFire();

	    if (action == Commands.WeaponNext    ) current.weaponChange( 1);
	    if (action == Commands.WeaponPrevious) current.weaponChange(-1);

// 	    if (action == Commands.Speak)
// 	    {
// 		/* Create a prop to talk */
// 		new Speak(s, current);
// 	    }

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
