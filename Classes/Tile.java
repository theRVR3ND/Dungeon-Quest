//A "dungeon card" on the game board

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile{

	private BufferedImage img;	      //Image of this tile
	private Button[] buttons;			//Choices for player in tile (move arrow, search, unlock, etc.)
	private String[] sides;				/*
													The contents of the 0 (Middle), 1 (North), 2 (East),
													3 (South), 4 (West) sides of this tile (wall, door, etc.)
												*/

	//--Initialize--//
   
   //ARGS: tileType is name of folder in DungeonQuest -> Resources -> Board -> Tiles folder
	public Tile(){
		sides = new String[5];
      randomGen();
		createTileImg();
	}
   
   //ARGS: dir is either 1 (up), 2 (right), 3 (down), 4 (left), entrance will point in direction dir
   public Tile(int dir){
		sides = new String[5];
      sides[dir - 1] = "OPEN";//Force open side
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
   public Tile(String s0, String s1, String s2, String s3, String s4){
      sides = new String[5];
      sides[0] = s0;
      sides[1] = s1;
      sides[2] = s2; 
      sides[3] = s3; 
      sides[4] = s4;
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
	public Tile(String[] sides){
		this.sides = sides;
		createTileImg();
	}
	
	//--Access--//
	
   public BufferedImage getImage(){
      return img;
   }
   
   //pre: 0 < dir <= 4
   //post: Returns action needed for hero to move through side dir
   public String actionNeeded(int dir){
      if(sides[dir].equals("OPEN"))
         return "NONE";//No actions (card drawings) needed
      else if(sides[dir].equals("DOOR") || sides[dir].equals("PORTCULLIS"))
         return "DRAW";//Card draw required
      else//if(sides[dir].equals("WALL"))
         return "NOPASS";//Sides is completely impassible
   }
   
   //pre:
   //post: Returns action needed to be executed by board as hero arrives in tile
   public String arrivalAction(){
      if(sides[0].equals("SOLID"))
         return "NONE";
      else if(sides[0].equals("HOLE") || sides[0].equals("CAVEIN"))
         return "DRAW";
      else if(sides[0].equals("TRAP"))
         return "TRAP";
      else//if(sides[0].equals("INKYHOLE"))
         return "";
   }
   
	//--Mutate--//
	
	//pre:
	//post: x, y are coordinates of a mouse click, relative to tile
	public void mouseClick(int x, int y){
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
		if(x > 20 && x < 40 && y > 20 && y < 40){
			System.out.println("Center");
		}
		
		//Top click
		else if(x > y && x < -y + 60){
			System.out.println("Top");
		}
		
		//Left click
		else if(x < y && x < -y + 60){
			System.out.println("Left");
		}
		
		//Bottom click
		else if(x < y && x > -y + 60){
			System.out.println("Bottom");
		}
		
		//Right click
		else if(x > y && x > -y + 60){
			System.out.println("Right");
		}
	}
	
   //--Helper--//
   
   private void randomGen(){
      //Generate center spot (hole, solid, cave-in, trap, dark inky hole) if not already generated
      if(sides[0] == null){
         if(Math.random() <= 0.30){//Generate other than solid
            double gen = Math.random();
            if(gen <= 0.25)
               sides[0] = "HOLE";
            else if(gen <= 0.50)
               sides[0] = "CAVEIN";
            else if(gen <= 0.75)
               sides[0] = "TRAP";
            else//if(gen <= 1.00)
               sides[0] = "INKYHOLE";
         }else{//Solid center of tile
            sides[0] = "SOLID";
         }
      }
      //Generate each side (wall, door, portcullis, open) if not previously generated
      for(int i = 1; i <= 4; i++){
         if(sides[i] != null)
            continue;
         if(Math.random() <= 0.30){
            double gen = Math.random();
            if(gen <= 0.33)
               sides[i] = "DOOR";
            
            else if(gen <= 0.66)
               sides[i] = "PORTCULLIS";
            
            else//if(gen <= 1.00)
               sides[i] = "WALL";
         }else{
            sides[i] = "OPEN";
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
		for(int i = 0; i < 4; i++){
			tileImg.drawImage(DungeonQuest.loadImage("Tiles/Corner.png"), null, 0, 0);
			tileImg.rotate(Math.toRadians(90), 30, 30);
		}
		
		//Draw out each side's contents
		for(int i = 0; i < 4; i++){
			tileImg.drawImage(DungeonQuest.loadImage("Tiles/" + sides[i + 1] + ".png"), null, 20, 0);
			tileImg.rotate(Math.toRadians(90), 30, 30);
		}
		
		//Draw out center contents
		if(! sides[0].equals("TRAP"))//Add random rotation (0, 90, 180, 270) to all center contents EXCEPT trap
			tileImg.rotate(Math.toRadians((int)(Math.random() * 4) * 90), 30, 30);
		tileImg.drawImage(DungeonQuest.loadImage("Tiles/" + sides[0] + ".png"), null, 20, 20);
	}
}