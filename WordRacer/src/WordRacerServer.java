/**
 * Hen Fung (Jonathon) Ng
 * CSE 219
 * Homework# 1
 */



import java.util.*;
import java.io.*;
import java.net.*;

/**
 * WordRacerServer class is the class which manipulates what should happen
 * to all the clients for a certain action and what should be displayed on
 * all the clients at a certain time. The server class has the true state
 * of what all the clients should be seeing so that the clients can't cheat.
 * @author Jon
 *
 */

public class WordRacerServer {

	/**
	 * Makes an ArrayList of WordRacerServerThreads to 
	 * make multiple threads for the server class
	 */
	private static ArrayList<WordRacerServerThread> threads 
				= new ArrayList<WordRacerServerThread>(); 
	private static ArrayList<String> usedWords = new ArrayList<String>();
	private static Random rnd = new Random();
	private static MyTimer game_timer;
	private static MyStartTimer start_timer;
	
	/**
	 * instantiates a random board that will be sent to
	 * the clients after it has been created and a player 
	 * score list that will be saved as well to update
	 * the player score list.
	 */
	private static char random_board[][];
	private static int player_scores[] = new int[8];
	private static String word_list = "Word \t Points \n ";
	
	/**
	 * booleans to check if the game has started and if
	 * the input of the user is actually a word that exists
	 * on the board
	 */
	private static boolean game_started = false;
	private static boolean word_exists;
	
	
	public static void sendToAll(
			String message, int clientNumber) 
				throws IOException {
      message = "Player " 
			+ (clientNumber+1) 
			+ ": " + message;
    	WordRacerNetworkObject outgoing 
			= new WordRacerNetworkObject(
				WordRacerNetworkObject.CHAT_MESSAGE,
				message);
	  for (int i = 0; i < threads.size(); i++)
			threads.get(i).send(outgoing);
  }
	
	/**
	 * generateStartTimer starts the start timer thread after 
	 * the start button is pressed which counts down from 10 to
	 * 0
	 */
	public static void generateStartTimer(){
		game_started = false;
		start_timer = new MyStartTimer();
		start_timer.start();
		
	}

	/**
	 * generateGameTimer starts the game timer thread after
	 * the start timer ends and counts down from 2 minutes to 
	 * 0
	 */
	public static void generateGameTimer(){
		game_timer = new MyTimer();
		game_timer.start();
	}
	
	/**
	 * updateGameTimer updates the timer in the client side 
	 * so that all clients could see the exact same time
	 * that every other client is seeing.
	 * @param game_time
	 */
	public static void updateGameTimer(String game_time){
		 /**
		   * A WordRacerNetworkObject is created to send gameTimer out and using
		   * the GAME_TIMER constant 
		   */
		  WordRacerNetworkObject outgoing
		  	= new WordRacerNetworkObject(
		  			WordRacerNetworkObject.GAME_TIMER,
					game_time);
		  try {
			  for(int i = 0; i < threads.size(); i++)      //try and catch to catch
				  threads.get(i).send(outgoing);      // an IOExcepion when 
		  }                                //threads is sending the outgoing message to
		  catch(IOException e) {           //the Client Server
			  e.printStackTrace();
		  }
		
	}
	
	/**
	 * generateNewBoard generates a new board for 
	 * randomboard and so that each player will have the same 
	 * board this initiates the 26 alphabets into an array to allow
	 * a random integer to choose a random alphabet
	 */
	
