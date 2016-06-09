//A player in the game (of LIFE!). Basically a player.

import java.awt.Graphics;
import java.util.Scanner;
import java.io.File;

public class Hero extends Entity{

   //Statistics about a hero (all information taken from rule book)
   
	private final byte maxLife;				//Max life of Hero. Equivallent to initial life.
	private boolean alignEdge;					//Should Hero graphically not move to center of tile?
   private byte numSearches;              //Number of consecutive searches this hero has performed
	private boolean secretDoor;				//Has Hero found a secret door?
   private final byte strength,           //Hero's might and stamina
                      agility,            //Hero's spped ability to react quickly
                      armor,              //Hero's ability to prevent or ignore wounds
                      luck;               //A Hero's good fortune or blessing
							
   private final String specialAbility,   //'Nuff said
                        flavorText;       //A bit of the Hero's personality
   
   //--Initialize--//
	
	//ARGS: name is valid name of file (.png and .txt) in "Heros" folder
	public Hero(String name){
		super(name);
		
		//Read in text file of player stats
		Scanner input;
		byte[] stats = new byte[5];
		String[] text = new String[2];
		try{
			input = new Scanner(new File(DungeonQuest.getDirectory() + "Resources/Heros/" + super.getName() + ".txt"));
			//Loading numerical stats
			for(byte i = 0; i < 5; i++){
				if(! input.hasNextByte()){
					stats[i] = 0;
					continue;
				}
				stats[i] = input.nextByte();
			}
			input.nextLine();
			//Loading in string info
			for(byte i = 0; i < 2; i++){
				if(! input.hasNextLine()){
					text[i] = "";
					continue;
				}
				text[i] = input.nextLine();
			}
		}catch(Exception e){
			System.out.println("Could not load " + DungeonQuest.getDirectory() + "Resources/Heros/" + super.getName() + ".txt");
			System.exit(1);
		}
		
      specialAbility = 	text[0];
      flavorText = 		text[1];
		
      super.setHealth(stats[0]);
		secretDoor = false;
      strength = 	stats[1];
      agility = 	stats[2];
      armor = 		stats[3];
      luck = 		stats[4];
		maxLife = super.getHealth();
   }
   
   //--Graphics--//
   
	//pre: g != null
	/*
		post: Aligns (x, y) position with edge of tile if in combat, then
				draws Hero's image. If not in combat, calls parent draw().
 	*/
	public void draw(Graphics g){
		if(alignEdge){
			//Glide towards wanted location (edge of tile)
	   	if(super.getX() < super.getColumn() * 60 + Panel.boardX + 2)			//Glide to left edge
				super.setX(super.getX() + 1);
				
			else if(super.getX() > super.getColumn() * 60 + Panel.boardX + 42)	//Glide to right edge
				super.setX(super.getX() - 1);
			
	   	if(super.getY() < super.getRow() * 60 + Panel.boardY - 17)				//Glide to top edge
				super.setY(super.getY() + 1);
				
			else if(super.getY() > super.getRow() * 60 + Panel.boardY + 47)		//Glide to bottom edge
				super.setY(super.getY() - 1);
			
			g.drawImage(DungeonQuest.loadImage("Heros/" + super.getName() + ".png"), super.getX(), super.getY(), null);
			
		}else//If not in combat, do normal movement
			super.draw("Heros/", g);
   }
	
   //--Access--//
	
	//pre:
	//post:
	public boolean doneGliding(){
		if(alignEdge){
			if(super.getX() == super.getColumn() * 60 + Panel.boardX + 2 || 
				super.getX() == super.getColumn() * 60 + Panel.boardX + 42 ||
				super.getY() == super.getRow() * 60 + Panel.boardY - 17 ||
				super.getY() == super.getRow() * 60 + Panel.boardY + 47)
				return true;
		}else
			return super.doneGliding();
		return false;
	}
	
	//pre:
	//post: Returns true if moving only to edge of tile, false otherwise
	public boolean getEdgeAlign(){
		return alignEdge;
	}
	
	//pre:
	//post: Returns value of a random attack on monster
	public byte getAttack(){
		return (byte)(Math.random() * strength);
	}
   
   //pre: 
   //post: Returns strength of Hero
   public byte getStrength(){
      return strength;
   }
   
   //pre: 
   //post: Returns agility of Hero
   public byte getAgility(){
      return agility;
   }
   
   //pre: 
   //post: Returns armor of Hero
   public byte getArmor(){
      return armor;
   }
   
   //pre: 
   //post: Returns luck of Hero
   public byte getLuck(){
      return luck;
   }
   
   //pre: 
   //post: Returns special ability of Hero
   public String getSpecialAbility(){
      return specialAbility;
   }
   
   //pre: 
   //post: Returns flavor text of Hero
   public String getFlavorText(){
      return flavorText;
   }
   
   //pre:
   /* 
      post: Returns number of consecutive searches 
      hero has performed in current room
   */
   public byte getNumSearches(){
      return numSearches;
   }
	
	//pre:
	/*
		post: Returns true if Hero should be allowed
				to clip through wall
	*/
	public boolean foundSecretDoor(){
		return secretDoor;
	}
	
   //--Mutate--//
   
	//pre:
	//post: Sets alignEdge to edgeAlign
	public void setEdgeAlign(boolean edgeAlign){
		alignEdge = edgeAlign;
	}
	
	//pre:
	//post: Sets secretDoor to secretDoor
	public void setSecretDoor(boolean secretDoor){
		this.secretDoor = secretDoor;
	}
	
   //pre:
   //post: Increases numSearches by one
   public void searched(){
      numSearches++;
   }
	
   //pre:
   //post: Changes health value by change
   public void changeHealth(byte change){
      super.changeHealth(change);
      if(super.getHealth() > maxLife)//Make sure any healing does not exceed original life value
         super.setHealth(maxLife);
   }
   
   //pre:
   //post: Calls super.move() and resets searching counter
   public void move(byte r, byte c){
      super.move(r, c);
      numSearches = 0;
   }
}