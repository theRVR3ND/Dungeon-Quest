//The contents of the JFrame in driver program

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class Panel extends JPanel{
	
	private final BufferedImage bgImg;						//Background image
	private Hero[] players;										//All players in the game
	private SparseMatrix<Tile> grid;							//Matrix for all tile spots on board. Basically the game board.
	private String message;										//Any text to display to board
	private byte messageUpdates;								//Number of screen updates message should be displayed for
	private SunToken sunToken;                         //"Chip" used to represent/advance time
	
	private final int boardX = 50,							//Used for easier shifting of board position
							boardY = 70;
   
   //--Initialize--//

   public Panel(String[] playerNames){
		grid = new SparseMatrix<Tile>(10, 13);
      sunToken = new SunToken();
      bgImg = DungeonQuest.loadImage("Board/Board.png");
      
      //Create heros based on names from menu
      players = new Hero[playerNames.length];
      for(int i = 0; i < playerNames.length; i++)
		   players[i] = new Hero(playerNames[i]);
			
      //Place player(s) in corners of grid, in clockwise fashion from top left corner
      int setR = 0,//Row position to set Hero at 
          setC = 0;//Column
      for(int i = 0; i < players.length; i++){
         players[i].setPosition(setR, setC);
         //Move set position clockwise in corners
         if(setR == 0){
            if(setC == 0)//If at top left
               setC = 9;
            else if(setC == 9){//If at top right
               setR = 12;
            }
         }else{//if(setR == 12)
            if(setC == 9)//If at bottom right
               setC = 0;
         }
      }
      //Add starting corner towers
		//						Center	Top,	  Right,  Bottom, Left
      grid.add(new Tile("SOLID", "EXIT", "OPEN", "OPEN", "EXIT"), 0, 0);		//Top left tower
      grid.add(new Tile("SOLID", "EXIT", "EXIT", "OPEN", "OPEN"), 0, 12);		//Top right
      grid.add(new Tile("SOLID", "OPEN", "OPEN", "EXIT", "EXIT"), 9, 0);		//Bottom left
      grid.add(new Tile("SOLID", "OPEN", "EXIT", "EXIT", "OPEN"), 9, 12);		//Bottom right
   }
   
   //--Graphics--//
   
   //pre: g != null
   //post: Updates screen once, while moving all neccessary things
   public void paintComponent(Graphics g){
      super.paintComponent(g);
      //------//
		//Draw background
      g.drawImage(bgImg, 0, 0, 1200, 750, null);
      
		//Draw all tiles
      for(int i = 0; i < grid.size(); i++){
         int[] coord = grid.locationOf(grid.get(i));//In row, column form
         g.drawImage(((Tile)(grid.get(i))).getImage(), coord[1] * 60 + boardX, coord[0] * 60 + boardY, null);
      }
		
		//Draw rectangle around board area
		g.setColor(new Color(188, 175, 120));
		g.drawRect(boardX - 10, boardY - 10, 800, 620);
		
      //Draw all heros
      for(Hero h : players)
         h.draw(g);
			
		//Draw message
		if(message != null && messageUpdates > 0){
			g.setFont(new Font("Pristine", Font.PLAIN, 20));
			g.drawString(message, boardX + 800, boardY + 590);
			messageUpdates--;
		}
   }
   
	//--Access--//
	
   //--Mutate--//
   
	//pre: message != null
	//post: Sets message to message and resets update counter
	public void setMessage(String message){
		this.message = message;
		messageUpdates = 100;
	}
	
   //pre:
   //post: Goes through each player, letting them take a turn
   public void turn(){
      sunToken.move();//Advance day if needed
   }
   
   //pre: k is a valid key code
   //post: Performs an action(s) based on key press with key code k
   public void keyPressed(int k){
   
   }
	
	//pre: 
	//post: Determines which tile on board was clicked (if any) and passes on click coordinates to tile.
	public void mouseClick(int x, int y){
		//Figure out which tile is being clicked
		int cR = (y - 50) / 60;//Click column (of board)
		int cC = (x - 50) / 60;//		 row
		
		//Check if any tile is there
		if(grid.get(cR, cC) == null)
			return;
			
		//Send tile at location mouse coordinates, RELATIVE TO TILE
		grid.get(cR, cC).mouseClick(x - cC * 60 - boardX, y - cR * 60 - boardY);
	}
   
   //--Helper--//
	
   //--Sun Token Class--//
   
   private class SunToken{
      
      private int trackPosition;                      //Spaces advanced along sun track
      private final int[] probs = {1};
      
      public SunToken(){
         trackPosition = 0;
      }
      
      //--Mutate--//
      
      //pre:
      //post: Advances sun token based on probability
      public void move(){
         if(DungeonQuest.rollDice(1) <= probs[trackPosition])
            trackPosition++;
      }
   }
}