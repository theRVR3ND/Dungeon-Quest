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
	private int[] treasure;										//Amount of treasure for each Hero
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
		treasure = new int[playerNames.length];
		
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
		if(DungeonQuest.e != null)
			return;
      super.paintComponent(g);
      //------//
		//Wait until enough time has passed since last call
		while(System.currentTimeMillis() < lastUpdate + 1000.0 / MAX_FPS){}
		
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
      
		//Draw out current hero info
		g.setFont(new Font("Pristina", Font.PLAIN, 35));
		g.setColor(new Color(127, 0, 0));
		if(players[turnInd] != null){
			g.drawString(players[turnInd].getName(), boardX + 800, boardY + 20);
				//Draw out health
			for(byte i = 0; i < players[turnInd].getHealth(); i++){
				BufferedImage heart = DungeonQuest.loadImage("Heros/HealthHeart.png");
				g.drawImage(heart, boardX + 800 + 20 * i, boardY + 40, null);
			}
		}
		
		//Draw message
		g.setFont(new Font("Pristina", Font.PLAIN, 30));
		if(message != null){
			messageUpdates--;
			if(messageUpdates <= 30){
				double fade = (30 - messageUpdates) / 30.0;//Make message fade as it updates more and more
				g.setColor(new Color((float)(127.0 / 255), 0, 0, 1 - (float)fade));
			}
         //Split message with carriage return (if present), draw with seperate line each time
         if(message.indexOf("\n") < 0){//If no carriage returns present
            g.drawString(message, boardX + 800, boardY + 600);
				
			//If at least one carriage return
         }else{
            byte lineNum = 0;//Line of text we are one (for drawing)
            byte lastReturn = 0;//Index of carriage return per line
            do{
               //Draw each section between carriage returns, with 40 pixel vertical spacing
   			   g.drawString(message.substring(lastReturn, message.indexOf("\n", lastReturn + 1)), 
                            boardX + 800, boardY + 550 + lineNum * 40);
               lastReturn = (byte)message.indexOf("\n", lastReturn + 1);//Find next carriage return
               lineNum++;
            }while(lastReturn >= 0 && message.indexOf("\n", lastReturn + 1) >= 0);
				//Draw last line section (it is not drawn in while loop)
              g.drawString(message.substring(lastReturn), boardX + 800, boardY + 550 + lineNum * 40);
         }
         //"Delete" message if needed (reached end of updating cycle)
			if(messageUpdates == 0)
				message = null;
		}
		
		//------//
		
		//Check if any gliding images have not reached desired position
		boolean entitiesDoneGliding = true;
			//If any players or monsters are not done gliding
		for(byte i = 0; i < players.length; i++)
			if(players[i] != null && ! players[i].doneGliding()){
				entitiesDoneGliding = false;
				break;
			}
		if(entitiesDoneGliding)
			for(Monster m : monsters)
				if(! m.doneGliding()){
					entitiesDoneGliding = false;
					break;
				}
		
		//Draw stuff for combat
		if(inCombat && entitiesDoneGliding){
			//Draw clickable options for Melee, Ranged, and Magic attacks
			g.drawImage(DungeonQuest.loadImage("Board/MeleeIcon.png"), boardX + 810, boardY + 70, null);
			g.drawImage(DungeonQuest.loadImage("Board/RangeIcon.png"), boardX + 870, boardY + 70, null);
			g.drawImage(DungeonQuest.loadImage("Board/MagicIcon.png"), boardX + 930, boardY + 70, null);
		}
		
      /*
			Check if hero has glided out of dungeon area, sun has set, or is dead, 
			if so, take hero out of game and repaint to "hide" Hero (repaint over)
		*/
		for(byte i = 0; i < players.length; i++){
			if(players[i] == null)
				continue;
				//If out of dungeon
	      if(players[i].getX() < boardX - 10 || players[i].getX() > boardX + 775 || 
	         players[i].getY() < boardY - 10 || players[i].getY() > boardY + 590){
				setMessage(players[i].getName() + " leaves the Dungeon\nwith " + 
							  treasure[i] + " gold...");
							  
				//If Hero is killed
			}else if(players[i].getHealth() <= 0){
				setMessage(players[i].getName() + " has been smitten...");
				treasure[i] = 0;//Player loses all treasure if killed
				
				//If sun has set
			}else if(sun.isNight() && sun.doneGliding()){
				treasure[i] = 0;
			}else
				continue;
			//Do the following of either out of dungeon or dead
			players[i] = null;
			advanceTurn();
			entitiesDoneGliding = false;
		}
		
		//Rotate current Hero's room, if it is a rotating room
		if(entitiesDoneGliding){
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
					
				}else if(grid.get(h.getRow(), h.getColumn()).getSide((byte)0) == 'T'){//Trap room
					//Perform trap card actions based on random gen
					final byte gen = (byte)(Math.random() * 6 + 1);
					if(gen == 1){//Explosion
						setMessage("Explosion");
					}else if(gen == 2){//Crossfire trap
						setMessage("Crossfire");
					}else if(gen == 3){//Poisonous gas
						setMessage("Poisonous Gas");
					}else if(gen == 4){//Poisonous snakes
						setMessage("Poisonous Snakes");
					}else{//if(gen > 4)	Trapdoor
						setMessage("Trapdoor");
					}
					//Trap is "triggered", will be reset later
					grid.get(h.getRow(), h.getColumn()).setSide('t', (byte)0);
				}
			}
			repaint(0, 0, 1200, 750);
		}
		lastUpdate = System.currentTimeMillis();//Update time of last update
		//Check if we need to repaint screen (will stop repainting to save resources)
		if(message != null || ! entitiesDoneGliding || ! sun.doneGliding()){
			repaint(0, 0, 1200, 750);
		}
   }
	//--End graphics--//
   
	//--Access--//
	
	//pre:
	//post: Returns number of total players (in or out of game)
	public byte numPlayers(){
		return (byte)players.length;
	}
	
   //--Mutate--//
   
	//pre: message != null
	//post: Sets message to message, if master message is null and resets update counter
	public void setMessage(String message){
		this.message = message;
		messageUpdates = 50;
		//Reflect any graphical changes
		repaint(0, 0, 1200, 750);
	}
	
	//pre: 
	//post: Determines which tile on board was clicked (if any) and passes on click coordinates to tile.
	public void mouseClick(int x, int y){
		//Make sure all players and sun is done gliding before anything
		for(Hero h : players)
			if(h != null && ! h.doneGliding())
				return;
		if(! sun.doneGliding())
			return;
			
		/*
			Find coordinates of Tile to check click from (make coordinates 
			relative to tile), same as player's current coordinates
		*/
		byte cR = players[turnInd].getRow(),
			  cC = players[turnInd].getColumn();
		
		//Make sure click is not too far away from hero's tile
		if(Math.abs((cR * 60 + boardY + 30) - y) >= 100 || Math.abs((cC * 60 + boardX + 30) - x) >= 100){
			cR = -1;
			cC = -1;
		}
		
		//"Side" of tile being clicked	  
		final byte dir;
		if(grid.get(cR, cC) != null)
			dir = grid.get(cR, cC).mouseClick(x - cC * 60 - boardX, y - cR * 60 - boardY);
		else
			dir = -2;
			
		//Combat or obstacle stuff
		if(players[turnInd].getEdgeAlign()){
			//Figure out side of tile Hero is aligned with
			final byte heroDir;
			if(players[turnInd].getY() == cR * 60 + boardY - 17)			//Aligned top
				heroDir = 1;
			else if(players[turnInd].getX() == cC * 60 + boardX + 42)	//Aligned left
				heroDir = 2;
			else if(players[turnInd].getY() == cR * 60 + boardY + 47)	//Aligned bottom
				heroDir = 3;
			else if(players[turnInd].getX() == cC * 60 + boardX + 2)		//Aligned left
				heroDir = 4;
			else
				heroDir = -1;
			//--COMBAT--//
			if(inCombat){
				//Check for click on Melee, Ranged, or Magic attack icons
				if(x >= boardX + 810 && x <= boardX + 980 && y >= boardY + 70 && y <= boardY + 120){
					//Figure out which monster Hero is in combat with
					byte combatInd = -1;
					for(byte i = 0; i < monsters.size(); i++){
						if(players[turnInd].getRow() == monsters.get(i).getRow() && 
							players[turnInd].getColumn() == monsters.get(i).getColumn()){
							combatInd = i;
							break;
						}
					}
					//Check for click on buttons, then damage monster based on the click
					final byte attackValue;
					if(x <= boardX + 860){//Click on Melee
						attackValue = players[turnInd].getAttack();
						monsters.get(combatInd).changeHealth((byte)(-attackValue));
						
					}else if(x <= boardX + 920){//Click on Ranged
						attackValue = players[turnInd].getAttack();
						
					}else if(x <= boardX + 980){//Click on Magic
						attackValue = players[turnInd].getAttack();
						
					}else
						attackValue = 0;
					monsters.get(combatInd).changeHealth((byte)(-attackValue));
					setMessage("The attack does " + attackValue + " damage");
					
					//Check if monster has just been smitted
					if(monsters.get(combatInd).getHealth() <= 0){
						message += " and\nsmites the " + monsters.get(combatInd).getName();//Add on to current message
						monsters.remove(combatInd);
						players[turnInd].setEdgeAlign(false);
						inCombat = false;
						advanceTurn();
					}else{//If monster still alive
						//Monster counter attack
							//Damage hero
						final byte damage = monsters.get(combatInd).getAttack();
						players[turnInd].changeHealth((byte)(-damage));
						message += "\n" + players[turnInd].getName() + " takes\n" + damage + " damage";
					}
				}else{
					//Check if player tried to escape monster
					if(dir == heroDir){
						//Try to escape monster
						if(Math.random() < 0.7){//If unable to escape
							final byte hurt = (byte)(3 * Math.random() + 2);
							players[turnInd].changeHealth((byte)(-hurt));
							setMessage(players[turnInd].getName() + " tries to escape but is\nblocked and loses " + hurt + " health");
						}else{//If able to escape
							inCombat = false;
							players[turnInd].setEdgeAlign(false);
							moveHero(dir);
							setMessage(players[turnInd].getName() + " narrowly escapes");
						}
					}
				}
				return;
			}
			//--End Combat--//
			
			/*
				If mouse click was out of bounds, return. Only returning now because the combat process must
				track clicks on attack buttons, which otherwise would be ignored.
			*/
			if(cR == -1)
				return;
			
			//--Obstacle--//
				//If player is currently at a hole or cave-in tile
			final char tileSide = grid.get(cR, cC).getSide((byte)0);
				//Compare click direction and hero direction
			if(dir == heroDir){//Player chooses to back away from obstacle
				//DO NOTHING
			}else{//Player chooses to try and cross obstacle
				if(tileSide == 'C'){												//If crossing cave-in
					if(Math.random() < 0){
						setMessage(players[turnInd].getName() + "trips, falls and dies...");
						players[turnInd].changeHealth((byte)(-100));
					}else{
						setMessage(players[turnInd].getName() + " survives");
					}
					
				}else if(tileSide == 'H'){										//Leaping over hole
					if(Math.random() < 0){
						setMessage(players[turnInd].getName() + "trips, falls and dies...");
						players[turnInd].changeHealth((byte)(-100));
					}else{
						setMessage(players[turnInd].getName() + " survives");
					}
					
				}
			}
			players[turnInd].setEdgeAlign(false);
			moveHero(dir);
			return;
			//--End Obstacle Stuff--//
		}
		
		//Ignore out of bound clicks
		if(dir == -2)
			return;
		
		//Do stuff based on direction of click (and tile contents)
		if(dir == 0){					//Center action
			centerAction(cR, cC);
		}else{							//Side movement
			//If at inky darkness, choose random valid direction (ignore player input)
			if(grid.get((byte)cR, (byte)cC) != null &&
				grid.get((byte)cR, (byte)cC).getSide((byte)0) == 'I'){
				byte randDir = (byte)(Math.random() * 4 + 1);
					//Make sure we move to valid spot (not wall)
				for(byte i = 0; i < 4; i++){
					if(grid.get((byte)cR, (byte)cC).getSide((byte)randDir) != 'W')
						break;
					randDir++;
					if(randDir == 5)
						randDir = 1;
				}
				moveHero(randDir);
			}else//If not at inky darkness
				moveHero(dir);
		}
	}
   
   //--Helper--//
	
	//pre:
	/*
		post: Advances turnInd to match index of next non-null player in array players.
				Also, advances sun token if player one has just gone. Further, call method 
				to move monsters randomly. Called once per turn ONLY.
	*/
	private void advanceTurn(){
		//If sun has "set", tell players that
		if(sun.isNight())
			setMessage("The sun sets, and Kalladra\nsmites all the remaining heroes");
		//Check if all players are dead
		for(byte i = 0; i < players.length; i++){
			if(players[i] != null)
				break;
			//If all players are dead/out of game
			if(i == players.length - 1)
				//End game overall
				DungeonQuest.e = new EndGame(treasure);
		}
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
		//Monster wandering
		moveMonsters();
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
			if(treasure[turnInd] > 0){
				players[turnInd].move(moveR, moveC);//Make Hero glide out of dungeon like a magical unicorn
				advanceTurn();
				repaint(0, 0, 1200, 750);
			}else
				setMessage("Heros may not exit without\ntreasure...");
			return;
		}
		
		//Check if move is in bounds
		if(cR + moveR < 0 || cR + moveR >= 10 ||
			cC + moveC < 0 || cC + moveC >= 13)
			return;
		
		//Check if current tile has an opening at moving direction, or perform needed action to move
		if(side == 'W'){				//If wall
			/*
				If hero has not found a secret door, do nothing. If a secret door 
				HAS been found, allow hero to phase through walls.
			*/
			if(players[turnInd].foundSecretDoor())
				players[turnInd].setSecretDoor(false);
			else
				return;
		}
		
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
	
		//"Reset" trap if Hero is on one
		if(grid.get(cR, cC).getSide((byte)0) == 't')
			grid.get(cR, cC).setSide('T', (byte)0);
	
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
			/*
				Make sure there is no center obstacle if Hero has just moved from obstacle tile
				(Hella graphical jankiness otherwise)
			*/
			switch(grid.get(cR, cC).getSides()[0]){
				case('C'): ;
				case('H'): //Force any center EXCEPT obstacles
							  if(Math.random() < 0.3)
							     forceSides[0] = 'S';
							  else if(Math.random() < 0.3)
								  forceSides[0] = 'T';
							  else
								  forceSides[0] = 'R';
			}
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
			
			//--Monster Creation Stuff--//
			/*
				Add monster if:
					- Random probability
					- Monster not already at position
					- If going to tile is solid in center
					- If Hero was NOT just in a obstacle or monster situation
			*/
			if(grid.get((byte)(cR + moveR), (byte)(cC + moveC)).getSides()[0] == 'S'){
				boolean createMonster = true;//Should a monster be created later?
					//Check if hero just came from obstacle tile
				switch(grid.get(cR, cC).getSides()[0]){
					case('H'):
					case('C'): createMonster = false;
				}
					//Check if any monster is already at (cR + moveR, cC + moveC)
				for(Monster m : monsters){
					if(m.getRow() == cR + moveR && m.getColumn() == cC + moveC){
						createMonster = false;
						break;
					}
				}
				//Create monster
				if(createMonster && Math.random() < 10.25){
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
					players[turnInd].setEdgeAlign(true);
					inCombat = true;
					return;
				}
			}
			//--End Monster Creation Stuff--//
		}else{//If entering preexisting tile
			//Check if entering grid spot with a monster in it
			for(Monster m : monsters)
				if(m.getRow() == cR + moveR && m.getColumn() == cC + moveC){
					players[turnInd].setEdgeAlign(true);
					inCombat = true;
					repaint(0, 0, 1200, 750);
					return;//Do not advance turn
				}
		}
		//Check if entering cave-in or other obstacle
		switch(grid.get((byte)(cR + moveR), (byte)(cC + moveC)).getSides()[0]){
			case('C'): ;
			case('H'): players[turnInd].setEdgeAlign(true);//Wait on edge of tile (not in obstacle)
						  repaint(0, 0, 1200, 750);
						  return;
		}
		//Make sure hero does not clip through any more walls
		players[turnInd].setSecretDoor(false);
		//Give next player the turn
		advanceTurn();
		//Reflect any graphical changes
		repaint(0, 0, 1200, 750);
	}
	
	//pre:
	//post: Performs action dictated by contents of center of tile at (r, c) in grid
	private void centerAction(byte r, byte c){
		//Figure out tile's center contents
		final char cent = grid.get(r, c).getSides()[0];
		//Solid ground or rotating room (search)
		if(cent == 'S'){
         //Check if hero has searched twice consecutively in current room without moving
         if(players[turnInd].getNumSearches() >= 2){
				//Check if no moves are actually possible
				char[] sides = grid.get(r, c).getSides();
				if(sides[1] == 'W' && sides[2] == 'W' && sides[3] == 'W' && sides[4] == 'W'){
					setMessage(players[turnInd].getName() + " is stuck forever in the\ndungeon");
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
			final byte gen = (byte)(Math.random() * 10 + 1);
			short gold = -1;//Treasure gained from searching, -1 if no treasure found
			if(gen == 1){//Potion
				setMessage("Potion");
				
			}else if(gen == 2){//Golden guineas
				setMessage(players[turnInd].getName() + "\nfinds 10 golden guineas");
				gold = 10;
				
			}else if(gen == 3){//Giant centipede
				setMessage("Giant Centipede");
				
			}else if(gen == 4){//Ring
				setMessage(players[turnInd].getName() + "\nfinds a ring worth 90 guineas");
				gold = 90;
				
			}else if(gen == 5){//Jewellry
				setMessage(players[turnInd].getName() + "\nfinds jewellry worth 200 guineas");
				gold = 200;
				
			}else if(gen == 6){//Secret Door
				//Make sure there is at least one wall, so a secret door could exist
				boolean wallExists = false;
				for(byte i = 1; i < 5; i++){
					if(grid.get(r, c).getSides()[i] == 'W'){
						wallExists = true;
						break;
					}
				}
				if(wallExists){
					setMessage(players[turnInd].getName() + " finds a secret door");
					players[turnInd].setSecretDoor(true);
				}else{//If no wall exists in tile
					setMessage("Nothing shows up...");
				}
			}else{//if(gen > 6){	Empty
				setMessage("Nothing shows up...");
			}
			
			if(gold != -1)
				treasure[turnInd] += gold;
			
		//Treasure room (draw treasure card)
		}else if(cent == 'G'){
			if(Math.random() <= 7.0 / 8){
				final int gen = (int)(Math.random() * 45) * 10 + 100;//Treasure token
				setMessage("The Dragon slumbers and " + players[turnInd].getName() + "\ntakes " + gen + " gold");
				treasure[turnInd] += gen;
			}else{
				setMessage("Kalladra awakes and burns\n" + players[turnInd].getName() + " with Dragon Breath");
				players[turnInd].changeHealth((byte)(-100));
				repaint(0, 0, 1200, 750);
			}
		}else
			return;
		//If valid action was performed, do the following:
		advanceTurn();
	}
	
	//pre:
	//post: Moves all monsters (which are not in combat) by one row or column move randomly, if possible
	private void moveMonsters(){
		eachMonster:
			for(byte i = 0; i < monsters.size(); i++){
				Monster m = monsters.get(i);
				if(Math.random() < 0.6)//Do not always move monster
					continue;
				final byte cR = m.getRow(),		//Current row position
							  cC = m.getColumn();	//			 column
					/*
						Figure out if in combat. For each Hero, check if at same position. 
						If so, monster is in combat and should not be moved.
					*/
				for(byte k = 0; k < players.length; k++){
					if(players[turnInd] == null)
						continue;
					if(players[turnInd].getRow() == cR &&
						players[turnInd].getColumn() == cC)
						continue eachMonster;
				}
					//Random direction to move monster
				byte randDir = (byte)(Math.random() * 4 + 1);
					//Try to find first valid direction for movement (open side)
				for(byte j = 0; j < 4; j++){
					if(grid.get(cR, cC) != null &&
						grid.get(cR, cC).getSide(randDir) == 'O'){//Open side found
						//Valid move found, execute it
						byte moveR = 0, moveC = 0;//(r, c) to move monster by
						if(randDir == 1)
							moveR = -1;
						else if(randDir == 2)
							moveC = 1;
						else if(randDir == 3)
							moveR = 1;
						else if(randDir == 4)
							moveC = -1;
						//Make sure tile already exists at wanted position
						if(grid.get((byte)(cR + moveR), (byte)(cC + moveC)) == null)
							continue eachMonster;
						//Do not move to position of hero
						for(Hero h : players){
							if(h == null)
								continue;
							if(h.getRow() == cR + moveR && 
								h.getColumn() == cC + moveC)
								continue eachMonster;
						}
						//Do not move to position of another monster
						for(Monster n : monsters)
							if(n.getRow() == cR + moveR &&
								n.getColumn() == cC + moveC)
								continue eachMonster;
						m.move(moveR, moveC);
						break;
					}else{//If no opening, try next direction
						randDir++;
						if(randDir == 5)
							randDir = 0;
					}
				}
			}
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
		//post: Implements gliding if neccessary, and draws token at current x
		public void draw(Graphics g){
			//Draw path which token will follow (orange) or has already covered (dark blue)
			g.setColor(new Color(0, 0, 120));
			for(byte i = 0; i < 30; i++){
				if(i == iter)//Onto path not yet covered
					g.setColor(new Color(255, 160, 0));
				g.drawOval(Panel.boardX + i * 25 + 10, 25, 10, 10);
				if(i < 29)
					g.drawLine(Panel.boardX + i * 25 + 20, 30, Panel.boardX + i * 25 + 35, 30);
			}
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
			return iter == 29;
		}
		
		//--Mutate--//
		
		//pre:
		//post: Advances sun token along path by one step
		public void advance(){
			if(iter < 29)
				iter++;
		}
	}
}