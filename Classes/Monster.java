//A monster to defeat the heros who travel the dungeon

import java.awt.Graphics;

public class Monster extends Entity{
	
	private byte attackValue,				//Strength of monster
					 attackType;				//Ranged (0), melee (1), or magic (3) attack
	
	//--Initialize--//
	
	/*
		ARGS: name is name of monster (for resources in "Monsters" folder),
				calls super class contructor with random (r, c) position for Panel board.
	*/
	public Monster(String name){
		super(name, (byte)(Math.random() * 10), (byte)(Math.random() * 13));
	}
	
	//--Graphics--//
	
	public void draw(Graphics g){
		super.draw("Monsters/" + super.getName() + ".png", g);
	}
	
	//--Access--//
	
	//--Mutate--//
}