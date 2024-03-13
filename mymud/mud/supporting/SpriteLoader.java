package mud.supporting;

import java.awt.Image;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;


/**Loads all sprites and is used to return an image
   when a fileName is given*/

public class SpriteLoader
{
    int s = 0;
    Hashtable filesAndSprites = new Hashtable(); 
    Sprite defaultSprite;

    /**
    Creates a new set of sprites from the specified
    directory
    @param dir the directory to laod the sprites from
    */
    public SpriteLoader()
    {
        defaultSprite = new Sprite(fromFile("data/default.png"), this, "data/default.png",0,1);
    }


    /**Loads all SpriteFiles into an ArrayList.
       SpriteFiles consist of both the image and
       filename for easy reference and lookup*/    
    
    public void loadAll()
    {
        for (int fold = 0; fold < 10; fold ++)
        {

            Hashtable folderSprites = new Hashtable();
//
//        File file = new File("./data/"+fold);
//
//        if (!file.exists())
//        {
//            System.err.println("Sprite Folder "+fold+" not found");
//            System.exit(-1);
//        }

//        if (file.isDirectory())
//        {
//            File[] fileNames = file.listFiles();
//            if (fileNames != null)
//            {
                for (String s : AssetFinder.pngs)
                {
                        if (!s.contains( "/"+fold+"/")) // slow! folder indexing
                            continue;

                        String tail = s.substring( s.lastIndexOf( '/' ) +1, s.length() );
//                        String fname = (new Integer(fold)).toString();
                        String imageName = (tail);
                        Image bob = fromFile(s);
                        //int lastslash = imageName.lastIndexOf('/');
                        int lastslash = Math.max( imageName.lastIndexOf('/'), imageName.lastIndexOf('\\'));
                        String ref = imageName.substring(lastslash+1, lastslash+4);
                        
                        try 
                        {
                            folderSprites.put(new Integer(ref),new SpriteFile(new Sprite(bob, this,imageName, fold, new Integer(ref).intValue()), imageName));
                            System.out.println(ref+" "+fold+"  "+s);
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("PNG's must start with 3 numbers");
//                            System.exit(-1);
                        }
                }

            filesAndSprites.put(new Integer(fold),folderSprites);
//            }
//        }

        }
    }

    /**
    Returns a image when given a filename
    @return getIm    referenced by filename
    @param  fileName filename of image   
    */    
    public Image getImage(String s)
    {
        Image getIm = defaultSprite.getImage();
        return getIm;
    }
    

    public Image getImage (int f, int p)
    {
        SpriteFile bob;
        SpriteFile b;  
        Image getIm = defaultSprite.getImage();
        int j = 0;
                
        if (filesAndSprites.size() > j)
        {
            bob = (SpriteFile)((Hashtable)filesAndSprites.get(new Integer(f))).get(new Integer(p));
            if(bob == null)
            {
                return getIm;
            }
            getIm = bob.getSprite().getImage();
        }   
        return getIm;
    }


    /**
    returns a sprite from a filename
    @param fileName the file name to get the sprite of
     */
    public Sprite getSprite (String fileName)
    {
        Sprite b = defaultSprite;
        return b;
    }

    public Sprite getSprite (int f, int p)
    {
	Object o;
        Sprite b = defaultSprite;
	if (f==-1 || p == -1) return b;
        SpriteFile bob = null;
        int j = 0;
        
        if (filesAndSprites.size() > j)
        {
	    o = filesAndSprites.get(new Integer(f));
	    
	    if (o == null) return b;
	    bob = (SpriteFile)((Hashtable)o).get(new Integer(p));
            if(bob == null)
            {
                return b;
            }
            b = bob.getSprite();
        }
        return b;
    }
            

    /**
    Uses the imagio package to obtain the image
    from disk
     */
    private Image fromFile (String file)
    {
    	System.out.println("Trying to read from file "+file);
	    Image output = null;
	    try
	    {
	        output = ImageIO.read(getClass().getClassLoader().getResourceAsStream( "mud/"+file ));
	    }
	    catch (java.io.FileNotFoundException e)
	    {
            System.out.println("File not found" + file);
	        return null;
	    }
	    catch (java.io.IOException e)
	    {
	        System.out.println("Error in image file "+file);
	        System.exit(-1);
	    }
	    catch (SecurityException e)
        {
	        System.out.println("Security will not allow operation on file "+file);
	        System.exit(0);
	}
	catch (IllegalArgumentException e)
	{
	    System.out.println("fromFile in Sprite Loader passed null argument");
	    System.exit(-1);
	}

	return output;
    }

}
