import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;

public class EndGame extends JPanel{

	private TextBox[] names;				//For each player to enter their name
	private int[] treasure;					//Gold points earned by each hero
	private byte modInd;						//Index of text box in names[] which is being modified
	private Score[] previousScores;		//Scores from previous games read from text file
	
	//--Initialize--//
	
	public EndGame(int[] treasure){
		this.treasure = treasure;
		names = new TextBox[DungeonQuest.p.numPlayers()];//One name for each player
		for(byte i = 0; i < names.length; i++){
			names[i] = new TextBox((byte)10);//Allow for max. 10-length names
			names[i].setContents("Player " + (i + 1));
		}
		modInd = 0;
		previousScores = readScores();
	}
	
	//--Graphics--//
	
	//pre: g != null
	//post: Draws all textboxes and stuff
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//------//
		//Draw background
		g.drawImage(DungeonQuest.loadImage("Menu/MenuScreen.png"), 0, 0, 1200, 750, null);
		
		//Draw title
		g.setFont(new Font("Pristina", Font.PLAIN, 48));
		g.setColor(new Color(127, 0, 0));
		g.drawString("The Legend Continues...", 100, 100);
		
		//Draw names and other info
			//Set Font
		g.setFont(new Font("Pristina", Font.PLAIN, 36));
		g.drawString("Gold earned:", 450, 150);
		for(byte i = 0; i < names.length; i++){
			//Tell textbox to draw text cursor if needed
			if(i == modInd)
				names[i].draw(g, 100, i * 100 + 200, true);
			else
				names[i].draw(g, 100, i * 100 + 200, false);
			//Draw out score of player
			g.drawString(treasure[i] + "", 500, i * 100 + 200);
		}
		
