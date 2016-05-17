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
	private SunToken sun;										//Token to keep track of day
	
	public static final byte boardX = 50,					//Used for easier shifting of board position
							 		 boardY = 70;
									 
	private final byte MAX_FPS = 30;							//Maximum frame updates per second
   
   //--Initialize--//

	//ARGS: playerNames is array of names of Heros, one for each person playing
   public Panel(String[] playerNames){
		grid = new SparseMatrix<Tile>((byte)10, (byte)13);
      bgImg = DungeonQuest.loadImage("Board/Board.png");
		sun = new SunToken();
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
		//						Center	Top,		Right,  	Bottom, 	Left
      grid.add(new Tile('S', 		'E', 		'O', 		'O', 		'E'), (byte)0, (byte)0);		//Top left tower
      grid.add(new Tile('S', 		'E', 		'E',	 	'O', 		'O'), (byte)0, (byte)12);		//Top right
      grid.add(new Tile('S', 		'O', 		'O', 		'E', 		'E'), (byte)9, (byte)0);		//Bottom left
      grid.add(new Tile('S', 		'O', 		'E', 		'E', 		'O'), (byte)9, (byte)12);		//Bottom right
		//Add 2 treasure (dragon) rooms in center
		//						Center	Top,		Right,  	Bottom, 	Left
		grid.add(new Tile('G', 		'O', 		'O', 		'O', 		'O'), (byte)4, (byte)6);
		grid.add(new Tile('G', 		'O', 		'O', 		'O', 		'O'), (byte)5, (byte)6);
   }
   
   //--Graphics--//
   
   //pre: g != null
   //post: Updates screen once, while moving all neccessary things
   public void paintComponent(Graphics g){
      super.paintComponent(g);
      //------//
		final long startTime = System.currentTimeMillis();
		
		//Draw background
      g.drawImage(bgImg, 0, 0, 1200, 750, null);
      
		//Draw sun token/path
		sun.draw(g);
		
		//Draw all tiles
      for(byte i = 0; i < grid.size(); i++){
         byte[] coord = grid.locationOf(grid.get(i));//In row, column form
         g.drawImage(((Tile)(grid.get(i))).getImage(), coord[1] * 60 + boardX, coord[0] * 60 + boardY, null);
      }
		
		//Draw rectangle around board area
		g.setColor(new Color(188, 175, 120));
		g.drawRect(boardX - 10, boardY - 10, 800, 620);
		
      //Draw all heros whom are still in game
      for(Hero h : players)
			if(h != null)
         	h.draw(g);
			
		//Draw message
		if(message != null){
			messageUpdates--;
			if(messageUpdates <= 30){
				double fade = (30 - messageUpdates) / 30.0;//Make message fade as it updates more and more
				g.setColor(new Color((float)(127.0 / 255), 0, 0, 1 - (float)fade));
			}else{
				g.setColor(new Color(127, 0, 0));
			}
			g.setFont(new Font("Pristina", Font.PLAIN, 35));
			g.drawString(message, boardX + 800, boardY + 600);
			if(messageUpdates == 0)
				message = null;
		}
		
		//------//
		//Repaint if any gliding images have not reached desired position
		boolean playersDoneGliding = true;
			//If any players are not done gliding
		for(byte i = 0; i < players.length; i++){
			if(players[i] == null)
				continue;
			if(! players[i].doneGliding()){
				playersDoneGliding = false;
				break;
			}
		}
		//Rotate current Hero's room, if it is a rotating room
		if(playersDoneGliding){
				//Find "previous" player, as advanceTurn() has already been called
			byte prevInd = turnInd;
			do{
				prevInd--;
				if(prevInd < 0)
					prevInd = (byte)(players.length - 1);
			}while(players[prevInd] == null);
			
			Hero h = players[prevInd];
			if(grid.get(h.getRow(), h.getColumn()).getSide((byte)0) == 'R'){//If rotating room
				//Rotate room's sides by 180
				char[] sides = grid.get(h.getRow(), h.getColumn()).getSides();
					//Swap the opposite tile sides
				char temp = sides[1];	//Swaping top and bottom
				sides[1] = sides[3];
				sides[3] = temp;
				
				temp = sides[2];			//Swaping left and right
				sides[2] = sides[4];
				sides[4] = temp;
				
				//Turn room into regular type (so it does not rotate anymore)
				sides[0] = 'S';
				
				//Implement changes
				grid.get(h.getRow(), h.getColumn()).setSides(sides);
				//Reflect changes
				repaint(0, 0, 1200, 750);
			}
		}
		//Check if we need to repaint screen (will stop repainting to save resources)
		if(message != null || ! playersDoneGliding || ! sun.doneGliding()){
			while(System.currentTimeMillis() < startTime + 1000.0 / MAX_FPS)//Wait so max frame updates per second is maintained
				System.out.print("");
			repaint(0, 0, 1200, 750);
		}
   }
   
	//--Access--//
	
   //--Mutate--//
   
	//pre: message != null
	//post: Sets message to message and resets update counter
	public void setMessage(String message){
		this.message = message;
		messageUpdates = 100;
		//Reflect any graphical changes
		repaint(0, 0, 1200, 750);
	}
	
	//pre: 
	//post: Determines which tile on board was clicked (if any) and passes on click coordinates to tile.
	public void mouseClick(int x, int y){
		//Make sure all players are done gliding before anything
		for(Hero h : players)
			if(! h.doneGliding())
				return;
		//Figure out which tile is being clicked
		int cR = (y - boardY) / 60,//Click column (of board)
			 cC = (x - boardX) / 60;//		 row
		
		//Check if tile clicked "contains" Hero whom has the turn
		if(cR != players[turnInd].getRow() || cC != players[turnInd].getColumn())
			return;
			
		//Check if any tile is there
		if(grid.get((byte)cR, (byte)cC) == null)
			return;
		
		//Send tile at location mouse coordinates, RELATIVE TO TILE
		byte direction = grid.get((byte)cR, (byte)cC).mouseClick(x - cC * 60 - boardX, y - cR * 60 - boardY);
		
		//Do stuff based on direction of click (and tile contents)
		if(direction == 0){			//Center
			centerAction((byte)cR, (byte)cC);
			
		}else if(direction == 1){	//North
			moveHero((byte)1);
			
		}else if(direction == 2){	//East
			moveHero((byte)2);
			
		}else if(direction == 3){	//South
			moveHero((byte)3);
			
		}else if(direction == 4){	//West
			moveHero((byte)4);
		}
	}
   
   //--Helper--//
	
	//pre:
	//post: Advances turnInd to match index of next non-null player in array players
	private void advanceTurn(){
		//Advancing turnInd
		do{
			turnInd++;
			if(turnInd == players.length){
				turnInd = 0;
				sun.advance();
			}
		}while(players[turnInd] == null);
		//Make message fade soon(er), we don't want it persisting too long
		if(messageUpdates > 50)
			messageUpdates = 50;
	}
	
	//pre: dir = 1, 2, 3, or 4
	//post: Moves current turn hero in direction dir, if possible
	private void moveHero(byte dir){
		
		byte moveR = 0, moveC = 0;//Amount for hero to move
		
		//Figure out moveR and moveC
		if(dir == 1)		//Moving north
			moveR = -1;
		else if(dir == 2)	//Moving east
			moveC = 1;
		else if(dir == 3)	//Moving	south
			moveR = 1;
		else if(dir == 4)	//Moving west
			moveC = -1;
		
		byte cR = players[turnInd].getRow(),		//Hero's current row - column position
			  cC = players[turnInd].getColumn();
		char side = grid.get(cR, cC).getSide(dir);//Side symbol which hero is moving to
		
		//Check if hero is exiting dungeon
		if(side == 'E'){//If exit (of the dungeon)
			if(players[turnInd].getTreasure() > 0){
				players[turnInd].move(moveR, moveC);//Make Hero glide out of dungeon like a magical unicorn
				advanceTurn();
			}else
				setMessage("You may not exit without treasure...");
			return;
		}
		
		//Check if move is in bounds
		if(cR + moveR < 0 || cR + moveR >= 10 ||
			cC + moveC < 0 || cC + moveC >= 13)
			return;
		
		//Check if current tile has an opening at moving direction, or perform needed action to move
		if(side == 'W')				//If wall
			return;
			
		else if(side == 'D'){		//If door
			if(! openDoor()){//Draw a door card
				advanceTurn();
				return;
			}
		}else if(side == 'P'){		//If portcullis
			if(! movePortcullis()){//Test hero's strength
				advanceTurn();
				return;
			}
		}
		
		/* MOVE MUST BE VALID AT THIS POINT */
		
		//Tell turn hero to move
		players[turnInd].move(moveR, moveC);
		
		//Create tile at new location, if not previously there
		if(grid.get((byte)(cR + moveR), (byte)(cC + moveC)) == null){
			char[] forceSides = new char[5];//Sides to force. Blank spots will be filled randomly as usual.
			//Force an opening at Hero's entering direction
			byte forceOpen = (byte)(dir + 2);
			if(forceOpen >= 5)
				forceOpen = (byte)(forceOpen % 4);
			forceSides[forceOpen] = 'O';
			//Force walls on all edges of board
			if(cC + moveC == 0)			//If on left edge
				forceSides[4] = 'W';
				
			else if(cC + moveC == 12)	//If on right edge
				forceSides[2] = 'W';
				
			else if(cR + moveR == 0)	//If on top edge
				forceSides[1] = 'W';
			
			else if(cR + moveR == 9)	//If on bottom edge
				forceSides[3] = 'W';
			
			//Finally create actual tile
			grid.add(new Tile(forceSides), (byte)(cR + moveR), (byte)(cC + moveC));
		}
		
		//Give next player the turn
		advanceTurn();
		//Reflect any graphical changes
		repaint(0, 0, 1200, 750);
	}
	
	//pre:
	//post: Performs action dictated by contents of center of tile at (r, c) in grid
	private void centerAction(byte r, byte c){
		//Figure out what is in the center
		char cent = grid.get(r, c).getSide((byte)0);
		if(cent == 'S' || cent == 'R'){			//Solid ground or rotating room (search)
			if(Math.random() < 0.5){
				setMessage("You find something");
			}else{
				setMessage("Nothing shows up");
			}
		}else if(cent == 'G'){						//Treasure room (draw treasure card)
			if(Math.random() < 0.5){
				setMessage("Much gold");
				players[turnInd].addTreasure((byte)(Math.random() * 50 + 50));
			}else{
				setMessage("Rekt");
			}
		}/*else if(cent == 'S'){ 
		
		}else if(cent == 'S'){
		
		}else if(cent == 'S'){
		
		}*/
		else
			return;
		//Give next player the turn
		advanceTurn();
		//Reflect any graphical changes
		repaint(0, 0, 1200, 750);
	}
	
	//pre:
	//post: Simulates drawing of a door card
	private boolean openDoor(){
		if(Math.random() < 0.3){
			if(Math.random() < 0.5)
				setMessage("The door creaks open...");
			else
				setMessage("Creak...");
			return true;
		}else{
			if(Math.random() < 0.5)
				setMessage("It will not budge...");
			else
				setMessage("The door is shut tight...");
			return false;
		}
	}
	
	//pre:
	//post: Returns true if current hero can move a portcullis (by luck and strength), false otherwise
	private boolean movePortcullis(){
		return true;
	}
	
	//--Sun Token Class--//
	
	private class SunToken{
		private int x;							//Graphical horizontal position
		private byte iter;					//How many steps along (out of ) on "sun path"
		
		//--Initialize--//
		
		public SunToken(){
			x = Panel.boardX;
			iter = 0;
		}
		
		//--Graphics--//
		
		//pre: g != null
		//post: Implements gliding if neccessary, and draws token an current x
		public void draw(Graphics g){
			//Draw actual token
			if(x < iter * 25 + Panel.boardX)//Glide towards each position
				x++;
			g.drawImage(DungeonQuest.loadImage("Board/SunToken.png"), x, 15, null);
		}
		
		//--Access--//
		
		//pre:
		//post: Returns true if x matches position of iter
		public boolean doneGliding(){
			return x == iter * 25 + Panel.boardX;
		}
		
		//pre:
		//post: Returns true if it is night time (game over, all heros in dungeon die), false otherwise
		public boolean isNight(){
			return iter >= 30;
		}
		
		//--Mutate--//
		
		//pre:
		//post: Advances token's location by one iteration
		public void advance(){
			if(iter < 30)
				iter++;
		}
	}
}