	public static void generateNewBoard(){
		char alphabet[] = new char[26];
		random_board = new char[4][4];
		/**
		 * instantiating all 26 letters of the alphabet
		 * into the array called alphabet
		**/
		alphabet[0] = 'A';
		alphabet[1] = 'B';
		alphabet[2] = 'C';
		alphabet[3] = 'D';
		alphabet[4] = 'E';
		alphabet[5] = 'F';
		alphabet[6] = 'G';
		alphabet[7] = 'H';
		alphabet[8] = 'I';
		alphabet[9] = 'J';
		alphabet[10] = 'K';
		alphabet[11] = 'L';
		alphabet[12] = 'M';
		alphabet[13] = 'N';
		alphabet[14] = 'O';
		alphabet[15] = 'P';
		alphabet[16] = 'Q';
		alphabet[17] = 'R';
		alphabet[18] = 'S';
		alphabet[19] = 'T';
		alphabet[20] = 'U';
		alphabet[21] = 'V';
		alphabet[22] = 'W';
		alphabet[23] = 'X';
		alphabet[24] = 'Y';
		alphabet[25] = 'Z';
		/**
		 * Making a for loop to add a random letter
		 * of the alphabet to each piece of the 
		 * random_board matrix which will b e used
		 * to add to the client side's button's 
		 * matrix
		 */
		for(int i=0;i<4; i++){
			for(int j = 0; j < 4; j++){
				random_board[i][j] = alphabet[rnd.nextInt(25)];
			}
		}
		
		sendNewBoard(random_board);
	}
  /**
   * The sendNewBoard function sends a new Board with
   * random generated characters to the client class so
   * that the client class may use it to create a random
   * generated board.
   */
	
	
  public static void sendNewBoard(char [][] board){
	  /**
	   * A WordRacerNetworkObject is created to send the random letters out 
	   * and using the NEW_BOARD constant 
	   */
	  WordRacerNetworkObject outgoing
	  	= new WordRacerNetworkObject(
	  			WordRacerNetworkObject.NEW_BOARD,
				board);

	  game_started = true; //game_started is true if the board is set
	  				//the first time
	  
	  try {
		  for(int i = 0; i < threads.size(); i++)      //try and catch to catch
			  threads.get(i).send(outgoing);      // an IOExcepion when 
	  }                                //threads is sending the outgoing message to
	  catch(IOException e) {           //the Client Server
		  e.printStackTrace();
	  }
  }
  /**
   * WordisUsed function checks if the word is used or if the
   * word is under 3 letters long so that it shouldn't have to
   * go through the wordIsTrue function to be checked for validity
   * since it has already been used or is not a valid entry.
   * @param word
   * @param clientNum
   */
  
  public static void wordIsUsed(String word, int clientNum){
	  boolean isUsed = false;
	  for(int i =0; i < usedWords.size(); i++){ //for loop to check the usedwords
		                                          //array list
		  if((usedWords.get(i).equals(word)) || word.length() < 3 || word.length() > 16){
			                         //if statement that checks if the word is less than 3
			                        //or if the word is already used or greater than 16
			  isUsed = true;// if it isn't then the word is set to true
		  }
	  }
	  if(isUsed == false){            // if the word isn't used then
		  wordIsTrue(word, clientNum);  //wordIsTrue function is called for the
		                            // specific client
  
	  }
  }
  
  /**
   * wordIsTrue function searches for the first letter of the word 
   * throughout the whole board and calls the function checkWord each
   * time the board finds the first letter of the word that exists until
   * the whole word if found within the board. This will send the word
   * and the points of the word to the server so that it may update the
   * wordlist.
   * @param word
   * @return
   */
  
