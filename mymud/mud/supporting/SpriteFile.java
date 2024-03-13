package mud.supporting;
import java.awt.Image;

/**Creates the data structure SpriteFile
which holds an Image and a String. The String
is a filename, so the Images are easy to reference*/

public class SpriteFile
{
    private Sprite image;
    private String file;

    public SpriteFile(Sprite i, String f)
    {
        image = i;
        file = f;
    }

    /**Returns an Image*/    

    public Sprite getSprite()
    {
        return image;
    }

    /**Returns a String*/

    public String fileName()
    {
        return file;
    }
}


