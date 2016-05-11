//A player in the game (of LIFE!). Basically a player.

import java.awt.Graphics;

public class Hero{

   //Statistics about a hero (all information taken from rule book)
   
   private int lifeValue;                 //Amout of health of Hero
   private final int strength,            //Hero's might and stamina
                     agility,             //Hero's spped ability to react quickly
                     armor,               //Hero's ability to prevent or ignore wounds
                     luck;                //A Hero's good fortune or blessing
							
   private final String name,					//Name of this Hero
								specialAbility,   //'Nuff said
                        flavorText;       //A bit of the Hero's personality
   
   private int r,                         //Row position on board
               c;                         //Column position on board
               
   private int x,                         //x position on screen (in panel)
               y;                         //y position on screen (in panel)
   
   //--Initialize--//
   
   public Hero(){
      lifeValue = 1;
      strength = 0;
      agility = 0;
      armor = 0;
      luck = 0;
		name = "";
      specialAbility = "";
      flavorText = "";
   }
	
	public Hero(String name){
      this.name = "";
      lifeValue = 1;
      strength = 0;
      agility = 0;
      armor = 0;
      luck = 0;
		name = "";
      specialAbility = "";
      flavorText = "";
   }
   
   //pre: stats.length == 5, text.length == 2
   //ARGS: stats is all numerical statistics. text contains all text.
   public Hero(int[] stats, String[] text){
      lifeValue =	stats[0];
      strength = 	stats[1];
      agility = 	stats[2];
      armor = 		stats[3];
      luck = 		stats[4];
		
      specialAbility = 	text[0];
		name = 				text[1];
      flavorText = 		text[2];
   }
   
   //--Graphics--//
   
   public void draw(Graphics g){
   
   }
   
   //--Access--//
   
   //pre: 
   //post: Returns life value of Hero
   public int getLifeValue(){
      return lifeValue;
   }
	
	//pre:
	//post: Returns true if live value > 0
	public boolean isAlive(){
		return lifeValue != 0;
	}
   
   //pre: 
   //post: Returns strength of Hero
   public int getStrength(){
      return strength;
   }
   
   //pre: 
   //post: Returns agility of Hero
   public int getAgility(){
      return agility;
   }
   
   //pre: 
   //post: Returns armor of Hero
   public int getArmor(){
      return armor;
   }
   
   //pre: 
   //post: Returns luck of Hero
   public int getLuck(){
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
   
   //--Mutate--//
   
   //pre: 0 <= r < 13, 0 <= c < 9
   //post: Sets Hero position to (r, c), also sets x and y
   public void setPosition(int r, int c){
      this.r = r;
      this.c = c;
      
   }
   
   //pre: healthDeduction > 0
   //post: Reduces health value by healthDeduction
   public void hurtBy(int healthDeduction){
      lifeValue -= healthDeduction;
   }
}