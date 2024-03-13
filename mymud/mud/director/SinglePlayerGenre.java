/* $Id: SinglePlayerGenre.java,v 1.4 2004/03/04 15:57:47 tk1748 Exp $ */
package mud.director;

import mud.trunk.*;
import mud.input.*;
import java.util.ArrayList;

/**
 Defines the game type "single player" in terms of the events that
 happen per user keystroke
 */
public class SinglePlayerGenre implements Genre
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
	    current = (Player)((PlayerKeys)inputs.get(i)).getPlayer();
	    action = ((PlayerKeys)inputs.get(i)).getKey();

	    if (Math.abs(action)== Commands.AngleUp  ) current.onAimUp  (action > 0);
	    if (Math.abs(action)== Commands.AngleDown) current.onAimDown(action > 0);

	    if (action ==   Commands.FireUp) current.onStartFire()  ;
	    if (action ==  -Commands.FireUp) current.onEndFire();

	    if (action == Commands.WeaponNext    ) current.weaponChange( 1);
	    if (action == Commands.WeaponPrevious) current.weaponChange(-1);

	    if (action == Commands.Menu) 
	    {
		current.onEndFire();
		return Director.MENU;
	    }
	}


	return Director.NORM;
    }

}
