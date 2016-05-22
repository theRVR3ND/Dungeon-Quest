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
   private byte[] charInd;                         /*
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
		for(byte i = 0; i < 4; i++)
			//								Seperating overall image into individual button images (130x60)
			buttons[i] = new Button(buttonImgs.getSubimage(i * 130, 0, 130, 60),
			//								Align buttons 10 pixels apart starting at (240, 350), going right
											240 + 150 * i, 350);
			//Create start game button
		buttons[4] = new Button(buttonImgs.getSubimage(520, 0, 130, 60), 1000, 600);
      
      //Assign random hero to each player button (without duplication)
      charInd = new byte[4];
      for(byte i = 0; i < 4; i++){
         do{
            charInd[i] = (byte)(Math.random() * heroNames.length);
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
		g.drawImage(DungeonQuest.loadImage("Menu/MenuTitle.png"), 106, 104, null);
		
		//By default draw four player selection buttons
		for(byte i = 0; i < 4; i++){
			buttons[i].draw(g);
         if(buttons[i].isDown()){//If button is down, draw hero assigned to it underneath
            String name = heroNames[charInd[i]];
            g.drawImage(DungeonQuest.loadImage("Menu/" + name + ".png"), 
                        buttons[i].getX() + 10, buttons[i].getY() + 80, null);
         }
		}
		
		//Draw start game button, if at least one of the player selection buttons is down
		for(byte i = 0; i < 4; i++)
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
				byte numPlayers = 0;
				for(byte i = 0; i < 4; i++)
					if(buttons[i].isDown())
						numPlayers++;
            
            //Create game panel
            String[] names = new String[numPlayers];//List of all names of heros being used during game
            byte namesInd = 0;
            for(byte i = 0; i < 4; i++){
               if(buttons[i].isDown())
                  names[namesInd++] = heroNames[charInd[i]];
            }
            DungeonQuest.p = new Panel(names);//Create panel with names of all heros being used
			}
		//Check for click on player selection buttons
   	for(byte i = 0; i < 4; i++)
			buttons[i].checkClick(x, y);
		//Repaint screen in case anything changed
      repaint(0, 0, 0, 1200, 750);
   }
	
	//--Button Class--//

	public class Button{
	
		private BufferedImage img;							//Image of this button
		private boolean down;								//Is this buttons clicked ("down")?
		private int x,											//x position of this button
						y;											//y position of this button
	
		//--Initialize--//
	
		/*
			ARGS: imgName is name of button's image.
			Button is positioned at (x, y) in JFrame.
		*/
		public Button(String imgName, int x, int y){
			img = DungeonQuest.loadImage(imgName);
			down = false;
			this.x = x;
			this.y = y;
		}
		
		/*
			ARGS: img is button's graphical representation. 
			Button is positioned at (x, y) in JFrame.
		*/
		public Button(BufferedImage img, int x, int y){
			this.img = img;
			down = false;
			this.x = x;
			this.y = y;
		}
		
		//--Graphics--//
		
		//pre: g != null
		//post: Draws button at (x, y), with size based on click state
		public void draw(Graphics g){
			if(down)//When mouse over or down
				g.drawImage(img, x, y, null);
			else//If not down, shrink slightly
				g.drawImage(img,
								x + (int)(img.getWidth() * 0.025), 
								y + (int)(img.getHeight() * 0.025), 
								(int)(img.getWidth() * 0.95), 
								(int)(img.getHeight() * 0.95), null);
		}
		
		//--Access--//
		
		//pre:
		//post: Returns x position (in JFrame) of button
		public int getX(){
			return x;
		}
		
		//pre:
		//post: Returns y position (in JFrame) of button
		public int getY(){
			return y;
		}
		
		//pre:
		//post: Returns true if button is clicked "down", false otherwise
		public boolean isDown(){
			return down;
		}
		
		//--Mutate--//
		
		//pre:
		/*
			post: Returns true if (x, y) is inside area of button.
					Changes up/down state of button based on click.
		*/
		public boolean checkClick(int x, int y){
			if(this.x <= x && this.x + img.getWidth() >= x &&
				this.y <= y && this.y + img.getHeight() >= y){
				down = !down;//Change state of button
				return true;
			}else
				return false;
		}
	}
}