//A player in the game (of LIFE!). Basically a player card.

public class Hero{

   //Statistics about a hero (all information taken from rule book)
   
   private int lifeValue;                 //Amout of health of Hero
   private final int strength,            //Hero's might and stamina
                     agility,             //Hero's spped ability to react quickly
                     armor,               //Hero's ability to prevent or ignore wounds
                     luck;                //A Hero's good fortune or blessing
   private final String specialAbility,   //'Nuff said
                        flavorText;       //A bit of the Hero's personality
   
   private int r,                         //Row position on board
               c;                         //Column position on board
   
   //--Initialize--//
   
   public Hero(){
      lifeValue = 1;
      strength = 0;
      agility = 0;
      armor = 0;
      luck = 0;
      specialAbility = "";
      flavorText = "";
   }
   
   //pre: stats.length == 5, text.length == 2
   //ARGS: stats is all numerical statistics. text contains all text.
   public Hero(int[] stats, String[] text){
      lifeValue = stats[0];
      strength = stats[1];
      agility = stats[2];
      armor = stats[3];
      luck = stats[4];
      specialAbility = text[0];
      flavorText = "";
   }
   
   //--Access--//
   
   //pre: 
   //post: Returns life value of Hero
   public int getLifeValue(){
      return lifeValue;
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
   
   //pre: healthDeduction > 0
   //post: Reduces health value by healthDeduction
   public void hurtBy(int healthDeduction){
      lifeValue -= healthDeduction;
   }
}