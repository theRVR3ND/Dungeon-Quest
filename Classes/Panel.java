//The contents of the JFrame in driver program

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Panel extends JPanel{
	
	private final BufferedImage bgImg;						//Background image
	private Hero[] players;										//All players in the game
	private SparseMatrix<Tile> grid;							//Matrix for all tile spots on board. Basically the game board.
	private SunToken sunToken;                         //"Chip" used to represent/advance time
   
   //--Initialize--//

   public Panel(String[] playerNames){
		grid = new SparseMatrix<Tile>(10, 13);
      sunToken = new SunToken();
      bgImg = DungeonQuest.loadImage("Board/Board.png");
      
      //Create heros based on names from menu
      players = new Hero[playerNames.length];
      for(int i = 0; i < playerNames.length; i++)
		   players[i] = new Hero(playerNames[i]);
			
      //Place player(s) in corners of grid, in clockwise fashion from top left corner
      int setR = 0,//Row position to set Hero at 
          setC = 0;//Column
      for(int i = 0; i < players.length; i++){
         players[i].setPosition(setR, setC);
         //Move set position clockwise in corners
         if(setR == 0){
            if(setC == 0)//If at top left
               setC = 9;
            else if(setC == 9){//If at top right
               setR = 12;
            }
         }else{//if(setR == 12)
            if(setC == 9)//If at bottom right
               setC = 0;
         }
      }
   }
   
   //--Graphics--//
   
   //pre: g != null
   //post: Updates screen once, while moving all neccessary things
   public void paintComponent(Graphics g){
      super.paintComponent(g);
      //------//
      //Draw background
      g.drawImage(bgImg, 0, 0, 80 * DungeonQuest.screenFactor, 50 * DungeonQuest.screenFactor, null);
      //Draw all tiles
      for(int i = 0; i < grid.size(); i++){
         int[] coord = grid.locationOf(grid.get(i));//In row, column form
         g.drawImage(((Tile)(grid.get(i))).getImage(), coord[1] * 20 + 50, coord[0] * 20 + 50, null);
      }
      //Draw all heros
      for(Hero h : players)
         h.draw(g);
      //------//
      repaint(0, 0, 80 * DungeonQuest.screenFactor, 50 * DungeonQuest.screenFactor);
   }
   
	//--Access--//
	
   //--Mutate--//
   
   //pre:
   //post: Goes through each player, letting them take a turn
   public void turn(){
      sunToken.move();//Advance day if needed
   }
   
   //pre: k is a valid key code
   //post: Performs an action(s) based on key press with key code k
   public void keyPressed(int k){
   
   }
	
	//pre: 
	//post: Determines which tile on board was clicked (if any) and passes on click coordinates to tile.
	public void mouseClick(int x, int y){
	
	}
   
   //--Helper--//
   
   //pre: 0 <= r < 10, 0 <= c < 13
   //post: Adds new tile at (r, c) with entrance in direction 1 (up), 2 (right), 3 (down), 4 (left)
   private void createTile(int r, int c, int dir){
      grid.add(new Tile(dir), r, c);
   }
   
   /*
      Draw a grid with current graphics color and size of 
      (w, h) at (x, y) with numR rows and numC columns
   
   private void drawGrid(Graphics g, int x, int y, int w, int h, int numR, int numC){
      for(int xD = x; xD < x + w; xD += (w / (numC + 1.0)))
         g.drawLine(xD, y, xD, y + h);
      for(int yD = y; yD < y + h; yD += (h / (numR + 1.0)))
         g.drawLine(yD, x, yD, x + w);
   }
   */
   //--Sun Token Class--//
   
   private class SunToken{
      
      private int trackPosition;                      //Spaces advanced along sun track
      private final int[] probs = {1};
      
      public SunToken(){
         trackPosition = 0;
      }
      
      //--Mutate--//
      
      //pre:
      //post: Advances sun token based on probability
      public void move(){
         if(DungeonQuest.rollDice(1) <= probs[trackPosition])
            trackPosition++;
      }
   }
}