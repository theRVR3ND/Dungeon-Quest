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
		super.setHealth((byte)10);
	}
	
	/*
		ARGS: name is monster's name (type), r is row position 
				on board, c is column position on board
	*/
	public Monster(String name, byte r, byte c){
		super(name, r, c);
		super.setHealth((byte)10);
	}
	
	//--Graphics--//
	
	//pre: g != null
	//post: Calls super draw method, sending directory of monster images
	public void draw(Graphics g){
		super.draw("Monsters/", g);
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
			if(Math.random() < 0.2){
				return 'E';//Let player escape
			}else{
				super.setHealth((byte)(Math.random() * 6 + 6));//Combat
				return 'C';
			}
		}
	}
	
	//pre:
	//post:
	public byte getAttack(){
		return (byte)(1);
	}
	
	//--Mutate--//
	
	//pre: playerAction = 1 (Range attack), 2 (Melee attack), or 3 (Magic attack)
	//post: Returns damage dealt to Hero
	public byte fight(byte playerAction){
		/*
		final byte action = (byte)(Math.random() * 3 + 1);//Monster's action (one of 3 attacks)
		if(playerAction == 1){							//Hero melee attack
			if(action == 1){									//Monster melee attack
				super.changeHealth((byte)(-2));
				return (byte)2;
				
			}else if(action == 2){							//Monster range attack
				super.changeHealth((byte)());
				return (byte);
			}
			
		}else if(playerAction == 2){					//Hero range attack
			if(action == 1){									//Monster melee attack
				super.changeHealth((byte)());
				return (byte);
				
			}else if(action == 2){							//Monster range attack
				super.changeHealth((byte)());
				return (byte);
			}
			
		}else if(playerAction == 3){					//Hero magic attack
			if(action == 1){									//Monster melee attack
				super.changeHealth((byte)());
				return (byte);
				
			}else if(action == 2){							//Monster range attack
				super.changeHealth((byte)());
				return (byte);
			}
		}
		*/
		return 0;
	}
}