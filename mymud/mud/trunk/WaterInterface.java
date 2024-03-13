/* $Id: WaterInterface.java,v 1.2 2004/03/09 11:03:11 tk1748 Exp $ */

package mud.trunk;

import mud.supporting.IntPos;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;

public interface WaterInterface
{

    public void popContactMap();
    public boolean isWater(IntPos x);
    public Image getImage();
    IntPos getNextPixel(IntPos currentPix);
    public boolean hasPixelMoved(int px, int py, LinkedList moved_list);
    public IntPos getTopPixel(int heightY, int heightX);
    int getMoveType(IntPos movePixel);
    public void updateContact();
    public void doMove(int moveType, IntPos mPix);
    public void doPhysics();
    public void addWater(IntPos p);
    public int waterDepth(IntPos x);



}
