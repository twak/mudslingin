package mud.supporting;

import java.util.*;
import java.io.*;
import java.applet.*;
public class SoundLoader
{
 Hashtable soundfiles = new Hashtable();
 SoundFile defaultSound;
  public SoundLoader()
  {
      defaultSound = new SoundFile("./data/default.wav");
  }
 
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
                        SoundFile SF = new SoundFile(s);
                        //int lastslash = imageName.lastIndexOf('/');
                        int lastslash = Math.max( imageName.lastIndexOf('/'), imageName.lastIndexOf('\\'));
                        String ref = imageName.substring(lastslash+1, lastslash+4);

                        try
                        {
                            folderSprites.put(new Integer(ref),SF);
                            System.out.println(ref+" "+fold+"  "+s);
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("WAV's must start with 3 numbers");
//                            System.exit(-1);
                        }
                }

            soundfiles.put(new Integer(fold),folderSprites);
//            }
//        }

        }

//    Hashtable sounds = new Hashtable();
//
//    File file = new File("./data/"+fold);
//    if (!file.exists())
//    {
//        System.err.println("Sound Folder "+fold+" not found");
//        System.exit(-1);
//    }
//    if (file.isDirectory())
//    {
//            File[] fileNames = file.listFiles();
//            if (fileNames != null)
//            {
//                for (int i=0; i<fileNames.length; i++)
//                {
//                    if (fileNames[i].getName().endsWith(".wav"))
//                    {
//                        String fname = (new Integer(fold)).toString();
//                        String soundName = ((fileNames[i]).toString());
//                        int lastslash = Math.max( soundName.lastIndexOf('/'), soundName.lastIndexOf('\\'));
//                        String reference = soundName.substring(lastslash+1, lastslash+4);
//                        SoundFile SF = new SoundFile(soundName);
//                        try
//                        {
//                            sounds.put(new Integer(reference),(SF));
//                        }
//                        catch (NumberFormatException e)
//                        {
//                            System.err.println("WAV's must start with 3 numbers");
//                            System.exit(-1);
//                        }
//
//                    }
//                }
//            }
//        }
//
//    soundfiles.put(new Integer(fold),sounds);
  }


   public void playSound(int f, int s)
   {
    SoundFile tune;
    if(f <= -1 || s <= -1)
    {
       tune = defaultSound;
    }
    else
    {
     tune = (SoundFile)((Hashtable)soundfiles.get(new Integer(f))).get(new Integer(s)); 
    }
    AudioClip clip = tune.getFile();
    clip.play();
   }
}
