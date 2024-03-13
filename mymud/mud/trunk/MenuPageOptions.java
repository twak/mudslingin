/* $Id: MenuPageOptions.java,v 1.13 2004/04/27 16:30:05 tk1748 Exp $ */
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
   Offers the option of saving perminatant options to disk
   Should use fancy hoodicky to get correct key for input.
   This is all a bit of a botch of methods and hacks, but
   was the easiest way to stick it all together.
 */
public class MenuPageOptions extends MenuPage implements MenuOptionInterface
{
    /* Executive*/
    private Executive executive;
    private static final String FILE = "data/settings.mud";
    /* Set up the defaults for the boolean options */
    protected boolean stats  = false, 
                      print  = false, 
                      full   = false, 
                      back   = true  ;

    private int proxyPort = 0;
    private String proxyHost = "";
    private int gamePort  = 15000;
    private boolean newFile = false;

    /* For the perl server */
    private String twakServer = "http://www.twak.co.uk/cgi-bin/mudsling/";

    /* These two for the boolean options */
    protected      boolean[]bools = {false,false,false,true,true};
    protected      String[] names = {"showStats" ,
                                     "showPrints",
                                     "fullScreen",
                                     "bitmapBackground",
                                     "sound"};

    protected      int[]    intVals = {60,3};
    protected      String[] intNames = {"fps", "numberOfClouds"};

    private static String[] vkNames;
    private static int[]    vkValues;
    private        int      playerNumber = -1;

    /* Data storage for keys */
    private ArrayList ksPlayer    = new ArrayList(), 
	              ksVK        = new ArrayList(), 
                      ksCommand   = new ArrayList(),
                      ksComString = new ArrayList();
    private KeyAction myKeyAction;

    /**
       creates a new menupage
     */
    public MenuPageOptions (Executive e)
    {
	executive = e;
	myKeyAction = e.getKeyAction();
	if (bools.length != names.length)
	{
	    System.err.println("MenuPageOptions not set up right");
	    System.exit(-1);
	}
    }

    /**
       Called when we are about to enter the stage
       @param s the stage we are being added too 
     */
    public void setUp(Stage s)
    {
	/* set up vk values */
	getVKs();
	/* reset command list */
	ksPlayer = new ArrayList(); 
	ksVK = new ArrayList(); 
	ksCommand = new ArrayList(); 
	ksComString = new ArrayList();
	playerNumber = -1;
	/* set stage, read in data, add menu items */
	myStage = s;
	readFile();
	clearPage();
	makeOptions();
    }

    /**
       Creates a menu from a set of saved files
     */
    private void makeOptions()
    {
	clearPage();

	new MenuTitle (this,myStage,"Options menu");
	/* Boolean options */
	for (int i = 0; i < bools.length; i++)
	{
	    new MenuOptionBool (this,myStage,names[i],bools[i],i);
	}
	/* different integer options */
	new MenuOptionInt     (this,myStage,"speed",0,10,6,10,intVals[0]);
	new MenuOptionInt     (this,myStage,"number of clouds",1,1,5,1,intVals[1]);
	/* 100 is the maximum length of the proxy */
	new MenuText          (this,myStage, proxyHost,"proxy host:",100);
	new MenuOptionNum     (this,myStage,"proxy port: ",new Integer (proxyPort).toString());
	new MenuOptionNum     (this,myStage,"game port: ",new Integer (gamePort).toString());
	new MenuOptionListener(this,myStage,"set Keys",1);

	/* Save and quits */
	new MenuOptionSave(this,myStage,8);
	new MenuNavigator (this,myStage,"don't save settings",8);
    }

    private void keysMenu()
    {
	clearPage();

	int p = -1;
	for (int i = 0; i < ksPlayer.size(); i++)
	{
	    if (((Integer)ksPlayer.get(i)).intValue() != p)
	    {
		new MenuTitle (this,myStage,"Player"+(Integer)ksPlayer.get(i));
		p = ((Integer)ksPlayer.get(i)).intValue();
	    }
	    new MenuOptionKey(this,myStage,myKeyAction,(String)ksComString.get(i),((Integer)ksVK.get(i)).intValue(),i);
	}

	new MenuOptionListener(this,myStage,"back to options, remember to save",2);
    }

