/* $Id: StageChange.java,v 1.3 2003/11/04 18:39:17 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;
import java.util.ArrayList;

/**
   This keeps a list of the Changes made to
   the stage this turn. NOT FINISHED
   WAITING FOR NETWORKING & SPRITE INFORMATION
*/

public class StageChange
{
    private ArrayList changes;

    /**
    Constructs a new set of changes
    */
    public StageChange()
    {
	changes = new ArrayList();
    }

    /**
    Clears all changes for a new frame
    */
    public void newFrame()
    {
	changes = new ArrayList();
    }
    
    /**
    Adds a new change to our set
    @param change the change object
    */
    public void addChange(Change change)
    {
	changes.add(change);
    }

    /**
    Returns a list of Changes
    @return changes the list of changes this frame
    */
    public ArrayList getChanges()
    {
	return changes;
    }
}
