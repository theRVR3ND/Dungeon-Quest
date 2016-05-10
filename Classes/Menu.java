//Menu screen for game initialization

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Menu extends JPanel{
	
	private final BufferedImage menuImg;	//Background image of menu
	private Button[] buttons;					/*
															All buttons in screen. Index 0 - 3 are for
															each player's active or not button. Index
															4 is for the "Begin!" buttons
														*/
	
   //--Initialize--//

   public Menu(){
		menuImg = DungeonQuest.loadImage("MenuScreen.png");
	
		//All button images are in one file
		BufferedImage buttonImgs = DungeonQuest.loadImage("Buttons.png");
		//Create all buttons
		buttons = new Button[5];
			//Create player selection buttons
		for(int i = 0; i < 4; i++)
			//								Seperating overall image into individual button images (130x60)
			buttons[i] = new Button(buttonImgs.getSubimage(i * 130, 0, 130, 60),
			//								Align buttons 10 pixels apart starting at (60, 140), going right
											60 + 140 * i, 210);
			//Create start game button
		buttons[4] = new Button(buttonImgs.getSubimage(520, 0, 130, 60), 650, 400);
   }
   
   //--Graphics--//
   
   public void paintComponent(Graphics g){
      super.paintComponent(g);
      //------//
		//Draw background
		g.drawImage(menuImg, 0, 0, null);
		//By default draw four player selection buttons
		for(int i = 0; i < 4; i++)
			buttons[i].draw(g);
		//Draw start game button, if at least one of the player selection buttons is down
		for(int i = 0; i < 4; i++)
			if(buttons[i].isDown()){
				buttons[4].draw(g);
				break;//Make sure we don't just draw same button multiple times
			}
      //------//
      repaint(0, 0, 0, 800, 500);
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
				DungeonQuest.numPlayers = numPlayers;
			}
		//Check for click on player selection buttons
   	for(int i = 0; i < 4; i++)
			buttons[i].checkClick(x, y);
   }
}