  public static void wordIsTrue(String word, int clientNum){
	  //set int variables to check matrix and store matrix locations
	  int i, j, x, y;
	  //index is to check which letter the charAt is currently at
	  int index = 0;
	  //boolean taken makes sure the current letter at a certain spot
	  //has not been used already
	  boolean taken[][] = new boolean[4][4];
	  //checks if the letter is true is really here just to set
	  //the boolean function to something
	  boolean letter_is_true;
	  letter_is_true = false;
	  //sets word_exists to false so that it will only return true
	  //if the function sets word_exists to true
	  word_exists = false;
	  //sets string result to store in the result of the wordcheck
	  //which is the word and the points
	  String result;
	  
	  //for loop that sets the taken matrix to all false
	  for(i = 0; i < 4; i++){
		  for(j = 0; j < 4; j++){
			  taken[i][j] = false;
		  }
	  }
	  /**
	   * double for loop that checks the whole board for the first letter of
	   * the word and then proceeds to check the surrounding letters for the
	   * second word and so on with the checkword function
	   */
	  for(i = 0; i < 4; i++){
		  for(j = 0; j < 4; j++){
			  if(random_board[i][j] == word.charAt(0)){
				  x = i;
				  y = j;
				  index = 1;
				  taken[i][j] = true;
				  letter_is_true = checkWord(word, taken, x, y, index);
			  }
		  }
	  }
	  //if word exists then word is added to usedWords and result is stored
	  //into word_list
	  if(word_exists == true){ //placed if statement outside so scores aren't doubled.
		  usedWords.add(word);
		  player_scores[clientNum] = player_scores[clientNum] + checkPointsOfWord(word);
		  result = word+ "\t" + checkPointsOfWord(word) + "\n";
		  word_list += result;
	  }
	  
	  /**
	   * A WordRacerNetworkObject is created to send wordList out and using
	   * the CHECK_WORD constant 
	   */
	  WordRacerNetworkObject outgoing
	  	= new WordRacerNetworkObject(
	  			WordRacerNetworkObject.CHECK_WORD,
				word_list);
	  try {
		  for(i = 0; i < threads.size(); i++)      //try and catch to catch
			  threads.get(i).send(outgoing);      // an IOExcepion when 
	  }                                //threads is sending the outgoing message to
	  catch(IOException e) {           //the Client Server
		  e.printStackTrace();
	  }
  }
  /**
   * checkPointsofWord checks how many letters are in the word that is correct and 
   * returns the amount of points that word deserves 
   * @return
   */
  
  public static int checkPointsOfWord(String word){
	  /**
	   * 3 letters = 10 points
	   * 4 letters = 20 points
	   * 5 letters = 40 points
	   * 6 letters = 70 points
	   * 7 letters = 110 points
	   * 8 letters = 150 points
	   * every other over 8 is
	   * +40 to the 150
	   * over 16 and under 3 is 0
	   * because there can not be
	   * more than 16 letters or 
	   * under 3 letters.
	   */
	  if(word.length() == 3){
		  return 10;
	  }else if(word.length() == 4){
		  return 20;
	  }else if(word.length() == 5){
		  return 40;
	  }else if(word.length() == 6){
		  return 70;
	  }else if(word.length() == 7){
		  return 110;
	  }else if(word.length() >= 8){
		  for(int i = 0; i < 9; i++){
			  if(word.length() == (8+i)){
				  return 150+(i*40);
			  }
		  }
	  }
	  return 0;
  }
  /**
   * checkWord checks the letters after the first letter of the word and checks
   * the board up, down, left, right, upleft, upright, downleft and downright to 
   * see if the next letter of the word exists and if it does it recursively goes
   * to the next letter of the word and does the same thing while checking if the
   * spots are taken and if not it will return false but if the whole loop finishes 
   * then it will return true
   * @param word
   * @param taken
   * @param x
   * @param y
   * @param index
   * @param stillTrue <--this will be false and will be set to true to keep it true
   * @return
   */
  public static boolean checkWord(String word, boolean taken[][], int x, int y, int index){
	  //a new_taken matrix is set to detect which letters are
	  //already used for each new case so that it isn't just one board
	  //that has been used but it varies for each new case that
	  //appears
	  boolean new_taken[][] = new boolean[4][4];
	  for(int i = 0; i <4; i++){
		  for(int j = 0; j < 4; j++){
			  new_taken[i][j] = taken[i][j];
		  }
	  }
	  if(index == word.length()){ //checks if the index reaches to the word length
		                  //because if it is then the whole word has been found
		                  //so true is placed in word_exists
		  word_exists =  true;
	  }
	  else if(((x-1) != -1) && random_board[x-1][y] == word.charAt(index)  && taken[x-1][y] == false){
		  new_taken[x-1][y] = true;  //checks up
		  checkWord(word, new_taken, x-1, y, index+1);
	  }
	  else if(((y-1) != -1) && random_board[x][y-1] == word.charAt(index)  && taken[x][y-1] == false){
		  new_taken[x][y-1] = true;  //checks left
		  checkWord(word, new_taken, x, y-1, index+1);
	  }
	  else if(((x+1) != 4) && random_board[x+1][y] == word.charAt(index)  && taken[x+1][y] == false){
		  new_taken[x+1][y] = true;  //checks down
		  checkWord(word, new_taken, x+1, y, index+1);
	  }
	  else if(((y+1) != 4) && random_board[x][y+1] == word.charAt(index)  && taken[x][y+1] == false){
		  new_taken[x][y+1] = true;  //checks right
		  checkWord(word, new_taken, x, y+1, index+1);
	  }
	  else if(((x-1) != -1) && ((y-1) != -1) && random_board[x-1][y-1] == word.charAt(index)  && taken[x-1][y-1] == false){
		  new_taken[x-1][y-1] = true;  //checks up left
		  checkWord(word, new_taken, x-1, y-1, index+1);
	  }
	  else if(((x-1) != -1) && ((y+1) != 4) && random_board[x-1][y+1] == word.charAt(index)  && taken[x-1][y+1] == false){
		  new_taken[x-1][y+1] = true;  //checks up right
		  checkWord(word, new_taken, x-1, y+1, index+1);
	  }
	  else if(((x+1) != 4) && ((y-1) != -1) && random_board[x+1][y-1] == word.charAt(index)  && taken[x+1][y-1] == false){
		  new_taken[x+1][y-1] = true;  //checks down left
		  checkWord(word, new_taken, x+1, y-1, index+1);
	  }
	  else if(((x+1) != 4) && ((y+1) != 4) && random_board[x+1][y+1] == word.charAt(index)  && taken[x+1][y+1] == false){
		  new_taken[x+1][y+1] = true;  //checks up
		  checkWord(word, new_taken, x+1, y+1, index+1);
	  }
	  return false; // false is sent if letter is not found
  }
  /**
   * The setNewScoreList function just cleans out the player score list and makes
   * it so that everyone's score is 0.
   *
   */
  
