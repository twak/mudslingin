/* $Id: Executive.java,v 1.44 2004/05/01 13:27:18 th2150 Exp $ */
package mud;
import mud.input.*;
import mud.trunk.*;
import mud.supporting.*;
import mud.display.*;
import mud.director.*;
import mud.broadcast.*;
import java.io.*;
import java.lang.reflect.*;

/**
The executive reads in a level file (*.mud) and creates
all the main objects with the parameters necessary to
run the level. Any parameters not specified by the level .mud
are taken fron the default.mud file.
To start the game it starts a StageManager
The game values are loaded from an Executive Helper. These
are then overwritten by the values in a settings file. This
contains things like default key bindings, port numbers, ip addresses,
and performance settings, like transparency and number of clouds.
*/
public class Executive
{
    private static String DEFAULT_SETTINGS = "data/settings.mud";
    
    private        String levelFile;

    private StageManager    stageManager;
    private SpriteLoader    spriteLoader;
    private SoundLoader     soundLoader;
    /* This is called a lot so has short name */
    private ExecutiveHelper lb;
    /* Second stage is set if we are loading a saved game */
    private Stage           stage, secondStage = null;
    private Audience        allAudiences;

    private KeyAction myKeyAction;

    private boolean goAgain;

    /**
    constructs a new level from fileName file
    @param fileName level file
    */
    public Executive(String fileName)
    {
	levelFile = fileName;

	goAgain = false;

	/* This is only created once per game */
	myKeyAction = new KeyAction();
	/* This stores all the data we have read so far*/
	lb = new ExecutiveHelper(myKeyAction);

	warmUp();
    }

    /**
       Loads from an input file (may be .mud or .mmud)
       @param mudFile the input file
       @param s the stage to load it onto
     */
    public void loadMud(String mudFile, Stage s)
    {
	levelFile = mudFile;

	lb = new ExecutiveHelper(myKeyAction);

	goAgain = true;

	/* Now everything is done, force the old game
	   to quit */
	s.getGameValue().quitNow = true;
    }

    /**
       Basic method. In a straight game this runs just once
       when we load a game, the game quits, and we come back
       here and load a new game.
       The relevant values have, by this point been loaded in
       lb and the secondary stage set appropriately. The next step is
       to load values from lb, and then set the stage.
     */

    public void warmUp()
    {
	ExecutiveHelper settings;

	/* we only load all those sprites once */
	spriteLoader = new SpriteLoader();
//	for (int i = 0; i < 10; i++)
//	{
	    spriteLoader.loadAll();
//	}
	soundLoader = new SoundLoader();
	soundLoader.loadAll();
//	soundLoader.loadAll(1);

	do
	{
            /* Pointer to Stage so new props can be added */
	    stage = new Stage();

	    stage.setGameValue(new GameValues(800,600,levelFile));

	    /* tell the helper where our stage is, so props know which
	       stage the're on. This is ignored when loading*/
	    lb.setStage(stage);
	    goAgain = false;
	    /* we need to load the sprite loader before the props are loaded */
	    if (secondStage == null)
	    {
		stage.setSpriteLoader(spriteLoader, soundLoader);
	    }
	    else
	    {
		/* This to allow readFile to run */
		stage.setSpriteLoader(spriteLoader, soundLoader);
		/* This is what really counts later */
		secondStage.setSpriteLoader(spriteLoader, soundLoader);
	    }

	    settings = new ExecutiveHelper(myKeyAction);

	    /* read in, and run the file */
	    if   (readFile(levelFile       ,lb      ,false) 
               && readFile(DEFAULT_SETTINGS,lb      , true))
	    {
		/* set up other classes, start StageManager */
		openThetreDoors();
	    }
	    else
	    {
		System.err.println("Error in input files \""+levelFile+"\" or \""+DEFAULT_SETTINGS+"\", quitting");
		System.exit(-1);
	    }
	}
	while (secondStage != null || goAgain);
	System.err.println("Normal system shutdown, have a nice day");
    }

