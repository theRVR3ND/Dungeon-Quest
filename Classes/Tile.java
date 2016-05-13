//A "dungeon card" on the game board

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile{

	private BufferedImage img;	      //Image of this tile
	private final int rotation;		/*
                                       Rotation of this tile's entrance (in degrees, 
                                       with 0 being north, 90 being east, etc.)
                                    */
	private Button[] buttons;			//Choices for player in tile (move arrow, search, unlock, etc.)
	private String[] sides;				/*
													The contents of the 0 (Middle), 1 (North), 2 (East),
													3 (South), 4 (West) sides of this tile (wall, door, etc.)
												*/

	//--Initialize--//
   
   /*
      ARGS: tileType is name of folder in DungeonQuest -> Resources -> Board -> Tiles folder.
            rotation is rotation of tile.
   */
	public Tile(){
      this.rotation = 0;
      randomGen();
		createTileImg();
	}
   
   //ARGS: dir is either 1 (up), 2 (right), 3 (down), 4 (left), entrance will point in direction dir
   public Tile(int dir){
      rotation = 90 * (dir - 1);
      randomGen();
		createTileImg();
   }
	
	/*
		ARGS: sides is array containing: [0] - contents of center of tile
													[1] - upper wall/side
													[2] - right
													[3] - bottom 
													[4] - left
	*/
	public Tile(String[] sides){
		this.sides = sides;
		rotation = 0;
		createTileImg();
	}
	
	//--Access--//
	
   public BufferedImage getImage(){
      return img;
   }
   
	//--Mutate--//
	
   //--Helper--//
   
   private void randomGen(){
		sides = new String[5];
      //Generate center spot (hole, solid, cave-in, trap, dark inky hole)
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
      //Generate each side (wall, door, portcullis, open)
      for(int i = 1; i <= 4; i++){
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
	
	//pre: sides != null
	//post: Makes img a tile representation matching the contents of sides
	private void createTileImg(){
		//Create graphics of img as blank 60 x 60 image
		img = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tileImg = img.createGraphics();
		
		//Draw corner sections (always the same)
		for(int i = 0; i < 4; i++){
			tileImg.rotate(Math.toRadians(90 * i), 30, 30);
			tileImg.drawImage(DungeonQuest.loadImage("Tiles/Corner.png"), null, 0, 0);
		}
		
		//Draw out each side's contents
		for(int i = 0; i < 4; i++){
			tileImg.rotate(Math.toRadians(90 * i), 30, 30);
			tileImg.drawImage(DungeonQuest.loadImage("Tiles/" + sides[i + 1] + ".png"), null, 20, 0);
		}
		
		//Draw out center contents
		if(! sides[0].equals("TRAP"))//Add random rotation (0, 90, 180, 270) to all center contents EXCEPT trap
			tileImg.rotate(Math.toRadians((int)(Math.random() * 4) * 90), 30, 30);
		tileImg.drawImage(DungeonQuest.loadImage("Tiles/" + sides[0] + ".png"), null, 20, 20);
	}
}