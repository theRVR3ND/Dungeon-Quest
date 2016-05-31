/*
   Driver program for Dragon Quest game
   By: Kelvin Peng
   under the guidance of Reverend Oberle
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
   public static EndGame e;					//End game menu
	
   //--Main--//

   public static void main(String[] args){
      //--Initialize--//
      
      f = new JFrame("Dungeon Quest || Begin your Quest...");
		f.setIconImage(loadImage("Board/FrameIcon.png"));
      f.setSize(1200, 750);
      f.setLocation(20, 20);
      f.setResizable(false);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.addMouseListener(new mouseListen());
      
      m = new Menu();
      
      f.setContentPane(m);//First display menu
      f.setVisible(true);
      
      //--End Initialize--//
      
      //--Menu open--//
      
      //Keep doing nothing while settings have not been selected by player(s)
      while(p == null){
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
		
		while(p.gameGoing()){
			System.out.print("");
		}
		
		//--End Game--//
		
		//Wait two seconds before changing screen to show end game
		if(true){
			final long start = System.currentTimeMillis();
			while(start + 2000 > System.currentTimeMillis()){}
		}
		
		e = new EndGame();
		p = null;
		
		f.setTitle("Dungeon Quest || The End");
      f.remove(f.getContentPane());
      f.removeMouseListener(f.getMouseListeners()[0]);
      f.setContentPane(e);
      f.validate();
      f.repaint();
		
		while(e.stillWaiting()){
			System.out.print("");
		}
		
		System.exit(0);
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
	
   //pre: name is valid image directory in "Resources" folder
   //post: Returns BufferedImage found with directory in "Resources" folder
   public static BufferedImage loadImage(String name){
      try{
			name = getDirectory() + "Resources/" + name;//Change name to image's directory
			return ImageIO.read(new File(name));
		}catch(Exception e){
			System.out.println("Could not load " + name + ". Please make sure it exists in \"Resources\" subfolder");
			System.exit(1);
		}
		return null;
   }
   
   //--Listener Classes--//
   
   public static class mouseListen implements MouseListener{
      public void mouseEntered(MouseEvent e){}
      
      public void mouseExited(MouseEvent e){}
      
      public void mouseReleased(MouseEvent e){}
      
      public void mousePressed(MouseEvent e){}
		
		public void mouseClicked(MouseEvent e){
			if(m != null)			//If menu open
				m.mouseClick(e.getX() - 3, e.getY() - 26);
				
			else if(p != null)	//If game board panel open
				p.mouseClick(e.getX() - 3, e.getY() - 26);
		}
   }
   
   public static class keyListen implements KeyListener{
      public void keyReleased(KeyEvent e){}
      
      public void keyTyped(KeyEvent e){}
      
      public void keyPressed(KeyEvent e){}
   }
}