//Menu screen for game initialization

import javax.swing.JPanel;
import java.awt.Graphics;

public class Menu extends JPanel{

   //--Initialize--//

   public Menu(){
   
   }
   
   //--Graphics--//
   
   public void paintComponent(Graphics g){
      super.paintComponent(g);
      //------//
      
      //------//
      repaint(0, 0, 0, 80 * DungeonQuest.screenFactor, 50 * DungeonQuest.screenFactor);
   }
   
   //--Access--//
   
   //--Mutate--//
   
   public void mouseClick(int x, int y){
   
   }
}