/**
 * Hen Fung (Jonathon) Ng
 * CSE 219
 * Homework# 1
 */


import java.io.*;

/**
 * WordRacerClientThread continuously checks on things that should
 * be changed to the WordRacerClient class by interacting with the
 * Network Object and WordRAcerServerThread
 * @author Jon
 *
 */

public class WordRacerClientThread extends Thread {
	/**
	 * Instantiating a reader to read from the WordRacerServerThread class
	 * and makes a WordRacerClient gui so that it could use the gui
	 * that the client has and update the format of the gamestate
	 */
	private ObjectInputStream reader;
    private WordRacerClient gui;

    /**
     * WordRacerClientThread constructor
     * 
     * @param initReader
     * @param initGUI
     * 
     * This takes in the reader from the Client class and takes it for its own
     * and then takes in the gui of the WordRacerClient class as well
     */
    public WordRacerClientThread(
		ObjectInputStream initReader, WordRacerClient initGUI) {
        reader = initReader;
        gui = initGUI;
    }

    /**
     * runs the thread where it continually updates players everytime a 
     * new player joins
     */
    public void run() {
      WordRacerNetworkObject incoming; //creates a NetworkObject
      while (reader != null) { //while reader has information in it
        try {                   //continuously try to do
          incoming = (WordRacerNetworkObject)reader.readObject(); 
                              //incoming takes in the reader from client class
                            // and reads wha is in it
          if (incoming.getType() == WordRacerNetworkObject.NEW_PLAYER) { 
        	                         //if reader picks up the NEW_PLAYER command
        	                        //then it will store the data into a string
        	  String text = (String)incoming.getData();
        	  gui.updatePlayerList(text); //finally the guiwill update the playerlist
          }
          
          /**
           * checks if reader picks up the NEW_BOARD command and it will take in a randomly
           * generated list of letters and set the board
           */
          if(incoming.getType() == WordRacerNetworkObject.NEW_BOARD){
        	  char letters[][] = (char[][])incoming.getData();
        	  gui.disableStartButton();
        	  gui.setBoard(letters);
          }
          if(incoming.getType() == WordRacerNetworkObject.CHAT_MESSAGE){
        	  String message = (String)incoming.getData();
        	  gui.addText(message);
          }
          if(incoming.getType() == WordRacerNetworkObject.CHECK_WORD){
        	  String isWord = (String)incoming.getData();
        	  gui.updateScore(isWord);
          }
          if(incoming.getType() == WordRacerNetworkObject.SCORE_BOARD){
        	  String scoreBoard = (String)incoming.getData();
        	  gui.updateScore(scoreBoard);
          }
          if(incoming.getType() == WordRacerNetworkObject.GAME_TIMER){
        	  String gameTime = (String)incoming.getData();
        	  gui.updateTime(gameTime);
          }
          if(incoming.getType() == WordRacerNetworkObject.END_GAME){
        	  String winner = (String)incoming.getData();
        	  gui.endGame(winner);
          }
          if(incoming.getType() == WordRacerNetworkObject.GAME_FULL){
        	  gui.displayGameFull();
          }

        }
        catch (Exception e) { 
        	e.printStackTrace();    //catches an Exception
        }
      }
    }
}


