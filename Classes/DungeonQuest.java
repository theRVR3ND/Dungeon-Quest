/*
   Driver program for Dragon Quest game
   By: Kelvin Peng
   under guidance of Reverend Oberle
   for the APCS Final Project 2015-2016
*/

import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DungeonQuest{
   
   //--Frame and Panels--//
   
   public static JFrame f;                //The one and only JFrame for displaying
   public static Menu m;                  //Menu screen
   public static Panel p;                 //Main game content pane
   
   //--Settings--//
   
   public static int numPlayer;           //Number of players (1 to 4)
   public static int screenFactor;        /* 
                                             Screen (and frame) resizing factor. Frame will always
                                             be in 80:50 ratio, but overall size will change. Ranges
                                             from 10 to 20.
                                          */
   
   //--Main--//

   public static void main(String[] args){
   
      //--Initialize--//
      
      screenFactor = 10;
      numPlayer = 0;
      
      f = new JFrame("Dungeon Quest || The Treasure Awaits...");
      f.setSize(80 * screenFactor, 50 * screenFactor);
      f.setLocation(20, 20);
      f.setResizable(false);//Frame size will be changed by (+) and (-) keys
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.addKeyListener(new keyListen());
      
      m = new Menu();
      p = new Panel();
      
      f.setContentPane(m);//First display menu
      f.setVisible(true);
      
      //--End Initialize--//
      
      //--Menu open--//
      
      //Keep doing nothing while numPlayer has not been selected by player(s)
      while(numPlayer == 0){}
      
      //--The GAME--//
      
      while(true){
      
      }
   }
   
   //--Static Utility Classes--//
   
   //pre: name is valid image name in "Resources" folder
   //post: Returns BufferedImage found with title name in "Resources" folder
   public static BufferedImage loadImage(String name){
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
      
      public void mouseClicked(MouseEvent e){}
      
      public void mouseReleased(MouseEvent e){}
      
      public void mousePressed(MouseEvent e){}
   }
   
   public static class keyListen implements KeyListener{
      public void keyReleased(KeyEvent e){}
      
      public void keyTyped(KeyEvent e){}
      
      public void keyPressed(KeyEvent e){
         //Screen resizing
         if(m == null){//Only resize if menu is closed (we are in actual game)
            if(e.getKeyCode() == KeyEvent.VK_EQUALS){          //Increase frame size factor
               if(screenFactor < 15){
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
         p.keyPressed(e.getKeyCode());
      }
   }
}