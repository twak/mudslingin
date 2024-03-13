/* $Id: NextKey.java,v 1.1 2004/02/14 13:05:07 tk1748 Exp $ */
package mud.trunk;

import mud.trunk.*;
import java.awt.event.KeyEvent;


/**
  Hard lock out of other listeners from the
  KeyListender
*/
public interface NextKey
{
    /**
       The next key has been pushed
       @param e the key pushed
     */
    public void nextKey(KeyEvent e);

}
