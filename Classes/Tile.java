//A "dungeon card" on the game board

import java.awt.Graphics;
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
	}
   
   //ARGS: dir is either 1 (up), 2 (right), 3 (down), 4 (left), entrance will point in direction dir
   public Tile(int dir){
      rotation = 90 * (dir - 1);
      randomGen();
   }
	
	//--Access--//
	
   public BufferedImage getImage(){
      return img;
   }
   
	//--Mutate--//
	
   //--Helper--//
   
   private void randomGen(){
      //Generate center spot (hole, solid, cave-in, trap, dark)
      if(Math.random() <= 0.10){//Generate other than solid
         double gen = Math.random();
         if(gen <= 0.25)
            sides[0] = "HOLE";
         else if(gen <= 0.50)
            sides[0] = "CAVE";
         else if(gen <= 0.75)
            sides[0] = "TRAP";
         else//if(gen <= 1.00)
            sides[0] = "DARK";
      }else{//Solid center of tile
         sides[0] = "SOLID";
      }
      //Generate each side (wall, door, portcullis, open)
      for(int i = 1; i <= 4; i++){
         if(Math.random() <= 0.10){
            double gen = Math.random();
            if(gen <= 0.33)
               sides[i] = "OP";
            
            else if(gen <= 0.66)
               sides[i] = "OP";
            
            else//if(gen <= 1.00)
               sides[i] = "OP";
         }else{
            sides[i] = "WALL";
         }
      }
   }
}