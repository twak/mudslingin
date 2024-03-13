The supporting package contains little classes such as position and Sprite THAT ARE USED by the trunk package. You can add other little things here.

Trunk package has all stage, prop, datamap etc.. classes in

all other classes Script Editors/Writers/Audience can go in this (root) folder.

the data folder is where all data (duh) goes: Sprites, level.mud files etc.

**************IMPORTANT*********************************************
to set your classpath so you can compile anything anywhere type
source setpath
in the source folder. Its a little script I wrote, so if you know
what your doing, add it too your .bashrc thingy.
OR in GoodOldWindows(tm) add the source folder to you CLASSPATH 
environment variable
********************************************************************

at the moment a user will run the game by typing 
java mud.Mudsling
This only works if the classpath is set as above.

DOCUMENTATION: to make the documentation on unix type
makedoc
and a script runs, and makes all the documentation in
mudsling/documents/api