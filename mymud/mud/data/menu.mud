* ********************************************************
*                        intro.mud                       *
**********************************************************
**This is our default file, it contains settings for     *
**most items. It should not be changed by end users.     *
**each line here calls a class in ExecutiveHelper.java   *
**             this is all CaSe SeNsItIvE                *  
**eg "width 800" calls widthHelper(800).        tk1748   *
**********************************************************

width      	 800   *arse
height     	 600 anything after the first 2 words is ignored
fps              30
waterFile     	 water.png
mudFile       	 intro.png
gravity          90
initialWind      20
*if this is 0, there is no wind
numberOfClouds   0
gustyWind        5
maxWind          100
showStats        false
* showStats default value false
showPrints       false
* showPrints default value 
gameType         singlePlayer
startMenu        0
* **** PLAYER ONE INFORMATION ****
newPlayerName 	 player1
setKeyVK_A       AngleUp
setKeyVK_S       AngleDown
setKeyVK_Z       WeaponNext
setKeyVK_X       WeaponPrevious
setKeyVK_W       FireUp
setKeyVK_T       Menu
playerX          50
playerY          450.02

