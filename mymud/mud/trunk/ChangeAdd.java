/* $Id: ChangeAdd.java,v 1.2 2003/11/04 18:39:17 tk1748 Exp $ */
package mud.trunk;

import mud.supporting.*;

/**
Specifies that a new prop has been added to the
Stage since last Frame
*/

public class ChangeAdd implements Change
{
    private Prop p;

    /* Specifies that a new prop
    has been added to the Stage
    @param newp New prop to add
    */
    public ChangeAdd(Prop newp)
    {
	p = newp;
    }

    /** 
    returns a pointer to
    the new Prop 
    @return p the new prop
    */
    public Prop getProp()
    {
	return p;
    }
}