    private void getVKs() 
    {
	try
	{
	    Class c = Class.forName("java.awt.event.KeyEvent");
	    Field[] publicFields = c.getDeclaredFields();
	    vkNames   = new String[publicFields.length];
	    vkValues  = new int[publicFields.length];
	    for (int i = 0; i < publicFields.length; i++) {
		String fieldName = publicFields[i].getName();
		Class typeClass = publicFields[i].getType();
		vkNames[i] = fieldName;
		if (typeClass.toString().compareTo("int")==0 && i < 193)
		{
		    vkValues[i] = publicFields[i].getInt(null);
		}    
	    }
	}
	catch (Exception e)
	{
	    System.err.println("Error in MenuPageOptions reflective code");
	    e.printStackTrace();
	}
    }

    /**
      This lets us create a settings file if it does not exist
     */
    public void createNew()
    {
	try
	{
	    newFile = true;
	    writeOut();
	}
	catch (IOException e)
	{
	    System.err.println("Error creating settings file");
	    System.exit(-1);
	}
    }


    /**
       Outputs our data when we save to disk
     */
    protected void writeOut() throws IOException
    {
	System.err.println("Writing out \""+FILE+"\"");
	File outputFile = new File(FILE);
        FileWriter out = new FileWriter(outputFile);
	out.write("* Auto generated settings file *\n");
	for (int i = 0; i < intVals.length; i++)
	{
	    out.write(intNames[i]+"  "+intVals[i]+"\n");
	}
	/* Booleans section */
	for (int i = 0; i < bools.length; i++)
	{
	    if (bools[i])
	    {
		out.write(names[i]+"    true\n");
	    }
	    else
	    {
		out.write(names[i]+"    false\n");
	    }
	}
	out.write("setProxyHost    "+proxyHost+"\n");
	out.write("setProxyPort    "+proxyPort+"\n");
	out.write("setGamePort     "+gamePort+"\n");

	if (proxyPort != 0)
	{
            /* Also need to set values here, no reload between here &usgae */
	    System.getProperties().put( "proxySet", "true" );
	    System.getProperties().put( "proxyHost", proxyHost );
	    System.getProperties().put( "proxyPort", new Integer(proxyPort).toString() );
	}
	else
	{
	    /* Dont do proxy */
	    System.getProperties().put( "proxySet", "false" );
	}

	int procPlayer = -1;
	 for (int i = 0; i < ksPlayer.size(); i++)
	 {
	     if (procPlayer != ((Integer)ksPlayer.get(i)).intValue())
	     {
		 out.write("newPlayerName    Player"+((Integer)ksPlayer.get(i)));
		 procPlayer = ((Integer)ksPlayer.get(i)).intValue();
		 out.write("\n");
	     }
	     out.write("setKey"+getVKString(((Integer)ksVK.get(i)).intValue()));
	     out.write("      "+ksComString.get(i)+"\n");
	 }

	 /*
	   We just copy over the server string
	  */
	 out.write("setServer      "+twakServer+"\n");

	 /* If writing for first time add player details */
	 if (newFile)
	 {
	     out.write("newPlayerName    Player1\nsetKeyVK_LEFT      AngleUp\nsetKeyVK_RIGHT      AngleDown\nsetKeyVK_DOWN      WeaponNext\nsetKeyVK_INSERT      WeaponPrevious\nsetKeyVK_UP      FireUp\nsetKeyVK_T      Menu\nsetKeyVK_ENTER      Speak\nnewPlayerName    Player2\nsetKeyVK_K      AngleUp\nsetKeyVK_L      AngleDown\nsetKeyVK_N      WeaponNext\nsetKeyVK_M      WeaponPrevious\nsetKeyVK_O      FireUp\nsetKeyVK_P      Speak");
	 }

	out.close();
    }

