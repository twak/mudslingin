package mud.supporting;
import java.awt.Image;
import java.net.*;
import java.applet.*;

/**
Stores the Filename of the image and a quick reference number
*/

public class SoundFile
{

    private URL file;
    private AudioClip mySong;

   public SoundFile(String filename)  
   {
       try
       {
          file = new URL("file:"+filename);
	  mySong = java.applet.Applet.newAudioClip(file);
       }
       catch (java.net.MalformedURLException e)
       {
         System.out.println("Incorrect file path"+ e);
         System.exit(-1);
       }
   }  

   public AudioClip getFile()
   {
       return mySong;
   }
}
