//A "dungeon card" on the game board

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile{

	private BufferedImage img;	      //Image of this tile
	private char[] sides;				/*
													The contents of the 0 (Middle), 1 (North), 2 (East),
													3 (South), 4 (West) sides of this tile.
													Center contents:
														Solid = 'S'
														Hole = 'H'
														Cave-in = 'C'
														Trap = 'T'
														Inky Hole = 'I'
														Dragonfire Dungeon (GOLD!) = 'G'
														Rotating = 'R'
													Wall contents: 
														Wall = 'W'
														Open = 'O'
														Door = 'D'
														Portcullis = 'P'
														Exit = 'E'
												*/

	//--Initialize--//
   
   //ARGS: tileType is name of folder in DungeonQuest -> Resources -> Board -> Tiles folder
	public Tile(){
		sides = new char[5];
      randomGen();
		createTileImg();
	}
   
   //ARGS: dir is either 1 (up), 2 (right), 3 (down), 4 (left), entrance will point in direction dir
   public Tile(byte dir){
		sides = new char[5];
      sides[dir] = 'O';//Force open side
      randomGen();
		createTileImg();
   }
   
	/*
		ARGS: s0 is contents of center
				s1						top
				s2						right
				s3						bottom
				s4						left
	*/ 
   public Tile(char s0, char s1, char s2, char s3, char s4){
      sides = new char[5];
      sides[0] = s0;
      sides[1] = s1;
      sides[2] = s2; 
      sides[3] = s3; 
      sides[4] = s4;
		randomGen();//Generate any missing contents
      createTileImg();
   }
   
	/*
		ARGS: sides.length == 5
            sides is array containing: [0] - contents of center of tile
													[1] - upper wall/side
													[2] - right
													[3] - bottom 
													[4] - left
	*/
	public Tile(char[] sides){
		this.sides = sides;
		randomGen();//Generate any missing contents
		createTileImg();
	}
	
	//--Access--//
	
	//pre:
	//post: Returns this tile's image
   public BufferedImage getImage(){
      return img;
   }
	
   //pre: x, y are coordinates of a mouse click, relative to tile
	//post: Returns side clicked
	public byte mouseClick(int x, int y){
		/*  _______ 
			|\	    /|
			| \___/ |
			| |   | |		The five click "zone" divisions, with the center square dimensions being
			| |___| |		20 x 20, centered on tile. The slope of dividing lines is 1 and -1.
			| /   \ |
			|/_____\|
			
			5 click zones: Center - Search room
								Top - Move hero up
								Left - Move hero left
								Bottom - Move hero down
								Right - Move hero right
		*/
		
		//Check for center box click
		if(x > 20 && x < 40 && y > 20 && y < 40)
			return 0;
					
		//Top click
		else if(x > y && x < -y + 60)
			return 1;
		
		//Right click
		else if(x > y && x > -y + 60)
			return 2;
		
		//Bottom click
		else if(x < y && x > -y + 60)
			return 3;
		
		//Left click
		else if(x < y && x < -y + 60)
			return 4;
		
		return -1;
	}
   
   //pre: 0 <= i < 5
   //post: Returns index i of sides
   public char getSide(byte i){
		return sides[i];
   }
	
	//pre:
	//post: Returns sides
	public char[] getSides(){
		return sides;
	}
	
	//--Mutate--//
	
	//pre: sides.length == 5
	//post: Sets sides to sides
	public void setSides(char[] sides){
		for(byte i = 0; i < 5; i++){
			if(sides[i] != '\u0000')
				this.sides[i] = sides[i];
		}
		createTileImg();
	}
	
   //--Helper--//
   
	//pre: sides has been initialized
	//post: Randomly creates contents of this tile in sides[] if not already created
	private void randomGen(){
      //Generate center spot (hole, solid, cave-in, trap, dark inky hole) if not already generated
      if(sides[0] == '\0'){
         double gen = Math.random();
  			if(gen < 0.4)       
				sides[0] = 'S';		//Solid
				
         else if(gen < 0.5)
            sides[0] = 'H';		//Hole
				
         else if(gen < 0.6)
            sides[0] = 'C';		//Cave-in
				
         else if(gen < 0.7)
            sides[0] = 'T';		//Trap
				
			else if(gen < 0.9)
				sides[0] = 'R';		//Rotating room
				
         else//if(gen <= 1.00)
            sides[0] = 'I';		//Inky hole
					
      }
      //Generate each side (wall, door, portcullis, open) if not previously generated
      for(byte i = 1; i <= 4; i++){
         if(sides[i] == '\0'){
				if(sides[0] == 'R'){//If rotating room, all sides (except force open) are walls
					sides[i] = 'W';
					continue;
				}
			
            double gen = Math.random();
            if(gen <= 0.3)
               sides[i] = 'W';	//Wall
         	
				else if(gen < 0.7)
            	sides[i] = 'O';	//Open
				        
            else if(gen <= 0.8)
               sides[i] = 'D';	//Door
            
            else//if(gen <= 1.00)
               sides[i] = 'P';	//Portcullis
			}
      }
   }
	
	//pre: sides != null, all indeces of sides are not null
	//post: Makes img a tile representation matching the contents of sides
	private void createTileImg(){
		//Create graphics of img as blank 60 x 60 image
		img = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tileImg = img.createGraphics();
		
		//Draw corner sections (always the same)
		for(byte i = 0; i < 4; i++){
			tileImg.drawImage(DungeonQuest.loadImage("Tiles/Corner.png"), null, 0, 0);
			tileImg.rotate(Math.toRadians(90), 30, 30);
		}
		
		//Draw out each side's contents
		for(byte i = 0; i < 4; i++){
			tileImg.drawImage(DungeonQuest.loadImage("Tiles/" + getName(sides[i + 1]) + ".png"), null, 20, 0);
			tileImg.rotate(Math.toRadians(90), 30, 30);
		}
		
		//Draw out center contents
			//Add random rotation (0, 90, 180, 270) to all center contents EXCEPT trap
		if(sides[0] != 'T' && sides[0] != 't')
			tileImg.rotate(Math.toRadians((int)(Math.random() * 4) * 90), 30, 30);
		tileImg.drawImage(DungeonQuest.loadImage("Tiles/" + getName(sides[0]) + ".png"), null, 20, 20);
	}
	
	//pre:
	//post: Returns name of image of contents n
	private String getName(char n){
		switch(n){
			case('W'): return "WALL";
			
			case('S'): return "SOLID";
			
			case('O'): return "OPEN";
			
			case('P'): return "PORTCULLIS";
			
			case('T'): return "TRAP";
			
			case('t'): return "TRAP";
			
			case('I'): return "INKYHOLE";
			
			case('R'): return "ROTATING";
			
			case('D'): return "DOOR";
			
			case('C'): return "CAVEIN";
			
			case('H'): return "HOLE";
			
			case('G'): return "TREASURE";
			
			default: return "EXIT";
		}
	}
}