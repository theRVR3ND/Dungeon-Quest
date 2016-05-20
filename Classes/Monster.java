//A monster to defeat the heros who travel the dungeon

import java.awt.Graphics;

public class Monster extends Entity{
	
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
	
	//pre: type is either 1 (Attack or Wait and See) or 2 (Escape)
	//post: Returns random action which results in encounter with Hero
	public char getAction(byte type){
		if(type == 1){			 //Player wants to fight
			if(Math.random() < 0.8){//Combat!
				super.setHealth((byte)(Math.random() * 4 + 5));
			}
			return 'C';
		}else{//if(type == 2)	Player attempts to flee
			if(Math.random() < 0.8){
				return 'E';//Let player escape
			}else{
				super.setHealth((byte)(Math.random() * 6 + 5));//Combat
				return 'C';
			}
		}
		return 'C';
	}
	
	//pre:
	//post:
	public byte getAttack(){
		return (byte)(1);
	}
	
	//--Mutate--//
}