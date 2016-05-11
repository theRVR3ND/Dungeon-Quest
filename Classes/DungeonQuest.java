/*
   Driver program for Dragon Quest game
   By: Kelvin Peng
   under guidance of Reverend Oberle
   for the APCS Final Project 2015-2016
*/

import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DungeonQuest{
   
   //--Frame and Panels--//
   
   public static JFrame f;                //The one and only JFrame for displaying
   public static Menu m;                  //Menu screen
   public static Panel p;                 //Main game content pane
   
   //--Settings--//
   
	public static int numPlayers;				//Number of players (1 to 4) selected by user(s)
   public static int screenFactor;        /* 
                                             Screen (and frame) resizing factor. Frame will always
                                             be in 80:50 ratio, but overall size will change. Ranges
                                             from 10 to 20.
                                          */
   
   //--Main--//

   public static void main(String[] args){
   
      //--Initialize--//
      
      screenFactor = 10;
		numPlayers = -1;
      
      f = new JFrame("Dungeon Quest || Begin your Quest...");
      f.setSize(80 * screenFactor, 50 * screenFactor);
      f.setLocation(20, 20);
      f.setResizable(false);//Frame size will be changed by (+) and (-) keys
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.addMouseListener(new mouseListen());
      f.addKeyListener(new keyListen());
      
      m = new Menu();
      
      f.setContentPane(m);//First display menu
      f.setVisible(true);
      
      //--End Initialize--//
      
      //--Menu open--//
      
      //Keep doing nothing while settings have not been selected by player(s)
      while(numPlayers == -1){
			System.out.print("");
		}
		
		//Recycle menu. p is initialized in menu.
		m = null;
		
		f.setTitle("Dungeon Quest || The Journey");
      f.remove(f.getContentPane());
      f.setContentPane(p);
      f.validate();
      f.repaint();
		
      //--The GAME--//
      
      while(true){
      
      }
   }
   
   //--Static Utility Classes--//
   
	//pre:
	//post: Returns file directory for "Dungeon Quest" folder
	public static String getDirectory(){
		//URL in String representation, leading directly to this java file
		String dir = (DungeonQuest.class.getResource("DungeonQuest.java")).toString();
		//Crop dir to lead to "Dungeon Quest" folder
		dir = dir.substring(5, dir.length() - 25);
		return dir.replaceAll("%20", " ");//Finally replace all "%20" URL space symbols with real spaces
	}
	
   //pre: name is valid image name in "Resources" folder
   //post: Returns BufferedImage found with title name in "Resources" folder
   public static BufferedImage loadImage(String name){
      try{
			name = getDirectory() + "Resources/" + name;//Change name to image's directory
			return ImageIO.read(new File(name));
		}catch(Exception e){
			System.out.println("Could not load " + name + ". Please make sure it exists in \"Resources\" folder");
			System.exit(1);
		}
		return null;
   }
   
   //pre: 
   //post: Returns sum of values from simulated roll of numDice dies
   public static int rollDice(int numDice){
      int sum = 0;
      for(int i = 0; i < numDice; i++){
         sum += (int)(Math.random() * 6 + 1);
      }
      return sum;
   }
   
   //--Listener Classes--//
   
   public static class mouseListen implements MouseListener{
      public void mouseEntered(MouseEvent e){}
      
      public void mouseExited(MouseEvent e){}
      
      public void mouseClicked(MouseEvent e){
			if(m != null)			//If menu open
				m.mouseClick((int)((e.getX() - 10) / (screenFactor / 10.0)), 
								 (int)((e.getY() - 30) / (screenFactor / 10.0)));
			else if(p != null)	//If game board panel open
				p.mouseClick(e.getX() - 10, e.getY() - 30);
		}
      
      public void mouseReleased(MouseEvent e){}
      
      public void mousePressed(MouseEvent e){}
   }
   
   public static class keyListen implements KeyListener{
      public void keyReleased(KeyEvent e){}
      
      public void keyTyped(KeyEvent e){}
      
      public void keyPressed(KeyEvent e){
			if(m != null){//Only change frame size if in actual game play
            if(e.getKeyCode() == KeyEvent.VK_EQUALS){          //Increase frame size factor
               if(screenFactor < 20){
                  screenFactor++;
                  f.setSize(80 * screenFactor, 50 * screenFactor);
               }
               return;
            }else if(e.getKeyCode() == KeyEvent.VK_MINUS){     //Decrease frame size factor
               if(screenFactor > 10){
                  screenFactor--;
                  f.setSize(80 * screenFactor, 50 * screenFactor);
               }
               return;
            }
         }
			if(p != null)
         	p.keyPressed(e.getKeyCode());
			
      }
   }
}