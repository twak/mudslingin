package mud;


import mud.trunk.*;
import mud.supporting.*;
import java.util.ArrayList;
import java.io.*;
import java.lang.reflect.*;


/**
This class loads and restores all props on the stage
and the gameValues to disk
 */
public class DeusEx
{


    public DeusEx()
    {
    }

    /**
    Saves the current game state
    @param filename the file to save too
    */
    public int save(String filename, Stage myStage)
    {


	/* Remove the println box if it exists, not very proper but
	   it works */
	if (myStage.getGameValue().systemOut != null)
	{
	    myStage.getGameValue().systemOut.finalCurtain();
	    myStage.getGameValue().systemOut = null;
	}

	    for (int i = myStage.allProps().size()-1; i >= 0; i--)
	    {
		Prop p = (Prop)myStage.allProps().get(i);
		if (p instanceof Text || p instanceof MenuText || p instanceof Speak)
		{
		    p.finalCurtain();
		}
	    }

	System.err.println("Saving to file \"saves/"+filename+"\"");

	try
	{
	    FileOutputStream f_out     = new FileOutputStream ("saves/"+filename);
	    ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
	    /* write the stage to disk */


//  	    System.err.println(myStage.allProps().size());

	    obj_out.writeObject(myStage);
	}
	catch (Exception e)
	{
	    System.err.println (e+   "  "+e.getLocalizedMessage()+"   "+e.getMessage());
	    e.printStackTrace();

	    return -1;
	}
	return 0;
    }


    /**
    Tidy wrapper so that both load and save appear to happen from
    deus ex ;)
    @param filename the file to load
    @param executive the executive in charge of this run of mudsling
     */
    public int load (String filename,Executive executive)
    {
	return executive.load(filename);
    }


    /**
    Loads a game state
    @param filename the game state to load
    */
    public Stage stageFromFile(String filename)
    {
	Object obj,o;
	Class  cls;

	System.err.println("Loading from file \""+filename+"\"");


	try
	{
	    FileInputStream f_in     = new FileInputStream ("saves/"+filename);
	    ObjectInputStream obj_in = new ObjectInputStream (f_in);
	    /* recover the stage object */
	    System.err.println("in");
	    obj = obj_in.readObject ();
	    System.err.println("out");
	    System.err.println("DeusEx Load says number of props ="+((Stage)obj).allProps().size());
	    return ((Stage)obj);
	}
	catch (EOFException e)
	{
	    /* End of the file - wow */
	}
	catch (Exception e)
	{
	    System.err.println("Error - incompatable input file \""+filename+"\"(different version of Mudsling?)");
	    System.err.println(e);
	    e.printStackTrace();
	    return null;
	    //System.exit (1);
	}
	return null;

    }

}