  public static void setNewScoreList() {
	  for(int i = 0; i < 8; i++){
		  player_scores[i] = 0;
	  }
  }
  
  /**
   * This sendNewScoreList function is used primarily for players who have just entered
   * the server so that they could update their word list.
   *
   */
  
  public static void sendNewScoreList(){
	  /**
	   * A WordRacerNetworkObject is created to send scoreList out and using
	   * the NEW_PLAYER constant 
	   */
	  WordRacerNetworkObject outgoing
	  	= new WordRacerNetworkObject(
	  			WordRacerNetworkObject.SCORE_BOARD,
				word_list);
	  try {
		  for(int i = 0; i < threads.size(); i++)      //try and catch to catch
			  threads.get(i).send(outgoing);      // an IOExcepion when 
	  }                                //threads is sending the outgoing message to
	  catch(IOException e) {           //the Client Server
		  e.printStackTrace();
	  }
	  
  }
  
  /**
   * This function sends a new Player List to the Client
   * class so that the client class may use it to update
   * the playerlist text area
   */
  
  
  
  public static void sendNewPlayerList() {
	  
	  String playerList = "Player\tScore\n"; //top line of textarea
	  
	  /**
	   * this for loop uses the size of the threads becauase
	   * that determines how many clients are connected to the
	   * server
	   */
	  for(int i = 0; i < threads.size(); i++) {
		  playerList += "Player " + (i+1) + "\t" + player_scores[i] +"\n";//this adds a client to the player
	  }                                    //list text area
	  
	  /**
	   * A WordRacerNetworkObject is created to send playerList out and using
	   * the NEW_PLAYER constant 
	   */
	  WordRacerNetworkObject outgoing
	  	= new WordRacerNetworkObject(
	  			WordRacerNetworkObject.NEW_PLAYER,
				playerList);
	  try {
		  for(int i = 0; i < threads.size(); i++)      //try and catch to catch
			  threads.get(i).send(outgoing);      // an IOExcepion when 
	  }                                //threads is sending the outgoing message to
	  catch(IOException e) {           //the Client Server
		  e.printStackTrace();
	  }
  }
  

