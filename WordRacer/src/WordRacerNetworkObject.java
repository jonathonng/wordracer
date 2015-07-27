/**
 * Hen Fung (Jonathon) Ng
 * CSE 219
 * Homework# 1
 */



import java.io.Serializable;

/**
 * WordRacerNetworkObject class allows the interaction between
 * all the Serverthread,Server, Client and ClientThread classes
 * with their readers and writers
 * @author Jon
 *
 */

public class WordRacerNetworkObject implements Serializable
{
	/**
	 * creates instance variables type is used to check
	 * which type of command the Server or Client is looking 
	 * at. 
	 */
	private int type;
	private Object data = null;

	/**
	 * These constants are used to determine the labels
	 * of which types of commands the server or client is looking
	 * at
	 */
	public static final int NEW_PLAYER = 0;
	public static final int SCORE_BOARD = 1;
	public static final int NEW_BOARD = 2;
	public static final int START_BUTTON = 3;
	public static final int CHECK_WORD = 4;
	public static final int GAME_TIMER = 5;
	public static final int START_TIMER = 6;
	public static final int GAME_FULL = 7;
	public static final int END_GAME = 8;
	public static final int LOG_IN_GAME = 9;
	public static final int CHECK_PLAYERS_INGAME = 10;
	public static final int CHANGE_PASSWORD = 11;
	public static final int SEND_CHAT = 12;
	public static final int CHECK_PLAYER_SCORE = 13;
	public static final int UPDATE_TABLES = 14;
	public static final int CHAT_MESSAGE = 15;
	
	
	/**
	 * Constructor to be used when only taking in the type
	 * of command being used
	 * @param initType
	 */
	public WordRacerNetworkObject(int initType) {
		type = initType;
	}
	
	public WordRacerNetworkObject
			(int initType, Object initData) { 
		data = initData;
		type = initType;
	}

	/**
	 * Accessor methods to take in the actual data or
	 * what kind of type it is
	 * @return
	 */
	public Object getData() { return data; }
	public int getType() 	{ return type; }
}
