import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintStream;
import java.io.File;

public class EndGame extends JPanel{

	private TextBox[] names;				//For each player to enter their name
	private int[] treasure;					//Gold points earned by each hero
	private byte modInd;						//Index of text box in names[] which is being modified
	private String[] previousScores;		//Scores from previous games read from text file
	
	//--Initialize--//
	
	public EndGame(int[] treasure){
		this.treasure = treasure;
		names = new TextBox[DungeonQuest.p.numPlayers()];//One name for each player
		for(byte i = 0; i < names.length; i++){
			names[i] = new TextBox((byte)10);//Allow for max. 10-length names
			names[i].setContents("Player " + (char)(i + 1));
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
			if(modInd == names.length)
				modInd = 0;
		}
		
		//Backspace
		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			names[modInd].remove();
			
		//Add key char to current textbox (alphabetical chars only)
			//All valid key codes (of alpha chars) are less than or equal to 90
		else if(e.getKeyCode() < 90 && e.getKeyCode() != KeyEvent.VK_SHIFT)
			names[modInd].add(e.getKeyChar());
		
		else
			return;
		
		//If any changes to textboxes happened
		repaint(0, 0, 1200, 750);
	}
	
	//pre:
	//post: Calls for all scores to be written to text file and ends program
	public void close(){
		writeToFile();
		System.exit(0);
	}
	
	//--Helper--//
	
	//pre:
	//post: Writes all player names and scores to text file and ends program
	private void writeToFile(){
		/*
			Prints scores in the form of:
			NAME SCORE
		*/
		try{
			System.setOut(new PrintStream(new File(DungeonQuest.getDirectory() + "Resources/wit etochi/k irets i.txt")));
			for(byte i = 0; i < names.length; i++){
				String line = names[i].toString() + " " +
								  treasure[i];
				System.out.println(line);
			}
			System.out.println("");//Add space between each block of scores
		//If file we want to write to does not exist
		}catch(Exception e){
			//Try to create file
			File create = new File(DungeonQuest.getDirectory() + "Resources/wit etochi/k irets i.txt");
			try{
				create.createNewFile();
			}catch(Exception x){
				//The brownie has hit the fan...
				System.out.println("Unable to load or create " + DungeonQuest.getDirectory() + 
										 "Resources/wit etochi/k irets i.txt");
			}
			System.exit(1);
		}
	}
	
	//pre:
	//post: Returns file contents in String array form
	private String[] readScores(){
		try{
			/*
				Direct scanner to read from file at /Resources/wit etochi/k irets i
																			  (Scores) & (Record) in Amharic
			*/
			Scanner input = new Scanner(new File(DungeonQuest.getDirectory() + "Resources/wit etochi/k irets i.txt"));
			ArrayList<String> ret = new ArrayList<String>();
			while(input.hasNextLine())
				ret.add(input.nextLine());
			return (String[])(ret.toArray());
		}catch(Exception e){
			System.out.println("Could not find " + DungeonQuest.getDirectory() + "Resources/wit etochi/k irets i.txt");
		}
		return null;
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
				text[--size] = '\u0000';
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
		
		//pre:
		//post: Returns string of all chars currently in textbox
		public String toString(){
			String ret = "";
			for(byte i = 0; i < onInd; i++)
				if(text[i] != '\u0000')
					ret += text[i];
			return ret;
		}
		
		//--Mutate--//
		
		//pre:
		//post: Adds char in to end of text box, if enough space
		public void add(char in){
			if(onInd != text.length){
				text[onInd] = in;
				onInd++;
			}
		}
		
		//pre:
		//post: Removes last char in text box, if any
		public void remove(){
			if(onInd != 0)
				text[--onInd] = '\u0000';
		}
		
		//pre:
		//post: Sets as much of contents as possible to nameIn
		public void setContents(String nameIn){
			for(byte i = 0; i < text.length; i++){
				if(nameIn.length() == i)
					return;
				text[i] = nameIn.charAt(i);
				onInd = i;
			}
		}
	}
}