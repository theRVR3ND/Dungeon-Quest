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

	public Tile(String[] sides, int rotation){
		this.sides = sides;
		this.rotation = rotation;
		//if(sides[0].equals("HOLE"))
			img = DungeonQuest.loadImage("Dungeons/BottomlessPit.png");
	}
	
	//--Access--//
	
	public boolean isPassable(String direction){
		switch(direction){
			case(""): return false;
		}
		return false;
	}
	
	//--Mutate--//
	
}