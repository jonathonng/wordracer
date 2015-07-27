/**
 * Hen Fung (Jonathon) Ng
 * CSE 219
 * Homework# 1
 */


import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * MyTimer class was taken from the professor and was originally
 * a timer that started from 0 and goes to 2:00 but was switched
 * around to go backwards this will b e used by tthe server class
 * to update the gui on client class so that the client
 * may only play a game for 2:00
 * @author Jon
 *
 */
public class MyTimer extends Thread{
	private Calendar startTime;  //class Calendar starttime object
	private boolean die = false;
	
	public MyTimer(){
	}
	
	public void kill() {die = true;}
	
	public void run(){
		startTime = new GregorianCalendar();
		Calendar endTime;        //creates endTime Calendar object
		long timeDifference;
		while(!die){
			try{
				endTime = new GregorianCalendar();
				timeDifference = endTime.getTimeInMillis() - startTime.getTimeInMillis();
				/**
				 * subtracted 1 and 60 so that the numbers would go backwards
				 */
				String minutes = ""+ (1 - (timeDifference/60000));
				String seconds = ""+ (60 - ((timeDifference/1000)%60));
				/**
				 * this is for the last part where minutes goes to -1 at the very end
				 * of the timer
				 */
				if(minutes.equals("-1")){
					minutes = "0";
				}
				/**
				 * this is to get rid of 60 by making it just to 00
				 */
				if(seconds.equals("60")){
					seconds = "00";
				}
				/**
				 * this makes it so that if the seconds length is 1 it will add another 0
				 */
				if(seconds.length() == 1){
					seconds = "0" + seconds;
				}
				String timeDisplay = minutes + ":" + seconds; // minutes and seconds are stored
				if(timeDifference == 0){ //if the timer just started timedisplay will be 2:00
				timeDisplay = "2:00";
				}
				WordRacerServer.updateGameTimer(timeDisplay); //timer is updated to the client class
				if(timeDifference >= (1000 * 60 * 2)){ //if timer is at 2 minutes then thread is killed
					kill();
					WordRacerServer.determineWinner();
				}
				sleep(1000); //if not, wait 1 second and do it again
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}
}
