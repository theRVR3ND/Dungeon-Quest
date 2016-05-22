//The contents of the JFrame in driver program

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	
	//--Combat Stuff--//
	private ArrayList<Monster> monsters;					//All monsters in dungeon
	private boolean inCombat;									//Is there combat going on?
	
	//--FPS Control Stuff--//								 
	private final byte MAX_FPS = 30;							//Maximum frame updates per second
	private long lastUpdate;									//Last time (in milliseconds) paintComponent() executed
	
   //--Initialize--//

	//ARGS: playerNames is array of names of Heros, one for each person playing
   public Panel(String[] playerNames){
		grid = new SparseMatrix<Tile>((byte)10, (byte)13);
      bgImg = DungeonQuest.loadImage("Board/Board.png");
		monsters = new ArrayList<Monster>();
		inCombat = false;
		sun = new SunToken();
  		turnInd = 0;
		lastUpdate = 0;
		
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
		//Wait until enough time has passed since last call
		while(System.currentTimeMillis() < lastUpdate + 1000.0 / MAX_FPS)//Wait so max frame updates per second is maintained
				System.out.print("");
		
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
				
		//Draw all monsters
		for(Monster m : monsters)
			m.draw(g);
      
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
         //Split message with carriage return (if present), draw with seperate line each time
         if(message.indexOf("\n") < 0){//If no carriage returns present
            g.drawString(message, boardX + 800, boardY + 600);
         }else{//If at least one carriage return
            byte lineNum = 0;//Line of text we are one (for drawing)
            byte lastReturn = 0;//Index of carriage return per line
            do{
               //Draw each section between carriage returns, with 40 pixel vertical spacing
   			   g.drawString(message.substring(lastReturn, message.indexOf("\n", lastReturn + 1)), 
                            boardX + 800, boardY + 560 + lineNum * 40);
               lastReturn = (byte)message.indexOf("\n", lastReturn + 1);//Find next carriage return
               lineNum++;
                  //If we have just drawn second to last section, draw last section and break
               if(message.indexOf("\n", lastReturn + 1) < 0){
                  g.drawString(message.substring(lastReturn), boardX + 800, boardY + 560 + lineNum * 40);
                  break;
               }
            }while(lastReturn >= 0);
         }
         //"Delete" message if needed (reached end of updating cycle)
			if(messageUpdates == 0)
				message = null;
		}
		
		//------//
		
		//Check if any gliding images have not reached desired position
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
		
		//Draw stuff for combat
		if(inCombat && playersDoneGliding){
			//Draw clickable options for Melee, Ranged, and Magic attacks
			g.drawImage(DungeonQuest.loadImage("Board/MeleeIcon.png"), boardX + 810, boardY, null);
			g.drawImage(DungeonQuest.loadImage("Board/RangeIcon.png"), boardX + 870, boardY, null);
			g.drawImage(DungeonQuest.loadImage("Board/MagicIcon.png"), boardX + 930, boardY, null);
		}
		
      /*
			Check if hero has glided out of dungeon area or is dead, 
			if so, repaint to "hide" Hero (repaint over)
		*/
		for(byte i = 0; i < players.length; i++){
			if(players[i] == null)
				continue;
				//If out of dungeon
	      if(players[i].getX() < boardX - 10 || players[i].getX() > boardX + 775 || 
	         players[i].getY() < boardY - 10 || players[i].getY() > boardY + 590){
				setMessage(players[i].getName() + " leaves the Dungeon...");
				//If Hero is killed
			}else if(players[i].getHealth() <= 0){
				setMessage(players[i].getName() + " has been smitten...");
			}else
				continue;
			//Do the following of either out of dungeon or dead
			players[i] = null;
			advanceTurn();
			playersDoneGliding = false;
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
			if(grid.get(h.getRow(), h.getColumn()) != null){
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
					
					//Make any sides touching outside walls into walls
					if(h.getRow() == 0)				//Force top wall
						sides[1] = 'W';
					else if(h.getRow() == 9)		//Force bottom wall
						sides[3] = 'W';
					else if(h.getColumn() == 0)	//Force left wall
						sides[4] = 'W';
					else if(h.getColumn() == 12)	//Force right wall
						sides[2] = 'W';
						
					//Turn room into regular type (so it does not rotate anymore)
					sides[0] = 'S';
					
					//Implement changes
					grid.get(h.getRow(), h.getColumn()).setSides(sides);
					//Reflect changes
					repaint(0, 0, 1200, 750);
				}
			}
		}
		lastUpdate = System.currentTimeMillis();//Update time of last update
		//Check if we need to repaint screen (will stop repainting to save resources)
		if(message != null || ! playersDoneGliding || ! sun.doneGliding()){
			repaint(0, 0, 1200, 750);
		}
   }
	//--End graphics--//
   
	//--Access--//
	
	//pre:
	//post: Returns true if at least one player is still in game or message is still up
	public boolean gameGoing(){
			//Check if at least one live hero
		for(Hero h : players)
			if(h != null)//All dead/exited players will be or have been set to null
				return true;
		/*
			//If message still up
		if(messageUpdates > 0){
			repaint(0, 0, 1200, 750);
			return true;
		}*/
		return false;
	}
	
   //--Mutate--//
   
	//pre: message != null
	//post: Sets message to message and resets update counter
	public void setMessage(String message){
		this.message = message;
		messageUpdates = 50;
		//Reflect any graphical changes
		repaint(0, 0, 1200, 750);
	}
	
	//pre: 
	//post: Determines which tile on board was clicked (if any) and passes on click coordinates to tile.
	public void mouseClick(int x, int y){
		//Make sure all players are done gliding before anything
		for(Hero h : players)
			if(h != null && ! h.doneGliding())
				return;
		
		//--COMBAT--// 
		
		if(inCombat){
			//Check for click on Melee, Ranged, or Magic attack icons
			if(y >= boardY && y <= boardY + 50){
				if(x >= boardX + 810 && x <= boardX + 860){//Click on Melee
					byte attackValue = players[turnInd].getAttack();
					monsters.get(monsters.size() - 1).changeHealth((byte)(-attackValue));
					setMessage("Your Melee attack does " + attackValue + "\ndamage to the monster");
					
				}else if(x >= boardX + 870 && x <= boardX + 920){//Click on Ranged
					byte attackValue = players[turnInd].getAttack();
					monsters.get(monsters.size() - 1).changeHealth((byte)(-attackValue));
					setMessage("Your Range attack does " + attackValue + "\ndamage to the monster");
				
				}else if(x >= boardX + 930 && x <= boardX + 980){//Click on Magic
					byte attackValue = players[turnInd].getAttack();
					monsters.get(monsters.size() - 1).changeHealth((byte)(-attackValue));
					setMessage("Your Magic attack does " + attackValue + "\ndamage to the monster");
				}else
					return;
				//Check if monster has just been smitted
				if(monsters.get(monsters.size() - 1).getHealth() <= 0){
					setMessage("The " + monsters.get(monsters.size() - 1).getName() + " has been smitten");
					monsters.remove(monsters.size() - 1);
					players[turnInd].setCombatState(false);
					inCombat = false;
				}
			}
			return;//Ignore everything else in screen except combat buttons
		}
		
		//--End Combat--//
		
		//Figure out which tile is being clicked
		byte cR = (byte)((y - boardY) / 60),//Click column (of board)
			  cC = (byte)((x - boardX) / 60);//		  row
		
		//Check if tile clicked "contains" Hero whom has the turn
		if(cR != players[turnInd].getRow() || cC != players[turnInd].getColumn())
			return;
			
		//Check if any tile is there
		if(grid.get((byte)cR, (byte)cC) == null)
			return;
		
		//Send tile at location mouse coordinates, RELATIVE TO TILE
		byte direction = grid.get(cR, cC).mouseClick(x - cC * 60 - boardX, y - cR * 60 - boardY);
		
		//Do stuff based on direction of click (and tile contents)
		if(direction == 0){			//Center
			centerAction(cR, cC);
			
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
		for(byte i = 0; i < players.length; i++){
			turnInd++;
			if(turnInd == players.length){
				turnInd = 0;
				sun.advance();
			}
			if(players[turnInd] != null)
				break;
		}
	}
	
	//pre: dir = 1 (North), 2 (East), 3 (South), or 4 (West)
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
				repaint(0, 0, 1200, 750);
			}else
				setMessage("You may not exit without\ntreasure...");
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
			//Draw a door card
			if(Math.random() < 0.4){
				//Unable to open door
				if(Math.random() < 0.5){
					setMessage("The door is shut tight...");
				}else{
					setMessage("It will not budge...");
				}
				advanceTurn();
				return;
			}else{
				//Player opens the door
				if(Math.random() < 0.5){
					setMessage("The door creaks open...");
				}else{
					setMessage("Creak...");
				}
			}
		}else if(side == 'P'){		//If portcullis
			//Test hero's strength
			if(players[turnInd].getStrength() > (byte)(Math.random() * 8)){
				//Opens portcullis
				if(Math.random() < 0.5){
					setMessage("The grate moves upward...");
				}else{
					setMessage(players[turnInd].getName() + " lifts the portcullis");
				}
			}else{
				//If unable to move portcullis
				if(Math.random() < 0.5){
					setMessage(players[turnInd].getName() + " is unable to lift the\nheavy grate...");
				}else{
					setMessage("The portcullis remains shut...");
				}
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
			
			//Add monster (sometimes), also only if going to tile is solid in center
			if(grid.get((byte)(cR + moveR), (byte)(cC + moveC)).getSides()[0] == 'S' && Math.random() < 0.25){
				byte gen = (byte)(Math.random() * 5);
				String name;//Name of monster
				if(gen == 0)
					name = "Demon";
				else if(gen == 1)
					name = "Golem";
				else if(gen == 2)
					name = "Skeleton";
				else if(gen == 3)
					name = "Sorcerer";
				else//if(gen == 4)
					name = "Troll";
				monsters.add(new Monster(name, (byte)(cR + moveR), (byte)(cC + moveC)));
				//Tell hero it is in combat
				players[turnInd].setCombatState(true);
				inCombat = true;
			}
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
         //Check if hero has searched twice consecutively in current room without moving
         if(players[turnInd].getNumSearches() >= 2){
				//Check if no moves are actually possible
				char[] sides = grid.get(r, c).getSides();
				if(sides[1] == 'W' && sides[2] == 'W' && sides[3] == 'W' && sides[4] == 'W'){
					setMessage(players[turnInd].getName() + " is stuck forever in the dungeon");
					players[turnInd].changeHealth((byte)(-100));
					repaint(0, 0, 1200, 750);
				}else{
	            setMessage("You must move on now...");
				}
	         return;
         }
         //Tell hero to keep track of number of searches
         players[turnInd].searched();
         //Actual probability/searching
			if(Math.random() < 0.5){
            byte gen = (byte)(Math.random() * 50 + 25);
            setMessage("You find " + gen + " pieces of gold...");
            players[turnInd].addTreasure(gen);
			}else{
				setMessage("Nothing shows up");
			}
		}else if(cent == 'G'){						//Treasure room (draw treasure card)
			if(Math.random() < 0.5){
				setMessage("The Dragon slumbers...");
				players[turnInd].addTreasure((byte)(Math.random() * 50 + 50));
			}else{
				setMessage("Kalladra awakes and burns\n" + players[turnInd].getName() + " with Dragon Breath");
				players[turnInd].changeHealth((byte)(-100));
			}
		}else if(cent == 'C'){ 						//Cave-in
			//if(Math.random() * 6 + 1 < 
		}/*else if(cent == 'S'){
		
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
	//post: Returns location in (r, c) form of location of hero in combat
	private byte[] combatLocation(){
		byte[] ret = {-1, -1};
		if(! inCombat)//If there are no combat heros at the moment
			return ret;
		//Checking for combat hero location
		for(Hero h : players){
			if(h.getCombatState()){//If Hero is in combat
				ret[0] = h.getRow();
				ret[1] = h.getColumn();
				break;
			}
		}
		return ret;
	}
	
	//--Sun Token Class--//
	
	private class SunToken{
		private int x;							//Graphical horizontal position
		private byte iter;					//How many steps along (out of 30) on "sun path"
		
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