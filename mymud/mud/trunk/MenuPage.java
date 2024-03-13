/* $Id: MenuPage.java,v 1.6 2004/04/14 12:16:44 tk1748 Exp $ */
package mud.trunk;

import mud.trunk.*;
import mud.supporting.*;
import java.util.Vector;
import java.io.*;
/**
   While not stricly a prop at the moment this may become one
   as it represents a group of menu items, so we may draw a menu 
   or something
 */
public class MenuPage
{
    
    protected Vector items;
    protected int selected;
    protected Stage myStage;

    /**
       creates a new menupage
     */
    public MenuPage ()
    {
	items = new Vector(20);
	selected = -1;
    }

    /**
       If extending classes need a stage
       refernce
       @param s current stage
     */
    public void setUp(Stage s)
    {
	myStage = s;
    }

    /**
       Adds an item to this page and re-centers the 
       rest of the items on the page
       @param m the item to add
     */
    public void add(MenuItem m)
    {
	/* We are not a prop, so have to leaver
	   stage access from a prop */
	myStage = m.myStage();
	items.add(m);
	positionAll();

	/* If this is the top-but-one item so its selected */
	if (items.size()==2)
	{
	    selected = 1;
	    getSelected().onEnter();
	}
    }

    /**
       Removes an item to this page and re-centers the 
       rest of the items on the page
       @param m the item to add
     */
    public void remove(MenuItem m)
    {
	/* We are not a prop, so have to leaver
	   stage access from a prop */
	myStage = m.myStage();
	m.finalCurtain();
	items.remove(m);
	positionAll();

	/* If this is the top-but-one item so its selected */
	if (items.size()==2)
	{
	    selected = 1;
	    getSelected().onEnter();
	}
    }


    /**
       Returns a list of the menu items/"options"
       avaiable to the <drug> user from this
       page
    */
    public Vector getOptions()
    {
	return items;
    }

    /**
       Re-positions all the items so they have
       equal size
     */
    private void positionAll()
    {
	double screenW  = myStage.getGameValue().mapSizeX;
	double screenH  = myStage.getGameValue().mapSizeY;
	double interval = screenH/(items.size()+1);

	double i = interval;

	for (int j = 0; j < items.size(); j++)
	{
	    ((MenuItem)items.get(j)).setMiddle(new 
		                   RealPos(screenW/2,i)
                                                       );
	    i+=interval;
	}
	
    }

    /**
       Returns the currently selected menuItem
       @return m the selected item
     */
    public MenuItem getSelected()
    {
	//if (selected == -1) return null;
	return (MenuItem)items.get(selected);
    }

    /**
    Sets the next item as selected
     */
    public void selectNext()
    {
	getSelected().onLeave();
	selected++;

	/* menu's cant be selected */
	if ((MenuItem)items.get(roundSelect(selected)) 
                                instanceof MenuTitle )
	{
	    selected=roundSelect(selected)+1;
	}
	selected = roundSelect(selected);
	getSelected().onEnter();
    }

    /**
       sets the previous item in the list
       as selected
    */
    public void selectPrevious()
    {
	getSelected().onLeave();
	selected--;

	/* menu's cant be selected */
	if ((MenuItem)items.get(roundSelect(selected)) 
                                 instanceof MenuTitle )
	{
	    selected=roundSelect(selected)-1;
	}
	selected = roundSelect(selected);
	getSelected().onEnter();
    }

    /**
       Tells the menu item that it has been activted
       @return the next menu code
     */
    public int activate()
    {
	return getSelected().onActivate();
    }

    /**
       Forces all the props to leave the stage and
       go home. Probably to go on holiday somewhere 
       very warm. With exotic women. But with a stage-
       hand's wage they should be so lucky.
     */
    public void leaveStage()
    {
	for (int j = 0; j < items.size(); j++)
	{
	    ((MenuItem)items.get(j)).finalCurtain();
	    ((MenuItem)items.get(j)).setOnStage(false);
	}
    }

    /**
       removes items from this menu and the stage
     */
    public void clearPage()
    {
	leaveStage();
	items.clear();
    }

    /**
       Re-hires all the menu props to go back
       on to the
       @param stage to work
     */
    public void enterStage(Stage s)
    {
	s.playSound(0,1);
	for (int j = 0; j < items.size(); j++)
	{
	    ((MenuItem)items.get(j)).backOnStage(s);
	}
	positionAll();
    }

    /**
       Rounds the selected value to that of a item
       that exists
     */
    private int roundSelect(int input)
    {
	if (input >= items.size())
	{
	    return 0;
	}
	else if (input < 0)
	{
	    return items.size() -1;
	}
	else
	{
	    return input;
	}
    }

    /** 
       We are not a prop, so this is not called except in
       very special (hacked) circumstances
    */
    public void doPhysics()
    {

    }
}
