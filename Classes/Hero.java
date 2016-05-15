//A player in the game (of LIFE!). Basically a player.

import java.awt.Graphics;
import java.util.Scanner;
import java.io.File;

public class Hero{

   //Statistics about a hero (all information taken from rule book)
   
	private final byte maxLife;				//Max life of Hero. Equivallent to initial life.
   private byte lifeValue;                //Amout of health of Hero
	private byte treasure;						//Amout of treasure player has
   private final byte strength,           //Hero's might and stamina
                      agility,            //Hero's spped ability to react quickly
                      armor,              //Hero's ability to prevent or ignore wounds
                      luck;               //A Hero's good fortune or blessing
							
   private final String name,					//Name of this Hero
								specialAbility,   //'Nuff said
                        flavorText;       //A bit of the Hero's personality
   
   private byte r,                        //Row position on board
                c;                        //Column position on board
               
   private int x,                         //x position on screen (in panel)
               y;                         //y position on screen (in panel)
   
   //--Initialize--//
   
   public Hero(){
      lifeValue = 1;
		treasure = 0;
		maxLife = lifeValue;
      strength = 0;
      agility = 0;
      armor = 0;
      luck = 0;
		name = "";
      specialAbility = "";
      flavorText = "";
		
		x = c * 60 + Panel.boardX + 15;
		y = r * 60 + Panel.boardY + 15;
   }
	
	public Hero(String name){
		//Read in text file of player stats
		Scanner input;
		byte[] stats = new byte[5];
		String[] text = new String[3];
		try{
			input = new Scanner(new File(DungeonQuest.getDirectory() + "Resources/Heros/" + name + ".txt"));
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
			System.out.println("Could not load " + DungeonQuest.getDirectory() + "Resources/Heros/" + name + ".txt");
			System.exit(1);
		}
      lifeValue =	stats[0];
      strength = 	stats[1];
      agility = 	stats[2];
      armor = 		stats[3];
      luck = 		stats[4];
		maxLife = lifeValue;
		treasure = 0;
		
      specialAbility = 	text[0];
		this.name = 		text[1];
      flavorText = 		text[2];
		
		x = c * 60 + Panel.boardX + 15;
		y = r * 60 + Panel.boardY + 15;
   }
   
   //pre: stats.length == 5, text.length == 2
   //ARGS: stats is all numerical statistics. text contains all text.
   public Hero(byte[] stats, String[] text){
      lifeValue =	stats[0];
      strength = 	stats[1];
      agility = 	stats[2];
      armor = 		stats[3];
      luck = 		stats[4];
		maxLife = lifeValue;
		treasure = 0;
		
      specialAbility = 	text[0];
		name = 				text[1];
      flavorText = 		text[2];
		
		x = c * 60 + Panel.boardX + 15;
		y = r * 60 + Panel.boardY + 15;
   }
   
   //--Graphics--//
   
   public void draw(Graphics g){
		//Glide towards wanted location
   	if(x < c * 60 + Panel.boardX + 15)
			x++;
		else if(x > c * 60 + Panel.boardX + 15)
			x--;
		
   	if(y < r * 60 + Panel.boardY + 15)
			y++;
		else if(y > r * 60 + Panel.boardY + 15)
			y--;
			
		g.drawImage(DungeonQuest.loadImage("Heros/" + name + ".png"), x, y, null);
   }
   
   //--Access--//
   
   //pre: 
   //post: Returns life value of Hero
   public byte getLifeValue(){
      return lifeValue;
   }
	
	//pre:
	//post: Returns treasure which hero has accquired
	public byte getTreasure(){
		return treasure;
	}
	
	//pre:
	//post: Returns true if live value > 0
	public boolean isAlive(){
		return lifeValue != 0;
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
	//post: Returns row position of Hero on Panel grid 
	public byte getRow(){
		return r;
	}
	
  	//pre:
	//post: Returns column position of Hero on Panel grid 
	public byte getColumn(){
		return c;
	}
	
	//pre:
	//post: Returns true if (x, y) position have "glided" to match (r, c)
	public boolean doneGliding(){
		return x == c * 60 + Panel.boardX + 15 && y == r * 60 + Panel.boardY + 15;
	}
	
   //--Mutate--//
   
   //pre: 0 <= r < 13, 0 <= c < 9
   //post: Sets Hero position to (r, c), also sets x and y
   public void setPosition(byte r, byte c){
      this.r = r;
      this.c = c;
      
		x = c * 60 + Panel.boardX + 15;
		y = r * 60 + Panel.boardY + 15;
   }
	
	//pre: 
	//post: Changes r and c based on rMove and cMove, but does not change x, y graphic position
	public void move(byte rMove, byte cMove){
		r += rMove;
		c += cMove;
	}
   
	//pre:
	//post:
	public void addTreasure(byte i){
		treasure += i;
	}
	
   //pre: healthDeduction > 0
   //post: Reduces health value by healthDeduction
   public void hurtBy(byte healthDeduction){
      lifeValue -= healthDeduction;
   }
}