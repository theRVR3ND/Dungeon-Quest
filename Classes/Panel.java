//The contents of the JFrame in driver program

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Panel extends JPanel{
	
	private BufferedImage bgImg;								//Background image
	private Hero[] players;										//All players in the game
	private SparseMatrix<Tile> grid;							//Matrix for all tile spots on board. Basically the game board.
	
   //--Initialize--//

   public Panel(Hero[] playersIn){
		grid = new SparseMatrix<Tile>(13, 9);
		players = playersIn;
		for(int i = 0; i < players.length; i++){
			//Randomly find locations for each Hero in each corner of grid
			players[i] = new Hero();
		}
   }
   
	//--Access--//
	
   //--Mutate--//
   
   //pre: k is a valid key code
   //post: Performs an action(s) based on key press with key code k
   public void keyPressed(int k){
   
   }
	
	//pre: 
	//post: Determines which tile on board was clicked (if any) and passes on click coordinates to tile.
	public void mouseClick(int x, int y){
	
	}
}