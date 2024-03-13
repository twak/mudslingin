//To run this with hprof

//java -Xrunhprof:cpu=samples mud.trunk.TestWater


package mud.trunk;

import mud.supporting.IntPos;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.FileInputStream;

public class TestWater
{
    MudFloorTwo floor;
    WaterThree water;
    Stage st = new Stage();;

    public static void main(String args[])
    {
	TestWater tw = new TestWater();
        tw.test();
    }

    public void test()
    {
	floor = new MudFloorTwo("data.png"  ,st, new Color(0,0,0,0));
	water = new WaterThree("water.png",st,(MudFloorInterface)floor,new Color(0,0,0,0));

        for(int i=0; i<1000;i++)
        {
	    water.doPhysics();
        }

    }

}



//To access help use
//java -Xrunhprof:help

//java -Xrunhprof[:help][:<suboption>=<value>,...] MyMainClass

// suboption
// heap=sites: Tells hprof to generate stack traces indicating where memory was allocated
// cpu=samples: Tells hprof to use statistical sampling to determine where the CPU spends its time
// depth=10: Tells hprof to show stack traces 10 levels deep, at most
// monitor=y: Tells hprof to generate information on contention monitors used to synchronize the work of multiple threads
// thread=y: Tells hprof to identify threads in stack traces
// doe=y: Tells hprof to produce a dump of profiling data upon exit
