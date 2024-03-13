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
waterFile     	 water.png
mudFile       	 intro.png
gravity          90
initialWind      20
*if this is 0, there is no wind
gustyWind        5
maxWind          100
gameType         singlePlayer
* **** PLAYER ONE INFORMATION ****
newPlayerName 	 player1
playerX          150
playerY          400
* **** PLAYER TWO INFORMATION ***
newPlayerName    player2
playerX          650.01
playerY          400

