import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Color;

public class EndGame extends JPanel{

	private TextBox[] names;				//For each player to enter their name
	private byte modInd;						//Index of text box in names[] which is being modified

	//--Initialize--//
	
	public EndGame(){
		//names = new TextBox[DungeonQuest.p.numPlayers()];//One name for each player
		names = new TextBox[1];
		for(byte i = 0; i < names.length; i++){
			names[i] = new TextBox((byte)10);//Allow for max. 10-length names
			names[i].setContents("Player " + (i + 1));
		}
		modInd = 0;
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
		//Draw names
			//Set Font
		g.setFont(new Font("Pristina", Font.PLAIN, 36));
		for(byte i = 0; i < names.length; i++)
			names[i].draw(g, 100, i * 100 + 200);
	}
	
	//--Access--//
	
	//--Mutate--//
	
	//pre:
	//post: Performs any actions needed based on key press
	public void keyPress(KeyEvent e){
		//Tab (and dab)
			//Advance index of names[] which is being typed into
		if(e.getKeyCode() == KeyEvent.VK_TAB){
			modInd++;
			if(modInd == names.length)
				modInd = 0;
		}
		//Backspace
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			names[modInd].remove();
		//Add key char to current textbox (alphabetical chars only)
			//All valid key codes (of alpha chars) are less than or equal to 90
		if(e.getKeyCode() < 90)
			names[modInd].add(e.getKeyChar());
		//Update any graphical changes
		repaint(0, 0, 1200, 750);
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
		public void draw(Graphics g, int x, int y){
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