    /**
    Call to build create the objects as required
    for the level, and to start the level 
    If second stage is null then we are loading a game's settings
    from a .mud file. Otherwise we are loading a saved game's stage from
    second stage. The lb (ExecutiveHelper) values have already been
    read in by this stage.
    @param audience  if audience is null (the house is empty) then this
    (invites) creates a new audience, else it must continue with
    the current audience
    */
    public void openThetreDoors()
    {
	GameValues gv;
	gv     = stage.getGameValue();
	gv.wind           = lb.wind   ;
	gv.gravity        = lb.gravity;
	gv.levelSetUpFile = levelFile;


        /* If we are loading a new stage, switch it now.
	   All the above was to make the genre correct.
	   secondStage is used as a flag, and set to null
	   at the end of this method
	 */
	if (secondStage != null)
	{
	    stage = secondStage;
	    stage.hack();
	    stage.getGameValue().isNetworked = false;
	}
	else
	{
	    stage.setStage(gv, 
			   lb.mudFile, 
			   lb.waterFile, 
			   lb.bgfile, 
			   spriteLoader,
			   soundLoader,
			   myKeyAction.isServer(),
			   lb.mudColour,
			   lb.waterColour);
	    stage.getGameValue().fps=lb.ourFPS;
	    stage.getGameValue().isNetworked = myKeyAction.getSocket() != null;
	    stage.hack();
	}
	/* If we are in a networked fame then we should
	   remove any kind of counter */
	if (stage.getGameValue().isNetworked)
	{
	    if ( stage.getGameValue().fpsCounter != null)
	    {
		stage.getGameValue().fpsCounter.finalCurtain();
		stage.getGameValue().fpsCounter = null;
	    }
	}

	/* Set the game not to quit immediately */
	stage.getGameValue().quitNow = false;

	/* add a wind object to the stage - this is optional it
	 will only happen if clouds has been specified in the 
	 input file, and this is not a load from disk. */
	if (secondStage == null && lb.windMax!= 0 && !stage.getGameValue().isNetworked )
	{
	    new Wind(stage, lb.windJitter, 
                            lb.windMax   , 
                            Math.max(lb.numClouds,0));
	}
	if (stage.getGameValue().isNetworked && lb.windMax!= 0 && myKeyAction.isServer())
	{
		/* We need to add clouds if were a client or
		   server so that continuity in prop numbers
		   is kept */
	    new Wind(stage, lb.windJitter, 
                            lb.windMax   , 
                            Math.max(lb.numClouds,0));
	}
 

	/* Initialise the stage manager */
	stageManager = new StageManager(stage);

	/**
	   If audience is null, this is the first time
	   otherwise we can keep the same audience and
	   key action
	*/
	if (allAudiences == null)
 	{
	    /* The value lb.fullScreen is a boolean that
	       is read in, and is intended for audience's
	       constructor
	    */
	    allAudiences = new Audience(myKeyAction, lb.fullScreen);
	}

	ScriptEditor scripteditor = new ScriptEditor(myKeyAction);


	/* If we are the client and we're networked then we
	   set the genre to be 'special' */
	if (myKeyAction.getSocket() != null)
	{ 
	    if (!myKeyAction.isServer())
	    {
		lb.genre = new ClientGenre();
	    }
	}
	Director director = new Director(stageManager, 
                                         this, 
                                         scripteditor, 
                                         lb.genre,
					 lb.startMenu);

	stage.setDirector(director);

	/* for each player create a new scriptWriter
	   and add it to a script editor */

	Player currentPlayer, firstPlayer = null;
	KeyCommandPlayer kcp;
	LocalScriptWriter localscriptwriter;
	ServerScriptWriter serverScriptWriter;

	for (int p = 0; p < lb.players.size(); p++)
	{
	    if (p == 0) firstPlayer = (Player)lb.players.elementAt(p);
	    if (secondStage == null)
	    {
		currentPlayer = (Player)lb.players.elementAt(p);
	    }
	    else
	    {
		try
		{
		    currentPlayer = (Player)stage.allPlayers().get(p);
		}
		catch (Exception e)
		{
		    System.err.println("Error incompatability between save file and mud file");
		    System.err.println(e);
		    System.exit(-1);
		    currentPlayer = null;
		}
	    }
	    /* Local player */
	    if (currentPlayer.getType() == currentPlayer.LOCAL)
	    {
		localscriptwriter = new LocalScriptWriter(currentPlayer);

		for (int i = 0; i < lb.ckp.size(); i++)
		{
		    kcp = ((KeyCommandPlayer)lb.ckp.elementAt(i));
		    if (kcp.getPlayer().getName().compareTo(currentPlayer.getName())==0)
		    {
			localscriptwriter.setKeys(kcp.getCommand(),
						  kcp.getKey    () );
		    }
		}

		scripteditor.addScriptWriter(localscriptwriter);
	    }
	    /* We are a client - use 1st person key's */
	    else if (currentPlayer.getType() == currentPlayer.REMOTE_CLIENT)
	    {
		serverScriptWriter = new ServerScriptWriter(currentPlayer, 
                                     new Client(myKeyAction.getSocket()));

		for (int i = 0; i < lb.ckp.size(); i++)
		{
		    kcp = ((KeyCommandPlayer)lb.ckp.elementAt(i));
		    if (kcp.getPlayer().getName().compareTo(firstPlayer.getName())==0)
		    {
			serverScriptWriter.setKeys(kcp.getCommand(),
						   kcp.getKey    () );
		    }
		}
		scripteditor.addScriptWriter(serverScriptWriter);
	    }
	    /* We are a server: */
	    else if (currentPlayer.getType() == currentPlayer.REMOTE_SERVER)
	    {
		serverScriptWriter = new ServerScriptWriter(currentPlayer, new Server(myKeyAction.getSocket()));
		for (int i = 0; i < lb.ckp.size(); i++)
		{
		    kcp = ((KeyCommandPlayer)lb.ckp.elementAt(i));
		    if (kcp.getPlayer().getName().compareTo(firstPlayer.getName())==0)
		    {
			serverScriptWriter.setKeys(kcp.getCommand(),
						  kcp.getKey    () );
		    }
		}
                scripteditor.addScriptWriter(serverScriptWriter);
	    }
	    /* We are a server's place holder on a client */
	    else if (currentPlayer.getType() == currentPlayer.REMOTE_BOT)
	    {
		/* This does nothing, dont think it needs to do anything either...? */
		LocalScriptWriter localScriptWriter = new  LocalScriptWriter(currentPlayer); 
                scripteditor.addScriptWriter(localScriptWriter);
	    }
	    else
	    {
		System.err.println("Invalid game type: "+ currentPlayer.getType());
		System.exit(-1);
	    }
	/* End of loop over all players */
	}
	/* add the required stageAccess' to the StageManager IN ORDER */



	/* If this is a server, then we load a broadcast module 
	   after physics. If this is a client we just load a
	   reciever module. If this isn't networked we just
	   have physics */
	if (myKeyAction.getSocket() == null)
	{
	    /* Normal local game */
	    stageManager.registerStageAccess(director);
	    Physics  physics  = new Physics ();
	    stageManager.registerStageAccess(physics );
	    /* Always have a display module */
	    stageManager.registerStageAccess(allAudiences);
	}
	else
	{
	    /* Networked game */
	    if (myKeyAction.isServer())
	    {
		/* Server in a game, so need phy & broadcast */
		stageManager.registerStageAccess(director);
		Physics  physics  = new Physics ();
		stageManager.registerStageAccess(physics );
		Broadcast broadcast = new Broadcast(myKeyAction, stage);
		stageManager.registerStageAccess(broadcast);
		/* Always have a display module */
		stageManager.registerStageAccess(allAudiences);		
	    }
	    else
	    {
		/* This is a client, load recieving module */
		stageManager.registerStageAccess(director);
		Receiver receiver = new Receiver(myKeyAction, stage);
		stageManager.registerStageAccess(receiver);
		/* Always have a display module */
		stageManager.registerStageAccess(allAudiences);
	    }
	}


	/* Reset the loading flag */
	secondStage = null;

	/* Free up any memory we have */
	System.gc();

	/* Everything is ready so we make it go */
	stageManager.openCurtains();
    }

