/* $Id: MenuOptionInterface.java,v 1.1 2004/03/01 17:57:22 tk1748 Exp $ */
package mud.trunk;

import mud.*;
import mud.trunk.*;
import mud.supporting.*;
import mud.input.*;
import java.util.Vector;
import java.io.*;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

/**
   This just says that the class can call MenuOptionListener
 */
public interface MenuOptionInterface
{
    public void message(int i);
    public void text(String s);
}