    /**
       Looks up in the list of KeyEvent variable
       and returns a VKString from a key code
       @input the keycode
     */
    protected String getVKString(int input)
    {
	for (int i = 0; i < vkNames.length; i++)
	{
	    if (vkValues[i]==input)
	    {
		return vkNames[i];
	    }
	}
	return "***Corrupt settings file***";
    }
    /**
       This is called when a boolean menu item is toggled
       @param mob the item generating this event
       @param active the value returned by the item
       @param num the nubber in the array that the item is
     */
    protected void toggled(MenuOptionBool mob, boolean active, int num)
    {
	bools[num] = active;
    }


    public void message(int num)
    {
	if (num == 1)
	{
	    /* go to other menu */
	    keysMenu();
	}
	else if (num == 2)
	{
	    makeOptions();
	}
    }

    /**
       This is called when a intvalue is cahnged 
       @param mob the item generating this event
       @param value the value set by this item
       @param num the nubber in the array that the item is
    */
    protected void intSet(MenuOptionInt mob, int value, int num)
    {
	intVals[num] = value;
    }

    /**
       Called back when the ip is changed
     */
    protected void setIP(String ip)
    {
	//ipAddr = ip;
    }

    /**
       Called when one of the set keys is changed 
       @param mok the menuOptionKey that changed
       @param row the row in our data tables changed
       @param vkCode the new integer value of the vkcode we want
     */
    protected void setKey(MenuOptionKey mok, int row, int vkCode)
    {
	ksVK.set(row,new Integer(vkCode));	
    }

    /**
       Initialises the internal values for options from our
       string pairs read from readFile
       @param command the command read in
       @param value   the value read in
     */
    private void setValue(String command, String value)
    {
	String realCase = command;
	command = command.toLowerCase();
	for (int i = 0; i < bools.length; i++)
	{
	    if (command.compareTo(names[i].toLowerCase())==0)
	    {
		/* Valid command */
		if (value.toLowerCase().compareTo("true")==0)
		{
		    bools[i] = true;
		}
		else
		{
		    bools[i] = false;
		}
		return;
	    }
	}

	/* Proxy host */
	if (command.compareTo("setproxyhost")==0)
	{
	    proxyHost = value;
	    return;
	}
	if (command.compareTo("setserver")==0)
	{
	    twakServer = value;
	    return;
	}   

	/* for fps and clouds */
	try
	{
	    /* For Game port -- */
	    if (command.compareTo("setgameport")==0)
	    {
		gamePort = new Integer(value).intValue();
		return;
	    }
	    /* For proxy port */
	    if (command.compareTo("setproxyport")==0)
	    {
		proxyPort = new Integer(value).intValue();
		return;
	    }
	    if (command.compareTo("fps")==0)
	    {
		intVals[0] = new Integer(value).intValue();
		return;
	    }
	    else if (command.compareTo("numberOfClouds")==0)
	    {
		intVals[1] = new Integer(value).intValue();
		return;
	    }

	}
	catch (NumberFormatException e)
	{
	    System.err.println("Bad number \""+value+"\" in "+FILE);
	}
	if (command.compareTo("newplayername")==0)
	{
	    playerNumber ++;
	}

	/* Deep-breath for key controls */
	if (command.substring(0,Math.min(6,command.length())).compareTo("setkey")==0)
	{
	    if (playerNumber == -1)
	    {
		System.err.println("Key definition before newPlayerHelper in "+FILE);
	    }
	    else
	    {
		String vk = realCase.substring(6,command.length());
		/* Loop through values to find the one we want */
		for (int i = 0; i < vkNames.length; i++)
		{
		    if (vkNames[i].compareTo(vk)==0)
		    {
			ksPlayer.add(new Integer(playerNumber+1));
			ksVK    .add(new Integer(vkValues[i]));
			ksCommand.add(new Integer(commandIndex(value)));
			ksComString.add(value);
		    }
		}
	    }
	}
    }

