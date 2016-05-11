//A "dungeon card" on the game board

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile{

	private final BufferedImage img;	//Image of this tile
	private final int rotation;		//Rotation of this tile in grid (in degrees, with 0 being north, 90 being east, etc.)
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
	public Tile(String tileType, int rotation){
      img = DungeonQuest.loadImage("Board/Tiles/" + tileType + "/img.png");
      this.rotation = rotation;
      //Figure out how many action buttons are possible for sides and create buttons based on it
      int numPassable = 0;
      //for(
      buttons = new Button[1];
	}
	
	//--Access--//
	
	//--Mutate--//
	
}