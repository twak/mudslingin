/* $Id: StageAccess.java,v 1.2 2003/11/04 18:39:01 tk1748 Exp $ */
package mud;

import mud.trunk.*;

/**
   All classes requiring access to the Stage
   Object must implement this interface
*/
public interface StageAccess
{
    /**
    Called once, make yourself and the
    Stage ready to begin, start auxillary
    data structures and threads

    @param s The Stage to be used
     */
    public void makeUp(Stage s);


    /**
    Called each frame, make changes to
    the stage here.
    @param s The Stage to be used
     */
    public void perform(Stage s);

    /**
    Called when this use of the Stage
    terminated. Kill all axuillary threads/
    data strucs here
     */
    public void goHome();
}
