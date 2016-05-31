import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;

public class EndGame extends JPanel{

	private TextBox[] names;				//For each player to enter their name
	private byte modInd;						//Index of text box in names[] which is being modified

	//--Initialize--//
	
	public EndGame(){
		names = new TextBox[DungeonQuest.p.numPlayers()];//One name for each player
		for(byte i = 0; i < names.length; i++)
			names[i] = new TextBox((byte)10);//Allow for max. 10-length names
		modInd = 0;
	}
	
	//--Graphics--//
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//------//
		System.out.println("K");
		//Draw background
		g.drawImage(DungeonQuest.loadImage("Menu/MenuScreen.png"), 0, 0, 1200, 750, null);
		//Draw names
			//Set Font and Color
		for(byte i = 0; i < names.length; i++)
			names[i].draw(g, 100, i + 100 * 50);
		//------//
		repaint(0, 0, 1200, 750);
	}
	
	//--Access--//
	
	public boolean stillWaiting(){
		return true;
	}
	
	//--Mutate--//
	
	//pre:
	//post: Performs any actions needed based on key press
	public void keyPress(int keyCode){
		//Tab (and dab)
			//Advance index of names[] which is being typed into
		if(keyCode == KeyEvent.VK_TAB){
			modInd++;
			if(modInd == names.length)
				modInd = 0;
		}
		//Add key char to current textbox (alphabetical chars only)
			//All valid key codes (of alpha chars) are less than
	}
	
	//--TextBox Class--//
	
	//A drawable textbox, which can hold chars
	
	private class TextBox{
	
		char[] text;			//Holds chars for textbox
		final byte size;		//Max number of chars stored
		byte onInd;				//Number of chars in textBox
	
		//--Initialize--//
	
		public TextBox(byte size){
			this.size = size;
			text = new char[size];
		}
		
		//--Graphics--//
		
		public void draw(Graphics g, int x, int y){
			g.drawString(toString(), x, y);
		}
		
		//--Access--//
		
		public String toString(){
			return new String(text, 0, onInd);
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
			if(onInd != 0){
				text[onInd] = '\u0000';
				onInd--;
			}
		}
	}
}