    /**
    Reflection code to return a Integer a command string, ripped from
    Executive helper
    @param input the 
    */
    private int commandIndex(String input)
    {
	Field theField;
	int theValue;
	KeyEvent t = null;
	Class c = mud.input.Commands.class;
	try {
	    theField = c.getField(input);
	    theValue = ((Integer) theField.get(t)).intValue();
	    return theValue;
	} catch (NoSuchFieldException e) {
	    System.out.println("Invalid Command name in input file:");
	    System.out.println(e);
	    return -1;
	} catch (SecurityException e) {
	    System.out.println("commandStroke, security error:");
	    System.out.println(e);
	    return -1;
	} catch (IllegalAccessException e) {
	    System.out.println("commandStroke, security error:");
	    System.out.println(e);
	    return -1;
        }
    }

    /**
    this reads in a file and sets up our ExecutiveHelper
    object with the details,
    @param fileName file to read
    @return success true if successful, false if major error
    if line(s) in fileName don't parse true is still returned
    */ 
    private void readFile()
    {
	int start, stop, counter = 0;
        BufferedReader input= null;
        String line, command, value;
	String[] splitUp;
	/* remembers if we are successful so far */
	boolean success = true;
	String fileName = FILE;

	System.err.println("Reading settings file \""+fileName+"\"");

//	try
//	{
	    input = new BufferedReader ( new InputStreamReader ( getClass().getClassLoader().getResourceAsStream( "mud/"+fileName.replace( '\\', '/' )) ) );
//	}
//	catch (FileNotFoundException e)
//	{
//	    System.err.println("File \""+fileName+"\" not found");
//	    System.exit(-1);
//	}
	try
	{
	    line = input.readLine();
	    while (line != null)
	    {
		/* For each line of the file split into
		   command & value*/
		counter++;

		/* splits on whitespace or tab PERL REGEX style commands
		 *new*in*java* */
		/* remove anything after punctuation */
		splitUp = line.split("[\\*]");
	
		/* raplace any tabs or spaces with one space */ 
		splitUp = splitUp[0].split("[\\t\\s]+");
		
		if (splitUp.length > 0)
		{
		    /* remove the first aray slot if its empty */
		    if (splitUp[0].compareTo("")==0)
		    {
			for (int i = 0; i< splitUp.length-1; i++)
			{
			    splitUp[i] = splitUp[i+1];
			}
			splitUp[splitUp.length-1] = "";
		    }
		
		    if (splitUp.length >= 2)
		    {
			if (splitUp[0].compareTo("")!=0 &&
			    splitUp[1].compareTo("")!=0)
			{
			    command = splitUp[0];
			    value   = splitUp[1];
			    /* calls the method to read the values in */
			    setValue(command, value);
			} else if (splitUp.length == 1 && splitUp[0].compareTo("")==0)
			{
			    /* This is a line with a comment */
			}
			else
			{
			    System.err.println("Only two items per line: command and value on line "+counter);
			    System.err.println("Use punctuation (:;/) etc to declare comments ");
			}
		    }
		}
		line = input.readLine();
	    }
	}
	catch (IOException e)
	{
	    System.err.println("Error while reading file \""+ fileName+"\"");
	}
    }

    /**
       Called when a number changes
     */
    protected void gotNumber(int i, String name)
    {
	if (name.compareTo("proxy port: ")==0)
	{
	    /* If we want to disable this, use 0 */
		proxyPort = i;
	}
	else if (name.compareTo("game port: ")==0)
	{
	    /* If we want to disable this, use 0 */
	    gamePort = i;
	}
    }

    /**
       Return call from the setproxy string
     */
    public void text(String s)
    {
	proxyHost = s;
    }

    /**
       So as we quit we can re-set the game prot
     */
    public int getGamePort()
    {
	return gamePort;
    }
}