    /**
    Loads a file from disk
    @param dataFile the file to load
    */
    public int load(String dataFile)
    {
	DeusEx dE = new DeusEx();
	secondStage =  dE.stageFromFile(dataFile);

	/* If the load failed */
	if (secondStage == null)
	{
	    return -1;
	}

	/* Reset the game data */
	lb = new ExecutiveHelper(myKeyAction);

	/* Load the default values from the mudfile the
	   save file originally came from */
	levelFile = secondStage.getGameValue().mudFile;

	/* Now everything is done, force the old game
	   to quit */
	stage.getGameValue().quitNow = true;

	goAgain = true;

	System.err.println("Loading");

  	return 0;
    }


    /**
    this reads in a file and sets up our ExecutiveHelper
    object with the details,
    Creates a srtting file if it ins't found
    @param fileName file to read
    @param lb the executive helper to store values in
    @param isSetting whethere we are reading a settings file
    @return success true if successful, false if major error
    if line(s) in fileName don't parse true is still returned
    */ 
    private boolean readFile(String fileName,
                             ExecutiveHelper lb,
			     boolean isSetting)
    {
	int start, stop, counter = 0;
        BufferedReader input= null;
        String line, command, value;
	String[] splitUp;
	/* remembers if we are successful so far */
	boolean success = true;

	System.err.println("Reading definition file \""+fileName+"\"");

//	try
//	{
	    input = new BufferedReader ( new InputStreamReader( getClass().getClassLoader().getResourceAsStream( "mud/"+fileName.replace( '\\', '/' ))));//new FileReader (fileName));
        if (input == null)
            return false;
//	}
//	catch (FileNotFoundException e)
//	{
//	    /* If not settings file, try and make one */
//	    if (isSetting)
//	    {
//		MenuPageOptions tmp = new MenuPageOptions(this);
//		tmp.createNew();
//		try
//		{
//		    input = new BufferedReader (new FileReader (fileName));
//		}
//		catch (FileNotFoundException f)
//		{
//		    System.err.println("Unable to create settings file \""+fileName+"\"");
//		    System.exit(-1);
//		}
//	    }
//	    else
//	    {
//		System.err.println("File \""+fileName+"\" not found");
//		return false;
//	    }
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
		/* remove anything after stars */
		splitUp = line.split("[\\*]");
		if (splitUp.length > 0)
		{
		    /* raplace any tabs or spaces with one space */ 
		    splitUp = splitUp[0].split("[\\t\\s]+");
		
		    /* remove the first aray slot if its empty */
		    if (splitUp[0].compareTo("")==0)
		    {
			for (int i = 0; i< splitUp.length-1; i++)
			{
			    splitUp[i] = splitUp[i+1];
			}
			splitUp[splitUp.length-1] = "";
		    }
		    
		    /* If we have 2 arguments... */
		    if (splitUp.length >= 2)
		    {
			if (splitUp[0].compareTo("")!=0 &&
			    splitUp[1].compareTo("")!=0)
			{
			    command = splitUp[0];
			    value   = splitUp[1];
			    /* calls the method to read the values in */
			    callMethod(command, value, counter, lb, isSetting);
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
	    return false;
	}
	return success;
    }


    /**
    calls the method on the line of the input file: calles method.concat("Helper")
    @param method the helper method to call
    @param value  the argument to pass
    @param line   the line number that we are processing
    @return boolean true if successful false otherwise
    */
    private boolean callMethod (String          method, 
                                String          value, 
                                int             line, 
                                ExecutiveHelper lb,
				boolean         isSetting)
    {
	
	Class lib = lb.getClass();
        Method[] theMethods = lib.getMethods();
	Class methodReturns = null;
	/* Stores whether this is setKeyXXXXHelper method */
	String keyCommand = null;

	/* This is a set-key method, we know what types it could have */
	if (method.indexOf("setKey")==0)
	{
	    keyCommand = method.substring(6,method.length());
	    method = "setKey";
	}


	/* Checks the requested method exists & gets parameter list */
	for (int i = 0; i < theMethods.length; i++)
	{
	    /* If we are reading a settings file then only menthods
	       ending in S are relevant */
	    if (theMethods[i].getName().compareTo(method+"Helper") == 0
		&& !isSetting)
	    {
		methodReturns = theMethods[i].getParameterTypes()[0];
	    }
	    else if (theMethods[i].getName().compareTo(method+"HelperS") == 0
		&& isSetting)
	    {
		methodReturns = theMethods[i].getParameterTypes()[0];
	    }
	}


	if (methodReturns == null)
	{
	    System.err.println("I cant find how to \""+method+"\" on line "+line);
	    return false;
	}

	String stringReturn = methodReturns.getName();
	/* This is a normal helper */
	Object[] arguments;
	Class [] argTypes;  
	if (keyCommand == null)
	{
	    arguments = new Object[1];
	    argTypes  = new Class [1];
	}
	/* This is a set key helper */
	else
	{
	    arguments = new Object[2];
	    arguments[1] = new String(keyCommand);
	    argTypes  = new Class [2];
	    argTypes[1] = keyCommand.getClass();
	}
	/* Takes each possible input type and returns it value and Class*/
	try
	{
	    if (stringReturn.compareTo("java.lang.Integer")==0)
	    {
		Integer i = (Integer.valueOf(value));
		arguments[0] = i;
		argTypes[0] = i.getClass();
	    } else if (stringReturn.compareTo("java.lang.String")==0)
	    {
		arguments[0] = value;
		argTypes[0] = value.getClass();
	    } else if (stringReturn.compareTo("java.lang.Double")==0)
	    {
		Double i = (Double.valueOf(value));
		arguments[0] = i;
		argTypes[0] = i.getClass();
	    } else
	    {
	        System.out.println("Helper Class Parameter Type \""+stringReturn+"\" not implemented on line "+ line);
	        return false;
	    }
	}
	catch (NumberFormatException e)
	{
	    System.err.println("Bad number on "+line);
	    return false;
	}
	
	/* Finally run the Method, and handle format errors */
	try
        {
	    Method ourMethod;
	    if (!isSetting)
	    {
		ourMethod = lib.getMethod((method+"Helper"), argTypes);
	    }
	    else
	    {
		ourMethod = lib.getMethod((method+"HelperS"), argTypes);
	    }
	    ourMethod.invoke(lb, arguments);
	}
	catch (NoSuchMethodException e)
	{
	    System.out.println("I cant find how to \""+method+"\" on line "+line);
	    //System.err.println(e.toString());
	    return false;
	}
	catch (IllegalAccessException e)
	{
	    System.err.println("Illeagal Access Exception by line "+line);
	    return false;
	}
	catch (InvocationTargetException e)
	{
	    System.err.println("While trying to \""+method+"\" on line "+line+" it went wrong, and produced an unhelpful stack trace");
	    return false;
	}

	/* we were successful!*/
	return true;
    }

    /**
       When we need to override input systems to
       get a wider range, this allows it
       @return the keylister attached to the games JFrame
     */
    public KeyAction getKeyAction()
    {
	return myKeyAction;
    }

/*
   sets up a list of
   
    private void findMethods ()
    {
        Method[] ourMethods = this.getClass().getMethods();
        Vector ourMethodNames = new Vector(ourMethods.length);
	for (int i = 0; i < ourMethods.length; i++)
	{
	    ourMethodNames.add(ourMethods[i].getName());
	}
	}*/
}


