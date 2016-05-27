import java.awt.Graphics;
import javax.swing.JPanel;

public class EndGame extends JPanel{

	private TextBox[] names;				//For each player to enter their name

	//--Initialize--//
	
	public EndGame(){
		names = new TextBox[0];
		for(byte i = 0; i < names.length; i++)
			names[i] = new TextBox((byte)10);//Allow for max. 10-length names
	}
	
	//--Graphics--//
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//------//
		System.out.println("F");
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
	//post: Performs any actions needed based on mouse click
	public void mouseClick(int x, int y){
		//Check for click on textbox area
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