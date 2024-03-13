* ********************************************************
*                        default.mud                     *
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
mudFile       	 data.png
backgroundFile   data/mario.png
backgroundColour 200001001
gravity          90
initialWind      20
gustyWind        5
maxWind          100 
gameType         singlePlayer
time             0
startMenu        9
* **** PLAYER ONE INFORMATION ****
newPlayerName 	 player1
playerX          700.01
playerY          320
playerType       local
* **** PLAYER TWO INFORMATION ***
newPlayerName    player2
playerX          100
playerY          400.02
playerType       client

