/**
 * Hen Fung (Jonathon) Ng
 * CSE 219
 * Homework# 1
 */



	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;

	import java.io.*;
import java.net.*;

/**
 * WordRacerClient class contains the gui format and 
 * is used by the client to play the game as well as takes in
 * obects from the server to perform certain actions onto the
 * gui itself.
 */
	
public class WordRacerClient extends JFrame 
							implements ActionListener {

	/**
	 * Instance variables for interaction between server
	 * and client
	 */
	private Socket connection;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	
	/**
	 * Button to join server
	 */
	private JButton join_button = new JButton("Join Server");

	/**
	 * Instantiating labels for the gui
	 */
	private JLabel word_label = new JLabel();
	private JLabel name_label = new JLabel();
	private JLabel timeLabel = new JLabel();
	private MyTimer timer;
	/**
	 * instantiating board objects and objects required
	 * to run the game
	 */
	private JPanel boardPanel = new JPanel();
	private JPanel startPanel = new JPanel(new FlowLayout());
	private JPanel wordPanel = new JPanel(new BorderLayout());
	private JPanel namePanel = new JPanel(new BorderLayout());
	private JPanel textPanel = new JPanel(new BorderLayout());
	
	/**
	 * instantiating the text Text Field
	 */
	private JTextField text = new JTextField();

	
	/**
	 * instantiating the name Text Area
	 */
	private JTextArea nameList = new JTextArea(10, 15);
	private JScrollPane nameScrollPane = new JScrollPane(nameList);
	
	/**
	 * instantiating the word List text area
	 */
	private JTextArea wordList = new JTextArea(10, 15);
	private JScrollPane wordScrollPane = new JScrollPane(wordList);
	
	
	/**
	 * instantiating chat text area
	 */
	private JTextArea chatBox = new JTextArea(15,15);
	private JScrollPane chatScrollPane = new JScrollPane(chatBox);
	
	/**
	 * chat textfield
	 */
	private JTextField chatField = new JTextField();
	
	
	/**
	 * instantiating the buttons
	 */
	private JButton[][] boardButtons = new JButton[4][4];
	private JButton startButton = new JButton("Start Game");

	private boolean is_connected = false;
	/**
	 * constructor for the WordRacerClient class
	 */
	
	public WordRacerClient() {
	  super("Jzeron's Ultimate Word Racer");
	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  setSize(700,700);
	  setupGUI();
	}

	/**
	 * This is the funcion that constructs the whole gui 
	 * layout of the game. It will create the whole template 
	 * of BorderLayouts as well as adding on te textareas,
	 * textfields, and buttons
	 */
	 
	public void setupGUI(){
		//label the labels
		word_label.setText("Words Found: ");
		name_label.setText("Players: ");	
		//add label and textarea to panels east and west
		wordPanel.add(word_label, BorderLayout.NORTH);
		wordPanel.add(wordScrollPane, BorderLayout.CENTER);
		namePanel.add(name_label, BorderLayout.NORTH);
		namePanel.add(nameScrollPane, BorderLayout.CENTER);
		namePanel.add(timeLabel, BorderLayout.SOUTH);
		textPanel.add(text, BorderLayout.NORTH);
		textPanel.add(chatScrollPane,BorderLayout.CENTER);
		textPanel.add(chatField,BorderLayout.SOUTH);
		//add the main panels
		this.add(namePanel, BorderLayout.WEST);
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(wordPanel, BorderLayout.EAST);
		this.add(textPanel, BorderLayout.SOUTH);
		this.add(startPanel, BorderLayout.NORTH);
			
		//add the buttons onto the Center panel
		boardPanel.setLayout(new GridLayout(4, 4));
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				boardButtons[i][j] = new JButton("");
				boardButtons[i][j].addActionListener(this);
				boardButtons[i][j].setActionCommand("" + i + "" + j);
				boardPanel.add(boardButtons[i][j]);
			}
		}
			
		// add action listener to the start button and the text field
		startButton.addActionListener(this);
		startButton.setActionCommand("START BUTTON");
		join_button.addActionListener(this);
		join_button.setActionCommand("JOIN BUTTON");
		startPanel.add(join_button);
		startPanel.add(startButton);
		text.addActionListener(this);
		chatField.addActionListener(this);
			
		//start timer
	
	}

	/**
	 * This function allows the class to update the timer of the 
	 * game to make the timer appear on the label as a moving timer.
	 * @param time
	 */
	public void updateTime(String time){
		timeLabel.setText("Time Left: " + time);
	}

	/**
	 * This function ends the game by disabling all active gui related to
	 * the game and displaying the winner for all the clients to see
	 *
	 */
	public void endGame(String player){
		startButton.setEnabled(true);
		text.setEnabled(false);
		JOptionPane.showMessageDialog(this, player);
		
	}
	
	
	/**
	 * addtext adds text from other users onto the chatbox
	 * @param text
	 */

	
	  public void addText(String text) {
			chatBox.append(text + "\n");
	  }
	/**
	 * This function allows the client to connect to the server
	 *
	 */
	public void connect() {
		try {
			connection = new Socket("localhost", 8000); 
			//ip adress to check
			OutputStream os = connection.getOutputStream();  
			//sends out objets
			writer = new ObjectOutputStream(os);
			InputStream is = connection.getInputStream(); 
			//receives objects
			reader = new ObjectInputStream(is);
			WordRacerClientThread listener           //listener is the thread that
					= new WordRacerClientThread(reader, this); //uses reader to
			listener.start();                                //find outt what is needed
			//from the server. It takes in server commands
			//listener.start() makes the run function start over and over
			
			WordRacerNetworkObject outgoing_playerlist = new WordRacerNetworkObject(
					WordRacerNetworkObject.NEW_PLAYER); //create a Networkobject to send
			//data to server for server to decide what should be done to all clients with
			//player updates
			WordRacerNetworkObject outgoing_newboard = new WordRacerNetworkObject(
					WordRacerNetworkObject.NEW_BOARD);

			/**
			 * check on how to make new board.
			 */
			writer.writeObject(outgoing_playerlist);
			writer.writeObject(outgoing_newboard);
			//writes object to server class
	    }
	    catch(IOException ioe){ 
	    	ioe.printStackTrace(); //exception that might occur
	    }
	  }
		
	/**
	 * actionPerformed is for the buttons such as start game and 
	 * for the textarea where the words are inputted to be searched.
	 */
	public void actionPerformed(ActionEvent ae) {

	
		String command = ae.getActionCommand(); //stores ae which decides which gui
		                               //button is clicked on
		
		String chatMessage = chatField.getText();
		chatField.setText("");


		
		String wordMessage = text.getText(); //this takes in the message from the textfield
		text.setText(""); //textfield is set to "" after enter is pressed

		try{
			if(command.equals("JOIN BUTTON")){
				connect();
				join_button.setEnabled(false);
			}
			if(command.equals("START BUTTON")){  //sends out that the startbutton is pressed
				WordRacerNetworkObject outgoing = new WordRacerNetworkObject(
						WordRacerNetworkObject.START_BUTTON);
				writer.writeObject(outgoing);
			} 
			if(wordMessage.length() != 0){   //sends out the word to be checked
				WordRacerNetworkObject outgoing = new WordRacerNetworkObject(
						WordRacerNetworkObject.CHECK_WORD, wordMessage);
				writer.writeObject(outgoing);
			}
			if(chatMessage.length() != 0){
				WordRacerNetworkObject outgoing = new WordRacerNetworkObject(
						WordRacerNetworkObject.CHAT_MESSAGE, chatMessage);
				writer.writeObject(outgoing);
			}
		}catch(IOException ioe){
			
		}
	}
			
	/**
	 * update PlayerList consistently changes the playerlist textarea
	 * so that a full list of how many players are actually playing the 
	 * game
	 * @param text
	 */
	public void updatePlayerList(String text) {
		nameList.setText("");  //clears list
		nameList.append(text);  //replaces the list with new player list
	}
	
	/**
	 * updateScore adds onto the score everytime a player correctly inputs
	 * a word
	 * @param text
	 */
	public void updateScore(String text) {
		wordList.setText("");
		wordList.append(text);
	}
	/**
	 * setBoard sets characters to each button on the board
	 * @param letters
	 */
	public void setBoard(char letters[][]){
		for(int i = 0; i <4; i++){  //double for loop to add text to the matrix button
			for(int j = 0; j < 4; j++){
				boardButtons[i][j].setText("" + letters[i][j]);
			}
		}
	}
	
	/**
	 * Disable the start button
	 *
	 */
	public void disableStartButton(){
		startButton.setEnabled(false);
		text.setEnabled(true);
	}
	
	public void displayGameFull(){
		JOptionPane.showMessageDialog(this, "Game is full! Try again later.");
		System.exit(0);
	}
			
	/**
	 * This is the main function which allows the gui to be seen and
	 * creates a new frame.
	 * @param args
	 */
	public static void main(String[] args) {
		WordRacerClient frame = new WordRacerClient();
		frame.setVisible(true);
	}
}




