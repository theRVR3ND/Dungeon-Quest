//The contents of the JFrame in driver program

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class Panel extends JPanel{
	
	private final BufferedImage bgImg;						//Background image
	private Hero[] players;										//All players in the game
	private byte turnInd;										//Index of player (in players) who has the turn
	private SparseMatrix<Tile> grid;							//Matrix for all tile spots on board. Basically the game board.
	private String message;										//Any text to display to board
	private byte messageUpdates;								//Number of screen updates message should be displayed for
	private SunToken sunToken;                         //"Chip" used to represent/advance time
	
	public static final byte boardX = 50,					//Used for easier shifting of board position
							 		 boardY = 70;
   
   //--Initialize--//

   public Panel(String[] playerNames){
		grid = new SparseMatrix<Tile>((byte)10, (byte)13);
      sunToken = new SunToken();
      bgImg = DungeonQuest.loadImage("Board/Board.png");
  		turnInd = 0;    
		
      //Create heros based on names from menu
      players = new Hero[playerNames.length];
      for(byte i = 0; i < playerNames.length; i++)
		   players[i] = new Hero(playerNames[i]);
			
      //Place player(s) in corners of grid, in clockwise fashion from top left corner
      byte setR = 0,//Row position to set Hero at 
           setC = 0;//Column
      for(byte i = 0; i < players.length; i++){
         players[i].setPosition(setR, setC);
         //Move set position clockwise in corners
         if(setR == 0){
            if(setC == 0)//If at top left
               setC = 12;
            else if(setC == 12){//If at top right
               setR = 9;
            }
         }else{//if(setR == 9)
            if(setC == 12)//If at bottom right
               setC = 0;
         }
      }
      //Add starting corner towers
		//						Center	Top,	  Right,  Bottom, Left
      grid.add(new Tile("SOLID", "EXIT", "OPEN", "OPEN", "EXIT"), (byte)0, (byte)0);		//Top left tower
      grid.add(new Tile("SOLID", "EXIT", "EXIT", "OPEN", "OPEN"), (byte)0, (byte)12);		//Top right
      grid.add(new Tile("SOLID", "OPEN", "OPEN", "EXIT", "EXIT"), (byte)9, (byte)0);		//Bottom left
      grid.add(new Tile("SOLID", "OPEN", "EXIT", "EXIT", "OPEN"), (byte)9, (byte)12);		//Bottom right
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
      for(byte i = 0; i < grid.size(); i++){
         byte[] coord = grid.locationOf(grid.get(i));//In row, column form
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
		
		//------//
		if(messageUpdates > 0 || ! players[turnInd].doneGliding())
			repaint(0, 0, 1200, 750);
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
		int cR = (y - boardY) / 60,//Click column (of board)
			 cC = (x - boardX) / 60;//		 row
		
		//Check if tile clicked "contains" Hero whom has the turn
		if(cR != players[turnInd].getRow() || cC != players[turnInd].getColumn())
			return;
			
		//Check if any tile is there
		if(grid.get((byte)cR, (byte)cC) == null)
			return;
			
		Tile curr = grid.get((byte)cR, (byte)cC);
		
		//Send tile at location mouse coordinates, RELATIVE TO TILE
		byte direction = curr.mouseClick(x - cC * 60 - boardX, y - cR * 60 - boardY);
		
		//Do stuff based on direction of click (and tile contents)
		if(direction == 0){
			searchTile((byte)cR, (byte)cC);
		}else if(direction == 1){//North
			if(performAction(curr.actionNeeded(direction)))
				moveHero((byte)1);
		}else if(direction == 2){//East
			if(performAction(curr.actionNeeded(direction)))
				moveHero((byte)2);
		
		}else if(direction == 3){//South
			if(performAction(curr.actionNeeded(direction)))
				moveHero((byte)3);
		
		}else if(direction == 4){//West
			if(performAction(curr.actionNeeded(direction)))
				moveHero((byte)4);
		
		}
	}
   
   //--Helper--//
	
	private boolean performAction(char in){
		return true;
	}
	
	//pre: dir = 1, 2, 3, or 4
	//post: Moves current turn hero in direction dir, if possible
	private void moveHero(byte dir){
		if(dir == 1)//North
			players[turnInd].move((byte)-1, (byte)0);
		
		else if(dir == 2)//East
			players[turnInd].move((byte)0, (byte)1);
		
		else if(dir == 3)//South
			players[turnInd].move((byte)1, (byte)0);
			
		else if(dir == 4)//West
			players[turnInd].move((byte)0, (byte)-1);
			
		//Update screen to reflect changes
		repaint(0, 0, 1200, 750);
	}
	
	private void searchTile(byte r, byte c){
		//Draw search card
	}
	
	private boolean drawDoor(){
		return true;
	}
	
   //--Sun Token Class--//
   
   private class SunToken{
      
      private byte trackPosition;                     //Spaces advanced along sun track
      private final int[] probs = {1};
      
      public SunToken(){
         trackPosition = 0;
      }
      
      //--Mutate--//
      
      //pre:
      //post: Advances sun token based on probability
      public void move(){
         if(DungeonQuest.rollDice((byte)1) <= probs[trackPosition])
            trackPosition++;
      }
   }
}