  public static void sendIsGameFull(){
	  String isFull = "false";
	  if(threads.size() > 1){
		  isFull = "true";
	  }
	  /**
	   * A WordRacerNetworkObject is created to send playerList out and using
	   * the NEW_PLAYER constant 
	   */
	  WordRacerNetworkObject outgoing
	  	= new WordRacerNetworkObject(
	  			WordRacerNetworkObject.GAME_FULL,
				isFull);
	  try {
		  for(int i = 0; i < threads.size(); i++)      //try and catch to catch
			  threads.get(i).send(outgoing);      // an IOExcepion when 
	  }                                //threads is sending the outgoing message to
	  catch(IOException e) {           //the Client Server
		  e.printStackTrace();
	  }
	  
  }
  
  public static void checkNewClient(){
	  for(int i = 0; i < threads.size(); i++)
	   if(threads.get(i).isConnected() == false)
		   threads.remove(i);
	  sendNewPlayerList();
  }
  
  public static void determineWinner(){
	  int highScore;
	  int player = 1;
	  String result;
	  highScore = player_scores[0];
	  for(int i = 1; i < player_scores.length; i++){
		  if(player_scores[i] > highScore){
			  highScore = player_scores[i];
			  player = i+1;
		  }  
	  }
	  result = "Player "+ player +" wins!\n";
	  for(int i = 0; i < player_scores.length; i++){
		  result += "Player " + (i+1) + " Score : " + player_scores[i] + "\n";
	  }
	  /**
	   * A WordRacerNetworkObject is created to send playerList out and using
	   * the NEW_PLAYER constant 
	   */
	  WordRacerNetworkObject outgoing
	  	= new WordRacerNetworkObject(
	  			WordRacerNetworkObject.END_GAME,
				result);
	  try {
		  for(int i = 0; i < threads.size(); i++)      //try and catch to catch
			  threads.get(i).send(outgoing);      // an IOExcepion when 
	  }                                //threads is sending the outgoing message to
	  catch(IOException e) {           //the Client Server
		  e.printStackTrace();
	  }
	  
  }
  
  public static void disableStartButton(){
	  game_started = false;
	  for(int i = 0; i< player_scores.length; i++){
		  player_scores[i] = 0;
	  }
	  word_list = "Word \t Points\n";
	  usedWords.clear();
	  setNewScoreList();
	  sendNewScoreList();
	  sendNewPlayerList();
	  
	  
	  /**
	   * A WordRacerNetworkObject is created to send playerList out and using
	   * the NEW_PLAYER constant 
	   */
	  WordRacerNetworkObject outgoing
	  	= new WordRacerNetworkObject(
	  			WordRacerNetworkObject.START_BUTTON);
	  try {
		  for(int i = 0; i < threads.size(); i++)      //try and catch to catch
			  threads.get(i).send(outgoing);      // an IOExcepion when 
	  }                                //threads is sending the outgoing message to
	  catch(IOException e) {           //the Client Server
		  e.printStackTrace();
	  }
  }
  
  /**
   * This is the main function of the WordRacerServer class
   * This manages he connection data for the client servers to 
   * connect
   * @param args
   */
  public static void main(String[] args) {
	    try {
	      ServerSocket server = new ServerSocket(8000); //sets server socket route to 8000
	      while (true) {     //infinite loop to always allow clients to join
	    	Socket socket = server.accept();     //server accepts connection to client
	    	if(threads.size() < 8){
	    		WordRacerServerThread newClient      //A thread is made for the New client
	    			= new WordRacerServerThread(
	    					socket, threads.size());
	    		threads.add(newClient);          //the threads arraylist adds the new Client
	    		newClient.start();       //all threads are started for the newClient
	    	}
	    	else{
	    		OutputStream out = socket.getOutputStream();
	    		ObjectOutputStream outObj = new ObjectOutputStream(out);
	    		WordRacerNetworkObject no = new WordRacerNetworkObject(WordRacerNetworkObject.GAME_FULL);
	    		outObj.writeObject(no);
	    	}
	    	if(game_started == true){
	        	sendNewBoard(random_board);
	        }
	      }
	    } catch (IOException ioe) {ioe.printStackTrace(); }
  }
}
