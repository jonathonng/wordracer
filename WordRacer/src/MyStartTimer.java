/**
 * Hen Fung (Jonathon) Ng
 * CSE 219
 * Homework# 1
 */




/**
 * MyStartTimer class is a thread that runs the timer that
 * occurs after the start button is pressed so that 
 * it counts down from 10 to 0
 * @author Jon
 *
 */

public class MyStartTimer extends Thread{
	private boolean die = false;
	
	public MyStartTimer(){
	}
	
	public void kill() {die = true;}
	
	public void run(){
		//sets i to 10 because timer startst at 10
		int i = 10;
		while(!die){
			try{
				//stores the seconds into a string
				String seconds = i+"";
				//updates the timer of the gui for all clients through 
				//the wordracerserver class
				WordRacerServer.updateGameTimer(seconds);
				if(i == 0){
					//makes a new board and new gametimer
					WordRacerServer.generateNewBoard();
					WordRacerServer.generateGameTimer();
					kill();
				}
				sleep(1000); //sleep 1000 makes it stop for 1 second
				i--;
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}
}
