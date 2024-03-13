package mud;

import mud.trunk.*;

/**
   Stores a connection between a player, a key on the keybord 
   and a command
 */
public class KeyCommandPlayer
{
    Integer key;
    Integer command;
    Player  player;

    /**
    creates a new link between a player, command and a key press
    @param k the key stroke (see KeyEvent in java api)
    @param c the command code (see input/Command.java)
    @param p the player
    */
    public KeyCommandPlayer(Integer k, Integer c, Player p)
    {
	key = k;
	command = c;
	player = p;
    }

    /**
    key
    @return key.intValue() the value of the key
    */
    public int getKey()
    {
	return key.intValue();
    }


    /**
    returns the defined key command code
    @return command.intValue() the value the command code
    */
    public int getCommand()
    {
	return command.intValue();
    }    


    /**
    returns the player specified
    @return player the player
    */
    public Player getPlayer()
    {
	return player;
    }

}
