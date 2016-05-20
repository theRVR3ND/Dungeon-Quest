//A player in the game (of LIFE!). Basically a player.

import java.awt.Graphics;
import java.util.Scanner;
import java.io.File;

public class Hero extends Entity{

   //Statistics about a hero (all information taken from rule book)
   
	private final byte maxLife;				//Max life of Hero. Equivallent to initial life.
	private byte treasure;						//Amout of treasure player has
   private byte numSearches;              //Number of consecutive searches this hero has performed
   private final byte strength,           //Hero's might and stamina
                      agility,            //Hero's spped ability to react quickly
                      armor,              //Hero's ability to prevent or ignore wounds
                      luck;               //A Hero's good fortune or blessing
							
   private final String specialAbility,   //'Nuff said
                        flavorText;       //A bit of the Hero's personality
   
   //--Initialize--//
   
   public Hero(){
		super();
		treasure = 0;
		maxLife = super.getHealth();
      strength = 0;
      agility = 0;
      armor = 0;
      luck = 0;
      specialAbility = "";
      flavorText = "";
   }
	
	//ARGS: name is valid name of file (.png and .txt) in "Heros" folder
	public Hero(String name){
		super(name);
		
		//Read in text file of player stats
		Scanner input;
		byte[] stats = new byte[5];
		String[] text = new String[3];
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
			for(byte i = 0; i < 3; i++){
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
		
      specialAbility = 	text[1];
      flavorText = 		text[2];
		
      super.setHealth(stats[0]);
      strength = 	stats[1];
      agility = 	stats[2];
      armor = 		stats[3];
      luck = 		stats[4];
		maxLife = super.getHealth();
		treasure = 0;
   }
   
   
   /*
		ARGS: stats is all numerical statistics. text contains all text.
				stats.length == 5, text.length == 2
	*/
   public Hero(byte[] stats, String[] text){
		super(text[0]);
      specialAbility = 	text[1];
      flavorText = 		text[2];
		
      super.setHealth(stats[0]);
      strength = 	stats[1];
      agility = 	stats[2];
      armor = 		stats[3];
      luck = 		stats[4];
		maxLife = super.getHealth();
		treasure = 0;
   }
   
   //--Graphics--//
   
	//pre: g != null
	//post: Calls Entity draw method, sending name of this Hero's image
   public void draw(Graphics g){
		super.draw("Heros/" + super.getName() + ".png", g);
   }
   
   //--Access--//
	
	//pre:
	//post: Returns treasure which hero has accquired
	public byte getTreasure(){
		return treasure;
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
	
   //--Mutate--//
   
   //pre:
   //post: Increases numSearches by one
   public void searched(){
      numSearches++;
   }
   
	//pre:
	//post:
	public void addTreasure(byte i){
		treasure += i;
	}
	
   //pre:
   //post: Changes health value by change
   public void changeHealth(byte change){
      super.setHealth((byte)(super.getHealth() + change));
      if(super.getHealth() > maxLife)
         super.setHealth(maxLife);
   }
   
   //pre:
   //post: Calls super.move() and resets searching counter
   public void move(byte r, byte c){
      super.move(r, c);
      numSearches = 0;
   }
}