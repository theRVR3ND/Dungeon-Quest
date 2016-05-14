//Menu screen for game initialization

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Menu extends JPanel{
	
   private String[] heroNames = {"Challara",       //All hero's names
                                 "Gherinn", 
                                 "Hugo", 
                                 "Krutzbeck", 
                                 "Lindel", 
                                 "Tatianna"};
   private int[] charInd;                          /*
                                                      Indexes of character (in heroNames) assigned
                                                      to each player button
                                                   */
	private Button[] buttons;					         /*
															         All buttons in screen. Index 0 - 3 are for
															         each player's active or not button. Index
															         4 is for the "Begin!" buttons
														         */
	
   //--Initialize--//

   public Menu(){
		//Create all buttons
		   //All button images are in one file
		BufferedImage buttonImgs = DungeonQuest.loadImage("Menu/Buttons.png");
      
		buttons = new Button[5];
			//Create player selection buttons
		for(int i = 0; i < 4; i++)
			//								Seperating overall image into individual button images (130x60)
			buttons[i] = new Button(buttonImgs.getSubimage(i * 130, 0, 130, 60),
			//								Align buttons 10 pixels apart starting at (240, 350), going right
											240 + 150 * i, 350);
			//Create start game button
		buttons[4] = new Button(buttonImgs.getSubimage(520, 0, 130, 60), 1000, 600);
      
      //Assign random hero to each player button (without duplication)
      charInd = new int[4];
      for(int i = 0; i < 4; i++){
         do{
            charInd[i] = (int)(Math.random() * heroNames.length);
         }while(heroNames[charInd[i]] == null);
         heroNames[charInd[i]] = null;//Make sure we don't use same player again
      }
      
      //Reset heroNames for later hero image loading
      heroNames[0] = "Challara";
      heroNames[1] = "Gherinn";
      heroNames[2] = "Hugo";
      heroNames[3] = "Krutzbeck"; 
      heroNames[4] = "Lindel"; 
      heroNames[5] = "Tatianna";
   }
   
   //--Graphics--//
   
   public void paintComponent(Graphics g){
      super.paintComponent(g);
      //------//
		//Draw background
		g.drawImage(DungeonQuest.loadImage("Menu/MenuScreen.png"), 0, 0, 1200, 750, null);
		
		//By default draw four player selection buttons
		for(int i = 0; i < 4; i++){
			buttons[i].draw(g);
         if(buttons[i].isDown()){//If button is down, draw hero assigned to it underneath
            String name = heroNames[charInd[i]];
            g.drawImage(DungeonQuest.loadImage("Menu/" + name + ".png"), 
                        buttons[i].getX() + 10, buttons[i].getY() + 80, null);
         }
		}
		
		//Draw start game button, if at least one of the player selection buttons is down
		for(int i = 0; i < 4; i++)
			if(buttons[i].isDown()){
				buttons[4].draw(g);
				break;//Make sure we don't just draw same button multiple times
			}
	}
   
   //--Access--//
	
   //--Mutate--//
   
   public void mouseClick(int x, int y){
		//Check for start game button click
		if(buttons[0].isDown() || buttons[1].isDown() || 
			buttons[2].isDown() || buttons[3].isDown())
			if(buttons[4].checkClick(x, y)){//If start game button has been clicked, begin start game process
				int numPlayers = 0;
				for(int i = 0; i < 4; i++)
					if(buttons[i].isDown())
						numPlayers++;
            
            //Create game panel
            String[] names = new String[numPlayers];//List of all names of heros being used during game
            int namesInd = 0;
            for(int i = 0; i < 4; i++){
               if(buttons[i].isDown())
                  names[namesInd++] = heroNames[charInd[i]];
            }
            DungeonQuest.p = new Panel(names);//Create panel with names of all heros being used
            
				DungeonQuest.numPlayers = numPlayers;
			}
		//Check for click on player selection buttons
   	for(int i = 0; i < 4; i++)
			buttons[i].checkClick(x, y);
		//Repaint screen in case anything changed
      repaint(0, 0, 0, 1200, 750);
   }
}