		//Draw previous scores
		for(byte i = 0; i < previousScores.length; i++){
			if(previousScores[i] != null)
				g.drawString(previousScores[i].toString(), 600, i * 50 + 200);
		}
	}
	
	//--Access--//
	
	//--Mutate--//
	
	//pre:
	//post: Performs any actions needed based on key press
	public void keyPress(KeyEvent e){
		//Enter
			//Advance index of names[] which is being typed into
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			modInd++;
			//If all textboxes have been modified, write to file and end program
			if(modInd == names.length){
				writeToFile();
				System.exit(0);
			}
		}
		
		//Backspace
		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			names[modInd].remove();
			
		//Add key char to current textbox (alphabetical chars only)
			//All valid key codes (of alpha chars) are less than or equal to 90
		else if(e.getKeyCode() < 90){
				//Ignore SPACE, SHIFT, UP, LEFT, RIGHT, and DOWN keys
			switch(e.getKeyCode()){
				case(KeyEvent.VK_SPACE): 	;
				case(KeyEvent.VK_SHIFT): 	;
				case(KeyEvent.VK_UP): 		;
				case(KeyEvent.VK_LEFT): 	;
				case(KeyEvent.VK_RIGHT):	;
				case(KeyEvent.VK_DOWN): return;
			}
			names[modInd].add(e.getKeyChar());
		
		}else
			return;
		
		//If any changes to textboxes happened
		repaint(0, 0, 1200, 750);
	}
	
	//--Helper--//
	
	//pre:
	//post: Writes high scores (player names and scores) to text file (sorted by score)
	private void writeToFile(){
		/*
			Prints scores in the form of:
			NAME SCORE
		*/
		try{
			System.setOut(new PrintStream(new File(DungeonQuest.getDirectory() + "Resources/wit etochi/k irets i")));
			//Sort all scores, previous and current
			Score[] highScores = new Score[5];
			Score[] allScores = new Score[names.length + previousScores.length];
				//Add current scores to allScores
			for(byte i = 0; i < names.length; i++)
				allScores[i] = new Score(names[i].toString(), treasure[i]);
				//Add previousScores to allScores
			for(byte i = (byte)names.length; i < allScores.length; i++)
				allScores[i] = previousScores[i - names.length];
				//Actual sorting
			for(byte i = 0; i < 5; i++){
				byte highestInd = -1;//Index of highest non-null score in allScores[]
				int highestScore = -1;//Highest non-null score
				for(byte j = 0; j < allScores.length; j++){
					if(allScores[j] == null)
						continue;
					if(allScores[j].getScore() >= highestScore){
						highestScore = allScores[j].getScore();
						highestInd = j;
					}
				}
				if(highestInd != -1){//If a score is found
					highScores[i] = allScores[highestInd];
					allScores[highestInd] = null;
				}else
					break;
			}
			//Printing scores
			for(byte i = 0; i < highScores.length; i++){
				if(highScores[i] == null)
					break;
				System.out.println(highScores[i]);
			}
				
		//If file we want to write to does not exist
		}catch(Exception e){
			//Try to create file
			try{
				File create = new File(DungeonQuest.getDirectory() + "Resources/wit etochi/k irets i");
				create.createNewFile();
			}catch(Exception x){
				//The brownie has hit the fan...
				System.out.println("Unable to load or create " + DungeonQuest.getDirectory() + 
										 "Resources/wit etochi/k irets i");
			}
			System.exit(1);
		}
	}
	
	//pre:
	//post: Returns file contents of "k irets i" in Score array form
	private Score[] readScores(){
		try{
			/*
				Direct scanner to read from file at /Resources/wit etochi/k irets i
																			  (Scores) & (Record) in Amharic
			*/
			Scanner input = new Scanner(new File(DungeonQuest.getDirectory() + "Resources/wit etochi/k irets i"));
			Score[] ret = new Score[5];//Read/show top 5 scores
			for(byte i = 0; i < 5; i++){
				if(! input.hasNextLine())
					break;
				//Add new score, splitting NAME and SCORE component of read in line
				String line = input.nextLine();
					//If reached end of file (at println carriage return)
				if(line.equals(""))
					break;
				ret[i] = new Score(line.substring(0, line.indexOf(" ")),								//Name component
										Integer.parseInt(line.substring(line.indexOf(" ") + 1)));	//Score component
			}
			return ret;
		}catch(FileNotFoundException e){
			//Return blank score array. Score file will be created in writeToFile()
			return new Score[0];
		}
	}
	
	//--TextBox Class--//
	
	//A drawable textbox, which can hold chars
	
	private class TextBox{
	
		char[] text;			//Holds chars for textbox
		byte onInd;				//Number of chars in textBox
	
		//--Initialize--//
	
		/*
			ARGS: size is maximum number of chars 
					which can be entered in text box
		*/
		public TextBox(byte size){
			text = new char[size];
			while(size > 0)
				text[--size] = '\0';
			onInd = 0;
		}
		
		//--Graphics--//
		
		//pre: g != null
		//post: Draws textbox at (x, y) in graphics
		public void draw(Graphics g, int x, int y, boolean drawCursor){
			if(drawCursor)
				g.drawString(toString() + "/", x, y);
			else
				g.drawString(toString(), x, y);
		}
		
		//--Access--//
		
		//--Mutate--//
		
		//pre:
		//post: Adds char in to end of text box, if enough space
		public void add(char in){
			if(onInd < text.length){	
				text[onInd] = in;
				onInd++;
			}
		}
		
		//pre:
		//post: Removes last char in text box, if any
		public void remove(){
			if(onInd != 0)
				onInd--;
			text[onInd] = '\0';
		}
		
		//pre:
		//post: Sets as much of contents as possible to nameIn
		public void setContents(String nameIn){
			for(byte i = 0; i < text.length; i++){
				if(nameIn.length() == i)
					return;
				text[i] = nameIn.charAt(i);
				onInd = (byte)(i + 1);
			}
		}
		
		//--toString--//
		
		//pre:
		//post: Returns string of all chars currently in textbox
		public String toString(){
			String ret = "";
			for(byte i = 0; i < text.length; i++){
				if(text[i] == '\0')
					return ret;
				else
					ret += text[i];
			}
			return ret;
		}
	}
	
	//--Score Class--//
	
	//A score read in from scores text file
	
	public class Score{
	
		private final String name;				//Name of player
		private final int score;				//Score of player
		
		//--Initliaize--//
		
		public Score(String name, int score){
			this.name = name;
			this.score = score;
		}
		
		//--Access--//
		
		//pre:
		//post: Returns score
		public int getScore(){
			return score;
		}
		
		//--toString--//
		
		//pre:
		//post: Returns string representation on score
		public String toString(){
			return name + " " + score;
		}
	}
}