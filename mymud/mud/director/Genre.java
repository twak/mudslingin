/* $Id: Genre.java,v 1.1 2004/01/27 20:10:36 tk1748 Exp $ */
package mud.director;

import mud.trunk.Stage;
import java.util.ArrayList;

/**
 This is the template for a game type, that defines how a game
 progresses
*/

public interface Genre
{
    /**
    This is called once per frame. It should define the events
    for the specified user input
    @param s the current stage to manipulate
    @param keys the keystrokes this turn
    */
    public int events(Stage s, ArrayList keys);
}
