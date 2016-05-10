//The contents of the JFrame in driver program

import javax.swing.JPanel;
import java.awt.Graphics;

public class Panel extends JPanel{
	
	private BufferedImage bgImg;								//Background image
	private Hero[] players;										//All players in the game
	private SparseMatrix<Tile> grid;							//Matrix for all tile spots on board. Basically the game board.
	
   //--Initialize--//

   public Panel(){
		players = new Hero[DungeonQuest.numPlayers];
		for(int i = 0; i < players.length; i++){
			//Randomly find locations for each Hero in each corner of grid
			players[i] = new Hero();
		}
   }
   
   //--Mutate--//
   
   //pre: k is a valid key code
   //post: Performs an action(s) based on key press with key code k
   public void keyPressed(int k){
   
   }
}