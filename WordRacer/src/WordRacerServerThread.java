/**
 * Hen Fung (Jonathon) Ng
 * CSE 219
 * Homework# 1
 */


import java.io.*;
import java.net.*;

/**
 * This is the WordRacerServerThread class is a thread that sends 
 * data back to client thread and reads data from the Client class
 * which is used to run functions on the server class which will
 * change the gui of the client class.
 * @author Jon
 *
 */

public class WordRacerServerThread extends Thread {
	/**
	 * reader and writer Objects to read and write to the 
	 * client server num determines the number of the thread
	 * that it currently is, such as the 3rd or 4th thread 
	 * that is made
	 */
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private int num;
	
	private boolean stillConnected;
	    
	/**
	 * construction of the WordRacerThread class, it throws
	 * an ioexception so that it may be tried and catched in the
	 * server class. Socket connnection is the socket of the client
	 * @param connection
	 * @param initNum
	 * @throws IOException
	 */
	public WordRacerServerThread(Socket connection, int initNum)
			throws IOException {
		num = initNum; //stores the number of the thread
		OutputStream os = connection.getOutputStream(); //takes down the
		                            //socket of the specific thread client for output
		writer = new ObjectOutputStream(os);  //creates a new writer for this
		                            //client
		InputStream is = connection.getInputStream(); //takes down the 
		                           //socket of the specifictread client for input
		reader = new ObjectInputStream(is);   //creates a new reader for this
		                           //client
	}
	   
	public boolean isConnected(){
		return stillConnected;
	}
	/**
	 * This function allows the Server class to use this method to 
	 * send specific data to the client class to be used
	 * @param outgoing
	 * @throws IOException
	 */
	public void send(WordRacerNetworkObject outgoing)
			throws IOException {
		writer.writeObject(outgoing);
	}

	/**
	 * This function allows the thread to be alive and if starteD()
	 * will allow the thread to keep on running and continuously 
	 * take in data
	 */
	public void run() {
		WordRacerNetworkObject incoming; //New Network Object to allow interaction
		
		stillConnected = true; //checks if the client is still connected to
		                  //the server
		while(stillConnected) {  //if so this while loops continutes for that client
			try {
				incoming = (WordRacerNetworkObject)reader.readObject(); //keeps reading
				                                  //objects from the Client class by
				                                 //NetworkObjectClass
				if(incoming.getType() == 
					WordRacerNetworkObject.CHAT_MESSAGE){
					WordRacerServer.sendToAll((String)incoming.getData(), num);
				}				
				if (incoming.getType() ==                  
						WordRacerNetworkObject.NEW_PLAYER) {
					WordRacerServer.sendNewPlayerList();    //if the Networkobject
					                           //type is the NEW_PLAYER command then
					                    // a new playerlis will be sent to all clents
					WordRacerServer.sendNewScoreList(); //as well as a new score list
				}
				if(incoming.getType() == 
					WordRacerNetworkObject.START_BUTTON) {
					/**
					 * if start button is pressed then a new score list
					 * is set and the start timer is started
					 */
					WordRacerServer.disableStartButton();
					WordRacerServer.generateStartTimer();
					
				}
				if(incoming.getType() == 
					WordRacerNetworkObject.CHECK_WORD){
					/**
					 * if a word is entered into the text ield
					 * then the wordisUsed function is called and then
					 * a new Playerlist is sent to update the scores
					 * of all the players
					 */
					WordRacerServer.wordIsUsed((String)incoming.getData(), num);
					WordRacerServer.sendNewPlayerList();
					
				}
			}
			catch (Exception e) {        //if the player disconnects while loop ends
				stillConnected = false;
				
				WordRacerServer.checkNewClient();
			}
		}
	}
}
